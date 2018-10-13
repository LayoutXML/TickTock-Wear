package com.layoutxml.tickingsound;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.wearable.activity.WearableActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends WearableActivity {

    private MediaPlayer mediaPlayer;
    private Boolean isPlaying;
    private SharedPreferences sharedPreferences;
    private Button button;
    private ImageView buttonIcon;
    private ConstraintLayout constraintLayout;
    private TextView volumeText;
    private Integer maxVolume = 11;
    private Integer currentVolume = 6;
    private Button volumeDown;
    private Button volumeUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer  = new MediaPlayer();

        sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences),Context.MODE_PRIVATE);
        isPlaying = sharedPreferences.getBoolean(getString(R.string.isPlayingKey_preference),false);
        currentVolume = sharedPreferences.getInt(getString(R.string.volume_preference),6);

        button = findViewById(R.id.button_background);
        buttonIcon = findViewById(R.id.button_icon);
        constraintLayout = findViewById(R.id.constraintLayout);
        volumeText = findViewById(R.id.volumeText);
        volumeDown = findViewById(R.id.volumeDown);
        volumeUp = findViewById(R.id.volumeUp);

        if (isPlaying || getIntent().getBooleanExtra("fromBoot",false))
            stopTicking(false);

        if (isPlaying) {
            buttonIcon.setImageDrawable(getDrawable(R.drawable.ic_pause));
        } else {
            buttonIcon.setImageDrawable(getDrawable(R.drawable.ic_play));
        }

        changeVolume();

        volumeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentVolume++;
                changeVolume();
            }
        });

        volumeDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentVolume--;
                changeVolume();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    stopTicking(false);
                } else {
                    startTicking();
                }
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        constraintLayout.setPadding(0,0,0,displayMetrics.heightPixels/2-32);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTicking(true);
    }

    private void stopTicking(Boolean forced) {
        if (isPlaying && forced) {
            Toast.makeText(this,"To resume sound, minimize the app instead of closing it",Toast.LENGTH_LONG).show();
        }
        mediaPlayer.stop();
        if (forced)
            mediaPlayer.release();
        buttonIcon.setImageDrawable(getDrawable(R.drawable.ic_play));
        isPlaying = false;
        sharedPreferences.edit().putBoolean(getString(R.string.isPlayingKey_preference),isPlaying).apply();
    }

    private void startTicking() {
        mediaPlayer = MediaPlayer.create(this,R.raw.ticking_sound);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        changeVolume();
        buttonIcon.setImageDrawable(getDrawable(R.drawable.ic_pause));
        isPlaying = !isPlaying;
        sharedPreferences.edit().putBoolean(getString(R.string.isPlayingKey_preference),isPlaying).apply();
    }

    private void changeVolume() {
        if (currentVolume>=1 && currentVolume<=11) {
            float newVolume = (float) (Math.log(maxVolume - currentVolume) / Math.log(maxVolume));
            mediaPlayer.setVolume(1 - newVolume, 1 - newVolume);
            updateVolumeText();
        } else {
            if (currentVolume<=1)
                currentVolume=1;
            else
                currentVolume=11;
        }
        sharedPreferences.edit().putInt(getString(R.string.volume_preference),currentVolume).apply();
    }

    private void updateVolumeText() {
        String current = (currentVolume-1<10) ? "0"+(currentVolume-1) : (currentVolume-1)+"";
        volumeText.setText("Volume: "+current+"/"+(maxVolume-1));
        if (currentVolume.equals(maxVolume)) {
            volumeUp.setBackgroundResource(R.drawable.ic_circle_grayscale);
            volumeDown.setBackgroundResource(R.drawable.ic_circle);
        } else if (currentVolume.equals(1)) {
            volumeUp.setBackgroundResource(R.drawable.ic_circle);
            volumeDown.setBackgroundResource(R.drawable.ic_circle_grayscale);
        } else {
            volumeUp.setBackgroundResource(R.drawable.ic_circle);
            volumeDown.setBackgroundResource(R.drawable.ic_circle);
        }
    }
}
