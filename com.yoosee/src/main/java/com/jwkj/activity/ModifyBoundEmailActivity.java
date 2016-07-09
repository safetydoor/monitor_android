package com.jwkj.activity;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Pattern;

import android.R.bool;
import android.R.integer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jwkj.data.Contact;
import com.jwkj.data.DataManager;
import com.jwkj.data.SharedPreferencesManager;
import com.jwkj.entity.Email;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.widget.NormalDialog;
import com.p2p.core.P2PHandler;
import com.yoosee.R;


public class ModifyBoundEmailActivity extends BaseActivity implements
        OnClickListener {
    private final static String TAG = "ModifyBoundEmailActivity";
    private final static int GET_TIMES = 5;
    Context mContext;
    Contact mContact;
    ImageView mBack, img_manual_set;
    Button mSave;
    EditText mEmail, mSend, mPassword;
    TextView tv_manual_set;
    LinearLayout l_manual_set, l_Automatic_set;
    EditText et_addressee, et_smpt_address, et_sender, et_mpassword, et_port;
    RadioButton radio_one, radio_two, radio_three;
    TextView tv_choosee_port;
    LinearLayout encryption;
    LinearLayout manual_set;
    LinearLayout l_btn_clear;
    NormalDialog dialog;
    private boolean isRegFilter = false;
    String email;
    String email_name;
    private TextView txSend, txPassword, txSendSelf;
    private CheckBox cbSendSelf;
    List<String> data_list;
    String sendEmail, emailRoot, emailPwd;
    private boolean isSendSelf = true;// 需求更改，值不会改变，一直为true
    private boolean isEmailLegal;
    private boolean isSurportSMTP;
    private boolean isEmailChecked;
    private LinearLayout llSendEmail, llPassword, llEmail, llEmailsmtp;
    private TextView txErrorTips;
    private TextView etSmtp;
    private int CheckedEmailTimes = 0;
    private Button btnClear, btnChecked;
    private boolean isNeedClearEmail = false;
    private boolean isProcessResult = false;// 是否处理结果
    boolean isManualSet = false;
    byte encryptType = 0;
    String senderEmail;
    int encrypt;
    int smtpport;
    String[] port_list = {"25(非加密)", "465(SSL)", "587(TLS)", "自定义"};
    boolean isGetEmail = false;
    boolean isSupportManual = false;
    int count_set = 0;

    boolean isManual = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_npc_bound_email);
        mContact = (Contact) getIntent().getSerializableExtra("contact");
        email_name = getIntent().getStringExtra("email");
        sendEmail = getIntent().getStringExtra("sendEmail");
        emailRoot = getIntent().getStringExtra("emailRoot");
        emailPwd = getIntent().getStringExtra("emailPwd");
        isEmailLegal = getIntent().getBooleanExtra("isEmailLegal", true);
        isSurportSMTP = getIntent().getBooleanExtra("isSurportSMTP", false);
        isEmailChecked = getIntent().getBooleanExtra("isEmailChecked", false);
        isSupportManual = getIntent().getBooleanExtra("isSupportManual", false);
        senderEmail = getIntent().getStringExtra("senderEmail");
        encrypt = getIntent().getIntExtra("encrypt", -1);
        smtpport = getIntent().getIntExtra("smtpport", -1);
        // encryptType=(byte)encrypt;
        mContext = this;
        initCompent();
        initEncrypt();
        initData();
        regFilter();
    }

    public void initCompent() {
        mBack = (ImageView) findViewById(R.id.back_btn);
        mSave = (Button) findViewById(R.id.save);
        mEmail = (EditText) findViewById(R.id.email);
        mSend = (EditText) findViewById(R.id.ed_sendemail);
        etSmtp = (TextView) findViewById(R.id.et_smtp);
        btnClear = (Button) findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(this);
        btnChecked = (Button) findViewById(R.id.btn_checked);
        tv_manual_set = (TextView) findViewById(R.id.tv_manual_set);
        img_manual_set = (ImageView) findViewById(R.id.img_manual_set);
        l_Automatic_set = (LinearLayout) findViewById(R.id.l_Automatic_set);
        l_manual_set = (LinearLayout) findViewById(R.id.l_manual_set);
        et_addressee = (EditText) findViewById(R.id.et_addressee);
        et_smpt_address = (EditText) findViewById(R.id.et_smpt_address);
        et_sender = (EditText) findViewById(R.id.et_sender);
        et_mpassword = (EditText) findViewById(R.id.et_mpassword);
        et_port = (EditText) findViewById(R.id.et_port);
        radio_one = (RadioButton) findViewById(R.id.radio_one);
        radio_two = (RadioButton) findViewById(R.id.radio_two);
        radio_three = (RadioButton) findViewById(R.id.radio_three);
        tv_choosee_port = (TextView) findViewById(R.id.tv_choosee_port);
        encryption = (LinearLayout) findViewById(R.id.encryption);
        txErrorTips = (TextView) findViewById(R.id.tx_error_tips);
        manual_set = (LinearLayout) findViewById(R.id.manual_set);
        l_btn_clear = (LinearLayout) findViewById(R.id.l_btn_clear);
        mPassword = (EditText) findViewById(R.id.et_password);
        mPassword.setTransformationMethod(PasswordTransformationMethod
                .getInstance());
        et_sender.setText(senderEmail);
        mPassword.setText(emailPwd);
        et_mpassword.setText(emailPwd);
        if (sendEmail.length() <= 0 || sendEmail.equals("0")
                || sendEmail.split("@")[0].equals("0")) {
            if (!email_name.equals("Unbound") && (!email_name.equals("未绑定"))
                    && (!email_name.equals("未綁定"))) {
                mSend.setText(email_name.substring(0,
                        email_name.lastIndexOf("@")));
                etSmtp.setText(email_name.substring(email_name.lastIndexOf("@")));
                isNeedClearEmail = false;
            } else {
                mSend.setText("");
                etSmtp.setText(Email.getInstence().getUIEmail(emailRoot));
                isNeedClearEmail = true;
            }
        } else {
            et_addressee.setText(email_name);
            et_port.setText(String.valueOf(smtpport));
            if (smtpport == 25 || smtpport == 465 || smtpport == 587) {
                encryption.setVisibility(LinearLayout.GONE);
            }
            String[] emails = sendEmail.split(",");
            if (emails.length > 1 || !email_name.equals(senderEmail)
                    && isSurportSMTP) {
                mSend.setText("");
                mPassword.setText("");
                txErrorTips.setVisibility(TextView.GONE);
                isNeedClearEmail = true;
                isManualSet = true;
                isManual = true;
                showManualSet();
            } else {
                mSend.setText(sendEmail.substring(0, sendEmail.lastIndexOf("@")));
                isNeedClearEmail = true;
                if (emailRoot != null) {
                    etSmtp.setText(email_name.substring(email_name
                            .lastIndexOf("@")));
                }
                isManual = false;
                showAutomaticSet();
            }
        }
        mSend.setSelection(mSend.getText().length());
        txSend = (TextView) findViewById(R.id.tx_send);
        txPassword = (TextView) findViewById(R.id.tx_password);
        txSendSelf = (TextView) findViewById(R.id.tx_sen_self);
        cbSendSelf = (CheckBox) findViewById(R.id.cb_sen_self);
        llEmailsmtp = (LinearLayout) findViewById(R.id.ll_emial_smtp);
        // isSendSelf = SharedPreferencesManager.getInstance()
        // .getIsSendemailToSelf(mContext);
        cbSendSelf.setChecked(isSendSelf);
        mBack.setOnClickListener(this);
        mSave.setOnClickListener(this);
        // tv_manual_set.setOnClickListener(this);
        // img_manual_set.setOnClickListener(this);
        radio_one.setOnClickListener(this);
        radio_two.setOnClickListener(this);
        radio_three.setOnClickListener(this);
        manual_set.setOnClickListener(this);

        llSendEmail = (LinearLayout) findViewById(R.id.ll_sendemail);
        llPassword = (LinearLayout) findViewById(R.id.layout_password);
        llEmail = (LinearLayout) findViewById(R.id.layout_cNumber);
        et_smpt_address.setText(emailRoot);
        if (!email_name.equals("Unbound") && (!email_name.equals("未绑定"))
                && (!email_name.equals("未綁定"))) {
            mEmail.setText(email_name);
            mEmail.setSelection(email_name.length());
            isNeedClearEmail = true;
        } else {
            isNeedClearEmail = false;
        }
        if (isSurportSMTP) {
            llEmail.setVisibility(View.GONE);
            llPassword.setVisibility(View.VISIBLE);
            llSendEmail.setVisibility(View.VISIBLE);
            if (isSupportManual) {
                manual_set.setVisibility(View.VISIBLE);
            } else {
                manual_set.setVisibility(View.GONE);
            }
            if (isNeedClearEmail) {
                l_btn_clear.setVisibility(View.VISIBLE);
            } else {
                l_btn_clear.setVisibility(View.GONE);
            }
            changeTextTips();
        } else {
            llEmail.setVisibility(View.VISIBLE);
            llPassword.setVisibility(View.GONE);
            llSendEmail.setVisibility(View.GONE);
            btnClear.setVisibility(View.GONE);
            manual_set.setVisibility(View.GONE);
        }
        initPopwindow();
        initPortPopwindow();
    }

    public void initEncrypt() {
        if (encrypt == 0) {
            radio_one.setChecked(true);
            radio_two.setChecked(false);
            radio_three.setChecked(false);
            encryptType = 0;
        } else if (encrypt == 1) {
            radio_one.setChecked(false);
            radio_two.setChecked(true);
            radio_three.setChecked(false);
            encryptType = 1;
        } else if (encrypt == 2) {
            radio_one.setChecked(false);
            radio_two.setChecked(false);
            radio_three.setChecked(true);
            encryptType = 2;
        } else {
            radio_one.setChecked(true);
            radio_two.setChecked(false);
            radio_three.setChecked(false);
        }
        if (smtpport == 25) {
            encryptType = 0;
        } else if (smtpport == 465) {
            encryptType = 1;
        } else if (smtpport == 587) {
            encryptType = 2;
        }
    }

    void changeTextTips() {
        if (isEmailChecked) {
            if (isEmailLegal) {
                // 邮箱可用
                txErrorTips.setVisibility(View.GONE);
                btnChecked.setVisibility(View.GONE);
            } else {
                // 邮箱不可用
                if (isManual) {
                    txErrorTips.setText(R.string.smtp_or_password_error);
                } else {
                    txErrorTips.setText(R.string.email_error_tips);
                }
                txErrorTips.setVisibility(View.VISIBLE);
                // btnChecked.setVisibility(View.VISIBLE);
            }
        } else {
            txErrorTips.setText(R.string.email_notcheck_smtp);
            txErrorTips.setVisibility(View.VISIBLE);
            // btnChecked.setVisibility(View.VISIBLE);
        }
    }

    void initPopwindow() {
        data_list = Email.getInstence().getUIEmailList();
        llEmailsmtp.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        showPopwindow(v, 0, 0, 0);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private PopupWindow popupWindow;
    private ListView lvEmail;
    private MySpinnerAdapter arr_adapter = new MySpinnerAdapter();

    void showPopwindow(View parent, int x, int y, int postion) {
        View popView = LayoutInflater.from(mContext).inflate(
                R.layout.pop_email_smtp, null);
        popupWindow = new PopupWindow(popView, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(parent.getMeasuredWidth());
        popupWindow.setHeight((int) (parent.getMeasuredHeight() * 4));
        lvEmail = (ListView) popView.findViewById(R.id.lv_email_smtp);
        lvEmail.setAdapter(arr_adapter);
        lvEmail.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // if (position == data_list.size() - 1) {
                // // 是系统邮箱发件人不可编辑，发给自己按钮不可点击，并且在给自己按钮
                // isSendSelf = false;
                // cbSendSelf.setEnabled(false);
                // cbSendSelf.setChecked(false);
                // mSend.setText("");
                // mSend.setEnabled(false);
                // mPassword.setText("");
                // mPassword.setEnabled(false);
                // TextEnable(true);
                // } else {
                // 不是系统邮箱，在发给自己按钮选中时同步更改收件人
                cbSendSelf.setEnabled(true);
                mSend.setEnabled(true);
                mPassword.setEnabled(true);
                TextEnable(false);
                // }
                setReseveEmail();
                etSmtp.setText((String) (parent.getAdapter().getItem(position)));
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        // popupWindow.setAnimationStyle(R.style.PopupAnimation);
        // 设置popwindow显示位置
        // popupWindow.showAtLocation(parent, 0, x, y);
        popupWindow.showAsDropDown(parent);
        // 获取popwindow焦点
        popupWindow.setFocusable(true);
        // 设置popwindow如果点击外面区域，便关闭。
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
    }

    public void initPortPopwindow() {
        tv_choosee_port.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        showPortPopwindow(v, 0, 0, 0);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private PopupWindow port_popupWindow;
    private ListView lvPort;
    private MySpinnerPortAdapter port_adapter = new MySpinnerPortAdapter();

    public void showPortPopwindow(View parent, int x, int y, int postion) {
        View popView = LayoutInflater.from(mContext).inflate(
                R.layout.pop_email_smtp, null);
        port_popupWindow = new PopupWindow(popView, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        port_popupWindow.setWidth(parent.getMeasuredWidth());
        port_popupWindow.setHeight((int) (parent.getMeasuredHeight() * 4));
        lvPort = (ListView) popView.findViewById(R.id.lv_email_smtp);
        lvPort.setAdapter(port_adapter);
        lvPort.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String p = (String) (parent.getAdapter().getItem(position));
                if (position < 3) {
                    encryptType = (byte) position;
                }
                if (position == 3) {
                    et_port.setText("");
                    encryption.setVisibility(LinearLayout.VISIBLE);
                    et_port.requestFocus();
                } else {
                    et_port.setText(p.substring(0, p.indexOf("(")));
                    encryption.setVisibility(LinearLayout.GONE);
                    et_port.setSelection(p.substring(0, p.indexOf("("))
                            .length());
                    et_mpassword.requestFocus();
                }
                if (port_popupWindow.isShowing()) {
                    port_popupWindow.dismiss();
                }
            }
        });
        port_popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        // popupWindow.setAnimationStyle(R.style.PopupAnimation);
        // 设置popwindow显示位置
        // popupWindow.showAtLocation(parent, 0, x, y);
        port_popupWindow.showAsDropDown(parent);
        // 获取popwindow焦点
        port_popupWindow.setFocusable(true);
        // 设置popwindow如果点击外面区域，便关闭。
        port_popupWindow.setOutsideTouchable(true);
        port_popupWindow.update();
    }

    private void initData() {
        cbSendSelf.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                isSendSelf = isChecked;
                // 收件人邮箱要联动且设置为不可编辑
                setReseveEmail();
                mEmail.setEnabled(!isSendSelf);
            }
        });

        mSend.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!isEmailLegal) {
                    if (s.length() >= 1) {
                        txErrorTips.setVisibility(View.GONE);
                    } else {
                        txErrorTips.setVisibility(View.VISIBLE);
                    }
                }
                setReseveEmail();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    void TextEnable(boolean isGray) {
        if (isGray) {
            txSend.setTextColor(Utils
                    .getColorByResouse(R.color.text_color_gray));
            txPassword.setTextColor(Utils
                    .getColorByResouse(R.color.text_color_gray));
            txSendSelf.setTextColor(Utils
                    .getColorByResouse(R.color.text_color_gray));
        } else {
            txSend.setTextColor(Utils.getColorByResouse(R.color.black));
            txPassword.setTextColor(Utils.getColorByResouse(R.color.black));
            txSendSelf.setTextColor(Utils.getColorByResouse(R.color.black));
        }

    }

    void setReseveEmail() {
        if (isSendSelf) {
            Editable senemail = mSend.getText();
            mEmail.setText(senemail);
            mEmail.append(etSmtp.getText());
        } else {
            mEmail.setEnabled(true);
        }
    }

    class MySpinnerAdapter extends BaseAdapter {

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            final ViewHolder holder;
            if (null == convertView) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.spinner_item, null);
                // convertView = LayoutInflater.from(mContext).inflate(
                // android.R.layout.simple_spinner_item, null);
                holder = new ViewHolder();
                holder.txEmail = (TextView) convertView
                        .findViewById(R.id.tx_emails);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txEmail.setText(data_list.get(position));
            return convertView;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return data_list.size();
        }

        @Override
        public String getItem(int position) {
            // TODO Auto-generated method stub
            return data_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (null == convertView) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.spinner_item, null);
                holder = new ViewHolder();
                holder.txEmail = (TextView) convertView
                        .findViewById(R.id.tx_emails);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txEmail.setText(data_list.get(position));
            return convertView;
        }

        class ViewHolder {
            public TextView txEmail;
        }

    }

    class MySpinnerPortAdapter extends BaseAdapter {

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            final ViewHolder holder;
            if (null == convertView) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.spinner_item, null);
                // convertView = LayoutInflater.from(mContext).inflate(
                // android.R.layout.simple_spinner_item, null);
                holder = new ViewHolder();
                holder.txEmail = (TextView) convertView
                        .findViewById(R.id.tx_emails);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txEmail.setText(port_list[position]);
            return convertView;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return port_list.length;
        }

        @Override
        public String getItem(int position) {
            // TODO Auto-generated method stub
            return port_list[position];
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (null == convertView) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.spinner_item, null);
                holder = new ViewHolder();
                holder.txEmail = (TextView) convertView
                        .findViewById(R.id.tx_emails);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txEmail.setText(port_list[position]);
            return convertView;
        }

        class ViewHolder {
            public TextView txEmail;
        }

    }

    public void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.P2P.ACK_RET_SET_ALARM_EMAIL);
        filter.addAction(Constants.P2P.RET_SET_ALARM_EMAIL);
        filter.addAction(Constants.P2P.RET_GET_ALARM_EMAIL_WITHSMTP);
        filter.addAction(Constants.P2P.ACK_RET_GET_ALARM_EMAIL);
        filter.addAction(Constants.P2P.ACK_RET_GET_DEVICE_INFO);
        filter.addAction(Constants.P2P.RET_GET_DEVICE_INFO);
        mContext.registerReceiver(mReceiver, filter);
        isRegFilter = true;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            if (!isProcessResult) {
                return;
            }
            if (intent.getAction().equals(Constants.P2P.RET_SET_ALARM_EMAIL)) {
                int result = intent.getIntExtra("result", -1);
                Log.i("dxsemail", "result-->" + result);
                if ((result & (1 << 0)) == Constants.P2P_SET.ALARM_EMAIL_SET.SETTING_SUCCESS) {
                    if (isSurportSMTP) {
                        CheckedEmailTimes++;
                        delayGetAlarmEmial();
                    } else {
                        if (null != dialog && dialog.isShowing()) {
                            dialog.dismiss();
                            dialog = null;
                        }
                        getSMTPMessage(result);
                    }

                } else if (result == -1) {
                    T.showShort(mContext, R.string.operator_error);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_RET_SET_ALARM_EMAIL)) {
                int result = intent.getIntExtra("result", -1);

                if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                    if (null != dialog && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Intent i = new Intent();
                    i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);
                    finish();
                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    if (null != dialog && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    T.showShort(mContext, R.string.net_error_operator_fault);
                    isGetEmail = false;
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_GET_ALARM_EMAIL_WITHSMTP)) {
                isGetEmail = false;
                String contectid = intent.getStringExtra("contectid");
                if (contectid != null && contectid.equals(mContact.contactId)) {
                    String email = intent.getStringExtra("email");
                    String[] SmptMessage = intent
                            .getStringArrayExtra("SmptMessage");
                    int result = intent.getIntExtra("result", 0);
                    getSMTPMessage(result);
                }
            } else if (intent.getAction().equalsIgnoreCase(
                    Constants.P2P.ACK_RET_GET_ALARM_EMAIL)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                    Intent i = new Intent();
                    i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);
                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    P2PHandler.getInstance().getAlarmEmail(mContact.contactId,
                            mContact.contactPassword);
                }
            }
        }
    };

    private void getSMTPMessage(int result) {
        if ((byte) ((result >> 1) & (0x1)) == 0) {
            isSurportSMTP = false;
            T.showShort(mContext, R.string.modify_success);
            SharedPreferencesManager.getInstance().putIsSendemailToSelf(
                    mContext, isSendSelf);
            finish();
        } else {
            isSurportSMTP = true;
            if ((byte) ((result >> 4) & (0x1)) == 0) {// 验证通过
                isEmailChecked = true;
                if (null != dialog && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
                if ((byte) ((result >> 2) & (0x1)) == 0) {
                    isEmailLegal = false;
                    if (isManual) {
                        T.showLong(mContext, R.string.smtp_or_password_error);
                    } else {
                        T.showLong(mContext, R.string.email_error_tips);
                    }
                } else {
                    if ((byte) ((result >> 3) & (0x1)) == 0) {
                        isEmailLegal = true;
                        T.showShort(mContext, R.string.modify_success);
                        SharedPreferencesManager.getInstance()
                                .putIsSendemailToSelf(mContext, isSendSelf);
                        finish();
                    } else {// 格式错误
                        T.showShort(mContext, R.string.email_format_error);
                    }

                }
                changeTextTips();
            } else {
                isEmailChecked = false;
                if (CheckedEmailTimes < GET_TIMES) {
                    CheckedEmailTimes++;
                    delayGetAlarmEmial();
                } else {
                    changeTextTips();
                    if (null != dialog && dialog.isShowing()) {
                        dialog.dismiss();
                        dialog = null;
                    }
                    T.showLong(mContext, R.string.email_notcheck_smtp);
                }
            }

        }
    }

    private Handler getAlarmEmailHandler = new Handler();
    private Runnable runable = new Runnable() {
        public void run() {
            P2PHandler.getInstance().getAlarmEmail(mContact.contactId,
                    mContact.contactPassword);
        }
    };

    void delayGetAlarmEmial() {
        getAlarmEmailHandler.postDelayed(runable, 3000);
        Log.i("dxsSMTP", "第" + CheckedEmailTimes + "次请求");
    }

    public Handler myHandler = new Handler();
    public Runnable myRunnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (isGetEmail) {
                P2PHandler.getInstance().getAlarmEmail(mContact.contactId,
                        mContact.contactPassword);
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        isProcessResult = true;
        int resId = view.getId();
        if (resId == R.id.back_btn) {
            Utils.hindKeyBoard(view);
            finish();
        } else if (resId == R.id.btn_clear) {
            // 清除邮箱
            if (email_name.equals("") || email_name.equals("Unbound")
                    || email_name.equals("未绑定") || email_name.equals("未綁定")) {
                T.showShort(mContext, R.string.no_bind_email);
                return;
            }
            showClearEmail();
        } else if (resId == R.id.save) {
            CheckedEmailTimes = 0;
            count_set++;
            Utils.hindKeyBoard(view);
            if (!isManualSet) {
                isManual = false;
                if (isSurportSMTP) {
                    saveEmailWithEMTP();
                } else {
                    saveEmail();
                }
            } else {
                isManual = true;
                saveManualSetEmail();
            }
        } else if (resId == R.id.tv_manual_set) {
        } else if (resId == R.id.img_manual_set) {
        } else if (resId == R.id.radio_one) {
            // 不加密
            encryptType = 0;
        } else if (resId == R.id.radio_two) {
            // SSL加密
            encryptType = 1;
        } else if (resId == R.id.radio_three) {
            // TLS加密
            encryptType = 2;
        } else if (resId == R.id.manual_set) {
            changeSet();
        }
    }

    Handler timeoutHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            count_set--;
            if (isGetEmail && count_set == 0) {
                T.showShort(mContext, R.string.time_out);
                finish();
            }
        }

    };

    public void saveManualSetEmail() {
        String addressee = et_addressee.getText().toString().trim()
                .replaceAll(" ", "");
        String smtpAddress = et_smpt_address.getText().toString().trim()
                .replaceAll(" ", "");
        String sender = et_sender.getText().toString().trim()
                .replaceAll(" ", "");
        String emailPwd = et_mpassword.getText().toString().trim();
        String port_number = et_port.getText().toString().trim();
        int port = 0;
        if (addressee == null || addressee.equals("")) {
            T.showShort(mContext, R.string.addressee_not_empty);
            return;
        }
        if (addressee.charAt(addressee.length() - 1) == ','
                || addressee.charAt(addressee.length() - 1) == '，') {
            addressee = addressee.substring(0, addressee.length() - 1);
        }
        String[] addressees = addressee.split(",|，");
        if (addressees.length > 3) {
            T.showShort(mContext, R.string.addressee_to_much);
            return;
        }
        if (!isEmail(addressee)) {
            T.showShort(mContext, R.string.addressee_email_error);
            return;
        }
        if (sender == null || sender.equals("")) {
            T.showShort(mContext, R.string.sender_not_empty);
            return;
        }
        if (sender.charAt(sender.length() - 1) == ','
                || sender.charAt(sender.length() - 1) == '，') {
            sender = sender.substring(0, sender.length() - 1);
        }
        String[] senders = sender.split(",|，");
        if (senders.length > 1) {
            T.showShort(mContext, R.string.sender_one);
            return;
        }
        if (!Utils.isEmial(sender)) {
            T.showShort(mContext, R.string.sender_email_error);
            return;
        }
        if (smtpAddress == null || smtpAddress.equals("")) {
            T.showShort(mContext, R.string.smtp_not_empty);
            return;
        }
        if (smtpAddress.charAt(smtpAddress.length() - 1) == ','
                || smtpAddress.charAt(smtpAddress.length() - 1) == '，') {
            smtpAddress = smtpAddress.substring(0, smtpAddress.length() - 1);
        }
        String[] smtps = smtpAddress.split(",");
        if (smtps.length > 5) {
            T.showShort(mContext, R.string.smtp_addresses_to_much);
            return;
        }
        if (!checkSmtpAddress(smtpAddress)) {
            T.showShort(mContext, R.string.smtp_addresses_error);
            return;
        }
        if (port_number != null && !port_number.equals("")) {
            port = Integer.parseInt(port_number);
        } else {
            T.showShort(mContext, R.string.port_not_empty);
            return;
        }
        if (port < 1 || port > 65535) {
            T.showShort(mContext, R.string.port_between);
            return;
        }
        if (emailPwd == null || emailPwd.equals("")) {
            T.showShort(mContext, R.string.input_password);
            return;
        }

        if (null == dialog) {
            dialog = new NormalDialog(this, this.getResources().getString(
                    R.string.verification), "", "", "");
            dialog.setStyle(NormalDialog.DIALOG_STYLE_LOADING);
        }
        try {
            String subject = new String(Email.subject.getBytes(), "UTF-8");
            String countent = new String(Email.countent.getBytes(), "UTF-8");
            dialog.setOnCancelListener(dialogCancelListner);
            dialog.showDialog();
            dialog.setCanceledOnTouchOutside(false);
            Log.e("sendmanual", "addressee=" + addressee + "--" + "port="
                    + port + "--" + "smtpAddress=" + smtpAddress + "sender="
                    + sender + "--" + smtpAddress + "--" + "emailPwd="
                    + emailPwd + "--" + "subject=" + subject + "--"
                    + "countent=" + countent + "--" + "encryptType="
                    + encryptType);
            isGetEmail = true;
            P2PHandler.getInstance().setAlarmEmailWithSMTP(mContact.contactId,
                    mContact.contactPassword, (byte) 3, addressee, port,
                    smtpAddress, sender, emailPwd, subject, countent,
                    encryptType, (byte) 0, 0);
//			myHandler.postDelayed(myRunnable, 2000);
            Message m = new Message();
            int timeOut = 7000 + (smtps.length - 1) * 4000;
            Log.e("timeout", timeOut + "--");
            timeoutHandler.sendMessageDelayed(m, timeOut);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public boolean checkSmtpAddress(String smtp) {
        String[] smtps = smtp.split(",|，");
        for (int i = 0; i < smtps.length; i++) {
            String[] ips = smtps[i].split("\\.");
            boolean isIp = true;
            for (int j = 0; j < ips.length; j++) {
                if (!Utils.isNumeric(ips[j])) {
                    isIp = false;
                    break;
                }
            }
            if (isIp) {
                if (!checkIP(smtps[i])) {
                    return false;
                }
            }
        }
        return true;
    }

    // 判断ip地址是否合法
    public boolean checkIP(String str) {
        Pattern pattern = Pattern
                .compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]"
                        + "|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$");
        return pattern.matcher(str).matches();
    }

    public boolean isEmail(String email) {
        String[] emails = email.split(",|，");
        for (int i = 0; i < emails.length; i++) {
            if (!Utils.isEmial(emails[i])) {
                return false;
            }
        }
        return true;
    }

    public void changeSet() {
        if (isManualSet) {
            img_manual_set.setImageResource(R.drawable.check_off);
            l_Automatic_set.setVisibility(LinearLayout.VISIBLE);
            l_manual_set.setVisibility(LinearLayout.GONE);
            isManualSet = false;
        } else {
            img_manual_set.setImageResource(R.drawable.check_on);
            l_Automatic_set.setVisibility(LinearLayout.GONE);
            l_manual_set.setVisibility(LinearLayout.VISIBLE);
            isManualSet = true;
        }
    }

    void showClearEmail() {
        NormalDialog dialog = new NormalDialog(mContext, mContext
                .getResources().getString(R.string.unbind), mContext
                .getResources().getString(R.string.unbind)
                + " "
                + email_name
                + "?", mContext.getResources().getString(R.string.confirm),
                mContext.getResources().getString(R.string.cancel));
        dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {

            @Override
            public void onClick() {
                ClearEmail(false);
            }
        });
        dialog.showDialog();
    }

    void ClearEmail(boolean isOldDevice) {
        if (null == dialog) {
            dialog = new NormalDialog(this, this.getResources().getString(
                    R.string.verification), "", "", "");
            dialog.setStyle(NormalDialog.DIALOG_STYLE_LOADING);
        }
        dialog.setOnCancelListener(dialogCancelListner);
        dialog.showDialog();
        dialog.setCanceledOnTouchOutside(false);
        if (isOldDevice) {
            P2PHandler.getInstance().setAlarmEmailWithSMTP(mContact.contactId,
                    mContact.contactPassword, (byte) 3, "0", 0, "", "0", "", "", "", (byte) 0, (byte) 0, 0);
        } else {
            P2PHandler.getInstance().setAlarmEmailWithSMTP(mContact.contactId,
                    mContact.contactPassword, (byte) 3, "0", 11, "11", "11", "11", "11", "11", (byte) 11, (byte) 0, 0);
        }
    }

    private OnCancelListener dialogCancelListner = new OnCancelListener() {

        @Override
        public void onCancel(DialogInterface dialog) {
            CheckedEmailTimes = GET_TIMES;
            isProcessResult = false;
            if (getAlarmEmailHandler != null && runable != null) {
                getAlarmEmailHandler.removeCallbacks(runable);
            }
            Log.i("dxsSMTP", "CheckedEmailTimes-->" + CheckedEmailTimes);
        }
    };

    private void saveEmailWithEMTP() {
        String sendEmails = mSend.getText().toString();// 发件人
        String emailRoot = etSmtp.getText().toString();// 后缀
        String pwd = mPassword.getText().toString().trim();

        // if (sendEmails.trim().length() <= 0 && pwd.trim().length() <=
        // 0&&Email.getInstence().isSurportThisSMTP(emailRoot)) {
        // sendEmails = "0";
        // ClearEmail();
        // return;
        // }
        if (sendEmails.trim().length() <= 0) {
            T.show(mContext, R.string.input_email, 2000);
            return;
        }
        if (!Email.getInstence().isSurportThisSMTP(emailRoot)) {
            T.show(mContext, String.format(
                    getString(R.string.email_notsurpport_smtp), emailRoot),
                    2000);
            return;
        }
        if (("".equals(pwd.trim()))) {
            T.show(mContext, R.string.inputpassword, 2000);
            return;
        }
        if (sendEmails.length() > 32
                || sendEmails.length() < 3) {
            T.showShort(this, R.string.email_too_long);
            return;
        }
        if (null == dialog) {
            dialog = new NormalDialog(this, this.getResources().getString(
                    R.string.verification), "", "", "");
            dialog.setStyle(NormalDialog.DIALOG_STYLE_LOADING);
        }

        dialog.setOnCancelListener(dialogCancelListner);
        dialog.showDialog();
        dialog.setCanceledOnTouchOutside(false);
        String[] emailMessage = Email.getInstence().getEmailMessage(emailRoot);
        byte encrypt = Email.getInstence().getEncryptByPort(
                Integer.parseInt(emailMessage[2]));
        isGetEmail = true;
        P2PHandler.getInstance().setAlarmEmailWithSMTP(mContact.contactId,
                mContact.contactPassword, (byte) 3,
                sendEmails + emailMessage[0],
                Integer.parseInt(emailMessage[2]), emailMessage[1],
                sendEmails + emailMessage[0], pwd, emailMessage[3],
                emailMessage[4], encrypt, (byte) 0, 0);
//		P2PHandler.getInstance().setAlarmEmailWithSMTP(mContact.contactId,
//				mContact.contactPassword, (byte)3, "0", 0, "", "0", "", "", "",(byte)0,(byte)0,0);
//		myHandler.postDelayed(myRunnable, 2000);
//		Message m = new Message();
//		timeoutHandler.sendMessageDelayed(m, 5000);
        Log.e("encrypt", "encrypt=" + encrypt);
    }

    private void saveEmail() {
        email = mEmail.getText().toString();
        if ("".equals(email.trim())) {
            ClearEmail(true);
            return;
        }
        if (!Utils.isEmial(email)) {
            T.showShort(this, R.string.email_format_error);
            return;
        }
        if (email.length() > 32 || email.length() < 3) {
            T.showShort(this, R.string.email_too_long);
            return;
        }
        if (null == dialog) {
            dialog = new NormalDialog(this, this.getResources().getString(
                    R.string.verification), "", "", "");
            dialog.setStyle(NormalDialog.DIALOG_STYLE_LOADING);

        }
        dialog.setOnCancelListener(dialogCancelListner);
        dialog.showDialog();
        dialog.setCanceledOnTouchOutside(true);
        isGetEmail = true;
        P2PHandler.getInstance().setAlarmEmailWithSMTP(mContact.contactId,
                mContact.contactPassword, (byte) 0, email, 0, "", "", "", "",
                "", (byte) 0, (byte) 0, 0);
//		myHandler.postDelayed(myRunnable, 2000);
//		Message m = new Message();
//		timeoutHandler.sendMessageDelayed(m, 7000);

    }

    public void showManualSet() {
        l_manual_set.setVisibility(LinearLayout.VISIBLE);
        l_Automatic_set.setVisibility(LinearLayout.GONE);
        img_manual_set.setImageResource(R.drawable.check_on);
        isManualSet = true;
    }

    public void showAutomaticSet() {
        l_manual_set.setVisibility(LinearLayout.GONE);
        l_Automatic_set.setVisibility(LinearLayout.VISIBLE);
        img_manual_set.setImageResource(R.drawable.check_off);
        isManualSet = false;
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (isRegFilter) {
            mContext.unregisterReceiver(mReceiver);
            isRegFilter = false;
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public int getActivityInfo() {
        return Constants.ActivityInfo.ACTIVITY_MODIFYBOUNDEMAILACTIVITY;
    }

}
