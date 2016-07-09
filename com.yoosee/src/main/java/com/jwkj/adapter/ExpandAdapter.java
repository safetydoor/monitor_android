package com.jwkj.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandAdapter extends BaseExpandableListAdapter {
	private Context mContext;
	List<String> group;
	List<List<String>> child;

	public ExpandAdapter(Context mContext) {
		this.mContext = mContext;
		group = new ArrayList<String>();
		child = new ArrayList<List<String>>();

		addInfo("Andy", new String[] { "male", "138123***", "GuangZhou" });
		addInfo("Fairy", new String[] { "female", "138123***", "GuangZhou" });
	}

	private void addInfo(String g, String[] c) {
		group.add(g);
		List<String> childitem = new ArrayList<String>();
		for (int i = 0; i < c.length; i++) {
			childitem.add(c[i]);
		}
		child.add(childitem);
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return child.get(arg0).get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return arg1;
	}

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		// TODO Auto-generated method stub
		String string = child.get(arg0).get(arg1);
		return getGenericView(string);
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return child.get(arg0).size();
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return group.get(arg0);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return group.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		// TODO Auto-generated method stub
		String string = group.get(arg0);
		return new TextView(mContext);
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	public TextView getGenericView(String s) {
		// Layout parameters for the ExpandableListView
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, 120);

		TextView text = new TextView(mContext);
		text.setLayoutParams(lp);
		// Center the text vertically
		text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		// Set the text starting position
		text.setPadding(36, 0, 0, 0);

		text.setText(s);
		return text;
	}
}
