package com.amenuo.monitor.action;

import android.content.Context;
import android.text.TextUtils;

import com.amenuo.monitor.application.MonitorApplication;
import com.jwkj.data.SharedPreferencesManager;
import com.jwkj.entity.Account;
import com.jwkj.global.AccountPersist;
import com.jwkj.global.Constants;
import com.jwkj.global.NpcCommon;
import com.p2p.core.network.LoginResult;

/**
 * Created by laps on 7/17/16.
 */
public class LoginStateAction {

    public static void saveState(String userName, String passWord, LoginResult loginResult) {
        if (TextUtils.isEmpty(userName)) {
            return;
        }
        if (TextUtils.isEmpty(passWord)) {
            return;
        }
        if (loginResult == null) {
            return;
        }
        Context context = MonitorApplication.getContext();
        SharedPreferencesManager.getInstance()
                .putData(context,
                        SharedPreferencesManager.SP_FILE_GWELL,
                        SharedPreferencesManager.KEY_RECENTNAME,
                        userName);
        SharedPreferencesManager.getInstance().putData(context,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTPASS, passWord);
        String code = "86";
        SharedPreferencesManager.getInstance().putData(context,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTCODE, code);
        SharedPreferencesManager.getInstance().putRecentLoginType(
                context, Constants.LoginType.PHONE);

        String codeStr1 = String.valueOf(Long.parseLong(loginResult.rCode1));
        String codeStr2 = String.valueOf(Long.parseLong(loginResult.rCode2));
        Account account = AccountPersist.getInstance()
                .getActiveAccountInfo(context);
        if (null == account) {
            account = new Account();
        }
        account.three_number = loginResult.contactId;
        account.phone = loginResult.phone;
        account.email = loginResult.email;
        account.sessionId = loginResult.sessionId;
        account.rCode1 = codeStr1;
        account.rCode2 = codeStr2;
        account.countryCode = loginResult.countryCode;
        AccountPersist.getInstance()
                .setActiveAccount(context, account);
        NpcCommon.mThreeNum = AccountPersist.getInstance()
                .getActiveAccountInfo(context).three_number;
    }

    public static Boolean checkLogin() {
        Account activeUser = AccountPersist.getInstance().getActiveAccountInfo(
                MonitorApplication.getContext());

        if (activeUser != null && !activeUser.three_number.equals("0517401")) {
            NpcCommon.mThreeNum = activeUser.three_number;
            return true;
        }
        return false;
    }
}
