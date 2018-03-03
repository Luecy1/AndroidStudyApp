package com.github.luecy1.androidstudyapp.db;

import android.support.test.runner.AndroidJUnit4;

import com.github.luecy1.androidstudyapp.TestUtil;
import com.github.luecy1.androidstudyapp.vo.User;

import org.junit.Test;
import org.junit.runner.RunWith;

import static com.github.luecy1.androidstudyapp.LiveDataTestUtil.getValue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by you on 2018/02/17.
 */
@RunWith(AndroidJUnit4.class)
public class UserDaoTest extends DbTest {

    @Test
    public void insertAndLoad() throws InterruptedException {
        final User user = TestUtil.createUser("foo");
        db.userDao().insert(user);

        final User loaded = getValue(db.userDao().findByLogin(user.login));
        assertThat(loaded.login, is("foo"));

        final User replacement = TestUtil.createUser("foo2");
        db.userDao().insert(replacement);

        final User loadedReplacement = getValue(db.userDao().findByLogin("foo2"));
        assertThat(loadedReplacement.login, is("foo2"));
    }
}
