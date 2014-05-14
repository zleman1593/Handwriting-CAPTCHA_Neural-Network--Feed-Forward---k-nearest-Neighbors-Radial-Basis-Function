import java.io.IOException;

/*
 * This class creates multiple instances of each  network and allows tests to be run one after another and have the reulsts neatly colected.
 * */
public class testAllNetworksQueue {
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		

		
		runFFHiddenNodeEx();
		
		
		runFFLearningRateEx();

		
		
	
		 runFFTrainingExampleEx();
		

	}
	
	
	
	public static void runFFHiddenNodeEx() throws IOException, ClassNotFoundException{
		 String filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/15/Results";
		 String filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/15/TrainedSetOutputWeights.txt";
		 String filePathTrainedHiddenWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/15/TrainedSetHiddenWeights.txt";
	//	 15 Hidden Nodes, learning rate 0.3, binary input, continue training 5 epochs at a time
	/*	 	NeuralNet firstFF1 = new NeuralNet(15,5,0.3,0,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 1-5
		NeuralNet firstFF2 = new NeuralNet(15,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 6-10
		NeuralNet firstFF3 = new NeuralNet(15,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 11-15
		NeuralNet firstFF4 = new NeuralNet(15,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 16-20
			try
			{
			firstFF1=null;
			firstFF2=null;
			firstFF3=null;
			firstFF4=null;
			System.gc();
			    Thread.sleep(10000); // Sleep for one second
			}
			catch (InterruptedException e)
			{
			    Thread.currentThread().interrupt();
			}
		
		  filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/30/Results";
		  filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/30/TrainedSetOutputWeights.txt";
		  filePathTrainedHiddenWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/30/TrainedSetHiddenWeights.txt";
		// 30 Hidden Nodes, learning rate 0.3, binary input, continue training 5 epochs at a time
		NeuralNet firstFF1A = new NeuralNet(30,5,0.3,0,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 1-5
		NeuralNet firstFF2A = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 6-10
		NeuralNet firstFF3A = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 11-15
		NeuralNet firstFF4A = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 16-20
		try
		{
		    Thread.sleep(10000); // Sleep for one second
		}
		catch (InterruptedException e)
		{
		    Thread.currentThread().interrupt();
		}
		 filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/50/Results";
		  filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/50/TrainedSetOutputWeights.txt";
		  filePathTrainedHiddenWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/50/TrainedSetHiddenWeights.txt";
		// 50 Hidden Nodes, learning rate 0.3, binary input, continue training 5 epochs at a time
		NeuralNet firstFF1AA = new NeuralNet(50,5,0.3,0,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 1-5
		NeuralNet firstFF2AA = new NeuralNet(50,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 6-10
		NeuralNet firstFF22AA = new NeuralNet(50,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 11-15
		NeuralNet firstFF3AA = new NeuralNet(50,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 16-20
		try
		{
		    Thread.sleep(10000); // Sleep for one second
		}
		catch (InterruptedException e)
		{
		    Thread.currentThread().interrupt();
		}*/
		 filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/100/Results";
		  filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/100/TrainedSetOutputWeights.txt";
		  filePathTrainedHiddenWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/100/TrainedSetHiddenWeights.txt";
		// 100 Hidden Nodes, learning rate 0.3, binary input, continue training 5 epochs at a time
		//NeuralNet FF1B = new NeuralNet(100,5,0.3,0,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 1-5
		NeuralNet FF2B = new NeuralNet(100,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 6-10
		NeuralNet FF3B = new NeuralNet(100,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 11-15
		NeuralNet FF4B = new NeuralNet(100,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 16-20
		try
		{
		    Thread.sleep(10000); // Sleep for one second
		}
		catch (InterruptedException e)
		{
		    Thread.currentThread().interrupt();
		}
		
		
	}
		
	
	
public static void runFFLearningRateEx() throws IOException, ClassNotFoundException{
	
	  String filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/0.1/Results";
	  String filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/0.1/TrainedSetOutputWeights.txt";
	  String filePathTrainedHiddenWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/0.1/TrainedSetHiddenWeights.txt";
	// 30 Hidden Nodes, learning rate 0.1, binary input, continue training 5 epochs at a time
	NeuralNet secondFF1 = new NeuralNet(30,5,0.1,0,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 1-5
	NeuralNet secondFF2 = new NeuralNet(30,5,0.1,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 6-10
	NeuralNet secondFF3 = new NeuralNet(30,5,0.1,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 11-15
	NeuralNet secondFF4 = new NeuralNet(30,5,0.1,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 16-20
	try
	{
	    Thread.sleep(10000); // Sleep for one second
	}
	catch (InterruptedException e)
	{
	    Thread.currentThread().interrupt();
	}
	  filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/0.2/Results";
	  filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/0.2/TrainedSetOutputWeights.txt";
	  filePathTrainedHiddenWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/0.2/TrainedSetHiddenWeights.txt";
	// 30 Hidden Nodes, learning rate 0.2, binary input, continue training 5 epochs at a time
	NeuralNet secondFF1B = new NeuralNet(30,5,0.2,0,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 1-5
	NeuralNet secondFF2B = new NeuralNet(30,5,0.2,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 6-10
	NeuralNet secondFF3B = new NeuralNet(30,5,0.2,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 11-15
	NeuralNet secondFF4B = new NeuralNet(30,5,0.2,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 16-20
	try
	{
	    Thread.sleep(10000); // Sleep for one second
	}
	catch (InterruptedException e)
	{
	    Thread.currentThread().interrupt();
	}
	
	  filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/0.5/Results";
	  filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/0.5/TrainedSetOutputWeights.txt";
	  filePathTrainedHiddenWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/0.5/TrainedSetHiddenWeights.txt";
	// 30 Hidden Nodes, learning rate 0.5, binary input, continue training 5 epochs at a time
	NeuralNet secondFF1C = new NeuralNet(30,5,0.5,0,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 1-5
	NeuralNet secondFF2C = new NeuralNet(30,5,0.5,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 6-10
	NeuralNet secondFF3C = new NeuralNet(30,5,0.5,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 11-15
	NeuralNet secondFF4C = new NeuralNet(30,5,0.5,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 16-20
	try
	{
	    Thread.sleep(10000); // Sleep for one second
	}
	catch (InterruptedException e)
	{
	    Thread.currentThread().interrupt();
	}
	
	  filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/0.7/Results";
	  filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/0.7/TrainedSetOutputWeights.txt";
	  filePathTrainedHiddenWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/0.7/TrainedSetHiddenWeights.txt";
	// 30 Hidden Nodes, learning rate 0.7, binary input, continue training 5 epochs at a time
	NeuralNet secondFF1D = new NeuralNet(30,5,0.7,0,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 1-5
	NeuralNet secondFF2D = new NeuralNet(30,5,0.7,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 6-10
	NeuralNet secondFF3D = new NeuralNet(30,5,0.7,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 11-15
	NeuralNet secondFF4D = new NeuralNet(30,5,0.7,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 16-20
	try
	{
	    Thread.sleep(10000); // Sleep for one second
	}
	catch (InterruptedException e)
	{
	    Thread.currentThread().interrupt();
	}
	
	
	
	  filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/1/Results";
	  filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/1/TrainedSetOutputWeights.txt";
	  filePathTrainedHiddenWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/1/TrainedSetHiddenWeights.txt";
	// 30 Hidden Nodes, learning rate 1, binary input, continue training 5 epochs at a time
	NeuralNet secondFF1E = new NeuralNet(30,5,1,0,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 1-5
	NeuralNet secondFF2E = new NeuralNet(30,5,1,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 6-10
	NeuralNet secondFF3E = new NeuralNet(30,5,1,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 11-15
	NeuralNet secondFF4E = new NeuralNet(30,5,1,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 16-20
	
	
		
	}

			public static void runFFTrainingExampleEx() throws IOException, ClassNotFoundException{

				  String filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/1t/Results";
				  String filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/1t/TrainedSetOutputWeights.txt";
				  String filePathTrainedHiddenWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/1t/TrainedSetHiddenWeights.txt";
				// 30 Hidden Nodes, learning rate 0.3, binary input, continue training 5 epochs at a time
				NeuralNet secondFF1 = new NeuralNet(30,5,0.3,0,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 1-5
				NeuralNet secondFF2 = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 6-10
				NeuralNet secondFF3 = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 11-15
				NeuralNet secondFF4 = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,1);//epochs 16-20
				try
				{
				    Thread.sleep(10000); // Sleep for one second
				}
				catch (InterruptedException e)
				{
				    Thread.currentThread().interrupt();
				}
				  filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/2t/Results";
				  filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/2t/TrainedSetOutputWeights.txt";
				  filePathTrainedHiddenWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/2t/TrainedSetHiddenWeights.txt";
				// 30 Hidden Nodes, learning rate 0.3, binary input, continue training 5 epochs at a time
				NeuralNet secondFF1B = new NeuralNet(30,5,0.3,0,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,2);//epochs 1-5
				NeuralNet secondFF2B = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,2);//epochs 6-10
				NeuralNet secondFF3B = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,2);//epochs 11-15
				NeuralNet secondFF4B = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,2);//epochs 16-20
				try
				{
				    Thread.sleep(10000); // Sleep for one second
				}
				catch (InterruptedException e)
				{
				    Thread.currentThread().interrupt();
				}
				
				  filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/10t/Results";
				  filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/10t/TrainedSetOutputWeights.txt";
				  filePathTrainedHiddenWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/10t/TrainedSetHiddenWeights.txt";
				// 30 Hidden Nodes, learning rate 0.3, binary input, continue training 5 epochs at a time
				NeuralNet secondFF1C = new NeuralNet(30,5,0.3,0,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,10);//epochs 1-5
				NeuralNet secondFF2C = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,10);//epochs 6-10
				NeuralNet secondFF3C = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,10);//epochs 11-15
				NeuralNet secondFF4C = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,10);//epochs 16-20
				try
				{
				    Thread.sleep(10000); // Sleep for one second
				}
				catch (InterruptedException e)
				{
				    Thread.currentThread().interrupt();
				}
				
				  filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/30t/Results";
				  filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/30t/TrainedSetOutputWeights.txt";
				  filePathTrainedHiddenWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/30t/TrainedSetHiddenWeights.txt";
				// 30 Hidden Nodes, learning rate 0.7, binary input, continue training 5 epochs at a time
				NeuralNet secondFF1D = new NeuralNet(30,5,0.3,0,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,30);//epochs 1-5
				NeuralNet secondFF2D = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,30);//epochs 6-10
				NeuralNet secondFF3D = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,30);//epochs 11-15
				NeuralNet secondFF4D = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,30);//epochs 16-20
				try
				{
				    Thread.sleep(10000); // Sleep for one second
				}
				catch (InterruptedException e)
				{
				    Thread.currentThread().interrupt();
				}
				
				
				
				  filePathResults = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/60t/Results";
				  filePathTrainedOutputWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/60t/TrainedSetOutputWeights.txt";
				  filePathTrainedHiddenWeights = "/Users/zackeryleman/Desktop/NeuralNetOutput/FF/60t/TrainedSetHiddenWeights.txt";
				// 30 Hidden Nodes, learning rate 1, binary input, continue training 5 epochs at a time
				NeuralNet secondFF1E = new NeuralNet(30,5,0.3,0,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,60);//epochs 1-5
				NeuralNet secondFF2E = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,60);//epochs 6-10
				NeuralNet secondFF3E = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,60);//epochs 11-15
				NeuralNet secondFF4E = new NeuralNet(30,5,0.3,2,true,filePathResults,filePathTrainedOutputWeights,filePathTrainedHiddenWeights,60);//epochs 16-20
				

}
			
}


//KNearestNeighbors firstkNN = new KNearestNeighbors(3,false,5,200);




//sigmaSquaredSquared = 1000000; //:  Experiment with numbers for this value 1000000     (sigmaSquared=100000 gave 29.86%)   (sigmaSquared=1000000 gave 86.36% another 20 epochs or so brings it to 92% with leanring at 1)
// For binary I am testing with sigam =  between 15-10 (11 seems optimal)
//RadialBasisFunction firstRBF = new RadialBasisFunction(20,false,1000000,5,0.5,false);


