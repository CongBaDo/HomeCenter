package com.HomeCenter2.house;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.text.TextUtils;
import android.util.Log;

import com.HomeCenter2.PInfo;
import com.HomeCenter2.R;
import com.HomeCenter2.data.configManager;

public class Camera extends Control {
	private boolean state;
	private PInfo appInfo;

	public Camera() {
		super();
		state = false;
		icon = R.drawable.ic_camera_blk_wht;
		appInfo = null;
	}

	private static final String TAG = "TMT Camera";

	public Camera(int id, String name, boolean state) {
		super(id, name);
		this.state = state;
		icon = R.drawable.ic_camera_blk_wht;
		appInfo = null;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public void addDeviceToXML(Document document, Element rootElement) {
		Element childElement = document.createElement(configManager.DEVICE);

		Element em = document.createElement(configManager.TYPE);
		em.appendChild(document.createTextNode(String
				.valueOf(configManager.CAMERA)));
		childElement.appendChild(em);

		em = document.createElement(configManager.ID);
		em.appendChild(document.createTextNode(String.valueOf(getId())));
		childElement.appendChild(em);

		em = document.createElement(configManager.NAME);
		em.appendChild(document.createTextNode(getName()));
		childElement.appendChild(em);

		em = document.createElement(configManager.STATUS);
		if (isState())
			em.appendChild(document.createTextNode("1"));
		else
			em.appendChild(document.createTextNode("0"));
		childElement.appendChild(em);

		em = document.createElement(configManager.ACTIVE);
		if (isActive())
			em.appendChild(document.createTextNode("1"));
		else
			em.appendChild(document.createTextNode("0"));
		childElement.appendChild(em);

		em = document.createElement(configManager.LINK_APP);
		if (appInfo != null)
			em.appendChild(document.createTextNode(appInfo.getPname()));
		else
			em.appendChild(document.createTextNode(""));
		childElement.appendChild(em);

		rootElement.appendChild(childElement);
	}

	public void readDeviceFromXML(NodeList lst) {
		int size = lst.getLength();
		String name, value;
		for (int i = 1; i < size; i++) {
			Node fsNode = lst.item(i);
			if (fsNode != null) {
				name = fsNode.getNodeName().toString();
				Node first = fsNode.getFirstChild();
				if (first != null) {
					value = first.getNodeValue();
					if (TextUtils.isEmpty(value)) {
						continue;
					}
					if (name.equals(configManager.ID)) {
						setId(Integer.parseInt(value));
					} else if (name.equals(configManager.NAME)) {
						setName(value);
					} else if (name.equals(configManager.STATUS)) {
						setState(Boolean.valueOf(value));
					} else if (name.equals(configManager.ACTIVE)) {
						if (Integer.valueOf(value) == 0) {
							setActive(false);
						} else {
							setActive(true);
						}
					} else if (name.equals(configManager.LINK_APP)) {
						appInfo = new PInfo();
						appInfo.setPname(value);
					}
				}
			}
		}
	}

	public PInfo getAppInfo() {
		return appInfo;
	}

	public void setAppInfo(PInfo appInfo) {
		this.appInfo = appInfo;
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
