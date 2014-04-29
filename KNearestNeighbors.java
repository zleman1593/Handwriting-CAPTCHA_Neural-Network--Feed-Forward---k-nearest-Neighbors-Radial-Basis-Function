
//TODO (IVY): The "solveTestingData" method needs to be written to use the k-nearest neighbors. Currently only uses the closest neigbor.

import java.util.*;
import java.io.IOException;
import java.lang.Math;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.*;

public class KNearestNeighbors {
	// Tracks the number of images processed in the testing set.
	public static double countOfImagesAnalyzed = 0;
	// Tracks the number of images correctly identified in the testing set.
	public static double countOfCorrectImagesAnalyzed = 0;
	// Tracks running time of the hidden layer construction
	public static long executionTime;
	// The number of input nodes will be equal to the number of pixels in the image
	public static int numberOfInputNodes;
	// Create array of Nodes in first layer and associate done that points to the correct output
	public static ArrayList<ArrayList<Double>> hiddenLayerNodes = new ArrayList<ArrayList<Double>>();
	public static ArrayList<Double> hiddenLayerToOutput = new ArrayList<Double>();
	public static ArrayList<Double> hiddenLayerDotedOutputValues = new ArrayList<Double>();

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		// usePriorWeights=Boolean.parseBolean(args[4]);
		// String trainingImages=args[7];
		// String testingImages=args[8];
		// String trainingLabels=args[9];
		// String testingLabels=args[10];

		// These are hard coded versions of the above
		String trainingImages = "Training-Images";
		String testingImages = "Testing-images";
		String trainingLabels = "Training-Labels";
		String testingLabels = "Testing-Labels";

		initializeKNearestNeighbours(trainingImages, trainingLabels);

		// Test the test K-Nearest Neighbors Network
		testKNearestNeighbours(testingImages, testingLabels);
	}

	public static void initializeKNearestNeighbours(String trainingImages, String trainingLabels) throws IOException {

		// Loads training and testing data sets
		DigitImageLoadingService train = new DigitImageLoadingService(trainingLabels, trainingImages);
		ArrayList<DigitImage> trainingData = new ArrayList<DigitImage>();
		try {
			// Our data structure holds the training data
			trainingData = train.loadDigitImages();
			// Alters data into proper form
			for (int i = 0; i < trainingData.size(); i++) {
				trainingData.get(i).vectorizeTrainingData();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Looks at a representation of an image
		// and determines how many pixels and thus how many input nodes are needed
		// (one per pixel)
		numberOfInputNodes = trainingData.get(0).getData().length;

		long startTime = System.currentTimeMillis();
		// Initialize weights with values corresponding to the binary pixel value for all nodes in the first hidden layer.
		// Currently dividing by 3 to only use a third of the training set so we don't run out of memory. We likely don't need that many anyway.
		for (int i = 0; i < trainingData.size() / 3; i++) {
			ArrayList<Double> weights = new ArrayList<Double>(numberOfInputNodes);
			weights = trainingData.get(i).getArrayListData();
			hiddenLayerNodes.add(weights);
			hiddenLayerToOutput.add((double) trainingData.get(i).getLabel());
		}

		long endTime = System.currentTimeMillis();
		executionTime = endTime - startTime;
		System.out.println("Training time: " + executionTime + " milliseconds");

	}

	public static void testKNearestNeighbours(String testingImages, String testingLabels) throws IOException {

		// Loads testing data set
		DigitImageLoadingService test = new DigitImageLoadingService(testingLabels, testingImages);
		ArrayList<DigitImage> testingData = new ArrayList<DigitImage>();
		try {
			// Our data structure holds the testing data
			testingData = test.loadDigitImages();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Tests the network with the testing Data and prints results to file
		// write(solveTestingData(testingData));
		solveTestingData(testingData);

		// reports network Performance
		double percentCorrect = (countOfCorrectImagesAnalyzed / countOfImagesAnalyzed) * 100;
		System.out.println("Analyzed " + countOfImagesAnalyzed + " images with " + percentCorrect + " percent accuracy.");
	}

	/*
	 * Returns the output from a given node after the input has been summed.It takes the layer that the node is in, the index of the node in the
	 * layer, and the output from the previous layer
	 */
	public static double nodeOutput(ArrayList<ArrayList<Double>> layerOfNodes, ArrayList<Double> outputFromPreviousLayer, int indexOfNodeinlayer) {
		double sum = 0;
		for (int i = 0; i < outputFromPreviousLayer.size(); i++) {
			// sum=sum+ Math.abs((layerOfNodes.get(indexOfNodeinlayer).get(i) - outputFromPreviousLayer.get(i)));
			sum = sum + (layerOfNodes.get(indexOfNodeinlayer).get(i) * outputFromPreviousLayer.get(i));
		}
		return sum;
	}

	/* This returns an array representing the output of all nodes in the given layer */
	public static ArrayList<Double> outPutOfLayer(ArrayList<ArrayList<Double>> currentLayer, ArrayList<Double> outputFromPreviousLayer) {
		ArrayList<Double> outputOfCurrentlayer = new ArrayList<Double>();
		for (int i = 0; i < currentLayer.size(); i++) {
			double output = nodeOutput(currentLayer, outputFromPreviousLayer, i);
			outputOfCurrentlayer.add(output);
		}
		return outputOfCurrentlayer;
	}

	public static void solveTestingData(ArrayList<DigitImage> networkInputData) {
		// Just look at 20 images for now
		int numberOfImagesToDebugWith = 20;

		long startTime = System.currentTimeMillis();
		for (int i = 1; i <= numberOfImagesToDebugWith; i++) {
			ArrayList<Double> temp = networkInputData.get(i).getArrayListData();
			hiddenLayerDotedOutputValues = outPutOfLayer(hiddenLayerNodes, temp);

			// Find the ten nodes with the highest output value
			QuickSelect o = new QuickSelect();
			int k = hiddenLayerDotedOutputValues.size();

				//FIX-THROWS ERROS----------------------------------------------------------------
	//The idea here is to find the ten nodes with the highest output values using quicksort in the unsorted ArrayList  "hiddenLayerDotedOutputValues".
	//Once those nodes are identified (i.e we know the index of the ten nodes with the highest values in the array "hiddenLayerDotedOutputValues")
	// we will find the corresponding output value (number to guess that) is at the equivalent index in the array "hiddenLayerToOutput".
	//Whatever number 0-9 is the most common out of those ten numbers indicated, becomes the network output.			
				
				/*		
				System.out.println( o.position(hiddenLayerDotedOutputValues,k));
				System.out.println( o.position(hiddenLayerDotedOutputValues,k-1));
				System.out.println( o.position(hiddenLayerDotedOutputValues,k-2));
				System.out.println( o.position(hiddenLayerDotedOutputValues,k-3));
			System.out.println( o.position(hiddenLayerDotedOutputValues,k-4));
				System.out.println( o.position(hiddenLayerDotedOutputValues,k-5));
				System.out.println( o.position(hiddenLayerDotedOutputValues,k-6));
				System.out.println( o.position(hiddenLayerDotedOutputValues,k-7));
					System.out.println( o.position(hiddenLayerDotedOutputValues,k-8));
				System.out.println( o.position(hiddenLayerDotedOutputValues,k-9));
				System.out.println( o.position(hiddenLayerDotedOutputValues,k-10));
				System.out.println(" ");
				*/

				
				//To easy debugging this will print the the ten guesses of the correct number from ten nearest neighbors
				
				/*System.out.println(hiddenLayerToOutput.get( o.position(hiddenLayerDotedOutputValues,k)));
				System.out.println(hiddenLayerToOutput.get( o.position(hiddenLayerDotedOutputValues,k-1)));
				System.out.println(hiddenLayerToOutput.get( o.position(hiddenLayerDotedOutputValues,k-2)));
				System.out.println(hiddenLayerToOutput.get( o.position(hiddenLayerDotedOutputValues,k-3)));
				System.out.println(hiddenLayerToOutput.get( o.position(hiddenLayerDotedOutputValues,k-4)));
				System.out.println(hiddenLayerToOutput.get( o.position(hiddenLayerDotedOutputValues,k-5)));
				System.out.println(hiddenLayerToOutput.get( o.position(hiddenLayerDotedOutputValues,k-6)));
				System.out.println(hiddenLayerToOutput.get( o.position(hiddenLayerDotedOutputValues,k-7)));
				System.out.println(hiddenLayerToOutput.get( o.position(hiddenLayerDotedOutputValues,k-8)));
				System.out.println(hiddenLayerToOutput.get( o.position(hiddenLayerDotedOutputValues,k-9)));*/
				
				//Next: Write the code for:
				//Find what number the majority of ten nodes thinks the input number is closest to and output that guess
				
			//FIX: THROWS ERROS----------------------------------------------------------------
				
				
				
				
				
				
			//The below code between the two lines uses only one nearest neighbor
			//and will be deleted on the k-nearest Neighbor code is complete.
			//-----------------------------------------
				
			//Find which node has the maximum output and then
			//return the number that is at that node position in the associated output array.
			double currentOutput = 0;
			double currentMax = 0;
			for (int j = 0; j < hiddenLayerDotedOutputValues.size(); j++) {
				if (hiddenLayerDotedOutputValues.get(j) > currentMax) {
					currentMax = hiddenLayerDotedOutputValues.get(j);
					currentOutput = hiddenLayerToOutput.get(j);
				}
			}
			System.out.println("Guess using the closest match: " + currentOutput);
			double number = networkInputData.get(i).getLabel();
			System.out.println("Correct answer: " + number);

			countOfImagesAnalyzed++;
			if (number == currentOutput) {
				countOfCorrectImagesAnalyzed++;
				System.out.println("Network was Correct");
			} else {
				System.out.println(" Network was Wrong");
			}
			System.out.println(" ");
		}
		// -----------------------------------------

		long endTime = System.currentTimeMillis();
		executionTime = endTime - startTime;
		System.out.println("Running time for " + countOfImagesAnalyzed + " images: " + executionTime + " milliseconds");

	}

}
