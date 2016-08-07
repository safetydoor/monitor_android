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
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

public class PictrueTextView extends TextView{
	private int DrablePadding = Utils.dip2px(getContext(), 6);// 不知道为什么文字自己会有10dp的Padding;找到原因后可自由调节
	private Bitmap draw;
	private int oretation = 0;
	private String text="";
	private Rect textRect=new Rect();
	private Paint mPaint;
	private Bitmap draw_p;
	private int draw_up_id;
	private int normal_color;
	private int press_color;
	private int unpress_color;
	private boolean isPress=false;
	private boolean unPress=false;

	public PictrueTextView(Context context) {
		super(context);
	}

	public PictrueTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.ImageviewTextView);
		oretation = a.getInteger(R.styleable.ImageviewTextView_it_orientation,4);
		draw = BitmapFactory.decodeResource(getResources(), a.getResourceId(
				R.styleable.ImageviewTextView_it_drawableid, R.drawable.ic_launcher));
		draw_p=BitmapFactory.decodeResource(getResources(), a.getResourceId(
				R.styleable.ImageviewTextView_it_drawableidp, R.drawable.ic_launcher));
		draw_up_id= a.getResourceId(
				R.styleable.ImageviewTextView_it_drawableidup, R.drawable.ic_launcher);
		normal_color=a.getColor(R.styleable.ImageviewTextView_it_textcolor, getResources().getColor(R.color.text_gray));
		press_color=a.getColor(R.styleable.ImageviewTextView_it_textcolorp, getResources().getColor(R.color.text_gray_p));
		unpress_color=a.getColor(R.styleable.ImageviewTextView_it_textcolorup, getResources().getColor(R.color.unpress_gray));
		text=a.getString(R.styleable.ImageviewTextView_it_textid);
		a.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if(unPress==true){
			DrawButton(canvas, BitmapFactory.decodeResource(getResources(),draw_up_id), unpress_color);
		}else{
			if(!isPress){
				DrawButton(canvas,draw,normal_color);
			}else{
				DrawButton(canvas,draw_p,press_color);
			}		
		}
		super.onDraw(canvas);
	}
	
	private void DrawButton(Canvas canvas,Bitmap bitmap,int color){
		float textWidth = 0;
		int drawablePadding = 0;
		int drawableWidth = 0;
		int drawableHeight = 0;
		if(text!=null&&text.length()>0&&oretation!=5){
			textWidth=getPaint().measureText(text);
		}
		if(bitmap != null&&oretation!=4){
			drawablePadding = DrablePadding;
			drawableWidth = bitmap.getWidth();
			drawableHeight = bitmap.getHeight();
		}
		mPaint=getPaint();
		final float bodyWidth = textWidth + drawableWidth + drawablePadding;
		canvas.translate((getWidth() - bodyWidth) / 2, 0);
		if (bitmap != null&&oretation!=4) {
			canvas.drawBitmap(bitmap, 0, (getHeight() - drawableHeight) / 2,mPaint);
		}
		if(text!=null&&text.length()>0&&oretation!=5){
			mPaint.getTextBounds(text, 0, text.length(), textRect);
			mPaint.setTextSize(Utils.dip2px(getContext(), 11));
		    mPaint.setColor(color);
		    FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();  
		    int baseline = (getHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;  
		    canvas.drawText(text, drawableWidth+drawablePadding, baseline, mPaint);
		}else{
			Log.e("dxslayout", "noText");
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
	public void setTextPictrueColor(boolean unPress){
		this.unPress=unPress;
		invalidate();
	}
}
