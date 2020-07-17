package edu.lyuconl.log.entry;

/**
 * entry工厂类
 *
 * @date 2020年7月16日09点33分
 * @author lyuconl
 */
public class EntryFactory {
    public Entry create(int kind, int index, int term, byte[] commandBytes) {
        switch (kind) {
            case Entry.KIND_NO_OP:
                return new NoOpEntry(index, term);
            case Entry.KIND_GENERAL:
                return new GeneralEntry(index, term, commandBytes);
            default:
                throw new IllegalArgumentException("unexpected entry kind " + kind);
        }
    }
}
