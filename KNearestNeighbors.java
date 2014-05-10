


import java.io.IOException;
import java.lang.Math;
import java.util.ArrayList;

public class KNearestNeighbors {

	// Just look at 200 images for now
	public static int numberOfImagesToDebugWith;
	// Tracks running time of the hidden layer construction
	public static long executionTime;
	// The number of input nodes will be equal to the number of pixels in the image
	public static int numberOfInputNodes;
	// Create array of Nodes in first layer and associate done that points to the correct output
	public static ArrayList<ArrayList<Double>> hiddenLayerNodes = new ArrayList<ArrayList<Double>>();
	public static ArrayList<Integer> hiddenLayerToOutput = new ArrayList<Integer>();
	public static ArrayList<Double> hiddenLayerDottedOutputValues = new ArrayList<Double>();
	public static ArrayList<Double> hiddenLayerDottedOutputValues2 = new ArrayList<Double>();
	public static ArrayList<Double> hiddenLayerDottedOutputValues3 = new ArrayList<Double>();
	public static ArrayList<Double> hiddenLayerDottedOutputValues4 = new ArrayList<Double>();
	public static ArrayList<Double> hiddenLayerDottedOutputValues5 = new ArrayList<Double>();
	public static ArrayList<Double> hiddenLayerDottedOutputValues6 = new ArrayList<Double>();
	public static ArrayList<Double> hiddenLayerDottedOutputValues7 = new ArrayList<Double>();
	public static ArrayList<Double> hiddenLayerDottedOutputValues8 = new ArrayList<Double>();
	// Tracks the number of images correctly identified in the testing set.
	public static ArrayList<Integer> countOfCorrectImagesAnalyzed = new ArrayList<Integer>();
	// Tracks the number of images processed in the testing set.
	public static ArrayList<Integer> countOfImagesAnalyzed = new ArrayList<Integer>();
	
	


	public static ArrayList<DigitImage> testingData = new ArrayList<DigitImage>();

	//How many nearest Neighbors to use
	public static int k;
	// Is true if the input into the network consists of binary images. False if Grayscale.
	public static boolean binaryInput;
	public static final int TRAINING_SET_REDUCTION_FACTOR=10;
	public static int[]  holder=new int[10];
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		for (int m = 0; m < holder.length; m++) {
			holder[m]=0;
		}
		// usePriorWeights=Boolean.parseBolean(args[4]);
		// String trainingImages=args[7];
		// String testingImages=args[8];
		// String trainingLabels=args[9];
		// String testingLabels=args[10];
		// int k = Integer.parseInt(args[11]); 
		System.out.println("There are " +Runtime.getRuntime().availableProcessors()+ " cores avalible to the JVM.");
		System.out.println("Intel hyperthreading can be responsible for the apparent doubling  in cores.");
		// These are hard coded versions of the above
		String trainingImages = "Training-Images";
		String testingImages = "Testing-images";
		String trainingLabels = "Training-Labels";
		String testingLabels = "Testing-Labels";
		k = 3;
		binaryInput=false;
		numberOfImagesToDebugWith = 200;
		// Trains the network
		initializeKNearestNeighbours(trainingImages, trainingLabels);

		long startTime = System.currentTimeMillis();

		
		
		
		countOfImagesAnalyzed.add(0);
		countOfImagesAnalyzed.add(0);
		countOfImagesAnalyzed.add(0);
		countOfImagesAnalyzed.add(0);
		countOfImagesAnalyzed.add(0);
		countOfImagesAnalyzed.add(0);
		countOfImagesAnalyzed.add(0);
		countOfImagesAnalyzed.add(0);
		countOfCorrectImagesAnalyzed.add(0);
		countOfCorrectImagesAnalyzed.add(0);
		countOfCorrectImagesAnalyzed.add(0);
		countOfCorrectImagesAnalyzed.add(0);
		countOfCorrectImagesAnalyzed.add(0);
		countOfCorrectImagesAnalyzed.add(0);
		countOfCorrectImagesAnalyzed.add(0);
		countOfCorrectImagesAnalyzed.add(0);
		
		
		
		// Loads test data for the K-Nearest Neighbors Network
		testKNearestNeighbours(testingImages, testingLabels);

		
		Runnable r1 = new Runnable() {
			public void run() {
				//Tests the first quarter of the input data
				solveTestingData(testingData, k);
			}
		};
		Runnable r2 = new Runnable() {
			public void run() {
				//Tests the second fourth of the input data
				solveTestingData2(testingData, k);
			}
		};
		Runnable r3 = new Runnable() {
			public void run() {
				//Tests the third fourth of the input data
				solveTestingData3(testingData, k);
			}
		};
		Runnable r4 = new Runnable() {
			public void run() {
				//Tests the last fourth of the input data
				solveTestingData4(testingData, k);
			}
		};

		Runnable r5 = new Runnable() {
			public void run() {
				//Tests the first quarter of the input data
				solveTestingData5(testingData, k);
			}
		};
		Runnable r6 = new Runnable() {
			public void run() {
				//Tests the second fourth of the input data
				solveTestingData6(testingData, k);
			}
		};
		Runnable r7 = new Runnable() {
			public void run() {
				//Tests the third fourth of the input data
				solveTestingData7(testingData, k);
			}
		};
		Runnable r8 = new Runnable() {
			public void run() {
				//Tests the last fourth of the input data
				solveTestingData8(testingData, k);
			}
		};


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
		long endTime = System.currentTimeMillis();
		long executionTime = endTime - startTime;
		double countOfCorrectImagesAnalyzedTotal=0;
		double countOfImagesAnalyzedTotal=0;
		for(int x=0;x< countOfCorrectImagesAnalyzed.size();x++){
			countOfCorrectImagesAnalyzedTotal=countOfCorrectImagesAnalyzedTotal+countOfCorrectImagesAnalyzed.get(x);
		}
		for(int x=0;x< countOfImagesAnalyzed.size();x++){
			countOfImagesAnalyzedTotal=countOfImagesAnalyzedTotal+countOfImagesAnalyzed.get(x);
		}
		
		double percentCorrect = (countOfCorrectImagesAnalyzedTotal / countOfImagesAnalyzedTotal) * 100;
		System.out.println("Analyzed " + countOfImagesAnalyzedTotal + " images with " + percentCorrect + " percent accuracy.");
		System.out.println("Solution time: " + executionTime + " milliseconds");
		System.out.println("# Correct: " + countOfCorrectImagesAnalyzedTotal);
		for (int m = 0; m < holder.length; m++) {
			System.out.println("Number " + m+" was guessed " +holder[m]+ " times, when it should have guessed another number.");
		}

	}

	public static void initializeKNearestNeighbours(String trainingImages, String trainingLabels) throws IOException {

		// Loads training and testing data sets
		DigitImageLoadingService train = new DigitImageLoadingService(trainingLabels, trainingImages,binaryInput);
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
		// Currently dividing by 2 to only use a half of the training set so we don't run out of memory. We likely don't need that many anyway.
		for (int i = 0; i < trainingData.size()/TRAINING_SET_REDUCTION_FACTOR; i++) {
			ArrayList<Double> weights = new ArrayList<Double>(numberOfInputNodes);
			weights = trainingData.get(i).getArrayListData();
			hiddenLayerNodes.add(weights);
			hiddenLayerToOutput.add((int) trainingData.get(i).getLabel());
		}

		long endTime = System.currentTimeMillis();
		executionTime = endTime - startTime;
		System.out.println("Training time: " + executionTime + " milliseconds");

	}

	public static void testKNearestNeighbours(String testingImages, String testingLabels) throws IOException {

		// Loads testing data set
		DigitImageLoadingService test = new DigitImageLoadingService(testingLabels, testingImages,binaryInput);
		testingData = new ArrayList<DigitImage>();
		try {
			// Our data structure holds the testing data
			testingData = test.loadDigitImages();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	/*
	 * Returns the output from a given node after the input has been summed.It takes the layer that the node is in, the index of the node in the
	 * layer, and the output from the previous layer
	 */
	public static double nodeOutput(ArrayList<ArrayList<Double>> layerOfNodes, ArrayList<Double> outputFromPreviousLayer, int indexOfNodeinlayer) {
		double sum = 0;
		for (int i = 0; i < outputFromPreviousLayer.size(); i++) {
			double output= Math.abs((layerOfNodes.get(indexOfNodeinlayer).get(i) - outputFromPreviousLayer.get(i)));
			if(output<=20){
				output=1;
			}else{
				output=0;
			}
			sum = sum + output ;
		}
		return sum;
	}

	/* This returns an array representing the output of all nodes in the given layer */
	public static ArrayList<Double> outPutOfLayer(ArrayList<ArrayList<Double>> currentLayer, ArrayList<Double> outputFromPreviousLayer) {
		ArrayList<Double> outputOfCurrentlayer = new ArrayList<Double>();
		for (int i = 0; i < currentLayer.size(); i++) {
			double output;
			if(!binaryInput){
				output = nodeOutput(currentLayer, outputFromPreviousLayer, i);
			}else{
				output = nodeOutputBinary(currentLayer, outputFromPreviousLayer, i);
			}
			outputOfCurrentlayer.add(output);
		}
		return outputOfCurrentlayer;
	}

	public static void solveTestingData(ArrayList<DigitImage> networkInputData, int k) {
		//	long startTime = System.currentTimeMillis();
		for (int i = 0; i <numberOfImagesToDebugWith/8; i++) {
			ArrayList<Double> temp = networkInputData.get(i).getArrayListData();
			hiddenLayerDottedOutputValues = outPutOfLayer(hiddenLayerNodes, temp);


			//I IF K=1 just run the commented out code as it is faster.	
			double output = 0;
			if(k==1){	


				//Find which node has the maximum output and then
				//return the number that is at that node position in the associated output array.

				double currentMax = 0;
				for (int j = 0; j < hiddenLayerDottedOutputValues.size(); j++) {
					if (hiddenLayerDottedOutputValues.get(j) > currentMax) {
						currentMax = hiddenLayerDottedOutputValues.get(j);
						output = hiddenLayerToOutput.get(j);
					}
				}
			}
			else{

				int[] indicesOfDottedOutputList = new int[hiddenLayerDottedOutputValues.size()];
				ArrayList<Integer> bestKOutputs = new ArrayList<Integer>();


				initializeIndices(indicesOfDottedOutputList);
				parallelSorting(indicesOfDottedOutputList, hiddenLayerDottedOutputValues);
				findBestKOutputs(indicesOfDottedOutputList, hiddenLayerToOutput, bestKOutputs, k);
				output = findMostCommonOccurrenceAmongKOutputs(bestKOutputs);
			}
			System.out.println("Guess using the closest match: " + output);
			double number = networkInputData.get(i).getLabel();
			System.out.println("Thread: 1" );
			System.out.println("Correct answer: " + number);

			countOfImagesAnalyzed.set(0,countOfImagesAnalyzed.get(0)+1);
			if (number == output) {
				countOfCorrectImagesAnalyzed.set(0,countOfCorrectImagesAnalyzed.get(0)+1);
				System.out.println("Network was Correct");
			} else {
				System.out.println(" Network was Wrong");
				holder[(int) output]++;	
			}
			System.out.println(" ");
		}

	}

	// Initialize the ordered indicies for the hiddenLayerDottedOuput list
	public static void initializeIndices (int[] indicesArray) {
		for (int index = 0; index < indicesArray.length; index++) {
			indicesArray[index] = index;
		}
	}

	public static void parallelSorting(int[] indicesToBeSorted, ArrayList<Double> listToBeSorted) {
		for (int i = 0; i < listToBeSorted.size(); i++) {
			for (int j = i + 1; j < listToBeSorted.size(); j++) {
				// Swap so that bigger numbers go in the front.
				if (listToBeSorted.get(j) > listToBeSorted.get(i)) {
					Double temp = new Double(listToBeSorted.get(i));
					listToBeSorted.set(i, listToBeSorted.get(j));
					listToBeSorted.set(j, temp);
					int tempIndex = i;
					indicesToBeSorted[i] = j; 
					indicesToBeSorted[j] = tempIndex; 
				}
			}
		}
	}

	// The bestKOutputsList is constructed from the sorted hiddenLaYerDottedOutput lists's indices and the 
	// values of hiddenLayerToOutput list at the corresponding indices. 	
	public static void findBestKOutputs(int[] sortedIndices, ArrayList<Integer> outputsList, ArrayList<Integer> bestKOutputsList, int k) {
		for (int i = 0; i < k; i++) {
			bestKOutputsList.add(outputsList.get(sortedIndices[i]));
		}
	}

	// This method finds the most commonly occurred output among the best K outputs.
	public static int findMostCommonOccurrenceAmongKOutputs (ArrayList<Integer> bestKOutputsList) {
		//This is simpler:
		int[]  holder=new int[10];
		for (int m = 0; m < holder.length; m++) {
			holder[m]=0;
		}
		for (int m = 0; m < bestKOutputsList.size(); m++) {
			holder[bestKOutputsList.get(m)]++;	
		}
		int mostCommonValue=0;
		int max=0;
		for (int m = 0; m < holder.length; m++) {
			if(holder[m]>max){
				max=holder[m];
				mostCommonValue=m;
			}
		}
		return mostCommonValue;


	}


	public static void solveTestingData2(ArrayList<DigitImage> networkInputData, int k) {


		//long startTime = System.currentTimeMillis();
		for (int i = (numberOfImagesToDebugWith*1)/8; i <(numberOfImagesToDebugWith*2)/8 ; i++) {
			ArrayList<Double> temp = networkInputData.get(i).getArrayListData();
			hiddenLayerDottedOutputValues2 = outPutOfLayer(hiddenLayerNodes, temp);


			//I IF K=1 just run the commented out code as it is faster.	
			double output = 0;
			if(k==1){	


				//Find which node has the maximum output and then
				//return the number that is at that node position in the associated output array.

				double currentMax = 0;
				for (int j = 0; j < hiddenLayerDottedOutputValues2.size(); j++) {
					if (hiddenLayerDottedOutputValues.get(j) > currentMax) {
						currentMax = hiddenLayerDottedOutputValues2.get(j);
						output = hiddenLayerToOutput.get(j);
					}
				}
			}
			else{

				int[] indicesOfDottedOutputList = new int[hiddenLayerDottedOutputValues2.size()];
				ArrayList<Integer> bestKOutputs = new ArrayList<Integer>();


				initializeIndices(indicesOfDottedOutputList);
				parallelSorting(indicesOfDottedOutputList, hiddenLayerDottedOutputValues2);
				findBestKOutputs(indicesOfDottedOutputList, hiddenLayerToOutput, bestKOutputs, k);
				output = findMostCommonOccurrenceAmongKOutputs(bestKOutputs);

				System.out.println("Guess using the closest match: " + output);
			}
			double number = networkInputData.get(i).getLabel();
			System.out.println("Thread: 2" );
			System.out.println("Correct answer: " + number);


			countOfImagesAnalyzed.set(1,countOfImagesAnalyzed.get(1)+1);
			if (number == output) {
				countOfCorrectImagesAnalyzed.set(1,countOfCorrectImagesAnalyzed.get(1)+1);
				System.out.println("Network was Correct");
			} else {
				System.out.println(" Network was Wrong");
				holder[(int) output]++;	
			}
			System.out.println(" ");
		}
	}



	public static void solveTestingData3(ArrayList<DigitImage> networkInputData, int k) {
		//long startTime = System.currentTimeMillis();
		for (int i =(numberOfImagesToDebugWith*2)/8; i <(numberOfImagesToDebugWith*3)/8 ; i++) {
			ArrayList<Double> temp = networkInputData.get((int)i).getArrayListData();
			hiddenLayerDottedOutputValues3 = outPutOfLayer(hiddenLayerNodes, temp);


			//I IF K=1 just run the commented out code as it is faster.	
			double output = 0;
			if(k==1){	


				//Find which node has the maximum output and then
				//return the number that is at that node position in the associated output array.

				double currentMax = 0;
				for (int j = 0; j < hiddenLayerDottedOutputValues3.size(); j++) {
					if (hiddenLayerDottedOutputValues3.get(j) > currentMax) {
						currentMax = hiddenLayerDottedOutputValues3.get(j);
						output = hiddenLayerToOutput.get(j);
					}
				}
			}
			else{

				int[] indicesOfDottedOutputList = new int[hiddenLayerDottedOutputValues3.size()];
				ArrayList<Integer> bestKOutputs = new ArrayList<Integer>();


				initializeIndices(indicesOfDottedOutputList);
				parallelSorting(indicesOfDottedOutputList, hiddenLayerDottedOutputValues3);
				findBestKOutputs(indicesOfDottedOutputList, hiddenLayerToOutput, bestKOutputs, k);
				output = findMostCommonOccurrenceAmongKOutputs(bestKOutputs);
			}
			System.out.println("Guess using the closest match: " + output);
			double number = networkInputData.get((int)i).getLabel();
			System.out.println("Thread: 3" );
			System.out.println("Correct answer: " + number);

			countOfImagesAnalyzed.set(2,countOfImagesAnalyzed.get(2)+1);
			if (number == output) {
				countOfCorrectImagesAnalyzed.set(2,countOfCorrectImagesAnalyzed.get(2)+1);
				System.out.println("Network was Correct");
			} else {
				System.out.println(" Network was Wrong");
				holder[(int) output]++;	
			}
			System.out.println(" ");
		}
	}


	public static void solveTestingData4(ArrayList<DigitImage> networkInputData, int k) {

		//long startTime = System.currentTimeMillis();
		for (int i = (numberOfImagesToDebugWith*3)/8; i <(numberOfImagesToDebugWith*4)/8 ; i++) {
			ArrayList<Double> temp = networkInputData.get(i).getArrayListData();
			hiddenLayerDottedOutputValues4 = outPutOfLayer(hiddenLayerNodes, temp);
			//I IF K=1 just run the commented out code as it is faster.	
			double output = 0;
			if(k==1){	
				//Find which node has the maximum output and then
				//return the number that is at that node position in the associated output array.

				double currentMax = 0;
				for (int j = 0; j < hiddenLayerDottedOutputValues4.size(); j++) {
					if (hiddenLayerDottedOutputValues4.get(j) > currentMax) {
						currentMax = hiddenLayerDottedOutputValues4.get(j);
						output = hiddenLayerToOutput.get(j);
					}
				}
			}
			else{
				int[] indicesOfDottedOutputList = new int[hiddenLayerDottedOutputValues4.size()];
				ArrayList<Integer> bestKOutputs = new ArrayList<Integer>();
				initializeIndices(indicesOfDottedOutputList);
				parallelSorting(indicesOfDottedOutputList, hiddenLayerDottedOutputValues4);
				findBestKOutputs(indicesOfDottedOutputList, hiddenLayerToOutput, bestKOutputs, k);
				output = findMostCommonOccurrenceAmongKOutputs(bestKOutputs);
			}
			System.out.println("Guess using the closest match: " + output);
			double number = networkInputData.get(i).getLabel();
			System.out.println("Thread: 4" );
			System.out.println("Correct answer: " + number);

			countOfImagesAnalyzed.set(3,countOfImagesAnalyzed.get(3)+1);
			if (number == output) {
				countOfCorrectImagesAnalyzed.set(3,countOfCorrectImagesAnalyzed.get(3)+1);
				System.out.println("Network was Correct");
			} else {
				System.out.println(" Network was Wrong");
				holder[(int) output]++;	
			}
			System.out.println(" ");
		}
	}

	public static void solveTestingData5(ArrayList<DigitImage> networkInputData, int k) {

		//long startTime = System.currentTimeMillis();
		for (int i = (numberOfImagesToDebugWith*4)/8; i <(numberOfImagesToDebugWith*5)/8 ; i++) {
			ArrayList<Double> temp = networkInputData.get(i).getArrayListData();
			hiddenLayerDottedOutputValues5 = outPutOfLayer(hiddenLayerNodes, temp);
			//I IF K=1 just run the commented out code as it is faster.	
			double output = 0;
			if(k==1){	
				//Find which node has the maximum output and then
				//return the number that is at that node position in the associated output array.

				double currentMax = 0;
				for (int j = 0; j < hiddenLayerDottedOutputValues5.size(); j++) {
					if (hiddenLayerDottedOutputValues5.get(j) > currentMax) {
						currentMax = hiddenLayerDottedOutputValues5.get(j);
						output = hiddenLayerToOutput.get(j);
					}
				}
			}
			else{
				int[] indicesOfDottedOutputList = new int[hiddenLayerDottedOutputValues5.size()];
				ArrayList<Integer> bestKOutputs = new ArrayList<Integer>();
				initializeIndices(indicesOfDottedOutputList);
				parallelSorting(indicesOfDottedOutputList, hiddenLayerDottedOutputValues5);
				findBestKOutputs(indicesOfDottedOutputList, hiddenLayerToOutput, bestKOutputs, k);
				output = findMostCommonOccurrenceAmongKOutputs(bestKOutputs);
			}
			System.out.println("Guess using the closest match: " + output);
			double number = networkInputData.get(i).getLabel();
			System.out.println("Thread: 5" );
			System.out.println("Correct answer: " + number);

			countOfImagesAnalyzed.set(3,countOfImagesAnalyzed.get(3)+1);
			if (number == output) {
				countOfCorrectImagesAnalyzed.set(3,countOfCorrectImagesAnalyzed.get(3)+1);
				System.out.println("Network was Correct");
			} else {
				System.out.println(" Network was Wrong");
				holder[(int) output]++;	
			}
			System.out.println(" ");
		}
	}
	public static void solveTestingData6(ArrayList<DigitImage> networkInputData, int k) {

		//long startTime = System.currentTimeMillis();
		for (int i = (numberOfImagesToDebugWith*5)/8; i <(numberOfImagesToDebugWith*6)/8 ; i++) {
			ArrayList<Double> temp = networkInputData.get(i).getArrayListData();
			hiddenLayerDottedOutputValues6 = outPutOfLayer(hiddenLayerNodes, temp);
			//I IF K=1 just run the commented out code as it is faster.	
			double output = 0;
			if(k==1){	
				//Find which node has the maximum output and then
				//return the number that is at that node position in the associated output array.

				double currentMax = 0;
				for (int j = 0; j < hiddenLayerDottedOutputValues6.size(); j++) {
					if (hiddenLayerDottedOutputValues6.get(j) > currentMax) {
						currentMax = hiddenLayerDottedOutputValues6.get(j);
						output = hiddenLayerToOutput.get(j);
					}
				}
			}
			else{
				int[] indicesOfDottedOutputList = new int[hiddenLayerDottedOutputValues6.size()];
				ArrayList<Integer> bestKOutputs = new ArrayList<Integer>();
				initializeIndices(indicesOfDottedOutputList);
				parallelSorting(indicesOfDottedOutputList, hiddenLayerDottedOutputValues6);
				findBestKOutputs(indicesOfDottedOutputList, hiddenLayerToOutput, bestKOutputs, k);
				output = findMostCommonOccurrenceAmongKOutputs(bestKOutputs);
			}
			System.out.println("Guess using the closest match: " + output);
			double number = networkInputData.get(i).getLabel();
			System.out.println("Thread: 6" );
			System.out.println("Correct answer: " + number);

			countOfImagesAnalyzed.set(3,countOfImagesAnalyzed.get(3)+1);
			if (number == output) {
				countOfCorrectImagesAnalyzed.set(3,countOfCorrectImagesAnalyzed.get(3)+1);
				System.out.println("Network was Correct");
			} else {
				System.out.println(" Network was Wrong");
				holder[(int) output]++;	
			}
			System.out.println(" ");
		}
	}
	public static void solveTestingData7(ArrayList<DigitImage> networkInputData, int k) {

		//long startTime = System.currentTimeMillis();
		for (int i = (numberOfImagesToDebugWith*6)/8; i <(numberOfImagesToDebugWith*7)/8 ; i++) {
			ArrayList<Double> temp = networkInputData.get(i).getArrayListData();
			hiddenLayerDottedOutputValues7 = outPutOfLayer(hiddenLayerNodes, temp);
			//I IF K=1 just run the commented out code as it is faster.	
			double output = 0;
			if(k==1){	
				//Find which node has the maximum output and then
				//return the number that is at that node position in the associated output array.

				double currentMax = 0;
				for (int j = 0; j < hiddenLayerDottedOutputValues7.size(); j++) {
					if (hiddenLayerDottedOutputValues7.get(j) > currentMax) {
						currentMax = hiddenLayerDottedOutputValues7.get(j);
						output = hiddenLayerToOutput.get(j);
					}
				}
			}
			else{
				int[] indicesOfDottedOutputList = new int[hiddenLayerDottedOutputValues7.size()];
				ArrayList<Integer> bestKOutputs = new ArrayList<Integer>();
				initializeIndices(indicesOfDottedOutputList);
				parallelSorting(indicesOfDottedOutputList, hiddenLayerDottedOutputValues7);
				findBestKOutputs(indicesOfDottedOutputList, hiddenLayerToOutput, bestKOutputs, k);
				output = findMostCommonOccurrenceAmongKOutputs(bestKOutputs);
			}
			System.out.println("Guess using the closest match: " + output);
			double number = networkInputData.get(i).getLabel();
			System.out.println("Thread: 7" );
			System.out.println("Correct answer: " + number);

			countOfImagesAnalyzed.set(3,countOfImagesAnalyzed.get(3)+1);
			if (number == output) {
				countOfCorrectImagesAnalyzed.set(3,countOfCorrectImagesAnalyzed.get(3)+1);
				System.out.println("Network was Correct");
			} else {
				System.out.println(" Network was Wrong");
				holder[(int) output]++;	
			}
			System.out.println(" ");
		}
	}

	public static void solveTestingData8(ArrayList<DigitImage> networkInputData, int k) {

		//long startTime = System.currentTimeMillis();
		for (int i = (numberOfImagesToDebugWith*7)/8; i < numberOfImagesToDebugWith ; i++) {
			ArrayList<Double> temp = networkInputData.get(i).getArrayListData();
			hiddenLayerDottedOutputValues8 = outPutOfLayer(hiddenLayerNodes, temp);
			//I IF K=1 just run the commented out code as it is faster.	
			double output = 0;
			if(k==1){	
				//Find which node has the maximum output and then
				//return the number that is at that node position in the associated output array.

				double currentMax = 0;
				for (int j = 0; j < hiddenLayerDottedOutputValues.size(); j++) {
					if (hiddenLayerDottedOutputValues8.get(j) > currentMax) {
						currentMax = hiddenLayerDottedOutputValues.get(j);
						output = hiddenLayerToOutput.get(j);
					}
				}
			}
			else{
				int[] indicesOfDottedOutputList = new int[hiddenLayerDottedOutputValues8.size()];
				ArrayList<Integer> bestKOutputs = new ArrayList<Integer>();
				initializeIndices(indicesOfDottedOutputList);
				parallelSorting(indicesOfDottedOutputList, hiddenLayerDottedOutputValues8);
				findBestKOutputs(indicesOfDottedOutputList, hiddenLayerToOutput, bestKOutputs, k);
				output = findMostCommonOccurrenceAmongKOutputs(bestKOutputs);
			}
			System.out.println("Guess using the closest match: " + output);
			double number = networkInputData.get(i).getLabel();
			System.out.println("Thread: 8" );
			System.out.println("Correct answer: " + number);

			countOfImagesAnalyzed.set(3,countOfImagesAnalyzed.get(3)+1);
			if (number == output) {
				countOfCorrectImagesAnalyzed.set(3,countOfCorrectImagesAnalyzed.get(3)+1);
				System.out.println("Network was Correct");
			} else {
				System.out.println(" Network was Wrong"); 
				holder[(int) output]++;	
			}
			System.out.println(" ");
		}
	}

	/*
	 * Returns the output from a given node after the input has been summed.It takes the layer that the node is in, the index of the node in the
	 * layer, and the output from the previous layer
	 */
	public static double nodeOutputBinary(ArrayList<ArrayList<Double>> layerOfNodes, ArrayList<Double> outputFromPreviousLayer, int indexOfNodeinlayer) {
		double sum = 0;
		for (int i = 0; i < outputFromPreviousLayer.size(); i++) {
			//This searches for a match with any adjacent pixels
			//(increases likely hood to match binary images) (like the diffuse border on grayscale images)
			double blob=0;
			blob = blob + outputFromPreviousLayer.get(i);

			if(!(i<=30)){
				blob = blob + outputFromPreviousLayer.get(i-1);
				blob = blob + outputFromPreviousLayer.get(i-2);
				blob = blob + outputFromPreviousLayer.get(i-26);
				blob = blob + outputFromPreviousLayer.get(i-27);
				blob = blob + outputFromPreviousLayer.get(i-28);
				blob = blob + outputFromPreviousLayer.get(i-29);
				blob = blob + outputFromPreviousLayer.get(i-30);


			}
			if(!(i>=754)){
				blob = blob + outputFromPreviousLayer.get(i+1);
				blob = blob + outputFromPreviousLayer.get(i+2);
				blob = blob + outputFromPreviousLayer.get(i+26);
				blob = blob + outputFromPreviousLayer.get(i+27);
				blob = blob + outputFromPreviousLayer.get(i+28); 
				blob = blob + outputFromPreviousLayer.get(i+29);
				blob = blob + outputFromPreviousLayer.get(i+30);
			}


			if(blob>=1){
				blob=1;
			}
			double output= Math.abs((layerOfNodes.get(indexOfNodeinlayer).get(i) - blob));

			if(output==0){  
				output=1;
			}else{
				output=0;
			}

			sum = sum + output ;

		}
		return sum;
	}
	
}
	/*public static void solveTestingDataXX(ArrayList<DigitImage> networkInputData, int k) {

		//long startTime = System.currentTimeMillis();
		for (int i = 0; i < numberOfImagesToDebugWith; i++) {
			ArrayList<Double> temp = networkInputData.get(i).getArrayListData();
			hiddenLayerDottedOutputValues4 = outPutOfLayer(hiddenLayerNodes, temp);
			//I IF K=1 just run the commented out code as it is faster.	
			double output = 0;
			if(k==1){	
				//Find which node has the maximum output and then
				//return the number that is at that node position in the associated output array.

				double currentMax = 0;
				for (int j = 0; j < hiddenLayerDottedOutputValues.size(); j++) {
					if (hiddenLayerDottedOutputValues.get(j) > currentMax) {
						currentMax = hiddenLayerDottedOutputValues.get(j);
						output = hiddenLayerToOutput.get(j);
					}
				}
			}
			else{
				int[] indicesOfDottedOutputList = new int[hiddenLayerDottedOutputValues4.size()];
				ArrayList<Integer> bestKOutputs = new ArrayList<Integer>();
				initializeIndices(indicesOfDottedOutputList);
				parallelSorting(indicesOfDottedOutputList, hiddenLayerDottedOutputValues4);
				findBestKOutputs(indicesOfDottedOutputList, hiddenLayerToOutput, bestKOutputs, k);
				output = findMostCommonOccurrenceAmongKOutputs(bestKOutputs);
			}
			System.out.println("Guess using the closest match: " + output);
			double number = networkInputData.get(i).getLabel();
			System.out.println("Correct answer4: " + number);

			countOfImagesAnalyzed.set(3,countOfImagesAnalyzed.get(3)+1);
			if (number == output) {
				countOfCorrectImagesAnalyzed.set(3,countOfCorrectImagesAnalyzed.get(3)+1);
				System.out.println("Network was Correct");
			} else {
				System.out.println(" Network was Wrong");
			}
			System.out.println(" ");
		}
	}*/


