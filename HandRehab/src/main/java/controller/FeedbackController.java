package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.leapmotion.leap.Finger;

import entity.Feedback;
import entity.MovementPattern;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utility.AnalyzeData;
import utility.JAXBTools;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;

/**
 * Feedback Controller class.
 * Handle the actions from the Feedback view, extends Gcontroller.
 * @author maor
 *
 */
public class FeedbackController extends GController implements Initializable {

	public static final Map <Integer ,  String> fingerNames;
	static
		  {
			fingerNames = new HashMap<Integer, String>();
			fingerNames.put(0, "Thumb Finger");
			fingerNames.put(1, "Index Finger");
			fingerNames.put(2, "Middle Finger");
			fingerNames.put(3, "Ring Finger");
			fingerNames.put(4, "Pinky Finger");
		
		   }
	
	
	@FXML
	private LineChart<Number,Number> lineChart;
	
	@FXML
	private Button nextFingerBtn;
	
	@FXML
	private Button prevFingerBtn;
	
	@FXML
	private NumberAxis xAxis ;
	
	@FXML
	private ProgressIndicator thumbProgress;
	
	@FXML
	private ProgressIndicator indexProgress;
	
	@FXML
	private ProgressIndicator middleProgress;
	
	@FXML
	private ProgressIndicator ringProgress;
	
	@FXML
	private ProgressIndicator pinkyProgress;
	
	@FXML
	private ProgressIndicator finalProgress;
	
	@FXML
	private NumberAxis yAxis ;
	
	@FXML
	private TextArea textArea;
	 
	private int fingerID;
	
	MovementPattern trainMP,rehabMP;
	
	Feedback feedbackScores;
	
	 VBox box ;
	
	/**
	 * Initialize all class members.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		fadeTransition(ap);
		String filename = location.getFile().substring(location.getFile().lastIndexOf('/')+1, location.getFile().length());
	    fingerID = 0;
	   
	    if(filename.equals("FeedbackView.fxml"))
	    {
	    	loadData(e -> {
	    		
	    		ap.getChildren().remove(box);
				ap.setDisable(false);
				buildChart(fingerID);});
	    }
	    else 
	    {
	    	loadData(e -> {
	    		ap.getChildren().remove(box);
				ap.setDisable(false);
				showFinalScores();});
	    
	    }
	  
	}
	
	/**
	 * This method creates JavaFX task for reading the trainMP,rehabMP XML files and generating the feedback into
	 *  feedbackScores list.
	 * @param eventHandler - the handler to rum after the task is finished.
	 */
	private void loadData(EventHandler<WorkerStateEvent> eventHandler) {
		// TODO Auto-generated method stub
		 ProgressIndicator pi = new ProgressIndicator();
		 box = new VBox(pi);
         box.setAlignment(Pos.BOTTOM_CENTER);
         box.setLayoutX(350);
         box.setLayoutY(200);
         // Grey Background
         ap.setDisable(true);
         ap.getChildren().add(box);
		
		
		Task<Void> getResults = new Task<Void>(){

			@Override
			protected Void call() throws Exception {
				// TODO Auto-generated method stub
				 	trainMP = JAXBTools.getPatternFromXML("trainMP.xml");
				 	updateProgress(1, 3);
				    rehabMP = JAXBTools.getPatternFromXML("rehabMP.xml");
				    updateProgress(2, 3);
				    feedbackScores = AnalyzeData.generateFeedback(trainMP, rehabMP);
				    updateProgress(3, 3);
					return null;
			}
			
		};
		pi.progressProperty().bind(getResults.progressProperty());
		
		
		new Thread(getResults).start();
		
		getResults.setOnSucceeded(eventHandler);
		
	}

	/**
	 * This method change the line Chart to the next finger data chart.
	 * @param event - ActionEvent object.
	 * @throws IOException
	 */
	@FXML
	 public void nextFingerChart(ActionEvent event) throws IOException
	 {
		 if(fingerID == 4)
		 {
			showReview();
		}
		
		if(fingerID < 4)
			buildChart(++fingerID);
		
	
		
	 }
	
	/**
	 * This method loads the final results view and replacing the Scene with FeedbackReview FXML file.
	 * @param event - ActionEvent object.
	 * @throws IOException
	 */
	private void showReview() throws IOException
	{
		 Stage stage = (Stage) nextFingerBtn.getScene().getWindow();
		 Parent root = FXMLLoader.load(getClass().getResource("/fxml/FeedbackReview.fxml"));
		 Scene scene = new Scene(root);
		 stage.setScene(scene);
	}

	/**
	 * This method change the line Chart to the previous finger data chart.
	 * @param event - ActionEvent object.
	 * @throws IOException
	 */
	@FXML
	 public void prevFingerChart(ActionEvent event) throws IOException
	 {
		if(fingerID > 0)
			buildChart(--fingerID);
	 }
	
	/**
	 * Add new point to the line chart.
	 * @param series - Rehabilitation or Training series.
	 * @param x - x value.
	 * @param y - y value.
	 */
	public void addDataToSeries(XYChart.Series<Number,Number> series, int x, double y)
	{
		 series.getData().add(new XYChart.Data<Number,Number>(x,y));
	}
	
	
	/**
	 * Build the line chart according to the finger id.
	 * @param fingerID - the fingerID value (0-4).
	 */
	@SuppressWarnings("unchecked")
	public void buildChart(int fingerID)
	{
		 if(fingerID == 4)
				nextFingerBtn.setText("View Review");
		 
		textArea.clear();
		double [] feedback = feedbackScores.getFeedback(fingerID);
		textArea.appendText("Matching percentage by the parts of the movement: \n");
		textArea.appendText("First Part : " + String.format("%.2f" ,feedback[0]) + "%"  +  "\n" +
				"Second Part : " +String.format("%.2f" ,feedback[1]) + "%" + "\n"+ "Third Part : " +String.format("%.2f" ,feedback[2])+ "%" + "\n" + "Fourth Part : " +String.format("%.2f" ,feedback[3])+ "%" );
		lineChart.getData().remove(0,(lineChart.getData().size()));
		lineChart.setTitle(fingerNames.get(fingerID));
		XYChart.Series<Number,Number> seriesRehab = new XYChart.Series<Number,Number>();
		seriesRehab.setName("Rehab");
		XYChart.Series<Number,Number> seriesTrain = new XYChart.Series<Number,Number>();
		seriesTrain.setName("Training");
		int size = trainMP.getSize();
		
		 
		lineChart.getData().addAll(seriesRehab, seriesTrain);
		
		for (int i = 0; i < size  ; i++)
		{	
			double angle1 = rehabMP.getVector(i).getCoordinate(fingerID);
			angle1 = angle1 * (180/ Math.PI);
			double angle2 = trainMP.getVector(i).getCoordinate(fingerID);
			angle2 = angle2 * (180/ Math.PI);
			addDataToSeries(seriesRehab, i , angle1);
			addDataToSeries(seriesTrain, i , angle2);
		}
		seriesTrain.getNode().setStyle("-fx-stroke:#00cd00");
		seriesRehab.getNode().setStyle("-fx-stroke:#fa0000");
		
	     lineChart.setCreateSymbols(false);	
	    
	}
	
	/**
	 * Update progress indicators of the final scores according to the feedback.
	 */
	private void showFinalScores()
	{
		double [] fingerScore = new double[5];
		int index = 0;
		for (int i = 0 ; i < feedbackScores.getSize(); i++ )
		{
			double [] score = feedbackScores.getFeedback(i);
			fingerScore[index] = 0;
			for(int j = 0 ; j  < score.length ; j++)
			{
				fingerScore[index] += score[j];
			}
			fingerScore[index] = fingerScore[index] / 4;
			index ++ ;
		}
	
		setIndicatorStyle(thumbProgress,fingerScore[0]);
		setIndicatorStyle(indexProgress,fingerScore[1]);
		setIndicatorStyle(middleProgress,fingerScore[2]);
		setIndicatorStyle(ringProgress,fingerScore[3]);
		setIndicatorStyle(pinkyProgress,fingerScore[4]);
		setIndicatorStyle(finalProgress,(fingerScore[0]+fingerScore[1]+fingerScore[2]+fingerScore[3]+fingerScore[4]) / 5);
	
		
	}
	
	/**
	 * Set style and progress of indicator.
	 * @param indicator - The indicator object.
	 * @param progress - the progress of the indicator according to the feedback.
	 */
	private void setIndicatorStyle(ProgressIndicator indicator , double progress)
	{
		if(progress > 0 && progress < 25)
			indicator.setStyle(" -fx-progress-color: red;");
		else if (progress >= 25 && progress < 50)
			indicator.setStyle(" -fx-progress-color: #f1ea00;");
		else if (progress >= 50 && progress < 75)
			indicator.setStyle(" -fx-progress-color: orange;");
		else if (progress >= 75 && progress < 100)
			indicator.setStyle(" -fx-progress-color: green;");
		
		indicator.setProgress(progress/100);
	}
	
	
	

}
