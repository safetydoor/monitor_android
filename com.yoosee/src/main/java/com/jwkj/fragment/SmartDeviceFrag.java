package com.jwkj.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;


import com.jwkj.adapter.SensorRecycleAdapter;
import com.jwkj.adapter.SensorRecycleAdapter.ViewHolder;
import com.jwkj.data.Contact;
import com.jwkj.entity.Sensor;
import com.jwkj.fisheye.FisheyeSetHandler;
import com.jwkj.global.Constants;

import com.jwkj.utils.FishAckUtils;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.widget.EditorAndDeletePop;
import com.jwkj.widget.ImputDialog;
import com.jwkj.widget.NormalDialog;

import com.yoosee.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dxs on 2016/1/12.
 */
public class SmartDeviceFrag extends BaseFragment implements OnClickListener {
    private Context mContext;
    private static final int PopH = 142;
    private int device_type = 0;
    private ListView mListView;
    private ImageView ivAddSensor;
    private boolean isRegFilter = false;
    public Contact contact;
    private SensorRecycleAdapter mAdapter;
    private List<Sensor> sensors = new ArrayList<Sensor>();
    private EditorAndDeletePop pop;
    private int modifySensorPostion = -1;//准备修改的传感器编号
    private String nameTemp = "";//用户输入的名字缓存
    private SensorRecycleAdapter.ViewHolder holder;
    private Sensor sensorSwitch;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_control, container,
                false);
        mContext = getActivity();
        contact = (Contact) getArguments().getSerializable("contact");
        device_type = getArguments().getInt("type", 0);
        initUI(view);
        initData();
        return view;
    }

    private void initUI(View view) {
        mListView = (ListView) view.findViewById(R.id.rlv_sensor);
        ivAddSensor = (ImageView) view.findViewById(R.id.iv_add_sensor);
        mAdapter = new SensorRecycleAdapter(mContext, sensors, contact);
        mListView.setAdapter(mAdapter);
        mAdapter.setOnSensorRecycleAdatperClickListner(listner);
        ivAddSensor.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        regFilter();
    }

    public void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.P2P.ACK_FISHEYE);
        filter.addAction(Constants.P2P.RET_GET_SENSOR_WORKMODE);
        filter.addAction(Constants.P2P.RET_INTO_LEARN_STATE);
        filter.addAction(Constants.P2P.RET_DELETE_ONE_CONTROLER);
        filter.addAction(Constants.P2P.RET_DELETE_ONE_SENSOR);
        filter.addAction(Constants.P2P.RET_CHANGE_CONTROLER_NAME);
        filter.addAction(Constants.P2P.RET_CHANGE_SENSOR_NAME);
        filter.addAction(Constants.P2P.RET_GET_ALL_SPECIAL_ALARM);
        filter.addAction(Constants.P2P.RET_GET_LAMPSTATE);
//        filter.addAction(Constants.P2P.RET_TURN_SENSOR);
        filter.addAction(Constants.P2P.RET_SET_SENSER_WORKMODE);
        mContext.registerReceiver(mReceiver, filter);
        isRegFilter = true;
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int iSrcID = intent.getIntExtra("iSrcID", 0);
            byte boption = intent.getByteExtra("boption", (byte) -1);
            byte[] data = intent.getByteArrayExtra("data");
            if (intent.getAction().equals(Constants.P2P.ACK_FISHEYE)) {
                int arg1 = intent.getIntExtra("arg1", 0);
                int result = intent.getIntExtra("result", 0);
                //ACK回调
//                getAddSensorACK(arg1, result);
            } else if (intent.getAction().equals(Constants.P2P.RET_GET_SENSOR_WORKMODE)) {
                //获取传感器防护计划返回
                if (boption == Constants.FishBoption.MESG_GET_OK) {
                    clearNormalSensor();
                    paserSensorData(data, data[4], data[5], data[6], data[7]);
                    mAdapter.notifySensorData(sensors);
                    getLampState((byte) 1);
                } else {
                    T.showLong(mContext, "获取错误");
                }
            } else if (intent.getAction().equals(Constants.P2P.RET_INTO_LEARN_STATE)) {
                getSensorData(data, boption);
            } else if (intent.getAction().equals(Constants.P2P.RET_DELETE_ONE_CONTROLER)) {
                //遥控删除
                deleteSensorResult(boption, data);
            } else if (intent.getAction().equals(Constants.P2P.RET_DELETE_ONE_SENSOR)) {
                //传感器删除
                deleteSensorResult(boption, data);
            } else if (intent.getAction().equals(Constants.P2P.RET_CHANGE_CONTROLER_NAME)) {
                //修改传感器名字
                modifySensorName(boption);
            } else if (intent.getAction().equals(Constants.P2P.RET_CHANGE_SENSOR_NAME)) {
                //修改遥控名字
                modifySensorName(boption);
            } else if (intent.getAction().equals(Constants.P2P.RET_GET_ALL_SPECIAL_ALARM)) {
                //获取所有的特殊传感器
                if (boption == Constants.FishBoption.MESG_GET_OK) {
                    paserSpecialSensorData(data, data[3]);

                }
                getAllSensorData();
            } else if (intent.getAction().equals(Constants.P2P.RET_GET_LAMPSTATE)) {
                Log.e("dxsTeat", "LAMPSTATE---");
                if (boption == Constants.FishBoption.MESG_GET_OK) {
                    Sensor sensor = getSensorByData(data, 4);
                    if (sensor != null) {
                        sensor.setLampState(data[3]);
                        Sensor sensorsssss = sensors.get(sensors.indexOf(sensor));
                        Log.e("dxsTeat", "sensorsssss-->" + sensorsssss.getLampState());
                        mAdapter.notifySensorData(sensors);
                    } else {
                        Log.e("dxsTest", "sensor为空");
                    }
                }
            } else if (intent.getAction().equals(Constants.P2P.RET_TURN_SENSOR)) {
                //设置开关
                if (boption == Constants.FishBoption.MESG_SET_OK) {
                    setSensorSwitch(sensorSwitch);
                } else if (boption == Constants.FishBoption.MESG_SENSOR_NOT_LEARN_YET) {
                    //无此传感器
                }
            } else if (intent.getAction().equals(Constants.P2P.RET_SET_SENSER_WORKMODE)) {
                if (boption == Constants.FishBoption.MESG_SET_OK) {
//            		  setSensorSwitch(sensorSwitch);
                    mAdapter.notifySensorData(sensors);
                }
            }
        }
    };

    private void initData() {
        //先获取特殊传感器，获取到之后再获取一般传感器
        //getAllSpecialSensorData();
        getAllSensorData();
    }

    /**
     * 获取所有的一般传感器
     */
    private void getAllSensorData() {
        FisheyeSetHandler.getInstance().sGetSenSorWorkMode(contact.contactId, contact.contactPassword);
    }

    /**
     * 获取所有的特殊传感器
     */
    private void getAllSpecialSensorData() {
        FisheyeSetHandler.getInstance().sGetAllSpecialAlarmData(contact.contactId, contact.contactPassword);
    }

    /**
     * 获取所有插座状态
     *
     * @param lamstate 1.查询  2.开  3.关闭
     */
    private void getLampState(byte lamstate) {
        for (Sensor sensor : sensors) {
            if (sensor.getSensorType() == Constants.SensorType.TYPE_JACK) {
                getLampState(lamstate, sensor);
            }
        }
    }

    /**
     * 通过特征码获取列表的传感器
     *
     * @param data   原始数据
     * @param offset 数据偏移值
     * @return 不存在返回null
     */
    private Sensor getSensorByData(byte[] data, int offset) {
        byte[] dataTemp = new byte[4];
        byte[] sensorTemp = new byte[4];
        System.arraycopy(data, offset, dataTemp, 0, dataTemp.length);
        for (Sensor sensor : sensors) {
            byte[] sensorInfo = sensor.getSensorData();
            System.arraycopy(sensorInfo, 0, sensorTemp, 0, sensorTemp.length);
            if (Arrays.equals(sensorTemp, dataTemp)) {
                return sensor;
            }
        }
        return null;
    }

    /**
     * 获取sensor在列表的位子
     *
     * @param sensor
     * @return
     */
    private int getSensorPosition(Sensor sensor) {
        return sensors.indexOf(sensor);
    }

    /**
     * 获取或者设置插座状态
     *
     * @param lamstate 0.查询  2.开  3.关闭
     * @param sensor
     */
    private void getLampState(byte lamstate, Sensor sensor) {
        if (sensor.isControlSensor()) {
            FisheyeSetHandler.getInstance().sGetLampStatu(contact.contactId, contact.contactPassword, lamstate, sensor.getSensorData());
        }
    }

    private SensorRecycleAdapter.onSensorRecycleAdatperClickListner listner = new SensorRecycleAdapter.onSensorRecycleAdatperClickListner() {
        @Override
        public void onItemClick(View contentview, Sensor sensor, int position) {
            //单击进入
            if (sensor.getSensorCategory() == Sensor.SENSORCATEGORY_NORMAL && sensor.getSensorType() == Constants.SensorType.TYPE_REMOTE_CONTROLLER) {
                T.showShort(mContext, sensor.getName());
            } else if (sensor.getSensorType() == Constants.SensorType.TYPE_JACK) {
                T.showShort(mContext, sensor.getName());
            } else {
//                Intent modify = new Intent();
////              modify.setClass(mContext, ModifySensorActivity.class);
//                modify.putExtra("sensor", sensor);
//                modify.putExtra("position", position);
//                modify.putExtra("contact", content);
//                startActivityForResult(modify, 1);
            }
        }

        @Override
        public void onLongClick(SensorRecycleAdapter.ViewHolder holder, Sensor sensor, int position) {
            if (sensor.getSensorCategory() == Sensor.SENSORCATEGORY_NORMAL) {
                //长按编辑或删除
                pop = new EditorAndDeletePop(mContext, Utils.dip2px(mContext, PopH));
                pop.setSensor(sensor);
                pop.setPosition(position);
                pop.setHolder(holder);
                pop.setOnDeleteAndEditorListner(Poplistner);
                pop.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);
            } else {
                //特殊传感器不可长按
                T.showShort(mContext, sensor.getName());
            }

        }

        @Override
        public void onSwitchClick(SensorRecycleAdapter.ViewHolder holder, Sensor sensor, int position) {
            //带开关的传感器的开关点击
            if (sensor.isControlSensor()) {
                if (sensor.getLampState() == 1 || sensor.getLampState() == 3) {
                    getLampState((byte) 3, sensor);
                } else if (sensor.getLampState() == 2 || sensor.getLampState() == 4) {
                    getLampState((byte) 2, sensor);
                }
                sensor.setLampState((byte) 0);
                mAdapter.notifySensorData(sensors);
            }
        }

        @Override
        public void onSensorSwitchClick(ViewHolder holder, Sensor sensor,
                                        int position) {
            // TODO Auto-generated method stub
            sensorSwitch = sensor;
        }
    };

    private EditorAndDeletePop.onDeleteAndEditorListner Poplistner = new EditorAndDeletePop.onDeleteAndEditorListner() {
        @Override
        public void EditorClick(SensorRecycleAdapter.ViewHolder holder, Sensor sensor, int position) {
            //弹编辑框
            modifySensorPostion = position;
            showInputDialog(inputClickListner, R.string.modify_sensor_name, R.string.sensor_inputname_hint, sensor.getName(), R.string.yes, R.string.no);
            pop.dismiss();
        }

        @Override
        public void DeletClick(SensorRecycleAdapter.ViewHolder holde, Sensor sensor, int position) {
            holder = holde;
            showLoadingDialog(listener, 2);
            DeleteSensor(sensor);
            pop.dismiss();
        }
    };

    private ImputDialog.MyInputClickListner inputClickListner = new ImputDialog.MyInputClickListner() {
        @Override
        public void onYesClick(Dialog dialog, View v, String input) {
            if (input == null || input.length() <= 0) {
                T.showShort(mContext, R.string.sensor_inputname_notnull);
            } else if (input.getBytes().length > Sensor.NameLen) {
                T.showShort(mContext, R.string.sensor_inputname_long);
            } else {
                nameTemp = input;
                if (inputDialog != null && inputDialog.isShowing()) {
                    inputDialog.inputDialogDismiss();
                }
                showLoadingDialog(listener, 3);
                modifySensorName(sensors.get(modifySensorPostion), input);
            }
        }

        @Override
        public void onNoClick(View v) {

        }
    };

    /**
     * 解析传感器数据
     *
     * @param data
     * @param Controcons
     * @param controlen
     * @param Sensorcons
     * @param Sensorlen
     */
    private void paserSensorData(byte[] data, byte Controcons, byte controlen, byte Sensorcons, byte Sensorlen) {
        byte[] contro = new byte[21];
        for (int i = 0; i < Controcons; i++) {
            System.arraycopy(data, 8 + i * contro.length, contro, 0, contro.length);
            Sensor sensor = new Sensor(Sensor.SENSORCATEGORY_NORMAL, contro, contro[0]);
            sensors.add(sensor);
        }
        if (data[3] == 0) {//没有防护计划
            byte[] sens = new byte[24];
            for (int i = 0; i < Sensorcons; i++) {
                System.arraycopy(data, 8 + controlen + i * sens.length, sens, 0, sens.length);
                Sensor sensor = new Sensor(Sensor.SENSORCATEGORY_NORMAL, sens, sens[0]);
                sensors.add(sensor);
            }
        } else if (data[3] == 1) {//有24位的防护计划
            byte[] sens = new byte[49];
            byte[] RealSens = new byte[24];
            for (int i = 0; i < Sensorcons; i++) {
                System.arraycopy(data, 8 + controlen + i * sens.length, sens, 0, sens.length);
                System.arraycopy(sens, 0, RealSens, 0, RealSens.length);
                Sensor sensor = new Sensor(Sensor.SENSORCATEGORY_NORMAL, RealSens, RealSens[0]);
                sensors.add(sensor);
            }
        }

    }

    /**
     * 解析特殊传感器数据
     *
     * @param data
     * @param len
     */
    private void paserSpecialSensorData(byte[] data, byte len) {
        byte[] contro = new byte[4];
        int k = len / contro.length;
        for (int i = 0; i < k; i++) {
            System.arraycopy(data, 4 + i * contro.length, contro, 0, contro.length);
            Sensor sensor = new Sensor(Sensor.SENSORCATEGORY_SPECIAL, contro, contro[0]);
            sensor.setName(getSpecialSensorName(sensor.getSensorType()));
            sensors.add(sensor);
        }
        Log.e("dxsTest", "size-->" + sensors.size());
    }


    @Override
    public void onPause() {
        super.onPause();
        if (isRegFilter) {
            isRegFilter = false;
            mContext.unregisterReceiver(mReceiver);
        }
    }

    private int counts = 0;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_add_sensor) {
            Log.e("onclick", "onclick+++");
            counts++;
            addSensor();
        }
    }

    /**
     * 学习传感器
     */
    private void addSensor() {
        FisheyeSetHandler.getInstance().sSetIntoLearnState(contact.contactId, contact.contactPassword);
    }

    /**
     * 删除传感器
     *
     * @param sensor
     */
    private void DeleteSensor(Sensor sensor) {
        if (sensor.getSensorType() == Constants.SensorType.TYPE_REMOTE_CONTROLLER) {
            FisheyeSetHandler.getInstance().sDeleteOneControler(contact.contactId, contact.contactPassword, sensor.getSensorSignature());
        } else {
            FisheyeSetHandler.getInstance().sDeleteOneSensor(contact.contactId, contact.contactPassword, sensor.getSensorSignature());
        }
    }

    /**
     * 修改传感器名字
     *
     * @param sensor
     */
    private void modifySensorName(Sensor sensor, String newName) {
        if (sensor.getSensorType() == Constants.SensorType.TYPE_REMOTE_CONTROLLER) {
            FisheyeSetHandler.getInstance().sChangeControlerName(contact.contactId, contact.contactPassword, sensor.getSensorSignature(), newName.getBytes());
        } else {
            FisheyeSetHandler.getInstance().sChangeSensorName(contact.contactId, contact.contactPassword, sensor.getSensorSignature(), newName.getBytes());
        }
    }

    /**
     * 处理学习传感器返回数据
     *
     * @param data
     * @param boption
     */
    private void getSensorData(byte[] data, byte boption) {
        if (boption != Constants.FishBoption.MESG_SET_OK) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        if (boption == Constants.FishBoption.MESG_SET_OK) {
            //进入学习
            showLoadingDialog(listener, 1);
        } else if (boption == Constants.LenStateCode.LEARN_STATE_SUCCESS) {
            //成功
            T.showShort(mContext, R.string.learning_success);
            Sensor sensor = new Sensor(Sensor.SENSORCATEGORY_NORMAL, data);
            sensors.add(sensor);
            mAdapter.notifySensorData(sensors);
        } else if (boption == Constants.LenStateCode.LEARN_STATE_TIMEOUT) {
            //超时
            T.showShort(mContext, R.string.time_out);
        } else if (boption == Constants.LenStateCode.LEARN_STATE_CONTROLER_FULL) {
            //遥控已学满
            T.showShort(mContext, R.string.sensor_control_full);
        } else if (boption == Constants.LenStateCode.LEARN_STATE_SENSOR_FULL) {
            //传感器已学满
            T.showShort(mContext, R.string.sensor_full);
        } else if (boption == Constants.LenStateCode.LEARN_STATE_BUSY) {
            //已经在学习
            T.showShort(mContext, R.string.sensor_busy);
        } else if (boption == Constants.LenStateCode.LEARN_STATE_ALREADY_LEARN) {
            //已经学习
            T.showShort(mContext, R.string.sensor_already_lean);
            //getAllSensorData();
        }
    }

//    private void getAddSensorACK(int arg1, int result) {
//        if (FishAckUtils.getACKCmd(arg1) == Constants.MsgSection.MSG_ID_FISHEYE_INTO_LEARN_STATE) {
//            if (result == Constants.P2P_SET.ACK_RESULT.ACK_SUCCESS) {
//                //弹出等待框
//                showLoadingDialog(listener, 1);
//            } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
//                //网络异常,不起作用，好像没有收到ACK回调，待查
//                if (counts >= 5) {
//                    T.showShort(mContext, R.string.net_error_operator_fault);
//                    counts = 0;
//                } else {
//                    addSensor();
//                }
//            }
//        }
//    }

    private NormalDialog.OnCustomCancelListner listener = new NormalDialog.OnCustomCancelListner() {
        @Override
        public void onCancle(int mark) {
            if (mark == 1) {
                T.showShort(mContext, R.string.net_error_operator_fault);
            } else if (mark == 2) {
                //删除放弃
            } else if (mark == 3) {
                //改名对话框消失
            }

        }
    };

    /**
     * 处理删除传感器返回
     *
     * @param option
     * @param data
     */
    private void deleteSensorResult(byte option, byte[] data) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        if (option == Constants.FishBoption.MESG_SET_OK) {
            sensors.remove(holder.position);
            mAdapter.notifySensorData(sensors);
        } else if (option == Constants.FishBoption.MESG_SET_DELETE_ONE_SENSOR_ERROR) {
            //传感器不存在
        }
    }

    /**
     * 处理修改数据返回
     *
     * @param option
     */
    private void modifySensorName(byte option) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        if (option == Constants.FishBoption.MESG_SET_OK) {
            Sensor s = sensors.get(modifySensorPostion);
            s.setName(nameTemp);
            mAdapter.notifySensorData(sensors);
        } else if (option == Constants.FishBoption.MESG_SET_DELETE_ONE_SENSOR_ERROR) {
            //传感器不存在
        }
        modifySensorPostion = -1;
    }

    /**
     * 清除所有一般传感器
     */
    private void clearNormalSensor() {
        for (int i = 0; i < sensors.size(); i++) {
            if (sensors.get(i).getSensorCategory() == Sensor.SENSORCATEGORY_NORMAL) {
                sensors.remove(i);
            }
        }
        mAdapter.notifySensorData(sensors);
    }

    /**
     * 获取特殊传感器的默认名
     *
     * @param type 传感器类型
     * @return 默认名
     */
    private String getSpecialSensorName(byte type) {
        if (type == Constants.SpecialSensorType.INDEX_ALARM_TYPE_MD) {
            return Utils.getStringForId(R.string.special_sensor_md);
        } else if (type == Constants.SpecialSensorType.INDEX_ALARM_TYPE_ATTACH) {
            return Utils.getStringForId(R.string.special_sensor_attach);
        }
        return "";
    }


    public boolean onBackPressed() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            Log.e("dxsTest", "true");
            return true;
        }
        Log.e("dxsTest", "false");
        return false;
    }

    /**
     * 更新传感器总开关
     */
    private void setSensorSwitch(Sensor sensor) {
        if (sensor.getSensorSwitch()) {
            sensor.setSensorSwitch(false);
        } else {
            sensor.setSensorSwitch(true);
        }
        mAdapter.notifyDataSetChanged();
    }
}
