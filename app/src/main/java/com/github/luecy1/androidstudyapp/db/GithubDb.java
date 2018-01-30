package com.github.luecy1.androidstudyapp.db;

import android.arch.persistence.room.RoomDatabase;

/**
 * Created by you on 2018/01/30.
 */
//@Database(entities )
public abstract class GithubDb extends RoomDatabase {

    abstract public UserDao userDao();

    abstract public RepoDao repoDao();
}
