package com.leap;

import java.util.ArrayList;

import com.tools.JAXBTools;

public class Feedback {
	
	
	
	public static void generateFeedback(MovementPattern rehabMP, MovementPattern trainMP)
	{
		int size = rehabMP.getSize();
		
		ArrayList<DataVector> propVecRehab = createPropVector(rehabMP);
		ArrayList<DataVector> propVecTrain = createPropVector(trainMP);
		MovementPattern mpRehab = new MovementPattern(propVecRehab);
		MovementPattern mpTrain = new MovementPattern(propVecTrain);
		JAXBTools.savePatternXML(mpRehab, "propRehab.xml");
		JAXBTools.savePatternXML(mpTrain, "propTraining.xml");	
		ArrayList<DataVector> diffVecArr = createDiffVector(propVecRehab, propVecTrain);
		System.out.println("");
		
	}
	
	public static double[] getScore(int fingerID , MovementPattern trainMP, MovementPattern rehabMP)
	{
		double minAngleTrain = trainMP.getMinAngle(fingerID);
		double maxAngleTrain = trainMP.getMaxAngle(fingerID);
		double minAngleRehab = rehabMP.getMinAngle(fingerID);
		double maxAngleRehab = rehabMP.getMaxAngle(fingerID);
		double minDiff = 100 - (Math.abs(minAngleTrain - minAngleRehab) / minAngleTrain) * 100;
		double maxDiff = 100 - (Math.abs(maxAngleTrain - maxAngleRehab) / maxAngleTrain) * 100;
		double arr [] = new double[2];
		arr[0] = minDiff;
		arr[1] = maxDiff;
		
		return arr;
	}
	
	public static double[] getScore2(int fingerID , MovementPattern trainMP, MovementPattern rehabMP)
	{
		int numOfFrames = trainMP.getSize();
		int groupSize = numOfFrames / 4; 
		
		double sumTrain = 0;
		double sumRehab = 0;
		double distance = 0;
		double [] diffs = new double [4];
		for (int i = 0; i < 4; i++)
		{
			DataVector group1 = new DataVector();
			DataVector group2 = new DataVector();
			for(int j = 0; j < groupSize; j++)
			{
				
				sumTrain += trainMP.getVector(j + i * groupSize).getCoordinate(fingerID);
				sumRehab += rehabMP.getVector(j + i * groupSize).getCoordinate(fingerID);
				group1.addCoordinate(trainMP.getVector(j + i * groupSize).getCoordinate(fingerID));
				group2.addCoordinate(rehabMP.getVector(j + i * groupSize).getCoordinate(fingerID));
			}
			
			try {
				 distance = group1.distanceTo(group2);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			diffs [i] = 100 - (Math.abs(sumTrain - sumRehab) / sumTrain) * 100;
			
		}
		
		return diffs;
	}
	
	
	public static String getSentance (double percent)
	{
		if(percent > 0 && percent <= 25)
		{
			return ""+percent;
		}
		
		else if (percent > 25 && percent <= 50)
		{
			return ""+percent;
		}
		
		else if (percent > 50 && percent <= 75)
		{
			return ""+percent;
		}
		
		else if (percent > 75 && percent <= 100)
		{
			return ""+percent;
		}
		
		return "";
	}
	
	public static ArrayList<DataVector> createPropVector(MovementPattern mp)
	{
		int size = mp.getSize();
		ArrayList<DataVector> propVecArr = new ArrayList<DataVector>();
		
		for(int i = 1; i < size ; i++)
		{
			DataVector vec1 = mp.getVector(0);
			DataVector vec2 = mp.getVector(i);
			DataVector diff = vec2.minus(vec1);
			propVecArr.add(diff);
		}
		
		return propVecArr;
		
	}
	
	public static ArrayList<DataVector> createDiffVector(ArrayList<DataVector> propVecRehab , ArrayList<DataVector> propVecTrain)
	{
		int size = propVecRehab.size();
		ArrayList<DataVector> diffVecArr = new ArrayList<DataVector>();
		for(int i = 0; i < size ; i++)
		{
			DataVector diff = propVecRehab.get(i).minus(propVecTrain.get(i));
			diffVecArr.add(diff);
		}
		
		return diffVecArr;
		
	}


}
