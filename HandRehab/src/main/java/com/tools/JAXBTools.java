package com.tools;
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.leap.MovementPattern;
import com.leap.SampleSet;
import com.leap.test;


public class JAXBTools {
	
	public static void savePatternXML(MovementPattern movementPattern) 
	{
    	JAXBContext jaxbContext;
    	Marshaller jaxbMarshaller;
		try {
			 jaxbContext = JAXBContext.newInstance(MovementPattern.class);
		     jaxbMarshaller = jaxbContext.createMarshaller();
		     jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		     jaxbMarshaller.marshal(movementPattern, System.out);
		     jaxbMarshaller.marshal(movementPattern, new File("pattern.xml"));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
 
	}
	
	public static void saveSampleSetXML(SampleSet sampleSet) 
	{
    	JAXBContext jaxbContext;
    	Marshaller jaxbMarshaller;
		try {
			 jaxbContext = JAXBContext.newInstance(SampleSet.class);
		     jaxbMarshaller = jaxbContext.createMarshaller();
		     jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		     //jaxbMarshaller.marshal(sampleSet, System.out);
		     jaxbMarshaller.marshal(sampleSet, new File("samples.xml"));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
 
	}
	
	public static void saveSampleSetXML(test Test) 
	{
    	JAXBContext jaxbContext;
    	Marshaller jaxbMarshaller;
		try {
			 jaxbContext = JAXBContext.newInstance(test.class);
		     jaxbMarshaller = jaxbContext.createMarshaller();
		     jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		     //jaxbMarshaller.marshal(sampleSet, System.out);
		     jaxbMarshaller.marshal(Test, new File("test.xml"));
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
		     sampleSet = (SampleSet) jaxbUnmarshaller.unmarshal( new File("samples.xml") );
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return sampleSet;
	}
}


