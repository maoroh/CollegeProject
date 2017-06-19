package com.leap;

import java.util.ArrayList;
import java.util.InputMismatchException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Finger.Type;
import com.leapmotion.leap.Vector;

@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class DataVector implements Comparable<DataVector> {
	private Finger.Type name;
	private ArrayList<Double> coordinates;
	private double distanceToTestingPoint;
	
	public DataVector()
	{
		coordinates = new ArrayList<Double>();
	}
	
	public DataVector(int size)
	{
		coordinates = new ArrayList<Double>();
		for(int i=0 ; i<size; i++)
		{
			this.addCoordinate(0.0);
		}
	}
	
	public void addCoordinate(double coordinate)
	{
		this.coordinates.add(coordinate);
	}
	
	public double distanceTo(DataVector vec) throws Exception
	{
		if(this.getSize() != vec.getSize()) 
			throw new InputMismatchException("Vectors are not in the same size!");
		
		double sum = 0;
		
		for(int i = 0 ; i<this.getSize(); i++)
			sum += Math.pow(vec.getCoordinate(i) - this.getCoordinate(i),2);
		
		return Math.sqrt(sum);
	}
	
	public double getCoordinate(int index)
	{
		return this.coordinates.get(index);
	}
	
	public double setCoordinate(int index, double coordinate)
	{
		return this.coordinates.set(index, coordinate);
	}
	
	public int getSize()
	{
		return this.coordinates.size();
	}

	@Override
	public int compareTo(DataVector o) {
		// TODO Auto-generated method stub
		return (int)(this.getDistanceToTestingPoint() -o.getDistanceToTestingPoint());
	}

	public double getDistanceToTestingPoint() {
		return distanceToTestingPoint;
	}

	public void setDistanceToTestingPoint(double distanceToTestingPoint) {
		this.distanceToTestingPoint = distanceToTestingPoint;
	}
	
	public void plus(DataVector a)
	{
		if(this.getSize() != a.getSize()) throw new InputMismatchException("Vectors are not in the same size");
		
		for(int i=0; i<this.getSize(); i++)
			this.setCoordinate(i, this.getCoordinate(i) + a.getCoordinate(i));	

	}
	
	public void multiplyVec(double mul)
	{
		for(int i=0; i<this.getSize(); i++)
			this.setCoordinate(i, this.getCoordinate(i) * mul);	
	}
	
	@Override
	public String toString()
	{
		String res = "";
		res = res + "(";
		for(int i=0; i<this.getSize(); i++)
		{
			if(i<this.getSize()-1)
			res = res + this.getCoordinate(i) + ',';
			else res = res + this.getCoordinate(i);
		}
		
		res = res + ")";
		return res;
	}
	
	public void toDegrees()
	{
		for(int i = 0 ; i < this.coordinates.size(); i++)
		{
			this.setCoordinate(i, this.getCoordinate(i) * (180/Math.PI));
		}
	}
	
	public DataVector minus(DataVector vec )
	{
		DataVector res = new DataVector();
		
		for(int i = 0 ; i< vec.getSize() ; i++)
		{
			res.addCoordinate(this.getCoordinate(i) - vec.getCoordinate(i));
		}
		
		return res;
	}
	
	public static DataVector convertToDataVector(Vector vec)
	{
		DataVector dataVec = new DataVector();
		dataVec.addCoordinate(vec.getX());
		dataVec.addCoordinate(vec.getY());
		dataVec.addCoordinate(vec.getZ());
		
		return dataVec;
	}

	public Finger.Type getName() {
		return name;
	}

	public void setName(Type name) {
		this.name = name;
	}
	
	public Vector toVector()
	{
		return new Vector((float)this.getCoordinate(0) , (float)this.getCoordinate(1), (float)this.getCoordinate(2));
	}
	
	public void clearVector()
	{
		this.coordinates.clear();
	}
	
	
}
