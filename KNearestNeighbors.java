/*
 * Hand Writing Recognition and Simple CAPTCHA Neural Network
 * CS 3425 Final project
 * Spring 2014
 * Min "Ivy" Xing, Zackery Leman
 * This is a K-Nearest Neighbor network that reads the MNIST data.
 * Notes: The way threads are  implemented, the code is optimized for either 8 or 24 real cores. 
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Math;
import java.util.ArrayList;

public class KNearestNeighbors {

	// Tells the network how many images in the test set should be processed
	public static int numberOfImagesToTest;
	// Tracks running time of the hidden layer construction
	public static long executionTime;
	// The number of input nodes will be equal to the number of pixels in the image
	public static int numberOfInputNodes;
	// Create array of Nodes in first layer and associated one that points to the correct output
	public static ArrayList<ArrayList<Double>> hiddenLayerNodes = new ArrayList<ArrayList<Double>>();
	public static ArrayList<Integer> hiddenLayerToOutput = new ArrayList<Integer>();
	
	// Holds the image data set that will be used to test the network
	public static ArrayList<DigitImage> testingData = new ArrayList<DigitImage>();
	//How many nearest Neighbors to use.
	public static int k;
	// Is true if the input into the network consists of binary images. False if Grayscale.
	public static boolean binaryInput;
	// set to one to use all of the training data to train the network. The number of training examples  is divided by this number
	public  static int trainingSetReductionFactor;
	//Sets up an array that will allow us to keep track of the number of wrong guesses for each number
	public static int[]  holder=new int[10];
	
	//These allow each thread to manipulate its own version of a similar data structure
	public static ArrayList<ArrayList<Double>> hiddenLayerDottedOutputValuesHolderArray = new ArrayList<ArrayList<Double>>();

	
	// Tracks the number of images correctly identified in the testing set.
	public static ArrayList<Integer> countOfCorrectImagesAnalyzed = new ArrayList<Integer>();
	// Tracks the number of images processed in the testing set.
	public static ArrayList<Integer> countOfImagesAnalyzed = new ArrayList<Integer>();
	
	public static  double countOfImagesAnalyzedTotal=0;
	
	public static  double countOfCorrectImagesAnalyzedTotal=0;
	
	public static String filePathResults;
	
	public static long trainingTime;
	
	public static final int NUMBER_OF_CORES=24;
	//These are just constants
	public static final String  trainingImages = "Training-Images";
	public static final	String testingImages = "Testing-images";
	public static final	String trainingLabels = "Training-Labels";
	public static final	String testingLabels = "Testing-Labels";
	
	public KNearestNeighbors(int k1,boolean binaryInput1, int trainingSetReductionFactor1, int numberOfImagesToTest1, String filePathResults1) throws IOException, ClassNotFoundException {
		//These lines are needed to prevent errors where objects refrence
		//each others' data structures
		hiddenLayerNodes.clear();
		hiddenLayerToOutput.clear();
		hiddenLayerDottedOutputValuesHolderArray.clear();
		countOfImagesAnalyzed.clear();
		countOfCorrectImagesAnalyzed.clear();
		testingData.clear();
		
	k = k1;
	binaryInput=binaryInput1;
	trainingSetReductionFactor=trainingSetReductionFactor1;
	numberOfImagesToTest = numberOfImagesToTest1;
	filePathResults=filePathResults1;

		//Sets up an array that will allow us to keep track of the number of wrong guesses for each number
		for (int m = 0; m < holder.length; m++) {
			holder[m]=0;
		}
		for (int m = 0; m < NUMBER_OF_CORES; m++) {
			 ArrayList<Double> hiddenLayerDottedOutputValues = new ArrayList<Double>();
		hiddenLayerDottedOutputValuesHolderArray.add(hiddenLayerDottedOutputValues);
		}
		
		
		System.out.println("There are " +Runtime.getRuntime().availableProcessors()+ " cores avalible to the JVM.");
		System.out.println("Intel hyperthreading can be responsible for the apparent doubling  in cores.");
	
		
		
	

		
		if(!binaryInput){
			System.out.println("It is normal for this network to not preform well with binary data.");
			}
		
		// "Train" the network AKA create hidden layer
		long startTime = System.currentTimeMillis();
		trainKNearestNeighbours();

		
		
		//Sets up trackers for each thread
		for(int y=0; y<NUMBER_OF_CORES;y++){
			countOfImagesAnalyzed.add(0);
			countOfCorrectImagesAnalyzed.add(0);
		}
		
		
		
		
		// Loads test data for the K-Nearest Neighbors Network
		loadtestDataForKNearestNeighbours(testingImages, testingLabels);
		 startTime = System.currentTimeMillis();
		 if(NUMBER_OF_CORES==8){
			 eightCore();
		 }else if (NUMBER_OF_CORES==24) {
			 twentyFourCore();
		 }else{
			 System.out.println("There are not 24 or 8 cores?");
		 }
		
		 long endTime = System.currentTimeMillis();
		 executionTime = endTime - startTime;
		
		 countOfCorrectImagesAnalyzedTotal=0;
		 countOfImagesAnalyzedTotal=0;
		
		for(int x=0;x< countOfCorrectImagesAnalyzed.size();x++){
			countOfCorrectImagesAnalyzedTotal=countOfCorrectImagesAnalyzedTotal+countOfCorrectImagesAnalyzed.get(x);
		}
		for(int x=0;x< countOfImagesAnalyzed.size();x++){
			countOfImagesAnalyzedTotal=countOfImagesAnalyzedTotal+countOfImagesAnalyzed.get(x);
		}
		
		//Summarizes results
		double percentCorrect = (countOfCorrectImagesAnalyzedTotal / countOfImagesAnalyzedTotal) * 100;
		System.out.println("Analyzed " + countOfImagesAnalyzedTotal + " images with " + percentCorrect + " percent accuracy.");
		System.out.println("Solution time: " + executionTime + " milliseconds");
		System.out.println("# Correct: " + countOfCorrectImagesAnalyzedTotal);
		
		write();
		//Prints out the stats for each number
		for (int m = 0; m < holder.length; m++) {
			System.out.println("Number " + m+" was guessed " +holder[m]+ " times, when it should have guessed another number.");
		}

	}

	public static void trainKNearestNeighbours() throws IOException {

		// Loads training and testing data sets
		DigitImageLoadingService train = new DigitImageLoadingService(trainingLabels, trainingImages,binaryInput);
		ArrayList<DigitImage> trainingData = new ArrayList<DigitImage>();
		try {
			// Our data structure holds the training data
			trainingData = train.loadDigitImages();
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
		for (int i = 0; i < trainingData.size()/trainingSetReductionFactor; i++) {
			ArrayList<Double> weights = new ArrayList<Double>(numberOfInputNodes);
			weights = trainingData.get(i).getArrayListData();
			hiddenLayerNodes.add(weights);
			hiddenLayerToOutput.add((int) trainingData.get(i).getLabel());
		}

		long endTime = System.currentTimeMillis();
		trainingTime = endTime - startTime;
		System.out.println("Training time: " + trainingTime + " milliseconds");

	}

	public static void loadtestDataForKNearestNeighbours(String testingImages, String testingLabels) throws IOException {

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

	
	public static void solveTestingData(ArrayList<DigitImage> networkInputData, int k, int thread, int startIndex, int EndIndex) {
		//	long startTime = System.currentTimeMillis();
		for (int i = startIndex; i <EndIndex; i++) {
			ArrayList<Double> temp = networkInputData.get(i).getArrayListData();
			hiddenLayerDottedOutputValuesHolderArray.set(thread,outPutOfLayer(hiddenLayerNodes, temp));


			//I IF K=1 just run this code because it is faster	
			double output = 0;
			if(k==1){	


				//Find which node has the maximum output and then
				//return the number that is at that node position in the associated output array.

				double currentMax = 0;
				for (int j = 0; j < hiddenLayerDottedOutputValuesHolderArray.get(thread).size(); j++) {
					if (hiddenLayerDottedOutputValuesHolderArray.get(thread).get(j) > currentMax) {
						currentMax = hiddenLayerDottedOutputValuesHolderArray.get(thread).get(j);
						output = hiddenLayerToOutput.get(j);
					}
				}
			}
			else{

				int[] indicesOfDottedOutputList = new int[hiddenLayerDottedOutputValuesHolderArray.get(thread).size()];
				ArrayList<Integer> bestKOutputs = new ArrayList<Integer>();


				initializeIndices(indicesOfDottedOutputList);
				parallelSorting(indicesOfDottedOutputList, hiddenLayerDottedOutputValuesHolderArray.get(thread));
				findBestKOutputs(indicesOfDottedOutputList, hiddenLayerToOutput, bestKOutputs, k);
				output = findMostCommonOccurrenceAmongKOutputs(bestKOutputs);
			}
			System.out.println("Guess using the closest match: " + output);
			double number = networkInputData.get(i).getLabel();
			System.out.println("Thread: "+ thread );
			System.out.println("Correct answer: " + number);

			countOfImagesAnalyzed.set(thread,countOfImagesAnalyzed.get(thread)+1);
			if (number == output) {
				countOfCorrectImagesAnalyzed.set(thread,countOfCorrectImagesAnalyzed.get(thread)+1);
				System.out.println("Network was Correct");
			} else {
				System.out.println(" Network was Wrong");
				holder[(int) output]++;	
			}
			System.out.println(" ");
		}

	}

	
	
	/* This returns an array representing the output of all nodes in the given layer */
	public static ArrayList<Double> outPutOfLayer(ArrayList<ArrayList<Double>> currentLayer, ArrayList<Double> outputFromPreviousLayer) {
		ArrayList<Double> outputOfCurrentlayer = new ArrayList<Double>();
		for (int i = 0; i < currentLayer.size(); i++) {
			double output;
			output = nodeOutput(currentLayer, outputFromPreviousLayer, i);
			outputOfCurrentlayer.add(output);
		}
		return outputOfCurrentlayer;
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

	
	//----------------------START UTILITY METHODS---------------------------------------------------------------------------------------------
	
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

	
	/*
	 * Writes the output of the Neural Net stored in an array of OutputVectors to a text file
	 */
	//public static void write(ArrayList<OutputVector> x) throws IOException {
	public static void write() throws IOException {
		BufferedWriter outputWriter = null;
		String randomString = Double.toString(Math.random());
		File file = new File(filePathResults + randomString + ".txt");

		// If file does not exists, then create it.
		if (!file.exists()) {
			file.createNewFile();
		}
		outputWriter = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
		outputWriter.write("k: " + Double.toString(k));
		outputWriter.newLine();
		outputWriter.write("Number of nodes (training examples used) in hidden layer: " + Integer.toString(60000/trainingSetReductionFactor));
		outputWriter.newLine();
		double percentCorrect = (countOfCorrectImagesAnalyzedTotal / countOfImagesAnalyzedTotal) * 100;
		outputWriter.write("Analyzed " + countOfImagesAnalyzedTotal + " images with " + percentCorrect + " percent accuracy.");
		outputWriter.newLine();
		outputWriter.write("Testing time: " + trainingTime + " milliseconds");
		outputWriter.newLine();
		outputWriter.write("Training time: " + executionTime + " milliseconds");
		outputWriter.newLine();
		outputWriter.write("There were " +Runtime.getRuntime().availableProcessors()+ " cores avalible to the JVM");
		outputWriter.newLine();
		outputWriter.write("Image data binary: " + binaryInput);
		outputWriter.newLine();
		//for (int i = 0; i < x.size(); i++) {
			//outputWriter.write("Correct: " + x.get(i).getCorrect() + "  ");
			//outputWriter.write("Neural net output: " + Integer.toString(x.get(i).getNeuralNetOutput()) + "   ");
			//outputWriter.write("Expected output: " + Double.toString(x.get(i).getExpectedOutput()));
			outputWriter.newLine();
		//}
			
			for (int m = 0; m < holder.length; m++) {
				outputWriter.write("Number " + m+" was guessed " +holder[m]+ " times, when it should have guessed another number.");
				outputWriter.newLine();
			}
		outputWriter.flush();
		outputWriter.close();
	}

	
	//----------------------END UTILITY METHODS-----------------------------------------------------------------------------------------------
	
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

	
	public static void eightCore(){
		
		
		//Creates 8 threads and splits the test set into eight parts each of which is handled by a seperate thread 
		Runnable r1 = new Runnable() {
			public void run() {
				//Tests the first quarter of the input data
				solveTestingData(testingData, k, 0, 0, numberOfImagesToTest/8);
			}
		};
		Runnable r2 = new Runnable() {
			public void run() {
				//Tests the second fourth of the input data
				solveTestingData(testingData, k, 1,(numberOfImagesToTest*1)/8,(numberOfImagesToTest*2)/8);
			}
		};
		Runnable r3 = new Runnable() {
			public void run() {
				//Tests the third fourth of the input data
				solveTestingData(testingData, k, 2,(numberOfImagesToTest*2)/8,(numberOfImagesToTest*3)/8);
			}
		};
		Runnable r4 = new Runnable() {
			public void run() {
				//Tests the last fourth of the input data
				solveTestingData(testingData, k, 3,(numberOfImagesToTest*3)/8,(numberOfImagesToTest*4)/8);
			}
		};

		Runnable r5 = new Runnable() {
			public void run() {
				//Tests the first quarter of the input data
				solveTestingData(testingData, k, 4,(numberOfImagesToTest*4)/8,(numberOfImagesToTest*5)/8);
			}
		};
		Runnable r6 = new Runnable() {
			public void run() {
				//Tests the second fourth of the input data
				solveTestingData(testingData, k, 5,(numberOfImagesToTest*5)/8,(numberOfImagesToTest*6)/8);
			}
		};
		Runnable r7 = new Runnable() {
			public void run() {
				//Tests the third fourth of the input data
				solveTestingData(testingData, k, 6,(numberOfImagesToTest*6)/8,(numberOfImagesToTest*7)/8);
			}
		};
		Runnable r8 = new Runnable() {
			public void run() {
				//Tests the last fourth of the input data
				solveTestingData(testingData, k, 7,(numberOfImagesToTest*7)/8,numberOfImagesToTest);
			}
		};

		//Starts the 8 threads
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


	
public static void twentyFourCore(){
		
		
		//Creates 8 threads and splits the test set into eight parts each of which is handled by a seperate thread 
		Runnable r1 = new Runnable() {
			public void run() {
				//Tests the first quarter of the input data
				solveTestingData(testingData, k, 0, 0, numberOfImagesToTest/24);
			}
		};
		Runnable r2 = new Runnable() {
			public void run() {
				//Tests the second fourth of the input data
				solveTestingData(testingData, k, 1,(numberOfImagesToTest*1)/24,(numberOfImagesToTest*2)/24);
			}
		};
		Runnable r3 = new Runnable() {
			public void run() {
				//Tests the third fourth of the input data
				solveTestingData(testingData, k, 2,(numberOfImagesToTest*2)/24,(numberOfImagesToTest*3)/24);
			}
		};
		Runnable r4 = new Runnable() {
			public void run() {
				//Tests the last fourth of the input data
				solveTestingData(testingData, k, 3,(numberOfImagesToTest*3)/24,(numberOfImagesToTest*4)/24);
			}
		};

		Runnable r5 = new Runnable() {
			public void run() {
				//Tests the first quarter of the input data
				solveTestingData(testingData, k, 4,(numberOfImagesToTest*4)/24,(numberOfImagesToTest*5)/24);
			}
		};
		Runnable r6 = new Runnable() {
			public void run() {
				//Tests the second fourth of the input data
				solveTestingData(testingData, k, 5,(numberOfImagesToTest*5)/24,(numberOfImagesToTest*6)/24);
			}
		};
		Runnable r7 = new Runnable() {
			public void run() {
				//Tests the third fourth of the input data
				solveTestingData(testingData, k, 6,(numberOfImagesToTest*6)/24,(numberOfImagesToTest*7)/24);
			}
		};
		Runnable r8 = new Runnable() {
			public void run() {
				//Tests the last fourth of the input data
				solveTestingData(testingData, k, 7,(numberOfImagesToTest*7)/24,(numberOfImagesToTest*8)/24);
			}
		};
		
		Runnable r9 = new Runnable() {
			public void run() {
				//Tests the second fourth of the input data
				solveTestingData(testingData, k, 8,(numberOfImagesToTest*8)/24,(numberOfImagesToTest*9)/24);
			}
		};
		
		
		Runnable r10 = new Runnable() {
			public void run() {
				//Tests the second fourth of the input data
				solveTestingData(testingData, k, 9,(numberOfImagesToTest*9)/24,(numberOfImagesToTest*10)/24);
			}
		};
		Runnable r11 = new Runnable() {
			public void run() {
				//Tests the third fourth of the input data
				solveTestingData(testingData, k, 10,(numberOfImagesToTest*10)/24,(numberOfImagesToTest*11)/24);
			}
		};
		Runnable r12 = new Runnable() {
			public void run() {
				//Tests the last fourth of the input data
				solveTestingData(testingData, k, 11,(numberOfImagesToTest*11)/24,(numberOfImagesToTest*12)/24);
			}
		};

		Runnable r13 = new Runnable() {
			public void run() {
				//Tests the first quarter of the input data
				solveTestingData(testingData, k, 12,(numberOfImagesToTest*12)/24,(numberOfImagesToTest*13)/24);
			}
		};
		Runnable r14 = new Runnable() {
			public void run() {
				//Tests the second fourth of the input data
				solveTestingData(testingData, k, 13,(numberOfImagesToTest*13)/24,(numberOfImagesToTest*14)/24);
			}
		};
		Runnable r15 = new Runnable() {
			public void run() {
				//Tests the third fourth of the input data
				solveTestingData(testingData, k, 14,(numberOfImagesToTest*14)/24,(numberOfImagesToTest*15)/24);
			}
		};
		Runnable r16 = new Runnable() {
			public void run() {
				//Tests the last fourth of the input data
				solveTestingData(testingData, k, 15,(numberOfImagesToTest*15)/24,(numberOfImagesToTest*16)/24);
			}
		};
		
		
		Runnable r17 = new Runnable() {
			public void run() {
				//Tests the first quarter of the input data
				solveTestingData(testingData, k, 16,(numberOfImagesToTest*16)/24,(numberOfImagesToTest*17)/24);
			}
		};
		Runnable r18 = new Runnable() {
			public void run() {
				//Tests the second fourth of the input data
				solveTestingData(testingData, k, 17,(numberOfImagesToTest*17)/24,(numberOfImagesToTest*18)/24);
			}
		};
		Runnable r19 = new Runnable() {
			public void run() {
				//Tests the third fourth of the input data
				solveTestingData(testingData, k, 18,(numberOfImagesToTest*18)/24,(numberOfImagesToTest*19)/24);
			}
		};
		Runnable r20 = new Runnable() {
			public void run() {
				//Tests the last fourth of the input data
				solveTestingData(testingData, k, 19,(numberOfImagesToTest*19)/24,(numberOfImagesToTest*20)/24);
			}
		};

		Runnable r21 = new Runnable() {
			public void run() {
				//Tests the first quarter of the input data
				solveTestingData(testingData, k, 20,(numberOfImagesToTest*20)/24,(numberOfImagesToTest*21)/24);
			}
		};
		Runnable r22 = new Runnable() {
			public void run() {
				//Tests the second fourth of the input data
				solveTestingData(testingData, k, 21,(numberOfImagesToTest*21)/24,(numberOfImagesToTest*22)/24);
			}
		};
		Runnable r23 = new Runnable() {
			public void run() {
				//Tests the third fourth of the input data
				solveTestingData(testingData, k, 22,(numberOfImagesToTest*22)/24,(numberOfImagesToTest*23)/24);
			}
		};
		Runnable r24 = new Runnable() {
			public void run() {
				//Tests the last fourth of the input data
				solveTestingData(testingData, k, 23,(numberOfImagesToTest*23)/24,numberOfImagesToTest);
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

	
}
	


