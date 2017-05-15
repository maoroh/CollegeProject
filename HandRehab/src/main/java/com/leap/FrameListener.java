package com.leap;

import java.util.ArrayList;
import java.util.Timer;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Pointable;
import com.leapmotion.leap.PointableList;
import com.leapmotion.leap.Vector;


public class FrameListener extends Listener {
	private Frame currentFrame;
	Controller controller;
	boolean startMotion = false;
	boolean stopMotion = false;
	ArrayList<Frame> frames = new ArrayList<Frame>();
	boolean isOver = false;
	boolean check = false;
	boolean check2 = false;
	Timer t;
	
	int numOfFrames=0;
	public FrameListener(Controller controller)
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

    }
	private boolean staticMovement(ArrayList<Vector> speed) {
		// TODO Auto-generated method stub
		 float speedInterval = 50;
		 for(int i=0; i<speed.size(); i++)
		 {
			 if(Math.sqrt(Math.pow(speed.get(i).getX(), 2) + Math.pow(speed.get(i).getY(), 2) + Math.pow(speed.get(i).getZ(), 2))  > speedInterval)
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
