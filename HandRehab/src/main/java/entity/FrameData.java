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

import utility.JAXBTools;

/**
 * FrameData
 * Stores all the related Leap Motion Frame data.
 * @author maor
 *
 */
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
	
	/**
	 * Initialize new FrameData object.
	 * @param fingersData - Map of FingerData elements according to Finger.Type key.
	 * @param palmDirection - the palm direction vector.
	 * @param tipDirections - Map of Vector elements according to Finger.Type key.
	 */
	public FrameData(Map<Finger.Type,FingerData> fingersData, Vector palmDirection, Map<Finger.Type, Vector> tipDirections)
	{
		this.fingersData = fingersData;
		this.palmDirection = palmDirection;
		this.setTipDirections(tipDirections);
		this.anglesVector = null;
		this.anglesVectorTips= null;
		
	}

	/**
	 * FingersData Map getter.
	 * @return the fingersData map.
	 */
	public Map<Finger.Type,FingerData> getFingersData() {
		return fingersData;
	}

	/**
	 * Palm direction getter.
	 * @return
	 */
	public Vector getPalmDirection() {
		return palmDirection;
	}

	/**
	 * Set the angles vector of current frame.
	 * Calculate for each tipDirection of a finger the angle with the palmDirection and add them to anglesVector DataVector.
	 * Calculate for each distal bone direction the angle with the palmDirection and add them to anglesVector DataVector.
	 */
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
	
	
	/**
	 * Calculate for each tipDirection of a finger the angle with the initialData tipDirection of the same finger. 
	 * @param initialData - initial state of fingers , it stores all the tip directions in this state.
	 */
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


	/**
	 * anglesVector getter.
	 * @return anglesVector.
	 */
	public DataVector getAnglesVector() {
		return anglesVector;
	}
	
	/**
	 * anglesVectorTips getter.
	 * @return anglesVectorTips.
	 */
	public DataVector getAnglesVectorTips() {
		return anglesVectorTips;
	}
	
	/**
	 * Find the mean of 2 frames.
	 * @param f1 - the first frame data.
	 * @param f2 - the second frame data.
	 * @return - the mean frame.
	 */
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
	
	/**
	 * Distance getter.
	 * @return the distance.
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * Distance setter.
	 * @param distance - the distance value.
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}

	/**
	 * tipDirections getter.
	 * @return the tipDirections map.
	 */
	public Map<Finger.Type, Vector> getTipDirections() {
		return tipDirections;
	}

	/**
	 * tipDirections setter.
	 * @param tipDirections - the map of tips.
	 */
	public void setTipDirections(Map<Finger.Type, Vector> tipDirections) {
		this.tipDirections = tipDirections;
	}
	
	/**
	 * sets the value of anglesVectorTips to another value.
	 * @param vec - the DataVector values.
	 */
	public void replaceAngles(DataVector vec)
	{
		this.anglesVectorTips = vec;
	}
	
	/**
	 * Get Tips directions in this frame.
	 * @return InitialData object that stores all the tips directions.
	 */
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
