package entity;

import java.util.ArrayList;

/**
 * Feedback 
 * Stores the feedback scores in a list.
 * @author maor
 *
 */
public class Feedback {
	
	private ArrayList <double [] > feedbackScores;
	
	/**
	 * Initialize new Feedback.
	 * @param feedbackScores - the feedback scores list.
	 */
	public Feedback(ArrayList <double [] > feedbackScores)
	{
		this.feedbackScores = feedbackScores;
	}
	
	/**
	 * Get the feedback according the fingerID.
	 * @param fingerID - the fingerID.
	 * @return
	 */
	public double [] getFeedback (int fingerID)
	{
		return this.feedbackScores.get(fingerID);
	}

	/**
	 * Size getter.
	 * @return the size of the feedbackScores.
	 */
	public int getSize() {
		// TODO Auto-generated method stub
		return this.feedbackScores.size();
	}

}
