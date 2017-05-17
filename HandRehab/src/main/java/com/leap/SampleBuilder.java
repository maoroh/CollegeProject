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

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class SampleBuilder {
	private FrameListener listener;
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
	public SampleBuilder()
	{
		
	 	controller = new Controller();
		listener = new FrameListener(controller);
		sampleSet = new SampleSet();
		initVec = Files.ReadVector();
	}
	
	public void startRecording()
	{
		 isStopped = false;
		 numOfFrames = 0;
		 sampleData = new SampleData();
		 Timer t = new Timer();
	     t.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
				
			        listener.onFrame(controller);
			        Frame frame = listener.getCurrentFrame();
			        if(checkFrame(frame))
			        {
			        	numOfFrames++;
			        	FrameData fr = handleFrame(listener.getCurrentFrame());
			        	fr.setAnglesVector();
			        	AnglesVector frameAngles = fr.getAnglesVector();
			        
			        	try {
			        		System.out.println(frameAngles.distanceTo(initVec));
			        		System.out.println("Current Angles : " + frameAngles);
			        		System.out.println("KNN Angles : " + initVec);
							if(frameAngles.distanceTo(initVec) < 0.8)
							{
								s++;
								if(s>=4)
								{
									try {
										/*/
										System.out.println("begin Test");
										SampleData avg = AnalyzeData.avgSample(sampleData);
										System.out.println(avg.getFrame(6).getFingersData().get(Finger.Type.TYPE_INDEX).getBonesDirection().get(Bone.Type.TYPE_INTERMEDIATE));
										System.out.println(sampleData.getFrame(9).getFingersData().get(Finger.Type.TYPE_INDEX).getBonesDirection().get(Bone.Type.TYPE_INTERMEDIATE));
										System.out.println(sampleData.getFrame(10).getFingersData().get(Finger.Type.TYPE_INDEX).getBonesDirection().get(Bone.Type.TYPE_INTERMEDIATE));
										System.out.println(sampleData.getFrame(11).getFingersData().get(Finger.Type.TYPE_INDEX).getBonesDirection().get(Bone.Type.TYPE_INTERMEDIATE));
										System.out.println(sampleData.getFrame(12).getFingersData().get(Finger.Type.TYPE_INDEX).getBonesDirection().get(Bone.Type.TYPE_INTERMEDIATE));
										System.out.println(avg.getNumOfFrames());/*/
										//return;
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								t.cancel();
								isStopped = true;
								System.out.println("Finished  "+ + numOfFrames +  " s= " +s );
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        	
			        
			        }
		
			        
				}

	
			},50, 50);
	}
	 
	public void startRecordingFull()
	{
		 isStopped = false;
		 numOfFrames = 0;
		 sampleData = new SampleData();
		
		 Timer t = new Timer();
	     t.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
				
			        listener.onFrame(controller);
			        Frame frame = listener.getCurrentFrame();
			        if(checkFrame(frame))
			        {
			        	numOfFrames++;
			        	FrameData fr = handleFrame(listener.getCurrentFrame());
			        	fr.setAnglesVector();
			        	AnglesVector frameAngles = fr.getAnglesVector();
			        
			        	try {
			        		System.out.println(frameAngles.distanceTo(initVec));
			        		System.out.println("Current Angles : " + frameAngles);
			        		System.out.println("KNN Angles : " + initVec);
			        		double distance = frameAngles.distanceTo(initVec) ;
			        		fr.setDistance(distance);
							if(distance < 0.8)
							{
								s++;
								if(s>=4)
								{
									s = 0;
									if(sampleData.getNumOfFrames()>20)
									{
										sampleSet.addSample(sampleData);
										sampleCount.set(sampleSet.getSize());
									}
									sampleData = new SampleData();
									if(sampleSet.getSize() == 5)
									{
										t.cancel();
										//Notify thread
							        	synchronized (this) {
							    			isStopped = true;
							    			this.notify();
							    		}
										MovementPattern pattern = AnalyzeData.buildMovementPattern(sampleSet);
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

	
			},50, 50);
	}

	
	public void recognizeHand()
	{
		isStopped = false;
		numOfFrames = 0;
		 sampleData = new SampleData();
		 Timer t = new Timer();
	     t.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
				
			        listener.onFrame(controller);
			        Frame frame = listener.getCurrentFrame();
			        if(checkFrame2(frame))
			        {
			        	//Notify thread
			        	synchronized (this) {
			    			isStatic = true;
			    			this.notify();
			    		}
			        	numOfFrames++;
			        	if(numOfFrames >= 20)
			        		handleFrame(listener.getCurrentFrame());
			        	//fr.setAnglesVector();
			        	if(numOfFrames == 120)
			        	{
			        		numOfFrames++;
			        		isStopped = true;
			        		t.cancel();
			        		buildInitial();
			        	}
			        	
			        } 
				}

	
			},50, 50);
	}

	protected void buildInitial() {
		// TODO Auto-generated method stub
		ArrayList<AnglesVector> vectors = sampleData.getSamplesVector();
		try {
			this.initVec = AnalyzeData.KNNRegression(vectors.get(50), vectors);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected FrameData handleFrame(Frame currentFrame) {
		// TODO Auto-generated method stub
		 // Get fingers
		Map <Finger.Type, Vector> tipDirections = new HashMap<Finger.Type, Vector>();
		PointableList pointableList = currentFrame.pointables();

	   	  for(Pointable pointable : pointableList)
	      {
	         if(pointable.isFinger() && pointable.isValid())
	         {
	        	 Finger finger = new Finger(pointable);
	        	 tipDirections.put(finger.type() , pointable.direction());
	         }

	      }
	   	  
		Map <Finger.Type, FingerData> fingersData = new HashMap<Finger.Type, FingerData>();
		
		HandList hands = currentFrame.hands();
		Hand hand = hands.get(0);
		
		
		//Get fingers
        for (Finger finger : hand.fingers()) {
        	Map <Bone.Type, Vector> bonesDirection = new HashMap<Bone.Type, Vector>();
        	
            //Get Bones
            for(Bone.Type boneType : Bone.Type.values()) {
            	
                Bone bone = finger.bone(boneType);
                bonesDirection.put(boneType,  bone.direction());
               
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
		 double speedInterval = 100;
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
   	 
   	  if(frame.hands().count() >= 1 && startMotion) 
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
}
