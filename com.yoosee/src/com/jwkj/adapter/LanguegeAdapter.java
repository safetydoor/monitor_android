package com.jwkj.adapter;

import com.jwkj.adapter.RecordAdapter.ViewHolder;
import com.jwkj.global.Constants;
import com.yoosee.R;

import android.R.raw;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

public class LanguegeAdapter extends BaseAdapter {
	private Context context;
	private int languegecount;
	private int curlanguege;
	private int[] langueges;

	public LanguegeAdapter(Context context, int languegecount, int curlanguege,
			int[] langueges) {
		this.context = context;
		this.languegecount = languegecount;
		this.curlanguege = curlanguege;
		this.langueges = langueges;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return languegecount;
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

	class ViewHolder {
		public TextView tv_languege;
		public ImageView img_languege;

		public TextView getTv_languege() {
			return tv_languege;
		}

		public void setTv_languege(TextView tv_languege) {
			this.tv_languege = tv_languege;
		}

		public ImageView getImg_languege() {
			return img_languege;
		}

		public void setImg_languege(ImageView img_languege) {
			this.img_languege = img_languege;
		}

	}

	@Override
	public View getView(final int position, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View view = arg1;
		final ViewHolder holder;
		if (null == view) {
			view = LayoutInflater.from(context).inflate(
					R.layout.list_languege_item, null);
			holder = new ViewHolder();
			holder.setTv_languege((TextView) view
					.findViewById(R.id.tv_languege));
			holder.setImg_languege((ImageView) view
					.findViewById(R.id.img_languege));
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.getTv_languege().setText(getLanguege(langueges[position]));
		if (langueges[position] != curlanguege) {
			holder.getImg_languege().setBackgroundResource(
					R.drawable.not_select);
		} else {
			holder.getImg_languege().setBackgroundResource(R.drawable.select);
		}
		return view;
	}

	public void notifydata(int position) {
		this.curlanguege = position;
		notifyDataSetChanged();
	}

	public String getLanguege(int l) {
		switch (l) {
		case Constants.Languege.LANG_EN:
			return context.getResources().getString(R.string.english);
		case Constants.Languege.LANG_CHS:
			return context.getResources()
					.getString(R.string.chinese_simplified);
		case Constants.Languege.LANG_CF:
			return context.getResources().getString(
					R.string.chinese_traditional);
		default:
			return "";
		}
	}

}
