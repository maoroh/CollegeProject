package com.tools;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.leap.Data;
import com.leap.DataVector;
import com.leap.MovementPattern;
import com.leap.SampleSet;



public class JAXBTools {
	
	public static final Map <String , String> filePaths;
	static
		  {
				filePaths = new HashMap<String, String>();
				filePaths.put("propRehab", "propRehab.xml");
				filePaths.put("propTraining", "propTraining.xml");
		   }
	
	
	public static void savePatternXML(MovementPattern movementPattern , String name) 
	{
    	JAXBContext jaxbContext;
    	Marshaller jaxbMarshaller;
		try {
			 jaxbContext = JAXBContext.newInstance(MovementPattern.class);
		     jaxbMarshaller = jaxbContext.createMarshaller();
		     jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		     jaxbMarshaller.marshal(movementPattern, System.out);
		     jaxbMarshaller.marshal(movementPattern, new File(name));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
 
	}
	
	public static void saveDataXML(Data data , String name) 
	{
    	JAXBContext jaxbContext;
    	Marshaller jaxbMarshaller;
		try {
			 jaxbContext = JAXBContext.newInstance(Data.class);
		     jaxbMarshaller = jaxbContext.createMarshaller();
		     jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		     jaxbMarshaller.marshal(data, new File(name));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
 
	}
	
	public static Data getDataFromXML() 
	{
		JAXBContext jaxbContext;
		Data data = null;
		try {
			 jaxbContext = JAXBContext.newInstance(Data.class);
		     Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();  
		     File file = new File("data.xml");
		     if(file.exists())
		    	 data = (Data) jaxbUnmarshaller.unmarshal( new File("data.xml") );
		     else data = null;
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return data;
	}
	
	
	public static void saveSampleSetXML(SampleSet sampleSet , String name) 
	{
    	JAXBContext jaxbContext;
    	Marshaller jaxbMarshaller;
		try {
			 jaxbContext = JAXBContext.newInstance(SampleSet.class);
		     jaxbMarshaller = jaxbContext.createMarshaller();
		     jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		     //jaxbMarshaller.marshal(sampleSet, System.out);
		     jaxbMarshaller.marshal(sampleSet, new File(name));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
 
	}
	
	
	public static MovementPattern getPatternFromXML() 
	{
		JAXBContext jaxbContext;
		MovementPattern movementPattern = null;
		try {
			 jaxbContext = JAXBContext.newInstance(MovementPattern.class);
		     Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();   
		     movementPattern = (MovementPattern) jaxbUnmarshaller.unmarshal( new File("pattern.xml") );
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return movementPattern;
	}
	
	public static SampleSet getTrainingFromXML() 
	{
		JAXBContext jaxbContext;
		SampleSet sampleSet = null;
		try {
			 jaxbContext = JAXBContext.newInstance(SampleSet.class);
		     Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();   
		     sampleSet = (SampleSet) jaxbUnmarshaller.unmarshal( new File("trainData.xml") );
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return sampleSet;
	}
	
	
	public static MovementPattern getDataXML(String name) 
	{
		JAXBContext jaxbContext;
		MovementPattern mp = null;
		try {
			 jaxbContext = JAXBContext.newInstance(MovementPattern.class);
		     Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();   
		     mp = (MovementPattern) jaxbUnmarshaller.unmarshal( new File(filePaths.get(name)) );
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return mp;
	}
}


