package com.starter.data.model.mappers;

import com.starter.data.local.model.ContributorEntity;
import com.starter.data.model.Contributor;
import java.util.ArrayList;
import java.util.List;

public class ContributorsEntitiesToContributorsMapper
    implements Mapper<List<ContributorEntity>, List<Contributor>> {

    @Override
    public List<Contributor> map(final List<ContributorEntity> contributorEntities) {
        List<Contributor> contributors = new ArrayList<>();
        for (ContributorEntity contributor : contributorEntities) {
            contributors.add(new Contributor(contributor));
        }

        return contributors;
    }
}