package tools;

import java.util.ArrayList;

import entity.DataVector;
import entity.MovementPattern;

public class FeedbackGenerator {
	
	
	
	public static void generateFeedback(MovementPattern rehabMP, MovementPattern trainMP)
	{
		
		
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
	
	



}
