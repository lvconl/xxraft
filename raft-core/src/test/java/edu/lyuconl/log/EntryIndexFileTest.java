package edu.lyuconl.log;

import edu.lyuconl.log.sequence.EntryIndexFile;
import edu.lyuconl.log.sequence.EntryIndexItem;
import edu.lyuconl.support.ByteArraySeekableFile;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Iterator;

/**
 *
 * @date 2020年7月28日15点51分
 * @author lyuconl
 */
public class EntryIndexFileTest {

    private ByteArraySeekableFile makeEntryIndexFileContent(
            int minEntryIndex, int maxEntryIndex) throws IOException {
        ByteArraySeekableFile seekableFile = new ByteArraySeekableFile();
        seekableFile.writeInt(minEntryIndex);
        seekableFile.writeInt(maxEntryIndex);
        for (int i = minEntryIndex; i <= maxEntryIndex; i++) {
            seekableFile.writeLong(10L * i);
            seekableFile.writeInt(1);
            seekableFile.writeInt(i);
        }
        seekableFile.seek(0L);
        return seekableFile;
    }

    @Test
    public void testLoad() throws IOException {
        ByteArraySeekableFile seekableFile = makeEntryIndexFileContent(3, 4);
        EntryIndexFile file = new EntryIndexFile(seekableFile);
        Assert.assertEquals(3, file.getMinEntryIndex());
        Assert.assertEquals(4, file.getMaxEntryIndex());
        Assert.assertEquals(2, file.getEntryIndexCount());
        EntryIndexItem item = file.get(3);
        Assert.assertNotNull(item);
        Assert.assertEquals(30L, item.getOffset());
        Assert.assertEquals(1, item.getKind());
        Assert.assertEquals(3, item.getTerm());
        item = file.get(4);
        Assert.assertNotNull(item);
        Assert.assertEquals(40L, item.getOffset());
        Assert.assertEquals(1, item.getKind());
        Assert.assertEquals(4, item.getTerm());
    }

    @Test
    public void testAppendEntryIndex() throws IOException {
        ByteArraySeekableFile seekableFile = new ByteArraySeekableFile();
        EntryIndexFile file = new EntryIndexFile(seekableFile);
        file.appendEntryIndex(10, 100L, 1, 2);
        Assert.assertEquals(1, file.getEntryIndexCount());
        Assert.assertEquals(10, file.getMinEntryIndex());
        Assert.assertEquals(10, file.getMaxEntryIndex());
        seekableFile.seek(0L);
        Assert.assertEquals(10, seekableFile.readInt());
        Assert.assertEquals(10, seekableFile.readInt());
        Assert.assertEquals(100L, seekableFile.readLong());
        Assert.assertEquals(1, seekableFile.readInt());
        Assert.assertEquals(2, seekableFile.readInt());

        EntryIndexItem item = file.get(10);
        Assert.assertNotNull(item);
        Assert.assertEquals(100L, item.getOffset());
        Assert.assertEquals(1, item.getKind());
        Assert.assertEquals(2, item.getTerm());

        file.appendEntryIndex(11, 200L, 1, 2);
        Assert.assertEquals(2, file.getEntryIndexCount());
        Assert.assertEquals(10, file.getMinEntryIndex());
        Assert.assertEquals(11, file.getMaxEntryIndex());
        seekableFile.seek(24L);
        Assert.assertEquals(200L, seekableFile.readLong());
        Assert.assertEquals(1, seekableFile.readInt());
        Assert.assertEquals(2, seekableFile.readInt());
    }

    @Test
    public void testClear() throws IOException {
        ByteArraySeekableFile seekableFile = makeEntryIndexFileContent(5, 6);
        EntryIndexFile file = new EntryIndexFile(seekableFile);
        Assert.assertFalse(file.isEmpty());
        file.clear();
        Assert.assertTrue(file.isEmpty());
        Assert.assertEquals(0, file.getEntryIndexCount());
        Assert.assertEquals(0L, seekableFile.size());
    }

    @Test
    public void testRemoveAfter() throws IOException {
        ByteArraySeekableFile seekableFile = makeEntryIndexFileContent(5, 6);
        long oldSize = seekableFile.size();
        EntryIndexFile file = new EntryIndexFile(seekableFile);
        file.removeAfter(6);
        Assert.assertEquals(5, file.getMinEntryIndex());
        Assert.assertEquals(6, file.getMaxEntryIndex());
        Assert.assertEquals(oldSize, seekableFile.size());
        Assert.assertEquals(2, file.getEntryIndexCount());
    }

    @Test
    public void testIterator() throws IOException {
        EntryIndexFile file = new EntryIndexFile(makeEntryIndexFileContent(3, 4));
        Iterator<EntryIndexItem> iterator = file.iterator();
        Assert.assertTrue(iterator.hasNext());
        EntryIndexItem item = iterator.next();
        Assert.assertEquals(3, item.getIndex());
        Assert.assertEquals(1, item.getKind());
        Assert.assertEquals(3, item.getTerm());
        Assert.assertTrue(iterator.hasNext());
        item = iterator.next();
        Assert.assertEquals(4, item.getIndex());
        Assert.assertFalse(iterator.hasNext());
    }
}
