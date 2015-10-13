package com.HomeCenter2.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.test.suitebuilder.annotation.Smoke;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.HomeCenter2.R;
import com.HomeCenter2.customview.ToolImageView;
import com.HomeCenter2.house.Device;
import com.HomeCenter2.house.DoorStatus;
import com.HomeCenter2.house.GroupBotoomTool;
import com.HomeCenter2.house.Light;
import com.HomeCenter2.house.Motion;
import com.HomeCenter2.house.Sensor;
import com.HomeCenter2.house.Temperature;
import com.HomeCenter2.house.GroupBotoomTool.SPECIAL;
import com.HomeCenter2.imageprocessing.PhotoSortrView.ImgListener;
import com.HomeCenter2.ui.ScheduleImageView;

public class BottomToolAdapter extends BaseAdapter{

	private static final String TAG = "BottomToolAdapter";
	
	private Context context;
	private ArrayList<GroupBotoomTool> devices;
	
	public BottomToolAdapter(Context context, ArrayList<GroupBotoomTool> sensors){
		this.context = context;
		this.devices = sensors;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return devices.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return devices.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
		ViewHolder vh;
        if (convertView == null) {
        	convertView = inflater.inflate(R.layout.item_bottom_tool_view, parent, false);
            vh = new ViewHolder();
            vh.imgToolBottom = (ToolImageView) convertView.findViewById(R.id.img_tool_bottom);
            vh.imgToolTop = (ToolImageView)convertView.findViewById(R.id.img_tool_top);
            vh.tvTool = (TextView)convertView.findViewById(R.id.tv_value);
            vh.imgSpecial = (ScheduleImageView)convertView.findViewById(R.id.img_tool_on);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        
        if(devices.get(position).getSpecialToolType() == SPECIAL.NONE){
        	vh.imgSpecial.setVisibility(View.GONE);
        	if(devices.get(position).getDoubSensor()[0] instanceof Temperature){
            	vh.tvTool.setText(((Temperature)devices.get(position).getDoubSensor()[0]).getTemperature()+"");
            	vh.tvTool.setVisibility(View.VISIBLE);
            	vh.imgToolTop.setVisibility(View.GONE);
            }else{
            	
            	vh.imgToolTop.setBackgroundResource(devices.get(position).getDoubSensor()[0].getIcon());
            	vh.imgToolTop.setVisibility(View.VISIBLE);
            	vh.tvTool.setVisibility(View.GONE);
            }
            
            if(devices.get(position).getDoubSensor()[1] instanceof Temperature){
            	vh.tvTool.setText(((Temperature)devices.get(position).getDoubSensor()[1]).getTemperature()+"");
            	vh.tvTool.setVisibility(View.VISIBLE);
            	vh.imgToolBottom.setVisibility(View.GONE);
            }else{
            	
            	vh.imgToolBottom.setBackgroundResource(devices.get(position).getDoubSensor()[1].getIcon());
            	vh.imgToolBottom.setVisibility(View.VISIBLE);
            	vh.tvTool.setVisibility(View.GONE);
            }
        }else{
        	
        	if(devices.get(position).getSpecialToolType() == SPECIAL.MIC){
        		vh.imgSpecial.setBackgroundResource(R.drawable.ic_micro);
        	}else if(devices.get(position).getSpecialToolType() == SPECIAL.ON){
        		vh.imgSpecial.setBackgroundResource(R.drawable.btn_on_icon);
        	}else if(devices.get(position).getSpecialToolType() == SPECIAL.OFF){
        		vh.imgSpecial.setBackgroundResource(R.drawable.btn_off_icon);
        	}
        	vh.imgSpecial.setVisibility(View.VISIBLE);
        	vh.imgToolBottom.setVisibility(View.GONE);
        	vh.imgToolTop.setVisibility(View.GONE);
        }
		
        
		
		return convertView;
	}
	
	static class ViewHolder {
		TextView tvTool;
		ToolImageView imgToolTop;
        ToolImageView imgToolBottom;
        ScheduleImageView imgSpecial;
    }
}