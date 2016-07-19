package com.amenuo.monitor.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by laps on 7/17/16.
 */
public class InputVerifyUtils {

    public static boolean verifyPhoneNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)){
            return false;
        }
        Pattern pattern = Pattern.compile("^1\\d{10}$");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static boolean verifyPassword(String password) {
        if (TextUtils.isEmpty(password)){
            return false;
        }
        return password.length() >= 6;
    }

    public static boolean verifyVerificationCode(String code) {
        return !TextUtils.isEmpty(code);
    }
}
