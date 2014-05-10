/*
 *This class will read in CAPTCHA image files.
 *It will parse the image, isolate individual letters, and put them in a data structure for the neural net to process.
 *For training data, it will load single letter image files. It will also have the ability to train on full CAPTCHA's if also provided the solution.
 *Currently this is a nonfunctional class and the entirety of the below code may likely be replaced.
 */

//IVY: The goal is to fill up the "alltrainingData" and "allCaptchas" arrays appropriately, so that we can pass them to the neural networks for training and testing
//The hope is that this class will be finished tonight.

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
	//Number of chars in CAPTCHA
	public static  final int LENGTH_OF_CAPTCHA =5;
	//Holds all the Captcha images
	public static ArrayList<BufferedImage> img = new ArrayList<BufferedImage>();
	//Holds all the Captcha names (solution) (names at corresponding indicies in img array)
	public static ArrayList<String> imgName = new ArrayList<String>();
	//For debugging purposes, currently this array will hold 5 DigitImage elements each of which is one of the 5 chars in the Captcha
	public static ArrayList<DigitImage> images = new ArrayList<DigitImage>();
	//Is the parent data structure that holds arrayLists of captcha characters in the digitImage format
	//This data structure is then fed to the Neural Networks
	public static ArrayList<ArrayList<DigitImage>> allCaptchas = new ArrayList<ArrayList<DigitImage>> ();
	// Holds all of the training images that will be fed to the neural networks
	public static ArrayList<DigitImage> alltrainingData = new ArrayList<DigitImage>();

	public static void main(String args[]) throws IOException {
		
		//Reads and breaks up all captchas. Stores them to be read by the neural nets
		readAllCaptchas();
		
		//Reads all the  individual letters and numbers
	//	readAllTrainingData();
		
	}
	
	public static void readAllCaptchas() throws IOException{
	
	int[] imageData= new int[9*22];//Just a large number for now that should hold the 9*84 px images
	//Import the CAPTCHAs. Images in one array and names at corresponding indicies in other array
	File folder = new File("Captcha Testing Data");
	File[] listOfFiles = folder.listFiles();
	for (int i = 0; i < listOfFiles.length; i++) {
		if (listOfFiles[i].isFile()&& !(listOfFiles[i].getName().contentEquals(".DS_Store"))) {
			System.out.println("File " + listOfFiles[i].getName());
			img.add(ImageIO.read(new File("Captcha Testing Data/" +listOfFiles[i].getName())));
			imgName.add(listOfFiles[i].getName());
		}
	}

	//For every CAPTCHA
	for (int i=0; i<folder.listFiles().length;i++){	


		int lastColumnExplored=-1;
		//For a single CAPTCHA
		for (int j=0; j<LENGTH_OF_CAPTCHA;j++){
			//For each char in the CAPTCHA


			//IVY
			//Starting from lastColumnExplored+1
			//Search  columns until a no black pixels are detected
			//Then search for the next column with black pixels 
			//The the value of column to x
			int x=0;// not zero

			lastColumnExplored=x+7;
			BufferedImage subImage=img.get(i).getSubimage(x,0 , 9, 22);//reads nine pixels in width and entire height
			try{				
				ImageIO.write(subImage, "gif", new File("letter_output.gif")  );//so we can visualize the output
				int w = subImage.getWidth();
				int h = subImage.getHeight();
				int[] dataBuffInt = subImage.getRGB(0, 0, w, h, null, 0, w); 
				for(int u=0;u<dataBuffInt.length; u++){
					Color c = new Color(dataBuffInt[u]);
					//System.out.print(c.getRed()+ " "+c.getGreen()+ " "  + c.getBlue()+ " "+c.getAlpha()+" "+u);
					//System.out.println("");
					imageData[i]=c.getRed();
				}
			}catch(IOException e){
				System.out.println(e.getMessage());
			}	
			//Add letter to array holding each letter of a single captcha
			//IVY: This array "images"  currently has all chars from all CAPTCHAS added to it.  Change this so each captcha has its own arraylist.
			//After each arraylist for a single captcha has all chars from the captcha added, add the array list to a parent arraylist.
			try { 
				Integer.parseInt(imgName.get(i).substring(j, j+1)); 
				images.add(new DigitImage(Integer.parseInt(imgName.get(i).substring(j, j+1)), imageData, false));
			} catch(NumberFormatException e) { 
				//IVY: Convert  imgName.get(i).substring(j, j+1) into a number and replace the "9" below
				images.add(new DigitImage(9, imageData, false));
			}
			imageData=null;
		}
		//Add broken up CAPTCHA to parent data structure
		allCaptchas.add(images);
	}
}

	
	public static void readAllTrainingData(){
		//Ivy: Here is a rough skeleton. Use the code from the the other method to help write this.
		
		
		int[] imageData= new int[9*22];//Just a large number for now that should hold the 9*84 px images
		//read an image to a buffered image
		
		
		//If image has less than 9 columns (width is less than 9) append enough zeros to the array to simulate the missing columns.
		//we want a total of nine columns in all cases
		
		//If image has more than 9 columns
		//read  subimage  up to nine columns. i.e don't read  read column ten 
	//USE:	BufferedImage subImage=   .getSubimage(x,0 , 9, 22);//reads nine pixels in width and entire height
		
	
		
		//"label" will be the name of the folder in which the image was read from
		//alltrainingData.add(new DigitImage(label, imageData, false));
		
	}
	
}
