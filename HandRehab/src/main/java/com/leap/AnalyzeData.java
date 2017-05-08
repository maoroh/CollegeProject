package com.leap;

import java.util.ArrayList;
import java.util.Collections;

public class AnalyzeData
{
	private static final int K = 10;
	private static final int numOfFrames = 20;

	
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
	
	public static MovementPattern buildMovementPattern(SampleSet samples) throws Exception
	{
		MovementPattern mPattern = new MovementPattern();
		ArrayList<AnglesVector> anglesVectorOfFrame = new ArrayList<AnglesVector>();
		
		for(int i=0; i<numOfFrames; i++)
		{
			//Clear the list
			anglesVectorOfFrame.clear();
			
			//Loop all samples
			for(int j=0; j<samples.getSize(); j++)
			{
				SampleData sampleData = samples.getSample(j);
				//sampleData.getFrame(i).setAnglesVector();
				anglesVectorOfFrame.add(sampleData.getFrame(i).getAnglesVector());
			}
			
			AnglesVector testingPoint = anglesVectorOfFrame.get(anglesVectorOfFrame.size()/2);
			
			//Add the mean frame after KNNRegression to the pattern
			mPattern.addVector(KNNRegression(testingPoint, anglesVectorOfFrame));
		}
		
		return mPattern;
	} 
	
	public static SampleData avgSample (SampleData sample) throws Exception
	{
		
		ArrayList<FrameData> avgFramesData = new ArrayList<FrameData>();
		int index = 0;
		
		if(sample.getNumOfFrames() < numOfFrames)
			throw new Exception("illegal number of frames for sample");
		
		else if(sample.getNumOfFrames() == numOfFrames) return sample;
		
		while(true)
		{
			
			if(sample.getNumOfFrames() / 2 > numOfFrames)
			{
		
				while(index < sample.getNumOfFrames())
				{
					FrameData avgFrame = FrameData.framesAvg(sample.getFrame(index),sample.getFrame(index + 1));
					avgFramesData.add(avgFrame);
					index += 2;
				}
			
			
			
				//size not divide by 2
				if(sample.getNumOfFrames() % 2 != 0)
				{
					
					index++;
					avgFramesData.add(sample.getFrame(index));
				}
				
				sample = new SampleData(new ArrayList<FrameData>(avgFramesData));
				avgFramesData.clear();
				index = 0;
			}
			

			else if(sample.getNumOfFrames() == numOfFrames) return sample;
			
			else
			{
				int size = sample.getNumOfFrames();
			
				while(size > numOfFrames)
				{
					FrameData avgFrame = FrameData.framesAvg(sample.getFrame(index),sample.getFrame(index + 1));
					avgFramesData.add(avgFrame);
					index += 2;
					size -- ;
				}
			
				for(int i = index + 1; i < sample.getNumOfFrames(); i++)
				{
					avgFramesData.add(sample.getFrame(index));
				}
			
				return new SampleData(new ArrayList<FrameData>(avgFramesData));
			}
			
		
		
		}
	
	}
	
	
	
	
	
	
}
