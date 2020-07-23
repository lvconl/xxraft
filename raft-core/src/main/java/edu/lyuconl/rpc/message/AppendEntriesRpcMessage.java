package edu.lyuconl.rpc.message;

import edu.lyuconl.node.NodeId;
import edu.lyuconl.rpc.Channel;

/**
 * 追加条目rpc消息体
 *
 * @date 2020年7月21日10点19分
 * @author lyuconl
 */
public class AppendEntriesRpcMessage extends AbstractRpcMessage<AppendEntriesRpc> {

    public AppendEntriesRpcMessage(AppendEntriesRpc rpc, NodeId sourceNodeId, Channel channel) {
        super(rpc, sourceNodeId, channel);
    }
}
