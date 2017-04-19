package com.leap;

import java.util.Map;

import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Finger.Type;
import com.leapmotion.leap.Vector;

public final class VecTools {
	
	
	
	public static AnglesVector getAnglesVector (FrameData data)
	{
		Map<Type, FingerData> fingersData = data.getFingersData();
		Vector palmDirection = data.getPalmDirection();
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
		return anglesVector;
	}

}
