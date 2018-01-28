package com.github.luecy1.androidstudyapp.util;

import android.arch.lifecycle.LiveData;

/**
 * Created by you on 2018/01/28.
 */

public class AbsentLiveData extends LiveData {

    private AbsentLiveData() {
        postValue(null);
    }

    public static <T> LiveData<T> create() {
        return new AbsentLiveData();
    }
}
