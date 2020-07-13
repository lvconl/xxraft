package edu.lyuconl.log.entry;

import java.util.Arrays;

/**
 * 通用日志条目（非选举日志）
 *
 * @date 2020年7月13日15点52分
 * @author lyuconl
 */
public class GeneralEntry extends AbstractEntry {

    private final byte[] commandBytes;

    GeneralEntry(int index, int term, byte[] commandBytes) {
        super(KIND_GENERAL, index, term);
        this.commandBytes = commandBytes;
    }

    @Override
    public byte[] getCommandBytes() {
        return commandBytes;
    }

    @Override
    public String toString() {
        return "GeneralEntry{" +
                "commandBytes=" + Arrays.toString(commandBytes) +
                ", index=" + index +
                ", term=" + term +
                '}';
    }
}
