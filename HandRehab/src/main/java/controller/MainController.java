package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Main Controller Class 
 * Handle the actions from the Main Menu view, extends TrainingController.
 * @author maor
 *
 */
public class MainController extends GController implements Initializable {
	
	@FXML
	Button rehabButton;
	
	@FXML
	Button trainingButton;
	
	@FXML
	Button feedbackButton;

	@FXML
	ImageView bg;
	
	@FXML
	Label label;
	 
	
	/**
	 * Handle the ActionEvent from the Training button option.
	 * @param event 
	 * @throws IOException
	 */
	  @FXML
	 public void trainingButton(ActionEvent event) throws IOException
	 {
		 Stage stage = (Stage) trainingButton.getScene().getWindow();
		 Parent root = FXMLLoader.load(getClass().getResource("/fxml/TrainingView.fxml"));
		 Scene scene = new Scene(root);
	     stage.setScene(scene);

	 }
	  
	  /**
	   * Handle the ActionEvent from the Rehabilitation button option.
	   * @param event
	   * @throws IOException
	   */
	  @FXML
	  public void RehabButton(ActionEvent event) throws IOException
	  {
		 Stage stage = (Stage) rehabButton.getScene().getWindow();
		 Parent root = FXMLLoader.load(getClass().getResource("/fxml/RehabView.fxml"));
		 Scene scene = new Scene(root);
		 stage.setScene(scene);
	  }
	  
	  /**
	   * Handle the ActionEvent from the Feedback button option.
	   * @param event
	   * @throws IOException
	   */
	  @FXML
	  public void feedbackButton(ActionEvent event) throws IOException
	  {
		  	Stage stage = (Stage) feedbackButton.getScene().getWindow();
			 Parent root = FXMLLoader.load(getClass().getResource("/fxml/FeedbackView.fxml"));
			 Scene scene = new Scene(root);
			 stage.setScene(scene);
	  }



}
