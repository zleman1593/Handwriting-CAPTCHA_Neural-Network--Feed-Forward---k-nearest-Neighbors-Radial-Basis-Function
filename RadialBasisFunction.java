/*This is a Radial Basis Function Neural Network trained by gradient descent on the weights from the hidden layer to the output nodes*/

//Notes: This has only been tested on non thresholded images. Need to experiment with binary
//Notes: The way threads are being implemented, up to ten cores can be used. Any more will not provide an advantage as only ten threads can be created.
//If more than ten cores are available we could think about change the way we do multi-threading.

import java.util.*;
import java.io.IOException;
import java.lang.Math;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class RadialBasisFunction {

	// For a given training image this array is filled with the output for each
	// layer and then reset for the next image.
	// Prevents duplicate calculations from being performed.
	public static ArrayList<ArrayList<Double>> tempOutput = new ArrayList<ArrayList<Double>>();
	// The number of times the network is trained with the training Data
	public static int epochs;
	// Creates a random number generator
	public static Random random = new Random();
	// Tracks the number of images processed in the testing set.
	public static double countOfImagesAnalyzed = 0;
	// Tracks the number of images correctly identified in the testing set.
	public static double countOfCorrectImagesAnalyzed = 0;
	// Tracks running time of the hidden layer construction and training
	//of weights from the hidden layer to the output layer
	public static long executionTime;
	// The number of input nodes will be equal to the number of pixels in the image
	public static int numberOfInputNodes;
	// Create array of Nodes in first layer and output layer
	public static ArrayList<ArrayList<Double>> hiddenLayerNodes = new ArrayList<ArrayList<Double>>();
	public static ArrayList<ArrayList<Double>> outputLayerNodes = new ArrayList<ArrayList<Double>>();
	// The learning rate for the network
	public static double learningRate;
	// Whether to use weights that have already been trained or to train network again
	public static boolean usePriorWeights;
	//Dictates the square standard deviation in the gaussian RBF
	public static double sigmaSquared;
	// Number of output nodes (Currently the network depends on 10  or 36 output nodes)
	public static final int NUMBER_OF_OUTPUT_NODES = 10;//---------------------------------------------------------------------------------
	//File paths
	public static String filePathResults;
	public static String filePathTrainedOutputWeights;

	public static ArrayList<DigitImage> trainingData = new ArrayList<DigitImage>();

	// Is true if the input into the network consists of binary images. False if Grayscale.
	public static boolean binaryInput;
	// set to one to use all of the training data to train the network. The number of training examples  is divided by this number
	public  static int trainingSetReductionFactor;

	public static int[]  holder=new int[10];

	public RadialBasisFunction(int trainingSetReductionFactor1,boolean binaryInput1, int sigmaSquared1, int epochs1, double learningRate1, boolean usePriorWeights1, String filePathResults1 ,String filePathTrainedOutputWeights1 ) throws IOException, ClassNotFoundException{
		hiddenLayerNodes.clear();
		outputLayerNodes.clear();
		trainingData.clear();
		tempOutput.clear();
		
		binaryInput = binaryInput1;
		trainingSetReductionFactor = trainingSetReductionFactor1;

		sigmaSquared = sigmaSquared1;
		epochs = epochs1;
		learningRate = learningRate1;
		usePriorWeights = usePriorWeights1;
		filePathResults=filePathResults1;
		filePathTrainedOutputWeights=filePathTrainedOutputWeights1;
		
		String trainingImages = "Training-Images";
		String testingImages = "Testing-images";
		String trainingLabels = "Training-Labels";
		String testingLabels = "Testing-Labels";
		
		
		
		
		
		//Sets up an array that will allow us to keep track of the number of wrong guesses for each number
		for (int m = 0; m < holder.length; m++) {
			holder[m]=0;
		}
		System.out.println("There are " +Runtime.getRuntime().availableProcessors()+ " cores avalible to the JVM.");
		System.out.println("Intel hyperthreading can be responsible for the apparent doubling  in cores.");



		initializeRBF(trainingImages, trainingLabels);

		//After reaching 92.12% accuracy when training on 1000000 with learning rate of 1. I started testing on 700000 =>93.0%  500000=> less accurate%


	
			long startTime = System.currentTimeMillis();
			if (usePriorWeights) {
				readDataFromTrainedFiles();
			}//Only for testing purposes (allows breaks between training epochs) uncomment only when  usePriorWeights is false
			trainTheNetwork(trainingData);
			long endTime = System.currentTimeMillis();
			executionTime = endTime - startTime;
			System.out.println("Training time: " + executionTime + " milliseconds");
			writeTrainedWeights();

		



		// Test the  RBF Network
		testRBF(testingImages, testingLabels);

		for (int m = 0; m < holder.length; m++) {
			System.out.println("Number " + m+" was guessed " +holder[m]+ " times, when it should have guessed another number.");
		}
	}

	public static void initializeRBF(String trainingImages, String trainingLabels) throws IOException {

		// Loads training and testing data sets
		DigitImageLoadingService train = new DigitImageLoadingService(trainingLabels, trainingImages,binaryInput);
		trainingData = new ArrayList<DigitImage>();
		try {
			// Our data structure holds the training datagi
			trainingData = train.loadDigitImages();
			// Alters data into proper form
			if(NUMBER_OF_OUTPUT_NODES==10){
				for (int i = 0; i < trainingData.size(); i++) {
					trainingData.get(i).vectorizeTrainingData();
				}
			}
			else{
				for (int i = 0; i < trainingData.size(); i++) {
					trainingData.get(i).vectorizeTrainingDataAlphaNum();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Looks at a representation of an image
		// and determines how many pixels and thus how many input nodes are needed
		// (one per pixel)
		numberOfInputNodes = trainingData.get(0).getData().length;


		// Initialize weights with values corresponding to the binary pixel value for all nodes in the first hidden layer.
		// Currently dividing by 20 to only use a 2oth of the training set so we don't run out of memory. We likely don't need that many anyway.
		for (int i = 0; i < trainingData.size()/trainingSetReductionFactor; i++) {
			ArrayList<Double> weights = new ArrayList<Double>(numberOfInputNodes);
			weights = trainingData.get(i).getArrayListData();
			hiddenLayerNodes.add(weights);

		}


		// Initialize weights with random values for all nodes in the output
		// layer.
		for (int i = 0; i < NUMBER_OF_OUTPUT_NODES; i++) {
			ArrayList<Double> weights = new ArrayList<Double>();
			for (int j = 0; j <  trainingData.size()/trainingSetReductionFactor; j++) {
				weights.add(random.nextGaussian());
			}
			outputLayerNodes.add(weights);
		}



	}

	public static void testRBF(String testingImages, String testingLabels) throws IOException {
		long startTime = System.currentTimeMillis();
		countOfImagesAnalyzed=0;
		countOfCorrectImagesAnalyzed=0;
		// Loads testing data set
		DigitImageLoadingService test = new DigitImageLoadingService(testingLabels, testingImages,binaryInput);
		ArrayList<DigitImage> testingData = new ArrayList<DigitImage>();
		try {
			// Our data structure holds the testing data
			testingData = test.loadDigitImages();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Tests the network with the testing Data and prints results to file
		write(solveTestingData(testingData));
		// reports network Performance
		double percentCorrect = (countOfCorrectImagesAnalyzed / countOfImagesAnalyzed) * 100;
		System.out.println("Analyzed " + countOfImagesAnalyzed + " images with " + percentCorrect + " percent accuracy.");
		System.out.println("Look in " +filePathResults+  " directory to find  the output.");
		long endTime = System.currentTimeMillis();
		executionTime = endTime - startTime;
		System.out.println("Testing time: " + executionTime + " milliseconds");
	}

	/*
	 * Returns the output from a given node after the input has been summed.It takes the layer that the node is in, the index of the node in the
	 * layer, and the output from the previous layer
	 */
	public static double hiddenNodeOutput(ArrayList<ArrayList<Double>> layerOfNodes, ArrayList<Double> outputFromPreviousLayer, int indexOfNodeinlayer) {
		double sum = 0;
		for (int i = 0; i < outputFromPreviousLayer.size(); i++) {

			sum=sum+Math.pow(layerOfNodes.get(indexOfNodeinlayer).get(i) - outputFromPreviousLayer.get(i),2);

		}
		return Math.exp(-1*(sum/sigmaSquared));
	}

	public static double outputNodeOutput(ArrayList<ArrayList<Double>> layerOfNodes, ArrayList<Double> outputFromPreviousLayer, int indexOfNodeinlayer) {
		double sum = 0;
		for (int i = 0; i < outputFromPreviousLayer.size(); i++) {
			sum = sum + (layerOfNodes.get(indexOfNodeinlayer).get(i) * outputFromPreviousLayer.get(i));
		}
		return activationFunction(sum);
	}

	/* This returns an array representing the output of all nodes in the given layer */
	public static ArrayList<Double> outPutOfLayer(ArrayList<ArrayList<Double>> currentLayer, ArrayList<Double> outputFromPreviousLayer, int hidden) {
		ArrayList<Double> outputOfCurrentlayer = new ArrayList<Double>();
		for (int i = 0; i < currentLayer.size(); i++) {
			double output;
			if(hidden==1){
				output = hiddenNodeOutput(currentLayer, outputFromPreviousLayer, i);
			} else{
				output = outputNodeOutput(currentLayer, outputFromPreviousLayer, i);
			}
			outputOfCurrentlayer.add(output);
		}
		return outputOfCurrentlayer;
	}



	/*
	 * This takes the training data and 
	 * attempts to train the neural net to learn how to recognize characters from images.
	 * Duplicated code exists because threads do not allow parameters be passed to methods,
	 *  which would have allowed us to condense the code. Just read one thread.
	 */
	public static void trainTheNetwork(ArrayList<DigitImage> trainingData) {

		for (int i = 0; i < epochs; i++) { // for each epoch
			//for every image in the training file
			long startTime = System.currentTimeMillis();
			for (int images = 0; images < trainingData.size()/trainingSetReductionFactor; images++) { 

				networkOutputError(trainingData, images);
				
				
				//Creates 8 threads and splits the test set into eight parts each of which is handled by a seperate thread 
				Runnable r1 = new Runnable() {
					public void run() {
						// Update the weights to the output nodes
						trainingSubRoutine(0, NUMBER_OF_OUTPUT_NODES/8);

					}};

					Runnable r2 = new Runnable() {
						public void run() {
							// Update the weights to the output nodes
							trainingSubRoutine(NUMBER_OF_OUTPUT_NODES/8,NUMBER_OF_OUTPUT_NODES/4);

						}};

						Runnable r3 = new Runnable() {
							public void run() {
								// Update the weights to the output nodes
								trainingSubRoutine(NUMBER_OF_OUTPUT_NODES/4,(NUMBER_OF_OUTPUT_NODES*3)/8);

							}};

							Runnable r4  = new Runnable() {
								public void run() {
									// Update the weights to the output nodes
									trainingSubRoutine((NUMBER_OF_OUTPUT_NODES*3)/8,NUMBER_OF_OUTPUT_NODES/2);

								}};
								Runnable r5 =  new Runnable() {
									public void run() {
										// Update the weights to the output nodes
										trainingSubRoutine(NUMBER_OF_OUTPUT_NODES/2,(NUMBER_OF_OUTPUT_NODES*5)/8);

									}};

									Runnable r6=  new Runnable() {
										public void run() {
											// Update the weights to the output nodes
											trainingSubRoutine((NUMBER_OF_OUTPUT_NODES*5)/8,(NUMBER_OF_OUTPUT_NODES*6)/8);

										}};

										Runnable r7=  new Runnable() {
											public void run() {
												// Update the weights to the output nodes
												trainingSubRoutine((NUMBER_OF_OUTPUT_NODES*6)/8,(NUMBER_OF_OUTPUT_NODES*7)/8);

											}};


											Runnable r8 =  new Runnable() {
												public void run() {
													// Update the weights to the output nodes
													trainingSubRoutine((NUMBER_OF_OUTPUT_NODES*7)/8,NUMBER_OF_OUTPUT_NODES);

												}};


												//Now run the threads
												Thread thr1 = new Thread(r1);
												Thread thr2 = new Thread(r2);
												Thread thr3 = new Thread(r3);
												Thread thr4 = new Thread(r4);
												Thread thr5 = new Thread(r5);
												Thread thr6 = new Thread(r6);
												Thread thr7 = new Thread(r7);
												Thread thr8 = new Thread(r8);
												thr1.start();
												thr2.start();
												thr3.start();
												thr4.start();
												thr5.start();
												thr6.start();
												thr7.start();
												thr8.start();
												try {
													thr1.join();
													thr2.join();
													thr3.join();
													thr4.join();
													thr5.join();
													thr6.join();
													thr7.join();
													thr8.join();
												} catch (InterruptedException e) {
													e.printStackTrace();
												}

												// Resets temporary data structure
												tempOutput = new ArrayList<ArrayList<Double>>();
			}


			long endTime = System.currentTimeMillis();
			executionTime = endTime - startTime;
			System.out.println("Training time: " + executionTime + " milliseconds");

			System.out.println("Epoch " + (i+1) + " has finished.");

		}

	}

	public static void trainingSubRoutine(int start, int stop)   {
		// Update the weights to the output nodes
		for (int ii = start; ii < stop; ii++) {
			for (int j = 0; j < hiddenLayerNodes.size(); j++) {
				// Grabs the error that was calculated for the output of
				// this output node
				double error = tempOutput.get(tempOutput.size() - 1).get(ii);
				// Update the weight using gradient descent
				outputLayerNodes.get(ii).set(j,outputLayerNodes.get(ii).get(j)
						+ (learningRate * error
								* sigmoidPrimeDynamicProgramming(tempOutput.get(tempOutput.size() - 2).get(ii))
								* tempOutput.get(tempOutput.size() - 3).get(j)));
			}

		}
	}


	/*
	 * Takes the weighted sum as the parameter and returns the output of the sigmoid activation function
	 */
	public static double activationFunction(double weightedSum) {
		double output = 1 / (1 + Math.exp(((-1) * weightedSum)));
		return output;
	}

	/* Returns the derivative of the output of the sigmoid activation function */
	public static double sigmoidPrime(double input) {
		double temp = 1 / (1 + Math.exp(((-1) * input)));
		double output = (temp * (1 - temp));
		return output;
	}


	/*
	 * Returns the summed total error of the output nodes and creates temporary storage for the output of all nodes for a given image
	 */
	public static void networkOutputError(ArrayList<DigitImage> networkInputData, int imageNumber) {

		// Creates an Arraylist holding the output of each node in this layer
		ArrayList<Double> rawSingleImageData = networkInputData.get(imageNumber).getArrayListData();
		//This step may be unnecessary. Be careful when removing as other indicies will need to change.
		tempOutput.add(rawSingleImageData);

		// Stores result to be used later(This will be moved into the "outPutOfLayer" method at some point.)
		ArrayList<Double> hidenLayerOutput = outPutOfLayer(hiddenLayerNodes, rawSingleImageData,1);
		tempOutput.add(hidenLayerOutput);
		// Just like the others
		ArrayList<Double> outputLayerOutput = outPutOfLayer(outputLayerNodes, hidenLayerOutput,2);
		tempOutput.add(outputLayerOutput);
		// Adds the error from each output node to an array which is then stored
		// along with the other above arrays to be used later.
		ArrayList<Double> errorLayer = new ArrayList<Double>();

		for (int i = 0; i < NUMBER_OF_OUTPUT_NODES; i++) {
			double correctOutput = networkInputData.get(imageNumber).getSolutionVector().get(i);
			double output = outputLayerOutput.get(i);
			double rawError = correctOutput - output;
			errorLayer.add(rawError);
		}
		tempOutput.add(errorLayer);
	}


	/*
	 * Takes an image and returns the results of the neural network on the Testing Data in an object that can then be read and written to a file
	 */
	public static ArrayList<OutputVector> solveTestingData(ArrayList<DigitImage> networkInputData) {
		ArrayList<OutputVector> newtworkResults = new ArrayList<OutputVector>();
		for (int i = 0; i < networkInputData.size(); i++) {
			newtworkResults.add(networkSolution(networkInputData, i));
		}
		return newtworkResults;
	}

	/* This looks at one image and reports what number it thinks it is. */
	public static OutputVector networkSolution(ArrayList<DigitImage> networkInputData, int imageNumber) {

		ArrayList<Double> rawSingleImageData = networkInputData.get(imageNumber).getArrayListData();
		ArrayList<Double> hidenLayerOutput = outPutOfLayer(hiddenLayerNodes, rawSingleImageData,1);
		ArrayList<Double> outputLayerOutput = outPutOfLayer(outputLayerNodes, hidenLayerOutput,2);

		double networkOutput = 0;
		double correctOutput = networkInputData.get(imageNumber).getLabel();
		int maxInt = 0;

		for (int i = 0; i < NUMBER_OF_OUTPUT_NODES; i++) {
			double output = outputLayerOutput.get(i);
			if (output > networkOutput) {
				networkOutput = output;
				maxInt = i;
			}
		}
		if (correctOutput == maxInt) {
			//System.out.println("The network is correct. The correct number is: " + (int) correctOutput);
			countOfCorrectImagesAnalyzed++;
		} else {

			holder[(int) maxInt]++;	


			//System.out.println("The network wrongly guessed: " + maxInt + " The correct number was: " + (int) correctOutput);
		}

		OutputVector result = new OutputVector(correctOutput, maxInt);
		countOfImagesAnalyzed++;
		return result;
	}



	/*
	 * Returns the derivative of the output of the sigmoid activation function but takes as a parameter the already computer sigmoid output
	 */
	public static double sigmoidPrimeDynamicProgramming(double sigmoidPrime) {
		double output = (sigmoidPrime * (1 - sigmoidPrime));
		return output;
	}




	public static void readDataFromTrainedFiles() throws IOException, ClassNotFoundException {
		// Grabs weights to output nodes
		FileInputStream fin = new FileInputStream(filePathTrainedOutputWeights);
		ObjectInputStream ois = new ObjectInputStream(fin);
		outputLayerNodes = (ArrayList<ArrayList<Double>>) ois.readObject();
		fin.close();
	}



	/*
	 * Writes the output of the Neural Net stored in an array of OutputVectors to a text file
	 */
	public static void write(ArrayList<OutputVector> x) throws IOException {
		BufferedWriter outputWriter = null;
		String randomString = Double.toString(Math.random());
		File file = new File(filePathResults + randomString + ".txt");

		// If file does not exists, then create it.
		if (!file.exists()) {
			file.createNewFile();
		}
		outputWriter = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
		outputWriter.write("Learning rate: " + Double.toString(learningRate));
		outputWriter.newLine();
		outputWriter.write("Epochs: " + Integer.toString(epochs));
		outputWriter.newLine();
		outputWriter.write("sigmaSquared: " + Double.toString(sigmaSquared));
		outputWriter.newLine();
		outputWriter.write("Number of nodes (training examples used) in hidden layer: " + Integer.toString(60000/trainingSetReductionFactor));
		outputWriter.newLine();
		double percentCorrect = (countOfCorrectImagesAnalyzed / countOfImagesAnalyzed) * 100;
		outputWriter.write("Analyzed " + countOfImagesAnalyzed + " images with " + percentCorrect + " percent accuracy.");
		outputWriter.newLine();
		outputWriter.write("Training time: " + executionTime + " milliseconds");
		outputWriter.newLine();
		outputWriter.write("There were " +Runtime.getRuntime().availableProcessors()+ " cores avalible to the JVM");
		outputWriter.newLine();
		outputWriter.write("Image data binary: " + binaryInput);
		outputWriter.newLine();
		for (int i = 0; i < x.size(); i++) {
			outputWriter.write("Correct: " + x.get(i).getCorrect() + "  ");
			outputWriter.write("Neural net output: " + Integer.toString(x.get(i).getNeuralNetOutput()) + "   ");
			outputWriter.write("Expected output: " + Double.toString(x.get(i).getExpectedOutput()));
			outputWriter.newLine();
		}
		for (int m = 0; m < holder.length; m++) {
			outputWriter.write("Number " + m+" was guessed " +holder[m]+ " times, when it should have guessed another number.");
			outputWriter.newLine();
		}
		
		outputWriter.flush();
		outputWriter.close();
	}

	public static void writeTrainedWeights() throws IOException {
		// We serialize these data structures and write to file. These can then
		// be read back into the neural net.
		FileOutputStream fout = new FileOutputStream(filePathTrainedOutputWeights);
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(outputLayerNodes);	
		oos.close();
		fout.close();

	}
	
	
	

}



