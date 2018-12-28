package com.swz.lipstick.ac;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.swz.lipstick.monitor.INetInterface;
import com.swz.lipstick.monitor.NetworkMonitor;
import com.swz.lipstick.R;
import com.swz.lipstick.ui.NetWorkView;

public class MainActivity extends Activity implements INetInterface {

    private static final String TAG = "MainActivity_";
    private static final String URL = "https://app.ibluesand.cn/app/index.php?i=29&c=entry&plugin=wap&do=reg&m=junsion_winaward_plugin_wap";

    WebView mWb;
    Button mBtn;
    int mType;
    NetWorkView mErrorPage;
    RelativeLayout mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NetworkMonitor.getInstance();


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

        mType = getIntent().getIntExtra("TYPE", -1);

        if (mType == 1) {
            mBtn.setVisibility(View.GONE);
            mWb.setVisibility(View.VISIBLE);
        } else if (mType == 2) {
            mBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mWb != null) {
                        mWb.reload();
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

        mContent = findViewById(R.id.rl_content);
        mErrorPage = findViewById(R.id.view_net_error);


        if (!NetworkMonitor.isNetworkConnected()) {
            mErrorPage.show();
            mContent.setVisibility(View.INVISIBLE);
        }

        NetworkMonitor.getInstance().registerNetListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWb.canGoBack()) {
            mWb.goBack();
            return true;
        }

        if (mType == 2 && mWb.getVisibility() == View.VISIBLE) {
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


    @Override
    public void connected() {
        Log.i(TAG, "net work connected!");
        if (mErrorPage != null) {
            mErrorPage.hide();
            mContent.setVisibility(View.VISIBLE);
            mWb.reload();
        }
    }

    @Override
    public void disConnected() {
        Log.i(TAG, "net work disConnected!");
        if (mErrorPage != null) {
            mErrorPage.show();
            mContent.setVisibility(View.INVISIBLE);
        }
    }
}
