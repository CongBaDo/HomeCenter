package com.HomeCenter2.house;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.util.Log;

import com.HomeCenter2.data.configManager;

public class Area {
	private int id;
	private String name;
	private List<Room> rooms = null;

	public static final String TAG = "TMT Arear";

	public Area() {
		id = -1;
		name = "";
		setRooms(new ArrayList<Room>());
	}

	public Area(int id, String name, List<Room> rooms) {
		this.id = id;
		this.name = name;
		this.rooms = rooms;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

	public void clearAllRoom() {
		this.rooms.clear();
	}

	public void addRoomToArea(Room room) {
		if (room == null) {
			return;
		}
		Log.d(TAG, "addRoomToFloor");
		room.setAreaId(this.id);
		this.rooms.add(room);
	}

	public Element addAreaToXML(Document document) {
		Element childElement = document.createElement(configManager.AREA);

		Element em;

		em = document.createElement(configManager.ID);
		em.appendChild(document.createTextNode(String.valueOf(getId())));
		childElement.appendChild(em);

		em = document.createElement(configManager.NAME);
		Log.d(TAG, "AddArea:mNameTV: " + getName());
		em.appendChild(document.createTextNode(getName()));
		childElement.appendChild(em);

		return childElement;

	}

	/**
	 * get all node is aree. Other Area is getten list node. 1/ id 2/ mNameTV 3/
	 * childs
	 * 
	 * @param document
	 * @param objects
	 */
	public void readAreaFromXML(NodeList lst, List<Object> objects) {

		int sizeChild = lst.getLength();
		Room room = null;
		for (int j = 0; j < sizeChild; j++) {
			Node fsNode = lst.item(j);
			if (fsNode != null) {
				String name = fsNode.getNodeName();
				String value;
				if (name.equals(configManager.ID)) {
					value = fsNode.getFirstChild().getNodeValue();
					setId(Integer.valueOf(value));
				} else if (name.equals(configManager.NAME)) {
					value = fsNode.getFirstChild().getNodeValue();
					setName(value);
				} else if (name.equals(configManager.ROOM)) {
					NodeList lstChild = fsNode.getChildNodes();
					if (lstChild != null) {
						room = new Room();
						objects.add(room);
						this.getRooms().add(room);
						room.setAreaId(this.id);
						room.setActive(true);
						room.readRoomFromXML(lstChild, objects);						
					}
				}
			}
		}
	}
	
	public void addAllObjectInHome(List<Object> objects){		
		int size = rooms.size();
		Room room = null;
		for(int i = 0;i < size; i++){
			room = rooms.get(i);
			objects.add(room);
			room.addAllObjectInHome(objects);
		}
	}

	private void refreshRoomsByActive(){
		int size = rooms.size();
		Room room = null;		
		for(int i = 0; i<size ;i++){
			room = rooms.get(i);
			room.refreshActiveTempByActiveValue();
		}
	}
	
	private void refreshRoomsByActiveTemp(){
		int size = rooms.size();
		Room room = null;
		for(int i = 0; i<size ;i++){
			room = rooms.get(i);			
			room.refreshActiveByActiveTempValue();
		}
	}
	
	public void refreshRoomsInArea(boolean isActive){
		if(isActive){
			refreshRoomsByActive();
		}else {
			Log.d(TAG, "refreshRooms::"+ rooms.size() + "//////////////////");
			refreshRoomsByActiveTemp();
		}
		
	}
}
