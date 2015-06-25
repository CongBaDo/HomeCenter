package com.HomeCenter2.ui.menuscreen;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.HomeCenter2.R;
import com.HomeCenter2.HomeCenter2;
import com.HomeCenter2.data.XMLHelper;
import com.HomeCenter2.data.configManager;
import com.HomeCenter2.house.Area;
import com.HomeCenter2.house.Cock;
import com.HomeCenter2.house.Device;
import com.HomeCenter2.house.DeviceType;
import com.HomeCenter2.house.DoorLock;
import com.HomeCenter2.house.DoorStatus;
import com.HomeCenter2.house.Fan;
import com.HomeCenter2.house.Fridge;
import com.HomeCenter2.house.Lamp;
import com.HomeCenter2.house.LampRoot;
import com.HomeCenter2.house.Light;
import com.HomeCenter2.house.Motion;
import com.HomeCenter2.house.RollerShutter;
import com.HomeCenter2.house.Room;
import com.HomeCenter2.house.Temperature;
import com.HomeCenter2.ui.slidingmenu.framework.SlidingBaseActivity;

public class XMLDevices extends Fragment implements OnClickListener {

	public static final String TAG = "TMT XMLDevices";
	HomeCenter2 mContext;
	LinearLayout mDevicesLinear = null;
	LayoutInflater mInflater;
	List<Area> mAreas;
	List<Room> mRooms;
	List<Device> mDevices;

	List<Object> mItems;
	DeviceTypeAdapter mDeviceAdapter = null;

	class Item {
		EditText id;
		EditText name;
		int type;

		public Item() {
			id = null;
			name = null;
			type = configManager.ROOM_TYPE;
		}
	}

	class ItemDevice {
		EditText id;
		Spinner name;
		int type;

		public ItemDevice() {
			id = null;
			name = null;
			type = configManager.DEVICE_TYPE;
		}
	}

	public XMLDevices(SlidingBaseActivity context) {
		mContext = (HomeCenter2) context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = getLayoutInflater(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.setting_devices, container, false);
		Button btn = (Button) view.findViewById(R.id.btn_add_area);
		btn.setOnClickListener(this);

		btn = (Button) view.findViewById(R.id.btn_add_room);
		btn.setOnClickListener(this);

		btn = (Button) view.findViewById(R.id.btn_add_device);
		btn.setOnClickListener(this);

		mDevicesLinear = (LinearLayout) view.findViewById(R.id.ln_devices);

		mItems = new ArrayList<Object>();

		ItemDevice device = new ItemDevice();
		return view;
	}

	@Override
	public void onClick(View v) {
		Item item;
		LinearLayout view;
		switch (v.getId()) {
		case R.id.btn_add_area:
			item = new Item();
			view = (LinearLayout) mInflater.inflate(
					R.layout.setting_devices_add_area, null);
			mDevicesLinear.addView(view);

			item.id = (EditText) view.findViewById(R.id.edit_area_id);
			item.name = (EditText) view.findViewById(R.id.edit_area_name);
			item.type = configManager.AREA_TYPE;
			mItems.add(item);
			break;
		case R.id.btn_add_room:
			item = new Item();
			view = (LinearLayout) mInflater.inflate(
					R.layout.setting_devices_add_room, null);
			mDevicesLinear.addView(view);

			item.id = (EditText) view.findViewById(R.id.edit_room_id);
			item.name = (EditText) view.findViewById(R.id.edit_room_name);
			item.type = configManager.ROOM_TYPE;
			mItems.add(item);
			break;
		case R.id.btn_add_device:
			ItemDevice device = new ItemDevice();
			view = (LinearLayout) mInflater.inflate(
					R.layout.setting_devices_add_device, null);
			mDevicesLinear.addView(view);

			device.id = (EditText) view.findViewById(R.id.edit_device_id);
			device.name = (Spinner) view.findViewById(R.id.spin_device_name);
			device.type = configManager.AREA_TYPE;
			DeviceTypeAdapter adapter = new DeviceTypeAdapter(mContext,
					configManager.deviceTypes);
			device.name.setAdapter(adapter);
			mItems.add(device);
			break;
		default:
			break;
		}
	}

	public class DeviceTypeAdapter extends BaseAdapter {
		List<DeviceType> mDeviceType = null;

		public DeviceTypeAdapter(Context context, List<DeviceType> devices) {
			this.mDeviceType = devices;
		}

		@Override
		public int getCount() {
			if (this.mDeviceType == null)
				return 0;
			return this.mDeviceType.size();
		}

		@Override
		public Object getItem(int arg0) {
			if (this.mDeviceType == null)
				return null;
			return this.mDeviceType.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		class DeviceTypeHolder {
			TextView tvDeviceType;
		}

		@Override
		public View getView(int arg0, View view, ViewGroup arg2) {
			DeviceTypeHolder holder = null;

			if (view == null) {
				view = mInflater.inflate(R.layout.setting_devices_type_item,
						null);

				holder = new DeviceTypeHolder();
				holder.tvDeviceType = (TextView) view
						.findViewById(R.id.tv_device_type);
				view.setTag(holder);
			} else {
				holder = (DeviceTypeHolder) view.getTag();
			}
			holder.tvDeviceType.setText(this.mDeviceType.get(arg0).getName());
			return view;
		}

	}

	public void saveSetting() {	
		List<Object> objects = new ArrayList<Object>();
		int size = mItems.size();
		String id, name;
		Area area = null;
		Room room = null;

		for (int i = 0; i < size; i++) {
			Object view = mItems.get(i);
			if (view != null && view instanceof Item) {
				Item itemView = (Item) view;
				if (itemView.type == configManager.AREA_TYPE) {
					area = new Area();

					id = itemView.id.getText().toString();
					if(TextUtils.isEmpty(id)){
						continue;					
					}
					area.setId(Integer.parseInt(id));
					name = itemView.name.getText().toString();
					area.setName(name);

					objects.add(area);

				} else if (itemView.type == configManager.ROOM_TYPE) {
					room = new Room();

					id = itemView.id.getText().toString();
					if(TextUtils.isEmpty(id)){
						continue;					
					}
					room.setId(Integer.parseInt(id));
					name = itemView.name.getText().toString();
					room.setName(name);					

					objects.add(room);
					area.getRooms().add(room);
				}
			} else if (view != null && view instanceof ItemDevice) {
				ItemDevice itemDeviceView = (ItemDevice) view;

				id = itemDeviceView.id.getText().toString();

				DeviceType type = (DeviceType) itemDeviceView.name
						.getSelectedItem();
				Device device = getDevice(type, id);
				if (room != null) {
					device.setRoomId(room.getId());
					room.getDevices().add(device);
					objects.add(device);
				}
			}
		}
		if (objects != null && objects.size() > 0) {			
			XMLHelper.createDeviceFileXml(objects);
		}
	}

	public Device getDevice(DeviceType type, String id) {
		if (type == null) {
			return null;
		}
		String name = type.getName();
		if(TextUtils.isEmpty(id)){
			return null;
		}
		switch (type.getType()) {
		case configManager.FAN:
			Fan fan = new Fan(Integer.parseInt(id), name, false);
			Log.d(TAG, "saveSetting::fan::251::fan:" + (fan == null) + " , id "
					+ Integer.parseInt(id) + " , mNameTV::" + name);
			return fan;			
		case configManager.COCK:
			Cock cock = new Cock(Integer.parseInt(id), name, false);
			Log.d(TAG, "saveSetting::cock::251::cock:" + (cock == null) + " , id "
					+ Integer.parseInt(id) + " , mNameTV::" + name);
			return cock;
		case configManager.LIGHT:
			Light light = new Light(Integer.parseInt(id), name, 0);
			Log.d(TAG, "saveSetting::light::251::light:" + (light == null)
					+ " , id " + Integer.parseInt(id) + " , mNameTV::" + name);
			return light;
		case configManager.FRIDGE:
			Fridge fridge = new Fridge(Integer.parseInt(id), name, false);
			Log.d(TAG, "saveSetting::cock::251::cock:" + (fridge == null) + " , id "
					+ Integer.parseInt(id) + " , mNameTV::" + name);
			return fridge;

		case configManager.TEMPERATURE:
			Temperature temperature = new Temperature(Integer.parseInt(id),
					name, 0);
			Log.d(TAG, "saveSetting:: TEMPERATURE ::251::light:"
					+ (temperature == null) + " , id " + Integer.parseInt(id)
					+ " , mNameTV::" + name);
			return temperature;

		case configManager.MOTION:
			Motion motion = new Motion(Integer.parseInt(id), name, false);
			Log.d(TAG, "saveSetting::light::251::light:" + (motion == null)
					+ " , id " + Integer.parseInt(id) + " , mNameTV::" + name);
			return motion;

		case configManager.DOOR_STATUS:
			DoorStatus doorStatus = new DoorStatus(Integer.parseInt(id), name,
					false);
			Log.d(TAG, "saveSetting::light::251::light:" + (doorStatus == null)
					+ " , id " + Integer.parseInt(id) + " , mNameTV::" + name);
			return doorStatus;

		case configManager.LAMP:
			Lamp lamp = new Lamp(Integer.parseInt(id), name, false);
			Log.d(TAG, "saveSetting::light::251::light:" + (lamp == null)
					+ " , id " + Integer.parseInt(id) + " , mNameTV::" + name);
			return lamp;

		case configManager.DOOR_LOCK:
			DoorLock doorLock = new DoorLock(Integer.parseInt(id), name, false);
			Log.d(TAG, "saveSetting::light::251::light:" + (doorLock == null)
					+ " , id " + Integer.parseInt(id) + " , mNameTV::" + name);
			return doorLock;			

		case configManager.ROLLER_SHUTTER:
			RollerShutter rollerShutter = new RollerShutter(
					Integer.parseInt(id), name, 0);
			Log.d(TAG, "saveSetting::roler::251::light:"
					+ (rollerShutter == null) + " , id " + Integer.parseInt(id)
					+ " , mNameTV::" + name);
			return rollerShutter;
		}
		return null;
	}
}
