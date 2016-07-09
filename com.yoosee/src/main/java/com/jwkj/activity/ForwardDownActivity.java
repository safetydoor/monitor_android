package com.jwkj.activity;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import com.jwkj.global.Constants;
import com.p2p.core.update.UpdateManager;

public class ForwardDownActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		int state = this.getIntent().getIntExtra("state", -1);

		switch (state) {
		case UpdateManager.HANDLE_MSG_DOWN_SUCCESS:
			Intent intent = new Intent(Intent.ACTION_VIEW);
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/" + Constants.Update.SAVE_PATH + "/"
					+ Constants.Update.FILE_NAME);
			if (!file.exists()) {
				return;
			}
			intent.setDataAndType(Uri.fromFile(file),
					Constants.Update.INSTALL_APK);
			this.startActivity(intent);
			break;
		case UpdateManager.HANDLE_MSG_DOWNING:
			UpdateManager.getInstance().cancelDown();
			break;
		case UpdateManager.HANDLE_MSG_DOWN_FAULT:
			break;
		}
		finish();
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_FORWARDDOWNACTIVITY;
	}
}
