package edu.lyuconl.log.sequence;

import edu.lyuconl.log.entry.Entry;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于内存实现的entry序列
 *
 * @date 2020年7月15日16点05分
 * @author lyuconl
 */
public class MemoryEntrySequence extends AbstractEntrySequence {
    private final List<Entry> entries = new ArrayList<>();
    private int commitIndex = 0;

    public MemoryEntrySequence() {
        this(1);
    }

    public MemoryEntrySequence(int logIndexOffset) {
        super(logIndexOffset);
    }

    @Override
    protected Entry doGetEntry(int index) {
        return entries.get(index - logIndexOffset);
    }

    @Override
    protected List<Entry> doSubList(int fromIndex, int toIndex) {
        return entries.subList(fromIndex - logIndexOffset, toIndex - logIndexOffset);
    }

    @Override
    protected void doAppend(Entry entry) {
        entries.add(entry);
    }

    @Override
    protected void doRemoveAfter(int index) {
        if (index < doGetFirstLogIndex()) {
            entries.clear();
            nextLogIndex = logIndexOffset;
        } else {
            entries.subList(index - logIndexOffset + 1, entries.size());
            nextLogIndex = index + 1;
        }
    }

    @Override
    public void commit(int index) {
        commitIndex = index;
    }

    @Override
    public int getCommitIndex() {
        return commitIndex;
    }

    @Override
    public void close() {

    }

    @Override
    public String toString() {
        return "MemoryEntrySequence{" +
                "entries size=" + entries.size() +
                ", commitIndex=" + commitIndex +
                ", logIndexOffset=" + logIndexOffset +
                ", nextLogIndex=" + nextLogIndex +
                '}';
    }
}
