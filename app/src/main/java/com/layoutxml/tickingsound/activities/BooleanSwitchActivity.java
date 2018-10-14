package com.layoutxml.tickingsound.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.support.wear.widget.WearableRecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.layoutxml.tickingsound.R;
import com.layoutxml.tickingsound.objects.BooleanOption;

import java.util.ArrayList;
import java.util.List;

public class BooleanSwitchActivity extends Activity {
    private List<BooleanOption> values = new ArrayList<>();
    private MiscOptionsAdapter mAdapter;
    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTheme(R.style.MainStyle);
        setContentView(R.layout.wearablerecyclerview_activity);

        prefs = this.getSharedPreferences(getString(R.string.sharedPreferences), Context.MODE_PRIVATE);

        WearableRecyclerView mWearableRecyclerView = findViewById(R.id.wearable_recycler_view);
        mWearableRecyclerView.setLayoutManager(new WearableLinearLayoutManager(this));
        mWearableRecyclerView.setEdgeItemsCenteringEnabled(true);

        mAdapter = new MiscOptionsAdapter();
        mWearableRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mWearableRecyclerView.setAdapter(mAdapter);

        if (getIntent().getStringExtra("Activity").equals("charging"))
            generateValues();
    }

    private void generateValues(){
        BooleanOption option = new BooleanOption();
        option.setName("Play while charging");
        option.setKey(getString(R.string.charging_preference));
        option.setDefaultValue(true);
        values.add(option);

        option = new BooleanOption();
        option.setName("Play while not charging");
        option.setKey(getString(R.string.notcharging_preference));
        option.setDefaultValue(true);
        values.add(option);

        mAdapter.notifyDataSetChanged();
    }

    public class MiscOptionsAdapter extends RecyclerView.Adapter<MiscOptionsAdapter.MyViewHolder>{

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView name;
            Switch switcher;

            MyViewHolder(View view) {
                super(view);
                name = view.findViewById(R.id.miscoptionsListTextView);
                switcher = view.findViewById(R.id.miscoptionsListSwitch);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition(); // gets item position
                        BooleanOption selectedMenuItem = values.get(position);
                        prefs.edit().putBoolean(selectedMenuItem.getKey(),!selectedMenuItem.getValue()).apply();
                        selectedMenuItem.setValue(!selectedMenuItem.getValue());
                        switcher.setChecked(selectedMenuItem.getValue());
                    }
                });
            }
        }

        @NonNull
        @Override
        public MiscOptionsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.switch_and_textview_item,parent,false);
            return new MiscOptionsAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MiscOptionsAdapter.MyViewHolder holder, int position) {
            BooleanOption option = values.get(position);
            holder.name.setText(option.getName());
            Boolean shouldBeOn = prefs.getBoolean(option.getKey(),option.getDefaultValue());
            holder.switcher.setChecked(shouldBeOn);
            option.setValue(shouldBeOn);
        }

        @Override
        public int getItemCount() {
            return values.size();
        }
    }
}
