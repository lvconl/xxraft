package edu.lyuconl.log.sequence;

import edu.lyuconl.log.entry.Entry;
import edu.lyuconl.log.entry.EntryFactory;
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

    /**
     * 向文件中追加entry
     *
     * @param entry 追加的entry
     * @return 文件中的偏移
     * @throws IOException IO异常
     */
    public long appendEntry(Entry entry) throws IOException {
        long offset = seekableFile.size();
        seekableFile.seek(offset);
        seekableFile.writeInt(entry.getKind());
        seekableFile.writeInt(entry.getIndex());
        seekableFile.writeInt(entry.getTerm());
        byte[] command = entry.getCommandBytes();
        seekableFile.writeInt(command.length);
        seekableFile.write(command);
        return offset;
    }

    /**
     * 从指定偏移处加载entry
     *
     * @param offset 指定偏移
     * @param factory entry工厂类
     * @return 加载的entry
     * @throws IOException IO异常
     */
    public Entry loadEntry(long offset, EntryFactory factory) throws IOException {
        if (offset > seekableFile.size()) {
            throw new IllegalArgumentException("offset > file's size");
        }
        seekableFile.seek(offset);
        int kind = seekableFile.readInt();
        int index = seekableFile.readInt();
        int term = seekableFile.readInt();
        int length = seekableFile.readInt();
        byte[] commandBytes = new byte[length];
        seekableFile.read(commandBytes);
        return factory.create(kind, index, term, commandBytes);
    }

    /**
     * 获取文件大小
     *
     * @return 文件大小
     * @throws IOException IO异常
     */
    public long size() throws IOException { return seekableFile.size(); }

    /**
     * 清除文件
     *
     * @throws IOException IO异常
     */
    public void clear() throws IOException { seekableFile.truncate(0L); }

    /**
     * 剪裁文件到指定大小
     *
     * @param offset 指定的偏移位置
     * @throws IOException IO异常
     */
    public void truncate(long offset) throws IOException {
        seekableFile.truncate(offset);
    }

    public void close() throws IOException {
        seekableFile.close();
    }

}
