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


/**
 * XML Utilty class using JAXB Java Library.
 * @author maor
 *
 */
public class JAXBTools {

	
	/**
	 * Saving a MovementPattern object in a XML File format.
	 * @param movementPattern - the MovementPattern object.
	 * @param name - the name of the file.
	 */
	public static void savePatternXML(MovementPattern movementPattern , String name) 
	{
    	JAXBContext jaxbContext;
    	Marshaller jaxbMarshaller;
		try {
			 jaxbContext = JAXBContext.newInstance(MovementPattern.class);
		     jaxbMarshaller = jaxbContext.createMarshaller();
		     jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		     jaxbMarshaller.marshal(movementPattern, new File(name));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
 
	}
	
	
	/**
	 * Saving sample set data in a XML File format.
	 * @param sampleSet - the SampleSet object.
	 * @param name - the name of the file.
	 */
	public static void saveSampleSetXML(SampleSet sampleSet , String name) 
	{
    	JAXBContext jaxbContext;
    	Marshaller jaxbMarshaller;
		try {
			 jaxbContext = JAXBContext.newInstance(SampleSet.class);
		     jaxbMarshaller = jaxbContext.createMarshaller();
		     jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		     jaxbMarshaller.marshal(sampleSet, new File(name));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
 
	}
	
	
	/**
	 * Get MovementPattern object from XML File.
	 * @param path - The path of the XML File.
	 * @return MovementPattern object.
	 */
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
	
	/**
	 * Get SampleSet object from XML File.
	 * @return SampleSet object.
	 */
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


