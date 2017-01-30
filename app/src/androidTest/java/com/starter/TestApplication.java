package com.starter;

import com.starter.data.di.DataModule;
import com.starter.di.AppModule;
import com.starter.di.DaggerAppComponent;
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
            .dataModule(new DataModule(RESTMockServer.getUrl()))
            .build();

        appComponent.inject(this);
    }
}