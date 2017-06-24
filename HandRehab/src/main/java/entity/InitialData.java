package entity;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Finger.Type;

@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class InitialData {
	
	private ArrayList <DataVector> fingersTip;
	
	public InitialData()
	{
		
	}
	public InitialData(ArrayList <DataVector> fingersTip)
	{
		this.setFingersTip(fingersTip);
	}

	public ArrayList <DataVector> getFingersTip() {
		return fingersTip;
	}

	public void setFingersTip(ArrayList <DataVector> fingersTip) {
		this.fingersTip = fingersTip;
	}
	
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
