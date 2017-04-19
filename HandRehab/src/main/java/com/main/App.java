package com.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.leap.AnglesVector;
import com.leap.AnalyzeData;
import com.leap.SampleBuilder;
import com.leapmotion.leap.Vector;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
      AnglesVector mean=null;
      AnglesVector a = new AnglesVector();
      a.addCoordinate(1);
      a.addCoordinate(2);
      a.addCoordinate(3);
      
      AnglesVector b = new AnglesVector();
      b.addCoordinate(2);
      b.addCoordinate(0);
      b.addCoordinate(2);
      
      AnglesVector c = new AnglesVector();
      c.addCoordinate(1);
      c.addCoordinate(1);
      c.addCoordinate(2);
      
      AnglesVector test = new AnglesVector();
      test.addCoordinate(3);
      test.addCoordinate(5);
      test.addCoordinate(2);
      
      ArrayList<AnglesVector> points = new ArrayList<>();
      points.add(a);
      points.add(b);
      points.add(c);
      
      
      
      try {
    	  //	mean = KNNAlgorithm.findKNearestNeighbors(test, points);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      
        System.out.println(mean);
    }
}
