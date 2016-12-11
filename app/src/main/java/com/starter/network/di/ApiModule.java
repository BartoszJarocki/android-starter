package com.starter.network.di;

import com.starter.network.Api;
import com.starter.network.ApiManager;
import com.starter.utils.ThreadConfiguration;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import retrofit2.Retrofit;

@Module
public class ApiModule {
    @Provides
    @Singleton
    public Api providesApi(Retrofit retrofit) {
        return retrofit.create(Api.class);
    }

    @Provides
    @Singleton
    public ApiManager providesApiManager(Api api, ThreadConfiguration threadConfiguration) {
        return new ApiManager(api, threadConfiguration);
    }
}
