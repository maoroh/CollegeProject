package com.leap;

import java.util.ArrayList;
import java.util.Collections;

import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Vector;
import com.tools.JAXBTools;

public class AnalyzeData
{
	private static int numOfFrames  ;

	
	public static DataVector KNNRegression(DataVector testingPoint , ArrayList<DataVector> points, int K) throws Exception
	{
		for(DataVector point : points)
			point.setDistanceToTestingPoint(testingPoint.distanceTo(point));
		
		//Sort the points according to the distances
		Collections.sort(points);
		
		DataVector mean = new DataVector(testingPoint.getSize());
		
		for(int i= points.size() - 1 ; i > points.size() - 1 - K ; i--)
			mean.plus(points.get(i));
		mean.plus(testingPoint);
		
		double mul = 1/(double)(K+1);
		
		mean.multiplyVec(mul);
		return mean;
	}
	
	
	
	
	
	public static MovementPattern buildMovementPattern(SampleSet samples) throws Exception
	{
		MovementPattern mPattern = new MovementPattern();
		ArrayList<DataVector> anglesVectorOfFrame = new ArrayList<DataVector>();
		
		for(int i=0; i<numOfFrames; i++)
		{
			//Clear the list
			anglesVectorOfFrame.clear();
			
			//Loop all samples
			for(int j = 0; j < samples.getSize(); j++)
			{
				SampleData sampleData = samples.getSample(j);
				
				anglesVectorOfFrame.add(sampleData.getFrame(i).getAnglesVector2());
			}
			
			DataVector testingPoint = anglesVectorOfFrame.get(samples.getSize()/2);
			
			//Add the mean frame after KNNRegression to the pattern
			mPattern.addVector(KNNRegression(testingPoint, anglesVectorOfFrame,4));
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
			
			if(sample.getNumOfFrames() / 2 >= numOfFrames)
			{
		
				while(index < sample.getNumOfFrames())
				{
					FrameData avgFrame;
					if(index != sample.getNumOfFrames() - 1 && index != 0) 
					{
						avgFrame = FrameData.framesAvg(sample.getFrame(index - 1),sample.getFrame(index));
						avgFrame = FrameData.framesAvg(avgFrame,sample.getFrame(index + 1));
					}
					else avgFrame = sample.getFrame(index);
					avgFramesData.add(avgFrame);
					index += 2;
					
				}

				sample = new SampleData(new ArrayList<FrameData>(avgFramesData));
				avgFramesData.clear();
				index = 0;
			}
		
			else if(sample.getNumOfFrames() == numOfFrames) return sample;
			
			else
			{
				avgFramesData.clear();
				index = 0;
				int size = sample.getNumOfFrames();
			
				while(size > numOfFrames)
				{
					FrameData avgFrame = FrameData.framesAvg(sample.getFrame(index),sample.getFrame(index + 1));
					avgFramesData.add(avgFrame);
					index += 2;
					size -- ;
				}
			
				for(int i = index ; i < sample.getNumOfFrames(); i++)
				{
					avgFramesData.add(sample.getFrame(i));
				}
			
				return new SampleData(new ArrayList<FrameData>(avgFramesData));
			}
		}
	
	}
	
	
	
	public static SampleSet fixSampleSet(SampleSet samplesSet) throws Exception
	{
	   SampleSet fixedSampleSet  = new SampleSet();
	   
	   for(int i = 0; i<samplesSet.getSize(); i++)
	   {
		   fixedSampleSet.addSample(avgSample(samplesSet.getSample(i)));
	   }
	   
	   return fixedSampleSet;
	}
	
	
	public static void rehabActions(SampleSet rehabSet) throws Exception
	{
		SampleSet trainingSet = JAXBTools.getTrainingFromXML();
		MovementPattern rehabMP,trainMP;
		smoothSampleSet(rehabSet);
		smoothSampleSet(trainingSet);
		int rehabMinFrameSize = findNumOfFrames (rehabSet);
		int trainingMinFrameSize = findNumOfFrames(trainingSet);
		JAXBTools.saveSampleSetXML(rehabSet , "rehabData.xml");
		numOfFrames = rehabMinFrameSize < trainingMinFrameSize ? rehabMinFrameSize : trainingMinFrameSize;
		SampleSet rehabFixed = fixSampleSet(rehabSet);
		SampleSet trainingFixed = fixSampleSet(trainingSet);
		JAXBTools.saveSampleSetXML(rehabFixed , "rehabDataFixed.xml");
		
		rehabMP = buildMovementPattern(rehabFixed);
		trainMP = buildMovementPattern(trainingFixed);
		JAXBTools.savePatternXML(rehabMP,"rehabMP.xml");
		JAXBTools.savePatternXML(trainMP,"trainMP.xml");
		Feedback.generateFeedback(rehabMP, trainMP);
		System.out.println("Success Rehab");
	}
	


	public static int findNumOfFrames(SampleSet samples)
	{
		
		int minFrames = samples.getSample(0).getNumOfFrames();
		
		for(int i = 1; i<samples.getSize(); i++)
		{
			
			int currentNumOfFrames = samples.getSample(i).getNumOfFrames();
			if(currentNumOfFrames < minFrames)
				minFrames = currentNumOfFrames;
		}
		
		return minFrames;
		
	}

	public static void trainingActions(SampleSet sampleSet) throws Exception {
		// TODO Auto-generated method stub
	
		JAXBTools.saveSampleSetXML(sampleSet , "trainData.xml");
	}
	
	
	public static void fixSampleSetNoise(SampleSet sampleSet)
	{
		for(int i = 0; i < sampleSet.getSize(); i++)
		{
			//fixSampleDataNoiseUp(sampleSet.getSample(i));
			
		}
	}
	

	
	public static void fixSampleDataNoiseUp(SampleData sampleData)
	{
		int index = 0;
		int threshold = 9;
		int counter = 0;
		int i = 0;
		while (i < sampleData.getNumOfFrames() - 1)
		{
			if(i + threshold > sampleData.getNumOfFrames())
				break;
			
			for(int j = i ; j < i + threshold ; j++)
			{
				FrameData frame = sampleData.getFrame(j);
				FrameData frameNext = sampleData.getFrame(j + 1);
				DataVector anglesVecFrame = frame.getAnglesVector2();
				DataVector anglesVecFrameNext = frameNext.getAnglesVector2();
				
				if(anglesVecFrameNext.getCoordinate(2) < anglesVecFrame.getCoordinate(2))
				{
					counter = 0;
					index = j;
					i = j + 1;
					break;
				}
				counter ++;
			
			 }
			
			if(counter >= threshold)
				break;
		}
		
		for(int k = 0 ; k < index; k++)
		{
			sampleData.deleteFrame(0);
		}
		
	}
	
	public static void smoothSampleSet(SampleSet sampleSet)
	{
		for(int i = 0 ; i < sampleSet.getSize(); i++)
		{
			smoothSample(sampleSet.getSample(i));
		}
	}
	
	
	public static void smoothSample(SampleData sample)
	{
		double [] weights = {0.015625,0.09375,0.234375,0.3125,0.234375,0.09375,0.015625};
		
		FrameData currentFrame;
		
			for (int j = 3; j < sample.getNumOfFrames() - 3 ; j++)
			{
				currentFrame = sample.getFrame(j);
				DataVector mean = new DataVector();
				
				for(int i = 0; i < 5; i++)
				{
					double frame0 = sample.getFrame(j-3).getAnglesVector2().getCoordinate(i);
					double frame1 = sample.getFrame(j-2).getAnglesVector2().getCoordinate(i);
					double frame2 = sample.getFrame(j-1).getAnglesVector2().getCoordinate(i);
					double frame3 = sample.getFrame(j).getAnglesVector2().getCoordinate(i);
					double frame4 = sample.getFrame(j+1).getAnglesVector2().getCoordinate(i);
					double frame5 = sample.getFrame(j+2).getAnglesVector2().getCoordinate(i);
					double frame6 = sample.getFrame(j+3).getAnglesVector2().getCoordinate(i);
					
					frame0 = frame0 * weights[0];
					frame1 = frame1 * weights[1];
					frame2 = frame2 * weights[2];
					frame3 = frame3 * weights[3];
					frame4 = frame4 * weights[4];
					frame5 = frame5 * weights[5];
					frame6 = frame6 * weights[6];
					
					double newAngle = frame0 + frame1 + frame2 + frame3 + frame4 + frame5 + frame6;
					mean.addCoordinate(newAngle);
				}
				
				
				currentFrame.replaceAngles(mean);
				
			}	
	}
	
	
	
	
	
	
	
	
	
}
