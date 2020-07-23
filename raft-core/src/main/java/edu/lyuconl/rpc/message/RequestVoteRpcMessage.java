package edu.lyuconl.rpc.message;

import edu.lyuconl.node.NodeId;
import edu.lyuconl.rpc.Channel;

/**
 * 投票请求消息体
 *
 * @date 2020年7月20日14点25分
 * @author lyuconl
 */
public class RequestVoteRpcMessage extends AbstractRpcMessage<RequestVoteRpc> {
    public RequestVoteRpcMessage(RequestVoteRpc rpc, NodeId sourceNodeId, Channel channel) {
        super(rpc, sourceNodeId, channel);
    }
}
