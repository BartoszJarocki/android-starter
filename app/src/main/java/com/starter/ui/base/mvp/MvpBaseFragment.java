package com.starter.ui.base.mvp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import com.starter.ui.base.mvp.core.MvpPresenter;
import com.starter.ui.base.mvp.core.MvpView;

public abstract class MvpBaseFragment<V extends MvpView, P extends MvpPresenter<V>> extends Fragment
    implements MvpView {

    protected P mPresenter;

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter = createPresenter();
        if (mPresenter == null) {
            throw new NullPointerException("Presenter cannot be null!");
        }

        mPresenter.attachView(getMvpView());
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
    public void onDestroyView() {
        mPresenter.detachView();
        super.onDestroyView();
    }
}
