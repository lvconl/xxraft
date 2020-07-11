package edu.lyuconl.node.store;

import edu.lyuconl.node.NodeId;

/**
 * 角色状态持久化，内存版本
 *
 * @date 2020年7月11日11点49分
 * @author 吕从雷
 */
public class MemoryNodeStore implements NodeStore {
    private int term;
    private NodeId votedFor;

    public MemoryNodeStore(int term, NodeId votedFor) {
        this.term = term;
        this.votedFor = votedFor;
    }

    public MemoryNodeStore() {
        this(0, null);
    }

    @Override
    public int getTerm() {
        return term;
    }

    @Override
    public void setTerm(int term) {
        this.term = term;
    }

    @Override
    public NodeId getVotedFor() {
        return votedFor;
    }

    @Override
    public void setVotedFor(NodeId votedFor) {
        this.votedFor = votedFor;
    }

    @Override
    public void close() {

    }
}
