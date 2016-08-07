package com.jwkj.widget;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.yoosee.R;
import com.jwkj.global.NpcCommon;
import com.jwkj.utils.ImageUtils;

public class AlarmHeaderView extends ImageView {
	Bitmap tempBitmap;

	public AlarmHeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		// TypedArray array = context.obtainStyledAttributes(attrs,
		// R.styleable.);

	}

	public void updateImage(String threeNum, String index) {
		try {

			// if(isGray){
			// tempBitmap = ImageUtils.getBitmap(new
			// File(Constants.Image.USER_HEADER_PATH+NpcCommon.mThreeNum+"/"+threeNum+"/"+Constants.Image.USER_GRAY_HEADER_FILE_NAME));
			// }else{
			// tempBitmap = ImageUtils.getBitmap(new
			// File(Constants.Image.USER_HEADER_PATH+NpcCommon.mThreeNum+"/"+threeNum+"/"+Constants.Image.USER_HEADER_FILE_NAME));
			// }

			tempBitmap = ImageUtils.getBitmap(new File(
					"/sdcard/screenshot/tempHead/alarm/" + NpcCommon.mThreeNum
							+ "/" + index + ".jpg"), 200, 200);

			// tempBitmap = ImageUtils.roundCorners(tempBitmap,
			// ImageUtils.getScaleRounded(tempBitmap.getWidth()));
			this.setImageBitmap(tempBitmap);
		} catch (Exception e) {
			// if(isGray){
			// tempBitmap = BitmapFactory.decodeResource(this.getResources(),
			// R.drawable.header_gray_icon);
			// }else{
			// tempBitmap = BitmapFactory.decodeResource(this.getResources(),
			// R.drawable.header_icon);
			// }
			tempBitmap = BitmapFactory.decodeResource(this.getResources(),
					R.drawable.header_icon);
			// tempBitmap = ImageUtils.roundCorners(tempBitmap,
			// ImageUtils.getScaleRounded(tempBitmap.getWidth()));
			this.setImageBitmap(tempBitmap);

		}
	}
}
