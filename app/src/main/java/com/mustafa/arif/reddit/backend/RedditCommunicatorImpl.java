package com.mustafa.arif.reddit.backend;

import com.mustafa.arif.reddit.backend.model.RedditTop;

import javax.inject.Inject;

import retrofit2.Call;

/**
 * Created by musta on 2/18/2018.
 */

public class RedditCommunicatorImpl implements RedditCommunicator {

    RedditRequestInterface redditRequestInterface;

    @Inject
    public RedditCommunicatorImpl(RedditRequestInterface redditRequestInterface) {
        this.redditRequestInterface = redditRequestInterface;
    }


    @Override
    public Call<RedditTop> getTop(int limit, int count, String after ) {
        return redditRequestInterface.getRedditTop(limit,count,after);
    }
}