package com.GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TrainingController implements Initializable{
	
	@FXML
	Button button;
	
	@FXML
	TextArea textArea;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		textArea.appendText("TEST");
      
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
	        dialog.setScene(dialogScene);
	        dialog.show();
		 }
	

}
