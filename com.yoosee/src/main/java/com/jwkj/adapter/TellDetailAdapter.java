package com.jwkj.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yoosee.R;
import com.jwkj.data.NearlyTell;
import com.jwkj.global.Constants;
import com.jwkj.utils.Utils;

public class TellDetailAdapter extends BaseAdapter {
	List<NearlyTell> list;
	Context context;

	public TellDetailAdapter(Context context, List<NearlyTell> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View view = arg1;
		if (null == view) {
			view = LayoutInflater.from(context).inflate(
					R.layout.list_tell_detail_item, null);
		}

		ImageView callState = (ImageView) view.findViewById(R.id.callState);
		TextView tellDate = (TextView) view.findViewById(R.id.tellDate);
		ImageView callType = (ImageView) view.findViewById(R.id.call_type);
		NearlyTell nearlyTell = list.get(arg0);
		switch (nearlyTell.tellState) {
		case NearlyTell.TELL_STATE_CALL_IN_REJECT:
			callState.setImageResource(R.drawable.call_in_reject);
			break;
		case NearlyTell.TELL_STATE_CALL_IN_ACCEPT:
			callState.setImageResource(R.drawable.call_in_accept);
			break;
		case NearlyTell.TELL_STATE_CALL_OUT_REJECT:
			callState.setImageResource(R.drawable.call_out_reject);
			break;
		case NearlyTell.TELL_STATE_CALL_OUT_ACCEPT:
			callState.setImageResource(R.drawable.call_out_accept);
			break;
		}
		tellDate.setText(Utils.getFormatTellDate(context, nearlyTell.tellTime));

		if (nearlyTell.tellType == Constants.P2P_TYPE.P2P_TYPE_CALL) {
			callType.setBackgroundResource(R.drawable.call);
		} else if (nearlyTell.tellType == Constants.P2P_TYPE.P2P_TYPE_MONITOR) {
			callType.setBackgroundResource(R.drawable.monitore);
		}

		return view;
	}

	public void updateData(List<NearlyTell> list) {
		this.list = list;
		notifyDataSetChanged();
	}
}
