package com.jwkj.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jwkj.data.AlarmMask;
import com.jwkj.data.DataManager;
import com.jwkj.global.Constants;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.T;
import com.yoosee.R;

import java.util.List;

public class AddAlarmMaskIdActivity extends BaseActivity implements
        OnClickListener {
    Context mContext;
    ImageView mBack;
    Button mSave;
    EditText mAlarmId;
    private boolean isRegFilter = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm_mask_id);

        mContext = this;
        initCompent();
        regFilter();
    }

    public void initCompent() {
        mBack = (ImageView) findViewById(R.id.back_btn);
        mSave = (Button) findViewById(R.id.save);
        mAlarmId = (EditText) findViewById(R.id.alarmId);

        mBack.setOnClickListener(this);
        mSave.setOnClickListener(this);
    }

    public void regFilter() {
        IntentFilter filter = new IntentFilter();
        mContext.registerReceiver(mReceiver, filter);
        isRegFilter = true;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
        }
    };

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        int resId = view.getId();
        if (resId == R.id.back_btn) {
            finish();
        } else if (resId == R.id.save) {
            String alarmId = mAlarmId.getText().toString();
            if ("".equals(alarmId.trim())) {
                T.showShort(mContext, R.string.input_alarm_mask_id);
                return;
            }

            if (alarmId.charAt(0) == '0') {
                T.showShort(mContext, R.string.format_error);
                return;
            }

            if (alarmId.length() > 9) {
                T.showShort(mContext, R.string.alarm_mask_id_too_long);
                return;
            }

            List<AlarmMask> list = DataManager.findAlarmMaskByActiveUser(
                    mContext, NpcCommon.mThreeNum);
            for (AlarmMask alarmMask : list) {
                if (alarmId.equals(alarmMask.deviceId)) {
                    T.showShort(mContext,
                            R.string.account_already_exists_in_mask_list);
                    return;
                }
            }

            AlarmMask alarmMask = new AlarmMask();
            alarmMask.deviceId = alarmId;
            alarmMask.activeUser = NpcCommon.mThreeNum;
            DataManager.insertAlarmMask(mContext, alarmMask);

            Intent add_success = new Intent();
            add_success.setAction(Constants.Action.ADD_ALARM_MASK_ID_SUCCESS);
            add_success.putExtra("alarmMask", alarmMask);
            mContext.sendBroadcast(add_success);

            T.showShort(mContext, R.string.add_success);
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isRegFilter) {
            mContext.unregisterReceiver(mReceiver);
            isRegFilter = false;
        }
    }

    @Override
    public int getActivityInfo() {
        // TODO Auto-generated method stub
        return Constants.ActivityInfo.ACTIVITY_ADDALARMMASKIDACTIVITY;
    }
}
