package com.leap;

import java.util.ArrayList;
import java.util.InputMismatchException;

public class AnglesVector implements Comparable<AnglesVector> {

	private ArrayList<Double> coordinates;
	private double distanceToTestingPoint;
	
	public AnglesVector()
	{
		coordinates = new ArrayList<Double>();
	}
	
	public AnglesVector(int size)
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
	
	public double distanceTo(AnglesVector vec) throws Exception
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
	public int compareTo(AnglesVector o) {
		// TODO Auto-generated method stub
		return (int)(this.getDistanceToTestingPoint() -o.getDistanceToTestingPoint());
	}

	public double getDistanceToTestingPoint() {
		return distanceToTestingPoint;
	}

	public void setDistanceToTestingPoint(double distanceToTestingPoint) {
		this.distanceToTestingPoint = distanceToTestingPoint;
	}
	
	public void plus(AnglesVector a)
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
		for(int i=0; i<this.getSize(); i++)
		{
			if(i<this.getSize()-1)
			res = res + this.getCoordinate(i) + ',';
			else res = res + this.getCoordinate(i);
		}
		return res;
	}
	
	
}
