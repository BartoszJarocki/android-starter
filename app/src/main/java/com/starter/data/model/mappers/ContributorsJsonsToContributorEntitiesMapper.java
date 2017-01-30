package com.starter.data.model.mappers;

import com.starter.data.local.model.ContributorEntity;
import com.starter.data.remote.model.ContributorJson;
import java.util.ArrayList;
import java.util.List;

public class ContributorsJsonsToContributorEntitiesMapper
    implements Mapper<List<ContributorJson>, List<ContributorEntity>> {

    @Override
    public List<ContributorEntity> map(final List<ContributorJson> contributors) {
        List<ContributorEntity> contributorEntities = new ArrayList<>();
        for (ContributorJson contributor : contributors) {
            contributorEntities.add(new ContributorEntity(contributor));
        }

        return contributorEntities;
    }
}