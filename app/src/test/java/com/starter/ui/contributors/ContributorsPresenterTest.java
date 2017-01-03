package com.starter.ui.contributors;

import com.starter.data.model.Contributor;
import com.starter.network.Api;
import com.starter.network.ApiManager;
import com.starter.utils.ThreadConfiguration;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import retrofit2.Response;
import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class ContributorsPresenterTest {

    private static final String REPO = "owner";
    private static final String OWNER = "repo";
    private static final boolean PULL_TO_REFRESH = false;

    @Mock ContributorsView contributorsView;
    @Mock Api api;

    ApiManager apiManager;
    ThreadConfiguration threadConfiguration =
        new ThreadConfiguration(Schedulers.immediate(), Schedulers.immediate());

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        apiManager = new ApiManager(api, threadConfiguration);
    }

    @Test
    public void loadContributors() throws Exception {
        ContributorsPresenter contributorsPresenter = new ContributorsPresenter(apiManager);
        contributorsPresenter.attachView(contributorsView);

        List<Contributor> contributors = getContributors();

        when(api.contributors(OWNER, REPO)).thenReturn(
            Observable.just(Response.success(contributors)));

        contributorsPresenter.loadContributors(OWNER, REPO, PULL_TO_REFRESH);

        verify(contributorsView).showProgress(PULL_TO_REFRESH);
        verify(api).contributors(OWNER, REPO);
        verify(contributorsView).showContributors(contributors);

        verifyNoMoreInteractions(contributorsView);
        verifyNoMoreInteractions(api);
    }

    @Test
    public void loadContributorsEmpty() throws Exception {
        ContributorsPresenter contributorsPresenter = new ContributorsPresenter(apiManager);
        contributorsPresenter.attachView(contributorsView);

        when(api.contributors(OWNER, REPO)).thenReturn(
            Observable.just(Response.success(new ArrayList<>())));

        contributorsPresenter.loadContributors(OWNER, REPO, PULL_TO_REFRESH);

        verify(contributorsView).showProgress(PULL_TO_REFRESH);
        verify(api).contributors(OWNER, REPO);
        verify(contributorsView).showEmpty();

        verifyNoMoreInteractions(contributorsView);
        verifyNoMoreInteractions(api);
    }

    List<Contributor> getContributors() {
        List<Contributor> contributors = new ArrayList<>();
        contributors.add(new Contributor("testuser", 12, "avatar_url"));

        return contributors;
    }
}