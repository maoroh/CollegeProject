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
	private ProgressBar progressBar;
	
	@FXML
	private ImageView exerciseImg;
	
	private SampleBuilder sb;
	
	private boolean isExerciseShowed;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		isExerciseShowed = true;
		//textArea.appendText("TEST");
		sb = new SampleBuilder();
		sb.getIntegerProperty().addListener((ov,oldVal,newVal)->
        {
        	if(sb.isStopped())
        		changeUi(()->{textArea.appendText("The training phase has completed successfully!");});
         	changeUi(()->{textArea.appendText("Sample num : " + newVal +"\n");});
        });
		//exerciseImg.setImage(new Image("file:a.gif"));
		//exerciseImg.setRotate(90);
	}
	
		@FXML
		 public void buttonClicked(ActionEvent event) throws IOException
		 {
			isExerciseShowed = true;
		  	Group popup = new Group();
			ImageView im=new ImageView(new Image("file:handAnim.gif"));
			im.setRotate(90);
			popup.getChildren().add(im);
			final Stage dialog = new Stage();
	        dialog.initModality(Modality.WINDOW_MODAL);
	        dialog.initOwner(button.getScene().getWindow());
	        Scene dialogScene = new Scene(popup);
	        dialogScene.setFill(Color.TRANSPARENT);
	        dialog.setScene(dialogScene);
	        dialog.initStyle(StageStyle.TRANSPARENT); 
	        dialog.setX(button.getScene().getWindow().getWidth() / 2 + 200);
	        dialog.setY(button.getScene().getWindow().getHeight() / 2 - 160 );
	        dialog.show();
	        
	        Timeline timeline = new Timeline();
            KeyFrame key = new KeyFrame(Duration.millis(4700),
                           new KeyValue (dialog.getScene().getRoot().opacityProperty(), 0.5)); 
            timeline.getKeyFrames().add(key);   
            timeline.setOnFinished((ae) -> dialog.hide()); 
            timeline.play();  
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
	  		Group popup = new Group();
			ImageView im=new ImageView(new Image("file:hand.gif"));
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
	        pi.setTranslateY(30);
	        pi.setTranslateX(30);
	        dialog.setX(button.getScene().getWindow().getWidth() / 2 + 200);
	        dialog.setY(button.getScene().getWindow().getHeight() / 2 - 160 );
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
				   changeUi(()->{dialog.getScene().getRoot().setOpacity(1);});
	        	   timeline.stop();
	        	   while(!sb.isStopped())
	        		   updateProgress(sb.getNumOfFrames(), 119);
	        	   
	        	   //Start Record
	        	   System.out.println("Start Recording");
	        	   
	        	   changeUi(()->{exerciseImg.setImage(new Image("file:handAnim.gif"));
	       					     exerciseImg.setRotate(90);
	        			   		 dialog.hide();});
	        	   
	        	   changeUi(()->
	        	   {
	        		   Alert alert = new Alert(AlertType.INFORMATION);
		               alert.setTitle("Next Step");
		               alert.initStyle(StageStyle.TRANSPARENT);
		               alert.setHeaderText("Now you need to perform a set of repetitions of the exercise...");
		               //alert.setContentText("Ooops, there was an error!");
		               alert.getButtonTypes().set(0,null);
		               alert.showAndWait();
		                
	        	   });
	        	  
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
	  	
	  	
	  	public void changeUi(Runnable runnable)
	  	{
	 	   Platform.runLater(runnable);
	  	}
	  	
	  	
}
