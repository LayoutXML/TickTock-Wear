package com.layoutxml.tickingsound;

import android.app.Activity;

class ActivityOption {
    private String name, extra;
    private Class activity;

    public ActivityOption() {
        name = "";
        extra = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public Class getActivity() {
        return activity;
    }

    public void setActivity(Class activity) {
        this.activity = activity;
    }
}
