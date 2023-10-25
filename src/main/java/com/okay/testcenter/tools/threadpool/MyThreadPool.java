package com.okay.testcenter.tools.threadpool;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author zhou
 * @date 2020/12/21
 */
public class MyThreadPool {

    private static final int DEFAULT_MAX_CONCURRENT = 10;
//    private static final int DEFAULT_MAX_CONCURRENT = Runtime.getRuntime().availableProcessors() * 2;
    private static final String THREAD_POOL_NAME = "MyThreadPool-%d";
    private static final ThreadFactory FACTORY = new BasicThreadFactory.Builder().namingPattern(THREAD_POOL_NAME)
            .daemon(true).build();
    private static final int DEFAULT_SIZE = 10000;
    private static final long DEFAULT_KEEP_ALIVE = 60L;
    static Logger LOGGER = LoggerFactory.getLogger(MyThreadPool.class);
    private static ExecutorService executor;

    private static BlockingQueue<Runnable> executeQueue = new ArrayBlockingQueue<>(DEFAULT_SIZE);


    static {
        try {
            executor = new ThreadPoolExecutor(DEFAULT_MAX_CONCURRENT, 50, DEFAULT_KEEP_ALIVE,
                    TimeUnit.SECONDS, executeQueue, FACTORY);

            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    LOGGER.info("MyThreadPool shutting down.");
                    executor.shutdown();

                    try {
                        if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                            LOGGER.error("MyThreadPool shutdown immediately due to wait timeout.");
                            executor.shutdownNow();
                        }
                    } catch (InterruptedException e) {
                        LOGGER.error("MyThreadPool shutdown interrupted.");
                        executor.shutdownNow();
                    }

                    LOGGER.info("MyThreadPool shutdown complete.");
                }
            }));
        } catch (Exception e) {
            LOGGER.error("MyThreadPool init error.", e);
            throw new ExceptionInInitializerError(e);
        }
    }

    private MyThreadPool() {
    }

    public static boolean execute(Runnable task) {

        try {
            executor.execute(task);
        } catch (RejectedExecutionException e) {
            LOGGER.error("Task executing was rejected.", e);
            return false;
        }

        return true;
    }

    public static <T> Future<T> submitTask(Callable<T> task) {

        try {
            return executor.submit(task);
        } catch (RejectedExecutionException e) {
            LOGGER.error("Task executing was rejected.", e);
            throw new UnsupportedOperationException("Unable to submit the task, rejected.", e);
        }
    }


    public static <T> List<Future<T>> submitTasks(Collection<? extends Callable<T>> tasks) {

        try {
            return executor.invokeAll(tasks);

        } catch (RejectedExecutionException e) {
            LOGGER.error("Task executing was rejected.", e);
            throw new UnsupportedOperationException("Unable to submit the task, rejected.", e);
        } catch (InterruptedException e) {
            e.printStackTrace();
            LOGGER.error("Task executing was InterruptedException.", e);
            throw new RuntimeException("Unable to submit the task, InterruptedException.", e);
        }
    }


    public static void getPoolSizeStatus() {
        ThreadPoolExecutor tpe = ((ThreadPoolExecutor) executor);
        int queueSize = tpe.getQueue().size();
        LOGGER.info("当前排队线程数：{}", queueSize);

        int activeCount = tpe.getActiveCount();
        LOGGER.info("当前活动线程数：{}", activeCount);

        long completedTaskCount = tpe.getCompletedTaskCount();
        LOGGER.info("执行完成线程数：{}", completedTaskCount);

        long taskCount = tpe.getTaskCount();
        LOGGER.info("总线程数：{}", taskCount);

    }

    public static ExecutorService getThreadPool() {
        return executor;
    }

}
