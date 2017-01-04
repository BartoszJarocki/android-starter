package com.starter.data.remote;

import android.support.annotation.NonNull;
import com.starter.data.model.Contributor;
import com.starter.utils.ThreadConfiguration;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Response;
import rx.Observable;

public class RemoteRepository {

    private final ThreadConfiguration threadConfiguration;
    private final GithubApi githubApi;

    @Inject
    public RemoteRepository(@NonNull GithubApi githubApi, @NonNull ThreadConfiguration threadConfiguration) {
        this.githubApi = githubApi;
        this.threadConfiguration = threadConfiguration;
    }

    public Observable<Response<List<Contributor>>> contributors(String owner, String repo) {
        return githubApi.contributors(owner, repo).compose(threadConfiguration.applySchedulers());
    }
}