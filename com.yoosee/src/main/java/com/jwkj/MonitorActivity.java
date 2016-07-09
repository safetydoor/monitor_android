package com.jwkj;

import java.io.File;
import java.util.List;

import android.R.bool;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.yoosee.R;
import com.jwkj.activity.DoorBellNewActivity;
import com.jwkj.activity.MainActivity;
import com.jwkj.data.Contact;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.global.NpcCommon;

import com.jwkj.global.MyApp;
import com.jwkj.utils.PhoneWatcher;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.widget.MLVerticalSeekBar;
import com.jwkj.widget.MyInputDialog;
import com.jwkj.widget.MyInputPassDialog;
import com.jwkj.widget.NormalDialog;
import com.jwkj.widget.MyInputPassDialog.OnCustomDialogListener;
import com.jwkj.widget.VerticalSeekBar;
import com.jwkj.widget.VerticalSeekBar.OnVerticalSeekBarChangeListener;
import com.p2p.core.BaseMonitorActivity;
import com.p2p.core.BaseP2PView;
import com.p2p.core.MediaPlayer;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;
import com.p2p.core.P2PView;
import com.p2p.core.utils.MyUtils;

public class MonitorActivity extends BaseMonitorActivity implements
        OnClickListener, OnLongClickListener, OnSeekBarChangeListener {
    Context mContext;
    boolean isRegFilter = false;
    boolean mIsCloseVoice = false;
    int type;
    ImageView screenshot, hungup, close_voice, send_voice, img_reverse,
            steering_wheel, open_door, defence_state;
    AudioManager mAudioManager = null;
    int mCurrentVolume, mMaxVolume;
    PhoneWatcher mPhoneWatcher;
    RelativeLayout control_bottom;
    LinearLayout layout_voice_state;
    LinearLayout control_top;
    ImageView voice_state;
    TextView video_mode_hd, video_mode_sd, video_mode_ld;
    TextView text_number;
    boolean isControlShow = true;
    boolean isReject = false;
    boolean isHD = false;
    boolean isShowVideo = false;
    int current_video_mode;
    String callId;
    String password;
    RelativeLayout layout_steering_wheel;
    boolean isOpenSteerWheel = true;
    Button bt_top, bt_bottom, bt_left, bt_right;
    Button choose_video_format;
    RelativeLayout tv_on1, tv_on2, tv_on3, tv_off1, tv_off2, tv_off3;
    int USR_CMD_CAR_DIR_CTL = 7;
    private final int USR_CMD_OPTION_CAR_TURN_LEFT = 0;
    private final int USR_CMD_OPTION_CAR_TURN_RIGHT = 1;
    private final int USR_CMD_OPTION_CAR_TURN_FORWARD = 2;
    private final int USR_CMD_OPTION_CAR_TURN_BACK = 3;
    private Handler mhandler = new Handler();
    boolean isSurpportOpenDoor;
    LinearLayout layout_gpio;
    RelativeLayout layout_arrow, layout_telescopic;
    ImageView img_zoom_small, img_zoom_big, img_focus_small, img_focus_big,
            img_aperture_small, img_aperture_big;
    RelativeLayout control_camera;
    SeekBar seekbar_zoom, seekbar_focus, seekbar_aperture;
    ImageView control;
    RelativeLayout l_focus;
    VerticalSeekBar seebar_focus;
    ImageView focus_add, focus_reduce;
    int[] location1 = new int[2];
    int[] location2 = new int[2];
    int[] location3 = new int[2];
    boolean isTelescopic = false;
    int height;
    int AudioType;
    boolean isSpeak = false;
    int contactType;
    ImageView img_arrow;
    boolean isPtzControl = false;
    // 灯光控制
    RelativeLayout rlLampControl;
    TextView txLamp;
    ProgressBar progressBarLamp;
    int lamp_switch;
    int cur_modify_lamp_state;
    private String NewMessageDeviceID;
    private int connectType = 0;
    private int defenceState = -1;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        P2PConnect.setPlaying(true);
        Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.p2p_monitor);
        type = this.getIntent().getIntExtra("type", -1);
        contactType = this.getIntent().getIntExtra("contactType", -1);
        callId = this.getIntent().getStringExtra("callId");
        connectType = getIntent().getIntExtra("connectType", 0);
        password = this.getIntent().getStringExtra("password");
        isSurpportOpenDoor = this.getIntent().getBooleanExtra(
                "isSurpportOpenDoor", false);
        contact = (Contact) this.getIntent().getSerializableExtra("contact");
        mContext = this;
        P2PConnect.setMonitorId(callId);// 设置在监控的ID
        SettingListener.setMonitorID(callId);// 设置在监控的ID
        initComponent();
        regFilter();
        Log.e("callId", "before callId=" + callId);
        if (contact != null && contact.ipadressAddress != null) {
            String mark = contact.ipadressAddress.getHostAddress();
            String ip = mark.substring(mark.lastIndexOf(".") + 1, mark.length());
            if (ip != null && !ip.equals("")) {
                callId = ip;
            }
        }
        Log.e("callId", "after callId=" + callId);
        BaseMonitorActivity.contactId = callId;
        BaseMonitorActivity.password = password;
        P2PHandler.getInstance().checkPassword(callId, password);
        P2PHandler.getInstance().getNpcSettings(callId, password);
        startWatcher();
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        }
        mCurrentVolume = mAudioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        mMaxVolume = mAudioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void onHomePressed() {
        // TODO Auto-generated method stub
        super.onHomePressed();
        reject();
    }

    private void startWatcher() {
        mPhoneWatcher = new PhoneWatcher(mContext);
        mPhoneWatcher
                .setOnCommingCallListener(new PhoneWatcher.OnCommingCallListener() {

                    @Override
                    public void onCommingCall() {
                        reject();
                    }

                });
        mPhoneWatcher.startWatcher();
    }

    public void initComponent() {
        pView = (P2PView) findViewById(R.id.pView);
        this.initP2PView(P2PConnect.getCurrentDeviceType());
        pView.contactId = callId;
        pView.password = password;
        BaseP2PView.contactId = callId;
        BaseP2PView.password = password;
        setMute(true);

        open_door = (ImageView) findViewById(R.id.open_door);
        screenshot = (ImageView) findViewById(R.id.screenshot);
        hungup = (ImageView) findViewById(R.id.hungup);
        close_voice = (ImageView) findViewById(R.id.close_voice);
        steering_wheel = (ImageView) findViewById(R.id.steering_wheel);
        control_bottom = (RelativeLayout) findViewById(R.id.control_bottom);
        control_top = (LinearLayout) findViewById(R.id.control_top);
        layout_voice_state = (LinearLayout) findViewById(R.id.layout_voice_state);
        send_voice = (ImageView) findViewById(R.id.send_voice);
        voice_state = (ImageView) findViewById(R.id.voice_state);
        defence_state = (ImageView) findViewById(R.id.defence_state);

        video_mode_hd = (TextView) findViewById(R.id.video_mode_hd);
        video_mode_sd = (TextView) findViewById(R.id.video_mode_sd);
        video_mode_ld = (TextView) findViewById(R.id.video_mode_ld);
        layout_steering_wheel = (RelativeLayout) findViewById(R.id.layout_steering_wheel);
        bt_top = (Button) findViewById(R.id.top_btn);
        bt_bottom = (Button) findViewById(R.id.bottom_btn);
        bt_left = (Button) findViewById(R.id.left_btn);
        bt_right = (Button) findViewById(R.id.right_btn);
        choose_video_format = (Button) findViewById(R.id.choose_video_format);

        tv_on1 = (RelativeLayout) findViewById(R.id.tv_on1);
        tv_on2 = (RelativeLayout) findViewById(R.id.tv_on2);
        tv_on3 = (RelativeLayout) findViewById(R.id.tv_on3);
        tv_off1 = (RelativeLayout) findViewById(R.id.tv_off1);
        tv_off2 = (RelativeLayout) findViewById(R.id.tv_off2);
        tv_off3 = (RelativeLayout) findViewById(R.id.tv_off3);
        layout_gpio = (LinearLayout) findViewById(R.id.layout_gpio);
        layout_telescopic = (RelativeLayout) findViewById(R.id.layout_telescopic);
        layout_arrow = (RelativeLayout) findViewById(R.id.layout_arrow);
        img_arrow = (ImageView) findViewById(R.id.img_arrow);
        layout_gpio.getBackground().setAlpha(220);
        layout_arrow.getBackground().setAlpha(220);
        text_number = (TextView) findViewById(R.id.text_number);
        img_zoom_small = (ImageView) findViewById(R.id.img_zoom_small);
        img_zoom_big = (ImageView) findViewById(R.id.img_zoom_big);
        img_focus_small = (ImageView) findViewById(R.id.img_focus_small);
        img_focus_big = (ImageView) findViewById(R.id.img_focus_big);
        img_aperture_small = (ImageView) findViewById(R.id.img_aperture_small);
        img_aperture_big = (ImageView) findViewById(R.id.img_aperture_big);
        control_camera = (RelativeLayout) findViewById(R.id.control_camera);
        seekbar_zoom = (SeekBar) findViewById(R.id.seekbar_zoom);
        seekbar_focus = (SeekBar) findViewById(R.id.seekbar_focus);
        seekbar_aperture = (SeekBar) findViewById(R.id.seekbar_aperture);
        control = (ImageView) findViewById(R.id.control);
        l_focus = (RelativeLayout) findViewById(R.id.l_focus);
        seebar_focus = (VerticalSeekBar) findViewById(R.id.seebar_focus);
        seebar_focus.setProgress(5);
        focus_add = (ImageView) findViewById(R.id.focus_add);
        focus_reduce = (ImageView) findViewById(R.id.focus_reduce);
        text_number.setText(this.getResources().getString(
                R.string.monitor_number)
                + " " + P2PConnect.getNumber());
        if (contactType == P2PValue.DeviceType.NPC) {
            current_video_mode = P2PValue.VideoMode.VIDEO_MODE_LD;
            Log.e("devicetype", "devicetype=" + "npc");
        } else {
            current_video_mode = P2PConnect.getMode();
            Log.e("devicetype", "devicetype=" + "其它");
        }
        if (isSurpportOpenDoor == true) {
            open_door.setVisibility(ImageView.VISIBLE);
            String cmd_connect = "IPC1anerfa:connect";
            P2PHandler.getInstance().sendCustomCmd(callId, password,
                    cmd_connect);
        } else {
            open_door.setVisibility(ImageView.GONE);
        }
        updateVideoModeText(current_video_mode);
        // current_video_mode =P2PValue.VideoMode.VIDEO_MODE_SD;
        // P2PHandler.getInstance().setVideoMode(current_video_mode);
        // updateVideoModeText(current_video_mode);
        if (P2PConnect.getCurrentDeviceType() == P2PValue.DeviceType.IPC) {
            video_mode_hd.setVisibility(RelativeLayout.VISIBLE);
        } else {
            video_mode_hd.setVisibility(RelativeLayout.GONE);
        }
        // -------------
        rlLampControl = (RelativeLayout) findViewById(R.id.rl_lamp);
        txLamp = (TextView) findViewById(R.id.tv_lamp_control);
        progressBarLamp = (ProgressBar) findViewById(R.id.progressBar_lamp);
        rlLampControl.setOnClickListener(this);
        // ------------------------
        final AnimationDrawable anim = (AnimationDrawable) voice_state
                .getDrawable();
        OnPreDrawListener opdl = new OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                anim.start();
                return true;
            }

        };
        voice_state.getViewTreeObserver().addOnPreDrawListener(opdl);
        if (contactType != P2PValue.DeviceType.DOORBELL && !isSurpportOpenDoor) {
            send_voice.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View arg0, MotionEvent event) {
                    // TODO Auto-generated method stub
                    Log.e("bug", "" + event.getAction());
                    Log.e("AudioType", "AudioType=" + AudioType);
                    if (AudioType != 1) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                hideVideoFormat();
                                layout_voice_state
                                        .setVisibility(RelativeLayout.VISIBLE);
                                send_voice
                                        .setBackgroundResource(R.drawable.ic_send_audio_p);
                                setMute(false);
                                return true;
                            case MotionEvent.ACTION_UP:
                                layout_voice_state
                                        .setVisibility(RelativeLayout.GONE);
                                send_voice
                                        .setBackgroundResource(R.drawable.ic_send_audio);
                                setMute(true);
                                return true;
                        }
                        return false;
                    }
                    return false;
                }
            });
        } else if (contactType == P2PValue.DeviceType.DOORBELL
                && !isSurpportOpenDoor) {
            send_voice.setOnClickListener(this);
        } else if (isSurpportOpenDoor) {
            send_voice.setOnClickListener(this);
            // 开始监控时没有声音，暂时这样
            send_voice.performClick();
            send_voice.performClick();
            // speak();
        }
        // AP模式不支持观看人数与清晰度切换
        if (connectType == CallActivity.P2PCONECT) {
            text_number.setVisibility(View.VISIBLE);
            choose_video_format.setVisibility(View.VISIBLE);
        } else {
            text_number.setVisibility(View.GONE);
            choose_video_format.setVisibility(View.GONE);
        }

        screenshot.setOnClickListener(this);
        hungup.setOnClickListener(this);
        close_voice.setOnClickListener(this);
        video_mode_hd.setOnClickListener(this);
        video_mode_sd.setOnClickListener(this);
        video_mode_ld.setOnClickListener(this);
        steering_wheel.setOnClickListener(this);
        bt_top.setOnClickListener(this);
        bt_bottom.setOnClickListener(this);
        bt_left.setOnClickListener(this);
        bt_right.setOnClickListener(this);
        open_door.setOnClickListener(this);
        tv_on1.setOnClickListener(this);
        tv_on2.setOnClickListener(this);
        tv_on3.setOnClickListener(this);
        tv_off1.setOnClickListener(this);
        tv_off2.setOnClickListener(this);
        tv_off3.setOnClickListener(this);

        layout_arrow.setOnClickListener(this);
        defence_state.setOnClickListener(this);
        choose_video_format.setOnClickListener(this);

        img_zoom_small.setOnClickListener(this);
        img_zoom_big.setOnClickListener(this);
        img_focus_small.setOnClickListener(this);
        img_focus_big.setOnClickListener(this);
        img_aperture_small.setOnClickListener(this);
        img_aperture_big.setOnClickListener(this);
        seekbar_zoom.setProgress(5);
        seekbar_focus.setProgress(5);
        seekbar_aperture.setProgress(5);
        seekbar_zoom.setOnSeekBarChangeListener(this);
        seekbar_focus.setOnSeekBarChangeListener(this);
        seekbar_aperture.setOnSeekBarChangeListener(this);
        control.setOnClickListener(this);
        focus_add.setOnClickListener(this);
        focus_reduce.setOnClickListener(this);
        seebar_focus.setSeeckListen(new OnVerticalSeekBarChangeListener() {

            @Override
            public void onStartTrackingTouch(VerticalSeekBar seekBar) {
                // TODO Auto-generated method stub
                int progress = seekBar.getProgress();
                if (progress < 5) {
//					P2PHandler.getInstance().controlCamera(callId, password, MyUtils.aperture_smal);
                    P2PHandler.getInstance().setFocus(Constants.FOCUS.FOCUS_SMALL);
                } else {
//					P2PHandler.getInstance().controlCamera(callId, password, MyUtils.aperture_big);
                    P2PHandler.getInstance().setFocus(Constants.FOCUS.FOCUS_BIG);
                }
                seekBar.setProgress(5);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        // if(isSurpportOpenDoor){
        // speak();
        // }
    }

    public void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.P2P.P2P_REJECT);
        filter.addAction(Constants.P2P.P2P_MONITOR_NUMBER_CHANGE);
        filter.addAction(Constants.P2P.P2P_RESOLUTION_CHANGE);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Constants.P2P.RET_CUSTOM_CMD_DISCONNECT);
        filter.addAction(Constants.P2P.RET_DEVICE_NOT_SUPPORT);
        filter.addAction(Constants.P2P.RET_SET_GPIO);
        // filter.addAction(Constants.P2P.RET_GET_AUDIO_DEVICE_TYPE);
        filter.addAction(Constants.P2P.ACK_RET_GET_NPC_SETTINGS);
        filter.addAction(Constants.P2P.RET_GET_REMOTE_DEFENCE);
        filter.addAction(Constants.P2P.RET_SET_REMOTE_DEFENCE);
        filter.addAction(Constants.P2P.ACK_RET_CHECK_PASSWORD);
        // 灯
        filter.addAction(Constants.P2P.SET_LAMP_STATUS);
        filter.addAction(Constants.P2P.GET_LAMP_STATUS);
        filter.addAction(Constants.P2P.ACK_SET_LAMP_STATUS);
        // 正在监控时新设备弹框
        filter.addAction(Constants.Action.MONITOR_NEWDEVICEALARMING);
        filter.addAction(Constants.P2P.RET_GET_FOCUS_ZOOM);
        mContext.registerReceiver(mReceiver, filter);
        isRegFilter = true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("my", "onConfigurationChanged:" + newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

        }
        mHandler.sendEmptyMessageDelayed(0, 500);
    }

    public Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub.
            pView.updateScreenOrientation();
            return false;
        }
    });

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            if (intent.getAction().equals(Constants.P2P.P2P_REJECT)) {
                reject();
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                reject();
            } else if (intent.getAction().equals(
                    Constants.P2P.P2P_MONITOR_NUMBER_CHANGE)) {
                int number = intent.getIntExtra("number", -1);
                if (number != -1) {
                    text_number.setText(mContext.getResources().getString(
                            R.string.monitor_number)
                            + " " + P2PConnect.getNumber());
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.P2P_RESOLUTION_CHANGE)) {
                int mode = intent.getIntExtra("mode", -1);
                if (mode != -1) {
                    current_video_mode = mode;
                    updateVideoModeText(current_video_mode);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_CUSTOM_CMD_DISCONNECT)) {
                reject();
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_DEVICE_NOT_SUPPORT)) {
                T.showShort(mContext, R.string.not_support);
            } else if (intent.getAction()
                    .equals(Constants.P2P.ACK_RET_SET_GPIO)) {

            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_RET_SET_GPIO1_0)) {

            } else if (intent.getAction().equals(Constants.P2P.RET_SET_GPIO)) {
                int result = intent.getIntExtra("result", -1);
                Log.e("result", "result=" + result);
                if (result == 0) {
                    T.showShort(mContext, R.string.gpio_success);
                } else if (result == 255) {
                    T.showShort(mContext, R.string.device_not_support);
                } else if (result == 86) {
                    T.showShort(mContext, R.string.not_open);
                } else if (result == 87) {
                    T.showShort(mContext, R.string.frequent_operation);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_GET_AUDIO_DEVICE_TYPE)) {
                int type = intent.getIntExtra("type", -1);
                if (type == 1) {
                    AudioType = 1;
                    layout_voice_state.setVisibility(RelativeLayout.VISIBLE);
                    setMute(false);
                    isSpeak = true;
                } else {
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_GET_REMOTE_DEFENCE)) {
                int state = intent.getIntExtra("state", -1);
                String contactId = intent.getStringExtra("contactId");
                Log.e("dxsdefence", "GET_REMOTE_DEFENCEstate-->" + state);
                if (contactId.equals(callId)) {
                    if (state == Constants.P2P_SET.REMOTE_DEFENCE_SET.ALARM_SWITCH_ON) {
                        defenceState = Constants.DefenceState.DEFENCE_STATE_ON;
                        defence_state.setImageResource(R.drawable.deployment);
                    } else {
                        defenceState = Constants.DefenceState.DEFENCE_STATE_OFF;
                        defence_state.setImageResource(R.drawable.disarm);
                    }
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
                    P2PHandler.getInstance().getNpcSettings(callId, password);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_RET_CHECK_PASSWORD)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_SUCCESS) {
                    defence_state.setVisibility(ImageView.VISIBLE);
                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    P2PHandler.getInstance().checkPassword(callId, password);
                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_INSUFFICIENT_PERMISSIONS) {
                    defence_state.setVisibility(ImageView.GONE);
                    // T.showShort(mContext,R.string.insufficient_permissions);
                }

            } else if (intent.getAction().equals(Constants.P2P.SET_LAMP_STATUS)) {
                int result = intent.getIntExtra("result", -1);
                Log.i("dxslamp", "result1-->" + result);
                if (result == 0) {
                    if (cur_modify_lamp_state == 0) {
                        lamp_switch = 0;
                        txLamp.setText("开灯");
                    } else {
                        lamp_switch = 1;
                        txLamp.setText("关灯");
                    }
                    showLampState();
                    T.showShort(mContext, R.string.modify_success);
                } else {
                    showLampState();
                    T.showShort(mContext, R.string.operator_error);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_SET_LAMP_STATUS)) {
                int result = intent.getIntExtra("result", -1);
                Log.i("dxslamp", "result2-->" + result);
            } else if (intent.getAction().equals(Constants.P2P.GET_LAMP_STATUS)) {
                int result = intent.getIntExtra("result", -1);
                if (result != -1) {
                    rlLampControl.setVisibility(View.VISIBLE);
                } else {
                    rlLampControl.setVisibility(View.GONE);
                }
                Log.i("dxslamp", "result3-->" + result);
                if (result == 1) {
                    lamp_switch = 1;
                    txLamp.setText("关灯");
                } else {
                    lamp_switch = 0;
                    txLamp.setText("开灯");
                }
                showLampState();
            } else if (intent.getAction().equals(
                    Constants.Action.MONITOR_NEWDEVICEALARMING)) {
                // 弹窗
                int MessageType = intent.getIntExtra("messagetype", 2);
                int type = intent.getIntExtra("alarm_type", 0);
                int group = intent.getIntExtra("group", -1);
                int item = intent.getIntExtra("item", -1);
                boolean isSupport = intent.getBooleanExtra("isSupport", false);
                if (MessageType == 1) {
                    // 报警推送
                    NewMessageDeviceID = intent.getStringExtra("alarm_id");
                    Log.i("dxsmoniter_alarm", "报警推送" + NewMessageDeviceID
                            + "type" + type);
                } else {
                    // 透传推送
                    NewMessageDeviceID = intent.getStringExtra("contactId");
                    type = 13;
                    Log.i("dxsmoniter_alarm", "透传推送" + NewMessageDeviceID);
                }
                String alarmtype = Utils.getAlarmType(type, isSupport, group,
                        item);
                StringBuffer NewMassage = new StringBuffer(
                        Utils.getStringByResouceID(R.string.tab_device))
                        .append(":").append(
                                Utils.getDeviceName(NewMessageDeviceID));
                NewMassage.append("\n").append(alarmtype);
                NewMessageDialog(NewMassage.toString(), NewMessageDeviceID);
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_SET_REMOTE_DEFENCE)) {
                int result = intent.getIntExtra("state", -1);
                if (result == 0) {
                    if (defenceState == Constants.DefenceState.DEFENCE_STATE_ON) {
                        defenceState = Constants.DefenceState.DEFENCE_STATE_OFF;
                    } else {
                        defenceState = Constants.DefenceState.DEFENCE_STATE_ON;
                    }
                    Log.e("dxsdefence", "result-->" + result);
                    changeDefence(defenceState);
                }

            } else if (intent.getAction().equals(Constants.P2P.RET_GET_FOCUS_ZOOM)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.CHANGE_FOCUS_ZOOM.CHANGE_FOCUS || result == Constants.CHANGE_FOCUS_ZOOM.CHANGE_FOCUS_ZOOM) {
                    l_focus.setVisibility(RelativeLayout.VISIBLE);
                } else {
                    l_focus.setVisibility(RelativeLayout.GONE);
                }
            }
        }
    };

    public void changeControl() {
        if (isSpeak) {// 对讲过程中不可消失
            return;
        }
        if (control_bottom.getVisibility() == RelativeLayout.VISIBLE) {
            isControlShow = false;
            Animation anim2 = AnimationUtils.loadAnimation(this,
                    android.R.anim.fade_out);
            anim2.setDuration(100);
            control_bottom.startAnimation(anim2);
            anim2.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation arg0) {
                    // TODO Auto-generated method stub
                    hideVideoFormat();
                    choose_video_format.setClickable(false);
                }

                @Override
                public void onAnimationRepeat(Animation arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animation arg0) {
                    // TODO Auto-generated method stub
                    hideVideoFormat();
                    control_bottom.setVisibility(RelativeLayout.GONE);
                    control_camera.setVisibility(RelativeLayout.GONE);
                    isPtzControl = false;
                    choose_video_format
                            .setBackgroundResource(R.drawable.sd_backgroud);
                    choose_video_format.setClickable(true);
                }
            });

            layout_steering_wheel.setVisibility(RelativeLayout.GONE);
            layout_telescopic.setVisibility(RelativeLayout.GONE);
            isOpenSteerWheel = true;
        } else {
            isControlShow = true;
            control_bottom.setVisibility(RelativeLayout.VISIBLE);
            layout_telescopic.setVisibility(RelativeLayout.VISIBLE);
            Animation anim2 = AnimationUtils.loadAnimation(this,
                    android.R.anim.fade_in);
            anim2.setDuration(100);
            control_bottom.startAnimation(anim2);
            anim2.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation arg0) {
                    // TODO Auto-generated method stub
                    hideVideoFormat();
                    choose_video_format.setClickable(false);
                }

                @Override
                public void onAnimationRepeat(Animation arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animation arg0) {
                    // TODO Auto-generated method stub
                    hideVideoFormat();
                    choose_video_format.setClickable(true);
                }
            });
        }
    }

    public void changevideoformat() {
        if (control_top.getVisibility() == RelativeLayout.VISIBLE) {
            Animation anim2 = AnimationUtils.loadAnimation(this,
                    android.R.anim.fade_out);
            anim2.setDuration(100);
            control_top.startAnimation(anim2);
            control_top.setVisibility(RelativeLayout.GONE);
            isShowVideo = false;
        } else {
            Animation anim2 = AnimationUtils.loadAnimation(this,
                    android.R.anim.fade_in);
            anim2.setDuration(100);
            control_top.setVisibility(RelativeLayout.VISIBLE);
            control_top.startAnimation(anim2);
            isShowVideo = true;
        }
    }

    public void hideVideoFormat() {
        if (control_top.getVisibility() == RelativeLayout.VISIBLE) {
            Animation anim2 = AnimationUtils.loadAnimation(this,
                    android.R.anim.fade_out);
            anim2.setDuration(100);
            control_top.startAnimation(anim2);
            control_top.setVisibility(RelativeLayout.GONE);
            isShowVideo = false;
        }
    }

    public void updateVideoModeText(int mode) {
        if (mode == P2PValue.VideoMode.VIDEO_MODE_HD) {
            video_mode_hd.setTextColor(mContext.getResources().getColor(
                    R.color.text_color_blue));
            video_mode_sd.setTextColor(mContext.getResources().getColor(
                    R.color.text_color_white));
            video_mode_ld.setTextColor(mContext.getResources().getColor(
                    R.color.text_color_white));
            choose_video_format.setText(R.string.video_mode_hd);
        } else if (mode == P2PValue.VideoMode.VIDEO_MODE_SD) {
            video_mode_hd.setTextColor(mContext.getResources().getColor(
                    R.color.text_color_white));
            video_mode_sd.setTextColor(mContext.getResources().getColor(
                    R.color.text_color_blue));
            video_mode_ld.setTextColor(mContext.getResources().getColor(
                    R.color.text_color_white));
            choose_video_format.setText(R.string.video_mode_sd);
        } else if (mode == P2PValue.VideoMode.VIDEO_MODE_LD) {
            video_mode_hd.setTextColor(mContext.getResources().getColor(
                    R.color.text_color_white));
            video_mode_sd.setTextColor(mContext.getResources().getColor(
                    R.color.text_color_white));
            video_mode_ld.setTextColor(mContext.getResources().getColor(
                    R.color.text_color_blue));
            choose_video_format.setText(R.string.video_mode_ld);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int resId = v.getId();
        // case R.id.change_video_mode:
        // if(isHD){
        // isHD = false;
        // int result =
        // P2PHandler.getInstance().setVideoMode(P2PValue.VideoMode.VIDEO_MODE_LD);
        // change_video_mode.setImageResource(R.drawable.ic_video_sd);
        // Log.e("my","setVideoMode:"+result);
        // }else{
        // isHD = true;
        // int result =
        // P2PHandler.getInstance().setVideoMode(P2PValue.VideoMode.VIDEO_MODE_HD);
        // change_video_mode.setImageResource(R.drawable.ic_video_hd);
        // Log.e("my","setVideoMode:"+result);
        // }
        // break;
        if (resId == R.id.video_mode_hd) {
            if (current_video_mode != P2PValue.VideoMode.VIDEO_MODE_HD) {
                current_video_mode = P2PValue.VideoMode.VIDEO_MODE_HD;
                P2PHandler.getInstance().setVideoMode(
                        P2PValue.VideoMode.VIDEO_MODE_HD);
                updateVideoModeText(current_video_mode);
            }
            hideVideoFormat();
        } else if (resId == R.id.video_mode_sd) {
            if (current_video_mode != P2PValue.VideoMode.VIDEO_MODE_SD) {
                current_video_mode = P2PValue.VideoMode.VIDEO_MODE_SD;
                P2PHandler.getInstance().setVideoMode(
                        P2PValue.VideoMode.VIDEO_MODE_SD);
                updateVideoModeText(current_video_mode);
            }
            hideVideoFormat();
        } else if (resId == R.id.video_mode_ld) {
            if (current_video_mode != P2PValue.VideoMode.VIDEO_MODE_LD) {
                current_video_mode = P2PValue.VideoMode.VIDEO_MODE_LD;
                P2PHandler.getInstance().setVideoMode(
                        P2PValue.VideoMode.VIDEO_MODE_LD);
                updateVideoModeText(current_video_mode);
            }
            hideVideoFormat();
        } else if (resId == R.id.screenshot) {
            this.captureScreen(-1);
        } else if (resId == R.id.hungup) {
            reject();
        } else if (resId == R.id.close_voice) {
            if (mIsCloseVoice) {
                mIsCloseVoice = false;
                close_voice.setBackgroundResource(R.drawable.m_voice_on);
                if (mCurrentVolume == 0) {
                    mCurrentVolume = 1;
                }
                if (mAudioManager != null) {
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            mCurrentVolume, 0);
                }
            } else {
                mIsCloseVoice = true;
                close_voice.setBackgroundResource(R.drawable.m_voice_off);
                if (mAudioManager != null) {
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0,
                            0);
                }
            }
        } else if (resId == R.id.steering_wheel) {
            if (isOpenSteerWheel == false) {
                isOpenSteerWheel = true;
                layout_steering_wheel.setVisibility(RelativeLayout.GONE);
            } else {
                isOpenSteerWheel = false;
                layout_steering_wheel.setVisibility(RelativeLayout.VISIBLE);
            }
        } else if (resId == R.id.top_btn) {
            P2PHandler.getInstance().sendCtlCmd(USR_CMD_CAR_DIR_CTL,
                    USR_CMD_OPTION_CAR_TURN_FORWARD);
            Log.e("forward", "ok-----");
        } else if (resId == R.id.bottom_btn) {
            P2PHandler.getInstance().sendCtlCmd(USR_CMD_CAR_DIR_CTL,
                    USR_CMD_OPTION_CAR_TURN_BACK);
        } else if (resId == R.id.left_btn) {
            P2PHandler.getInstance().sendCtlCmd(USR_CMD_CAR_DIR_CTL,
                    USR_CMD_OPTION_CAR_TURN_LEFT);
        } else if (resId == R.id.right_btn) {
            P2PHandler.getInstance().sendCtlCmd(USR_CMD_CAR_DIR_CTL,
                    USR_CMD_OPTION_CAR_TURN_RIGHT);
        } else if (resId == R.id.tv_on1) {
            P2PHandler.getInstance().setGPIO(callId, password, 0, 0);
        } else if (resId == R.id.tv_on2) {
            P2PHandler.getInstance().setGPIO(callId, password, 0, 2);
        } else if (resId == R.id.tv_on3) {
            P2PHandler.getInstance().setGPIO(callId, password, 0, 4);
        } else if (resId == R.id.tv_off1) {
            P2PHandler.getInstance().setGPIO(callId, password, 0, 1);
        } else if (resId == R.id.tv_off2) {
            P2PHandler.getInstance().setGPIO(callId, password, 0, 3);
        } else if (resId == R.id.tv_off3) {
            P2PHandler.getInstance().setGPIO(callId, password, 2, 6);
        } else if (resId == R.id.layout_arrow) {
            if (isTelescopic == false) {
                layout_gpio.setVisibility(LinearLayout.VISIBLE);
                img_arrow.setBackgroundResource(R.drawable.b_left);
                isTelescopic = true;
            } else {
                layout_gpio.setVisibility(LinearLayout.GONE);
                img_arrow.setBackgroundResource(R.drawable.b_right);
                isTelescopic = false;
            }
        } else if (resId == R.id.open_door) {
            // 开门
            openDor();
        } else if (resId == R.id.send_voice) {
            Log.e("dxsmonitor", "AudioType-->" + AudioType);
            // 删除了AuoduType==1的判断
            if (!isSpeak) {
                speak();
            } else {
                send_voice.setBackgroundResource(R.drawable.ic_send_audio);
                layout_voice_state.setVisibility(RelativeLayout.GONE);
                mhandler.postDelayed(mrunnable, 500);
                // layout_voice_state.setVisibility(RelativeLayout.GONE);
                // setMute(true);
                isSpeak = false;
            }
        } else if (resId == R.id.defence_state) {
            setDefence();
        } else if (resId == R.id.choose_video_format) {
            changevideoformat();
        } else if (resId == R.id.rl_lamp) {
            showProgress_lamp();
            if (lamp_switch != 0) {
                cur_modify_lamp_state = 0;
                P2PHandler.getInstance().vsetLampStatus(callId, password,
                        cur_modify_lamp_state);
            } else {
                cur_modify_lamp_state = 1;
                P2PHandler.getInstance().vsetLampStatus(callId, password,
                        cur_modify_lamp_state);
            }
        } else if (resId == R.id.img_zoom_small) {
            P2PHandler.getInstance().controlCamera(callId, password,
                    Utils.zoom_small);
        } else if (resId == R.id.img_zoom_big) {
            P2PHandler.getInstance().controlCamera(callId, password,
                    Utils.zoom_big);
        } else if (resId == R.id.img_focus_small) {
            P2PHandler.getInstance().controlCamera(callId, password,
                    Utils.focus_small);
        } else if (resId == R.id.img_focus_big) {
            P2PHandler.getInstance().controlCamera(callId, password,
                    Utils.focus_big);
        } else if (resId == R.id.img_aperture_small) {
            P2PHandler.getInstance().controlCamera(callId, password,
                    Utils.aperture_smal);
        } else if (resId == R.id.img_aperture_big) {
            P2PHandler.getInstance().controlCamera(callId, password,
                    Utils.aperture_big);
        } else if (resId == R.id.control) {
            if (isPtzControl) {
                control_camera.setVisibility(RelativeLayout.GONE);
                isPtzControl = false;
            } else {
                control_camera.setVisibility(RelativeLayout.VISIBLE);
                isPtzControl = true;
            }
        } else if (resId == R.id.focus_add) {
            P2PHandler.getInstance().setFocus(Constants.FOCUS.FOCUS_BIG);
        } else if (resId == R.id.focus_reduce) {
            P2PHandler.getInstance().setFocus(Constants.FOCUS.FOCUS_SMALL);
        }
    }

    private void changeDefence(int defencestate) {
        if (defencestate == Constants.DefenceState.DEFENCE_STATE_ON) {
            defence_state.setImageResource(R.drawable.deployment);
        } else {
            defence_state.setImageResource(R.drawable.disarm);
        }

    }

    private void setDefence() {
        if (defenceState == Constants.DefenceState.DEFENCE_STATE_ON) {
            P2PHandler.getInstance().setRemoteDefence(callId, password,
                    Constants.P2P_SET.REMOTE_DEFENCE_SET.ALARM_SWITCH_OFF);
        } else if (defenceState == Constants.DefenceState.DEFENCE_STATE_OFF) {
            P2PHandler.getInstance().setRemoteDefence(callId, password,
                    Constants.P2P_SET.REMOTE_DEFENCE_SET.ALARM_SWITCH_ON);
        }
    }

    // 设置成对话状态
    private void speak() {
        hideVideoFormat();
        layout_voice_state.setVisibility(RelativeLayout.VISIBLE);
        send_voice.setBackgroundResource(R.drawable.ic_send_audio_p);
        setMute(false);
        isSpeak = true;
    }

    /**
     * 开门
     */
    private void openDor() {
        NormalDialog dialog = new NormalDialog(mContext, mContext
                .getResources().getString(R.string.open_door), mContext
                .getResources().getString(R.string.confirm_open_door), mContext
                .getResources().getString(R.string.yes), mContext
                .getResources().getString(R.string.no));
        dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {

            @Override
            public void onClick() {
                String cmd = "IPC1anerfa:unlock";
                P2PHandler.getInstance().sendCustomCmd(callId, password, cmd);
                P2PHandler.getInstance().setGPIO1_0(callId, password);
            }
        });
        dialog.showDialog();
    }

    /**
     * 新报警信息
     */
    NormalDialog dialog;
    String contactidTemp = "";

    private void NewMessageDialog(String Meassage, final String contacid) {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog = new NormalDialog(mContext, "", Meassage, mContext
                    .getResources().getString(R.string.check), mContext
                    .getResources().getString(R.string.ignore));
            dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {

                @Override
                public void onClick() {
                    // 查看新监控--挂断当前监控，再次呼叫另一个监控
                    seeMonitor(contacid);
                }
            });
            dialog.showDialog();

        } else {
            // 还没有对话框
            dialog = new NormalDialog(mContext, "", Meassage, mContext
                    .getResources().getString(R.string.check), mContext
                    .getResources().getString(R.string.ignore));
            dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {

                @Override
                public void onClick() {
                    // 查看新监控--挂断当前监控，再次呼叫另一个监控
                    seeMonitor(contacid);
                }
            });
            dialog.showDialog();
        }

        contactidTemp = contacid;
    }

    private void seeMonitor(String contactId) {
        final Contact contact = FList.getInstance().isContact(contactId);
        if (null != contact) {
            reject();
            P2PConnect.vReject(0, "");
            new Thread() {
                @Override
                public void run() {
                    while (true) {
                        if (P2PConnect.getCurrent_state() == P2PConnect.P2P_STATE_NONE) {
                            Message msg = new Message();
                            String[] data = new String[]{contact.contactId,
                                    contact.contactPassword,
                                    String.valueOf(contact.contactType)};
                            msg.obj = data;
                            handler.sendMessage(msg);
                            break;
                        }
                        Utils.sleepThread(500);
                    }
                }
            }.start();
        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            Log.i("dxsmonitor", contactId);
            createPassDialog(contactId);
        }
    }

    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            String[] data = (String[]) msg.obj;
            Intent monitor = new Intent();
            monitor.setClass(mContext, CallActivity.class);
            monitor.putExtra("callId", data[0]);
            monitor.putExtra("password", data[1]);
            monitor.putExtra("isOutCall", true);
            monitor.putExtra("contactType", Integer.parseInt(data[2]));
            monitor.putExtra("type", Constants.P2P_TYPE.P2P_TYPE_MONITOR);
            if (Integer.parseInt(data[2]) == P2PValue.DeviceType.DOORBELL) {
                monitor.putExtra("isSurpportOpenDoor", true);
            }
            startActivity(monitor);
            finish();
            return false;
        }
    });
    private Dialog passworddialog;
    private OnCustomDialogListener listener = new OnCustomDialogListener() {

        @Override
        public void check(final String password, final String id) {
            if (password.trim().equals("")) {
                T.showShort(mContext, R.string.input_monitor_pwd);
                return;
            }

            if (password.length() > 9) {
                T.showShort(mContext, R.string.password_length_error);
                return;
            }

            P2PConnect.vReject(0, "");
            new Thread() {
                @Override
                public void run() {
                    while (true) {
                        if (P2PConnect.getCurrent_state() == P2PConnect.P2P_STATE_NONE) {
                            Message msg = new Message();
                            String[] data = new String[]{id, password,
                                    String.valueOf(P2PValue.DeviceType.IPC)};
                            msg.obj = data;
                            handler.sendMessage(msg);
                            break;
                        }
                        Utils.sleepThread(500);
                    }
                }
            }.start();

        }
    };

    void createPassDialog(String id) {
        passworddialog = new MyInputPassDialog(mContext,
                Utils.getStringByResouceID(R.string.check), id, listener);
        passworddialog.show();
    }

    // 灯光控制
    public void showLampState() {
        progressBarLamp.setVisibility(RelativeLayout.GONE);
        txLamp.setVisibility(RelativeLayout.VISIBLE);
        rlLampControl.setEnabled(true);
    }

    // 灯光控制
    public void showProgress_lamp() {
        progressBarLamp.setVisibility(RelativeLayout.VISIBLE);
        txLamp.setVisibility(RelativeLayout.GONE);
        rlLampControl.setEnabled(false);
    }

    private boolean isFirstMute = true;
    Runnable mrunnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            setMute(true);
            if (isFirstMute) {
                send_voice.performClick();
                isFirstMute = false;
            }
        }
    };

    @Override
    public void onBackPressed() {
        reject();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
            mCurrentVolume++;
            if (mCurrentVolume > mMaxVolume) {
                mCurrentVolume = mMaxVolume;
            }

            if (mCurrentVolume != 0) {
                mIsCloseVoice = false;
                close_voice.setBackgroundResource(R.drawable.m_voice_on);
            }
            return false;
        } else if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
            mCurrentVolume--;
            if (mCurrentVolume < 0) {
                mCurrentVolume = 0;
            }

            if (mCurrentVolume == 0) {
                mIsCloseVoice = true;
                close_voice.setBackgroundResource(R.drawable.m_voice_off);
            }

            return false;
        }

        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAudioManager != null) {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                    mCurrentVolume, 0);
        }
        if (isRegFilter) {
            mContext.unregisterReceiver(mReceiver);
            isRegFilter = false;
        }

        if (null != mPhoneWatcher) {
            mPhoneWatcher.stopWatcher();
        }
        P2PConnect.setPlaying(false);
        P2PConnect.setMonitorId("");// 设置在监控的ID为空
        SettingListener.setMonitorID("");
        if (!activity_stack
                .containsKey(Constants.ActivityInfo.ACTIVITY_MAINACTIVITY)) {
            Intent i = new Intent(this, MainActivity.class);
            this.startActivity(i);
        }

        Intent refreshContans = new Intent();
        refreshContans.setAction(Constants.Action.REFRESH_CONTANTS);
        mContext.sendBroadcast(refreshContans);
    }

    @Override
    public int getActivityInfo() {
        // TODO Auto-generated method stub
        return Constants.ActivityInfo.ACTIVITY_MONITORACTIVITY;
    }

    @Override
    protected void onP2PViewSingleTap() {
        // TODO Auto-generated method stub
        changeControl();
    }

    @Override
    protected void onGoBack() {
        // TODO Auto-generated method stub
        MyApp.app.showNotification();
    }

    @Override
    protected void onGoFront() {
        // TODO Auto-generated method stub
        MyApp.app.hideNotification();
    }

    @Override
    protected void onExit() {
        // TODO Auto-generated method stub
        MyApp.app.hideNotification();
    }

    @Override
    protected void onCaptureScreenResult(boolean isSuccess, int prepoint) {
        // TODO Auto-generated method stub
        if (isSuccess) {
            // Capture success
            T.showShort(mContext, R.string.capture_success);
            List<String> pictrues = Utils.getScreenShotImagePath(callId, 1);
            if (pictrues.size() <= 0) {
                return;
            }
            Utils.saveImgToGallery(pictrues.get(0));
        } else {
            T.showShort(mContext, R.string.capture_failed);
        }
    }

    public void reject() {
        if (!isReject) {
            isReject = true;
            P2PHandler.getInstance().reject();
            if (isSurpportOpenDoor == true) {
                String cmd_disconnect = "IPC1anerfa:disconnect";
                P2PHandler.getInstance().sendCustomCmd(callId, password,
                        cmd_disconnect);
                Log.e("cus_cmd", "---------------");
            }
            finish();
        }

    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                T.showShort(mContext, R.string.press_again_monitor);
                exitTime = System.currentTimeMillis();
            } else {
                reject();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekbar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekbar) {
        // TODO Auto-generated method stub
        int resId = seekbar.getId();
        if (resId == R.id.seekbar_zoom) {
            int progress_zoom = seekbar_zoom.getProgress();
            if (progress_zoom < 5) {
                P2PHandler.getInstance().controlCamera(callId, password,
                        MyUtils.zoom_small);
            } else {
                P2PHandler.getInstance().controlCamera(callId, password,
                        MyUtils.zoom_big);
            }
            seekbar_zoom.setProgress(5);
        } else if (resId == R.id.seekbar_focus) {
            int progress_focus = seekbar_focus.getProgress();
            if (progress_focus < 5) {
                P2PHandler.getInstance().controlCamera(callId, password,
                        MyUtils.focus_small);
            } else {
                P2PHandler.getInstance().controlCamera(callId, password,
                        MyUtils.focus_big);
            }
            seekbar_focus.setProgress(5);
        } else if (resId == R.id.seekbar_aperture) {
            int progress_aperture = seekbar_aperture.getProgress();
            if (progress_aperture < 5) {
                P2PHandler.getInstance().controlCamera(callId, password,
                        MyUtils.aperture_smal);
            } else {
                P2PHandler.getInstance().controlCamera(callId, password,
                        MyUtils.aperture_big);
            }
            seekbar_aperture.setProgress(5);
        }
    }

}
