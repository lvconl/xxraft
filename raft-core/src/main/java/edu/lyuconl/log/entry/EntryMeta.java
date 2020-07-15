package edu.lyuconl.log.entry;

/**
 * 日志条目元数据
 *
 * @date 2020年7月13日15点33分
 * @author lyuconl
 */
public class EntryMeta {

    private final int kind;
    private final int term;
    private final int index;

    public EntryMeta(int kind, int term, int index) {
        this.kind = kind;
        this.term = term;
        this.index = index;
    }

    public int getKind() {
        return kind;
    }

    public int getTerm() {
        return term;
    }

    public int getIndex() {
        return index;
    }
}
