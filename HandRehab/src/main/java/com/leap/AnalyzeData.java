package com.leap;

import java.util.ArrayList;
import java.util.Collections;

public class AnalyzeData
{
	private static final int K = 2;
	private static final int numOfFrames = 10;
	
	public static AnglesVector KNNRegression(AnglesVector testingPoint , ArrayList<AnglesVector> points) throws Exception
	{
		for(AnglesVector point : points)
			point.setDistanceToTestingPoint(testingPoint.distanceTo(point));
		
		//Sort the points according to the distances
		Collections.sort(points);
		
		AnglesVector mean = new AnglesVector(testingPoint.getSize());
		
		for(int i=0; i<K; i++)
			mean.plus(points.get(i));
		mean.plus(testingPoint);
		
		double mul = 1/(double)(K+1);
		
		mean.multiplyVec(mul);
		
		return mean;
	}
	
	public static void buildMovementPattern(SampleSet samples) throws Exception
	{
		MovementPattern mPattern = new MovementPattern();
		ArrayList<AnglesVector> anglesVectorOfFrame = new ArrayList<AnglesVector>();
		
		for(int i=0; i<numOfFrames; i++)
		{
			//Clear the list
			anglesVectorOfFrame.clear();
			
			for(int j=0; j<samples.getSize(); j++)
			{
				SampleData sampleData = samples.getSample(j);
				anglesVectorOfFrame.add(sampleData.getFrame(i).getAnglesVector());
			}
			
			AnglesVector testingPoint = anglesVectorOfFrame.get(anglesVectorOfFrame.size()/2);
			
			//Add the mean frame after KNNRegression to the pattern
			mPattern.addVector(KNNRegression(testingPoint, anglesVectorOfFrame));
		}
	}
	
	
	
	
	
	
}
