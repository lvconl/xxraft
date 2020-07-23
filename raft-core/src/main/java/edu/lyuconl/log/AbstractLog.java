package edu.lyuconl.log;

import edu.lyuconl.log.entry.Entry;
import edu.lyuconl.log.entry.EntryMeta;
import edu.lyuconl.log.entry.GeneralEntry;
import edu.lyuconl.log.entry.NoOpEntry;
import edu.lyuconl.log.sequence.EntrySequence;
import edu.lyuconl.log.snapshot.Snapshot;
import edu.lyuconl.node.NodeId;
import edu.lyuconl.rpc.message.AppendEntriesRpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 日志抽象类
 *
 * @date 2020年7月17日14点42分
 * @author lyuconl
 */
public abstract class AbstractLog implements Log {
    private static final Logger logger = LoggerFactory.getLogger(AbstractLog.class);

    protected EntrySequence entrySequence;
    protected Snapshot snapshot;

    protected int commitIndex = 0;

    @Override
    public EntryMeta getLastEntryMeta() {
        if (entrySequence.isEmpty()) {
            return new EntryMeta(Entry.KIND_NO_OP, 0, 0);
        }
        return entrySequence.getLastEntry().getMate();
    }

    @Override
    public AppendEntriesRpc createAppendEntriesRpc(int term, NodeId selfId, int nextIndex, int maxEntries) {
        // 检查nextIndex
        int nextLogIndex = entrySequence.getNextLogIndex();
        if (nextIndex > nextLogIndex) {
            throw new IllegalArgumentException("illegal next index " + nextIndex);
        }
        AppendEntriesRpc rpc = new AppendEntriesRpc();
        rpc.setTerm(term);
        rpc.setLeaderId(selfId);
        rpc.setLeaderCommit(commitIndex);
        // 设置前一条日志信息，前一条日志可能不存在
        Entry entry = entrySequence.getEntry(nextIndex - 1);
        if (entry != null) {
            rpc.setPrevLogIndex(entry.getIndex());
            rpc.setPrevLogTerm(entry.getTerm());
        }
        // 设置entries
        if (!entrySequence.isEmpty()) {
            int maxIndex = (maxEntries == ALL_ENTRIES ? nextLogIndex : Math.min(nextLogIndex, nextIndex + maxEntries));
            rpc.setEntires(entrySequence.subList(nextIndex, maxIndex));
        }
        return rpc;
    }

    @Override
    public boolean isNewerThan(int lastLogIndex, int lastLogTerm) {
        EntryMeta lastEntryMeta = getLastEntryMeta();
        logger.debug("last entry ({}, {}), candidate ({}, {})",
                lastEntryMeta.getIndex(), lastEntryMeta.getTerm(),
                lastLogIndex, lastLogTerm);
        return lastEntryMeta.getTerm() > lastLogTerm || lastEntryMeta.getIndex() > lastLogIndex;
    }

    @Override
    public NoOpEntry appendEntry(int term) {
        NoOpEntry entry = new NoOpEntry(entrySequence.getNextLogIndex(), term);
        entrySequence.append(entry);
        return entry;
    }

    @Override
    public GeneralEntry appendEntry(int term, byte[] command) {
        GeneralEntry entry = new GeneralEntry(entrySequence.getNextLogIndex(), term, command);
        entrySequence.append(entry);
        return entry;
    }

    @Override
    public boolean appendEntriesFromLeader(int preLogIndex, int preLogTerm, List<Entry> entries) {
        // 检查前一条日志是否匹配
        if (!checkIfPreviousLogMatches(preLogIndex, preLogTerm)) {
            return false;
        }

        // Leader节点传过来的日志为空
        if (entries.isEmpty()) {
            return true;
        }

        // 移除冲突的日志条目并返回接下来要追加的日志条目
        EntrySequenceView newEntries = removeUnmatchedLog(new EntrySequenceView(entries));
        appendEntriesFromLeader(newEntries);
        return true;
    }

    private void appendEntriesFromLeader(EntrySequenceView leaderEntries) {
        if (leaderEntries.isEmpty()) {
            return;
        }
        logger.debug("append entries from leader from {} to {}",
                leaderEntries.getFirstLogIndex(), leaderEntries.getLastLogIndex());
        for (Entry leaderEntry : leaderEntries) {
            entrySequence.append(leaderEntry);
        }
    }

    private EntrySequenceView removeUnmatchedLog(EntrySequenceView leaderEntries) {
        assert !leaderEntries.isEmpty();
        int firstUnmatched = findFirstUnmatchedLog(leaderEntries);
        // 没有不匹配的日志
        if (firstUnmatched < 0) {
            return new EntrySequenceView(Collections.emptyList());
        }
        removeEntriesAfter(firstUnmatched - 1);
        return leaderEntries.subView(firstUnmatched);
    }

    private int findFirstUnmatchedLog(EntrySequenceView leaderEntries) {
        assert !leaderEntries.isEmpty();
        int logIndex;
        EntryMeta followerEntryMeta;
        for (Entry leaderEntry : leaderEntries) {
            logIndex = leaderEntry.getIndex();
            followerEntryMeta = entrySequence.getEntryMeta(logIndex);
            if (followerEntryMeta == null || followerEntryMeta.getTerm() != leaderEntry.getTerm()) {
                return logIndex;
            }
        }
        return -1;
    }

    private void removeEntriesAfter(int index) {
        if (entrySequence.isEmpty() || index >= entrySequence.getLastLogIndex()) {
            return;
        }
        logger.debug("remove entries after {} ", index);
        entrySequence.removeAfter(index);
    }

    private boolean checkIfPreviousLogMatches(int preLogIndex, int preLogTerm) {
//        int lastIncludedIndex = snapshot.getLastIncludedIndex();
//        if (preLogIndex < lastIncludedIndex) {
//            logger.debug("previous log index {} < snapshot's last included index {}.", preLogIndex, lastIncludedIndex);
//            return false;
//        }
//        if (preLogIndex == lastIncludedIndex) {
//            int lastIncludedTerm = snapshot.getLastIncludedTerm();
//            if (preLogTerm != lastIncludedTerm) {
//                logger.debug("previous log index matches snapshot's last included index, " +
//                        "but term not (expected {}, actual {})", lastIncludedTerm, preLogTerm);
//                return false;
//            }
//            return true;
//        }
//        Entry entry = entrySequence.getEntry(preLogIndex);
//        if (entry == null) {
//            logger.debug("previous log {} not found", preLogIndex);
//            return false;
//        }
//        int term = entry.getTerm();
//        if (term != preLogTerm) {
//            logger.debug("different term of previous log, local {}, remote {}", term, preLogTerm);
//            return false;
//        }
//        return true;

        // 检查指定索引的日志条目
        EntryMeta entryMeta = entrySequence.getEntryMeta(preLogIndex);
        //日志不存在
        if (entryMeta == null) {
            logger.debug("previous log {} not found.", preLogIndex);
            return false;
        }
        int term = entryMeta.getTerm();
        if (term != preLogTerm) {
            logger.debug("different term of previous log, local {}, remote {}", term, preLogTerm);
            return false;
        }
        return true;
    }

    @Override
    public void advanceCommitIndex(int newCommitIndex, int currentTerm) {
        if (!validateNewCommitIndex(newCommitIndex, currentTerm)) {
            return;
        }
        logger.debug("advance commit index from {} to {}", commitIndex, newCommitIndex);
        entrySequence.commit(newCommitIndex);
    }

    private boolean validateNewCommitIndex(int newCommitIndex, int currentTerm) {
        // 小于当前commitIndex
        if (newCommitIndex <= entrySequence.getCommitIndex()) {
            return false;
        }
        EntryMeta meta = entrySequence.getEntryMeta(newCommitIndex);
        if (meta == null) {
            logger.debug("log of new commit index {} not found", newCommitIndex);
            return false;
        }
        // 日志条目term必须当前term，才可推进commitIndex
        if (meta.getTerm() != currentTerm) {
            logger.debug("log term of commit index != current term, ({} != {})", meta.getTerm(), currentTerm);
            return false;
        }
        return true;
    }

    private static class EntrySequenceView implements Iterable<Entry> {

        private final List<Entry> entries;
        private int firstLogIndex;
        private int lastLogIndex;

        EntrySequenceView(List<Entry> entries) {
            this.entries = entries;
            if (!entries.isEmpty()) {
                firstLogIndex = entries.get(0).getIndex();
                lastLogIndex = entries.get(entries.size() - 1).getIndex();
            }
        }

        Entry get(int index) {
            if (entries.isEmpty() || index > lastLogIndex || index < firstLogIndex) {
                return null;
            }
            return entries.get(index - firstLogIndex);
        }

        boolean isEmpty() {
            return entries.isEmpty();
        }

        int getFirstLogIndex() {
            return firstLogIndex;
        }

        int getLastLogIndex() {
            return lastLogIndex;
        }

        @Override
        @Nonnull
        public Iterator<Entry> iterator() {
            return entries.iterator();
        }

        EntrySequenceView subView(int fromIndex) {
            if (entries.isEmpty() || fromIndex >= lastLogIndex) {
                return new EntrySequenceView(Collections.emptyList());
            }
            return new EntrySequenceView(entries.subList(fromIndex - firstLogIndex, entries.size()));
        }
    }
}
