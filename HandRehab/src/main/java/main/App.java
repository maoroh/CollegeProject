package main;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class App extends Application {

	@Override
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/MainView.fxml"));
		Scene scene = new Scene(root);
	    primaryStage.setScene(scene);
	    primaryStage.show();
		
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
