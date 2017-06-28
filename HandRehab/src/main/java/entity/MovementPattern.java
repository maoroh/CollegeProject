package entity;

import java.util.ArrayList;
import java.util.Collections;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.leapmotion.leap.Finger.Type;

/**
 * Movement Pattern.
 * Stores all the mean frames of the movement after KNN Regression process.
 * @author maor
 *
 */
@XmlRootElement(name = "MovementPattern")
@XmlAccessorType (XmlAccessType.FIELD)
public class MovementPattern {
	
	private ArrayList <DataVector> meanFrames;
	
	/**
	 * Initialize new empty MovementPattern object.
	 */
	public MovementPattern()
	{
		this.meanFrames = new ArrayList<DataVector>();
	}
	
	/**
	 * Initiliaze new MovementPattern object.
	 * @param meanFrames - the meanFrames list.
	 */
	public MovementPattern(ArrayList<DataVector> meanFrames)
	{
		this.meanFrames = meanFrames;
	}
	
	/**
	 * Get a DataVector in a specific position.
	 * @param index - the position of the vector.
	 * @return DataVector in index position.
	 */
	public DataVector getVector(int index)
	{
		return this.meanFrames.get(index);
	}
	
	
	/**
	 * Add new vector to the MovementPattern.
	 * @param frame - the DataVector to add.
	 */
	public void addVector(DataVector frame)
	{
		this.meanFrames.add(frame);
	}
	
	/**
	 * Get size of meanFrames list.
	 * @return
	 */
	public int getSize()
	{
		return this.meanFrames.size();
	}
	
}
