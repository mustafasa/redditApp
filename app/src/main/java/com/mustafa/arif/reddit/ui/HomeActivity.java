package com.mustafa.arif.reddit.ui;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mustafa.arif.reddit.App;
import com.mustafa.arif.reddit.R;
import com.mustafa.arif.reddit.backend.model.Children;
import com.mustafa.arif.reddit.base.BaseActivity;
import com.mustafa.arif.reddit.recycler.RecycleAdapter;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity<HomePresenter> implements HomePresenter.View {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar progressBar;


    @Override
    protected void injectDependencies() {
        ((App) getApplication()).getActivityComponent().getActivityComponent().inject(this);
    }

    @Override
    protected void configureViewElements(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_home);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = findViewById(R.id.recycleView);
        progressBar = findViewById(R.id.progress_bar);
        recyclerBuilder();
    }


    @Override
    public void setRecycleAdapter(RecycleAdapter recycleAdapter, ArrayList<Children> children) {
        // Initialize and set the RecyclerView Adapter
        recycleAdapter.add(children);
        recycleAdapter.setOnClickListener(presenter.getRecyclerViewListener());
        mRecyclerView.setAdapter(recycleAdapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void updateRecyclerAdapter(RecycleAdapter recycleAdapter, ArrayList<Children> children) {
        recycleAdapter.add(children);
        recycleAdapter.notifyItemInserted(children.size() - 10);
        recycleAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        presenter.onRestore(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("key", presenter.getChildren());
        outState.putString("after", presenter.getAfter());
    }

    private void recyclerBuilder() {
        // set true if your RecyclerView is finite and has fixed size
        mRecyclerView.setHasFixedSize(false);
        // Set the required LayoutManager
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(mRecyclerView.getContext(),
                        mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSwipeRefreshLayout.setOnRefreshListener(presenter.getOnRefreshListener());
    }

    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                presenter.handleStoragePermissionDenied();
            }

        }
        return false;
    }


}
