package com.jwkj.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.test.MoreAsserts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jwkj.CallActivity;
import com.jwkj.P2PConnect;
import com.jwkj.SettingListener;
import com.jwkj.data.Contact;
import com.jwkj.data.SharedPreferencesManager;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.global.MyApp;
import com.jwkj.utils.MusicManger;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.widget.NormalDialog;
import com.lib.lockview.GlowPadView;
import com.lib.lockview.GlowPadView.OnTriggerListener;
import com.p2p.core.P2PHandler;
import com.yoosee.R;

public class DoorBellNewActivity extends Activity implements OnClickListener,
        OnTriggerListener {
    Context mContext;
    TextView monitor_btn, ignore_btn, shield_btn;
    int alarm_type, group, item;
    boolean isSupport;
    LinearLayout layout_area_chanel;
    TextView area_text, chanel_text;
    LinearLayout alarm_input, alarm_dialog;
    TextView alarm_go;
    EditText mPassword;
    boolean isAlarm;
    boolean hasContact = false;
    NormalDialog dialog;
    boolean isRegFilter = false;
    TextView tv_info, tv_type;
    String contactId = "";
    boolean isOpendoor;
    boolean isCustomCmdAlarm = false;

    RelativeLayout rlAlarmDefault, rlAlarmMotion, rlAlarmDoorbell;
    TextView alarm_id_text, alarm_type_text;
    ImageView alarm_img, alarm_bell, alarm_left_right;
    private GlowPadView mGlowPadView;
    TextView alarmIdName;
    private Dialog Pwddialog;
    private TextView tvDefenceArea;
    private int TIME_OUT = 20 * 1000;
    boolean CustomCmdDoorAlarm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // if(P2PConnect.isPlaying()){
        // if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // }
        // }
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        mContext = this;
        setContentView(R.layout.activity_newdoorbell);
        getExtralIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getExtralIntent(intent);
    }

    private void getExtralIntent(Intent intent) {
        String s = getIntent().getStringExtra("contactId");
        CustomCmdDoorAlarm = getIntent().getBooleanExtra("CustomCmdDoorAlarm", false);
        if (s.equals(contactId)) {
            contactId = s;
        } else {
            contactId = s;
            initComponent();
            excuteTimeOutTimer();
            regFilter();
        }
        P2PConnect.setDoorbellId(contactId);
    }

    public void loadMusicAndVibrate() {
        isAlarm = true;
        int a_muteState = SharedPreferencesManager.getInstance().getAMuteState(
                MyApp.app);
        if (a_muteState == 1) {
            MusicManger.getInstance().playAlarmMusic();
        }

        int a_vibrateState = SharedPreferencesManager.getInstance()
                .getAVibrateState(MyApp.app);
        if (a_vibrateState == 1) {
            new Thread() {
                public void run() {
                    while (isAlarm) {
                        MusicManger.getInstance().Vibrate();
                        Utils.sleepThread(100);
                    }
                    MusicManger.getInstance().stopVibrate();

                }
            }.start();
        }
    }

    public void initComponent() {
        mGlowPadView = (GlowPadView) findViewById(R.id.glow_pad_view);
        mGlowPadView.setOnTriggerListener(this);
        mGlowPadView.setShowTargetsOnIdle(true);

        rlAlarmDefault = (RelativeLayout) findViewById(R.id.rl_anim_alarm);
        rlAlarmMotion = (RelativeLayout) findViewById(R.id.rl_anim_motion);
        rlAlarmDoorbell = (RelativeLayout) findViewById(R.id.rl_anim_doorbell);
        // monitor_btn = (TextView) findViewById(R.id.monitor_btn);
        // ignore_btn = (TextView) findViewById(R.id.ignore_btn);
        // shield_btn = (TextView) findViewById(R.id.shield_btn);
        // alarm_id_text = (TextView) findViewById(R.id.tv_alarm_device_id);
        alarm_type_text = (TextView) findViewById(R.id.tv_alarm_type);

        alarm_img = (ImageView) findViewById(R.id.iv_alarm_anim);
        alarm_bell = (ImageView) findViewById(R.id.iv_alarm_bell);
        alarm_left_right = (ImageView) findViewById(R.id.iv_doorbell_left_right);

        alarm_id_text = (TextView) findViewById(R.id.tv_alarm_device_id);
        alarm_id_text.setText(String.format(alarm_id_text.getText().toString(),
                contactId));
        alarmIdName = (TextView) findViewById(R.id.tv_info);
        Contact contact = FList.getInstance().isContact(contactId);
        if (null != contact) {
            alarmIdName.setText(contact.contactName);
        } else {
            alarmIdName.setText(null);
        }
        tvDefenceArea = (TextView) findViewById(R.id.tv_defence_area);
        tvDefenceArea.setVisibility(View.GONE);
        showDoorbellAlarm();
    }

    void showDoorbellAlarm() {
        rlAlarmDefault.setVisibility(View.GONE);
        rlAlarmMotion.setVisibility(View.GONE);
        rlAlarmDoorbell.setVisibility(View.VISIBLE);
        final AnimationDrawable anim = (AnimationDrawable) alarm_bell
                .getDrawable();
        final AnimationDrawable anim1 = (AnimationDrawable) alarm_left_right
                .getDrawable();
        OnPreDrawListener opdl = new OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                anim.start();
                anim1.start();
                return true;
            }

        };
        setAlarmType(R.string.door_bell);
        alarm_bell.getViewTreeObserver().addOnPreDrawListener(opdl);
        alarm_left_right.getViewTreeObserver().addOnPreDrawListener(opdl);
    }

    void setAlarmType(int stringId) {
        String str = getResources().getString(stringId);
        alarm_type_text.setText(R.string.alarm_type);
        // alarm_type_text.setText(String.format(alarm_type_text.getText()
        // .toString(), str));
    }

    public void regFilter() {
        isRegFilter = true;
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.P2P.RET_CUSTOM_CMD_DISCONNECT);
        registerReceiver(br, filter);
    }

    BroadcastReceiver br = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            if (intent.getAction().equals(
                    Constants.P2P.RET_CUSTOM_CMD_DISCONNECT)) {
                finish();
            }

        }
    };
    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            String[] data = (String[]) msg.obj;
//			Intent monitor = new Intent();
//			monitor.setClass(mContext, CallActivity.class);
//			monitor.putExtra("callId", data[0]);
//			monitor.putExtra("password", data[1]);
//			monitor.putExtra("isOutCall", true);
//			monitor.putExtra("type", Constants.P2P_TYPE.P2P_TYPE_MONITOR);
//			monitor.putExtra("isSurpportOpenDoor", true);
//			startActivity(monitor);
            Contact contact = new Contact();
            contact.contactId = data[0];
            contact.contactName = data[0];
            contact.contactPassword = data[1];
            Intent monitor = new Intent(mContext, ApMonitorActivity.class);
            monitor.putExtra("contact", contact);
            monitor.putExtra("isSurpportOpenDoor", true);
            monitor.putExtra("CustomCmdDoorAlarm", CustomCmdDoorAlarm);
            monitor.putExtra("connectType", Constants.ConnectType.P2PCONNECT);
            startActivity(monitor);
            finish();
            return false;
        }
    });
    boolean viewed = false;
    Timer timeOutTimer;
    public static final int USER_HASNOTVIEWED = 3;
    private Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case USER_HASNOTVIEWED:
                    finish();
                    break;

                default:
                    break;
            }
            return false;
        }
    });

    // 超时计时器
    public void excuteTimeOutTimer() {
        timeOutTimer = new Timer();
        TimerTask mTask = new TimerTask() {
            @Override
            public void run() {
                // 弹出一个对话框
                if (!viewed) {
                    Message message = new Message();
                    message.what = USER_HASNOTVIEWED;
                    mHandler.sendMessage(message);
                }
            }
        };
        timeOutTimer.schedule(mTask, TIME_OUT);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int resId = v.getId();
        if (resId == R.id.ignore_btn) {
            int timeInterval = SharedPreferencesManager.getInstance()
                    .getAlarmTimeInterval(mContext);
            T.showShort(
                    mContext,
                    mContext.getResources().getString(
                            R.string.ignore_alarm_prompt_start)
                            + " "
                            + timeInterval
                            + " "
                            + mContext.getResources().getString(
                            R.string.ignore_alarm_prompt_end));
            finish();
        } else if (resId == R.id.monitor_btn) {
            alarm_go.setText("GO");
            mPassword.setHint(R.string.input_monitor_pwd);
            isOpendoor = false;
            final Contact contact = FList.getInstance().isContact(
                    String.valueOf(contactId));
            if (null != contact) {
                hasContact = true;
//				P2PConnect.vReject("");
//				new Thread() {
//					public void run() {
//						while (true) {
//							if (P2PConnect.getCurrent_state() == P2PConnect.P2P_STATE_NONE) {
//								Message msg = new Message();
//								String[] data = new String[] {
//										contact.contactId,
//										contact.contactPassword };
//								msg.obj = data;
//								handler.sendMessage(msg);
//								break;
//							}
//							Utils.sleepThread(500);
//						}
//					}
//				}.start();
                Intent monitor = new Intent(mContext, ApMonitorActivity.class);
                monitor.putExtra("contact", contact);
                monitor.putExtra("isCustomCmdAlarm", isCustomCmdAlarm);
                monitor.putExtra("connectType", Constants.ConnectType.P2PCONNECT);
                startActivity(monitor);
                finish();
            }
            if (!hasContact) {
                if (alarm_input.getVisibility() == RelativeLayout.VISIBLE) {
                    return;
                }

                alarm_input.setVisibility(RelativeLayout.VISIBLE);
                alarm_input.requestFocus();
                Animation anim = AnimationUtils.loadAnimation(mContext,
                        R.anim.slide_in_right);
                anim.setAnimationListener(new AnimationListener() {

                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        // TODO Auto-generated method stub
                        InputMethodManager m = (InputMethodManager) alarm_input
                                .getContext().getSystemService(
                                        Context.INPUT_METHOD_SERVICE);
                        m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                    }

                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationStart(Animation arg0) {
                        // TODO Auto-generated method stub

                    }

                });
                alarm_input.startAnimation(anim);
            }
        } else if (resId == R.id.alarm_go) {
            final String password = mPassword.getText().toString();
            if (password.trim().equals("")) {
                T.showShort(mContext, R.string.input_monitor_pwd);
                return;
            }

            if (password.length() > 9) {
                T.showShort(mContext, R.string.password_length_error);
                return;
            }
            if (!isOpendoor) {
                P2PConnect.vReject("");

                new Thread() {
                    public void run() {
                        while (true) {
                            if (P2PConnect.getCurrent_state() == P2PConnect.P2P_STATE_NONE) {
                                String pwd = P2PHandler.getInstance().EntryPassword(password);
                                Message msg = new Message();
                                String[] data = new String[]{
                                        String.valueOf(contactId), pwd};
                                msg.obj = data;
                                handler.sendMessage(msg);
                                break;
                            }
                            Utils.sleepThread(500);
                        }
                    }
                }.start();
            } else {
                String open_door_order = "IPC1anerfa:unlock";
                P2PHandler.getInstance().sendCustomCmd(contactId, password,
                        open_door_order);
                P2PHandler.getInstance().setGPIO1_0(contactId, password);
                finish();
            }
        } else if (resId == R.id.shield_btn) {
            Contact mcontact = FList.getInstance().isContact(
                    String.valueOf(contactId));
            String open_door_order = "IPC1anerfa:unlock";
            if (mcontact != null) {
                P2PHandler.getInstance().sendCustomCmd(mcontact.contactId,
                        mcontact.contactPassword, open_door_order);
                P2PHandler.getInstance().setGPIO1_0(mcontact.contactId,
                        mcontact.contactPassword);
                finish();
            } else {
                isOpendoor = true;
                alarm_go.setText(R.string.unlock);
                mPassword.setHint(R.string.input_lock_password);
                alarm_input.setVisibility(LinearLayout.VISIBLE);
            }
        }
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        SharedPreferencesManager.getInstance().putIgnoreAlarmTime(mContext,
                System.currentTimeMillis());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMusicAndVibrate();
        // SettingListener.setAlarm(true);
        P2PConnect.setDoorbell(true);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        isAlarm = false;
        MusicManger.getInstance().stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isRegFilter = true) {
            mContext.unregisterReceiver(br);
        }
        if (timeOutTimer != null) {
            timeOutTimer.cancel();
        }
        // SettingListener.setAlarm(false);
        P2PConnect.setDoorbell(false);
        P2PConnect.setDoorbellId("");
    }

    @Override
    public void onGrabbed(View v, int handle) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReleased(View v, int handle) {
        mGlowPadView.ping();

    }

    @Override
    public void onTrigger(View v, int target) {
        final int resId = mGlowPadView.getResourceIdForTarget(target);
        if (resId == R.drawable.ic_item_ignore) {
            viewed = true;
            int timeInterval = SharedPreferencesManager.getInstance()
                    .getAlarmTimeInterval(mContext);
            T.showShort(
                    mContext,
                    mContext.getResources().getString(
                            R.string.ignore_alarm_prompt_start)
                            + " "
                            + timeInterval
                            + " "
                            + mContext.getResources().getString(
                            R.string.ignore_alarm_prompt_end));
            P2PHandler.getInstance().setReceiveDoorBell(contactId);
            finish();
        } else if (resId == R.drawable.ic_item_go) {
            viewed = true;
            final Contact contact = FList.getInstance().isContact(contactId);
            if (null != contact) {
                hasContact = true;
                P2PConnect.vReject("");
                new Thread() {
                    @Override
                    public void run() {
                        while (true) {
                            if (P2PConnect.getCurrent_state() == P2PConnect.P2P_STATE_NONE) {
                                Message msg = new Message();
                                String[] data = new String[]{
                                        contact.contactId,
                                        contact.contactPassword};
                                msg.obj = data;
                                handler.sendMessage(msg);
                                break;
                            }
                            Utils.sleepThread(500);
                        }
                    }
                }.start();
            }

            if (!hasContact) {
                // 弹一个对话框
                creatDialog();
            }
        } else if (resId == 3) {
            // 开门，不能直接起用
            Contact mcontact = FList.getInstance().isContact(
                    String.valueOf(contactId));
            String open_door_order = "IPC1anerfa:unlock";
            if (mcontact != null) {
                P2PHandler.getInstance().sendCustomCmd(mcontact.contactId,
                        mcontact.contactPassword, open_door_order);
                P2PHandler.getInstance().setGPIO1_0(mcontact.contactId,
                        mcontact.contactPassword);
                finish();
            } else {
                isOpendoor = true;
                alarm_go.setText(R.string.unlock);
                mPassword.setHint(R.string.input_lock_password);
                alarm_input.setVisibility(LinearLayout.VISIBLE);
            }
        }
    }

    @Override
    public void onGrabbedStateChange(View v, int handle) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFinishFinalAnimation() {
        // TODO Auto-generated method stub

    }

    TextView tvDetermine, tvCancel;

    void creatDialog() {
        Pwddialog = new Dialog(DoorBellNewActivity.this,
                R.style.CustomnewDialog);
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.dialog_editext_pwd, null, false);
        Pwddialog.setContentView(view);
        tvDetermine = (TextView) view.findViewById(R.id.tv_alalrm_determine);
        tvCancel = (TextView) view.findViewById(R.id.tv_alalrm_cancel);
        mPassword = (EditText) view.findViewById(R.id.et_alarm_password);
        mPassword.requestFocus();
        tvCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dismissDialog();
                finish();
            }
        });
        tvDetermine.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final String password = mPassword.getText().toString();
                if (password.trim().equals("")) {
                    T.showShort(mContext, R.string.input_monitor_pwd);
                    return;
                }

                if (password.length() > 30 || password.charAt(0) == '0') {
                    T.showShort(mContext, R.string.device_password_invalid);
                    return;
                }

                P2PConnect.vReject("");

                new Thread() {
                    @Override
                    public void run() {
                        while (true) {
                            if (P2PConnect.getCurrent_state() == P2PConnect.P2P_STATE_NONE) {
                                Message msg = new Message();
                                String pwd = P2PHandler.getInstance().EntryPassword(password);
                                String[] data = new String[]{contactId,
                                        pwd};
                                msg.obj = data;
                                handler.sendMessage(msg);
                                break;
                            }
                            Utils.sleepThread(500);
                        }
                    }
                }.start();

            }
        });
        Pwddialog.show();

    }

    void dismissDialog() {
        if (null != Pwddialog) {
            Pwddialog.dismiss();
        }
    }

}
