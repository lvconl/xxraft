package edu.lyuconl.rpc.message;

import edu.lyuconl.node.NodeId;

import java.util.Collections;
import java.util.List;

/**
 * 追加条目
 *
 * @date 2020年7月6日10点51分
 * @author 吕从雷
 */
public class AppendEntries {
    private int term;
    private NodeId leaderId;
    private int prevLogIndex = 0;
    private int prevLogTerm;
    private List<Object> entires = Collections.emptyList();
    private int leaderCommit;

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public NodeId getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(NodeId leaderId) {
        this.leaderId = leaderId;
    }

    public int getPrevLogIndex() {
        return prevLogIndex;
    }

    public void setPrevLogIndex(int prevLogIndex) {
        this.prevLogIndex = prevLogIndex;
    }

    public int getPrevLogTerm() {
        return prevLogTerm;
    }

    public void setPrevLogTerm(int prevLogTerm) {
        this.prevLogTerm = prevLogTerm;
    }

    public List<Object> getEntires() {
        return entires;
    }

    public void setEntires(List<Object> entires) {
        this.entires = entires;
    }

    public int getLeaderCommit() {
        return leaderCommit;
    }

    public void setLeaderCommit(int leaderCommit) {
        this.leaderCommit = leaderCommit;
    }

    @Override
    public String toString() {
        return "AppendEntries{" +
                "term=" + term +
                ", leaderId=" + leaderId +
                ", prevLogIndex=" + prevLogIndex +
                ", prevLogTerm=" + prevLogTerm +
                ", entires=" + entires +
                ", leaderCommit=" + leaderCommit +
                '}';
    }
}
