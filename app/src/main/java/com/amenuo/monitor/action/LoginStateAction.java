package com.amenuo.monitor.action;

import com.amenuo.monitor.application.MonitorApplication;
import com.jwkj.entity.Account;
import com.jwkj.global.AccountPersist;

/**
 * Created by laps on 7/17/16.
 */
public class LoginStateAction {

    public static void saveState(){
                    Account account = AccountPersist.getInstance()
                    .getActiveAccountInfo(MonitorApplication.getContext());
            if (null == account) {
                account = new Account();
            }
            account.three_number = "03122580";
            account.phone = "18612233302";
            account.email = "";
            account.sessionId = "509882562";
            account.rCode1 = "305707964";
            account.rCode2 = "1414084427";
            account.countryCode = "86";
            AccountPersist.getInstance()
                    .setActiveAccount(MonitorApplication.getContext(), account);
    }
}
