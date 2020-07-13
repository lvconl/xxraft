package edu.lyuconl.log.entry;

/**
 * 空日志
 *
 * @date 2020年7月13日15点57分
 * @author lyuconl
 */
public class NoOpEntry extends AbstractEntry {
    NoOpEntry(int index, int term) {
        super(KIND_NO_OP, index, term);
    }

    @Override
    public byte[] getCommandBytes() {
        return new byte[0];
    }

    @Override
    public String toString() {
        return "GeneralEntry{" +
                "index=" + index +
                ", term=" + term +
                '}';
    }
}
