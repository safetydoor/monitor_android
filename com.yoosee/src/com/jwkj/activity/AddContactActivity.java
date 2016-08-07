package com.jwkj.activity;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yoosee.R;
import com.jwkj.data.Contact;
import com.jwkj.data.DataManager;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.widget.MyPassLinearLayout;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;

public class AddContactActivity extends BaseActivity implements OnClickListener {
	private TextView mNext;
	private ImageView mBack;
	Context mContext;
	EditText contactId;
	Contact mContact;
	Button ensure;
	EditText input_device_id, input_device_name, input_device_password;
	Contact saveContact = new Contact();
	private MyPassLinearLayout llPass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		mContact = (Contact) getIntent().getSerializableExtra("contact");
		mContext = this;
		initCompent();
		String path = Environment.getExternalStorageDirectory().getPath()
				+ "/DCIM/Camera";
		File dirFile = new File(path);
		if (dirFile.exists()) {
			Log.e("file", "------");
		}
	}
	public void initCompent() {
		// contactId = (EditText) findViewById(R.id.contactId);
		mBack = (ImageView) findViewById(R.id.back_btn);
		mNext = (TextView) findViewById(R.id.next);
		ensure = (Button) findViewById(R.id.bt_ensure);
		input_device_id = (EditText) findViewById(R.id.input_device_id);
		input_device_name = (EditText) findViewById(R.id.input_contact_name);
		input_device_password = (EditText) findViewById(R.id.input_contact_pwd);
		// if(null!=mContact){
		// contactId.setText(mContact.contactId);
		// }

		input_device_password
				.setTransformationMethod(PasswordTransformationMethod
						.getInstance());
		mBack.setOnClickListener(this);
		mNext.setOnClickListener(this);
		ensure.setOnClickListener(this);
		llPass = (MyPassLinearLayout) findViewById(R.id.ll_p);
		llPass.setEditextListener(input_device_password);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.back_btn) {
			this.finish();
		} else if (id == R.id.next) {
			next();
		} else if (id == R.id.bt_ensure) {
			next();
		} else {
		}
	}

	public void next() {
		String input_id = input_device_id.getText().toString();
		String input_name = input_device_name.getText().toString();
		String input_pwd = input_device_password.getText().toString();
		if (input_id != null && input_id.trim().equals("")) {
			T.showShort(mContext, R.string.input_contact_id);
			return;
		}
		if (input_id.charAt(0) == '0' || input_id.length() > 9
				|| !Utils.isNumeric(input_id)) {
			T.show(mContext, R.string.device_id_invalid, Toast.LENGTH_SHORT);
			return;
		}
		if (null != FList.getInstance().isContact(input_id)) {
			T.showShort(mContext, R.string.contact_already_exist);
			return;
		}

		int type;
		if (input_id.charAt(0) == '0') {
			type = P2PValue.DeviceType.PHONE;
		} else {
			type = P2PValue.DeviceType.UNKNOWN;
		}
		if (input_name != null && input_name.trim().equals("")) {
			T.showShort(mContext, R.string.input_contact_name);
			return;
		}
		saveContact.contactId = input_id;
		saveContact.contactType = type;
		saveContact.activeUser = NpcCommon.mThreeNum;
		saveContact.messageCount = 0;
		List<Contact> lists = DataManager.findContactByActiveUser(mContext,
				NpcCommon.mThreeNum);
		for (Contact c : lists) {
			if (c.contactName.equals(input_name)) {
				T.showShort(mContext, R.string.device_name_exist);
				return;
			}
		}
		if (input_pwd == null || input_pwd.trim().equals("")) {
			 T.showShort(this, R.string.input_password);
			 return;
//			input_pwd = "";
		}
		if (saveContact.contactType != P2PValue.DeviceType.PHONE) {
		 if (input_pwd != null && !input_pwd.trim().equals("")) {
		  if (input_pwd.charAt(0) == '0'|| input_pwd.length() > 30) {
		      T.showShort(mContext, R.string.device_password_invalid);
		      return;
		   }
		 }
	    }

		List<Contact> contactlist = DataManager.findContactByActiveUser(
				mContext, NpcCommon.mThreeNum);
		for (Contact contact : contactlist) {
			if (contact.contactId.equals(saveContact.contactId)) {
				T.showShort(mContext, R.string.contact_already_exist);
				return;
			}
		}
		saveContact.contactName = input_name;
		saveContact.userPassword=input_pwd;
		String pwd=P2PHandler.getInstance().EntryPassword(input_pwd);
		saveContact.contactPassword = pwd;
		FList.getInstance().insert(saveContact);
		FList.getInstance().updateLocalDeviceWithLocalFriends();
		sendSuccessBroadcast();
		finish();
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_ADDCONTACTACTIVITY;
	}

	public void sendSuccessBroadcast() {
		Intent refreshContans = new Intent();
		refreshContans.setAction(Constants.Action.REFRESH_CONTANTS);
		refreshContans.putExtra("contact", saveContact);
		mContext.sendBroadcast(refreshContans);

		Intent createPwdSuccess = new Intent();
		createPwdSuccess.setAction(Constants.Action.UPDATE_DEVICE_FALG);
		createPwdSuccess.putExtra("threeNum", saveContact.contactId);
		mContext.sendBroadcast(createPwdSuccess);

		Intent add_success = new Intent();
		add_success.setAction(Constants.Action.ADD_CONTACT_SUCCESS);
		add_success.putExtra("contact", saveContact);
		mContext.sendBroadcast(add_success);

		Intent refreshNearlyTell = new Intent();
		refreshNearlyTell
				.setAction(Constants.Action.ACTION_REFRESH_NEARLY_TELL);
		mContext.sendBroadcast(refreshNearlyTell);
		T.showShort(mContext, R.string.add_success);
	}
}
