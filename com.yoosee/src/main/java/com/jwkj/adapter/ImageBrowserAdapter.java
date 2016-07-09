package com.jwkj.adapter;

import java.io.File;
import java.io.FileFilter;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.yoosee.R;
import com.jwkj.activity.ImageBrowser;
import com.jwkj.activity.ImageSeeActivity;
import com.jwkj.widget.MyImageView;
import com.jwkj.widget.NormalDialog;

public class ImageBrowserAdapter extends BaseAdapter {
	File[] data;
	Context context;

	public ImageBrowserAdapter(Context context) {
		this.context = context;

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
		if (null == data) {
			data = new File[0];
		}
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

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		RelativeLayout view = (RelativeLayout) arg1;
		if (null == view) {
			view = (RelativeLayout) LayoutInflater.from(context).inflate(
					R.layout.list_imgbrowser_item, null);

		}
		String path = data[arg0].getPath();
		MyImageView img = (MyImageView) view.findViewById(R.id.img);
		img.setImageFilePath(path);
		// TextView text = (TextView) view.findViewById(R.id.text);
		// text.setText(fileName);

		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				// ((ImageBrowser)context).createGalleryDialog(arg0);
				Intent intent = new Intent();
				intent.setClass(context, ImageSeeActivity.class);
				intent.putExtra("currentImage", arg0);
				context.startActivity(intent);
			}

		});

		view.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View view) {
				// TODO Auto-generated method stub
				NormalDialog dialog = new NormalDialog(context, context
						.getResources().getString(R.string.delete), context
						.getResources().getString(R.string.confirm_delete),
						context.getResources().getString(R.string.delete),
						context.getResources().getString(R.string.cancel));
				dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {

					@Override
					public void onClick() {
						// TODO Auto-generated method stub
						File f = data[arg0];
						try {
							f.delete();
							updateData();
						} catch (Exception e) {
							Log.e("my",
									"delete file error->ImageBrowserAdapter.java");
						}
					}
				});
				dialog.showDialog();
				return true;
			}

		});
		Log.e("my", Runtime.getRuntime().totalMemory() + "");
		return view;
	}

	public void updateData() {
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
		File[] files = file.listFiles(filter);
		data = files;
		notifyDataSetChanged();
		// ((ImageBrowser)context).updateGalleryData(files);
	}
}
