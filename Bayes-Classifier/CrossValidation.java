import java.util.ArrayList;
import java.util.List;

/**
 * This class does cross validation
 * @author Hyung Rae Cho
 *
 */
public class CrossValidation {
    /*
     * Returns the k-fold cross validation score of classifier clf on training data.
     */
	/**
	 * This method return k-fold cross validation score of classifier clf on the trainin data
	 * @param clf Classifier
	 * @param trainData The list of trainData
	 * @param k The number of folds
	 * @param v The number of word space
	 * @return The k-fold scores
	 */
    public static double kFoldScore(Classifier clf, List<Instance> trainData, int k, int v) {
        // ArrayList to divide train data into k folds
    	ArrayList<ArrayList<Instance>> folds= new ArrayList<>();
    	// For-loop to initialize arraylist
    	for(int i=0;i<k;i++)
    	{
    		folds.add(new ArrayList<Instance>());
    	}
    	// Convert to k into double k
    	double kd=Double.valueOf(k);
    	// Convert to n into double n
    	double nd=Double.valueOf(trainData.size());
    	// For-loop to put into correspond array
    	for(int i1=0;i1<trainData.size();i1++)
    	{
    		// Calculate index
    		int index = (int)Math.floor((i1)/(nd/kd));
    	    // Put data into fold
    		folds.get(index).add(trainData.get(i1));
    	}
    	// Initialize the correct counter
    	double correct=0;
    	// Initialize the wrong counter
    	double wrong=0;
    	// for-loop to do cross validation
    	for(int i2=0;i2<k;i2++)
    	{
    		// Arraylist for store traindata
    		ArrayList<Instance> trainFold = new ArrayList<Instance>();
    		// Arraylist for test
    		ArrayList<Instance> testFold = folds.get(i2);
    		// For loop to make traindata
    		for(int i3=0;i3<k;i3++)
    		{
    			if(i3!=i2)
    			{
    				trainFold.addAll(folds.get(i3));
    			}
    		}
    		// Train with train data
    		clf.train(trainFold,v);
    		// For-loop to test
    		for(int i4=0;i4<testFold.size();i4++)
    		{
    			// Individual instance
    			Instance indiInstance = testFold.get(i4);
    			// When the label is correct
    			if(indiInstance.label==clf.classify(indiInstance.words).label)
    			{
    				// Increment the correct count
    				correct++;
    				
    			}
    			// Otherwise
    			else
    			{
    				// Increment the wrong count
    				wrong++;
    				
    			}
    		}
    	}
    	// Return the score
        return (correct)/(correct+wrong);
    }
}
