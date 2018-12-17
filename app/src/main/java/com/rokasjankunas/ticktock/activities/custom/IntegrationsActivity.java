package com.rokasjankunas.ticktock.activities.custom;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.support.wear.widget.WearableRecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.layoutxml.applistmanagerlibrary.AppList;
import com.layoutxml.applistmanagerlibrary.interfaces.AppListener;
import com.layoutxml.applistmanagerlibrary.interfaces.SortListener;
import com.layoutxml.applistmanagerlibrary.objects.AppData;
import com.rokasjankunas.ticktock.activities.BooleanSwitchActivity;

import java.util.ArrayList;
import java.util.List;

public class IntegrationsActivity extends Activity implements AppListener, SortListener {

    private List<AppData> values = new ArrayList<>();
    private IntegrationsAdapter mAdapter;
    private ProgressBar progressBar;
    private List<AppData> promotedIntegrations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.rokasjankunas.ticktock.R.layout.wearablerecyclerview_progressbar_activity);

        WearableRecyclerView mWearableRecyclerView = findViewById(com.rokasjankunas.ticktock.R.id.listView);
        mWearableRecyclerView.setLayoutManager(new WearableLinearLayoutManager(this));
        mWearableRecyclerView.setEdgeItemsCenteringEnabled(true);

        mAdapter = new IntegrationsAdapter(values);
        mWearableRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mWearableRecyclerView.setAdapter(mAdapter);

        String[] permissions = {"com.rokasjankunas.ticktock.AMBIENT_INTERACTIVE_MODE_CHANGE"};

        progressBar = findViewById(com.rokasjankunas.ticktock.R.id.progressBar);
        Toast.makeText(getApplicationContext(),"After changing settings go back to the main screen",Toast.LENGTH_SHORT).show();

        AppList.registerListeners(IntegrationsActivity.this,null,null,null,null,null,IntegrationsActivity.this);
        AppList.getSomeApps(getApplicationContext(),0,false,permissions,true,0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppList.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppList.registerListeners(IntegrationsActivity.this,null,null,null,null,null,IntegrationsActivity.this);
    }

    @Override
    public void appListener(List<AppData> list, Integer integer, Boolean aBoolean, String[] strings, Boolean aBoolean1, Integer integer1) {
        AppList.sort(addPromotedIntegrations(list),AppList.BY_APPNAME_IGNORE_CASE,AppList.IN_ASCENDING,integer1);
    }

    @Override
    public void sortListener(List<AppData> list, Integer integer, Integer integer1, Integer integer2) {
        values.clear();
        values.addAll(list);
        if (integer2==0) {
            progressBar.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        }
    }

    private List<AppData> addPromotedIntegrations(List<AppData> list) {
        generatePromotedIntegrations();
        for (AppData promotedIntegration : promotedIntegrations) {
            if (!list.contains(promotedIntegration)) {
                list.add(promotedIntegration);
            }
        }
        AppData self = new AppData();
        self.setPackageName("com.rokasjankunas.ticktock");
        list.remove(self);
        return list;
    }

    private void generatePromotedIntegrations() {
        promotedIntegrations.clear();

        AppData promotedIntegration = new AppData();
        promotedIntegration.setIcon(getDrawable(com.rokasjankunas.ticktock.R.drawable.ic_twelveish));
        promotedIntegration.setPackageName("com.layoutxml.twelveish");
        promotedIntegration.setName("Twelveish");
        promotedIntegration.setObject(false); //false if app is not installed, null if it is
        promotedIntegrations.add(promotedIntegration);
    }

    public class IntegrationsAdapter extends RecyclerView.Adapter<IntegrationsAdapter.MyViewHolder>{

        private List<AppData> appDataList;

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView name;
            ImageView logo, download;

            MyViewHolder(View v) {
                super(v);
                v.setOnClickListener(this);
                name = v.findViewById(com.rokasjankunas.ticktock.R.id.itemName);
                logo = v.findViewById(com.rokasjankunas.ticktock.R.id.itemLogo);
                download = v.findViewById(com.rokasjankunas.ticktock.R.id.downloadIcon);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition(); // gets item position
                if (values.get(position).getObject()==null) {
                    Intent intent;
                    intent = new Intent(IntegrationsActivity.this, BooleanSwitchActivity.class);
                    intent.putExtra("Activity", "ambient&interactive_modes");
                    intent.putExtra("Package", values.get(position).getPackageName());
                    IntegrationsActivity.this.startActivity(intent);
                } else {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + values.get(position).getPackageName())));
                    } catch (android.content.ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + values.get(position).getPackageName())));
                    }
                }
            }
        }

        IntegrationsAdapter(List<AppData> appDataList) {
            this.appDataList = appDataList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(com.rokasjankunas.ticktock.R.layout.item_view,parent,false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            AppData app = appDataList.get(position);
            holder.name.setText(app.getName());
            holder.logo.setImageDrawable(app.getIcon());
            if (app.getObject()==null) {
                holder.download.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return appDataList.size();
        }
    }
}
