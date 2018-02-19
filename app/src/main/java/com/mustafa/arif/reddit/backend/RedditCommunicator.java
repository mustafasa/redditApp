package com.mustafa.arif.reddit.backend;

import com.mustafa.arif.reddit.backend.model.RedditTop;

import retrofit2.Call;

/**
 * Created by musta on 2/18/2018.
 */

public interface RedditCommunicator {

    /**
     * Method to call Reddit web api and get top list of events
     * @param limit specify number of item needed per page
     * @param count specify number of count to get in.
     * @param after specify string of last item to get next item after the current.
     * @return
     */
    Call<RedditTop> getTop(int limit, int count, String after);

}
