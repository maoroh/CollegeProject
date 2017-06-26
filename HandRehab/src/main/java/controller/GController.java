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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GController implements Initializable {

	@FXML
	private MenuItem closeChoice ;
	
	@FXML 
	protected AnchorPane ap;
	
	@FXML
	private MenuItem backToMenu ;
	
	protected Task<Void> taskRecognize;
	
	protected Task<Void> taskRecording;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	 public void menuAction(ActionEvent event) throws IOException
	 {
		MenuItem mItem = (MenuItem) event.getSource();
		if(mItem == closeChoice)
		{
			System.exit(0);
		}
		
		if(mItem == backToMenu)
		{
			 Stage stage = (Stage) ap.getScene().getWindow();
			 Parent root = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));
			 Scene scene = new Scene(root);
		     stage.setScene(scene);
		     if(taskRecognize != null &&  taskRecording != null)
		     {
		    	 taskRecognize.cancel(true);
		         taskRecording.cancel(true);
		     }

		}
	 }
	
	 protected void fadeTransition(Node e){
	        FadeTransition x=new FadeTransition(new Duration(1500),e);
	        x.setFromValue(0);
	        x.setToValue(100);
	        x.setCycleCount(1);
	        x.setInterpolator(Interpolator.LINEAR);
	        x.play();
	    }

}
