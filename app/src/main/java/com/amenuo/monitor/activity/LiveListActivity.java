package com.amenuo.monitor.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amenuo.monitor.R;
import com.amenuo.monitor.adapter.LiveAdapter;
import com.amenuo.monitor.manager.LiveManager;
import com.amenuo.monitor.manager.LumpCategoryManager;
import com.amenuo.monitor.manager.LumpManager;
import com.amenuo.monitor.model.LiveModel;
import com.amenuo.monitor.utils.Constants;
import com.amenuo.monitor.utils.HttpRequest;
import com.amenuo.monitor.utils.PToast;
import com.amenuo.monitor.utils.SPHelper;
import com.amenuo.monitor.view.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LiveListActivity extends AppCompatActivity {

    private ListView mListView;
    private LiveAdapter mLiveAdapter;
    private TitleBar mTitleBar;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_list);
        mTitleBar = (TitleBar) this.findViewById(R.id.live_list_titlebar);
        mTitleBar.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mProgressBar = (ProgressBar) this.findViewById(R.id.live_list_progress);
        mListView = (ListView) this.findViewById(R.id.live_list_listview);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LiveModel liveModel = (LiveModel) mLiveAdapter.getItem(i);
                jumpToPlay(liveModel);
            }
        });
        mLiveAdapter = new LiveAdapter(this);
        mListView.setAdapter(mLiveAdapter);

        if (canRequest()){
            requestData();
        }
    }

    private Boolean canRequest(){
        long lastRequestTime = SPHelper.getInstance().getLong(Constants.KEY_SP_LIVE_LAST_REQUEST_TIME, 0);
        long currentTime = System.currentTimeMillis();
        int dataSize = LiveManager.getInstance().getLiveList().size();
        if (dataSize == 0){
            return true;
        }
        return (currentTime - lastRequestTime) > 24 * 60 * 60 * 1000;
    }

    private void requestData(){
        loading(true);

        HttpRequest.requestLives(new Callback() {
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
                        LiveManager.getInstance().saveJson(result);
                        SPHelper.getInstance().setLong(Constants.KEY_SP_LIVE_LAST_REQUEST_TIME , System.currentTimeMillis());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mLiveAdapter.notifyDataSetChanged();
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

    private void loading(Boolean loading) {
        if (loading) {
            mProgressBar.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }
    }

    private void jumpToPlay(LiveModel liveModel) {
        Intent intent = new Intent();
        intent.setClass(this, LivePlayerActivity.class);
        intent.putExtra("name", liveModel.getName());
        intent.putExtra("address", liveModel.getAddress());
        startActivity(intent);
    }
}
