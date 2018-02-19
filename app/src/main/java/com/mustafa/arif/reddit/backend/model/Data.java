package com.mustafa.arif.reddit.backend.model;

import java.util.ArrayList;

/**
 * Created by musta on 2/18/2018.
 */

public class Data {

    private String after;

    private ArrayList<Children> children;

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public  ArrayList<Children> getChildren() {
        return children;
    }

    public void setChildren( ArrayList<Children> children) {
        this.children = children;
    }

}

