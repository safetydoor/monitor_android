package com.jwkj.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yoosee.R;
import com.jwkj.data.Contact;
import com.jwkj.data.Message;
import com.jwkj.global.Constants;
import com.jwkj.global.FList;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.Utils;
import com.jwkj.widget.HeaderView;

/**
 * 消息ListView的Adapter
 * 
 * @author way
 */
public class MessageAdapter extends BaseAdapter {
	private Context context;
	private List<Message> lists;

	public MessageAdapter(Context context, List<Message> lists) {
		this.context = context;
		this.lists = lists;
	}

	class ViewHolder {

		public TextView sendTime;

		public TextView getSendTime() {
			return sendTime;
		}

		public void setSendTime(TextView sendTime) {
			this.sendTime = sendTime;
		}

		public TextView getUserName() {
			return userName;
		}

		public void setUserName(TextView userName) {
			this.userName = userName;
		}

		public TextView getContent() {
			return content;
		}

		public void setContent(TextView content) {
			this.content = content;
		}

		public HeaderView getHead_img() {
			return head_img;
		}

		public void setHead_img(HeaderView head_img) {
			this.head_img = head_img;
		}

		public TextView userName;
		public TextView content;
		public HeaderView head_img;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return lists.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getItemViewType(int position) {
		Message msg = lists.get(position);
		if (isComming(msg)) {
			return 0;
		} else {
			return 1;
		}

	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View view = arg1;
		ViewHolder holder;

		Message msg = lists.get(arg0);
		boolean isCommingMsg = isComming(msg);

		if (null == view) {
			if (isCommingMsg) {
				Log.e("my", "inflater left");
				view = LayoutInflater.from(context).inflate(
						R.layout.message_left, null);

			} else {
				Log.e("my", "inflater right");
				view = LayoutInflater.from(context).inflate(
						R.layout.message_right, null);
			}

			holder = new ViewHolder();
			holder.setHead_img((HeaderView) view.findViewById(R.id.iv_userhead));
			holder.setUserName((TextView) view.findViewById(R.id.tv_username));
			holder.setContent((TextView) view.findViewById(R.id.tv_chatcontent));
			holder.setSendTime((TextView) view.findViewById(R.id.tv_sendtime));

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if (isCommingMsg) {
			Contact contact = FList.getInstance().isContact(msg.fromId);
			holder.getHead_img().updateImage(msg.fromId, false);
			if (null != contact) {
				holder.getUserName().setText(contact.contactName);
			} else {
				holder.getUserName().setText(msg.fromId);
			}
		} else {
			holder.getHead_img().updateImage(NpcCommon.mThreeNum, false);
			holder.getUserName().setText(R.string.me);
		}

		holder.getContent().setText(msg.msg);
		if (Integer.parseInt(msg.msgState) == Constants.MessageType.SENDING) {
			holder.getSendTime().setText(R.string.sending);
		} else if (Integer.parseInt(msg.msgState) == Constants.MessageType.SEND_FAULT) {
			holder.getSendTime().setText(R.string.send_fault);
		} else {
			holder.getSendTime().setText(
					Utils.ConvertTimeByLong(Long.parseLong(msg.msgTime)));
		}
		return view;
	}

	public boolean isComming(Message msg) {
		if (msg.fromId.equals(msg.activeUser)) {
			return false;
		} else {
			return true;
		}
	}

	public void updateData(List<Message> lists) {
		this.lists = lists;
	}

}
