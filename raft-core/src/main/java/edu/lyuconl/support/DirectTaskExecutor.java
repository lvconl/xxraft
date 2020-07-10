package edu.lyuconl.support;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 不通过单线程，直接执行异步任务，执行器
 *
 * @date 2020年7月10日20点33分
 * @author 吕从雷
 */
public class DirectTaskExecutor implements TaskExecutor {
    @Override
    public Future<?> submit(Runnable task) {
        FutureTask<?> futureTask = new FutureTask<>(task, null);
        futureTask.run();
        return futureTask;
    }

    @Override
    public <V> Future<V> submit(Callable<V> task) {
        FutureTask<V> futureTask = new FutureTask<>(task);
        futureTask.run();
        return futureTask;
    }

    @Override
    public void shutdown() throws InterruptedException {

    }
}
