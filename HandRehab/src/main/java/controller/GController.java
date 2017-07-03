package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Abstract JavaFX Controller , that implements Initializable interface.
 * @author maor
 *
 */
public class GController implements Initializable {

	@FXML
	private MenuItem closeChoice ;
	
	@FXML 
	protected AnchorPane ap;
	
	@FXML
	private MenuItem backToMenu ;
	
	@FXML
	private MenuItem desMenu ;
	
	@FXML
	private MenuItem aboutMenu ;
	
	protected Task<Void> taskRecognize;
	
	protected Task<Void> taskRecording;
	
	protected URL location;
	
	/**
	 * Handle Menu Actions.
	 * @param event - The menu event object.
	 * @throws IOException
	 */
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		this.location = location;
	}
	
	
	@FXML
	 public void menuAction(ActionEvent event) throws IOException
	 {
		MenuItem mItem = (MenuItem) event.getSource();
		
		if(mItem == closeChoice)
		{
			System.exit(0);
		}
		
		if(mItem == desMenu)
		{
			String filename = location.getFile().substring(location.getFile().lastIndexOf('/')+1, location.getFile().length());
			
			if(filename.equals("MainView.fxml"))
			{
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Main Menu Help");
				alert.setHeaderText("Main View");
				alert.setContentText("You have 3 options : \n"
						+ 		     "1. Perform the training phase.\n"
						+ 			 "2. Perform the rehabilitation phase - You need to finish the training before.\n"
						+			 "3. View your feedback - You need to finish the training and the rehabilitation phases before.\n"
							 );
				alert.showAndWait();
			}
			
			else if(filename.equals("TrainingView.fxml") || filename.equals("RehabView.fxml") )
			{
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Training View Help");
				alert.setHeaderText("Training Phase");
				alert.setContentText("1. Connect your Leap Motion Controller.\n"
						+ 			 "2. View the exercise you need to perform.\n"
						+			 "3. Do Calibration phase.\n"
						+			 "4. Start recording samples while repeat the exercise 15 times.\n");

				alert.showAndWait();
			}
			
			else if(filename.equals("FeedbackView.fxml"))
			{
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Feedback View Help");
				alert.setHeaderText("Feedback Results");
				alert.setContentText("* Here you can view your feedback results for each finger.\n"
						+ 			"* You can move to the next finger or return by click on the Prev and Next buttons.");

				alert.showAndWait();
			}
			
			else if(filename.equals("FeedbackReview.fxml"))
			{
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Feedback View Help");
				alert.setHeaderText("Final Feedback Results");
				alert.setContentText("* Here you can view your final feedback results for each finger.\n"
						+ 			"* Your final score is shown in the last progress indicator.");

				alert.showAndWait();
			}
		}
		
		if(mItem == aboutMenu)
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("About");
			alert.setHeaderText("About");
			alert.setContentText("Developed by Maor Ohana and Shalev Lankri. \n"+
			"Final Project, July 2017 ,Ort Braude College.");

			alert.showAndWait();
		}
		
		if(mItem == backToMenu)
		{
			 Stage stage = (Stage) ap.getScene().getWindow();
			 Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainView.fxml"));
			 Scene scene = new Scene(root);
		     stage.setScene(scene);
		     if(taskRecognize != null &&  taskRecording != null)
		     {
		    	 taskRecognize.cancel(true);
		         taskRecording.cancel(true);
		     }

		}
	 }
	
	/**
	 * Creating Fade transition between views in a duration of 1500ms.
	 * @param e
	 */
	 protected void fadeTransition(Node e){
	        FadeTransition x=new FadeTransition(new Duration(1500),e);
	        x.setFromValue(0);
	        x.setToValue(100);
	        x.setCycleCount(1);
	        x.setInterpolator(Interpolator.LINEAR);
	        x.play();
	    }


}
