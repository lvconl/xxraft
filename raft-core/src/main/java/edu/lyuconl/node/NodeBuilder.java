package edu.lyuconl.node;

import com.google.common.eventbus.EventBus;
import edu.lyuconl.log.Log;
import edu.lyuconl.log.MemoryLog;
import edu.lyuconl.node.config.NodeConfig;
import edu.lyuconl.node.store.MemoryNodeStore;
import edu.lyuconl.node.store.NodeStore;
import edu.lyuconl.rpc.Connector;
import edu.lyuconl.schedule.DefaultScheduler;
import edu.lyuconl.schedule.Scheduler;
import edu.lyuconl.support.SingleThreadTaskExecutor;
import edu.lyuconl.support.TaskExecutor;

import java.util.Collection;
import java.util.Collections;

/**
 * 快速构建NodeContext
 *
 * @date  2020年7月22日15点59分
 * @author lyuconl
 */
public class NodeBuilder {
    private final NodeGroup group;
    private final NodeId selfId;
    private final EventBus eventBus;
    private  Scheduler scheduler = null;
    private Connector connector = null;
    private TaskExecutor taskExecutor = null;
    private boolean standby = false;
    private Log log = null;
    private NodeStore store;
    private NodeConfig config = new NodeConfig();

    public NodeBuilder(NodeEndpoint endpoint) {
        this(Collections.singletonList(endpoint), endpoint.getId());
    }

    public NodeBuilder(Collection<NodeEndpoint> endpoints, NodeId selfId) {
        this.group = new NodeGroup(endpoints, selfId);
        this.selfId = selfId;
        this.eventBus = new EventBus(selfId.getValue());
    }

    NodeBuilder setConnector(Connector connector) {
        this.connector = connector;
        return this;
    }

    NodeBuilder setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
        return this;
    }

    NodeBuilder setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
        return this;
    }

    NodeBuilder setStore(NodeStore store) {
        this.store = store;
        return this;
    }

    public Node build() {
        return new NodeImpl(buildContext());
    }

    private NodeContext buildContext() {
        NodeContext context = new NodeContext();
        context.setGroup(group);
        context.setSelfId(selfId);
        context.setStore(store != null ? store : new MemoryNodeStore());
        context.setEventBus(eventBus);
        context.setLog(log != null ? log : new MemoryLog());
        context.setScheduler(scheduler != null ? scheduler : new DefaultScheduler(config));
        context.setConnector(connector);
        context.setConfig(config);
        context.setTaskExecutor(taskExecutor != null ? taskExecutor : new SingleThreadTaskExecutor("node"));
        return context;
    }

    private NodeMode evaluateMode() {
        if (standby) {
            return NodeMode.STANDBY;
        }
        if (group.isStandalone()) {
            return NodeMode.STANDALONE;
        }
        return NodeMode.GROUP_MEMBER;
    }
}
