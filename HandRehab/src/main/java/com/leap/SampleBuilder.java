package com.leap;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.controller.MainController;
import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Vector;

public class SampleBuilder {
	private FrameListener listener;
 	private Controller controller;
 	private SampleSet sampleSet ;
 	private SampleData sampleData;
	private int numOfFrames = 0;
	
	
	public SampleBuilder()
	{
		
	 	controller = new Controller();
		listener = new FrameListener(controller);
		sampleSet = new SampleSet();
	}
	
	public void startRecording()
	{
		
		 sampleData = new SampleData();
		 Timer t = new Timer();
	     t.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
			        listener.onFrame(controller);
			        if(listener.getCurrentFrame() != null)
			        {
			        	numOfFrames++;
			        	handleFrame(listener.getCurrentFrame());
			        }
			        if(numOfFrames == 20)
			        {
			        	//t.cancel();
			        	sampleSet.addSample(sampleData);
			        	
			        	sampleData = new SampleData();
			        	numOfFrames = 0;
			        }
			        
			        if(sampleSet.getSize() == 1) 
			        {
			        	t.cancel();
			        	
			        }
			        
				}
			},50, 50);
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
        
        FrameData timeStamp = new FrameData(numOfFrames,fingersData,hand.palmNormal());
        sampleData.addFrame(timeStamp);
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
}
