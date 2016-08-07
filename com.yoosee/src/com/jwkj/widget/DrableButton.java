package com.jwkj.widget;

import com.jwkj.utils.Utils;
import com.yoosee.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * 四周可放图片的按钮，目前只默认在左
 * @author Administrator
 *
 */
public class DrableButton extends Button {
	private int DrablePadding = Utils.dip2px(getContext(), 10);
	private Bitmap draw;
	private int oretation = 0;
	private String text="";
	private Rect textRect=new Rect();
	private Paint mPaint;
	private Bitmap draw_p;
	private int normal_color;
	private int press_color;
	private boolean isPress=false;

	public DrableButton(Context context) {
		super(context);
	}

	public DrableButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.DrableButton);
		oretation = a.getInteger(R.styleable.DrableButton_orientation,4);
		draw = BitmapFactory.decodeResource(getResources(), a.getResourceId(
				R.styleable.DrableButton_drawableid, R.drawable.ic_launcher));
		draw_p=BitmapFactory.decodeResource(getResources(), a.getResourceId(
				R.styleable.DrableButton_drawableidp, R.drawable.ic_launcher));
		normal_color=a.getColor(R.styleable.DrableButton_textcolor, R.color.text_normal);
		press_color=a.getColor(R.styleable.DrableButton_textcolorp, R.color.text_press);
		text=a.getString(R.styleable.DrableButton_textid);
		a.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if(!isPress){
			DrawButton(canvas,draw,normal_color);
		}else{
			DrawButton(canvas,draw_p,press_color);
		}
		super.onDraw(canvas);
	}
	
	private void DrawButton(Canvas canvas,Bitmap bitmap,int color){
		float textWidth = 0;
		int drawablePadding = 0;
		int drawableWidth = 0;
		int drawableHeight = 0;
		mPaint=getPaint();
		if(text!=null&&text.length()>0&&oretation!=5){
			textWidth=getPaint().measureText(text);
		}
		if(bitmap != null&&oretation!=4){
			drawableWidth = bitmap.getWidth();
			drawableHeight = bitmap.getHeight();
		}
		if(oretation==4||oretation==5){
			drawablePadding=0;
		}else{
			drawablePadding = DrablePadding;
		}
		final float bodyWidth = textWidth + drawableWidth + drawablePadding;
		
		canvas.translate((getWidth() - bodyWidth) / 2, 0);
		if (bitmap != null&&oretation!=4) {
			canvas.drawBitmap(bitmap, 0, (getHeight() - drawableHeight) / 2,mPaint);
		}
		if(text!=null&&text.length()>0&&oretation!=5){
			mPaint.getTextBounds(text, 0, text.length(), textRect);
			mPaint.setTextSize(Utils.dip2px(getContext(), 12));
		    mPaint.setFakeBoldText(true);
		    mPaint.setColor(color);
		    FontMetricsInt fontMetrics = mPaint.getFontMetricsInt(); 
		    int baseline = (getHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;  
		    canvas.drawText(text, drawableWidth+drawablePadding, baseline, mPaint);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isPress=true;
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			isPress=false;
			invalidate();
			break;
		case MotionEvent.ACTION_CANCEL:
			isPress=false;
			invalidate();
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}
	
	public void setImageResource(int bimapId,int bimapId_p){
		draw = BitmapFactory.decodeResource(getResources(), bimapId);
		draw_p=BitmapFactory.decodeResource(getResources(), bimapId_p);
		invalidate();
	}
}
