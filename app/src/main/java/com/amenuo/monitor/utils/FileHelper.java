package com.amenuo.monitor.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;

import com.amenuo.monitor.application.MonitorApplication;

import org.apache.http.util.EncodingUtils;


public class FileHelper {

    //写数据
    public static void writeFile(String fileName, String writestr) {

        FileOutputStream fout = null;
        try {
            Context context = MonitorApplication.getContext();
            fout = context.openFileOutput(fileName, context.MODE_PRIVATE);
            byte[] bytes = writestr.getBytes();
            fout.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //读数据
    public static String readFile(String fileName) {
        String res = null;
        FileInputStream fin = null;
        try {
            Context context = MonitorApplication.getContext();
            fin = context.openFileInput(fileName);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }
}