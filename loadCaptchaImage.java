/*
 *This class will read in CAPTCHA image files.
 *It will parse the image, isolate individual letters, and put them in a data structure for the neural net to process.
 *For training data, it will load single letter image files. It will also have the ability to train on full CAPTCHA's if also provided the solution.
 *Currently this is a nonfunctional class and the entirety of the below code may likely be replaced.
 */

import javax.imageio.*;

import java.util.*;
import java.io.IOException;
import java.lang.Math;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.ByteArrayOutputStream;
import java.awt.image.DataBufferByte;
import java.awt.Color;

import javax.imageio.ImageIO;

public class loadCaptchaImage {
	// List of captchas fed to the Neural Networks.
	private static ArrayList<ArrayList<DigitImage>> allCaptchas = new ArrayList<ArrayList<DigitImage>> ();
	// List of training set of characters fed to the Neural Networks.
	private static ArrayList<DigitImage> alltrainingData = new ArrayList<DigitImage>();
	
	private static final int CHAR_WIDTH = 9; // Cut-off width of a character.
	private static final int CHAR_HEIGHT = 22; // Cut-off height of a character.
	//private static final int WHITE_COLOR = 255; // RGB value for white: 255, 255, 255.
	 //private static final int WHITE_BINARY = 0; // Binary representation of a white pixel.
	private static final int BLACK_COLOR = 0; // RGB value for black: 0, 0, 0.
	private static final int BLACK_BINARY = 1; // Binary representation of a black pixel. 
	private static final int CHAR_OFFSET = 87; // Convert char to int so that a is 10, b is 11, etc.
	private static final int NUM_CHARS_IN_CAPTCHA = 6; // 6 characters in each Captcha.
	
	// For testing only.
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		readAllTrainingData();
		//readAllCaptchas();
	}
	
	// Constructor.
	public loadCaptchaImage() throws IOException {
		// Reads and breaks up all Captchas for testing.
		readAllCaptchas();
		// Reads all the  individual characters for training.
		readAllTrainingData();
	}

	// Reads all the captcha images.
	public static void readAllCaptchas() throws IOException{
		// Holds all the Captcha images.
		ArrayList<BufferedImage> imgs = new ArrayList<BufferedImage>();
		// Holds all the Captcha names (correct answers).
		ArrayList<String> imgNames = new ArrayList<String>();
		// Import the Captcha images from folder.
		File folder = new File("src/Captcha Testing Data");
		File[] listOfFiles = folder.listFiles();
		for (int f = 0; f < listOfFiles.length; f++) {
			if (listOfFiles[f].isFile() && !(listOfFiles[f].getName().contentEquals(".DS_Store"))
					&& !(listOfFiles[f].getName().contentEquals("Thumbs.db"))) {
//				System.out.println("File " + listOfFiles[f].getName());
				imgs.add(ImageIO.read(new File("src/Captcha Testing Data/" + listOfFiles[f].getName())));
				imgNames.add(listOfFiles[f].getName());
			}
		}
		// Iterate through the list of Captcha images.
		for (int i = 0; i < imgs.size(); i++) {	
			BufferedImage captchaImg = imgs.get(i);
			ArrayList<DigitImage> oneCaptcha = new ArrayList<DigitImage>(); // List of character images.
			oneCaptcha.clear();
			int captchaCharPos = 0; // How many characters of the captcha have been read.
			// Read a single Captcha image.
			for (int j = 0; j < captchaImg.getWidth(); j++) {
				// Skip white spaces and find where the next character starts, then j jumps to that column.
				j = findCharCol(captchaImg, j);

				// Reaching the end of the Captcha image. 
				if (j + CHAR_WIDTH >= captchaImg.getWidth() || captchaCharPos >= NUM_CHARS_IN_CAPTCHA) {
					break;
				}
				
				// Stores one character of a Captcha.
				int[] charPixels = new int[CHAR_WIDTH * CHAR_HEIGHT];
				BufferedImage charImage = captchaImg.getSubimage(j, 0 , CHAR_WIDTH, CHAR_HEIGHT);
				scanCharPixels(charImage, charPixels, 0, 0);
				j = j + CHAR_WIDTH;
				
				// Turn a character in a Captcha into a DigitImage and add it to the Captcha's list of DigitImages (characters).
				int num = charToInt(imgNames, i, captchaCharPos);
				oneCaptcha.add(new DigitImage(num, charPixels, false));
				captchaCharPos++;
			}
			// Add one Captcha to the list of Captchas.
			allCaptchas.add(oneCaptcha);
		}
	}
	
	// Read all the training images (characters).
	public static void readAllTrainingData() throws IOException{
		// Holds all the Captcha images.
		ArrayList<BufferedImage> trainingImgs = new ArrayList<BufferedImage>();
		// Holds all the Captcha names (correct answers).
		ArrayList<String> trainingImgNames = new ArrayList<String>();
		File folder = new File("src/Captcha Training Data");
		File [] listOfFolders = folder.listFiles();
		for (int f = 0; f < listOfFolders.length; f++) {
//			System.out.println(listOfFolders[f].getName());
			if (!(listOfFolders[f].getName().contentEquals(".DS_Store")) && 
					!(listOfFolders[f].getName().contentEquals("Thumbs.db"))) {
				// Subfolders of the folder.
				String folderName = listOfFolders[f].getName();
				String folderPath = "src/Captcha Training Data/" + folderName;
				File[] listOfFiles = new File (folderPath).listFiles();
				// Go through the list of files in the sub-folders.
				for (int fi = 0; fi < listOfFiles.length; fi++) {
					if (listOfFiles[fi].isFile() && !(listOfFiles[fi].getName().contentEquals("Thumbs.db"))) {
						String fileName = listOfFiles[fi].getName();
//						System.out.println(fileName);
						trainingImgs.add(ImageIO.read(new File(folderPath + "/" + fileName)));
						trainingImgNames.add(folderName);
					}
				}
			}
		}
		for (int i = 0; i < trainingImgs.size(); i++) {	
			//For a single training character image.
			BufferedImage charImg = trainingImgs.get(i);
			// Stores the character image.
			int[] charPixels = new int[CHAR_WIDTH * CHAR_HEIGHT];
			// If the image is wider/taller than the width range, only look at the width/height range. 
			int offsetHeight = 0, offsetWidth = 0;
			if (charImg.getWidth() > CHAR_WIDTH) {
				offsetWidth = charImg.getWidth() - CHAR_WIDTH;
			}
			if (charImg.getHeight() > CHAR_HEIGHT) {
				offsetHeight = charImg.getHeight() - CHAR_HEIGHT;
			}
			scanCharPixels(charImg, charPixels, offsetHeight, offsetWidth);
			// Turn this training character into a DigitImage and add it to the Captcha's list of chars.
			int num = charToInt(trainingImgNames, i, 0);
			alltrainingData.add(new DigitImage(num, charPixels, false));
		
		}
	}
	
	// Finds the column where the next character starts (skipping over white space columns).
	public static int findCharCol(BufferedImage image, int currentCol) {
		for (int col = currentCol; col < image.getWidth(); col++) {
			for (int row = 0; row < image.getHeight(); row++) {
				Color c = new Color(image.getRGB(col, row));
				// Black pixel detected, record which column and set j.
				if (c.getRed() == BLACK_COLOR) {
					return col;
				}
			}
		}
		return -1; // No black pixel found.
	}
	
	// Given a character image, scans and stores its (binary) pixel values.
	public static void scanCharPixels (BufferedImage image, int[] pixels, int offsetH, int offsetW) {
		// Scans each pixel of this character.
		int height = image.getHeight() - offsetH;
		int width = image.getWidth() - offsetW;
		for (int y = 0; y < height; y++) {
		    for (int x = 0; x < width; x++) {
		    	Color c = new Color(image.getRGB(x, y));
		        // Convert 2-D positions to 1-D.
		    	int pos = y * width + x;
		        if (c.getRed() == BLACK_COLOR) {
		        	pixels[pos] = BLACK_BINARY;
		        }
		    }
		}
	}
	
	// We can't directly cast char to int because according to the setup for our NN 'a' needs to be 10 and the rest follows.
	public static int charToInt(ArrayList<String> imgName, int imgIndex, int charIndex) {	
		char c = imgName.get(imgIndex).charAt(charIndex);
		if (Character.isDigit(c)) {
			return (int)c;
		}
		return(int)c - CHAR_OFFSET;
	}
	
	// Returns all training data - list of character images.
	public ArrayList<DigitImage> getTrainingData() {
		return alltrainingData;
	}
	
	// Returns all Captchas - list of Captcha arrays, each array is a list of character images.
	public ArrayList<ArrayList<DigitImage>> getTestingData() {
		return allCaptchas;
	}
}
