package leap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Pointable;
import com.leapmotion.leap.PointableList;
import com.leapmotion.leap.Vector;

import controller.MainController;
import entity.BoneType;
import entity.DataVector;
import entity.FingerData;
import entity.FrameData;
import entity.Mode;
import entity.SampleData;
import entity.SampleSet;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import tools.AnalyzeData;

public class SampleBuilder {
	private LeapListener listener;
 	private Controller controller;
 	private SampleSet sampleSet ;
 	private SampleData sampleData;
	private int numOfFrames = 0;
	private boolean sampleCanSaved = false;
	private DataVector initVec;
	private boolean isStopped = false;
	private boolean isStatic = false;
	boolean startMotion = false;
	private IntegerProperty sampleCount = new SimpleIntegerProperty();
	private static final double recThreshold = 0.9;
	private static final int numOfSamples = 15;
	private boolean recording = false;
	private boolean recognize = false;
	private Mode mode ;
	private static final double minSpeed = 40;
	public static final int numOfCalibFrames = 100;
	
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
			handleNewFrame(frame);
		}
		else if(recognize)
		{
			handleCalibrationFrame(frame);
		}
	}
	
	private void handleCalibrationFrame(Frame frame) {
		// TODO Auto-generated method stub
		  
	        if(checkFrameCalib(frame))
	        {
	        	//Notify thread
	        	synchronized (SampleBuilder.this) {
	    			isStatic = true;
	    			SampleBuilder.this.notify();
	    		}
	        	numOfFrames++;
	        	buildNewFrame(frame);
	        	
	        
	        	if(numOfFrames == numOfCalibFrames)
	        	{
	        		try {
						buildInitial();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        		isStopped = true;
	        		
	        		controller.removeListener(listener);
					
	        	}
	        }
	        	
	}

	private void handleNewFrame(Frame frame)
	{
		
        if(checkFrame(frame) )
        {
        	numOfFrames++;
        	FrameData fr = buildNewFrame(frame);
        	fr.setAnglesVector();
        	//fr.setAnglesVector2(StaticData.initData);
        	DataVector frameAngles = fr.getAnglesVector();
        
        	try {
        		System.out.println(frameAngles.distanceTo(initVec));
        		
        		double distance = frameAngles.distanceTo(initVec) ;
        		fr.setDistance(distance);
				if(distance < recThreshold && sampleCanSaved )
				{
						sampleCanSaved = false;
						if(sampleData.getNumOfFrames() >= 60)
						{
						sampleSet.addSample(sampleData);
						sampleCount.set(sampleSet.getSize());
						sampleData = new SampleData();
						}
						
						if(sampleSet.getSize() == numOfSamples)
						{
							controller.removeListener(listener);
							 
							//Notify thread
							synchronized (SampleBuilder.this) {
				    			isStopped = true;
				    			SampleBuilder.this.notify();
				    		}
							
							if(this.mode == Mode.Training)		
								AnalyzeData.trainingActions(sampleSet);
							else 
								AnalyzeData.rehabActions(sampleSet);
								//Call Function					
							System.out.println("Finished");
						}
					}
				
				else if(distance > recThreshold + 0.5) {
					sampleCanSaved = true;
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
	 

	private void buildInitial() throws Exception {
		// TODO Auto-generated method stub
		ArrayList<DataVector> vectors = sampleData.getSamplesVector();
		
		try {
			this.initVec = AnalyzeData.KNNRegression(vectors.get(vectors.size() / 2), vectors , 15);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*/
		ArrayList <InitialData> fingersFramesTip = new ArrayList<InitialData>();
		
		for(int i = 0 ; i < sampleData.getNumOfFrames(); i++)
		{
			FrameData frameData = sampleData.getFrame(i);
			
			Map<Finger.Type, Vector> fingersTip = frameData.getTipDirections();
			
			ArrayList <DataVector> fingersTipData = new ArrayList<DataVector>();
			
			  for (Finger.Type finger : Finger.Type.values()) {
				  	Vector tipDirection = fingersTip.get(finger);
				  	DataVector tipDirectionData = DataVector.convertToDataVector(tipDirection);
				  	tipDirectionData.setName(finger);
				  	fingersTipData.add(tipDirectionData);
				  }
			  
			  fingersFramesTip.add(new InitialData(fingersTipData));
			 
		}
		
		ArrayList <DataVector> points = new ArrayList <DataVector> ();
		ArrayList <DataVector> avgTips = new ArrayList<DataVector>(); 

		for(int i = 0 ; i < Finger.Type.values().length ; i++)
		{
			for(int j = 0 ; j < fingersFramesTip.size() ; j++)
			{
					ArrayList<DataVector> fingersTip = fingersFramesTip.get(j).getFingersTip();
					points.add(fingersTip.get(i));
			}
			
			DataVector fingerTipAVG = AnalyzeData.KNNRegression(points.get(points.size() / 2), points, 10);
			Finger.Type type = Finger.Type.values()[i];
			fingerTipAVG.setName(type);
			avgTips.add(fingerTipAVG);
			
		}
		
		avgData = new InitialData(avgTips);/*/
		
	}
	
	
	

	private FrameData buildNewFrame(Frame currentFrame) {
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
        
        FrameData timeStamp = new FrameData(fingersData,hand.palmNormal(),tipDirections);
        sampleData.addFrame(timeStamp);
        return timeStamp;
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
   	 
   	  if(frame.hands().count() >= 1 && startMotion  ) 
   	 {
   		 return true;
   	 }
   	  
   	  return false;
   	  
	}
	
	
	private boolean checkFrameCalib(Frame frame)
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
	
	public void stopRecord()
	{
		controller.removeListener(listener);
	}


	
}
