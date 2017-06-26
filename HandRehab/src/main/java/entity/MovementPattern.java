package entity;

import java.util.ArrayList;
import java.util.Collections;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.leapmotion.leap.Finger.Type;

@XmlRootElement(name = "MovementPattern")
@XmlAccessorType (XmlAccessType.FIELD)
public class MovementPattern {
	
	private ArrayList <DataVector> meanFrames;
	
	public MovementPattern()
	{
		this.meanFrames = new ArrayList<DataVector>();
	}
	
	public MovementPattern(ArrayList<DataVector> meanFrames)
	{
		this.meanFrames = meanFrames;
	}
	
	public DataVector getVector(int index)
	{
		return this.meanFrames.get(index);
	}
	
	public void addVector(DataVector frame)
	{
		this.meanFrames.add(frame);
	}
	
	public int getSize()
	{
		return this.meanFrames.size();
	}
	
}
