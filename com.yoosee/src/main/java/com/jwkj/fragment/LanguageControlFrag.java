package com.jwkj.fragment;

import java.util.zip.Inflater;

import com.jwkj.activity.MainControlActivity;
import com.jwkj.adapter.LanguegeAdapter;
import com.jwkj.data.Contact;
import com.jwkj.global.Constants;
import com.jwkj.utils.T;
import com.jwkj.widget.NormalDialog;
import com.p2p.core.P2PHandler;
import com.yoosee.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.RadialGradient;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class LanguageControlFrag extends BaseFragment {
	ListView list_languege;
	int languegecount, curlanguege;
	int[] langueges;
	Contact contact;
	LanguegeAdapter adapter;
	private Context mContext;
	NormalDialog dialog;
	boolean isRegFilter = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
    @Override
    public View onCreateView(LayoutInflater inflater,
    		@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	mContext = getActivity();
    	contact = (Contact) getArguments().getSerializable("contact");
    	languegecount=getArguments().getInt("languegecount");
    	curlanguege=getArguments().getInt("curlanguege");
    	langueges=getArguments().getIntArray("langueges");
    	Log.e("languege", "languegecount="+languegecount+"--"+"curlanguege="+curlanguege+"--"+"langueges_length="+langueges.length);
    	View view=inflater.inflate(R.layout.fragment_language_control, container, false);
    	initComponent(view);
    	regFilter();
    	return view;
    }
	public void initComponent(View view) {
		list_languege = (ListView) view.findViewById(R.id.list_languege);
		adapter = new LanguegeAdapter(mContext, languegecount, curlanguege,
				langueges);
		list_languege.setAdapter(adapter);
		list_languege.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				curlanguege = langueges[position];
				adapter.notifydata(curlanguege);
				dialog = new NormalDialog(mContext);
				dialog.setStyle(NormalDialog.DIALOG_STYLE_LOADING);
				dialog.showDialog();
				P2PHandler.getInstance().setDevieceLanguege(contact.contactId,
						contact.contactPassword, curlanguege);
			}
		});

	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.P2P.RET_SET_LANGUEGE);
		mContext.registerReceiver(br, filter);
		isRegFilter = true;
	}

	BroadcastReceiver br = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(Constants.P2P.RET_SET_LANGUEGE)) {
				if (dialog != null) {
					dialog.dismiss();
				}
				int result = intent.getIntExtra("result", -1);
				if (result == 0) {
					T.showShort(mContext, R.string.language_set_success);
				} else {
					T.showShort(mContext, R.string.language_set_fail);
				}
			}
		}
	};

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (isRegFilter) {
			mContext.unregisterReceiver(br);
			isRegFilter = false;
		}
	}
}
