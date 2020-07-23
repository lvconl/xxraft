package edu.lyuconl.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

/**
 * 测试用定时器
 *
 * @date 2020年7月22日10点26分
 * @author lyuconl
 */
public class NullScheduler implements Scheduler {

    private static final Logger logger = LoggerFactory.getLogger(NullScheduler.class);

    @Override
    public LogReplicationTask scheduledLogReplicationTask(@Nonnull Runnable task) {
        logger.debug("schedule log replication task");
        return LogReplicationTask.NONE;
    }

    @Override
    public ElectionTimeout scheduledElectionTimeout(@Nonnull Runnable task) {
        logger.debug("schedule election timeout");
        return ElectionTimeout.NONE;
    }

    @Override
    public void stop() throws InterruptedException {

    }
}
