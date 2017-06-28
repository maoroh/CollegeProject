package controller;

import java.net.URL;
import java.util.ResourceBundle;

import entity.Mode;

/**
 *  Handle the actions from the Rehabilitation view, extends TrainingController.
 * @author maor
 *
 */
public class RehabController extends TrainingController {
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		this.setMode(Mode.Rehab);
		
	}
	
	@Override
	protected void recordSamples()
	{
		super.recordSamples();
	}
	
	
	

}
