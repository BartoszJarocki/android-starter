package com.starter.data.model.mappers;

import com.starter.data.model.Contributor;
import com.starter.data.remote.model.ContributorJson;
import java.util.ArrayList;
import java.util.List;

public class ContributorsJsonsToContributorListMapper
    implements Mapper<List<ContributorJson>, List<Contributor>> {

    @Override
    public List<Contributor> map(final List<ContributorJson> contributors) {
        List<Contributor> contributorEntities = new ArrayList<>();
        for (ContributorJson contributor : contributors) {
            contributorEntities.add(new Contributor(contributor));
        }

        return contributorEntities;
    }
}