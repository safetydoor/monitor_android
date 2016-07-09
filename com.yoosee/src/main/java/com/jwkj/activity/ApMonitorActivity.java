package com.jwkj.activity;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jwkj.P2PConnect;
import com.jwkj.SettingListener;
import com.jwkj.data.Contact;
import com.jwkj.global.AppConfig;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.global.MyApp;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.widget.HeaderView;
import com.jwkj.widget.MyInputPassDialog;
import com.jwkj.widget.MyInputPassDialog.OnCustomDialogListener;
import com.jwkj.widget.NormalDialog;
import com.jwkj.widget.NormalDialog.OnAlarmClickListner;
import com.p2p.core.BaseMonitorActivity;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;
import com.p2p.core.P2PView;
import com.yoosee.R;

import java.util.ArrayList;
import java.util.List;

public class ApMonitorActivity extends BaseMonitorActivity implements OnClickListener {
    RelativeLayout layout_title;
    ImageView iv_full_screen, iv_voice, iv_speak, iv_defence, iv_screenshot, open_door;
    LinearLayout l_control;
    RelativeLayout rl_control, control_bottom;
    LinearLayout control_top;
    Button choose_video_format;
    View line;
    RelativeLayout back_btn;
    TextView video_mode_hd, video_mode_sd, video_mode_ld;
    ImageView close_voice, send_voice, iv_half_screen, hungup, screenshot, defence_state;
    LinearLayout layout_voice_state;
    TextView tv_name;
    RelativeLayout r_p2pview;
    ImageView voice_state;
    ImageView iv_last, iv_next;
    LinearLayout l_device_list;
    TextView tv_choosee_device;
    private Contact mContact;
    int callType = 3;
    private Context mContext;
    boolean isReject = false;
    boolean isRegFilter = false;
    private int ScrrenOrientation;
    int window_width, window_height;
    String callId, password;
    int connectType;
    private int defenceState = -1;
    boolean mIsCloseVoice = false;
    int mCurrentVolume, mMaxVolume;
    AudioManager mAudioManager;
    boolean isSurpportOpenDoor = false;
    boolean isShowVideo = false;
    boolean isSpeak = false;
    int current_video_mode;
    int screenWidth;
    int screenHeigh;
    private String NewMessageDeviceID;
    // 刷新监控部分
    private RelativeLayout rlPrgTxError;
    private TextView txError, tx_wait_for_connect;
    private Button btnRefrash;
    private ProgressBar progressBar;
    private HeaderView ivHeader;
    private String alarm_id = "";
    private String[] ipcList;
    int number;
    int currentNumber = 0;
    boolean isShowDeviceList = false;
    List<TextView> devicelist = new ArrayList<TextView>();
    //  摇手机切换ipc
    Vibrator vibrator;
    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorListener;
    boolean isShake = true;
    private long lastUpdateTime;
    private float lastX;
    private float lastY;
    private float lastZ;
    private static final int UPTATE_INTERVAL_TIME = 70;
    private static final int SPEED_SHRESHOLD = 2000;
    private boolean isReceveHeader = false;
    boolean isPermission = true;
    private View vLineHD;
    private boolean connectSenconde = false;
    int pushAlarmType;
    boolean isCustomCmdAlarm = false;
    private Handler mhandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apmonitor);
        P2PConnect.setPlaying(true);
        Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        mContext = this;
        mContact = (Contact) getIntent().getSerializableExtra("contact");
        if (mContact.contactType == P2PValue.DeviceType.IPC) {
            setIsLand(false);
        } else {
            setIsLand(true);
        }
        ipcList = getIntent().getStringArrayExtra("ipcList");
        number = getIntent().getIntExtra("number", -1);
        connectType = getIntent().getIntExtra("connectType", Constants.ConnectType.P2PCONNECT);
        isSurpportOpenDoor = getIntent().getBooleanExtra("isSurpportOpenDoor", false);
        isCustomCmdAlarm = getIntent().getBooleanExtra("isCustomCmdAlarm", false);
        callId = mContact.contactId;
        if (number > 0) {
            callId = ipcList[0];
        }
        password = mContact.contactPassword;
        P2PConnect.setMonitorId(callId);// 设置在监控的ID
        SettingListener.setMonitorID(callId);// 设置在监控的ID
        getScreenWithHeigh();
        regFilter();
        callDevice();
        initcComponent();
//		readyCallDevice();
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        }
        mCurrentVolume = mAudioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        mMaxVolume = mAudioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        ScrrenOrientation = Configuration.ORIENTATION_PORTRAIT;
        vibrator = (Vibrator) mContext.getSystemService(mContext.VIBRATOR_SERVICE);
    }

    public void initcComponent() {
        pView = (P2PView) findViewById(R.id.p2pview);
//		this.initP2PView(mContact.contactType);
        P2PView.type = 0;
//		WindowManager manager = getWindowManager();
//	    window_width = manager.getDefaultDisplay().getWidth();
//	    window_height = manager.getDefaultDisplay().getHeight();
//	    this.initScaleView(this, window_width, window_height);
//	    setMute(true);
        layout_title = (RelativeLayout) findViewById(R.id.layout_title);
        iv_full_screen = (ImageView) findViewById(R.id.iv_full_screen);
        l_control = (LinearLayout) findViewById(R.id.l_control);
        line = (View) findViewById(R.id.line);
        rl_control = (RelativeLayout) findViewById(R.id.rl_control);
        iv_voice = (ImageView) findViewById(R.id.iv_voice);
        iv_defence = (ImageView) findViewById(R.id.iv_defence);
        iv_speak = (ImageView) findViewById(R.id.iv_speak);
        iv_screenshot = (ImageView) findViewById(R.id.iv_screenshot);
        back_btn = (RelativeLayout) findViewById(R.id.back_btn);
        control_bottom = (RelativeLayout) findViewById(R.id.control_bottom);
        control_top = (LinearLayout) findViewById(R.id.control_top);
        video_mode_hd = (TextView) findViewById(R.id.video_mode_hd);
        video_mode_sd = (TextView) findViewById(R.id.video_mode_sd);
        video_mode_ld = (TextView) findViewById(R.id.video_mode_ld);
        vLineHD = findViewById(R.id.v_line_hd);
        choose_video_format = (Button) findViewById(R.id.choose_video_format);
        close_voice = (ImageView) findViewById(R.id.close_voice);
        send_voice = (ImageView) findViewById(R.id.send_voice);
        layout_voice_state = (LinearLayout) findViewById(R.id.layout_voice_state);
        iv_half_screen = (ImageView) findViewById(R.id.iv_half_screen);
        hungup = (ImageView) findViewById(R.id.hungup);
        screenshot = (ImageView) findViewById(R.id.screenshot);
        defence_state = (ImageView) findViewById(R.id.defence_state);
        open_door = (ImageView) findViewById(R.id.open_door);
        tv_name = (TextView) findViewById(R.id.tv_name);
        r_p2pview = (RelativeLayout) findViewById(R.id.r_p2pview);
        voice_state = (ImageView) findViewById(R.id.voice_state);
        iv_last = (ImageView) findViewById(R.id.iv_last);
        iv_next = (ImageView) findViewById(R.id.iv_next);
        l_device_list = (LinearLayout) findViewById(R.id.l_device_list);
        tv_choosee_device = (TextView) findViewById(R.id.tv_choosee_device);
        setControlButtomHeight(0);
//	    initIpcDeviceList();
        frushLayout(mContact.contactType);
        if (number > 1) {
            iv_last.setVisibility(View.VISIBLE);
            iv_next.setVisibility(View.VISIBLE);
//			tv_choosee_device.setVisibility(View.VISIBLE);
            sensorManager = (SensorManager) mContext.getSystemService(mContext.SENSOR_SERVICE);
            if (sensorManager != null) {
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                sensorListener = new SensorEventListener() {

                    @Override
                    public void onSensorChanged(SensorEvent event) {
                        // TODO Auto-generated method stub
                        if (!isShake) {
                            // 现在检测时间
                            long currentUpdateTime = System.currentTimeMillis();
                            // 两次检测的时间间隔
                            long timeInterval = currentUpdateTime - lastUpdateTime;
                            if (timeInterval < UPTATE_INTERVAL_TIME)
                                return;
                            // 现在的时间变成last时间
                            lastUpdateTime = currentUpdateTime;

                            // 获得x,y,z坐标
                            float x = event.values[0];
                            float y = event.values[1];
                            float z = event.values[2];

                            // 获得x,y,z的变化值
                            float deltaX = x - lastX;
                            float deltaY = y - lastY;
                            float deltaZ = z - lastZ;

                            // 将现在的坐标变成last坐标
                            lastX = x;
                            lastY = y;
                            lastZ = z;
                            double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ
                                    * deltaZ)
                                    / timeInterval * 10000;
                            // 达到速度阀值，发出提示
                            if (speed >= SPEED_SHRESHOLD) {
                                isShake = true;
                                vibrator.vibrate(new long[]{500, 200, 500, 200}, -1);
                                switchNext();
                            }
                        }
                    }

                    @Override
                    public void onAccuracyChanged(Sensor sensor, int accuracy) {
                        // TODO Auto-generated method stub

                    }

                };
            }
            sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_GAME);
        } else {
            iv_last.setVisibility(View.INVISIBLE);
            iv_next.setVisibility(View.INVISIBLE);
            tv_choosee_device.setVisibility(View.GONE);
        }
        // 刷新监控
        rlPrgTxError = (RelativeLayout) findViewById(R.id.rl_prgError);
        txError = (TextView) findViewById(R.id.tx_monitor_error);
        btnRefrash = (Button) findViewById(R.id.btn_refrash);
        progressBar = (ProgressBar) findViewById(R.id.prg_monitor);
        tx_wait_for_connect = (TextView) findViewById(R.id.tx_wait_for_connect);
        ivHeader = (HeaderView) findViewById(R.id.hv_header);
        rlPrgTxError.setOnClickListener(this);
        btnRefrash.setOnClickListener(this);
        // 更新头像
        setHeaderImage();

        iv_full_screen.setOnClickListener(this);
        iv_voice.setOnClickListener(this);
        iv_defence.setOnClickListener(this);
        iv_screenshot.setOnClickListener(this);
        back_btn.setOnClickListener(this);
        choose_video_format.setOnClickListener(this);
        close_voice.setOnClickListener(this);
        send_voice.setOnClickListener(this);
        iv_half_screen.setOnClickListener(this);
        hungup.setOnClickListener(this);
        screenshot.setOnClickListener(this);
        video_mode_hd.setOnClickListener(this);
        video_mode_sd.setOnClickListener(this);
        video_mode_ld.setOnClickListener(this);
        defence_state.setOnClickListener(this);
        iv_last.setOnClickListener(this);
        iv_next.setOnClickListener(this);
        tv_choosee_device.setOnClickListener(this);
        open_door.setOnClickListener(this);
        tv_name.setText(mContact.getContactName());
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
        if (mContact.contactType == P2PValue.DeviceType.NPC) {
            current_video_mode = P2PValue.VideoMode.VIDEO_MODE_LD;
        } else {
            current_video_mode = P2PConnect.getMode();
        }
        if (isSurpportOpenDoor == true) {
            open_door.setVisibility(ImageView.VISIBLE);
        } else {
            open_door.setVisibility(ImageView.GONE);
        }
        updateVideoModeText(current_video_mode);
        if (mContact.contactType != P2PValue.DeviceType.DOORBELL && !isSurpportOpenDoor) {
            iv_speak.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View arg0, MotionEvent event) {
                    // TODO Auto-generated method stub
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            hideVideoFormat();
                            layout_voice_state.setVisibility(RelativeLayout.VISIBLE);
                            iv_speak.setBackgroundResource(R.drawable.portrait_speak_p);
//						    T.showShort(mContext, R.string.hold_talk);
                            setMute(false);
                            return true;
                        case MotionEvent.ACTION_UP:
                            layout_voice_state
                                    .setVisibility(RelativeLayout.GONE);
                            iv_speak
                                    .setBackgroundResource(R.drawable.portrait_speak);
                            setMute(true);
                            return true;
                    }
                    return false;
                }
            });
            send_voice.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View arg0, MotionEvent event) {
                    // TODO Auto-generated method stub
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
            });
        } else if (mContact.contactType == P2PValue.DeviceType.DOORBELL
                && !isSurpportOpenDoor) {
            isFirstMute = false;
            iv_speak.setOnClickListener(this);
            send_voice.setOnClickListener(this);
        } else if (isSurpportOpenDoor) {
            Log.e("leleTest", "isSurpportOpenDoor=" + isSurpportOpenDoor);
            iv_speak.setOnClickListener(this);
            // 开始监控时没有声音，暂时这样
            send_voice.setOnClickListener(this);
            iv_speak.performClick();
            iv_speak.performClick();
//			speak();
//			speak();
//			send_voice.performClick();
            // speak();
        }
        initIpcDeviceList();
    }

    public void initSpeark(int deviceType, boolean isOpenDor) {
        if (isOpenDor == true) {
            open_door.setVisibility(View.VISIBLE);
        } else {
            open_door.setVisibility(View.GONE);
        }
        if (deviceType != P2PValue.DeviceType.DOORBELL && !isOpenDor) {
            iv_speak.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View arg0, MotionEvent event) {
                    // TODO Auto-generated method stub
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            hideVideoFormat();
                            layout_voice_state
                                    .setVisibility(RelativeLayout.VISIBLE);

//						    iv_speak.setBackgroundResource(R.drawable.portrait_speak_p);
                            T.showShort(mContext, R.string.hold_talk);
                            hideVideoFormat();
                            setMute(false);
                            return true;
                        case MotionEvent.ACTION_UP:
                            layout_voice_state
                                    .setVisibility(RelativeLayout.GONE);
                            iv_speak
                                    .setBackgroundResource(R.drawable.portrait_speak);
                            setMute(true);
                            return true;
                    }
                    return false;
                }
            });
            send_voice.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View arg0, MotionEvent event) {
                    // TODO Auto-generated method stub
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
            });
        } else if (deviceType == P2PValue.DeviceType.DOORBELL
                && !isOpenDor) {
            isFirstMute = false;
            iv_speak.setOnTouchListener(null);
            send_voice.setOnTouchListener(null);
            iv_speak.setOnClickListener(this);
            send_voice.setOnClickListener(this);
        } else if (isOpenDor) {
            iv_speak.setOnTouchListener(null);
            send_voice.setOnTouchListener(null);
            control_bottom.setVisibility(View.VISIBLE);
            iv_speak.setOnClickListener(this);
            // 开始监控时没有声音，暂时这样
            send_voice.setOnClickListener(this);
            isFirstMute = true;
            iv_speak.performClick();
            iv_speak.performClick();
            // speak();
        }
    }

    private void setHeaderImage() {
        ivHeader.updateImage(callId, true, 1);
    }

    /**
     * 刷新IPC和NPC布局异同
     */
    private void frushLayout(int contactType) {
        if (contactType == P2PValue.DeviceType.IPC) {
            video_mode_hd.setVisibility(View.VISIBLE);
            vLineHD.setVisibility(View.VISIBLE);
        } else if (contactType == P2PValue.DeviceType.NPC) {
            video_mode_hd.setVisibility(View.GONE);
            vLineHD.setVisibility(View.GONE);
        }
    }

    public void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.P2P.P2P_ACCEPT);
        filter.addAction(Constants.P2P.P2P_READY);
        filter.addAction(Constants.P2P.P2P_REJECT);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Constants.P2P.ACK_RET_CHECK_PASSWORD);
        filter.addAction(Constants.P2P.RET_GET_REMOTE_DEFENCE);
        filter.addAction(Constants.P2P.RET_SET_REMOTE_DEFENCE);
        filter.addAction(Constants.P2P.P2P_RESOLUTION_CHANGE);
        filter.addAction(Constants.P2P.DELETE_BINDALARM_ID);
        filter.addAction(Constants.Action.MONITOR_NEWDEVICEALARMING);
        filter.addAction(Constants.P2P.RET_P2PDISPLAY);
        filter.addAction(Constants.P2P.ACK_GET_REMOTE_DEFENCE);
        mContext.registerReceiver(mReceiver, filter);
        isRegFilter = true;
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(Constants.P2P.P2P_READY)) {
                Log.e("monitor", "P2P_READY" + "callId=" + callId);
                P2PHandler.getInstance().getDefenceStates(callId, password);
                isReceveHeader = false;
                isShake = false;
                iv_last.setClickable(true);
                iv_next.setClickable(true);
                P2PConnect.setMonitorId(callId);
                SettingListener.setMonitorID(callId);
            } else if (intent.getAction().equals(Constants.P2P.P2P_ACCEPT)) {
                int[] type = intent.getIntArrayExtra("type");
                P2PView.type = type[0];
                P2PView.scale = type[1];
                int Heigh = 0;
                if (P2PView.type == 1 && P2PView.scale == 0) {
                    Heigh = screenWidth * 3 / 4;
                    setIsLand(true);
                } else {
                    Heigh = screenWidth * 9 / 16;
                    setIsLand(false);
                }
                if (ScrrenOrientation == Configuration.ORIENTATION_PORTRAIT) {
                    LinearLayout.LayoutParams parames = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    parames.height = Heigh;
                    r_p2pview.setLayoutParams(parames);
                }
            } else if (intent.getAction().equals(Constants.P2P.ACK_RET_CHECK_PASSWORD)) {
                finish();
            } else if (intent.getAction().equals(Constants.P2P.P2P_REJECT)) {
                String error = intent.getStringExtra("error");
                int code = intent.getIntExtra("code", 9);
                showError(error, code);
                isShake = false;
                iv_last.setClickable(true);
                iv_next.setClickable(true);
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_GET_REMOTE_DEFENCE)) {
                String ids = intent.getStringExtra("contactId");
                if (!ids.equals("") && ids.equals(callId)) {
                    defenceState = intent.getIntExtra("state", -1);
                    changeDefence(defenceState);
                }
                defence_state.setVisibility(ImageView.VISIBLE);
            } else if (intent.getAction().equals(Constants.P2P.RET_SET_REMOTE_DEFENCE)) {
                int result = intent.getIntExtra("state", -1);
                if (result == 0) {
                    if (defenceState == Constants.DefenceState.DEFENCE_STATE_ON) {
                        defenceState = Constants.DefenceState.DEFENCE_STATE_OFF;
                    } else {
                        defenceState = Constants.DefenceState.DEFENCE_STATE_ON;
                    }
                    changeDefence(defenceState);
                }
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                String error = intent.getStringExtra("error");
                showError(error, 0);
            } else if (intent.getAction().equals(
                    Constants.Action.MONITOR_NEWDEVICEALARMING)) {
                // 弹窗
                int MessageType = intent.getIntExtra("messagetype", 2);
                int type = intent.getIntExtra("alarm_type", 0);
                pushAlarmType = type;
                disconnectDooranerfa();
                isCustomCmdAlarm = intent.getBooleanExtra("isCustomCmdAlarm", false);
                int group = intent.getIntExtra("group", -1);
                int item = intent.getIntExtra("item", -1);
                boolean isSupport = intent.getBooleanExtra("isSupport", false);
                boolean isSupportDelete = intent.getBooleanExtra("isSupportDelete", false);
                if (MessageType == 1) {
                    // 报警推送
                    NewMessageDeviceID = intent.getStringExtra("alarm_id");
                    if (alarm_id.equals(NewMessageDeviceID) && passworddialog != null && passworddialog.isShowing()) {
                        return;
                    } else {
                        alarm_id = NewMessageDeviceID;
                    }
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
                        .append("：").append(
                                Utils.getDeviceName(NewMessageDeviceID));
                NewMassage.append("\n").append(Utils.getStringByResouceID(R.string.allarm_type)).
                        append(alarmtype);
                NewMessageDialog(NewMassage.toString(), NewMessageDeviceID, isSupportDelete);
            } else if (intent.getAction().equals(Constants.P2P.RET_P2PDISPLAY)) {
                Log.e("monitor", "RET_P2PDISPLAY");
                connectSenconde = true;
                if (!isReceveHeader) {
                    hindRlProTxError();
                    pView.updateScreenOrientation();
//					 iv_full_screen.setVisibility(View.VISIBLE);
                    isReceveHeader = true;
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.DELETE_BINDALARM_ID)) {
                int result = intent.getIntExtra("deleteResult", 1);
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (result == 0) {
                    // 删除成功
                    T.showShort(mContext, R.string.modify_success);
                } else if (result == -1) {
                    // 不支持
                    T.showShort(mContext, R.string.device_not_support);
                } else {
                    // 失败
                }
            } else if (intent.getAction().equals(Constants.P2P.ACK_GET_REMOTE_DEFENCE)) {
                String contactId = intent.getStringExtra("contactId");
                int result = intent.getIntExtra("result", -1);
                if (contactId.equals(callId)) {
                    if (result == Constants.P2P_SET.ACK_RESULT.ACK_INSUFFICIENT_PERMISSIONS) {
                        isPermission = false;
                    }
                }

            }
        }
    };

    public void changeDefence(int defencestate) {
        if (defencestate == Constants.DefenceState.DEFENCE_STATE_ON) {
            iv_defence.setBackgroundResource(R.drawable.selector_portrait_arm);
            defence_state.setImageResource(R.drawable.deployment);
        } else {
            iv_defence.setBackgroundResource(R.drawable.selector_portrait_disarm);
            defence_state.setImageResource(R.drawable.disarm);
        }
    }

    /**
     * 隐藏过度页
     */
    private void hindRlProTxError() {
//		ObjectAnimator anima = ObjectAnimator.ofFloat(rlPrgTxError, "alpha",
//				1.0f, 0f);
        rlPrgTxError.setVisibility(View.GONE);
//		anima.setDuration(500).start();
//		anima.addListener(new AnimatorListener() {
//
//			@Override
//			public void onAnimationStart(Animator animation) {
//			}
//
//			@Override
//			public void onAnimationRepeat(Animator animation) {
//				// TODO Auto-generated method stub
//			}
//
//			@Override
//			public void onAnimationEnd(Animator animation) {
//				rlPrgTxError.setVisibility(View.GONE);
//			}
//
//			@Override
//			public void onAnimationCancel(Animator animation) {
//			}
//		});
    }

    private void showRlProTxError() {
        ObjectAnimator anima = ObjectAnimator.ofFloat(rlPrgTxError, "alpha",
                0f, 1.0f);
        rlPrgTxError.setVisibility(View.VISIBLE);
        anima.setDuration(500).start();
        anima.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }
        });
    }

    // 设置布防
    public void setDefence() {
        if (!isPermission) {
            T.showShort(mContext, R.string.insufficient_permissions);
            return;
        }
        if (defenceState == Constants.DefenceState.DEFENCE_STATE_ON) {
            P2PHandler.getInstance().setRemoteDefence(mContact.getContactId(), password,
                    Constants.P2P_SET.REMOTE_DEFENCE_SET.ALARM_SWITCH_OFF);

        } else if (defenceState == Constants.DefenceState.DEFENCE_STATE_OFF) {
            P2PHandler.getInstance().setRemoteDefence(mContact.getContactId(), password,
                    Constants.P2P_SET.REMOTE_DEFENCE_SET.ALARM_SWITCH_ON);
        }

    }

    public void callDevice() {
        P2PConnect.setCurrent_state(P2PConnect.P2P_STATE_CALLING);
        P2PConnect.setCurrent_call_id(callId);
        String push_mesg = NpcCommon.mThreeNum
                + ":"
                + mContext.getResources().getString(
                R.string.p2p_call_push_mesg);
        Log.e("dxsTest", "NpcCommon.mThreeNum-->" + NpcCommon.mThreeNum + "mContact.contactId-->" + mContact.contactId + "connectType-->" + connectType + "AppConfig.VideoMode-->" + AppConfig.VideoMode);
        if (connectType == Constants.ConnectType.RTSPCONNECT) {
            callType = 3;
            String ipAddress = "";
            String ipFlag = "";
            if (mContact.ipadressAddress != null) {
                ipAddress = mContact.ipadressAddress.getHostAddress();
                ipFlag = ipAddress.substring(ipAddress.lastIndexOf(".") + 1, ipAddress.length());
            } else {

            }
            P2PHandler.getInstance().call(NpcCommon.mThreeNum, "0", true, Constants.P2P_TYPE.P2P_TYPE_MONITOR, "1", "1", push_mesg, AppConfig.VideoMode, mContact.contactId);
//			P2PHandler.getInstance().RTSPConnect(NpcCommon.mThreeNum, mContact.contactPassword, true, callType, mContact.contactId, ipFlag, push_mesg, ipAddress,AppConfig.VideoMode,rtspHandler);	
        } else if (connectType == Constants.ConnectType.P2PCONNECT) {
            callType = 1;
            String ipAdress = FList.getInstance().getCompleteIPAddress(mContact.contactId);
            P2PHandler.getInstance().call(NpcCommon.mThreeNum, password, true, Constants.P2P_TYPE.P2P_TYPE_MONITOR, callId, ipAdress, push_mesg, AppConfig.VideoMode, mContact.contactId);
        }
    }

    Handler rtspHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Log.e("dxswifi", "rtsp失败");
                    showError("connect error", 0);
                    P2PHandler.getInstance().reject();
                    break;
                case 1:
                    Log.e("dxswifi", "rtsp成功");
                    rlPrgTxError.setVisibility(View.GONE);
                    P2PConnect.setCurrent_state(2);
                    playReady();
                    mContact.apModeState = Constants.APmodeState.LINK;
                    break;
            }
        }
    };

    private void playReady() {
//		P2PHandler.getInstance().openAudioAndStartPlaying(callType);
        Intent ready = new Intent();
        ready.setAction(Constants.P2P.P2P_READY);
        this.sendBroadcast(ready);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ScrrenOrientation = Configuration.ORIENTATION_LANDSCAPE;
            layout_title.setVisibility(View.GONE);
            l_control.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            rl_control.setVisibility(View.GONE);
//			设置control_bottom的高度
            int height = (int) getResources().getDimension(R.dimen.p2p_monitor_bar_height);
            setControlButtomHeight(height);
            control_bottom.setVisibility(View.VISIBLE);
//			setIsLand(true);
            pView.fullScreen();
            isFullScreen = true;
            LinearLayout.LayoutParams parames = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            r_p2pview.setLayoutParams(parames);
        } else {
            ScrrenOrientation = Configuration.ORIENTATION_PORTRAIT;
            layout_title.setVisibility(View.VISIBLE);
            l_control.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
            rl_control.setVisibility(View.VISIBLE);
//			设置control_bottom的高度等于0
            setControlButtomHeight(0);
            control_bottom.setVisibility(View.INVISIBLE);
            control_top.setVisibility(View.GONE);
//			setIsLand(false);
            if (isFullScreen) {
                isFullScreen = false;
                pView.halfScreen();
                Log.e("half", "half screen--");
            }
            if (P2PView.type == 1) {
                if (P2PView.scale == 0) {
                    int Heigh = screenWidth * 3 / 4;
                    LinearLayout.LayoutParams parames = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    parames.height = Heigh;
                    r_p2pview.setLayoutParams(parames);
                } else {
                    int Heigh = screenWidth * 9 / 16;
                    LinearLayout.LayoutParams parames = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    parames.height = Heigh;
                    r_p2pview.setLayoutParams(parames);
                }
            } else {
                if (mContact.contactType == P2PValue.DeviceType.NPC) {
                    int Heigh = screenWidth * 3 / 4;
                    LinearLayout.LayoutParams parames = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    parames.height = Heigh;
                    r_p2pview.setLayoutParams(parames);
                } else {
                    int Heigh = screenWidth * 9 / 16;
                    LinearLayout.LayoutParams parames = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    parames.height = Heigh;
                    r_p2pview.setLayoutParams(parames);
                }
            }
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
    protected void onP2PViewSingleTap() {
        // TODO Auto-generated method stub
        changeControl();
    }

    @Override
    protected void onCaptureScreenResult(boolean isSuccess, int prePoint) {
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

    @Override
    public int getActivityInfo() {
        // TODO Auto-generated method stub
        return Constants.ActivityInfo.ACTIVITY_APMONITORACTIVITY;
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
    public void onBackPressed() {
        reject();
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        if (mAudioManager != null) {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                    mCurrentVolume, 0);
        }
        if (isRegFilter) {
            mContext.unregisterReceiver(mReceiver);
            isRegFilter = false;
        }
        P2PConnect.setPlaying(false);
        P2PConnect.setMonitorId("");// 设置在监控的ID为空
        SettingListener.setMonitorID("");
        if (sensorListener != null) {
            sensorManager.unregisterListener(sensorListener);
        }
        if (!activity_stack
                .containsKey(Constants.ActivityInfo.ACTIVITY_MAINACTIVITY)) {
            Intent i = new Intent(this, MainActivity.class);
            this.startActivity(i);
        }
        Intent refreshContans = new Intent();
        refreshContans.setAction(Constants.Action.REFRESH_CONTANTS);
        mContext.sendBroadcast(refreshContans);
        super.onDestroy();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
            mCurrentVolume++;
            if (mCurrentVolume > mMaxVolume) {
                mCurrentVolume = mMaxVolume;
            }

            if (mCurrentVolume != 0) {
                mIsCloseVoice = false;
                iv_voice.setImageResource(R.drawable.selector_half_screen_voice_open);
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
                iv_voice.setImageResource(R.drawable.selector_half_screen_voice_close);
                close_voice.setBackgroundResource(R.drawable.m_voice_off);
            }

            return false;
        }

        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int resId = v.getId();
        if (resId == R.id.iv_full_screen) {
            ScrrenOrientation = Configuration.ORIENTATION_LANDSCAPE;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (resId == R.id.defence_state || resId == R.id.iv_defence) {
            setDefence();
        } else if (resId == R.id.close_voice || resId == R.id.close_voice) {
            if (mIsCloseVoice) {
                mIsCloseVoice = false;
                iv_voice.setImageResource(R.drawable.selector_half_screen_voice_open);
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
                iv_voice.setImageResource(R.drawable.selector_half_screen_voice_close);
                close_voice.setBackgroundResource(R.drawable.m_voice_off);
                if (mAudioManager != null) {
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0,
                            0);
                }
            }
        } else if (resId == R.id.screenshot || resId == R.id.iv_screenshot) {
            this.captureScreen(-1);
        } else if (resId == R.id.hungup || resId == R.id.back_btn) {
            reject();
        } else if (resId == R.id.choose_video_format) {
            changevideoformat();
        } else if (resId == R.id.iv_half_screen) {
            control_bottom.setVisibility(View.INVISIBLE);
            ScrrenOrientation = Configuration.ORIENTATION_PORTRAIT;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            // 竖屏使半屏
//             if (isFullScreen) {
//                  isFullScreen = false;
//                  pView.halfScreen();
//                  Log.e("half", "half screen++");
//             }
        } else if (resId == R.id.video_mode_hd) {
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
        } else if (resId == R.id.rl_prgError || resId == R.id.btn_refrash) {
            if (btnRefrash.getVisibility() == View.VISIBLE) {
                hideError();
                callDevice();
            }
        } else if (resId == R.id.iv_next) {
            switchNext();
        } else if (resId == R.id.iv_last) {
            switchLast();
        } else if (resId == R.id.tv_choosee_device) {
            if (isShowDeviceList) {
                l_device_list.setVisibility(View.GONE);
                isShowDeviceList = false;
            } else {
                l_device_list.setVisibility(View.VISIBLE);
                isShowDeviceList = true;
            }
        } else if (resId == R.id.open_door) {
            openDor();
        } else if (resId == R.id.iv_speak || resId == R.id.send_voice) {
            if (!isSpeak) {
                speak();
            } else {
                noSpeak();
            }
        }
    }

    // 设置成对话状态
    private void speak() {
        hideVideoFormat();
        layout_voice_state.setVisibility(RelativeLayout.VISIBLE);
        send_voice.setBackgroundResource(R.drawable.ic_send_audio_p);
        iv_speak.setBackgroundResource(R.drawable.portrait_speak_p);
        setMute(false);
        isSpeak = true;
        Log.e("leleSpeak", "speak--" + isSpeak);
    }

    private void noSpeak() {
        send_voice.setBackgroundResource(R.drawable.ic_send_audio);
        iv_speak.setBackgroundResource(R.drawable.portrait_speak);
        layout_voice_state.setVisibility(RelativeLayout.GONE);
        setMute(true);
        isSpeak = false;
        mhandler.postDelayed(mrunnable, 500);
        Log.e("leleSpeak", "no speak--" + isSpeak);
    }

    private boolean isFirstMute = true;
    Runnable mrunnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (isFirstMute) {
                Log.e("leleSpeak", "mrunnable--");
                send_voice.performClick();
                isFirstMute = false;
            }
        }
    };

    public void stopSpeak() {
        send_voice.setBackgroundResource(R.drawable.ic_send_audio);
        iv_speak.setBackgroundResource(R.drawable.portrait_speak);
        layout_voice_state.setVisibility(RelativeLayout.GONE);
        setMute(true);
        isSpeak = false;
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
                if (isCustomCmdAlarm == true) {
                    String cmd = "IPC1anerfa:unlock";
                    P2PHandler.getInstance().sendCustomCmd(callId, password, cmd);
                } else {
                    P2PHandler.getInstance().setGPIO1_0(callId, password);
                }
            }
        });
        dialog.showDialog();
    }

    Handler sHandler = new Handler() {
        public void handleMessage(Message msg) {
            switchConnect();
        }

        ;
    };

    /**
     * 展示连接错误
     *
     * @param error
     */
    public void showError(String error, int code) {
        if (!connectSenconde && code != 9) {
            callDevice();
            connectSenconde = true;
            return;
        }
        progressBar.setVisibility(View.GONE);
        tx_wait_for_connect.setVisibility(View.GONE);
        txError.setVisibility(View.VISIBLE);
        btnRefrash.setVisibility(View.VISIBLE);
        txError.setText(error);
    }

    /**
     * 隐藏连接错误
     */
    private void hideError() {
        progressBar.setVisibility(View.VISIBLE);
        tx_wait_for_connect.setText(getResources().getString(R.string.waite_for_linke));
        tx_wait_for_connect.setVisibility(View.VISIBLE);
        txError.setVisibility(View.GONE);
        btnRefrash.setVisibility(View.GONE);
    }

    /**
     * 切换连接
     */
    private void switchConnect() {
        progressBar.setVisibility(View.VISIBLE);
        tx_wait_for_connect.setText(getResources().getString(R.string.switch_connect));
        tx_wait_for_connect.setVisibility(View.VISIBLE);
        txError.setVisibility(View.GONE);
        btnRefrash.setVisibility(View.GONE);
//		iv_full_screen.setVisibility(View.INVISIBLE);
        showRlProTxError();
        Log.e("switchConnect", "switchConnect");
    }

    public void reject() {
        if (!isReject) {
            isReject = true;
            P2PHandler.getInstance().reject();
            disconnectDooranerfa();
            finish();
        }
    }

    public void readyCallDevice() {
        if (connectType == Constants.ConnectType.P2PCONNECT) {
            P2PHandler.getInstance().openAudioAndStartPlaying(1);
            P2PHandler.getInstance().getDefenceStates(callId, password);
        } else {
            P2PHandler.getInstance().openAudioAndStartPlaying(1);
            callId = "1";
            password = "0";
            P2PHandler.getInstance().getDefenceStates(callId, password);
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

    public void changeControl() {
        if (isSpeak) {// 对讲过程中不可消失
            return;
        }
        if (ScrrenOrientation == Configuration.ORIENTATION_PORTRAIT) {
            return;
        }
        Log.e("changeControl", "changeControl");
        if (control_bottom.getVisibility() == RelativeLayout.VISIBLE) {
            Log.e("changeControl", "changeControl--VISIBLE");
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
                    control_bottom.setVisibility(RelativeLayout.INVISIBLE);
                    choose_video_format
                            .setBackgroundResource(R.drawable.sd_backgroud);
                    choose_video_format.setClickable(true);
                }
            });

        } else {
            Log.e("changeControl", "changeControl--INVISIBLE");
            control_bottom.setVisibility(RelativeLayout.VISIBLE);
            control_bottom.bringToFront();
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

    /**
     * 新报警信息
     */
    NormalDialog dialog;
    String contactidTemp = "";

    private void NewMessageDialog(String Meassage, final String contacid, boolean isSurportdelete) {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        dialog = new NormalDialog(mContext);
        dialog.setContentStr(Meassage);
        dialog.setbtnStr1(R.string.check);
        dialog.setbtnStr2(R.string.cancel);
        dialog.setbtnStr3(R.string.clear_bundealarmid);
        dialog.showAlarmDialog(isSurportdelete, contacid);
        dialog.setOnAlarmClickListner(AlarmClickListner);
        contactidTemp = contacid;
    }

    /**
     * 监控对话框单击回调
     */
    private OnAlarmClickListner AlarmClickListner = new OnAlarmClickListner() {

        @Override
        public void onOkClick(String alarmID, boolean isSurportDelete, Dialog dialog) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            // 查看新监控--挂断当前监控，再次呼叫另一个监控
            seeMonitor(alarmID);
        }

        @Override
        public void onDeleteClick(String alarmID, boolean isSurportDelete,
                                  Dialog dialog) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            DeleteDevice(alarmID);
        }

        @Override
        public void onCancelClick(String alarmID, boolean isSurportDelete,
                                  Dialog dialog) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    };

    //解绑确认弹框
    private void DeleteDevice(final String alarmId) {
        dialog = new NormalDialog(mContext, mContext.getResources().getString(
                R.string.clear_bundealarmid), mContext.getResources()
                .getString(R.string.clear_bundealarmid_tips), mContext
                .getResources().getString(R.string.ensure), mContext
                .getResources().getString(R.string.cancel));
        dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {

            @Override
            public void onClick() {
                P2PHandler.getInstance().DeleteDeviceAlarmId(
                        String.valueOf(alarmId));
                dialog.dismiss();
                ShowLoading();
            }
        });
        dialog.showDialog();
    }

    private void ShowLoading() {
        dialog = new NormalDialog(mContext);
        dialog.showLoadingDialog();
    }

    private void seeMonitor(String contactId) {
        number = 1;
        final Contact contact = FList.getInstance().isContact(contactId);
        if (null != contact) {
            P2PHandler.getInstance().reject();
            switchConnect();
            changeDeviceListTextColor();
            callId = contact.contactId;
            password = contact.contactPassword;
            tv_name.setText(callId);
            if (isSpeak) {
                stopSpeak();
            }
            setHeaderImage();
            if (pushAlarmType == P2PValue.AlarmType.ALARM_TYPE_DOORBELL_PUSH) {
                initSpeark(contact.contactType, true);
            } else {
                initSpeark(contact.contactType, false);
            }
            connectDooranerfa();
            callDevice();
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            frushLayout(P2PValue.DeviceType.IPC);
        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            Log.i("dxsmonitor", contactId);
            createPassDialog(contactId);
        }
    }

    private Dialog passworddialog;

    void createPassDialog(String id) {
        passworddialog = new MyInputPassDialog(mContext, Utils.getStringByResouceID(R.string.check), id, listener);
        passworddialog.show();
    }

    private OnCustomDialogListener listener = new OnCustomDialogListener() {

        @Override
        public void check(final String password, final String id) {
            if (password.trim().equals("")) {
                T.showShort(mContext, R.string.input_monitor_pwd);
                return;
            }

            if (password.length() > 30 || password.charAt(0) == '0') {
                T.showShort(mContext, R.string.device_password_invalid);
                return;
            }

            P2PConnect.vReject(9, "");
            new Thread() {
                @Override
                public void run() {
                    while (true) {
                        if (P2PConnect.getCurrent_state() == P2PConnect.P2P_STATE_NONE) {
                            Message msg = new Message();
                            String pwd = P2PHandler.getInstance().EntryPassword(password);
                            String[] data = new String[]{id, pwd,
                                    String.valueOf(pushAlarmType)};
                            msg.what = 1;
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
    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (msg.what == 0) {
                Contact contact = (Contact) msg.obj;
                Intent monitor = new Intent(mContext, ApMonitorActivity.class);
                monitor.putExtra("contact", contact);
                monitor.putExtra("connectType", Constants.ConnectType.P2PCONNECT);
                startActivity(monitor);

//				Intent monitor = new Intent();
//				monitor.setClass(mContext, CallActivity.class);
//				monitor.putExtra("callId", contact.contactId);
//				monitor.putExtra("password", contact.contactPassword);
//				monitor.putExtra("isOutCall", true);
//				monitor.putExtra("contactType", P2PValue.DeviceType.NPC);
//				monitor.putExtra("type", Constants.P2P_TYPE.P2P_TYPE_MONITOR);


//				if (Integer.parseInt(data[2]) == P2PValue.DeviceType.DOORBELL) {
//					monitor.putExtra("isSurpportOpenDoor", true);
//				}
//				startActivity(monitor);
//				finish();
            } else if (msg.what == 1) {
                if (passworddialog != null && passworddialog.isShowing()) {
                    passworddialog.dismiss();
                }
                String[] data = (String[]) msg.obj;
//				Contact contact=new Contact();
//				contact.contactId=data[0];
//				contact.contactName=data[0];
//				contact.contactPassword=data[1];
//				contact.contactType=P2PValue.DeviceType.IPC;
//				Intent monitor=new Intent(mContext,ApMonitorActivity.class);
//				monitor.putExtra("contact", contact);
//				monitor.putExtra("connectType", Constants.ConnectType.P2PCONNECT);
//				startActivity(monitor);
//				finish();
                P2PHandler.getInstance().reject();
                switchConnect();
                changeDeviceListTextColor();
                callId = data[0];
                password = data[1];
                tv_name.setText(callId);
                if (isSpeak) {
                    stopSpeak();
                }
                setHeaderImage();
                if (pushAlarmType == P2PValue.AlarmType.ALARM_TYPE_DOORBELL_PUSH) {
                    initSpeark(P2PValue.DeviceType.DOORBELL, true);
                    Log.e("leleMonitor", "switch doorbell push");
                } else {
                    initSpeark(P2PValue.DeviceType.IPC, false);
                    Log.e("leleMonitor", "switch---");
                }
                connectDooranerfa();
                callDevice();
                frushLayout(P2PValue.DeviceType.IPC);

            }
//			Intent monitor = new Intent();
//			monitor.setClass(mContext, CallActivity.class);
//			monitor.putExtra("callId", data[0]);
//			monitor.putExtra("password", data[1]);
//			monitor.putExtra("isOutCall", true);
//			monitor.putExtra("contactType", Integer.parseInt(data[2]));
//			monitor.putExtra("type", Constants.P2P_TYPE.P2P_TYPE_MONITOR);
//			if (Integer.parseInt(data[2]) == P2PValue.DeviceType.DOORBELL) {
//				monitor.putExtra("isSurpportOpenDoor", true);
//			}
            return false;
        }
    });

    @Override
    public void onHomePressed() {
        // TODO Auto-generated method stub
        super.onHomePressed();
        reject();
    }

    public void getScreenWithHeigh() {
        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeigh = dm.heightPixels;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        readyCallDevice();
        initp2pView();
    }

    /*
    * 初始化P2pview
    */
    public void initp2pView() {
        this.initP2PView(mContact.contactType);
        WindowManager manager = getWindowManager();
        window_width = manager.getDefaultDisplay().getWidth();
        window_height = manager.getDefaultDisplay().getHeight();
        this.initScaleView(this, window_width, window_height);
        setMute(true);
    }

    public void initIpcDeviceList() {
//    	if(number<0){
//    		number=0;
//    	}
        LinearLayout.LayoutParams p = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        p.height = dip2px(mContext, 40 * number);
        l_device_list.setLayoutParams(p);
        for (int i = 0; i < number; i++) {
            View view = LayoutInflater.from(mContext).inflate(
                    R.layout.item_device, null);
            final TextView tv_deviceId = (TextView) view.findViewById(R.id.tv_deviceId);
            tv_deviceId.setText(ipcList[i]);
            if (i == 0) {
                tv_deviceId.setTextColor(getResources().getColor(R.color.device_blue_p));
            } else {
                tv_deviceId.setTextColor(getResources().getColor(R.color.white));
            }
            devicelist.add(tv_deviceId);
            l_device_list.addView(view);
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    Message msg = new Message();
                    msg.what = Integer.parseInt(tv_deviceId.getText().toString());
                    chandler.sendMessage(msg);
                }
            });
        }
    }

    Handler chandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            for (int i = 0; i < ipcList.length; i++) {
                if (ipcList[i].equals(String.valueOf(msg.what))) {
                    currentNumber = i;
                    P2PHandler.getInstance().reject();
                    changeDeviceListTextColor();
                    callId = ipcList[currentNumber];
                    callDevice();
                    iv_last.setClickable(false);
                }
            }


        }
    };

    public void changeDeviceListTextColor() {
        for (int i = 0; i < devicelist.size(); i++) {
            if (i == currentNumber) {
                devicelist.get(i).setTextColor(getResources().getColor(R.color.device_blue_p));
                devicelist.get(i).setClickable(false);
            } else {
                devicelist.get(i).setTextColor(getResources().getColor(R.color.white));
                devicelist.get(i).setClickable(true);
            }
        }

    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void switchNext() {
        if (currentNumber < number - 1) {
            currentNumber = currentNumber + 1;
        } else {
            currentNumber = 0;
        }
        P2PHandler.getInstance().reject();
        switchConnect();
        changeDeviceListTextColor();
        callId = ipcList[currentNumber];
        tv_name.setText(callId);
        setHeaderImage();
        iv_next.setClickable(false);
    }

    public void switchLast() {
        if (currentNumber > 0) {
            currentNumber = currentNumber - 1;
        } else {
            currentNumber = number - 1;
        }
        P2PHandler.getInstance().reject();
        switchConnect();
        changeDeviceListTextColor();
        callId = ipcList[currentNumber];
        tv_name.setText(callId);
        setHeaderImage();
        callDevice();
        iv_last.setClickable(false);
    }

    public void connectDooranerfa() {
        if (isCustomCmdAlarm == true) {
            String cmd_connect = "IPC1anerfa:connect";
            P2PHandler.getInstance().sendCustomCmd(callId, password,
                    cmd_connect);
        }
    }

    public void disconnectDooranerfa() {
        if (isCustomCmdAlarm == true) {
            String cmd_disconnect = "IPC1anerfa:disconnect";
            P2PHandler.getInstance().sendCustomCmd(callId, password,
                    cmd_disconnect);
        }
    }

    public void setControlButtomHeight(int height) {
        LinearLayout.LayoutParams control_bottom_parames = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        control_bottom_parames.height = height;
        control_bottom.setLayoutParams(control_bottom_parames);
    }
}
