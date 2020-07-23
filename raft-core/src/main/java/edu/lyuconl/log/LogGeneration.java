package edu.lyuconl.log;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * log目录分代
 *
 * @date 2020年7月19日11点54分
 * @author lyuconl
 */
public class LogGeneration extends AbstractLogDir implements Comparable<LogGeneration> {

    private static final Pattern DIR_NAME_PATTERN = Pattern.compile("log-(\\d+)");
    private final int lastIncludedIndex;

    LogGeneration(File baseDir, int lastIncludedIndex) {
        super(new File(baseDir, generateDirName(lastIncludedIndex)));
        this.lastIncludedIndex = lastIncludedIndex;
    }

    LogGeneration(File dir) {
        super(dir);
        Matcher matcher = DIR_NAME_PATTERN.matcher(dir.getName());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("not a dir name of log generation, [ " + dir.getName() + " ]");
        }
        lastIncludedIndex = Integer.parseInt(matcher.group(1));
    }

    static boolean isValidDirName(String name) {
        return DIR_NAME_PATTERN.matcher(name).matches();
    }

    private static String generateDirName(int lastIncludedIndex) {
        return "log-" + lastIncludedIndex;
    }

    public int getLastIncludedIndex() {
        return lastIncludedIndex;
    }

    @Override
    public int compareTo(LogGeneration o) {
        return Integer.compare(lastIncludedIndex, o.lastIncludedIndex);
    }

    @Override
    public String toString() {
        return "LogGeneration{" +
                "lastIncludedIndex=" + lastIncludedIndex +
                ", dir=" + dir +
                '}';
    }
}
