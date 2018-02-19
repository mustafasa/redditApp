package com.mustafa.arif.reddit.recycler;

/**
 * Created by musta on 2/18/2018.
 */

import android.view.View;

/**
 * Listener interface to get user event from recyclerView to presenter
 */

public interface RecyclerViewListener {
    /**
     * when user click on item on recyclerView item, then this method is called passing two
     * param.
     *
     * @param view
     * @param position
     */
    void onClick(View view, int position);

    /**
     * When the user scroll recyclerView to bottom of the list, this method is called.
     */
    void onBottom();
}