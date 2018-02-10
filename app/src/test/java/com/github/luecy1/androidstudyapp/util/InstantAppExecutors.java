package com.github.luecy1.androidstudyapp.util;

import com.github.luecy1.androidstudyapp.AppExecutors;

import java.util.concurrent.Executor;

/**
 * Created by you on 2018/02/10.
 */

public class InstantAppExecutors extends AppExecutors {

    private static Executor instant = command -> command.run();

    public InstantAppExecutors() {
        super(instant, instant, instant);
    }
}
