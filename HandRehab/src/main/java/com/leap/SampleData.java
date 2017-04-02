package com.leap;

import java.util.ArrayList;

public class SampleData {
	ArrayList <TimeStampData> timeStampsData;
	
	public SampleData()
	{
		this.timeStampsData = new ArrayList<TimeStampData>();
	}
	
	public void addTimeStamp(TimeStampData data)
	{
		this.timeStampsData.add(data);
	}
	
	public TimeStampData getTimeStamp(int index)
	{
		return this.timeStampsData.get(index);
	}
	
}
