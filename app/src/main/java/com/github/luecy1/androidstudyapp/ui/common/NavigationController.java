package com.github.luecy1.androidstudyapp.ui.common;


import android.support.v4.app.FragmentManager;

import com.github.luecy1.androidstudyapp.MainActivity;
import com.github.luecy1.androidstudyapp.R;

import javax.inject.Inject;

/**
 * Created by you on 2018/01/28.
 */

public class NavigationController {
    private final int containerId;
    private final FragmentManager fragmentManager;

    @Inject
    public NavigationController(MainActivity mainActivity) {
        this.containerId = R.id.container;
        this.fragmentManager = mainActivity.getSupportFragmentManager();
    }

    public void navigateToSearch() {
//        S
    }

    public void navigateToRepo(String owner, String name) {
        //TODO
    }

    public void navigateToUser(String login) {
        // TODO
    }
}
