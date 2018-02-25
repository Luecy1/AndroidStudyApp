package com.github.luecy1.androidstudyapp.ui.user;

import android.arch.lifecycle.MutableLiveData;
import android.support.test.rule.ActivityTestRule;

import com.github.luecy1.androidstudyapp.R;
import com.github.luecy1.androidstudyapp.TestUtil;
import com.github.luecy1.androidstudyapp.binding.FragmentBindingAdapters;
import com.github.luecy1.androidstudyapp.testing.SingleFragmentActivity;
import com.github.luecy1.androidstudyapp.ui.common.NavigationController;
import com.github.luecy1.androidstudyapp.util.EspressoTestUtil;
import com.github.luecy1.androidstudyapp.util.ViewModelUtil;
import com.github.luecy1.androidstudyapp.vo.Repo;
import com.github.luecy1.androidstudyapp.vo.Resource;
import com.github.luecy1.androidstudyapp.vo.User;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
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
// TODO
public class UserFragmentTest {

    @Rule
    public ActivityTestRule<SingleFragmentActivity> activityRule =
            new ActivityTestRule<SingleFragmentActivity>(SingleFragmentActivity.class, true, true);

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
        fragment.dataBindingComponent = () -> fragmentBindingAdapters;

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
}
