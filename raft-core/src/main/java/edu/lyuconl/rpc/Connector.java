package edu.lyuconl.rpc;

import edu.lyuconl.node.NodeEndpoint;
import edu.lyuconl.rpc.message.*;

import java.util.Collection;

/**
 * rpc连接器接口
 *
 * @date 2020年7月6日11点08分
 * @author 吕从雷
 */
public interface Connector {
    /**
     * 初始化方法
     */
    void initialize();

    /**
     * 发送RequestVote给多个节点
     *
     * @param rpc 投票请求
     * @param destinationEndpoints 目标主机集合
     */
    void sendRequestVote(RequestVoteRpc rpc,
                         Collection<NodeEndpoint> destinationEndpoints);

    /**
     * 回复RequestVote给单个节点
     *
     * @param result 投票结果
     * @param rpcMessage 请求消息
     */
    void replyRequestVote(RequestVoteResult result,
                          RequestVoteRpcMessage rpcMessage);

    /**
     * 发送AppendEntries给单个节点
     *
     * @param rpc AppendEntries rpc
     * @param destinationEndpoint 目标主机
     */
    void sendAppendEntries(AppendEntriesRpc rpc,
                           NodeEndpoint destinationEndpoint);

    /**
     * AppendEntries回复
     *
     * @param result 回复结果
     * @param rpcMessage 请求消息
     */
    void replyAppendEntries(AppendEntriesResult result,
                            AppendEntriesRpcMessage rpcMessage);

    /**
     * 重置连接
     */
    void resetChannels();

    /**
     * 关闭方法
     */
    void close();
}
