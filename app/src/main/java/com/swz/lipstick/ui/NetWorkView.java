package com.swz.lipstick.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.swz.lipstick.R;

/**
 * @author nemoqjzhang
 * @date 2018/12/28 21:19.
 */
public class NetWorkView extends RelativeLayout {



    public NetWorkView(Context context) {
        this(context, null);
    }

    public NetWorkView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NetWorkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_net_error, this);
    }

    public void hide() {
        setVisibility(GONE);
    }

    public void show() {
       setVisibility(VISIBLE);
    }
}
