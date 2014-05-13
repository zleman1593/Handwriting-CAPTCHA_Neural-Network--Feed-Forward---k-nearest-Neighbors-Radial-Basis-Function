/*
 Hand Writing Recognition and Simple CAPTCHA Neural Network
  CS 3425 Final project
  Spring 2014
  Min "Ivy" Xing, Zackery Leman
  This network works by reading in an image and then selecting the number 0-9 that corresponds to the output node with greatest activation.
  It is a feed-forward neural network that uses back propagation.
 */

// NOTES: To run this we had to pass the argument " -Xmx800M"  to the java virtual machine
// NOTES: I got 89 percent accuracy when I set epochs to 30 and hidden nodes to 15. But it took 30 minutes to run.

// IVY NOTE: You will need to change the user name to your user name in all paths like "/Users/zackeryleman/Desktop/NeuralNetOutput/TrainedSetOutputWeights.txt"
//ALSO: Add the folder NeuralNetOutput that is currently in the same folder as this file to your desktop.
//ALSO: set epochs to 5 and hidden nodes to 6 for a fairly quick run to see how it works..

import java.util.*;
import java.io.IOException;
import java.lang.Math;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.*;

public class NeuralNet {
	// Tracks the number of images processed in the testing set.
	public static double countOfImagesAnalyzed = 0;
	// Tracks the number of images correctly identified in the testing set.
	public static double countOfCorrectImagesAnalyzed = 0;
	// Tracks running time of the training
	public static long executionTime;
	// The number of times the network is trained with the training Data
	public static int epochs;
	// Creates a random number generator
	public static Random random = new Random();
	// The number of input nodes will be equal to the number of pixels in the
	// image
	public static int numberOfInputNodes;
	// Number of hidden layers
	public static final int NUMBER_OF_HIDDEN_LAYERS = 1;
	// Number of hidden nodes in second layer (first hidden layer)
	public static int numberOfNodesInHiddenLayer;
	// Number of output nodes (Currently the network depends on 10 or 36 output nodes)
	public static final int NUMBER_OF_OUTPUT_NODES = 10;
	
	// Create array of Nodes in first layer and output layer
	public static ArrayList<ArrayList<Double>> hiddenLayerNodes = new ArrayList<ArrayList<Double>>();
	public static ArrayList<ArrayList<Double>> outputLayerNodes = new ArrayList<ArrayList<Double>>();
	
	// The learning rate for the network
	public static double learningRate;
	// Whether to use weights that have already been trained or to train network again in order to test the network
	public static boolean usePriorWeightsToTestNetwork;
	
	// Whether to use weights that have already been trained as the startig weights to continue a hiatus in training
	public static boolean usePriorWeightsToContinueTraining;
	// For a given training image this array is filled with the output for each
	// layer and then reset for the next image.
	// Prevents duplicate calculations from being performed.
	public static ArrayList<ArrayList<Double>> tempOutput = new ArrayList<ArrayList<Double>>();
	
		
	public static String filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/Results";
	public static String filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/TrainedSetOutputWeights.txt";
	public static String filePathTrainedHiddenWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/TrainedSetHiddenWeights.txt";
	
	// Is true if the input into the network consists of binary images. False if Grayscale.
	public static boolean binaryInput;
	
	public static int[]  holder=new int[10];//Should make this the size of the number of outputs 36
	
	
	public static ArrayList<DigitImage> trainingData = new ArrayList<DigitImage>();
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		//Sets up an array that will allow us to keep track of the number of wrong guesses for each number
		for (int m = 0; m < holder.length; m++) {
			holder[m]=0;
		}
		System.out.println("There are " +Runtime.getRuntime().availableProcessors()+ " cores avalible to the JVM.");
		System.out.println("Intel hyperthreading can be responsible for the apparent doubling  in cores.");


		// numberOfNodesInHiddenLayer=args[1];
		// epochs = Integer.parseInt(args[2]); //number of epochs to run
		// double learningRate = Double.parseDouble(args[3]); //learning rate
		// usePriorWeightsToTestNetwork=Boolean.parseBolean(args[4]);
		//binaryInput = Boolean.parseBolean(args[5]);
		// String trainingImages=args[6];
		// String testingImages=args[7];
		// String trainingLabels=args[8];
		// String testingLabels=args[9];
		
		
		// These are hard coded versions of the above
		
		numberOfNodesInHiddenLayer = 30;
		epochs = 10;
		learningRate = 0.8;
		// Set this to true to avoid retraining. Allows the files in
		// NeuralNetOutput folder to be loaded and used.
		usePriorWeightsToTestNetwork = true;
		binaryInput=true;
		String trainingImages = "Training-Images";
		String testingImages = "Testing-images";
		String trainingLabels = "Training-Labels";
		String testingLabels = "Testing-Labels";
		usePriorWeightsToContinueTraining = true;
		

		/*if (!usePriorWeightsToTestNetwork) {
			initializeMultilayerFeedForward(trainingImages, trainingLabels);
			//initializeMultilayerFeedForwardCaptcha();
			
			// Trains the network with the training Data
						long startTime = System.currentTimeMillis();
						trainTheNetwork(trainingData);
						long endTime = System.currentTimeMillis();
						executionTime = endTime - startTime;
						System.out.println("Training time: " + executionTime + " milliseconds");
						// Creates data files that can be reused by the network without
						// retraining.
						writeTrainedWeights();
		} */
		
		//if(usePriorWeightsToContinueTraining){
		initializeMultilayerFeedForward(trainingImages, trainingLabels);
			readDataFromTrainedFiles();
			// Trains the network with the training Data
			long startTime = System.currentTimeMillis();
			trainTheNetwork(trainingData);
			long endTime = System.currentTimeMillis();
			executionTime = endTime - startTime;
			System.out.println("Training time: " + executionTime + " milliseconds");
			// Creates data files that can be reused by the network without
			// retraining.
			writeTrainedWeights();
			
			
		//}
		/*if (usePriorWeightsToTestNetwork && !usePriorWeightsToContinueTraining)  {
			readDataFromTrainedFiles();
			System.out.println("Reading Data from trained files");
			numberOfInputNodes = hiddenLayerNodes.get(0).size();// This could be an issue
		}*/
		
		
		// Test the Feed-Forward network
		testMultilayerFeedForward(testingImages, testingLabels);
		//testMultilayerFeedForwardCaptcha();
		for (int m = 0; m < holder.length; m++) {
			System.out.println("Number " + m+" was guessed " +holder[m]+ " times, when it should have guessed another number.");
		}


	}

	
	public static void initializeMultilayerFeedForward(String trainingImages, String trainingLabels) throws IOException {

		// Loads training and testing data sets
		DigitImageLoadingService train = new DigitImageLoadingService(trainingLabels, trainingImages,binaryInput);
		try {
			// Our data structure holds the training data
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
		// and determines how many pixels and thus how many input nodes are
		// needed
		// (one per pixel)
		numberOfInputNodes = trainingData.get(0).getData().length;

		// Initialize weights with random values for all nodes in the first
		// hidden layer.
		for (int i = 0; i < numberOfNodesInHiddenLayer; i++) {
			ArrayList<Double> weights = new ArrayList<Double>(numberOfInputNodes);
			for (int j = 0; j < numberOfInputNodes; j++) {
				weights.add(random.nextGaussian());
			}
			hiddenLayerNodes.add(weights);
		}
		// Initialize weights with random values for all nodes in the output
		// layer.
		for (int i = 0; i < NUMBER_OF_OUTPUT_NODES; i++) {
			ArrayList<Double> weights = new ArrayList<Double>(numberOfNodesInHiddenLayer);
			for (int j = 0; j < numberOfNodesInHiddenLayer; j++) {
				weights.add(random.nextGaussian());
			}
			outputLayerNodes.add(weights);
		}



	}
	
	/*
	 * This takes the training data and attempts to train the neural net to learn how to recognize characters from images.
	 */
	public static void trainTheNetwork(ArrayList<DigitImage> trainingData) {

		for (int i = 0; i < epochs; i++) { // for each epoch
			//for every image in the training file
			long startTime = System.currentTimeMillis();
			for (int images = 0; images < trainingData.size(); images++) { 

				calculateErrorForEachOutputNode(trainingData, images);
				

				//The Duplicated code will be compressed into a method that takes four parameters that differ between the four threads.
				Runnable r1 = new Runnable() {
					public void run() {

						// Update the weights to the output nodes
						for (int ii = 0; ii < NUMBER_OF_OUTPUT_NODES / 4; ii++) {
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

						// Update the weights to the nodes going to the hidden nodes
						for (int ii = 0; ii < hiddenLayerNodes.size() / 4; ii++) {
							for (int j = 0; j < numberOfInputNodes; j++) {
								double error = 0;
								for (int k = 0; k < NUMBER_OF_OUTPUT_NODES; k++) {
									// This is the summed error for the output layer
									error = error + (sigmoidPrimeDynamicProgramming(tempOutput.get(tempOutput.size() - 2).get(k))
											* tempOutput.get(tempOutput.size() - 1).get(k)
											* outputLayerNodes.get(k).get(ii));

								}
								//Update the weight using gradient descent back propagation
								hiddenLayerNodes.get(ii).set(j,hiddenLayerNodes.get(ii).get(j)
										+ (learningRate * error
												* tempOutput.get(0).get(j))
												* sigmoidPrimeDynamicProgramming(tempOutput.get(1).get(ii)));
							}
						}
					}};
					
					Runnable r2 = new Runnable() {
						public void run() {
							
							
							// Update the weights to the output nodes
							for (int ii = NUMBER_OF_OUTPUT_NODES/4; ii < NUMBER_OF_OUTPUT_NODES/2; ii++) {
								for (int j = 0; j < hiddenLayerNodes.size(); j++) {
									// Grabs the error that was calculated for the output of
									// this output node
									double error = tempOutput.get(tempOutput.size() - 1).get(ii);
									// Update the weight using gradient descent
									outputLayerNodes.get(ii).set(j,outputLayerNodes.get(ii).get(j)
											+(learningRate
													*error
													*sigmoidPrimeDynamicProgramming(tempOutput.get(tempOutput.size() - 2).get(ii))
													*tempOutput.get(tempOutput.size() - 3).get(j)));
								}
							}
							
							
							
							// Update the weights to the nodes going to the hidden nodes
							for (int ii = hiddenLayerNodes.size()/4; ii <hiddenLayerNodes.size()/2 ; ii++) {


								for (int j = 0; j < numberOfInputNodes; j++) {
									double error = 0;
									for (int k = 0; k < NUMBER_OF_OUTPUT_NODES; k++) {
										// This is the summed error for the output layer
										error= error+(sigmoidPrimeDynamicProgramming(tempOutput.get(tempOutput.size() - 2).get(k))
												*tempOutput.get(tempOutput.size() - 1).get(k)
												*outputLayerNodes.get(k).get(ii));

									}
									//Update the weight using gradient descent back propagation
									hiddenLayerNodes.get(ii).set(j,hiddenLayerNodes.get(ii).get(j)
											+(learningRate
													*error
													*tempOutput.get(0).get(j))
													*sigmoidPrimeDynamicProgramming(tempOutput.get(1).get(ii)));
								}


							}
						}};
						
						Runnable r3 = new Runnable() {
							public void run() {
								
								
								// Update the weights to the output nodes
								for (int ii = NUMBER_OF_OUTPUT_NODES/2; ii < (NUMBER_OF_OUTPUT_NODES*3)/4; ii++) {
									for (int j = 0; j < hiddenLayerNodes.size(); j++) {
										// Grabs the error that was calculated for the output of
										// this output node
										double error = tempOutput.get(tempOutput.size() - 1).get(ii);
										// Update the weight using gradient descent
										outputLayerNodes.get(ii).set(j,outputLayerNodes.get(ii).get(j)
												+(learningRate
														*error
														*sigmoidPrimeDynamicProgramming(tempOutput.get(tempOutput.size() - 2).get(ii))
														*tempOutput.get(tempOutput.size() - 3).get(j)));
									}
								}
								
								
								// Update the weights to the nodes going to the hidden nodes
								for (int ii = hiddenLayerNodes.size()/2; ii <(hiddenLayerNodes.size()*3)/4 ; ii++) {


									for (int j = 0; j < numberOfInputNodes; j++) {
										double error = 0;
										for (int k = 0; k < NUMBER_OF_OUTPUT_NODES; k++) {
											// This is the summed error for the output layer
											error= error+(sigmoidPrimeDynamicProgramming(tempOutput.get(tempOutput.size() - 2).get(k))
													*tempOutput.get(tempOutput.size() - 1).get(k)
													*outputLayerNodes.get(k).get(ii));

										}
										//Update the weight using gradient descent back propagation
										hiddenLayerNodes.get(ii).set(j,hiddenLayerNodes.get(ii).get(j)
												+(learningRate
														*error
														*tempOutput.get(0).get(j))
														*sigmoidPrimeDynamicProgramming(tempOutput.get(1).get(ii)));
									}


								}
							}};
							
							
							
							Runnable r4 = new Runnable() {
								public void run() {
									
									
									// Update the weights to the output nodes
									for (int ii = (NUMBER_OF_OUTPUT_NODES*3)/4; ii <NUMBER_OF_OUTPUT_NODES; ii++) {
										for (int j = 0; j < hiddenLayerNodes.size(); j++) {
											// Grabs the error that was calculated for the output of
											// this output node
											double error = tempOutput.get(tempOutput.size() - 1).get(ii);
											// Update the weight using gradient descent
											outputLayerNodes.get(ii).set(j,outputLayerNodes.get(ii).get(j)
													+(learningRate
															*error
															*sigmoidPrimeDynamicProgramming(tempOutput.get(tempOutput.size() - 2).get(ii))
															*tempOutput.get(tempOutput.size() - 3).get(j)));
										}
									}
									
									
									
									// Update the weights to the nodes going to the hidden nodes
									for (int ii = (hiddenLayerNodes.size()*3)/4; ii <hiddenLayerNodes.size() ; ii++) {


										for (int j = 0; j < numberOfInputNodes; j++) {
											double error = 0;
											for (int k = 0; k < NUMBER_OF_OUTPUT_NODES; k++) {
												// This is the summed error for the output layer
												error= error+(sigmoidPrimeDynamicProgramming(tempOutput.get(tempOutput.size() - 2).get(k))
														*tempOutput.get(tempOutput.size() - 1).get(k)
														*outputLayerNodes.get(k).get(ii));

											}
											//Update the weight using gradient descent back propagation
											hiddenLayerNodes.get(ii).set(j,hiddenLayerNodes.get(ii).get(j)
													+(learningRate
															*error
															*tempOutput.get(0).get(j))
															*sigmoidPrimeDynamicProgramming(tempOutput.get(1).get(ii)));
										}

									}
								}};

					Thread thr1 = new Thread(r1);
					Thread thr2 = new Thread(r2);
					Thread thr3 = new Thread(r3);
					Thread thr4 = new Thread(r4);
					thr1.start();
					thr2.start();
					thr3.start();
					thr4.start();
					try {
						thr1.join();
						thr2.join();
						thr3.join();
						thr4.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					// Resets temporary data structure
					tempOutput = new ArrayList<ArrayList<Double>>();
			}
			// Test the Feed-Forward network
			/*try {
				
				testMultilayerFeedForward("Testing-images", "Testing-Labels");
				
			} catch (IOException e) {
				e.printStackTrace();
			}*/
			long endTime = System.currentTimeMillis();
			executionTime = endTime - startTime;
			System.out.println("Training time: " + executionTime + " milliseconds");
			System.out.println("Epoch " + (i+1) + " has finished.");

		}

	}

	
	
	/*
	 * This returns an array representing the output of all nodes in the given layer
	 */
	public static ArrayList<Double> outPutOfLayer(ArrayList<ArrayList<Double>> currentLayer, ArrayList<Double> outputFromPreviousLayer) {
		ArrayList<Double> outputOfCurrentlayer = new ArrayList<Double>();
		for (int i = 0; i < currentLayer.size(); i++) {
			double output = nodeOutput(currentLayer, outputFromPreviousLayer, i);
			outputOfCurrentlayer.add(output);
		}
		return outputOfCurrentlayer;
	}
	
	
	/*
	 * Returns the output from a given node after the input has been summed and processed by the activation functionIt takes the layer that the node
	 * is in, the index of the node in the layer, and the output from the previous layer
	 */
	public static double nodeOutput(ArrayList<ArrayList<Double>> layerOfNodes, ArrayList<Double> outputFromPreviousLayer, int indexOfNodeinlayer) {
		double sum = 0;
		for (int i = 0; i < outputFromPreviousLayer.size(); i++) {
			sum = sum + (layerOfNodes.get(indexOfNodeinlayer).get(i) * outputFromPreviousLayer.get(i));
		}
		return activationFunction(sum);
	}

	/*
	 * Takes the weighted sum as the parameter and returns the output of the sigmoid activation function
	 */
	public static double activationFunction(double weightedSum) {
		double output = 1 / (1 + Math.exp(((-1) * weightedSum)));
		return output;
	}
	


	/*
	 * Returns the derivative of the output of the sigmoid activation function but takes as a parameter the already computer sigmoid output
	 */
	public static double sigmoidPrimeDynamicProgramming(double sigmoid) {
		double output = (sigmoid * (1 - sigmoid));
		return output;
	}
	


	/*
	 * Creates temporary storage for the output of all nodes for a given image
	 */
	public static void calculateErrorForEachOutputNode(ArrayList<DigitImage> networkInputData, int imageNumber) {

		// Creates an Arraylist holding the output of each node in this layer
		ArrayList<Double> rawSingleImageData = networkInputData.get(imageNumber).getArrayListData();
		//This step may be unnecessary. Be careful when removing as other indicies will need to change.
		tempOutput.add(rawSingleImageData);

		// Stores result to be used later(This will be moved into the "outPutOfLayer" method at some point.)
		ArrayList<Double> hidenLayerOutput = outPutOfLayer(hiddenLayerNodes, rawSingleImageData);
		tempOutput.add(hidenLayerOutput);
		// Just like the others
		ArrayList<Double> outputLayerOutput = outPutOfLayer(outputLayerNodes, hidenLayerOutput);
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
	
	public static void testMultilayerFeedForward(String testingImages, String testingLabels) throws IOException {
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
		System.out.println("Look in /Users/\"your username\"/Desktop/NeuralNetOutput  directory to find  the output.");
	}

	
	/*
	 * Takes an image and returns the results of the neural network on the Testing Data in an object that can then be read and written to a file
	 */
	public static ArrayList<OutputVector> solveTestingData(ArrayList<DigitImage> networkInputData) {
		ArrayList<OutputVector> newtworkResults = new ArrayList<OutputVector>();
		for (int i = 0; i < networkInputData.size(); i++) {
			newtworkResults.add(singleImageBestGuess(networkInputData, i));
		}
		return newtworkResults;
	}

	
	

	/* This looks at one image and reports what number it thinks it is. */
	public static OutputVector singleImageBestGuess(ArrayList<DigitImage> networkInputData, int imageNumber) {

		ArrayList<Double> rawSingleImageData = networkInputData.get(imageNumber).getArrayListData();
		ArrayList<Double> hidenLayerOutput = outPutOfLayer(hiddenLayerNodes, rawSingleImageData);
		ArrayList<Double> outputLayerOutput = outPutOfLayer(outputLayerNodes, hidenLayerOutput);

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
			System.out.println("The network is correct. The correct number is: " + (int) correctOutput);
			countOfCorrectImagesAnalyzed++;
		} else {
			System.out.println("The network wrongly guessed: " + maxInt + " The correct number was: " + (int) correctOutput);
			holder[(int) maxInt]++;	
		}

		OutputVector result = new OutputVector(correctOutput, maxInt);
		countOfImagesAnalyzed++;
		return result;
	}

	
	
	



	//----------------------START UTILITY METHODS---------------------------------------------------------------------------------------------
	
	//Takes an int and turns it into the letter that it represents
	public static String getCharForNumber(int i) {
	    return i > 9 && i < 37 ? String.valueOf((char)(i + 55)) : null;
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
		outputWriter.write("Hidden Layers: " + Integer.toString(NUMBER_OF_HIDDEN_LAYERS));
		outputWriter.newLine();
		outputWriter.write("Number of nodes in each hidden layer: " + Integer.toString(numberOfNodesInHiddenLayer));
		outputWriter.newLine();
		outputWriter.write("Number of training examples used: " + Integer.toString(numberOfInputNodes));
		outputWriter.newLine();
		double percentCorrect = (countOfCorrectImagesAnalyzed / countOfImagesAnalyzed) * 100;
		outputWriter.write("Analyzed " + countOfImagesAnalyzed + " images with " + percentCorrect + " percent accuracy.");
		outputWriter.newLine();
		outputWriter.write("Training time: " + executionTime + " milliseconds");
		outputWriter.newLine();
		outputWriter.write("Image data binary: " + binaryInput);
		outputWriter.newLine();
	//Writes  letters or numbers
		for (int i = 0; i < x.size(); i++) {
			outputWriter.write("Correct: " + x.get(i).getCorrect() + "  ");
			if(x.get(i).getNeuralNetOutput()>9){
				outputWriter.write("Neural net output: " + getCharForNumber(x.get(i).getNeuralNetOutput()) + "   ");
			}else{
				outputWriter.write("Neural net output: " + Integer.toString(x.get(i).getNeuralNetOutput()) + "   ");
			}
			
			if(x.get(i).getExpectedOutput()<=9){
				outputWriter.write("Expected output: " + Double.toString(x.get(i).getExpectedOutput()));
			}else{
				outputWriter.write("Expected output: " + getCharForNumber((int)x.get(i).getExpectedOutput()));
			}
			outputWriter.newLine();
		}
		
		for (int m = 0; m < holder.length; m++) {
			outputWriter.write("Number " + m+" was guessed " +holder[m]+ " times, when it should have guessed another number.");
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
		FileOutputStream fout2 = new FileOutputStream(filePathTrainedHiddenWeights);
		ObjectOutputStream oos2 = new ObjectOutputStream(fout2);
		oos2.writeObject(hiddenLayerNodes);
		oos.close();
		fout.close();
		oos2.close();
		fout2.close();
	}

	
	public static void readDataFromTrainedFiles() throws IOException, ClassNotFoundException {
		// Grabs weights to output nodes
		FileInputStream fin = new FileInputStream("/Users/zackeryleman/Desktop/NeuralNetOutput/TrainedSetOutputWeights.txt");
		ObjectInputStream ois = new ObjectInputStream(fin);
		outputLayerNodes = (ArrayList<ArrayList<Double>>) ois.readObject();
		fin.close();
		// Grabs weights to hidden nodes
		FileInputStream fin2 = new FileInputStream("/Users/zackeryleman/Desktop/NeuralNetOutput/TrainedSetHiddenWeights.txt");
		ObjectInputStream ois2 = new ObjectInputStream(fin2);
		hiddenLayerNodes = (ArrayList<ArrayList<Double>>) ois2.readObject();
		fin2.close();

	}
	
//----------------------END UTILITY METHODS-----------------------------------------------------------------------------------------------
	
//The two methods below are slightly altered methods from above,
	//tailored to starting and testing the network with captchas.
	//No need to read the code, as it is almost identical to the previous  methods
	
	/*
	
	public static void initializeMultilayerFeedForwardCaptcha() throws IOException {

		// Loads training and testing data sets
		loadCaptchaImage dataSets = new loadCaptchaImage();
		ArrayList<DigitImage> trainingData = dataSets.getTrainingData();
	
		
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
	

		// Looks at a representation of an image
		// and determines how many pixels and thus how many input nodes are
		// needed
		// (one per pixel)
		numberOfInputNodes = trainingData.get(0).getData().length;

		// Initialize weights with random values for all nodes in the first
		// hidden layer.
		for (int i = 0; i < numberOfNodesInHiddenLayer; i++) {
			ArrayList<Double> weights = new ArrayList<Double>(numberOfInputNodes);
			for (int j = 0; j < numberOfInputNodes; j++) {
				weights.add(random.nextGaussian());
			}
			hiddenLayerNodes.add(weights);
		}
		// Initialize weights with random values for all nodes in the output
		// layer.
		for (int i = 0; i < NUMBER_OF_OUTPUT_NODES; i++) {
			ArrayList<Double> weights = new ArrayList<Double>(numberOfNodesInHiddenLayer);
			for (int j = 0; j < numberOfNodesInHiddenLayer; j++) {
				weights.add(random.nextGaussian());
			}
			outputLayerNodes.add(weights);
		}

		// Trains the network with the training Data
		long startTime = System.currentTimeMillis();
		trainTheNetwork(trainingData);
		long endTime = System.currentTimeMillis();
		executionTime = endTime - startTime;
		System.out.println("Training time: " + executionTime + " milliseconds");
		// Creates data files that can be reused by the network without
		// retraining.
		writeTrainedWeights();

	}

	
	public static void testMultilayerFeedForwardCaptcha() throws IOException {
		countOfImagesAnalyzed=0;
		countOfCorrectImagesAnalyzed=0;
		// Loads testing data set
		//loadCaptchaImage dataSets = new loadCaptchaImage();
		//ArrayList<ArrayList<DigitImage>> testingData = dataSets.getTestingData();
		
		loadCaptchaImage dataSets = new loadCaptchaImage();
		ArrayList<DigitImage> testingData = dataSets.getTrainingData();
		
		// Tests the network with the testing Data and prints results to file
		//write(solveTestingDataCaptcha(testingData));
		solveTestingData(testingData);
		// reports network Performance
		double percentCorrect = (countOfCorrectImagesAnalyzed / countOfImagesAnalyzed) * 100;
		System.out.println("Analyzed " + countOfImagesAnalyzed + " images with " + percentCorrect + " percent accuracy.");
		System.out.println("Look in /Users/\"your username\"/Desktop/NeuralNetOutput  directory to find  the output.");
	}
	*/
	/*
	 * Takes an image and returns the results of the neural network on the Testing Data in an object that can then be read and written to a file
	 */
	/*(public static ArrayList<OutputVector> solveTestingDataCaptcha(ArrayList<ArrayList<DigitImage>> networkInputData) {
		ArrayList<OutputVector> newtworkResults = new ArrayList<OutputVector>();
		for (int i = 0; i < networkInputData.size(); i++) {
			for(int j=0; j<networkInputData.get(i).size();j++){
			newtworkResults.add(singleImageBestGuess(networkInputData.get(i), j));
			}
		}
		return newtworkResults;
	}
	*/
	
	
}
