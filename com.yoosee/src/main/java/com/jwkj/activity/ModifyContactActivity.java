package com.jwkj.activity;

import java.net.InetAddress;
import java.net.UnknownHostException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yoosee.R;
import com.jwkj.data.APContact;
import com.jwkj.data.Contact;
import com.jwkj.data.DataManager;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.ImageUtils;
import com.jwkj.utils.T;
import com.jwkj.utils.TcpClient;
import com.jwkj.utils.Utils;
import com.jwkj.widget.HeaderView;
import com.jwkj.widget.MyPassLinearLayout;
import com.jwkj.widget.NormalDialog;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;

public class ModifyContactActivity extends BaseActivity implements
		OnClickListener {
	private static final int RESULT_GETIMG_FROM_CAMERA = 0x11;
	private static final int RESULT_GETIMG_FROM_GALLERY = 0x12;
	private static final int RESULT_CUT_IMAGE = 0x13;
	private TextView mSave;
	private ImageView mBack;
	HeaderView header_img;
	Context mContext;
	EditText contactName, contactPwd;
	LinearLayout layout_device_pwd;
	TextView contactId;
	Contact mModifyContact;
	NormalDialog dialog;
	RelativeLayout modify_header;
	TextView error_account1;
	private Bitmap tempHead;
	private MyPassLinearLayout llPass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_contact);
		mModifyContact = (Contact) getIntent().getSerializableExtra("contact");
		mContext = this;
		initCompent();
		Log.e("phone_ipdress","ipdress="+Utils.getPhoneIpdress());
	}

	public void initCompent() {
		contactId = (TextView) findViewById(R.id.contactId);
		contactName = (EditText) findViewById(R.id.contactName);
		contactPwd = (EditText) findViewById(R.id.contactPwd);
//		contactPwd.setTransformationMethod(PasswordTransformationMethod
//				.getInstance());
		layout_device_pwd = (LinearLayout) findViewById(R.id.layout_device_pwd);
		header_img = (HeaderView) findViewById(R.id.header_img);

		header_img.updateImage(mModifyContact.contactId, false);

		mBack = (ImageView) findViewById(R.id.back_btn);
		mSave = (TextView) findViewById(R.id.save);
		modify_header = (RelativeLayout) findViewById(R.id.modify_header);
		error_account1=(TextView)findViewById(R.id.error_account1);
		if (mModifyContact.contactType != P2PValue.DeviceType.PHONE) {
			layout_device_pwd.setVisibility(RelativeLayout.VISIBLE);
		} else {
			layout_device_pwd.setVisibility(RelativeLayout.GONE);
		}
		llPass = (MyPassLinearLayout) findViewById(R.id.ll_p);
		llPass.setEditextListener(contactPwd);
		if (mModifyContact != null) {
			contactId.setText(mModifyContact.contactId);
			contactName.setText(mModifyContact.contactName);
			if(mModifyContact.mode==P2PValue.DeviceMode.AP_MODE){
				error_account1.setText(R.string.device_wifi_pwd);
				contactPwd.setHint(R.string.input_device_wifi_pwd);
				APContact apContact=DataManager.findAPContactByActiveUserAndContactId(mContext, NpcCommon.mThreeNum,mModifyContact.contactId);
				if(apContact!=null){
					contactPwd.setText(apContact.Pwd);
				}
			}else{
				contactPwd.setTransformationMethod(PasswordTransformationMethod
						.getInstance());
				error_account1.setText(R.string.contact_pwd);
				contactPwd.setText(mModifyContact.userPassword);
				contactPwd.setHint(R.string.input_contact_pwd);
			}
		}
		modify_header.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mSave.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == RESULT_GETIMG_FROM_CAMERA) {
			try {
				Bundle extras = data.getExtras();
				tempHead = (Bitmap) extras.get("data");
				Log.e("my", tempHead.getWidth() + ":" + tempHead.getHeight());
				ImageUtils.saveImg(tempHead, Constants.Image.USER_HEADER_PATH,
						Constants.Image.USER_HEADER_TEMP_FILE_NAME);

				Intent cutImage = new Intent(mContext, CutImageActivity.class);
				cutImage.putExtra("contact", mModifyContact);
				startActivityForResult(cutImage, RESULT_CUT_IMAGE);
			} catch (NullPointerException e) {
				Log.e("my", "用户终止..");
			}
		} else if (requestCode == RESULT_GETIMG_FROM_GALLERY) {

			try {
				Uri uri = data.getData();
				tempHead = ImageUtils.getBitmap(
						ImageUtils.getAbsPath(mContext, uri),
						Constants.USER_HEADER_WIDTH_HEIGHT,
						Constants.USER_HEADER_WIDTH_HEIGHT);
				ImageUtils.saveImg(tempHead, Constants.Image.USER_HEADER_PATH,
						Constants.Image.USER_HEADER_TEMP_FILE_NAME);

				Intent cutImage = new Intent(mContext, CutImageActivity.class);
				cutImage.putExtra("contact", mModifyContact);
				startActivityForResult(cutImage, RESULT_CUT_IMAGE);

			} catch (NullPointerException e) {
				Log.e("my", "用户终止..");
			}
		} else if (requestCode == RESULT_CUT_IMAGE) {
			Log.e("my", resultCode + "");
			try {
				if (resultCode == 1) {
					header_img.updateImage(mModifyContact.contactId, false);
					Intent refreshContans = new Intent();
					refreshContans.setAction(Constants.Action.REFRESH_CONTANTS);
					refreshContans.putExtra("contact", mModifyContact);
					mContext.sendBroadcast(refreshContans);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void destroyTempHead() {
		if (tempHead != null && !tempHead.isRecycled()) {
			tempHead.recycle();
			tempHead = null;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int resId = v.getId();
		if (resId == R.id.back_btn) {
			this.finish();
//			P2PHandler.getInstance().getNvrIpcList(mModifyContact.contactId, mModifyContact.contactPassword);
		} else if (resId == R.id.save) {
			if(mModifyContact.mode==P2PValue.DeviceMode.AP_MODE){
				saveWifiName();
			}else{
				save();
			}
		} else if (resId == R.id.modify_header) {
			// dialog = new NormalDialog(mContext);
			// dialog.setTitle(R.string.operator);
			// String[] data = new String[]{
			// mContext.getResources().getString(R.string.gallery),
			// mContext.getResources().getString(R.string.camara)
			// };
			// dialog.setListData(data);
			// dialog.setOnItemClickListener(new OnItemClickListener(){
			//
			// @Override
			// public void onItemClick(AdapterView<?> arg0, View arg1,
			// int position, long arg3) {
			// // TODO Auto-generated method stub
			// switch(position){
			// case 0:
			// if(null!=dialog){
			// dialog.dismiss();
			// dialog = null;
			// }
			// Intent gallery = new Intent();
			// gallery.setAction(Intent.ACTION_GET_CONTENT);
			// gallery.setType("image/*");
			// startActivityForResult(gallery,RESULT_GETIMG_FROM_GALLERY);
			//
			// break;
			// case 1:
			// if(null!=dialog){
			// dialog.dismiss();
			// dialog = null;
			// }
			// Intent camera = new Intent();
			// camera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
			// startActivityForResult(camera,RESULT_GETIMG_FROM_CAMERA);
			// break;
			// }
			// }
			//
			// });
			// dialog.showSelectorDialog();
		}
	}

	void save() {
		String input_name = contactName.getText().toString();
		String input_pwd = contactPwd.getText().toString();

		if (input_name != null && input_name.trim().equals("")) {
			T.showShort(mContext, R.string.input_contact_name);
			return;
		}

		if (mModifyContact.contactType != P2PValue.DeviceType.PHONE) {
			if (input_pwd != null && input_pwd.trim().equals("")) {
				T.showShort(mContext, R.string.input_contact_pwd);
				return;
			}
			if(input_pwd.charAt(0) == '0'|| input_pwd.length() > 30){
				T.showShort(mContext, R.string.device_password_invalid);
				return;
			}
		}
		mModifyContact.contactName = input_name;
		mModifyContact.userPassword=input_pwd;
		String pwd=P2PHandler.getInstance().EntryPassword(input_pwd);
		mModifyContact.contactPassword =pwd;
		mModifyContact.defenceState= Constants.DefenceState.DEFENCE_STATE_LOADING;
		FList.getInstance().update(mModifyContact);
		Intent refreshContans = new Intent();
		refreshContans.setAction(Constants.Action.REFRESH_CONTANTS);
		refreshContans.putExtra("contact", mModifyContact);
		mContext.sendBroadcast(refreshContans);
		T.showShort(mContext, R.string.modify_success);
		finish();
	}
	public void saveWifiName(){
		String input_name=contactName.getText().toString();
		String input_pwd = contactPwd.getText().toString();
		if (input_name != null && input_name.trim().equals("")) {
			T.showShort(mContext, R.string.input_contact_name);
			return;
		}

		if (mModifyContact.contactType != P2PValue.DeviceType.PHONE) {
			if (input_pwd != null && input_pwd.trim().equals("")) {
				T.showShort(mContext, R.string.input_device_wifi_pwd);
				return;
			}
			if (input_pwd.length() < 8) {
				// 密码必须8位
				T.showShort(mContext, R.string.wifi_pwd_error);
				return;
			}
			mModifyContact.contactName = input_name;
			mModifyContact.wifiPassword=input_pwd;
			APContact apContact=changeContactToAPContact(mModifyContact);
			// 连接成功页面需要跳转并且保存数据库
			if (!DataManager.isAPContactExist(mContext,
					apContact.activeUser, apContact.contactId)) {
				// 保存
				Log.e("dxsTest","保存-->"+apContact.activeUser+"mModifyContact.wifiPassword-->"+apContact.Pwd);
				DataManager.insertAPContact(mContext, apContact);
			} else {
				// 更新
				Log.e("dxsTest","更新-->"+apContact.activeUser+"mModifyContact.wifiPassword-->"+apContact.Pwd);
				DataManager.updateAPContact(mContext, apContact);
			}
			Intent refreshContans = new Intent();
			refreshContans.setAction(Constants.Action.REFRESH_CONTANTS);
			refreshContans.putExtra("contact", mModifyContact);
			mContext.sendBroadcast(refreshContans);
			T.showShort(mContext, R.string.modify_success);
			finish();
		}
	}
	
	private APContact changeContactToAPContact(Contact contact) {
		if (NpcCommon.mThreeNum == null) {
			NpcCommon.mThreeNum = "0517401";
		}
		APContact ap = DataManager.findAPContactByActiveUserAndContactId(
				mContext, NpcCommon.mThreeNum, contact.contactId);
		if (ap != null) {
			ap.Pwd=contact.wifiPassword;
			return ap;
		} else {
			return new APContact(contact.contactId, contact.contactName,
					contact.contactName, contact.wifiPassword, NpcCommon.mThreeNum);
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		destroyTempHead();
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_MODIFYCONTACTACTIVITY;
	}
}
