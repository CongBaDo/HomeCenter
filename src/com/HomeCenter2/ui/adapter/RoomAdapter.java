package com.HomeCenter2.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.HomeCenter2.R;
import com.HomeCenter2.customview.ToolImageView;

public class RoomAdapter extends BaseAdapter{

	private static final String TAG = "RoomAdapter";
	
	private int[] DRAWS = new int[]{R.drawable.ic_lamp_wht, R.drawable.ic_lock_wht, R.drawable.ic_fan_wht, R.drawable.ic_lamp_wht, R.drawable.ic_lock_wht, R.drawable.ic_fan_wht};
	
	private Context context;
	
	public RoomAdapter(Context context){
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return DRAWS.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return DRAWS[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return DRAWS[position];
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
		ViewHolder vh;
        if (convertView == null) {
        	convertView = inflater.inflate(R.layout.item_image_room_view, parent, false);
            vh = new ViewHolder();
            vh.imgTool = (ToolImageView) convertView.findViewById(R.id.img_tool);
            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }
		
        vh.imgTool.setBackgroundResource(DRAWS[position]);
		
		return convertView;
	}
	
	static class ViewHolder {
        ToolImageView imgTool;
    }
}
