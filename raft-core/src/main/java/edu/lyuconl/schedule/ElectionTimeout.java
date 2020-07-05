package edu.lyuconl.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 超时调度
 *
 * @date 2020年7月5日16点54分
 * @author 吕从雷
 */
public class ElectionTimeout {
    private static final Logger logger = LoggerFactory.getLogger(ElectionTimeout.class);
    public static final ElectionTimeout NONE = new ElectionTimeout(new NullScheduledFuture());

    private final ScheduledFuture<?> scheduledFuture;

    public ElectionTimeout(ScheduledFuture<?> scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }

    public void cancel() {
        logger.debug("cancel election timeout");
        this.scheduledFuture.cancel(false);
    }

    @Override
    public String toString() {
        if (this.scheduledFuture.isCancelled()) {
            return "ElectionTimeout(state=canceled)";
        }
        if (this.scheduledFuture.isDone()) {
            return "ElectionTimeout(state=done)";
        }
        return "ElectionTimeout{ delay = " + scheduledFuture.getDelay(TimeUnit.MILLISECONDS) + "ms";
    }
}
