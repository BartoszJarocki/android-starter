package com.starter.data;

import com.starter.data.model.Contributor;
import java.util.List;
import retrofit2.Response;
import rx.Observable;

public interface Repository {
    Observable<Response<List<Contributor>>> contributors(String owner, String repo);
}