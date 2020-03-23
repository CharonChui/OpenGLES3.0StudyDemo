package com.charon.opengles30studydemo;

import android.app.Activity;

public class MainBean {
    private String name;
    private Class<? extends Activity> startClazz;

    public MainBean(String name, Class<? extends Activity> startClazz) {
        this.name = name;
        this.startClazz = startClazz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<? extends Activity> getStartClazz() {
        return startClazz;
    }

    public void setStartClazz(Class<? extends Activity> startClazz) {
        this.startClazz = startClazz;
    }
}
