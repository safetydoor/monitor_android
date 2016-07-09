package com.jwkj.adapter;

import java.util.List;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yoosee.R;
import com.jwkj.data.Contact;
import com.jwkj.fragment.KeyboardFrag;

public class FilterUserAdapter extends BaseAdapter {
	private Context context;
	private List<Contact> data;

	public FilterUserAdapter(Context context, List<Contact> data) {
		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
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
	public View getView(int position, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View view = arg1;
		if (null == view) {
			view = LayoutInflater.from(context).inflate(
					R.layout.list_filter_user_item, null);
		}

		TextView name = (TextView) view.findViewById(R.id.name);
		TextView account = (TextView) view.findViewById(R.id.account);
		Contact contact = data.get(position);
		name.setText(contact.contactName);
		SpannableStringBuilder builder = new SpannableStringBuilder(
				contact.contactId);
		ForegroundColorSpan greenSpan = new ForegroundColorSpan(context
				.getResources().getColor(R.color.text_color_blue));
		// builder.setSpan(greenSpan,
		// contact.contactId.indexOf(KeyboardFrag.searchTellNum),
		// contact.contactId.indexOf(KeyboardFrag.searchTellNum)+KeyboardFrag.searchTellNum.length(),
		// Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		account.setText(builder);

		return view;
	}

	public void upDateData(List<Contact> data) {
		this.data = data;
		notifyDataSetChanged();
	}

	public void clear() {
		this.data.clear();
		notifyDataSetChanged();
	}
}
