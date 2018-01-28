package com.github.luecy1.androidstudyapp.binding;

import android.view.View;

/**
 * Created by you on 2018/01/27.
 */

public class BindingAdapters {
    public static void showHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
