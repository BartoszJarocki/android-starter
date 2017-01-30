package com.starter;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import com.squareup.leakcanary.LeakCanary;
import com.starter.data.di.DataModule;
import com.starter.di.AppComponent;
import com.starter.di.AppModule;
import com.starter.di.DaggerAppComponent;
import timber.log.Timber;

public class StarterApplication extends Application {
    @SuppressLint("StaticFieldLeak")
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
        initLeakCannary();
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
            .dataModule(new DataModule(BuildConfig.API_BASE_URL))
            .build();

        appComponent.inject(this);
    }

    public void initLeakCannary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
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