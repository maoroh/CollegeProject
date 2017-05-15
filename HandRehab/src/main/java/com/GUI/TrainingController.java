package com.GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.IntToDoubleFunction;

import com.leap.SampleBuilder;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class TrainingController implements Initializable{
	
	@FXML
	Button button;
	
	@FXML
	TextArea textArea;
	
	@FXML
	Button startRecordBtn;
	
	@FXML
	ProgressBar progressBar;
	
	SampleBuilder sb;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		textArea.appendText("TEST");
		sb = new SampleBuilder();
      
	}
	
		@FXML
		 public void buttonClicked(ActionEvent event) throws IOException
		 {
		  	Group popup = new Group();
			ImageView im=new ImageView(new Image("file:a.gif"));
			im.setRotate(90);
			popup.getChildren().add(im);
			final Stage dialog = new Stage();
	        dialog.initModality(Modality.WINDOW_MODAL);
	        dialog.initOwner(button.getScene().getWindow());
	        Scene dialogScene = new Scene(popup);
	        dialogScene.setFill(Color.TRANSPARENT);
	        dialog.setScene(dialogScene);
	        dialog.initStyle(StageStyle.TRANSPARENT); 
	        dialog.show();
	        
	        Timeline timeline = new Timeline();
            KeyFrame key = new KeyFrame(Duration.millis(5000),
                           new KeyValue (dialog.getScene().getRoot().opacityProperty(), 0.7)); 
            timeline.getKeyFrames().add(key);   
            timeline.setOnFinished((ae) -> dialog.hide()); 
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.setAutoReverse(true); 
            timeline.play();
          
           
		 }
	  
	  	@FXML
		 public void startRecord(ActionEvent event) 
		 {
	  		Group popup = new Group();
			ImageView im=new ImageView(new Image("file:hand.jpg"));
			//final ProgressBar pb = new ProgressBar(0.5);
		    final ProgressIndicator pi = new ProgressIndicator(0);
			popup.getChildren().addAll(im,pi);
			final Stage dialog = new Stage();
	        dialog.initModality(Modality.WINDOW_MODAL);
	        dialog.initOwner(button.getScene().getWindow());
	        Scene dialogScene = new Scene(popup);
	        dialogScene.setFill(Color.TRANSPARENT);
	        dialog.setScene(dialogScene);
	        dialog.initStyle(StageStyle.TRANSPARENT); 
	        dialog.show();
	        
	        Timeline timeline = new Timeline();
            KeyFrame key = new KeyFrame(Duration.millis(500),
                           new KeyValue (dialog.getScene().getRoot().opacityProperty(), 0)); 
            timeline.getKeyFrames().add(key);   
            timeline.setOnFinished((ae) -> dialog.hide()); 
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.setAutoReverse(true); 
            //Play animation
            timeline.play();
        	
    	  	Task<Void> taskRecording = new Task<Void>() {

    			@Override
    			public void run() {
    				// TODO Auto-generated method stub
    				boolean a = false;
    				  updateProgress(0, 100);
    				sb.startRecording();
    				 while(!a)
    	        	   {
    	        		   a = sb.isStopped();
    	        		   try {
    						Thread.yield();
    					} catch (Exception e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
    	        	   }
    	        	 
    				 updateProgress(100, 100);
    	        		 
    	        	  
    	        	   
    			}

    			@Override
    			protected Void call() throws Exception {
    				// TODO Auto-generated method stub
    				
    				return null;
    			}

    	  	};
    	  	
		
	  	Task<Void> taskRecognize = new Task<Void>() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean a = false;
				sb.recognizeHand();
				 while(!a)
	        	   {
	        		   a = sb.isStatic();
	        		   try {
						Thread.yield();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	   }
	        	   System.out.println("NOT");
	        	   timeline.stop();
	        	   
	        	   while(!sb.isStopped())
	        		   updateProgress(sb.getNumOfFrames(), 119);
	        	   
	        	   System.out.println("Start Recording");
	        	
	        	   new Thread(taskRecording).start();
	        	   
			}

			@Override
			protected Void call() throws Exception {
				// TODO Auto-generated method stub
				
				return null;
			}

	  	};
	  	
	  
	  	pi.progressProperty().bind(taskRecognize.progressProperty());
	
	  	new Thread(taskRecognize).start();
		}
}
