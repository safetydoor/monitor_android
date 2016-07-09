package com.jwkj.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jwkj.data.Contact;
import com.jwkj.data.DataManager;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.ImageUtils;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.widget.HeaderView;
import com.jwkj.widget.MyPassLinearLayout;
import com.jwkj.widget.NormalDialog;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;
import com.p2p.core.utils.MyUtils;
import com.yoosee.R;

import java.io.File;
import java.util.List;

public class AddContactNextActivity extends BaseActivity implements
        OnClickListener {
    private static final int RESULT_GETIMG_FROM_CAMERA = 0x11;
    private static final int RESULT_GETIMG_FROM_GALLERY = 0x12;
    private static final int RESULT_CUT_IMAGE = 0x13;
    private TextView mSave;
    private ImageView mBack;
    Context mContext;
    EditText contactName, contactPwd;
    LinearLayout layout_device_pwd;
    TextView contactId;
    HeaderView header_img;
    NormalDialog dialog;
    Contact mSaveContact;
    RelativeLayout modify_header;
    LinearLayout layout_create_pwd;
    EditText createPwd1, createPwd2;
    private Bitmap tempHead;
    boolean isSave = false;
    boolean isCreatePassword = false;
    String input_name, input_pwd, input_create_pwd1, input_create_pwd2;
    boolean isRegFilter;
    String ipFlag;
    boolean isfactory;
    private MyPassLinearLayout llPass;
    private MyPassLinearLayout llPass2;
    String userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact_next);
        mSaveContact = (Contact) getIntent().getSerializableExtra("contact");
        isCreatePassword = getIntent().getBooleanExtra("isCreatePassword",
                false);
        isfactory = getIntent().getBooleanExtra("isfactory", false);
        ipFlag = getIntent().getStringExtra("ipFlag");
        mContext = this;
        initCompent();
        regFilter();
    }

    public void initCompent() {
        contactId = (TextView) findViewById(R.id.contactId);
        contactName = (EditText) findViewById(R.id.contactName);
        contactPwd = (EditText) findViewById(R.id.contactPwd);
        contactPwd.setTransformationMethod(PasswordTransformationMethod
                .getInstance());
        layout_device_pwd = (LinearLayout) findViewById(R.id.layout_device_pwd);
        layout_create_pwd = (LinearLayout) findViewById(R.id.layout_create_pwd);
        createPwd1 = (EditText) findViewById(R.id.createPwd1);
        createPwd2 = (EditText) findViewById(R.id.createPwd2);
        createPwd1.setTransformationMethod(PasswordTransformationMethod
                .getInstance());
        createPwd2.setTransformationMethod(PasswordTransformationMethod
                .getInstance());
        mBack = (ImageView) findViewById(R.id.back_btn);
        mSave = (TextView) findViewById(R.id.save);
        header_img = (HeaderView) findViewById(R.id.header_img);
        modify_header = (RelativeLayout) findViewById(R.id.modify_header);

        header_img.updateImage(mSaveContact.contactId, false);
        contactName.setText("Cam" + mSaveContact.contactId);
        llPass = (MyPassLinearLayout) findViewById(R.id.ll_p);
        llPass.setEditextListener(contactPwd);
        llPass2 = (MyPassLinearLayout) findViewById(R.id.ll_p2);
        llPass2.setEditextListener(createPwd1);
        if (isCreatePassword) {
            createPwd1.requestFocus();
            layout_create_pwd.setVisibility(RelativeLayout.VISIBLE);
            layout_device_pwd.setVisibility(RelativeLayout.GONE);
        } else {
            contactPwd.requestFocus();
            layout_create_pwd.setVisibility(RelativeLayout.GONE);

            if (mSaveContact.contactType != P2PValue.DeviceType.PHONE) {
                layout_device_pwd.setVisibility(RelativeLayout.VISIBLE);
            } else {
                layout_device_pwd.setVisibility(RelativeLayout.GONE);
            }
        }

        Contact contact = FList.getInstance().isContact(mSaveContact.contactId);
        if (null != contact) {
            contactName.setText(contact.contactName);
        }
        contactId.setText(mSaveContact.contactId);
        modify_header.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mSave.setOnClickListener(this);
    }

    public void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.P2P.ACK_RET_SET_INIT_PASSWORD);
        filter.addAction(Constants.P2P.RET_SET_INIT_PASSWORD);
        mContext.registerReceiver(mReceiver, filter);
        isRegFilter = true;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            if (intent.getAction().equals(Constants.P2P.RET_SET_INIT_PASSWORD)) {
                int result = intent.getIntExtra("result", -1);
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                if (result == Constants.P2P_SET.INIT_PASSWORD_SET.SETTING_SUCCESS) {
                    Contact contact = FList.getInstance().isContact(
                            mSaveContact.contactId);
                    if (null != contact) {
                        contact.contactName = input_name;
                        contact.contactPassword = input_create_pwd1;
                        contact.userPassword = userPassword;
                        FList.getInstance().update(contact);
                    } else {
                        mSaveContact.contactName = input_name;
                        mSaveContact.contactPassword = input_create_pwd1;
                        mSaveContact.userPassword = userPassword;
                        FList.getInstance().insert(mSaveContact);
                    }

                    FList.getInstance().updateLocalDeviceFlag(
                            mSaveContact.contactId,
                            Constants.DeviceFlag.ALREADY_SET_PASSWORD);
                    isSave = true;
                    FList.getInstance().updateLocalDeviceWithLocalFriends();
                    sendSuccessBroadcast();
                    Intent createPwdSuccess = new Intent();
                    createPwdSuccess
                            .setAction(Constants.Action.ACTIVITY_FINISH);
                    mContext.sendBroadcast(createPwdSuccess);
                    T.showShort(mContext, R.string.add_success);
                    finish();
                } else if (result == Constants.P2P_SET.INIT_PASSWORD_SET.ALREADY_EXIST_PASSWORD) {
                    Intent createPwdSuccess = new Intent();
                    createPwdSuccess
                            .setAction(Constants.Action.UPDATE_DEVICE_FALG);
                    createPwdSuccess.putExtra("threeNum",
                            mSaveContact.contactId);
                    mContext.sendBroadcast(createPwdSuccess);
                    T.showShort(mContext, R.string.already_init_passwd);
                    finish();
                } else {
                    T.showShort(mContext, R.string.operator_error);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_RET_SET_INIT_PASSWORD)) {
                int result = intent.getIntExtra("result", -1);
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                    T.showShort(mContext, R.string.password_error);
                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    T.showShort(mContext, R.string.net_error_operator_fault);
                }
            }
        }
    };

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
                cutImage.putExtra("contact", mSaveContact);
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
                cutImage.putExtra("contact", mSaveContact);
                startActivityForResult(cutImage, RESULT_CUT_IMAGE);

            } catch (NullPointerException e) {
                Log.e("my", "用户终止..");
            }
        } else if (requestCode == RESULT_CUT_IMAGE) {
            Log.e("my", resultCode + "");
            try {
                if (resultCode == 1) {
                    header_img.updateImage(mSaveContact.contactId, false);
                    Intent refreshContans = new Intent();
                    refreshContans.setAction(Constants.Action.REFRESH_CONTANTS);
                    refreshContans.putExtra("contact", mSaveContact);
                    mContext.sendBroadcast(refreshContans);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int resId = v.getId();
        if (resId == R.id.back_btn) {
            this.finish();
        } else if (resId == R.id.save) {
            save();
        }
    }

    public void destroyTempHead() {
        if (tempHead != null && !tempHead.isRecycled()) {
            tempHead.recycle();
            tempHead = null;
        }
    }

    void save() {
        input_name = contactName.getText().toString();
        input_pwd = contactPwd.getText().toString();
        input_create_pwd1 = createPwd1.getText().toString();
        input_create_pwd2 = createPwd2.getText().toString();
        if (input_name != null && input_name.trim().equals("")) {
            T.showShort(mContext, R.string.input_contact_name);
            return;
        }

        if (isCreatePassword) {
            Log.e("sendwifi", "------------------");

            if (null == input_create_pwd1 || "".equals(input_create_pwd1)) {
                T.showShort(this, R.string.inputpassword);
                return;
            }
            if (input_create_pwd1.charAt(0) == '0' || input_create_pwd1.length() > 30) {
                T.showShort(this, R.string.device_password_invalid);
            }

            if (null == input_create_pwd2 || "".equals(input_create_pwd2)) {
                T.showShort(this, R.string.reinputpassword);
                return;
            }
            if (!input_create_pwd1.equals(input_create_pwd2)) {
                T.showShort(this, R.string.differentpassword);
                return;
            }
            if (llPass2.isWeakpassword()) {
                T.showShort(mContext, R.string.simple_password);
                return;
            }
            if (null == dialog) {
                dialog = new NormalDialog(this, this.getResources().getString(
                        R.string.verification), "", "", "");
                dialog.setStyle(NormalDialog.DIALOG_STYLE_LOADING);
            }
            String tips = "---------------------------";
            if (null != ipFlag && !ipFlag.equals("")
                    && MyUtils.isNumeric(ipFlag)) {
//				tips = "--IP-->" + ipFlag + "-ID-->" + mSaveContact.contactId;
//				dialog.setTitle(tips);
                userPassword = input_create_pwd1;
                Log.e("password", "before-->" + userPassword);
                input_create_pwd1 = P2PHandler.getInstance().EntryPassword(input_create_pwd1);
                Log.e("password", "after-->" + input_create_pwd1 + "--" + "ipFlag=" + ipFlag);
                P2PHandler.getInstance().setInitPassword(ipFlag,
                        input_create_pwd1);
            } else {
                // P2PHandler.getInstance().setInitPassword(
                // mSaveContact.contactId, input_create_pwd1);
                // Log.e("sendwifi", "contactId=" + mSaveContact.contactId +
                // "--"
                // + "device_pwd=" + input_create_pwd1);
                T.showShort(mContext, "IP没有找到");
            }
            Log.e("dxsinitPassWord", "tips-->" + tips);
            dialog.showDialog();

        } else {
            if (input_pwd == null || input_pwd.trim().equals("")) {
                T.showShort(this, R.string.input_password);
                return;
            }
            if (mSaveContact.contactType != P2PValue.DeviceType.PHONE) {
                if (input_pwd != null && !input_pwd.trim().equals("")) {
                    if (input_pwd.length() > 30 || input_pwd.charAt(0) == '0') {
                        T.showShort(mContext, R.string.device_password_invalid);
                        return;
                    }
                }
            }
            if (isfactory == false) {
                List<Contact> lists = DataManager.findContactByActiveUser(
                        mContext, NpcCommon.mThreeNum);
                for (Contact c : lists) {
                    if (c.contactName.equals(input_name)) {
                        T.showShort(mContext, R.string.device_name_exist);
                        return;
                    }
                    if (c.contactId.equals(mSaveContact.contactId)) {
                        T.showShort(mContext, R.string.contact_already_exist);
                        return;
                    }
                }
                mSaveContact.contactName = input_name;
                mSaveContact.userPassword = input_pwd;
                input_pwd = P2PHandler.getInstance().EntryPassword(input_pwd);
                mSaveContact.contactPassword = input_pwd;
                FList.getInstance().insert(mSaveContact);
                FList.getInstance().updateLocalDeviceWithLocalFriends();
                isSave = true;
                sendSuccessBroadcast();
                finish();
            } else {
                Contact contact = FList.getInstance().isContact(
                        mSaveContact.contactId);
                if (null != contact) {
                    contact.contactName = input_name;
                    contact.userPassword = input_pwd;
                    input_pwd = P2PHandler.getInstance().EntryPassword(input_pwd);
                    contact.contactPassword = input_pwd;
                    FList.getInstance().update(contact);
                } else {
                    mSaveContact.contactName = input_name;
                    mSaveContact.userPassword = input_pwd;
                    input_pwd = P2PHandler.getInstance().EntryPassword(input_pwd);
                    mSaveContact.contactPassword = input_pwd;
                    FList.getInstance().insert(mSaveContact);
                }
                FList.getInstance().updateLocalDeviceWithLocalFriends();
                isSave = true;
                sendSuccessBroadcast();
                finish();
            }
        }
    }

    public void sendSuccessBroadcast() {
        Intent refreshContans = new Intent();
        refreshContans.setAction(Constants.Action.REFRESH_CONTANTS);
        refreshContans.putExtra("contact", mSaveContact);
        mContext.sendBroadcast(refreshContans);

        Intent createPwdSuccess = new Intent();
        createPwdSuccess.setAction(Constants.Action.UPDATE_DEVICE_FALG);
        createPwdSuccess.putExtra("threeNum", mSaveContact.contactId);
        mContext.sendBroadcast(createPwdSuccess);

        Intent add_success = new Intent();
        add_success.setAction(Constants.Action.ADD_CONTACT_SUCCESS);
        add_success.putExtra("contact", mSaveContact);
        mContext.sendBroadcast(add_success);

        Intent refreshNearlyTell = new Intent();
        refreshNearlyTell
                .setAction(Constants.Action.ACTION_REFRESH_NEARLY_TELL);
        mContext.sendBroadcast(refreshNearlyTell);
        T.showShort(mContext, R.string.add_success);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        destroyTempHead();

        if (isCreatePassword) {
            Contact contact = FList.getInstance().isContact(
                    mSaveContact.contactId);
            if (!isSave && null == contact) {
                File file = new File(Constants.Image.USER_HEADER_PATH
                        + NpcCommon.mThreeNum + "/" + mSaveContact.contactId);
                Utils.deleteFile(file);
            }
        } else {
            if (!isSave) {
                File file = new File(Constants.Image.USER_HEADER_PATH
                        + NpcCommon.mThreeNum + "/" + mSaveContact.contactId);
                Utils.deleteFile(file);
            }
        }

        if (isRegFilter) {
            mContext.unregisterReceiver(mReceiver);
            isRegFilter = false;
        }
    }

    @Override
    public int getActivityInfo() {
        // TODO Auto-generated method stub
        return Constants.ActivityInfo.ACTIVITY_ADDCONTACTNEXTACTIVITY;
    }
}
