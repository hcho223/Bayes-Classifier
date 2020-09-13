import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 *  Implementation of a naive bayes classifier
 * @author Hyung Rae, Cho
 *
 */
public class NaiveBayesClassifier implements Classifier {

	// Global variable for train
	private List<Instance> trainData;
	// Word space size
	private double vocSize;
	// Map of documents counts
	private Map<Label,Integer> docsCount;
	// Map of words counts
	private Map<Label,Integer> wordsCount;
	// Individual words occurrence with positive
	private HashMap<String,Integer> posiWordOccur;
	// Individual words occurrence with negative
	private HashMap<String,Integer> negWordOccur;
	/**
	 * Trains the classifier with the provided training data and vocabulary size
	 * @param trainData Train data
	 * @param v The size of the word space
	 */
    @Override
    public void train(List<Instance> trainData, int v) {
    	// Set to global variable
    	this.trainData=trainData;
    	this.vocSize=Double.valueOf(v);
    	this.docsCount=this.getDocumentsCountPerLabel(trainData);
    	this.wordsCount=this.getWordsCountPerLabel(trainData);
    	this.posiWordOccur=new HashMap<String,Integer>();
    	this.negWordOccur=new HashMap<String,Integer>();
    	//
    	for(int i=0;i<trainData.size();i++)
    	{
    		Instance indiInstance=trainData.get(i);
    		if(indiInstance.label==Label.POSITIVE)
    		{
    			for(int i1=0;i1<indiInstance.words.size();i1++)
    			{
    				String indiWord= indiInstance.words.get(i1);
    				if(posiWordOccur.get(indiWord)==null)
    				{
    					posiWordOccur.put(indiWord, 1);
    				}
    				else
    				{
    					posiWordOccur.put(indiWord, posiWordOccur.get(indiWord)+1);
    				}
    				
    			}
    		}
    		else
    		{
    			for(int i1=0;i1<indiInstance.words.size();i1++)
    			{
    				String indiWord= indiInstance.words.get(i1);
    				if(negWordOccur.get(indiWord)==null)
    				{
    					negWordOccur.put(indiWord, 1);
    				}
    				else
    				{
    					negWordOccur.put(indiWord, negWordOccur.get(indiWord)+1);
    				}
    				
    			}
    		}
    	}
    }
    
    /**
     * Counts the number of words for each label
     * @param trainData list of intances
     * @return the map data for number of words counts per label
     */
    @Override
    public Map<Label, Integer> getWordsCountPerLabel(List<Instance> trainData) {
        // Initialize Map
    	HashMap<Label,Integer> hmap= new HashMap<Label,Integer>();
    	// Initialize positive number
    	int posiNum=0;
    	// Initialize nugative number
    	int negNum=0;
    	// For loop to iterate all train data
    	for(int i=0;i<trainData.size();i++)
    	{
    		// Individual instance
    		Instance indiInstance=trainData.get(i);
    		// Word list
    		List<String> indiList=indiInstance.words;
    		// Switch for label
    		switch (indiInstance.label)
    		{
    			// When label is POSITIVE
    			case POSITIVE:
    				posiNum+=indiList.size();
    				break;
    			// When label is NEGATIVE
    			case NEGATIVE:
    				negNum+=indiList.size();
    				break;
    			default:
    				System.out.println("Don't know");
    		}
    	}
    	// Put the data into map
    	hmap.put(Label.POSITIVE,posiNum);
    	hmap.put(Label.NEGATIVE,negNum);
        return hmap;
    }

    /**
     * Counts the number of documentss for each label
     * @param trainData list of intances
     * @return the map data for number of Documents counts per label
     */
    @Override
    public Map<Label, Integer> getDocumentsCountPerLabel(List<Instance> trainData) {
        // Initialize the hmap
    	HashMap<Label,Integer> hmap= new HashMap<Label,Integer>();
    	// POSITIVE COUNTER
    	int posiNum=0;
    	// NEGATIVE COUNTER
    	int negNum=0;
    	// For loop to iterate train data
    	for(int i=0;i<trainData.size();i++)
    	{
    		// Individual instance
    		Instance indiInstance=trainData.get(i);
    		// Check the label
    		switch (indiInstance.label)
    		{
    			// When label is POSITIVE
    			case POSITIVE:
    				posiNum=posiNum+1;
    				break;
    		    // When label is NEGATIVE
    			case NEGATIVE:
    				negNum=negNum+1;
    				break;
    				
    			default:
    				System.out.println("Don't know");
    		
    		}
    	}
    	// Put data into hmap
    	hmap.put(Label.POSITIVE,posiNum);
    	hmap.put(Label.NEGATIVE, negNum); 	
        return hmap;
    }

    /**
     * Returns the prior probability of the label parameter
     * @param label of the instance
     * @return The prior probability of the label parameter
     */
    private double p_l(Label label) {
        // TODO : Implement
    	
        // Calculate the probability for the label. No smoothing here.
        // Just the number of label counts divided by the number of documents.
    	// Initialize prob
        double pl=0;
        // Get the number of documents in positive lable and negative label&total
        double posiNum=Double.valueOf(this.docsCount.get(Label.POSITIVE));
        double negNum=Double.valueOf(this.docsCount.get(Label.NEGATIVE));
        double total= posiNum+negNum;
        
        switch(label)
        {
        	// When the given label is positive
        	case POSITIVE:
        		pl=posiNum/total;
        		break;
        	// When the given label is negative
        	case NEGATIVE:
        		pl=negNum/total;
        		break;
        	default:
        		System.out.println("Time out");
        }
    	return pl;
    }

    /**
     * Returns the smoothed conditional probability of the word given the label
     * @param word Given probability
     * @param label of the instance
     * @return The smoothed conditional probability of the label parameter
     */
    private double p_w_given_l(String word, Label label) {
        // TODO : Implement
        // Calculate the probability with Laplace smoothing for word in class(label)
    	// Get Cl(w)
    	double clw=0;
        // When the label is POSITIVE
    	if(label==Label.POSITIVE)
    	{
    		// When the word was not occurred in the training set
    		if(this.posiWordOccur.get(word)==null)
    		{
    			
    		}
    		// Otherwise
    		else 
    		{
    			// Get the occurence count
    			clw=Double.valueOf(this.posiWordOccur.get(word));
    		}
    	}
    	// NEGATIVE
    	else
    	{
    		// When the word was not occurred in the training set
    		if(this.negWordOccur.get(word)==null)
    		{
    			
    		}
    		// Otherwise
    		else
    		{
    			// Get the occurence count
    			clw=Double.valueOf(this.negWordOccur.get(word));
    		}
    	}
    	// Get sigmaClv
    	double sigmaClv=Double.valueOf(this.wordsCount.get(label));
    	// Calculate the smoothed conditional prob
    	double pwl=(clw+1)/(sigmaClv+1*vocSize);
    	// Return probability
        return pwl;
    }

    /**
     * Classifies an array of words as either POSITIVE or NEGATIVE.
     * @param words the list of words
     * @return ClassifyResult with log probability and label guess
     */
    @Override
    public ClassifyResult classify(List<String> words) {
        // Get prior probability of each label
    	double logProbPosi=Math.log(p_l(Label.POSITIVE));
    	double logProbNeg=Math.log(this.p_l(Label.NEGATIVE));
    	// For-loop to iterate all word list
    	for(int i1=0;i1<words.size();i1++)
    	{
    		// Get individual word
    		String indiWord=words.get(i1);
    		// Add individual smoothed conditional log probability for POSITIVE AND NEGATIVE 
    		logProbPosi=logProbPosi+Math.log(this.p_w_given_l(indiWord, Label.POSITIVE));
    		logProbNeg=logProbNeg+Math.log(this.p_w_given_l(indiWord, Label.NEGATIVE));
    	}
    	// Initialize result
    	ClassifyResult result = new ClassifyResult();
    	// When positive is smaller than negative
    	if(logProbPosi<logProbNeg)
    	{
    		result.label=Label.NEGATIVE;
    	}
    	// Otherwise
    	else
    	{
    		result.label=Label.POSITIVE;
    	}
    	// Initialize hashmap to store log probability
    	HashMap<Label,Double> logProbWLabel=new HashMap<Label,Double>();
    	logProbWLabel.put(Label.POSITIVE, logProbPosi);
    	logProbWLabel.put(Label.NEGATIVE, logProbNeg);
    	// Set the hash map to the result
    	result.logProbPerLabel=logProbWLabel;
        return result;
    }


}
