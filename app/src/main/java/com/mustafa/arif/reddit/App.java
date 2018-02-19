package com.mustafa.arif.reddit;

import android.app.Application;

import com.mustafa.arif.reddit.di.component.DaggerMainComponent;
import com.mustafa.arif.reddit.di.component.MainComponent;
import com.mustafa.arif.reddit.di.module.MainModule;

/**
 * Created by musta on 2/18/2018.
 */

public class App extends Application {
    private MainComponent mc;

    @Override
    public void onCreate() {
        super.onCreate();
        mc = DaggerMainComponent.builder().mainModule(new MainModule(this)).build();
    }

    public MainComponent getActivityComponent() {
        return mc;
    }
}
