package com.jwkj.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jwkj.adapter.DateNumericAdapter;
import com.jwkj.data.Contact;
import com.jwkj.global.Constants;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.wheel.widget.WheelView;
import com.jwkj.widget.NormalDialog;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;
import com.yoosee.R;

public class RecordControlFrag extends BaseFragment implements OnClickListener,
        OnTouchListener {
    private Context mContext;
    private Contact contact;
    private boolean isRegFilter = false;

    RelativeLayout change_record_type, change_record_time, change_plan_time,
            change_plan_time_title;
    LinearLayout record_type_radio, record_time_radio, time_picker;
    ProgressBar progressBar_record_type, progressBar_record_time,
            progressBar_plan_time;
    RadioButton radio_one, radio_two, radio_three;
    RadioButton radio_one_time, radio_two_time, radio_three_time;
    TextView time_text;
    WheelView hour_from, minute_from, hour_to, minute_to;
    String cur_modify_plan_time;
    int cur_modify_record_type;
    int cur_modify_record_time;

    RelativeLayout change_record, change_pre_record;
    ProgressBar progressBar_record, progressBar_pre_record;
    ImageView record_img, pre_record_img;
    TextView record_text, pre_record_text;
    ScrollView scroll_view;
    RelativeLayout sd_format, usb_capacity, usb_remainning_capacity, sd_card_remainning_capacity;
    TextView tv_total_capacity, tv_sd_remainning_capacity;
    TextView tv_usb_total_capacity, tv_usb_remainning_capacity;
    ImageView format_icon;
    ProgressBar progress_format;

    LinearLayout l_sd_card, l_usb;
    ImageView img_dropdown;
    Button bt_plan_time;
    int recordState;
    int last_record;
    int last_modify_record;
    boolean isOpenPreRecord = false;
    int last_pre_record;
    int last_modify_pre_record;
    boolean isSupportPreRecored = false;
    int type;
    String idOrIp;

    String command;
    int SDcardId;
    int sdId;
    int usbId;
    boolean isSDCard;
    int count = 0;
    boolean isDropDown = false;

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
        contact = (Contact) getArguments().getSerializable("contact");
        idOrIp = contact.contactId;
        if (contact.ipadressAddress != null) {
            String mark = contact.ipadressAddress.getHostAddress();
            String ip = mark.substring(mark.lastIndexOf(".") + 1, mark.length());
            if (!ip.equals("") && ip != null) {
                idOrIp = ip;
            }
        }
        View view = inflater.inflate(R.layout.fragment_record_control,
                container, false);
        initComponent(view);
        regFilter();

        showProgress_record_type();
        P2PHandler.getInstance()
                .getNpcSettings(idOrIp, contact.contactPassword);
        command = createCommand("80", "0", "00");
        P2PHandler.getInstance().getSdCardCapacity(idOrIp, contact.contactPassword, command);
        return view;
    }

    public void initComponent(View view) {
        scroll_view = (ScrollView) view.findViewById(R.id.scroll_view);
        change_record_type = (RelativeLayout) view
                .findViewById(R.id.change_record_type);
        record_type_radio = (LinearLayout) view
                .findViewById(R.id.record_type_radio);
        progressBar_record_type = (ProgressBar) view
                .findViewById(R.id.progressBar_record_type);

        radio_one = (RadioButton) view.findViewById(R.id.radio_one);
        radio_two = (RadioButton) view.findViewById(R.id.radio_two);
        radio_three = (RadioButton) view.findViewById(R.id.radio_three);

        change_record_time = (RelativeLayout) view
                .findViewById(R.id.change_record_time);
        record_time_radio = (LinearLayout) view
                .findViewById(R.id.record_time_radio);
        progressBar_record_time = (ProgressBar) view
                .findViewById(R.id.progressBar_record_time);

        radio_one_time = (RadioButton) view.findViewById(R.id.radio_one_time);
        radio_two_time = (RadioButton) view.findViewById(R.id.radio_two_time);
        radio_three_time = (RadioButton) view
                .findViewById(R.id.radio_three_time);

        change_plan_time = (RelativeLayout) view
                .findViewById(R.id.change_plan_time);
        change_plan_time_title = (RelativeLayout) view
                .findViewById(R.id.change_plan_time_title);
        progressBar_plan_time = (ProgressBar) view
                .findViewById(R.id.progressBar_plan_time);
        time_picker = (LinearLayout) view.findViewById(R.id.time_picker);
        time_text = (TextView) view.findViewById(R.id.time_text);
        initTimePicker(view);

//		change_plan_time.setOnClickListener(this);

        radio_one.setOnClickListener(this);
        radio_two.setOnClickListener(this);
        radio_three.setOnClickListener(this);

        radio_one_time.setOnClickListener(this);
        radio_two_time.setOnClickListener(this);
        radio_three_time.setOnClickListener(this);

        change_record = (RelativeLayout) view.findViewById(R.id.change_record);
        record_img = (ImageView) view.findViewById(R.id.record_img);
        record_text = (TextView) view.findViewById(R.id.record_text);
        progressBar_record = (ProgressBar) view
                .findViewById(R.id.progressBar_record);

        change_pre_record = (RelativeLayout) view
                .findViewById(R.id.change_pre_record);
        pre_record_text = (TextView) view.findViewById(R.id.pre_record_text);
        pre_record_img = (ImageView) view.findViewById(R.id.pre_record_img);
        progressBar_pre_record = (ProgressBar) view
                .findViewById(R.id.progressBar_pre_record);
        change_record.setOnClickListener(this);
        change_pre_record.setOnClickListener(this);
        change_pre_record.setClickable(false);

        tv_total_capacity = (TextView) view.findViewById(R.id.tv_sd_capacity);
        tv_sd_remainning_capacity = (TextView) view
                .findViewById(R.id.tv_sd_remainning_capacity);
        sd_format = (RelativeLayout) view.findViewById(R.id.sd_format);
        format_icon = (ImageView) view.findViewById(R.id.format_icon);
        progress_format = (ProgressBar) view.findViewById(R.id.progress_format);
        usb_capacity = (RelativeLayout) view.findViewById(R.id.usb_capacity);
        usb_remainning_capacity = (RelativeLayout) view
                .findViewById(R.id.usb_remainning_capacity);
        tv_usb_total_capacity = (TextView) view
                .findViewById(R.id.tv_usb_capacity);
        tv_usb_remainning_capacity = (TextView) view
                .findViewById(R.id.tv_usb_remainning_capacity);
        sd_format.setOnClickListener(this);
        sd_card_remainning_capacity = (RelativeLayout) view.findViewById(R.id.sd_card_remainning_capacity);
        if (contact.contactType == P2PValue.DeviceType.NPC) {
            sd_format.setVisibility(RelativeLayout.GONE);
            usb_capacity.setVisibility(RelativeLayout.VISIBLE);
            usb_remainning_capacity.setVisibility(RelativeLayout.VISIBLE);
            sd_card_remainning_capacity.setBackgroundResource(R.drawable.tiao_bg_bottom);
        }
        l_sd_card = (LinearLayout) view.findViewById(R.id.l_sd_card);
        l_usb = (LinearLayout) view.findViewById(R.id.l_usb);
        img_dropdown = (ImageView) view.findViewById(R.id.img_dropdown);
        bt_plan_time = (Button) view.findViewById(R.id.bt_plan_time);
        img_dropdown.setOnClickListener(this);
        bt_plan_time.setOnClickListener(this);
    }

    public void initTimePicker(View view) {
        hour_from = (WheelView) view.findViewById(R.id.hour_from);
        hour_from.setViewAdapter(new DateNumericAdapter(mContext, 0, 23));
        hour_from.setCyclic(true);

        minute_from = (WheelView) view.findViewById(R.id.minute_from);
        minute_from.setViewAdapter(new DateNumericAdapter(mContext, 0, 59));
        minute_from.setCyclic(true);

        hour_to = (WheelView) view.findViewById(R.id.hour_to);
        hour_to.setViewAdapter(new DateNumericAdapter(mContext, 0, 23));
        hour_to.setCyclic(true);

        minute_to = (WheelView) view.findViewById(R.id.minute_to);
        minute_to.setViewAdapter(new DateNumericAdapter(mContext, 0, 59));
        minute_to.setCyclic(true);
        hour_from.setOnTouchListener(this);
        minute_from.setOnTouchListener(this);
        hour_to.setOnTouchListener(this);
        minute_to.setOnTouchListener(this);
    }

    public void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.P2P.ACK_RET_GET_NPC_SETTINGS);

        filter.addAction(Constants.P2P.ACK_RET_SET_RECORD_TYPE);
        filter.addAction(Constants.P2P.RET_SET_RECORD_TYPE);
        filter.addAction(Constants.P2P.RET_GET_RECORD_TYPE);

        filter.addAction(Constants.P2P.ACK_RET_SET_RECORD_TIME);
        filter.addAction(Constants.P2P.RET_SET_RECORD_TIME);
        filter.addAction(Constants.P2P.RET_GET_RECORD_TIME);

        filter.addAction(Constants.P2P.ACK_RET_SET_RECORD_PLAN_TIME);
        filter.addAction(Constants.P2P.RET_SET_RECORD_PLAN_TIME);
        filter.addAction(Constants.P2P.RET_GET_RECORD_PLAN_TIME);

        filter.addAction(Constants.P2P.ACK_RET_SET_REMOTE_RECORD);
        filter.addAction(Constants.P2P.RET_SET_REMOTE_RECORD);
        filter.addAction(Constants.P2P.RET_GET_REMOTE_RECORD);
        filter.addAction(Constants.P2P.RET_GET_PRE_RECORD);
        filter.addAction(Constants.P2P.ACK_RET_SET_PRE_RECORD);
        filter.addAction(Constants.P2P.RET_SET_PRE_RECORD);
        filter.addAction(Constants.P2P.ACK_GET_SD_CARD_CAPACITY);
        filter.addAction(Constants.P2P.RET_GET_SD_CARD_CAPACITY);
        filter.addAction(Constants.P2P.ACK_GET_SD_CARD_FORMAT);
        filter.addAction(Constants.P2P.RET_GET_SD_CARD_FORMAT);
        filter.addAction(Constants.P2P.RET_GET_USB_CAPACITY);
        filter.addAction(Constants.P2P.RET_DEVICE_NOT_SUPPORT);
        mContext.registerReceiver(mReceiver, filter);
        isRegFilter = true;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            if (intent.getAction().equals(
                    Constants.P2P.ACK_RET_GET_NPC_SETTINGS)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                    Intent i = new Intent();
                    i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);
                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my", "net error resend:get npc settings");
                    P2PHandler.getInstance().getNpcSettings(idOrIp,
                            contact.contactPassword);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_GET_RECORD_TYPE)) {
                type = intent.getIntExtra("type", -1);
                updateRecordType(type);
                showRecordType();
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_SET_RECORD_TYPE)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.RECORD_TYPE_SET.SETTING_SUCCESS) {
                    P2PHandler.getInstance().getNpcSettings(idOrIp,
                            contact.contactPassword);
                    // updateRecordType(cur_modify_record_type);
                    // showRecordType();
                    T.showShort(mContext, R.string.modify_success);
                } else {
                    showRecordType();
                    T.showShort(mContext, R.string.operator_error);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_GET_RECORD_TIME)) {
                int time = intent.getIntExtra("time", -1);
                if (time == Constants.P2P_SET.RECORD_TIME_SET.RECORD_TIME_ONE_MINUTE) {
                    radio_one_time.setChecked(true);
                } else if (time == Constants.P2P_SET.RECORD_TIME_SET.RECORD_TIME_TWO_MINUTE) {
                    radio_two_time.setChecked(true);
                } else if (time == Constants.P2P_SET.RECORD_TIME_SET.RECORD_TIME_THREE_MINUTE) {
                    radio_three_time.setChecked(true);
                }
                radio_one_time.setEnabled(true);
                radio_two_time.setEnabled(true);
                radio_three_time.setEnabled(true);
                progressBar_record_time.setVisibility(RelativeLayout.GONE);
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_SET_RECORD_TIME)) {
                int result = intent.getIntExtra("result", -1);
                if (result == 0) {
                    if (cur_modify_record_time == Constants.P2P_SET.RECORD_TIME_SET.RECORD_TIME_ONE_MINUTE) {
                        radio_one_time.setChecked(true);
                    } else if (cur_modify_record_time == Constants.P2P_SET.RECORD_TIME_SET.RECORD_TIME_TWO_MINUTE) {
                        radio_two_time.setChecked(true);
                    } else if (cur_modify_record_time == Constants.P2P_SET.RECORD_TIME_SET.RECORD_TIME_THREE_MINUTE) {
                        radio_three_time.setChecked(true);
                    }
                    radio_one_time.setEnabled(true);
                    radio_two_time.setEnabled(true);
                    radio_three_time.setEnabled(true);
                    progressBar_record_time.setVisibility(RelativeLayout.GONE);
                    T.showShort(mContext, R.string.modify_success);
                } else {
                    radio_one_time.setEnabled(true);
                    radio_two_time.setEnabled(true);
                    radio_three_time.setEnabled(true);
                    progressBar_record_time.setVisibility(RelativeLayout.GONE);
                    T.showShort(mContext, R.string.operator_error);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_GET_RECORD_PLAN_TIME)) {
                String time = intent.getStringExtra("time");
                Log.e("time", time);
                String startTime1 = time.substring(0, 2);
                String startTime2 = time.substring(3, 5);
                String endTime1 = time.substring(6, 8);
                String endTime2 = time.substring(9, 11);
                if (Integer.parseInt(startTime1) < 10) {
                    startTime1 = time.substring(1, 2);
                }
                if (Integer.parseInt(startTime2) < 10) {
                    startTime2 = time.substring(4, 5);
                }
                if (Integer.parseInt(endTime1) < 10) {
                    endTime1 = time.substring(7, 8);
                }
                if (Integer.parseInt(endTime2) < 10) {
                    endTime2 = time.substring(10, 11);
                }
                hour_from.setCurrentItem(Integer.parseInt(startTime1));
                minute_from.setCurrentItem(Integer.parseInt(startTime2));
                hour_to.setCurrentItem(Integer.parseInt(endTime1));
                minute_to.setCurrentItem(Integer.parseInt(endTime2));
                Log.e("time", startTime1 + " " + startTime2);
                Log.e("time", endTime1 + " " + endTime2);
                time_text.setText(time);
                change_plan_time.setEnabled(true);
                progressBar_plan_time.setVisibility(RelativeLayout.GONE);
                time_text.setVisibility(RelativeLayout.VISIBLE);
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_SET_RECORD_PLAN_TIME)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.RECORD_PLAN_TIME_SET.SETTING_SUCCESS) {
                    time_text.setText(cur_modify_plan_time);
                    change_plan_time.setEnabled(true);
                    progressBar_plan_time.setVisibility(RelativeLayout.GONE);
                    time_text.setVisibility(RelativeLayout.VISIBLE);
                    T.showShort(mContext, R.string.modify_success);
                } else {
                    change_plan_time.setEnabled(true);
                    progressBar_plan_time.setVisibility(RelativeLayout.GONE);
                    time_text.setVisibility(RelativeLayout.VISIBLE);
                    T.showShort(mContext, R.string.operator_error);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_RET_SET_RECORD_TYPE)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                    Intent i = new Intent();
                    i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);
                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my", "net error resend:set npc settings record type");
                    P2PHandler.getInstance().setRecordType(idOrIp,
                            contact.contactPassword, cur_modify_record_type);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_RET_SET_RECORD_TIME)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                    Intent i = new Intent();
                    i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);
                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my", "net error resend:set npc settings record time");
                    P2PHandler.getInstance().setRecordType(idOrIp,
                            contact.contactPassword, cur_modify_record_type);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_RET_SET_RECORD_PLAN_TIME)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                    Intent i = new Intent();
                    i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);
                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my",
                            "net error resend:set npc settings record plan time");
                    P2PHandler.getInstance().setRecordPlanTime(idOrIp,
                            contact.contactPassword, cur_modify_plan_time);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_GET_REMOTE_RECORD)) {
                int state = intent.getIntExtra("state", -1);
                progressBar_record.setVisibility(RelativeLayout.GONE);
                record_img.setVisibility(RelativeLayout.VISIBLE);
                updateRecord(state);
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_SET_REMOTE_RECORD)) {
                int state = intent.getIntExtra("state", -1);
                P2PHandler.getInstance().getNpcSettings(idOrIp,
                        contact.contactPassword);
                // updateRecord(state);
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_RET_SET_REMOTE_RECORD)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                    Intent i = new Intent();
                    i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);
                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my", "net error resend:set remote record");
                    P2PHandler.getInstance().setRemoteRecord(idOrIp,
                            contact.contactPassword, last_modify_record);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_GET_PRE_RECORD)) {
                int state = intent.getIntExtra("state", -1);
                isSupportPreRecored = true;
                change_pre_record.setClickable(true);
                if (type == Constants.P2P_SET.RECORD_TYPE_SET.RECORD_TYPE_ALARM) {
                    change_pre_record.setVisibility(RelativeLayout.VISIBLE);
                }
                if (state == 1) {
                    pre_record_img
                            .setBackgroundResource(R.drawable.ic_checkbox_on);
                    last_pre_record = Constants.P2P_SET.PRE_RECORD_SET.PRE_RECORD_SWITCH_ON;
                } else if (state == 0) {
                    pre_record_img
                            .setBackgroundResource(R.drawable.ic_checkbox_off);
                    last_pre_record = Constants.P2P_SET.PRE_RECORD_SET.PRE_RECORD_SWITCH_OFF;
                }
                showPreRecordImg();
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_RET_SET_PRE_RECORD)) {
                int result = intent.getIntExtra("state", -1);
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                    Intent i = new Intent();
                    i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);
                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my", "net error resend:set npc settings record type");
                    P2PHandler.getInstance().setPreRecord(idOrIp,
                            contact.contactPassword, last_modify_pre_record);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_SET_PRE_RECORD)) {
                int result = intent.getIntExtra("result", -1);
                if (result == 0) {
                    P2PHandler.getInstance().getNpcSettings(idOrIp,
                            contact.contactPassword);
                    T.showShort(mContext, R.string.modify_success);
                } else if (result == 83) {
                    T.showShort(mContext, R.string.operator_error);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_GET_SD_CARD_CAPACITY)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {

                    Intent i = new Intent();
                    i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);

                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my", "net error resend:get npc time");
                    P2PHandler.getInstance().getSdCardCapacity(idOrIp,
                            contact.contactPassword, command);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_GET_SD_CARD_CAPACITY)) {
                Log.e("usb", "no sd card");
                int total_capacity = intent.getIntExtra("total_capacity", -1);
                int remain_capacity = intent.getIntExtra("remain_capacity", -1);
                int state = intent.getIntExtra("state", -1);
                SDcardId = intent.getIntExtra("SDcardID", -1);
                String id = Integer.toBinaryString(SDcardId);
                Log.e("id", "msga" + id);
                while (id.length() < 8) {
                    id = "0" + id;
                }
                char index = id.charAt(3);
                Log.e("id", "msgb" + id);
                Log.e("id", "msgc" + index);
                if (state == 1) {
                    if (index == '1') {
                        sdId = SDcardId;
                        tv_total_capacity.setText(String
                                .valueOf(total_capacity) + "M");
                        tv_sd_remainning_capacity.setText(String
                                .valueOf(remain_capacity) + "M");
                        showSDImg();
                    } else if (index == '0') {
                        usbId = SDcardId;
                        tv_usb_total_capacity.setText(String
                                .valueOf(total_capacity) + "M");
                        tv_usb_remainning_capacity.setText(String
                                .valueOf(remain_capacity) + "M");
                    }
                    l_sd_card.setVisibility(LinearLayout.VISIBLE);
                } else {
                    // count++;
                    // if(contact.contactType==P2PValue.DeviceType.IPC){
                    // if(count==1){
                    // Intent back = new Intent();
                    // back.setAction(Constants.Action.REPLACE_MAIN_CONTROL);
                    // mContext.sendBroadcast(back);
                    // T.showShort(mContext,R.string.sd_no_exist);
                    // }
                    // }else{
                    // if(count==2){
                    // Intent back = new Intent();
                    // back.setAction(Constants.Action.REPLACE_MAIN_CONTROL);
                    // mContext.sendBroadcast(back);
                    // T.showShort(mContext,R.string.sd_no_exist);
                    // }
                    // }
//					Intent back = new Intent();
//					back.setAction(Constants.Action.REPLACE_MAIN_CONTROL);
//					mContext.sendBroadcast(back);
//					T.showShort(mContext, R.string.sd_no_exist);
                    l_sd_card.setVisibility(LinearLayout.GONE);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_GET_SD_CARD_FORMAT)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {

                    Intent i = new Intent();
                    i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);

                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my", "net error resend:get npc time");
                    P2PHandler.getInstance().setSdFormat(idOrIp,
                            contact.contactPassword, sdId);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_GET_SD_CARD_FORMAT)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.SD_FORMAT.SD_CARD_SUCCESS) {
                    T.showShort(mContext, R.string.sd_format_success);
                } else if (result == Constants.P2P_SET.SD_FORMAT.SD_CARD_FAIL) {
                    T.showShort(mContext, R.string.sd_format_fail);
                } else if (result == Constants.P2P_SET.SD_FORMAT.SD_NO_EXIST) {
                    T.showShort(mContext, R.string.sd_no_exist);
                }
                showSDImg();
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_GET_USB_CAPACITY)) {
                Log.e("usb", "get usb");
                int total_capacity = intent.getIntExtra("total_capacity", -1);
                int remain_capacity = intent.getIntExtra("remain_capacity", -1);
                int state = intent.getIntExtra("state", -1);
                SDcardId = intent.getIntExtra("SDcardID", -1);
                String id = Integer.toBinaryString(SDcardId);
                Log.e("id", "msga" + id);
                while (id.length() < 8) {
                    id = "0" + id;
                }
                char index = id.charAt(3);
                Log.e("id", "msgb" + id);
                Log.e("id", "msgc" + index);
                if (state == 1) {
                    if (index == '1') {
                        sdId = SDcardId;
                        tv_total_capacity.setText(String
                                .valueOf(total_capacity) + "M");
                        tv_sd_remainning_capacity.setText(String
                                .valueOf(remain_capacity) + "M");
                        showSDImg();
                    } else if (index == '0') {
                        usbId = SDcardId;
                        tv_usb_total_capacity.setText(String
                                .valueOf(total_capacity) + "M");
                        tv_usb_remainning_capacity.setText(String
                                .valueOf(remain_capacity) + "M");
                    }
                    sd_card_remainning_capacity.setBackgroundResource(R.drawable.tiao_bg_center);
                    l_usb.setVisibility(LinearLayout.VISIBLE);
                } else {
                    Log.e("usb", "no usb");
                    count++;
                    if (contact.contactType == P2PValue.DeviceType.IPC) {
                        if (count == 1) {
                            Intent back = new Intent();
                            back.setAction(Constants.Action.REPLACE_MAIN_CONTROL);
                            mContext.sendBroadcast(back);
                            T.showShort(mContext, R.string.sd_no_exist);
                        }
                    } else {
                        if (count == 2) {
                            Intent back = new Intent();
                            back.setAction(Constants.Action.REPLACE_MAIN_CONTROL);
                            mContext.sendBroadcast(back);
                            T.showShort(mContext, R.string.sd_no_exist);
                        }
                        sd_card_remainning_capacity.setBackgroundResource(R.drawable.tiao_bg_bottom);
                    }
                    l_usb.setVisibility(LinearLayout.GONE);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_DEVICE_NOT_SUPPORT)) {

            }
        }

    };

    public void updateRecord(int state) {
        if (state == Constants.P2P_SET.REMOTE_RECORD_SET.RECORD_SWITCH_ON) {
            last_record = Constants.P2P_SET.REMOTE_RECORD_SET.RECORD_SWITCH_ON;
            record_img.setBackgroundResource(R.drawable.ic_checkbox_on);
        } else {
            last_record = Constants.P2P_SET.REMOTE_RECORD_SET.RECORD_SWITCH_OFF;
            record_img.setBackgroundResource(R.drawable.ic_checkbox_off);
        }
    }

    void updateRecordType(int type) {
        if (type == Constants.P2P_SET.RECORD_TYPE_SET.RECORD_TYPE_MANUAL) {
            radio_one.setChecked(true);
            hideRecordTime();
            hidePlanTime();
            showManual();
            hideSetPlanTime();
        } else if (type == Constants.P2P_SET.RECORD_TYPE_SET.RECORD_TYPE_ALARM) {
            radio_two.setChecked(true);
            hidePlanTime();
            hideManual();
            showRecordTime();
            hideSetPlanTime();
        } else if (type == Constants.P2P_SET.RECORD_TYPE_SET.RECORD_TYPE_TIMER) {
            radio_three.setChecked(true);
            hideRecordTime();
            hideManual();
            showPlanTime();
        }
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        int resId = view.getId();
        if (resId == R.id.change_record) {
            progressBar_record.setVisibility(RelativeLayout.VISIBLE);
            record_img.setVisibility(RelativeLayout.GONE);
            if (last_record == Constants.P2P_SET.REMOTE_RECORD_SET.RECORD_SWITCH_ON) {
                last_modify_record = Constants.P2P_SET.REMOTE_RECORD_SET.RECORD_SWITCH_OFF;
                P2PHandler.getInstance().setRemoteRecord(idOrIp,
                        contact.contactPassword, last_modify_record);
            } else {
                last_modify_record = Constants.P2P_SET.REMOTE_RECORD_SET.RECORD_SWITCH_ON;
                P2PHandler.getInstance().setRemoteRecord(idOrIp,
                        contact.contactPassword, last_modify_record);
            }
        } else if (resId == R.id.radio_one) {
            progressBar_record_type.setVisibility(RelativeLayout.VISIBLE);
            radio_one.setEnabled(false);
            radio_two.setEnabled(false);
            radio_three.setEnabled(false);
            cur_modify_record_type = Constants.P2P_SET.RECORD_TYPE_SET.RECORD_TYPE_MANUAL;
            showRecordSwitchProgress();
            P2PHandler.getInstance().setRecordType(idOrIp,
                    contact.contactPassword, cur_modify_record_type);
        } else if (resId == R.id.radio_two) {
            progressBar_record_type.setVisibility(RelativeLayout.VISIBLE);
            radio_one.setEnabled(false);
            radio_two.setEnabled(false);
            radio_three.setEnabled(false);
            cur_modify_record_type = Constants.P2P_SET.RECORD_TYPE_SET.RECORD_TYPE_ALARM;
            showPreRecordProgress();
            P2PHandler.getInstance().setRecordType(idOrIp,
                    contact.contactPassword, cur_modify_record_type);
        } else if (resId == R.id.radio_three) {
            radio_one.setEnabled(false);
            radio_two.setEnabled(false);
            radio_three.setEnabled(false);
            progressBar_record_type.setVisibility(RelativeLayout.VISIBLE);
            cur_modify_record_type = Constants.P2P_SET.RECORD_TYPE_SET.RECORD_TYPE_TIMER;
            P2PHandler.getInstance().setRecordType(idOrIp,
                    contact.contactPassword, cur_modify_record_type);
        } else if (resId == R.id.radio_one_time) {
            progressBar_record_time.setVisibility(RelativeLayout.VISIBLE);
            radio_one_time.setEnabled(false);
            radio_two_time.setEnabled(false);
            radio_three_time.setEnabled(false);
            cur_modify_record_time = Constants.P2P_SET.RECORD_TIME_SET.RECORD_TIME_ONE_MINUTE;
            P2PHandler.getInstance().setRecordTime(idOrIp,
                    contact.contactPassword, cur_modify_record_time);
        } else if (resId == R.id.radio_two_time) {
            progressBar_record_time.setVisibility(RelativeLayout.VISIBLE);
            radio_one_time.setEnabled(false);
            radio_two_time.setEnabled(false);
            radio_three_time.setEnabled(false);
            cur_modify_record_time = Constants.P2P_SET.RECORD_TIME_SET.RECORD_TIME_TWO_MINUTE;
            P2PHandler.getInstance().setRecordTime(idOrIp,
                    contact.contactPassword, cur_modify_record_time);
        } else if (resId == R.id.radio_three_time) {
            progressBar_record_time.setVisibility(RelativeLayout.VISIBLE);
            radio_one_time.setEnabled(false);
            radio_two_time.setEnabled(false);
            radio_three_time.setEnabled(false);
            cur_modify_record_time = Constants.P2P_SET.RECORD_TIME_SET.RECORD_TIME_THREE_MINUTE;
            P2PHandler.getInstance().setRecordTime(idOrIp,
                    contact.contactPassword, cur_modify_record_time);
        } else if (resId == R.id.change_pre_record) {
            // 1.??
            showPreRecordProgress();
            if (last_pre_record == Constants.P2P_SET.PRE_RECORD_SET.PRE_RECORD_SWITCH_ON) {
                last_modify_pre_record = Constants.P2P_SET.PRE_RECORD_SET.PRE_RECORD_SWITCH_OFF;
                P2PHandler.getInstance().setPreRecord(idOrIp,
                        contact.contactPassword, last_modify_pre_record);
                isOpenPreRecord = false;
            } else if (last_pre_record == Constants.P2P_SET.PRE_RECORD_SET.PRE_RECORD_SWITCH_OFF) {
                last_modify_pre_record = Constants.P2P_SET.PRE_RECORD_SET.PRE_RECORD_SWITCH_ON;
                P2PHandler.getInstance().setPreRecord(idOrIp,
                        contact.contactPassword, last_modify_pre_record);
                isOpenPreRecord = true;
            }
        } else if (resId == R.id.sd_format) {
            final NormalDialog dialog = new NormalDialog(mContext, mContext
                    .getResources().getString(R.string.sd_formatting), mContext
                    .getResources().getString(R.string.delete_sd_remind),
                    mContext.getResources().getString(R.string.ensure),
                    mContext.getResources().getString(R.string.cancel));
            dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {

                @Override
                public void onClick() {
                    // TODO Auto-generated method stub
                    P2PHandler.getInstance().setSdFormat(idOrIp,
                            contact.contactPassword, sdId);
                    Log.e("SDcardId", "SDcardId" + SDcardId);
                }
            });
            dialog.setOnButtonCancelListener(new NormalDialog.OnButtonCancelListener() {

                @Override
                public void onClick() {
                    // TODO Auto-generated method stub
                    showSDImg();
                    dialog.dismiss();
                }
            });
            dialog.showNormalDialog();
            dialog.setCanceledOnTouchOutside(false);
            showSDProgress();
        } else if (resId == R.id.img_dropdown) {
            if (isDropDown == false) {
                showSetPlanTime();
            } else {
                hideSetPlanTime();
            }
        } else if (resId == R.id.bt_plan_time) {
            showProgress_plan_time();

            cur_modify_plan_time = Utils.convertPlanTime(
                    hour_from.getCurrentItem(), minute_from.getCurrentItem(),
                    hour_to.getCurrentItem(), minute_to.getCurrentItem());
            P2PHandler.getInstance().setRecordPlanTime(idOrIp,
                    contact.contactPassword, cur_modify_plan_time);
        }
    }

    public void showSetPlanTime() {
        time_picker.setVisibility(View.VISIBLE);
        change_plan_time.setVisibility(View.VISIBLE);
        change_plan_time_title.setBackgroundResource(R.drawable.tiao_bg_up);
        img_dropdown.setBackgroundResource(R.drawable.img_pull);
        isDropDown = true;
    }

    public void hideSetPlanTime() {
        time_picker.setVisibility(View.GONE);
        change_plan_time.setVisibility(View.GONE);
        change_plan_time_title.setBackgroundResource(R.drawable.tiao_bg_single);
        img_dropdown.setBackgroundResource(R.drawable.img_drop_down);
        isDropDown = false;

    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        if (isRegFilter) {
            mContext.unregisterReceiver(mReceiver);
            isRegFilter = false;
        }
    }

    public void showRecordType() {
        change_record_type.setBackgroundResource(R.drawable.tiao_bg_up);
        progressBar_record_type.setVisibility(RelativeLayout.GONE);
        record_type_radio.setVisibility(RelativeLayout.VISIBLE);
        radio_one.setEnabled(true);
        radio_two.setEnabled(true);
        radio_three.setEnabled(true);
    }

    public void showProgress_record_type() {
        change_record_type.setBackgroundResource(R.drawable.tiao_bg_single);
        progressBar_record_type.setVisibility(RelativeLayout.VISIBLE);
        record_type_radio.setVisibility(RelativeLayout.GONE);
    }

    public void showRecordTime() {
        change_record_time.setVisibility(RelativeLayout.VISIBLE);
        record_time_radio.setVisibility(RelativeLayout.VISIBLE);
        progressBar_record_time.setVisibility(RelativeLayout.GONE);
        if (isSupportPreRecored == true) {
            change_pre_record.setVisibility(RelativeLayout.VISIBLE);
        } else {
            change_pre_record.setVisibility(RelativeLayout.GONE);
        }
    }

    public void showProgress_record_time() {
        change_record_time.setVisibility(RelativeLayout.VISIBLE);
        record_time_radio.setVisibility(RelativeLayout.VISIBLE);
        progressBar_record_time.setVisibility(RelativeLayout.VISIBLE);
    }

    public void showPlanTime() {
        change_pre_record.setVisibility(RelativeLayout.GONE);
        change_record.setVisibility(RelativeLayout.GONE);
//		time_picker.setVisibility(RelativeLayout.VISIBLE);
//		change_plan_time.setVisibility(RelativeLayout.VISIBLE);
        change_plan_time_title.setVisibility(RelativeLayout.VISIBLE);
//		progressBar_plan_time.setVisibility(RelativeLayout.GONE);
        time_text.setVisibility(RelativeLayout.VISIBLE);

    }

    public void showProgress_plan_time() {
        time_picker.setVisibility(RelativeLayout.VISIBLE);
        change_plan_time.setVisibility(RelativeLayout.VISIBLE);
        change_plan_time.setEnabled(false);
        progressBar_plan_time.setVisibility(RelativeLayout.VISIBLE);
        time_text.setVisibility(RelativeLayout.GONE);
    }

    public void showManual() {
        change_record.setVisibility(RelativeLayout.VISIBLE);
        change_pre_record.setVisibility(RelativeLayout.GONE);
    }

    public void showPreRecordProgress() {
        progressBar_pre_record.setVisibility(ProgressBar.VISIBLE);
        pre_record_img.setVisibility(ImageView.GONE);
    }

    public void showPreRecordImg() {
        progressBar_pre_record.setVisibility(ProgressBar.GONE);
        pre_record_img.setVisibility(ImageView.VISIBLE);
    }

    public void hideRecordTime() {
        change_record_time.setVisibility(RelativeLayout.GONE);
        record_time_radio.setVisibility(RelativeLayout.GONE);
    }

    public void hidePlanTime() {
        change_plan_time_title.setVisibility(RelativeLayout.GONE);
        change_plan_time.setVisibility(RelativeLayout.GONE);
        time_picker.setVisibility(RelativeLayout.GONE);
    }

    public void hideManual() {
        change_record.setVisibility(RelativeLayout.GONE);
    }

    public void showRecordSwitchProgress() {
        progressBar_record.setVisibility(RelativeLayout.VISIBLE);
        record_img.setVisibility(RelativeLayout.GONE);
    }

    public void showRecordSwitchImg() {
        progressBar_record.setVisibility(RelativeLayout.GONE);
        record_img.setVisibility(RelativeLayout.VISIBLE);
    }

    public void showSDImg() {
        format_icon.setVisibility(ImageView.VISIBLE);
        progress_format.setVisibility(progress_format.GONE);
        sd_format.setClickable(true);
    }

    public void showSDProgress() {
        format_icon.setVisibility(ImageView.GONE);
        progress_format.setVisibility(progress_format.VISIBLE);
        sd_format.setClickable(false);
    }

    public String createCommand(String bCommandType, String bOption,
                                String SDCardCounts) {
        return bCommandType + bOption + SDCardCounts;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent it = new Intent();
        it.setAction(Constants.Action.CONTROL_BACK);
        mContext.sendBroadcast(it);
    }

    @Override
    public boolean onTouch(View arg0, MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_UP) {
            scroll_view.requestDisallowInterceptTouchEvent(false);
        } else {
            scroll_view.requestDisallowInterceptTouchEvent(true);
        }
        return false;
    }
}
