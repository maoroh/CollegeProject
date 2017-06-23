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
	
	
	public static ArrayList <double [] > getScore(MovementPattern trainMP, MovementPattern rehabMP)
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
