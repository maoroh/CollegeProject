package entity;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.leapmotion.leap.Vector;
import com.leapmotion.leap.Bone;

/**
 * FingerData
 * Stores all the finger data from the Leap Motion Controller.
 * @author maor
 *
 */
@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class FingerData {
	
	
	private Map <BoneType, Vector> bonesDirection;
	
	public FingerData()
	{
		
	}
	/**
	 * Initialize new FingerData object with a Map of bones directions.
	 * @param bonesDirectionToPalm - the bonesDirections map.
	 */
	public FingerData( Map <BoneType, Vector> bonesDirectionToPalm)
	{
		this.setBonesDirectionToPalm(bonesDirectionToPalm);
		
	
	}
	
	/**
	 * bonesDirections getter.
	 * @return bonesDirections Map.
	 */
	public Map <BoneType, Vector> getBonesDirection() {
		return bonesDirection;
	}
	
	/**
	 * bonesDirection setter.
	 * @param bonesDirectionToPalm - the Map to set.
	 */
	public void setBonesDirectionToPalm(Map <BoneType, Vector> bonesDirectionToPalm) {
		this.bonesDirection = bonesDirectionToPalm;
	}
	
	
}

