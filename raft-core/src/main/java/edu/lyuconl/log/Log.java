package edu.lyuconl.log;

import edu.lyuconl.log.entry.Entry;
import edu.lyuconl.log.entry.EntryMeta;
import edu.lyuconl.log.entry.GeneralEntry;
import edu.lyuconl.log.entry.NoOpEntry;
import edu.lyuconl.node.NodeId;
import edu.lyuconl.rpc.message.AppendEntriesRpc;

import java.util.List;

/**
 * 日志类
 *
 * @date 2020年7月13日16点12分
 * @author lyuconl
 */
public interface Log {

    int ALL_ENTRIES = -1;

    /**
     * 获取最后一条日志的元数据信息
     *
     * @return 最后一条日志元数据信息
     */
    EntryMeta getLastEntryMeta();

    /**
     * 创建AppendEntries消息
     *
     * @param term 当前term
     * @param selfId 当前机器ID
     * @param nextIndex 下一条日志索引
     * @param maxEntries 最后一条日志
     * @return rpc
     */
    AppendEntriesRpc createAppendEntriesRpc(int term, NodeId selfId, int nextIndex, int maxEntries);

    /**
     * 获取下一条日志的索引
     *
     * @return 下一条日志的索引
     */
    int getNextIndex();

    /**
     * 获取当前的commitIndex
     *
     * @return 当前的commitIndex
     */
    int getCommitIndex();

    /**
     * 判断对方的lastIndex，lastTerm
     *
     * @param lastLogIndex lastLogIndex
     * @param lastLogTerm lastLogTerm
     * @return 是否大于当前index&term
     */
    boolean isNewerThan(int lastLogIndex, int lastLogTerm);

    /**
     * 增加一个NoOp日志
     *
     * @param term current term
     * @return 增加的日志
     */
    NoOpEntry appendEntry(int term);

    /**
     * Append a general entry
     *
     * @param term current term
     * @param command command in bytes
     * @return a general command
     */
    GeneralEntry appendEntry(int term, byte[] command);

    /**
     * 追加来自Leader的日志
     *
     * @param preLogIndex 前一个日志索引
     * @param preLogTerm 前一个日志term
     * @param entries 要追加的entry
     * @return 追加状态
     */
    boolean appendEntriesFromLeader(int preLogIndex, int preLogTerm, List<Entry> entries);

    /**
     * 推进commitIndex
     *
     * @param newCommitIndex new commit index
     * @param currentTerm current term
     */
    void advanceCommitIndex(int newCommitIndex, int currentTerm);

    /**
     * 关闭
     */
    void close();
}
