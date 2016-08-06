package com.amenuo.monitor.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.amenuo.monitor.application.MonitorApplication;

/**
 * SharedPreferences辅助类，线程安全
 */
public class SPHelper {
	//========================== SharedPreferences key =========================
	public final static String SHAREPREFERENCES_NAME = "com.amenuo.monitor.SharedPreferences";
	
	private static final SPHelper mSPHelper = new SPHelper();
	private SharedPreferences mSP;
	private Editor mEditor;
	
	public static SPHelper getInstance() {
		return mSPHelper;
	}
	
	public void init() {
		open();
	}
	
	public void init(Context context) {
		open(context);
	}
	
	private void open(Context context) {
		if(mSP == null) {
			mSP=context.getSharedPreferences(SHAREPREFERENCES_NAME, Context.MODE_WORLD_WRITEABLE);
			mEditor = mSP.edit();
		}
	}
	
	private void open() {
		if(mSP == null) {
			mSP= MonitorApplication.getContext().getSharedPreferences(SHAREPREFERENCES_NAME, Context.MODE_WORLD_WRITEABLE);
			mEditor = mSP.edit();
		}
	}
	
	public synchronized String getString(String key, String defValue) {
		return mSP.getString(key, defValue);
	}
	
	public synchronized void setString(String key, String value) {
		open();
		mEditor.putString(key, value);
		mEditor.commit();
	}
	
	public synchronized int getInt(String key, int defValue) {
		open();
		return mSP.getInt(key, defValue);
	}
	public synchronized void setInt(String key, int value) {
		open();
		mEditor.putInt(key, value);
		mEditor.commit();
	}
	
	public synchronized long getLong(String key, long defValue) {
		open();
		return mSP.getLong(key, defValue);
	}
	public synchronized void setLong(String key, long value) {
		open();
		mEditor.putLong(key, value);
		mEditor.commit();
	}
	
	public synchronized float getFloat(String key, float defValue) {
		open();
		return mSP.getFloat(key, defValue);
	}
	public synchronized void seFloat(String key, float value) {
		open();
		mEditor.putFloat(key, value);
		mEditor.commit();
	}
	
	public synchronized boolean getBoolean(String key, boolean defValue) {
		open();
		boolean flag =  mSP.getBoolean(key, defValue);
		return mSP.getBoolean(key, defValue);
	}
	public synchronized void setBoolean(String key, boolean value) {
		open();
		mEditor.putBoolean(key, value);
		mEditor.commit();
	}
	
}
