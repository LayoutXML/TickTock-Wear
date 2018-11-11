package com.rokasjankunas.ticktock.activities.custom;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rokasjankunas.ticktock.R;

public class BatteryPercentageActivity extends Activity {

    private Integer min;
    private Integer max;
    private SharedPreferences sharedPreferences;
    private EditText minText;
    private EditText maxText;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battery_percentage_activity);

        sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences),Context.MODE_PRIVATE);
        min = sharedPreferences.getInt(getString(R.string.minBattery_preference),0);
        max = sharedPreferences.getInt(getString(R.string.maxBattery_preference),100);

        minText = findViewById(R.id.batteryMin);
        maxText = findViewById(R.id.batteryMax);
        submit = findViewById(R.id.battery_submit);

        minText.setText(min+"");
        maxText.setText(max+"");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newMin = Integer.parseInt(minText.getText().toString());
                int newMax = Integer.parseInt(maxText.getText().toString());
                if (newMin<0) {
                    Toast.makeText(getApplicationContext(),"Error: Min value below 0",Toast.LENGTH_SHORT).show();
                    finish();
                } else
                if (newMin>100) {
                    Toast.makeText(getApplicationContext(),"Error: Min value over 100",Toast.LENGTH_SHORT).show();
                    finish();
                } else
                if (newMax>100) {
                    Toast.makeText(getApplicationContext(),"Error: Max value over 100",Toast.LENGTH_SHORT).show();
                    finish();
                } else
                if (newMax<0) {
                    Toast.makeText(getApplicationContext(),"Error: Min value below 0",Toast.LENGTH_SHORT).show();
                    finish();
                } else
                if (newMin>newMax) {
                    Toast.makeText(getApplicationContext(),"Error: Min value higher than max value",Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    sharedPreferences.edit().putInt(getString(R.string.minBattery_preference),newMin).apply();
                    sharedPreferences.edit().putInt(getString(R.string.maxBattery_preference),newMax).apply();
                    Toast.makeText(getApplicationContext(),newMin+" and "+newMax+" set",Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });

    }
}
