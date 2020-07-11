package edu.lyuconl.support;

import java.io.IOException;
import java.io.InputStream;

/**
 * 文件操作接口
 *
 * @date 2020年7月11日16点47分
 * @author lyuconl
 */
public interface SeekableFile {

    /**
     * 获取偏移位置
     *
     * @return 偏移位置
     * @throws IOException 读取文件的IO异常
     */
    long position() throws IOException;

    void seek(long position) throws IOException;

    void writeInt(int i) throws IOException;

    void writeLong(long l) throws IOException;

    void write(byte[] b) throws IOException;

    int readInt() throws IOException;

    long readLong() throws IOException;

    int read(byte[] b) throws IOException;

    long size() throws IOException;

    void truncate(long size) throws IOException;

    InputStream inputStream(long start) throws IOException;

    void flush() throws IOException;

    void close() throws IOException;
}
