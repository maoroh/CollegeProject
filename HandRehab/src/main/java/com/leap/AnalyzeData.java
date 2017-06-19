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
		
		for(int i=0; i < K; i++)
			mean.plus(points.get(i));
		mean.plus(testingPoint);
		
		double mul = 1/(double)(K+1);
		
		mean.multiplyVec(mul);
		return mean;
	}
	
	
	/*/
	public static MovementPattern buildMovementPattern2(SampleSet recordedSamples) throws Exception
	{
		SampleSet samples = fixSampleSet(recordedSamples);
		int size = recordedSamples.getSize();
		ArrayList<Double> counters = new ArrayList<>();
		counters.add(0.0);
		counters.add(0.0);
		counters.add(0.0);
		counters.add(0.0);
		counters.add(0.0);
		counters.add(0.0);
		counters.add(0.0);
		counters.add(0.0);
		counters.add(0.0);
		counters.add(0.0);
	
		int K = 3;
		counters.set(0, Double.MAX_VALUE);

		for(int i = 1; i < size ; i++)
		{
			for (int j = 0 ; j < numOfFrames; j++)
			{
				 DataVector dTesting = samples.getSample(0).getFrame(j).getAnglesVector2();
				 DataVector dCurrent = samples.getSample(i).getFrame(j).getAnglesVector2();
				 double distance = dTesting.distanceTo(dCurrent);
				 counters.set(i, counters.get(i) + distance);
			}
		}
		
		ArrayList<Double> temp = new ArrayList<Double>(counters);
		Collections.sort(temp);
		
		SampleData newSample = new SampleData();
	
		int minIndex1 = counters.indexOf(temp.get(0));
		int minIndex2 = counters.indexOf(temp.get(1));
		int minIndex3 = counters.indexOf(temp.get(2));
			
		SampleData s1 = samples.getSample(minIndex1);
		SampleData s2 = samples.getSample(minIndex2);
		SampleData s3 = samples.getSample(minIndex3);
			
		SampleData sTesting = samples.getSample(0);
		
		for(int j = 0 ; j < numOfFrames; j++)
		{
			FrameData fAvg = FrameData.framesAvg(s1.getFrame(j), sTesting.getFrame(j));
			fAvg = FrameData.framesAvg(fAvg, s2.getFrame(j));
			fAvg = FrameData.framesAvg(fAvg, s3.getFrame(j));
			newSample.addFrame(fAvg);
				
		}
		
		return new MovementPattern (newSample.getSamplesVector2());
	}/*/
	
	
	
	public static MovementPattern buildMovementPattern(SampleSet recordedSamples) throws Exception
	{
		SampleSet samples = fixSampleSet(recordedSamples);
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
			mPattern.addVector(KNNRegression(testingPoint, anglesVectorOfFrame,5));
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
					if(index != sample.getNumOfFrames() - 1) 
						avgFrame = FrameData.framesAvg(sample.getFrame(index),sample.getFrame(index + 1));
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
	
	
	public static void rehabActions(SampleSet rehabSamples) throws Exception
	{
		SampleSet trainingSet = JAXBTools.getTrainingFromXML();
		MovementPattern rehabMP,trainMP;
		fixSampleSetNoise(rehabSamples);
		fixSampleSetNoise(trainingSet);
		int rehabMinFrameSize = findNumOfFrames (rehabSamples);
		int trainingMinFrameSize = findNumOfFrames(trainingSet);
		JAXBTools.saveSampleSetXML(rehabSamples , "rehabData.xml");
		numOfFrames = rehabMinFrameSize < trainingMinFrameSize ? rehabMinFrameSize : trainingMinFrameSize;
		SampleSet samplesRehab = fixSampleSet(rehabSamples);
		SampleSet samplesTraining = fixSampleSet(trainingSet);
		JAXBTools.saveSampleSetXML(samplesRehab , "rehabDataFixed.xml");
		rehabMP = buildMovementPattern(rehabSamples);
		trainMP = buildMovementPattern(samplesTraining);
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
			//fixSampleNoise(sampleSet.getSample(i));
			fixSampleDataNoiseUp(sampleSet.getSample(i));
			
		}
	}
	
	public static void fixSampleNoise(SampleData sample)
	{
		int i = 0;
		while( i < sample.getNumOfFrames() - 1 )
		{
			FrameData frame = sample.getFrame(i);
			FrameData frameNext = sample.getFrame(i + 1);
			DataVector anglesVecFrame = frame.getAnglesVector2();
			DataVector anglesVecFrameNext = frameNext.getAnglesVector2();
			if(Math.abs(anglesVecFrameNext.getCoordinate(0) - anglesVecFrame.getCoordinate(0)) >= 0.5
					|| Math.abs(anglesVecFrameNext.getCoordinate(1) - anglesVecFrame.getCoordinate(1)) >= 0.5
					|| Math.abs(anglesVecFrameNext.getCoordinate(2) - anglesVecFrame.getCoordinate(2)) >= 0.5
					|| Math.abs(anglesVecFrameNext.getCoordinate(3) - anglesVecFrame.getCoordinate(3)) >= 0.5
					|| Math.abs(anglesVecFrameNext.getCoordinate(4) - anglesVecFrame.getCoordinate(4)) >= 0.5)
			{
				sample.deleteFrame(i + 1);
				
			}
			i++;
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
	
	
	
	
	
	
	
	
	
}
