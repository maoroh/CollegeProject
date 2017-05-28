package com.GUI;

import java.net.URL;
import java.util.ResourceBundle;

import com.leap.Mode;


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
