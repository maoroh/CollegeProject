package com.GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.leap.Feedback;
import com.leap.MovementPattern;
import com.leapmotion.leap.Finger;
import com.tools.JAXBTools;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;


@SuppressWarnings("unchecked")
public class FeedbackController extends GController implements Initializable {

	public static final Map <Integer ,  Finger.Type> fingerNames;
	static
		  {
			fingerNames = new HashMap<Integer, Finger.Type>();
			fingerNames.put(0, Finger.Type.values()[0]);
			fingerNames.put(1, Finger.Type.values()[1]);
			fingerNames.put(2, Finger.Type.values()[2]);
			fingerNames.put(3, Finger.Type.values()[3]);
			fingerNames.put(4, Finger.Type.values()[4]);
		
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
	
	ArrayList <double [] > feedbackScores;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		fadeTransition(ap);
		String filename = location.getFile().substring(location.getFile().lastIndexOf('/')+1, location.getFile().length());
	    fingerID = 0;
	    trainMP = JAXBTools.getPatternFromXML("trainMP.xml");
	    rehabMP = JAXBTools.getPatternFromXML("rehabMP.xml");
	   
	    feedbackScores = Feedback.getScore(trainMP, rehabMP);
	    if(filename.equals("FeedbackView.fxml"))
	    {
	    buildChart(fingerID);
	    lineChart.setId("chart1");
	    }
	    else 
	    {
	    	
	    	showFinalScores();
	    }
	  
	}
	
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
	
	
	private void showReview() throws IOException
	{
		 Stage stage = (Stage) nextFingerBtn.getScene().getWindow();
		 Parent root = FXMLLoader.load(getClass().getResource("FeedbackReview.fxml"));
		 Scene scene = new Scene(root);
		 stage.setScene(scene);
	}

	@FXML
	 public void prevFingerChart(ActionEvent event) throws IOException
	 {
		if(fingerID > 0)
			buildChart(--fingerID);
	 }
	
	public void addDataToSeries(XYChart.Series<Number,Number> series, int x, double y)
	{
		 series.getData().add(new XYChart.Data<Number,Number>(x,y));
	}
	
	
	public void buildChart(int fingerID)
	{
		 if(fingerID == 4)
				nextFingerBtn.setText("View Review");
		 
		textArea.clear();
		double [] feedback = feedbackScores.get(fingerID);
		textArea.appendText("1 : " + feedback[0] + "\n" +
				"2 : " +feedback[1] + "\n"+ "3 : " +feedback[2] + "\n" + "4 : " +feedback[3]);
		lineChart.getData().remove(0,(lineChart.getData().size()));
		lineChart.setTitle(fingerNames.get(fingerID).name());
		XYChart.Series<Number,Number> seriesRehab = new XYChart.Series<Number,Number>();
		seriesRehab.setName("Rehab");
		XYChart.Series<Number,Number> seriesTrain = new XYChart.Series<Number,Number>();
		
		seriesTrain.setName("Training");
		MovementPattern dataRehab = JAXBTools.getDataXML("propRehab");
		int size = dataRehab.getSize();
		MovementPattern dataTraining  = JAXBTools.getDataXML("propTraining");
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
	
	private void showFinalScores()
	{
		double [] fingerScore = new double[5];
		int index = 0;
		for (double [] score: feedbackScores)
		{
			fingerScore[index] = 0;
			for(int i = 0 ; i < score.length ; i++)
			{
				fingerScore[index] += score[i];
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
	
	private void setIndicatorStyle(ProgressIndicator indicator , double progress)
	{
		if(progress > 0 && progress < 25)
			indicator.setStyle(" -fx-progress-color: red;");
		else if (progress >= 25 && progress < 50)
			indicator.setStyle(" -fx-progress-color: yellow;");
		else if (progress >= 50 && progress < 75)
			indicator.setStyle(" -fx-progress-color: orange;");
		else if (progress >= 75 && progress < 100)
			indicator.setStyle(" -fx-progress-color: green;");
		
		indicator.setProgress(progress/100);
	}
	
	
	

}
