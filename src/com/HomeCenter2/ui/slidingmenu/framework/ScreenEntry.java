package com.HomeCenter2.ui.slidingmenu.framework;

public class ScreenEntry {
	private int id;
	private int groupID;
	private int titleId;
	private int iconId;	
	private int iconIdSelected;


	private Class<?> primaryClass;
	private int numbersNotification;
	private String tag;	

	public ScreenEntry(int _groupId, int _id, int _titleId, int _iconId, int _iconIdSelected, String _tag,
			Class<?> _primaryClass) {
		groupID = _groupId;
		id = _id;
		titleId = _titleId;
		iconId = _iconId;
		iconIdSelected = _iconIdSelected;
		primaryClass = _primaryClass;
		numbersNotification = 0;	
		tag= _tag;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

	public int getGroupID() {
		return groupID;
	}

	public void setTitleId(int titleId) {
		this.titleId = titleId;
	}

	public int getTitleId() {
		return titleId;
	}

	public void setIconId(int iconId) {
		this.iconId = iconId;
	}

	public int getIconId() {
		return iconId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setPrimaryClass(Class<?> primaryClass) {
		this.primaryClass = primaryClass;
	}

	public Class<?> getPrimaryClass() {
		return primaryClass;
	}

	public void setNumbersNotification(int numbersNotification) {
		this.numbersNotification = numbersNotification;
	}

	public int getNumbersNotification() {
		return numbersNotification;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public int getIconIdSelected() {
		return iconIdSelected;
	}

	public void setIconIdSelected(int iconIdSelected) {
		this.iconIdSelected = iconIdSelected;
	}
}
