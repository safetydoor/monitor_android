package com.amenuo.monitor.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import com.amenuo.monitor.R;
import com.amenuo.monitor.adapter.ExpandFollowAdapter;
import com.amenuo.monitor.manager.LumpCategoryManager;
import com.amenuo.monitor.utils.Constants;
import com.amenuo.monitor.utils.HttpRequest;
import com.amenuo.monitor.utils.PLog;
import com.amenuo.monitor.utils.PToast;
import com.amenuo.monitor.utils.SPHelper;
import com.amenuo.monitor.view.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FollowActivity extends ActionBarActivity {

    private TitleBar mTitleBar;
    private ExpandableListView mListView;
    private ProgressBar mProgressBar;
    private ExpandFollowAdapter mFollowAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        PLog.e("follow create :" + Thread.currentThread().getName());
        mTitleBar = (TitleBar)this.findViewById(R.id.follow_titlebar);
        mTitleBar.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mFollowAdapter = new ExpandFollowAdapter(this);
        mProgressBar = (ProgressBar)this.findViewById(R.id.follow_progress);
        mListView = (ExpandableListView)this.findViewById(R.id.follow_listview);
        mListView.setAdapter(mFollowAdapter);

        expandedListView();
        if (canRequest()){
            requestData();
        }
    }

    private void expandedListView(){
        mListView.setGroupIndicator(null);
        for (int i = 0; i < mFollowAdapter.getGroupCount(); i++) {
            mListView.expandGroup(i);
        }
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // TODO Auto-generated method stub
                return true;
            }
        });
    }

    private Boolean canRequest(){
        long lastRequestTime = SPHelper.getInstance().getLong(Constants.KEY_SP_LUMP_LAST_REQUEST_TIME, 0);
        long currentTime = System.currentTimeMillis();
        int dataSize = LumpCategoryManager.getInstance().getLumpCategoryList().size();
        if (dataSize == 0){
            return true;
        }
        return (currentTime - lastRequestTime) > 24 * 60 * 60 * 1000;
    }

    private void requestData(){
        loading(true);
        HttpRequest.requestLumpCategorys(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading(false);
                        PToast.show("网络连接失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject object = new JSONObject(json);
                    int code = object.getInt("code");
                    if (code != 0) {
                        final String msg = object.getString("msg");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                PToast.show(msg);
                            }
                        });
                    } else {
                        String result = object.getString("result");
                        LumpCategoryManager.getInstance().saveJson(result);
                        SPHelper.getInstance().setLong(Constants.KEY_SP_LUMP_LAST_REQUEST_TIME , System.currentTimeMillis());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mFollowAdapter.notifyDataSetChanged();
                                expandedListView();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading(false);
                    }
                });
            }
        });
    }

    private void loading(Boolean loading){
        if (loading){
            mProgressBar.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }else{
            mProgressBar.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }
    }
}
