package com.webakruti.iot;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000; // 5 sec

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //SharedPreferenceManager.setApplicationContext(SplashActivity.this);

        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                try {

                    /*if (SharedPreferenceManager.getObject() != null) {
                        Intent i = new Intent(SplashActivity.this, CameraListActivity.class);
                        startActivity(i);
                        finish();
                    } else {*/

                                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                                startActivity(i);
                                finish();


                        //}


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
