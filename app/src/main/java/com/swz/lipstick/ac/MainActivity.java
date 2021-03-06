package com.swz.lipstick.ac;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.swz.lipstick.R;
import com.swz.lipstick.monitor.INetInterface;
import com.swz.lipstick.monitor.NetworkMonitor;
import com.swz.lipstick.ui.NetWorkView;
import com.swz.lipstick.utils.X5WebView;

public class MainActivity extends Activity implements INetInterface {

    private static final String TAG = "MainActivity_";
//    private static final String URL = "https://app.ibluesand.cn/app/index.php?i=1&c=entry&eid=25";
    private static final String URL = "https://www.paysapi.com/";

    X5WebView mWb;
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


        findViewById(R.id.btn_about_us).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AboutUsActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        findViewById(R.id.btn_lipstick_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,VideoActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

//
//        WebSettings webSettings = mWb.getSettings();
//        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
//        webSettings.setLoadWithOverviewMode(true);
//        webSettings.setSupportZoom(false);
//        webSettings.setDefaultTextEncodingName("utf-8");
//        webSettings.setLoadsImagesAutomatically(true);
//        webSettings.setJavaScriptEnabled(true);
//
//        mWb.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return super.shouldOverrideUrlLoading(view, url);
//            }
//        });

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

        if(keyCode == KeyEvent.KEYCODE_BACK){
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
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        if(isFinishing()){
            if(mWb != null){
                mWb.destroy();
            }
        }
        super.onStop();
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
