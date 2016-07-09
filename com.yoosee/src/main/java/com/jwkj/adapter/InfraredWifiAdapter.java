package com.jwkj.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yoosee.R;

@SuppressLint("ResourceAsColor")
public class InfraredWifiAdapter extends BaseAdapter {
	private Context mContext;
	private boolean isConnected;
	private WifiInfo mWifiInfo;
	private boolean isScan;
	private List<ScanResult> datas = new ArrayList<ScanResult>();
	private WifiManager mWifiManager;
	private InfraredWifiAdapter mAdapter;
	private MyHandler mHandler;
	private TextView mPromptView;

	public InfraredWifiAdapter(Context context, TextView promptView) {
		mContext = context;
		this.mPromptView = promptView;
		mWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		// if (!mWifiManager.isWifiEnabled())
		// {
		// mWifiManager.setWifiEnabled(true);
		// }
		mHandler = new MyHandler();

		isScan = true;
		mAdapter = this;
		new WifiThread().start();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (!mWifiManager.isWifiEnabled()) {
			this.mPromptView.setVisibility(RelativeLayout.VISIBLE);
			return 0;
		}

		if (datas == null || datas.size() == 0) {
			this.mPromptView.setVisibility(RelativeLayout.VISIBLE);
			return 0;
		} else {
			this.mPromptView.setVisibility(RelativeLayout.GONE);
			return datas.size();
		}

	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return datas.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View view = arg1;
		if (null == view) {
			view = LayoutInflater.from(mContext).inflate(
					R.layout.list_infrared_wifi_item, null);
		}

		TextView name = (TextView) view.findViewById(R.id.text_name);
		ImageView wifi_type = (ImageView) view.findViewById(R.id.wifi_type);
		ScanResult result = datas.get(position);

		int type = 0;
		if (result.capabilities.indexOf("WPA") > 0) {
			type = 2;
		} else if (result.capabilities.indexOf("WEP") > 0) {
			type = 1;
		} else {
			type = 0;
		}

		if (type == 0) {
			wifi_type.setVisibility(RelativeLayout.GONE);
		} else {
			wifi_type.setVisibility(RelativeLayout.VISIBLE);
		}

		name.setText(result.SSID);
		return view;
	}

	public class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			mAdapter.notifyDataSetChanged();
		}

	}

	public class WifiThread extends Thread {
		public void run() {
			while (isScan) {
				if (!mWifiManager.isWifiEnabled()) {
					try {
						mHandler.sendEmptyMessage(0);
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					continue;
				}
				mWifiInfo = mWifiManager.getConnectionInfo();
				mWifiManager.startScan();
				datas = mWifiManager.getScanResults();

				mHandler.sendEmptyMessage(0);
				try {
					Thread.sleep(1000 * 15);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}

	public void stopScan() {

		this.isScan = false;
	}
}
