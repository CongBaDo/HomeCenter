package com.HomeCenter2.ui.slidingmenu.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import com.HomeCenter2.HomeCenter2;
import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.R;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.ui.mainS.MyAudioSystemScreen;
import com.HomeCenter2.ui.mainS.MyCamerasScreen;
import com.HomeCenter2.ui.mainS.MyDevicesScreen;
import com.HomeCenter2.ui.mainS.MyEnergyScreen;
import com.HomeCenter2.ui.mainS.MyParametersScreen;
import com.HomeCenter2.ui.mainS.MyRemotesScreen;
import com.HomeCenter2.ui.mainS.MySensorsScreen;

public class ScreenManager {
	static final String TAG = "ScreenManager";

	public final static int HOUSE_GROUP_ID = 0;

	// Change mNameTV (no longer tabs)

	public final static int MY_ACCOUNT_TAB_ID = 0;
	public final static int MY_DEVICES_TAB_ID = MY_ACCOUNT_TAB_ID + 2;
	public final static int MY_SENSORS_TAB_ID = MY_ACCOUNT_TAB_ID + 3;
	public final static int MY_CAMERAS_TAB_ID = MY_ACCOUNT_TAB_ID + 4;
	public final static int MY_REMOTE_TAB_ID = MY_ACCOUNT_TAB_ID + 5;
	public final static int EXIT_TAB_ID = MY_ACCOUNT_TAB_ID + 6;
	public final static int MY_AUDIO_TAB_ID = MY_ACCOUNT_TAB_ID + 7;
	public final static int MY_ENERGY_TAB_ID = MY_ACCOUNT_TAB_ID + 8;

	public final static int SETTING_TAB_ID = MY_ACCOUNT_TAB_ID + 9;
	public final static int CONFIG_TAG_ID = MY_ACCOUNT_TAB_ID + 11;

	// Sub main screen
	public final static String MY_ACCOUNT_TAB = "MyAccountScreen";
	public final static String MY_DEVICE_TAG = "MyDeviceScreen";
	public final static String MY_SENSORS_TAB = "MysSensorsScreen";
	public final static String MY_CAMERAS_TAB = "MyCamerasScreen";
	public final static String MY_REMOTE_TAB = "MyRemotesScreen";
	public final static String MY_AUDIO_TAB = "MyAudioDevice";
	public final static String MY_ENERGY_TAB = "MyEnergyDevice";
	public final static String EXIT_TAB = "ExitDevice";

	public final static String DETAIL_DEVICE_TAG = "MyDetailScreen";
	public final static String SETTING_TAG = "SettingHouse";
	public final static String CONFIG_TAG = "Config";

	public final static String PARAMETER_CONFIG_TAG = "ParameterConfigScreen";
	public final static String PARAMETER_ROOM_TAG = "ParameterRoomScreen";

	public final static String SCHEDULE_DEVICE_TAG = "ShceduleDeviceScreen";
	public final static String SCHEDULE_REMOTE_TAG = "ShceduleRemoteScreen";
	public final static String MAIN_MENU_TAG = "MainMenuScreen";

	public final static int NUM_SECTION_TABS = 5;
	public final static ScreenGroupEntry SCREEN_GROUP[] = {};

	public final static ScreenEntry MENU_SCREEN_ENTRIES[] = {
		
			new ScreenEntry(HOUSE_GROUP_ID, MY_ACCOUNT_TAB_ID,
				R.string.my_account_menu_item, R.drawable.ic_home,
				R.drawable.ic_home_selected, MY_ACCOUNT_TAB,
				MyParametersScreen.class),
		
			new ScreenEntry(HOUSE_GROUP_ID, MY_ACCOUNT_TAB_ID,
					R.string.my_account_menu_item, R.drawable.ic_home,
					R.drawable.ic_home_selected, MY_ACCOUNT_TAB,
					MyParametersScreen.class),

					
					
			new ScreenEntry(HOUSE_GROUP_ID, MY_DEVICES_TAB_ID,
					R.string.my_devices_menu_item, R.drawable.ic_prise,
					R.drawable.ic_prise_selected, MY_DEVICE_TAG,
					MyDevicesScreen.class),

			new ScreenEntry(HOUSE_GROUP_ID, MY_SENSORS_TAB_ID,
					R.string.my_sensors_menu_item, R.drawable.ic_temperature,
					R.drawable.ic_temperature_selected, MY_SENSORS_TAB,
					MySensorsScreen.class),

			new ScreenEntry(HOUSE_GROUP_ID, MY_REMOTE_TAB_ID,
					R.string.my_remote_menu_item, R.drawable.ic_remote,
					R.drawable.ic_remote_selected, MY_REMOTE_TAB,
					MyRemotesScreen.class),

			new ScreenEntry(HOUSE_GROUP_ID, MY_AUDIO_TAB_ID,
					R.string.my_audio_menu_item, R.drawable.ic_audio,
					R.drawable.ic_audio_selected, MY_AUDIO_TAB,
					MyAudioSystemScreen.class),

			new ScreenEntry(HOUSE_GROUP_ID, MY_CAMERAS_TAB_ID,
					R.string.my_cameras_menu_item, R.drawable.ic_surveillance,
					R.drawable.ic_surveillance_selected, MY_CAMERAS_TAB,
					MyCamerasScreen.class),

			new ScreenEntry(HOUSE_GROUP_ID, MY_ENERGY_TAB_ID,
					R.string.my_energy_menu_item, R.drawable.ic_energy,
					R.drawable.ic_energy_selected, MY_ENERGY_TAB,
					MyEnergyScreen.class),

			new ScreenEntry(HOUSE_GROUP_ID, EXIT_TAB_ID, R.string.exit,
					R.drawable.sidebar_exit_icon, R.drawable.sidebar_exit_icon,
					EXIT_TAB, null)

	};

	private ScreenEntry[] menuScreenEntries;

	private List<OnVisiblePrimariesChangeListener> mListOnVisiblePrimaryChangeListener;
	private List<OnPrimaryIconChangeListener> mListOnPrimaryIconChangeListener;
	private List<OnPrimaryBadgeChangeListener> mListOnPrimaryBadgeChangeListeners;

	private HashMap<Integer, ScreenEntry> mMap;
	private List<ScreenEntry> mPrimaryEntries;
	private List<ScreenEntry> visibleScreenEntries = null;

	private List<OnVisibleGroupsChangeListener> mListOnVisibleGroupsChangeListener;
	// private List<OnGroupIconChangeListener> mListOnGroupIconChangeListener;
	// private List<OnGroupBadgeChangeListener>
	// mListOnGroupBadgeChangeListeners;

	private HashMap<Integer, ScreenGroupEntry> mMapGroup;
	private List<ScreenGroupEntry> mGroupEntries;

	private ScreenManager() {
		// Group
		mListOnVisibleGroupsChangeListener = new ArrayList<OnVisibleGroupsChangeListener>();
		// mListOnGroupIconChangeListener = new
		// ArrayList<OnGroupIconChangeListener>();
		// mListOnGroupBadgeChangeListeners = new
		// ArrayList<OnGroupBadgeChangeListener>();

		mMapGroup = new HashMap<Integer, ScreenGroupEntry>();
		mGroupEntries = new ArrayList<ScreenGroupEntry>();

		loadDefaultSectionsOrder();

		// Primary
		mListOnVisiblePrimaryChangeListener = new ArrayList<OnVisiblePrimariesChangeListener>();
		mListOnPrimaryIconChangeListener = new ArrayList<OnPrimaryIconChangeListener>();
		mListOnPrimaryBadgeChangeListeners = new ArrayList<OnPrimaryBadgeChangeListener>();

		mMap = new HashMap<Integer, ScreenEntry>();
		mPrimaryEntries = new ArrayList<ScreenEntry>();
	}

	private int getPrimaryEntryByPrimaryId(int id) {
		ScreenEntry screenEntry;
		for (int i = 0; i < menuScreenEntries.length; i++) {
			screenEntry = menuScreenEntries[i];
			if (screenEntry.getId() == id) {
				return i;
			}
		}
		return -1;
	}

	public HashMap<Integer, ScreenGroupEntry> getMapGroup() {
		return mMapGroup;
	}

	public int[] getSectionsOrder() {
		int length = mMapGroup.size() < NUM_SECTION_TABS ? mMapGroup.size()
				: NUM_SECTION_TABS;
		int[] order = new int[length];

		for (int i = 0; i < length; i++)
			order[i] = ((ScreenGroupEntry) mMapGroup.get(i)).getId();

		return order;
	}

	public void setSectionsOrder(int[] order) {
		int length = order.length;

		if (length <= NUM_SECTION_TABS) {
			mMapGroup.clear();
			mGroupEntries.clear();

			for (int i = 0; i < length; i++) {
				mGroupEntries.add(SCREEN_GROUP[order[i]]);
				// i same id
				mMapGroup.put(i, (ScreenGroupEntry) mGroupEntries.get(i));
			}
		}

		for (int i = 0; i < mListOnVisibleGroupsChangeListener.size(); i++) {
			mListOnVisibleGroupsChangeListener.get(i).onVisibleGroupsChanged();
		}
	}

	public void loadDefaultSectionsOrder() {
		mMapGroup.clear();
		mGroupEntries.clear();
		for (int i = 0; i < SCREEN_GROUP.length; i++) {
			mGroupEntries.add(SCREEN_GROUP[i]);
			mMapGroup.put(SCREEN_GROUP[i].getId(), mGroupEntries.get(i));
		}
	}

	public static int getSizeGroupPrimary() {
		return SCREEN_GROUP.length;
	}

	public ScreenEntry getPrimaryEntryByPositionInGroupId(int idPosition,
			int groupId) {
		if (idPosition < 0 || groupId < 0) {
			return null;
		}
		List<ScreenEntry> entriesInGroup = primaryEntriesByGroupId(groupId);
		if (entriesInGroup == null) {
			return null;
		}
		if (idPosition >= entriesInGroup.size()) {
			return null;
		}
		return entriesInGroup.get(idPosition);
	}

	public synchronized List<ScreenEntry> primaryEntriesByGroupId(int groupId) {
		List<ScreenEntry> primaryEntries = new ArrayList<ScreenEntry>();
		if (groupId < 0) {
			return null;
		}

		for (ScreenEntry entry : visibleScreenEntries) {
			if (entry.getGroupID() == groupId) {
				primaryEntries.add(entry);
			}
		}
		return primaryEntries;
	}

	public ScreenEntry getMainScreenEntryByTabID(int tabId) {
		int len = menuScreenEntries.length;
		ScreenEntry entry = null;
		for (int i = 0; i < len; i++) {
			entry = menuScreenEntries[i];
			if (entry.getId() == tabId) {
				return entry;
			}
		}
		return null;
	}

	public Fragment createScreenByPrimaryEntry(ScreenEntry entry,
			Activity activity) {
		return createScreenByPrimaryEntry(entry, activity, null);
	}

	public Fragment createScreenByPrimaryEntry(ScreenEntry entry,
			Activity activity, Bundle bundle) {

		if (entry == null)
			return null;

		Fragment fragment = null;
		int id = entry.getId();
		SlidingBaseActivity slidingBaseActivity = (SlidingBaseActivity) activity;
		switch (id) {

		case MY_ACCOUNT_TAB_ID:
			fragment = new MyParametersScreen(entry.getTitleId(),
					entry.getTag(), slidingBaseActivity);
			break;

		case MY_DEVICES_TAB_ID:
			fragment = new MyDevicesScreen(entry.getTitleId(), entry.getTag(),
					slidingBaseActivity);
			break;
		case MY_SENSORS_TAB_ID:
			fragment = new MySensorsScreen(entry.getTitleId(), entry.getTag(),
					slidingBaseActivity);
			break;
		case MY_CAMERAS_TAB_ID:
			fragment = new MyCamerasScreen(entry.getTitleId(), entry.getTag(),
					slidingBaseActivity);
			break;
		case MY_REMOTE_TAB_ID:
			fragment = new MyRemotesScreen(entry.getTitleId(), entry.getTag(),
					slidingBaseActivity);
			break;
		case EXIT_TAB_ID:
			Log.d(TAG, "onOptionItemSelected");
			HomeCenterUIEngine uiEngine = RegisterService
					.getHomeCenterUIEngine();
			if (uiEngine != null) {

				uiEngine.setIsLogined(false);

				uiEngine.closeSocket();
				((HomeCenter2) activity).doExit(activity);
			}
			break;
		case MY_AUDIO_TAB_ID:
			fragment = new MyAudioSystemScreen(entry.getTitleId(),
					entry.getTag(), slidingBaseActivity);
			break;
		case MY_ENERGY_TAB_ID:
			fragment = new MyEnergyScreen(entry.getTitleId(), entry.getTag(),
					slidingBaseActivity);
			break;
		default:
			fragment = new Fragment();
		}
		return fragment;
	}

	public interface OnVisiblePrimariesChangeListener {
		public void onVisiblePrimariesChanged();
	}

	public interface OnPrimaryIconChangeListener {
		public void onPrimaryIconChanged(int primaryId, int iconId);
	}

	public interface OnPrimaryBadgeChangeListener {
		public void onPrimaryBadgeChanged(int priamryId, int badge);
	}

	public interface OnVisibleGroupsChangeListener {
		public void onVisibleGroupsChanged();
	}

	public interface OnGroupIconChangeListener {
		public void onGroupIconChanged(int primaryId, int iconId);
	}

	public interface OnGroupBadgeChangeListener {
		public void onGroupBadgeChanged(int priamryId, int badge);
	}

	private static ScreenManager m_primaryManager = null;

	public static synchronized ScreenManager getInstance() {
		if (m_primaryManager == null) {
			m_primaryManager = new ScreenManager();
		}
		return m_primaryManager;
	}

	public ScreenEntry getPrimaryEntryById(int primaryId) {
		return mMap.get(primaryId);
	}

	public int[] getAllPrimaryIds() {
		int[] ids = new int[menuScreenEntries.length];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = menuScreenEntries[i].getId();
		}
		return ids;
	}

	// public void changePrimaryIcon(int primaryId, int iconId) {
	// ScreenEntry primaryEntry = (ScreenEntry) mMap.get(primaryId);
	// if (primaryEntry != null) {
	// primaryEntry.setIconId(iconId);
	// }
	//
	// for (int i = 0; i < mListOnPrimaryIconChangeListener.size(); i++) {
	// mListOnPrimaryIconChangeListener.get(i).onPrimaryIconChanged(
	// primaryId, iconId);
	// }
	// }

	public void setBadge(int screenId, int number) {
		int size = menuScreenEntries.length;
		ScreenEntry primaryEntry = null;
		for (int i = 0; i < size; i++) {
			primaryEntry = menuScreenEntries[i];
			if (primaryEntry.getId() == screenId) {
				primaryEntry.setNumbersNotification(number);
				for (int j = 0, count = mListOnPrimaryBadgeChangeListeners
						.size(); j < count; j++) {
					mListOnPrimaryBadgeChangeListeners.get(j)
							.onPrimaryBadgeChanged(screenId, number);
				}
			}
		}
	}

	public void addOnVisiblePrimariesChangeListener(
			OnVisiblePrimariesChangeListener listener) {
		mListOnVisiblePrimaryChangeListener.add(listener);
	}

	public void removeOnVisibleTabsChangeListener(
			OnVisiblePrimariesChangeListener listener) {
		mListOnVisiblePrimaryChangeListener.remove(listener);
	}

	public void addOnPrimaryIconChangeListener(
			OnPrimaryIconChangeListener listener) {
		mListOnPrimaryIconChangeListener.add(listener);
	}

	public void removeOnPrimaryIconChangeListener(
			OnPrimaryIconChangeListener listener) {
		mListOnPrimaryIconChangeListener.remove(listener);
	}

	public void addOnPrimaryBadgeChangeListener(
			OnPrimaryBadgeChangeListener listener) {
		mListOnPrimaryBadgeChangeListeners.add(listener);
	}

	public void removeOnPrimaryBadgeChangeListener(
			OnPrimaryBadgeChangeListener listener) {
		mListOnPrimaryBadgeChangeListeners.remove(listener);
	}

	public void addOnVisibleGroupsChangeListener(
			OnVisibleGroupsChangeListener listener) {
		mListOnVisibleGroupsChangeListener.add(listener);
	}

	public void removeOnVisibleGroupsChangeListener(
			OnVisibleGroupsChangeListener listener) {
		mListOnVisibleGroupsChangeListener.remove(listener);
	}

	/**
	 * Group Sort
	 */
	public final static int NUM_GROUP = SCREEN_GROUP.length;

	public int[] getAllGroupsIds() {
		int[] ids = new int[NUM_GROUP];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = SCREEN_GROUP[i].getId();
		}
		return ids;
	}

	public int[] getGroupIds(Context context) {
		if (context == null) {
			return null;
		}
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		String str = sharedPreferences.getString("GroupIndex", null);

		int[] groups = null;
		if (str == null) {
			groups = new int[NUM_GROUP];
			for (int i = 0; i < groups.length; i++) {
				groups[i] = i;
			}
		} else {
			String[] ids = str.split(",");
			groups = new int[ids.length];
			for (int i = 0; i < groups.length; i++) {
				if (!TextUtils.isEmpty(ids[i]))
					groups[i] = Integer.parseInt(ids[i]);
			}
		}
		return groups;
	}

	public synchronized List<ScreenEntry> getVisibleScreenEntries() {
		return visibleScreenEntries;
	}

	public synchronized int findPositionScreenById(int id) {
		int size = visibleScreenEntries.size();
		ScreenEntry screenEntry = null;
		for (int i = 0; i < size; i++) {
			screenEntry = visibleScreenEntries.get(i);
			if (screenEntry.getId() == id) {
				return i;
			}
		}
		return -1;
	}

	public synchronized ScreenEntry getScreenEntryByPosition(int position) {
		ScreenEntry screen = visibleScreenEntries.get(position);

		return screen;
	}

	public ScreenEntry getScreenEntryByTag(String tag) {
		ScreenEntry screenEntry = null;
		int size = MENU_SCREEN_ENTRIES.length;
		for (int i = 0; i < size; i++) {
			screenEntry = MENU_SCREEN_ENTRIES[i];
			if (screenEntry.getTag().equals(tag)) {
				return screenEntry;
			}
		}
		return null;
	}

	public ScreenEntry getScreenEntryByTagId(int tagId) {
		ScreenEntry screenEntry = null;
		int size = MENU_SCREEN_ENTRIES.length;
		for (int i = 0; i < size; i++) {
			screenEntry = MENU_SCREEN_ENTRIES[i];
			if (screenEntry.getId() == tagId) {
				return screenEntry;
			}
		}
		return null;
	}

	public int getPositionInListByTag(String tag) {
		ScreenEntry screenEntry = null;
		int size = MENU_SCREEN_ENTRIES.length;
		for (int i = 0; i < size; i++) {
			screenEntry = MENU_SCREEN_ENTRIES[i];
			if (screenEntry.getTag().equals(tag)) {
				return i;
			}
		}
		return 1;
	}
}
