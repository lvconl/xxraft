package edu.lyuconl.node.role;

import edu.lyuconl.node.NodeId;
import edu.lyuconl.schedule.LogReplicationTask;

/**
 * 领导者role
 *
 * @date 2020年7月6日08点03分
 * @author 吕从雷
 */
public class LeaderNodeRole extends AbstractNodeRole{

    private final LogReplicationTask logReplicationTask;

    public LeaderNodeRole(int term, LogReplicationTask logReplicationTask) {
        super(RoleName.LEADER, term);
        this.logReplicationTask = logReplicationTask;
    }

    @Override
    public void cancelTimeoutOrTask() {
        logReplicationTask.cancel();
    }

    @Override
    public NodeId getLeaderId(NodeId selfId) {
        return selfId;
    }

    @Override
    public String toString() {
        return "LeaderNodeRole{" +
                "term=" + term +
                ", logReplicationTask=" + logReplicationTask +
                '}';
    }
}
