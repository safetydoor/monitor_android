package com.jwkj.fisheye;

import android.util.Log;

import com.jwkj.utils.Utils;
import com.p2p.core.P2PHandler;
import com.p2p.core.global.Constants;

import java.util.Arrays;

/**
 * Created by dxs on 2015/12/31.
 * 鱼眼项目新命令集合
 */
public class FisheyeSetHandler {
    private static FisheyeSetHandler manager=null;
    private static byte MESG_TYPE_WORKMODE_SETTING= (byte) 149;
    private static byte MESG_TYPE_GET_MEMBER_LIST= (byte) 214;
    private static int MSG_ID_FISHEYE_SETTING_WORKMODE_DEFULT =Constants.MsgSection.MSG_ID_FISHEYE_SETTING_WORKMODE_DEFULT;
    private static int MSG_ID_FISHEYE_SETTING_IPC_WORKMODE =Constants.MsgSection.MSG_ID_FISHEYE_SETTING_IPC_WORKMODE;
    private static int MSG_ID_FISHEYE_SETTING_SENSOR_WORKMODE =Constants.MsgSection.MSG_ID_FISHEYE_SETTING_SENSOR_WORKMODE;
    private static int MSG_ID_FISHEYE_SETTING_SCHEDULE_WORKMODE =Constants.MsgSection.MSG_ID_FISHEYE_SETTING_SCHEDULE_WORKMODE;
    private static int MSG_ID_FISHEYE_DELETE_SCHEDULE =Constants.MsgSection.MSG_ID_FISHEYE_DELETE_SCHEDULE;
    private static int MSG_ID_FISHEYE_GET_CURRENTWORKMODE =Constants.MsgSection.MSG_ID_FISHEYE_GET_CURRENTWORKMODE;
    private static int MSG_ID_FISHEYE_GET_SENSORWORKMODE =Constants.MsgSection.MSG_ID_FISHEYE_GET_SENSORWORKMODE;
    private static int MSG_ID_FISHEYE_WORKMODE_SCHEDULE =Constants.MsgSection.MSG_ID_FISHEYE_WORKMODE_SCHEDULE;
    private static int MSG_ID_FISHEYE_SETTING_ALL_SENSOR_SWITCH =Constants.MsgSection.MSG_ID_FISHEYE_SETTING_ALL_SENSOR_SWITCH;
    private static int MSG_ID_FISHEYE_GET_ALL_SENSOR_SWITCH =Constants.MsgSection.MSG_ID_FISHEYE_GET_ALL_SENSOR_SWITCH;
    private static int MSG_ID_FISHEYE_SET_LOW_VOL_TIMEINTERVAL =Constants.MsgSection.MSG_ID_FISHEYE_SET_LOW_VOL_TIMEINTERVAL;
    private static int MSG_ID_FISHEYE_GET_LOW_VOL_TIMEINTERVAL =Constants.MsgSection.MSG_ID_FISHEYE_GET_LOW_VOL_TIMEINTERVAL;
    //第二次添加
    private static int MSG_ID_FISHEYE_DELETE_ONE_CONTROLER =Constants.MsgSection.MSG_ID_FISHEYE_DELETE_ONE_CONTROLER;
    private static int MSG_ID_FISHEYE_DELETE_ONE_SENSOR =Constants.MsgSection.MSG_ID_FISHEYE_DELETE_ONE_SENSOR;
    private static int MSG_ID_FISHEYE_CHANGE_CONTROLER_NAME =Constants.MsgSection.MSG_ID_FISHEYE_CHANGE_CONTROLER_NAME;
    private static int MSG_ID_FISHEYE_CHANGE_SENSOR_NAME =Constants.MsgSection.MSG_ID_FISHEYE_CHANGE_SENSOR_NAME;
    private static int MSG_ID_FISHEYE_INTO_LEARN_STATE =Constants.MsgSection.MSG_ID_FISHEYE_INTO_LEARN_STATE;
    private static int MSG_ID_FISHEYE_TURN_SENSOR =Constants.MsgSection.MSG_ID_FISHEYE_TURN_SENSOR;
    private static int MSG_ID_FISHEYE_SHARE_TO_MEMBER =Constants.MsgSection.MSG_ID_FISHEYE_SHARE_TO_MEMBER;
    private static int MSG_ID_FISHEYE_GOT_SHARE_MESG =Constants.MsgSection.MSG_ID_FISHEYE_GOT_SHARE_MESG;
    private static int MSG_ID_FISHEYE_DEV_RECV_MEMBER_FEEDBACK =Constants.MsgSection.MSG_ID_FISHEYE_DEV_RECV_MEMBER_FEEDBACK;
    private static int MSG_ID_FISHEYE_ADMIN_DELETE_ONE_MEMBER =Constants.MsgSection.MSG_ID_FISHEYE_ADMIN_DELETE_ONE_MEMBER;
    private static int MSG_ID_FISHEYE_DELETE_DEV =Constants.MsgSection.MSG_ID_FISHEYE_DELETE_DEV;
    private static int MSG_ID_FISHEYE_GET_MEMBER_LIST =Constants.MsgSection.MSG_ID_FISHEYE_GET_MEMBER_LIST;
    private static int MSG_ID_FISHEYE_SET_SPECIAL_ALARM =Constants.MsgSection.MSG_ID_FISHEYE_SET_SPECIAL_ALARM;
    private static int MSG_ID_FISHEYE_GET_ALL_SPECIAL_ALARM =Constants.MsgSection.MSG_ID_FISHEYE_GET_ALL_SPECIAL_ALARM;
    private static int MSG_ID_FISHEYE_GET_LAMP_STATE=Constants.MsgSection.MSG_ID_FISHEYE_GET_LAMP_STATE;
    private static int MSG_ID_FISHEYE_KEEP_CLIENT_STATE=Constants.MsgSection.MSG_ID_FISHEYE_KEEP_CLIENT_STATE;


    private FisheyeSetHandler() {
    }
    public synchronized static FisheyeSetHandler getInstance() {
        if (null == manager) {
            synchronized (FisheyeSetHandler.class) {
                manager = new FisheyeSetHandler();
            }
        }
        return manager;
    }

    /**
     * 设置工作模式
     * @param contactId 设备ID
     * @param password 设备密码
     * @param workMode 工作模式
     */
    public void SetFishWorkMode(String contactId, String password,int workMode){
        if ((MSG_ID_FISHEYE_SETTING_IPC_WORKMODE) >= (Constants.MsgSection.MSG_ID_FISHEYE_SETTING_IPC_WORKMODE)) {
            MSG_ID_FISHEYE_SETTING_IPC_WORKMODE = Constants.MsgSection.MSG_ID_FISHEYE_SETTING_IPC_WORKMODE - 1000;
        }
        byte[] fishdata=new byte[4];
        fishdata[0]=MESG_TYPE_WORKMODE_SETTING;
        fishdata[1]=FishSubCmd.MESG_SUBTYPE_SETTING_IPC_WORKMODE;
        fishdata[3]= (byte) workMode;
        P2PHandler.getInstance().setFishEye(Integer.parseInt(contactId),Integer.parseInt(password),MSG_ID_FISHEYE_SETTING_IPC_WORKMODE,fishdata);
        MSG_ID_FISHEYE_SETTING_IPC_WORKMODE++;
    }

    /**
     * 设置传感器工作模式
     * @param contactId 设备ID
     * @param password 设备密码
     * @param sensarg 传感器参数,长度为7
     */
    public void sSettingSensorWorkMode(String contactId, String password,byte[] sensarg){
        if ((MSG_ID_FISHEYE_SETTING_SENSOR_WORKMODE) >= (Constants.MsgSection.MSG_ID_FISHEYE_SETTING_SENSOR_WORKMODE)) {
            MSG_ID_FISHEYE_SETTING_SENSOR_WORKMODE = Constants.MsgSection.MSG_ID_FISHEYE_SETTING_SENSOR_WORKMODE - 1000;
        }
        byte[] fishdata=new byte[10];
        fishdata[0]=MESG_TYPE_WORKMODE_SETTING;
        fishdata[1]=FishSubCmd.MESG_SUBTYPE_SETTING_SENSOR_WORKMODE;
        System.arraycopy(sensarg,0,fishdata,3,sensarg.length);
        P2PHandler.getInstance().setFishEye(Integer.parseInt(contactId),Integer.parseInt(password),MSG_ID_FISHEYE_SETTING_SENSOR_WORKMODE,fishdata);
        MSG_ID_FISHEYE_SETTING_SENSOR_WORKMODE++;
    }

    public void sSettingScheduleTimeGroup(String contactId, String password,byte[] grop){
        if ((MSG_ID_FISHEYE_SETTING_SCHEDULE_WORKMODE) >= (Constants.MsgSection.MSG_ID_FISHEYE_SETTING_SCHEDULE_WORKMODE)) {
            MSG_ID_FISHEYE_SETTING_SCHEDULE_WORKMODE = Constants.MsgSection.MSG_ID_FISHEYE_SETTING_SCHEDULE_WORKMODE - 1000;
        }
        byte[] fishdata=new byte[8];
        fishdata[0]=MESG_TYPE_WORKMODE_SETTING;
        fishdata[1]=FishSubCmd.MESG_SUBTYPE_SETTING_SCHEDULE_WORKMODE;
        System.arraycopy(grop,0,fishdata,3,grop.length);
        P2PHandler.getInstance().setFishEye(Integer.parseInt(contactId), Integer.parseInt(password), MSG_ID_FISHEYE_SETTING_SCHEDULE_WORKMODE, fishdata);
        Log.e("dxsTest", "fishdata:addtime-->" + Arrays.toString(fishdata));
        MSG_ID_FISHEYE_SETTING_SCHEDULE_WORKMODE++;
    }

    public void sClearScheduleTimeGroup(String contactId, String password,int bGroupIndex){
        if ((MSG_ID_FISHEYE_DELETE_SCHEDULE) >= (Constants.MsgSection.MSG_ID_FISHEYE_DELETE_SCHEDULE)) {
            MSG_ID_FISHEYE_DELETE_SCHEDULE = Constants.MsgSection.MSG_ID_FISHEYE_DELETE_SCHEDULE - 1000;
        }
        byte[] fishdata=new byte[4];
        fishdata[0]=MESG_TYPE_WORKMODE_SETTING;
        fishdata[1]=FishSubCmd.MESG_SUBTYPE_DELETE_SCHEDULE;
        fishdata[3]= (byte) bGroupIndex;
        P2PHandler.getInstance().setFishEye(Integer.parseInt(contactId), Integer.parseInt(password), MSG_ID_FISHEYE_DELETE_SCHEDULE, fishdata);
        MSG_ID_FISHEYE_DELETE_SCHEDULE++;
    }

    public void sGetCurrentWorkMode(String contactId, String password){
        if ((MSG_ID_FISHEYE_GET_CURRENTWORKMODE) >= (Constants.MsgSection.MSG_ID_FISHEYE_GET_CURRENTWORKMODE)) {
            MSG_ID_FISHEYE_GET_CURRENTWORKMODE = Constants.MsgSection.MSG_ID_FISHEYE_GET_CURRENTWORKMODE - 1000;
        }
        byte[] fishdata=new byte[4];
        fishdata[0]=MESG_TYPE_WORKMODE_SETTING;
        fishdata[1]=FishSubCmd.MESG_SUBTYPE_GET_CURRENTWORKMODE;
        P2PHandler.getInstance().setFishEye(Integer.parseInt(contactId),Integer.parseInt(password),MSG_ID_FISHEYE_GET_CURRENTWORKMODE,fishdata);
        MSG_ID_FISHEYE_GET_CURRENTWORKMODE++;
    }

    public void sGetSenSorWorkMode(String contactId, String password){
        if ((MSG_ID_FISHEYE_GET_SENSORWORKMODE) >= (Constants.MsgSection.MSG_ID_FISHEYE_GET_SENSORWORKMODE)) {
            MSG_ID_FISHEYE_GET_SENSORWORKMODE = Constants.MsgSection.MSG_ID_FISHEYE_GET_SENSORWORKMODE - 1000;
        }
        byte[] fishdata=new byte[67];
        fishdata[0]=MESG_TYPE_WORKMODE_SETTING;
        fishdata[1]=FishSubCmd.MESG_SUBTYPE_GET_SENSORWORKMODE;
        P2PHandler.getInstance().setFishEye(Integer.parseInt(contactId),Integer.parseInt(password),MSG_ID_FISHEYE_GET_SENSORWORKMODE,fishdata);
        MSG_ID_FISHEYE_GET_SENSORWORKMODE++;
    }

    /**
     * 获取工作模式
     * @param contactId
     * @param password
     */
    public void sGetWorkModeSchedule(String contactId, String password){
        if ((MSG_ID_FISHEYE_WORKMODE_SCHEDULE) >= (Constants.MsgSection.MSG_ID_FISHEYE_WORKMODE_SCHEDULE)) {
            MSG_ID_FISHEYE_WORKMODE_SCHEDULE = Constants.MsgSection.MSG_ID_FISHEYE_WORKMODE_SCHEDULE - 1000;
        }
        byte[] fishdata=new byte[27];
        fishdata[0]=MESG_TYPE_WORKMODE_SETTING;
        fishdata[1]=FishSubCmd.MESG_SUBTYPE_GET_WORKMODE_SCHEDULE;
        P2PHandler.getInstance().setFishEye(Integer.parseInt(contactId),Integer.parseInt(password),MSG_ID_FISHEYE_WORKMODE_SCHEDULE,fishdata);
        MSG_ID_FISHEYE_WORKMODE_SCHEDULE++;
    }

    public void sSetAllSensorSwitch(String contactId, String password,int bSetValue){
        if ((MSG_ID_FISHEYE_SETTING_ALL_SENSOR_SWITCH) >= (Constants.MsgSection.MSG_ID_FISHEYE_SETTING_ALL_SENSOR_SWITCH)) {
            MSG_ID_FISHEYE_SETTING_ALL_SENSOR_SWITCH = Constants.MsgSection.MSG_ID_FISHEYE_SETTING_ALL_SENSOR_SWITCH - 1000;
        }
        byte[] fishdata=new byte[4];
        fishdata[0]=MESG_TYPE_WORKMODE_SETTING;
        fishdata[1]=FishSubCmd.MESG_SUBTYPE_SETTING_ALL_SENSOR_SWITCH;
        fishdata[3]= (byte) bSetValue;
        P2PHandler.getInstance().setFishEye(Integer.parseInt(contactId),Integer.parseInt(password),MSG_ID_FISHEYE_SETTING_ALL_SENSOR_SWITCH,fishdata);
        MSG_ID_FISHEYE_SETTING_ALL_SENSOR_SWITCH++;
    }

    public void sGetAllSensorSwitch(String contactId, String password,int bSetValue){
        if ((MSG_ID_FISHEYE_GET_ALL_SENSOR_SWITCH) >= (Constants.MsgSection.MSG_ID_FISHEYE_GET_ALL_SENSOR_SWITCH)) {
            MSG_ID_FISHEYE_GET_ALL_SENSOR_SWITCH = Constants.MsgSection.MSG_ID_FISHEYE_GET_ALL_SENSOR_SWITCH - 1000;
        }
        byte[] fishdata=new byte[4];
        fishdata[0]=MESG_TYPE_WORKMODE_SETTING;
        fishdata[1]=FishSubCmd.MESG_SUBTYPE_GET_ALL_SENSOR_SWITCH;
        fishdata[3]= (byte) bSetValue;
        P2PHandler.getInstance().setFishEye(Integer.parseInt(contactId),Integer.parseInt(password),MSG_ID_FISHEYE_GET_ALL_SENSOR_SWITCH,fishdata);
        MSG_ID_FISHEYE_GET_ALL_SENSOR_SWITCH++;
    }

    public void sSetLowVolPushTimeInterval(String contactId, String password,int bMemo,short wTimeVal){
        if ((MSG_ID_FISHEYE_SET_LOW_VOL_TIMEINTERVAL) >= (Constants.MsgSection.MSG_ID_FISHEYE_SET_LOW_VOL_TIMEINTERVAL)) {
            MSG_ID_FISHEYE_SET_LOW_VOL_TIMEINTERVAL = Constants.MsgSection.MSG_ID_FISHEYE_SET_LOW_VOL_TIMEINTERVAL - 1000;
        }
        byte[] fishdata=new byte[6];
        fishdata[0]=MESG_TYPE_WORKMODE_SETTING;
        fishdata[1]=FishSubCmd.MESG_SUBTYPE_SET_LOW_VOL_TIMEINTERVAL;
        fishdata[3]= (byte) bMemo;
        fishdata[4]= (byte) (wTimeVal>>0);
        fishdata[5]= (byte) (wTimeVal>>8);
        P2PHandler.getInstance().setFishEye(Integer.parseInt(contactId),Integer.parseInt(password),MSG_ID_FISHEYE_SET_LOW_VOL_TIMEINTERVAL,fishdata);
        MSG_ID_FISHEYE_SET_LOW_VOL_TIMEINTERVAL++;
    }

    public void sGetLowVolPushTimeInterval(String contactId, String password){
        if ((MSG_ID_FISHEYE_GET_LOW_VOL_TIMEINTERVAL) >= (Constants.MsgSection.MSG_ID_FISHEYE_GET_LOW_VOL_TIMEINTERVAL)) {
            MSG_ID_FISHEYE_GET_LOW_VOL_TIMEINTERVAL = Constants.MsgSection.MSG_ID_FISHEYE_GET_LOW_VOL_TIMEINTERVAL - 1000;
        }
        byte[] fishdata=new byte[6];
        fishdata[0]=MESG_TYPE_WORKMODE_SETTING;
        fishdata[1]=FishSubCmd.MESG_SUBTYPE_GET_LOW_VOL_TIMEINTERVAL;
        P2PHandler.getInstance().setFishEye(Integer.parseInt(contactId),Integer.parseInt(password),MSG_ID_FISHEYE_GET_LOW_VOL_TIMEINTERVAL,fishdata);
        MSG_ID_FISHEYE_GET_LOW_VOL_TIMEINTERVAL++;
    }

    /**
     * 删除遥控
     * @param contactId
     * @param password
     */
    public void sDeleteOneControler(String contactId, String password,byte[] sensordata){
        if ((MSG_ID_FISHEYE_DELETE_ONE_CONTROLER) >= (Constants.MsgSection.MSG_ID_FISHEYE_DELETE_ONE_CONTROLER)) {
            MSG_ID_FISHEYE_DELETE_ONE_CONTROLER = Constants.MsgSection.MSG_ID_FISHEYE_DELETE_ONE_CONTROLER - 1000;
        }
        byte[] fishdata=new byte[7];
        fishdata[0]=MESG_TYPE_WORKMODE_SETTING;
        fishdata[1]=FishSubCmd.MESG_SUBTYPE_DELETE_ONE_CONTROLER;
        System.arraycopy(sensordata,0,fishdata,3,sensordata.length);
        P2PHandler.getInstance().setFishEye(Integer.parseInt(contactId),Integer.parseInt(password),MSG_ID_FISHEYE_DELETE_ONE_CONTROLER,fishdata);
        MSG_ID_FISHEYE_DELETE_ONE_CONTROLER++;
    }

    /**
     * 删除传感器
     * @param contactId
     * @param password
     */
    public void sDeleteOneSensor(String contactId, String password,byte[] sensordata){
        if ((MSG_ID_FISHEYE_DELETE_ONE_SENSOR) >= (Constants.MsgSection.MSG_ID_FISHEYE_DELETE_ONE_SENSOR)) {
            MSG_ID_FISHEYE_DELETE_ONE_SENSOR = Constants.MsgSection.MSG_ID_FISHEYE_DELETE_ONE_SENSOR - 1000;
        }
        byte[] fishdata=new byte[7];
        fishdata[0]=MESG_TYPE_WORKMODE_SETTING;
        fishdata[1]=FishSubCmd.MESG_SUBTYPE_DELETE_ONE_SENSOR;
        System.arraycopy(sensordata,0,fishdata,3,sensordata.length);
        P2PHandler.getInstance().setFishEye(Integer.parseInt(contactId),Integer.parseInt(password),MSG_ID_FISHEYE_DELETE_ONE_SENSOR,fishdata);
        MSG_ID_FISHEYE_DELETE_ONE_SENSOR++;
    }

    /**
     * 改变遥控名字
     * @param contactId
     * @param password
     * @param newname
     */
    public void sChangeControlerName(String contactId, String password,byte[] sensor,byte[] newname){
        if ((MSG_ID_FISHEYE_CHANGE_CONTROLER_NAME) >= (Constants.MsgSection.MSG_ID_FISHEYE_CHANGE_CONTROLER_NAME)) {
            MSG_ID_FISHEYE_CHANGE_CONTROLER_NAME = Constants.MsgSection.MSG_ID_FISHEYE_CHANGE_CONTROLER_NAME - 1000;
        }
        byte[] fishdata=new byte[23];
        fishdata[0]=MESG_TYPE_WORKMODE_SETTING;
        fishdata[1]=FishSubCmd.MESG_SUBTYPE_CHANGE_CONTROLER_NAME;
        System.arraycopy(sensor,0,fishdata,3,sensor.length);
        System.arraycopy(newname,0,fishdata,3+sensor.length,newname.length);
        P2PHandler.getInstance().setFishEye(Integer.parseInt(contactId),Integer.parseInt(password),MSG_ID_FISHEYE_CHANGE_CONTROLER_NAME,fishdata);
        MSG_ID_FISHEYE_CHANGE_CONTROLER_NAME++;
    }

    /**
     * 修改传感器名字
     * @param contactId
     * @param password
     * @param newname
     */
    public void sChangeSensorName(String contactId, String password,byte[] sensor,byte[] newname){
        if ((MSG_ID_FISHEYE_CHANGE_SENSOR_NAME) >= (Constants.MsgSection.MSG_ID_FISHEYE_CHANGE_SENSOR_NAME)) {
            MSG_ID_FISHEYE_CHANGE_SENSOR_NAME = Constants.MsgSection.MSG_ID_FISHEYE_CHANGE_SENSOR_NAME - 1000;
        }
        byte[] fishdata=new byte[23];
        fishdata[0]=MESG_TYPE_WORKMODE_SETTING;
        fishdata[1]=FishSubCmd.MESG_SUBTYPE_CHANGE_SENSOR_NAME;
        System.arraycopy(sensor,0,fishdata,3,sensor.length);
        System.arraycopy(newname,0,fishdata,3+sensor.length,newname.length);
        P2PHandler.getInstance().setFishEye(Integer.parseInt(contactId),Integer.parseInt(password),MSG_ID_FISHEYE_CHANGE_SENSOR_NAME,fishdata);
        MSG_ID_FISHEYE_CHANGE_SENSOR_NAME++;
    }

    /**
     * 打开或关闭传感器
     * @param contactId
     * @param password
     */
    public void sTurnSensor(String contactId, String password,byte[] sensor,byte sensorState){
        if ((MSG_ID_FISHEYE_TURN_SENSOR) >= (Constants.MsgSection.MSG_ID_FISHEYE_TURN_SENSOR)) {
            MSG_ID_FISHEYE_TURN_SENSOR = Constants.MsgSection.MSG_ID_FISHEYE_TURN_SENSOR - 1000;
        }
        byte[] fishdata=new byte[8];
        fishdata[0]=MESG_TYPE_WORKMODE_SETTING;
        fishdata[1]=FishSubCmd.MESG_SUBTYPE_TURN_SENSOR;
        fishdata[3]=sensorState;
        System.arraycopy(sensor,0,fishdata,4,sensor.length);
        P2PHandler.getInstance().setFishEye(Integer.parseInt(contactId),Integer.parseInt(password),MSG_ID_FISHEYE_TURN_SENSOR,fishdata);
        MSG_ID_FISHEYE_TURN_SENSOR++;
    }

    /**
     * 学习传感器
     * @param contactId
     * @param password
     */
    public void sSetIntoLearnState(String contactId, String password){
        if ((MSG_ID_FISHEYE_INTO_LEARN_STATE) >= (Constants.MsgSection.MSG_ID_FISHEYE_INTO_LEARN_STATE)) {
            MSG_ID_FISHEYE_INTO_LEARN_STATE = Constants.MsgSection.MSG_ID_FISHEYE_INTO_LEARN_STATE - 1000;
        }
        byte[] fishdata=new byte[4];
        fishdata[0]=MESG_TYPE_WORKMODE_SETTING;
        fishdata[1]=FishSubCmd.MESG_SUBTYPE_INTO_LEARN_STATE;
        P2PHandler.getInstance().setFishEye(Integer.parseInt(contactId),Integer.parseInt(password),MSG_ID_FISHEYE_INTO_LEARN_STATE,fishdata);
        MSG_ID_FISHEYE_INTO_LEARN_STATE++;
    }

    /**
     * 分享给成员
     * @param contactId
     * @param password
     */
    public void sShareToMember(String contactId, String password,byte[] data){
        if ((MSG_ID_FISHEYE_SHARE_TO_MEMBER) >= (Constants.MsgSection.MSG_ID_FISHEYE_SHARE_TO_MEMBER)) {
            MSG_ID_FISHEYE_SHARE_TO_MEMBER = Constants.MsgSection.MSG_ID_FISHEYE_SHARE_TO_MEMBER - 1000;
        }
        byte[] fishdata=new byte[83];
        fishdata[0]=MESG_TYPE_WORKMODE_SETTING;
        fishdata[1]=FishSubCmd.MESG_SUBTYPE_SHARE_TO_MEMBER;
        System.arraycopy(data,0,fishdata,3,data.length);
        P2PHandler.getInstance().setFishEye(Integer.parseInt(contactId),Integer.parseInt(password),MSG_ID_FISHEYE_SHARE_TO_MEMBER,fishdata);
        MSG_ID_FISHEYE_SHARE_TO_MEMBER++;
    }

    /**
     * 成员是否同意添加
     * @param contactId
     * @param password
     */
    public void sMemberAgree(String contactId, String password,byte isgaree,byte[] deviceInfo){
        if ((MSG_ID_FISHEYE_GOT_SHARE_MESG) >= (Constants.MsgSection.MSG_ID_FISHEYE_GOT_SHARE_MESG)) {
            MSG_ID_FISHEYE_GOT_SHARE_MESG = Constants.MsgSection.MSG_ID_FISHEYE_GOT_SHARE_MESG - 1000;
        }
        byte[] fishdata=new byte[83];
        fishdata[0]=MESG_TYPE_WORKMODE_SETTING;
        fishdata[1]=FishSubCmd.MESG_SUBTYPE_GOT_SHARE_MESG_RET;
        fishdata[2]=isgaree;
        System.arraycopy(deviceInfo,0,fishdata,3,deviceInfo.length);
        P2PHandler.getInstance().setFishEye(Integer.parseInt(contactId),Integer.parseInt(password),MSG_ID_FISHEYE_GOT_SHARE_MESG,fishdata);
        MSG_ID_FISHEYE_GOT_SHARE_MESG++;
    }

    /**
     * 删除用户
     * @param contactId
     * @param password
     */
    public void sDeleteMember(String contactId, String password,int APPID,int userid){
        if ((MSG_ID_FISHEYE_ADMIN_DELETE_ONE_MEMBER) >= (Constants.MsgSection.MSG_ID_FISHEYE_ADMIN_DELETE_ONE_MEMBER)) {
            MSG_ID_FISHEYE_ADMIN_DELETE_ONE_MEMBER = Constants.MsgSection.MSG_ID_FISHEYE_ADMIN_DELETE_ONE_MEMBER - 1000;
        }
        byte[] fishdata=new byte[13];
        fishdata[0]=MESG_TYPE_WORKMODE_SETTING;
        fishdata[1]=FishSubCmd.MESG_SUBTYPE_ADMIN_DELETE_ONE_MEMBER;
        byte[] AppID= Utils.intToBytes(APPID|0x80000000);
        System.arraycopy(AppID,0,fishdata,5,AppID.length);
        byte[] id= Utils.intToBytes(userid|0x80000000);
        System.arraycopy(id,0,fishdata,9,id.length);
        P2PHandler.getInstance().setFishEye(Integer.parseInt(contactId),Integer.parseInt(password),MSG_ID_FISHEYE_ADMIN_DELETE_ONE_MEMBER,fishdata);
        MSG_ID_FISHEYE_ADMIN_DELETE_ONE_MEMBER++;
    }

    /**
     * 删除设备
     * @param contactId
     * @param password
     */
    public void sDeleteDevice(String contactId, String password,int AppId){
        if ((MSG_ID_FISHEYE_DELETE_DEV) >= (Constants.MsgSection.MSG_ID_FISHEYE_DELETE_DEV)) {
            MSG_ID_FISHEYE_DELETE_DEV = Constants.MsgSection.MSG_ID_FISHEYE_DELETE_DEV - 1000;
        }
        byte[] fishdata=new byte[13];
        fishdata[0]=MESG_TYPE_WORKMODE_SETTING;
        fishdata[1]=FishSubCmd.MESG_SUBTYPE_DELETE_DEV;
        byte[] id=Utils.intToBytes(AppId|0x80000000);
        System.arraycopy(id,0,fishdata,5,id.length);
        P2PHandler.getInstance().setFishEye(Integer.parseInt(contactId),Integer.parseInt(password),MSG_ID_FISHEYE_DELETE_DEV,fishdata);
        MSG_ID_FISHEYE_DELETE_DEV++;
    }

    /**
     * 获取列表
     * @param contactId
     * @param password
     */
    public void sMemberWhiteList(String contactId, String password,int AppID){
        if ((MSG_ID_FISHEYE_GET_MEMBER_LIST) >= (Constants.MsgSection.MSG_ID_FISHEYE_GET_MEMBER_LIST)) {
            MSG_ID_FISHEYE_GET_MEMBER_LIST = Constants.MsgSection.MSG_ID_FISHEYE_GET_MEMBER_LIST - 1000;
        }
        byte[] fishdata=new byte[83];
        fishdata[0]=MESG_TYPE_GET_MEMBER_LIST;
        fishdata[1]=0;
        byte[] app=Utils.intToBytes(AppID|0x80000000);
        System.arraycopy(app,0,fishdata,5,app.length);
        P2PHandler.getInstance().setFishEye(Integer.parseInt(contactId),Integer.parseInt(password),MSG_ID_FISHEYE_GET_MEMBER_LIST,fishdata);
        MSG_ID_FISHEYE_GET_MEMBER_LIST++;
    }

    /**
     * 设置特殊传感器
     * @param contactId
     * @param password
     */
    public void sSpecialAlarmData(String contactId, String password,byte[] specalData){
        if ((MSG_ID_FISHEYE_SET_SPECIAL_ALARM) >= (Constants.MsgSection.MSG_ID_FISHEYE_SET_SPECIAL_ALARM)) {
            MSG_ID_FISHEYE_SET_SPECIAL_ALARM = Constants.MsgSection.MSG_ID_FISHEYE_SET_SPECIAL_ALARM - 1000;
        }
        byte[] fishdata=new byte[8];
        fishdata[0]=MESG_TYPE_WORKMODE_SETTING;
        fishdata[1]=FishSubCmd.MESG_SUBTYPE_SET_ONE_SPECIAL_ALARM;
        System.arraycopy(specalData,0,fishdata,4,specalData.length);
        P2PHandler.getInstance().setFishEye(Integer.parseInt(contactId),Integer.parseInt(password),MSG_ID_FISHEYE_SET_SPECIAL_ALARM,fishdata);
        MSG_ID_FISHEYE_SET_SPECIAL_ALARM++;
    }

    /**
     * 获取所有的特殊传感器
     * @param contactId
     * @param password
     */
    public void sGetAllSpecialAlarmData(String contactId, String password){
        if ((MSG_ID_FISHEYE_GET_ALL_SPECIAL_ALARM) >= (Constants.MsgSection.MSG_ID_FISHEYE_GET_ALL_SPECIAL_ALARM)) {
            MSG_ID_FISHEYE_GET_ALL_SPECIAL_ALARM = Constants.MsgSection.MSG_ID_FISHEYE_GET_ALL_SPECIAL_ALARM - 1000;
        }
        byte[] fishdata=new byte[7];
        fishdata[0]=MESG_TYPE_WORKMODE_SETTING;
        fishdata[1]=FishSubCmd.MESG_SUBTYPE_GET_ALL_SPECIAL_ALARM;
        P2PHandler.getInstance().setFishEye(Integer.parseInt(contactId),Integer.parseInt(password),MSG_ID_FISHEYE_GET_ALL_SPECIAL_ALARM,fishdata);
        MSG_ID_FISHEYE_GET_ALL_SPECIAL_ALARM++;
    }

    /**
     * 查询或者获取开关类传感器的状态
     * @param contactId
     * @param password
     * @param lampState 1，查询 2，打开 3，关闭
     * @param lampInfo 开关类传感器特征码
     */
    public void sGetLampStatu(String contactId, String password,byte lampState,byte[] lampInfo){
        if ((MSG_ID_FISHEYE_GET_LAMP_STATE) >= (Constants.MsgSection.MSG_ID_FISHEYE_GET_LAMP_STATE)) {
            MSG_ID_FISHEYE_GET_LAMP_STATE = Constants.MsgSection.MSG_ID_FISHEYE_GET_LAMP_STATE - 1000;
        }
        byte[] fishdata=new byte[11];
        fishdata[0]=MESG_TYPE_WORKMODE_SETTING;
        fishdata[1]=FishSubCmd.MESG_SUBTYPE_DEAL_LAMP;
        fishdata[3]=lampState;
        System.arraycopy(lampInfo,0,fishdata,4,lampInfo.length);
        P2PHandler.getInstance().setFishEye(Integer.parseInt(contactId),Integer.parseInt(password),MSG_ID_FISHEYE_GET_LAMP_STATE,fishdata);
        MSG_ID_FISHEYE_GET_LAMP_STATE++;
    }

    /**
     * 使设备静音
     * @param contactId
     * @param password
     */
    public void sKeepClientCmd(String contactId, String password){
        if ((MSG_ID_FISHEYE_KEEP_CLIENT_STATE) >= (Constants.MsgSection.MSG_ID_FISHEYE_KEEP_CLIENT_STATE)) {
            MSG_ID_FISHEYE_KEEP_CLIENT_STATE = Constants.MsgSection.MSG_ID_FISHEYE_KEEP_CLIENT_STATE - 1000;
        }
        byte[] fishdata=new byte[4];
        fishdata[0]=MESG_TYPE_WORKMODE_SETTING;
        fishdata[1]=FishSubCmd.MESG_SUBTYPE_KEEPCLIENT;
        fishdata[3]=1;
        P2PHandler.getInstance().setFishEye(Integer.parseInt(contactId),Integer.parseInt(password),MSG_ID_FISHEYE_KEEP_CLIENT_STATE,fishdata);
        MSG_ID_FISHEYE_KEEP_CLIENT_STATE++;
    }

}
