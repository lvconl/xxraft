package edu.lyuconl.log;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

/**
 * 抽象LogDir
 *
 * @date 2020年7月18日18点10分
 * @author lyuconl
 */
public class AbstractLogDir implements LogDir {

    final File dir;

    AbstractLogDir(File dir) {
        this.dir = dir;
    }

    @Override
    public void initialize() {
        if (!dir.exists() && !dir.mkdir()) {
            throw new LogException("failed to create dir " + dir);
        }
        try {
            Files.touch(getEntriesFile());
            Files.touch(getEntryOffsetIndexFile());
        } catch (IOException e) {
            throw new LogException("failed to create file", e);
        }
    }

    @Override
    public boolean exists() {
        return dir.exists();
    }

    @Override
    public File getEntriesFile() {
        return new File(dir, RootDir.FILE_NAME_ENTRIES);
    }

    @Override
    public File getEntryOffsetIndexFile() {
        return new File(dir, RootDir.FILE_NAME_ENTRY_OFFSET_INDEX);
    }

    @Override
    public File getSnapshotFile() {
        return new File(dir, RootDir.FILE_NAME_SNAPSHOT);
    }

    @Override
    public File get() {
        return dir;
    }

    @Override
    public boolean renameTo(LogDir logDir) {
        return dir.renameTo(logDir.get());
    }
}
