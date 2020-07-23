package edu.lyuconl.node;

import com.google.common.eventbus.Subscribe;
import edu.lyuconl.node.role.*;
import edu.lyuconl.node.store.NodeStore;
import edu.lyuconl.rpc.message.*;
import edu.lyuconl.schedule.ElectionTimeout;
import edu.lyuconl.schedule.LogReplicationTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Objects;

/**
 * 节点具体实现（核心组件）
 *
 * @date 2020年7月19日17点57分
 * @author lyuconl
 */
@ThreadSafe
public class NodeImpl implements Node {

    private static final Logger logger = LoggerFactory.getLogger(NodeImpl.class);
    private final NodeContext context;
    private boolean started;
    private AbstractNodeRole role;

    NodeImpl(NodeContext context) {
        this.context = context;
    }

    /**
     * 测试用，暴露context
     *
     * @return Node核心context
     */
    NodeContext getContext() {
        return this.context;
    }

    /**
     * 测试用，暴露Role
     *
     * @return Node核心Role
     */
    AbstractNodeRole getRole() {
        return this.role;
    }

    @Override
    public synchronized void start() {
        // 如果启动，则直接跳过
        if (started) {
            return;
        }

        // 注册自己到EventBus
        context.getEventBus().register(this);
        // 初始化连接器
        context.getConnector().initialize();
        // 启动时为Follower角色
        NodeStore store = context.getStore();
        changeToRole(new FollowerNodeRole(store.getTerm(), store.getVotedFor(), null, scheduleElectionTimeout()));
        started = true;
    }

    @Override
    public synchronized void stop() throws InterruptedException {
        // 不允许没有启动就关闭
        if (!started) {
            throw new IllegalStateException("node not started");
        }
        // 关闭定时器
        context.getScheduler().stop();
        // 关闭连接器
        context.getConnector().close();
        // 关闭任务执行器
        context.taskExecutor().shutdown();
        started = false;

    }

    private void changeToRole(AbstractNodeRole newRole) {
        logger.debug("node {}, role state changed -> {}", context.getSelfId(), newRole);
        NodeStore store = context.getStore();
        store.setTerm(newRole.getTerm());
        if (newRole.getName() == RoleName.FOLLOWER) {
            store.setVotedFor(((FollowerNodeRole)newRole).getVoteFor());
        }
        role = newRole;
    }

    private ElectionTimeout scheduleElectionTimeout() {
        return context.getScheduler().scheduledElectionTimeout(this::electionTimeout);
    }

    void electionTimeout() {
        context.taskExecutor().submit(this::doProcessElectionTimeout);
    }

    private void doProcessElectionTimeout() {
        // Leader角色不能参与选举
        if (role.getName() == RoleName.LEADER) {
            logger.warn("node {}, current role is leader, ignore election timeout", context.getSelfId());
            return;
        }

        // 对于follower
        // 对于candidate节点来说是再次发起选举
        // 选举term加1
        int newTerm = role.getTerm() + 1;
        role.cancelTimeoutOrTask();
        logger.info("start election");
        // 改变当前角色role为candidate
        changeToRole(new CandidateNodeRole(newTerm, scheduleElectionTimeout()));

        // 发送RequestVote消息
        RequestVoteRpc rpc = new RequestVoteRpc();
        rpc.setTerm(newTerm);
        rpc.setCandidateId(context.getSelfId());
        rpc.setLastLogIndex(0);
        rpc.setLastLogTerm(0);
        context.getConnector().sendRequestVote(rpc, context.getGroup().listEndpointExceptSelf());
    }

    @Subscribe
    public void onReceiveRequestVoteRpc(RequestVoteRpcMessage rpcMessage) {
        context.taskExecutor().submit(() -> context.getConnector().replyRequestVote(
                doProcessRequestVoteRpc(rpcMessage),
                context.getGroup().findMember(rpcMessage.getSourceNodeId()).getEndpoint()
        ));
    }

    private RequestVoteResult doProcessRequestVoteRpc(RequestVoteRpcMessage rpcMessage) {
        // 如果对方的term比自己的小，不投票并且返回自己的term给对象
        RequestVoteRpc rpc = rpcMessage.getRpc();
        if (rpc.getTerm() < role.getTerm()) {
            logger.debug("term from rpc < current term, don't vote ({} < {})", rpc.getTerm(), role.getTerm());
            return new RequestVoteResult(role.getTerm(), false);
        }

        boolean voteForCandidate = true;

        // 如果对象的term比自己的大，则切换为Follower角色
        if (rpc.getTerm() > role.getTerm()) {
            becomeFollower(rpc.getTerm(), (voteForCandidate ? rpc.getCandidateId() : null),
                    null, true);
        }

        // 本地的term与消息的term一致
        switch (role.getName()) {
            case FOLLOWER:
                FollowerNodeRole follower = (FollowerNodeRole) role;
                NodeId votedFor = follower.getVoteFor();
                // 以下两种情况投票
                // case 1. 自己尚未投过票，并且对方日子比自己新
                // case 2. 自己已经给对方投过票
                if ((votedFor == null && voteForCandidate)
                        || Objects.equals(votedFor, rpc.getCandidateId())) {
                    becomeFollower(role.getTerm(), rpc.getCandidateId(), null, true);
                    return new RequestVoteResult(rpc.getTerm(), true);
                }
                return new RequestVoteResult(role.getTerm(), true);
            case CANDIDATE:
            case LEADER:
                return new RequestVoteResult(role.getTerm(), false);
            default:
                throw new IllegalStateException("unexpected node role [" + role.getName() + "]");
        }
    }

    private void becomeFollower(int term, NodeId votedFor, NodeId leaderId,
                                boolean scheduleElectionTimeout) {
        role.cancelTimeoutOrTask();
        if (leaderId != null && !leaderId.equals(role.getLeaderId(context.getSelfId()))) {
            logger.info("current leader is {}, term {}", leaderId, term);
        }

        // 重新创建选举超时定时器或者空定时器
        ElectionTimeout electionTimeout = scheduleElectionTimeout ? scheduleElectionTimeout() : ElectionTimeout.NONE;
        changeToRole(new FollowerNodeRole(term, votedFor, leaderId, electionTimeout));
    }

    public void onReceiveRequestVoteResult(RequestVoteResult result) {
        context.taskExecutor().submit(() -> doProcessRequestVoteResult(result));
    }

    private void doProcessRequestVoteResult(RequestVoteResult result) {
        // 如果对方的term比自己的大，则退化为Follower角色
        if (role.getTerm() < result.getTerm()) {
            becomeFollower(result.getTerm(), null, null, true);
            return;
        }
        // 如果自己不是candidate角色（已选举成功），则忽略
        if (role.getName() != RoleName.CANDIDATE) {
            logger.debug("receive request vote result and current role is not candidate, ignore");
            return;
        }
        // 如果对方比自己的term小或者没有给自己投票，则忽略
        if (result.getTerm() < role.getTerm() || !result.isVoteGranted()) {
            return;
        }
        // 当前票数
        int currentVotesCount = ((CandidateNodeRole) role).getVotesCount() + 1;
        // 当前节点数
        int countOfMajor = context.getGroup().getCount();
        logger.debug("votes count {}, node count {}", currentVotesCount, countOfMajor);
        role.cancelTimeoutOrTask();
        if (currentVotesCount > countOfMajor / 2) {
            // 称为leader角色
            logger.info("become leader, term {}", role.getTerm());
            changeToRole(new LeaderNodeRole(role.getTerm(), scheduleLogReplicationTask()));
        } else {
            changeToRole(new CandidateNodeRole(role.getTerm(), currentVotesCount, scheduleElectionTimeout()));
        }
    }

    private LogReplicationTask scheduleLogReplicationTask() {
        return context.getScheduler().scheduledLogReplicationTask(this::replicateLog);
    }

    void replicateLog() {
        // lambda -> 方法引用
        context.taskExecutor().submit((Runnable) this::doReplicateLog);
    }

    private void doReplicateLog() {
        logger.debug("replicate log");
        // 向日志复制对象发送AppendEntries消息
        for (GroupMember member : context.getGroup().listReplicationTarget()) {
            doReplicateLog(member);
        }
    }

    private void doReplicateLog(GroupMember member) {
        AppendEntriesRpc rpc = new AppendEntriesRpc();
        rpc.setTerm(role.getTerm());
        rpc.setLeaderId(context.getSelfId());
        rpc.setPrevLogIndex(0);
        rpc.setPrevLogTerm(0);
        rpc.setLeaderCommit(0);
        context.getConnector().sendAppendEntries(rpc, member.getEndpoint());
    }

    public void onReceiveAppendEntriesRpc(AppendEntriesRpcMessage rpcMessage) {
        context.taskExecutor().submit(() -> context.getConnector().replyAppendEntries(doProcessAppendEntriesRpc(rpcMessage),
                context.getGroup().findMember(rpcMessage.getSourceNodeId()).getEndpoint()));
    }

    private AppendEntriesResult doProcessAppendEntriesRpc(AppendEntriesRpcMessage rpcMessage) {
        AppendEntriesRpc rpc = rpcMessage.getRpc();
        // 如果对方的term比自己的小，则回复自己的term
        if (rpc.getTerm() < role.getTerm()) {
            return new AppendEntriesResult(role.getTerm(), false);
        }
        // 如果对方的term比自己的大，则退化成follower角色
        if (rpc.getTerm() > role.getTerm()) {
            becomeFollower(rpc.getTerm(), null, rpc.getLeaderId(), true);
            // 并追加日志
            return new AppendEntriesResult(rpc.getTerm(), appendEntries(rpc));
        }
        assert rpc.getTerm() == role.getTerm();
        switch (role.getName()) {
            case FOLLOWER:
                // 设置leaderId并重置选举定时器
                becomeFollower(rpc.getTerm(), ((FollowerNodeRole) role).getVoteFor(), rpc.getLeaderId(), true);
                // 追加日志
                return new AppendEntriesResult(rpc.getTerm(), appendEntries(rpc));
            case CANDIDATE:
                // 如果有两个Candidate角色，并且另外一个Candidate先成了Leader
                // 则当前节点退化为Follower角色并重置选举定时器
                becomeFollower(rpc.getTerm(), null, rpc.getLeaderId(), true);
                return new AppendEntriesResult(rpc.getTerm(), appendEntries(rpc));
            case LEADER:
                // 打印警告信息
                logger.warn("receive append entries rpc from another leader {}, ignore", rpc.getLeaderId());
                return new AppendEntriesResult(rpc.getTerm(), false);
            default:
                throw new IllegalStateException("unexpected node role [" + role.getName() + "]");
        }
    }

    private boolean appendEntries(AppendEntriesRpc rpc) {
        return true;
    }

    private void doProcessAppendEntriesResult(AppendEntriesResultMessage resultMessage) {
        AppendEntriesResult result = resultMessage.getRpc();
        // 如果对方的term比自己的大，则退化为Follower角色
        if (result.getTerm() > role.getTerm()) {
            becomeFollower(result.getTerm(), null, null, true);
            return;
        }
        // 检查自己的角色
        if (role.getName() != RoleName.LEADER) {
            logger.warn("receive append entries result from node {} but current node is not leader, ignore",
                    resultMessage.getSourceNodeId());
        }
    }

}
