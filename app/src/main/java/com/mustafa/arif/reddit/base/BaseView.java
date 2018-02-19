package com.mustafa.arif.reddit.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

/**
 * Interface for p>V communication
 */

public interface BaseView {
    /**
     * Open a default browser app with provided uri.
     *
     * @param url the url address to open.
     */
    void openBrowser(@NonNull String url);

    /**
     * Show a standard alert dialog.
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
    void showSingleAlertDialog(@StringRes int titleStringId, @StringRes int messageStringId,
                               @StringRes int positiveButtonStringId, @Nullable Runnable onPositiveButtonClicked,
                               @StringRes int negativeButtonStringId, @Nullable Runnable onNegativeButtonClicked);

    /**
     * Hide a standard alert dialog.
     */
    void hideAlertDialog();


    /**
     * Finishing an activity where currently shown (foreground) activity.
     */
    public void finishCurrentActivity();

    /**
     * Open storage permission OS setting
     */
    public void showStoragePermissionSetting();

    /**
     * Open storage permission runtime
     */
    public void showStoragePermissionRuntime();

    /**
     * Show standard short toast message
     * @param message
     */
    public void toastMessage(@StringRes int message);

}
