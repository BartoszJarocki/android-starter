package com.starter.network;

import android.support.annotation.NonNull;
import com.starter.data.model.Contributor;
import com.starter.utils.ThreadConfiguration;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Response;
import rx.Observable;

public class ApiManager {

    private final ThreadConfiguration threadConfiguration;
    private final Api api;

    @Inject
    public ApiManager(@NonNull Api api, @NonNull ThreadConfiguration threadConfiguration) {
        this.api = api;
        this.threadConfiguration = threadConfiguration;
    }

    public Observable<Response<List<Contributor>>> contributors(String owner, String repo) {
        return api.contributors(owner, repo).compose(threadConfiguration.applySchedulers());
    }
}