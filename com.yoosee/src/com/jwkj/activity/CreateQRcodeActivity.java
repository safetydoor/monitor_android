package com.jwkj.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.google.zxing.WriterException;

import com.yoosee.R;
import com.jwkj.global.Constants;
import com.jwkj.utils.EncodingHandler;
import com.jwkj.utils.T;
import com.jwkj.utils.UDPHelper;
import com.jwkj.widget.NormalDialog;

public class CreateQRcodeActivity extends BaseActivity implements
		OnClickListener {
	private Context mContext;
	ImageView img_qrcode, img_back;
	String ssidname;
	String wifipwd;
	Button bt_hear;
	UDPHelper mHelper;
	NormalDialog waitdialog;
	boolean isReceive = false;
	private Handler myhandler = new Handler();
	Button bt_help;
	WifiManager.MulticastLock lock;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		mContext = this;
		setContentView(R.layout.activity_creat_qr_code);
		WifiManager manager = (WifiManager) this
				.getSystemService(Context.WIFI_SERVICE);
		lock = manager.createMulticastLock("localWifi");
		ssidname = getIntent().getStringExtra("ssidname");
		wifipwd = getIntent().getStringExtra("wifiPwd");
		initComponent();
		maxVoice();
		qrcode();
		lock.acquire();
		mHelper = new UDPHelper(9988);
		mHelper.setCallBack(new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case UDPHelper.HANDLER_MESSAGE_BIND_ERROR:
					Log.e("my", "HANDLER_MESSAGE_BIND_ERROR");
					T.showShort(mContext, R.string.port_is_occupied);
					break;
				case UDPHelper.HANDLER_MESSAGE_RECEIVE_MSG:
					isReceive = true;
					Log.e("my", "HANDLER_MESSAGE_RECEIVE_MSG");
					waitdialog.dismiss();
					// NormalDialog successdialog=new NormalDialog(mContext);
					// successdialog.successDialog();
					T.showShort(mContext, R.string.set_wifi_success);
					mHelper.StopListen();
					Intent it = new Intent();
					it.setAction(Constants.Action.SETTING_WIFI_SUCCESS);
					mContext.sendBroadcast(it);
					// Intent add_device=new Intent(mContext,
					// LocalDeviceListActivity.class);
					// startActivity(add_device);
					finish();
					break;
				}
			}

		});
	}

	public void initComponent() {
		img_qrcode = (ImageView) findViewById(R.id.img_qrcode);
		img_back = (ImageView) findViewById(R.id.img_back);
		bt_hear = (Button) findViewById(R.id.bt_hear);
		bt_help = (Button) findViewById(R.id.bt_help);
		img_back.setOnClickListener(this);
		bt_hear.setOnClickListener(this);
		bt_help.setOnClickListener(this);
	}

	public void qrcode() {
		RelativeLayout.LayoutParams parms = (LayoutParams) img_qrcode
				.getLayoutParams();
		try {
			String QRinfo = "EnCtYpE_ePyTcNeEsSiD" + ssidname + "dIsSeCoDe"
					+ wifipwd + "eDoC";
			Bitmap qrCodeBitmap = EncodingHandler.createQRCode(QRinfo,
					parms.width);
			img_qrcode.setImageBitmap(qrCodeBitmap);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void maxVoice() {
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		int maxVolume = audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.img_back) {
			finish();
		} else if (id == R.id.bt_hear) {
			Intent itent = new Intent();
			itent.setClass(mContext, AddWaitActicity.class);
			itent.putExtra("ssidname", "");
			itent.putExtra("wifiPwd", "");
			itent.putExtra("type", -1);
			itent.putExtra("LocalIp", -1);
			itent.putExtra("isNeedSendWifi", false);
			startActivity(itent);
			finish();
		} else if (id == R.id.bt_help) {
			NormalDialog helpdialog = new NormalDialog(mContext);
			helpdialog.showQRcodehelp();
		} else {
		}

	}

	public Runnable mrunnable = new Runnable() {

		@Override
		public void run() {
			if (!isReceive) {
				waitdialog.dismiss();
				NormalDialog faileddialog = new NormalDialog(mContext);
				faileddialog.faildDialog();
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// mHelper.StopListen();
		// myhandler.removeCallbacks(mrunnable);
		lock.release();
	}

	@Override
	public int getActivityInfo() {
		return Constants.ActivityInfo.ACTIVITY_CREATEQRCODEACTIVITY;
	}

}
