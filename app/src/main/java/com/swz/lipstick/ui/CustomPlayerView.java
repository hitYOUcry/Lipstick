package com.swz.lipstick.ui;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.exoplayer2.ui.PlayerView;

/**
 * @author nemoqjzhang
 * @date 2018/6/5 19:31.
 */

public class CustomPlayerView extends PlayerView {
	public CustomPlayerView(Context context) {
		this(context, null);
	}

	public CustomPlayerView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setControllerHideOnTouch(true);

	}

}
