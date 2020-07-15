package edu.lyuconl.log.sequence;

import edu.lyuconl.log.entry.Entry;
import edu.lyuconl.log.entry.EntryMeta;

import java.util.Collections;
import java.util.List;

/**
 * 抽象实现AbstractEntrySequence
 *
 * @date 2020年7月15日10点21分
 * @author lyuconl
 */
public abstract class AbstractEntrySequence implements EntrySequence {
    int logIndexOffset;
    int nextLogIndex;

    AbstractEntrySequence(int logIndexOffset) {
        this.logIndexOffset = logIndexOffset;
        this.nextLogIndex = logIndexOffset;
    }

    @Override
    public boolean isEmpty() {
        return logIndexOffset == nextLogIndex;
    }

    /**
     * 获取日志索引偏移
     *
     * @return 日志索引偏移
     */
    public int doGetFirstLogIndex() {
        return logIndexOffset;
    }

    @Override
    public int getFirstLogIndex() {
        if (isEmpty()) {
            throw new EmptySequenceException();
        }
        return doGetFirstLogIndex();
    }

    @Override
    public int getLastLogIndex() {
        if (isEmpty()) {
            throw new EmptySequenceException();
        }
        return doGetLastLogIndex();
    }

    public int doGetLastLogIndex() {
        return nextLogIndex - 1;
    }

    @Override
    public int getNextLogIndex() {
        return nextLogIndex;
    }

    @Override
    public boolean isEntryPresent(int index) {
        return !isEmpty() && index >= doGetFirstLogIndex() && index <= doGetLastLogIndex();
    }

    @Override
    public Entry getEntry(int index) {
        if (!isEntryPresent(index)) {
            return null;
        }
        return doGetEntry(index);
    }

    @Override
    public EntryMeta getEntryMeta(int index) {
        Entry entry = getEntry(index);
        return entry != null ? entry.getMate() : null;
    }

    @Override
    public Entry getLastEntry() {
        return isEmpty() ? null : doGetEntry(doGetLastLogIndex());
    }

    @Override
    public List<Entry> subList(int fromIndex) {
        if (isEmpty() || fromIndex > doGetLastLogIndex()) {
            return Collections.emptyList();
        }
        return subList(Math.max(fromIndex, doGetFirstLogIndex()), nextLogIndex);
    }

    @Override
    public List<Entry> subList(int fromIndex, int toIndex) {
        if (isEmpty()) {
            throw new EmptySequenceException();
        }
        if (fromIndex < doGetFirstLogIndex() || toIndex > doGetLastLogIndex() + 1 || fromIndex > toIndex) {
            throw new IllegalArgumentException("Illegal from index " + fromIndex + " or to index" + toIndex);
        }
        return doSubList(fromIndex, toIndex);
    }

    @Override
    public void append(Entry entry) {
        if (entry.getIndex() != nextLogIndex) {
            throw new IllegalArgumentException("entry index must be " + nextLogIndex);
        }
        doAppend(entry);
        nextLogIndex++;
    }

    @Override
    public void append(List<Entry> entries) {
        for (Entry entry : entries) {
            append(entry);
        }
    }

    @Override
    public void removeAfter(int index) {
        if (isEmpty() || index >= doGetLastLogIndex()) {
            return;
        }
        doRemoveAfter(index);
    }

    /**
     * 根据index获取entry
     *
     * @param index 指定的index
     * @return 根据index获取的entry
     */
    protected abstract Entry doGetEntry(int index);

    /**
     * 获取一个序列的子视图
     *
     * @param fromIndex 起始索引
     * @param toIndex 终止索引
     * @return 根据索引获取的子视图
     */
    protected abstract List<Entry> doSubList(int fromIndex, int toIndex);

    /**
     * 追加日志（抽象方法）
     *
     * @param entry 要追加的entry
     */
    protected abstract void doAppend(Entry entry);

    /**
     * 移除指定索引后的entry（抽象方法）
     *
     * @param index 指定索引
     */
    protected abstract void doRemoveAfter(int index);
}
