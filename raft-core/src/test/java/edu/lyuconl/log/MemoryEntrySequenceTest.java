package edu.lyuconl.log;

import edu.lyuconl.log.entry.Entry;
import edu.lyuconl.log.entry.EntryMeta;
import edu.lyuconl.log.entry.NoOpEntry;
import edu.lyuconl.log.sequence.MemoryEntrySequence;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * 基于内存的日志序列测试
 *
 * @date 2020年7月27日10点57分
 * @author lyuconl
 */
public class MemoryEntrySequenceTest {

    @Test
    public void testAppendEntry() {
        MemoryEntrySequence sequence = new MemoryEntrySequence();
        sequence.append(new NoOpEntry(sequence.getNextLogIndex(), 1));
        Assert.assertEquals(2, sequence.getNextLogIndex());
        Assert.assertEquals(1, sequence.getLastLogIndex());
    }

    @Test
    public void testGetEntry() {
        MemoryEntrySequence sequence = new MemoryEntrySequence(2);
        sequence.append(Arrays.asList(
                new NoOpEntry(2, 1),
                new NoOpEntry(3, 1)
        ));
        Assert.assertNull(sequence.getEntry(1));
        Assert.assertEquals(2, sequence.getEntry(2).getIndex());
        Assert.assertEquals(3, sequence.getEntry(3).getIndex());
        Assert.assertNull(sequence.getEntry(4));
    }

    @Test
    public void testGetEntryMeta() {
        MemoryEntrySequence sequence = new MemoryEntrySequence(2);
        Assert.assertNull(sequence.getEntry(2));
        sequence.append(new NoOpEntry(2, 1));
        EntryMeta meta = sequence.getEntryMeta(2);
        Assert.assertNotNull(meta);
        Assert.assertEquals(2, meta.getIndex());
        Assert.assertEquals(1, meta.getTerm());
    }

    @Test
    public void testSubListOnElement() {
        MemoryEntrySequence sequence = new MemoryEntrySequence(2);
        sequence.append(Arrays.asList(
                new NoOpEntry(2, 1),
                new NoOpEntry(3, 1)
        ));
        List<Entry> subList = sequence.subList(2, 3);
        Assert.assertEquals(1, subList.size());
        Assert.assertEquals(2, subList.get(0).getIndex());
    }

    @Test
    public void testRemoveAfterPartial() {
        MemoryEntrySequence sequence = new MemoryEntrySequence(2);
        sequence.append(Arrays.asList(
                new NoOpEntry(2, 1),
                new NoOpEntry(3, 1)
        ));
        sequence.removeAfter(2);
        Assert.assertEquals(2, sequence.getLastLogIndex());
        Assert.assertEquals(3, sequence.getNextLogIndex());
    }
}
