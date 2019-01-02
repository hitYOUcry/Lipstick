package com.swz.lipstick.ac;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.swz.lipstick.R;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        TextView txv = findViewById(R.id.txv_welcome);
        ObjectAnimator animator = ObjectAnimator.ofFloat(txv, "rotation", 0f, 360f);
        animator.setDuration(1500);

        animator.start();

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                Intent i  = new Intent(SplashActivity.this,MainActivity.class);
                i.putExtra("TYPE",2);

                SplashActivity.this.startActivity(i);

                SplashActivity.this.finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


    }
}
