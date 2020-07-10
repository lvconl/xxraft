package edu.lyuconl.support;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 任务执行器接口
 *
 * @date 2020年7月10日17点40分
 * @author 吕从雷
 */
public interface TaskExecutor {
    /**
     * 提交任务，无返回值
     *
     * @param task 提交的任务
     * @return 任务结果
     */
    Future<?> submit(Runnable task);

    /**
     * 提交任务，有返回值
     *
     * @param task 提交的任务
     * @param <V> 任务返回值类型
     * @return 任务结果
     */
    <V> Future<V> submit(Callable<V> task);

    /**
     * 关闭任务执行器
     *
     * @throws InterruptedException 中断异常
     */
    void shutdown() throws InterruptedException;
}
