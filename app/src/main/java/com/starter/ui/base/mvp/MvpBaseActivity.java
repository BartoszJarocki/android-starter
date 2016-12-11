package com.starter.ui.base.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import com.starter.ui.base.mvp.core.MvpPresenter;
import com.starter.ui.base.mvp.core.MvpView;

public abstract class MvpBaseActivity<V extends MvpView, P extends MvpPresenter<V>>
    extends AppCompatActivity implements MvpView {

    protected P presenter;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        presenter = createPresenter();
        if (presenter == null) {
            throw new NullPointerException("Presenter cannot be null!");
        }

        presenter.attachView(getMvpView());
    }

    /**
     * Instantiate a presenter instance
     *
     * @return The {@link MvpPresenter} for this view
     */
    @NonNull
    public abstract P createPresenter();

    @NonNull
    public V getMvpView() {
        return (V) this;
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }
}
