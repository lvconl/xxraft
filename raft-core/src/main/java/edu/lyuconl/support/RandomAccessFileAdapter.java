package edu.lyuconl.support;

import java.io.*;


/**
 *
 * @date 2020年7月11日16点53分
 * @author lyuconl
 */
public class RandomAccessFileAdapter implements SeekableFile {

    private final File file;
    private final RandomAccessFile randomAccessFile;

    public RandomAccessFileAdapter(File file, String mode) throws FileNotFoundException {
        this.file = file;
        randomAccessFile = new RandomAccessFile(file, mode);
    }

    public RandomAccessFileAdapter(File file) throws FileNotFoundException {
        this(file, "rw");
    }

    @Override
    public long position() throws IOException {
        return randomAccessFile.getFilePointer();
    }

    @Override
    public void seek(long position) throws IOException {
        randomAccessFile.seek(position);
    }

    @Override
    public void writeInt(int i) throws IOException {
        randomAccessFile.writeInt(i);
    }

    @Override
    public void writeLong(long l) throws IOException {
        randomAccessFile.writeLong(l);
    }

    @Override
    public void write(byte[] b) throws IOException {
        randomAccessFile.write(b);
    }

    @Override
    public int readInt() throws IOException {
        return randomAccessFile.readInt();
    }

    @Override
    public long readLong() throws IOException {
        return randomAccessFile.readLong();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return randomAccessFile.read(b);
    }

    @Override
    public long size() throws IOException {
        return randomAccessFile.length();
    }

    @Override
    public void truncate(long size) throws IOException {
        randomAccessFile.setLength(size);
    }

    @Override
    public InputStream inputStream(long start) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        if (start > 0) {
            inputStream.skip(start);
        }
        return inputStream;
    }

    @Override
    public void flush() throws IOException {

    }

    @Override
    public void close() throws IOException {
        randomAccessFile.close();
    }
}
