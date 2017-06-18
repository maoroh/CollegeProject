package com.GUI;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.leap.Feedback;
import com.leap.MovementPattern;
import com.leapmotion.leap.Finger;
import com.tools.JAXBTools;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.chart.XYChart;
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
	private NumberAxis yAxis ;
	
	@FXML
	private TextArea textArea;
	 
	private int fingerID;
	
	 MovementPattern trainMP,rehabMP;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	   fingerID = 0;
        //populating the series with data
	    trainMP = JAXBTools.getPatternFromXML("trainMP.xml");
	    rehabMP = JAXBTools.getPatternFromXML("rehabMP.xml");
	    buildChart(fingerID);
	    lineChart.setId("chart1");
	   
       	//yAxis.setAutoRanging(false);
       	//yAxis.setTickUnit(0.1);
       	//yAxis.setLowerBound(-2);
	   	//yAxis.setUpperBound(2);
	    
	   
      
       
       
       
	}
	
	@FXML
	 public void nextFingerChart(ActionEvent event) throws IOException
	 {
		if(fingerID < 4)
			buildChart(++fingerID);
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
		textArea.clear();
		textArea.appendText("1 : " + Feedback.getScore2(fingerID, trainMP, rehabMP)[0] + "\n" +
				"2 : " +Feedback.getScore2(fingerID, trainMP, rehabMP)[1] + "\n"+ "3 : " + Feedback.getScore2(fingerID, trainMP, rehabMP)[2] + "\n" +
		"4 : " +Feedback.getScore2(fingerID, trainMP, rehabMP)[3]);
		lineChart.getData().remove(0,(lineChart.getData().size()));
		lineChart.setTitle(fingerNames.get(fingerID).name());
		XYChart.Series<Number,Number> seriesRehab = new XYChart.Series<Number,Number>();
		seriesRehab.setName("Rehab");
		XYChart.Series<Number,Number> seriesTrain = new XYChart.Series<Number,Number>();
		seriesTrain.setName("Training");
		MovementPattern dataRehab = JAXBTools.getDataXML("propRehab");
		int size = dataRehab.getSize();
		MovementPattern dataTraining  = JAXBTools.getDataXML("propTraining");
		
		for (int i = 0; i < size  ; i++)
		{
			double angle1 = rehabMP.getVector(i).getCoordinate(fingerID);
			angle1 = angle1 * (180/ Math.PI);
			double angle2 = trainMP.getVector(i).getCoordinate(fingerID);
			angle2 = angle2 * (180/ Math.PI);
			addDataToSeries(seriesRehab, i , angle1);
			addDataToSeries(seriesTrain, i , angle2);
		}
		
		 lineChart.getData().addAll(seriesRehab, seriesTrain);
	}
	
	
	

}