package com.jwkj.widget;

import java.util.ArrayList;
import java.util.List;

import com.yoosee.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

public class EmailPopwindow extends PopupWindow {
	private View conentView;
	private ListView lvEmailList;
	private List<String> listEmail = new ArrayList<String>();
	private onSelected selected;

	public EmailPopwindow(final Activity context, List<String> listEmail) {
		this.listEmail = listEmail;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		conentView = inflater.inflate(R.layout.popwindow_email, null);
		int h = context.getWindowManager().getDefaultDisplay().getHeight();
		int w = context.getWindowManager().getDefaultDisplay().getWidth();
		// 设置SelectPicPopupWindow的View
		this.setContentView(conentView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(w / 2 + 50);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		// 刷新状态
		this.update();
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0000000000);
		// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
		this.setBackgroundDrawable(dw);
		// mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.popdown_up);
		lvEmailList = (ListView) conentView.findViewById(R.id.lv_emaillist);
		ArrayAdapter adpter = new ArrayAdapter<String>(context,
				android.R.layout.simple_expandable_list_item_1, listEmail);
		lvEmailList.setAdapter(adpter);
		lvEmailList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selected.selected(position);
				popDismiss();

			}
		});
	}

	/**
	 * 显示popupWindow
	 * 
	 * @param parent
	 */
	public void showPopupWindow(View parent) {
		if (!this.isShowing()) {
			// 以下拉方式显示popupwindow
			this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);
		} else {
			this.dismiss();
		}
	}

	private void popDismiss() {
		if (this.isShowing()) {
			this.dismiss();
		}
	}

	public interface onSelected {
		void selected(int position);

	}

	public void setonSelectedListener(onSelected listener) {
		this.selected = listener;
	}

	public void setSelection(int position) {
		lvEmailList.setSelection(position);
	}

	class EmailAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listEmail.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listEmail.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}

	}
}
