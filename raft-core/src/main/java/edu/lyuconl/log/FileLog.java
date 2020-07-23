package edu.lyuconl.log;

import edu.lyuconl.log.sequence.FileEntrySequence;

import java.io.File;

/**
 * 基于文件的Log
 *
 * @date 2020年7月18日17点47分
 * @author lyuconl
 */
public class FileLog extends AbstractLog {

    private final RootDir rootDir;

    public FileLog(File baseDir) {
        rootDir = new RootDir(baseDir);

        //获取最新的日志代
        LogGeneration latestGeneration = rootDir.getLatestGeneration();
        if (latestGeneration != null) {
            entrySequence = new FileEntrySequence(latestGeneration, latestGeneration.getLastIncludedIndex());
        } else {
            LogGeneration firstGeneration = rootDir.createFirstGeneration();
            entrySequence = new FileEntrySequence(firstGeneration, 1);
        }

    }


    @Override
    public int getNextIndex() {
        return 0;
    }

    @Override
    public int getCommitIndex() {
        return 0;
    }

    @Override
    public void close() {

    }
}
