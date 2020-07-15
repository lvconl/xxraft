package edu.lyuconl.log.entry;

/**
 * 日志条目抽象实现
 *
 * @date 2020年7月13日15点42分
 * @author lyuconl
 */
public class AbstractEntry implements Entry {

    private final int kind;
    protected final int index;
    protected final int term;

    AbstractEntry(int kind, int index, int term) {
        this.kind = kind;
        this.index = index;
        this.term = term;
    }

    @Override
    public int getKind() {
        return kind;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public int getTerm() {
        return term;
    }

    @Override
    public EntryMeta getMate() {
        return new EntryMeta(kind, term, index);
    }

    @Override
    public byte[] getCommandBytes() {
        return new byte[0];
    }
}
