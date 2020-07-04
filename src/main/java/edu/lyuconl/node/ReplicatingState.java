package edu.lyuconl.node;

/**
 * 日志复制状态
 *
 * @date 2020年6月26日20点41分
 * @author 吕从雷
 */
public class ReplicatingState {
    private int nextIndex;
    private int matchIndex;
    private boolean replicating = false;
    private long lastReplicatedAt = 0;

    ReplicatingState (int nextIndex, int matchIndex) {
        this.nextIndex = nextIndex;
        this.matchIndex = matchIndex;
    }

    ReplicatingState(int nextIndex) {
        this(nextIndex, 0);
    }

    public int getMatchIndex() {
        return matchIndex;
    }

    public int getNextIndex() {
        return nextIndex;
    }

    /**
     * 回退nextIndex
     *
     * @return 回退状态，成功返回true，当nextIndex小于等于 {@code 1}是返回false
     */
    boolean backOffNextIndex() {
        if (nextIndex > 1) {
            nextIndex--;
            return true;
        }
        return false;
    }

    /**
     * 推进matchIndex和nextIndex
     *
     * @param lastEntryIndex 最后一个条目的index
     * @return 推进状态
     */
    boolean advance (int lastEntryIndex) {
        boolean result = (matchIndex != lastEntryIndex || nextIndex != (lastEntryIndex + 1));

        matchIndex = lastEntryIndex;
        nextIndex = lastEntryIndex + 1;
        return result;
    }

    /**
     * 是否是正在复制状态
     *
     * @return 复制返回true，否则返回false
     */
    boolean isReplicating() {
        return replicating;
    }

    public void setReplicating(boolean replicating) {
        this.replicating = replicating;
    }

    public long getLastReplicatedAt() {
        return lastReplicatedAt;
    }

    public void setLastReplicatedAt(long lastReplicatedAt) {
        this.lastReplicatedAt = lastReplicatedAt;
    }

    @Override
    public String toString() {
        return "ReplicatingState{" +
                "nextIndex=" + nextIndex +
                ", matchIndex=" + matchIndex +
                ", replicating=" + replicating +
                ", lastReplicatedAt=" + lastReplicatedAt +
                '}';
    }
}
