package com.github.luecy1.androidstudyapp;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by you on 2018/01/30.
 */

@Singleton
public class AppExecutors {

    private final Executor diskIo;

    private final Executor networkIo;

    private final Executor mainThread;

    public AppExecutors(Executor diskIo, Executor networkIo, Executor mainThread) {
        this.diskIo = diskIo;
        this.networkIo = networkIo;
        this.mainThread = mainThread;
    }

    @Inject
    public AppExecutors() {
        this(Executors.newSingleThreadExecutor(),
                Executors.newFixedThreadPool(3),
                new MainThreadExecutor());
    }

    public Executor diskIo() {
        return diskIo;
    }

    public Executor networkIo() {
        return networkIo;
    }

    public Executor mainThread() {
        return mainThread;
    }

    private static class MainThreadExecutor implements  Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
