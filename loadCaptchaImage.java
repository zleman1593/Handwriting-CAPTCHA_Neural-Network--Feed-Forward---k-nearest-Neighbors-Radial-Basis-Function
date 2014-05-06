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

public class loadCaptchaImage {
	public static BufferedReader fileReader;

	public static void main(String args[]) {

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("xfnrsn.gif"));
		} catch (IOException e) {
		}
		int q=img.getRGB(0,0);
		int q2=img.getRGB(1,1);
		int q3=img.getRGB(10,10);
		getHistogram(img);
		
		// openFile("xfnrsn.gif");

		// closeFile();

	}

	public static void openFile(String filename) {
		try { // try to read from file
			fileReader = new BufferedReader(new FileReader(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void closeFile() {
		try {
			if (fileReader != null)
				fileReader.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void getHistogram(BufferedImage image) {

		int height = image.getHeight();
		int width = image.getWidth();
		Raster raster = image.getRaster();
		int[][] bins = new int[3][256];

		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++) {
				bins[0][raster.getSample(i, j, 0)]++;
				bins[1][raster.getSample(i, j, 1)]++;
				bins[2][raster.getSample(i, j, 2)]++;
			}
		System.out.println(" ");
	}

}
