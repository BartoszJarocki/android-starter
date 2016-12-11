package com.starter.network;

import com.starter.data.model.Contributor;
import java.util.List;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface Api {
    @GET("/repos/{owner}/{repo}/contributors")
    Observable<Response<List<Contributor>>> contributors(@Path("owner") String owner,
        @Path("repo") String repo);
}