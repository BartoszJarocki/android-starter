package com.starter.ui.contributors;

import com.starter.data.model.Contributor;
import com.starter.ui.base.mvp.core.MvpView;
import java.util.List;

public interface ContributorsView extends MvpView {
    void showProgress(boolean pullToRefresh);

    void showContributors(List<Contributor> contributors);

    void showEmpty();

    void showError(final String error);
}
