/*
 *This class stores the output of the neural network for each testing image. 
 * It indicates whether the network was correct, what the network guessed, and  what the true value was.
 * When an array of OutputVector is created it makes it easier to store and write to a file.
 */

public class OutputVector {
	
	private double expectedOutput;
	private int neuralNetOutput;
	private boolean correct;
	
	public OutputVector(double expectedOutput, int neuralNetOutput) {
		this.expectedOutput = expectedOutput;
		this.neuralNetOutput = neuralNetOutput;
		if(expectedOutput==neuralNetOutput){
			correct=true;
		} else{
			correct=false;
		}
	}

	public double getExpectedOutput() {
		return expectedOutput;
	}
	
	
	public int getNeuralNetOutput() {
		return neuralNetOutput;
	}
	
	
	public boolean getCorrect() {
		return correct;
	}
}
