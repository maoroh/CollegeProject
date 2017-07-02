package utility;

import java.util.ArrayList;
import java.util.Collections;

import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Vector;

import entity.DataVector;
import entity.Feedback;
import entity.FrameData;
import entity.InitialData;
import entity.MovementPattern;
import entity.SampleData;
import entity.SampleSet;

/**
 * Analyze utility class.
 * Handle all the analyze functions.
 * @author maor
 *
 */
public class AnalyzeData
{
	private static int numOfFrames  ;

	/**
	 * Apply KNN Regression algorithm on the points array according to the testing point.
	 * @param testingPoint - the testing point.
	 * @param points - the dataset.
	 * @param K - the number of neighbors.
	 * @return the mean frame according to the K neighbors.
	 * @throws Exception if the vectors are not in the same size.
	 */
	public static DataVector KNNRegression(DataVector testingPoint , ArrayList<DataVector> points, int K) throws Exception
	{
		for(DataVector point : points)
			point.setDistanceToTestingPoint(testingPoint.distanceTo(point));
		
		//Sort the points according to the distances
		Collections.sort(points);
		
		DataVector mean = new DataVector(testingPoint.getSize());
		
		for(int i= 0 ; i < K ; i++)
			mean.plus(points.get(i));
		
		double mul = 1/(double)K;
		
		mean.multiplyVec(mul);
		return mean;
	}
	
	
	
	
	/**
	 * Creates an array of frames consists of each frame timestamp from each sample.
	   After we have the array, we using KNN Regression for finding the “accurate” mean of each frame according all the samples.
	 * @param samples - The samples set.
	 * @return MovementPattern that represents the patient movement.
	 * @throws Exception 
	 */
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
				
				anglesVectorOfFrame.add(sampleData.getFrame(i).getAnglesVectorTips());
			}
			
			DataVector testingPoint = anglesVectorOfFrame.get(samples.getSize()/2);
			
			//Add the mean frame after KNNRegression to the pattern
			mPattern.addVector(KNNRegression(testingPoint, anglesVectorOfFrame , 5));
		}
		
		return mPattern;
	} 
	
	/**
	 * Resize a sample according numOfFrames value using an average of closing frames.
	 * @param sample - The sample.
	 * @return resized sample with numOfFrames size.
	 * @throws Exception if sample size is smaller then numOfFrames.
	 */
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
	
	
	/**
	 * Resize all the samples in a set.
	 * @param samplesSet - the samples set needs to be resized.
	 * @return a resized samples set that each sample size is numOfFrames. 
	 * @throws Exception
	 */
	public static SampleSet fixSampleSet(SampleSet samplesSet) throws Exception
	{
	   SampleSet fixedSampleSet  = new SampleSet();
	   
	   for(int i = 0; i<samplesSet.getSize(); i++)
	   {
		   fixedSampleSet.addSample(avgSample(samplesSet.getSample(i)));
	   }
	   
	   return fixedSampleSet;
	}
	
	/**
	 * Loading the training set from XML and creating the angles vector for the Rehab Set and Training Set.
	 * Smoothing the data sets using smoothSampleSet method.
	 * Finding the minimum sample size.
	 * Fix the samples size according to this minimum.
	 * Generate Movement patterns.
	 * Save the movement patterns in XML Files.
	 * @param rehabSet - the record Rehab data set.
	 * @throws Exception
	 */
	public static void rehabActions(SampleSet rehabSet) throws Exception
	{
		SampleSet trainingSet = JAXBTools.getTrainingFromXML();
		MovementPattern rehabMP,trainMP;
		calcAngles(rehabSet);
		calcAngles(trainingSet);
		//smoothSampleSet(rehabSet);
		//smoothSampleSet(trainingSet);
		int rehabMinFrameSize = findMinSampleFrames (rehabSet);
		int trainingMinFrameSize = findMinSampleFrames(trainingSet);
		numOfFrames = rehabMinFrameSize < trainingMinFrameSize ? rehabMinFrameSize : trainingMinFrameSize;
		SampleSet rehabFixed = fixSampleSet(rehabSet);
		SampleSet trainingFixed = fixSampleSet(trainingSet);
		rehabMP = buildMovementPattern(rehabFixed);
		trainMP = buildMovementPattern(trainingFixed);
		JAXBTools.savePatternXML(rehabMP,"rehabMP.xml");
		JAXBTools.savePatternXML(trainMP,"trainMP.xml");
	}
	

	/**
	 * Finding the minimum sample size.
	 * @param samples - The sample set.
	 * @return the minimum sample size in this set.
	 */
	public static int findMinSampleFrames(SampleSet samples)
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
	
	/**
	 * Calculate the angles vector for each sample.
	 * The angles vector in each frame is build according the first frame in the sample.
	 * Each angle is between tip direction in the current frame and the tip direction in the initial frame.
	 * @param samples - The sample set.
	 */
	public static void calcAngles(SampleSet samples)
	{
		
		for(int i = 0 ; i < samples.getSize(); i ++)
		{
			SampleData sample = samples.getSample(i);
			InitialData initialData = sample.getFrame(0).getTipsAngles();
			for(int j = 0 ; j < sample.getNumOfFrames(); j++)
				sample.getFrame(j).setAnglesVectorTips(initialData);
		}
	}

	/**
	 * Saving the training data set after the training phase is finished.
	 * @param trainingSet - The sample set.
	 * @throws Exception
	 */
	public static void trainingActions(SampleSet trainingSet) throws Exception {
		// TODO Auto-generated method stub
	
		JAXBTools.saveSampleSetXML(trainingSet , "trainData.xml");
	}
	
	
	/**
	 * Smoothing sample set using smoothSample method.
	 * @param sampleSet - the sample set.
	 */
	public static void smoothSampleSet(SampleSet sampleSet)
	{
		for(int i = 0 ; i < sampleSet.getSize(); i++)
		{
			smoothSample(sampleSet.getSample(i));
		}
	}
	
	/**
	 * Apply an average filter on a sample using Binomial distribution.
	 * @param sample - the sample data.
	 */
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
					double frame0 = sample.getFrame(j-3).getAnglesVectorTips().getCoordinate(i);
					double frame1 = sample.getFrame(j-2).getAnglesVectorTips().getCoordinate(i);
					double frame2 = sample.getFrame(j-1).getAnglesVectorTips().getCoordinate(i);
					double frame3 = sample.getFrame(j).getAnglesVectorTips().getCoordinate(i);
					double frame4 = sample.getFrame(j+1).getAnglesVectorTips().getCoordinate(i);
					double frame5 = sample.getFrame(j+2).getAnglesVectorTips().getCoordinate(i);
					double frame6 = sample.getFrame(j+3).getAnglesVectorTips().getCoordinate(i);
					
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
	
	/**
	 * Generating a feedback according to the movement patterns difference.
	 * @param trainMP - The training movement pattern.
	 * @param rehabMP - The rehabilitation movement pattern.
	 * @return a Feedback object that contain the feedback results.
	 */
	public static Feedback generateFeedback(MovementPattern trainMP, MovementPattern rehabMP)
	{
		int numOfFrames = trainMP.getSize();
		int groupSize = numOfFrames / 4; 
		
		double sumTrain ,sumRehab;

		ArrayList <double [] > diffs = new ArrayList<double [] >(); 
		
		for (int fingerID = 0 ; fingerID < 5; fingerID++ )
		{
			double [] diffFinger = new double [4];
			for (int i = 0; i < 4; i++)
			{
				sumTrain = 0;
				sumRehab = 0;
				
				for(int j = 0; j < groupSize; j++)
				{
				
					sumTrain += trainMP.getVector(j + i * groupSize).getCoordinate(fingerID);
					sumRehab += rehabMP.getVector(j + i * groupSize).getCoordinate(fingerID);
				}
		
				double div = sumTrain > sumRehab ? sumTrain : sumRehab;
				double a = Math.abs(sumTrain - sumRehab) / div ;
				diffFinger [i] = 100 - a * 100;
			}
			diffs.add(diffFinger);
		}
		
		return new Feedback(diffs);
	}
	
	
	
	
	
	
	
	
}
