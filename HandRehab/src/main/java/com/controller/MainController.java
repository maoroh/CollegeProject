package com.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import com.leap.SampleBuilder;
import com.leapmotion.leap.Vector;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainController {
	
	@FXML
	Button button;
	@FXML
	ImageView bg;
	
	@FXML
	Label label;
	 SampleBuilder sb;
	  public void initialize() {
		 //button.setDisable(true);
		 sb = new SampleBuilder();
		 sb.startRecording();
		 BufferedImage bufferedImage;
		 try {
			bufferedImage = ImageIO.read(new File("bg.png"));
			 Image image = SwingFXUtils.toFXImage(bufferedImage, null);
			 this.bg.setImage(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
	  
	 }
	  
	 //Test method
	 public void buttonClicked()
	 {
		
		 System.out.println("Button Clicked");
		  Vector vec = sb.getVec();
		  System.out.println("" + vec.getX() +" "+ vec.getY()+ " " + vec.getZ());
	    
	 }

}
