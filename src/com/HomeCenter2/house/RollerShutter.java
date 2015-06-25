package com.HomeCenter2.house;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.R.integer;
import android.text.TextUtils;
import android.util.Log;

import com.HomeCenter2.R;
import com.HomeCenter2.data.configManager;

public class RollerShutter extends Device{
	private int roller;
	
	public RollerShutter(){
		super();
		setRoller(0);
		icon = R.drawable.ic_objet_volet;
	}
	
	private static final String TAG= "TMT RollerShutter";
	
	public RollerShutter(int id, String name, int roller){
		super(id, name);
		this.roller = roller;
		icon = R.drawable.ic_objet_volet;		
	}

	
	public void addDeviceToXML(Document document, Element rootElement){
		Element childElement = document.createElement(configManager.DEVICE);
		
		Element em = document.createElement(configManager.TYPE);
		em.appendChild(document.createTextNode(String.valueOf(configManager.ROLLER_SHUTTER)));
		childElement.appendChild(em);
		
		em = document.createElement(configManager.ID);
		em.appendChild(document.createTextNode(String.valueOf(getId())));
		childElement.appendChild(em);

		em = document.createElement(configManager.NAME);
		em.appendChild(document.createTextNode(getName()));
		childElement.appendChild(em);

		em = document.createElement(configManager.ROLLER);
		em.appendChild(document.createTextNode(String.valueOf(getRoller())));
		childElement.appendChild(em);
		
		em = document.createElement(configManager.ACTIVE);
		if(isActive())			
				em.appendChild(document.createTextNode("1"));
			else		
				em.appendChild(document.createTextNode("0"));
		childElement.appendChild(em);

		rootElement.appendChild(childElement);
	}
	
	public void readDeviceFromXML(NodeList lst){
		int size = lst.getLength();
		String name, value;
		for (int i = 1; i < size; i++) {
			Node fsNode = lst.item(i);
			if (fsNode != null) {							
				name = fsNode.getNodeName().toString();
				value = fsNode.getFirstChild().getNodeValue();		
				if(TextUtils.isEmpty(value)){
					continue;					
				}
				if(name.equals(configManager.ID)){
					setId(Integer.parseInt(value));
				}else if(name.equals(configManager.NAME))				{
					setName(value);					
				}else if(name.equals(configManager.LIGH)){
					setRoller(Integer.getInteger(value));
				}else if(name.equals(configManager.ACTIVE)){
					if(Integer.valueOf(value) == 0){
						setActive(false);	
					}else{
						setActive(true);
					}
				}
			}
		}		
	}


	public int getRoller() {
		return roller;
	}


	public void setRoller(int mLight) {
		this.roller = mLight;
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
