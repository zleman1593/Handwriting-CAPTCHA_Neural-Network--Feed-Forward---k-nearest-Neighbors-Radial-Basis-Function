/*
 * Adapted from IntelliJ IDEA user vivin ( 11/11/11).
 * This is a class which stores a single image and corresponding value for the training and testing data.
 * It converts the image data to a binary representation.
 */

import java.util.*;
import java.io.IOException;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

public class DigitImage {

	// Is the number that is in the image
	private int label;
	// This vector represents the number that is in the image in a vector form.
	private ArrayList<Integer> solutionVector = new ArrayList<Integer>(10);
	// This is an array of pixels with value 0 or 1
	private double[] data;

	public DigitImage(int label, byte[] data, boolean binary) {
		this.label = label;
		this.data = new double[data.length];
		for (int i = 0; i < this.data.length; i++) {
			this.data[i] = data[i] & 0xFF; // convert to unsigned
		}
		if (binary==true){
			otsu();
		}
	}
	
	/*public DigitImage(int label, int[] data, boolean binary) {
		this.label = label;
		this.data = new double[data.length];
		for (int i = 0; i < this.data.length; i++) {
			this.data[i] = data[i] & 0xFF; // convert to unsigned
		}
		if (binary==true){
			otsu();
		}
	}*/

	// Uses Otsu's Threshold algorithm to convert from grayscale to black and
	// white
	private void otsu() {
		int[] histogram = new int[256];

		for (double datum : data) {
			histogram[(int) datum]++;
		}

		double sum = 0;
		for (int i = 0; i < histogram.length; i++) {
			sum += i * histogram[i];
		}

		double sumB = 0;
		int wB = 0;
		int wF = 0;

		double maxVariance = 0;
		int threshold = 0;

		int i = 0;
		boolean found = false;

		while (i < histogram.length && !found) {
			wB += histogram[i];

			if (wB != 0) {
				wF = data.length - wB;

				if (wF != 0) {
					sumB += (i * histogram[i]);

					double mB = sumB / wB;
					double mF = (sum - sumB) / wF;

					double varianceBetween = wB * Math.pow((mB - mF), 2);

					if (varianceBetween > maxVariance) {
						maxVariance = varianceBetween;
						threshold = i;
					}
				}

				else {
					found = true;
				}
			}

			i++;
		}

		for (i = 0; i < data.length; i++) {
			data[i] = data[i] <= threshold ? 0 : 1;
		}

	}

	// return number in image
	public int getLabel() {
		return label;
	}

	// return image as a plain array
	public double[] getData() {
		return data;
	}

	// return number in image in vector form
	public ArrayList<Integer> getSolutionVector() {
		return solutionVector;
	}

	// return image as an ArrayList
	public ArrayList<Double> getArrayListData() {

		ArrayList<Double> doubleList = new ArrayList<Double>(data.length);
		for (int index = 0; index < data.length; index++) {
			doubleList.add(data[index]);
		}
		return doubleList;
	}

	/*
	 * Creates 10-dimensional unit vector with a 1.0 in the jth position and
	 * zeroes elsewhere. This is used to convert a digit (0...9) into a
	 * corresponding desired output from the neural network, making the training
	 * data easier to work with.
	 */
	public void vectorizeTrainingData() {
		for (int i = 0; i < 10; i++) {
			solutionVector.add(0);
		}
		solutionVector.set((int) label, 1);
	}

}