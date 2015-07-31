package com.HomeCenter2.utils;

import com.HomeCenter2.data.configManager;

public class HCUtils {

	/**
	 * @param fileName ex: left.png, right.png
	 * @param roomName
	 * */
	public static String getFilePath(String fileName, String roomName){
		
		String value = null;
		value = configManager.FOLDERNAME+"/"+roomName.replace(" ", "")+"/"+fileName;
		return value;
	}
}
