package gui;

import java.net.URL;
import java.util.ResourceBundle;

import entity.Mode;


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
