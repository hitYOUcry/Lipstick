package com.swz.lipstick.ac;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.swz.lipstick.R;

public class AboutUsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        findViewById(R.id.txv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
