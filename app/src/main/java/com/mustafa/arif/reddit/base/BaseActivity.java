package com.mustafa.arif.reddit.base;

/**
 * Created by musta on 2/18/2018.
 */

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.net.URL;

import javax.inject.Inject;

import static com.mustafa.arif.reddit.ui.HomePresenter.isValid;

/**
 * Base activity for all the activities which use presenters (descendants of the {@link
 * BasePresenter}).
 *
 * @param <P> Class of a concrete presenter used by a concrete activity of the type V.
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {
    private Dialog singleDialog;

    @Inject
    protected P presenter;

    /**
     * The method to configureViewElements specific controls.
     */
    protected abstract void configureViewElements(@Nullable Bundle savedInstanceState);

    /**
     * The method for concrete activities to perform DI injection into itself (included ancestors).
     */
    protected abstract void injectDependencies();

    @Override
    protected void onStart() {
        super.onStart();
        presenter.bind(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.unbind();
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
        configureViewElements(savedInstanceState);
    }

    @Override
    public void openBrowser(@NonNull String url) {
        if (!isValid(url)) return;

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);

    }

    /**
     * Show a single standard alert dialog. If while showing previous dialog it will try to show
     * second single dialog, then previous dialog will be closed.
     *
     * @param titleStringId           Title text resource ID, if 0 - no title is needed.
     * @param messageStringId         Message text resource ID, if 0 - no message needed.
     * @param positiveButtonStringId  Positive button text resource ID, if 0 - no positive button is
     *                                needed.
     * @param onPositiveButtonClicked Positive button click callback, if null - no callback needed.
     * @param negativeButtonStringId  Negative button text resource ID, if 0 - no positive button is
     *                                needed.
     * @param onNegativeButtonClicked Positive button click callback, if null - no callback needed.
     */
    public void showSingleAlertDialog(@StringRes final int titleStringId,
                                      @StringRes final int messageStringId,
                                      @StringRes final int positiveButtonStringId,
                                      @Nullable final Runnable onPositiveButtonClicked,
                                      @StringRes final int negativeButtonStringId,
                                      @Nullable final Runnable onNegativeButtonClicked) {
        if (null != singleDialog && singleDialog.isShowing()) {
            singleDialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        if (titleStringId != 0) {
            builder.setTitle(titleStringId);
        }
        if (messageStringId != 0) {
            builder.setMessage(messageStringId);
        }
        if (positiveButtonStringId != 0) {
            builder.setPositiveButton(
                    positiveButtonStringId,
                    new OnDialogButtonClickListenerRunner(onPositiveButtonClicked));
        }
        if (negativeButtonStringId != 0) {
            builder.setNegativeButton(
                    negativeButtonStringId,
                    new OnDialogButtonClickListenerRunner(onNegativeButtonClicked));
        }
        singleDialog = builder.show();
    }

    @Override
    public void hideAlertDialog() {

        singleDialog.dismiss();
    }

    @Override
    public void finishCurrentActivity() {

    }

    @Override
    public void toastMessage(@StringRes int message) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getApplicationContext(), message, duration);
        toast.show();
    }

    @Override
    public void showStoragePermissionSetting() {
        Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + getApplication().getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        getApplication().startActivity(i);
    }

    @Override
    public void showStoragePermissionRuntime() {
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }



    /**
     * Runner to run dialogbox Runnable action.
     */
    private static class OnDialogButtonClickListenerRunner
            implements DialogInterface.OnClickListener {

        private Runnable runnable;

        OnDialogButtonClickListenerRunner(@Nullable final Runnable runnable) {
            this.runnable = runnable;
        }

        @Override
        public void onClick(final DialogInterface dialogInterface, final int i) {
            if (runnable != null) {
                runnable.run();
            }
        }
    }

}
