package com.jwkj.activity;

import java.util.ArrayList;
import java.util.List;

import com.yoosee.R;
import com.jwkj.global.Constants;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.widget.NormalDialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.LabeledIntent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RadarAddActivity extends BaseActivity implements OnClickListener {
    private Context mContext;
    String ssid;
    int type;
    int mLocalIp;
    Button bt_next;
    TextView tv_ssid;
    EditText edit_pwd;
    ImageView back_btn;
    boolean bool1, bool2, bool3, bool4;
    private byte mAuthMode;
    private byte AuthModeAutoSwitch = 2;
    private byte AuthModeOpen = 0;
    private byte AuthModeShared = 1;
    private byte AuthModeWPA = 3;
    private byte AuthModeWPA1PSKWPA2PSK = 9;
    private byte AuthModeWPA1WPA2 = 8;
    private byte AuthModeWPA2 = 6;
    private byte AuthModeWPA2PSK = 7;
    private byte AuthModeWPANone = 5;
    private byte AuthModeWPAPSK = 4;
    boolean isRegFilter = false;
    private RelativeLayout rlPwd;
    private boolean isWifiOpen = false;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        mContext = this;
        setContentView(R.layout.activity_radar_add);
        initComponent();
        regFilter();
        currentWifi();
    }

    public void initComponent() {
        tv_ssid = (TextView) findViewById(R.id.tv_ssid);
        edit_pwd = (EditText) findViewById(R.id.edit_pwd);
        back_btn = (ImageView) findViewById(R.id.back_btn);
        bt_next = (Button) findViewById(R.id.next);
        rlPwd = (RelativeLayout) findViewById(R.id.layout_pwd);
        bt_next.setOnClickListener(this);
        back_btn.setOnClickListener(this);
    }

    public void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.Action.RADAR_SET_WIFI_FAILED);
        filter.addAction(Constants.Action.RADAR_SET_WIFI_SUCCESS);
        registerReceiver(br, filter);
        isRegFilter = true;

    }

    BroadcastReceiver br = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(
                    Constants.Action.RADAR_SET_WIFI_FAILED)) {
                NormalDialog dialog = new NormalDialog(mContext);
                dialog.setOnButtonCancelListener(new NormalDialog.OnButtonCancelListener() {
                    @Override
                    public void onClick() {
                        // TODO Auto-generated method stub
                        finish();
                    }
                });
                dialog.showConnectFail();
            } else if (intent.getAction().equals(
                    Constants.Action.RADAR_SET_WIFI_SUCCESS)) {
                finish();
            }
        }
    };

    public void currentWifi() {
        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (!manager.isWifiEnabled())
            return;
        WifiInfo info = manager.getConnectionInfo();
        ssid = info.getSSID();
        mLocalIp = info.getIpAddress();
        Log.e("ssid", ssid);
        List<ScanResult> datas = new ArrayList<ScanResult>();
        if (!manager.isWifiEnabled())
            return;
        manager.startScan();
        datas = manager.getScanResults();
        if (ssid == null) {
            return;
        }
        if (ssid.equals("")) {
            return;
        }
        int a = ssid.charAt(0);
        if (a == 34) {
            ssid = ssid.substring(1, ssid.length() - 1);
        }
        if (!ssid.equals("<unknown ssid>") && !ssid.equals("0x")) {
            tv_ssid.setText(ssid);
        }
        for (int i = 0; i < datas.size(); i++) {
            ScanResult result = datas.get(i);
            if (!result.SSID.equals(ssid)) {
                continue;
            }
            if (Utils.isWifiOpen(result)) {
                type = 0;
                isWifiOpen = true;
                rlPwd.setVisibility(View.GONE);
            } else {
                type = 1;
                isWifiOpen = false;
                rlPwd.setVisibility(View.VISIBLE);
            }
            bool1 = result.capabilities.contains("WPA-PSK");
            bool2 = result.capabilities.contains("WPA2-PSK");
            bool3 = result.capabilities.contains("WPA-EAP");
            bool4 = result.capabilities.contains("WPA2-EAP");
            if (result.capabilities.contains("WEP")) {
                this.mAuthMode = this.AuthModeOpen;
            }
            if ((bool1) && (bool2)) {
                mAuthMode = AuthModeWPA1PSKWPA2PSK;
            } else if (bool2) {
                this.mAuthMode = this.AuthModeWPA2PSK;
            } else if (bool1) {
                this.mAuthMode = this.AuthModeWPAPSK;
            } else if ((bool3) && (bool4)) {
                this.mAuthMode = this.AuthModeWPA1WPA2;
            } else if (bool4) {
                this.mAuthMode = this.AuthModeWPA2;
            } else {
                if (!bool3)
                    break;
                this.mAuthMode = this.AuthModeWPA;
            }

        }

    }

    @Override
    public int getActivityInfo() {
        // TODO Auto-generated method stub
        return Constants.ActivityInfo.ACTIVITY_RARDARADDACTIVITY;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int resId = v.getId();
        if (resId == R.id.next) {
            InputMethodManager manager = (InputMethodManager) getSystemService(mContext.INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.hideSoftInputFromWindow(edit_pwd.getWindowToken(), 0);
            }
            String wifiPwd = edit_pwd.getText().toString();
            if (ssid == null || ssid.equals("")) {
                T.showShort(mContext, R.string.please_choose_wireless);
                return;
            }
            if (ssid.equals("<unknown ssid>")) {
                T.showShort(mContext, R.string.please_choose_wireless);
                return;
            }
            if (!isWifiOpen) {
                if (null == wifiPwd || wifiPwd.length() <= 0
                        && (type == 1 || type == 2)) {
                    T.showShort(mContext, R.string.please_input_wifi_password);
                    return;
                }
            }

            Intent device_network = new Intent(mContext, AddWaitActicity.class);
            device_network.putExtra("ssidname", ssid);
            device_network.putExtra("wifiPwd", wifiPwd);
            device_network.putExtra("type", mAuthMode);
            device_network.putExtra("LocalIp", mLocalIp);
            device_network.putExtra("isNeedSendWifi", true);
            startActivity(device_network);
        } else if (resId == R.id.back_btn) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (isRegFilter == true) {
            unregisterReceiver(br);
            isRegFilter = false;
        }
    }

}
