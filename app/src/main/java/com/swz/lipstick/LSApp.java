package com.swz.lipstick;

import android.app.Application;

/**
 * @author nemoqjzhang
 * @date 2018/12/28 21:06.
 */
public class LSApp extends Application {

    public static LSApp me;

    @Override
    public void onCreate() {
        super.onCreate();
        me = this;
    }
}
