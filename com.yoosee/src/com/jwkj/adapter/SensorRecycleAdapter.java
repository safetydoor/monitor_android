package com.jwkj.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.jwkj.data.Contact;
import com.jwkj.entity.Sensor;
import com.jwkj.fisheye.FisheyeSetHandler;
import com.jwkj.fragment.SmartDeviceFrag;

import com.jwkj.global.Constants;
import com.yoosee.R;

import java.util.Arrays;
import java.util.List;

import javax.crypto.spec.IvParameterSpec;

/**
 * Created by dxs on 2015/12/29.
 */
public class SensorRecycleAdapter extends BaseAdapter {
    private Context mContext;
    private List<Sensor> lists;
    private Contact contatc;
    public SensorRecycleAdapter(Context context) {
        mContext=context;
    }

    public SensorRecycleAdapter(Context mContext, List<Sensor> lists,Contact contatc) {
        this.mContext = mContext;
        this.lists = lists;
        this.contatc=contatc;
    }
    public class ViewHolder {
//        public ImageView Point;
        public TextView txName;
        public ImageView Raw;
        public ImageView StateOn;
        public ProgressBar  progress;
        public ImageView iv_sensor_switch;
        public int position;

    }
    private onSensorRecycleAdatperClickListner listner;
    public void setOnSensorRecycleAdatperClickListner(onSensorRecycleAdatperClickListner listner){
        this.listner=listner;
    }
    public interface onSensorRecycleAdatperClickListner{
        void onItemClick(View contentview, Sensor sensor, int position);
        void onLongClick(ViewHolder holder, Sensor sensor, int position);
        void onSwitchClick(ViewHolder holder, Sensor sensor, int position);
        void onSensorSwitchClick(ViewHolder holder, Sensor sensor, int position);
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		Log.e("sensorsize", "getCount"+lists.size());
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.e("sensorsize", "sensorsize"+lists.size());
		final ViewHolder holder;
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.items_sensor_list, null);
			holder=new ViewHolder();
//			holder.Point= (ImageView) convertView.findViewById(R.id.iv_point);
			holder.txName= (TextView) convertView.findViewById(R.id.tx_sensor_name);
			holder.Raw= (ImageView) convertView.findViewById(R.id.iv_sensor_raw);
			holder.StateOn= (ImageView) convertView.findViewById(R.id.iv_off_on);
			holder.progress= (ProgressBar) convertView.findViewById(R.id.progressBar_sensor);
			holder.iv_sensor_switch=(ImageView)convertView.findViewById(R.id.iv_sensor_switch);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.position=position;
		  final Sensor sensor=lists.get(position);
		   Log.e("sensorsize", lists.get(position).getName());
	        holder.txName.setText(sensor.getName());
	        if(sensor.getSensorCategory()==Sensor.SENSORCATEGORY_NORMAL){
	            //一般传感器
	            if(sensor.getSensorType()== Constants.SensorType.TYPE_REMOTE_CONTROLLER){
	                //遥控
//	                holder.Point.setVisibility(View.INVISIBLE);
	                holder.progress.setVisibility(View.GONE);
	                holder.StateOn.setVisibility(View.GONE);
	                holder.Raw.setVisibility(View.GONE);
	            }else if(sensor.isControlSensor()){
	                //插座
//	                holder.Point.setVisibility(View.INVISIBLE);
	                holder.iv_sensor_switch.setVisibility(View.GONE);
	                if(sensor.getLampState()==0){//插座在查询
	                    holder.progress.setVisibility(View.VISIBLE);
	                    holder.StateOn.setVisibility(View.GONE);
	                }else if(sensor.getLampState()==1||sensor.getLampState()==3){//插座开
	                	Log.e("sensorsize", "插座开");
	                    holder.progress.setVisibility(View.GONE);
	                    holder.StateOn.setVisibility(View.VISIBLE);
	                    holder.StateOn.setImageResource(R.drawable.on);
	                }else{//插座关
	                    holder.progress.setVisibility(View.GONE);
	                    holder.StateOn.setVisibility(View.VISIBLE);
	                    holder.StateOn.setImageResource(R.drawable.off);
	                }
	                holder.Raw.setVisibility(View.GONE);
	            }else{
	                holder.Raw.setVisibility(View.GONE);
	                holder.progress.setVisibility(View.GONE);//暂时没有支持的传感器显示
	                holder.StateOn.setVisibility(View.GONE);//暂时没有支持的传感器显示
//	                if(sensor.getSensorSwitch()){
//	                    holder.Point.setVisibility(View.VISIBLE);
//	                }else{
//	                    holder.Point.setVisibility(View.INVISIBLE);
//	                }
	                if(sensor.getSensorSwitch()){
	                	holder.iv_sensor_switch.setBackgroundResource(R.drawable.check_on);
	                	holder.progress.setVisibility(View.GONE);
	                }else{
	                	holder.iv_sensor_switch.setBackgroundResource(R.drawable.check_off);
	                	holder.progress.setVisibility(View.GONE);
	                }
	                holder.iv_sensor_switch.setVisibility(View.VISIBLE);
	            }
	        }else{
	            //特殊传感器
	            holder.Raw.setVisibility(View.VISIBLE);
	            holder.progress.setVisibility(View.GONE);//暂时没有支持的传感器显示
	            holder.StateOn.setVisibility(View.GONE);//暂时没有支持的传感器显示
//	            if(sensor.getSensorSwitch()){
//	                holder.Point.setVisibility(View.VISIBLE);
//	            }else{
//	                holder.Point.setVisibility(View.INVISIBLE);
//	            }
	        }
	        convertView.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                listner.onItemClick(v,sensor,position);
	            }
	        });
	        convertView.setOnLongClickListener(new View.OnLongClickListener() {
	            @Override
	            public boolean onLongClick(View v) {
	                listner.onLongClick(holder, sensor, position);
	                return true;
	            }
	        });
	        holder.StateOn.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                listner.onSwitchClick(holder, sensor, position);
	            }
	        });
	        holder.iv_sensor_switch.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					 iv_sensor_switch.setModeStatde(SwitchView.State_progress);
					 listner.onSensorSwitchClick(holder, sensor, position);
					 Log.e("onClick", "onClick-------");
					 holder.iv_sensor_switch.setVisibility(View.GONE);
					 holder.progress.setVisibility(View.VISIBLE);
		             if (sensor.getSensorSwitch()) {
		                    changeSensorSwitch((byte) 0,sensor);
		             } else {
		                    changeSensorSwitch((byte) 1,sensor);
		             }
				}
			});
		return convertView;
	}
	public void notifySensorData(List<Sensor> data){
		this.lists=data;
		this.notifyDataSetChanged();
	}
	 /**
     * 打开或关闭一个传感器
     *
     * @param state 开关状态
     */
    private void changeSensorSwitch(byte state,Sensor sensor) {
//        if(sensor.getSensorCategory()==Sensor.SENSORCATEGORY_NORMAL){
//            FisheyeSetHandler.getInstance().sTurnSensor(SmartDeviceFrag.contact.contactId,SmartDeviceFrag.contact.contactPassword, sensor.getSensorSignature(), state);
//        }else {
            if(state==1){
                sensor.setSensorSwitch(true);
                sensor.setSensorStateAtMode(0,0,true);
                sensor.setSensorStateAtMode(1,0,true);
                sensor.setSensorStateAtMode(2,0,true);
            }else {
                sensor.setSensorSwitch(false);
                sensor.setSensorStateAtMode(0,0,false);
                sensor.setSensorStateAtMode(1,0,false);
                sensor.setSensorStateAtMode(2,0,false);
            }
            setSensorData(sensor);
//        }

    }
    /**
     * 设置传感器
     *
     * @param sensor
     */
    private void setSensorData(Sensor sensor) {
        if(sensor.getSensorCategory()==Sensor.SENSORCATEGORY_NORMAL) {
            FisheyeSetHandler.getInstance().sSettingSensorWorkMode(contatc.getContactId(), contatc.contactPassword, sensor.getSensorData());
        }else{
            Log.e("dxsTest","special--->"+ Arrays.toString(sensor.getSensorData()));
            FisheyeSetHandler.getInstance().sSpecialAlarmData(contatc.getContactId(), contatc.contactPassword, sensor.getSensorData());
        }
    }
}
