package edu.lyuconl.log.sequence;

import edu.lyuconl.log.entry.Entry;
import edu.lyuconl.support.RandomAccessFileAdapter;
import edu.lyuconl.support.SeekableFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * entry文件
 *
 * @date 2020年7月15日16点53分
 * @author lyuconl
 */
public class EntriesFile {
    private final SeekableFile seekableFile;

    public EntriesFile(File file) throws FileNotFoundException {
        this(new RandomAccessFileAdapter(file));
    }

    public EntriesFile(SeekableFile seekableFile) {
        this.seekableFile = seekableFile;
    }

    public long appendEntry(Entry entry) throws IOException {
        long offset = seekableFile.size();
        seekableFile.writeInt(entry.getKind());
        seekableFile.writeInt(entry.getIndex());
        seekableFile.writeInt(entry.getTerm());
        byte[] command = entry.getCommandBytes();
        seekableFile.writeInt(command.length);
        seekableFile.write(command);
        return offset;
    }
}
