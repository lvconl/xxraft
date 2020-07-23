package edu.lyuconl.log;

import java.io.File;

/**
 * 获取指定文件接口
 *
 * @date 2020年7月16日16点02分
 * @author lyuconl
 */
public interface LogDir {

    /**
     * 初始化方法
     *
     */
    void initialize();

    /**
     * 文件是否存在
     *
     * @return 存在返回true，否则返回false
     */
    boolean exists();

    /**
     * 获取EntriesFile对应的文件
     *
     * @return EntriesFile对应的文件
     */
    File getEntriesFile();

    /**
     * 获取EntryIndexFile对应的文件
     *
     * @return EntryIndexFile对应的文件
     */
    File getEntryOffsetIndexFile();

    /**
     * 获取快照文件
     *
     * @return 快照文件
     */
    File getSnapshotFile();

    /**
     * 获取目录
     *
     * @return 目录
     */
    File get();

    /**
     * 重命名目录
     *
     * @param logDir 新目录名
     * @return 修改状态
     */
    boolean renameTo(LogDir logDir);

}
