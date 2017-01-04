package com.starter.di;

import android.app.Application;
import android.content.SharedPreferences;
import com.starter.data.di.DataModule;
import com.starter.ui.contributors.ContributorsActivity;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = { AppModule.class, DataModule.class })
public interface AppComponent {

    Application getApplication();

    SharedPreferences getSharedPreferences();

    void inject(Application application);

    void inject(ContributorsActivity contributorsActivity);
}
