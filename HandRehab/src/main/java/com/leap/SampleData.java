package com.leap;

import java.util.ArrayList;

public class SampleData {
	private ArrayList <FrameData> framesData;
	
	public SampleData()
	{
		this.framesData = new ArrayList<FrameData>();
	}
	
	public SampleData(ArrayList<FrameData> framesData)
	{
		this.setFrames(framesData);
	}
	
	public void addFrame(FrameData data)
	{
		this.framesData.add(data);
	}
	
	public FrameData getFrame(int index)
	{
		return this.framesData.get(index);	
	}
	
	public ArrayList <AnglesVector> getSamplesVector()
	{
		ArrayList<AnglesVector> vectors = new ArrayList<AnglesVector>();
		
		for(FrameData data : this.framesData)
		{
			data.setAnglesVector();
			vectors.add(data.getAnglesVector());
		}
		
		return vectors;
	}
	
	public void setFrames(ArrayList<FrameData> framesData)
	{
		this.framesData = framesData;
	}
	
	public int getNumOfFrames()
	{
		return this.framesData.size();
	}
}
