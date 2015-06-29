package com.HomeCenter2.house;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.text.TextUtils;
import android.util.Log;

import com.HomeCenter2.R;
import com.HomeCenter2.data.configManager;

public abstract class LampRoot extends Device {
	private boolean state;	

	public LampRoot() {
		super();
		setState(false);		
	}
	
	public LampRoot(String name) {
		super();		
		setName(name);
		setState(false);
	}

	private static final String TAG = "TMT LampRoot";

	public LampRoot(int id, String name, boolean state) {
		super(id, name);
		setState(state);		
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
		if(this.state){
			setOnIcon();
		}else{
			setOffIcon();
		}
	}

	public void addDeviceToXML(Document document, Element rootElement) {
		Element childElement = document.createElement(configManager.DEVICE);

		Element em = document.createElement(configManager.TYPE);
		em.appendChild(document.createTextNode(String
				.valueOf(configManager.LAMP)));
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

		rootElement.appendChild(childElement);
	}

	public void readDeviceFromXML(NodeList lst) {
		int size = lst.getLength();
		String name, value;
		for (int i = 1; i < size; i++) {
			Node fsNode = lst.item(i);
			if (fsNode != null) {
				name = fsNode.getNodeName().toString();
				value = fsNode.getFirstChild().getNodeValue();
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
				}
			}
		}
	}
	
	public abstract void copyToDevice(LampRoot device);
	
	public abstract void setOnIcon();
	
	public abstract void setOffIcon();
	
}