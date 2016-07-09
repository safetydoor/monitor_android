package com.jwkj.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.jwkj.APdeviceMonitorActivity;
import com.jwkj.CallActivity;
import com.jwkj.P2PConnect;
import com.jwkj.PlayBackListActivity;
import com.jwkj.activity.AddApDeviceActivity;
import com.jwkj.activity.MainControlActivity;
import com.jwkj.activity.ModifyApWifiPwd;
import com.jwkj.data.APContact;
import com.jwkj.data.Contact;
import com.jwkj.data.DataManager;
import com.jwkj.global.AppConfig;
import com.jwkj.global.Constants;
import com.jwkj.global.MyApp;
import com.jwkj.global.NpcCommon;
import com.jwkj.widget.DrableButton;
import com.jwkj.widget.HeaderView;
import com.jwkj.utils.T;
import com.jwkj.utils.WifiUtils;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;
import com.yoosee.R;

public class APModeFrag extends BaseFragment implements OnClickListener {
    private Context mContext;
    private APContact apContact;
    private Contact APContacts;
    private DrableButton btnPlayback, btnSetting, btnSetWifiPwd;
    private DrableButton btnExiteAPMode;
    private DrableButton ivBtnDefence;
    private HeaderView Headicon;
    private ImageButton header_icon_play;
    private Bundle bundle;
    private boolean isRegFilter = false;
    private int defenceState = -1;
    private String callId = "1";
    int callType = 3;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_apmode, container, false);
        mContext = getActivity();

        if (savedInstanceState == null) {
            APContacts = (Contact) getArguments().getSerializable("contact");
        } else {
            APContacts = (Contact) savedInstanceState
                    .getSerializable("contact");
        }
        // 类型转换

        Log.e("dxsData", APContacts.contactId);
        apContact = changeContactToAPContact(APContacts);
        callId = APContacts.contactId;
        APContacts.contactId = "1";
        APContacts.contactPassword = "0";
        P2PHandler.getInstance().getDefenceStates(APContacts.contactId,
                APContacts.contactPassword);
        initUI(view);
        return view;
    }

    public void callDevice() {
        String ipAddress = APContacts.ipadressAddress.getHostAddress();
        String ipFlag = ipAddress.substring(ipAddress.lastIndexOf(".") + 1, ipAddress.length());
        String push_mesg = NpcCommon.mThreeNum
                + ":"
                + mContext.getResources().getString(
                R.string.p2p_call_push_mesg);
        P2PHandler.getInstance().RTSPConnect(NpcCommon.mThreeNum, APContacts.contactPassword, true, 3, callId, ipFlag, push_mesg, ipAddress, AppConfig.VideoMode, rtspHandler, callId);

    }

    private Handler rtspHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Log.e("dxswifi", "rtsp失败");
                    T.showShort(mContext, R.string.conn_fail);
//				reject();
                    break;
                case 1:
                    Log.e("dxswifi", "rtsp成功");
                    P2PConnect.setCurrent_state(2);
                    playReady();
                    break;
            }
        }
    };

    private void playReady() {
        P2PHandler.getInstance().openAudioAndStartPlaying(callType);
        Intent ready = new Intent();
        ready.setAction(Constants.P2P.P2P_READY);
        MyApp.app.sendBroadcast(ready);
    }

    private void initUI(View view) {

        btnPlayback = (DrableButton) view.findViewById(R.id.btn_ap_playback);
        btnSetting = (DrableButton) view.findViewById(R.id.btn_ap_setting);
        btnSetWifiPwd = (DrableButton) view.findViewById(R.id.btn_ap_wifipwd);
        btnExiteAPMode = (DrableButton) view.findViewById(R.id.btn_ap_existe);
        ivBtnDefence = (DrableButton) view.findViewById(R.id.ivbtn_ap_defence);
        Headicon = (HeaderView) view.findViewById(R.id.user_icon);
        header_icon_play = (ImageButton) view.findViewById(R.id.header_icon_play);


        btnPlayback.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        btnSetWifiPwd.setOnClickListener(this);
        btnExiteAPMode.setOnClickListener(this);
        ivBtnDefence.setOnClickListener(this);
        Headicon.setOnClickListener(this);
        header_icon_play.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        regFilter();
        if (Headicon != null) {
            Headicon.updateImage(callId, false, 1);
        }
    }

    public void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.P2P.RET_GET_REMOTE_DEFENCE);
        filter.addAction(Constants.P2P.RET_SET_REMOTE_DEFENCE);
        filter.addAction(Constants.P2P.P2P_READY);
        mContext.registerReceiver(mReceiver, filter);
        isRegFilter = true;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            if (intent.getAction().equals(Constants.P2P.RET_GET_REMOTE_DEFENCE)) {
                String ids = intent.getStringExtra("contactId");
                if (!ids.equals("") && ids.equals(APContacts.contactId)) {
                    defenceState = intent.getIntExtra("state", -1);
                    changeDefence(defenceState);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_SET_REMOTE_DEFENCE)) {
                int result = intent.getIntExtra("state", -1);
                if (result == 0) {
                    if (defenceState == Constants.DefenceState.DEFENCE_STATE_ON) {
                        defenceState = Constants.DefenceState.DEFENCE_STATE_OFF;
                    } else {
                        defenceState = Constants.DefenceState.DEFENCE_STATE_ON;
                    }
                    changeDefence(defenceState);
                }
            }
        }
    };

    void changeDefence(int defencestate) {
        if (defencestate == Constants.DefenceState.DEFENCE_STATE_ON) {
            ivBtnDefence.setImageResource(R.drawable.defence_off, R.drawable.defence_off_p);
        } else {
            ivBtnDefence.setImageResource(R.drawable.defence_on, R.drawable.defence_on_p);
        }

    }

    void setDefence() {
        if (defenceState == Constants.DefenceState.DEFENCE_STATE_ON) {
            P2PHandler.getInstance().setRemoteDefence(APContacts.contactId,
                    APContacts.contactPassword,
                    Constants.P2P_SET.REMOTE_DEFENCE_SET.ALARM_SWITCH_OFF);
        } else if (defenceState == Constants.DefenceState.DEFENCE_STATE_OFF) {
            P2PHandler.getInstance().setRemoteDefence(APContacts.contactId,
                    APContacts.contactPassword,
                    Constants.P2P_SET.REMOTE_DEFENCE_SET.ALARM_SWITCH_ON);
        }
    }

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (resId == R.id.btn_ap_playback) {
            // 回放
            Intent playback = new Intent();
            playback.setClass(mContext, PlayBackListActivity.class);
            playback.putExtra("contact", APContacts);
            mContext.startActivity(playback);
        } else if (resId == R.id.btn_ap_setting) {
            // 设置
            Intent control = new Intent();
            control.setClass(mContext, MainControlActivity.class);
            Contact c = APContacts;
            c.contactId = "1";
            control.putExtra("contact", c);
            control.putExtra("connectType", 1);
            control.putExtra("type", P2PValue.DeviceType.IPC);
            mContext.startActivity(control);
        } else if (resId == R.id.btn_ap_wifipwd) {
            // 设密码
            Intent modifyPwd = new Intent(mContext, ModifyApWifiPwd.class);
            modifyPwd.putExtra("contactId", apContact.contactId);
            startActivity(modifyPwd);
        } else if (resId == R.id.btn_ap_existe) {
            // 退出AP模式
            Intent quite = new Intent();
            quite.setAction(Constants.Action.SEARCH_AP_QUITEAPDEVICE);
            mContext.sendBroadcast(quite);
        } else if (resId == R.id.ivbtn_ap_defence) {
            // 布撤防
            setDefence();
        } else if (resId == R.id.user_icon) {
            Log.e("dxspppwwwddd", "密码--" + APContacts.contactPassword);
            Intent monitor = new Intent();
            monitor.setClass(mContext, CallActivity.class);
            monitor.putExtra("callId", callId);
            monitor.putExtra("contactName", APContacts.contactName);
            monitor.putExtra("password", APContacts.contactPassword);
            monitor.putExtra("isOutCall", true);
            monitor.putExtra("connectType", 1);
            String mark = APContacts.ipadressAddress.getHostAddress();
            monitor.putExtra("ipAddress", mark);
            monitor.putExtra("ipFlag",
                    mark.substring(mark.lastIndexOf(".") + 1, mark.length()));
            monitor.putExtra("type", Constants.P2P_TYPE.P2P_TYPE_MONITOR);
            mContext.startActivity(monitor);
        } else if (resId == R.id.header_icon_play) {
            Log.e("dxspppwwwddd", "密码--" + APContacts.contactPassword);
            Intent monitor1 = new Intent();
            monitor1.setClass(mContext, CallActivity.class);
            monitor1.putExtra("callId", callId);
            monitor1.putExtra("contactName", APContacts.contactName);
            monitor1.putExtra("password", APContacts.contactPassword);
            monitor1.putExtra("isOutCall", true);
            monitor1.putExtra("connectType", 1);
            String mark1 = APContacts.ipadressAddress.getHostAddress();
            monitor1.putExtra("ipAddress", mark1);
            monitor1.putExtra("ipFlag",
                    mark1.substring(mark1.lastIndexOf(".") + 1, mark1.length()));
            monitor1.putExtra("type", Constants.P2P_TYPE.P2P_TYPE_MONITOR);
            mContext.startActivity(monitor1);
        }

    }

    private APContact changeContactToAPContact(Contact contact) {
        APContact ap = DataManager.findAPContactByActiveUserAndContactId(
                mContext, NpcCommon.mThreeNum, contact.contactId);
        if (ap != null) {
            Log.e("dxswifi", "ap!=null---" + contact.contactId);
            return ap;
        } else {
            Log.e("dxswifi", "ap==null---" + contact.contactId);
            return new APContact(contact.contactId, contact.contactName,
                    contact.contactName, "", NpcCommon.mThreeNum);
        }
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (isRegFilter) {
            isRegFilter = false;
            mContext.unregisterReceiver(mReceiver);
        }
    }
}
