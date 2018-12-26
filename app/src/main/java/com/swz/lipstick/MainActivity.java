package com.swz.lipstick;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

public class MainActivity extends Activity {

    private static final String URL = "https://app.ibluesand.cn/app/index.php?i=29&c=entry&plugin=wap&do=reg&m=junsion_winaward_plugin_wap";

    WebView mWb;
    Button mBtn;
    int mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtn = findViewById(R.id.btn_begin);
        mWb = findViewById(R.id.webview);


        WebSettings webSettings = mWb.getSettings();
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(false);
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setLoadsImagesAutomatically(true);

        mWb.requestFocusFromTouch();

        mWb.loadUrl(URL);

        mType = getIntent().getIntExtra("TYPE",-1);

        if(mType == 1){
            mBtn.setVisibility(View.GONE);
            mWb.setVisibility(View.VISIBLE);
        }else if(mType == 2){
            mBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //                mBtn.setVisibility(View.GONE);
                    //
                    if (mWb != null) {
                        mWb.setVisibility(View.VISIBLE);
                    }
                    mBtn.setClickable(false);
                    ObjectAnimator wbAnim = ObjectAnimator.ofFloat(mWb, "alpha", 0f, 1f);
                    wbAnim.setDuration(1000);

                    ObjectAnimator btnAnim = ObjectAnimator.ofFloat(mBtn, "alpha", 1f, 0f);
                    btnAnim.setDuration(1000);

                    wbAnim.start();
                    btnAnim.start();
                }
            });
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWb.canGoBack()) {
            mWb.goBack();
            return true;
        }

        if(mType == 2 && mWb.getVisibility() == View.VISIBLE){
            mBtn.setVisibility(View.VISIBLE);
            mWb.setVisibility(View.INVISIBLE);
            mBtn.setClickable(true);

            ObjectAnimator wbAnim = ObjectAnimator.ofFloat(mWb, "alpha", 1f, 0f);
            wbAnim.setDuration(1000);

            ObjectAnimator btnAnim = ObjectAnimator.ofFloat(mBtn, "alpha", 0f, 1f);
            btnAnim.setDuration(1000);

            wbAnim.start();
            btnAnim.start();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
