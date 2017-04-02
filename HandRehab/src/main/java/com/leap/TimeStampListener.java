package com.leap;

import java.util.ArrayList;
import java.util.Timer;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Pointable;
import com.leapmotion.leap.PointableList;
import com.leapmotion.leap.Vector;

public class TimeStampListener extends Listener {
	private Frame currentFrame;
	Controller controller;
	int counter = 0;
	boolean isOver = false;
	boolean check = false;
	boolean check2 = false;
	Timer t;
	int numOfFrames=0;
	public TimeStampListener(Controller controller)
	{
		this.controller = controller;
	}
	 @Override
	public void onInit(Controller controller) {
        System.out.println("Initialized");
    }

	 @Override
    public void onConnect(Controller controller) {
        System.out.println("Connected");
        /*/controller.enableGesture(Gesture.Type.TYPE_SWIPE);
        controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
        controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
        controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);/*/
    }

    @Override
    public void onDisconnect(Controller controller) {
        //Note: not dispatched when running in a debugger.
        System.out.println("Disconnected");
    }

    @Override
    public void onExit(Controller controller) {
        System.out.println("Exited");
    }


    public void onFrame(Controller controller) {
    	
    	Frame frame = controller.frame();
    	this.setCurrentFrame(frame);
    	/*/
    	numOfFrames++;
    	Frame frame =  controller.frame();
        PointableList pointableList = frame.pointables();
        ArrayList <Vector> speed = new  ArrayList<Vector>();
        for(Pointable pointable : pointableList)
        {
        if(pointable.isFinger() && pointable.isValid())
        speed.add(pointable.tipVelocity());
       
        }
       
        if(speed.size() == 5 && startMotion(speed) && !check) 
        {
        	counter = 0;
        	check = true;
        	System.out.println("GETTTT");
        	
        }
       
       System.out.println("Frame id: " + frame.id()
       + ", timestamp: " + frame.timestamp()
       + ", hands: " + frame.hands().count()
       + ", fingers: " + frame.fingers().count()
       + ", tools: " + frame.tools().count()
       + ", gestures " + frame.gestures().count());
      if(check)
      {
       if(speed.size() == 5 && stopMotion(speed) ) 
       {
    	   counter++;
       }
  
       if(counter >= 2) 
       {
    	  t.cancel();
    	  isOver = true;
       }
      }
      if(speed.size() > 0)
       System.out.println(speed.get(0).getX() +" "+ isOver + " "+ counter+" " +numOfFrames);/*/
    }
	private boolean stopMotion(ArrayList<Vector> speed) {
		// TODO Auto-generated method stub
		 float speedInterval = 30;
		 for(int i=0; i<speed.size(); i++)
		 {
			 if(Math.sqrt(Math.pow(speed.get(i).getX(), 2) + Math.pow(speed.get(i).getY(), 2) + Math.pow(speed.get(i).getZ(), 2))  > speedInterval)
			 {
				 return false;
			 }
		 }
		return true;
	}
	
	private boolean startMotion(ArrayList<Vector>  speed) {
		// TODO Auto-generated method stub
		 float speedInterval = 30;
		 for(int i=0; i<speed.size(); i++)
		 {
			 if(Math.sqrt(Math.pow(speed.get(i).getX(), 2) + Math.pow(speed.get(i).getY(), 2) + Math.pow(speed.get(i).getZ(), 2)) < speedInterval)
			 {
				 return false;
			 }
		 }
		return true;
	}
	public Frame getCurrentFrame() {
		return currentFrame;
	}
	public void setCurrentFrame(Frame currentFrame) {
		this.currentFrame = currentFrame;
	}

}
