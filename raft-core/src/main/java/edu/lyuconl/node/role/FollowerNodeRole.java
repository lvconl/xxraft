package edu.lyuconl.node.role;

import edu.lyuconl.node.NodeId;
import edu.lyuconl.schedule.ElectionTimeout;

/**
 * 跟随者角色
 *
 * @date 2020年7月5日17点24分
 * @author 吕从雷
 */
public class FollowerNodeRole extends AbstractNodeRole {
    private final NodeId voteFor;
    private final NodeId leaderId;
    private final ElectionTimeout electionTimeout;

    public FollowerNodeRole(int term, NodeId voteFor, NodeId leaderId,
                            ElectionTimeout electionTimeout) {
        super(RoleName.FOLLOWER, term);
        this.voteFor = voteFor;
        this.leaderId = leaderId;
        this.electionTimeout = electionTimeout;
    }

    public NodeId getVoteFor() {
        return voteFor;
    }

    public NodeId getLeaderId() {
        return leaderId;
    }

    @Override
    public void cancelTimeoutOrTask() {
        electionTimeout.cancel();
    }

    @Override
    public String toString() {
        return "FollowerNodeRole{" +
                "term" + term +
                ", voteFor=" + voteFor +
                ", leaderId=" + leaderId +
                ", electionTimeout=" + electionTimeout +
                '}';
    }
}
