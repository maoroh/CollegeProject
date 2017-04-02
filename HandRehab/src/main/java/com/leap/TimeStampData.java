package com.leap;

import java.util.Map;

import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Vector;

public class TimeStampData {
	private int timeID;
	private Map<Finger.Type,FingerData> fingersData;
	private Vector palmDirection;
	
	public TimeStampData(int timeID, Map<Finger.Type,FingerData> fingersData, Vector palmDirection)
	{
		this.setTimeID(timeID);
		this.setFingersData(fingersData);
		this.setPalmDirection(palmDirection);
	}

	public Map<Finger.Type,FingerData> getFingersData() {
		return fingersData;
	}

	public void setFingersData(Map<Finger.Type,FingerData> fingersData) {
		this.fingersData = fingersData;
	}

	public Vector getPalmDirection() {
		return palmDirection;
	}

	public void setPalmDirection(Vector palmDirection) {
		this.palmDirection = palmDirection;
	}

	public int getTimeID() {
		return timeID;
	}

	public void setTimeID(int timeID) {
		this.timeID = timeID;
	}
	
}
