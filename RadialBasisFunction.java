/*
 * Hand Writing Recognition and Simple CAPTCHA Neural Network
 * CS 3425 Final project
 * Spring 2014
 * Min "Ivy" Xing, Zackery Leman
 * This is a Radial Basis Function Neural Network trained by gradient descent on the weights from the hidden layer to the output nodes.
 * Notes: The way threads are  implemented, the code is optimized for either 8 or 24 real cores. 
 */

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
	// The number of times the network is trained with the training data.
	public static int epochs;
	// Creates a random number generator.
	public static Random random = new Random();
	// Tracks running time of the hidden layer construction and training
	//of weights from the hidden layer to the output layer
	public static long executionTime;
	// The number of input nodes will be equal to the number of pixels in the image
	public static int numberOfInputNodes;
	// Create array of nodes in first hidden layer and output layer. Each node will hold an array of weights.
	public static ArrayList<ArrayList<Double>> hiddenLayerNodes = new ArrayList<ArrayList<Double>>();
	public static ArrayList<ArrayList<Double>> outputLayerNodes = new ArrayList<ArrayList<Double>>();
	// The learning rate for the network.
	public static double learningRate;
	// 0=Trains the Network from scratch, 2=Trains the Network starting from weights stored in file, 1=Tests network using weights stored in file without retraining
	public static int usePriorWeights;
	//Dictates the "square of the standard deviation" (variance) in the gaussian RBF
	public static double sigmaSquared;
	// Number of output nodes (Currently the network depends on 10  or 36 output nodes)
	public static final int NUMBER_OF_OUTPUT_NODES = 10;
	//File paths
	public static String filePathResults;
	public static String filePathTrainedOutputWeights;
	// The MNIST data is loaded into these
	public static ArrayList<DigitImage> trainingData = new ArrayList<DigitImage>();
	public static ArrayList<DigitImage> testingData = new ArrayList<DigitImage>();
	// Is true if the input into the network consists of binary images. False if Grayscale.
	public static boolean binaryInput;
	// Set to one to use all of the training data to train the network. The number of training examples is divided by this number
	public  static int trainingSetReductionFactor;
	// Keeps track of false positive guesses for each alphanumeric character.
	public static int[]  falsePositiveCount = new int[NUMBER_OF_OUTPUT_NODES];
	public static long testingTime;
	public static long startTime;
	public static final int NUMBER_OF_CORES = 8;
	// Tracks the number of images correctly identified in the testing set.
	public static ArrayList<Integer> countOfCorrectImagesAnalyzed = new ArrayList<Integer>();
	// Tracks the number of images processed in the testing set.
	public static ArrayList<Integer> countOfImagesAnalyzed = new ArrayList<Integer>();
	public static  double totalCountOfImagesAnalyzed = 0;
	public static  double totalCountOfCorrectImagesAnalyzed = 0;
	public static ArrayList<OutputVector> newtworkResults = new ArrayList<OutputVector>();
	//These are just the data files that hold the MNIST d testing and training sets
	public static final String  trainingImages = "Training-Images";
	public static final String testingImages = "Testing-images";
	public static final String trainingLabels = "Training-Labels";
	public static final String testingLabels = "Testing-Labels";


	public RadialBasisFunction(int trainingSetReductionFactor1,boolean binaryInput1, int sigmaSquared1, int epochs1, double learningRate1, int usePriorWeights1, String filePathResults1 ,String filePathTrainedOutputWeights1 ) throws IOException, ClassNotFoundException{
		
		hiddenLayerNodes.clear();
		outputLayerNodes.clear();
		trainingData.clear();
		tempOutput.clear();
		newtworkResults.clear();
		countOfImagesAnalyzed.clear();
		countOfCorrectImagesAnalyzed.clear();



		binaryInput = binaryInput1;
		trainingSetReductionFactor = trainingSetReductionFactor1;
		sigmaSquared = sigmaSquared1;
		epochs = epochs1;
		learningRate = learningRate1;
		usePriorWeights = usePriorWeights1;
		filePathResults = filePathResults1;
		filePathTrainedOutputWeights = filePathTrainedOutputWeights1;



		//Sets up count trackers for each thread
		for (int y = 0; y < NUMBER_OF_CORES; y++){
			countOfImagesAnalyzed.add(0);
			countOfCorrectImagesAnalyzed.add(0);
		}



		//Sets up an array that will allow us to keep track of the number of false positive guesses for each number.
		for (int m = 0; m < falsePositiveCount.length; m++) {
			falsePositiveCount[m] = 0;
		}
		System.out.println("There are " + Runtime.getRuntime().availableProcessors() + " cores avalible to the JVM.");
		System.out.println("Intel hyperthreading can be responsible for the apparent doubling  in cores.");


		initializeRBF();
		long startTime = System.currentTimeMillis();
		if (usePriorWeights == 1) {	
			// Trains the Network starting from weights stored in file
			System.out.println("Reading Data from Trainined Files and continuing Training");
			readDataFromTrainedFiles();
			trainTheNetwork();
			long endTime = System.currentTimeMillis();
			executionTime = endTime - startTime;
			System.out.println("Total training time" + executionTime + " milliseconds");
			writeTrainedWeights();

			// Test the  RBF Network
			testRBF(testingImages, testingLabels);
		} else if (usePriorWeights == 0) {	
			// Trains the Network from scratch
			System.out.println("Training from scratch");
			trainTheNetwork();
			long endTime = System.currentTimeMillis();
			executionTime = endTime - startTime;
			System.out.println("Total training time" + executionTime + " milliseconds");
			writeTrainedWeights();
			// Test the  RBF Network
			testRBF(testingImages, testingLabels);
		} else if (usePriorWeights == 2) { 	
			// Tests network using weights stored in file without retraining
			readDataFromTrainedFiles();
			// Test the  RBF Network
			System.out.println("Testing only. Using trained files");
			testRBF(testingImages, testingLabels);
		}
		//Output all false positives
		for (int m = 0; m < falsePositiveCount.length; m++) {
			System.out.println("Number " + m +" was guessed " +falsePositiveCount[m] + " times, when it should have guessed another number.");
		}
	}

	public static void initializeRBF() throws IOException {
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
		for (int i = 0; i < trainingData.size()/trainingSetReductionFactor; i++) {
			ArrayList<Double> weights = new ArrayList<Double>(numberOfInputNodes);
			weights = trainingData.get(i).getArrayListData();
			hiddenLayerNodes.add(weights);
		}

		// Initialize weights with random values for all nodes in the outputlayer.
		for (int i = 0; i < NUMBER_OF_OUTPUT_NODES; i++) {
			ArrayList<Double> weights = new ArrayList<Double>();
			for (int j = 0; j <  trainingData.size()/trainingSetReductionFactor; j++) {
				weights.add(random.nextGaussian());
			}
			outputLayerNodes.add(weights);
		}
	}

	public static void testRBF(String testingImages, String testingLabels) throws IOException {
		startTime = System.currentTimeMillis();
		// Loads testing data set
		DigitImageLoadingService test = new DigitImageLoadingService(testingLabels, testingImages,binaryInput);
		try {
			// Our data structure holds the testing data
			testingData = test.loadDigitImages();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Tests the network with the testing data and prints results to file
		solveTestingData();
		write(newtworkResults);
		//Summarizes results
		double percentCorrect = (totalCountOfCorrectImagesAnalyzed / totalCountOfImagesAnalyzed) * 100;
		System.out.println("Analyzed " + totalCountOfImagesAnalyzed + " images with " + percentCorrect + " percent accuracy.");
		System.out.println("Look in " + filePathResults +  " directory to find  the output.");
		long endTime = System.currentTimeMillis();
		testingTime = endTime - startTime;
		System.out.println("Testing time: " + testingTime + " milliseconds");
	}

	/*
	 * Returns the output from a given node ( in the hidden layer) after the input has been summed.It takes the layer that the node is in, the index of the node in the
	 * layer, and the output from the previous layer
	 */
	public static double hiddenNodeOutput(ArrayList<ArrayList<Double>> layerOfNodes, ArrayList<Double> outputFromPreviousLayer, int indexOfNodeinlayer) {
		double sum = 0;
		for (int i = 0; i < outputFromPreviousLayer.size(); i++) {

			sum = sum + Math.pow(layerOfNodes.get(indexOfNodeinlayer).get(i) - outputFromPreviousLayer.get(i), 2);

		}
		return Math.exp(-1 * (sum / sigmaSquared));
	}
	/*
	 * Returns the final layer output from a given node after the input has been summed. It takes the layer that the node is in, the index of the node in the
	 * layer, and the output from the previous layer
	 */
	public static double outputNodeOutput(ArrayList<ArrayList<Double>> layerOfNodes, ArrayList<Double> outputFromPreviousLayer, int indexOfNodeinlayer) {
		double sum = 0;
		for (int i = 0; i < outputFromPreviousLayer.size(); i++) {
			sum = sum + (layerOfNodes.get(indexOfNodeinlayer).get(i) * outputFromPreviousLayer.get(i));
		}
		return activationFunction(sum);
	}

	/* This returns an array representing the output of all nodes in the given layer */
	public static ArrayList<Double> outPutOfLayer(ArrayList<ArrayList<Double>> currentLayer, ArrayList<Double> outputFromPreviousLayer, boolean hidden) {
		ArrayList<Double> outputOfCurrentlayer = new ArrayList<Double>();
		for (int i = 0; i < currentLayer.size(); i++) {
			double output;
			if(hidden){
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
	 * When reviewing code just read one thread in either eightCores() or twentyFourCores().
	 */
	public static void trainTheNetwork() {
		for (int i = 0; i < epochs; i++) { // for each epoch
			//For every image in the training file
			long startTime = System.currentTimeMillis();
			for (int images = 0; images < trainingData.size()/trainingSetReductionFactor; images++) { 

				calculateErrorForEachOutputNode(images);

				//Now update all weights
				if (NUMBER_OF_CORES == 8){
					eightCores();
				}
				else if (NUMBER_OF_CORES == 24) {
					twentyFourCores();
				}
				else{
					System.out.println("There are not 24 or 8 cores?");
				}

				// Resets temporary data structure create din call to calculateErrorForEachOutputNode
				tempOutput = new ArrayList<ArrayList<Double>>();
			}
			long endTime = System.currentTimeMillis();
			executionTime = endTime - startTime;
			System.out.println("Training time: " + executionTime + " milliseconds");
			System.out.println("Epoch " + (i + 1) + " has finished.");
		}
	}
	/*
	 * This is the heart of the code that trains the network using gradient descent 
	 */
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
	 * Creates temporary storage for the output of all nodes after a given image has been run through the network.
	 * This allows the other methods to access this  stored data.
	 */
	public static void calculateErrorForEachOutputNode(int imageNumber) {
		ArrayList<Double> rawSingleImageData = trainingData.get(imageNumber).getArrayListData();
		tempOutput.add(rawSingleImageData);
		ArrayList<Double> hidenLayerOutput = outPutOfLayer(hiddenLayerNodes, rawSingleImageData,true);
		tempOutput.add(hidenLayerOutput);
		ArrayList<Double> outputLayerOutput = outPutOfLayer(outputLayerNodes, hidenLayerOutput,false);
		tempOutput.add(outputLayerOutput);
		
		// Adds the error from each output node to an array which is then stored
		// along with the other above arrays to be used later.
		ArrayList<Double> errorLayer = new ArrayList<Double>();

		for (int i = 0; i < NUMBER_OF_OUTPUT_NODES; i++) {
			double correctOutput = trainingData.get(imageNumber).getSolutionVector().get(i);
			double output = outputLayerOutput.get(i);
			double rawError = correctOutput - output;
			errorLayer.add(rawError);
		}
		tempOutput.add(errorLayer);
	}

	/*
	 * Takes an image and updates the results of the neural network on the testing data
	 * in an object that can then be read and then written to a file.
	 */
	public static void solveTestingData() {		
		if (NUMBER_OF_CORES == 8){
			eightCoreSolve();
		}
		else if (NUMBER_OF_CORES == 24) {
			twentyFourCoreSolve();
		}
		else{
			System.out.println("There are not 24 or 8 cores?");
		}

	}

	/* This looks at one image and reports what number it thinks it is. */
	public static OutputVector singleImageBestGuess(ArrayList<DigitImage> networkInputData, int imageNumber, int thread) {
		//Processes image through all layers
		ArrayList<Double> rawSingleImageData = networkInputData.get(imageNumber).getArrayListData();
		ArrayList<Double> hidenLayerOutput = outPutOfLayer(hiddenLayerNodes, rawSingleImageData,true);
		ArrayList<Double> outputLayerOutput = outPutOfLayer(outputLayerNodes, hidenLayerOutput,false);

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
			countOfCorrectImagesAnalyzed.set(thread,countOfCorrectImagesAnalyzed.get(thread)+1);
		} else {
			falsePositiveCount[(int) maxInt]++;	
			//System.out.println("The network wrongly guessed: " + maxInt + " The correct number was: " + (int) correctOutput);
		}
		OutputVector result = new OutputVector(correctOutput, maxInt);
		countOfImagesAnalyzed.set(thread,countOfImagesAnalyzed.get(thread)+1);
		return result;
	}

	/*
	 * Returns the derivative of the output of the sigmoid activation function but takes as a parameter the already computer sigmoid output
	 */
	public static double sigmoidPrimeDynamicProgramming(double sigmoidPrime) {
		double output = (sigmoidPrime * (1 - sigmoidPrime));
		return output;
	}

	public static void eightCores(){

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


	}


	public static void twentyFourCores(){

		//Creates 24 threads and splits the test set into 24 parts each of which is handled by a different thread 
		Runnable r1 = new Runnable() {
			public void run() {
				trainingSubRoutine(0, NUMBER_OF_OUTPUT_NODES / 24);
			}
		};
		Runnable r2 = new Runnable() {
			public void run() {
				trainingSubRoutine(NUMBER_OF_OUTPUT_NODES / 24,NUMBER_OF_OUTPUT_NODES *2/ 24); 
			}
		};
		Runnable r3 = new Runnable() {
			public void run() {
				trainingSubRoutine(NUMBER_OF_OUTPUT_NODES*2/24,(NUMBER_OF_OUTPUT_NODES*3)/24);
			}
		};
		Runnable r4 = new Runnable() {
			public void run() {
				trainingSubRoutine((NUMBER_OF_OUTPUT_NODES*3)/24,NUMBER_OF_OUTPUT_NODES*4/24);
			}
		};
		Runnable r5 = new Runnable() {
			public void run() {
				trainingSubRoutine(NUMBER_OF_OUTPUT_NODES*4 /24,(NUMBER_OF_OUTPUT_NODES*5)/24); 
			}
		};
		Runnable r6 = new Runnable() {
			public void run() {
				trainingSubRoutine((NUMBER_OF_OUTPUT_NODES*5 )/24,(NUMBER_OF_OUTPUT_NODES*6 )/24);
			}
		};
		Runnable r7 = new Runnable() {
			public void run() {
				trainingSubRoutine((NUMBER_OF_OUTPUT_NODES*6)/24,(NUMBER_OF_OUTPUT_NODES*7)/8);
			}
		};
		Runnable r8 = new Runnable() {
			public void run() {
				trainingSubRoutine((NUMBER_OF_OUTPUT_NODES* 7)/24,(NUMBER_OF_OUTPUT_NODES*8 )/24);
			}
		};
		Runnable r9 = new Runnable() {
			public void run() {
				trainingSubRoutine((NUMBER_OF_OUTPUT_NODES*8 )/24,(NUMBER_OF_OUTPUT_NODES*9 )/24);
			}
		};
		Runnable r10 = new Runnable() {
			public void run() {
				trainingSubRoutine((NUMBER_OF_OUTPUT_NODES* 9)/24,(NUMBER_OF_OUTPUT_NODES* 10)/24);
			}
		};
		Runnable r11 = new Runnable() {
			public void run() {
				trainingSubRoutine((NUMBER_OF_OUTPUT_NODES*10 )/24,(NUMBER_OF_OUTPUT_NODES*11 )/24);
			}
		};
		Runnable r12 = new Runnable() {
			public void run() {
				trainingSubRoutine((NUMBER_OF_OUTPUT_NODES*11 )/24,(NUMBER_OF_OUTPUT_NODES* 12)/24);
			}
		};

		Runnable r13 = new Runnable() {
			public void run() {
				trainingSubRoutine((NUMBER_OF_OUTPUT_NODES*12 )/24,(NUMBER_OF_OUTPUT_NODES*13 )/24);
			}
		};
		Runnable r14 = new Runnable() {
			public void run() {
				trainingSubRoutine((NUMBER_OF_OUTPUT_NODES*13 )/24,(NUMBER_OF_OUTPUT_NODES* 14)/24);
			}
		};
		Runnable r15 = new Runnable() {
			public void run() {
				trainingSubRoutine((NUMBER_OF_OUTPUT_NODES*14 )/24,(NUMBER_OF_OUTPUT_NODES*15 )/24);
			}
		};

		Runnable r16 = new Runnable() {
			public void run() {
				trainingSubRoutine((NUMBER_OF_OUTPUT_NODES*15 )/24,(NUMBER_OF_OUTPUT_NODES* 16)/24); 

			}
		};
		Runnable r17 = new Runnable() {
			public void run() {
				trainingSubRoutine((NUMBER_OF_OUTPUT_NODES*16 )/24,(NUMBER_OF_OUTPUT_NODES*17 )/24);
			}
		};
		Runnable r18 = new Runnable() {
			public void run() {
				trainingSubRoutine((NUMBER_OF_OUTPUT_NODES*17 )/24,(NUMBER_OF_OUTPUT_NODES*18 )/24);
			}
		};
		Runnable r19 = new Runnable() {
			public void run() {
				trainingSubRoutine((NUMBER_OF_OUTPUT_NODES*18 )/24,(NUMBER_OF_OUTPUT_NODES*19 )/24);
			}
		};


		Runnable r20 = new Runnable() {
			public void run() {
				trainingSubRoutine((NUMBER_OF_OUTPUT_NODES*19 )/24,(NUMBER_OF_OUTPUT_NODES*20 )/24);
			}
		};
		Runnable r21 = new Runnable() {
			public void run() {
				trainingSubRoutine((NUMBER_OF_OUTPUT_NODES*20 )/24,(NUMBER_OF_OUTPUT_NODES*21 )/24);
			}
		};
		Runnable r22 = new Runnable() {
			public void run() {
				trainingSubRoutine((NUMBER_OF_OUTPUT_NODES*21 )/24,(NUMBER_OF_OUTPUT_NODES*22 )/24);
			}
		};

		Runnable r23 = new Runnable() {
			public void run() {
				trainingSubRoutine((NUMBER_OF_OUTPUT_NODES*22 )/24,(NUMBER_OF_OUTPUT_NODES*23 )/24);
			}
		};

		Runnable r24 = new Runnable() {
			public void run() {
				trainingSubRoutine((NUMBER_OF_OUTPUT_NODES*23)/24,NUMBER_OF_OUTPUT_NODES);
			}
		};


		//Starts the 24 threads
		Thread thr1 = new Thread(r1);
		Thread thr2 = new Thread(r2);
		Thread thr3 = new Thread(r3);
		Thread thr4 = new Thread(r4);
		Thread thr5 = new Thread(r5);
		Thread thr6 = new Thread(r6);
		Thread thr7 = new Thread(r7);
		Thread thr8 = new Thread(r8);
		Thread thr9 = new Thread(r9);
		Thread thr10 = new Thread(r10);
		Thread thr11 = new Thread(r11);
		Thread thr12 = new Thread(r12);
		Thread thr13 = new Thread(r13);
		Thread thr14 = new Thread(r14);
		Thread thr15 = new Thread(r15);
		Thread thr16 = new Thread(r16);
		Thread thr17 = new Thread(r17);
		Thread thr18 = new Thread(r18);
		Thread thr19 = new Thread(r19);
		Thread thr20 = new Thread(r20);
		Thread thr21 = new Thread(r21);
		Thread thr22 = new Thread(r22);
		Thread thr23 = new Thread(r23);
		Thread thr24 = new Thread(r24);
		thr1.start();
		thr2.start();
		thr3.start();
		thr4.start();
		thr5.start();
		thr6.start();
		thr7.start();
		thr8.start();
		thr9.start();
		thr10.start();
		thr11.start();
		thr12.start();
		thr13.start();
		thr14.start();
		thr15.start();
		thr16.start();
		thr17.start();
		thr18.start();
		thr19.start();
		thr20.start();
		thr21.start();
		thr22.start();
		thr23.start();
		thr24.start();
		try {
			thr1.join();
			thr2.join();
			thr3.join();
			thr4.join();
			thr5.join();
			thr6.join();
			thr7.join();
			thr8.join();
			thr9.join();
			thr10.join();
			thr11.join();
			thr12.join();
			thr13.join();
			thr14.join();
			thr15.join();
			thr16.join();
			thr17.join();
			thr18.join();
			thr19.join();
			thr20.join();
			thr21.join();
			thr22.join();
			thr23.join();
			thr24.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	public static void eightCoreSolve(){
		//Creates 8 threads and splits the test set into eight parts each of which is handled by a seperate thread 
		Runnable r1 = new Runnable() {
			public void run() {
				for (int i = 0; i < testingData.size()/8; i++) {
					newtworkResults.add(singleImageBestGuess(testingData, i,0));
				}
			}};

			Runnable r2 = new Runnable() {
				public void run() {
					for (int i =  testingData.size()/8; i < testingData.size()/4; i++) {
						newtworkResults.add(singleImageBestGuess(testingData, i,1));
					}

				}};

				Runnable r3 = new Runnable() {
					public void run() {
						for (int i =  testingData.size()/4; i < testingData.size()*3/8; i++) {
							newtworkResults.add(singleImageBestGuess(testingData, i,2));
						}

					}};

					Runnable r4  = new Runnable() {
						public void run() {
							for (int i =  testingData.size()*3/8; i < testingData.size()/2; i++) {
								newtworkResults.add(singleImageBestGuess(testingData, i,3));
							}
						}};
						Runnable r5 =  new Runnable() {
							public void run() {

								for (int i =  testingData.size()/2; i < testingData.size()*5/8; i++) {
									newtworkResults.add(singleImageBestGuess(testingData, i,4));
								}
							}};

							Runnable r6=  new Runnable() {
								public void run() {
									for (int i =  testingData.size()*5/8; i < testingData.size()*6/8; i++) {
										newtworkResults.add(singleImageBestGuess(testingData, i,5));
									}
								}};

								Runnable r7=  new Runnable() {
									public void run() {
										for (int i =  testingData.size()*6/8; i < testingData.size()*7/8; i++) {
											newtworkResults.add(singleImageBestGuess(testingData, i,6));
										}
									}};


									Runnable r8 =  new Runnable() {
										public void run() {

											for (int i =  testingData.size()*7/8; i < testingData.size(); i++) {
												newtworkResults.add(singleImageBestGuess(testingData, i,7));
											}

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

	}

	public static void twentyFourCoreSolve(){
		//Creates 24 threads and splits the test set into 24 parts each of which is handled by a seperate thread 
		Runnable r1 = new Runnable() {
			public void run() {
				for (int i = 0; i < testingData.size()/24; i++) {
					newtworkResults.add(singleImageBestGuess(testingData, i,0));
				}
			}};

			Runnable r2 = new Runnable() {
				public void run() {
					for (int i =  testingData.size()/24; i < testingData.size()/12; i++) {
						newtworkResults.add(singleImageBestGuess(testingData, i,1));
					}

				}};

				Runnable r3 = new Runnable() {
					public void run() {
						for (int i =  testingData.size()/12; i < testingData.size()*3/24; i++) {
							newtworkResults.add(singleImageBestGuess(testingData, i,2));
						}

					}};

					Runnable r4  = new Runnable() {
						public void run() {
							for (int i =  testingData.size()*3/24; i < testingData.size()*4/24; i++) {
								newtworkResults.add(singleImageBestGuess(testingData, i,3));
							}
						}};
						Runnable r5 =  new Runnable() {
							public void run() {

								for (int i =  testingData.size()*4/24; i < testingData.size()*5/24; i++) {
									newtworkResults.add(singleImageBestGuess(testingData, i,4));
								}
							}};

							Runnable r6=  new Runnable() {
								public void run() {
									for (int i =  testingData.size()*5/24; i < testingData.size()*6/24; i++) {
										newtworkResults.add(singleImageBestGuess(testingData, i,5));
									}
								}};

								Runnable r7=  new Runnable() {
									public void run() {
										for (int i =  testingData.size()*6/24; i < testingData.size()*7/24; i++) {
											newtworkResults.add(singleImageBestGuess(testingData, i,6));
										}
									}};


									Runnable r8 =  new Runnable() {
										public void run() {

											for (int i =  testingData.size()*7/24; i < testingData.size()*8/24; i++) {
												newtworkResults.add(singleImageBestGuess(testingData, i,7));
											}

										}};


										Runnable r9 = new Runnable() {
											public void run() {
												for (int i = testingData.size()*8/24; i < testingData.size()*9/24; i++) {
													newtworkResults.add(singleImageBestGuess(testingData, i,8));
												}
											}};

											Runnable r10 = new Runnable() {
												public void run() {
													for (int i =  testingData.size()*9/24; i < testingData.size()*10/24; i++) {
														newtworkResults.add(singleImageBestGuess(testingData, i,9));
													}

												}};

												Runnable r11 = new Runnable() {
													public void run() {
														for (int i = testingData.size()*10/24; i < testingData.size()*11/24; i++) {
															newtworkResults.add(singleImageBestGuess(testingData, i,10));
														}

													}};

													Runnable r12  = new Runnable() {
														public void run() {
															for (int i =  testingData.size()*11/24; i < testingData.size()*12/24; i++) {
																newtworkResults.add(singleImageBestGuess(testingData, i,11));
															}
														}};
														Runnable r13 =  new Runnable() {
															public void run() {

																for (int i =  testingData.size()*12/24; i < testingData.size()*13/24; i++) {
																	newtworkResults.add(singleImageBestGuess(testingData, i,12));
																}
															}};

															Runnable r14=  new Runnable() {
																public void run() {
																	for (int i =  testingData.size()*13/24; i < testingData.size()*14/24; i++) {
																		newtworkResults.add(singleImageBestGuess(testingData, i,13));
																	}
																}};

																Runnable r15=  new Runnable() {
																	public void run() {
																		for (int i =  testingData.size()*14/24; i < testingData.size()*15/24; i++) {
																			newtworkResults.add(singleImageBestGuess(testingData, i,14));
																		}
																	}};


																	Runnable r16 =  new Runnable() {
																		public void run() {

																			for (int i =  testingData.size()*15/24; i < testingData.size()*16/24; i++) {
																				newtworkResults.add(singleImageBestGuess(testingData, i,15));
																			}

																		}};

																		Runnable r17 = new Runnable() {
																			public void run() {
																				for (int i = testingData.size()*16/24; i < testingData.size()*17/24; i++) {
																					newtworkResults.add(singleImageBestGuess(testingData, i,16));
																				}
																			}};

																			Runnable r18 = new Runnable() {
																				public void run() {
																					for (int i =  testingData.size()*17/24; i < testingData.size()*18/24; i++) {
																						newtworkResults.add(singleImageBestGuess(testingData, i,17));
																					}

																				}};

																				Runnable r19 = new Runnable() {
																					public void run() {
																						for (int i =  testingData.size()*18/24; i < testingData.size()*19/24; i++) {
																							newtworkResults.add(singleImageBestGuess(testingData, i,18));
																						}

																					}};

																					Runnable r20  = new Runnable() {
																						public void run() {
																							for (int i =  testingData.size()*19/24; i < testingData.size()*20/24; i++) {
																								newtworkResults.add(singleImageBestGuess(testingData, i,19));
																							}
																						}};
																						Runnable r21 =  new Runnable() {
																							public void run() {

																								for (int i =  testingData.size()*20/24; i < testingData.size()*21/24; i++) {
																									newtworkResults.add(singleImageBestGuess(testingData, i,20));
																								}
																							}};

																							Runnable r22=  new Runnable() {
																								public void run() {
																									for (int i =  testingData.size()*21/24; i < testingData.size()*22/24; i++) {
																										newtworkResults.add(singleImageBestGuess(testingData, i,21));
																									}
																								}};

																								Runnable r23=  new Runnable() {
																									public void run() {
																										for (int i =  testingData.size()*22/24; i < testingData.size()*23/24; i++) {
																											newtworkResults.add(singleImageBestGuess(testingData, i,22));
																										}
																									}};


																									Runnable r24 =  new Runnable() {
																										public void run() {

																											for (int i =  testingData.size()*23/24; i < testingData.size(); i++) {
																												newtworkResults.add(singleImageBestGuess(testingData, i,23));
																											}

																										}};


																										//Starts the 24 threads
																										Thread thr1 = new Thread(r1);
																										Thread thr2 = new Thread(r2);
																										Thread thr3 = new Thread(r3);
																										Thread thr4 = new Thread(r4);
																										Thread thr5 = new Thread(r5);
																										Thread thr6 = new Thread(r6);
																										Thread thr7 = new Thread(r7);
																										Thread thr8 = new Thread(r8);
																										Thread thr9 = new Thread(r9);
																										Thread thr10 = new Thread(r10);
																										Thread thr11 = new Thread(r11);
																										Thread thr12 = new Thread(r12);
																										Thread thr13 = new Thread(r13);
																										Thread thr14 = new Thread(r14);
																										Thread thr15 = new Thread(r15);
																										Thread thr16 = new Thread(r16);
																										Thread thr17 = new Thread(r17);
																										Thread thr18 = new Thread(r18);
																										Thread thr19 = new Thread(r19);
																										Thread thr20 = new Thread(r20);
																										Thread thr21 = new Thread(r21);
																										Thread thr22 = new Thread(r22);
																										Thread thr23 = new Thread(r23);
																										Thread thr24 = new Thread(r24);
																										thr1.start();
																										thr2.start();
																										thr3.start();
																										thr4.start();
																										thr5.start();
																										thr6.start();
																										thr7.start();
																										thr8.start();
																										thr9.start();
																										thr10.start();
																										thr11.start();
																										thr12.start();
																										thr13.start();
																										thr14.start();
																										thr15.start();
																										thr16.start();
																										thr17.start();
																										thr18.start();
																										thr19.start();
																										thr20.start();
																										thr21.start();
																										thr22.start();
																										thr23.start();
																										thr24.start();
																										try {
																											thr1.join();
																											thr2.join();
																											thr3.join();
																											thr4.join();
																											thr5.join();
																											thr6.join();
																											thr7.join();
																											thr8.join();
																											thr9.join();
																											thr10.join();
																											thr11.join();
																											thr12.join();
																											thr13.join();
																											thr14.join();
																											thr15.join();
																											thr16.join();
																											thr17.join();
																											thr18.join();
																											thr19.join();
																											thr20.join();
																											thr21.join();
																											thr22.join();
																											thr23.join();
																											thr24.join();
																										} catch (InterruptedException e) {
																											e.printStackTrace();
																										}



	}


//-------------------Utility Methods-------------------------
	
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
		totalCountOfCorrectImagesAnalyzed=0;
		totalCountOfImagesAnalyzed=0;

		for(int y=0;y< countOfCorrectImagesAnalyzed.size();y++){
			totalCountOfCorrectImagesAnalyzed=totalCountOfCorrectImagesAnalyzed+countOfCorrectImagesAnalyzed.get(y);
		}
		for(int y=0;y< countOfImagesAnalyzed.size();y++){
			totalCountOfImagesAnalyzed=totalCountOfImagesAnalyzed+countOfImagesAnalyzed.get(y);
		}
		long endTime = System.currentTimeMillis();
		testingTime = endTime - startTime;
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
		double percentCorrect = (totalCountOfCorrectImagesAnalyzed / totalCountOfImagesAnalyzed) * 100;
		outputWriter.write("Analyzed " + totalCountOfImagesAnalyzed + " images with " + percentCorrect + " percent accuracy.");
		outputWriter.newLine();
		outputWriter.write("Training time: " + executionTime + " milliseconds");
		outputWriter.newLine();
		outputWriter.write("Testing time: " + testingTime + " milliseconds");
		outputWriter.newLine();
		outputWriter.write("There were " +Runtime.getRuntime().availableProcessors()+ " cores avalible to the JVM");
		outputWriter.newLine();
		outputWriter.write("Image data binary: " + binaryInput);
		outputWriter.newLine();
		/*for (int i = 0; i < x.size(); i++) {
			outputWriter.write("Correct: " + x.get(i).getCorrect() + "  ");
			outputWriter.write("Neural net output: " + Integer.toString(x.get(i).getNeuralNetOutput()) + "   ");
			outputWriter.write("Expected output: " + Double.toString(x.get(i).getExpectedOutput()));
			outputWriter.newLine();
		}*/
		for (int m = 0; m < falsePositiveCount.length; m++) {
			outputWriter.write("Number " + m+" was guessed " +falsePositiveCount[m]+ " times, when it should have guessed another number.");
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
//------------------------------------------


}
