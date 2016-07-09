package com.jwkj.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class RotateLayout extends FrameLayout {
	private Matrix mForward = new Matrix();
	private Matrix mReverse = new Matrix();
	private float[] mTemp = new float[2];

	public RotateLayout(Context context) {
		super(context);
	}

	public RotateLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.FrameLayout#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// This didn't work:
		// super.onMeasure(heightMeasureSpec, widthMeasureSpec);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.FrameLayout#onSizeChanged(int, int, int, int)
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		canvas.rotate(-90, -getHeight(), 0);
		// This code will stretch the canvas to accommodate the new screen size.
		// This is not what I want.
		// float scaleX=(float)getHeight()/getWidth();
		// float scaleY=(float)getWidth()/getHeight();
		// canvas.scale(scaleX, scaleY, getWidth()/2, getHeight()/2);
		mForward = canvas.getMatrix();
		mForward.invert(mReverse);
		canvas.save();
		canvas.setMatrix(mForward); // This is the matrix we need to use for
									// proper positioning of touch events
		super.dispatchDraw(canvas);
		canvas.restore();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final float[] temp = mTemp;
		temp[0] = event.getX();
		temp[1] = event.getY();

		mReverse.mapPoints(temp);

		event.setLocation(temp[0], temp[1]);
		return super.dispatchTouchEvent(event);
	}
}