package com.amenuo.monitor.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amenuo.monitor.R;

/**
 * Created by laps on 7/18/16.
 */
public class TitleBar extends FrameLayout {


    private ImageView mImageView;
    private TextView mTextView;

    public TitleBar(Context context) {
        super(context);
        init(null, 0);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TitleBar, defStyle, 0);

        String title = a.getString(R.styleable.TitleBar_middleTitle);
        Drawable leftIcon = a.getDrawable(R.styleable.TitleBar_leftIcon);

        a.recycle();

        View root = LayoutInflater.from(getContext()).inflate(R.layout.layout_common_titlebar, this);
        mImageView = (ImageView) root.findViewById(R.id.titlebar_leftImage);
        mTextView = (TextView) root.findViewById(R.id.titlebar_middleTitle);
        setLeftIcon(leftIcon);
        setMiddleTitle(title);
    }

    public void setLeftIcon(Drawable leftIcon) {
        mImageView.setImageDrawable(leftIcon);
    }

    public void setLeftOnClickListener(OnClickListener listener) {
        mImageView.setOnClickListener(listener);
    }

    public void setMiddleTitle(String title) {
        mTextView.setText(title);
    }
}
