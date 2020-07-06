package edu.lyuconl.schedule;

/**
 * 定时器组件接口
 *
 * @date 2020年7月6日09点40分
 * @author 吕从雷
 */
public interface Scheduler {
    /**
     * 创建日志复制任务
     *
     * @param task 日志复制任务的runnable接口实现
     * @return 日志复制任务
     */
    LogReplicationTask scheduledLogReplicationTask(Runnable task);

    /**
     * 创建选举超时器
     *
     * @param task 超时任务的runnable接口实现
     * @return 超时任务
     */
    ElectionTimeout scheduledElectionTimeout(Runnable task);

    /**
     * 关闭定时器
     *
     * @throws InterruptedException 中断异常
     */
    void stop() throws InterruptedException;
}
