import java.io.IOException;

/*
 * This class creates multiple instances of each  network and allows tests to be run one after another and have the reulsts neatly colected.
 * */
public class testAllNetworksQueue {
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		
		

		//NeuralNet firstFF = new NeuralNet(100,5,0.3,2,true);
		
	
		KNearestNeighbors firstkNN = new KNearestNeighbors(3,false,5,200);
		
		
		

		//sigmaSquaredSquared = 1000000; //:  Experiment with numbers for this value 1000000     (sigmaSquared=100000 gave 29.86%)   (sigmaSquared=1000000 gave 86.36% another 20 epochs or so brings it to 92% with leanring at 1)
		// For binary I am testing with sigam =  between 15-10 (11 seems optimal)
		RadialBasisFunction firstRBF = new RadialBasisFunction(20,false,1000000,5,0.5,false);
	
	
	
	
	}
		

}
