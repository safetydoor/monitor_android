package com.amenuo.monitor.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.amenuo.monitor.R;

/**
 * Created by laps on 7/17/16.
 */
public class MainLeftView extends FrameLayout implements View.OnClickListener {

    private View mFindPasswordView;
    private View mAboutView;
    private View mExitView;
    private OnItemClickListener mOnItemClickListener;

    public MainLeftView(Context context) {
        super(context);
        View root = LayoutInflater.from(context).inflate(R.layout.layout_main_left, this);
        mFindPasswordView = findViewById(R.id.main_left_find_password);
        mAboutView = findViewById(R.id.main_left_about);
        mExitView = findViewById(R.id.main_left_exit);
        mFindPasswordView.setOnClickListener(this);
        mAboutView.setOnClickListener(this);
        mExitView.setOnClickListener(this);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (resId == R.id.main_left_find_password) {

        } else if (resId == R.id.main_left_about) {

        } else if (resId == R.id.main_left_exit) {

        }

        mOnItemClickListener.onItemClick();
    }

    public interface OnItemClickListener {
        public void onItemClick();
    }
}
