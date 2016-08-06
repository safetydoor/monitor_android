package com.amenuo.monitor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;


import com.amenuo.monitor.action.SlidingMenuAction;
import com.amenuo.monitor.action.TwiceBackAction;
import com.amenuo.monitor.adapter.MainPageAdapter;
import com.amenuo.monitor.model.LumpModel;
import com.amenuo.monitor.utils.PLog;
import com.amenuo.monitor.lib.HeaderGridView;
import com.amenuo.monitor.view.MainHeaderView;
import com.amenuo.monitor.R;
import com.amenuo.monitor.view.MainLeftView;
import com.amenuo.monitor.view.TitleBar;
import com.jwkj.activity.MainActivity;

public class MainPageActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    private HeaderGridView mGridView;
    private MainHeaderView mMainHeaderView;
    private MainLeftView mMainLeftView;
    private TwiceBackAction mTwiceBack;
    private MainPageAdapter mMainPageAdapter;
    private boolean edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_page);

        final SlidingMenuAction slidingMenuAction = new SlidingMenuAction(this);

        mMainLeftView = new MainLeftView(this);
        mMainLeftView.setOnItemClickListener(new MainLeftView.OnItemClickListener() {
            @Override
            public void onItemClick() {
                slidingMenuAction.toogle();
            }
        });
        mMainHeaderView = new MainHeaderView(this);

        slidingMenuAction.setMenu(mMainLeftView);
        mMainHeaderView.setSlidingMenu(slidingMenuAction.getSlidingMenu());

        mGridView = (HeaderGridView)this.findViewById(R.id.main_gridView);
        mGridView.addHeaderView(mMainHeaderView);
        mMainPageAdapter = new MainPageAdapter(this);
        mGridView.setAdapter(mMainPageAdapter);
        mGridView.setOnItemClickListener(this);
        mGridView.setOnItemLongClickListener(this);

        TitleBar titleBar = (TitleBar)findViewById(R.id.main_titlebar);
        titleBar.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingMenuAction.toogle();
            }
        });

        mTwiceBack = new TwiceBackAction();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMainPageAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mTwiceBack.backPress();
            if (!mTwiceBack.canBack()) {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        PLog.e("posotion:"+position);
        position = position - 2; // headerview占用了2个position

        if (edit){
            edit = false;
            mMainPageAdapter.setEdit(false);
            mMainPageAdapter.notifyDataSetChanged();
            return;
        }
        Intent intent = new Intent();
        if (position == 0){
            intent.setClass(this, MainActivity.class);
        }else if(position == 1){
            intent.setClass(this, LiveListActivity.class);
        }else{
            LumpModel model = (LumpModel) view.getTag();
            if (model != null){
                if (model.getId() == 0){
                    intent.setClass(this, FollowActivity.class);
                }else{
                    intent.setClass(this, WebviewActivity.class);
                    intent.putExtra("name", model.getName());
                    intent.putExtra("address", model.getUrl());
                }
            }
        }
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        edit = true;
        mMainPageAdapter.setEdit(true);
        mMainPageAdapter.notifyDataSetChanged();
        return true;
    }
}
