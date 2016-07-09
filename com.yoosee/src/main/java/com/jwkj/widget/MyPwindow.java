package com.jwkj.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.yoosee.R;

public class MyPwindow {
	private Context context;
	private PopupWindow pwindow;
	private View parent;
	private String content;

	public MyPwindow(Context context, View parent) {
		this.context = context;
		this.parent = parent;
		this.content = "";
	}

	public void showToast() {
		dismiss();
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_toast,
				null);
		TextView text = (TextView) view.findViewById(R.id.content);
		text.setText(content);
		pwindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		pwindow.setTouchable(false);
		pwindow.setAnimationStyle(R.style.dialog_normal);
		pwindow.showAtLocation(parent, Gravity.BOTTOM, 0, (int) context
				.getResources().getDimension(R.dimen.ipc_toast_margin_bottom));
	}

	public void dismiss() {
		if (null != pwindow) {
			pwindow.dismiss();
		}
	}

	public void setContentText(String content) {
		this.content = content;
	}

}
