package edu.lyuconl.log.entry;

/**
 *
 * 日志条目通用接口
 *
 * @date 2020年7月13日15点21分
 * @author lyuconl
 */
public interface Entry {

    int KIND_NO_OP = 0;
    int KIND_GENERAL = 1;

    /**
     * 获得类型
     *
     * @return 日志条目类型
     */
    int getKind();

    /**
     * 获得索引
     *
     * @return 日志条目索引
     */
    int getIndex();

    /**
     * 获得term
     *
     * @return 日志条目term
     */
    int getTerm();

    /**
     * 获得日志条目元数据
     *
     * @return 日志条目元数据
     */
    EntryMeta getMate();

    /**
     * 获取日志负载
     *
     * @return 日志负载
     */
    byte[] getCommandBytes();
}
