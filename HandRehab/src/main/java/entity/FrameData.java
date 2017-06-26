package entity;

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
import com.leapmotion.leap.Finger.Type;

import tools.JAXBTools;

@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)

public class FrameData {
	private Map<Finger.Type, FingerData> fingersData;
	private Map<Finger.Type, Vector> tipDirections;
	private Vector palmDirection;
	private DataVector anglesVector;
	private DataVector anglesVectorTips;
	private double distance;
	
	public FrameData()
	{
		
		
	}
	
	public FrameData(Map<Finger.Type,FingerData> fingersData, Vector palmDirection, Map<Finger.Type, Vector> tipDirections)
	{
		this.setFingersData(fingersData);
		this.setPalmDirection(palmDirection);
		this.setTipDirections(tipDirections);
		this.anglesVector = null;
		
	}
	
	public FrameData(Map<Finger.Type,FingerData> fingersData, Vector palmDirection)
	{
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
			
			this.anglesVector = anglesVector;
	}
	
	
	public void setAnglesVectorTips(InitialData initialData) {
		Map<Finger.Type, Vector> fingersTip = this.getTipDirections();
		
		DataVector anglesVector2 = new DataVector();
		
		for(Finger.Type type: Finger.Type.values())
		{
			Vector tipDirection = fingersTip.get(type);
			double angle = tipDirection.angleTo(initialData.getTipVector(type).toVector());
			anglesVector2.addCoordinate(angle);
		}
		
		this.anglesVectorTips = anglesVector2;
	}



	public DataVector getAnglesVector() {
		return anglesVector;
	}
	
	public DataVector getAnglesVectorTips() {
		return anglesVectorTips;
	}
	
	public static FrameData framesAvg(FrameData f1, FrameData f2) 
	{
		DataVector d1 = f1.getAnglesVectorTips();
		DataVector d2 = f2.getAnglesVectorTips();
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
		this.anglesVectorTips = vec;
	}
	
	public InitialData getTipsAngles()
	{
		ArrayList <DataVector> fingersTip = new ArrayList<DataVector>();
		
		
		for(Finger.Type finger : Finger.Type.values())
		{
			Vector tipDirection = this.tipDirections.get(finger);
			DataVector fingerTip = DataVector.convertToDataVector(tipDirection);
			fingerTip.setName(finger);
			fingersTip.add(fingerTip);
		}
		
		return new InitialData(fingersTip);
	}

	
	
}
