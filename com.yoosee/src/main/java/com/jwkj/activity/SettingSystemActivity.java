package com.jwkj.activity;

import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yoosee.R;
import com.jwkj.data.SharedPreferencesManager;
import com.jwkj.data.SystemDataManager;
import com.jwkj.global.Constants;
import com.jwkj.global.NpcCommon;

public class SettingSystemActivity extends BaseActivity implements
        OnClickListener {
    ImageView back_btn;
    RelativeLayout c_vibrate_btn, c_mute_btn, a_vibrate_btn, a_mute_btn;
    RelativeLayout notify_icon_btn, auto_start_btn;
    ImageView notify_icon_img, auto_start_icon_img;
    ImageView c_vibrate_img, c_mute_img, a_vibrate_img, a_mute_img;
    RelativeLayout set_commingRing_btn, set_allarmRing_btn, alarm_set_btn;
    TextView selectedCRing, selectedARing;
    boolean myreceiverIsReg = false;
    public static final int SET_TYPE_COMMING_RING = 0;
    public static final int SET_TYPE_ALLARM_RING = 1;
    private Context mContext;
    public static final String ACTION_CHANGEBELL = "com.jwkj.changebell";
    MyReceiver receiver;
    int c_vibrateState;
    int a_vibrateState;
    int c_muteState;
    int a_muteState;
    int cRingType;
    int aRingType;
    boolean isShowNotify;
    boolean isAutoStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_set);
        c_vibrateState = SharedPreferencesManager.getInstance()
                .getCVibrateState(this);
        c_muteState = SharedPreferencesManager.getInstance()
                .getCMuteState(this);
        a_vibrateState = SharedPreferencesManager.getInstance()
                .getAVibrateState(this);
        a_muteState = SharedPreferencesManager.getInstance()
                .getAMuteState(this);
        isShowNotify = SharedPreferencesManager.getInstance().getIsShowNotify(
                this);
        isAutoStart = SharedPreferencesManager.getInstance().getIsAutoStart(
                this, NpcCommon.mThreeNum);
        initCompent();
        initBtnState();
        initSelectMusicName();
        mContext = this;
        registerMonitor();
    }

    public void initCompent() {
        back_btn = (ImageView) findViewById(R.id.back_btn);
        c_vibrate_btn = (RelativeLayout) findViewById(R.id.c_vibrate_btn);
        c_mute_btn = (RelativeLayout) findViewById(R.id.c_mute_btn);
        a_vibrate_btn = (RelativeLayout) findViewById(R.id.a_vibrate_btn);
        a_mute_btn = (RelativeLayout) findViewById(R.id.a_mute_btn);

        c_vibrate_img = (ImageView) findViewById(R.id.c_vibrate_img);
        c_mute_img = (ImageView) findViewById(R.id.c_mute_img);
        a_vibrate_img = (ImageView) findViewById(R.id.a_vibrate_img);
        a_mute_img = (ImageView) findViewById(R.id.a_mute_img);

        set_commingRing_btn = (RelativeLayout) findViewById(R.id.set_commingRing);
        set_allarmRing_btn = (RelativeLayout) findViewById(R.id.set_allarmRing);
        selectedCRing = (TextView) findViewById(R.id.selectedCommingRing);
        selectedARing = (TextView) findViewById(R.id.selectAllarmRing);

        notify_icon_btn = (RelativeLayout) findViewById(R.id.notify_icon_btn);
        notify_icon_img = (ImageView) findViewById(R.id.notify_icon_img);

        auto_start_btn = (RelativeLayout) findViewById(R.id.auto_start_btn);
        auto_start_icon_img = (ImageView) findViewById(R.id.auto_start_icon_img);

        alarm_set_btn = (RelativeLayout) findViewById(R.id.alarm_set_btn);

        auto_start_btn.setOnClickListener(this);
        alarm_set_btn.setOnClickListener(this);
        notify_icon_btn.setOnClickListener(this);
        c_vibrate_btn.setOnClickListener(this);
        c_mute_btn.setOnClickListener(this);
        a_vibrate_btn.setOnClickListener(this);
        a_mute_btn.setOnClickListener(this);
        set_commingRing_btn.setOnClickListener(this);
        set_allarmRing_btn.setOnClickListener(this);
        back_btn.setOnClickListener(this);
    }

    public void initBtnState() {
        if (c_vibrateState == 0) {
            c_vibrate_img.setImageResource(R.drawable.check_off);
        } else {
            c_vibrate_img.setImageResource(R.drawable.check_on);
        }

        if (c_muteState == 0) {
            c_mute_img.setImageResource(R.drawable.check_off);
        } else {
            c_mute_img.setImageResource(R.drawable.check_on);
        }

        if (a_vibrateState == 0) {
            a_vibrate_img.setImageResource(R.drawable.check_off);
        } else {
            a_vibrate_img.setImageResource(R.drawable.check_on);
        }

        if (a_muteState == 0) {
            a_mute_img.setImageResource(R.drawable.check_off);
        } else {
            a_mute_img.setImageResource(R.drawable.check_on);
        }

        if (isShowNotify) {
            notify_icon_img.setImageResource(R.drawable.ic_checkbox_on);
        } else {
            notify_icon_img.setImageResource(R.drawable.ic_checkbox_off);
        }

        if (isAutoStart) {
            auto_start_icon_img.setImageResource(R.drawable.ic_checkbox_on);
        } else {
            auto_start_icon_img.setImageResource(R.drawable.ic_checkbox_off);
        }
    }

    // 加载选择的音乐文字
    public void initSelectMusicName() {
        int cbellType = SharedPreferencesManager.getInstance().getCBellType(
                this);
        if (cbellType == SharedPreferencesManager.TYPE_BELL_SYS) {
            int bellId = SharedPreferencesManager.getInstance()
                    .getCSystemBellId(this);
            HashMap<String, String> data = SystemDataManager.getInstance()
                    .findSystemBellById(this, bellId);
            if (null != data) {
                selectedCRing.setText(data.get("bellName"));
            }
        } else {
            int bellId = SharedPreferencesManager.getInstance().getCSdBellId(
                    this);
            HashMap<String, String> data = SystemDataManager.getInstance()
                    .findSdBellById(this, bellId);
            if (null != data) {
                selectedCRing.setText(data.get("bellName"));
            }
        }

        int abellType = SharedPreferencesManager.getInstance().getABellType(
                this);
        if (abellType == SharedPreferencesManager.TYPE_BELL_SYS) {
            int bellId = SharedPreferencesManager.getInstance()
                    .getASystemBellId(this);
            HashMap<String, String> data = SystemDataManager.getInstance()
                    .findSystemBellById(this, bellId);
            if (null != data) {
                selectedARing.setText(data.get("bellName"));
            }
        } else {
            int bellId = SharedPreferencesManager.getInstance().getASdBellId(
                    this);
            HashMap<String, String> data = SystemDataManager.getInstance()
                    .findSdBellById(this, bellId);
            if (null != data) {
                selectedARing.setText(data.get("bellName"));
            }
        }
    }

    public void registerMonitor() {
        myreceiverIsReg = true;
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_CHANGEBELL);
        receiver = new MyReceiver();
        this.registerReceiver(receiver, filter);
    }

    public void changeCVibrate() {
        if (c_vibrateState == 0) {
            c_vibrate_img.setImageResource(R.drawable.check_on);
            c_vibrateState = 1;
            SharedPreferencesManager.getInstance().putCVibrateState(
                    c_vibrateState, this);
        } else {
            c_vibrate_img.setImageResource(R.drawable.check_off);
            c_vibrateState = 0;
            SharedPreferencesManager.getInstance().putCVibrateState(
                    c_vibrateState, this);
        }
    }

    public void changeCMute() {
        if (c_muteState == 0) {
            c_mute_img.setImageResource(R.drawable.check_on);
            c_muteState = 1;
            SharedPreferencesManager.getInstance().putCMuteState(c_muteState,
                    this);
        } else {
            c_mute_img.setImageResource(R.drawable.check_off);
            c_muteState = 0;
            SharedPreferencesManager.getInstance().putCMuteState(c_muteState,
                    this);
        }
    }

    public void changeAVibrate() {
        if (a_vibrateState == 0) {
            a_vibrate_img.setImageResource(R.drawable.check_on);
            a_vibrateState = 1;
            SharedPreferencesManager.getInstance().putAVibrateState(
                    a_vibrateState, this);
        } else {
            a_vibrate_img.setImageResource(R.drawable.check_off);
            a_vibrateState = 0;
            SharedPreferencesManager.getInstance().putAVibrateState(
                    a_vibrateState, this);
        }
    }

    public void changeAMute() {
        if (a_muteState == 0) {
            a_mute_img.setImageResource(R.drawable.check_on);
            a_muteState = 1;
            SharedPreferencesManager.getInstance().putAMuteState(a_muteState,
                    this);
        } else {
            a_mute_img.setImageResource(R.drawable.check_off);
            a_muteState = 0;
            SharedPreferencesManager.getInstance().putAMuteState(a_muteState,
                    this);
        }
    }

    public void changeIsShowNotifyIcon() {
        if (isShowNotify) {
            isShowNotify = false;
            notify_icon_img.setImageResource(R.drawable.ic_checkbox_off);
            SharedPreferencesManager.getInstance().putIsShowNotify(this,
                    isShowNotify);
        } else {
            isShowNotify = true;
            notify_icon_img.setImageResource(R.drawable.ic_checkbox_on);
            SharedPreferencesManager.getInstance().putIsShowNotify(this,
                    isShowNotify);
        }
    }

    public void changeIsAutoStartIcon() {
        if (isAutoStart) {
            isAutoStart = false;
            auto_start_icon_img.setImageResource(R.drawable.ic_checkbox_off);
            SharedPreferencesManager.getInstance().putIsAutoStart(this,
                    isAutoStart);
        } else {
            isAutoStart = true;
            auto_start_icon_img.setImageResource(R.drawable.ic_checkbox_on);
            SharedPreferencesManager.getInstance().putIsAutoStart(this,
                    isAutoStart);
        }
    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(
                    SettingSystemActivity.ACTION_CHANGEBELL)) {
                initSelectMusicName();
            }
        }

    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        int resId = view.getId();
        if (resId == R.id.back_btn) {
            this.finish();
        } else if (resId == R.id.c_vibrate_btn) {
            changeCVibrate();
        } else if (resId == R.id.set_commingRing) {
            Intent set_comming_bellRing = new Intent(this,
                    SettingBellRingActivity.class);
            set_comming_bellRing.putExtra("type", SET_TYPE_COMMING_RING);
            startActivity(set_comming_bellRing);
        } else if (resId == R.id.c_mute_btn) {
            changeCMute();
        } else if (resId == R.id.a_vibrate_btn) {
            changeAVibrate();
        } else if (resId == R.id.notify_icon_btn) {
            changeIsShowNotifyIcon();
        } else if (resId == R.id.auto_start_btn) {
            changeIsAutoStartIcon();
        } else if (resId == R.id.set_allarmRing) {
            Intent set_allarm_bellRing = new Intent(this,
                    SettingBellRingActivity.class);
            set_allarm_bellRing.putExtra("type", SET_TYPE_ALLARM_RING);
            startActivity(set_allarm_bellRing);
        } else if (resId == R.id.a_mute_btn) {
            changeAMute();
        } else if (resId == R.id.alarm_set_btn) {
            Intent go_alarm_set = new Intent(this, AlarmSetActivity.class);
            startActivity(go_alarm_set);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myreceiverIsReg) {
            this.unregisterReceiver(receiver);
        }
    }

    @Override
    public int getActivityInfo() {
        // TODO Auto-generated method stub
        return Constants.ActivityInfo.ACTIVITY_SETTINGSYSTEMACTIVITY;
    }
}
