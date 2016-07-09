package com.jwkj.utils;

import com.p2p.core.global.Constants;

/**
 * Created by dxs on 2016/1/12.
 * 处理ACK回调，代码中按需处理，返回值可以判断出对应命令
 */
public class FishAckUtils {
    public static int getACKCmd(int arg1){
        if (arg1 < Constants.MsgSection.MSG_ID_FISHEYE_SETTING_WORKMODE_DEFULT
                && arg1 >= (Constants.MsgSection.MSG_ID_FISHEYE_SETTING_WORKMODE_DEFULT - 1000)) {
            return Constants.MsgSection.MSG_ID_FISHEYE_SETTING_WORKMODE_DEFULT;
        }else if(arg1 < Constants.MsgSection.MSG_ID_FISHEYE_SETTING_IPC_WORKMODE
                && arg1 >= (Constants.MsgSection.MSG_ID_FISHEYE_SETTING_IPC_WORKMODE - 1000)){
            return Constants.MsgSection.MSG_ID_FISHEYE_SETTING_IPC_WORKMODE;
        }else if(arg1 < Constants.MsgSection.MSG_ID_FISHEYE_SETTING_SENSOR_WORKMODE
                && arg1 >= (Constants.MsgSection.MSG_ID_FISHEYE_SETTING_SENSOR_WORKMODE - 1000)){
            return Constants.MsgSection.MSG_ID_FISHEYE_SETTING_SENSOR_WORKMODE;
        }else if(arg1 < Constants.MsgSection.MSG_ID_FISHEYE_SETTING_SCHEDULE_WORKMODE
                && arg1 >= (Constants.MsgSection.MSG_ID_FISHEYE_SETTING_SCHEDULE_WORKMODE - 1000)){
            return Constants.MsgSection.MSG_ID_FISHEYE_SETTING_SCHEDULE_WORKMODE;
        }else if(arg1 < Constants.MsgSection.MSG_ID_FISHEYE_DELETE_SCHEDULE
                && arg1 >= (Constants.MsgSection.MSG_ID_FISHEYE_DELETE_SCHEDULE - 1000)){
            return Constants.MsgSection.MSG_ID_FISHEYE_DELETE_SCHEDULE;
        }else if(arg1 < Constants.MsgSection.MSG_ID_FISHEYE_GET_CURRENTWORKMODE
                && arg1 >= (Constants.MsgSection.MSG_ID_FISHEYE_GET_CURRENTWORKMODE - 1000)){
            return Constants.MsgSection.MSG_ID_FISHEYE_GET_CURRENTWORKMODE;
        }else if(arg1 < Constants.MsgSection.MSG_ID_FISHEYE_GET_SENSORWORKMODE
                && arg1 >= (Constants.MsgSection.MSG_ID_FISHEYE_GET_SENSORWORKMODE - 1000)){
            return Constants.MsgSection.MSG_ID_FISHEYE_GET_SENSORWORKMODE;
        }else if(arg1 < Constants.MsgSection.MSG_ID_FISHEYE_WORKMODE_SCHEDULE
                && arg1 >= (Constants.MsgSection.MSG_ID_FISHEYE_WORKMODE_SCHEDULE - 1000)){
            return Constants.MsgSection.MSG_ID_FISHEYE_WORKMODE_SCHEDULE;
        }else if(arg1 < Constants.MsgSection.MSG_ID_FISHEYE_SETTING_ALL_SENSOR_SWITCH
                && arg1 >= (Constants.MsgSection.MSG_ID_FISHEYE_SETTING_ALL_SENSOR_SWITCH - 1000)){
            return Constants.MsgSection.MSG_ID_FISHEYE_SETTING_ALL_SENSOR_SWITCH;
        }else if(arg1 < Constants.MsgSection.MSG_ID_FISHEYE_SET_LOW_VOL_TIMEINTERVAL
                && arg1 >= (Constants.MsgSection.MSG_ID_FISHEYE_SET_LOW_VOL_TIMEINTERVAL - 1000)){
            return Constants.MsgSection.MSG_ID_FISHEYE_SET_LOW_VOL_TIMEINTERVAL;
        }else if(arg1 < Constants.MsgSection.MSG_ID_FISHEYE_GET_LOW_VOL_TIMEINTERVAL
                && arg1 >= (Constants.MsgSection.MSG_ID_FISHEYE_GET_LOW_VOL_TIMEINTERVAL - 1000)){
            return Constants.MsgSection.MSG_ID_FISHEYE_GET_LOW_VOL_TIMEINTERVAL;
        }else if(arg1 < Constants.MsgSection.MSG_ID_FISHEYE_DELETE_ONE_CONTROLER
                && arg1 >= (Constants.MsgSection.MSG_ID_FISHEYE_DELETE_ONE_CONTROLER - 1000)){
            return Constants.MsgSection.MSG_ID_FISHEYE_DELETE_ONE_CONTROLER;
        }else if(arg1 < Constants.MsgSection.MSG_ID_FISHEYE_DELETE_ONE_SENSOR
                && arg1 >= (Constants.MsgSection.MSG_ID_FISHEYE_DELETE_ONE_SENSOR - 1000)){
            return Constants.MsgSection.MSG_ID_FISHEYE_DELETE_ONE_SENSOR;
        }else if(arg1 < Constants.MsgSection.MSG_ID_FISHEYE_CHANGE_CONTROLER_NAME
                && arg1 >= (Constants.MsgSection.MSG_ID_FISHEYE_CHANGE_CONTROLER_NAME - 1000)){
            return Constants.MsgSection.MSG_ID_FISHEYE_CHANGE_CONTROLER_NAME;
        }else if(arg1 < Constants.MsgSection.MSG_ID_FISHEYE_CHANGE_SENSOR_NAME
                && arg1 >= (Constants.MsgSection.MSG_ID_FISHEYE_CHANGE_SENSOR_NAME - 1000)){
            return Constants.MsgSection.MSG_ID_FISHEYE_CHANGE_SENSOR_NAME;
        }else if(arg1 < Constants.MsgSection.MSG_ID_FISHEYE_INTO_LEARN_STATE
                && arg1 >= (Constants.MsgSection.MSG_ID_FISHEYE_INTO_LEARN_STATE - 1000)){
            return Constants.MsgSection.MSG_ID_FISHEYE_INTO_LEARN_STATE;
        }else if(arg1 < Constants.MsgSection.MSG_ID_FISHEYE_TURN_SENSOR
                && arg1 >= (Constants.MsgSection.MSG_ID_FISHEYE_TURN_SENSOR - 1000)){
            return Constants.MsgSection.MSG_ID_FISHEYE_TURN_SENSOR;
        }else if(arg1 < Constants.MsgSection.MSG_ID_FISHEYE_SHARE_TO_MEMBER
                && arg1 >= (Constants.MsgSection.MSG_ID_FISHEYE_SHARE_TO_MEMBER - 1000)){
            return Constants.MsgSection.MSG_ID_FISHEYE_SHARE_TO_MEMBER;
        }else if(arg1 < Constants.MsgSection.MSG_ID_FISHEYE_GOT_SHARE_MESG
                && arg1 >= (Constants.MsgSection.MSG_ID_FISHEYE_GOT_SHARE_MESG - 1000)){
            return Constants.MsgSection.MSG_ID_FISHEYE_GOT_SHARE_MESG;
        }else if(arg1 < Constants.MsgSection.MSG_ID_FISHEYE_DEV_RECV_MEMBER_FEEDBACK
                && arg1 >= (Constants.MsgSection.MSG_ID_FISHEYE_DEV_RECV_MEMBER_FEEDBACK - 1000)){
            return Constants.MsgSection.MSG_ID_FISHEYE_DEV_RECV_MEMBER_FEEDBACK;
        }else if(arg1 < Constants.MsgSection.MSG_ID_FISHEYE_ADMIN_DELETE_ONE_MEMBER
                && arg1 >= (Constants.MsgSection.MSG_ID_FISHEYE_ADMIN_DELETE_ONE_MEMBER - 1000)){
            return Constants.MsgSection.MSG_ID_FISHEYE_ADMIN_DELETE_ONE_MEMBER;
        }else if(arg1 < Constants.MsgSection.MSG_ID_FISHEYE_DELETE_DEV
                && arg1 >= (Constants.MsgSection.MSG_ID_FISHEYE_DELETE_DEV - 1000)){
            return Constants.MsgSection.MSG_ID_FISHEYE_DELETE_DEV;
        }else if(arg1 < Constants.MsgSection.MSG_ID_FISHEYE_GET_MEMBER_LIST
                && arg1 >= (Constants.MsgSection.MSG_ID_FISHEYE_GET_MEMBER_LIST - 1000)){
            return Constants.MsgSection.MSG_ID_FISHEYE_GET_MEMBER_LIST;
        }
        return -1;
    }
}
