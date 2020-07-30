package edu.lyuconl.log;

import edu.lyuconl.log.entry.Entry;
import edu.lyuconl.log.entry.NoOpEntry;
import edu.lyuconl.node.NodeId;
import edu.lyuconl.rpc.message.AppendEntriesRpc;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author lyuconl
 */
public class MemoryLogTest {

    @Test
    public void testCreateAppendEntriesRpcStartFromOne() {
        MemoryLog log = new MemoryLog();
        log.appendEntry(1);
        log.appendEntry(2);
        AppendEntriesRpc rpc = log.createAppendEntriesRpc(
                1, new NodeId("A"), 1, Log.ALL_ENTRIES
        );

        Assert.assertEquals(1, rpc.getTerm());
        Assert.assertEquals(0, rpc.getPrevLogIndex());
        Assert.assertEquals(0, rpc.getPrevLogTerm());
        Assert.assertEquals(2, rpc.getEntires().size());
        Assert.assertEquals(1, rpc.getEntires().get(0).getIndex());
    }

    @Test
    public void testAppendEntriesFromLeaderSkip() {
        // (index, term)
        // follower: (1, 1), (2, 1)
        // leader:           (2, 1), (3, 2)
        MemoryLog log = new MemoryLog();
        log.appendEntry(1);
        log.appendEntry(1);
        List<Entry> leaderEntries = Arrays.asList(
                new NoOpEntry(2, 1),
                new NoOpEntry(3, 2)
        );
        Assert.assertTrue(log.appendEntriesFromLeader(1, 1, leaderEntries));
    }

    @Test
    public void testAppendEntriesFromLeaderConflict1() {
        // (index, term)
        // follower: (1, 1), (2, 1)
        // leader:           (2, 2), (3, 2)
        MemoryLog log = new MemoryLog();
        log.appendEntry(1);
        log.appendEntry(1);
        List<Entry> leaderEntries = Arrays.asList(
                new NoOpEntry(2, 2),
                new NoOpEntry(3, 2)
        );
        Assert.assertTrue(log.appendEntriesFromLeader(1, 1, leaderEntries));
    }
}
