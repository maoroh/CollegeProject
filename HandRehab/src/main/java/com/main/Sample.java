
package com.main;
import java.io.IOException;
import java.lang.Math;
import java.util.Timer;
import java.util.TimerTask;

import com.leap.LeapListener;
import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;

class SampleListener extends Listener {
    
}

public class Sample {
    public static void main(String[] args) {
        // Create a sample listener and controller
    	
        
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
        	Controller controller = new Controller();
	        LeapListener listener = new LeapListener(controller,t);
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
		        listener.onFrame(controller);
			}
		},100, 100);
       	
      
        // Have the sample listener receive events from the controller
       // controller.addListener(listener);

        // Keep this process running until Enter is pressed
       
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Remove the sample listener when done
       
    }
}





