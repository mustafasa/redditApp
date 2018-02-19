package com.mustafa.arif.reddit.backend;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import javax.inject.Inject;

/**
 * Created by musta on 2/15/2018.
 */

public class CommunicationCheckerImpl implements CommunicationChecker {
    private Context context;

    @Inject
    public CommunicationCheckerImpl(Context context) {
        this.context = context;
    }

    @Override
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
