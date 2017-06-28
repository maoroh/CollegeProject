package utility;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import entity.DataVector;
import entity.InitialData;
import entity.MovementPattern;
import entity.SampleSet;



public class JAXBTools {

	
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
	
	
	public static MovementPattern getPatternFromXML(String path) 
	{
		JAXBContext jaxbContext;
		MovementPattern movementPattern = null;
		try {
			 jaxbContext = JAXBContext.newInstance(MovementPattern.class);
		     Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();   
		     movementPattern = (MovementPattern) jaxbUnmarshaller.unmarshal( new File(path) );
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
	

}


