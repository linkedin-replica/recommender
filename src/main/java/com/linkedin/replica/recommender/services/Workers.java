package com.linkedin.replica.recommender.services;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.linkedin.replica.recommender.utils.Configuration;

public class Workers {
    private ExecutorService executor;
    private static volatile Workers instance;

    private Workers() throws IOException {
        // initialize executor pool to a FixedThreadPool with the number of threads configured
        executor = Executors.newFixedThreadPool(Integer.parseInt(Configuration.getInstance().getAppConfig("app.max_thread_count")));
    }

    public static Workers getInstance() throws IOException {
        if (instance == null) {
            synchronized (Workers.class) {
                if (instance == null)
                    instance = new Workers();
            }
        }
        return instance;
    }

    public void setNumThreads(int newLimit) {
        ((ThreadPoolExecutor) executor).setCorePoolSize(newLimit); //newLimit is new size of the pool
    }

    public void submit(Runnable runnable) {
        executor.submit(runnable);
    }
}