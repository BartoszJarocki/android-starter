package com.starter;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.test.espresso.Espresso;
import android.support.test.runner.AndroidJUnitRunner;
import com.starter.util.RxIdlingResource;
import io.appflate.restmock.RESTMockServerStarter;
import io.appflate.restmock.android.AndroidAssetsFileParser;
import io.appflate.restmock.android.AndroidLogger;
import rx.plugins.RxJavaHooks;

public class StarterTestRunner extends AndroidJUnitRunner {
    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
        throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        String testApplicationClassName = TestApplication.class.getCanonicalName();

        return super.newApplication(cl, testApplicationClassName, context);
    }

    @Override
    public void onCreate(Bundle arguments) {
        super.onCreate(arguments);

        RESTMockServerStarter.startSync(new AndroidAssetsFileParser(this.getContext()),
            new AndroidLogger());

        registerRxJavaIdlingResources();
    }

    private void registerRxJavaIdlingResources() {
        RxIdlingResource rxIdlingResource = new RxIdlingResource();

        RxJavaHooks.setOnObservableStart((observable, onSubscribe) -> {
            rxIdlingResource.incrementActiveSubscriptionsCount();
            return onSubscribe;
        });

        RxJavaHooks.setOnObservableReturn(subscription -> {
            rxIdlingResource.decrementActiveSubscriptionsCount();
            return subscription;
        });

        RxJavaHooks.setOnObservableSubscribeError(throwable -> {
            rxIdlingResource.decrementActiveSubscriptionsCount();
            return throwable;
        });

        Espresso.registerIdlingResources(rxIdlingResource);
    }
}