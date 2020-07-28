package edu.lyuconl.log;

import edu.lyuconl.log.entry.Entry;
import edu.lyuconl.log.entry.EntryFactory;
import edu.lyuconl.log.entry.GeneralEntry;
import edu.lyuconl.log.entry.NoOpEntry;
import edu.lyuconl.log.sequence.EntriesFile;
import edu.lyuconl.support.ByteArraySeekableFile;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * EntriesFile测试
 *
 * @date 2020年7月28日11点13分
 * @author lyuconl
 */
public class EntriesFileTest {

    @Test
    public void testAppendEntry() throws IOException {
        ByteArraySeekableFile seekableFile = new ByteArraySeekableFile();
        EntriesFile file = new EntriesFile(seekableFile);
        Assert.assertEquals(0L, file.appendEntry(new NoOpEntry(2, 3)));
        seekableFile.seek(0);
        Assert.assertEquals(Entry.KIND_NO_OP, seekableFile.readInt());
        // index
        Assert.assertEquals(2, seekableFile.readInt());
        // term
        Assert.assertEquals(3, seekableFile.readInt());
        // command bytes length
        Assert.assertEquals(0, seekableFile.readInt());

        byte[] commandBytes = "test".getBytes();
        Assert.assertEquals(16L, file.appendEntry(new GeneralEntry(3, 3, commandBytes)));
        seekableFile.seek(16L);
        Assert.assertEquals(Entry.KIND_GENERAL, seekableFile.readInt());
        // index
        Assert.assertEquals(3, seekableFile.readInt());
        // term
        Assert.assertEquals(3, seekableFile.readInt());
        // commandBytesLength
        Assert.assertEquals(4, seekableFile.readInt());
        byte[] buffer = new byte[4];
        seekableFile.read(buffer);
        Assert.assertArrayEquals(commandBytes, buffer);
    }

    @Test
    public void testLoadEntry() throws IOException {
        ByteArraySeekableFile seekableFile = new ByteArraySeekableFile();
        EntriesFile file = new EntriesFile(seekableFile);
        Assert.assertEquals(0L, file.appendEntry(new NoOpEntry(2, 3)));
        Assert.assertEquals(16L, file.appendEntry(new GeneralEntry(3, 3, "test".getBytes())));
        Assert.assertEquals(36L, file.appendEntry(new GeneralEntry(4, 3, "foo".getBytes())));

        EntryFactory factory = new EntryFactory();

        Entry entry = file.loadEntry(0L, factory);
        Assert.assertEquals(Entry.KIND_NO_OP, entry.getKind());
        Assert.assertEquals(2, entry.getIndex());
        Assert.assertEquals(3, entry.getTerm());
        entry = file.loadEntry(36L, factory);
        Assert.assertEquals(Entry.KIND_GENERAL, entry.getKind());
        Assert.assertEquals(4, entry.getIndex());
        Assert.assertEquals(3, entry.getTerm());
        Assert.assertArrayEquals("foo".getBytes(), entry.getCommandBytes());
    }

    @Test
    public void testTruncate() throws IOException {
        ByteArraySeekableFile seekableFile = new ByteArraySeekableFile();
        EntriesFile file = new EntriesFile(seekableFile);
        file.appendEntry(new NoOpEntry(2, 3));
        Assert.assertTrue(seekableFile.size() > 0);
        file.truncate(0L);
        Assert.assertEquals(0L, seekableFile.size());
    }
}
