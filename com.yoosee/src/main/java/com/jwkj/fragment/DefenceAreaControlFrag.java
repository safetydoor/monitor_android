package com.jwkj.fragment;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yoosee.R;
import com.jwkj.activity.CutImageActivity;
import com.jwkj.activity.MainControlActivity;
import com.jwkj.data.Contact;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.widget.NormalDialog;
import com.p2p.core.P2PHandler;

public class DefenceAreaControlFrag extends BaseFragment implements
		OnClickListener {
	private Context mContext;
	private Contact contact;
	private boolean isRegFilter = false;
	RelativeLayout change_defence_area1, change_defence_area2,
			change_defence_area3, change_defence_area4, change_defence_area5,
			change_defence_area6, change_defence_area7, change_defence_area8,
			change_defence_area9;
	LinearLayout defence_area_content1, defence_area_content2,
			defence_area_content3, defence_area_content4,
			defence_area_content5, defence_area_content6,
			defence_area_content7, defence_area_content8,
			defence_area_content9;
	ProgressBar progressBar_defence_area1, progressBar_defence_area2,
			progressBar_defence_area3, progressBar_defence_area4,
			progressBar_defence_area5, progressBar_defence_area6,
			progressBar_defence_area7, progressBar_defence_area8,
			progressBar_defence_area9;
	RelativeLayout one1, one2, one3, one4, one5, one6, one7, one8;
	RelativeLayout two1, two2, two3, two4, two5, two6, two7, two8;
	RelativeLayout three1, three2, three3, three4, three5, three6, three7,
			three8;
	RelativeLayout four1, four2, four3, four4, four5, four6, four7, four8;
	RelativeLayout five1, five2, five3, five4, five5, five6, five7, five8;
	RelativeLayout six1, six2, six3, six4, six5, six6, six7, six8;
	RelativeLayout seven1, seven2, seven3, seven4, seven5, seven6, seven7,
			seven8;
	RelativeLayout eight1, eight2, eight3, eight4, eight5, eight6, eight7,
			eight8;
	RelativeLayout nine1, nine2, nine3, nine4, nine5, nine6, nine7, nine8;

	ImageView switch_one1, switch_one2, switch_one3, switch_one4, switch_one5,
			switch_one6, switch_one7, switch_one8;
	ImageView switch_two1, switch_two2, switch_two3, switch_two4, switch_two5,
			switch_two6, switch_two7, switch_two8;
	ImageView switch_three1, switch_three2, switch_three3, switch_three4,
			switch_three5, switch_three6, switch_three7, switch_three8;
	ImageView switch_four1, switch_four2, switch_four3, switch_four4,
			switch_four5, switch_four6, switch_four7, switch_four8;
	ImageView switch_five1, switch_five2, switch_five3, switch_five4,
			switch_five5, switch_five6, switch_five7, switch_five8;
	ImageView switch_six1, switch_six2, switch_six3, switch_six4, switch_six5,
			switch_six6, switch_six7, switch_six8;
	ImageView switch_seven1, switch_seven2, switch_seven3, switch_seven4,
			switch_seven5, switch_seven6, switch_seven7, switch_seven8;
	ImageView switch_eight1, switch_eight2, switch_eight3, switch_eight4,
			switch_eight5, switch_eight6, switch_eight7, switch_eight8;
	ImageView switch_nine1, switch_nine2, switch_nine3, switch_nine4,
			switch_nine5, switch_nine6, switch_nine7, switch_nine8;

	ImageView clear_one1, clear_one2, clear_one3, clear_one4, clear_one5,
			clear_one6, clear_one7, clear_one8;
	ImageView clear_two1, clear_two2, clear_two3, clear_two4, clear_two5,
			clear_two6, clear_two7, clear_two8;
	ImageView clear_three1, clear_three2, clear_three3, clear_three4,
			clear_three5, clear_three6, clear_three7, clear_three8;
	ImageView clear_four1, clear_four2, clear_four3, clear_four4, clear_four5,
			clear_four6, clear_four7, clear_four8;
	ImageView clear_five1, clear_five2, clear_five3, clear_five4, clear_five5,
			clear_five6, clear_five7, clear_five8;
	ImageView clear_six1, clear_six2, clear_six3, clear_six4, clear_six5,
			clear_six6, clear_six7, clear_six8;
	ImageView clear_seven1, clear_seven2, clear_seven3, clear_seven4,
			clear_seven5, clear_seven6, clear_seven7, clear_seven8;
	ImageView clear_eight1, clear_eight2, clear_eight3, clear_eight4,
			clear_eight5, clear_eight6, clear_eight7, clear_eight8;
	ImageView clear_nine1, clear_nine2, clear_nine3, clear_nine4, clear_nine5,
			clear_nine6, clear_nine7, clear_nine8;

	ProgressBar pre_two1, pre_two2, pre_two3, pre_two4, pre_two5, pre_two6,
			pre_two7, pre_two8;
	ProgressBar pre_three1, pre_three2, pre_three3, pre_three4, pre_three5,
			pre_three6, pre_three7, pre_three8;
	ProgressBar pre_four1, pre_four2, pre_four3, pre_four4, pre_four5,
			pre_four6, pre_four7, pre_four8;
	ProgressBar pre_five1, pre_five2, pre_five3, pre_five4, pre_five5,
			pre_five6, pre_five7, pre_five8;
	ProgressBar pre_six1, pre_six2, pre_six3, pre_six4, pre_six5, pre_six6,
			pre_six7, pre_six8;
	ProgressBar pre_seven1, pre_seven2, pre_seven3, pre_seven4, pre_seven5,
			pre_seven6, pre_seven7, pre_seven8;
	ProgressBar pre_eight1, pre_eight2, pre_eight3, pre_eight4, pre_eight5,
			pre_eight6, pre_eight7, pre_eight8;
	ProgressBar pre_nine1, pre_nine2, pre_nine3, pre_nine4, pre_nine5,
			pre_nine6, pre_nine7, pre_nine8;
	NormalDialog dialog_loading;

	int current_group;
	int current_item;
	int current_type;

	boolean is_one_active = false;
	boolean is_two_active = false;
	boolean is_three_active = false;
	boolean is_four_active = false;
	boolean is_five_active = false;
	boolean is_six_active = false;
	boolean is_seven_active = false;
	boolean is_eight_active = false;
	boolean is_nine_active = false;
	private static final int EXPAND_OR_SHRINK = 0x11;
	private static final int END_EXPAND_OR_SHRINK = 0x12;
	int currentSwitch;
	int currentgroup;
	int currentitem;
	boolean isSurpport = true;
	String idOrIp;

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
		idOrIp=contact.contactId;
		if(contact.ipadressAddress!=null){
			String mark=contact.ipadressAddress.getHostAddress();
			String ip=mark.substring(mark.lastIndexOf(".")+1,mark.length());
			if(!ip.equals("")&&ip!=null){
				idOrIp=ip;
			}	
		}
		View view = inflater.inflate(R.layout.fragment_defence_area_control,
				container, false);
		initComponent(view);
		regFilter();

		P2PHandler.getInstance().getDefenceAreaAlarmSwitch(idOrIp,
				contact.contactPassword);
		P2PHandler.getInstance()
				.getDefenceArea(idOrIp, contact.contactPassword);
		return view;
	}

	public void initComponent(View view) {
		change_defence_area1 = (RelativeLayout) view
				.findViewById(R.id.change_defence_area1);
		defence_area_content1 = (LinearLayout) view
				.findViewById(R.id.defence_area_content1);
		progressBar_defence_area1 = (ProgressBar) view
				.findViewById(R.id.progressBar_defence_area1);

		change_defence_area2 = (RelativeLayout) view
				.findViewById(R.id.change_defence_area2);
		defence_area_content2 = (LinearLayout) view
				.findViewById(R.id.defence_area_content2);
		progressBar_defence_area2 = (ProgressBar) view
				.findViewById(R.id.progressBar_defence_area2);

		change_defence_area3 = (RelativeLayout) view
				.findViewById(R.id.change_defence_area3);
		defence_area_content3 = (LinearLayout) view
				.findViewById(R.id.defence_area_content3);
		progressBar_defence_area3 = (ProgressBar) view
				.findViewById(R.id.progressBar_defence_area3);

		change_defence_area4 = (RelativeLayout) view
				.findViewById(R.id.change_defence_area4);
		defence_area_content4 = (LinearLayout) view
				.findViewById(R.id.defence_area_content4);
		progressBar_defence_area4 = (ProgressBar) view
				.findViewById(R.id.progressBar_defence_area4);

		change_defence_area5 = (RelativeLayout) view
				.findViewById(R.id.change_defence_area5);
		defence_area_content5 = (LinearLayout) view
				.findViewById(R.id.defence_area_content5);
		progressBar_defence_area5 = (ProgressBar) view
				.findViewById(R.id.progressBar_defence_area5);

		change_defence_area6 = (RelativeLayout) view
				.findViewById(R.id.change_defence_area6);
		defence_area_content6 = (LinearLayout) view
				.findViewById(R.id.defence_area_content6);
		progressBar_defence_area6 = (ProgressBar) view
				.findViewById(R.id.progressBar_defence_area6);

		change_defence_area7 = (RelativeLayout) view
				.findViewById(R.id.change_defence_area7);
		defence_area_content7 = (LinearLayout) view
				.findViewById(R.id.defence_area_content7);
		progressBar_defence_area7 = (ProgressBar) view
				.findViewById(R.id.progressBar_defence_area7);

		change_defence_area8 = (RelativeLayout) view
				.findViewById(R.id.change_defence_area8);
		defence_area_content8 = (LinearLayout) view
				.findViewById(R.id.defence_area_content8);
		progressBar_defence_area8 = (ProgressBar) view
				.findViewById(R.id.progressBar_defence_area8);

		change_defence_area9 = (RelativeLayout) view
				.findViewById(R.id.change_defence_area9);
		defence_area_content9 = (LinearLayout) view
				.findViewById(R.id.defence_area_content9);
		progressBar_defence_area9 = (ProgressBar) view
				.findViewById(R.id.progressBar_defence_area9);

		one1 = (RelativeLayout) view.findViewById(R.id.one1);
		one2 = (RelativeLayout) view.findViewById(R.id.one2);
		one3 = (RelativeLayout) view.findViewById(R.id.one3);
		one4 = (RelativeLayout) view.findViewById(R.id.one4);
		one5 = (RelativeLayout) view.findViewById(R.id.one5);
		one6 = (RelativeLayout) view.findViewById(R.id.one6);
		one7 = (RelativeLayout) view.findViewById(R.id.one7);
		one8 = (RelativeLayout) view.findViewById(R.id.one8);

		two1 = (RelativeLayout) view.findViewById(R.id.two1);
		two2 = (RelativeLayout) view.findViewById(R.id.two2);
		two3 = (RelativeLayout) view.findViewById(R.id.two3);
		two4 = (RelativeLayout) view.findViewById(R.id.two4);
		two5 = (RelativeLayout) view.findViewById(R.id.two5);
		two6 = (RelativeLayout) view.findViewById(R.id.two6);
		two7 = (RelativeLayout) view.findViewById(R.id.two7);
		two8 = (RelativeLayout) view.findViewById(R.id.two8);

		three1 = (RelativeLayout) view.findViewById(R.id.three1);
		three2 = (RelativeLayout) view.findViewById(R.id.three2);
		three3 = (RelativeLayout) view.findViewById(R.id.three3);
		three4 = (RelativeLayout) view.findViewById(R.id.three4);
		three5 = (RelativeLayout) view.findViewById(R.id.three5);
		three6 = (RelativeLayout) view.findViewById(R.id.three6);
		three7 = (RelativeLayout) view.findViewById(R.id.three7);
		three8 = (RelativeLayout) view.findViewById(R.id.three8);

		four1 = (RelativeLayout) view.findViewById(R.id.four1);
		four2 = (RelativeLayout) view.findViewById(R.id.four2);
		four3 = (RelativeLayout) view.findViewById(R.id.four3);
		four4 = (RelativeLayout) view.findViewById(R.id.four4);
		four5 = (RelativeLayout) view.findViewById(R.id.four5);
		four6 = (RelativeLayout) view.findViewById(R.id.four6);
		four7 = (RelativeLayout) view.findViewById(R.id.four7);
		four8 = (RelativeLayout) view.findViewById(R.id.four8);

		five1 = (RelativeLayout) view.findViewById(R.id.five1);
		five2 = (RelativeLayout) view.findViewById(R.id.five2);
		five3 = (RelativeLayout) view.findViewById(R.id.five3);
		five4 = (RelativeLayout) view.findViewById(R.id.five4);
		five5 = (RelativeLayout) view.findViewById(R.id.five5);
		five6 = (RelativeLayout) view.findViewById(R.id.five6);
		five7 = (RelativeLayout) view.findViewById(R.id.five7);
		five8 = (RelativeLayout) view.findViewById(R.id.five8);

		six1 = (RelativeLayout) view.findViewById(R.id.six1);
		six2 = (RelativeLayout) view.findViewById(R.id.six2);
		six3 = (RelativeLayout) view.findViewById(R.id.six3);
		six4 = (RelativeLayout) view.findViewById(R.id.six4);
		six5 = (RelativeLayout) view.findViewById(R.id.six5);
		six6 = (RelativeLayout) view.findViewById(R.id.six6);
		six7 = (RelativeLayout) view.findViewById(R.id.six7);
		six8 = (RelativeLayout) view.findViewById(R.id.six8);

		seven1 = (RelativeLayout) view.findViewById(R.id.seven1);
		seven2 = (RelativeLayout) view.findViewById(R.id.seven2);
		seven3 = (RelativeLayout) view.findViewById(R.id.seven3);
		seven4 = (RelativeLayout) view.findViewById(R.id.seven4);
		seven5 = (RelativeLayout) view.findViewById(R.id.seven5);
		seven6 = (RelativeLayout) view.findViewById(R.id.seven6);
		seven7 = (RelativeLayout) view.findViewById(R.id.seven7);
		seven8 = (RelativeLayout) view.findViewById(R.id.seven8);

		eight1 = (RelativeLayout) view.findViewById(R.id.eight1);
		eight2 = (RelativeLayout) view.findViewById(R.id.eight2);
		eight3 = (RelativeLayout) view.findViewById(R.id.eight3);
		eight4 = (RelativeLayout) view.findViewById(R.id.eight4);
		eight5 = (RelativeLayout) view.findViewById(R.id.eight5);
		eight6 = (RelativeLayout) view.findViewById(R.id.eight6);
		eight7 = (RelativeLayout) view.findViewById(R.id.eight7);
		eight8 = (RelativeLayout) view.findViewById(R.id.eight8);

		nine1 = (RelativeLayout) view.findViewById(R.id.nine1);
		nine2 = (RelativeLayout) view.findViewById(R.id.nine2);
		nine3 = (RelativeLayout) view.findViewById(R.id.nine3);
		nine4 = (RelativeLayout) view.findViewById(R.id.nine4);
		nine5 = (RelativeLayout) view.findViewById(R.id.nine5);
		nine6 = (RelativeLayout) view.findViewById(R.id.nine6);
		nine7 = (RelativeLayout) view.findViewById(R.id.nine7);
		nine8 = (RelativeLayout) view.findViewById(R.id.nine8);

		switch_one1 = (ImageView) view.findViewById(R.id.switch_one1);
		switch_one2 = (ImageView) view.findViewById(R.id.switch_one2);
		switch_one3 = (ImageView) view.findViewById(R.id.switch_one3);
		switch_one4 = (ImageView) view.findViewById(R.id.switch_one4);
		switch_one5 = (ImageView) view.findViewById(R.id.switch_one5);
		switch_one6 = (ImageView) view.findViewById(R.id.switch_one6);
		switch_one7 = (ImageView) view.findViewById(R.id.switch_one7);
		switch_one8 = (ImageView) view.findViewById(R.id.switch_one8);

		switch_two1 = (ImageView) view.findViewById(R.id.switch_two1);
		switch_two2 = (ImageView) view.findViewById(R.id.switch_two2);
		switch_two3 = (ImageView) view.findViewById(R.id.switch_two3);
		switch_two4 = (ImageView) view.findViewById(R.id.switch_two4);
		switch_two5 = (ImageView) view.findViewById(R.id.switch_two5);
		switch_two6 = (ImageView) view.findViewById(R.id.switch_two6);
		switch_two7 = (ImageView) view.findViewById(R.id.switch_two7);
		switch_two8 = (ImageView) view.findViewById(R.id.switch_two8);

		switch_three1 = (ImageView) view.findViewById(R.id.switch_three1);
		switch_three2 = (ImageView) view.findViewById(R.id.switch_three2);
		switch_three3 = (ImageView) view.findViewById(R.id.switch_three3);
		switch_three4 = (ImageView) view.findViewById(R.id.switch_three4);
		switch_three5 = (ImageView) view.findViewById(R.id.switch_three5);
		switch_three6 = (ImageView) view.findViewById(R.id.switch_three6);
		switch_three7 = (ImageView) view.findViewById(R.id.switch_three7);
		switch_three8 = (ImageView) view.findViewById(R.id.switch_three8);

		switch_four1 = (ImageView) view.findViewById(R.id.switch_four1);
		switch_four2 = (ImageView) view.findViewById(R.id.switch_four2);
		switch_four3 = (ImageView) view.findViewById(R.id.switch_four3);
		switch_four4 = (ImageView) view.findViewById(R.id.switch_four4);
		switch_four5 = (ImageView) view.findViewById(R.id.switch_four5);
		switch_four6 = (ImageView) view.findViewById(R.id.switch_four6);
		switch_four7 = (ImageView) view.findViewById(R.id.switch_four7);
		switch_four8 = (ImageView) view.findViewById(R.id.switch_four8);

		switch_five1 = (ImageView) view.findViewById(R.id.switch_five1);
		switch_five2 = (ImageView) view.findViewById(R.id.switch_five2);
		switch_five3 = (ImageView) view.findViewById(R.id.switch_five3);
		switch_five4 = (ImageView) view.findViewById(R.id.switch_five4);
		switch_five5 = (ImageView) view.findViewById(R.id.switch_five5);
		switch_five6 = (ImageView) view.findViewById(R.id.switch_five6);
		switch_five7 = (ImageView) view.findViewById(R.id.switch_five7);
		switch_five8 = (ImageView) view.findViewById(R.id.switch_five8);

		switch_six1 = (ImageView) view.findViewById(R.id.switch_six1);
		switch_six2 = (ImageView) view.findViewById(R.id.switch_six2);
		switch_six3 = (ImageView) view.findViewById(R.id.switch_six3);
		switch_six4 = (ImageView) view.findViewById(R.id.switch_six4);
		switch_six5 = (ImageView) view.findViewById(R.id.switch_six5);
		switch_six6 = (ImageView) view.findViewById(R.id.switch_six6);
		switch_six7 = (ImageView) view.findViewById(R.id.switch_six7);
		switch_six8 = (ImageView) view.findViewById(R.id.switch_six8);

		switch_seven1 = (ImageView) view.findViewById(R.id.switch_seven1);
		switch_seven2 = (ImageView) view.findViewById(R.id.switch_seven2);
		switch_seven3 = (ImageView) view.findViewById(R.id.switch_seven3);
		switch_seven4 = (ImageView) view.findViewById(R.id.switch_seven4);
		switch_seven5 = (ImageView) view.findViewById(R.id.switch_seven5);
		switch_seven6 = (ImageView) view.findViewById(R.id.switch_seven6);
		switch_seven7 = (ImageView) view.findViewById(R.id.switch_seven7);
		switch_seven8 = (ImageView) view.findViewById(R.id.switch_seven8);

		switch_eight1 = (ImageView) view.findViewById(R.id.switch_eight1);
		switch_eight2 = (ImageView) view.findViewById(R.id.switch_eight2);
		switch_eight3 = (ImageView) view.findViewById(R.id.switch_eight3);
		switch_eight4 = (ImageView) view.findViewById(R.id.switch_eight4);
		switch_eight5 = (ImageView) view.findViewById(R.id.switch_eight5);
		switch_eight6 = (ImageView) view.findViewById(R.id.switch_eight6);
		switch_eight7 = (ImageView) view.findViewById(R.id.switch_eight7);
		switch_eight8 = (ImageView) view.findViewById(R.id.switch_eight8);

		switch_nine1 = (ImageView) view.findViewById(R.id.switch_nine1);
		switch_nine2 = (ImageView) view.findViewById(R.id.switch_nine2);
		switch_nine3 = (ImageView) view.findViewById(R.id.switch_nine3);
		switch_nine4 = (ImageView) view.findViewById(R.id.switch_nine4);
		switch_nine5 = (ImageView) view.findViewById(R.id.switch_nine5);
		switch_nine6 = (ImageView) view.findViewById(R.id.switch_nine6);
		switch_nine7 = (ImageView) view.findViewById(R.id.switch_nine7);
		switch_nine8 = (ImageView) view.findViewById(R.id.switch_nine8);

		clear_one1 = (ImageView) view.findViewById(R.id.clear_one1);
		clear_one2 = (ImageView) view.findViewById(R.id.clear_one2);
		clear_one3 = (ImageView) view.findViewById(R.id.clear_one3);
		clear_one4 = (ImageView) view.findViewById(R.id.clear_one4);
		clear_one5 = (ImageView) view.findViewById(R.id.clear_one5);
		clear_one6 = (ImageView) view.findViewById(R.id.clear_one6);
		clear_one7 = (ImageView) view.findViewById(R.id.clear_one7);
		clear_one8 = (ImageView) view.findViewById(R.id.clear_one8);

		clear_two1 = (ImageView) view.findViewById(R.id.clear_two1);
		clear_two2 = (ImageView) view.findViewById(R.id.clear_two2);
		clear_two3 = (ImageView) view.findViewById(R.id.clear_two3);
		clear_two4 = (ImageView) view.findViewById(R.id.clear_two4);
		clear_two5 = (ImageView) view.findViewById(R.id.clear_two5);
		clear_two6 = (ImageView) view.findViewById(R.id.clear_two6);
		clear_two7 = (ImageView) view.findViewById(R.id.clear_two7);
		clear_two8 = (ImageView) view.findViewById(R.id.clear_two8);

		clear_three1 = (ImageView) view.findViewById(R.id.clear_three1);
		clear_three2 = (ImageView) view.findViewById(R.id.clear_three2);
		clear_three3 = (ImageView) view.findViewById(R.id.clear_three3);
		clear_three4 = (ImageView) view.findViewById(R.id.clear_three4);
		clear_three5 = (ImageView) view.findViewById(R.id.clear_three5);
		clear_three6 = (ImageView) view.findViewById(R.id.clear_three6);
		clear_three7 = (ImageView) view.findViewById(R.id.clear_three7);
		clear_three8 = (ImageView) view.findViewById(R.id.clear_three8);

		clear_four1 = (ImageView) view.findViewById(R.id.clear_four1);
		clear_four2 = (ImageView) view.findViewById(R.id.clear_four2);
		clear_four3 = (ImageView) view.findViewById(R.id.clear_four3);
		clear_four4 = (ImageView) view.findViewById(R.id.clear_four4);
		clear_four5 = (ImageView) view.findViewById(R.id.clear_four5);
		clear_four6 = (ImageView) view.findViewById(R.id.clear_four6);
		clear_four7 = (ImageView) view.findViewById(R.id.clear_four7);
		clear_four8 = (ImageView) view.findViewById(R.id.clear_four8);

		clear_five1 = (ImageView) view.findViewById(R.id.clear_five1);
		clear_five2 = (ImageView) view.findViewById(R.id.clear_five2);
		clear_five3 = (ImageView) view.findViewById(R.id.clear_five3);
		clear_five4 = (ImageView) view.findViewById(R.id.clear_five4);
		clear_five5 = (ImageView) view.findViewById(R.id.clear_five5);
		clear_five6 = (ImageView) view.findViewById(R.id.clear_five6);
		clear_five7 = (ImageView) view.findViewById(R.id.clear_five7);
		clear_five8 = (ImageView) view.findViewById(R.id.clear_five8);

		clear_six1 = (ImageView) view.findViewById(R.id.clear_six1);
		clear_six2 = (ImageView) view.findViewById(R.id.clear_six2);
		clear_six3 = (ImageView) view.findViewById(R.id.clear_six3);
		clear_six4 = (ImageView) view.findViewById(R.id.clear_six4);
		clear_six5 = (ImageView) view.findViewById(R.id.clear_six5);
		clear_six6 = (ImageView) view.findViewById(R.id.clear_six6);
		clear_six7 = (ImageView) view.findViewById(R.id.clear_six7);
		clear_six8 = (ImageView) view.findViewById(R.id.clear_six8);

		clear_seven1 = (ImageView) view.findViewById(R.id.clear_seven1);
		clear_seven2 = (ImageView) view.findViewById(R.id.clear_seven2);
		clear_seven3 = (ImageView) view.findViewById(R.id.clear_seven3);
		clear_seven4 = (ImageView) view.findViewById(R.id.clear_seven4);
		clear_seven5 = (ImageView) view.findViewById(R.id.clear_seven5);
		clear_seven6 = (ImageView) view.findViewById(R.id.clear_seven6);
		clear_seven7 = (ImageView) view.findViewById(R.id.clear_seven7);
		clear_seven8 = (ImageView) view.findViewById(R.id.clear_seven8);

		clear_eight1 = (ImageView) view.findViewById(R.id.clear_eight1);
		clear_eight2 = (ImageView) view.findViewById(R.id.clear_eight2);
		clear_eight3 = (ImageView) view.findViewById(R.id.clear_eight3);
		clear_eight4 = (ImageView) view.findViewById(R.id.clear_eight4);
		clear_eight5 = (ImageView) view.findViewById(R.id.clear_eight5);
		clear_eight6 = (ImageView) view.findViewById(R.id.clear_eight6);
		clear_eight7 = (ImageView) view.findViewById(R.id.clear_eight7);
		clear_eight8 = (ImageView) view.findViewById(R.id.clear_eight8);

		clear_nine1 = (ImageView) view.findViewById(R.id.clear_nine1);
		clear_nine2 = (ImageView) view.findViewById(R.id.clear_nine2);
		clear_nine3 = (ImageView) view.findViewById(R.id.clear_nine3);
		clear_nine4 = (ImageView) view.findViewById(R.id.clear_nine4);
		clear_nine5 = (ImageView) view.findViewById(R.id.clear_nine5);
		clear_nine6 = (ImageView) view.findViewById(R.id.clear_nine6);
		clear_nine7 = (ImageView) view.findViewById(R.id.clear_nine7);
		clear_nine8 = (ImageView) view.findViewById(R.id.clear_nine8);

		pre_two1 = (ProgressBar) view.findViewById(R.id.pre_two1);
		pre_two2 = (ProgressBar) view.findViewById(R.id.pre_two2);
		pre_two3 = (ProgressBar) view.findViewById(R.id.pre_two3);
		pre_two4 = (ProgressBar) view.findViewById(R.id.pre_two4);
		pre_two5 = (ProgressBar) view.findViewById(R.id.pre_two5);
		pre_two6 = (ProgressBar) view.findViewById(R.id.pre_two6);
		pre_two7 = (ProgressBar) view.findViewById(R.id.pre_two7);
		pre_two8 = (ProgressBar) view.findViewById(R.id.pre_two8);

		pre_three1 = (ProgressBar) view.findViewById(R.id.pre_three1);
		pre_three2 = (ProgressBar) view.findViewById(R.id.pre_three2);
		pre_three3 = (ProgressBar) view.findViewById(R.id.pre_three3);
		pre_three4 = (ProgressBar) view.findViewById(R.id.pre_three4);
		pre_three5 = (ProgressBar) view.findViewById(R.id.pre_three5);
		pre_three6 = (ProgressBar) view.findViewById(R.id.pre_three6);
		pre_three7 = (ProgressBar) view.findViewById(R.id.pre_three7);
		pre_three8 = (ProgressBar) view.findViewById(R.id.pre_three8);

		pre_four1 = (ProgressBar) view.findViewById(R.id.pre_four1);
		pre_four2 = (ProgressBar) view.findViewById(R.id.pre_four2);
		pre_four3 = (ProgressBar) view.findViewById(R.id.pre_four3);
		pre_four4 = (ProgressBar) view.findViewById(R.id.pre_four4);
		pre_four5 = (ProgressBar) view.findViewById(R.id.pre_four5);
		pre_four6 = (ProgressBar) view.findViewById(R.id.pre_four6);
		pre_four7 = (ProgressBar) view.findViewById(R.id.pre_four7);
		pre_four8 = (ProgressBar) view.findViewById(R.id.pre_four8);

		pre_five1 = (ProgressBar) view.findViewById(R.id.pre_five1);
		pre_five2 = (ProgressBar) view.findViewById(R.id.pre_five2);
		pre_five3 = (ProgressBar) view.findViewById(R.id.pre_five3);
		pre_five4 = (ProgressBar) view.findViewById(R.id.pre_five4);
		pre_five5 = (ProgressBar) view.findViewById(R.id.pre_five5);
		pre_five6 = (ProgressBar) view.findViewById(R.id.pre_five6);
		pre_five7 = (ProgressBar) view.findViewById(R.id.pre_five7);
		pre_five8 = (ProgressBar) view.findViewById(R.id.pre_five8);

		pre_six1 = (ProgressBar) view.findViewById(R.id.pre_six1);
		pre_six2 = (ProgressBar) view.findViewById(R.id.pre_six2);
		pre_six3 = (ProgressBar) view.findViewById(R.id.pre_six3);
		pre_six4 = (ProgressBar) view.findViewById(R.id.pre_six4);
		pre_six5 = (ProgressBar) view.findViewById(R.id.pre_six5);
		pre_six6 = (ProgressBar) view.findViewById(R.id.pre_six6);
		pre_six7 = (ProgressBar) view.findViewById(R.id.pre_six7);
		pre_six8 = (ProgressBar) view.findViewById(R.id.pre_six8);

		pre_seven1 = (ProgressBar) view.findViewById(R.id.pre_seven1);
		pre_seven2 = (ProgressBar) view.findViewById(R.id.pre_seven2);
		pre_seven3 = (ProgressBar) view.findViewById(R.id.pre_seven3);
		pre_seven4 = (ProgressBar) view.findViewById(R.id.pre_seven4);
		pre_seven5 = (ProgressBar) view.findViewById(R.id.pre_seven5);
		pre_seven6 = (ProgressBar) view.findViewById(R.id.pre_seven6);
		pre_seven7 = (ProgressBar) view.findViewById(R.id.pre_seven7);
		pre_seven8 = (ProgressBar) view.findViewById(R.id.pre_seven8);

		pre_eight1 = (ProgressBar) view.findViewById(R.id.pre_eight1);
		pre_eight2 = (ProgressBar) view.findViewById(R.id.pre_eight2);
		pre_eight3 = (ProgressBar) view.findViewById(R.id.pre_eight3);
		pre_eight4 = (ProgressBar) view.findViewById(R.id.pre_eight4);
		pre_eight5 = (ProgressBar) view.findViewById(R.id.pre_eight5);
		pre_eight6 = (ProgressBar) view.findViewById(R.id.pre_eight6);
		pre_eight7 = (ProgressBar) view.findViewById(R.id.pre_eight7);
		pre_eight8 = (ProgressBar) view.findViewById(R.id.pre_eight8);

		pre_nine1 = (ProgressBar) view.findViewById(R.id.pre_nine1);
		pre_nine2 = (ProgressBar) view.findViewById(R.id.pre_nine2);
		pre_nine3 = (ProgressBar) view.findViewById(R.id.pre_nine3);
		pre_nine4 = (ProgressBar) view.findViewById(R.id.pre_nine4);
		pre_nine5 = (ProgressBar) view.findViewById(R.id.pre_nine5);
		pre_nine6 = (ProgressBar) view.findViewById(R.id.pre_nine6);
		pre_nine7 = (ProgressBar) view.findViewById(R.id.pre_nine7);
		pre_nine8 = (ProgressBar) view.findViewById(R.id.pre_nine8);

	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();

		filter.addAction(Constants.P2P.ACK_RET_SET_DEFENCE_AREA);
		filter.addAction(Constants.P2P.ACK_RET_GET_DEFENCE_AREA);
		filter.addAction(Constants.P2P.ACK_RET_CLEAR_DEFENCE_AREA);
		filter.addAction(Constants.P2P.RET_CLEAR_DEFENCE_AREA);
		filter.addAction(Constants.P2P.RET_SET_DEFENCE_AREA);
		filter.addAction(Constants.P2P.RET_GET_DEFENCE_AREA);
		filter.addAction(Constants.P2P.RET_DEVICE_NOT_SUPPORT);
		filter.addAction(Constants.P2P.ACK_RET_GET_SENSOR_SWITCH);
		filter.addAction(Constants.P2P.ACK_RET_SET_SENSOR_SWITCH);
		filter.addAction(Constants.P2P.RET_GET_SENSOR_SWITCH);
		filter.addAction(Constants.P2P.RET_SET_SENSOR_SWITCH);

		mContext.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent.getAction().equals(Constants.P2P.RET_GET_DEFENCE_AREA)) {
				ArrayList<int[]> data = (ArrayList<int[]>) intent
						.getSerializableExtra("data");
				initData(data);
				showDefence_area1();
			} else if (intent.getAction().equals(
					Constants.P2P.RET_SET_DEFENCE_AREA)) {
				if (null != dialog_loading) {
					dialog_loading.dismiss();
					dialog_loading = null;
				}
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.DEFENCE_AREA_SET.SETTING_SUCCESS) {
					if (current_type == Constants.P2P_SET.DEFENCE_AREA_SET.DEFENCE_AREA_TYPE_CLEAR) {
						grayButton(current_group, current_item);
						T.showShort(mContext, R.string.clear_success);
					} else {
						lightButton(current_group, current_item);
						T.showShort(mContext, R.string.learning_success);
					}
				} else if (result == 30) {
					grayButton(current_group, current_item);
					T.showShort(mContext, R.string.clear_success);
				} else if (result == 32) {
					int group = intent.getIntExtra("group", -1);
					int item = intent.getIntExtra("item", -1);
					Log.e("my", "group:" + group + " item:" + item);
					T.showShort(
							mContext,
							Utils.getDefenceAreaByGroup(mContext, group)
									+ ":"
									+ (item + 1)
									+ " "
									+ mContext.getResources().getString(
											R.string.channel)
									+ " "
									+ mContext.getResources().getString(
											R.string.has_been_learning));
				} else if (result == 41) {
					Intent back = new Intent();
					back.setAction(Constants.Action.REPLACE_MAIN_CONTROL);
					mContext.sendBroadcast(back);
					T.showShort(mContext,
							R.string.device_unsupport_defence_area);
				} else {
					T.showShort(mContext, R.string.operator_error);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.RET_CLEAR_DEFENCE_AREA)) {
				if (null != dialog_loading) {
					dialog_loading.dismiss();
					dialog_loading = null;
				}
				int result = intent.getIntExtra("result", -1);
				if (result == 0) {
					grayButton(current_group, 0);
					grayButton(current_group, 1);
					grayButton(current_group, 2);
					grayButton(current_group, 3);
					grayButton(current_group, 4);
					grayButton(current_group, 5);
					grayButton(current_group, 6);
					grayButton(current_group, 7);
					T.showShort(mContext, R.string.clear_success);
				} else {
					T.showShort(mContext, R.string.operator_error);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.RET_DEVICE_NOT_SUPPORT)) {
				if (null != dialog_loading) {
					dialog_loading.dismiss();
					dialog_loading = null;
				}
				isSurpport = false;
				// T.showShort(mContext, R.string.not_surpport_sensor);
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_GET_DEFENCE_AREA)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					mContext.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					Log.e("my", "net error resend:get defence area");
					P2PHandler.getInstance().getDefenceArea(idOrIp,
							contact.contactPassword);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_SET_DEFENCE_AREA)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					mContext.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					Log.e("my", "net error resend:set defence area");
					P2PHandler.getInstance().setDefenceAreaState(idOrIp,
							contact.contactPassword, current_group,
							current_item, current_type);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_CLEAR_DEFENCE_AREA)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					mContext.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					Log.e("my", "net error resend:clear defence area");
					P2PHandler.getInstance().clearDefenceAreaState(idOrIp,
							contact.contactPassword, current_group);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_GET_SENSOR_SWITCH)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					mContext.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					Log.e("my", "net error resend:set defence area");
					P2PHandler.getInstance().getDefenceAreaAlarmSwitch(idOrIp,
							contact.contactPassword);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_SET_SENSOR_SWITCH)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					mContext.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					Log.e("my", "net error resend:set defence area");
					P2PHandler.getInstance().setDefenceAreaAlarmSwitch(idOrIp,
							contact.contactPassword, currentSwitch,
							currentgroup, currentitem);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.RET_GET_SENSOR_SWITCH)) {
				int result = intent.getIntExtra("result", -1);
				ArrayList<int[]> sensors = (ArrayList<int[]>) intent
						.getSerializableExtra("data");
				if (result == 1) {
					initSensorSwitch(sensors);
				} else if (result == 41) {
					// T.showShort(mContext,
					// R.string.device_unsupport_defence_area);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.RET_SET_SENSOR_SWITCH)) {
				int result = intent.getIntExtra("result", -1);
				if (result == 0) {
					setgraySwitch(currentgroup + 1, currentitem, currentSwitch);
				} else if (result == 41) {
					// T.showShort(mContext,
					// R.string.device_unsupport_defence_area);
				} else {

				}
			}
		}
	};

	public void initData(ArrayList<int[]> data) {
		for (int i = 0; i < data.size(); i++) {
			int[] status = data.get(i);
			for (int j = 0; j < status.length; j++) {

				if (status[j] == 1) {
					grayButton(i, j);
				} else {
					lightButton(i, j);
				}
			}
		}
	}

	public void lightButton(final int i, final int j) {
		RelativeLayout item = this.getKeyBoard(i, j);
		ImageView clear_item = this.getClear(i, j);
		ImageView switch_item = this.getSwitch(i, j);
		if (null != item) {
			// item.setOnClickListener(new OnClickListener(){
			//
			// @Override
			// public void onClick(View arg0) {
			// // TODO Auto-generated method stub
			// clear(i,j);
			// }
			//
			// });
			item.setClickable(false);
			clear_item.setVisibility(ImageView.VISIBLE);
			if (i != 0 && isSurpport == true) {
				switch_item.setVisibility(ImageView.VISIBLE);
			}
			clear_item.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					clear(i, j);
				}
			});
			// item.setBackgroundResource(R.drawable.button_bg_dialog_ok);
			// item.setTextColor(mContext.getResources().getColor(R.color.text_color_black));
		}
	}

	public void grayButton(final int i, final int j) {
		RelativeLayout item = this.getKeyBoard(i, j);
		ImageView clear_item = this.getClear(i, j);
		final ImageView switch_item = this.getSwitch(i, j);
		if (null != item) {
			item.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					study(i, j);
					// P2PHandler.getInstance().getDefenceAreaAlarmSwitch(contact.contactId,
					// contact.contactPassword);
				}

			});
			item.setClickable(true);
			if (i != 0) {
				switch_item.setVisibility(ImageView.INVISIBLE);
			}
			clear_item.setVisibility(ImageView.GONE);
			// item.setBackgroundResource(R.drawable.button_bg_dialog_cancel);
			// item.setTextColor(mContext.getResources().getColor(R.color.text_color_gray));
		}
	}

	public Handler mHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub

			switch (msg.what) {
			case EXPAND_OR_SHRINK:
				int group1 = msg.arg1;
				int length = msg.arg2;
				LinearLayout item = getContent(group1);
				LinearLayout.LayoutParams params = (LayoutParams) item
						.getLayoutParams();
				params.height = length;
				item.setLayoutParams(params);
				break;
			case END_EXPAND_OR_SHRINK:
				int group2 = msg.arg1;
				if (group2 == 8) {
					RelativeLayout bar = getBar(group2);
					bar.setBackgroundResource(R.drawable.tiao_bg_bottom);
				}
				//
				break;
			}
			return false;
		}
	});

	public void shrinkItem(final int i) {
		if (this.getIsActive(i)) {
			return;
		} else {
			setActive(i, true);
		}
		new Thread() {
			public void run() {
				int length = (int) mContext.getResources().getDimension(
						R.dimen.defen_area_expand_view_height);
				while (length > 0) {
					length = length - 20;
					if (length <= 0) {
						length = 0;
					}
					Message msg = new Message();
					msg.what = EXPAND_OR_SHRINK;
					msg.arg1 = i;
					msg.arg2 = length;
					mHandler.sendMessage(msg);
					Utils.sleepThread(20);
				}

				Message end = new Message();
				end.what = END_EXPAND_OR_SHRINK;
				end.arg1 = i;
				mHandler.sendMessage(end);
				setActive(i, false);
				RelativeLayout item = getBar(i);
				item.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						expandItem(i);
					}

				});

			}
		}.start();
	}

	public void expandItem(final int i) {
		if (this.getIsActive(i)) {
			return;
		} else {
			this.setActive(i, true);
		}

		final RelativeLayout item = getBar(i);
		if (i == 8) {
			item.setBackgroundResource(R.drawable.tiao_bg_center);
		}

		new Thread() {
			public void run() {
				int length = 0;

				int total = (int) mContext.getResources().getDimension(
						R.dimen.defen_area_expand_view_height);
				while (length < total) {
					length = length + 20;
					if (length > total) {
						length = total;
					}
					Message msg = new Message();
					msg.what = EXPAND_OR_SHRINK;
					msg.arg1 = i;
					msg.arg2 = length;
					mHandler.sendMessage(msg);
					Utils.sleepThread(20);
				}
				setActive(i, false);
				item.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						shrinkItem(i);
					}

				});

			}
		}.start();
	}

	public void showDefence_area1() {
		progressBar_defence_area1.setVisibility(RelativeLayout.GONE);
		progressBar_defence_area2.setVisibility(RelativeLayout.GONE);
		progressBar_defence_area3.setVisibility(RelativeLayout.GONE);
		progressBar_defence_area4.setVisibility(RelativeLayout.GONE);
		progressBar_defence_area5.setVisibility(RelativeLayout.GONE);
		progressBar_defence_area6.setVisibility(RelativeLayout.GONE);
		progressBar_defence_area7.setVisibility(RelativeLayout.GONE);
		progressBar_defence_area8.setVisibility(RelativeLayout.GONE);
		progressBar_defence_area9.setVisibility(RelativeLayout.GONE);
		for (int i = 0; i < 9; i++) {
			RelativeLayout item = this.getBar(i);
			final int group = i;
			item.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					expandItem(group);
				}
			});
			item.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub

					NormalDialog dialog = new NormalDialog(mContext, mContext
							.getResources().getString(R.string.clear_code),
							mContext.getResources().getString(
									R.string.clear_code_prompt), mContext
									.getResources().getString(R.string.ensure),
							mContext.getResources().getString(R.string.cancel));

					dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {

						@Override
						public void onClick() {
							// TODO Auto-generated method stub
							if (null == dialog_loading) {
								dialog_loading = new NormalDialog(mContext,
										mContext.getResources().getString(
												R.string.clearing), "", "", "");
								dialog_loading
										.setStyle(NormalDialog.DIALOG_STYLE_LOADING);
							}
							dialog_loading.showDialog();
							current_group = group;
							current_type = Constants.P2P_SET.DEFENCE_AREA_SET.DEFENCE_AREA_TYPE_CLEAR_GROUP;
							P2PHandler.getInstance().clearDefenceAreaState(
									idOrIp, contact.contactPassword, group);
						}
					});

					dialog.showNormalDialog();
					dialog.setCanceledOnTouchOutside(false);

					return false;
				}

			});
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	public void study(final int group, final int item) {

		NormalDialog dialog = new NormalDialog(mContext, mContext
				.getResources().getString(R.string.learing_code), mContext
				.getResources().getString(R.string.learing_code_prompt),
				mContext.getResources().getString(R.string.ensure), mContext
						.getResources().getString(R.string.cancel));

		dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {

			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				if (null == dialog_loading) {
					dialog_loading = new NormalDialog(mContext, mContext
							.getResources().getString(R.string.studying), "",
							"", "");
					dialog_loading.setStyle(NormalDialog.DIALOG_STYLE_LOADING);
				}
				dialog_loading.showDialog();
				current_type = Constants.P2P_SET.DEFENCE_AREA_SET.DEFENCE_AREA_TYPE_LEARN;
				current_group = group;
				current_item = item;
				P2PHandler
						.getInstance()
						.setDefenceAreaState(
								idOrIp,
								contact.contactPassword,
								group,
								item,
								Constants.P2P_SET.DEFENCE_AREA_SET.DEFENCE_AREA_TYPE_LEARN);
			}
		});

		dialog.showNormalDialog();
		dialog.setCanceledOnTouchOutside(false);
	}

	public void clear(final int group, final int item) {

		NormalDialog dialog = new NormalDialog(mContext, mContext
				.getResources().getString(R.string.clear_code), mContext
				.getResources().getString(R.string.clear_code_prompt), mContext
				.getResources().getString(R.string.ensure), mContext
				.getResources().getString(R.string.cancel));

		dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {

			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				if (null == dialog_loading) {
					dialog_loading = new NormalDialog(mContext, mContext
							.getResources().getString(R.string.clearing), "",
							"", "");
					dialog_loading.setStyle(NormalDialog.DIALOG_STYLE_LOADING);
				}
				dialog_loading.showDialog();
				current_type = Constants.P2P_SET.DEFENCE_AREA_SET.DEFENCE_AREA_TYPE_CLEAR;
				current_group = group;
				current_item = item;
				P2PHandler
						.getInstance()
						.setDefenceAreaState(
								idOrIp,
								contact.contactPassword,
								group,
								item,
								Constants.P2P_SET.DEFENCE_AREA_SET.DEFENCE_AREA_TYPE_CLEAR);
			}
		});

		dialog.showNormalDialog();
		dialog.setCanceledOnTouchOutside(false);
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

	public RelativeLayout getBar(int group) {
		switch (group) {
		case 0:
			return this.change_defence_area1;
		case 1:
			return this.change_defence_area2;
		case 2:
			return this.change_defence_area3;
		case 3:
			return this.change_defence_area4;
		case 4:
			return this.change_defence_area5;
		case 5:
			return this.change_defence_area6;
		case 6:
			return this.change_defence_area7;
		case 7:
			return this.change_defence_area8;
		case 8:
			return this.change_defence_area9;
		}
		return null;
	}

	public LinearLayout getContent(int group) {
		switch (group) {
		case 0:
			return this.defence_area_content1;
		case 1:
			return this.defence_area_content2;
		case 2:
			return this.defence_area_content3;
		case 3:
			return this.defence_area_content4;
		case 4:
			return this.defence_area_content5;
		case 5:
			return this.defence_area_content6;
		case 6:
			return this.defence_area_content7;
		case 7:
			return this.defence_area_content8;
		case 8:
			return this.defence_area_content9;
		}
		return null;
	}

	public RelativeLayout getKeyBoard(int group, int item) {
		switch (group) {
		case 0:
			if (item == 0) {
				return this.one1;
			} else if (item == 1) {
				return this.one2;
			} else if (item == 2) {
				return this.one3;
			} else if (item == 3) {
				return this.one4;
			} else if (item == 4) {
				return this.one5;
			} else if (item == 5) {
				return this.one6;
			} else if (item == 6) {
				return this.one7;
			} else if (item == 7) {
				return this.one8;
			}
			break;
		case 1:
			if (item == 0) {
				return this.two1;
			} else if (item == 1) {
				return this.two2;
			} else if (item == 2) {
				return this.two3;
			} else if (item == 3) {
				return this.two4;
			} else if (item == 4) {
				return this.two5;
			} else if (item == 5) {
				return this.two6;
			} else if (item == 6) {
				return this.two7;
			} else if (item == 7) {
				return this.two8;
			}
			break;
		case 2:
			if (item == 0) {
				return this.three1;
			} else if (item == 1) {
				return this.three2;
			} else if (item == 2) {
				return this.three3;
			} else if (item == 3) {
				return this.three4;
			} else if (item == 4) {
				return this.three5;
			} else if (item == 5) {
				return this.three6;
			} else if (item == 6) {
				return this.three7;
			} else if (item == 7) {
				return this.three8;
			}
			break;
		case 3:
			if (item == 0) {
				return this.four1;
			} else if (item == 1) {
				return this.four2;
			} else if (item == 2) {
				return this.four3;
			} else if (item == 3) {
				return this.four4;
			} else if (item == 4) {
				return this.four5;
			} else if (item == 5) {
				return this.four6;
			} else if (item == 6) {
				return this.four7;
			} else if (item == 7) {
				return this.four8;
			}
			break;
		case 4:
			if (item == 0) {
				return this.five1;
			} else if (item == 1) {
				return this.five2;
			} else if (item == 2) {
				return this.five3;
			} else if (item == 3) {
				return this.five4;
			} else if (item == 4) {
				return this.five5;
			} else if (item == 5) {
				return this.five6;
			} else if (item == 6) {
				return this.five7;
			} else if (item == 7) {
				return this.five8;
			}
			break;
		case 5:
			if (item == 0) {
				return this.six1;
			} else if (item == 1) {
				return this.six2;
			} else if (item == 2) {
				return this.six3;
			} else if (item == 3) {
				return this.six4;
			} else if (item == 4) {
				return this.six5;
			} else if (item == 5) {
				return this.six6;
			} else if (item == 6) {
				return this.six7;
			} else if (item == 7) {
				return this.six8;
			}
			break;
		case 6:
			if (item == 0) {
				return this.seven1;
			} else if (item == 1) {
				return this.seven2;
			} else if (item == 2) {
				return this.seven3;
			} else if (item == 3) {
				return this.seven4;
			} else if (item == 4) {
				return this.seven5;
			} else if (item == 5) {
				return this.seven6;
			} else if (item == 6) {
				return this.seven7;
			} else if (item == 7) {
				return this.seven8;
			}
			break;
		case 7:
			if (item == 0) {
				return this.eight1;
			} else if (item == 1) {
				return this.eight2;
			} else if (item == 2) {
				return this.eight3;
			} else if (item == 3) {
				return this.eight4;
			} else if (item == 4) {
				return this.eight5;
			} else if (item == 5) {
				return this.eight6;
			} else if (item == 6) {
				return this.eight7;
			} else if (item == 7) {
				return this.eight8;
			}
			break;
		case 8:
			if (item == 0) {
				return this.nine1;
			} else if (item == 1) {
				return this.nine2;
			} else if (item == 2) {
				return this.nine3;
			} else if (item == 3) {
				return this.nine4;
			} else if (item == 4) {
				return this.nine5;
			} else if (item == 5) {
				return this.nine6;
			} else if (item == 6) {
				return this.nine7;
			} else if (item == 7) {
				return this.nine8;
			}
			break;
		}
		return null;
	}

	public boolean getIsActive(int group) {
		switch (group) {
		case 0:
			return this.is_one_active;
		case 1:
			return this.is_two_active;
		case 2:
			return this.is_three_active;
		case 3:
			return this.is_four_active;
		case 4:
			return this.is_five_active;
		case 5:
			return this.is_six_active;
		case 6:
			return this.is_seven_active;
		case 7:
			return this.is_eight_active;
		case 8:
			return this.is_nine_active;
		}
		return true;
	}

	public void setActive(int group, boolean bool) {
		switch (group) {
		case 0:
			this.is_one_active = bool;
			break;
		case 1:
			this.is_two_active = bool;
			break;
		case 2:
			this.is_three_active = bool;
			break;
		case 3:
			this.is_four_active = bool;
			break;
		case 4:
			this.is_five_active = bool;
			break;
		case 5:
			this.is_six_active = bool;
			break;
		case 6:
			this.is_seven_active = bool;
			break;
		case 7:
			this.is_eight_active = bool;
			break;
		case 8:
			this.is_nine_active = bool;
			break;
		}
	}

	public ImageView getClear(int group, int item) {
		switch (group) {
		case 0:
			if (item == 0) {
				return this.clear_one1;
			} else if (item == 1) {
				return this.clear_one2;
			} else if (item == 2) {
				return this.clear_one3;
			} else if (item == 3) {
				return this.clear_one4;
			} else if (item == 4) {
				return this.clear_one5;
			} else if (item == 5) {
				return this.clear_one6;
			} else if (item == 6) {
				return this.clear_one7;
			} else if (item == 7) {
				return this.clear_one8;
			}
			break;
		case 1:
			if (item == 0) {
				return this.clear_two1;
			} else if (item == 1) {
				return this.clear_two2;
			} else if (item == 2) {
				return this.clear_two3;
			} else if (item == 3) {
				return this.clear_two4;
			} else if (item == 4) {
				return this.clear_two5;
			} else if (item == 5) {
				return this.clear_two6;
			} else if (item == 6) {
				return this.clear_two7;
			} else if (item == 7) {
				return this.clear_two8;
			}
			break;
		case 2:
			if (item == 0) {
				return this.clear_three1;
			} else if (item == 1) {
				return this.clear_three2;
			} else if (item == 2) {
				return this.clear_three3;
			} else if (item == 3) {
				return this.clear_three4;
			} else if (item == 4) {
				return this.clear_three5;
			} else if (item == 5) {
				return this.clear_three6;
			} else if (item == 6) {
				return this.clear_three7;
			} else if (item == 7) {
				return this.clear_three8;
			}
			break;
		case 3:
			if (item == 0) {
				return this.clear_four1;
			} else if (item == 1) {
				return this.clear_four2;
			} else if (item == 2) {
				return this.clear_four3;
			} else if (item == 3) {
				return this.clear_four4;
			} else if (item == 4) {
				return this.clear_four5;
			} else if (item == 5) {
				return this.clear_four6;
			} else if (item == 6) {
				return this.clear_four7;
			} else if (item == 7) {
				return this.clear_four8;
			}
			break;
		case 4:
			if (item == 0) {
				return this.clear_five1;
			} else if (item == 1) {
				return this.clear_five2;
			} else if (item == 2) {
				return this.clear_five3;
			} else if (item == 3) {
				return this.clear_five4;
			} else if (item == 4) {
				return this.clear_five5;
			} else if (item == 5) {
				return this.clear_five6;
			} else if (item == 6) {
				return this.clear_five7;
			} else if (item == 7) {
				return this.clear_five8;
			}
			break;
		case 5:
			if (item == 0) {
				return this.clear_six1;
			} else if (item == 1) {
				return this.clear_six2;
			} else if (item == 2) {
				return this.clear_six3;
			} else if (item == 3) {
				return this.clear_six4;
			} else if (item == 4) {
				return this.clear_six5;
			} else if (item == 5) {
				return this.clear_six6;
			} else if (item == 6) {
				return this.clear_six7;
			} else if (item == 7) {
				return this.clear_six8;
			}
			break;
		case 6:
			if (item == 0) {
				return this.clear_seven1;
			} else if (item == 1) {
				return this.clear_seven2;
			} else if (item == 2) {
				return this.clear_seven3;
			} else if (item == 3) {
				return this.clear_seven4;
			} else if (item == 4) {
				return this.clear_seven5;
			} else if (item == 5) {
				return this.clear_seven6;
			} else if (item == 6) {
				return this.clear_seven7;
			} else if (item == 7) {
				return this.clear_seven8;
			}
			break;
		case 7:
			if (item == 0) {
				return this.clear_eight1;
			} else if (item == 1) {
				return this.clear_eight2;
			} else if (item == 2) {
				return this.clear_eight3;
			} else if (item == 3) {
				return this.clear_eight4;
			} else if (item == 4) {
				return this.clear_eight5;
			} else if (item == 5) {
				return this.clear_eight6;
			} else if (item == 6) {
				return this.clear_eight7;
			} else if (item == 7) {
				return this.clear_eight8;
			}
			break;
		case 8:
			if (item == 0) {
				return this.clear_nine1;
			} else if (item == 1) {
				return this.clear_nine2;
			} else if (item == 2) {
				return this.clear_nine3;
			} else if (item == 3) {
				return this.clear_nine4;
			} else if (item == 4) {
				return this.clear_nine5;
			} else if (item == 5) {
				return this.clear_nine6;
			} else if (item == 6) {
				return this.clear_nine7;
			} else if (item == 7) {
				return this.clear_nine8;
			}
			break;
		}
		return null;

	}

	public ImageView getSwitch(int group, int item) {
		switch (group) {
		case 0:
			if (item == 0) {
				return this.switch_one1;
			} else if (item == 1) {
				return this.switch_one2;
			} else if (item == 2) {
				return this.switch_one3;
			} else if (item == 3) {
				return this.switch_one4;
			} else if (item == 4) {
				return this.switch_one5;
			} else if (item == 5) {
				return this.switch_one6;
			} else if (item == 6) {
				return this.switch_one7;
			} else if (item == 7) {
				return this.switch_one8;
			}
			break;
		case 1:
			if (item == 0) {
				return this.switch_two1;
			} else if (item == 1) {
				return this.switch_two2;
			} else if (item == 2) {
				return this.switch_two3;
			} else if (item == 3) {
				return this.switch_two4;
			} else if (item == 4) {
				return this.switch_two5;
			} else if (item == 5) {
				return this.switch_two6;
			} else if (item == 6) {
				return this.switch_two7;
			} else if (item == 7) {
				return this.switch_two8;
			}
			break;
		case 2:
			if (item == 0) {
				return this.switch_three1;
			} else if (item == 1) {
				return this.switch_three2;
			} else if (item == 2) {
				return this.switch_three3;
			} else if (item == 3) {
				return this.switch_three4;
			} else if (item == 4) {
				return this.switch_three5;
			} else if (item == 5) {
				return this.switch_three6;
			} else if (item == 6) {
				return this.switch_three7;
			} else if (item == 7) {
				return this.switch_three8;
			}
			break;
		case 3:
			if (item == 0) {
				return this.switch_four1;
			} else if (item == 1) {
				return this.switch_four2;
			} else if (item == 2) {
				return this.switch_four3;
			} else if (item == 3) {
				return this.switch_four4;
			} else if (item == 4) {
				return this.switch_four5;
			} else if (item == 5) {
				return this.switch_four6;
			} else if (item == 6) {
				return this.switch_four7;
			} else if (item == 7) {
				return this.switch_four8;
			}
			break;
		case 4:
			if (item == 0) {
				return this.switch_five1;
			} else if (item == 1) {
				return this.switch_five2;
			} else if (item == 2) {
				return this.switch_five3;
			} else if (item == 3) {
				return this.switch_five4;
			} else if (item == 4) {
				return this.switch_five5;
			} else if (item == 5) {
				return this.switch_five6;
			} else if (item == 6) {
				return this.switch_five7;
			} else if (item == 7) {
				return this.switch_five8;
			}
			break;
		case 5:
			if (item == 0) {
				return this.switch_six1;
			} else if (item == 1) {
				return this.switch_six2;
			} else if (item == 2) {
				return this.switch_six3;
			} else if (item == 3) {
				return this.switch_six4;
			} else if (item == 4) {
				return this.switch_six5;
			} else if (item == 5) {
				return this.switch_six6;
			} else if (item == 6) {
				return this.switch_six7;
			} else if (item == 7) {
				return this.switch_six8;
			}
			break;
		case 6:
			if (item == 0) {
				return this.switch_seven1;
			} else if (item == 1) {
				return this.switch_seven2;
			} else if (item == 2) {
				return this.switch_seven3;
			} else if (item == 3) {
				return this.switch_seven4;
			} else if (item == 4) {
				return this.switch_seven5;
			} else if (item == 5) {
				return this.switch_seven6;
			} else if (item == 6) {
				return this.switch_seven7;
			} else if (item == 7) {
				return this.switch_seven8;
			}
			break;
		case 7:
			if (item == 0) {
				return this.switch_eight1;
			} else if (item == 1) {
				return this.switch_eight2;
			} else if (item == 2) {
				return this.switch_eight3;
			} else if (item == 3) {
				return this.switch_eight4;
			} else if (item == 4) {
				return this.switch_eight5;
			} else if (item == 5) {
				return this.switch_eight6;
			} else if (item == 6) {
				return this.switch_eight7;
			} else if (item == 7) {
				return this.switch_eight8;
			}
			break;
		case 8:
			if (item == 0) {
				return this.switch_nine1;
			} else if (item == 1) {
				return this.switch_nine2;
			} else if (item == 2) {
				return this.switch_nine3;
			} else if (item == 3) {
				return this.switch_nine4;
			} else if (item == 4) {
				return this.switch_nine5;
			} else if (item == 5) {
				return this.switch_nine6;
			} else if (item == 6) {
				return this.switch_nine7;
			} else if (item == 7) {
				return this.switch_nine8;
			}
			break;
		}
		return null;
	}

	public ProgressBar getProgress(int group, int item) {
		switch (group) {
		case 1:
			if (item == 0) {
				return this.pre_two1;
			} else if (item == 1) {
				return this.pre_two2;
			} else if (item == 2) {
				return this.pre_two3;
			} else if (item == 3) {
				return this.pre_two4;
			} else if (item == 4) {
				return this.pre_two5;
			} else if (item == 5) {
				return this.pre_two6;
			} else if (item == 6) {
				return this.pre_two7;
			} else if (item == 7) {
				return this.pre_two8;
			}
			break;
		case 2:
			if (item == 0) {
				return this.pre_three1;
			} else if (item == 1) {
				return this.pre_three2;
			} else if (item == 2) {
				return this.pre_three3;
			} else if (item == 3) {
				return this.pre_three4;
			} else if (item == 4) {
				return this.pre_three5;
			} else if (item == 5) {
				return this.pre_three6;
			} else if (item == 6) {
				return this.pre_three7;
			} else if (item == 7) {
				return this.pre_three8;
			}
			break;
		case 3:
			if (item == 0) {
				return this.pre_four1;
			} else if (item == 1) {
				return this.pre_four2;
			} else if (item == 2) {
				return this.pre_four3;
			} else if (item == 3) {
				return this.pre_four4;
			} else if (item == 4) {
				return this.pre_four5;
			} else if (item == 5) {
				return this.pre_four6;
			} else if (item == 6) {
				return this.pre_four7;
			} else if (item == 7) {
				return this.pre_four8;
			}
			break;
		case 4:
			if (item == 0) {
				return this.pre_five1;
			} else if (item == 1) {
				return this.pre_five2;
			} else if (item == 2) {
				return this.pre_five3;
			} else if (item == 3) {
				return this.pre_five4;
			} else if (item == 4) {
				return this.pre_five5;
			} else if (item == 5) {
				return this.pre_five6;
			} else if (item == 6) {
				return this.pre_five7;
			} else if (item == 7) {
				return this.pre_five8;
			}
			break;
		case 5:
			if (item == 0) {
				return this.pre_six1;
			} else if (item == 1) {
				return this.pre_six2;
			} else if (item == 2) {
				return this.pre_six3;
			} else if (item == 3) {
				return this.pre_six4;
			} else if (item == 4) {
				return this.pre_six5;
			} else if (item == 5) {
				return this.pre_six6;
			} else if (item == 6) {
				return this.pre_six7;
			} else if (item == 7) {
				return this.pre_six8;
			}
			break;
		case 6:
			if (item == 0) {
				return this.pre_seven1;
			} else if (item == 1) {
				return this.pre_seven2;
			} else if (item == 2) {
				return this.pre_seven3;
			} else if (item == 3) {
				return this.pre_seven4;
			} else if (item == 4) {
				return this.pre_seven5;
			} else if (item == 5) {
				return this.pre_seven6;
			} else if (item == 6) {
				return this.pre_seven7;
			} else if (item == 7) {
				return this.pre_seven8;
			}
			break;
		case 7:
			if (item == 0) {
				return this.pre_eight1;
			} else if (item == 1) {
				return this.pre_eight2;
			} else if (item == 2) {
				return this.pre_eight3;
			} else if (item == 3) {
				return this.pre_eight4;
			} else if (item == 4) {
				return this.pre_eight5;
			} else if (item == 5) {
				return this.pre_eight6;
			} else if (item == 6) {
				return this.pre_eight7;
			} else if (item == 7) {
				return this.pre_eight8;
			}
			break;
		case 8:
			if (item == 0) {
				return this.pre_nine1;
			} else if (item == 1) {
				return this.pre_nine2;
			} else if (item == 2) {
				return this.pre_nine3;
			} else if (item == 3) {
				return this.pre_nine4;
			} else if (item == 4) {
				return this.pre_nine5;
			} else if (item == 5) {
				return this.pre_nine6;
			} else if (item == 6) {
				return this.pre_nine7;
			} else if (item == 7) {
				return this.pre_nine8;
			}
			break;
		}
		return null;
	}

	public void initSensorSwitch(ArrayList<int[]> sensors) {
		for (int i = 0; i < sensors.size(); i++) {
			int[] sensor = sensors.get(i);
			for (int j = 0; j < sensor.length; j++) {
				graySwitch(i + 1, j, sensor[7 - j]);
			}
		}
	}

	public void graySwitch(final int i, final int j, final int isOpen) {
		final ImageView item = getSwitch(i, j);
		final ProgressBar pre_item = getProgress(i, j);
		if (pre_item != null) {
			pre_item.setVisibility(ProgressBar.GONE);
		}
		if (isOpen == 1) {
			item.setImageResource(R.drawable.check_on);
		} else {
			item.setImageResource(R.drawable.check_off);
		}
		item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (pre_item != null) {
					pre_item.setVisibility(ProgressBar.VISIBLE);
				}
				item.setVisibility(ImageView.INVISIBLE);
				if (isOpen == 1) {
					currentSwitch = 0;
					currentgroup = i - 1;
					currentitem = j;
					P2PHandler.getInstance().setDefenceAreaAlarmSwitch(idOrIp,
							contact.contactPassword, currentSwitch,
							currentgroup, currentitem);
				} else {
					currentSwitch = 1;
					currentgroup = i - 1;
					currentitem = j;
					P2PHandler.getInstance().setDefenceAreaAlarmSwitch(idOrIp,
							contact.contactPassword, currentSwitch,
							currentgroup, currentitem);
				}
			}
		});
	}

	public void setgraySwitch(final int i, final int j, final int isOpen) {
		final ImageView item = getSwitch(i, j);
		final ProgressBar pre_item = getProgress(i, j);
		if (pre_item != null) {
			pre_item.setVisibility(ProgressBar.GONE);
		}
		if (isOpen == 1) {
			item.setImageResource(R.drawable.check_on);
		} else {
			item.setImageResource(R.drawable.check_off);
		}
		item.setVisibility(ImageView.VISIBLE);
		item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (pre_item != null) {
					pre_item.setVisibility(ProgressBar.VISIBLE);
				}
				item.setVisibility(ImageView.INVISIBLE);
				if (isOpen == 1) {
					currentSwitch = 0;
					currentgroup = i - 1;
					currentitem = j;
					P2PHandler.getInstance().setDefenceAreaAlarmSwitch(idOrIp,
							contact.contactPassword, currentSwitch,
							currentgroup, currentitem);
				} else {
					currentSwitch = 1;
					currentgroup = i - 1;
					currentitem = j;
					P2PHandler.getInstance().setDefenceAreaAlarmSwitch(idOrIp,
							contact.contactPassword, currentSwitch,
							currentgroup, currentitem);
				}
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Intent it = new Intent();
		it.setAction(Constants.Action.CONTROL_BACK);
		mContext.sendBroadcast(it);
	}
}
