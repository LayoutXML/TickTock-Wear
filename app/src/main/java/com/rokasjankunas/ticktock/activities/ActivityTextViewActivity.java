package com.rokasjankunas.ticktock.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.support.wear.widget.WearableRecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rokasjankunas.ticktock.R;
import com.rokasjankunas.ticktock.activities.custom.BatteryPercentageActivity;
import com.rokasjankunas.ticktock.objects.ActivityOption;

import java.util.ArrayList;
import java.util.List;

public class ActivityTextViewActivity extends Activity {

    private List<ActivityOption> values = new ArrayList<>();
    private SettingsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wearablerecyclerview_activity);

        WearableRecyclerView mWearableRecyclerView = findViewById(R.id.wearable_recycler_view);
        mWearableRecyclerView.setLayoutManager(new WearableLinearLayoutManager(this));
        mWearableRecyclerView.setEdgeItemsCenteringEnabled(true);

        mAdapter = new SettingsAdapter();
        mWearableRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mWearableRecyclerView.setAdapter(mAdapter);

        if (getIntent().getStringExtra("Activity").equals("restrictions")) {
            Toast.makeText(getApplicationContext(),"After changing settings go back to the main screen",Toast.LENGTH_SHORT).show();
            generateRestrictionsValues();
        }
    }

    private void generateRestrictionsValues(){
        ActivityOption activityOption = new ActivityOption();
        activityOption.setName("Battery percentage");
        activityOption.setActivity(BatteryPercentageActivity.class);
        activityOption.setExtra("battery_percentage");
        values.add(activityOption);

        activityOption = new ActivityOption();
        activityOption.setName("Charging");
        activityOption.setActivity(BooleanSwitchActivity.class);
        activityOption.setExtra("charging");
        values.add(activityOption);

        mAdapter.notifyDataSetChanged();
    }

    public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.MyViewHolder>{

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView name;

            MyViewHolder(View view) {
                super(view);
                name = view.findViewById(R.id.textView);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition(); // gets item position
                        Intent intent;
                        intent = new Intent(ActivityTextViewActivity.this, values.get(position).getActivity());
                        intent.putExtra("Activity",values.get(position).getExtra());
                        ActivityTextViewActivity.this.startActivity(intent);
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
