package com.layoutxml.tickingsound;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;

public class MainActivity extends WearableActivity {

    private AssetFileDescriptor assetFileDescriptor;
    private FileDescriptor fileDescriptor;
    private MediaPlayer mediaPlayer;
    private Boolean isPlaying;
    private SharedPreferences sharedPreferences;
    private Button button;
    private ImageView buttonIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assetFileDescriptor = getResources().openRawResourceFd(R.raw.ticking_sound);
        fileDescriptor = assetFileDescriptor.getFileDescriptor();
        mediaPlayer  = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(fileDescriptor, assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
        } catch (IOException e) {
            Toast.makeText(this,"Unable to initialize. "+e.toString(),Toast.LENGTH_SHORT).show();
            System.exit(0);
        }
        mediaPlayer.setLooping(true);

        sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences),Context.MODE_PRIVATE);
        isPlaying = sharedPreferences.getBoolean(getString(R.string.isPlayingKey),false);

        button = findViewById(R.id.button_background);
        buttonIcon = findViewById(R.id.button_icon);

        if (isPlaying) {
            buttonIcon.setImageDrawable(getDrawable(R.drawable.ic_pause));
        } else {
            buttonIcon.setImageDrawable(getDrawable(R.drawable.ic_play));
        }

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
        sharedPreferences.edit().putBoolean(getString(R.string.isPlayingKey),isPlaying).apply();
    }

    private void startTicking() {
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            Toast.makeText(this,"Unable to initialize. "+e.toString(),Toast.LENGTH_SHORT).show();
            System.exit(0);
        }
        mediaPlayer.start();
        buttonIcon.setImageDrawable(getDrawable(R.drawable.ic_pause));
        isPlaying = !isPlaying;
        sharedPreferences.edit().putBoolean(getString(R.string.isPlayingKey),isPlaying).apply();
    }
}
