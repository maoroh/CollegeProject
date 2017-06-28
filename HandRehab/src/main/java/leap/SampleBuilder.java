package leap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Pointable;
import com.leapmotion.leap.PointableList;
import com.leapmotion.leap.Vector;
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
import utility.AnalyzeData;

/**
 * SampleBuilder.
 * This class is responsible for recording all the training/rehabilitation samples data using the Leap controller.
 * @author maor
 *
 */
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
	private boolean calibration = false;
	private Mode mode ;
	private static final double minSpeed = 40;
	public static final int numOfCalibFrames = 100;
	
	/**
	 * Initialize new SampleBuilder object.
	 */
	public SampleBuilder()
	{
	 	controller = new Controller();
		listener = new LeapListener(this);
		controller.addListener(listener);
		sampleSet = new SampleSet();
		
	}
	
	/**
	 * Initialize recording process.
	 * @param mode - Training/ Rehablitation.
	 */
	public void initRecording(Mode mode)
	{
		 isStopped = false;
		 this.mode = mode;
		 numOfFrames = 0;
		 sampleData = new SampleData();
		 recording = true;
		 controller.addListener(listener);
		
	}
	
	/**
	 * Initialize Calibration process.
	 */
	public void initCalibration()
	{
		calibration = true;
		isStopped = false;
		numOfFrames = 0;
		sampleData = new SampleData();
		recording = false;
	}
	
	
	/**
	 * Checking if the current process is calibration or recording and handle this frame.
	 * @param frame - The frame from the LeapListener.
	 */
	public void newFrame(Frame frame)
	{
		if(recording)
		{
			handleNewFrame(frame);
		}
		else if(calibration)
		{
			handleCalibrationFrame(frame);
		}
	}
	
	/**
	 * Handle calibration frame.
	 * Checks if the patient hand in front of the leap :
	 * if true - Notify to the waiting thread , build the frame with the necessary elements and add this frmae to the sample.
	 * if numOfFrames = numOfCalibFrames - stop the recording and build the initial data vector, else continue getting frames.
	 * @param frame - The frame from the LeapListener.
	 */
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

	/**
	 * Handle recording frame.
	 * Checks if the frame is a valid frame :
	 * if true - build the frame with the necessary elements , set the angles vector of this frame and add this frame to the sample.
	 * if this frame distance from the initialFrame less than the recThreshold - create a new sample.
	 * else continue getting frames for current sample.
	 * @param frame - The Frame from the LeapListener.
	 */
	private void handleNewFrame(Frame frame)
	{
		
        if(checkFrame(frame) )
        {
        	numOfFrames++;
        	FrameData fr = buildNewFrame(frame);
        	fr.setAnglesVector();
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
	 

	/**
	 * Build the initialFrame for recognize the end of a sample.
	 * @throws Exception  - KNNRegression exception.
	 */
	private void buildInitial() throws Exception {
		// TODO Auto-generated method stub
		ArrayList<DataVector> vectors = sampleData.getSamplesVector();
		
		try {
			this.initVec = AnalyzeData.KNNRegression(vectors.get(vectors.size() / 2), vectors , 15);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Taking the neccessary elements from the Leap Frame data.
	 * @param currentFrame - the Frame from the LeapListener.
	 * @return FrameData with the specific elements we need for the algorithm.
	 */
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
	
	
	/**
	 * isStopped getter.
	 * @return isStopped value.
	 */
	public boolean isStopped()
	{
		return this.isStopped ;
	}
	
	/**
	 * This method checks if the patient in a static movement or not.
	 * @param speed - an array of speed values .
	 * @return true - if static , false - if not static.
	 */
	private boolean staticMovement(ArrayList<Vector> speed) {
		// TODO Auto-generated method stub
		 double speedInterval = minSpeed ;
		 for(int i=0; i<speed.size(); i++)
		 {
			 double currentSpeed = Math.sqrt(Math.pow(speed.get(i).getX(), 2) + Math.pow(speed.get(i).getY(), 2) + Math.pow(speed.get(i).getZ(), 2));
			 if(currentSpeed > speedInterval)
			 {
				 return false;
			 }
		 }
		return true;
	}
	
	/**
	 * This method checks if current frame is a static frame or not and if it's valid .
	 * First it checks the if the patient in a static movement:
	 * If true - return false.
	 * If false - set startMotion to true (recognize the start of the movement) and return true.
	 * the validation of the frame is according to the hands count from the leap data.
	 * @param frame - The frame from the LeapListener.
	 * @return true - if the frame is valid and the patient start move.
	 * false - if the frame is not valid or the patient is static.
	 */
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
	
	
	/**
	 * This method check if the current frame is a static frame or not and if it's valid in the calibration mode.
	 * When the method recognize the hand of the patient in a static state in the first time it will return true.
	 * @param frame - The frame from the LeapListener.
	 * @return true - if the patient in a static state and in front of the leap motion controller(the frame is valid).
	 * false - if the patient not in a static state or the frame is not valid.
	 */
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

	/**
	 * isStatic getter.
	 * @return isStatic value.
	 */
	public boolean isStatic() {
		return isStatic;
	}
	


	/**
	 * isStatic setter.
	 * @param isStatic - set isStatic value.
	 */
	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}
	
	/**
	 * numOfFrames getter.
	 * @return numOfFrames value.
	 */
	public int getNumOfFrames()
	{
		return this.numOfFrames;
	}
	
	/**
	 * sampleCount property getter.
	 * @return sampleCount property value.
	 */
	public IntegerProperty getIntegerProperty()
	{
		return this.sampleCount;
	}
	
	/**
	 * LeapStatusProperty getter.
	 * @return LeapStatusProperty value.
	 */
	public BooleanProperty getLeapProperty()
	{
		return listener.getLeapStatusProperty();
	}
	
	/**
	 * Remove the listener from the leap controller and stop getting new frames.
	 */
	public void stopRecord()
	{
		controller.removeListener(listener);
	}


	
}
