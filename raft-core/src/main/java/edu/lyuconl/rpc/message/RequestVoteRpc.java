package edu.lyuconl.rpc.message;

import edu.lyuconl.node.NodeId;

/**
 * 投票请求实体
 *
 * @date 2020年7月6日10点34分
 * @author 吕从雷
 */
public class RequestVoteRpc {
    /**
     * 选举term
     */
    private int term;
    /**
     * 候选者id，一般为发送者自己
     */
    private NodeId candidateId;
    /**
     * 候选者最后一条日志的index
     */
    private int lastLogIndex = 0;
    /**
     * 候选者最后一条日志的term
     */
    private int lastLogTerm = 0;

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public NodeId getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(NodeId candidateId) {
        this.candidateId = candidateId;
    }

    public int getLastLogIndex() {
        return lastLogIndex;
    }

    public void setLastLogIndex(int lastLogIndex) {
        this.lastLogIndex = lastLogIndex;
    }

    public int getLastLogTerm() {
        return lastLogTerm;
    }

    public void setLastLogTerm(int lastLogTerm) {
        this.lastLogTerm = lastLogTerm;
    }

    @Override
    public String toString() {
        return "RequestVoteRpc{" +
                "term=" + term +
                ", candidateId=" + candidateId +
                ", lastLogIndex=" + lastLogIndex +
                ", lastLogTerm=" + lastLogTerm +
                '}';
    }
}
