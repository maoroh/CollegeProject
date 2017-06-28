package leap;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Listener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * LeapListener class extends Listener class from the Leap Motion API.
 * This Listener handle all the actions related to the Leap Motion Controller.
 * @author maor
 *
 */
public class LeapListener extends Listener {
	private SampleBuilder sampleBuilder;
	private BooleanProperty leapStatus = new SimpleBooleanProperty();
	
	/**
	 * Initialize new LeapListener object.
	 * @param sample - The SampleBuilder object that will getting the frames.
	 */
	public LeapListener(SampleBuilder sample)
	{
		this.sampleBuilder = sample;
		 leapStatus.set(false);
	}
	
	/**
	 * Dispatched when the Leap Initialized.
	 */
	 @Override
	public void onInit(Controller controller) {
        System.out.println("Initialized");
    }

	 /**
	  * Dispatched when the Leap is connected.
	  */
	 @Override
    public void onConnect(Controller controller) {
        System.out.println("Connected");
        leapStatus.set(true);
    }

	 /**
	  * Dispatched when the Leap is disconnected.
	  */
    @Override
    public void onDisconnect(Controller controller) {
        //Note: not dispatched when running in a debugger.
        System.out.println("Disconnected");
        leapStatus.set(false);
    }

    /**
	  * Dispatched when the Leap is exited.
	  */
    @Override
    public void onExit(Controller controller) {
        System.out.println("Exited");
    }

    /**
	  * Dispatched when the Leap is getting new frame and call to newFrame method in sampleBuilder with this frame.
	  */
    @Override
    public void onFrame(Controller controller) {
    	
    	Frame frame = controller.frame();
    	sampleBuilder.newFrame(frame);

    }
	
    /**
     * Get the leapStatus property.
     * @return the leapStatus property.
     */
	public BooleanProperty getLeapStatusProperty() {
		return leapStatus;
	}



}
