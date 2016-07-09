package com.jwkj.fisheye;

/**
 * Created by dxs on 2015/12/31.
 */
public class FishSubCmd {
    public static final byte MESG_SUBTYPE_SETTING_WORKMODE_DEFAULT = 0;
    public static final byte MESG_SUBTYPE_SETTING_IPC_WORKMODE = 1;
    public static final byte MESG_SUBTYPE_SETTING_IPC_WORKMODE_RET = 2;
    public static final byte MESG_SUBTYPE_SETTING_SENSOR_WORKMODE = 3;
    public static final byte MESG_SUBTYPE_SETTING_SENSOR_WORKMODE_RET = 4;
    public static final byte MESG_SUBTYPE_SETTING_SCHEDULE_WORKMODE = 5;
    public static final byte MESG_SUBTYPE_SETTING_SCHEDULE_WORKMODE_RET = 6;
    public static final byte MESG_SUBTYPE_DELETE_SCHEDULE = 7;
    public static final byte MESG_SUBTYPE_DELETE_SCHEDULE_RET = 8;
    public static final byte MESG_SUBTYPE_GET_CURRENTWORKMODE = 9;
    public static final byte MESG_SUBTYPE_GET_CURRENTWORKMODE_RET = 10;
    public static final byte MESG_SUBTYPE_GET_SENSORWORKMODE = 11;
    public static final byte MESG_SUBTYPE_GET_SENSORWORKMODE_RET = 12;
    public static final byte MESG_SUBTYPE_GET_WORKMODE_SCHEDULE = 13;
    public static final byte MESG_SUBTYPE_GET_WORKMODE_SCHEDULE_RET = 14;
    public static final byte MESG_SUBTYPE_SETTING_ALL_SENSOR_SWITCH = 15;
    public static final byte MESG_SUBTYPE_SETTING_ALL_SENSOR_SWITCH_RET = 16;
    public static final byte MESG_SUBTYPE_GET_ALL_SENSOR_SWITCH = 17;
    public static final byte MESG_SUBTYPE_GET_ALL_SENSOR_SWITCH_RET = 18;
    public static final byte MESG_SUBTYPE_SET_LOW_VOL_TIMEINTERVAL = 19;
    public static final byte MESG_SUBTYPE_SET_LOW_VOL_TIMEINTERVAL_RET = 20;
    public static final byte MESG_SUBTYPE_GET_LOW_VOL_TIMEINTERVAL = 21;
    public static final byte MESG_SUBTYPE_GET_LOW_VOL_TIMEINTERVAL_RET = 22;
    //第二次添加
    public static final byte MESG_SUBTYPE_DELETE_ONE_CONTROLER = 23;
    public static final byte MESG_SUBTYPE_DELETE_ONE_CONTROLER_RET = 24;
    public static final byte MESG_SUBTYPE_DELETE_ONE_SENSOR = 25;
    public static final byte MESG_SUBTYPE_DELETE_ONE_SENSOR_RET = 26;
    public static final byte MESG_SUBTYPE_CHANGE_CONTROLER_NAME = 27;
    public static final byte MESG_SUBTYPE_CHANGE_CONTROLER_NAME_RET = 28;
    public static final byte MESG_SUBTYPE_CHANGE_SENSOR_NAME = 29;
    public static final byte MESG_SUBTYPE_CHANGE_SENSOR_NAME_RET = 30;
    public static final byte MESG_SUBTYPE_INTO_LEARN_STATE = 31;
    public static final byte MESG_SUBTYPE_INTO_LEARN_STATE_RET = 32;
    public static final byte MESG_SUBTYPE_TURN_SENSOR = 33;
    public static final byte MESG_SUBTYPE_TURN_SENSOR_RET = 34;
    public static final byte MESG_SUBTYPE_SHARE_TO_MEMBER = 35;
    public static final byte MESG_SUBTYPE_SHARE_TO_MEMBER_RET = 36;
    public static final byte MESG_SUBTYPE_GOT_SHARE_MESG = 37;
    public static final byte MESG_SUBTYPE_GOT_SHARE_MESG_RET = 38;
    public static final byte MESG_SUBTYPE_DEV_RECV_MEMBER_FEEDBACK = 39;
    public static final byte MESG_SUBTYPE_ADMIN_DELETE_ONE_MEMBER = 40;
    public static final byte MESG_SUBTYPE_ADMIN_DELETE_ONE_MEMBER_RET = 41;
    public static final byte MESG_SUBTYPE_DELETE_DEV = 42;
    public static final byte MESG_SUBTYPE_DELETE_DEV_RET = 43;
    public static final byte MESG_SUBTYPE_GET_MEMBER_LIST = 44;//过期列表命令，作废
    public static final byte MESG_SUBTYPE_GET_MEMBER_LIST_RET = 45;//同废
    public static final byte MESG_SUBTYPE_SET_ONE_SPECIAL_ALARM = 46;
    public static final byte MESG_SUBTYPE_SET_ONE_SPECIAL_ALARM_RET = 47;
    public static final byte MESG_SUBTYPE_GET_ALL_SPECIAL_ALARM = 48;
    public static final byte MESG_SUBTYPE_GET_ALL_SPECIAL_ALARM_RET = 49;
    public static final byte MESG_SUBTYPE_DEAL_LAMP = 50;
    public static final byte MESG_SUBTYPE_DEAL_LAMP_RET = 51;
    public static final byte MESG_SUBTYPE_KEEPCLIENT=52;
    public static final byte MESG_SUBTYPE_KEEPCLIENT_RET=53;
}
