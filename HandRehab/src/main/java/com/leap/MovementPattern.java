package com.leap;

import java.util.ArrayList;

public class MovementPattern {
	
	private ArrayList <AnglesVector> meanFrames;
	
	public MovementPattern()
	{
		this.meanFrames = new ArrayList<AnglesVector>();
	}
	
	public AnglesVector getVector(int index)
	{
		return this.meanFrames.get(index);
	}
	
	public void addVector(AnglesVector frame)
	{
		this.meanFrames.add(frame);
	}

}
