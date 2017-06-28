package entity;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Finger.Type;

/**
 * InitialData
 * Stores each finger tip direction in a DataVector object.
 * @author maor
 *
 */
@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class InitialData {
	
	private ArrayList <DataVector> fingersTip;
	
	public InitialData()
	{
		
	}
	
	/**
	 * Initialize new InitialData object.
	 * @param fingersTip - the fingers Tips list.
	 */
	public InitialData(ArrayList <DataVector> fingersTip)
	{
		this.setFingersTip(fingersTip);
	}

	/**
	 * fingersTip getter.
	 * @return the fingersTip list.
	 */
	public ArrayList <DataVector> getFingersTip() {
		return fingersTip;
	}

	/**
	 * fingersTip setter.
	 * @param fingersTip - the list to set.
	 */
	public void setFingersTip(ArrayList <DataVector> fingersTip) {
		this.fingersTip = fingersTip;
	}
	
	/**
	 * Get tip direction vector of a specific finger.
	 * @param finger - the type of finger.
	 * @return the tip direction vector of finger.
	 */
	public DataVector getTipVector(Type finger) 
	{
		for(DataVector dataVec : this.fingersTip)
		{
			if(dataVec.getName() == finger)
				return dataVec;
		}
		
		return null;
	}

}
