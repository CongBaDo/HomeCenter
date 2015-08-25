package com.HomeCenter2.ui.adapter;

import java.util.List;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.DragShadowBuilder;
import android.view.View.OnLongClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.HomeCenter2.R;
import com.HomeCenter2.customview.ToolImageView;
import com.HomeCenter2.house.DeviceTypeOnOff;
import com.HomeCenter2.house.Sensor;

public class OnOffTypeAdapter extends BaseAdapter{

	private static final String TAG = "OnOffTypeAdapter";
	
	private Context context;
	private List<DeviceTypeOnOff> types;
	
	private interface LongCallback{
		public void longCallback(View v);
	}
	
	private LongCallback callback;
	
	public void setCallback(LongCallback callback){
		this.callback = callback;
	}
	
	public OnOffTypeAdapter(Context context, List<DeviceTypeOnOff> types){
		Log.e(TAG, "OnOffTypeAdapter");
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
            vh.tv = (TextView)convertView.findViewById(R.id.tv_value);
            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }
		
        vh.imgTool.setBackgroundResource(types.get(position).getIconOn());
        vh.tv.setVisibility(View.GONE);
        
        vh.imgTool.setTag(position+"");
		
        vh.imgTool.setOnLongClickListener(new MyClickListener());
        
		return convertView;
	}
	
	private final class MyClickListener implements OnLongClickListener {

	    // called when the item is long-clicked
		@Override
		public boolean onLongClick(View view) {
		// TODO Auto-generated method stub
		
			// create it from the object's tag
			ClipData.Item item = new ClipData.Item((CharSequence)view.getTag());

	        String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
	        ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);
	        DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
	   
	        view.startDrag( data, //data to be dragged
	        				shadowBuilder, //drag shadow
	        				view, //local data about the drag and drop operation
	        				0   //no needed flags
	        			  );
	        
	        
	        view.setVisibility(View.INVISIBLE);
	        return true;
		}	
	}
	
	static class ViewHolder {
        ToolImageView imgTool;
        TextView tv;
    }
}