package edu.lyuconl.log;

import edu.lyuconl.log.sequence.EntrySequence;
import edu.lyuconl.log.sequence.MemoryEntrySequence;

/**
 * 基于内存的Log
 *
 * @date 2020年7月18日17点31分
 * @author lyuconl
 */
public class MemoryLog extends AbstractLog {

    public MemoryLog() {
        this(new MemoryEntrySequence());
    }

    MemoryLog(EntrySequence entrySequence) {
        this.entrySequence = entrySequence;
    }

    @Override
    public int getNextIndex() {
        return 0;
    }

    @Override
    public int getCommitIndex() {
        return commitIndex;
    }

    @Override
    public void close() {
    }
}
