package com.jwkj.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

/**
 * 竖值SeekBar 参考一下网上的例子，但是有个bug和一个需要注意的点
 * 1.bug：拖动的时候没问题，当调用setProgress（）时，滑块移动不跟进度条移动
 * 2.注意：因为竖直seekbar是交换了宽高，所以设置padding时，设置 android:paddingLeft="20dp"
 * android:paddingRight="20dp"，实际是设置bottom和top的padding。所以要想设置底部
 * 和上部的宽高就设置左右padding，解决滑块显示不全问题。
 * 
 * @author malong
 * 
 */
public class MLVerticalSeekBar extends SeekBar {

	public MLVerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MLVerticalSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MLVerticalSeekBar(Context context) {
		super(context);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(h, w, oldh, oldw);
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		canvas.rotate(-90);
		canvas.translate(-getHeight(), 0);
		super.onDraw(canvas);
	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec) {
		super.onMeasure(heightMeasureSpec, widthMeasureSpec);
		setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth() + 100);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (!isEnabled()) {
			return false;
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
		case MotionEvent.ACTION_UP:
			setProgress(getMax()
					- (int) (getMax() * event.getY() / getHeight()));
			onSizeChanged(getWidth(), getHeight(), 0, 0);
			break;

		case MotionEvent.ACTION_CANCEL:
			break;
		}

		return true;
	}

	// 解决调用setProgress（）方法时滑块不跟随的bug
	@Override
	public synchronized void setProgress(int progress) {
		super.setProgress(progress);
		onSizeChanged(getWidth(), getHeight(), 0, 0);

	}

}
