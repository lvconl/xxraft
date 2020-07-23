package edu.lyuconl.rpc;

import edu.lyuconl.node.NodeEndpoint;
import edu.lyuconl.node.NodeId;
import edu.lyuconl.rpc.message.AppendEntriesResult;
import edu.lyuconl.rpc.message.AppendEntriesRpc;
import edu.lyuconl.rpc.message.RequestVoteResult;
import edu.lyuconl.rpc.message.RequestVoteRpc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * 测试用mock框架
 *
 * @date 2020年7月22日10点37分
 * @author lyuconl
 */
public class MockConnector implements Connector {

    private LinkedList<Message> messages = new LinkedList<>();

    @Override
    public void initialize() {

    }

    @Override
    public void sendRequestVote(RequestVoteRpc rpc, Collection<NodeEndpoint> destinationEndpoints) {
        Message msg = new Message();
        msg.rpc = rpc;
        messages.add(msg);
    }

    @Override
    public void replyRequestVote(RequestVoteResult result, NodeEndpoint destinationEndpoint) {
        Message msg = new Message();
        msg.result = result;
        msg.destinationNodeId = destinationEndpoint.getId();
        messages.add(msg);
    }

    @Override
    public void sendAppendEntries(AppendEntriesRpc rpc, NodeEndpoint destinationEndpoint) {
        Message msg = new Message();
        msg.rpc = rpc;
        msg.destinationNodeId = destinationEndpoint.getId();
        messages.add(msg);
    }

    @Override
    public void replyAppendEntries(AppendEntriesResult result, NodeEndpoint destinationEndpoint) {
        Message msg = new Message();
        msg.result = result;
        msg.destinationNodeId = destinationEndpoint.getId();
        messages.add(msg);
    }

    /**
     * 获取最后一条消息
     *
     * @return 最后一条消息
     */
    public Message getLastMessage() {
        return messages.isEmpty() ? null : messages.getLast();
    }

    /**
     * 获取最后一条消息或者空消息
     *
     * @return 最后一条消息或者空消息
     */
    private Message getLastMessageOrDefault() {
        return messages.isEmpty() ? new Message() : messages.getLast();
    }

    /**
     * 获取最后一条rpc消息
     *
     * @return 最后一条rpc消息
     */
    public Object getRpc() {
        return getLastMessageOrDefault().rpc;
    }

    /**
     * 获取最后一条result消息
     *
     * @return 最后一条result消息
     */
    public Object getResult() {
        return getLastMessageOrDefault().result;
    }

    /**
     * 获取最后一条消息的目标节点
     *
     * @return 最后一条消息的目标节点
     */
    public NodeId getDestinationNodeId() {
        return getLastMessageOrDefault().destinationNodeId;
    }

    /**
     * 获取消息数量
     *
     * @return 消息数量
     */
    public int getMessageCount() {
        return messages.size();
    }

    /**
     * 获取所有消息
     *
     * @return 所有消息
     */
    public List<Message> getMessages() {
        return new ArrayList<>(messages);
    }

    public void clearMessage() {
        messages.clear();
    }

    @Override
    public void close() {

    }

    public static class Message {
        private Object rpc;
        private NodeId destinationNodeId;
        private Object result;

        public Object getRpc() {
            return rpc;
        }

        public NodeId getDestinationNodeId() {
            return destinationNodeId;
        }

        public Object getResult() {
            return result;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "rpc=" + rpc +
                    ", destinationNodeId=" + destinationNodeId +
                    ", result=" + result +
                    '}';
        }
    }
}
