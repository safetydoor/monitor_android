package com.jwkj.adapter;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.test.UiThreadTest;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jwkj.APdeviceMonitorActivity;
import com.jwkj.CallActivity;
import com.jwkj.PlayBackListActivity;
import com.yoosee.R;
import com.jwkj.activity.AddApDeviceActivity;
import com.jwkj.activity.AddContactNextActivity;
import com.jwkj.activity.ApMonitorActivity;
import com.jwkj.activity.DeviceUpdateActivity;
import com.jwkj.activity.MainActivity;
import com.jwkj.activity.MainControlActivity;
import com.jwkj.activity.ModifyContactActivity;
import com.jwkj.activity.ModifyNpcPasswordActivity;
import com.jwkj.data.APContact;
import com.jwkj.data.Contact;
import com.jwkj.data.DataManager;
import com.jwkj.data.SharedPreferencesManager;
import com.jwkj.entity.LocalDevice;
import com.jwkj.fragment.APModeFrag;
import com.jwkj.fragment.ContactFrag;
import com.jwkj.fragment.SmartDeviceFrag;
import com.jwkj.global.AppConfig;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.global.MyApp;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.T;
import com.jwkj.utils.Utils;
import com.jwkj.utils.WifiUtils;
import com.jwkj.widget.HeaderView;
import com.jwkj.widget.NormalDialog;
import com.jwkj.widget.PictrueTextView;
import com.jwkj.widget.NormalDialog.OnNormalDialogTimeOutListner;
import com.lib.pullToRefresh.FlipLoadingLayout;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;
import com.p2p.core.P2PView;

public class MainAdapter extends BaseAdapter {
	Context context;
	private ContactFrag cf;

	public MainAdapter(Context context, ContactFrag cf) {
		this.context = context;
		this.cf = cf;
	}

	class ViewHolder {
		private HeaderView head;
		private TextView name;
		private TextView online_state;
		private ImageView iv_defence_state;
		private ProgressBar progress_defence;
		private ImageView iv_weakpassword;
		private ImageView iv_update;
		private ImageView iv_editor;
		private ImageView iv_playback;
		private ImageView iv_set;
		private RelativeLayout r_online_state;
		private LinearLayout l_ap;
		private ImageView iv_ap_state;
		private ImageView iv_call;
		public HeaderView getHead() {
			return head;
		}
		public void setHead(HeaderView head) {
			this.head = head;
		}
		public TextView getName() {
			return name;
		}
		public void setName(TextView name) {
			this.name = name;
		}
		public TextView getOnline_state() {
			return online_state;
		}
		public void setOnline_state(TextView online_state) {
			this.online_state = online_state;
		}
		public ImageView getIv_defence_state() {
			return iv_defence_state;
		}
		public void setIv_defence_state(ImageView iv_defence_state) {
			this.iv_defence_state = iv_defence_state;
		}
		public ProgressBar getProgress_defence() {
			return progress_defence;
		}
		public void setProgress_defence(ProgressBar progress_defence) {
			this.progress_defence = progress_defence;
		}
		public ImageView getIv_weakpassword() {
			return iv_weakpassword;
		}
		public void setIv_weakpassword(ImageView iv_weakpassword) {
			this.iv_weakpassword = iv_weakpassword;
		}
		public ImageView getIv_update() {
			return iv_update;
		}
		public void setIv_update(ImageView iv_update) {
			this.iv_update = iv_update;
		}
		public ImageView getIv_editor() {
			return iv_editor;
		}
		public void setIv_editor(ImageView iv_editor) {
			this.iv_editor = iv_editor;
		}
		public ImageView getIv_playback() {
			return iv_playback;
		}
		public void setIv_playback(ImageView iv_playback) {
			this.iv_playback = iv_playback;
		}
		public ImageView getIv_set() {
			return iv_set;
		}
		public void setIv_set(ImageView iv_set) {
			this.iv_set = iv_set;
		}
		public RelativeLayout getR_online_state() {
			return r_online_state;
		}
		public void setR_online_state(RelativeLayout r_online_state) {
			this.r_online_state = r_online_state;
		}
		public LinearLayout getL_ap() {
			return l_ap;
		}
		public void setL_ap(LinearLayout l_ap) {
			this.l_ap = l_ap;
		}
		public ImageView getIv_ap_state() {
			return iv_ap_state;
		}
		public void setIv_ap_state(ImageView iv_ap_state) {
			this.iv_ap_state = iv_ap_state;
		}
		public ImageView getIv_call() {
			return iv_call;
		}
		public void setIv_call(ImageView iv_call) {
			this.iv_call = iv_call;
		}
	    


	}

	class ViewHolder2 {
		public TextView name;
		public ImageView device_type;

		public TextView getName() {
			return name;
		}

		public void setName(TextView name) {
			this.name = name;
		}

		public ImageView getDevice_type() {
			return device_type;
		}

		public void setDevice_type(ImageView device_type) {
			this.device_type = device_type;
		}

	}
	class ViewHolder3{
		public TextView name;
		private HeaderView head;
		private LinearLayout l_ap;
		private ImageView iv_ap_state;
		private ImageView iv_defence_state;
		private ProgressBar progress_defence;
		private ImageView iv_weakpassword;
		private ImageView iv_update;
		private ImageView iv_playback;
		private ImageView iv_set;
		private ImageView iv_editor;
		private RelativeLayout r_online_state;
		public TextView getName() {
			return name;
		}
		public void setName(TextView name) {
			this.name = name;
		}
		public HeaderView getHead() {
			return head;
		}
		public void setHead(HeaderView head) {
			this.head = head;
		}
		public LinearLayout getL_ap() {
			return l_ap;
		}
		public void setL_ap(LinearLayout l_ap) {
			this.l_ap = l_ap;
		}
		public ImageView getIv_ap_state() {
			return iv_ap_state;
		}
		public void setIv_ap_state(ImageView iv_ap_state) {
			this.iv_ap_state = iv_ap_state;
		}
		public ImageView getIv_defence_state() {
			return iv_defence_state;
		}
		public void setIv_defence_state(ImageView iv_defence_state) {
			this.iv_defence_state = iv_defence_state;
		}
		public ProgressBar getProgress_defence() {
			return progress_defence;
		}
		public void setProgress_defence(ProgressBar progress_defence) {
			this.progress_defence = progress_defence;
		}
		public ImageView getIv_weakpassword() {
			return iv_weakpassword;
		}
		public void setIv_weakpassword(ImageView iv_weakpassword) {
			this.iv_weakpassword = iv_weakpassword;
		}
		public ImageView getIv_update() {
			return iv_update;
		}
		public void setIv_update(ImageView iv_update) {
			this.iv_update = iv_update;
		}
		public ImageView getIv_playback() {
			return iv_playback;
		}
		public void setIv_playback(ImageView iv_playback) {
			this.iv_playback = iv_playback;
		}
		public ImageView getIv_set() {
			return iv_set;
		}
		public void setIv_set(ImageView iv_set) {
			this.iv_set = iv_set;
		}
		public RelativeLayout getR_online_state() {
			return r_online_state;
		}
		public void setR_online_state(RelativeLayout r_online_state) {
			this.r_online_state = r_online_state;
		} 
		public ImageView getIv_editor() {
			return iv_editor;
		}
		public void setIv_editor(ImageView iv_editor) {
			this.iv_editor = iv_editor;
		}
		
	}
	class ViewHolder4{
		public TextView name;
		private TextView online_state;
		private ImageView img_set;
		public TextView getName() {
			return name;
		}
		public void setName(TextView name) {
			this.name = name;
		}
		public TextView getOnline_state() {
			return online_state;
		}
		public void setOnline_state(TextView online_state) {
			this.online_state = online_state;
		}
		public ImageView getImg_set() {
			return img_set;
		}
		public void setImg_set(ImageView img_set) {
			this.img_set = img_set;
		}
		
		
		
	}

	@Override
	public int getCount() {
		// int size = FList.getInstance().getUnsetPasswordLocalDevices().size();
		// return FList.getInstance().size() + size;
		int size=FList.getInstance().size()+FList.getInstance().apListsize();
		return size;
	}

	@Override
	public Contact getItem(int position) {
		return FList.getInstance().get(position);
	}

    
	@Override
	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		if (position<FList.getInstance().size()) {
			return 0;
		} else{
		   return 1;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		int size=FList.getInstance().size();
		// 显示所有已经添加的设备
		 if (position < size) {
		View view = convertView;
		final ViewHolder holder;
		if (null == view||size==1) {
			view = LayoutInflater.from(context).inflate(
					R.layout.list_device_item, null);
			holder = new ViewHolder();
			HeaderView head = (HeaderView) view.findViewById(R.id.user_icon);
			holder.setHead(head);
			TextView name = (TextView) view.findViewById(R.id.tv_name);
			holder.setName(name);
			TextView onlineState = (TextView) view
					.findViewById(R.id.tv_online_state);
			holder.setOnline_state(onlineState);
			ImageView iv_defence_state=(ImageView)view.findViewById(R.id.iv_defence_state);
			holder.setIv_defence_state(iv_defence_state);
			ProgressBar progress_defence=(ProgressBar)view.findViewById(R.id.progress_defence);
			holder.setProgress_defence(progress_defence);
			ImageView iv_weakpassword=(ImageView)view.findViewById(R.id.iv_weakpassword);
			holder.setIv_weakpassword(iv_weakpassword);
			ImageView iv_update=(ImageView)view.findViewById(R.id.iv_update);
			holder.setIv_update(iv_update);
			ImageView iv_editor=(ImageView)view.findViewById(R.id.iv_editor);
			holder.setIv_editor(iv_editor);
			ImageView iv_playback=(ImageView)view.findViewById(R.id.iv_playback);
			holder.setIv_playback(iv_playback);
			ImageView iv_set=(ImageView)view.findViewById(R.id.iv_set);
			holder.setIv_set(iv_set);
			LinearLayout l_ap=(LinearLayout)view.findViewById(R.id.l_ap);
			holder.setL_ap(l_ap);
		    RelativeLayout r_online_state=(RelativeLayout)view.findViewById(R.id.r_online_state);
		    holder.setR_online_state(r_online_state);
		    ImageView iv_ap_state=(ImageView)view.findViewById(R.id.iv_ap_state);
		    holder.setIv_ap_state(iv_ap_state);
		    ImageView iv_call=(ImageView)view.findViewById(R.id.iv_call);
		    holder.setIv_call(iv_call);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final Contact contact = FList.getInstance().get(position);
		if(holder==null){
			Log.e("lelenull", "holder");
		}
		if(holder.getName()==null){
			Log.e("lelenull", "holder.getName()");
		}
		if(contact==null){
			Log.e("lelenull", "contact");
		}
		if(contact.contactName==null){
			Log.e("lelenull", "contact.contactName");
		}
		holder.getName().setText(contact.contactName);
		holder.getL_ap().setVisibility(View.GONE);
		if(contact.isConnectApWifi==true){
			holder.getR_online_state().setVisibility(View.GONE);
			holder.getIv_defence_state().setVisibility(View.GONE);
			holder.getProgress_defence().setVisibility(View.GONE);
		}else{
			holder.getR_online_state().setVisibility(View.VISIBLE);
			
		}
		    int deviceType = contact.contactType;
			switch (deviceType) {
			case P2PValue.DeviceType.IPC:
				holder.getIv_playback().setVisibility(View.VISIBLE);
				holder.getIv_set().setVisibility(View.VISIBLE);
				holder.getIv_editor().setVisibility(View.VISIBLE);
				holder.getIv_call().setVisibility(View.GONE);
				break;
			case P2PValue.DeviceType.NPC:
//				holder.getIv_defence_state().setVisibility(View.VISIBLE);
				holder.getIv_playback().setVisibility(View.VISIBLE);
				holder.getIv_set().setVisibility(View.VISIBLE);
				holder.getIv_editor().setVisibility(View.VISIBLE);
				holder.getIv_call().setVisibility(View.VISIBLE);
				break;
			case P2PValue.DeviceType.NVR:
				holder.getIv_defence_state().setVisibility(View.INVISIBLE);
				holder.getProgress_defence().setVisibility(View.GONE);
				holder.getIv_playback().setVisibility(View.GONE);
				holder.getIv_set().setVisibility(View.VISIBLE);
				holder.getIv_editor().setVisibility(View.VISIBLE);
				holder.getIv_call().setVisibility(View.GONE);
				break;
			default:
				holder.getIv_call().setVisibility(View.GONE);
				break;
			}
			if(holder==null){
				Log.e("leleTest", "holder null");
			}
			if(contact.contactId==null){
				Log.e("leleTest", "contact.contactId null");
			}
			if(holder.getHead()==null){
				Log.e("leleTest", "holder.getHead() null");
			}
			holder.getHead().updateImage(contact.contactId, false);
			if (contact.onLineState == Constants.DeviceState.ONLINE) {
				holder.getOnline_state().setText(R.string.online_state);
				holder.getOnline_state().setTextColor(
						context.getResources().getColor(R.color.white));
				if (contact.contactType == P2PValue.DeviceType.UNKNOWN
						|| contact.contactType == P2PValue.DeviceType.PHONE||contact.contactType==P2PValue.DeviceType.NVR) {
					holder.getIv_defence_state().setVisibility(
							RelativeLayout.INVISIBLE);
				} else {
					holder.getIv_defence_state().setVisibility(
								RelativeLayout.VISIBLE);
					if (contact.defenceState == Constants.DefenceState.DEFENCE_STATE_LOADING) {
						holder.getProgress_defence().setVisibility(
									RelativeLayout.VISIBLE);
						holder.getIv_defence_state().setVisibility(
								RelativeLayout.INVISIBLE);
					} else if (contact.defenceState == Constants.DefenceState.DEFENCE_STATE_ON) {
						holder.getProgress_defence().setVisibility(
								RelativeLayout.GONE);
						holder.getIv_defence_state().setImageResource(
								R.drawable.item_arm);
						holder.getIv_defence_state().setVisibility(
								RelativeLayout.VISIBLE);
					} else if (contact.defenceState == Constants.DefenceState.DEFENCE_STATE_OFF) {
						holder.getProgress_defence().setVisibility(
								RelativeLayout.GONE);
						holder.getIv_defence_state().setImageResource(
								R.drawable.item_disarm);
						holder.getIv_defence_state().setVisibility(
								RelativeLayout.VISIBLE);
					} else if (contact.defenceState == Constants.DefenceState.DEFENCE_STATE_WARNING_NET) {
						holder.getProgress_defence().setVisibility(
								RelativeLayout.GONE);
						holder.getIv_defence_state().setImageResource(
								R.drawable.ic_defence_warning);
						holder.getIv_defence_state().setVisibility(
								RelativeLayout.VISIBLE);
					} else if (contact.defenceState == Constants.DefenceState.DEFENCE_STATE_WARNING_PWD) {
						holder.getProgress_defence().setVisibility(
								RelativeLayout.GONE);
						holder.getIv_defence_state().setImageResource(
								R.drawable.ic_defence_warning);
						holder.getIv_defence_state().setVisibility(
								RelativeLayout.VISIBLE);
					} else if (contact.defenceState == Constants.DefenceState.DEFENCE_NO_PERMISSION) {
						holder.getProgress_defence().setVisibility(
								RelativeLayout.GONE);
						holder.getIv_defence_state().setImageResource(
								R.drawable.limit);
						holder.getIv_defence_state().setVisibility(
								RelativeLayout.VISIBLE);
					}
				}
				// 如果是门铃且不是访客密码则获取报警推送账号并判断自己在不在其中，如不在则添加(只执行一次)
				if (deviceType == P2PValue.DeviceType.DOORBELL
						&& contact.defenceState != Constants.DefenceState.DEFENCE_NO_PERMISSION) {
					if (!getIsDoorBellBind(contact.contactId)) {
						getBindAlarmId(contact.contactId, contact.contactPassword);
					} else {

					}
				}

			} else {
				holder.getOnline_state().setText(R.string.offline_state);
				holder.getOnline_state().setTextColor(
						context.getResources().getColor(R.color.text_color_white));
				holder.getIv_defence_state().setVisibility(RelativeLayout.INVISIBLE);
				holder.getProgress_defence().setVisibility(View.GONE);
			}
			// 获得布防状态之后判断弱密码
			if (contact.onLineState == Constants.DeviceState.ONLINE
					&& (contact.defenceState == Constants.DefenceState.DEFENCE_STATE_ON || contact.defenceState == Constants.DefenceState.DEFENCE_STATE_OFF)) {	
				if (Utils.isWeakPassword(contact.userPassword)) {
					holder.getIv_weakpassword().setVisibility(View.VISIBLE);
				} else {
					holder.getIv_weakpassword().setVisibility(View.GONE);
				}
				if(contact.Update==Constants.P2P_SET.DEVICE_UPDATE.HAVE_NEW_VERSION||contact.Update==Constants.P2P_SET.DEVICE_UPDATE.HAVE_NEW_IN_SD){
					holder.getIv_update().setVisibility(ImageView.VISIBLE);
				}else{
					holder.getIv_update().setVisibility(ImageView.GONE);
				}
			}else{
				holder.getIv_weakpassword().setVisibility(View.GONE);
				holder.getIv_update().setVisibility(ImageView.GONE);
			}
			if (deviceType == P2PValue.DeviceType.NPC
					|| deviceType == P2PValue.DeviceType.IPC
					|| deviceType == P2PValue.DeviceType.DOORBELL) {
				holder.getHead().setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if(!ContactFrag.isHideAdd){
							Intent it = new Intent();
							it.setAction(Constants.Action.DIAPPEAR_ADD);
							context.sendBroadcast(it);
							return;
						}
						if(contact.isConnectApWifi==true){
							T.showShort(context,R.string.change_phone_net);
							return;
						}
						LocalDevice localDevice = FList.getInstance()
								.isContactUnSetPassword(contact.contactId);
						if (null != localDevice) {
							Contact saveContact = new Contact();
							saveContact.contactId = localDevice.contactId;
							saveContact.contactType = localDevice.type;
							saveContact.messageCount = 0;
							saveContact.activeUser = NpcCommon.mThreeNum;

							Intent modify = new Intent();
							modify.setClass(context, AddContactNextActivity.class);
							modify.putExtra("isCreatePassword", true);
							modify.putExtra("contact", saveContact);
							String mark = localDevice.address.getHostAddress();
							modify.putExtra(
									"ipFlag",
									mark.substring(mark.lastIndexOf(".") + 1,
											mark.length()));
							context.startActivity(modify);
							return;
						}
						toMonitor(contact);
					}
						

				});
			}else if(deviceType==P2PValue.DeviceType.NVR){
				holder.getHead().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
					 if(!ContactFrag.isHideAdd){
						Intent it = new Intent();
						it.setAction(Constants.Action.DIAPPEAR_ADD);
						context.sendBroadcast(it);
						return;
					 }
//					    隐藏nvr功能
//					 listner.onNvrClick(contact);
					}
				});
				
			}else if (deviceType == P2PValue.DeviceType.PHONE) {
				holder.getHead().setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
//						if(isNeedModifyPwd(contact)){
//							Intent modify_pwd=new Intent(context,ModifyNpcPasswordActivity.class);
//							modify_pwd.putExtra("contact",contact);
//							modify_pwd.putExtra("isWeakPwd",true);
//							context.startActivity(modify_pwd);
//							return;
//						}
						if(!ContactFrag.isHideAdd){
							Intent it = new Intent();
							it.setAction(Constants.Action.DIAPPEAR_ADD);
							context.sendBroadcast(it);
							return;
						}
						if (contact.contactId == null
								|| contact.contactId.equals("")) {
							T.showShort(context, R.string.username_error);
							return;
						}

						Intent call = new Intent();
						call.setClass(context, CallActivity.class);
						call.putExtra("callId", contact.contactId);
						call.putExtra("contact",contact);
						call.putExtra("isOutCall", true);
						call.putExtra("type", Constants.P2P_TYPE.P2P_TYPE_CALL);
						context.startActivity(call);
					}

				});
//				holder.getHeader_icon_play().setVisibility(RelativeLayout.VISIBLE);
			} else {
				holder.getHead().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if(!ContactFrag.isHideAdd){
							Intent it = new Intent();
							it.setAction(Constants.Action.DIAPPEAR_ADD);
							context.sendBroadcast(it);
							return;
						}
						if(contact.isConnectApWifi==true){
							T.showShort(context,R.string.change_phone_net);
						}
						if (Integer.parseInt(contact.contactId) < 256) {
							toMonitor(contact);
						} else {
							holder.getHead().setOnClickListener(null);
						}
						
					}
				});

			}
			holder.getIv_defence_state().setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View arg0) {
								if (contact.defenceState == Constants.DefenceState.DEFENCE_STATE_WARNING_NET
										|| contact.defenceState == Constants.DefenceState.DEFENCE_STATE_WARNING_PWD) {
									holder.getProgress_defence().setVisibility(
											RelativeLayout.VISIBLE);
									holder.getIv_defence_state().setVisibility(
											RelativeLayout.INVISIBLE);
									P2PHandler.getInstance().getDefenceStates(
											contact.contactId, contact.contactPassword);
									FList.getInstance().setIsClickGetDefenceState(
											contact.contactId, true);
								} else if (contact.defenceState == Constants.DefenceState.DEFENCE_STATE_ON) {
									holder.getProgress_defence().setVisibility(
											RelativeLayout.VISIBLE);
									holder.getIv_defence_state().setVisibility(
											RelativeLayout.INVISIBLE);
									P2PHandler
											.getInstance()
											.setRemoteDefence(
													contact.contactId,
													contact.contactPassword,
													Constants.P2P_SET.REMOTE_DEFENCE_SET.ALARM_SWITCH_OFF);
									FList.getInstance().setIsClickGetDefenceState(
											contact.contactId, true);
								} else if (contact.defenceState == Constants.DefenceState.DEFENCE_STATE_OFF) {
									holder.getProgress_defence().setVisibility(
											RelativeLayout.VISIBLE);
									holder.getIv_defence_state().setVisibility(
											RelativeLayout.INVISIBLE);
									P2PHandler
											.getInstance()
											.setRemoteDefence(
													contact.contactId,
													contact.contactPassword,
													Constants.P2P_SET.REMOTE_DEFENCE_SET.ALARM_SWITCH_ON);
									FList.getInstance().setIsClickGetDefenceState(
											contact.contactId, true);
								}
							}

					});
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(!ContactFrag.isHideAdd){
						Intent it = new Intent();
						it.setAction(Constants.Action.DIAPPEAR_ADD);
						context.sendBroadcast(it);
						return;
					}
					LocalDevice localDevice = FList.getInstance()
							.isContactUnSetPassword(contact.contactId);
					if (null != localDevice) {
						Contact saveContact = new Contact();
						saveContact.contactId = localDevice.contactId;
						saveContact.contactType = localDevice.type;
						saveContact.messageCount = 0;
						saveContact.activeUser = NpcCommon.mThreeNum;

						Intent modify = new Intent();
						modify.setClass(context, AddContactNextActivity.class);
						modify.putExtra("isCreatePassword", true);
						modify.putExtra("contact", saveContact);
						String mark = localDevice.address.getHostAddress();
						modify.putExtra(
								"ipFlag",
								mark.substring(mark.lastIndexOf(".") + 1,
										mark.length()));
						context.startActivity(modify);
						return;
					}
				}

			});

			holder.getHead().setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View arg0) {
					// TODO Auto-generated method stub

					NormalDialog dialog = new NormalDialog(context, context
							.getResources().getString(R.string.delete_contact),
							context.getResources().getString(
									R.string.are_you_sure_delete)
									+ " " + contact.contactId + "?", context
									.getResources().getString(R.string.delete),
							context.getResources().getString(R.string.cancel));
					dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {

						@Override
						public void onClick() {
							// TODO Auto-generated method stub
							FList.getInstance().delete(contact, position, handler);
							File file = new File(Constants.Image.USER_HEADER_PATH
									+ NpcCommon.mThreeNum + "/" + contact.contactId);
							Utils.deleteFile(file);
							if (position == 0 && FList.getInstance().size() == 0&&FList.getInstance().apListsize()==0) {
								Intent it = new Intent();
								it.setAction(Constants.Action.DELETE_DEVICE_ALL);
								MyApp.app.sendBroadcast(it);
							}
						}
					});
					dialog.showDialog();
					return true;
				}

			});
			holder.getIv_weakpassword().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent modify_pwd=new Intent(context,ModifyNpcPasswordActivity.class);
					modify_pwd.putExtra("contact",contact);
					modify_pwd.putExtra("isWeakPwd",true);
					context.startActivity(modify_pwd);
				}
			});
			holder.getIv_playback().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(contact.contactType==P2PValue.DeviceType.UNKNOWN){
						return;
					}
				    if(contact.isConnectApWifi==true){
						T.showShort(context,R.string.change_phone_net);
						return;
					}
				    if(contact.onLineState==Constants.DeviceState.ONLINE){
				    	Intent playback = new Intent();
						playback.setClass(context, PlayBackListActivity.class);
						playback.putExtra("contact", contact);
						context.startActivity(playback);
				    }
				}
			});
			holder.getIv_editor().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					DeviceEditor(contact);
				}
			});
			holder.getIv_set().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if(contact.isConnectApWifi==true){
						T.showShort(context,R.string.change_phone_net);
						return;
					}
					if(contact.contactType==P2PValue.DeviceType.NVR){
//    隐藏nvr功能
//						Intent modify_password = new Intent(context,
//								ModifyNpcPasswordActivity.class);
//						modify_password.putExtra("contact", contact);
//						modify_password.putExtra("isModifyNvrPwd", true);
//						context.startActivity(modify_password);
					}else{
						DeviceSettingClick(contact);
					}
				}
			});
			holder.getIv_update().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(contact.contactType==P2PValue.DeviceType.UNKNOWN&&Integer.valueOf(contact.contactId)>256){
						return;
					}
					Intent check_update = new Intent(context,
							DeviceUpdateActivity.class);
					check_update.putExtra("contact", contact);
					check_update.putExtra("isUpdate", true);
					context.startActivity(check_update);
				}
			});
			holder.getIv_call().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stu
					if(contact.isConnectApWifi==true){
						T.showShort(context,R.string.change_phone_net);
						return;
					}
					if(contact.contactType==P2PValue.DeviceType.UNKNOWN&&Integer.valueOf(contact.contactId)>256){
						return;
					}
					 if(contact.contactId==null||contact.contactId.equals("")){
					 T.showShort(context,R.string.username_error);
					 return;
					 }
					 if(contact.contactPassword==null||contact.contactPassword.equals("")){
					 T.showShort(context,R.string.password_error);
					 return;
					 }
					 Intent i=new Intent();
					 i.putExtra("contact",contact);
					 i.setAction(Constants.Action.CALL_DEVICE);
					 context.sendBroadcast(i);
				}
			});
//		}
		
		return view;
		}
//		 else if(position<size1+size2){
//			View view=convertView;
//			final ViewHolder4 holder4;
//			if(view==null){
//				view=LayoutInflater.from(context).inflate(R.layout.list_contact_item_nvr, null);
//				holder4=new ViewHolder4();
//				TextView tv_name=(TextView)view.findViewById(R.id.tv_name);
//				holder4.setName(tv_name);
//				TextView tv_online_state=(TextView)view.findViewById(R.id.tv_online_state);
//				holder4.setOnline_state(tv_online_state);
//				ImageView img_set=(ImageView)view.findViewById(R.id.img_set);
//				holder4.setImg_set(img_set);
//				view.setTag(holder4);
//			}else{
//				holder4=(ViewHolder4) view.getTag();
//			}
//			final Contact contact=FList.getInstance().getNvr(position-size1);
//			holder4.getName().setText(contact.contactName);
//			if(contact.onLineState == Constants.DeviceState.ONLINE){
//				holder4.getOnline_state().setText(contact.onLineState);
//			}else{
//				holder4.getOnline_state().setText(R.string.offline_state);
//			}
//			holder4.getImg_set().setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					
//				}
//			});
//			return view;
//		}
		 else{
			 View view=convertView;
			 final ViewHolder3 holder3;
			 if(view==null){
				 view=LayoutInflater.from(context).inflate(R.layout.list_device_item_no_login, null);
				 holder3=new ViewHolder3();
				 HeaderView head = (HeaderView) view.findViewById(R.id.user_icon);
				 holder3.setHead(head);
				 TextView name = (TextView) view.findViewById(R.id.tv_name);
				 holder3.setName(name);
				 ImageView iv_defence_state=(ImageView)view.findViewById(R.id.iv_defence_state);
				 holder3.setIv_defence_state(iv_defence_state);
					ProgressBar progress_defence=(ProgressBar)view.findViewById(R.id.progress_defence);
					holder3.setProgress_defence(progress_defence);
					ImageView iv_weakpassword=(ImageView)view.findViewById(R.id.iv_weakpassword);
					holder3.setIv_weakpassword(iv_weakpassword);
					ImageView iv_update=(ImageView)view.findViewById(R.id.iv_update);
					holder3.setIv_update(iv_update);
					ImageView iv_playback=(ImageView)view.findViewById(R.id.iv_playback);
					holder3.setIv_playback(iv_playback);
					ImageView iv_set=(ImageView)view.findViewById(R.id.iv_set);
					holder3.setIv_set(iv_set);
					ImageView iv_editor=(ImageView)view.findViewById(R.id.iv_editor);
					holder3.setIv_editor(iv_editor);
					ImageView iv_ap_state=(ImageView)view.findViewById(R.id.iv_ap_state);
					holder3.setIv_ap_state(iv_ap_state);
					LinearLayout l_ap=(LinearLayout)view.findViewById(R.id.l_ap);
					holder3.setL_ap(l_ap);
				    RelativeLayout r_online_state=(RelativeLayout)view.findViewById(R.id.r_online_state);
				    holder3.setR_online_state(r_online_state);
				 view.setTag(holder3);
			 }else{
				 holder3=(ViewHolder3) view.getTag();
				 
			 }
			 final LocalDevice apdevice=FList.getInstance().getAPDdeviceByPosition(position-size);
			 holder3.getR_online_state().setVisibility(View.GONE);
			 holder3.getL_ap().setVisibility(view.VISIBLE);
			 holder3.getIv_weakpassword().setVisibility(View.GONE);
		     holder3.name.setText(apdevice.name);
		     holder3.getHead().updateImage(apdevice.contactId, true);
		     if(WifiUtils.getInstance().isConnectWifi(AppConfig.Relese.APTAG+apdevice.contactId)){
		    	holder3.getIv_ap_state().setBackgroundResource(R.drawable.item_ap_link);
				if (apdevice.defenceState == Constants.DefenceState.DEFENCE_STATE_LOADING) {
					holder3.getProgress_defence().setVisibility(
								RelativeLayout.VISIBLE);
					holder3.getIv_defence_state().setVisibility(
							RelativeLayout.INVISIBLE);
				} else if (apdevice.defenceState == Constants.DefenceState.DEFENCE_STATE_ON) {
					holder3.getProgress_defence().setVisibility(
							RelativeLayout.GONE);
					holder3.getIv_defence_state().setImageResource(
							R.drawable.item_arm);
					holder3.getIv_defence_state().setVisibility(
							RelativeLayout.VISIBLE);

				} else if (apdevice.defenceState == Constants.DefenceState.DEFENCE_STATE_OFF) {
					holder3.getProgress_defence().setVisibility(
							RelativeLayout.GONE);
					holder3.getIv_defence_state().setImageResource(
							R.drawable.item_disarm);
					holder3.getIv_defence_state().setVisibility(
							RelativeLayout.VISIBLE);
				} else if (apdevice.defenceState == Constants.DefenceState.DEFENCE_STATE_WARNING_NET) {
					holder3.getProgress_defence().setVisibility(
							RelativeLayout.GONE);
					holder3.getIv_defence_state().setImageResource(
							R.drawable.ic_defence_warning);
					holder3.getIv_defence_state().setVisibility(
							RelativeLayout.VISIBLE);
				} else if (apdevice.defenceState == Constants.DefenceState.DEFENCE_STATE_WARNING_PWD) {
					holder3.getProgress_defence().setVisibility(
							RelativeLayout.GONE);
					holder3.getIv_defence_state().setImageResource(
							R.drawable.ic_defence_warning);
					holder3.getIv_defence_state().setVisibility(
							RelativeLayout.VISIBLE);
				} else if (apdevice.defenceState == Constants.DefenceState.DEFENCE_NO_PERMISSION) {
					holder3.getProgress_defence().setVisibility(
							RelativeLayout.GONE);
					holder3.getIv_defence_state().setImageResource(
							R.drawable.limit);
					holder3.getIv_defence_state().setVisibility(
							RelativeLayout.VISIBLE);
				}
			 }else{
				 holder3.getIv_ap_state().setBackgroundResource(R.drawable.item_ap_unlick);
				 holder3.getIv_defence_state().setVisibility(View.GONE);
				 holder3.progress_defence.setVisibility(View.GONE);
			 }
		     holder3.getIv_editor().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						
						Contact saveContact = new Contact();
						saveContact.contactId = apdevice.contactId;
						saveContact.contactName = apdevice.name;
						saveContact.contactType = apdevice.type;
						APContact ap=DataManager.findAPContactByActiveUserAndContactId(context, NpcCommon.mThreeNum, apdevice.contactId);
						if(ap!=null){
							saveContact.wifiPassword = ap.Pwd;
						}else{
							saveContact.wifiPassword = "";
						}
						saveContact.contactPassword = "0";
						saveContact.contactFlag=apdevice.flag;
						saveContact.mode=P2PValue.DeviceMode.AP_MODE;
						try {
							saveContact.ipadressAddress=InetAddress.getByName("192.168.1.1");
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
						saveContact.messageCount = 0;
						saveContact.activeUser = NpcCommon.mThreeNum;
						Intent modify = new Intent();
						modify.setClass(context, ModifyContactActivity.class);
						modify.putExtra("contact", saveContact);
						modify.putExtra("isEditorWifiPwd", true);
						context.startActivity(modify);
					}
				});
		     holder3.getIv_defence_state().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//ap设备布撤防单击，完全复制普通设备单击事件
					Contact saveContact = new Contact();
					saveContact.contactId = apdevice.contactId;
					saveContact.contactName = apdevice.name;
					saveContact.contactType = apdevice.type;
					saveContact.contactPassword = "0";
					saveContact.contactFlag=apdevice.flag;
					saveContact.mode=P2PValue.DeviceMode.AP_MODE;
					try {
						saveContact.ipadressAddress=InetAddress.getByName("192.168.1.1");
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
					saveContact.messageCount = 0;
					saveContact.activeUser = NpcCommon.mThreeNum;
					if(saveContact.contactType==P2PValue.DeviceType.IPC){
						if (apdevice.defenceState == Constants.DefenceState.DEFENCE_STATE_WARNING_NET
								|| apdevice.defenceState == Constants.DefenceState.DEFENCE_STATE_WARNING_PWD) {
							holder3.getProgress_defence().setVisibility(
									RelativeLayout.VISIBLE);
							holder3.getIv_defence_state().setVisibility(
									RelativeLayout.INVISIBLE);
							P2PHandler.getInstance().getDefenceStates(
									saveContact.getContactId(), "0");
//							FList.getInstance().setIsClickGetDefenceState(
//									saveContact.contactId, true);
						} else if (apdevice.defenceState == Constants.DefenceState.DEFENCE_STATE_ON) {
							holder3.getProgress_defence().setVisibility(
									RelativeLayout.VISIBLE);
							holder3.getIv_defence_state().setVisibility(
									RelativeLayout.INVISIBLE);
							P2PHandler
									.getInstance()
									.setRemoteDefence(
											saveContact.getContactId(), "0",
											Constants.P2P_SET.REMOTE_DEFENCE_SET.ALARM_SWITCH_OFF);
//							FList.getInstance().setIsClickGetDefenceState(
//									saveContact.contactId, true);
						} else if (apdevice.defenceState == Constants.DefenceState.DEFENCE_STATE_OFF) {
							holder3.getProgress_defence().setVisibility(
									RelativeLayout.VISIBLE);
							holder3.getIv_defence_state().setVisibility(
									RelativeLayout.INVISIBLE);
							P2PHandler
									.getInstance()
									.setRemoteDefence(
											saveContact.getContactId(), "0",
											Constants.P2P_SET.REMOTE_DEFENCE_SET.ALARM_SWITCH_ON);
//							FList.getInstance().setIsClickGetDefenceState(
//									saveContact.contactId, true);
						}
					}
				}
			});
		     holder3.getIv_set().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(WifiUtils.getInstance().isConnectWifi(AppConfig.Relese.APTAG+apdevice.contactId)){
						Contact saveContact = new Contact();
						saveContact.contactId = apdevice.contactId;
						saveContact.contactName = apdevice.name;
						saveContact.contactType = apdevice.type;
						saveContact.contactPassword = "0";
						saveContact.contactFlag=apdevice.flag;
						saveContact.mode=P2PValue.DeviceMode.AP_MODE;
						try {
							saveContact.ipadressAddress=InetAddress.getByName("192.168.1.1");
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
						saveContact.messageCount = 0;
						saveContact.activeUser = NpcCommon.mThreeNum;
						ApDeviceToMainContro(saveContact);
					}else{
						T.showShort(context, R.string.connect_device_wifi);
					}
				}
			});
		     holder3.getIv_playback().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if(WifiUtils.getInstance().isConnectWifi(AppConfig.Relese.APTAG+apdevice.contactId)){
						Contact saveContact = new Contact();
						saveContact.contactId = apdevice.contactId;
						saveContact.contactName = apdevice.name;
						saveContact.contactPassword = "0";
						saveContact.contactType = apdevice.type;
						saveContact.contactFlag=apdevice.flag;
						saveContact.mode=P2PValue.DeviceMode.AP_MODE;
						try {
							saveContact.ipadressAddress=InetAddress.getByName("192.168.1.1");
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
						saveContact.messageCount = 0;
						saveContact.activeUser = NpcCommon.mThreeNum;
						Intent control = new Intent();
						control.setClass(context, PlayBackListActivity.class);
						control.putExtra("contact", saveContact);
						context.startActivity(control);
					}else{
						T.showShort(context, R.string.connect_device_wifi);
					}
				}
			});
		     holder3.getHead().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if(!ContactFrag.isHideAdd){
						Intent it = new Intent();
						it.setAction(Constants.Action.DIAPPEAR_ADD);
						context.sendBroadcast(it);
						return;
					}
					Contact saveContact = new Contact();
					saveContact.contactId = apdevice.contactId;
					saveContact.contactName = apdevice.name;
					saveContact.contactType = apdevice.type;
					saveContact.contactPassword = "0";
					saveContact.contactFlag=apdevice.flag;
					saveContact.mode=P2PValue.DeviceMode.AP_MODE;
					try {
						saveContact.ipadressAddress=InetAddress.getByName("192.168.1.1");
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					saveContact.messageCount = 0;
					saveContact.activeUser = NpcCommon.mThreeNum;
					String mark = apdevice.address.getHostAddress();
					if(WifiUtils.getInstance().isConnectWifi(saveContact.getAPName())){
						//已连接WIFI
						Intent apMonitor=new Intent(context, ApMonitorActivity.class);
						 try {
							 saveContact.ipadressAddress=InetAddress.getByName("192.168.1.1");
						 } catch (UnknownHostException e) {
								e.printStackTrace();
						 }
						 apMonitor.putExtra("contact", saveContact);
						 apMonitor.putExtra("connectType", Constants.ConnectType.RTSPCONNECT);
						 apMonitor.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						 context.startActivity(apMonitor);
					}else{
						//未连接WIFI
						Intent modify = new Intent();
						modify.setClass(context, AddApDeviceActivity.class);
						modify.putExtra("isCreatePassword", false);
						modify.putExtra("isAPModeConnect", 0);
						modify.putExtra("contact", saveContact);
						modify.putExtra("ipFlag","1");
						context.startActivity(modify);	
					}
						
//					}
//					
				}
			});
//		 	holder3.getIv_set().setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					if(WifiUtils.getInstance().isConnectWifi(AppConfig.Relese.APTAG+apdevice.contactId)){
//						Intent control = new Intent();
//						control.setClass(context, MainControlActivity.class);
//						Contact c = new Contact();
//						c.contactName=apdevice.name;
//						c.contactPassword="0";
////						c.userPassword=apdevice.userPassword;
//						c.messageCount=0;
//						c.contactId="1";
////						c.activeUser=apdevice.activeUser;
//						c.contactType=P2PValue.DeviceType.IPC;
////						c.defenceState=apdevice.defenceState;
//						try {
//							c.ipadressAddress=InetAddress.getByName("192.168.1.1");
//						} catch (UnknownHostException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						c.mode=P2PValue.DeviceMode.AP_MODE;
//						control.putExtra("contact", c);
//						control.putExtra("connectType", 1);
//						control.putExtra("type", P2PValue.DeviceType.IPC);
//						context.startActivity(control);
//					}else{
//						T.showShort(context, R.string.connect_device_wifi);
//					}
//				}
//			});
//			holder3.getIv_playback().setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					if(WifiUtils.getInstance().isConnectWifi(AppConfig.Relese.APTAG+apdevice.contactId)){
//						Intent control = new Intent();
//						control.setClass(context, PlayBackListActivity.class);
//						Contact c = new Contact();
//						c.contactName=apdevice.name;
//						c.contactPassword="0";
////						c.userPassword=apdevice.userPassword;
//						c.messageCount=0;
//						c.contactId="1";
////						c.activeUser=apdevice.activeUser;
//						c.contactType=P2PValue.DeviceType.IPC;
////						c.defenceState=apdevice.defenceState;
//						try {
//							c.ipadressAddress=InetAddress.getByName("192.168.1.1");
//						} catch (UnknownHostException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						c.mode=P2PValue.DeviceMode.AP_MODE;
//						control.putExtra("contact", c);
//						context.startActivity(control);
//					}else{
//						T.showShort(context, R.string.connect_device_wifi);
//					}
//				}
//			});
//			holder3.getIv_defence_state().setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					if (apdevice.defenceState == Constants.DefenceState.DEFENCE_STATE_WARNING_NET
//							|| apdevice.defenceState == Constants.DefenceState.DEFENCE_STATE_WARNING_PWD) {
//						holder3.getProgress_defence().setVisibility(
//								RelativeLayout.VISIBLE);
//						holder3.getIv_defence_state().setVisibility(
//								RelativeLayout.INVISIBLE);
//						P2PHandler.getInstance().getDefenceStates(
//								"1", "0");
//						FList.getInstance().setIsClickGetDefenceState(
//								apdevice.contactId, true);
//					} else if (apdevice.defenceState == Constants.DefenceState.DEFENCE_STATE_ON) {
//						holder3.getProgress_defence().setVisibility(
//								RelativeLayout.VISIBLE);
//						holder3.getIv_defence_state().setVisibility(
//								RelativeLayout.INVISIBLE);
//						P2PHandler
//								.getInstance()
//								.setRemoteDefence(
//										"1",
//										"0",
//										Constants.P2P_SET.REMOTE_DEFENCE_SET.ALARM_SWITCH_OFF);
//						APList.getInstance().setIsClickGetDefenceState(
//								apdevice.contactId, true);
//					} else if (apdevice.defenceState == Constants.DefenceState.DEFENCE_STATE_OFF) {
//						holder3.getProgress_defence().setVisibility(
//								RelativeLayout.VISIBLE);
//						holder3.getIv_defence_state().setVisibility(
//								RelativeLayout.INVISIBLE);
//						P2PHandler
//								.getInstance()
//								.setRemoteDefence(
//										"1",
//										"0",
//										Constants.P2P_SET.REMOTE_DEFENCE_SET.ALARM_SWITCH_ON);
//						APList.getInstance().setIsClickGetDefenceState(
//								apdevice.contactId, true);
//					}
//				}
//			});
		     return view;
		 }
		// } else {
		// // 显示没有设置密码的的设备
		// View view = convertView;
		// final ViewHolder2 holder2;
		// if (view == null) {
		// view = LayoutInflater.from(context).inflate(
		// R.layout.list_contact_item2, null);
		// holder2 = new ViewHolder2();
		// TextView name = (TextView) view.findViewById(R.id.user_name);
		// holder2.setName(name);
		// ImageView typeImg = (ImageView) view
		// .findViewById(R.id.login_type);
		// holder2.setDevice_type(typeImg);
		// view.setTag(holder2);
		// } else {
		// holder2 = (ViewHolder2) view.getTag();
		// }
		// final LocalDevice localDevice =
		// FList.getInstance().getUnsetPasswordLocalDevices().get(position -
		// size1);
		// holder2.name.setText(localDevice.getContactId());
		// switch (localDevice.getType()) {
		// case P2PValue.DeviceType.NPC:
		// holder2.device_type
		// .setImageResource(R.drawable.ic_device_type_npc);
		// break;
		// case P2PValue.DeviceType.IPC:
		// holder2.device_type
		// .setImageResource(R.drawable.ic_device_type_ipc);
		// break;
		// case P2PValue.DeviceType.DOORBELL:
		// holder2.device_type
		// .setImageResource(R.drawable.ic_device_type_door_bell);
		// break;
		// case P2PValue.DeviceType.UNKNOWN:
		// holder2.device_type
		// .setImageResource(R.drawable.ic_device_type_unknown);
		// break;
		// default:
		// holder2.device_type
		// .setImageResource(R.drawable.ic_device_type_unknown);
		// break;
		// }
		// view.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // 跳转到设置密码页面
		// Contact saveContact = new Contact();
		// saveContact.contactId = localDevice.contactId;
		// saveContact.contactType = localDevice.type;
		// saveContact.messageCount = 0;
		// saveContact.activeUser = NpcCommon.mThreeNum;
		// Intent modify = new Intent();
		// modify.setClass(context, AddContactNextActivity.class);
		// modify.putExtra("isCreatePassword", true);
		// modify.putExtra("contact", saveContact);
		// String mark = localDevice.address.getHostAddress();
		// modify.putExtra("ipFlag", mark.substring(mark.lastIndexOf(".")+1,
		// mark.length()));
		// context.startActivity(modify);
		//
		// }
		// });
		// return view;
		// }
	}
	
	/**
	 * ap设备跳转到设置页
	 * @param contact
	 */
	private void ApDeviceToMainContro(Contact contact){
		Intent control = new Intent();
		control.setClass(context, MainControlActivity.class);
		Contact c = new Contact();
		c.contactName=contact.contactName;
		c.contactPassword=contact.contactPassword;
		c.userPassword=contact.userPassword;
		c.messageCount=contact.messageCount;
		c.contactId="1";
		c.activeUser=contact.activeUser;
		c.contactType=contact.contactType;
		c.defenceState=contact.defenceState;
		c.ipadressAddress=contact.ipadressAddress;
		c.mode=contact.mode;
		control.putExtra("contact", c);
		control.putExtra("connectType", 1);
		control.putExtra("type", P2PValue.DeviceType.IPC);
		context.startActivity(control);
	}
	
	/**
	 * 设置按钮单击跳转流程
	 * @param contact
	 */
	private void DeviceSettingClick(Contact contact){
//		if(isNeedModifyPwd(contact)){
//			Intent modify_pwd=new Intent(context,ModifyNpcPasswordActivity.class);
//			modify_pwd.putExtra("contact",contact);
//			modify_pwd.putExtra("isWeakPwd",true);
//			context.startActivity(modify_pwd);
//			return;
//		}
		if(contact.contactType==P2PValue.DeviceType.UNKNOWN&&Integer.valueOf(contact.contactId)>256){
			return;
		}
		if(contact.contactId==null||contact.contactId.equals("")){
			T.showShort(context,R.string.username_error);
			return;
		}
		if(contact.contactPassword==null||contact.contactPassword.equals("")){
			T.showShort(context,R.string.password_error);
			return;
		}
		Intent i=new Intent();
		i.putExtra("contact",contact);
		i.setAction(Constants.Action.ENTER_DEVICE_SETTING);
		context.sendBroadcast(i);
	}
	/**
	 * 普通模式下的编辑
	 * @param contact
	 */
	private void DeviceEditor(Contact contact) {
		LocalDevice localDevice = FList.getInstance()
				.isContactUnSetPassword(contact.contactId);
		//判断局域网密码
		if (null != localDevice) {
			Contact saveContact = new Contact();
			saveContact.contactId = localDevice.contactId;
			saveContact.contactType = localDevice.type;
			saveContact.messageCount = 0;
			saveContact.activeUser = NpcCommon.mThreeNum;

			Intent modify = new Intent();
			modify.setClass(context, AddContactNextActivity.class);
			modify.putExtra("isCreatePassword", true);
			modify.putExtra("contact", saveContact);
			String mark = localDevice.address.getHostAddress();
			modify.putExtra(
					"ipFlag",
					mark.substring(mark.lastIndexOf(".") + 1,
							mark.length()));
			context.startActivity(modify);
			return;
		}else{
			Intent modify = new Intent();
			modify.setClass(context, ModifyContactActivity.class);
			modify.putExtra("contact", contact);
			modify.putExtra("isEditorWifiPwd", false);
			context.startActivity(modify);
		}
	}
	/**
	 * 普通设备去监控
	 * @param contact
	 */
	private void toMonitor(Contact contact){
//		if(isNeedModifyPwd(contact)){
//			Intent modify_pwd=new Intent(context,ModifyNpcPasswordActivity.class);
//			modify_pwd.putExtra("contact",contact);
//			modify_pwd.putExtra("isWeakPwd",true);
//			context.startActivity(modify_pwd);
//			return;
//		}
		Log.e("toMonitor", "toMonitor");
		if (null != FList.getInstance().isContactUnSetPassword(
				contact.contactId)) {
			return;
		}
		if (contact.contactId == null
				|| contact.contactId.equals("")) {
			T.showShort(context, R.string.username_error);
			return;
		}
		if (contact.contactPassword == null
				|| contact.contactPassword.equals("")) {
			T.showShort(context, R.string.password_error);
			return;
		}
		Intent monitor = new Intent();
		monitor.setClass(context, ApMonitorActivity.class);
		monitor.putExtra("contact", contact);
		monitor.putExtra("connectType",Constants.ConnectType.P2PCONNECT);
		monitor.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(monitor);
	}
	

	Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			notifyDataSetChanged();
			return true;
		}
	});

	List<String> doorbells = new ArrayList<String>();
	Map<String, String[]> idMaps = new HashMap<String, String[]>();

	private void getBindAlarmId(String id, String password) {
		if (!doorbells.contains(id)) {
			doorbells.add(id);
		}
		P2PHandler.getInstance().getBindAlarmId(id, password);
	}
    public boolean isNeedModifyPwd(Contact contact){ 
    	
    	if (contact.onLineState == Constants.DeviceState.ONLINE
				&& (contact.defenceState == Constants.DefenceState.DEFENCE_STATE_ON || contact.defenceState == Constants.DefenceState.DEFENCE_STATE_OFF)) {	
			if(Utils.isWeakPassword(contact.userPassword)){
				return true;
			}		
		}
    	return false;
    }
	public void getAllBindAlarmId() {
		for (String ss : doorbells) {
			getBindAlarmId(ss);
		}
	}

	private int count = 0;// 总请求数计数器
	private int SumCount = 20;// 总请求次数上限

	public void getBindAlarmId(String id) {

		Contact contact = DataManager.findContactByActiveUserAndContactId(
				context, NpcCommon.mThreeNum, id);
		if (contact != null && count <= SumCount) {
			// 获取绑定id列表
			P2PHandler.getInstance().getBindAlarmId(contact.contactId,
					contact.contactPassword);
			count++;
		}
	}

	public void setBindAlarmId(String id, String[] ids) {
		int ss = 0;
		String[] new_data;
		for (int i = 0; i < ids.length; i++) {
			if (!NpcCommon.mThreeNum.equals(ids[i])) {
				ss++;
			}
		}
		if (ss == ids.length) {
			// 不包含则设置
			new_data = new String[ids.length + 1];
			for (int i = 0; i < ids.length; i++) {
				new_data[i] = ids[i];
			}
			new_data[new_data.length - 1] = NpcCommon.mThreeNum;
			Contact contact = DataManager.findContactByActiveUserAndContactId(
					context, NpcCommon.mThreeNum, id);
			P2PHandler.getInstance().setBindAlarmId(contact.contactId,
					contact.contactPassword, new_data.length, new_data);
		} else {
			new_data = ids;
		}
		idMaps.put(id, new_data);

	}

	public void setBindAlarmId(String Id) {
		Contact contact = DataManager.findContactByActiveUserAndContactId(
				context, NpcCommon.mThreeNum, Id);
		if (contact != null && (!idMaps.isEmpty())) {
			String[] new_data = idMaps.get(Id);
			P2PHandler.getInstance().setBindAlarmId(contact.contactId,
					contact.contactPassword, new_data.length, new_data);
		}

	}

	public void setBindAlarmIdSuccess(String doorbellid) {
		SharedPreferencesManager.getInstance().putIsDoorbellBind(doorbellid,
				true, context);
	}

	private boolean getIsDoorBellBind(String doorbellid) {
		return SharedPreferencesManager.getInstance().getIsDoorbellBind(
				context, doorbellid);
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
	public onConnectListner listner;
	public interface onConnectListner{
		void onNvrClick(Contact contact);
	}
	public void setOnSrttingListner(onConnectListner listner){
		this.listner=listner;
	}
}
