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
import com.leapmotion.leap.Vector;

public class SampleBuilder {
	private FrameListener listener;
 	private Controller controller;
 	private SampleSet sampleSet ;
 	private SampleData sampleData;
	private int numOfFrames = 0;
	private AnglesVector initVec;
	int s = 0;
	public SampleBuilder()
	{
		
	 	controller = new Controller();
		listener = new FrameListener(controller);
		sampleSet = new SampleSet();
		initVec = Files.ReadVector();
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
			        Frame frame = listener.getCurrentFrame();
			        if(frame != null)
			        {
			        	numOfFrames++;
			        	FrameData fr = handleFrame(listener.getCurrentFrame());
			        	fr.setAnglesVector();
			        	
			        	AnglesVector frameAngles = fr.getAnglesVector();
			        
			        	try {
			        		System.out.println(frameAngles.distanceTo(initVec));
							if(frameAngles.distanceTo(initVec) < 0.7)
							{
								s++;
								if(s>=2)
								{
									try {
										System.out.println("begin Test");
										SampleData avg = AnalyzeData.avgSample(sampleData);
										System.out.println(avg.getFrame(0).getFingersData().get(Finger.Type.TYPE_INDEX).getBonesDirection().get(Bone.Type.TYPE_INTERMEDIATE));
										System.out.println(sampleData.getFrame(0).getFingersData().get(Finger.Type.TYPE_INDEX).getBonesDirection().get(Bone.Type.TYPE_INTERMEDIATE));
										System.out.println(avg.getNumOfFrames());
										//return;
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								t.cancel();
								System.out.println("Finished " + numOfFrames);
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        	
			        
			        }
			        if(numOfFrames == 200)
			        {
			        	
			        	//t.cancel();
			        	sampleSet.addSample(sampleData);
			        	//testData(sampleData);
			        	sampleData = new SampleData();
			        	numOfFrames = 0;
			        }
			        
			        if(sampleSet.getSize() == 100000000) 
			        {
			        	t.cancel();
			        	
			        }
			        
				}

				private void testData(SampleData sampleData) {
					// TODO Auto-generated method stub
					ArrayList<AnglesVector> vectors = sampleData.getSamplesVector();
					AnglesVector a=null;
					try {
						a = AnalyzeData.KNNRegression(vectors.get(100), vectors);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				//	a.toDegrees();
					System.out.println(a.getSize());
					System.out.println(a);
					try {
						System.out.println(a.distanceTo(vectors.get(150)));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			},50, 50);
	}

	protected FrameData handleFrame(Frame currentFrame) {
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
}
