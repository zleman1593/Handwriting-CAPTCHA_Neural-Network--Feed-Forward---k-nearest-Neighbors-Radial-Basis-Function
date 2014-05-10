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
import java.net.*;

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

	public static void main(String args[]) throws IOException {
		  int[] imageData= new int[154];//Just a large number for now that should hold the 7*84 px images
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
			
			
	int lastColumnExplored=0;
			
			//For a single CAPTCHA
			for (int j=0; j<LENGTH_OF_CAPTCHA;j++){
				//For each char in the CAPTCHA
				
				//Starting from lastColumnExplored+1
				//Search  columns until a black pixel is detected
				//The pass x value of column to x
				
				
				
			int x=0;
			lastColumnExplored=x+7;
				BufferedImage subImage=img.get(i).getSubimage(0,0 , 7, 22);
				//byte[] byte_buffer = ((DataBufferByte) subImage.getRaster().getDataBuffer()).getData();
				try{				
					ImageIO.write(subImage, "gif", new File("letter_output.gif")  );//so we can visualize the output
					 int w = subImage.getWidth();
					    int h = subImage.getHeight();
					   int[] dataBuffInt = subImage.getRGB(0, 0, w, h, null, 0, w); 
					   for(int u=0;u<dataBuffInt.length; u++){
					    Color c = new Color(dataBuffInt[u]);
					    System.out.print(c.getRed()+ " "+c.getGreen()+ " "  + c.getBlue()+ " "+c.getAlpha()+" "+u);
					    System.out.println("");
					    imageData[i]=c.getRed();
					   }

					}catch(IOException e){
						System.out.println(e.getMessage());
					}	
				//Add letter to array holding each letter of a single captcha
				//Note: This array will currently have all chars from all CAPTCHAS added to it. This will change. 
				try { 
			        Integer.parseInt(imgName.get(i).substring(j, j+1)); 
			    	images.add(new DigitImage(Integer.parseInt(imgName.get(i).substring(j, j+1)), imageData, false));
			    } catch(NumberFormatException e) { 
			    	images.add(new DigitImage(9, imageData, true));//because DigitImage can only take ints for now. Just pass 9 if there is a letter
			    }
				imageData=null;
			}
			
			
			

		}
	}

}
