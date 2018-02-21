package com.github.luecy1.androidstudyapp.util;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;

import com.github.luecy1.androidstudyapp.testing.SingleFragmentActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

/**
 * Created by you on 2018/02/22.
 */
@RunWith(AndroidJUnit4.class)
// TODO 実行できない、週末に回す
public class AutoClearedValueTest {

    @Rule
    public ActivityTestRule<SingleFragmentActivity> activityRule =
            new ActivityTestRule<SingleFragmentActivity>(SingleFragmentActivity.class, true, true);

    private TestFragment testFragment;

    @Before
    public void init() {
        testFragment = new TestFragment();
        activityRule.getActivity().setFragment(testFragment);
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
    }

    @Test
    public void clearOnReplace() {
        testFragment.testValue = new AutoClearedValue<>(testFragment, "foo");
        activityRule.getActivity().replaceFragment(new TestFragment());
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        assertThat(testFragment.testValue.get(), nullValue());
    }

    private static class TestFragment extends Fragment {
        AutoClearedValue<String> testValue;
    }
}
