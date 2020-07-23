package edu.lyuconl.rpc.message;

import edu.lyuconl.node.NodeId;
import edu.lyuconl.rpc.Channel;

/**
 * 抽象rpc消息
 *
 * @date 2020年7月20日14点20分
 * @author lyuconl
 */
public abstract class AbstractRpcMessage<T> {

    private T rpc;
    private NodeId sourceNodeId;
    private Channel channel;

    AbstractRpcMessage(T rpc, NodeId sourceNodeId, Channel channel) {
        this.rpc = rpc;
        this.sourceNodeId = sourceNodeId;
        this.channel = channel;
    }

    public T getRpc() {
        return rpc;
    }

    public NodeId getSourceNodeId() {
        return sourceNodeId;
    }

    public Channel getChannel() {
        return channel;
    }
}
