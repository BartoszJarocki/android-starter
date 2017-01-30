package com.starter.data.local;

import com.starter.data.Repository;
import com.starter.data.local.model.ContributorEntity;
import com.starter.data.model.Contributor;
import com.starter.data.model.mappers.ContributorsEntitiesToContributorsMapper;
import com.starter.data.model.mappers.ContributorsJsonsToContributorEntitiesMapper;
import com.starter.data.remote.model.ContributorJson;
import com.starter.utils.ThreadConfiguration;
import io.objectbox.Box;
import java.util.List;
import rx.Observable;

public class LocalRepository implements Repository {
    private final Box<ContributorEntity> localContributorBox;
    private final ThreadConfiguration threadConfiguration;
    private final ContributorsJsonsToContributorEntitiesMapper
        contributorsJsonsToContributorEntitiesMapper =
        new ContributorsJsonsToContributorEntitiesMapper();
    private final ContributorsEntitiesToContributorsMapper entitiesToContributorsMapper =
        new ContributorsEntitiesToContributorsMapper();

    public LocalRepository(final Box<ContributorEntity> localContributorBox,
        final ThreadConfiguration threadConfiguration) {
        this.localContributorBox = localContributorBox;
        this.threadConfiguration = threadConfiguration;
    }

    @Override
    public Observable<List<Contributor>> contributors(final String owner, final String repo) {
        return Observable.defer(() -> Observable.just(localContributorBox.getAll()))
            .flatMap(contributorEntities -> Observable.just(
                entitiesToContributorsMapper.map(contributorEntities)))
            .compose(threadConfiguration.applySchedulers());
    }

    public void put(final List<ContributorJson> contributorJsons) {
        localContributorBox.put(contributorsJsonsToContributorEntitiesMapper.map(contributorJsons));
    }
}
