package com.amenuo.monitor.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

import com.amenuo.monitor.R;
import com.amenuo.monitor.adapter.FollowAdapter;
import com.amenuo.monitor.lib.PinnedHeaderListView;
import com.amenuo.monitor.view.TitleBar;

public class FollowActivity extends ActionBarActivity {

    private TitleBar mTitleBar;
    private PinnedHeaderListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        mTitleBar = (TitleBar)this.findViewById(R.id.follow_titlebar);
        mTitleBar.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mListView = (PinnedHeaderListView)this.findViewById(R.id.follow_listview);
        mListView.setAdapter(new FollowAdapter(this));
    }
}
