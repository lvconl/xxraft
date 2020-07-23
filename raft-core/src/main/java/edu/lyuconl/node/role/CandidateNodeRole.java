package edu.lyuconl.node.role;

import edu.lyuconl.node.NodeId;
import edu.lyuconl.schedule.ElectionTimeout;

/**
 * 候选者role
 *
 * @date 2020年7月6日07点53分
 * @author 吕从雷
 */
public class CandidateNodeRole extends AbstractNodeRole {

    private final int votesCount;
    private final ElectionTimeout electionTimeout;

    /**
     * 构造函数，指定票数1
     *
     * @param term 当前周期
     * @param electionTimeout 超时定时器
     */
    public CandidateNodeRole(int term, ElectionTimeout electionTimeout) {
        this(term, 1, electionTimeout);
    }

    /**
     * 构造器，指定票数
     *
     * @param term 当前周期
     * @param votesCount 票数
     * @param electionTimeout 超时定时器
     */
    public CandidateNodeRole(int term, int votesCount, ElectionTimeout electionTimeout) {
        super(RoleName.CANDIDATE, term);
        this.votesCount = votesCount;
        this.electionTimeout = electionTimeout;
    }

    /**
     * 取消选举超时
     */
    @Override
    public void cancelTimeoutOrTask() {
        electionTimeout.cancel();
    }

    @Override
    public NodeId getLeaderId(NodeId selfId) {
        return null;
    }

    /**
     * 增加票数方法
     *
     * @param electionTimeout 选举超时器
     * @return 票数增加后的candidate
     */
    public CandidateNodeRole increaseVotesCount(ElectionTimeout electionTimeout) {
        this.electionTimeout.cancel();
        return new CandidateNodeRole(term, votesCount + 1, electionTimeout);
    }

    public int getVotesCount() {
        return votesCount;
    }

    @Override
    public String toString() {
        return "CandidateNodeRole{" +
                "term=" + term +
                ", votesCount=" + votesCount +
                ", electionTimeout=" + electionTimeout +
                '}';
    }
}
