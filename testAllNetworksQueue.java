import java.io.IOException;

/*
 * This class creates multiple instances of each network type and allows tests to be run one after another and have the results neatly collected in output files in subfolders.
 * Make sure to pass the argument of 3 GB minimum of Ram to the JVM, in order to run properly.
 * 1. First unzip the NeuralNetOutput.zip in a directory.
 * 2. Change  "/Users/zackeryleman/Desktop" to be the file path of the NeuralNetOutput folder.
 * 3. Change the NUMBER_OF_CORES to 24 or 8 in each neural net class, depending on your machine. The processors are required to share memory and thus must be run on the same machine.
 */
public class testAllNetworksQueue {

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		
		
		//These are for demonstration purposes:--------------------------------
		String filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/Demo/Results";
		String filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/Demo/TrainedSetOutputWeights.txt";
		String filePathTrainedHiddenWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/Demo/TrainedSetHiddenWeights.txt";
		
		//Runs a FF on CAPTCHAS
		// Need to set NUMBER_OF_OUTPUT_NODES=36 for this to work.
		//NeuralNet secondFF4 = new NeuralNet(30,1000,0.3,0,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);
						
		
	
		// 30 Hidden Nodes, learning rate 0.3, binary input,  training 5 epochs on the full data set
		NeuralNet FFdemo = new NeuralNet(30,5,0.3,0,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);
		
		
		
		
		filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/Demo/KNNResults";
		KNearestNeighbors kNNDemo = new KNearestNeighbors(3,false,5,1000,filePathResults);
		
		 filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/Demo/RbfResults";
		 filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/Demo/TrainedRBFSetOutputWeights.txt";
		RadialBasisFunction RBFa = new RadialBasisFunction(20,false,1000000,5,0.5,0,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction RBFb = new RadialBasisFunction(20,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction RBFc = new RadialBasisFunction(20,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		
		//-------------------------------------------------------------
		
		
		//These are all tests--------------------------------  
		
		//runFFHiddenNodeEx(); //Exp #1
		//runFFLearningRateEx(); //Exp #2
		//runFFTrainingExampleEx(); //Exp #3
			
		//runRBFSigmaEx(); //Exp #4

		//runRBFLearningRateEx(); //Exp #5

		//runRBFTrainingExamplesEx(); //Exp #6
		
		
		//runKNNKEx();// Exp 7

		//runKNNTrainingExamplesEx(); //Exp 8

		//This class will then read the individual human readable output files and summarize the data in a way that makes it easier to graph.
		//This does not work with the Demo
		

	}


	/*
	 *  Runs Experiment #1
	 */
	public static void runFFHiddenNodeEx() throws IOException, ClassNotFoundException{

		String filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/15/Results";
		String filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/15/TrainedSetOutputWeights.txt";
		String filePathTrainedHiddenWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/15/TrainedSetHiddenWeights.txt";

		// 15 Hidden Nodes, learning rate 0.3, binary input, continue training 5 epochs at a time on the full data set
		NeuralNet firstFF1 = new NeuralNet(15,5,0.3,0,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 1-5
		NeuralNet firstFF2 = new NeuralNet(15,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 6-10
		NeuralNet firstFF3 = new NeuralNet(15,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 11-15
		NeuralNet firstFF4 = new NeuralNet(15,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 16-20

		firstFF1=null;
		firstFF2=null;
		firstFF3=null;
		firstFF4=null;

		filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/30/Results";
		filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/30/TrainedSetOutputWeights.txt";
		filePathTrainedHiddenWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/30/TrainedSetHiddenWeights.txt";
		// 30 Hidden Nodes, learning rate 0.3, binary input, continue training 5 epochs at a time on the full data set
		NeuralNet firstFF1A = new NeuralNet(30,5,0.3,0,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 1-5
		NeuralNet firstFF2A = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 6-10
		NeuralNet firstFF3A = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 11-15
		NeuralNet firstFF4A = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 16-20

		firstFF1A=null;
		firstFF2A=null;
		firstFF3A=null;
		firstFF4A=null;

		filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/50/Results";
		filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/50/TrainedSetOutputWeights.txt";
		filePathTrainedHiddenWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/50/TrainedSetHiddenWeights.txt";
		// 50 Hidden Nodes, learning rate 0.3, binary input, continue training 5 epochs at a time on the full data set
		NeuralNet firstFF1AA = new NeuralNet(50,5,0.3,0,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 1-5
		NeuralNet firstFF2AA = new NeuralNet(50,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 6-10
		NeuralNet firstFF22AA = new NeuralNet(50,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 11-15
		NeuralNet firstFF3AA = new NeuralNet(50,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 16-20

		firstFF1AA=null;
		firstFF2AA=null;
		firstFF22AA=null;
		firstFF3AA=null;

		filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/100/Results";
		filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/100/TrainedSetOutputWeights.txt";
		filePathTrainedHiddenWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/100/TrainedSetHiddenWeights.txt";
		// 100 Hidden Nodes, learning rate 0.3, binary input, continue training 5 epochs at a time on the full data set
		NeuralNet FF1B = new NeuralNet(100,5,0.3,0,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 1-5
		NeuralNet FF2B = new NeuralNet(100,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 6-10
		NeuralNet FF3B = new NeuralNet(100,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 11-15
		NeuralNet FF4B = new NeuralNet(100,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 16-20


		FF1B=null;
		FF2B=null;
		FF3B=null;
		FF4B=null;



	}


	/*
	 *  Runs Experiment #2
	 */
	public static void runFFLearningRateEx() throws IOException, ClassNotFoundException{

		String filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/0.1/Results";
		String filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/0.1/TrainedSetOutputWeights.txt";
		String filePathTrainedHiddenWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/0.1/TrainedSetHiddenWeights.txt";
		// 30 Hidden Nodes, learning rate 0.1, binary input, continue training 5 epochs at a time on the full data set
		NeuralNet secondFF1 = new NeuralNet(30,5,0.1,0,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 1-5
		NeuralNet secondFF2 = new NeuralNet(30,5,0.1,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 6-10
		NeuralNet secondFF3 = new NeuralNet(30,5,0.1,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 11-15
		NeuralNet secondFF4 = new NeuralNet(30,5,0.1,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 16-20

		secondFF1=null;
		secondFF2=null;
		secondFF3=null;
		secondFF4=null;
		


		filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/0.2/Results";
		filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/0.2/TrainedSetOutputWeights.txt";
		filePathTrainedHiddenWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/0.2/TrainedSetHiddenWeights.txt";
		// 30 Hidden Nodes, learning rate 0.2, binary input, continue training 5 epochs at a time on the full data set
		NeuralNet secondFF1B = new NeuralNet(30,5,0.2,0,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 1-5
		NeuralNet secondFF2B = new NeuralNet(30,5,0.2,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 6-10
		NeuralNet secondFF3B = new NeuralNet(30,5,0.2,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 11-15
		NeuralNet secondFF4B = new NeuralNet(30,5,0.2,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 16-20

		secondFF1B=null;
		secondFF2B=null;
		secondFF3B=null;
		secondFF4B=null;
		
		
		filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/0.5/Results";
		filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/0.5/TrainedSetOutputWeights.txt";
		filePathTrainedHiddenWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/0.5/TrainedSetHiddenWeights.txt";
		// 30 Hidden Nodes, learning rate 0.5, binary input, continue training 5 epochs at a time on the full data set
		NeuralNet secondFF1C = new NeuralNet(30,5,0.5,0,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 1-5
		NeuralNet secondFF2C = new NeuralNet(30,5,0.5,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 6-10
		NeuralNet secondFF3C = new NeuralNet(30,5,0.5,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 11-15
		NeuralNet secondFF4C = new NeuralNet(30,5,0.5,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 16-20

		secondFF1C=null;
		secondFF2C=null;
		secondFF3C=null;
		secondFF4C=null;
		

		filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/0.7/Results";
		filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/0.7/TrainedSetOutputWeights.txt";
		filePathTrainedHiddenWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/0.7/TrainedSetHiddenWeights.txt";
		// 30 Hidden Nodes, learning rate 0.7, binary input, continue training 5 epochs at a time on the full data set
		NeuralNet secondFF1D = new NeuralNet(30,5,0.7,0,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 1-5
		NeuralNet secondFF2D = new NeuralNet(30,5,0.7,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 6-10
		NeuralNet secondFF3D = new NeuralNet(30,5,0.7,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 11-15
		NeuralNet secondFF4D = new NeuralNet(30,5,0.7,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 16-20

		secondFF1D=null;
		secondFF2D=null;
		secondFF3D=null;
		secondFF4D=null;
	



		filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/1/Results";
		filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/1/TrainedSetOutputWeights.txt";
		filePathTrainedHiddenWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/1/TrainedSetHiddenWeights.txt";
		// 30 Hidden Nodes, learning rate 1, binary input, continue training 5 epochs at a time on the full data set
		NeuralNet secondFF1E = new NeuralNet(30,5,1,0,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 1-5
		NeuralNet secondFF2E = new NeuralNet(30,5,1,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 6-10
		NeuralNet secondFF3E = new NeuralNet(30,5,1,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 11-15
		NeuralNet secondFF4E = new NeuralNet(30,5,1,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 16-20


		secondFF1E=null;
		secondFF2E=null;
		secondFF3E=null;
		secondFF4E=null;
		
	}

	/*
	 *  Runs Experiment #3
	 */
	public static void runFFTrainingExampleEx() throws IOException, ClassNotFoundException{

		String filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/1t/Results";
		String filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/1t/TrainedSetOutputWeights.txt";
		String filePathTrainedHiddenWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/1t/TrainedSetHiddenWeights.txt";
		// 30 Hidden Nodes, learning rate 0.3, binary input, continue training 5 epochs at a time on a 1000th of the data set
		NeuralNet secondFF1 = new NeuralNet(30,5,0.3,0,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1000);//epochs 1-5
		NeuralNet secondFF2 = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1000);//epochs 6-10
		NeuralNet secondFF3 = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1000);//epochs 11-15
		NeuralNet secondFF4 = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1000);//epochs 16-20

		secondFF1=null;
		secondFF2=null;
		secondFF3=null;
		secondFF4=null;


		filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/2t/Results";
		filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/2t/TrainedSetOutputWeights.txt";
		filePathTrainedHiddenWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/2t/TrainedSetHiddenWeights.txt";
		// 30 Hidden Nodes, learning rate 0.3, binary input, continue training 5 epochs at a time on a half of the data set
		NeuralNet secondFF1B = new NeuralNet(30,5,0.3,0,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,2);//epochs 1-5
		NeuralNet secondFF2B = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,2);//epochs 6-10
		NeuralNet secondFF3B = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,2);//epochs 11-15
		NeuralNet secondFF4B = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,2);//epochs 16-20

		secondFF1B=null;
		secondFF2B=null;
		secondFF3B=null;
		secondFF4B=null;
		
		filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/10t/Results";
		filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/10t/TrainedSetOutputWeights.txt";
		filePathTrainedHiddenWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/10t/TrainedSetHiddenWeights.txt";
		// 30 Hidden Nodes, learning rate 0.3, binary input, continue training 5 epochs at a time on a tenth of the data set
		NeuralNet secondFF1C = new NeuralNet(30,5,0.3,0,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,10);//epochs 1-5
		NeuralNet secondFF2C = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,10);//epochs 6-10
		NeuralNet secondFF3C = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,10);//epochs 11-15
		NeuralNet secondFF4C = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,10);//epochs 16-20

		secondFF1C=null;
		secondFF2C=null;
		secondFF3C=null;
		secondFF4C=null;
		

		filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/30t/Results";
		filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/30t/TrainedSetOutputWeights.txt";
		filePathTrainedHiddenWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/30t/TrainedSetHiddenWeights.txt";
		// 30 Hidden Nodes, learning rate 0.7, binary input, continue training 5 epochs at a time on a 30th of the data set
		NeuralNet secondFF1D = new NeuralNet(30,5,0.3,0,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,30);//epochs 1-5
		NeuralNet secondFF2D = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,30);//epochs 6-10
		NeuralNet secondFF3D = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,30);//epochs 11-15
		NeuralNet secondFF4D = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,30);//epochs 16-20

		secondFF1D=null;
		secondFF2D=null;
		secondFF3D=null;
		secondFF4D=null;
		

		filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/60t/Results";
		filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/60t/TrainedSetOutputWeights.txt";
		filePathTrainedHiddenWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/60t/TrainedSetHiddenWeights.txt";
		// 30 Hidden Nodes, learning rate 1, binary input, continue training 5 epochs at a time on a 60th of the data set
		NeuralNet secondFF1E = new NeuralNet(30,5,0.3,0,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,60);//epochs 1-5
		NeuralNet secondFF2E = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,60);//epochs 6-10
		NeuralNet secondFF3E = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,60);//epochs 11-15
		NeuralNet secondFF4E = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,60);//epochs 16-20


		secondFF1E=null;
		secondFF2E=null;
		secondFF3E=null;
		secondFF4E=null;
		
	}



	/*
	 *  Runs Experiment #7
	 */
	public static void runKNNKEx() throws IOException, ClassNotFoundException{
		//Runs K-NN networks with multiple K values on the 12,000 images in the training set using raw gray scale images. Tests using 1000 images.
		String filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/KNN/k/KNNResults";
		KNearestNeighbors a = new KNearestNeighbors(1,false,5,1000,filePathResults);
		KNearestNeighbors b = new KNearestNeighbors(2,false,5,1000,filePathResults);
		KNearestNeighbors c = new KNearestNeighbors(3,false,5,1000,filePathResults);
		KNearestNeighbors d = new KNearestNeighbors(4,false,5,1000,filePathResults);
		KNearestNeighbors e2 = new KNearestNeighbors(5,false,5,1000,filePathResults);
		KNearestNeighbors f = new KNearestNeighbors(6,false,5,1000,filePathResults);
		KNearestNeighbors g = new KNearestNeighbors(7,false,5,1000,filePathResults);
		KNearestNeighbors h = new KNearestNeighbors(8,false,5,1000,filePathResults);
		KNearestNeighbors i = new KNearestNeighbors(9,false,5,1000,filePathResults);
		KNearestNeighbors j = new KNearestNeighbors(10,false,5,1000,filePathResults);
		a=null;
		b=null;
		c=null;
		d=null;
		e2=null;
		f=null;
		g=null;
		h=null;
		i=null;
		j=null;
		
	}






	/*
	 *  Runs Experiment #8
	 */
	public static void runKNNTrainingExamplesEx() throws IOException, ClassNotFoundException{
		//Runs K-NN networks with multiple K=3  on the various number of images in the training set using raw gray scale images. Tests using 1000 images.
		String filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/KNN/trainingExamples/KNNResults";
		KNearestNeighbors a = new KNearestNeighbors(3,false,1,1000,filePathResults);
		KNearestNeighbors b = new KNearestNeighbors(3,false,2,1000,filePathResults);
		KNearestNeighbors c = new KNearestNeighbors(3,false,3,1000,filePathResults);
		KNearestNeighbors d = new KNearestNeighbors(3,false,4,1000,filePathResults);
		KNearestNeighbors e2 = new KNearestNeighbors(3,false,5,1000,filePathResults);
		KNearestNeighbors f = new KNearestNeighbors(3,false,10,1000,filePathResults);
		KNearestNeighbors g = new KNearestNeighbors(3,false,20,1000,filePathResults);
		KNearestNeighbors h = new KNearestNeighbors(3,false,50,1000,filePathResults);
		KNearestNeighbors i = new KNearestNeighbors(3,false,100,1000,filePathResults);
		KNearestNeighbors j = new KNearestNeighbors(3,false,1000,1000,filePathResults);
		a=null;
		b=null;
		c=null;
		d=null;
		e2=null;
		f=null;
		g=null;
		h=null;
		i=null;
		j=null;
		
	}



	/*
	 *  Runs Experiment #5
	 */
	public static void runRBFLearningRateEx() throws IOException, ClassNotFoundException{
		//50 epochs for each learning rate

	
		String filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/RBF/LR/0.1/RbfResults";
		String filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/RBF/LR/0.1/TrainedRBFSetOutputWeights.txt";
		//Learning rate=0.1
		RadialBasisFunction a = new RadialBasisFunction(20,false,1000000,5,0.1,0,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction b = new RadialBasisFunction(20,false,1000000,5,0.1,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction c = new RadialBasisFunction(20,false,1000000,5,0.1,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction d = new RadialBasisFunction(20,false,1000000,5,0.1,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction ee = new RadialBasisFunction(20,false,1000000,5,0.1,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction f = new RadialBasisFunction(20,false,1000000,5,0.1,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction g = new RadialBasisFunction(20,false,1000000,5,0.1,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction h = new RadialBasisFunction(20,false,1000000,5,0.1,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction i = new RadialBasisFunction(20,false,1000000,5,0.1,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction j = new RadialBasisFunction(20,false,700000,5,0.1,2,filePathResults,filePathTrainedOutputWeights);
		a=null;
		b=null;
		c=null;
		d=null;
		ee=null;
		f=null;
		g=null;
		h=null;
		i=null;
		j=null;
		

		filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/RBF/LR/0.3/RbfResults";
		filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/RBF/LR/0.3/TrainedRBFSetOutputWeights.txt";
		//Learning rate=0.3
		RadialBasisFunction a2 = new RadialBasisFunction(20,false,1000000,5,0.3,0,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction b2 = new RadialBasisFunction(20,false,1000000,5,0.3,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction c2 = new RadialBasisFunction(20,false,1000000,5,0.3,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction d2 = new RadialBasisFunction(20,false,1000000,5,0.3,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction e2 = new RadialBasisFunction(20,false,1000000,5,0.3,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction f2 = new RadialBasisFunction(20,false,1000000,5,0.3,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction g2 = new RadialBasisFunction(20,false,1000000,5,0.3,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction h2 = new RadialBasisFunction(20,false,1000000,5,0.3,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction i2 = new RadialBasisFunction(20,false,1000000,5,0.3,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction j2 = new RadialBasisFunction(20,false,700000,5,0.3,2,filePathResults,filePathTrainedOutputWeights);
		b2=null;
		c2=null;
		d2=null;
		e2=null;
		f2=null;
		g2=null;
		h2=null;
		i2=null;
		j2=null;
		


		filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/RBF/LR/0.5/RbfResults";
		filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/RBF/LR/0.5/TrainedRBFSetOutputWeights.txt";
		//Learning rate=0.5
		RadialBasisFunction a3 = new RadialBasisFunction(20,false,1000000,5,0.5,0,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction b3 = new RadialBasisFunction(20,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction c3 = new RadialBasisFunction(20,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction d3 = new RadialBasisFunction(20,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction e3 = new RadialBasisFunction(20,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction f3 = new RadialBasisFunction(20,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction g3 = new RadialBasisFunction(20,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction h3 = new RadialBasisFunction(20,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction i3 = new RadialBasisFunction(20,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction j3 = new RadialBasisFunction(20,false,700000,5,0.5,2,filePathResults,filePathTrainedOutputWeights);
		a3=null;
		b3=null;
		c3=null;
		d3=null;
		e3=null;
		f3=null;
		g3=null;
		h3=null;
		i3=null;
		j3=null;
		


		filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/RBF/LR/0.9/RbfResults";
		filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/RBF/LR/0.9/TrainedRBFSetOutputWeights.txt";
		//Learning rate=0.9
		RadialBasisFunction a4 = new RadialBasisFunction(20,false,1000000,5,0.9,0,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction b4 = new RadialBasisFunction(20,false,1000000,5,0.9,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction c4 = new RadialBasisFunction(20,false,1000000,5,0.9,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction d4 = new RadialBasisFunction(20,false,1000000,5,0.9,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction e4 = new RadialBasisFunction(20,false,1000000,5,0.9,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction f4 = new RadialBasisFunction(20,false,1000000,5,0.9,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction g4 = new RadialBasisFunction(20,false,1000000,5,0.9,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction h4 = new RadialBasisFunction(20,false,1000000,5,0.9,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction i4 = new RadialBasisFunction(20,false,1000000,5,0.9,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction j4 = new RadialBasisFunction(20,false,700000,5,0.9,2,filePathResults,filePathTrainedOutputWeights);
		a4=null;
		b4=null;
		c4=null;
		d4=null;
		e4=null;
		f4=null;
		g4=null;
		h4=null;
		i4=null;
		j4=null;
		


	}




	/*
	 *  Runs Experiment #4
	 */
	public static void runRBFSigmaEx() throws IOException, ClassNotFoundException{
		//50 epochs for each  sigmaSquared

		String filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/RBF/Sigma/1000000/RbfResults";
		String filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/RBF/Sigma/1000000/TrainedRBFSetOutputWeights.txt";
//sigmaSquared=1000000
		RadialBasisFunction a = new RadialBasisFunction(20,false,1000000,5,0.5,2,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction b = new RadialBasisFunction(20,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction c = new RadialBasisFunction(20,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction d = new RadialBasisFunction(20,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction ee = new RadialBasisFunction(20,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction f = new RadialBasisFunction(20,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction g = new RadialBasisFunction(20,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction h = new RadialBasisFunction(20,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction i = new RadialBasisFunction(20,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction j = new RadialBasisFunction(20,false,700000,5,0.5,2,filePathResults,filePathTrainedOutputWeights);
		a=null;
		b=null;
		c=null;
		d=null;
		ee=null;
		f=null;
		g=null;
		h=null;
		i=null;
		j=null;
		

		filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/RBF/Sigma/100000/RbfResults";
		filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/RBF/Sigma/100000/TrainedRBFSetOutputWeights.txt";
		//sigmaSquared=100000
		RadialBasisFunction a2 = new RadialBasisFunction(20,false,100000,5,0.5,0,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction b2 = new RadialBasisFunction(20,false,100000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction c2 = new RadialBasisFunction(20,false,100000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction d2 = new RadialBasisFunction(20,false,100000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction e2 = new RadialBasisFunction(20,false,100000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction f2 = new RadialBasisFunction(20,false,100000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction g2 = new RadialBasisFunction(20,false,100000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction h2 = new RadialBasisFunction(20,false,100000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction i2 = new RadialBasisFunction(20,false,100000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction j2 = new RadialBasisFunction(20,false,70000,5,0.5,2,filePathResults,filePathTrainedOutputWeights);
		a2=null;
		b2=null;
		c2=null;
		d2=null;
		e2=null;
		f2=null;
		g2=null;
		h2=null;
		i2=null;
		j2=null;
		


		filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/RBF/Sigma/100/RbfResults";
		filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/RBF/Sigma/100/TrainedRBFSetOutputWeights.txt";
		//sigmaSquared=100
		RadialBasisFunction a3 = new RadialBasisFunction(20,false,100,5,0.5,0,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction b3 = new RadialBasisFunction(20,false,100,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction c3 = new RadialBasisFunction(20,false,100,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction d3 = new RadialBasisFunction(20,false,100,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction e3 = new RadialBasisFunction(20,false,100,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction f3 = new RadialBasisFunction(20,false,100,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction g3 = new RadialBasisFunction(20,false,100,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction h3 = new RadialBasisFunction(20,false,100,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction i3 = new RadialBasisFunction(20,false,100,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction j3 = new RadialBasisFunction(20,false,70,5,0.5,2,filePathResults,filePathTrainedOutputWeights);
		a3=null;
		b3=null;
		c3=null;
		d3=null;
		e3=null;
		f3=null;
		g3=null;
		h3=null;
		i3=null;
		j3=null;



		filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/RBF/Sigma/100000000/RbfResults";
		filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/RBF/Sigma/100000000/TrainedRBFSetOutputWeights.txt";
		//sigmaSquared=100000000
		RadialBasisFunction a4 = new RadialBasisFunction(20,false,100000000,5,0.5,0,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction b4 = new RadialBasisFunction(20,false,100000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction c4 = new RadialBasisFunction(20,false,100000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction d4 = new RadialBasisFunction(20,false,100000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction e4 = new RadialBasisFunction(20,false,100000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction f4 = new RadialBasisFunction(20,false,100000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction g4 = new RadialBasisFunction(20,false,100000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction h4 = new RadialBasisFunction(20,false,100000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction i4 = new RadialBasisFunction(20,false,100000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction j4 = new RadialBasisFunction(20,false,70000000,5,0.5,2,filePathResults,filePathTrainedOutputWeights);
		a4=null;
		b4=null;
		c4=null;
		d4=null;
		e4=null;
		f4=null;
		g4=null;
		h4=null;
		i4=null;
		j4=null;
		


	}



	/*
	 *  Runs Experiment #6
	 */
	public static void runRBFTrainingExamplesEx() throws IOException, ClassNotFoundException{
		// 50 epochs for each number of training examples

	
		String filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/RBF/TE/20/RbfResults";
		String filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/RBF/TE/20/TrainedRBFSetOutputWeights.txt";
		 // Training data set reduction factor = 20
		RadialBasisFunction a = new RadialBasisFunction(20,false,1000000,5,0.5,0,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction b = new RadialBasisFunction(20,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction c = new RadialBasisFunction(20,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction d = new RadialBasisFunction(20,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction ee = new RadialBasisFunction(20,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction f = new RadialBasisFunction(20,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction g = new RadialBasisFunction(20,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction h = new RadialBasisFunction(20,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction i = new RadialBasisFunction(20,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction j = new RadialBasisFunction(20,false,700000,5,0.5,2,filePathResults,filePathTrainedOutputWeights);
		a=null;
		b=null;
		c=null;
		d=null;
		ee=null;
		f=null;
		g=null;
		h=null;
		i=null;
		j=null;
		
		filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/RBF/TE/1/RbfResults";
		filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/RBF/TE/1/TrainedRBFSetOutputWeights.txt";
		 // Training data set reduction factor = 1
		RadialBasisFunction a2 = new RadialBasisFunction(1,false,1000000,5,0.5,0,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction b2 = new RadialBasisFunction(1,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction c2 = new RadialBasisFunction(1,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction d2 = new RadialBasisFunction(1,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction e2 = new RadialBasisFunction(1,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction f2 = new RadialBasisFunction(1,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction g2 = new RadialBasisFunction(1,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction h2 = new RadialBasisFunction(1,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction i2 = new RadialBasisFunction(1,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction j2 = new RadialBasisFunction(1,false,700000,5,0.5,2,filePathResults,filePathTrainedOutputWeights);
		b2=null;
		c2=null;
		d2=null;
		e2=null;
		f2=null;
		g2=null;
		h2=null;
		i2=null;
		j2=null;
		


		filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/RBF/TE/5/RbfResults";
		filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/RBF/TE/5/TrainedRBFSetOutputWeights.txt";
		 // Training data set reduction factor = 5
		RadialBasisFunction a3 = new RadialBasisFunction(5,false,1000000,5,0.5,0,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction b3 = new RadialBasisFunction(5,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction c3 = new RadialBasisFunction(5,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction d3 = new RadialBasisFunction(5,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction e3 = new RadialBasisFunction(5,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction f3 = new RadialBasisFunction(5,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction g3 = new RadialBasisFunction(5,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction h3 = new RadialBasisFunction(5,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction i3 = new RadialBasisFunction(5,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction j3 = new RadialBasisFunction(5,false,700000,5,0.5,2,filePathResults,filePathTrainedOutputWeights);
		a3=null;
		b3=null;
		c3=null;
		d3=null;
		e3=null;
		f3=null;
		g3=null;
		h3=null;
		i3=null;
		j3=null;
		


	

		filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/RBF/TE/200/RbfResults";
		filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/RBF/TE/200/TrainedRBFSetOutputWeights.txt";
		 // Training data set reduction factor = 200
		RadialBasisFunction a4 = new RadialBasisFunction(200,false,1000000,5,0.5,0,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction b4 = new RadialBasisFunction(200,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction c4 = new RadialBasisFunction(200,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction d4 = new RadialBasisFunction(200,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction e4 = new RadialBasisFunction(200,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction f4 = new RadialBasisFunction(200,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction g4 = new RadialBasisFunction(200,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction h4 = new RadialBasisFunction(200,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction i4 = new RadialBasisFunction(200,false,1000000,5,0.5,1,filePathResults,filePathTrainedOutputWeights);
		RadialBasisFunction j4 = new RadialBasisFunction(200,false,700000,5,0.5,2,filePathResults,filePathTrainedOutputWeights);
		a4=null;
		b4=null;
		c4=null;
		d4=null;
		e4=null;
		f4=null;
		g4=null;
		h4=null;
		i4=null;
		j4=null;
		


	}


}












