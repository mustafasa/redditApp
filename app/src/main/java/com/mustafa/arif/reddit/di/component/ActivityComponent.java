package com.mustafa.arif.reddit.di.component;

import com.mustafa.arif.reddit.ui.HomeActivity;

import dagger.Subcomponent;

/**
 * Created by musta on 2/18/2018.
 */

@Subcomponent
public interface ActivityComponent {
    void inject(HomeActivity activity);
}
