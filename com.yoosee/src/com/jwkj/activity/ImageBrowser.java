package com.jwkj.activity;

import java.io.File;
import java.io.FileFilter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ViewSwitcher.ViewFactory;

import com.yoosee.R;
import com.jwkj.adapter.GalleryAdapter;
import com.jwkj.adapter.ImageBrowserAdapter;
import com.jwkj.global.Constants;

public class ImageBrowser extends BaseActivity implements OnClickListener {
	File[] files;
	GridView list;
	ImageBrowserAdapter adapter;
	// GalleryAdapter gAdapter;
	ImageView back;
	private AlertDialog mDeleteDialog;
	Context context;
	int screenWidth, screenHeight;
	Bitmap mTempBitmap;

	int length;
	int selectedItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_browser);
		context = this;
		// String path =
		// Environment.getExternalStorageDirectory().getPath()+"/screenshot";
		// File file = new File(path);
		// FileFilter filter = new FileFilter(){
		//
		// @Override
		// public boolean accept(File pathname) {
		// // TODO Auto-generated method stub
		// if(pathname.getName().endsWith(".jpg")){
		// return true;
		// }else{
		// return false;
		// }
		//
		// }
		// };
		// File[] files = file.listFiles(filter);
		if (null == files) {
			files = new File[0];
		}
		screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		screenHeight = getWindowManager().getDefaultDisplay().getHeight();
		initComponent();

	}

	public void initComponent() {
		list = (GridView) findViewById(R.id.list_grid);
		back = (ImageView) findViewById(R.id.back_btn);
		DisplayMetrics dm = new DisplayMetrics();
		adapter = new ImageBrowserAdapter(this);
		// gAdapter = new GalleryAdapter(this,screenWidth);
		list.setAdapter(adapter);
		back.setOnClickListener(this);
	}

	// public void createGalleryDialog(final int position) {
	// LayoutInflater factor = (LayoutInflater) context
	// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	// View view = LayoutInflater.from(context).inflate(R.layout.dialog_gallery,
	// null);
	// //final Gallery gallery = (Gallery) view.findViewById(R.id.list_gallery);
	// //gAdapter = new GalleryAdapter(this,screenWidth);
	// //gallery.setAdapter(gAdapter);
	// //gallery.setSelection(position);
	// String screenshotPath =
	// Environment.getExternalStorageDirectory().getPath()+"/screenshot";
	// File file = new File(screenshotPath);
	// FileFilter filter = new FileFilter(){
	//
	// @Override
	// public boolean accept(File pathname) {
	// // TODO Auto-generated method stub
	// if(pathname.getName().endsWith(".jpg")){
	// return true;
	// }else{
	// return false;
	// }
	//
	// }
	// };
	// files = file.listFiles(filter);
	//
	// String path = ((File)files[position]).getPath();
	// mTempBitmap = BitmapFactory.decodeFile(path);
	// selectedItem = position;
	// final ImageSwitcher switcher = (ImageSwitcher)
	// view.findViewById(R.id.img_container);
	//
	//
	// switcher.setFactory(new ViewFactory(){
	//
	// @Override
	// public View makeView() {
	// // TODO Auto-generated method stub
	// ImageView view = new ImageView(context);
	// view.setScaleType(ScaleType.FIT_CENTER);
	// view.setLayoutParams(new
	// ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
	// Log.e("my",Runtime.getRuntime().totalMemory()+"");
	// return view;
	// }
	//
	// });
	// switcher.setImageDrawable(new BitmapDrawable(mTempBitmap));
	// final GestureDetector gd = new GestureDetector(new OnGestureListener(){
	//
	// @Override
	// public boolean onDown(MotionEvent arg0) {
	// // TODO Auto-generated method stub
	// return false;
	// }
	//
	// @Override
	// public boolean onFling(MotionEvent arg0, MotionEvent arg1,
	// float arg2, float arg3) {
	// // TODO Auto-generated method stub
	// float x1 = arg0.getRawX();
	// float x2 = arg1.getRawX();
	// float distance = x1 - x2;
	// if((distance>0)&&(Math.abs(distance)>30)){
	// if(++selectedItem<files.length){
	// switcher.setInAnimation(AnimationUtils.loadAnimation(context,
	// R.anim.slide_in_right_100));
	// switcher.setOutAnimation(AnimationUtils.loadAnimation(context,
	// R.anim.slide_out_left_100));
	//
	// String path = ((File)files[selectedItem]).getPath();
	// mTempBitmap = BitmapFactory.decodeFile(path);
	// switcher.setImageDrawable(new BitmapDrawable(mTempBitmap));
	// }else{
	// selectedItem = files.length-1;
	// }
	//
	// Log.e("my",Runtime.getRuntime().totalMemory()+"");
	// }else if((distance<0)&&(Math.abs(distance)>30)){
	// if(--selectedItem>=0){
	// switcher.setInAnimation(AnimationUtils.loadAnimation(context,
	// R.anim.slide_in_left_100));
	// switcher.setOutAnimation(AnimationUtils.loadAnimation(context,
	// R.anim.slide_out_right_100));
	// String path = ((File)files[selectedItem]).getPath();
	// mTempBitmap = BitmapFactory.decodeFile(path);
	// switcher.setImageDrawable(new BitmapDrawable(mTempBitmap));
	// }else{
	// selectedItem = 0;
	// }
	//
	// Log.e("my",Runtime.getRuntime().totalMemory()+"");
	// }
	// return true;
	// }
	//
	// @Override
	// public void onLongPress(MotionEvent arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public boolean onScroll(MotionEvent arg0, MotionEvent arg1,
	// float arg2, float arg3) {
	// // TODO Auto-generated method stub
	// return false;
	// }
	//
	// @Override
	// public void onShowPress(MotionEvent arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public boolean onSingleTapUp(MotionEvent arg0) {
	// // TODO Auto-generated method stub
	// return false;
	// }
	//
	// });
	// switcher.setOnTouchListener(new OnTouchListener(){
	//
	// @Override
	// public boolean onTouch(View arg0, MotionEvent arg1) {
	// // TODO Auto-generated method stub
	// gd.onTouchEvent(arg1);
	// return true;
	// }
	//
	// });

	// gallery.setOnTouchListener(new OnTouchListener(){
	//
	// @Override
	// public boolean onTouch(View arg0, MotionEvent arg1) {
	// // TODO Auto-generated method stub
	// switcher.setInAnimation(AnimationUtils.loadAnimation(context,
	// android.R.anim.fade_in));
	// switcher.setOutAnimation(AnimationUtils.loadAnimation(context,
	// android.R.anim.fade_out));
	// return false;
	// }
	//
	// });
	// gallery.setOnItemSelectedListener(new OnItemSelectedListener(){
	//
	// @Override
	// public void onItemSelected(AdapterView<?> arg0, View img,
	// int arg2, long arg3) {
	// // TODO Auto-generated method stub
	// String path = ((File)files[arg2]).getPath();
	// if(bitmap!=null&&!bitmap.isRecycled()){
	// //bitmap.recycle();
	// bitmap = BitmapFactory.decodeFile(path);
	// Log.e("my",Runtime.getRuntime().totalMemory()+"");
	// }
	// selectedItem = arg2;
	// switcher.setImageDrawable(new BitmapDrawable(bitmap));
	// gAdapter.setSelectedItem(arg2);
	//
	// }
	//
	// @Override
	// public void onNothingSelected(AdapterView<?> arg0) {
	// // TODO Auto-generated method stub
	// Log.e("my","onNothingSelected");
	// }
	//
	// });
	// AlertDialog.Builder builder = new AlertDialog.Builder(context);
	// mDeleteDialog = builder.create();
	// mDeleteDialog.show();
	// mDeleteDialog.setContentView(view);
	// FrameLayout.LayoutParams layout = new
	// FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
	// FrameLayout.LayoutParams params =
	// (android.widget.FrameLayout.LayoutParams) view.getLayoutParams();
	// params.width = screenWidth;
	// params.height = screenHeight;
	// view.setLayoutParams(params);
	// }

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.back_btn) {
			finish();
		}
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_IMAGEBROWSER;
	}
}
