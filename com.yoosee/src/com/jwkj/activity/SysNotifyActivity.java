package com.jwkj.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.yoosee.R;
import com.jwkj.global.Constants;

@SuppressLint("SetJavaScriptEnabled")
public class SysNotifyActivity extends BaseActivity implements OnClickListener {
	WebView web_panel;
	ImageView back_btn;
	ProgressBar progress;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_sys_notify);
		initComponent();
	}

	public void initComponent() {
		back_btn = (ImageView) findViewById(R.id.back_btn);
		progress = (ProgressBar) findViewById(R.id.progress);
		web_panel = (WebView) findViewById(R.id.web_panel);
		web_panel.getSettings().setJavaScriptEnabled(true);
		web_panel
				.loadUrl("http://www.gwelltimes.com/upg/android/00/00/SysNotify/index.html");
		web_panel.setWebViewClient(wvc);
		back_btn.setOnClickListener(this);
	}

	WebViewClient wvc = new WebViewClient() {

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			progress.setVisibility(RelativeLayout.GONE);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
			progress.setVisibility(RelativeLayout.VISIBLE);
		}

	};

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.back_btn) {
			finish();
		}
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_SYSNOTIFYACTIVITY;
	}
}
