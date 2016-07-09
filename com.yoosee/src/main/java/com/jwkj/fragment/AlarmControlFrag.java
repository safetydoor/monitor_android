package com.jwkj.fragment;

import android.R.bool;
import android.R.integer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yoosee.R;
import com.jwkj.CallActivity;
import com.jwkj.activity.AlarmPushAccountActivity;
import com.jwkj.activity.MainControlActivity;
import com.jwkj.activity.ModifyAlarmIdActivity;
import com.jwkj.activity.ModifyBoundEmailActivity;
import com.jwkj.data.Contact;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.global.NpcCommon;
import com.jwkj.global.Constants.P2P;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.widget.NormalDialog;
import com.lib.addBar.AddBar;
import com.lib.addBar.OnItemChangeListener;
import com.lib.addBar.OnLeftIconClickListener;
import com.p2p.core.P2PHandler;

public class AlarmControlFrag extends BaseFragment implements OnClickListener {
    private Context mContext;
    private Contact contact;

    RelativeLayout change_buzzer, change_motion, change_email, add_alarm_item,
            change_pir, alarm_input_switch, alarm_out_switch;
    LinearLayout buzzer_time;
    RadioButton radio_one, radio_two, radio_three;
    ProgressBar progressBar, progressBar_motion, progressBar_email,
            progressBar_alarmId, progressBar_pir, progressBar_alarm_input,
            progressBar_alarm_out, progressBar_receive_alarm;
    ImageView buzzer_img, motion_img, icon_add_alarm_id, pir_img,
            img_alarm_input, img_alarm_out, img_receive_alarm;
    AddBar addBar;
    int buzzer_switch;
    int motion_switch;
    private boolean isRegFilter = false;

    TextView email_text, alarmId_text;
    NormalDialog dialog_loading;
    String[] last_bind_data;
    int cur_modify_buzzer_state;
    int cur_modify_motion_state;
    int infrared_switch;
    int modify_infrared_state;
    boolean current_infrared_state;
    boolean isOpenWriedAlarmInput;
    boolean isOpenWriedAlarmOut;
    boolean isReceiveAlarm = true;
    RelativeLayout layout_alarm_switch;
    String[] new_data;
    int max_alarm_count;
    int lamp_switch;
    int cur_modify_lamp_state;
    String sendEmail = "";
    String emailRobot = "";
    String emailPwd = "";
    boolean isSurportSMTP = false;
    boolean isEmailLegal = true;
    boolean isEmailChecked = false;
    String idOrIp;// 如果是局域网用ip，不是局域网用id
    // boolean isRET=false;
    String senderEmail;
    int encrypt;
    int smtpport;
    boolean isSupportManual = false;
    private int connectType = 0;

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
        connectType = getArguments().getInt("connectType");
        View view = inflater.inflate(R.layout.fragment_alarm_control,
                container, false);
        initComponent(view);
        showProgress();
        showProgress_motion();
        regFilter();
        // showProgress_alarmId();
        idOrIp = contact.contactId;
        if (contact.ipadressAddress != null) {
            String mark = contact.ipadressAddress.getHostAddress();
            Log.e("callId", "mark=" + mark);
            String ip = mark.substring(mark.lastIndexOf(".") + 1, mark.length());
            if (!ip.equals("") && ip != null) {
                idOrIp = ip;
            }
        }
        Log.e("callId", "contact.contactId=" + idOrIp);
        P2PHandler.getInstance()
                .getNpcSettings(idOrIp, contact.contactPassword);
        P2PHandler.getInstance()
                .getBindAlarmId(idOrIp, contact.contactPassword);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        showProgress_email();
        P2PHandler.getInstance().getAlarmEmail(idOrIp,
                contact.contactPassword);
    }

    public void initComponent(View view) {
        change_buzzer = (RelativeLayout) view.findViewById(R.id.change_buzzer);
        buzzer_img = (ImageView) view.findViewById(R.id.buzzer_img);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        buzzer_time = (LinearLayout) view.findViewById(R.id.buzzer_time);

        change_motion = (RelativeLayout) view.findViewById(R.id.change_motion);
        motion_img = (ImageView) view.findViewById(R.id.motion_img);
        progressBar_motion = (ProgressBar) view
                .findViewById(R.id.progressBar_motion);

        radio_one = (RadioButton) view.findViewById(R.id.radio_one);
        radio_two = (RadioButton) view.findViewById(R.id.radio_two);
        radio_three = (RadioButton) view.findViewById(R.id.radio_three);

        change_email = (RelativeLayout) view.findViewById(R.id.change_email);
        email_text = (TextView) view.findViewById(R.id.email_text);
        progressBar_email = (ProgressBar) view
                .findViewById(R.id.progressBar_email);

        add_alarm_item = (RelativeLayout) view
                .findViewById(R.id.add_alarm_item);
        change_pir = (RelativeLayout) view.findViewById(R.id.change_pir);
        pir_img = (ImageView) view.findViewById(R.id.pir_img);
        progressBar_pir = (ProgressBar) view.findViewById(R.id.progressBar_pir);

        alarm_input_switch = (RelativeLayout) view
                .findViewById(R.id.alarm_input_switch);
        img_alarm_input = (ImageView) view.findViewById(R.id.img_alarm_input);
        progressBar_alarm_input = (ProgressBar) view
                .findViewById(R.id.progressBar_alarm_input);

        alarm_out_switch = (RelativeLayout) view
                .findViewById(R.id.alarm_out_switch);
        img_alarm_out = (ImageView) view.findViewById(R.id.img_alarm_out);
        progressBar_alarm_out = (ProgressBar) view
                .findViewById(R.id.progressBar_alarm_out);
        img_receive_alarm = (ImageView) view
                .findViewById(R.id.img_receive_alarm);
        layout_alarm_switch = (RelativeLayout) view
                .findViewById(R.id.layout_alarm_switch);
        progressBar_receive_alarm = (ProgressBar) view
                .findViewById(R.id.progressBar_receive_alarm);
        add_alarm_item.setOnClickListener(this);
        change_email.setOnClickListener(this);
        change_motion.setOnClickListener(this);
        change_buzzer.setOnClickListener(this);
        radio_one.setOnClickListener(this);
        radio_two.setOnClickListener(this);
        radio_three.setOnClickListener(this);
        change_pir.setOnClickListener(this);
        alarm_input_switch.setOnClickListener(this);
        alarm_out_switch.setOnClickListener(this);
        layout_alarm_switch.setOnClickListener(this);
        layout_alarm_switch.setClickable(false);
        add_alarm_item.setClickable(false);
//		if (NpcCommon.mThreeNum.equals("0517401")) {
//			layout_alarm_switch.setVisibility(RelativeLayout.GONE);
//		}
        // AP模式部分功能隐藏
        if (connectType == CallActivity.P2PCONECT) {
            layout_alarm_switch.setVisibility(View.VISIBLE);
            add_alarm_item.setVisibility(View.VISIBLE);
            change_email.setVisibility(View.VISIBLE);
        } else {
            layout_alarm_switch.setVisibility(View.GONE);
            add_alarm_item.setVisibility(View.GONE);
            change_email.setVisibility(View.GONE);
        }
    }

    public void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.P2P.ACK_RET_GET_NPC_SETTINGS);

        filter.addAction(Constants.P2P.ACK_RET_SET_BIND_ALARM_ID);
        filter.addAction(Constants.P2P.ACK_RET_GET_BIND_ALARM_ID);
        filter.addAction(Constants.P2P.RET_SET_BIND_ALARM_ID);
        filter.addAction(Constants.P2P.RET_GET_BIND_ALARM_ID);

        // filter.addAction(Constants.P2P.ACK_RET_SET_ALARM_EMAIL);
        filter.addAction(Constants.P2P.ACK_RET_GET_ALARM_EMAIL);
        // filter.addAction(Constants.P2P.RET_SET_ALARM_EMAIL);
        // filter.addAction(Constants.P2P.RET_GET_ALARM_EMAIL);
        filter.addAction(Constants.P2P.RET_GET_ALARM_EMAIL_WITHSMTP);

        filter.addAction(Constants.P2P.ACK_RET_SET_MOTION);
        filter.addAction(Constants.P2P.RET_SET_MOTION);
        filter.addAction(Constants.P2P.RET_GET_MOTION);

        filter.addAction(Constants.P2P.ACK_RET_SET_BUZZER);
        filter.addAction(Constants.P2P.RET_SET_BUZZER);
        filter.addAction(Constants.P2P.RET_GET_BUZZER);
        filter.addAction(Constants.P2P.RET_GET_INFRARED_SWITCH);
        filter.addAction(Constants.P2P.ACK_RET_SET_INFRARED_SWITCH);
        filter.addAction(Constants.P2P.RET_GET_WIRED_ALARM_INPUT);
        filter.addAction(Constants.P2P.RET_GET_WIRED_ALARM_OUT);
        filter.addAction(Constants.P2P.ACK_RET_SET_WIRED_ALARM_INPUT);
        filter.addAction(Constants.P2P.ACK_RET_SET_WIRED_ALARM_OUT);

        mContext.registerReceiver(mReceiver, filter);
        isRegFilter = true;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            if (intent.getAction().equals(Constants.P2P.RET_GET_BIND_ALARM_ID)) {
                showImg_receive_alarm();
                String[] data = intent.getStringArrayExtra("data");
                int max_count = intent.getIntExtra("max_count", 0);
                last_bind_data = data;
                max_alarm_count = max_count;
                showAlarmIdState();
                layout_alarm_switch.setClickable(true);
                add_alarm_item.setClickable(true);
                int count = 0;
                for (int i = 0; i < data.length; i++) {
                    if (data[i].equals(NpcCommon.mThreeNum)) {
                        img_receive_alarm
                                .setBackgroundResource(R.drawable.ic_checkbox_on);
                        isReceiveAlarm = false;
                        count = count + 1;
                        return;
                    }
                }
                if (count == 0) {
                    img_receive_alarm
                            .setBackgroundResource(R.drawable.ic_checkbox_off);
                    isReceiveAlarm = true;
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_SET_BIND_ALARM_ID)) {
                int result = intent.getIntExtra("result", -1);
                if (null != dialog_loading && dialog_loading.isShowing()) {
                    dialog_loading.dismiss();
                    dialog_loading = null;
                }
                if (result == Constants.P2P_SET.BIND_ALARM_ID_SET.SETTING_SUCCESS) {
                    P2PHandler.getInstance().getBindAlarmId(idOrIp,
                            contact.contactPassword);
                    T.showShort(mContext, R.string.modify_success);
                } else {
                    if (getIsRun()) {
                        T.showShort(mContext, R.string.operator_error);
                    }
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_GET_ALARM_EMAIL)) {
                // isRET=true;
                String email = intent.getStringExtra("email");
                int result = intent.getIntExtra("result", 0);
                getSMTPMessage(result);
                if (email.equals("") || email.equals("0")) {
                    email_text.setText(R.string.unbound);
                } else {
                    email_text.setText(email);
                }
                showEmailState();
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_GET_ALARM_EMAIL_WITHSMTP)) {
                // isRET=true;
                String contectid = intent.getStringExtra("contectid");
                Log.i("dxsemail", "contectid-->" + contectid);
                if (contectid != null && contectid.equals(idOrIp)) {
                    String email = intent.getStringExtra("email");
                    encrypt = intent.getIntExtra("encrypt", -1);
                    String[] SmptMessage = intent
                            .getStringArrayExtra("SmptMessage");
                    int result = intent.getIntExtra("result", 0);
                    int isSupport = intent.getIntExtra("isSupport", -1);
                    if (isSupport == 1) {
                        isSupportManual = true;
                    } else {
                        isSupportManual = false;
                    }
                    getSMTPMessage(result);
                    // sendEmail = SmptMessage[1];
                    sendEmail = email;
                    emailRobot = SmptMessage[0];
                    emailPwd = SmptMessage[2];
                    senderEmail = SmptMessage[1];
                    smtpport = intent.getIntExtra("smtpport", -1);
                    if (email.equals("") || email.equals("0")) {
                        email_text.setText(R.string.unbound);
                    } else {
                        email_text.setText(email);
                    }
                    showEmailState();
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_SET_ALARM_EMAIL)) {
                // P2PHandler.getInstance().getAlarmEmail(contact.contactId,
                // contact.contactPassword);

            } else if (intent.getAction().equals(Constants.P2P.RET_GET_MOTION)) {
                int state = intent.getIntExtra("motionState", -1);
                if (state == Constants.P2P_SET.MOTION_SET.MOTION_DECT_ON) {
                    motion_switch = Constants.P2P_SET.MOTION_SET.MOTION_DECT_ON;
                    motion_img.setBackgroundResource(R.drawable.ic_checkbox_on);
                } else {
                    motion_switch = Constants.P2P_SET.MOTION_SET.MOTION_DECT_OFF;
                    motion_img
                            .setBackgroundResource(R.drawable.ic_checkbox_off);
                }
                showMotionState();
            } else if (intent.getAction().equals(Constants.P2P.RET_SET_MOTION)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.MOTION_SET.SETTING_SUCCESS) {
                    if (cur_modify_motion_state == Constants.P2P_SET.MOTION_SET.MOTION_DECT_ON) {
                        motion_switch = Constants.P2P_SET.MOTION_SET.MOTION_DECT_ON;
                        motion_img
                                .setBackgroundResource(R.drawable.ic_checkbox_on);
                    } else {
                        motion_switch = Constants.P2P_SET.MOTION_SET.MOTION_DECT_OFF;
                        motion_img
                                .setBackgroundResource(R.drawable.ic_checkbox_off);
                    }
                    showMotionState();
                    T.showShort(mContext, R.string.modify_success);
                } else {
                    showMotionState();
                    T.showShort(mContext, R.string.operator_error);
                }
            } else if (intent.getAction().equals(Constants.P2P.RET_GET_BUZZER)) {
                int state = intent.getIntExtra("buzzerState", -1);
                updateBuzzer(state);
                showBuzzerTime();
            } else if (intent.getAction().equals(Constants.P2P.RET_SET_BUZZER)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.BUZZER_SET.SETTING_SUCCESS) {
                    updateBuzzer(cur_modify_buzzer_state);
                    showBuzzerTime();
                    T.showShort(mContext, R.string.modify_success);
                } else {
                    showBuzzerTime();
                    T.showShort(mContext, R.string.operator_error);
                }
            } else if (intent.getAction().equals(
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
                    Constants.P2P.ACK_RET_SET_BIND_ALARM_ID)) {
                int result = intent.getIntExtra("result", -1);

                if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                    if (null != dialog_loading && dialog_loading.isShowing()) {
                        dialog_loading.dismiss();
                        dialog_loading = null;
                    }
                    Intent i = new Intent();
                    i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);
                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my", "net error resend:set alarm bind id");
                    P2PHandler.getInstance().setBindAlarmId(idOrIp,
                            contact.contactPassword, new_data.length, new_data);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_RET_GET_BIND_ALARM_ID)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                    Intent i = new Intent();
                    i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);
                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my", "net error resend:get alarm bind id");
                    P2PHandler.getInstance().getBindAlarmId(idOrIp,
                            contact.contactPassword);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_RET_GET_ALARM_EMAIL)) {
                int result = intent.getIntExtra("result", -1);
                // if(isRET){
                // //如果接收到数据则不处理此处数据，原因是运行过程中运行异常，收到邮箱数据之后任然返回错误，导致不停GET
                // return;
                // }
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                    Intent i = new Intent();
                    i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);
                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my", "net error resend:get alarm email");
                    P2PHandler.getInstance().getAlarmEmail(idOrIp,
                            contact.contactPassword);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_RET_SET_MOTION)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                    Intent i = new Intent();
                    i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);
                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my", "net error resend:set npc settings motion");
                    P2PHandler.getInstance().setMotion(idOrIp,
                            contact.contactPassword, cur_modify_motion_state);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_RET_SET_BUZZER)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                    Intent i = new Intent();
                    i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);
                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my", "net error resend:set npc settings buzzer");
                    P2PHandler.getInstance().setBuzzer(idOrIp,
                            contact.contactPassword, cur_modify_buzzer_state);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_GET_INFRARED_SWITCH)) {
                int state = intent.getIntExtra("state", -1);
                if (state == Constants.P2P_SET.INFRARED_SWITCH.INFRARED_SWITCH_ON) {
                    change_pir.setVisibility(RelativeLayout.VISIBLE);
                    current_infrared_state = false;
                    pir_img.setBackgroundResource(R.drawable.ic_checkbox_on);
                } else if (state == Constants.P2P_SET.INFRARED_SWITCH.INFRARED_SWITCH_OFF) {
                    change_pir.setVisibility(RelativeLayout.VISIBLE);
                    current_infrared_state = true;
                    pir_img.setBackgroundResource(R.drawable.ic_checkbox_off);
                }
                showImg_infrared_switch();
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_RET_SET_INFRARED_SWITCH)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    if (current_infrared_state == true) {
                        P2PHandler.getInstance().setInfraredSwitch(idOrIp,
                                contact.contactPassword, 1);
                    } else {
                        P2PHandler.getInstance().setInfraredSwitch(idOrIp,
                                contact.contactPassword, 0);
                    }
                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_SUCCESS) {
                    if (current_infrared_state == true) {
                        current_infrared_state = false;
                        pir_img.setBackgroundResource(R.drawable.ic_checkbox_on);
                    } else {
                        current_infrared_state = true;
                        pir_img.setBackgroundResource(R.drawable.ic_checkbox_off);
                    }
                    showImg_infrared_switch();
                }

            } else if (intent.getAction().equals(
                    Constants.P2P.RET_GET_WIRED_ALARM_INPUT)) {
                int state = intent.getIntExtra("state", -1);
                if (state == Constants.P2P_SET.WIRED_ALARM_INPUT.ALARM_INPUT_ON) {
                    alarm_input_switch.setVisibility(RelativeLayout.VISIBLE);
                    isOpenWriedAlarmInput = false;
                    img_alarm_input
                            .setBackgroundResource(R.drawable.ic_checkbox_on);
                } else if (state == Constants.P2P_SET.WIRED_ALARM_INPUT.ALARM_INPUT_OFF) {
                    alarm_input_switch.setVisibility(RelativeLayout.VISIBLE);
                    isOpenWriedAlarmInput = true;
                    img_alarm_input
                            .setBackgroundResource(R.drawable.ic_checkbox_off);
                }
                showImg_wired_alarm_input();
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_GET_WIRED_ALARM_OUT)) {
                int state = intent.getIntExtra("state", -1);
                if (state == Constants.P2P_SET.WIRED_ALARM_OUT.ALARM_OUT_ON) {
                    alarm_out_switch.setVisibility(RelativeLayout.VISIBLE);
                    isOpenWriedAlarmOut = false;
                    img_alarm_out
                            .setBackgroundResource(R.drawable.ic_checkbox_on);
                } else if (state == Constants.P2P_SET.WIRED_ALARM_OUT.ALARM_OUT_OFF) {
                    alarm_out_switch.setVisibility(RelativeLayout.VISIBLE);
                    isOpenWriedAlarmOut = true;
                    img_alarm_out
                            .setBackgroundResource(R.drawable.ic_checkbox_off);
                }
                showImg_wired_alarm_out();
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_RET_SET_WIRED_ALARM_INPUT)) {
                int result = intent.getIntExtra("state", -1);
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    if (isOpenWriedAlarmInput == true) {
                        P2PHandler.getInstance().setWiredAlarmInput(idOrIp,
                                contact.contactPassword, 1);
                    } else {
                        P2PHandler.getInstance().setWiredAlarmInput(idOrIp,
                                contact.contactPassword, 0);
                    }

                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_SUCCESS) {
                    if (isOpenWriedAlarmInput == true) {
                        isOpenWriedAlarmInput = false;
                        img_alarm_input
                                .setBackgroundResource(R.drawable.ic_checkbox_on);
                    } else {
                        isOpenWriedAlarmInput = true;
                        img_alarm_input
                                .setBackgroundResource(R.drawable.ic_checkbox_off);
                    }
                    showImg_wired_alarm_input();
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_RET_SET_WIRED_ALARM_OUT)) {
                int result = intent.getIntExtra("state", -1);
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    if (isOpenWriedAlarmOut == true) {
                        P2PHandler.getInstance().setWiredAlarmOut(idOrIp,
                                contact.contactPassword, 1);
                    } else {
                        P2PHandler.getInstance().setWiredAlarmOut(idOrIp,
                                contact.contactPassword, 0);
                    }

                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_SUCCESS) {
                    if (isOpenWriedAlarmOut == true) {
                        isOpenWriedAlarmOut = false;
                        img_alarm_out
                                .setBackgroundResource(R.drawable.ic_checkbox_on);
                    } else {
                        isOpenWriedAlarmOut = true;
                        img_alarm_out
                                .setBackgroundResource(R.drawable.ic_checkbox_off);
                    }
                    showImg_wired_alarm_out();
                }
            }
        }
    };

    private void getSMTPMessage(int result) {
        Log.i("dxsalarm", "result------>" + result);
        if ((byte) ((result >> 1) & (0x1)) == 0) {
            isSurportSMTP = false;
        } else {
            isSurportSMTP = true;
            if ((byte) ((result >> 4) & (0x1)) == 0) {
                isEmailChecked = true;
                if ((byte) ((result >> 2) & (0x1)) == 0) {
                    isEmailLegal = false;
                } else {
                    isEmailLegal = true;
                }
            } else {
                isEmailChecked = false;
            }

        }
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        int resId = view.getId();
        if (resId == R.id.change_email) {
            Intent modify_email = new Intent(mContext,
                    ModifyBoundEmailActivity.class);
            modify_email.putExtra("contact", contact);
            modify_email.putExtra("email", email_text.getText().toString());
            modify_email.putExtra("sendEmail", sendEmail);
            modify_email.putExtra("emailRoot", emailRobot);
            modify_email.putExtra("emailPwd", emailPwd);
            modify_email.putExtra("isEmailLegal", isEmailLegal);
            modify_email.putExtra("isSurportSMTP", isSurportSMTP);
            modify_email.putExtra("isEmailChecked", isEmailChecked);
            modify_email.putExtra("senderEmail", senderEmail);
            modify_email.putExtra("encrypt", encrypt);
            modify_email.putExtra("smtpport", smtpport);
            modify_email.putExtra("isSupportManual", isSupportManual);
            mContext.startActivity(modify_email);
        } else if (resId == R.id.add_alarm_item) {
            if (last_bind_data.length <= 0
                    || (last_bind_data[0].equals("0") && last_bind_data.length == 1)) {
                T.showShort(mContext, R.string.no_alarm_account);
            } else {
                Intent it = new Intent(mContext, AlarmPushAccountActivity.class);
                it.putExtra("contactId", contact.contactId);
                it.putExtra("contactPassword", contact.contactPassword);
                startActivity(it);
            }
        } else if (resId == R.id.change_buzzer) {
            showProgress();
            if (buzzer_switch != Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_OFF) {
                cur_modify_buzzer_state = Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_OFF;
            } else {
                cur_modify_buzzer_state = Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_ON_ONE_MINUTE;
            }
            P2PHandler.getInstance().setBuzzer(idOrIp, contact.contactPassword,
                    cur_modify_buzzer_state);
        } else if (resId == R.id.change_motion) {
            showProgress_motion();
            if (motion_switch != Constants.P2P_SET.MOTION_SET.MOTION_DECT_OFF) {
                cur_modify_motion_state = Constants.P2P_SET.MOTION_SET.MOTION_DECT_OFF;
                P2PHandler.getInstance().setMotion(idOrIp,
                        contact.contactPassword, cur_modify_motion_state);
            } else {
                cur_modify_motion_state = Constants.P2P_SET.MOTION_SET.MOTION_DECT_ON;
                P2PHandler.getInstance().setMotion(idOrIp,
                        contact.contactPassword, cur_modify_motion_state);
            }
        } else if (resId == R.id.radio_one) {
            showProgress();
            cur_modify_buzzer_state = Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_ON_ONE_MINUTE;
            P2PHandler.getInstance().setBuzzer(idOrIp, contact.contactPassword,
                    cur_modify_buzzer_state);
        } else if (resId == R.id.radio_two) {
            showProgress();
            cur_modify_buzzer_state = Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_ON_TWO_MINUTE;
            P2PHandler.getInstance().setBuzzer(idOrIp, contact.contactPassword,
                    cur_modify_buzzer_state);
        } else if (resId == R.id.radio_three) {
            showProgress();
            cur_modify_buzzer_state = Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_ON_THREE_MINUTE;
            P2PHandler.getInstance().setBuzzer(idOrIp, contact.contactPassword,
                    cur_modify_buzzer_state);
        } else if (resId == R.id.change_pir) {
            showProgress_infrares_switch();
            if (current_infrared_state == true) {
                modify_infrared_state = Constants.P2P_SET.INFRARED_SWITCH.INFRARED_SWITCH_ON;
                P2PHandler.getInstance().setInfraredSwitch(idOrIp,
                        contact.contactPassword, modify_infrared_state);
            } else {
                modify_infrared_state = Constants.P2P_SET.INFRARED_SWITCH.INFRARED_SWITCH_OFF;
                P2PHandler.getInstance().setInfraredSwitch(idOrIp,
                        contact.contactPassword, modify_infrared_state);
            }
        } else if (resId == R.id.alarm_input_switch) {
            showProgress_wired_alarm_input();
            if (isOpenWriedAlarmInput == true) {
                P2PHandler.getInstance().setWiredAlarmInput(idOrIp,
                        contact.contactPassword, 1);
            } else {
                P2PHandler.getInstance().setWiredAlarmInput(idOrIp,
                        contact.contactPassword, 0);
            }
        } else if (resId == R.id.alarm_out_switch) {
            showProgress_wired_alarm_out();
            if (isOpenWriedAlarmOut == true) {
                P2PHandler.getInstance().setWiredAlarmOut(idOrIp,
                        contact.contactPassword, 1);
            } else {
                P2PHandler.getInstance().setWiredAlarmOut(idOrIp,
                        contact.contactPassword, 0);
            }
        } else if (resId == R.id.layout_alarm_switch) {
            showProgress_receive_alarm();
            if (isReceiveAlarm == true) {
                if (last_bind_data.length >= max_alarm_count) {
                    T.showShort(mContext, R.string.alarm_push_limit);
                    showImg_receive_alarm();
                    return;
                }
                new_data = new String[last_bind_data.length + 1];
                for (int i = 0; i < last_bind_data.length; i++) {
                    new_data[i] = last_bind_data[i];
                }
                new_data[new_data.length - 1] = NpcCommon.mThreeNum;
                // last_bind_data=new_data;
                P2PHandler.getInstance().setBindAlarmId(idOrIp,
                        contact.contactPassword, new_data.length, new_data);
            } else {
                new_data = new String[last_bind_data.length - 1];
                int count = 0;
                for (int i = 0; i < last_bind_data.length; i++) {
                    if (!last_bind_data[i].equals(NpcCommon.mThreeNum)) {
                        new_data[count] = last_bind_data[i];
                        count++;
                    }
                }
                if (new_data.length == 0) {
                    new_data = new String[]{"0"};
                }
                // last_bind_data=new_data;
                P2PHandler.getInstance().setBindAlarmId(idOrIp,
                        contact.contactPassword, new_data.length, new_data);
            }
        }
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

    public void updateBuzzer(int state) {
        if (state == Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_ON_ONE_MINUTE) {
            buzzer_switch = Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_ON_ONE_MINUTE;
            buzzer_img.setBackgroundResource(R.drawable.ic_checkbox_on);
            change_buzzer.setBackgroundResource(R.drawable.tiao_bg_up);
            buzzer_time.setVisibility(RelativeLayout.VISIBLE);
            radio_one.setChecked(true);
        } else if (state == Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_ON_TWO_MINUTE) {
            buzzer_switch = Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_ON_TWO_MINUTE;
            buzzer_img.setBackgroundResource(R.drawable.ic_checkbox_on);
            change_buzzer.setBackgroundResource(R.drawable.tiao_bg_up);
            buzzer_time.setVisibility(RelativeLayout.VISIBLE);
            radio_two.setChecked(true);
        } else if (state == Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_ON_THREE_MINUTE) {
            buzzer_switch = Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_ON_THREE_MINUTE;
            buzzer_img.setBackgroundResource(R.drawable.ic_checkbox_on);
            change_buzzer.setBackgroundResource(R.drawable.tiao_bg_up);
            buzzer_time.setVisibility(RelativeLayout.VISIBLE);
            radio_three.setChecked(true);
        } else {
            buzzer_switch = Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_OFF;
            buzzer_img.setBackgroundResource(R.drawable.ic_checkbox_off);
            change_buzzer.setBackgroundResource(R.drawable.tiao_bg_single);
            buzzer_time.setVisibility(RelativeLayout.GONE);
        }
    }

    public void showProgress() {
        progressBar.setVisibility(RelativeLayout.VISIBLE);
        buzzer_img.setVisibility(RelativeLayout.GONE);
        change_buzzer.setEnabled(false);
        radio_one.setEnabled(false);
        radio_two.setEnabled(false);
        radio_three.setEnabled(false);
    }

    public void showBuzzerTime() {
        progressBar.setVisibility(RelativeLayout.GONE);
        buzzer_img.setVisibility(RelativeLayout.VISIBLE);
        change_buzzer.setEnabled(true);
        radio_one.setEnabled(true);
        radio_two.setEnabled(true);
        radio_three.setEnabled(true);
    }

    public void showMotionState() {
        progressBar_motion.setVisibility(RelativeLayout.GONE);
        motion_img.setVisibility(RelativeLayout.VISIBLE);
        change_motion.setEnabled(true);
    }

    public void showProgress_motion() {
        progressBar_motion.setVisibility(RelativeLayout.VISIBLE);
        motion_img.setVisibility(RelativeLayout.GONE);
        change_motion.setEnabled(false);
    }

    public void showEmailState() {
        progressBar_email.setVisibility(RelativeLayout.GONE);
        email_text.setVisibility(RelativeLayout.VISIBLE);
        change_email.setEnabled(true);
    }

    public void showProgress_email() {
        progressBar_email.setVisibility(RelativeLayout.VISIBLE);
        email_text.setVisibility(RelativeLayout.GONE);
        change_email.setEnabled(false);
    }

    public void showAlarmIdState() {
        // progressBar_alarmId.setVisibility(RelativeLayout.GONE);
        // icon_add_alarm_id.setVisibility(RelativeLayout.VISIBLE);
        // add_alarm_item.setEnabled(true);
    }

    public void showProgress_alarmId() {
        // progressBar_alarmId.setVisibility(RelativeLayout.VISIBLE);
        // icon_add_alarm_id.setVisibility(RelativeLayout.GONE);
        // add_alarm_item.setEnabled(false);
    }

    public void showProgress_infrares_switch() {
        progressBar_pir.setVisibility(ProgressBar.VISIBLE);
        pir_img.setVisibility(ImageView.GONE);
    }

    public void showImg_infrared_switch() {
        progressBar_pir.setVisibility(progressBar.GONE);
        pir_img.setVisibility(ImageView.VISIBLE);
    }

    public void showProgress_wired_alarm_input() {
        progressBar_alarm_input.setVisibility(ProgressBar.VISIBLE);
        img_alarm_input.setVisibility(ImageView.GONE);
    }

    public void showImg_wired_alarm_input() {
        progressBar_alarm_input.setVisibility(ProgressBar.GONE);
        img_alarm_input.setVisibility(ImageView.VISIBLE);
    }

    public void showProgress_wired_alarm_out() {
        progressBar_alarm_out.setVisibility(ProgressBar.VISIBLE);
        img_alarm_out.setVisibility(ImageView.GONE);
    }

    public void showImg_wired_alarm_out() {
        progressBar_alarm_out.setVisibility(ProgressBar.GONE);
        img_alarm_out.setVisibility(ImageView.VISIBLE);
    }

    public void showProgress_receive_alarm() {
        progressBar_receive_alarm.setVisibility(ProgressBar.VISIBLE);
        img_receive_alarm.setVisibility(progressBar.GONE);
    }

    public void showImg_receive_alarm() {
        progressBar_receive_alarm.setVisibility(ProgressBar.GONE);
        img_receive_alarm.setVisibility(progressBar.VISIBLE);
    }

    @Override
    public void onDestroy() {
        Intent it = new Intent();
        it.setAction(Constants.Action.CONTROL_BACK);
        mContext.sendBroadcast(it);
        super.onDestroy();
    }
}
