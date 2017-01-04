package com.starter;

import com.starter.di.AppModule;
import com.starter.di.DaggerAppComponent;
import com.starter.data.di.ApiModule;
import com.starter.data.di.NetModule;
import io.appflate.restmock.RESTMockServer;

public class TestApplication extends StarterApplication {
    /**
     * Replaces API url with local mock server url.
     */
    @Override
    public void initGraph() {
        appModule = new AppModule(this);
        appComponent = DaggerAppComponent.builder()
            .appModule(appModule)
            .netModule(new NetModule(RESTMockServer.getUrl()))
            .apiModule(new ApiModule())
            .build();

        appComponent.inject(this);
    }
}