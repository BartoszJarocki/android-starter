package com.starter.data;

import com.starter.data.local.LocalRepository;
import com.starter.data.model.Contributor;
import com.starter.data.remote.RemoteRepository;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class AppRepository implements Repository {

    private final LocalRepository localRepository;
    private final RemoteRepository remoteRepository;

    @Inject
    public AppRepository(final LocalRepository localRepository,
        final RemoteRepository remoteRepository) {
        this.localRepository = localRepository;
        this.remoteRepository = remoteRepository;
    }

    @Override
    public Observable<List<Contributor>> contributors(final String owner, final String repo) {
        return Observable.concat(remoteRepository.contributors(owner, repo),
            localRepository.contributors(owner, repo))
            .first(contributorsResponse -> !contributorsResponse.isEmpty());
    }
}
