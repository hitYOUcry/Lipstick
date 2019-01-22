package com.swz.lipstick.ui;

import com.google.android.exoplayer2.DefaultLoadControl;

/**
 * @author nemoqjzhang
 * @date 2018/6/13 15:02.
 */
public class CustomLoadControl extends DefaultLoadControl {

    private static final String TAG = "CustomLoadControl";

    @Override
    public boolean shouldContinueLoading(long bufferedDurationUs, float playbackSpeed) {
        return super.shouldContinueLoading(bufferedDurationUs, playbackSpeed);
    }


}
