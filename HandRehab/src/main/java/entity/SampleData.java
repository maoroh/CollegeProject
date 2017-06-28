package entity;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * SampleData
 * Stores all sample data in a list of FrameData elements.
 * @author maor
 *
 */
@XmlRootElement(name = "SampleData")
@XmlAccessorType (XmlAccessType.FIELD)
public class SampleData {
	private ArrayList <FrameData> framesData;
	
	/**
	 * Initialize new empty SampleData object
	 */
	public SampleData()
	{
		this.framesData = new ArrayList<FrameData>();
	}
	
	/**
	 * Initialize new SampleData object.
	 * @param framesData - the frames list.
	 */
	public SampleData(ArrayList<FrameData> framesData)
	{
		this.framesData = framesData;
	}
	
	/**
	 * Add new FrameData to the list of frames.
	 * @param data - the FrameData object to add.
	 */
	public void addFrame(FrameData data)
	{
		this.framesData.add(data);
	}
	
	/**
	 * Get a frame in a specific position.
	 * @param index - the index of the frame.
	 * @return - FrameData in this position.
	 */
	public FrameData getFrame(int index)
	{
		return this.framesData.get(index);	
	}
	
	/**
	 * Creates a list of angles vector that contains all the frames angles vectors of this sample.
	 * @return a list of angles vector.
	 */
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
	
	/**
	 * Get size of frames list.
	 * @return the size of frames list.
	 */
	public int getNumOfFrames()
	{
		return this.framesData.size();
	}

	/**
	 * Set a FrameData in a specific position.
	 * @param frame - the FrameData object to set.
	 * @param index - the position.
	 */
	public void setFrame(FrameData frame, int index) {
		// TODO Auto-generated method stub
		this.framesData.set(index, frame);
	}
	
	/**
	 * Delete a frame in a specific position.
	 * @param index - the position of the frame.
	 */
	public void deleteFrame(int index) {
		// TODO Auto-generated method stub
		this.framesData.remove(index);
	}
}
