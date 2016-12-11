package com.starter;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import com.starter.di.AppComponent;
import com.starter.di.AppModule;
import com.starter.di.DaggerAppComponent;
import com.starter.network.di.ApiModule;
import com.starter.network.di.NetModule;
import timber.log.Timber;

public class StarterApplication extends Application {
    private static Context context;

    protected AppComponent appComponent;
    protected AppModule appModule;

    public static StarterApplication get() {
        return (StarterApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        StarterApplication.context = getApplicationContext();

        initTimber();
        initGraph();

        enableStrictMode();
    }

    private void initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    public void initGraph() {
        appModule = new AppModule(this);
        appComponent = DaggerAppComponent.builder()
            .appModule(appModule)
            .netModule(new NetModule(BuildConfig.API_BASE_URL))
            .apiModule(new ApiModule())
            .build();

        appComponent.inject(this);
    }

    private void enableStrictMode() {
        StrictMode.ThreadPolicy policy =
            new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build();
        StrictMode.setThreadPolicy(policy);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    /**
     * A tree which logs important information for crash reporting.
     */
    private static class CrashReportingTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            /*Crashlytics.log(priority, tag, message);

            if (t != null && priority == Log.ERROR) {
                Crashlytics.logException(t);
            }*/
        }
    }
}