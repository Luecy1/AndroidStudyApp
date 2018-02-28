package com.github.luecy1.androidstudyapp.ui.user;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.DataBindingComponent;
import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.github.luecy1.androidstudyapp.R;
import com.github.luecy1.androidstudyapp.TestUtil;
import com.github.luecy1.androidstudyapp.binding.FragmentBindingAdapters;
import com.github.luecy1.androidstudyapp.testing.SingleFragmentActivity;
import com.github.luecy1.androidstudyapp.ui.common.NavigationController;
import com.github.luecy1.androidstudyapp.util.EspressoTestUtil;
import com.github.luecy1.androidstudyapp.util.RecyclerViewMatcher;
import com.github.luecy1.androidstudyapp.util.ViewModelUtil;
import com.github.luecy1.androidstudyapp.vo.Repo;
import com.github.luecy1.androidstudyapp.vo.Resource;
import com.github.luecy1.androidstudyapp.vo.User;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by you on 2018/02/24.
 */
public class UserFragmentTest {

    @Rule
    public ActivityTestRule<SingleFragmentActivity> activityRule =
            new ActivityTestRule<>(SingleFragmentActivity.class, true, true);

    private UserViewModel viewModel;
    private NavigationController navigationController;
    private FragmentBindingAdapters fragmentBindingAdapters;
    private MutableLiveData<Resource<User>> userData = new MutableLiveData<>();
    private MutableLiveData<Resource<List<Repo>>> repoListData = new MutableLiveData<>();

    @Before
    public void init() throws Throwable {
        EspressoTestUtil.disableProgressBarAnimations(activityRule);
        UserFragment fragment = UserFragment.create("foo");
        viewModel = mock(UserViewModel.class);
        when(viewModel.getUser()).thenReturn(userData);
        when(viewModel.getRepositories()).thenReturn(repoListData);
        doNothing().when(viewModel).setLogin(anyString());
        navigationController = mock(NavigationController.class);
        fragmentBindingAdapters = mock(FragmentBindingAdapters.class);

        fragment.viewModelFactory = ViewModelUtil.createFor(viewModel);
        fragment.navigationController = navigationController;
//        fragment.dataBindingComponent = () -> fragmentBindingAdapters;
        fragment.dataBindingComponent = new DataBindingComponent(){
            @Override
            public FragmentBindingAdapters getFragmentBindingAdapters() {
                return fragmentBindingAdapters;
            }
        };

        activityRule.getActivity().setFragment(fragment);
        activityRule.runOnUiThread(() -> fragment.binding.get().repoList.setItemAnimator(null));
    }

    @Test
    public void loading() {
        userData.postValue(Resource.loading(null));
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()));
        onView(withId(R.id.retry)).check(matches(not(isDisplayed())));
    }

    @Test
    public void error() throws InterruptedException {
        doNothing().when(viewModel).retry();
        userData.postValue(Resource.error("wtf", null));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.error_msg)).check(matches(withText("wtf")));
        onView(withId(R.id.retry)).check(matches(isDisplayed()));
        onView(withId(R.id.retry)).perform(click());
        verify(viewModel).retry();
    }

    @Test
    public void loadingWithUser() {
        User user = TestUtil.createUser("foo");
        userData.postValue(Resource.loading(user));
        onView(withId(R.id.name)).check(matches(withText(user.name)));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
    }

    @Test
    public void loadedUser() {
        User user = TestUtil.createUser("foo");
        userData.postValue(Resource.success(user));
        onView(withId(R.id.name)).check(matches(withText(user.name)));
        onView(withId(R.id.progress_bar)).check(matches(not(user.name)));
    }

    @Test
    public void loadRepos() {
        List<Repo> repos = setRepos(2);

        for (int pos = 0; pos < repos.size(); pos++) {
            Repo repo = repos.get(pos);
            onView(listMatcher().atPosition(pos)).check(
                    matches(hasDescendant(withText(repo.name)))
            );
            onView(listMatcher().atPosition(pos)).check(
                    matches(hasDescendant(withText(repo.description)))
            );
            onView(listMatcher().atPosition(pos)).check(
                    matches(hasDescendant(withText("" + repo.stars)))
            );
        }
        Repo repo3 = setRepos(3).get(2);
        onView(listMatcher().atPosition(2)).check(
                matches(hasDescendant(withText(repo3.name)))
        );
    }

    @Test
    public void clickRepo() {
        List<Repo> repos = setRepos(2);
        Repo selected = repos.get(1);
        onView(withText(selected.description)).perform(click());
        verify(navigationController).navigateToRepo(selected.owner.login, selected.name);
    }

    @Test
    public void nullUser() {
        userData.postValue(null);
        onView(withId(R.id.name)).check(matches(not(isDisplayed())));
    }

    @Test
    public void nullRepoList() {
        repoListData.postValue(null);
        onView(listMatcher().atPosition(0)).check(doesNotExist());
    }

    @Test
    public void nulledUser() {
        User user = TestUtil.createUser("foo");
        userData.postValue(Resource.success(user));
        onView(withId(R.id.name)).check(matches(withText(user.name)));
        userData.postValue(null);
        onView(withId(R.id.name)).check(matches(not(isDisplayed())));
    }

    @Test
    public void nulledRepoList() {
        setRepos(5);
        onView(listMatcher().atPosition(1)).check(matches(isDisplayed()));
        repoListData.postValue(null);
        onView(listMatcher().atPosition(0)).check(doesNotExist());
    }

    @NonNull
    private RecyclerViewMatcher listMatcher() {
        return new RecyclerViewMatcher(R.id.repo_list);
    }

    private List<Repo> setRepos(int count) {
        List<Repo> repos = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            repos.add(TestUtil.createRepo("foo", "name" + i, "desc" + i));
        }
        repoListData.postValue(Resource.success(repos));
        return repos;
    }
}
