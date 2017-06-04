package com.leap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.GUI.MainController;
import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Pointable;
import com.leapmotion.leap.PointableList;
import com.leapmotion.leap.Vector;
import com.tools.JAXBTools;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class SampleBuilder {
	private LeapListener listener;
 	private Controller controller;
 	private SampleSet sampleSet ;
 	private SampleData sampleData;
	private int numOfFrames = 0;
	private AnglesVector initVec;
	int s = 0;
	private boolean isStopped = false;
	private boolean isStatic = false;
	boolean startMotion = false;
	private IntegerProperty sampleCount = new SimpleIntegerProperty();
	private static final double recThreshold = 0.8;
	private static final int numOfSamples = 5;
	private boolean recording = false;
	private boolean recognize = false;
	private Mode mode ;
	private static final double minSpeed = 35;
	
	public static final int numOfRecognizeFrames = 100;
	
	public SampleBuilder()
	{
	 	controller = new Controller();
		listener = new LeapListener(this);
		controller.addListener(listener);
		sampleSet = new SampleSet();
	}
	
	public void initRecording(Mode mode)
	{
		 isStopped = false;
		 this.mode = mode;
		 numOfFrames = 0;
		 sampleData = new SampleData();
		 recording = true;
		 controller.addListener(listener);
		
	}
	
	public void initRecognize()
	{
		recognize = true;
		isStopped = false;
		numOfFrames = 0;
		sampleData = new SampleData();
		recording = false;
	}
	
	
	public void newFrame(Frame frame)
	{
		if(recording)
		{
			recordNewFrame(frame);
		}
		else if(recognize)
		{
			recordNewFrameRecognize(frame);
		}
	}
	
	private void recordNewFrameRecognize(Frame frame) {
		// TODO Auto-generated method stub
		  
	        if(checkFrame2(frame))
	        {
	        	//Notify thread
	        	synchronized (SampleBuilder.this) {
	    			isStatic = true;
	    			SampleBuilder.this.notify();
	    		}
	        	numOfFrames++;
	        	handleFrame(frame);
	        	
	        	if(numOfFrames == numOfRecognizeFrames)
	        	{
	        		numOfFrames++;
	        		isStopped = true;
	        		System.out.println("stopp");
	        		controller.removeListener(listener);
	        		buildInitial();
	        	}
	        }
	        	
	}

	public void recordNewFrame(Frame frame)
	{
		
        if(checkFrame(frame) )
        {
        	numOfFrames++;
        	FrameData fr = handleFrame(frame);
        	fr.setAnglesVector();
        	AnglesVector frameAngles = fr.getAnglesVector();
        
        	try {
        		System.out.println(frameAngles.distanceTo(initVec));
        		//System.out.println("Current Angles : " + frameAngles);
        		//System.out.println("KNN Angles : " + initVec);
        		double distance = frameAngles.distanceTo(initVec) ;
        		fr.setDistance(distance);
				if(distance < recThreshold)
				{
					s++;
					if(s >= 1)
					{
						s = 0;
						if(sampleData.getNumOfFrames() > 30)
						{
							sampleSet.addSample(sampleData);
							sampleCount.set(sampleSet.getSize());
						}
						
						sampleData = new SampleData();
						
						if(sampleSet.getSize() == numOfSamples)
						{
							controller.removeListener(listener);
							 
							//Notify thread
							synchronized (SampleBuilder.this) {
				    			isStopped = true;
				    			SampleBuilder.this.notify();
				    		}
							
							if(this.mode == Mode.Training)
							{
							AnalyzeData.trainingActions(sampleSet);
							
							}
							
							else 
							{
								AnalyzeData.rehabActions(sampleSet);
								//Call Function
							}
							System.out.println("Finished");
						}
					
					//t.cancel();
					//isStopped = true;
					//System.out.println("Finished " + numOfFrames);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
	 

	protected void buildInitial() {
		// TODO Auto-generated method stub
		ArrayList<AnglesVector> vectors = sampleData.getSamplesVector();
		try {
			this.initVec = AnalyzeData.KNNRegression(vectors.get(40), vectors,10);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected FrameData handleFrame(Frame currentFrame) {
		// TODO Auto-generated method stub

	   	 PointableList pointableList = currentFrame.pointables();
	   	 Map<Finger.Type , Vector> tipDirections = new HashMap<Finger.Type,Vector>();
	   	
	   	  for(Pointable pointable : pointableList)
	      {
	         if(pointable.isFinger() && pointable.isValid())
	         {
	        	 Finger finger = new Finger(pointable);
	        	 tipDirections.put(finger.type(), pointable.direction());
	         }
	      }

		// Get fingers
		Map <Finger.Type, FingerData> fingersData = new HashMap<Finger.Type, FingerData>();
		
		HandList hands = currentFrame.hands();
		Hand hand = hands.get(0);
		
		
		//Get fingers
        for (Finger finger : hand.fingers()) {
        	Map <BoneType, Vector> bonesDirection = new HashMap<BoneType, Vector>();
        	
            //Get Bones
            for(Bone.Type boneType : Bone.Type.values()) {
                Bone bone = finger.bone(boneType);
                bonesDirection.put(BoneType.valueOf(boneType.name()),  bone.direction());
               
            }
           
            fingersData.put(finger.type(), new FingerData(bonesDirection));
            
        }
        
        FrameData timeStamp = new FrameData(numOfFrames,fingersData,hand.palmNormal(),tipDirections);
        sampleData.addFrame(timeStamp);
        return timeStamp;
	}
	
	public Vector getVec ()
	{
		   listener.onFrame(controller);
	        if(listener.getCurrentFrame() != null)
	        {
	        	 HandList hands = listener.getCurrentFrame().hands();
	 			Hand hand = hands.get(0);
	 			return hand.palmNormal();
	        }
	        return new Vector(0,0,0);
	       
				
	}
	
	public boolean isStopped()
	{
		return this.isStopped ;
	}
	
	private boolean staticMovement(ArrayList<Vector> speed) {
		// TODO Auto-generated method stub
		 double speedInterval = minSpeed ;
		 for(int i=0; i<speed.size(); i++)
		 {
			 double currentSpeed = Math.sqrt(Math.pow(speed.get(i).getX(), 2) + Math.pow(speed.get(i).getY(), 2) + Math.pow(speed.get(i).getZ(), 2));
			// System.out.println("Speed : " + currentSpeed);
			 if(currentSpeed > speedInterval)
			 {
				 return false;
			 }
		 }
		return true;
	}
	
	private boolean checkFrame(Frame frame)
	{

   	 PointableList pointableList = frame.pointables();
   	 
   	 ArrayList <Vector> speed = new  ArrayList<Vector>();
   	  for(Pointable pointable : pointableList)
         {
         if(pointable.isFinger() && pointable.isValid())
         speed.add(pointable.tipVelocity());
        
         }
   	 
   	 if(frame.hands().count() >= 1 && !startMotion) 
   	 {
   
   		 if(staticMovement(speed))
   		 {
   			
   			 System.out.println("STATI");
   			 
   			 return false;
   		 }
   		
   		 else startMotion = true;
   		 
   	 }
   	 
   	  if(frame.hands().count() >= 1 && startMotion && !staticMovement(speed)) 
   	 {
   		 return true;
   	 }
   	  
   	  return false;
   	  
	}
	
	
	private boolean checkFrame2(Frame frame)
	{

   	 PointableList pointableList = frame.pointables();
   	 
   	 ArrayList <Vector> speed = new  ArrayList<Vector>();
   	  for(Pointable pointable : pointableList)
         {
         if(pointable.isFinger() && pointable.isValid())
         speed.add(pointable.tipVelocity());
        
         }
   	 
   	 if(frame.hands().count() >= 1) 
   	 {
   		 if(staticMovement(speed))
   		 {
   			 System.out.println("STATI");
   			 return true;
   		 }
   		 
   		 else return false;
   		 
   	 }
   	 
   	  
   	  return false;
   	  
	}

	public boolean isStatic() {
		return isStatic;
	}
	


	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}
	
	public int getNumOfFrames()
	{
		return this.numOfFrames;
	}
	
	public IntegerProperty getIntegerProperty()
	{
		return this.sampleCount;
	}
	
	
	public BooleanProperty getLeapProperty()
	{
		return listener.getLeapStatusProperty();
	}


	
}
