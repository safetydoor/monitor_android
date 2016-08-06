package com.amenuo.monitor.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.amenuo.monitor.R;
import com.p2p.core.network.NetManager;

/**
 * Created by laps on 8/1/16.
 */
public class VerificationCodeTask extends AsyncTask<String, Void, Object> {

    private final static String CountryCode = "86";
    private String mPhoneNumber;
    private Context mContext;

    public VerificationCodeTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected Object doInBackground(String... params) {
        if (params!= null && params.length > 0){
            mPhoneNumber = params[0];
            return NetManager.getInstance(this.mContext)
                    .getPhoneCode(CountryCode, this.mPhoneNumber);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object object) {
        if (object == null){
            return;
        }
        int result = (Integer) object;
        String msg = null;
        switch (result) {
            case NetManager.CONNECT_CHANGE:
                new VerificationCodeTask(this.mContext).execute(this.mPhoneNumber);
                return;
            case NetManager.GET_PHONE_CODE_PHONE_USED:
                msg = this.mContext.getResources().getString(R.string.text_getVerificationCode_used);
                Toast.makeText(this.mContext, msg, Toast.LENGTH_SHORT).show();
                break;
            case NetManager.GET_PHONE_CODE_SUCCESS:
                msg = this.mContext.getResources().getString(R.string.text_getVerificationCode_success);
                Toast.makeText(this.mContext, msg, Toast.LENGTH_SHORT).show();
                break;
            case NetManager.GET_PHONE_CODE_TOO_TIMES:
                msg = this.mContext.getResources().getString(com.yoosee.R.string.get_phone_code_too_times);
                Toast.makeText(this.mContext, msg, Toast.LENGTH_SHORT).show();
                break;
            default:
                msg = this.mContext.getResources().getString(com.yoosee.R.string.registerfail);
                Toast.makeText(this.mContext, msg, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public interface Callback {

        public void onVerificationCodeResult(boolean success);
    }
}

//
//    private EventHandler eh = new EventHandler() {
//
//        @Override
//        public void afterEvent(int event, int result, Object data) {
//
//            if (result == SMSSDK.RESULT_COMPLETE) {
//                //回调完成
//                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
//                    //提交验证码成功
//                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
//                    //获取验证码成功
//
//                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
//                    //返回支持发送验证码的国家列表
//                }
//            } else {
//                ((Throwable) data).printStackTrace();
//            }
//        }
//    };

//
//SMSSDK.registerEventHandler(eh); //注册短信回调
//        SMSSDK.getSupportedCountries();

//SMSSDK.getVerificationCode("86", phoneNumber);