

	
	import java.util.*;
import java.io.IOException;
import java.lang.Math;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.*;


public class Knearesteighbours {
		//Tracks the number of images  processed in the testing set.
		public static double countOfImagesAnalyzed=0;
		//Tracks the number of images correctly identified in the testing set.
		public static double countOfCorrectImagesAnalyzed=0;
		//Tracks running time of the hidden layer construction
		public static long executionTime;
		//Creates a random number generator
		public static Random random = new Random();
		//The number of input nodes will be equal to the number of pixels in the image
		public static int numberOfInputNodes;
		//Create array of Nodes in first layer and associate done that points to the correct output
		public static ArrayList<ArrayList<Double>> hiddenLayerNodes = new ArrayList<ArrayList<Double>>();
		public static ArrayList<Double> hiddenLayerToOutput = new ArrayList<Double>();
		//The learning rate for the network
		public static double learningRate;
		//Whether to use weights and hidden nodes that have already been trained or to train network again
		public static boolean usePriorWeights;
	
		public static void main (String[] args) throws IOException, ClassNotFoundException {
			//usePriorWeights=Boolean.parseBolean(args[4]);
			//String trainingImages=args[7];
			//String testingImages=args[8];
			//String trainingLabels=args[9];
			//String testingLabels=args[10];
		

			//These are hard coded versions of the above
			//Set this to true to avoid retraining. Allows the files in NeuralNetOutput folder to be loaded and used.
			usePriorWeights=false;
			String trainingImages="Training-Images";
			String testingImages="Testing-images";
			String trainingLabels="Training-Labels";
			String testingLabels="Testing-Labels";
			
			
			if(!usePriorWeights){
				
			initializeKNearestNeighbours(trainingImages,trainingLabels);
			} else{
				readDataFromTrainedFiles();
				numberOfInputNodes=hiddenLayerNodes.get(0).size();
			}
			//Test the test K-Nearest Neighbours Network
			testKNearestNeighbours(testingImages,testingLabels);
		}

		/*Returns the output from a given node after the input has been summed and processed by the activation function
		 *It takes the layer that the node is in, the index of the node in the layer, and the output from the previous layer */
		public static double nodeOutput(ArrayList<ArrayList<Double>> layerOfNodes,  ArrayList<Double> outputFromPreviousLayer, int indexOfNodeinlayer) {
			double sum=0;
			for(int i=0;i<outputFromPreviousLayer.size();i++){
				sum=sum+(layerOfNodes.get(indexOfNodeinlayer).get(i) * outputFromPreviousLayer.get(i));
			}
			return sum;
		}


		/*This returns an array representing the output of all nodes in the given layer*/
		public static  ArrayList<Double> outPutOfLayer(ArrayList<ArrayList<Double>> currentLayer, ArrayList<Double> outputFromPreviousLayer) {
			ArrayList<Double> outputOfCurrentlayer = new ArrayList<Double>();
			for (int i=0; i<currentLayer.size(); i++) { 
				double output=nodeOutput(currentLayer,outputFromPreviousLayer,i);
				outputOfCurrentlayer.add(output);
			}
			return outputOfCurrentlayer;
		}


		

		/*Writes the output of the Neural Net stored in an array of OutputVectors to a text file*/
		public static void write (ArrayList<OutputVector> x) throws IOException{
			BufferedWriter outputWriter = null;
			String randomString=Double.toString(Math.random());
			File file = new File("/Users/zackeryleman/Desktop/NeuralNetOutput/Results"+randomString+".txt");

			// If file does not exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			outputWriter = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
				outputWriter.write("Learning rate: "+ Double.toString(learningRate));
				outputWriter.newLine();
				outputWriter.newLine();
				double percentCorrect= (countOfCorrectImagesAnalyzed/countOfImagesAnalyzed)*100;
				outputWriter.write("Analyzed " + countOfImagesAnalyzed+ " images with " +percentCorrect+ " percent accuracy.");
				outputWriter.newLine();
				outputWriter.write("Training time: " + executionTime + " milliseconds");
				outputWriter.newLine();
				for (int i = 0; i < x.size(); i++) {
				outputWriter.write("Correct: "+ x.get(i).getCorrect()+"  ");
				outputWriter.write("Neural net output: "+ Integer.toString(x.get(i).getNeuralNetOutput())+"   ");
				outputWriter.write("Expected output: "+ Double.toString(x.get(i).getExpectedOutput()));
				outputWriter.newLine();
			}
			outputWriter.flush();  
			outputWriter.close();  
		}
		public static void writeTrainedWeights () throws IOException{
				//TODO: Make the metadata for the training parameters that created these weights  associated with these files.
				
				//We serialize these data structures and write to file. These can then be read back into the neural net.
					FileOutputStream fout= new FileOutputStream ("/Users/zackeryleman/Desktop/NeuralNetOutput/TrainedSetHiddenLayerToOutput.txt");
					ObjectOutputStream oos = new ObjectOutputStream(fout);
					 oos.writeObject(hiddenLayerToOutput);
					FileOutputStream fout2= new FileOutputStream ("/Users/zackeryleman/Desktop/NeuralNetOutput/TrainedSetHiddenWeights.txt");
					ObjectOutputStream oos2 = new ObjectOutputStream(fout2);
			         oos2.writeObject(hiddenLayerNodes); 
			         oos.close();
			         fout.close();
			         oos2.close();
			         fout2.close();
		}

		
		public static void initializeKNearestNeighbours(String trainingImages, String trainingLabels) throws IOException{

					//Loads training and testing data sets
					DigitImageLoadingService train = new DigitImageLoadingService(trainingLabels,trainingImages);
					ArrayList<DigitImage> trainingData = new ArrayList<DigitImage>();
					try {
						//Our data structure holds the training data
						trainingData= train.loadDigitImages();
						//Alters data into proper form
						for(int i=0; i<trainingData.size(); i++){
							trainingData.get(i).vectorizeTrainingData();
						}
					} catch (IOException e) {
							e.printStackTrace();
						}

						//Looks at a representation of an image
						//and determines how many pixels and thus how many input nodes are needed
						//(one per pixel)
						numberOfInputNodes=trainingData.get(0).getData().length;

						long startTime = System.currentTimeMillis();
						//Initialize weights  with  values  corresponding to the binary pixel value for all nodes in the first hidden layer.
						for (int i=0; i<trainingData.size()/3; i++) { 
							ArrayList<Double> weights = new ArrayList<Double>(numberOfInputNodes); 
							weights=trainingData.get(i).getArrayListData();
							hiddenLayerNodes.add(weights); 
							hiddenLayerToOutput.add((double) trainingData.get(i).getLabel());
						}

						long endTime = System.currentTimeMillis();
				    	 executionTime = endTime - startTime;
				    	System.out.println("Training time: " + executionTime + " milliseconds");
				    	//Creates data files that can be reused by the network without retraining.
						writeTrainedWeights();
			
		}

		public static void testKNearestNeighbours(String testingImages, String testingLabels) throws IOException{
			
			//Loads testing data set
			DigitImageLoadingService test = new DigitImageLoadingService(testingLabels,testingImages);
			ArrayList<DigitImage> testingData= new ArrayList<DigitImage>();
			try {
				//Our data structure holds the testing data
				 testingData= test.loadDigitImages();
			} catch (IOException e) {
					e.printStackTrace();
				}
			
			//Tests the network with the testing Data and prints results to file
			//write(solveTestingData(testingData));
			
			//reports network Performance
			double percentCorrect= (countOfCorrectImagesAnalyzed/countOfImagesAnalyzed)*100;
			System.out.println("Analyzed " + countOfImagesAnalyzed+ " images with " +percentCorrect+ " percent accuracy.");
			System.out.println("Look in /Users/\"your username\"/NeuralNetOutput/Desktop  directory to find  the output.");
		}
		
		
		public static void readDataFromTrainedFiles() throws IOException, ClassNotFoundException{
			//Grabs weights to output nodes
			FileInputStream fin= new FileInputStream ("/Users/zackeryleman/Desktop/NeuralNetOutput/TrainedSetOutputWeights.txt");
			ObjectInputStream ois = new ObjectInputStream(fin);
			hiddenLayerToOutput = (ArrayList<Double>) ois.readObject();
			fin.close();
			//Grabs weights to hidden nodes
			FileInputStream fin2= new FileInputStream ("/Users/zackeryleman/Desktop/NeuralNetOutput/TrainedSetHiddenWeights.txt");
			ObjectInputStream ois2 = new ObjectInputStream(fin2);
			hiddenLayerNodes = (ArrayList<ArrayList<Double>>) ois2.readObject();
			fin2.close();
		
		}
		 
		
		/*public static ArrayList<OutputVector> solveTestingData(ArrayList<DigitImage>  networkInputData) {
			
		}*/
		
	}

