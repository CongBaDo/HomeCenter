package com.HomeCenter2.house;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.HomeCenter2.R;

public class Sensor extends Device {
	public Sensor() {
		super();
	}

	public Sensor(int id, String name) {
		super();
	}

	public Sensor(int id, String name, boolean state) {
		super(id, name);
	}

	@Override
	public void addDeviceToXML(Document document, Element rootElement) {
		// TODO Auto-generated method stub

	}

	@Override
	public void readDeviceFromXML(NodeList lst) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOnIcon() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOffIcon() {
		// TODO Auto-generated method stub

	}
}
