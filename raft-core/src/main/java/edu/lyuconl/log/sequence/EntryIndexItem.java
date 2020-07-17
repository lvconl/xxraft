package edu.lyuconl.log.sequence;

import edu.lyuconl.log.entry.EntryMeta;

import javax.annotation.concurrent.Immutable;

/**
 * entry索引文件条目
 *
 * @date 2020年7月16日10点00分
 * @author lyuconl
 */

@Immutable
public class EntryIndexItem {

    private final int index;
    private final long offset;
    private final int kind;
    private final int term;

    public EntryIndexItem(int index, long offset, int kind, int term) {
        this.index = index;
        this.offset = offset;
        this.kind = kind;
        this.term = term;
    }

    public int getIndex() {
        return index;
    }

    public long getOffset() {
        return offset;
    }

    public int getKind() {
        return kind;
    }

    public int getTerm() {
        return term;
    }

    EntryMeta toEntryMeta() {
        return new EntryMeta(kind, term, index);
    }
}
