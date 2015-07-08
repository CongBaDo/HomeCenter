package com.HomeCenter2.ui.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.HomeCenter2.R;
import com.HomeCenter2.customview.ToolImageView;
import com.HomeCenter2.house.DeviceTypeOnOff;
import com.HomeCenter2.house.Sensor;

public class OnOffTypeAdapter extends BaseAdapter{

	private static final String TAG = "ToolAdapter";
	
	private Context context;
	private List<DeviceTypeOnOff> types;
	
	public OnOffTypeAdapter(Context context, List<DeviceTypeOnOff> types){
		this.context = context;
		this.types = types;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return types.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return types.get(position);
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
		
        vh.imgTool.setBackgroundResource(types.get(position).getIconOn());
		
		return convertView;
	}
	
	static class ViewHolder {
        ToolImageView imgTool;
    }
}