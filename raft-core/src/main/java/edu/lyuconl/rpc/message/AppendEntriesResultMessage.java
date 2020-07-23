package edu.lyuconl.rpc.message;

import edu.lyuconl.node.NodeId;
import edu.lyuconl.rpc.Channel;

/**
 *
 * @date 2020年7月21日15点46分
 * @author lyuconl
 */
public class AppendEntriesResultMessage extends AbstractRpcMessage<AppendEntriesResult> {

    public AppendEntriesResultMessage(AppendEntriesResult rpc, NodeId sourceNodeId, Channel channel) {
        super(rpc, sourceNodeId, channel);
    }

}
