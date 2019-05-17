package com.webakruti.iot;

import android.app.Application;
import android.content.Intent;



/**
 * Created by DELL on 6/27/2018.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, MyService.class));
    }
}
