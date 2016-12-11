package com.starter.utils;

import rx.Observable;
import rx.Scheduler;

public class ThreadConfiguration {
    private Scheduler subscribeOnScheduler;
    private Scheduler observeOnScheduler;

    public ThreadConfiguration(final Scheduler subscribeOnScheduler,
        final Scheduler observeOnScheduler) {
        this.subscribeOnScheduler = subscribeOnScheduler;
        this.observeOnScheduler = observeOnScheduler;
    }

    public <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(subscribeOnScheduler)
            .observeOn(observeOnScheduler);
    }
}