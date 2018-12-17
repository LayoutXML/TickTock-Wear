package com.rokasjankunas.ticktock.activities.custom;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.rokasjankunas.ticktock.R;

import java.util.Calendar;

public class TimeActivity extends Activity {

    private Integer minMin;
    private Integer minH;
    private Integer maxMin;
    private Integer maxH;
    private SharedPreferences sharedPreferences;
    private EditText minText;
    private EditText maxText;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_activity);

        sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences), Context.MODE_PRIVATE);
        minMin = sharedPreferences.getInt(getString(R.string.minMin_preference), 0);
        minH = sharedPreferences.getInt(getString(R.string.minH_preference), 0);
        maxMin = sharedPreferences.getInt(getString(R.string.maxMin_preference), 0);
        maxH = sharedPreferences.getInt(getString(R.string.maxH_preference), 24);
        if (maxH==24) {
            maxH=0;
        }

        minText = findViewById(R.id.editText);
        maxText = findViewById(R.id.editText2);
        submit = findViewById(R.id.battery_submit);

        minText.setText(((minH > 9) ? minH : "0"+minH) + ":" + ((minMin > 9) ? minMin : "0"+minMin));
        maxText.setText(((maxH > 9) ? maxH : "0"+maxH) + ":" + ((maxMin > 9) ? maxMin : "0"+maxMin));

        minText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(TimeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        minH = selectedHour;
                        minMin = selectedMinute;
                        minText.setText(((minH > 9) ? minH : "0"+minH) + ":" + ((minMin > 9) ? minMin : "0"+minMin));
                    }
                }, minH, minMin, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        maxText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(TimeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        maxH = selectedHour;
                        maxMin = selectedMinute;
                        maxText.setText(((maxH > 9) ? maxH : "0"+maxH) + ":" + ((maxMin > 9) ? maxMin : "0"+maxMin));
                    }
                }, maxH, maxMin, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (maxH==0 && maxMin==0) {
                    maxH=24;
                }
                if ((minH < maxH) || (minH==maxH && minMin<maxMin)) {
                    sharedPreferences.edit().putInt(getString(R.string.minMin_preference), minMin).apply();
                    sharedPreferences.edit().putInt(getString(R.string.minH_preference), minH).apply();
                    sharedPreferences.edit().putInt(getString(R.string.maxMin_preference), maxMin).apply();
                    sharedPreferences.edit().putInt(getString(R.string.maxH_preference), maxH).apply();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"Error: \"From\" not smaller than \"To\"",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }
}
