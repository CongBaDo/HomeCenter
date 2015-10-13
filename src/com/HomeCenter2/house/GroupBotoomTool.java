package com.HomeCenter2.house;

public class GroupBotoomTool {
	
	public enum SPECIAL {NONE, ON, OFF, MIC} 
	
	private Sensor[] doubSensor;
	private String tempText;
	private int specialTool;
	private SPECIAL specialToolType;
	
	public SPECIAL getSpecialToolType() {
		return specialToolType;
	}
	public void setSpecialToolType(SPECIAL specialToolType) {
		this.specialToolType = specialToolType;
	}
	public Sensor[] getDoubSensor() {
		return doubSensor;
	}
	public void setDoubSensor(Sensor[] doubSensor) {
		this.doubSensor = doubSensor;
	}
	public String getTempText() {
		return tempText;
	}
	public void setTempText(String tempText) {
		this.tempText = tempText;
	}
	public int getSpecialTool() {
		return specialTool;
	}
	public void setSpecialTool(int specialTool) {
		this.specialTool = specialTool;
	}

}
