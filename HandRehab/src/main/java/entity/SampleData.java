package entity;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SampleData")
@XmlAccessorType (XmlAccessType.FIELD)
public class SampleData {
	private ArrayList <FrameData> framesData;
	
	public SampleData()
	{
		this.framesData = new ArrayList<FrameData>();
	}
	
	public SampleData(ArrayList<FrameData> framesData)
	{
		this.setFrames(framesData);
	}
	
	public void addFrame(FrameData data)
	{
		this.framesData.add(data);
	}
	
	public FrameData getFrame(int index)
	{
		return this.framesData.get(index);	
	}
	
	public ArrayList <DataVector> getSamplesVector()
	{
		ArrayList<DataVector> vectors = new ArrayList<DataVector>();
		
		for(FrameData data : this.framesData)
		{
			data.setAnglesVector();
			vectors.add(data.getAnglesVector());
		}
		
		return vectors;
	}
	
	
	public void setFrames(ArrayList<FrameData> framesData)
	{
		this.framesData = framesData;
	}
	
	public int getNumOfFrames()
	{
		return this.framesData.size();
	}

	public void setFrame(FrameData frame, int index) {
		// TODO Auto-generated method stub
		this.framesData.set(index, frame);
	}
	
	public void deleteFrame(int index) {
		// TODO Auto-generated method stub
		this.framesData.remove(index);
	}
}
