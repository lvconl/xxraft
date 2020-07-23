package edu.lyuconl.node;

import com.google.common.eventbus.EventBus;
import edu.lyuconl.log.Log;
import edu.lyuconl.node.config.NodeConfig;
import edu.lyuconl.node.store.NodeStore;
import edu.lyuconl.rpc.Connector;
import edu.lyuconl.schedule.Scheduler;
import edu.lyuconl.support.TaskExecutor;

/**
 * 节点上下文
 *
 * @date 2020年7月19日17点42分
 * @author lyuconl
 */
public class NodeContext {

    private NodeId selfId;
    private NodeGroup group;
    private Log log;
    private Connector connector;
    private NodeStore store;
    private Scheduler scheduler;
    private NodeMode mode;
    private NodeConfig config;
    private EventBus eventBus;
    private TaskExecutor taskExecutor;
    private TaskExecutor groupConfigChangeTaskExecutor;

    public NodeId getSelfId() {
        return selfId;
    }

    public void setSelfId(NodeId selfId) {
        this.selfId = selfId;
    }

    public NodeGroup getGroup() {
        return group;
    }

    public void setGroup(NodeGroup group) {
        this.group = group;
    }

    public Log getLog() {
        return log;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    public Connector getConnector() {
        return connector;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    public NodeStore getStore() {
        return store;
    }

    public void setStore(NodeStore store) {
        this.store = store;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public NodeMode getMode() {
        return mode;
    }

    public void setMode(NodeMode mode) {
        this.mode = mode;
    }

    public NodeConfig getConfig() {
        return config;
    }

    public void setConfig(NodeConfig config) {
        this.config = config;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public TaskExecutor taskExecutor() {
        return taskExecutor;
    }

    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public TaskExecutor getGroupConfigChangeTaskExecutor() {
        return groupConfigChangeTaskExecutor;
    }

    public void setGroupConfigChangeTaskExecutor(TaskExecutor groupConfigChangeTaskExecutor) {
        this.groupConfigChangeTaskExecutor = groupConfigChangeTaskExecutor;
    }
}
