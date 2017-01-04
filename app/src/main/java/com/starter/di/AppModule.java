package com.starter.di;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.starter.data.preferences.ApplicationPreferences;
import com.starter.utils.ThreadConfiguration;
import dagger.Module;
import dagger.Provides;
import java.io.File;
import javax.inject.Singleton;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Module
public class AppModule {
    private static final String IMAGE_CACHE_DIR = "cache_images";
    private static final int IMAGE_DISK_CACHE_SIZE = 100 * 1024 * 1024; // 100MB
    private final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    Picasso providePicasso(final Application app) {
        final OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();

        File cacheDir = new File(app.getCacheDir(), IMAGE_CACHE_DIR);
        httpBuilder.cache(new Cache(cacheDir, IMAGE_DISK_CACHE_SIZE));
        final OkHttp3Downloader okHttpDownloader = new OkHttp3Downloader(httpBuilder.build());

        return new Picasso.Builder(app).downloader(okHttpDownloader).build();
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Application app) {
        return PreferenceManager.getDefaultSharedPreferences(app);
    }

    @Provides
    @Singleton
    ThreadConfiguration provideThreadConfiguration() {
        return new ThreadConfiguration(Schedulers.io(), AndroidSchedulers.mainThread());
    }

    @Provides
    @Singleton
    ApplicationPreferences provideApplicationPreferences(SharedPreferences sharedPreferences) {
        return new ApplicationPreferences(sharedPreferences);
    }
}
