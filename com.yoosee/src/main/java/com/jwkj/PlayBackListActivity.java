package com.jwkj;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yoosee.R;
import com.jwkj.activity.BaseActivity;
import com.jwkj.adapter.DateNumericAdapter;
import com.jwkj.adapter.RecordAdapter;
import com.jwkj.data.Contact;
import com.jwkj.fragment.FaultFragment;
import com.jwkj.fragment.LoadingFragment;
import com.jwkj.fragment.RecordListFragment;
import com.jwkj.global.Constants;
import com.jwkj.utils.T;
import com.jwkj.wheel.widget.OnWheelScrollListener;
import com.jwkj.wheel.widget.WheelView;
import com.jwkj.widget.HeaderView;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PView;

public class PlayBackListActivity extends BaseActivity implements
        OnFocusChangeListener, OnClickListener {
    public static Context mContext;
    WheelView date_year, date_month, date_day, date_hour, date_minute;
    LinearLayout date_pick;
    EditText startTime, endTime;
    Button date_cancel, search_btn;
    ImageView back;
    TextView search_detail, search_one_day, search_three_day, search_one_month;
    ImageView cursor;
    Contact contact;
    boolean isDpShow = false;
    boolean isSearchLayoutShow = false;
    private boolean mIsReadyCall = false;
    boolean receiverIsReg;
    int selected_Date;
    public static final int START_TIME = 0;
    public static final int END_TIME = 1;
    private int selected_condition = 0;// 0,1,2,3
    private int currIndex = 0;
    private int cursorWidth;
    private int offset = 0;
    private int position_one;
    private int position_two;
    private int position_three;

    private boolean isWaite_0 = false;
    private boolean isWaite_1 = false;
    private boolean isWaite_2 = false;
    private boolean isWaite_3 = false;

    private int currnetTime = 0;

    private String[] fragments = new String[]{"recordFrag", "loadingFrag",
            "faultFrag"};
    RecordListFragment rlFrag;
    LoadingFragment loadFrag;
    FaultFragment faultFrag;

    private TextView contactName;
    HeaderView header_img;
    RecordAdapter madapter = new RecordAdapter();
    ArrayList<String> list = new ArrayList<String>();
    boolean isLoadingChange = false;
    int waitload = 0;
    public static int currentFile = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback_list);
        mContext = this;
        contact = (Contact) getIntent().getSerializableExtra("contact");
        initComponent();
        regFilter();

        initWidth();
        selected_condition = 0;
        isLoadingChange = true;
        searchByTime(1);
        updateCondition();
        loadFrag = new LoadingFragment();
        this.replaceFrag(loadFrag, fragments[1]);
    }

    public void initComponent() {
        search_btn = (Button) findViewById(R.id.search_btn);
        back = (ImageView) findViewById(R.id.back_btn);
        date_cancel = (Button) findViewById(R.id.date_cancel);
        date_pick = (LinearLayout) findViewById(R.id.date_pick);
        search_detail = (TextView) findViewById(R.id.search_detail);
        search_one_day = (TextView) findViewById(R.id.search_one_day);
        search_three_day = (TextView) findViewById(R.id.search_three_day);
        search_one_month = (TextView) findViewById(R.id.search_one_month);
        cursor = (ImageView) findViewById(R.id.cursor);
        date_pick.getBackground().setAlpha(230);
        startTime = (EditText) findViewById(R.id.start_time);
        endTime = (EditText) findViewById(R.id.end_time);
        contactName = (TextView) findViewById(R.id.contactName);
        header_img = (HeaderView) findViewById(R.id.header_img);
        header_img.updateImage(contact.contactId, false);
        contactName.setText(contact.contactName);

        startTime.setOnFocusChangeListener(this);
        endTime.setOnFocusChangeListener(this);
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        back.setOnClickListener(this);
        date_cancel.setOnClickListener(this);
        search_btn.setOnClickListener(this);
        search_detail.setOnClickListener(this);
        search_one_day.setOnClickListener(this);
        search_three_day.setOnClickListener(this);
        search_one_month.setOnClickListener(this);
        startTime.setInputType(InputType.TYPE_NULL);
        endTime.setInputType(InputType.TYPE_NULL);
        initWheel();
    }

    private void initWidth() {
        cursorWidth = cursor.getLayoutParams().width;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        int margin_left = ((screenW / 4) - cursorWidth) / 2;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) cursor
                .getLayoutParams();
        layoutParams.leftMargin = margin_left;
        cursor.setLayoutParams(layoutParams);

        offset = (int) ((screenW / 4.0 - cursorWidth) / 2);

        position_one = (int) (screenW / 4.0);
        position_two = position_one * 2;
        position_three = position_one * 3;
    }

    public void initWheel() {
        Calendar calendar = Calendar.getInstance();
        // year
        int curYear = calendar.get(Calendar.YEAR);
        date_year = (WheelView) findViewById(R.id.date_year);
        date_year.setViewAdapter(new DateNumericAdapter(mContext, 2010, 2036));
        date_year.setCurrentItem(curYear - 2010);
        date_year.addScrollingListener(scrolledListener);
        date_year.setCyclic(true);

        int curMonth = calendar.get(Calendar.MONTH) + 1;
        date_month = (WheelView) findViewById(R.id.date_month);
        date_month.setViewAdapter(new DateNumericAdapter(mContext, 1, 12));
        date_month.setCurrentItem(curMonth - 1);
        date_month.addScrollingListener(scrolledListener);
        date_month.setCyclic(true);

        int curDay = calendar.get(Calendar.DAY_OF_MONTH);
        date_day = (WheelView) findViewById(R.id.date_day);
        date_day.setViewAdapter(new DateNumericAdapter(mContext, 1, 31));
        date_day.setCurrentItem(curDay - 1);
        date_day.addScrollingListener(scrolledListener);
        date_day.setCyclic(true);

        int curHour = calendar.get(Calendar.HOUR_OF_DAY);
        date_hour = (WheelView) findViewById(R.id.date_hour);
        date_hour.setViewAdapter(new DateNumericAdapter(mContext, 0, 23));
        date_hour.setCurrentItem(curHour);
        date_hour.addScrollingListener(scrolledListener);
        date_hour.setCyclic(true);

        int curMinute = calendar.get(Calendar.MINUTE);
        date_minute = (WheelView) findViewById(R.id.date_minute);
        date_minute.setViewAdapter(new DateNumericAdapter(mContext, 0, 59));
        date_minute.setCurrentItem(curMinute);
        date_minute.addScrollingListener(scrolledListener);
        date_minute.setCyclic(true);

    }

    // Wheel scrolled flag
    private boolean wheelScrolled = false;

    // Wheel scrolled listener
    OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
        public void onScrollingStarted(WheelView wheel) {
            wheelScrolled = true;
            updateStatus();
            updateSearchEdit();
        }

        public void onScrollingFinished(WheelView wheel) {
            wheelScrolled = false;
            updateStatus();
            updateSearchEdit();
        }
    };

    public void updateStatus() {
        int year = date_year.getCurrentItem() + 2010;
        int month = date_month.getCurrentItem() + 1;

        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
                || month == 10 || month == 12) {
            date_day.setViewAdapter(new DateNumericAdapter(mContext, 1, 31));
        } else if (month == 2) {

            boolean isLeapYear = false;
            if (year % 100 == 0) {
                if (year % 400 == 0) {
                    isLeapYear = true;
                } else {
                    isLeapYear = false;
                }
            } else {
                if (year % 4 == 0) {
                    isLeapYear = true;
                } else {
                    isLeapYear = false;
                }
            }
            if (isLeapYear) {
                if (date_day.getCurrentItem() > 28) {
                    date_day.scroll(30, 2000);
                }
                date_day.setViewAdapter(new DateNumericAdapter(mContext, 1, 29));
            } else {
                if (date_day.getCurrentItem() > 27) {
                    date_day.scroll(30, 2000);
                }
                date_day.setViewAdapter(new DateNumericAdapter(mContext, 1, 28));
            }

        } else {
            if (date_day.getCurrentItem() > 29) {
                date_day.scroll(30, 2000);
            }
            date_day.setViewAdapter(new DateNumericAdapter(mContext, 1, 30));
        }

    }

    public void updateSearchEdit() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        int year = date_year.getCurrentItem() + 2010;
        int month = date_month.getCurrentItem() + 1;
        int day = date_day.getCurrentItem() + 1;
        int hour = date_hour.getCurrentItem();
        int minute = date_minute.getCurrentItem();
        StringBuilder sb = new StringBuilder();
        sb.append(year + "-");

        if (month < 10) {
            sb.append("0" + month + "-");
        } else {
            sb.append(month + "-");
        }

        if (day < 10) {
            sb.append("0" + day + " ");
        } else {
            sb.append(day + " ");
        }

        if (hour < 10) {
            sb.append("0" + hour + ":");
        } else {
            sb.append(hour + ":");
        }

        if (minute < 10) {
            sb.append("0" + minute);
        } else {
            sb.append("" + minute);
        }

        if (selected_Date == START_TIME) {
            startTime.setText(sb.toString());
        } else {
            endTime.setText(sb.toString());
        }
    }

    public void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.P2P.ACK_RET_GET_PLAYBACK_FILES);
        filter.addAction(Constants.P2P.RET_GET_PLAYBACK_FILES);
        filter.addAction(Constants.P2P.P2P_ACCEPT);
        filter.addAction(Constants.P2P.P2P_READY);
        filter.addAction(Constants.P2P.P2P_REJECT);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        this.registerReceiver(receiver, filter);
        receiverIsReg = true;
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(Constants.P2P.RET_GET_PLAYBACK_FILES)) {
                String[] names = (String[]) intent
                        .getCharSequenceArrayExtra("recordList");
                if (isRetutn()) {
                    return;
                }
                if (null == rlFrag) {
                    rlFrag = new RecordListFragment();
                    rlFrag.setUser(contact);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("list", list);
                    rlFrag.setArguments(bundle);
                }
                Log.e("waitload", "waitload=" + waitload
                        + "names.lenth-------------------->" + names.length);
                for (String str : names) {
                    if (!list.contains(str)) {
                        list.add(str);
                    }
                }
                if (waitload > 0) {
                    rlFrag.setList(list);
                    isLoadingChange = false;
                    replaceFrag(rlFrag, fragments[0]);
                    waitload--;
                    Log.e("waitload", "loaded" + waitload);
                } else {
                    rlFrag.updateList(list);
                    Intent it = new Intent();
                    it.setAction(Constants.Action.REPEAT_LOADING_DATA);
                    // it.putStringArrayListExtra("adddata", (ArrayList<String>)
                    // list);
                    sendBroadcast(it);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_RET_GET_PLAYBACK_FILES)) {
                if (null == faultFrag) {
                    faultFrag = new FaultFragment();
                }
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                    finish();
                    T.showShort(mContext, R.string.password_error);
                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my", "net error resend:set npc time");
                    faultFrag.setErrorText(getResources().getString(
                            R.string.net_error));
                    waitload = 0;
                    replaceFrag(faultFrag, fragments[2]);
                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_INSUFFICIENT_PERMISSIONS) {
                    finish();
                    T.showShort(mContext, R.string.insufficient_permissions);
                }
            } else if (intent.getAction().equals(Constants.P2P.P2P_ACCEPT)) {
                int[] type = intent.getIntArrayExtra("type");
                P2PView.type = type[0];
                P2PView.scale = type[1];
                P2PHandler.getInstance().openAudioAndStartPlaying(2);
            } else if (intent.getAction().equals(Constants.P2P.P2P_READY)) {
                Intent intentCall = new Intent();
                intentCall.setClass(PlayBackListActivity.this,
                        PlayBackActivity.class);
                intentCall.putExtra("type",
                        Constants.P2P_TYPE.P2P_TYPE_PLAYBACK);
                intentCall.putStringArrayListExtra("playbacklist", list);
                intentCall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentCall.putExtra("currentFile", currentFile);
                startActivity(intentCall);
                rlFrag.closeDialog();
            } else if (intent.getAction().equals(Constants.P2P.P2P_REJECT)) {
                rlFrag.closeDialog();
                P2PHandler.getInstance().reject();
            }
        }

    };

    @Override
    public void onFocusChange(View arg0, boolean arg1) {
        // TODO Auto-generated method stub
        int resId = arg0.getId();
        if (resId == R.id.start_time) {
            selected_Date = START_TIME;
            startTime.setTextColor(Color.BLUE);
            startTime.setHintTextColor(Color.BLUE);
            endTime.setTextColor(Color.BLACK);
            endTime.setHintTextColor(Color.BLACK);
        } else if (resId == R.id.end_time) {
            selected_Date = END_TIME;
            startTime.setTextColor(Color.BLACK);
            startTime.setHintTextColor(Color.BLACK);
            endTime.setTextColor(Color.BLUE);
            endTime.setHintTextColor(Color.BLUE);
        }
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        int resId = arg0.getId();
        if (resId == R.id.back_btn) {
            this.finish();
        } else if (resId == R.id.date_cancel) {
            hideDatePick();
        } else if (resId == R.id.search_btn) {
            currnetTime = 3;
            isLoadingChange = true;
            list.clear();
            waitload++;
            if (startTime.getText().toString().equals("")) {
                T.showShort(mContext, R.string.search_error1);
                return;
            }
            if (endTime.getText().toString().equals("")) {
                T.showShort(mContext, R.string.search_error2);
                return;
            }
            if (loadFrag == null) {
                loadFrag = new LoadingFragment();
            }
            this.replaceFrag(loadFrag, fragments[1]);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                Date start = sdf.parse(startTime.getText().toString());
                Date end = sdf.parse(endTime.getText().toString());
                Log.e("time1", "search1" + start.toString());
                Log.e("time2", "search2" + end.toString());
                if (start.after(end)) {
                    T.showShort(mContext, R.string.search_error3);
                    return;
                }
                madapter.setStartTime(start);
                P2PHandler.getInstance().getRecordFiles(contact.getContactId(),
                        contact.contactPassword, start, end);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            hideDatePick();
        } else if (resId == R.id.search_detail) {
            selected_condition = 3;
            updateCondition();
            startTime.requestFocus();
            if (!isDpShow) {
                showDatePick();
            }
        } else if (resId == R.id.search_one_day) {
            currnetTime = 0;
            isLoadingChange = true;
            list.clear();
            if (loadFrag == null)

            {
                loadFrag = new LoadingFragment();
            }

            this.replaceFrag(loadFrag, fragments[1]);

            selected_condition = 0;

            searchByTime(1);

            updateCondition();

        } else if (resId == R.id.search_three_day) {
            currnetTime = 1;
            isLoadingChange = true;
            list.clear();
            if (loadFrag == null)

            {
                loadFrag = new LoadingFragment();
            }

            this.replaceFrag(loadFrag, fragments[1]);

            selected_condition = 1;

            searchByTime(3);

            updateCondition();

        } else if (resId == R.id.search_one_month) {
            currnetTime = 2;
            isLoadingChange = true;
            list.clear();
            if (loadFrag == null) {
                loadFrag = new LoadingFragment();
            }
            this.replaceFrag(loadFrag, fragments[1]);
            selected_condition = 2;
            searchByTime(31);
            updateCondition();
        }

    }

    @Override
    public void onBackPressed() {
        if (isDpShow) {
            hideDatePick();
        } else {
            super.onBackPressed();
        }

    }

    public void showDatePick() {
        isDpShow = true;
        date_pick.setVisibility(RelativeLayout.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(this,
                R.anim.slide_in_bottom);
        date_pick.startAnimation(anim);
        if (rlFrag != null) {
            rlFrag.scrollOff();
        }
    }

    public void hideDatePick() {
        isDpShow = false;
        Animation anim = AnimationUtils.loadAnimation(this,
                R.anim.slide_out_top);
        date_pick.startAnimation(anim);
        date_pick.setVisibility(RelativeLayout.GONE);
        if (rlFrag != null) {
            rlFrag.scrollOn();
        }

    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if (receiverIsReg) {
            receiverIsReg = false;
            this.unregisterReceiver(receiver);
        }
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        P2PConnect.setPlayBack(true);
        if (!receiverIsReg) {
            regFilter();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        P2PConnect.setPlayBack(false);
    }

    public void searchByTime(int time) {
        waitload++;
        if (contact.contactPassword == null
                || contact.contactPassword.equals("")) {
            finish();
            T.showShort(mContext, R.string.password_error);
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date endDate = new Date(System.currentTimeMillis());
        Date startDate = new Date(System.currentTimeMillis() - time * 24L * 60L
                * 60L * 1000L);
        RecordAdapter.setStartTime(startDate);
        P2PHandler.getInstance().getRecordFiles(contact.getContactId(),
                contact.contactPassword, startDate, endDate);
    }

    public void updateCondition() {
        Animation animation = null;
        Log.e("e", currIndex + ":index");
        switch (selected_condition) {
            case 0:
                search_one_day.setTextColor(Color.BLACK);
                search_three_day
                        .setTextColor(getResources().getColor(R.color.gray));
                search_one_month
                        .setTextColor(getResources().getColor(R.color.gray));
                search_detail.setTextColor(getResources().getColor(R.color.gray));
                date_pick.setVisibility(RelativeLayout.GONE);
                if (currIndex == 1) {
                    animation = new TranslateAnimation(position_one, 0, 0, 0);
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(position_two, 0, 0, 0);
                } else if (currIndex == 3) {
                    animation = new TranslateAnimation(position_three, 0, 0, 0);
                }
                currIndex = 0;
                break;
            case 1:
                search_three_day.setTextColor(Color.BLACK);
                search_one_day.setTextColor(getResources().getColor(R.color.gray));
                search_one_month
                        .setTextColor(getResources().getColor(R.color.gray));
                search_detail.setTextColor(getResources().getColor(R.color.gray));
                date_pick.setVisibility(RelativeLayout.GONE);
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, position_one, 0, 0);
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(position_two, position_one,
                            0, 0);
                } else if (currIndex == 3) {
                    animation = new TranslateAnimation(position_three,
                            position_one, 0, 0);
                }
                currIndex = 1;
                break;
            case 2:
                search_one_month.setTextColor(Color.BLACK);
                search_three_day
                        .setTextColor(getResources().getColor(R.color.gray));
                search_one_day.setTextColor(getResources().getColor(R.color.gray));
                search_detail.setTextColor(getResources().getColor(R.color.gray));
                date_pick.setVisibility(RelativeLayout.GONE);
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, position_two, 0, 0);
                } else if (currIndex == 1) {
                    animation = new TranslateAnimation(position_one, position_two,
                            0, 0);
                } else if (currIndex == 3) {
                    animation = new TranslateAnimation(position_three,
                            position_two, 0, 0);
                }
                currIndex = 2;
                break;
            case 3:
                search_detail.setTextColor(Color.BLACK);
                search_three_day
                        .setTextColor(getResources().getColor(R.color.gray));
                search_one_month
                        .setTextColor(getResources().getColor(R.color.gray));
                search_one_day.setTextColor(getResources().getColor(R.color.gray));
                date_pick.setVisibility(RelativeLayout.VISIBLE);
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, position_three, 0, 0);
                } else if (currIndex == 1) {
                    animation = new TranslateAnimation(position_one,
                            position_three, 0, 0);
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(position_two,
                            position_three, 0, 0);
                }
                currIndex = 3;
                break;
        }
        if (animation != null) {
            animation.setFillAfter(true);
            animation.setDuration(300);
            cursor.startAnimation(animation);
        }
    }

    /**
     * 设置当前选项卡是否在等待数据
     *
     * @param isWaite 状态
     */
    public void setIsWaiteList(boolean isWaite) {
        setiswaite(currnetTime, isWaite);
    }

    /**
     * 是否过滤这次列表，仿照IOS解决列表之间串数据的问题，逻辑可做优化
     *
     * @return 是否过滤
     */
    private boolean isRetutn() {
        if (currnetTime == 0) {
            if (isWaite_1) {
                isWaite_1 = false;
                return true;
            }
            if (isWaite_2) {
                isWaite_2 = false;
                return true;
            }
            if (isWaite_3) {
                isWaite_3 = false;
                return true;
            }
            isWaite_0 = false;
        } else if (currnetTime == 1) {
            if (isWaite_0) {
                isWaite_0 = false;
                return true;
            }
            if (isWaite_2) {
                isWaite_2 = false;
                return true;
            }
            if (isWaite_3) {
                isWaite_3 = false;
                return true;
            }
            isWaite_1 = false;
        } else if (currnetTime == 2) {
            if (isWaite_0) {
                isWaite_0 = false;
                return true;
            }
            if (isWaite_1) {
                isWaite_1 = false;
                return true;
            }
            if (isWaite_3) {
                isWaite_3 = false;
                return true;
            }
            isWaite_2 = false;
        } else {
            if (isWaite_0) {
                isWaite_0 = false;
                return true;
            }
            if (isWaite_2) {
                isWaite_2 = false;
                return true;
            }
            if (isWaite_1) {
                isWaite_1 = false;
                return true;
            }
            isWaite_3 = false;
        }
        return false;
    }

    public void setiswaite(int currentTime, boolean isWaite) {
        switch (currentTime) {
            case 0:
                isWaite_0 = isWaite;
                break;
            case 1:
                isWaite_1 = isWaite;
                break;
            case 2:
                isWaite_2 = isWaite;
                break;
            case 3:
                isWaite_3 = isWaite;
                break;
        }
    }

    // 切换显示
    public void replaceFrag(Fragment fragment, String mark) {
        try {
            FragmentManager manager = getSupportFragmentManager();

            FragmentTransaction transaction = manager.beginTransaction();
            transaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            transaction.replace(R.id.record_container, fragment, mark);
            transaction.commit();
            manager.executePendingTransactions();
        } catch (Exception e) {
            Log.e("my", "replaceFrag error");
        }
    }

    @Override
    public int getActivityInfo() {
        // TODO Auto-generated method stub
        return Constants.ActivityInfo.ACTIVITY_PLAYBACKLISTACTIVITY;
    }
}
