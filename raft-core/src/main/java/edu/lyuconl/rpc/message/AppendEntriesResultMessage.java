package edu.lyuconl.rpc.message;

import com.google.common.base.Preconditions;
import edu.lyuconl.node.NodeId;
import edu.lyuconl.rpc.Channel;

import javax.annotation.Nonnull;

/**
 *
 * @date 2020年7月21日15点46分
 * @author lyuconl
 */
public class AppendEntriesResultMessage {

    private final AppendEntriesResult result;
    private final NodeId sourceNodeId;
    private final AppendEntriesRpc rpc;

    public AppendEntriesResultMessage(AppendEntriesResult result, NodeId sourceNodeId, @Nonnull AppendEntriesRpc rpc) {
        Preconditions.checkNotNull(rpc);
        this.result = result;
        this.sourceNodeId = sourceNodeId;
        this.rpc = rpc;
    }

    public AppendEntriesResult get() {
        return result;
    }

    public NodeId getSourceNodeId() {
        return sourceNodeId;
    }

    public AppendEntriesRpc getRpc() {
        return rpc;
    }
}
