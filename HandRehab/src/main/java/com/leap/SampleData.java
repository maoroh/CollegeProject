package com.leap;

import java.util.ArrayList;

public class SampleData {
	ArrayList <FrameData> framesData;
	
	public SampleData()
	{
		this.framesData = new ArrayList<FrameData>();
	}
	
	public void addFrame(FrameData data)
	{
		this.framesData.add(data);
	}
	
	public FrameData getFrame(int index)
	{
		return this.framesData.get(index);	
	}
	
}
