package edu.lyuconl.log.snapshot;

/**
 * 快照分块
 *
 * @date 2020年7月17日18点31分
 * @author lyuconl
 */
public class SnapshotChunk {

    private final byte[] bytes;
    private final boolean lastChunk;

    SnapshotChunk(byte[] bytes, boolean lastChunk) {
        this.bytes = bytes;
        this.lastChunk = lastChunk;
    }

    public boolean isLastChunk() {
        return lastChunk;
    }

    public byte[] toByteArray() {
        return bytes;
    }
}
