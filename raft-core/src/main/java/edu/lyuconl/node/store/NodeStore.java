package edu.lyuconl.node.store;

import edu.lyuconl.node.NodeId;

/**
 * 节点持久化数据接口
 *
 * @date 2020年7月11日11点33分
 * @author 吕从雷
 */
public interface NodeStore {
    /**
     * 获得currentTerm
     *
     * @return 当前term
     */
    int getTerm();

    /**
     * 设置term
     *
     * @param term 要设置的currentTerm
     */
    void setTerm(int term);

    /**
     * 获取voteFor
     *
     * @return vote for
     */
    NodeId getVotedFor();

    /**
     * 设置voted for
     *
     * @param votedFor vote for
     */
    void setVotedFor(NodeId votedFor);

    /**
     * 关闭文件
     */
    void close();
}
