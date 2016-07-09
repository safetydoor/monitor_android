package com.jwkj.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jwkj.utils.Utils;
import com.yoosee.R;

public class MyPassLinearLayout extends LinearLayout {
	private final static String TAG = "MyPassLinearLayout";
	private EditText etListen;
	private ImageView view1, view2, view3;
	private String password;
	private TextView txTips;
	private int PassWordStatus = 0;

	public MyPassLinearLayout(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.linerlayout_pass, this);
		initUI();
	}

	public MyPassLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.linerlayout_pass, this);
		initUI();
	}

	public void setEditextListener(EditText etListen) {
		this.etListen = etListen;
		initData();
	}

	private void initUI() {
		view1 = (ImageView) findViewById(R.id.v_pas_1);
		view2 = (ImageView) findViewById(R.id.v_pas_2);
		view3 = (ImageView) findViewById(R.id.v_pas_3);
		txTips = (TextView) findViewById(R.id.tx_pas);
	}

	private void initData() {
		etListen.addTextChangedListener(textWatcher);
		etListen.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				Log.i(TAG, "hasFocus--->" + hasFocus);
				if (hasFocus) {
					show();
				} else {
					dismiss();
				}

			}
		});
	}

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			password = etListen.getText().toString().trim();
			showLLPassWord(password);
		}
	};

	void showLLPassWord(String pass) {
		PassWordStatus = Utils.getPassWordStatus(pass);
		switch (PassWordStatus) {
		case 0:
			txTips.setText(R.string.weak);
			view1.setBackgroundColor(Utils.getColorByResouse(R.color.pass_gray));
			view2.setBackgroundColor(Utils.getColorByResouse(R.color.pass_gray));
			view3.setBackgroundColor(Utils.getColorByResouse(R.color.pass_gray));
			break;
		case 1:
			txTips.setText(R.string.weak);
			view1.setBackgroundColor(Utils.getColorByResouse(R.color.pass_red));
			view2.setBackgroundColor(Utils.getColorByResouse(R.color.pass_gray));
			view3.setBackgroundColor(Utils.getColorByResouse(R.color.pass_gray));
			break;
		case 2:
			txTips.setText(R.string.center);
			view1.setBackgroundColor(Utils
					.getColorByResouse(R.color.pass_yellow));
			view2.setBackgroundColor(Utils
					.getColorByResouse(R.color.pass_yellow));
			view3.setBackgroundColor(Utils.getColorByResouse(R.color.pass_gray));
			break;
		case 3:
			txTips.setText(R.string.strong);
			view1.setBackgroundColor(Utils.getColorByResouse(R.color.green));
			view2.setBackgroundColor(Utils.getColorByResouse(R.color.green));
			view3.setBackgroundColor(Utils.getColorByResouse(R.color.green));
			break;

		default:
			break;
		}

	}

	public void show() {
		this.setVisibility(View.VISIBLE);
	}

	public void dismiss() {
		this.setVisibility(View.GONE);
	}

	/**
	 * 获得密码等级
	 * 
	 * @return 密码等级
	 */
	public int getPasswordStatus() {
		return PassWordStatus;
	}

	/**
	 * 是否弱密码
	 * 
	 * @return
	 */
	public boolean isWeakpassword() {
		if (PassWordStatus < 2) {
			return true;
		}
		return false;
	}

}
