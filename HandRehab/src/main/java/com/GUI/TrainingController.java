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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
	private Button button;
	
	@FXML
	private TextArea textArea;
	
	@FXML
	private Button startRecordBtn;
	
	
	@FXML
	private ImageView exerciseImg;
	
	@FXML
	private ProgressIndicator progressIndicator;
	
	private SampleBuilder sb;
	
	private boolean isExerciseShowed;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		isExerciseShowed = false;
		startRecordBtn.setDisable(true);
		//textArea.appendText("TEST");
		sb = new SampleBuilder();
		sb.getIntegerProperty().addListener((ov,oldVal,newVal)->
        {
        	if(sb.isStopped())
        		changeUi(()->{textArea.appendText("The training phase has completed successfully!");});
         	changeUi(()->{textArea.appendText("Sample num : " + newVal +"\n");});
        });
		//exerciseImg.setImage(new Image("file:handAnim.gif"));
		//exerciseImg.setRotate(90);
	}
	
		@FXML
		 public void buttonClicked(ActionEvent event) throws IOException
		 {
			exerciseImg.setImage(new Image("file:handAnim.gif"));
			exerciseImg.setRotate(90);
			 Timeline timeline = new Timeline();
	            KeyFrame key = new KeyFrame(Duration.millis(4700),
	                           new KeyValue (exerciseImg.opacityProperty(), 0.8)); 
	            timeline.getKeyFrames().add(key);   
	            timeline.setOnFinished((ae) -> {
	            	exerciseImg.setImage(null);
	            	startRecordBtn.setDisable(false);
	            	}
	            );
	            timeline.play(); 
	            isExerciseShowed = true;
	           
	       
		 }
	  
	  	@FXML
		 public void startRecord(ActionEvent event) 
		 {
	  		if (!isExerciseShowed)
	  		{
	  		Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("You must view the exercise first!");
            alert.setContentText("Ooops, there was an error!");
            alert.showAndWait();
	  		}
	  		
	  		else recording();
	  		
		 }
	  	
	  	public void recording()
	  	{
	  		progressIndicator.setVisible(true);
	  		
	  		exerciseImg.setImage(new Image("file:hand.gif"));
	  		exerciseImg.setRotate(0);
	        Timeline timeline = new Timeline();
            KeyFrame key = new KeyFrame(Duration.millis(800),
                           new KeyValue (exerciseImg.opacityProperty(), 0)); 
            timeline.getKeyFrames().add(key);   
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.setAutoReverse(true); 
            //Play animation
            timeline.play();
            
     	   
    	  	Task<Void> taskRecording = new Task<Void>() {

    			@Override
    			public void run() {
    				// TODO Auto-generated method stub
    				boolean a = false;
    				sb.startRecordingFull();
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
				   changeUi(()->{exerciseImg.setOpacity(1);});
	        	   timeline.stop();
	        	   while(!sb.isStopped())
	        		   updateProgress(sb.getNumOfFrames(), 119);
	        	   
	        	   //Start Record
	        	   System.out.println("Start Recording");
	        	   
	        	   changeUi(()->{exerciseImg.setImage(new Image("file:handAnim.gif"));
	       					     exerciseImg.setRotate(90);
	        			   		 });
	        	   
	        	   
	        	   new Thread(taskRecording).start();
			}

			@Override
			protected Void call() throws Exception {
				// TODO Auto-generated method stub
				
				return null;
			}

	  	};
	  	
	  
	 	progressIndicator.progressProperty().bind(taskRecognize.progressProperty());
	
	  	new Thread(taskRecognize).start();
		
	  	}
	  	
	  	
	  	public void changeUi(Runnable runnable)
	  	{
	 	   Platform.runLater(runnable);
	  	}
	  	
	  	
}
