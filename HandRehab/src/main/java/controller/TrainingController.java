package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.IntToDoubleFunction;

import entity.Mode;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import leap.SampleBuilder;

/**
 *  Handle the actions from the Training view,extends GController.
 * @author maor
 *
 */
public class TrainingController extends GController implements Initializable{
	
	@FXML
	private Button button;
	
	@FXML
	private TextArea textArea;
	
	@FXML
	private Button startRecordBtn;

	@FXML
	private ImageView exerciseImg;
	
	@FXML
	private Label trainingStatusLbl;
	
	@FXML
	private Label leapStatusLbl;
	
	@FXML
	private MenuItem closeChoice;
	
	@FXML
	private ProgressIndicator progressIndicator;
	
	protected SampleBuilder sb;
	
	private boolean isExerciseShowed;

	private final String calibrateMsg = "Waiting for Calibration...";
	
	private final String recordMsg = "Recording has started...";
	
	private Mode mode;
	
	
	/**
	 * Initialize all the variables of the controller 
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("Controller");
		this.setMode(Mode.Training);
		isExerciseShowed = false;
		//startRecordBtn.setDisable(true);
		sb = new SampleBuilder();
		fadeTransition(ap);
		if(sb.getLeapProperty().getValue())
		{
			leapStatusLbl.setTextFill(Color.web("#16d713"));
			leapStatusLbl.setText("Connected");	
		}
		
		sb.getIntegerProperty().addListener((ov,oldVal,newVal)->
        {
         	changeUi(()->{textArea.appendText("Sample #" + newVal + " is recorded successfully..."+"\n");});
        });
		
		sb.getLeapProperty().addListener((ov,b,newB)->
        {
        	if(newB == true)
        		changeUi(()->{
        			leapStatusLbl.setTextFill(Color.web("#16d713"));
        			leapStatusLbl.setText("Connected");	
        		});
        	else
        		changeUi(()->{
        			leapStatusLbl.setTextFill(Color.RED);
        			leapStatusLbl.setText("Disconnected");	
        		});
        });

	}
	
	
		/**
		 * Handle the Show Exercise button event action, this method will create an ImageView in the screen 
		 * that will display the exercise. 
		 * @param event - ActionEvent object.
		 * @throws IOException
		 */
		@FXML
		 protected void showExercise(ActionEvent event) throws IOException
		 {
			exerciseImg.setImage(new Image("file:handAnim.gif"));
			exerciseImg.setRotate(90);
			createTextAnimation(exerciseImg, 4700 , 0.8, 
					(ae)->{
						exerciseImg.setImage(null);
		            	startRecordBtn.setDisable(false);
						createTextAnimation(trainingStatusLbl,450,0,(a)->{
						trainingStatusLbl.setTextFill(Color.BLUE);
		 	            trainingStatusLbl.setOpacity(1);
		 	            trainingStatusLbl.setText(calibrateMsg);
						});
					});
					
			isExerciseShowed = true;
		 }
		

		/**
		 * 	This method checks if isExerciseShowed is true:
		 * If true  - call recordSamples method , if false - it show an alert to the user.
		 * @param event - ActionEvent object.
		 * @throws IOException
		 */
	  	@FXML
	  	protected void startRecord(ActionEvent event) 
		 {
	  		if (!isExerciseShowed)
	  		{
	  			Alert alert = new Alert(AlertType.ERROR);
	            alert.setTitle("Message");
	            alert.setHeaderText("You must view the exercise first.");
	            alert.showAndWait();
	  		}
	  		
	  		else recordSamples();
	  		
		 }
	  	
		/**
		 * Handle the Start Record button event action, this method will create 2 tasks , first for the calibration process and the other 
		 * for the recording process using the SampleBuilder class.
		 */
	  	protected void recordSamples()
	  	{
	  		
	  		progressIndicator.setVisible(true);
	  		exerciseImg.setImage(new Image("file:hand.gif"));
	  		exerciseImg.setRotate(0);
	  		
	  		Timeline timeline = createImgAnimation(exerciseImg, 800, 0, null);

    	  		taskRecording = new Task<Void>() {
    			@Override
    			protected Void call() throws Exception {
    				// TODO Auto-generated method stub
    				sb.initRecording(mode);
    				synchronized (sb) {
   			         while (!sb.isStopped())
   						try {
   							sb.wait();
   						} catch (InterruptedException e) {
   							
   							sb.stopRecord();
   	                        return null;
   							
   						}
    				}
    				
    			
    				changeUi(()->{
    					String message = new String();
    					if(mode == Mode.Training)
    						message = "Training has completed successfully ! now you can start your rehablitation.";
    					else message = "Rehab has completed successfully ! now you can start your rehablitation.";
    					Alert alert = new Alert(AlertType.CONFIRMATION);
    		            alert.setTitle("Message");
    		            alert.setHeaderText(message);
    		            alert.showAndWait();
    		            try {
							BackToMenu();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    					});
					return null;
    			}
    	  	};
		
	  		taskRecognize = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				// TODO Auto-generated method stub
				 sb.initCalibration();
				 synchronized (sb) {
			         while (!sb.isStatic())
						try {
							sb.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							sb.stopRecord();
   	                        return null;
						}
			         
			     }
				   
				   changeUi(()->{exerciseImg.setOpacity(1);});
	        	   timeline.stop();
	        	   
	        	  while(!sb.isStopped())
	        		  updateProgress(sb.getNumOfFrames(), SampleBuilder.numOfCalibFrames);
	        	   
	        	   //Start Record
	        	   System.out.println("Start Recording");
	        	   
	        	   changeUi(()->{exerciseImg.setImage(new Image("file:handAnim.gif"));
	       					     exerciseImg.setRotate(90);
	       					  createTextAnimation(trainingStatusLbl, 450, 0, (as) -> {
		      	 	            	trainingStatusLbl.setOpacity(1);
		      	 	            	trainingStatusLbl.setTextFill(Color.web("#16d713"));
		      	 	            	trainingStatusLbl.setText(recordMsg);
		      	 	            	progressIndicator.setVisible(false);
		      	 	            });
	       					 	});
	        	  new Thread(taskRecording).start();
				return null;
			}

	  	};
	  	
	  
	 	progressIndicator.progressProperty().bind(taskRecognize.progressProperty());
	
	  	new Thread(taskRecognize).start();
		
	  	}
	  	
	  	
	  	/**
	  	 * This method loads the Main Menu screen.
	  	 * @throws IOException - if FXMLLoader failed.
	  	 */
	  	protected void BackToMenu() throws IOException {
	  		 Stage stage = (Stage) button.getScene().getWindow();
			 Parent root = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));
			 Scene scene = new Scene(root);
			 stage.setScene(scene);
			 sb = null ;
		}

	  	/**
	  	 * This method running the runnable object in the UI Thread using Platform.runLater.
	  	 * @param runnable - The runnable object to run in the UI Thread.
	  	 */
		protected void changeUi(Runnable runnable)
	  	{
	 	   Platform.runLater(runnable);
	  	}
	  	
		/**
		 * Creating new text animation.
		 * @param node - The JavaFX element.
		 * @param time - Animation duration.
		 * @param opacityVal - the final opacity value.
		 * @param handler - The handler to rum after the animation is finished.
		 */
	  	protected void createTextAnimation(Node node, int time, double opacityVal , EventHandler <ActionEvent> handler)
	  	{
	  		    Timeline timeline = new Timeline();
	            KeyFrame key = new KeyFrame(Duration.millis(time),
	                           new KeyValue (node.opacityProperty(), opacityVal)); 
	            timeline.getKeyFrames().add(key);   
	            timeline.setOnFinished(handler);
	            timeline.play();
	  	}
	  	
	  	/**
	  	 * Creating new image animation.
	  	 * @param node - The JavaFX element.
		 * @param time - Animation duration.
		 * @param opacityVal - the final opacity value.
		 * @param handler - The handler to rum after the animation is finished.
	  	 * @return the TimeLine object.
	  	 */
	  	protected Timeline createImgAnimation(Node node, int time, double opacityVal , EventHandler <ActionEvent> handler)
	  	{
	  		    Timeline timeline = new Timeline();
	            KeyFrame key = new KeyFrame(Duration.millis(time),
	                           new KeyValue (node.opacityProperty(), opacityVal)); 
	            timeline.getKeyFrames().add(key);   
	            timeline.setOnFinished(handler);
	            timeline.setCycleCount(Timeline.INDEFINITE);
	            timeline.setAutoReverse(true); 
	            timeline.play();
	            return timeline;
	  	}

	  	/**
	  	 * Mode getter.
	  	 * @return the mode.
	  	 */
		public Mode getMode() {
			return mode;
		}

		/**
		 * Mode setter.
		 * @param mode
		 */
		public void setMode(Mode mode) {
			this.mode = mode;
		}
		
		
	  	
	  	
}
