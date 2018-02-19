package com.mustafa.arif.reddit.di.component;

import com.mustafa.arif.reddit.di.module.MainModule;

import dagger.Component;

/**
 * Created by musta on 2/18/2018.
 */

@Component(modules = {MainModule.class})
public interface MainComponent {

    ActivityComponent getActivityComponent();
}

