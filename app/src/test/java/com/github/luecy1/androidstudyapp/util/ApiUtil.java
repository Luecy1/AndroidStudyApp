package com.github.luecy1.androidstudyapp.util;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.github.luecy1.androidstudyapp.api.ApiResponse;

import retrofit2.Response;

/**
 * Created by you on 2018/02/10.
 */
public class ApiUtil {
    public static <T> LiveData<ApiResponse<T>> successCall(T data) {
        return createCall(Response.success(data));
    }

    public static <T> LiveData<ApiResponse<T>> createCall(Response<T> response) {
        MutableLiveData<ApiResponse<T>> data = new MutableLiveData<>();
        data.setValue(new ApiResponse<T>(response));
        return data;
    }
}
