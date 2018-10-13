package com.layoutxml.tickingsound;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.support.wearable.activity.WearableActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends WearableActivity {

    private MediaPlayer mediaPlayer;
    private Boolean isPlaying;
    private SharedPreferences sharedPreferences;
    private Integer maxVolume = 11;
    private Integer currentVolume = 6;
    private List<ActivityOption> values = new ArrayList<>();
    private SettingsListAdapter mAdapter;
    //Elements
    private Button button;
    private ImageView buttonIcon;
    private ConstraintLayout constraintLayout;
    private TextView volumeText;
    private Button volumeDown;
    private Button volumeUp;
    private RecyclerView recyclerView;

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
        recyclerView = findViewById(R.id.settings_list);

        if (isPlaying || getIntent().getBooleanExtra("fromBoot",false))
            stopTicking(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SettingsListAdapter();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

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

        generateSettingsListValues();
    }

    private void generateSettingsListValues() {
        ActivityOption activityOption = new ActivityOption();
        activityOption.setName("Element 1");
        values.add(activityOption);

        activityOption = new ActivityOption();
        activityOption.setName("Element 2");
        values.add(activityOption);

        activityOption = new ActivityOption();
        activityOption.setName("Element 3");
        values.add(activityOption);


        mAdapter.notifyDataSetChanged();
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

    public class SettingsListAdapter extends RecyclerView.Adapter<SettingsListAdapter.MyViewHolder>{

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView name;

            MyViewHolder(View view) {
                super(view);
                name = view.findViewById(R.id.textView);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition(); // gets item position
                        Intent intent = new Intent(MainActivity.this, values.get(position).getActivity());
                        intent.putExtra("Activity",values.get(position).getExtra());
                        MainActivity.this.startActivity(intent);
                    }
                });
            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.textview_item,parent,false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            ActivityOption activityOption = values.get(position);
            holder.name.setText(activityOption.getName());
        }

        @Override
        public int getItemCount() {
            return values.size();
        }

    }
}
