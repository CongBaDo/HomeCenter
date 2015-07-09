package com.HomeCenter2.ui.adapter;

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
import com.HomeCenter2.house.Light;
import com.HomeCenter2.house.Motion;
import com.HomeCenter2.house.Sensor;
import com.HomeCenter2.house.Temperature;

public class ToolAdapter extends BaseAdapter{

	private static final String TAG = "ToolAdapter";
	
	private Context context;
	private List<Sensor> devices;
	
	public ToolAdapter(Context context, List<Sensor> sensors){
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
        	convertView = inflater.inflate(R.layout.item_tool_view, parent, false);
            vh = new ViewHolder();
            vh.imgTool = (ToolImageView) convertView.findViewById(R.id.img_tool);
            vh.tvTool = (TextView)convertView.findViewById(R.id.tv_value);
            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }
		
        if(devices.get(position) instanceof Temperature){
        	vh.tvTool.setText(((Temperature)devices.get(position)).getTemperature()+"");
        	vh.tvTool.setVisibility(View.VISIBLE);
        	vh.imgTool.setVisibility(View.GONE);
        }else{
        	
        	vh.imgTool.setBackgroundResource(devices.get(position).getIcon());
        	vh.imgTool.setVisibility(View.VISIBLE);
        	vh.tvTool.setVisibility(View.GONE);
        }
		
		return convertView;
	}
	
	static class ViewHolder {
		TextView tvTool;
        ToolImageView imgTool;
    }
}