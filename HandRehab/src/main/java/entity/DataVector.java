package entity;

import java.util.ArrayList;
import java.util.InputMismatchException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Finger.Type;
import com.leapmotion.leap.Vector;

/**
 * This class is represent a vector of n-coordinates.
 * Implements the Comparable interface.
 * @author maor
 *
 */
@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class DataVector implements Comparable<DataVector> {
	private Finger.Type name;
	private ArrayList<Double> coordinates;
	private double distanceToTestingPoint;
	
	/**
	 * Initailize new empty DataVector.
	 */
	public DataVector()
	{
		coordinates = new ArrayList<Double>();
	}
	
	/**
	 * Initialize new DataVector with n-coordinates.
	 * @param n - Number of coordinates.
	 */
	public DataVector(int n)
	{
		coordinates = new ArrayList<Double>();
		for(int i=0 ; i<n; i++)
		{
			this.addCoordinate(0.0);
		}
	}
	
	/**
	 * Add new coordinate to DataVector.
	 * @param coordinate - the coordinate value.
	 */
	public void addCoordinate(double coordinate)
	{
		this.coordinates.add(coordinate);
	}
	
	/**
	 * Calculate the euclidean distance to other DataVector.
	 * @param vec - the other DataVector object.
	 * @return the vector distance.
	 * @throws Exception if the vectors are not in the same size.
	 */
	public double distanceTo(DataVector vec) throws Exception
	{
		if(this.getSize() != vec.getSize()) 
			throw new InputMismatchException("Vectors are not in the same size!");
		
		double sum = 0;
		
		for(int i = 0 ; i<this.getSize(); i++)
			sum += Math.pow(vec.getCoordinate(i) - this.getCoordinate(i),2);
		
		return Math.sqrt(sum);
	}
	
	/**
	 * Get coordinate.
	 * @param index - the index of the coordinate. 
	 * @return
	 */
	public double getCoordinate(int index)
	{
		return this.coordinates.get(index);
	}
	
	/**
	 * Set coordinate.
	 * @param index - the index of the coordinate.
	 * @param coordinate - the value of the coordinate.
	 * @return
	 */
	public double setCoordinate(int index, double coordinate)
	{
		return this.coordinates.set(index, coordinate);
	}
	
	/**
	 * Get the size of the DataVector.
	 * @return size of DataVector.
	 */
	public int getSize()
	{
		return this.coordinates.size();
	}

	/**
	 * Implement the comparable interface method.
	 * compare between this object and other object-o according to their distance.
	 */
	@Override
	public int compareTo(DataVector o) {
		// TODO Auto-generated method stub
		if (this.getDistanceToTestingPoint() > o.getDistanceToTestingPoint()) return 1;
		else if (this.getDistanceToTestingPoint() < o.getDistanceToTestingPoint()) return -1;
		else return 0;
	}

	/**
	 * Get distance.
	 * @return distance.
	 */
	public double getDistanceToTestingPoint() {
		return distanceToTestingPoint;
	}

	/**
	 * Set distance.
	 * @param distanceToTestingPoint - the value of the distance.
	 */
	public void setDistanceToTestingPoint(double distanceToTestingPoint) {
		this.distanceToTestingPoint = distanceToTestingPoint;
	}
	
	/**
	 * Add a vector to this vector.
	 * @param a - the vector to add.
	 */
	public void plus(DataVector a)
	{
		if(this.getSize() != a.getSize()) throw new InputMismatchException("Vectors are not in the same size");
		
		for(int i=0; i< this.getSize(); i++)
			this.setCoordinate(i, this.getCoordinate(i) + a.getCoordinate(i));	

	}
	
	/**
	 * Multiply this vector in a factor of mul.
	 * @param mul - the multiplication factor.
	 */
	public void multiplyVec(double mul)
	{
		for(int i=0; i<this.getSize(); i++)
			this.setCoordinate(i, this.getCoordinate(i) * mul);	
	}
	
	/**
	 * Convert the DataVector values from radians to degrees.
	 */
	public void toDegrees()
	{
		for(int i = 0 ; i < this.coordinates.size(); i++)
		{
			this.setCoordinate(i, this.getCoordinate(i) * (180/Math.PI));
		}
	}
	
	/**
	 * Substruct a vector from this vector.
	 * @param vec - the other vector to substruct.
	 * @return the DataVector after the substruction.
	 */
	public DataVector minus(DataVector vec )
	{
		DataVector res = new DataVector();
		
		for(int i = 0 ; i< vec.getSize() ; i++)
		{
			res.addCoordinate(this.getCoordinate(i) - vec.getCoordinate(i));
		}
		
		return res;
	}
	
	/**
	 * Convert Leap Motion Vector to DataVector.
	 * @param vec - the Vector object.
	 * @return DataVector object.
	 */
	public static DataVector convertToDataVector(Vector vec)
	{
		DataVector dataVec = new DataVector();
		dataVec.addCoordinate(vec.getX());
		dataVec.addCoordinate(vec.getY());
		dataVec.addCoordinate(vec.getZ());
		
		return dataVec;
	}

	/**
	 * Name getter.
	 * @return the name of vector.
	 */
	public Finger.Type getName() {
		return name;
	}

	/**
	 * Name setter.
	 * @param name - the name to set.
	 */
	public void setName(Type name) {
		this.name = name;
	}
	
	/**
	 * Convert from DataVector to Leap Motion Vector.
	 * @return
	 */
	public Vector toVector()
	{
		return new Vector((float)this.getCoordinate(0) , (float)this.getCoordinate(1), (float)this.getCoordinate(2));
	}
	
	/**
	 * Clear the vector values.
	 */
	public void clearVector()
	{
		this.coordinates.clear();
	}
	
	
}
