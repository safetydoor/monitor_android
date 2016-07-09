package com.jwkj.thread;

import android.os.Handler;
import android.os.Message;

import com.jwkj.global.Constants;
import com.jwkj.global.MyApp;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.Utils;
import com.p2p.core.update.UpdateManager;

public class UpdateCheckVersionThread extends Thread {
	boolean isNeedUpdate = false;
	Handler handler;

	public UpdateCheckVersionThread(Handler handler) {
		this.handler = handler;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			isNeedUpdate = UpdateManager.getInstance().checkUpdate(NpcCommon.mThreeNum);
			if (isNeedUpdate) {
				Message msg = new Message();
				msg.what = Constants.Update.CHECK_UPDATE_HANDLE_TRUE;
				String data = "";
				if (Utils.isZh(MyApp.app)) {
					data = UpdateManager.getInstance().getUpdateDescription();
				} else {
					data = UpdateManager.getInstance()
							.getUpdateDescription_en();
				}
				msg.obj = data;
				handler.sendMessage(msg);
			} else {
				Message msg = new Message();
				msg.what = Constants.Update.CHECK_UPDATE_HANDLE_FALSE;
				handler.sendMessage(msg);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
