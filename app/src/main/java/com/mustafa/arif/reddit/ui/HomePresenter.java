package com.mustafa.arif.reddit.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.widget.SwipeRefreshLayout;

import com.mustafa.arif.reddit.R;
import com.mustafa.arif.reddit.backend.CommunicationChecker;
import com.mustafa.arif.reddit.backend.RedditCommunicator;
import com.mustafa.arif.reddit.backend.model.ChildData;
import com.mustafa.arif.reddit.backend.model.Children;
import com.mustafa.arif.reddit.backend.model.RedditTop;
import com.mustafa.arif.reddit.base.BasePresenter;
import com.mustafa.arif.reddit.base.BaseView;
import com.mustafa.arif.reddit.recycler.RecycleAdapter;
import com.mustafa.arif.reddit.recycler.RecyclerViewListener;

import java.net.URL;
import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by musta on 2/18/2018.
 */

public class HomePresenter extends BasePresenter<HomePresenter.View> {
    private CommunicationChecker communicationChecker;
    private RedditCommunicator redditCommunicator;
    private ArrayList<Children> children;
    private RecycleAdapter recycleAdapter;


    private String after;
    private static final String TAG = "ERROR";
    private static final int LIMIT = 50;

    @Inject
    public HomePresenter(CommunicationChecker communicationChecker, RedditCommunicator redditCommunicator) {
        this.communicationChecker = communicationChecker;
        this.redditCommunicator = redditCommunicator;
        recycleAdapter = new RecycleAdapter();
    }

    /**
     * Proivde ArrayList<Children>
     *
     * @return children
     */
    public ArrayList<Children> getChildren() {
        return children;
    }

    /**
     * Proivde current item id, for reference call to get next item.
     *
     * @return after
     */
    public String getAfter() {
        return after;
    }

    /**
     * This method is called, when onRestoreInstanceState is called on activity, and passed the
     * param.
     *
     * @param savedInstanceState
     */
    public void onRestore(Bundle savedInstanceState) {
        ArrayList<Children> children = savedInstanceState.<Children>getParcelableArrayList("key");
        if (children == null) {
            return;
        }
        this.children = children;
        recyclerViewUpdater(this.children, savedInstanceState.getString("after"), false);
    }

    /**
     * This method provide listener for on refresh.
     *
     * @return onRefreshListener
     */
    public SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
        return onRefreshListener;
    }

    /**
     * This method provide click item listener.
     *
     * @return recyclerViewListener
     */
    public RecyclerViewListener getRecyclerViewListener() {
        return recyclerViewListener;
    }

    /**
     * This method start communication with restFull API, if internet is available.
     */
    public void getData() {
        if (!getView().checkPermissionForReadExtertalStorage()) {
            return;
        }
        if (children == null) {
            ArrayList<Children> tempChild = new ArrayList<>();
            ChildData childData = new ChildData();
            childData.setTitle("pull to refresh");
            childData.setNum_comments(-1);
            Children children = new Children();
            children.setData(childData);
            tempChild.add(children);
            getView().setRecycleAdapter(recycleAdapter, tempChild);
            getTop();
        } else {
            getView().updateRecyclerAdapter(recycleAdapter, children);
        }
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener =
            new SwipeRefreshLayout.OnRefreshListener() {

                @Override
                public void onRefresh() {
                    callRefresh();
                }
            };
    private RecyclerViewListener recyclerViewListener = new RecyclerViewListener() {

        @Override
        public void onClick(android.view.View v, int position) {
            String thumbnail = children.get(position).getData().getThumbnail();
            if (thumbnail != null && !thumbnail.isEmpty() && isValid(thumbnail))
                getView().openBrowser(children.get(position).getData().getUrl());
        }

        @Override
        public void onBottom() {
            getView().showProgressBar(true);
            //checking if recycler is not empty
            if (children != null && children.size() >= 10) getTop();
        }
    };

    private void getTop() {
        if (children != null && children.size() >= LIMIT &&
                communicationChecker.isNetworkAvailable()) {
            getView().showProgressBar(false);
            getView().toastMessage(R.string.limit);
        } else if (communicationChecker.isNetworkAvailable()) {
            redditCommunicator.getTop(10, 10, after).enqueue(new Callback<RedditTop>() {
                @Override
                public void onResponse(Call<RedditTop> call, Response<RedditTop> response) {
                    recyclerViewUpdater(response.body().getData().getChildren(),
                            response.body().getData().getAfter(), children != null);
                }

                @Override
                public void onFailure(Call<RedditTop> call, Throwable t) {
                    getView().showProgressBar(false);
                }
            });
        } else {
            getView().showProgressBar(false);
            getView().toastMessage(R.string.no_internet);
        }
    }

    private void callRefresh() {
        if (communicationChecker.isNetworkAvailable() && getView()
                .checkPermissionForReadExtertalStorage()) {
            redditCommunicator.getTop(10, 10, "").enqueue(new Callback<RedditTop>() {
                @Override
                public void onResponse(Call<RedditTop> call, Response<RedditTop> response) {
                    recyclerViewUpdater(response.body().getData().getChildren(),
                            response.body().getData().getAfter(), false);
                }

                @Override
                public void onFailure(Call<RedditTop> call, Throwable t) {
                    getView().showProgressBar(false);
                }
            });
        } else if (!getView().checkPermissionForReadExtertalStorage()) {


        } else {
            getView().showProgressBar(false);
            getView().toastMessage(R.string.no_internet);
        }
    }

    private void recyclerViewUpdater(ArrayList<Children> children, String after, Boolean update) {
        if (update) {
            this.children.addAll(children);
            getView().updateRecyclerAdapter(recycleAdapter, this.children);
        } else {
            this.children = children;
            getView().setRecycleAdapter(recycleAdapter, this.children);
        }
        this.after = after;
        getView().showProgressBar(false);
    }

    @Override
    protected HomePresenter.View getView() {
        if (view == null) {
            return new HomePresenter.View() {

                @Override
                public void setRecycleAdapter(RecycleAdapter recycleAdapter, ArrayList<Children> children) {

                }

                @Override
                public void updateRecyclerAdapter(RecycleAdapter recycleAdapter, ArrayList<Children> children) {

                }

                @Override
                public void showProgressBar(boolean show) {

                }

                @Override
                public boolean checkPermissionForReadExtertalStorage() {
                    return false;
                }

                @Override
                public void openBrowser(@NonNull String url) {

                }

                @Override
                public void showSingleAlertDialog(int titleStringId, int messageStringId, int positiveButtonStringId, @Nullable Runnable onPositiveButtonClicked, int negativeButtonStringId, @Nullable Runnable onNegativeButtonClicked) {

                }

                @Override
                public void hideAlertDialog() {

                }

                @Override
                public void finishCurrentActivity() {

                }

                @Override
                public void showStoragePermissionSetting() {

                }

                @Override
                public void showStoragePermissionRuntime() {

                }

                @Override
                public void toastMessage(@StringRes int message) {

                }
            };
        } else {
            return view;
        }
    }

    interface View extends BaseView {
        /**
         * This method is utilzed to attach recyclerAdapter and children array to the recyclerView
         *
         * @param recycleAdapter adapter to attach
         * @param children       children arraylist to attach recycler adapter.
         */
        void setRecycleAdapter(RecycleAdapter recycleAdapter, ArrayList<Children> children);

        /**
         * This method is utilized to update recyclerView adapter and array.
         *
         * @param recycleAdapter adapter to update recyclerView
         * @param children       arraylist to update  recyclerView
         */
        void updateRecyclerAdapter(RecycleAdapter recycleAdapter, ArrayList<Children> children);

        /**
         * Control showing/hidden of progress bar
         *
         * @param show true if show,else false
         */
        void showProgressBar(boolean show);

        boolean checkPermissionForReadExtertalStorage();
    }
    /* Returns true if url is valid */
    public static boolean isValid(String url) {
        /* Try creating a valid URL */
        try {
            new URL(url).toURI();
            return true;
        }

        // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return false;
        }
    }

}