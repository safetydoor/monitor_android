package com.jwkj.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yoosee.R;
import com.jwkj.activity.MainControlActivity;
import com.jwkj.data.Contact;
import com.jwkj.global.Constants;
import com.p2p.core.P2PHandler;

public class RemoteControlFrag extends BaseFragment implements OnClickListener {
	private Context mContext;
	private Contact contact;
	RelativeLayout change_defence, change_record;
	ProgressBar progressBar_defence, progressBar_record;
	ImageView defence_img, record_img;
	TextView defence_text, record_text;
	int defenceState;
	int recordState;

	private boolean isRegFilter = false;

	int last_defence;
	int last_modify_defence;

	int last_record;
	int last_modify_record;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContext = getActivity();
		contact = (Contact) getArguments().getSerializable("contact");
		View view = inflater.inflate(R.layout.fragment_remote_control,
				container, false);
		initComponent(view);
		regFilter();

		P2PHandler.getInstance().getNpcSettings(contact.contactId,
				contact.contactPassword);
		return view;
	}

	public void initComponent(View view) {
		change_defence = (RelativeLayout) view
				.findViewById(R.id.change_defence);
		defence_img = (ImageView) view.findViewById(R.id.defence_img);
		defence_text = (TextView) view.findViewById(R.id.defence_text);

		change_record = (RelativeLayout) view.findViewById(R.id.change_record);
		record_img = (ImageView) view.findViewById(R.id.record_img);
		record_text = (TextView) view.findViewById(R.id.record_text);

		progressBar_defence = (ProgressBar) view
				.findViewById(R.id.progressBar_defence);
		progressBar_record = (ProgressBar) view
				.findViewById(R.id.progressBar_record);

		change_defence.setOnClickListener(this);
		change_record.setOnClickListener(this);
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.P2P.ACK_RET_GET_NPC_SETTINGS);

		filter.addAction(Constants.P2P.ACK_RET_SET_REMOTE_DEFENCE);
		filter.addAction(Constants.P2P.RET_SET_REMOTE_DEFENCE);
		filter.addAction(Constants.P2P.RET_GET_REMOTE_DEFENCE);

		filter.addAction(Constants.P2P.ACK_RET_SET_REMOTE_RECORD);
		filter.addAction(Constants.P2P.RET_SET_REMOTE_RECORD);
		filter.addAction(Constants.P2P.RET_GET_REMOTE_RECORD);

		mContext.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent.getAction().equals(Constants.P2P.RET_GET_REMOTE_DEFENCE)) {
				int state = intent.getIntExtra("state", -1);
				progressBar_defence.setVisibility(RelativeLayout.GONE);
				defence_img.setVisibility(RelativeLayout.VISIBLE);
				updateDefence(state);
			} else if (intent.getAction().equals(
					Constants.P2P.RET_SET_REMOTE_DEFENCE)) {
				int state = intent.getIntExtra("state", -1);
				P2PHandler.getInstance().getNpcSettings(contact.contactId,
						contact.contactPassword);
				// updateDefence(state);
			} else if (intent.getAction().equals(
					Constants.P2P.RET_GET_REMOTE_RECORD)) {
				int state = intent.getIntExtra("state", -1);
				progressBar_record.setVisibility(RelativeLayout.GONE);
				record_img.setVisibility(RelativeLayout.VISIBLE);
				updateRecord(state);
			} else if (intent.getAction().equals(
					Constants.P2P.RET_SET_REMOTE_RECORD)) {
				int state = intent.getIntExtra("state", -1);
				P2PHandler.getInstance().getNpcSettings(contact.contactId,
						contact.contactPassword);
				// updateRecord(state);
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_GET_NPC_SETTINGS)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					mContext.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					Log.e("my", "net error resend:get npc settings");
					P2PHandler.getInstance().getNpcSettings(contact.contactId,
							contact.contactPassword);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_SET_REMOTE_DEFENCE)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					mContext.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					Log.e("my", "net error resend:set remote defence");
					P2PHandler.getInstance().setRemoteDefence(
							contact.contactId, contact.contactPassword,
							last_modify_defence);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_SET_REMOTE_RECORD)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					mContext.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					Log.e("my", "net error resend:set remote record");
					P2PHandler.getInstance().setRemoteRecord(contact.contactId,
							contact.contactPassword, last_modify_record);
				}
			}

		}
	};

	public void updateDefence(int state) {
		if (state == Constants.P2P_SET.REMOTE_DEFENCE_SET.ALARM_SWITCH_ON) {
			last_defence = Constants.P2P_SET.REMOTE_DEFENCE_SET.ALARM_SWITCH_ON;
			defence_img.setBackgroundResource(R.drawable.ic_checkbox_on);
		} else {
			last_defence = Constants.P2P_SET.REMOTE_DEFENCE_SET.ALARM_SWITCH_OFF;
			defence_img.setBackgroundResource(R.drawable.ic_checkbox_off);
		}
	}

	public void updateRecord(int state) {
		if (state == Constants.P2P_SET.REMOTE_RECORD_SET.RECORD_SWITCH_ON) {
			last_record = Constants.P2P_SET.REMOTE_RECORD_SET.RECORD_SWITCH_ON;
			record_img.setBackgroundResource(R.drawable.ic_checkbox_on);
		} else {
			last_record = Constants.P2P_SET.REMOTE_RECORD_SET.RECORD_SWITCH_OFF;
			record_img.setBackgroundResource(R.drawable.ic_checkbox_off);
		}
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.change_defence) {
			progressBar_defence.setVisibility(RelativeLayout.VISIBLE);
			defence_img.setVisibility(RelativeLayout.GONE);
			if (last_defence == Constants.P2P_SET.REMOTE_DEFENCE_SET.ALARM_SWITCH_ON) {
				last_modify_defence = Constants.P2P_SET.REMOTE_DEFENCE_SET.ALARM_SWITCH_OFF;
				P2PHandler.getInstance().setRemoteDefence(contact.contactId,
						contact.contactPassword, last_modify_defence);
			} else {
				last_modify_defence = Constants.P2P_SET.REMOTE_DEFENCE_SET.ALARM_SWITCH_ON;
				P2PHandler.getInstance().setRemoteDefence(contact.contactId,
						contact.contactPassword, last_modify_defence);
			}
		} else if (id == R.id.change_record) {
			progressBar_record.setVisibility(RelativeLayout.VISIBLE);
			record_img.setVisibility(RelativeLayout.GONE);
			if (last_record == Constants.P2P_SET.REMOTE_RECORD_SET.RECORD_SWITCH_ON) {
				last_modify_record = Constants.P2P_SET.REMOTE_RECORD_SET.RECORD_SWITCH_OFF;
				P2PHandler.getInstance().setRemoteRecord(contact.contactId,
						contact.contactPassword, last_modify_record);
			} else {
				last_modify_record = Constants.P2P_SET.REMOTE_RECORD_SET.RECORD_SWITCH_ON;
				P2PHandler.getInstance().setRemoteRecord(contact.contactId,
						contact.contactPassword, last_modify_record);
			}
		}
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		if (isRegFilter) {
			mContext.unregisterReceiver(mReceiver);
			isRegFilter = false;
		}
	}

}
