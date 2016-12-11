package com.starter.ui.base.mvp.core;

/**
 * The base interface for each mvp presenter
 */
public interface MvpPresenter<V extends MvpView> {

    /**
     * Set or attach the view to this presenter
     */
    void attachView(V view);

    /**
     * Will be called if the view has been destroyed. Typically this method will be invoked from
     * <code>Activity.onDestroy()</code> or <code>Fragment.onDestroyView()</code>
     */
    void detachView();
}
