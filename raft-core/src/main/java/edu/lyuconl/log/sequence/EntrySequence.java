package edu.lyuconl.log.sequence;

import edu.lyuconl.log.entry.Entry;
import edu.lyuconl.log.entry.EntryMeta;

import java.util.List;

/**
 * 日志序列
 *
 * @date 2020年7月15日09点09分
 * @author lyuconl
 */
public interface EntrySequence {

    /**
     * 判断是否为空
     *
     * @return 空返回true，否则返回false
     */
    boolean isEmpty();

    /**
     * 获取第一条日志索引
     *
     * @return 第一条日志索引
     */
    int getFirstLogIndex();

    /**
     * 获取最后一条日志索引
     *
     * @return 最后一条日志索引
     */
    int getLastLogIndex();

    /**
     * 获取下一条日志索引
     *
     * @return 下一条日志索引
     */
    int getNextLogIndex();

    /**
     * 获取序列的子视图
     *
     * @param fromIndex 起始索引
     * @return 序列子视图
     */
    List<Entry> subList(int fromIndex);

    /**
     * 获取指定范围序列的子视图
     *
     * @param fromIndex 起始索引
     * @param toIndex 终止索引
     * @return 序列范围的子视图
     */
    List<Entry> subList(int fromIndex, int toIndex);

    /**
     * 检查某个日志条目是否存在
     *
     * @param index 指定的日志索引
     * @return 存在返回true，否则返回false
     */
    boolean isEntryPresent(int index);

    /**
     * 获取某个日志条目的元信息
     *
     * @param index 指定的日志索引
     * @return 指定日志条目的元信息
     */
    EntryMeta getEntryMeta(int index);

    /**
     * 根据index获取指定的日志条目
     *
     * @param index 指定的日志索引
     * @return 指定的日志条目
     */
    Entry getEntry(int index);

    /**
     * 获取最后一个日志条目
     *
     * @return 最后一个日志条目
     */
    Entry getLastEntry();

    /**
     * 追加日志条目
     *
     * @param entry 要追加的日志条目
     */
    void append(Entry entry);

    /**
     * 追加多条日志条目
     *
     * @param entries 要追加的多条日志条目
     */
    void append(List<Entry> entries);

    /**
     * 推进commitIndex
     *
     * @param index 指定的index
     */
    void commit(int index);

    /**
     * 获取当前的commitIndex
     *
     * @return 当前的commitIndex
     */
    int getCommitIndex();

    /**
     * 移除个索引之后的日志条目
     *
     * @param index 指定的index
     */
    void removeAfter(int index);

    /**
     * 关闭日志序列
     */
    void close();
}