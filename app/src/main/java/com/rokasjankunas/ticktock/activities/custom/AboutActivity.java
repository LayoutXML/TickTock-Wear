package com.rokasjankunas.ticktock.activities.custom;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.rokasjankunas.ticktock.BuildConfig;

public class AboutActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.rokasjankunas.ticktock.R.layout.about_activity);

        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;

        TextView License2 = findViewById(com.rokasjankunas.ticktock.R.id.license2);
        License2.setText("Version name: "+versionName);

        TextView License3 = findViewById(com.rokasjankunas.ticktock.R.id.license3);
        License3.setText("Version code: "+versionCode);
    }
}
