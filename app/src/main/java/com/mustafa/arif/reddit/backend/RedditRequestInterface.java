package com.mustafa.arif.reddit.backend;

import com.mustafa.arif.reddit.backend.model.RedditTop;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by musta on 2/18/2018.
 */

public interface RedditRequestInterface {

    @GET(".json")
    Call<RedditTop> getRedditTop(@Query("limit") Integer limit, @Query("count") Integer count, @Query("after") String after);
}
