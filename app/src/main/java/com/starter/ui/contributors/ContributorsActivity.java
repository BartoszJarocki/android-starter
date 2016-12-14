package com.starter.ui.contributors;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import com.starter.R;
import com.starter.StarterApplication;
import com.starter.data.model.Contributor;
import com.starter.network.ApiManager;
import com.starter.ui.base.OnRecyclerViewItemClickListener;
import com.starter.ui.base.mvp.MvpBaseActivity;
import com.starter.ui.common.widget.BetterViewAnimator;
import java.util.List;
import javax.inject.Inject;

public class ContributorsActivity extends MvpBaseActivity<ContributorsView, ContributorsPresenter>
    implements ContributorsView, OnRecyclerViewItemClickListener<Contributor> {

    @Inject ApiManager apiManager;
    @Inject Picasso picasso;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.progress_view) FrameLayout progressView;
    @BindView(R.id.error_text_view) TextView errorTextView;
    @BindView(R.id.try_again_view) TextView tryAgainView;
    @BindView(R.id.error_view) RelativeLayout errorView;
    @BindView(R.id.empty_title_view) TextView emptyTitleView;
    @BindView(R.id.empty_view) LinearLayout emptyView;
    @BindView(R.id.content_view) RecyclerView recyclerView;
    @BindView(R.id.swipe_to_refresh_view) SwipeRefreshLayout swipeToRefreshView;
    @BindView(R.id.view_animator) BetterViewAnimator viewAnimator;

    private LinearLayoutManager layoutManager;
    private ContributorsAdapter adapter;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        StarterApplication.get().getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contributors);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        setupViews();
    }

    @NonNull
    @Override
    public ContributorsPresenter createPresenter() {
        return new ContributorsPresenter(apiManager);
    }

    private void setupViews() {
        layoutManager = new LinearLayoutManager(this);
        adapter = new ContributorsAdapter(picasso);
        adapter.setOnItemClickListener(this);

        recyclerView.addItemDecoration(
            new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        presenter.loadContributors("square", "retrofit", false);

        swipeToRefreshView.setOnRefreshListener(
            () -> presenter.loadContributors("square", "retrofit", true));
    }

    @Override
    public void showProgress(boolean pullToRefresh) {
        if (pullToRefresh) {
            swipeToRefreshView.setRefreshing(true);
            return;
        }

        viewAnimator.setDisplayedChildId(R.id.progress_view);
    }

    @Override
    public void showContributors(final List<Contributor> contributors) {
        adapter.addAll(contributors);

        swipeToRefreshView.setRefreshing(false);
        viewAnimator.setDisplayedChildId(R.id.swipe_to_refresh_view);
    }

    @Override
    public void showEmpty() {
        viewAnimator.setDisplayedChildId(R.id.empty_view);
    }

    @Override
    public void showError(final String error) {
        viewAnimator.setDisplayedChildId(R.id.error_view);
    }

    @Override
    public void onItemClick(final View view, final Contributor contributor, final int position) {

    }
}
