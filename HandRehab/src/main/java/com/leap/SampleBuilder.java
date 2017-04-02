package com.leap;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Vector;

public class SampleBuilder {
	private TimeStampListener listener;
 	private Controller controller;
 	private SampleSet sampleSet ;
 	private SampleData sampleData;
	private int numOfFrames = 0;
	
	public SampleBuilder()
	{
	 	controller = new Controller();
		listener = new TimeStampListener(controller);
		sampleSet = new SampleSet();
	}
	
	public void recordNewSample()
	{
		
		 sampleData = new SampleData();
		 Timer t = new Timer();
	        t.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					numOfFrames++;
			        listener.onFrame(controller);
			        handleFrame(listener.getCurrentFrame());
			        if(numOfFrames == 20)
			        {
			        	//t.cancel();
			        	sampleSet.addSample(sampleData);
			        	sampleData = new SampleData();
			        	numOfFrames = 0;
			        }
			        
			        if(sampleSet.getSize() == 10) 
			        	t.cancel();
			        
				}
			},100, 100);
	}

	protected void handleFrame(Frame currentFrame) {
		// TODO Auto-generated method stub
		 // Get fingers
	
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
        
        TimeStampData timeStamp = new TimeStampData(numOfFrames,fingersData,hand.palmNormal());
        sampleData.addTimeStamp(timeStamp);
	}
}
