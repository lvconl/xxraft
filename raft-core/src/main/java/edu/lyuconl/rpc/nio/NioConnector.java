package edu.lyuconl.rpc.nio;

import com.google.common.eventbus.EventBus;
import edu.lyuconl.node.NodeEndpoint;
import edu.lyuconl.node.NodeId;
import edu.lyuconl.rpc.Connector;
import edu.lyuconl.rpc.message.AppendEntriesResult;
import edu.lyuconl.rpc.message.AppendEntriesRpc;
import edu.lyuconl.rpc.message.RequestVoteResult;
import edu.lyuconl.rpc.message.RequestVoteRpc;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Collection;

/**
 * 异步连接器实现
 *
 * @author lyuconl
 * @date 2020年7月30日17点26分
 */
@ThreadSafe
public class NioConnector implements Connector {

    private static final Logger logger = LoggerFactory.getLogger(NioConnector.class);
    /**
     * Selector线程池，此处为单线程
     */
    private final NioEventLoopGroup bossNioEventLoopGroup = new NioEventLoopGroup(1);

    /**
     * IO线程池，固定数量多线程
     */
    private final NioEventLoopGroup workerNioEventLoopGroup;

    /**
     * 是否和上层服务共享IO线程池
     */
    private final boolean workerGroupShared;
    private final EventBus eventBus;

    /**
     * 节点间通信端口
     */
    private final int port;

    private final InboundChannelGroup inboundChannelGroup = new InboundChannelGroup();
    private final OutboundChannelGroup outboundChannelGroup;

    public NioConnector(NioEventLoopGroup workerNioEventLoopGroup, boolean workerGroupShared, NodeId selfNodeId, EventBus eventBus, int port) {
        this.workerNioEventLoopGroup = workerNioEventLoopGroup;
        this.workerGroupShared = workerGroupShared;
        this.eventBus = eventBus;
        this.port = port;
        outboundChannelGroup = new OutboundChannelGroup(workerNioEventLoopGroup, eventBus, selfNodeId);
    }

    public NioConnector(NioEventLoopGroup workerNioEventLoopGroup, NodeId selfNodeId, EventBus eventBus, int port) {
        this(workerNioEventLoopGroup, true, selfNodeId, eventBus, port);
    }

    public NioConnector(NodeId selfNodeId, EventBus eventBus, int port) {
        this(new NioEventLoopGroup(), false, selfNodeId, eventBus, port);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void sendRequestVote(RequestVoteRpc rpc, Collection<NodeEndpoint> destinationEndpoints) {

    }

    @Override
    public void replyRequestVote(RequestVoteResult result, NodeEndpoint destinationEndpoint) {

    }

    @Override
    public void sendAppendEntries(AppendEntriesRpc rpc, NodeEndpoint destinationEndpoint) {

    }

    @Override
    public void replyAppendEntries(AppendEntriesResult result, NodeEndpoint destinationEndpoint) {

    }

    @Override
    public void resetChannels() {

    }

    @Override
    public void close() {

    }
}
