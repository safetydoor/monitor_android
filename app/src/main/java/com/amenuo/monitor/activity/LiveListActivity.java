package com.amenuo.monitor.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.amenuo.monitor.R;
import com.amenuo.monitor.adapter.LiveAdapter;
import com.amenuo.monitor.model.LiveModel;
import com.amenuo.monitor.view.TitleBar;

public class LiveListActivity extends AppCompatActivity {

    private ListView mlistView;
    private LiveAdapter mLiveAdapter;
    private TitleBar mTitleBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_list);
        mTitleBar = (TitleBar)this.findViewById(R.id.live_list_titlebar);
        mTitleBar.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mlistView = (ListView) this.findViewById(R.id.live_list_listview);
        mLiveAdapter = new LiveAdapter(this);
        mlistView.setAdapter(mLiveAdapter);
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LiveModel liveModel = (LiveModel)mLiveAdapter.getItem(i);
                jumpToPlay(liveModel);
            }
        });
    }

    private void jumpToPlay(LiveModel liveModel){
        Intent intent = new Intent();
        intent.setClass(this, LivePlayerActivity.class);
        intent.putExtra("name", liveModel.getName());
        intent.putExtra("address", liveModel.getAddress());
        startActivity(intent);
    }
}
