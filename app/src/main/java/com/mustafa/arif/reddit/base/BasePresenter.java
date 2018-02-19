package com.mustafa.arif.reddit.base;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.mustafa.arif.reddit.R;

/**
 * Created by musta on 2/18/2018.
 */

public abstract class BasePresenter<V extends BaseView> {

    protected V view;

    /**
     * Binding of the Presenter to its View. After the Presenter is bound to a view, it is free to
     * make calls to the view, including directly from withing this method.
     *
     * @param view A view to bind to.
     */
    @CallSuper
    public void bind(@NonNull final V view) {
        this.view = view;
    }

    /**
     * Unbinding of the Presenter from a View it was previously bound to.
     */
    @CallSuper
    public void unbind() {
        view = null;
    }

    public void handleStoragePermissionDenied() {
        getView().showSingleAlertDialog(R.string.title_storage_permission_denied,
                R.string.description_storage_permission_denied, R.string.button_setting,
                showStoragePermissionSetting(), android.R.string.ok, hideAlertDialogShowRunTime());
    }

    private Runnable hideAlertDialogShowRunTime() {
        return new Runnable() {
            @Override
            public void run() {
                getView().hideAlertDialog();
                getView().showStoragePermissionRuntime();
            }
        };
    }

    private Runnable showStoragePermissionSetting() {
        return new Runnable() {
            @Override
            public void run() {

                getView().showStoragePermissionSetting();
            }
        };
    }

    /**
     * To avoid null time exception when the view is not available, we provide dummyView
     *
     * @return V
     */
    protected abstract V getView();


}