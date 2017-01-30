package com.starter.data.remote;

import android.support.annotation.NonNull;
import com.starter.data.Repository;
import com.starter.data.local.LocalRepository;
import com.starter.data.model.Contributor;
import com.starter.data.model.mappers.ContributorsJsonsToContributorListMapper;
import com.starter.utils.ThreadConfiguration;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class RemoteRepository implements Repository {

    private final LocalRepository localRepository;
    private final ThreadConfiguration threadConfiguration;
    private final GithubApi githubApi;
    private final ContributorsJsonsToContributorListMapper
        contributorsJsonsToContributorListMapper = new ContributorsJsonsToContributorListMapper();

    @Inject
    public RemoteRepository(final LocalRepository localRepository, @NonNull GithubApi githubApi,
        @NonNull ThreadConfiguration threadConfiguration) {
        this.localRepository = localRepository;
        this.githubApi = githubApi;
        this.threadConfiguration = threadConfiguration;
    }

    public Observable<List<Contributor>> contributors(String owner, String repo) {
        return githubApi.contributors(owner, repo)
            .flatMap(response -> {
                if (response.isSuccessful()) {
                    return Observable.just(response.body());
                } else {
                    return Observable.error(new RuntimeException());
                }
            })
            .doOnNext(localRepository::put)
            .flatMap(contributors -> Observable.just(
                contributorsJsonsToContributorListMapper.map(contributors)))
            .onErrorResumeNext(throwable -> {
                if (throwable instanceof IOException) { // network errors
                    return Observable.empty(); // show local data!
                }

                return Observable.error(throwable);
            })
            .compose(threadConfiguration.applySchedulers());
    }
}