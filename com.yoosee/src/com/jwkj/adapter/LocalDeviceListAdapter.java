package com.jwkj.adapter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import com.yoosee.R;
import com.jwkj.activity.AddApDeviceActivity;
import com.jwkj.activity.AddContactNextActivity;
import com.jwkj.data.APContact;
import com.jwkj.data.Contact;
import com.jwkj.data.DataManager;
import com.jwkj.entity.LocalDevice;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.WifiUtils;
import com.p2p.core.P2PValue;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LocalDeviceListAdapter extends BaseAdapter {
	List<LocalDevice> datas;
	Context mContext;

	public LocalDeviceListAdapter(Context context) {
		datas = FList.getInstance().getLocalDevicesNoAP();
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public LocalDevice getItem(int arg0) {
		// TODO Auto-generated method stub
		return datas.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View view = arg1;
		if (null == view) {
			view = LayoutInflater.from(mContext).inflate(
					R.layout.list_item_local_device, null);
		}
		TextView name = (TextView) view.findViewById(R.id.text_name);
		ImageView typeImg = (ImageView) view.findViewById(R.id.img_type);
		TextView text_ip_address = (TextView) view
				.findViewById(R.id.text_ip_address);

		final LocalDevice localDevice = datas.get(position);
		if (localDevice.flag == Constants.DeviceFlag.AP_MODE) {
			name.setText(localDevice.getName());
		} else {
			name.setText(localDevice.getContactId());

		}
		text_ip_address.setText(localDevice.address.getHostAddress());
		switch (localDevice.getType()) {
		case P2PValue.DeviceType.NPC:
			typeImg.setImageResource(R.drawable.ic_device_type_npc);
			break;
		case P2PValue.DeviceType.IPC:
			typeImg.setImageResource(R.drawable.ic_device_type_ipc);
			break;
		case P2PValue.DeviceType.DOORBELL:
			typeImg.setImageResource(R.drawable.ic_device_type_door_bell);
			break;
		case P2PValue.DeviceType.UNKNOWN:
			typeImg.setImageResource(R.drawable.ic_device_type_unknown);
			break;
		case P2PValue.DeviceType.NVR:
			typeImg.setImageResource(R.drawable.ic_device_type_nvr);
			break;
		default:
			typeImg.setImageResource(R.drawable.ic_device_type_unknown);
			break;
		}

		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Contact saveContact = new Contact();
				saveContact.contactId = localDevice.contactId;
				saveContact.contactName = localDevice.name;
				saveContact.contactType = localDevice.type;
				saveContact.contactFlag = localDevice.flag;
				saveContact.messageCount = 0;
				saveContact.activeUser = NpcCommon.mThreeNum;
				saveContact.rtspflag=localDevice.rtspFrag;
				String mark = localDevice.address.getHostAddress();
				saveContact.ipadressAddress=localDevice.address;
				Intent modify = new Intent();
				if (localDevice.getFlag() == Constants.DeviceFlag.ALREADY_SET_PASSWORD) {
					modify.setClass(mContext, AddContactNextActivity.class);
					modify.putExtra("isCreatePassword", false);
				} else if (localDevice.getFlag() == Constants.DeviceFlag.UNSET_PASSWORD) {
					modify.setClass(mContext, AddContactNextActivity.class);
					modify.putExtra("isCreatePassword", true);
				} else {
					try {
						saveContact.ipadressAddress = InetAddress
								.getByName("192.168.1.1");
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					modify.setClass(mContext, AddApDeviceActivity.class);
					modify.putExtra("isCreatePassword", false);
					Log.e("dxswifi", "saveContact.contactId-->"
							+ saveContact.contactId
							+ "---saveContact.contactName"
							+ saveContact.contactName);
					if (WifiUtils.getInstance().isConnectWifi(
							saveContact.contactName)) {
						APContact cona = DataManager
								.findAPContactByActiveUserAndContactId(
										mContext, NpcCommon.mThreeNum,
										saveContact.contactId);
						if (cona != null && cona.Pwd != null
								&& cona.Pwd.length() > 0) {
							saveContact.contactPassword = cona.Pwd;
							modify.putExtra("isAPModeConnect", 1);
						} else {
							modify.putExtra("isAPModeConnect", 0);
						}
					} else {
						modify.putExtra("isAPModeConnect", 0);
					}
				}
				modify.putExtra("contact", saveContact);
				modify.putExtra("ipFlag", mark.substring(
						mark.lastIndexOf(".") + 1, mark.length()));
				mContext.startActivity(modify);
			}

		});
		return view;
	}

	public void updateData() {
		FList.getInstance().updateLocalDeviceWithLocalFriends();
		this.datas = FList.getInstance().getLocalDevicesNoAP();
		this.notifyDataSetChanged();
	}
}
