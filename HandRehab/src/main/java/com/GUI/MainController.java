package com.GUI;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import com.leap.SampleBuilder;
import com.leapmotion.leap.Vector;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController extends GController implements Initializable {
	
	@FXML
	Button rehabButton;
	
	@FXML
	ImageView bg;
	
	@FXML
	Label label;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	  
	 
	  @FXML
	 public void trainingButton(ActionEvent event) throws IOException
	 {
		 Stage stage = (Stage) rehabButton.getScene().getWindow();
		 Parent root = FXMLLoader.load(getClass().getResource("TrainingView.fxml"));
		 Scene scene = new Scene(root);
	     stage.setScene(scene);

	 }
	  
	  @FXML
	  public void RehabButton(ActionEvent event) throws IOException
	  {
		 Stage stage = (Stage) rehabButton.getScene().getWindow();
		 Parent root = FXMLLoader.load(getClass().getResource("RehabView.fxml"));
		 Scene scene = new Scene(root);
		 stage.setScene(scene);
	  }
	  
	  @FXML
	  public void feedbackButton(ActionEvent event) throws IOException
	  {
		  Stage stage = (Stage) rehabButton.getScene().getWindow();
			 Parent root = FXMLLoader.load(getClass().getResource("FeedbackView.fxml"));
			 Scene scene = new Scene(root);
			 stage.setScene(scene);
	  }



}
