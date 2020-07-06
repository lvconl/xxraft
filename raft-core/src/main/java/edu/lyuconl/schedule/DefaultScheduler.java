package edu.lyuconl.schedule;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 默认定时器实现
 *
 * @date 2020年7月6日09点46分
 * @author 吕从雷
 */
public class DefaultScheduler implements Scheduler {
    private final int maxElectionTimeout;
    private final int minElectionTimeout;
    private final int logReplicationDelay;
    private final int logReplicationInterval;
    private final Random electionTimeoutRandom;
    private final ScheduledExecutorService scheduledExecutorService;

    /**
     * 构造函数
     *
     * @param minElectionTimeout 最小选举超时时间
     * @param maxElectionTimeout 最大选举超时时间
     * @param logReplicationDelay 日志复制延迟
     * @param logReplicationInterval 日志复制周期
     */
    public DefaultScheduler(int minElectionTimeout, int maxElectionTimeout,
                            int logReplicationDelay, int logReplicationInterval) {
        if (minElectionTimeout <= 0 || maxElectionTimeout <= 0 || maxElectionTimeout <= minElectionTimeout) {
            throw new IllegalArgumentException("election timeout should not be 0 or min > max");
        }
        if (logReplicationDelay < 0 || logReplicationInterval <= 0) {
            throw new IllegalArgumentException("log replication delay < 0 or log replication interval <= 0");
        }
        this.minElectionTimeout = minElectionTimeout;
        this.maxElectionTimeout = maxElectionTimeout;
        this.logReplicationDelay = logReplicationDelay;
        this.logReplicationInterval = logReplicationInterval;
        electionTimeoutRandom = new Random();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "scheduler"));
    }

    @Override
    public LogReplicationTask scheduledLogReplicationTask(Runnable task) {
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(task,
                logReplicationDelay, logReplicationInterval, TimeUnit.MILLISECONDS);
        return new LogReplicationTask(scheduledFuture);
    }

    @Override
    public ElectionTimeout scheduledElectionTimeout(Runnable task) {
        // 随机选举超时时间
        int timeout = electionTimeoutRandom.nextInt(maxElectionTimeout - minElectionTimeout) + minElectionTimeout;
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.schedule(task, timeout, TimeUnit.MILLISECONDS);
        return new ElectionTimeout(scheduledFuture);
    }

    @Override
    public void stop() throws InterruptedException {
        scheduledExecutorService.shutdown();
    }
}
