package com.starter.data.remote;

import com.starter.data.remote.model.ContributorJson;
import java.util.List;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface GithubApi {
    @GET("/repos/{owner}/{repo}/contributors")
    Observable<Response<List<ContributorJson>>> contributors(@Path("owner") String owner,
        @Path("repo") String repo);
}