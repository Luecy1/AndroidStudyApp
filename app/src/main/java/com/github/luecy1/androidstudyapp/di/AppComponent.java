package com.github.luecy1.androidstudyapp.di;

import android.app.Application;

import com.github.luecy1.androidstudyapp.GithubApp;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

/**
 * Created by you on 2018/01/29.
 */

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        MainActivityModule.class
})

public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance Builder application(Application application);
        AppComponent build();
    }
    void inject(GithubApp githubApp);
}
