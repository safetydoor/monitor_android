package com.jwkj.adapter;

import java.io.File;
import java.io.FileFilter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.yoosee.R;
import com.jwkj.utils.Utils;
import com.jwkj.widget.MyImageView;

public class GalleryAdapter extends BaseAdapter {
	File[] data;
	Context context;
	int screenWidth;
	int selectedItemId;

	public GalleryAdapter(Context context, int screenWidth) {
		this.context = context;
		this.screenWidth = screenWidth;
		String path = Environment.getExternalStorageDirectory().getPath()
				+ "/screenshot";
		File file = new File(path);
		FileFilter filter = new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				// TODO Auto-generated method stub
				if (pathname.getName().endsWith(".jpg")) {
					return true;
				} else {
					return false;
				}

			}
		};
		data = file.listFiles(filter);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.length;
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

	public void setSelectedItem(int selectedItemId) {
		if (this.selectedItemId != selectedItemId) {
			this.selectedItemId = selectedItemId;
			notifyDataSetChanged();
		}
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		RelativeLayout view = (RelativeLayout) arg1;
		if (null == view) {
			view = (RelativeLayout) LayoutInflater.from(context).inflate(
					R.layout.list_imgbrowser_item, null);

		}
		String path = ((File) data[position]).getPath();

		MyImageView img = (MyImageView) view.findViewById(R.id.img);
		RelativeLayout.LayoutParams params = (LayoutParams) img
				.getLayoutParams();
		params.width = screenWidth / 5;
		params.height = screenWidth / 5;
		img.setLayoutParams(params);

		if (selectedItemId == position) {
			/*
			 * Animation anim = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
			 * Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
			 * 0.5f); anim.setDuration(200); anim.setFillAfter(true);
			 */
			Bitmap bitmap = BitmapFactory.decodeFile(path);
			Bitmap frame = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.frame);

			img.setImageBitmap(Utils.montageBitmap(frame, bitmap, 200, 200));

			int selectedWidth = (int) (screenWidth / 5 * 1.4);
			RelativeLayout.LayoutParams selectedParams = (LayoutParams) img
					.getLayoutParams();
			selectedParams.width = selectedWidth;
			selectedParams.height = selectedWidth;
			img.setLayoutParams(selectedParams);

		} else {
			Bitmap bitmap = BitmapFactory.decodeFile(path);
			img.setImageBitmap(bitmap);
			img.setLayoutParams(params);
		}
		// TextView text = (TextView) view.findViewById(R.id.text);
		// text.setText(fileName);
		Log.e("my", Runtime.getRuntime().totalMemory() + "");
		return view;
	}

	public void updateData(File[] files) {
		data = files;
		notifyDataSetChanged();
	}
}
