package com.leap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Vector;
import com.tools.JAXBTools;
import com.leapmotion.leap.Finger.Type;

@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)

public class FrameData {
	private int timeID;
	private Map<Finger.Type, FingerData> fingersData;
	private Map<Finger.Type, Vector> tipDirections;
	private Vector palmDirection;
	private DataVector anglesVector;
	private DataVector anglesVector2;
	private double distance;
	
	public FrameData()
	{
		
		
	}
	
	
	
	public FrameData(int timeID, Map<Finger.Type,FingerData> fingersData, Vector palmDirection, Map<Finger.Type, Vector> tipDirections)
	{
		this.setTimeID(timeID);
		this.setFingersData(fingersData);
		this.setPalmDirection(palmDirection);
		this.setTipDirections(tipDirections);
		this.anglesVector = null;
		
	}
	
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
		
			Map<Finger.Type, Vector> fingersTip = this.getTipDirections();
			Map<Type, FingerData> fingersData = this.getFingersData();
			Vector palmDirection = this.getPalmDirection();
			DataVector anglesVector = new DataVector();
			
			for(Finger.Type type: Finger.Type.values())
			{
				Vector tipDirection = fingersTip.get(type);
				double angle = tipDirection.angleTo(palmDirection);
				anglesVector.addCoordinate(angle);
			}
			
			//Create all the angles with the palm
			//Fingers
			
			
			for(Finger.Type type: Finger.Type.values())
			{
				FingerData fingerData = fingersData.get(type);
				Map <BoneType, Vector> bonesDirection = fingerData.getBonesDirection();
				
				//Bones
				  for(BoneType boneType : BoneType.values())
				  {
					  Vector boneDirection = bonesDirection.get(boneType);
					  double angle =  boneDirection.angleTo(palmDirection);
					  
					   		if(boneType == BoneType.TYPE_DISTAL)
						  anglesVector.addCoordinate(angle);
				  }
			}
			
			//Add angles of each bone to the another bones
			//Fingers
			
			
			for(Finger.Type type: Finger.Type.values())
			{
				FingerData fingerData = fingersData.get(type);
				Map <BoneType, Vector> bonesDirection = fingerData.getBonesDirection();
				
				Vector distalDirection = bonesDirection.get(BoneType.TYPE_DISTAL);
				Vector intermediateDirection = bonesDirection.get(BoneType.TYPE_INTERMEDIATE);
				Vector proximalDirection = bonesDirection.get(BoneType.TYPE_PROXIMAL);
				Vector metacarpalDirection = bonesDirection.get(BoneType.TYPE_METACARPAL);
			
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
	
	
	public void setAnglesVector2(Data initialData) {
		Map<Finger.Type, Vector> fingersTip = this.getTipDirections();
		
		DataVector anglesVector2 = new DataVector();
		
		for(Finger.Type type: Finger.Type.values())
		{
			Vector tipDirection = fingersTip.get(type);
			double angle = tipDirection.angleTo(initialData.getTipVector(type).toVector());
			anglesVector2.addCoordinate(angle);
		}
		
		this.anglesVector2 = anglesVector2;
	}



	public DataVector getAnglesVector() {
		return anglesVector;
	}
	
	public DataVector getAnglesVector2() {
		return anglesVector2;
	}
	
	public static FrameData framesAvg(FrameData f1, FrameData f2) 
	{
		DataVector d1 = f1.getAnglesVector2();
		DataVector d2 = f2.getAnglesVector2();
		DataVector dAvg = new DataVector();
		for( int i = 0 ; i< d1.getSize(); i++)
		{
			double avg = (d1.getCoordinate(i) + d2.getCoordinate(i)) / 2;
			dAvg.addCoordinate(avg);
		}
		
		
		FrameData fd =  new FrameData();
		fd.replaceAngles(dAvg);
		return fd;
	}
	

	/*/
	public static FrameData framesAvg(FrameData f1, FrameData f2) 
	{
		Map<Finger.Type, FingerData> avgFingersMapData = new HashMap<Finger.Type,FingerData>();
		
		Map<Finger.Type, FingerData> firstFingersMapData = f1.getFingersData();
		Map<Finger.Type, FingerData> secondFingersMapData = f2.getFingersData();
		Vector firstPalmDirection = f1.getPalmDirection();
		Vector secondPalmDirection = f2.getPalmDirection();
		Map<Finger.Type, Vector> firstFingersTip  = f1.getTipDirections();
		Map<Finger.Type, Vector> secondFingersTip  = f2.getTipDirections();
		Vector avgPalmDirection = firstPalmDirection.plus(secondPalmDirection).divide(2);
		Map<Finger.Type, Vector> avgTipDirections = new HashMap<Finger.Type,Vector>();
		
		for(Finger.Type fingerType: Finger.Type.values())
		{
			 Vector firstAvgTipDirection =  firstFingersTip.get(fingerType);
			 Vector secondAvgTipDirection =  secondFingersTip.get(fingerType);
			 avgTipDirections.put(fingerType, firstAvgTipDirection.plus(secondAvgTipDirection).divide(2));
		}

		for(Finger.Type fingerType: Finger.Type.values())
		{
			Map <BoneType, Vector> avgBonesDirections = new HashMap<BoneType,Vector>();
			//First frame fingers data
			FingerData firstFingerData = firstFingersMapData.get(fingerType);
			//Second frame fingers data
			FingerData secondFingerData = secondFingersMapData.get(fingerType);
		
			Map <BoneType, Vector> firstBonesDirections = firstFingerData.getBonesDirection();
			
			Map <BoneType, Vector> secondBonesDirections = secondFingerData.getBonesDirection();
			
			
			//Bones
			  for(BoneType boneType : BoneType.values()) 
			  {
				  Vector firstBoneDirection = firstBonesDirections.get(boneType);
				  Vector secondBoneDirection = secondBonesDirections.get(boneType);
				  Vector avgBoneDirection =  firstBoneDirection.plus(secondBoneDirection).divide(2);
				  avgBonesDirections.put(boneType, avgBoneDirection);
			  }
			  
			  avgFingersMapData.put(fingerType, new FingerData(avgBonesDirections)); 
		}
		FrameData fd =  new FrameData(f1.getTimeID(), avgFingersMapData , avgPalmDirection , avgTipDirections);
		fd.setAnglesVector();
		fd.setAnglesVector2(StaticData.initData);
		return fd;
	}/*/

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public Map<Finger.Type, Vector> getTipDirections() {
		return tipDirections;
	}

	public void setTipDirections(Map<Finger.Type, Vector> tipDirections) {
		this.tipDirections = tipDirections;
	}
	
	public void replaceAngles(DataVector vec)
	{
		this.anglesVector2 = vec;
	}

	
	
}
