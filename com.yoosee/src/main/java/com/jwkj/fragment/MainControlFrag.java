package com.jwkj.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yoosee.R;
import com.jwkj.CallActivity;
import com.jwkj.activity.DeviceUpdateActivity;
import com.jwkj.activity.MainControlActivity;
import com.jwkj.data.Contact;
import com.jwkj.data.DataManager;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.utils.T;
import com.jwkj.widget.NormalDialog;
import com.jwkj.widget.NormalDialog.OnButtonOkListener;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;

public class MainControlFrag extends BaseFragment implements OnClickListener {
    RelativeLayout time_contrl, remote_control, alarm_control, video_control,
            record_control, security_control, net_control, defenceArea_control,
            chekc_device_update, sd_card_control, language_control, modify_wifipwd_control, ap_statechange;
    private Context mContext;
    private Contact mContact;
    private int connectType = 0;
    boolean isRegFilter = false;
    NormalDialog dialog;
    private TextView tvAPmodeChange;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mContext = getActivity();
        mContact = (Contact) getArguments().getSerializable("contact");
        connectType = getArguments().getInt("connectType");
        View view = inflater.inflate(R.layout.fragment_control_main, container,
                false);
        initComponent(view);
        regFilter();
        initData();
        return view;
    }

    public void initComponent(View view) {
        time_contrl = (RelativeLayout) view.findViewById(R.id.time_control);
        remote_control = (RelativeLayout) view
                .findViewById(R.id.remote_control);
        alarm_control = (RelativeLayout) view.findViewById(R.id.alarm_control);
        video_control = (RelativeLayout) view.findViewById(R.id.video_control);
        record_control = (RelativeLayout) view
                .findViewById(R.id.record_control);
        security_control = (RelativeLayout) view
                .findViewById(R.id.security_control);
        net_control = (RelativeLayout) view.findViewById(R.id.net_control);
        defenceArea_control = (RelativeLayout) view
                .findViewById(R.id.defenceArea_control);
        chekc_device_update = (RelativeLayout) view
                .findViewById(R.id.check_device_update);
        sd_card_control = (RelativeLayout) view
                .findViewById(R.id.sd_card_control);
        language_control = (RelativeLayout) view
                .findViewById(R.id.language_control);
        modify_wifipwd_control = (RelativeLayout) view.
                findViewById(R.id.modify_wifipwd_control);
        ap_statechange = (RelativeLayout) view.findViewById(R.id.ap_statechange);
        tvAPmodeChange = (TextView) view.findViewById(R.id.tx_apmodecange);

        defenceArea_control.setOnClickListener(this);
        net_control.setOnClickListener(this);
        security_control.setOnClickListener(this);
        record_control.setOnClickListener(this);
        video_control.setOnClickListener(this);
        time_contrl.setOnClickListener(this);
        remote_control.setOnClickListener(this);
        alarm_control.setOnClickListener(this);
        chekc_device_update.setOnClickListener(this);
        sd_card_control.setOnClickListener(this);
        language_control.setOnClickListener(this);
        modify_wifipwd_control.setOnClickListener(this);
        ap_statechange.setOnClickListener(this);
        modifyFeatures(view);
        // AP模式部分功能隐藏
        if (connectType == CallActivity.P2PCONECT) {
            security_control.setVisibility(View.VISIBLE);
            net_control.setVisibility(View.VISIBLE);
            modify_wifipwd_control.setVisibility(View.GONE);
            modifyFeatures(view);
        } else {
            security_control.setVisibility(View.GONE);
            net_control.setVisibility(View.GONE);
            sd_card_control.setBackgroundResource(R.drawable.tiao_bg_bottom);
            chekc_device_update.setVisibility(View.GONE);
            modify_wifipwd_control.setVisibility(View.VISIBLE);
        }
    }

    private void initData() {
        if (mContact != null) {
            P2PHandler.getInstance().getNpcSettings(mContact.getContactId(), mContact.contactPassword);
        }
    }


    private void modifyFeatures(View view) {
        if (mContact.contactType == P2PValue.DeviceType.PHONE) {
            view.findViewById(R.id.control_main_frame).setVisibility(
                    RelativeLayout.GONE);
        } else if (mContact.contactType == P2PValue.DeviceType.NPC) {
            chekc_device_update.setVisibility(RelativeLayout.GONE);
            defenceArea_control.setBackgroundResource(R.drawable.tiao_bg_bottom);
        } else {
            chekc_device_update.setVisibility(RelativeLayout.VISIBLE);
            defenceArea_control
                    .setBackgroundResource(R.drawable.tiao_bg_center);
        }
    }

    public void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.P2P.RET_GET_LANGUEGE);
        filter.addAction(Constants.P2P.RET_DEVICE_NOT_SUPPORT);
        filter.addAction(Constants.P2P.RET_SET_AP_MODE);
        filter.addAction(Constants.P2P.RET_AP_MODESURPPORT);
        mContext.registerReceiver(br, filter);
        isRegFilter = true;
    }

    BroadcastReceiver br = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(Constants.P2P.RET_GET_LANGUEGE)) {
                int languegecount = intent.getIntExtra("languegecount", -1);
                int curlanguege = intent.getIntExtra("curlanguege", -1);
                int[] langueges = intent.getIntArrayExtra("langueges");
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    Intent go_language_control = new Intent();
                    go_language_control
                            .setAction(Constants.Action.REPLACE_LANGUAGE_CONTROL);
                    go_language_control.putExtra("isEnforce", true);
                    go_language_control
                            .putExtra("languegecount", languegecount);
                    go_language_control.putExtra("curlanguege", curlanguege);
                    go_language_control.putExtra("langueges", langueges);
                    mContext.sendBroadcast(go_language_control);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_DEVICE_NOT_SUPPORT)) {
                if (dialog != null) {
                    dialog.dismiss();
                    T.showShort(mContext, R.string.not_support);
                }
            } else if (intent.getAction().equals(Constants.P2P.RET_AP_MODESURPPORT)) {
                int result = intent.getIntExtra("result", 0);
                String id = intent.getStringExtra("id");
                Log.e("dxsTest", "RET_AP_MODESURPPORT:" + "id-->" + id + "result-->" + result + "contactId-->" + mContact.getContactId());
                if (id == null || mContact == null) {
                    return;
                }
                if (id.equals(mContact.getContactId())) {
                    if (result == 0) {//不支持AP模式
                        ap_statechange.setVisibility(View.GONE);
                    } else if (result == 1) {//支持AP模式,不处于AP模式
                        ap_statechange.setVisibility(View.VISIBLE);
                        tvAPmodeChange.setText(R.string.ap_modecahnge_line);
                    } else if (result == 2) {//支持AP模式，处于AP模式
                        ap_statechange.setVisibility(View.VISIBLE);
                        tvAPmodeChange.setText(R.string.ap_modecahnge_ap);
                    }
                    ap_statechange.setTag(result);
                }
            } else if (intent.getAction().equals(Constants.P2P.RET_SET_AP_MODE)) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                ((MainControlActivity) mContext).finish();
            }
        }
    };


    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        int resId = view.getId();
        if (resId == R.id.remote_control) {
            Intent go_remote_control = new Intent();
            go_remote_control
                    .setAction(Constants.Action.REPLACE_REMOTE_CONTROL);
            go_remote_control.putExtra("isEnforce", true);
            mContext.sendBroadcast(go_remote_control);
        } else if (resId == R.id.time_control) {
            Intent go_time_control = new Intent();
            go_time_control.setAction(Constants.Action.REPLACE_SETTING_TIME);
            go_time_control.putExtra("isEnforce", true);
            mContext.sendBroadcast(go_time_control);
        } else if (resId == R.id.alarm_control) {
            Intent go_alarm_control = new Intent();
            go_alarm_control.setAction(Constants.Action.REPLACE_ALARM_CONTROL);
            go_alarm_control.putExtra("isEnforce", true);
            mContext.sendBroadcast(go_alarm_control);
        } else if (resId == R.id.record_control) {
            Intent go_record_control = new Intent();
            go_record_control
                    .setAction(Constants.Action.REPLACE_RECORD_CONTROL);
            go_record_control.putExtra("isEnforce", true);
            mContext.sendBroadcast(go_record_control);
        } else if (resId == R.id.video_control) {
            Intent go_video_control = new Intent();
            go_video_control.setAction(Constants.Action.REPLACE_VIDEO_CONTROL);
            go_video_control.putExtra("isEnforce", true);
            mContext.sendBroadcast(go_video_control);
        } else if (resId == R.id.security_control) {
            Intent go_security_control = new Intent();
            go_security_control
                    .setAction(Constants.Action.REPLACE_SECURITY_CONTROL);
            go_security_control.putExtra("isEnforce", true);
            mContext.sendBroadcast(go_security_control);
        } else if (resId == R.id.net_control) {
            Intent go_net_control = new Intent();
            go_net_control.setAction(Constants.Action.REPLACE_NET_CONTROL);
            go_net_control.putExtra("isEnforce", true);
            mContext.sendBroadcast(go_net_control);
        } else if (resId == R.id.defenceArea_control) {
            Intent go_da_control = new Intent();
            go_da_control
                    .setAction(Constants.Action.REPLACE_DEFENCE_AREA_CONTROL);
            go_da_control.putExtra("isEnforce", true);
            mContext.sendBroadcast(go_da_control);
        } else if (resId == R.id.check_device_update) {
            Intent check_update = new Intent(mContext,
                    DeviceUpdateActivity.class);
            check_update.putExtra("contact", mContact);
            mContext.startActivity(check_update);
        } else if (resId == R.id.sd_card_control) {
            Intent go_sd_card_control = new Intent();
            go_sd_card_control
                    .setAction(Constants.Action.REPLACE_SD_CARD_CONTROL);
            go_sd_card_control.putExtra("isEnforce", true);
            mContext.sendBroadcast(go_sd_card_control);
        } else if (resId == R.id.language_control) {
            P2PHandler.getInstance().getDeviceLanguage(mContact.contactId,
                    mContact.contactPassword);
            dialog = new NormalDialog(mContext);
            dialog.setStyle(NormalDialog.DIALOG_STYLE_LOADING);
            dialog.showDialog();
        } else if (resId == R.id.modify_wifipwd_control) {
            Intent go_modify_wifipwd = new Intent();
            go_modify_wifipwd.setAction(Constants.Action.REPLACE_MODIFY_WIFI_PWD_CONTROL);
            go_modify_wifipwd.putExtra("isEnforce", true);
            mContext.sendBroadcast(go_modify_wifipwd);
        } else if (resId == R.id.ap_statechange) {
            int resuly = (Integer) view.getTag();
            dialog = new NormalDialog(mContext);
            if (resuly == 1) {//支持AP模式,不处于AP模式
                dialog.setTitle(R.string.ap_modecahnge_line);
            } else if (resuly == 2) {//支持AP模式，处于AP模式
                dialog.setTitle(R.string.ap_modecahnge_ap);
            }
            dialog.setContentStr(R.string.ap_modecahnge);
            dialog.setbtnStr2(R.string.cancel);
            dialog.setbtnStr1(R.string.ensure);
            dialog.setStyle(NormalDialog.DIALOG_STYLE_NORMAL);
            dialog.showDialog();
            dialog.setOnButtonOkListener(new OnButtonOkListener() {

                @Override
                public void onClick() {
                    P2PHandler.getInstance().setAPModeChange(mContact.getContactId(), mContact.contactPassword, 1);
                    dialog.showLoadingDialog2();
                }
            });
            //查看返回结果，再跳转
        }
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        if (isRegFilter) {
            mContext.unregisterReceiver(br);
            isRegFilter = false;
        }
    }
}
