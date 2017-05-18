package com.leap;

import java.util.ArrayList;
import java.util.Timer;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Pointable;
import com.leapmotion.leap.PointableList;
import com.leapmotion.leap.Vector;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;


public class LeapListener extends Listener {
	private Frame currentFrame;
	private SampleBuilder sampleBuilder;
	private BooleanProperty leapStatus = new SimpleBooleanProperty();
	
	public LeapListener(SampleBuilder sample)
	{
		this.sampleBuilder = sample;
		 leapStatus.set(false);
	}
	
	 @Override
	public void onInit(Controller controller) {
        System.out.println("Initialized");
    }

	 @Override
    public void onConnect(Controller controller) {
        System.out.println("Connected");
        leapStatus.set(true);
        /*/controller.enableGesture(Gesture.Type.TYPE_SWIPE);
        controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
        controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
        controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);/*/
    }

    @Override
    public void onDisconnect(Controller controller) {
        //Note: not dispatched when running in a debugger.
        System.out.println("Disconnected");
        leapStatus.set(false);
    }

    @Override
    public void onExit(Controller controller) {
        System.out.println("Exited");
    }

    @Override
    public void onFrame(Controller controller) {
    	
    	Frame frame = controller.frame();
    	//this.setCurrentFrame(frame);
    	sampleBuilder.newFrame(frame);

    }
	
	public Frame getCurrentFrame() {
		return currentFrame;
	}
	public void setCurrentFrame(Frame currentFrame) {
		this.currentFrame = currentFrame;
	}

	public BooleanProperty getLeapStatusProperty() {
		return leapStatus;
	}



}
