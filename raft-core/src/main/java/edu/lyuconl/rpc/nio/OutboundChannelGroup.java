package edu.lyuconl.rpc.nio;

import com.google.common.eventbus.EventBus;
import edu.lyuconl.rpc.Address;
import edu.lyuconl.node.NodeId;
import io.netty.channel.EventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;

/**
 * 出站channel
 *
 * @date 2020年7月31日10点35分
 * @author lyuconl
 */
class OutboundChannelGroup {

    private static final Logger logger = LoggerFactory.getLogger(OutboundChannelGroup.class);
    private final EventLoopGroup workerGroup;
    private final EventBus eventBus;
    private final NodeId selfId;
    private final ConcurrentMap<NodeId, Future<NioChannel>> channelMap = new ConcurrentHashMap<>();

    OutboundChannelGroup(EventLoopGroup workerGroup, EventBus eventBus, NodeId selfId) {
        this.workerGroup = workerGroup;
        this.eventBus = eventBus;
        this.selfId = selfId;
    }

    NioChannel getOrConnect(NodeId nodeId, Address address) {

    }
}
