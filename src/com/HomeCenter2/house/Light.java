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

public class Light extends Device{
	private int mLight;
	
	public Light(){
		super();
		setLight(0);
		
		icon = R.drawable.ic_objet_lumiere;
	}
	
	private static final String TAG= "TMT Light";
	
	public Light(int id, String name, int light){
		super(id, name);
		this.mLight = light;
		icon = R.drawable.ic_objet_lumiere;
		
	}

	
	public void addDeviceToXML(Document document, Element rootElement){
		Element childElement = document.createElement(configManager.DEVICE);
		
		Element em = document.createElement(configManager.TYPE);
		em.appendChild(document.createTextNode(String.valueOf(configManager.LIGHT)));
		childElement.appendChild(em);
		
		em = document.createElement(configManager.ID);
		em.appendChild(document.createTextNode(String.valueOf(getId())));
		childElement.appendChild(em);

		em = document.createElement(configManager.NAME);
		em.appendChild(document.createTextNode(getName()));
		childElement.appendChild(em);

		em = document.createElement(configManager.LIGH);
		em.appendChild(document.createTextNode(String.valueOf(getLight())));
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
				}else if(name.equals(configManager.NAME)){
					setName(value);					
				}else if(name.equals(configManager.LIGH)){					
					setLight(Integer.valueOf(value));
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


	public int getLight() {
		return mLight;
	}


	public void setLight(int mLight) {
		this.mLight = mLight;
	}

	@Override
	public void setOnIcon() {		
		icon = R.drawable.ic_objet_lumiere;
	}

	@Override
	public void setOffIcon() {
		icon = R.drawable.ic_objet_lumiere;
	}	
}
