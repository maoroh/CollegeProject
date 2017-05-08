package com.leap;

import java.util.HashMap;
import java.util.Map;

import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Vector;
import com.leapmotion.leap.Finger.Type;

public class FrameData {
	private int timeID;
	private Map<Finger.Type,FingerData> fingersData;
	private Vector palmDirection;
	private AnglesVector anglesVector;
	
	public FrameData(int timeID, Map<Finger.Type,FingerData> fingersData, Vector palmDirection)
	{
		this.setTimeID(timeID);
		this.setFingersData(fingersData);
		this.setPalmDirection(palmDirection);
		this.anglesVector = null;
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
	
	public void setAnglesVector() {
		
			Map<Type, FingerData> fingersData = this.getFingersData();
			Vector palmDirection = this.getPalmDirection();
			AnglesVector anglesVector = new AnglesVector();
			
			//Create all the angles with the palm
			//Fingers
			for(Finger.Type type: Finger.Type.values())
			{
				FingerData fingerData = fingersData.get(type);
				Map <Bone.Type, Vector> bonesDirection = fingerData.getBonesDirection();
				
				//Bones
				  for(Bone.Type boneType : Bone.Type.values()) {
					  Vector boneDirection = bonesDirection.get(boneType);
					  double angle =  boneDirection.angleTo(palmDirection);
					  anglesVector.addCoordinate(angle);
				  }
			}
			
			//Add angles of each bone to the another bones
			//Fingers
			for(Finger.Type type: Finger.Type.values())
			{
				FingerData fingerData = fingersData.get(type);
				Map <Bone.Type, Vector> bonesDirection = fingerData.getBonesDirection();
				
				Vector distalDirection = bonesDirection.get(Bone.Type.TYPE_DISTAL);
				Vector intermediateDirection = bonesDirection.get(Bone.Type.TYPE_INTERMEDIATE);
				Vector proximalDirection = bonesDirection.get(Bone.Type.TYPE_PROXIMAL);
				Vector metacarpalDirection = bonesDirection.get(Bone.Type.TYPE_METACARPAL);
			
				if(type != Finger.Type.TYPE_THUMB)
				{
					anglesVector.addCoordinate(distalDirection.angleTo(intermediateDirection));
					anglesVector.addCoordinate(intermediateDirection.angleTo(proximalDirection));
					anglesVector.addCoordinate(proximalDirection.angleTo(metacarpalDirection));
				}
				
				else //Thumb Finger
				{
					anglesVector.addCoordinate(distalDirection.angleTo(proximalDirection));
					anglesVector.addCoordinate(proximalDirection.angleTo(metacarpalDirection));
				}
			  
			}
			this.anglesVector = anglesVector;
	}


	public AnglesVector getAnglesVector() {
		return anglesVector;
	}
	
	public static FrameData framesAvg(FrameData f1, FrameData f2) 
	{
		Map<Type, FingerData> avgFingersMapData = new HashMap<Finger.Type,FingerData>();
		
		Map<Finger.Type, FingerData> firstFingersMapData = f1.getFingersData();
		Map<Finger.Type, FingerData> secondFingersMapData = f2.getFingersData();
		Vector firstPalmDirection = f1.getPalmDirection();
		Vector secondPalmDirection = f2.getPalmDirection();
		Vector avgPalmDirection = firstPalmDirection.plus(secondPalmDirection).divide(2);

		for(Finger.Type fingerType: Finger.Type.values())
		{
			Map <Bone.Type, Vector> avgBonesDirections = new HashMap<Bone.Type,Vector>();
			//First frame fingers data
			FingerData firstFingerData = firstFingersMapData.get(fingerType);
			//Second frame fingers data
			FingerData secondFingerData = secondFingersMapData.get(fingerType);
		
			Map <Bone.Type, Vector> firstBonesDirections = firstFingerData.getBonesDirection();
			
			Map <Bone.Type, Vector> secondBonesDirections = secondFingerData.getBonesDirection();
			
			
			//Bones
			  for(Bone.Type boneType : Bone.Type.values()) 
			  {
				  Vector firstBoneDirection = firstBonesDirections.get(boneType);
				  Vector secondBoneDirection = secondBonesDirections.get(boneType);
				  Vector avgBoneDirection =  firstBoneDirection.plus(secondBoneDirection).divide(2);
				  avgBonesDirections.put(boneType, avgBoneDirection);
			  }
			  
			  avgFingersMapData.put(fingerType, new FingerData(avgBonesDirections)); 
		}
		return new FrameData(f1.getTimeID(), avgFingersMapData , avgPalmDirection);
	}
	
	
}
