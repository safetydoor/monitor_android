package com.amenuo.monitor.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.amenuo.monitor.R;
import com.amenuo.monitor.utils.Constants;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WechatLoginActivity extends Activity implements OnClickListener, IWXAPIEventHandler {

    private Button mLoginButton;
    private Button mLoginOtherMethodButton;
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_wechat_login);

        mLoginButton = (Button) findViewById(R.id.login_login);
        mLoginButton.setOnClickListener(this);
        mLoginOtherMethodButton = (Button) findViewById(R.id.login_other_method);
        mLoginOtherMethodButton.setOnClickListener(this);

        api = WXAPIFactory.createWXAPI(this, Constants.WECHAT_APP_ID, false);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (resId == R.id.login_login) {
            api.registerApp(Constants.WECHAT_APP_ID);
        } else if (resId == R.id.login_other_method) {
            Intent intent = new Intent();
            intent.setClass(WechatLoginActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {

    }
}
