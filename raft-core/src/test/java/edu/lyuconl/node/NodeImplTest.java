package edu.lyuconl.node;

import edu.lyuconl.node.role.CandidateNodeRole;
import edu.lyuconl.node.role.FollowerNodeRole;
import edu.lyuconl.node.role.LeaderNodeRole;
import edu.lyuconl.rpc.MockConnector;
import edu.lyuconl.rpc.message.*;
import edu.lyuconl.schedule.NullScheduler;
import edu.lyuconl.support.DirectTaskExecutor;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * NodeImpl测试
 *
 * @date 2020年7月22日16点26分
 * @author lyuconl
 */
public class NodeImplTest {
    private NodeBuilder newNodeBuilder(NodeId selfId, NodeEndpoint... endpoints) {
        return new NodeBuilder(Arrays.asList(endpoints), selfId)
                .setScheduler(new NullScheduler())
                .setConnector(new MockConnector())
                .setTaskExecutor(new DirectTaskExecutor());
    }

    @Test
    public void startTest() {
        NodeImpl node = (NodeImpl) newNodeBuilder(NodeId.of("A"),
                new NodeEndpoint("A", "localhost", 2333)).build();
        node.start();
        FollowerNodeRole role = (FollowerNodeRole) node.getRole();
        Assert.assertEquals(0, role.getTerm());
        Assert.assertNull(role.getVoteFor());
    }

    @Test
    public void testElectionTimeoutWhenFollower() {
        NodeImpl node = (NodeImpl) newNodeBuilder(NodeId.of("A"),
                new NodeEndpoint("A", "localhost", 2333),
                new NodeEndpoint("B", "localhost", 2334),
                new NodeEndpoint("C", "localhost", 2335)).build();
        node.start();
        node.electionTimeout();
        CandidateNodeRole role = (CandidateNodeRole) node.getRole();

        // 开始选举后term为1, 自己向自己投票，votesCount为1
        Assert.assertEquals(1, role.getTerm());
        Assert.assertEquals(1, role.getVotesCount());
        MockConnector connector = (MockConnector) node.getContext().getConnector();

        // 当前节点向其它节点发送RequestVote消息
        RequestVoteRpc rpc = (RequestVoteRpc) connector.getRpc();
        Assert.assertEquals(1, rpc.getTerm());
        Assert.assertEquals(NodeId.of("A"), rpc.getCandidateId());
        Assert.assertEquals(0, rpc.getLastLogIndex());
        Assert.assertEquals(0, rpc.getLastLogTerm());
    }

    @Test
    public void testOnReceiveRequestVoteRpcFollower() {
        NodeImpl node = (NodeImpl) newNodeBuilder(NodeId.of("A"),
                new NodeEndpoint("A", "localhost", 2333),
                new NodeEndpoint("B", "localhost", 2334),
                new NodeEndpoint("C", "localhost", 2335))
                .build();
        node.start();
        RequestVoteRpc rpc = new RequestVoteRpc();
        rpc.setTerm(1);
        rpc.setCandidateId(NodeId.of("C"));
        rpc.setLastLogTerm(0);
        rpc.setLastLogIndex(0);
        node.onReceiveRequestVoteRpc(new RequestVoteRpcMessage(rpc, NodeId.of("C"), null));
        MockConnector mockConnector = (MockConnector) node.getContext().getConnector();
        RequestVoteResult result = (RequestVoteResult) mockConnector.getResult();
        Assert.assertEquals(1, result.getTerm());
        Assert.assertTrue(result.isVoteGranted());
        Assert.assertEquals(NodeId.of("C"), ((FollowerNodeRole) node.getRole()).getVoteFor());
    }

    @Test
    public void testOnReceiveRequestVoteResult() {
        NodeImpl node = (NodeImpl) newNodeBuilder(
                NodeId.of("A"),
                new NodeEndpoint("A", "localhost", 2333),
                new NodeEndpoint("B", "localhost", 2334),
                new NodeEndpoint("C", "localhost", 2335)
        ).build();
        node.start();
        // 转变role: follower——>candidate
        node.electionTimeout();
        node.onReceiveRequestVoteResult(new RequestVoteResult(1, true));
        LeaderNodeRole role = (LeaderNodeRole) node.getRole();
        Assert.assertEquals(1, role.getTerm());
    }

    @Test
    public void testReplicateLog() {
        NodeImpl node = (NodeImpl) newNodeBuilder(
                NodeId.of("A"),
                new NodeEndpoint("A", "localhost", 2333),
                new NodeEndpoint("B", "localhost", 2334),
                new NodeEndpoint("C", "localhost", 2335)
        ).build();
        node.start();
        node.electionTimeout();
        node.onReceiveRequestVoteResult(new RequestVoteResult(1, true));
        node.replicateLog();
        MockConnector mockConnector = (MockConnector) node.getContext().getConnector();
        // 检查消息数，一共3条消息
        Assert.assertEquals(3, mockConnector.getMessageCount());
        // 检查目标节点
        List<MockConnector.Message> messages = mockConnector.getMessages();
        Set<NodeId> destIds = messages.subList(1, 3).stream()
                .map(MockConnector.Message::getDestinationNodeId)
                .collect(Collectors.toSet());
        Assert.assertEquals(2, destIds.size());
        Assert.assertTrue(destIds.contains(NodeId.of("B")));
        Assert.assertTrue(destIds.contains(NodeId.of("C")));
        AppendEntriesRpc rpc = (AppendEntriesRpc) messages.get(2).getRpc();
        Assert.assertEquals(1, rpc.getTerm());
    }
}
