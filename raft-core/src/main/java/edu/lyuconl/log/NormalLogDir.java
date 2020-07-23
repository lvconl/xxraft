package edu.lyuconl.log;

import java.io.File;

/**
 * 普通日志目录实现
 *
 * @date 2020年7月18日18点10分
 * @author lyuconl
 */
public class NormalLogDir extends AbstractLogDir {

    NormalLogDir(File dir) {
        super(dir);
    }

    @Override
    public String toString() {
        return "NormalLogDir{" +
                "dir=" + dir +
                "}";
    }
}
