/*
 Copyright (c) 2012 Roman Truba

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial
 portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.lib.imagesee;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;

import java.io.File;
import java.io.FileInputStream;

import com.lib.imagesee.ImageTools;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class FileTouchImageView extends RelativeLayout {
	protected TouchImageView mImageView;
	protected Context mContext;
	private ImageLoader mImageLoader;

	public FileTouchImageView(Context ctx) {
		super(ctx);
		mContext = ctx;
		init();
	}

	public FileTouchImageView(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);
		mContext = ctx;
		init();
	}

	public TouchImageView getImageView() {
		return mImageView;
	}

	protected void init() {
		mImageLoader = ImageTools.getImageLoaderInstance(mContext);
		mImageView = new TouchImageView(mContext);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mImageView.setLayoutParams(params);
		this.addView(mImageView);
	}

	public void setScaleType(ScaleType scaleType) {
		mImageView.setScaleType(scaleType);
	}

	public void setUrl(String imagePath) {
		// mImageLoader.cancelDisplayTask(mImageView);
		mImageLoader.displayImage("file://" + imagePath, mImageView);
		// mImageLoader.displayImage(imagePath, mImageView,
		// new ImageLoadingListener() {
		//
		// @Override
		// public void onLoadingStarted(String arg0, View arg1) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onLoadingFailed(String arg0, View arg1,
		// FailReason arg2) {
		// Log.i("imageLoder", "Error:" + arg2.toString());
		//
		// }
		//
		// @Override
		// public void onLoadingComplete(String arg0, View arg1,
		// Bitmap arg2) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onLoadingCancelled(String arg0, View arg1) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
	}

}
