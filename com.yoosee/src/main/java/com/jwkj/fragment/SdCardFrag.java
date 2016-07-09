package com.jwkj.fragment;

import com.yoosee.R;
import com.jwkj.activity.MainControlActivity;
import com.jwkj.data.Contact;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.utils.T;
import com.jwkj.widget.NormalDialog;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SdCardFrag extends Fragment implements OnClickListener {
    private Context mContext;
    private Contact contact;
    RelativeLayout sd_format, usb_capacity, usb_remainning_capacity;
    String command;
    boolean isRegFilter = false;
    TextView tv_total_capacity, tv_sd_remainning_capacity;
    TextView tv_usb_total_capacity, tv_usb_remainning_capacity;
    ImageView format_icon;
    ProgressBar progress_format;
    int SDcardId;
    int sdId;
    int usbId;
    boolean isSDCard;
    int count = 0;
    String idOrIp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        contact = (Contact) getArguments().getSerializable("contact");
        idOrIp = contact.contactId;
        if (contact.ipadressAddress != null) {
            String mark = contact.ipadressAddress.getHostAddress();
            String ip = mark.substring(mark.lastIndexOf(".") + 1, mark.length());
            if (!ip.equals("") && ip != null) {
                idOrIp = ip;
            }
        }
        View view = inflater.inflate(R.layout.fragment_sd_card, container, false);
        initComponent(view);
        showSDProgress();
        regFilter();
        command = createCommand("80", "0", "00");
        Log.e("sdcapacity", command);
        Log.e("callId", "idOrIp=" + idOrIp);
        P2PHandler.getInstance().getSdCardCapacity(idOrIp, contact.contactPassword, command);
        return view;
    }

    public void initComponent(View view) {
        tv_total_capacity = (TextView) view.findViewById(R.id.tv_sd_capacity);
        tv_sd_remainning_capacity = (TextView) view
                .findViewById(R.id.tv_sd_remainning_capacity);
        sd_format = (RelativeLayout) view.findViewById(R.id.sd_format);
        format_icon = (ImageView) view.findViewById(R.id.format_icon);
        progress_format = (ProgressBar) view.findViewById(R.id.progress_format);
        usb_capacity = (RelativeLayout) view.findViewById(R.id.usb_capacity);
        usb_remainning_capacity = (RelativeLayout) view
                .findViewById(R.id.usb_remainning_capacity);
        tv_usb_total_capacity = (TextView) view
                .findViewById(R.id.tv_usb_capacity);
        tv_usb_remainning_capacity = (TextView) view
                .findViewById(R.id.tv_usb_remainning_capacity);
        sd_format.setOnClickListener(this);
        if (contact.contactType == P2PValue.DeviceType.NPC) {
            sd_format.setVisibility(RelativeLayout.GONE);
            usb_capacity.setVisibility(RelativeLayout.VISIBLE);
            usb_remainning_capacity.setVisibility(RelativeLayout.VISIBLE);
        }
    }

    public void regFilter() {
        isRegFilter = true;
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.P2P.ACK_GET_SD_CARD_CAPACITY);
        filter.addAction(Constants.P2P.RET_GET_SD_CARD_CAPACITY);
        filter.addAction(Constants.P2P.ACK_GET_SD_CARD_FORMAT);
        filter.addAction(Constants.P2P.RET_GET_SD_CARD_FORMAT);
        filter.addAction(Constants.P2P.RET_GET_USB_CAPACITY);
        filter.addAction(Constants.P2P.RET_DEVICE_NOT_SUPPORT);
        mContext.registerReceiver(br, filter);
    }

    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(
                    Constants.P2P.ACK_GET_SD_CARD_CAPACITY)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {

                    Intent i = new Intent();
                    i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);

                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my", "net error resend:get npc time");
                    P2PHandler.getInstance().getSdCardCapacity(idOrIp,
                            contact.contactPassword, command);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_GET_SD_CARD_CAPACITY)) {
                int total_capacity = intent.getIntExtra("total_capacity", -1);
                int remain_capacity = intent.getIntExtra("remain_capacity", -1);
                int state = intent.getIntExtra("state", -1);
                SDcardId = intent.getIntExtra("SDcardID", -1);
                String id = Integer.toBinaryString(SDcardId);
                Log.e("id", "msga" + id);
                while (id.length() < 8) {
                    id = "0" + id;
                }
                char index = id.charAt(3);
                Log.e("id", "msgb" + id);
                Log.e("id", "msgc" + index);
                if (state == 1) {
                    if (index == '1') {
                        sdId = SDcardId;
                        tv_total_capacity.setText(String
                                .valueOf(total_capacity) + "M");
                        tv_sd_remainning_capacity.setText(String
                                .valueOf(remain_capacity) + "M");
                        showSDImg();
                    } else if (index == '0') {
                        usbId = SDcardId;
                        tv_usb_total_capacity.setText(String
                                .valueOf(total_capacity) + "M");
                        tv_usb_remainning_capacity.setText(String
                                .valueOf(remain_capacity) + "M");
                    }
                } else {
                    // count++;
                    // if(contact.contactType==P2PValue.DeviceType.IPC){
                    // if(count==1){
                    // Intent back = new Intent();
                    // back.setAction(Constants.Action.REPLACE_MAIN_CONTROL);
                    // mContext.sendBroadcast(back);
                    // T.showShort(mContext,R.string.sd_no_exist);
                    // }
                    // }else{
                    // if(count==2){
                    // Intent back = new Intent();
                    // back.setAction(Constants.Action.REPLACE_MAIN_CONTROL);
                    // mContext.sendBroadcast(back);
                    // T.showShort(mContext,R.string.sd_no_exist);
                    // }
                    // }
                    Intent back = new Intent();
                    back.setAction(Constants.Action.REPLACE_MAIN_CONTROL);
                    mContext.sendBroadcast(back);
                    T.showShort(mContext, R.string.sd_no_exist);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_GET_SD_CARD_FORMAT)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {

                    Intent i = new Intent();
                    i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);

                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my", "net error resend:get npc time");
                    P2PHandler.getInstance().setSdFormat(idOrIp,
                            contact.contactPassword, sdId);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_GET_SD_CARD_FORMAT)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.SD_FORMAT.SD_CARD_SUCCESS) {
                    T.showShort(mContext, R.string.sd_format_success);
                } else if (result == Constants.P2P_SET.SD_FORMAT.SD_CARD_FAIL) {
                    T.showShort(mContext, R.string.sd_format_fail);
                } else if (result == Constants.P2P_SET.SD_FORMAT.SD_NO_EXIST) {
                    T.showShort(mContext, R.string.sd_no_exist);
                }
                showSDImg();
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_GET_USB_CAPACITY)) {
                int total_capacity = intent.getIntExtra("total_capacity", -1);
                int remain_capacity = intent.getIntExtra("remain_capacity", -1);
                int state = intent.getIntExtra("state", -1);
                SDcardId = intent.getIntExtra("SDcardID", -1);
                String id = Integer.toBinaryString(SDcardId);
                Log.e("id", "msga" + id);
                while (id.length() < 8) {
                    id = "0" + id;
                }
                char index = id.charAt(3);
                Log.e("id", "msgb" + id);
                Log.e("id", "msgc" + index);
                if (state == 1) {
                    if (index == '1') {
                        sdId = SDcardId;
                        tv_total_capacity.setText(String
                                .valueOf(total_capacity) + "M");
                        tv_sd_remainning_capacity.setText(String
                                .valueOf(remain_capacity) + "M");
                        showSDImg();
                    } else if (index == '0') {
                        usbId = SDcardId;
                        tv_usb_total_capacity.setText(String
                                .valueOf(total_capacity) + "M");
                        tv_usb_remainning_capacity.setText(String
                                .valueOf(remain_capacity) + "M");
                    }
                } else {
                    count++;
                    if (contact.contactType == P2PValue.DeviceType.IPC) {
                        if (count == 1) {
                            Intent back = new Intent();
                            back.setAction(Constants.Action.REPLACE_MAIN_CONTROL);
                            mContext.sendBroadcast(back);
                            T.showShort(mContext, R.string.sd_no_exist);
                        }
                    } else {
                        if (count == 2) {
                            Intent back = new Intent();
                            back.setAction(Constants.Action.REPLACE_MAIN_CONTROL);
                            mContext.sendBroadcast(back);
                            T.showShort(mContext, R.string.sd_no_exist);
                        }
                    }
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_DEVICE_NOT_SUPPORT)) {
                Intent back = new Intent();
                back.setAction(Constants.Action.REPLACE_MAIN_CONTROL);
                mContext.sendBroadcast(back);
                T.showShort(mContext, R.string.not_support);
            }
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sd_format) {
            final NormalDialog dialog = new NormalDialog(mContext, mContext
                    .getResources().getString(R.string.sd_formatting), mContext
                    .getResources().getString(R.string.delete_sd_remind),
                    mContext.getResources().getString(R.string.ensure),
                    mContext.getResources().getString(R.string.cancel));
            dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {

                @Override
                public void onClick() {
                    // TODO Auto-generated method stub
                    P2PHandler.getInstance().setSdFormat(idOrIp,
                            contact.contactPassword, sdId);
                    Log.e("SDcardId", "SDcardId" + SDcardId);
                }
            });
            dialog.setOnButtonCancelListener(new NormalDialog.OnButtonCancelListener() {

                @Override
                public void onClick() {
                    // TODO Auto-generated method stub
                    showSDImg();
                    dialog.dismiss();
                }
            });
            dialog.showNormalDialog();
            dialog.setCanceledOnTouchOutside(false);
            showSDProgress();
        }

    }

    public void showSDImg() {
        format_icon.setVisibility(ImageView.VISIBLE);
        progress_format.setVisibility(progress_format.GONE);
        sd_format.setClickable(true);
    }

    public void showSDProgress() {
        format_icon.setVisibility(ImageView.GONE);
        progress_format.setVisibility(progress_format.VISIBLE);
        sd_format.setClickable(false);
    }

    public String createCommand(String bCommandType, String bOption,
                                String SDCardCounts) {
        return bCommandType + bOption + SDCardCounts;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isRegFilter == true) {
            mContext.unregisterReceiver(br);
        }
        Intent it = new Intent();
        it.setAction(Constants.Action.CONTROL_BACK);
        mContext.sendBroadcast(it);
    }
}
