package com.leap;
import java.util.ArrayList;
import java.util.Map;

import com.leapmotion.leap.Vector;
import com.leapmotion.leap.Bone;

/**
 * 
 * @author maor
 *
 */
public class FingerData {
	private Map <Bone.Type, Vector> bonesDirection;
	
	public FingerData( Map <Bone.Type, Vector> bonesDirectionToPalm)
	{
		this.setBonesDirectionToPalm(bonesDirectionToPalm);
	
	}
	public Map <Bone.Type, Vector> getBonesDirection() {
		return bonesDirection;
	}
	
	
	public void setBonesDirectionToPalm(Map <Bone.Type, Vector> bonesDirectionToPalm) {
		this.bonesDirection = bonesDirectionToPalm;
	}
	
	
}

