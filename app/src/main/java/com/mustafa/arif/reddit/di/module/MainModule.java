package com.mustafa.arif.reddit.di.module;

import android.content.Context;

import com.mustafa.arif.reddit.backend.CommunicationChecker;
import com.mustafa.arif.reddit.backend.CommunicationCheckerImpl;
import com.mustafa.arif.reddit.backend.RedditCommunicator;
import com.mustafa.arif.reddit.backend.RedditCommunicatorImpl;
import com.mustafa.arif.reddit.backend.RedditRequestInterface;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by musta on 2/18/2018.
 */

@Module
public class MainModule {
    public static final String BASE_URL = "https://www.reddit.com/top/";
    private final Context context;

    public MainModule(Context context) {
        this.context = context;
    }

    @Provides
    public Context context() {
        return context;
    }

    @Provides
    CommunicationChecker getCommunicationChecker(CommunicationCheckerImpl communicationCheckerImpl) {
        return communicationCheckerImpl;
    }

    @Provides
    Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    RedditRequestInterface getRedditRequestInterface(Retrofit retrofit) {
        return retrofit.create(RedditRequestInterface.class);
    }

    @Provides
    RedditCommunicator getRedditCommunicator(RedditCommunicatorImpl redditCommunicatorImpl) {
        return redditCommunicatorImpl;
    }
}
