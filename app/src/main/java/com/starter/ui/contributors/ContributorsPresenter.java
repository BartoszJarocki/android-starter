package com.starter.ui.contributors;

import com.starter.data.model.Contributor;
import com.starter.network.ApiManager;
import com.starter.ui.base.mvp.core.MvpBasePresenter;
import java.util.List;
import retrofit2.Response;
import rx.Subscriber;
import rx.Subscription;
import timber.log.Timber;

public class ContributorsPresenter extends MvpBasePresenter<ContributorsView> {
    private ApiManager apiManager;
    private Subscription subscription;

    public ContributorsPresenter(final ApiManager apiManager) {
        this.apiManager = apiManager;
    }

    public void loadContributors(String owner, String repo, boolean pullToRefresh) {
        if (isViewAttached()) {
            getView().showProgress(pullToRefresh);
        }

        subscription = apiManager.contributors(owner, repo)
            .subscribe(new Subscriber<Response<List<Contributor>>>() {
                @Override
                public void onCompleted() {
                    Timber.d("Finished loading contributors.");
                }

                @Override
                public void onError(final Throwable e) {
                    if (isViewAttached()) {
                        getView().showError(e.toString());
                    }
                }

                @Override
                public void onNext(final Response<List<Contributor>> listResponse) {
                    if (!isViewAttached()) {
                        return;
                    }

                    if (!listResponse.isSuccessful()) {
                        getView().showError("Something went wrong...");

                        return;
                    }

                    List<Contributor> contributors = listResponse.body();
                    if (contributors != null && !contributors.isEmpty()) {
                        getView().showContributors(listResponse.body());
                    } else {
                        getView().showEmpty();
                    }
                }
            });
    }

    @Override
    public void detachView() {
        super.detachView();

        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
