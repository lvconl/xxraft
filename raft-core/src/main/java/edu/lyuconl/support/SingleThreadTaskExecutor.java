package edu.lyuconl.support;

import java.util.concurrent.*;

/**
 * 异步单线程任务执行器
 *
 * @date 2020年7月10日18点21分
 * @author 吕从雷
 */
public class SingleThreadTaskExecutor implements TaskExecutor {
    private final ExecutorService executorService;

    public SingleThreadTaskExecutor(ExecutorService service) {
        this(Executors.defaultThreadFactory());
    }

    public SingleThreadTaskExecutor(String name) {
        this(r -> new Thread(r, name));
    }

    public SingleThreadTaskExecutor(ThreadFactory threadFactory) {
        executorService = Executors.newSingleThreadExecutor(threadFactory);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return executorService.submit(task);
    }

    @Override
    public <V> Future<V> submit(Callable<V> task) {
        return executorService.submit(task);
    }

    @Override
    public void shutdown() throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);
    }
}
