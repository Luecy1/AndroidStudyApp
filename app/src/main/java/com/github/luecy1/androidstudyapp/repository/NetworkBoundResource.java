package com.github.luecy1.androidstudyapp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.github.luecy1.androidstudyapp.AppExecutors;
import com.github.luecy1.androidstudyapp.vo.Resource;

/**
 * Created by you on 2018/02/02.
 */

public abstract class NetworkBoundResource<ResultType, RequestType> {

    private final AppExecutors appExecutors;

    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    @MainThread
    NetworkBoundResource(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        result.setValue(Resource.loading(null));
//        LiveData<RequestType> dbSource =
//        result.addSource();
        // TODO
    }

    @NonNull
    @MainThread
    protected abstract LiveData<RequestType> loadFromDb();
}
