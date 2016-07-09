package com.jwkj.activity;

import com.yoosee.R;
import com.jwkj.global.Constants;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class ForgetPassword extends Activity implements OnClickListener {
	public WebView wv;
	public ImageView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgetpassword);
		back = (ImageView) findViewById(R.id.back_btn);
		back.setOnClickListener(this);
		wv = (WebView) findViewById(R.id.webView1);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.loadUrl(Constants.FORGET_PASSWORD_URL);
		wv.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.back_btn) {
			finish();
		}

	}

}
