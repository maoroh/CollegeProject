package entity;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SampleSet")
@XmlAccessorType (XmlAccessType.FIELD)
public class SampleSet {
	
	private ArrayList <SampleData> samplesSet;
	
	public SampleSet()
	{
		this.samplesSet = new ArrayList<SampleData>();
	}
	
	public void addSample(SampleData data)
	{
		this.samplesSet.add(data);
	}
	
	public SampleData getSample(int index)
	{
		return this.samplesSet.get(index);
	}
	
	public int getSize()
	{
		return this.samplesSet.size();
	}

}
