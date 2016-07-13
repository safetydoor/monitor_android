package com.amenuo.monitor.view;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by laps on 7/9/16.
 */
public class MainLumpImageView extends ImageView {
    private ColorMatrixColorFilter mColorMatrixFilter;

    public MainLumpImageView(Context context) {
        this(context, null);

        // TODO Auto-generated constructor stub
    }

    public MainLumpImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public MainLumpImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        switch (me.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setPressed(true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setPressed(false);
                break;
        }
        return super.onTouchEvent(me);
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        pressedProcess(pressed);
    }

    private void pressedProcess(boolean pressed) {
        if (pressed) {
            setColorFilter(mColorMatrixFilter);
        } else {
            clearColorFilter();
        }
    }


    private final void init() {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(new float[]{
                0.6f, 0, 0, 0, 0.8f,
                0, 0.6f, 0, 0, 0.8f,
                0, 0, 0.6f, 0, 0.8f,
                0, 0, 0, 1f, 0
        });

        mColorMatrixFilter = new ColorMatrixColorFilter(colorMatrix);
    }
}