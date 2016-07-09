package com.jwkj.fragment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import javax.security.auth.callback.Callback;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.jwkj.CallActivity;
import com.jwkj.PlayBackListActivity;
import com.yoosee.R;
import com.jwkj.activity.AddApDeviceActivity;
import com.jwkj.activity.AddContactActivity;
import com.jwkj.activity.ApMonitorActivity;
import com.jwkj.activity.DeviceUpdateActivity;
import com.jwkj.activity.DoorBellActivity;
import com.jwkj.activity.DoorBellNewActivity;
import com.jwkj.activity.LocalDeviceListActivity;
import com.jwkj.activity.LogoActivity;
import com.jwkj.activity.MainActivity;
import com.jwkj.activity.MainControlActivity;
import com.jwkj.activity.ModifyContactActivity;
import com.jwkj.activity.RadarAddActivity;
import com.jwkj.activity.RadarAddFirstActivity;
import com.jwkj.adapter.MainAdapter;
import com.jwkj.adapter.MainAdapter.onConnectListner;
import com.jwkj.data.Contact;
import com.jwkj.data.DataManager;
import com.jwkj.data.SharedPreferencesManager;
import com.jwkj.entity.LocalDevice;
import com.jwkj.global.AppConfig;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.global.MyApp;
import com.jwkj.global.NpcCommon;
import com.jwkj.thread.MainThread;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.utils.WifiUtils;
import com.jwkj.widget.HeaderTextView;
import com.jwkj.widget.NormalDialog;
import com.jwkj.widget.NormalDialog.OnNormalDialogTimeOutListner;
import com.lib.pullToRefresh.PullToRefreshBase;
import com.lib.pullToRefresh.PullToRefreshBase.OnRefreshListener;
import com.lib.pullToRefresh.PullToRefreshListView;
import com.lib.quick_action_bar.QuickAction;
import com.lib.quick_action_bar.QuickActionBar;
import com.lib.quick_action_bar.QuickActionWidget;
import com.lib.quick_action_bar.QuickActionWidget.OnQuickActionClickListener;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;

public class ContactFrag extends BaseFragment implements OnClickListener {

    public static final int CHANGE_REFRESHING_LABLE = 0x12;
    private Context mContext;
    private boolean isRegFilter = false;
    private boolean isDoorBellRegFilter = false;

    private ListView mListView;
    private ImageView mAddUser;
    private MainAdapter mAdapter;
    private PullToRefreshListView mPullRefreshListView;
    boolean refreshEnd = false;
    boolean isFirstRefresh = true;
    boolean isActive;
    boolean isCancelLoading;
    private QuickActionWidget mBar;
    private LinearLayout net_work_status_bar;
    private RelativeLayout local_device_bar_top;
    private TextView text_local_device_count;
    NormalDialog dialog;
    Handler handler;
    private Contact next_contact;
    private LinearLayout layout_add;
    private RelativeLayout layout_contact;
    private RelativeLayout radar_add, manually_add;
    boolean isOpenThread = false;
    Handler myHandler = new Handler();
    //RelativeLayout layout_no_device;
    int count1 = 0;
    int count2 = 0;

    public static boolean isHideAdd = true;
    Animation animation_out, animation_in;
    NormalDialog dialog_loading;
    NormalDialog dialog_update;
    NormalDialog dialog_connect;
    HeaderTextView v;
    String wifiName = "";
    private Contact apContact;
    private Contact nvrContact;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_contact, container,
                false);
        mContext = MainActivity.mContext;
        v = new HeaderTextView(mContext,
                Utils.getStringByResouceID(R.string.tv_add_device1),
                Utils.getStringByResouceID(R.string.tv_add_device2));
        Log.e("my", "createContactFrag");
        initComponent(view);
        regFilter();
        if (isFirstRefresh) {
            isFirstRefresh = !isFirstRefresh;
            FList flist = FList.getInstance();
            flist.updateOnlineState();
            flist.searchLocalDevice();
        }

        return view;
    }

    public void initComponent(View view) {
        mAddUser = (ImageView) view.findViewById(R.id.button_add);
        net_work_status_bar = (LinearLayout) view
                .findViewById(R.id.net_status_bar_top);
        local_device_bar_top = (RelativeLayout) view
                .findViewById(R.id.local_device_bar_top);
        text_local_device_count = (TextView) view
                .findViewById(R.id.text_local_device_count);
        mPullRefreshListView = (PullToRefreshListView) view
                .findViewById(R.id.pull_refresh_list);
        layout_add = (LinearLayout) view.findViewById(R.id.layout_add);
        layout_contact = (RelativeLayout) view
                .findViewById(R.id.layout_contact);
        radar_add = (RelativeLayout) view.findViewById(R.id.radar_add);
        manually_add = (RelativeLayout) view.findViewById(R.id.manually_add);
//		 layout_no_device = (RelativeLayout) view
//		 .findViewById(R.id.layout_no_device);
        radar_add.setOnClickListener(this);
        manually_add.setOnClickListener(this);
        layout_contact.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                if (isHideAdd == false) {
                    hideAdd();
                }
                return false;
            }
        });
        local_device_bar_top.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent i = new Intent(mContext, LocalDeviceListActivity.class);
                mContext.startActivity(i);
            }

        });
        mPullRefreshListView
                .setOnRefreshListener(new OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        String label = DateUtils.formatDateTime(mContext,
                                System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME
                                        | DateUtils.FORMAT_SHOW_DATE
                                        | DateUtils.FORMAT_ABBREV_ALL);

                        // Update the LastUpdatedLabel
                        refreshView.getLoadingLayoutProxy()
                                .setLastUpdatedLabel(label);

                        // Do work to refresh the list here.
                        new GetDataTask().execute();
                    }
                });
        mPullRefreshListView.setShowIndicator(false);
        mListView = mPullRefreshListView.getRefreshableView();
        mAdapter = new MainAdapter(mContext, this);
        mAdapter.setOnSrttingListner(new onConnectListner() {
            @Override
            public void onNvrClick(Contact contact) {
                // TODO Auto-generated method stub
                nvrContact = contact;
                Log.e("leleTest", "onNvrClick=" + nvrContact.contactType);
                P2PHandler.getInstance().getNvrIpcList(contact.contactId, contact.contactPassword);
                dialog_loading = new NormalDialog(mContext);
                dialog_loading.setTitle(getResources().getString(R.string.verification));
                dialog_loading.setStyle(NormalDialog.DIALOG_STYLE_LOADING);
                dialog_loading.showDialog();
            }
        });
        upadataTextView();
        mListView.setAdapter(mAdapter);
        mAddUser.setOnClickListener(this);
        mListView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                if (isHideAdd == false) {
                    hideAdd();
                }
                return false;
            }
        });
        List<LocalDevice> localDevices = FList.getInstance().getLocalDevicesNoAP();
        if (localDevices.size() > 0) {
            local_device_bar_top.setVisibility(RelativeLayout.VISIBLE);
            text_local_device_count.setText("" + localDevices.size());
        } else {
            local_device_bar_top.setVisibility(RelativeLayout.GONE);
        }
        List<Contact> contacts = DataManager.findContactByActiveUser(mContext,
                NpcCommon.mThreeNum);
        animation_out = AnimationUtils.loadAnimation(mContext,
                R.anim.scale_amplify);
        animation_in = AnimationUtils.loadAnimation(mContext,
                R.anim.scale_narrow);
    }

    public void upadataTextView() {
        // 添加空列表提示
        if (mAdapter.isEmpty()) {
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                    LayoutParams.MATCH_PARENT, 350);
            v.setLayoutParams(params);
            if (mListView.getHeaderViewsCount() <= 1) {
                mListView.addHeaderView(v, null, false);
            }
        } else {
            mListView.removeHeaderView(v);
        }
//		if(mListView.getHeaderViewsCount()<2){
//			addListHeader();
//		}
    }

    private void addListHeader() {
        // 添加头部
        HeaderTextView header = new HeaderTextView(mContext, "", "");
        AbsListView.LayoutParams headerParams = new AbsListView.LayoutParams(
                LayoutParams.MATCH_PARENT, R.dimen.contact_item_margin);
        header.setLayoutParams(headerParams);
        mListView.addHeaderView(header, null, false);
    }

    public void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.Action.REFRESH_CONTANTS);
        filter.addAction(Constants.Action.GET_FRIENDS_STATE);
        filter.addAction(Constants.Action.LOCAL_DEVICE_SEARCH_END);
        filter.addAction(Constants.Action.ACTION_NETWORK_CHANGE);
        filter.addAction(Constants.P2P.ACK_RET_CHECK_PASSWORD);
        filter.addAction(Constants.P2P.RET_GET_REMOTE_DEFENCE);
        filter.addAction(Constants.Action.SETTING_WIFI_SUCCESS);
        filter.addAction(Constants.Action.DIAPPEAR_ADD);
        filter.addAction(Constants.Action.ADD_CONTACT_SUCCESS);
        filter.addAction(Constants.Action.DELETE_DEVICE_ALL);
        // 接收报警ID----------
        filter.addAction(Constants.P2P.RET_GET_BIND_ALARM_ID);
        filter.addAction(Constants.P2P.RET_SET_BIND_ALARM_ID);
        filter.addAction(Constants.P2P.ACK_RET_SET_BIND_ALARM_ID);
        filter.addAction(Constants.P2P.ACK_RET_GET_BIND_ALARM_ID);
        filter.addAction(Constants.Action.SEARCH_AP_DEVICE);
        filter.addAction(Constants.Action.ENTER_DEVICE_SETTING);
        filter.addAction(Constants.Action.CALL_DEVICE);
        filter.addAction(Constants.Action.NET_WORK_TYPE_CHANGE);
        filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        filter.addAction(Constants.P2P.ACK_GET_NVR_IPC_LIST);
        filter.addAction(Constants.P2P.RET_GET_NVR_IPC_LIST);
        filter.addAction(Constants.P2P.RET_SET_REMOTE_DEFENCE);
        mContext.registerReceiver(mReceiver, filter);
        isRegFilter = true;
    }

    public void regDoorbellFilter() {
        IntentFilter filter = new IntentFilter();
        // 接收报警ID----------
        filter.addAction(Constants.P2P.RET_GET_BIND_ALARM_ID);
        filter.addAction(Constants.P2P.RET_SET_BIND_ALARM_ID);
        filter.addAction(Constants.P2P.ACK_RET_SET_BIND_ALARM_ID);
        filter.addAction(Constants.P2P.ACK_RET_GET_BIND_ALARM_ID);
        // 接收报警ID---------------
        mContext.registerReceiver(mDoorbellReceiver, filter);
        isDoorBellRegFilter = true;
    }

    BroadcastReceiver mDoorbellReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.P2P.RET_GET_BIND_ALARM_ID)) {
                String[] data = intent.getStringArrayExtra("data");
                String srcID = intent.getStringExtra("srcID");
                int max_count = intent.getIntExtra("max_count", 0);
                if (data.length >= max_count) {
                    if (!SharedPreferencesManager.getInstance()
                            .getIsDoorBellToast(mContext, srcID)) {
                        T.show(mContext, R.string.alarm_push_limit, 2000);
                        SharedPreferencesManager.getInstance()
                                .putIsDoorBellToast(srcID, true, mContext);
                    }
                } else {
                    // 处理绑定推送ID
                    mAdapter.setBindAlarmId(srcID, data);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_SET_BIND_ALARM_ID)) {
                int result = intent.getIntExtra("result", -1);
                String srcID = intent.getStringExtra("srcID");
                if (result == Constants.P2P_SET.BIND_ALARM_ID_SET.SETTING_SUCCESS) {
                    // 设置成功重新获取列表
                    // mAdapter.getBindAlarmId(srcID);
                    mAdapter.setBindAlarmIdSuccess(srcID);
                } else {

                }
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_RET_SET_BIND_ALARM_ID)) {
                int result = intent.getIntExtra("result", -1);
                String srcID = intent.getStringExtra("srcID");
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {

                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my", "net error resend:set alarm bind id");
                    // 设置时网络错误，重新设置
                    mAdapter.setBindAlarmId(srcID);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_RET_GET_BIND_ALARM_ID)) {
                int result = intent.getIntExtra("result", -1);
                String srcID = intent.getStringExtra("srcID");
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {

                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my", "net error resend:get alarm bind id");
                    // 获得列表网络错误，重新获取
                    mAdapter.getBindAlarmId(srcID);
                }
            }
        }
    };

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.Action.REFRESH_CONTANTS)) {
                FList flist = FList.getInstance();
                flist.updateOnlineState();
                mAdapter.notifyDataSetChanged();
                upadataTextView();
                List<LocalDevice> localDevices = FList.getInstance()
                        .getLocalDevicesNoAP();
                if (localDevices.size() > 0) {
                    local_device_bar_top.setVisibility(RelativeLayout.VISIBLE);
                    text_local_device_count.setText("" + localDevices.size());
                } else {
                    local_device_bar_top.setVisibility(RelativeLayout.GONE);
                }
            } else if (intent.getAction().equals(
                    Constants.Action.GET_FRIENDS_STATE)) {
                mAdapter.notifyDataSetChanged();
                refreshEnd = true;
            } else if (intent.getAction().equals(
                    Constants.Action.LOCAL_DEVICE_SEARCH_END)) {
                List<LocalDevice> localDevices = FList.getInstance()
                        .getLocalDevicesNoAP();
                if (localDevices.size() > 0) {
                    local_device_bar_top.setVisibility(RelativeLayout.VISIBLE);
                    text_local_device_count.setText("" + localDevices.size());
                } else {
                    local_device_bar_top.setVisibility(RelativeLayout.GONE);
                }
                mAdapter.notifyDataSetChanged();
                upadataTextView();
                Log.e("my", "" + localDevices.size());
            } else if (intent.getAction().equals(
                    Constants.Action.ACTION_NETWORK_CHANGE)) {
                ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetInfo = connectivityManager
                        .getActiveNetworkInfo();
                if (activeNetInfo != null) {
                    if (!activeNetInfo.isConnected()) {
                        T.showShort(mContext, getString(R.string.network_error)
                                + " " + activeNetInfo.getTypeName());
                        net_work_status_bar
                                .setVisibility(RelativeLayout.VISIBLE);
                    } else {
                        net_work_status_bar.setVisibility(RelativeLayout.GONE);
                    }
                } else {
                    T.showShort(mContext, R.string.network_error);
                    net_work_status_bar.setVisibility(RelativeLayout.VISIBLE);
                }

            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_RET_CHECK_PASSWORD)) {
                if (!isActive) {
                    return;
                }
                int result = intent.getIntExtra("result", -1);
                if (!isCancelLoading) {
                    if (result == Constants.P2P_SET.ACK_RESULT.ACK_SUCCESS) {
                        if (null != dialog && dialog.isShowing()) {
                            dialog.dismiss();
                            dialog = null;
                        }
                        Intent control = new Intent();
                        control.setClass(mContext, MainControlActivity.class);
                        control.putExtra("contact", next_contact);
                        control.putExtra("type", P2PValue.DeviceType.NPC);
                        mContext.startActivity(control);
                    } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                        if (null != dialog && dialog.isShowing()) {
                            dialog.dismiss();
                            dialog = null;
                        }
                        T.showShort(mContext, R.string.password_error);
                    } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                        if (next_contact != null) {
                            P2PHandler.getInstance().checkPassword(
                                    next_contact.contactId,
                                    next_contact.contactPassword);
                        }
                    } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_INSUFFICIENT_PERMISSIONS) {
                        if (null != dialog && dialog.isShowing()) {
                            dialog.dismiss();
                            dialog = null;
                        }
                        T.showShort(mContext, R.string.insufficient_permissions);
                    }
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_GET_REMOTE_DEFENCE)) {
                int state = intent.getIntExtra("state", -1);
                String contactId = intent.getStringExtra("contactId");
                if (!contactId.equals("1")) {
                    Contact contact = FList.getInstance().isContact(contactId);
                    if (state == Constants.DefenceState.DEFENCE_STATE_WARNING_NET) {
                        if (null != contact && contact.isClickGetDefenceState) {
                            T.showShort(mContext, R.string.net_error);
                        }
                    } else if (state == Constants.DefenceState.DEFENCE_STATE_WARNING_PWD) {
                        if (null != contact && contact.isClickGetDefenceState) {
                            T.showShort(mContext, R.string.password_error);
                        }
                    }

                    if (null != contact && contact.isClickGetDefenceState) {
                        FList.getInstance().setIsClickGetDefenceState(contactId,
                                false);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            } else if (intent.getAction().equals(
                    Constants.Action.SETTING_WIFI_SUCCESS)) {
                FList flist = FList.getInstance();
                flist.updateOnlineState();
                flist.searchLocalDevice();
            } else if (intent.getAction().equals(Constants.Action.DIAPPEAR_ADD)) {
                if (isHideAdd == false) {
                    hideAdd();
                }
            } else if (intent.getAction().equals(
                    Constants.Action.ADD_CONTACT_SUCCESS)) {
                List<LocalDevice> localDevices = FList.getInstance()
                        .getLocalDevicesNoAP();
                if (localDevices.size() > 0) {
                    local_device_bar_top.setVisibility(RelativeLayout.VISIBLE);
                    text_local_device_count.setText("" + localDevices.size());
                } else {
                    local_device_bar_top.setVisibility(RelativeLayout.GONE);
                }
//				layout_no_device.setVisibility(RelativeLayout.GONE);
                upadataTextView();
                mPullRefreshListView
                        .setVisibility(PullToRefreshListView.VISIBLE);
            } else if (intent.getAction().equals(
                    Constants.Action.DELETE_DEVICE_ALL)) {
//				layout_no_device.setVisibility(RelativeLayout.VISIBLE);
                mPullRefreshListView.setVisibility(PullToRefreshListView.VISIBLE);
                upadataTextView();
            } else if (intent.getAction().equals(
                    Constants.Action.ENTER_DEVICE_SETTING)) {
                Contact contact = (Contact) intent
                        .getSerializableExtra("contact");
                next_contact = contact;
                dialog = new NormalDialog(mContext);
                dialog.setOnCancelListener(new OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface arg0) {
                        // TODO Auto-generated method stub
                        isCancelLoading = true;
                    }

                });
                dialog.showLoadingDialog2();
                dialog.setCanceledOnTouchOutside(false);
                isCancelLoading = false;
                P2PHandler.getInstance().checkPassword(contact.contactId,
                        contact.contactPassword);
                myHandler.postDelayed(runnable, 20000);
                count1++;
            } else if (intent.getAction().equals(Constants.Action.CALL_DEVICE)) {
                Contact contact = (Contact) intent
                        .getSerializableExtra("contact");
                Intent call = new Intent();
                call.setClass(mContext, CallActivity.class);
                call.putExtra("callId", contact.contactId);
                call.putExtra("contactName", contact.contactName);
                call.putExtra("contact", contact);
                call.putExtra("isOutCall", true);
                call.putExtra("type", Constants.P2P_TYPE.P2P_TYPE_CALL);
                startActivity(call);
            } else if (intent.getAction().equals(Constants.Action.NET_WORK_TYPE_CHANGE)) {
                Log.e("connect_failed", "NET_WORK_TYPE_CHANGE");
                String connect_name = WifiUtils.getInstance().getConnectWifiName();
                boolean isApDevice = WifiUtils.getInstance().isApDevice(connect_name);
                if (isApDevice) {
                    String deviceId = connect_name.substring(AppConfig.Relese.APTAG.length());
                    Contact contact = FList.getInstance().isContact(deviceId);
                    if (contact != null) {
                        contact.apModeState = Constants.APmodeState.LINK;
                        Intent it = new Intent();
                        it.setAction(Constants.Action.REFRESH_CONTANTS);
                        context.sendBroadcast(it);
                    }
                }
                if (dialog_connect != null && dialog_connect.isShowing()) {
                    dialog_connect.dismiss();
                } else {
                    return;
                }
                if (WifiUtils.getInstance().isConnectWifi(
                        apContact.getAPName())) {
                    Intent apMonitor = new Intent(mContext, ApMonitorActivity.class);
                    try {
                        apContact.ipadressAddress = InetAddress.getByName("192.168.1.1");
                    } catch (UnknownHostException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    apMonitor.putExtra("contact", apContact);
                    apMonitor.putExtra("connectType", Constants.ConnectType.RTSPCONNECT);
                    startActivity(apMonitor);
                } else {
                    reConnectApModeWifi();
                }
            } else if (intent.getAction().equals(Constants.P2P.ACK_GET_NVR_IPC_LIST)) {
                int state = intent.getIntExtra("state", -1);
                Log.e("nvr_list", "state=" + state);
                if (state == Constants.P2P_SET.ACK_RESULT.ACK_SUCCESS) {

                } else if (state == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                    if (dialog_loading != null && dialog_loading.isShowing()) {
                        dialog_loading.dismiss();
                    }
                    T.showShort(mContext, R.string.pw_incrrect);
                } else if (state == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    if (dialog_loading != null && dialog_loading.isShowing()) {
                        dialog_loading.dismiss();
                    }
                    T.showShort(mContext, R.string.net_error);
                }

            } else if (intent.getAction().equals(Constants.P2P.RET_GET_NVR_IPC_LIST)) {
                String contactId = intent.getStringExtra("contactId");
                String[] data = intent.getStringArrayExtra("data");
                int number = intent.getIntExtra("number", -1);
                String s = "";
                if (dialog_loading != null || dialog_loading.isShowing()) {
                    dialog_loading.dismiss();
                }
                for (String d : data) {
                    s = s + d + " ";
                }
                if (number > 0) {
                    Intent monitor = new Intent(mContext, ApMonitorActivity.class);
                    monitor.putExtra("contact", nvrContact);
                    Log.e("leleTest", "onNvrClick=" + nvrContact.contactType);
                    monitor.putExtra("ipcList", data);
                    monitor.putExtra("number", number);
                    monitor.putExtra("connectType", Constants.ConnectType.P2PCONNECT);
                    mContext.startActivity(monitor);
                } else {
                    T.showShort(mContext, R.string.no_ipc_list);
                }
                Log.e("leleTest", "contactId=" + contactId + "--" + "data=" + s + "--" + "number=" + number);
            } else if (intent.getAction().equals(Constants.P2P.RET_SET_REMOTE_DEFENCE)) {
                String contactId = intent.getStringExtra("contactId");
                int state = intent.getIntExtra("state", -1);
                if (state == 0) {
                    if (contactId.equals("1")) {
                        P2PHandler.getInstance().getDefenceStates(contactId, "0");
                    }
                }
            }
        }
    };

    private Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case CHANGE_REFRESHING_LABLE:
                    String lable = (String) msg.obj;
                    // mPullRefreshListView.setHeadLable(lable);
                    break;
            }
            return false;
        }
    });

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            Log.e("my", "doInBackground");
            FList flist = FList.getInstance();
            flist.searchLocalDevice();
            if (flist.size() == 0) {
                return null;
            }
            refreshEnd = false;
            // if(isFirstRefresh){
            // Log.e("my","doInBackground2");
            // Utils.sleepThread(2000);
            // Log.e("my","doInBackground3");
            // flist.updateOnlineState();
            // isFirstRefresh = false;
            // }else{
            // flist.updateOnlineState();
            // }
            flist.updateOnlineState();
            flist.getDefenceState();
            flist.getCheckUpdate();
            while (!refreshEnd) {
                Utils.sleepThread(1000);
            }

            Message msg = new Message();
            msg.what = CHANGE_REFRESHING_LABLE;
            msg.obj = mContext.getResources().getString(
                    R.string.pull_to_refresh_refreshing_success_label);
            mHandler.sendMessage(msg);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            // mListItems.addFirst("Added after refresh...");
            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshListView.onRefreshComplete();
            super.onPostExecute(result);
        }
    }

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (resId == R.id.button_add) {
            // Intent it=new Intent();//门铃测试
            // it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // it.setClass(mContext,DoorBellNewActivity.class);
            // it.putExtra("contactId", String.valueOf(1092914));
            // mContext.startActivity(it);

            if (isHideAdd == true) {
                showAdd();
            } else {
                hideAdd();
            }
            mPullRefreshListView.setFocusable(false);
            mListView.setFocusable(false);
        } else if (resId == R.id.radar_add) {
            layout_add.setVisibility(LinearLayout.GONE);
            local_device_bar_top.setClickable(true);
            isHideAdd = true;
            Intent radar_add = new Intent(mContext, RadarAddFirstActivity.class);
            mContext.startActivity(radar_add);
        } else if (resId == R.id.manually_add) {
            layout_add.setVisibility(LinearLayout.GONE);
            local_device_bar_top.setClickable(true);
            isHideAdd = true;
            Intent add_contact = new Intent(mContext, AddContactActivity.class);
            mContext.startActivity(add_contact);
        }
    }

    public void showQuickActionBar(View view, final Contact contact) {
        if (contact.contactId != null && !contact.contactId.equals("")) {
            String type = contact.contactId.substring(0, 1);
            if (contact.contactType == P2PValue.DeviceType.PHONE) {
                showQuickActionBar_phone(view.findViewById(R.id.user_icon),
                        contact);
            } else if (contact.contactType == P2PValue.DeviceType.NPC) {
                showQuickActionBar_npc(view.findViewById(R.id.user_icon),
                        contact);
            } else if (contact.contactType == P2PValue.DeviceType.IPC) {
                showQuickActionBar_ipc(view.findViewById(R.id.user_icon),
                        contact);
            } else if (contact.contactType == P2PValue.DeviceType.DOORBELL) {
                showQuickActionBar_doorBell(view.findViewById(R.id.user_icon),
                        contact);
            } else {
                if (Integer.parseInt(contact.contactId) < 256) {
                    showQuickActionBar_ipc(view.findViewById(R.id.user_icon),
                            contact);
                } else {
                    showQuickActionBar_unknwon(
                            view.findViewById(R.id.user_icon), contact);
                }
            }
        }
    }

    private void showQuickActionBar_phone(View view, final Contact contact) {
        mBar = new QuickActionBar(getActivity());
        // mBar.addQuickAction(new QuickAction(getActivity(),
        // R.drawable.ic_action_call_pressed, R.string.chat));
        mBar.addQuickAction(new QuickAction(getActivity(),
                R.drawable.ic_action_message_pressed, R.string.message));
        mBar.addQuickAction(new QuickAction(getActivity(),
                R.drawable.ic_action_modify_pressed, R.string.edit));
        mBar.setOnQuickActionClickListener(new OnQuickActionClickListener() {

            @Override
            public void onQuickActionClicked(QuickActionWidget widget,
                                             int position) {
                // TODO Auto-generated method stub
                switch (position) {
                    // case 0:
                    // if(contact.contactId==null||contact.contactId.equals("")){
                    // T.showShort(mContext,R.string.username_error);
                    // return;
                    // }
                    //
                    // Intent call = new Intent();
                    // call.setClass(mContext, CallActivity.class);
                    // call.putExtra("callId", contact.contactId);
                    // call.putExtra("isOutCall", true);
                    // call.putExtra("type", Constants.P2P_TYPE.P2P_TYPE_CALL);
                    // startActivity(call);
                    //
                    // break;
                    case 0:
                        break;
                    case 1:
                        Intent modify = new Intent();
                        modify.setClass(mContext, ModifyContactActivity.class);
                        modify.putExtra("contact", contact);
                        mContext.startActivity(modify);
                        break;
                    default:
                        break;
                }
            }

        });

        mBar.show(view);
    }

    private void showQuickActionBar_npc(View view, final Contact contact) {
        mBar = new QuickActionBar(getActivity());
        // mBar.addQuickAction(new QuickAction(getActivity(),
        // R.drawable.ic_action_monitor_pressed, R.string.monitor));
        if (NpcCommon.mThreeNum.equals("0517401")) {
            mBar.addQuickAction(new QuickAction(getActivity(),
                    R.drawable.ic_action_playback_pressed, R.string.playback));
            mBar.addQuickAction(new QuickAction(getActivity(),
                    R.drawable.ic_action_control_pressed, R.string.control));
            mBar.addQuickAction(new QuickAction(getActivity(),
                    R.drawable.ic_action_modify_pressed, R.string.edit));
            mBar.setOnQuickActionClickListener(new OnQuickActionClickListener() {

                @Override
                public void onQuickActionClicked(QuickActionWidget widget,
                                                 int position) {
                    // TODO Auto-generated method stub
                    switch (position) {
                        // case 0:
                        // if(contact.contactId==null||contact.contactId.equals("")){
                        // T.showShort(mContext,R.string.username_error);
                        // return;
                        // }
                        // if(contact.contactPassword==null||contact.contactPassword.equals("")){
                        // T.showShort(mContext,R.string.password_error);
                        // return;
                        // }
                        // Intent monitor =new Intent();
                        // monitor.setClass(mContext, CallActivity.class);
                        // monitor.putExtra("callId", contact.contactId);
                        // monitor.putExtra("password", contact.contactPassword);
                        // monitor.putExtra("isOutCall", true);
                        // monitor.putExtra("type",
                        // Constants.P2P_TYPE.P2P_TYPE_MONITOR);
                        // startActivity(monitor);
                        // break;
                        case 0:
                            Intent playback = new Intent();
                            playback.setClass(mContext, PlayBackListActivity.class);
                            playback.putExtra("contact", contact);
                            mContext.startActivity(playback);
                            break;
                        case 1:
                            if (contact.contactId == null
                                    || contact.contactId.equals("")) {
                                T.showShort(mContext, R.string.username_error);
                                return;
                            }
                            if (contact.contactPassword == null
                                    || contact.contactPassword.equals("")) {
                                T.showShort(mContext, R.string.password_error);
                                return;
                            }
                            next_contact = contact;
                            dialog = new NormalDialog(mContext);
                            dialog.setOnCancelListener(new OnCancelListener() {

                                @Override
                                public void onCancel(DialogInterface arg0) {
                                    // TODO Auto-generated method stub
                                    isCancelLoading = true;
                                }

                            });
                            dialog.showLoadingDialog2();
                            dialog.setCanceledOnTouchOutside(false);
                            isCancelLoading = false;

                            P2PHandler.getInstance().checkPassword(
                                    contact.contactId, contact.contactPassword);
                            break;
                        case 2:
                            Intent modify = new Intent();
                            modify.setClass(mContext, ModifyContactActivity.class);
                            modify.putExtra("contact", contact);
                            mContext.startActivity(modify);
                            break;
                        default:
                            break;
                    }
                }

            });
            mBar.show(view);
        } else {
            mBar.addQuickAction(new QuickAction(getActivity(),
                    R.drawable.ic_action_call_pressed, R.string.chat));
            mBar.addQuickAction(new QuickAction(getActivity(),
                    R.drawable.ic_action_playback_pressed, R.string.playback));
            mBar.addQuickAction(new QuickAction(getActivity(),
                    R.drawable.ic_action_control_pressed, R.string.control));
            mBar.addQuickAction(new QuickAction(getActivity(),
                    R.drawable.ic_action_modify_pressed, R.string.edit));
            mBar.setOnQuickActionClickListener(new OnQuickActionClickListener() {

                @Override
                public void onQuickActionClicked(QuickActionWidget widget,
                                                 int position) {
                    // TODO Auto-generated method stub
                    switch (position) {
                        // case 0:
                        // if(contact.contactId==null||contact.contactId.equals("")){
                        // T.showShort(mContext,R.string.username_error);
                        // return;
                        // }
                        // if(contact.contactPassword==null||contact.contactPassword.equals("")){
                        // T.showShort(mContext,R.string.password_error);
                        // return;
                        // }
                        // Intent monitor =new Intent();
                        // monitor.setClass(mContext, CallActivity.class);
                        // monitor.putExtra("callId", contact.contactId);
                        // monitor.putExtra("password", contact.contactPassword);
                        // monitor.putExtra("isOutCall", true);
                        // monitor.putExtra("type",
                        // Constants.P2P_TYPE.P2P_TYPE_MONITOR);
                        // startActivity(monitor);
                        // break;
                        case 0:
                            if (contact.contactId == null
                                    || contact.contactId.equals("")) {
                                T.showShort(mContext, R.string.username_error);
                                return;
                            }
                            Intent call = new Intent();
                            call.setClass(mContext, CallActivity.class);
                            call.putExtra("callId", contact.contactId);
                            call.putExtra("contactName", contact.contactName);
                            call.putExtra("contact", contact);
                            call.putExtra("isOutCall", true);
                            call.putExtra("type", Constants.P2P_TYPE.P2P_TYPE_CALL);
                            startActivity(call);
                            break;
                        case 1:
                            Intent playback = new Intent();
                            playback.setClass(mContext, PlayBackListActivity.class);
                            playback.putExtra("contact", contact);
                            mContext.startActivity(playback);
                            break;
                        case 2:
                            if (contact.contactId == null
                                    || contact.contactId.equals("")) {
                                T.showShort(mContext, R.string.username_error);
                                return;
                            }
                            if (contact.contactPassword == null
                                    || contact.contactPassword.equals("")) {
                                T.showShort(mContext, R.string.password_error);
                                return;
                            }
                            next_contact = contact;
                            dialog = new NormalDialog(mContext);
                            dialog.setOnCancelListener(new OnCancelListener() {

                                @Override
                                public void onCancel(DialogInterface arg0) {
                                    // TODO Auto-generated method stub
                                    isCancelLoading = true;
                                }

                            });
                            dialog.showLoadingDialog2();
                            dialog.setCanceledOnTouchOutside(false);
                            isCancelLoading = false;
                            P2PHandler.getInstance().checkPassword(
                                    contact.contactId, contact.contactPassword);
                            myHandler.postDelayed(runnable, 20000);
                            count1++;
                            break;
                        case 3:
                            Intent modify = new Intent();
                            modify.setClass(mContext, ModifyContactActivity.class);
                            modify.putExtra("contact", contact);
                            mContext.startActivity(modify);
                            break;
                        default:
                            break;
                    }
                }

            });
            mBar.show(view);
        }

    }

    private void showQuickActionBar_ipc(View view, final Contact contact) {
        mBar = new QuickActionBar(getActivity());
        // mBar.addQuickAction(new QuickAction(getActivity(),
        // R.drawable.ic_action_monitor_pressed, R.string.monitor));
        mBar.addQuickAction(new QuickAction(getActivity(),
                R.drawable.ic_action_playback_pressed, R.string.playback));
        mBar.addQuickAction(new QuickAction(getActivity(),
                R.drawable.ic_action_control_pressed, R.string.sets_tab));
        mBar.addQuickAction(new QuickAction(getActivity(),
                R.drawable.ic_action_modify_pressed, R.string.edit));
        mBar.setOnQuickActionClickListener(new OnQuickActionClickListener() {

            @Override
            public void onQuickActionClicked(QuickActionWidget widget,
                                             int position) {
                // TODO Auto-generated method stub
                switch (position) {
                    // case 0:
                    // if(contact.contactId==null||contact.contactId.equals("")){
                    // T.showShort(mContext,R.string.username_error);
                    // return;
                    // }
                    // if(contact.contactPassword==null||contact.contactPassword.equals("")){
                    // T.showShort(mContext,R.string.password_error);
                    // return;
                    // }
                    // Intent monitor =new Intent();
                    // monitor.setClass(mContext, CallActivity.class);
                    // monitor.putExtra("callId", contact.contactId);
                    // monitor.putExtra("password", contact.contactPassword);
                    // monitor.putExtra("isOutCall", true);
                    // monitor.putExtra("type",
                    // Constants.P2P_TYPE.P2P_TYPE_MONITOR);
                    // startActivity(monitor);
                    // break;
                    case 0:
                        Intent playback = new Intent();
                        playback.setClass(mContext, PlayBackListActivity.class);
                        playback.putExtra("contact", contact);
                        mContext.startActivity(playback);
                        break;
                    case 1:
                        if (contact.contactId == null
                                || contact.contactId.equals("")) {
                            T.showShort(mContext, R.string.username_error);
                            return;
                        }
                        if (contact.contactPassword == null
                                || contact.contactPassword.equals("")) {
                            T.showShort(mContext, R.string.password_error);
                            return;
                        }
                        next_contact = contact;
                        dialog = new NormalDialog(mContext);
                        dialog.setOnCancelListener(new OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface arg0) {
                                // TODO Auto-generated method stub
                                isCancelLoading = true;
                            }

                        });
                        dialog.showLoadingDialog2();
                        dialog.setCanceledOnTouchOutside(false);
                        isCancelLoading = false;
                        P2PHandler.getInstance().checkPassword(contact.contactId,
                                contact.contactPassword);
                        myHandler.postDelayed(runnable, 20000);
                        count1++;
                        break;
                    case 2:
                        Intent modify = new Intent();
                        modify.setClass(mContext, ModifyContactActivity.class);
                        modify.putExtra("contact", contact);
                        mContext.startActivity(modify);
                        break;
                    default:
                        break;
                }
            }

        });
        mBar.show(view);
    }

    private void showQuickActionBar_doorBell(View view, final Contact contact) {
        mBar = new QuickActionBar(getActivity());
        // mBar.addQuickAction(new QuickAction(getActivity(),
        // R.drawable.ic_action_monitor_pressed, R.string.monitor));
        mBar.addQuickAction(new QuickAction(getActivity(),
                R.drawable.ic_action_playback_pressed, R.string.playback));
        mBar.addQuickAction(new QuickAction(getActivity(),
                R.drawable.ic_action_control_pressed, R.string.sets_tab));
        mBar.addQuickAction(new QuickAction(getActivity(),
                R.drawable.ic_action_modify_pressed, R.string.edit));
        mBar.setOnQuickActionClickListener(new OnQuickActionClickListener() {

            @Override
            public void onQuickActionClicked(QuickActionWidget widget,
                                             int position) {
                // TODO Auto-generated method stub
                switch (position) {
                    case 0:
                        Intent playback = new Intent();
                        playback.setClass(mContext, PlayBackListActivity.class);
                        playback.putExtra("contact", contact);
                        mContext.startActivity(playback);
                        break;
                    case 1:
                        if (contact.contactId == null
                                || contact.contactId.equals("")) {
                            T.showShort(mContext, R.string.username_error);
                            return;
                        }
                        if (contact.contactPassword == null
                                || contact.contactPassword.equals("")) {
                            T.showShort(mContext, R.string.password_error);
                            return;
                        }
                        next_contact = contact;
                        dialog = new NormalDialog(mContext);
                        dialog.setOnCancelListener(new OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface arg0) {
                                // TODO Auto-generated method stub
                                isCancelLoading = true;
                            }

                        });
                        dialog.showLoadingDialog2();
                        dialog.setCanceledOnTouchOutside(false);
                        isCancelLoading = false;
                        P2PHandler.getInstance().checkPassword(contact.contactId,
                                contact.contactPassword);
                        break;
                    case 2:
                        Intent modify = new Intent();
                        modify.setClass(mContext, ModifyContactActivity.class);
                        modify.putExtra("contact", contact);
                        mContext.startActivity(modify);
                        break;
                    default:
                        break;
                }
            }

        });
        mBar.show(view);
    }

    private void showQuickActionBar_unknwon(View view, final Contact contact) {
        mBar = new QuickActionBar(getActivity());
        mBar.addQuickAction(new QuickAction(getActivity(),
                R.drawable.ic_action_modify_pressed, R.string.edit));

        mBar.setOnQuickActionClickListener(new OnQuickActionClickListener() {

            @Override
            public void onQuickActionClicked(QuickActionWidget widget,
                                             int position) {
                // TODO Auto-generated method stub
                switch (position) {
                    case 0:
                        Intent modify = new Intent();
                        modify.setClass(mContext, ModifyContactActivity.class);
                        modify.putExtra("contact", contact);
                        mContext.startActivity(modify);
                        break;

                    default:
                        break;
                }
            }

        });
        mBar.show(view);
    }

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            count2 = count2 + 1;
            if (count2 == count1) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                        T.showShort(mContext, R.string.time_out);
                    }
                }
            }
        }
    };

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        MainThread.setOpenThread(false);
        super.onPause();
        isActive = false;
        if (isDoorBellRegFilter) {
            mContext.unregisterReceiver(mDoorbellReceiver);
        }

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MainThread.setOpenThread(true);
        regDoorbellFilter();
        isActive = true;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        // MainThread.getInstance().kill();
        super.onDestroy();
        Log.e("my", "onDestroy");
        if (isRegFilter) {
            mContext.unregisterReceiver(mReceiver);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void hideAdd() {
        layout_add.startAnimation(animation_in);
        layout_add.setVisibility(LinearLayout.GONE);
        local_device_bar_top.setClickable(true);
        isHideAdd = true;
    }

    public void showAdd() {
        layout_add.setVisibility(LinearLayout.VISIBLE);
        layout_add.startAnimation(animation_out);
        local_device_bar_top.setClickable(false);
        isHideAdd = false;
    }

    private void connectWifi(String wifiName, String wifiPwd) {
        if (wifiPwd.length() < 8) {
            // 密码必须8位
            T.showShort(mContext, R.string.wifi_pwd_error);
            return;
        }
        WifiUtils.getInstance().connectWifi(wifiName, wifiPwd,
                1);
        if (dialog_connect == null) {
            dialog_connect = new NormalDialog(mContext);
        }
        dialog_connect.setTitle(R.string.wait_connect);
        dialog_connect.showLoadingDialog();
        dialog_connect.setTimeOut(30 * 1000);
        dialog_connect.setOnNormalDialogTimeOutListner(new OnNormalDialogTimeOutListner() {

            @Override
            public void onTimeOut() {
                T.showShort(mContext, R.string.connect_wifi_timeout);
                reConnectApModeWifi();
            }
        });

    }

    public void reConnectApModeWifi() {
        try {
            apContact.ipadressAddress = InetAddress.getByName("192.168.1.1");
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Intent modify = new Intent();
        modify.setClass(mContext, AddApDeviceActivity.class);
        modify.putExtra("isAPModeConnect", 0);
        modify.putExtra("contact", apContact);
        modify.putExtra("ipFlag", "1");
        modify.putExtra("isCreatePassword", false);
        startActivity(modify);
        T.showShort(mContext, R.string.conn_fail);
    }
}