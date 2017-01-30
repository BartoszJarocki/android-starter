package com.starter.data;

import com.starter.data.model.Contributor;
import java.util.List;
import rx.Observable;

public interface Repository {
    Observable<List<Contributor>> contributors(String owner, String repo);
}