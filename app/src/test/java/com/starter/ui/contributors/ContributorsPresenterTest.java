package com.starter.ui.contributors;

import com.starter.data.AppRepository;
import com.starter.data.local.LocalRepository;
import com.starter.data.model.Contributor;
import com.starter.data.remote.GithubApi;
import com.starter.data.remote.RemoteRepository;
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
    @Mock GithubApi githubApi;

    LocalRepository localRepository;
    RemoteRepository remoteRepository;

    AppRepository appRepository;
    ThreadConfiguration threadConfiguration =
        new ThreadConfiguration(Schedulers.immediate(), Schedulers.immediate());

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        localRepository = new LocalRepository();
        remoteRepository = new RemoteRepository(githubApi, threadConfiguration);
        appRepository = new AppRepository(localRepository, remoteRepository);
    }

    @Test
    public void loadContributors() throws Exception {
        ContributorsPresenter contributorsPresenter = new ContributorsPresenter(appRepository);
        contributorsPresenter.attachView(contributorsView);

        List<Contributor> contributors = getContributors();

        when(githubApi.contributors(OWNER, REPO)).thenReturn(
            Observable.just(Response.success(contributors)));

        contributorsPresenter.loadContributors(OWNER, REPO, PULL_TO_REFRESH);

        verify(contributorsView).showProgress(PULL_TO_REFRESH);
        verify(githubApi).contributors(OWNER, REPO);
        verify(contributorsView).showContributors(contributors);

        verifyNoMoreInteractions(contributorsView);
        verifyNoMoreInteractions(githubApi);
    }

    @Test
    public void loadContributorsEmpty() throws Exception {
        ContributorsPresenter contributorsPresenter = new ContributorsPresenter(appRepository);
        contributorsPresenter.attachView(contributorsView);

        when(githubApi.contributors(OWNER, REPO)).thenReturn(
            Observable.just(Response.success(new ArrayList<>())));

        contributorsPresenter.loadContributors(OWNER, REPO, PULL_TO_REFRESH);

        verify(contributorsView).showProgress(PULL_TO_REFRESH);
        verify(githubApi).contributors(OWNER, REPO);
        verify(contributorsView).showEmpty();

        verifyNoMoreInteractions(contributorsView);
        verifyNoMoreInteractions(githubApi);
    }

    List<Contributor> getContributors() {
        List<Contributor> contributors = new ArrayList<>();
        contributors.add(new Contributor("testuser", 12, "avatar_url"));

        return contributors;
    }
}