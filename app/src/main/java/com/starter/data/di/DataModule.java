package com.starter.data.di;

import android.app.Application;
import android.support.annotation.NonNull;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.starter.BuildConfig;
import com.starter.data.AppRepository;
import com.starter.data.local.LocalRepository;
import com.starter.data.local.model.ContributorEntity;
import com.starter.data.local.model.MyObjectBox;
import com.starter.data.remote.GithubApi;
import com.starter.data.remote.RemoteRepository;
import com.starter.data.remote.converters.ToStringConverterFactory;
import com.starter.utils.ThreadConfiguration;
import dagger.Module;
import dagger.Provides;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import java.io.File;
import javax.inject.Singleton;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class DataModule {
    private static final int HTTP_DISK_CACHE_SIZE = 100 * 1024 * 1024; // 100MB
    private static final String HTTP_CACHE_DIR = "cache_http";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

    private final String apiBaseUrl;

    public DataModule(final String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat(DATE_FORMAT);

        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(Application application) {
        final OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();

        // Install an HTTP cache in the application cache directory.
        File cacheDir = new File(application.getCacheDir(), HTTP_CACHE_DIR);
        Cache cache = new Cache(cacheDir, HTTP_DISK_CACHE_SIZE);

        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        httpBuilder.addInterceptor(interceptor);
        httpBuilder.cache(cache);

        if (BuildConfig.DEBUG) {
            httpBuilder.addNetworkInterceptor(new StethoInterceptor());
        }

        return httpBuilder.build();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient client, Gson gson) {
        return new Retrofit.Builder().baseUrl(apiBaseUrl)
            .client(client)
            .addConverterFactory(new ToStringConverterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();
    }

    @Provides
    @Singleton
    public BoxStore providesObjectBox(Application application) {
        return MyObjectBox.builder().androidContext(application).build();
    }

    @Provides
    @Singleton
    public Box<ContributorEntity> providesContributorsObjectBox(BoxStore boxStore) {
        return boxStore.boxFor(ContributorEntity.class);
    }

    @Provides
    @Singleton
    public GithubApi providesGithubApi(Retrofit retrofit) {
        return retrofit.create(GithubApi.class);
    }

    @Provides
    @NonNull
    @Singleton
    public LocalRepository providesLocalRepository(Box<ContributorEntity> localContributorBox,
        ThreadConfiguration threadConfiguration) {
        return new LocalRepository(localContributorBox, threadConfiguration);
    }

    @Provides
    @Singleton
    public RemoteRepository providesRemoteRepository(LocalRepository localRepository,
        GithubApi githubApi, ThreadConfiguration threadConfiguration) {
        return new RemoteRepository(localRepository, githubApi, threadConfiguration);
    }

    @Provides
    @Singleton
    public AppRepository provideAppRepository(RemoteRepository remoteRepository,
        LocalRepository localRepository) {
        return new AppRepository(localRepository, remoteRepository);
    }
}
