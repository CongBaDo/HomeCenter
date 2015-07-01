package com.HomeCenter2.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.HomeCenter2.R;
import com.HomeCenter2.customview.ToolImageView;
import com.HomeCenter2.house.Device;

public class ToolAdapter extends BaseAdapter{

	private static final String TAG = "ToolAdapter";
	
	private Context context;
	private List<Device> devices;
	
	public ToolAdapter(Context context, List<Device> devices){
		this.context = context;
		this.devices = devices;
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
            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }
		
        vh.imgTool.setBackgroundResource(devices.get(position).getIcon());
		
		return convertView;
	}
	
	static class ViewHolder {
        ToolImageView imgTool;
    }
}
