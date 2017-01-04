package com.starter.data.local;

import com.starter.data.Repository;
import com.starter.data.model.Contributor;
import java.util.List;
import retrofit2.Response;
import rx.Observable;

public class LocalRepository implements Repository {
    @Override
    public Observable<Response<List<Contributor>>> contributors(final String owner,
        final String repo) {
        return Observable.empty();
    }
}
