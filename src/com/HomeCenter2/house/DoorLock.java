package com.HomeCenter2.house;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.text.TextUtils;
import android.util.Log;

import com.HomeCenter2.R;
import com.HomeCenter2.data.configManager;

public class DoorLock extends Device {
	
	private static final String TAG = "TMT DoorLock";
	private boolean state;
	private List<KeyDoorLock> keys;
	
	public List<KeyDoorLock> getKeys() {
		return keys;
	}

	public void setKeys(List<KeyDoorLock> keys) {
		this.keys = keys;
	}

	public DoorLock() {
		super();
		state = false;
		setOffIcon();
		keys = new ArrayList<KeyDoorLock>();
		initKey();
	}

	public DoorLock(int id, String name, boolean state) {
		super(id, name);
		this.state = state;
		if (state) {
			setOnIcon();
		} else {
			setOffIcon();
		}
		initKey();
		keys = new ArrayList<KeyDoorLock>();
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
				.valueOf(configManager.DOOR_LOCK)));
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

	public void setOnIcon() {
		icon = R.drawable.lock_opened_grayscale;
	}

	public void setOffIcon() {
		icon = R.drawable.lock_locked_grayscale;
	}
	
	public void initKey(){
		if(keys != null){		
			keys.clear();
			keys = null;
		}
		keys = new ArrayList<KeyDoorLock>();
		KeyDoorLock key; 
		for(int i = 0; i< configManager.MAX_KEY; i++){
			key= new KeyDoorLock(i, "0", "Key " + i , false);
			keys.add(key);
		}
	}

}
