package entity;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * SampleSet.
 * Stores all the samples data in a list of SampleData.
 * @author maor
 *
 */
@XmlRootElement(name = "SampleSet")
@XmlAccessorType (XmlAccessType.FIELD)
public class SampleSet {
	
	private ArrayList <SampleData> samplesSet;
	
	/**
	 * Initialize new SampleSet object.
	 */
	public SampleSet()
	{
		this.samplesSet = new ArrayList<SampleData>();
	}
	
	/**
	 * Add new sample to the samples list.
	 * @param data - the SampleData object object to add.
	 */
	public void addSample(SampleData data)
	{
		this.samplesSet.add(data);
	}
	
	/**
	 * Get a sample in a specific position.
	 * @param index - the position of the frame.
	 * @return the sample in this position.
	 */
	public SampleData getSample(int index)
	{
		return this.samplesSet.get(index);
	}
	
	/**
	 * Get the size of the samples list.
	 * @return the size of the samples list.
	 */
	public int getSize()
	{
		return this.samplesSet.size();
	}

}
