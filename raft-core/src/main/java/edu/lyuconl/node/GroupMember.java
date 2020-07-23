package edu.lyuconl.node;

import com.google.common.base.Preconditions;

/**
 * 集群成员实体类
 *
 * @date 2020年6月27日21点19分
 * @author 吕从雷
 */
public class GroupMember {
    private final NodeEndpoint endpoint;
    private ReplicatingState replicatingState;

    private boolean major;

    public GroupMember(NodeEndpoint endpoint, ReplicatingState replicatingState) {
        Preconditions.checkNotNull(endpoint);
        Preconditions.checkNotNull(replicatingState);
        this.endpoint = endpoint;
        this.replicatingState = replicatingState;
    }

    GroupMember(NodeEndpoint endpoint, ReplicatingState replicatingState, boolean major) {
        this.endpoint = endpoint;
        this.replicatingState = replicatingState;
        this.major = major;
    }

    public GroupMember(NodeEndpoint endpoint) {
        this(endpoint, null, true);
    }

    public int getNextIndex() {
        return ensureReplicatingState().getNextIndex();
    }

    public int getMatchIndex() {
        return ensureReplicatingState().getMatchIndex();
    }

    private ReplicatingState ensureReplicatingState() {
        if (replicatingState == null) {
            throw new IllegalStateException("replication state not set");
        }
        return replicatingState;
    }

    public boolean idEquals(NodeId id) {
        return endpoint.getId().equals(id);
    }

    public NodeEndpoint getEndpoint() {
        return endpoint;
    }

    public ReplicatingState getReplicatingState() {
        return replicatingState;
    }

    boolean isMajor() {
        return major;
    }
}
