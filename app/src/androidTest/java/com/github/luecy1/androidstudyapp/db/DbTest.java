package com.github.luecy1.androidstudyapp.db;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;

/**
 * Created by you on 2018/02/17.
 */
abstract class DbTest {
    protected GithubDb db;

    @Before
    public void initDb() {
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                GithubDb.class).build();
    }

    @After
    public void closeDb() {
        db.close();
    }
}
