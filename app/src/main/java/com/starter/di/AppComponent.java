package com.starter.di;

import android.app.Application;
import android.content.SharedPreferences;
import com.starter.network.di.ApiModule;
import com.starter.network.di.NetModule;
import com.starter.ui.contributors.ContributorsActivity;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = { AppModule.class, NetModule.class, ApiModule.class })
public interface AppComponent {

    Application getApplication();

    SharedPreferences getSharedPreferences();

    void inject(Application application);

    void inject(ContributorsActivity contributorsActivity);
}
