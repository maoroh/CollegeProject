package com.leap;

import java.util.ArrayList;

public class MovementPattern {
	
	private ArrayList <AnglesVector> meanFrames;
	
	public MovementPattern()
	{
		this.meanFrames = new ArrayList<AnglesVector>();
	}
	
	public void getVector(int index)
	{
		
	}
	
	public void addVector(AnglesVector frame)
	{
		this.meanFrames.add(frame);
	}

}
