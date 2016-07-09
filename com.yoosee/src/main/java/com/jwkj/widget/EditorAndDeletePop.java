package com.jwkj.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.PaintDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.jwkj.adapter.SensorRecycleAdapter;
import com.jwkj.entity.Sensor;
import com.yoosee.R;

/**
 * Created by dxs on 2016/1/19.
 */
public class EditorAndDeletePop extends PopupWindow {
    private LayoutInflater inflater;
    private View conentView;
    private Context context;
    private TextView txEditor,txDelete;
    private Sensor sensor;
    private int position;
    private SensorRecycleAdapter.ViewHolder holder;


    public EditorAndDeletePop(final Context context, int h) {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.pop_editoranddelete, null);
        this.context = context;
        setContentView(conentView);
        setHeight(h);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置动画效果
        this.setAnimationStyle(R.style.AnimationFade);
        this.setBackgroundDrawable(new PaintDrawable(0));
        this.setOutsideTouchable(true);
        WindowManager.LayoutParams lp = ((Activity) context).getWindow()
                .getAttributes();
        lp.alpha = 0.4f;
        ((Activity) context).getWindow().setAttributes(lp);
        this.setOnDismissListener(new OnDismissListener() {

            // 在dismiss中恢复透明度
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = ((Activity) context)
                        .getWindow().getAttributes();
                lp.alpha = 1f;
                ((Activity) context).getWindow().setAttributes(lp);
            }
        });
        initUI(conentView);
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private void initUI(View conentView) {
        txEditor= (TextView) conentView.findViewById(R.id.tx_editor);
        txDelete= (TextView) conentView.findViewById(R.id.tx_delete);
        txEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.EditorClick(holder,sensor,position);
            }
        });
        txDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.DeletClick(holder,sensor,position);
            }
        });
    }
    private onDeleteAndEditorListner listner;
    public void setOnDeleteAndEditorListner(onDeleteAndEditorListner listner){
        this.listner=listner;
    }
    public interface onDeleteAndEditorListner{
        void EditorClick(SensorRecycleAdapter.ViewHolder holder,Sensor sensor,int position);
        void DeletClick(SensorRecycleAdapter.ViewHolder holder,Sensor sensor,int position);
    }

    public SensorRecycleAdapter.ViewHolder getHolder() {
        return holder;
    }

    public void setHolder(SensorRecycleAdapter.ViewHolder holder) {
        this.holder = holder;
    }
}
