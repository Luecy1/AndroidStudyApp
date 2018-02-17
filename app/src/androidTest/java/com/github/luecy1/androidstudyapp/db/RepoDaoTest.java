package com.github.luecy1.androidstudyapp.db;

import android.arch.lifecycle.LiveData;
import android.database.sqlite.SQLiteException;
import android.support.test.runner.AndroidJUnit4;

import com.github.luecy1.androidstudyapp.TestUtil;
import com.github.luecy1.androidstudyapp.vo.Contributor;
import com.github.luecy1.androidstudyapp.vo.Repo;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.github.luecy1.androidstudyapp.LiveDataTestUtil.getValue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by you on 2018/02/17.
 */
@RunWith(AndroidJUnit4.class)
public class RepoDaoTest extends DbTest {

    @Test
    public void insertAndRead() throws Exception {
        Repo repo = TestUtil.createRepo("foo", "bar", "desc");
        db.repoDao().insert(repo);
        Repo loaded = getValue(db.repoDao().load("foo", "bar"));

        assertThat(loaded, notNullValue());
        assertThat(loaded.name,is("bar"));
    }

    @Test
    public void insertContributorsWithoutRepo() {
        Repo repo = TestUtil.createRepo("foo", "bar", "desc");
        Contributor contributor = TestUtil.createContributor(repo, "c1", 3);
        try {
            db.repoDao().insertContributes(Collections.singletonList(contributor));
            throw new AssertionError("must fail because does not exit");
        } catch (SQLiteException e) {
        }
    }

    @Test
    public void insertContributors() throws InterruptedException {
        Repo repo = TestUtil.createRepo("foo", "bar", "desc");
        Contributor c1 = TestUtil.createContributor(repo, "c1", 3);
        Contributor c2 = TestUtil.createContributor(repo, "c2", 7);
        db.beginTransaction();
        try {
            db.repoDao().insert(repo);
            db.repoDao().insertContributes(Arrays.asList(c1, c2));
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        List<Contributor> list = getValue(db.repoDao().loadContributors("foo", "bar"));
        assertThat(list.size(), is(2));
        Contributor first = list.get(0);

        assertThat(first.getLogin(), is("c2"));
        assertThat(first.getContributions(), is(7));

        Contributor second = list.get(1);
        assertThat(second.getLogin(), is("c1"));
        assertThat(second.getContributions(), is(3));
    }

    @Test
    public void createIfNotExists_exist() throws InterruptedException {
        Repo repo = TestUtil.createRepo("foo", "bar", "desc");
        db.repoDao().insert(repo);
        assertThat(db.repoDao().createRepoIfNotExists(repo), is(-1L));
    }

    @Test
    public void createIfNotExists_doesNotExist() {
        Repo repo = TestUtil.createRepo("foo", "bar", "desc");
        assertThat(db.repoDao().createRepoIfNotExists(repo), is(1L));
    }

    @Test
    public void insertContributorsThenUpdateRepo() throws InterruptedException {
        Repo repo = TestUtil.createRepo("foo", "bar", "desc");
        db.repoDao().insert(repo);
        Contributor contributor = TestUtil.createContributor(repo, "aa", 3);
        db.repoDao().insertContributes(Collections.singletonList(contributor));
        LiveData<List<Contributor>> data = db.repoDao().loadContributors("foo", "bar");
        assertThat(getValue(data).size(), is(1));

        Repo update = TestUtil.createRepo("foo", "bar", "desc");
        db.repoDao().insert(update);
        data = db.repoDao().loadContributors("foo", "bar");
        assertThat(getValue(data).size(), is(1));
    }
}
