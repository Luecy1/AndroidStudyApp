package com.github.luecy1.androidstudyapp.repository;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.github.luecy1.androidstudyapp.api.ApiResponse;
import com.github.luecy1.androidstudyapp.util.CountingAppExecutors;

import org.junit.Rule;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

/**
 * Created by you on 2018/02/11.
 */

// TODO
public class NetworkBoundResourceTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private Function<Foo, Void> saveCallResult;

    private Function<Foo, Boolean> shouldFetch;

    private Function<Void, LiveData<ApiResponse<Foo>>> createCall;

    private MutableLiveData<Foo> dbData = new MutableLiveData<>();

    private NetworkBoundResource<Foo, Foo> networkBoundResource;

    private AtomicBoolean fetchedOnce = new AtomicBoolean(false);
    private CountingAppExecutors countingAppExecutors;
    private final boolean useRealExecutors;

    public NetworkBoundResourceTest(boolean useRealExecutors) {
        this.useRealExecutors = useRealExecutors;
        if (useRealExecutors) {
            countingAppExecutors = new CountingAppExecutors();
        }
    }

    private static class Foo {

        int value;

        public Foo(int value) {
            this.value = value;
        }
    }
}