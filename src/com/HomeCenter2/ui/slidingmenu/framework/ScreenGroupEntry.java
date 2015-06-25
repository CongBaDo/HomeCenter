package com.HomeCenter2.ui.slidingmenu.framework;

public class ScreenGroupEntry {
	private int id;
	private int titleId;

	public ScreenGroupEntry(int _id, int _titleId) {
		id = _id;
		titleId = _titleId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setTitleId(int titleId) {
		this.titleId = titleId;
	}

	public int getTitleId() {
		return titleId;
	}
}
