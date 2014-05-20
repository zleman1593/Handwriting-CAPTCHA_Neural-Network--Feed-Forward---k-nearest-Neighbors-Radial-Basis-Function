import java.io.*;
import java.util.*;

/*
 * This class compiles raw data into text files that we can copy into Excel for graphing the data points.
 * Note that the raw data folders need to be in the following format:
 *  First layer: One big folder containing all the folders for all trials;
 * 	Second layer: Each trial folder contains the folders for all experiments;
 *  Third layer: Each experiment folder contains all the files that the computer recorded.
 */

public class CompileData {
	
	// Positions in string array.
	private static final int ACCURACY = 0; 		
	private static final int TRAIN_TIME = 1;	
	private static final int TEST_TIME = 2;
	// Number of images.
	private static final double IMG_NUM = 10000;
	// Look for the following keywords.
	private static final String ACCURACY_LINE_START = "Analyzed";
	private static final String TRAIN_TIME_LINE_START = "Training";
	private static final String TEST_TIME_LINE_START = "Testing";
	// Folder name. May need to delete src/
	private static String dataFolderName = "src/GraphingData"; 
	
	public static void main(String[] args) throws IOException {
		// Iterate through all files.
		File folder = new File(dataFolderName);
		File[] listOfParentFolders = folder.listFiles();
		for (int p = 0; p < listOfParentFolders.length; p++) {
			String parentFolderName = listOfParentFolders[p].getName();
			if (!parentFolderName.contentEquals(".DS_Store")) {
//				System.out.println(parentFolderName);
//				System.out.println();
				File [] listOfFolders = listOfParentFolders[p].listFiles();
				for (int f = 0; f < listOfFolders.length; f++) {
					File childFolder = listOfFolders[f];
					String folderName = childFolder.getName();
					if (!folderName.contentEquals(".DS_Store") && childFolder.isDirectory() && childFolder.list().length > 0) {
//						System.out.println();
//						System.out.println(folderName);
						File[] listOfFiles = listOfFolders[f].listFiles();
						// Sort the files by date modified so that we can order the information according to epoch #.
						Arrays.sort(listOfFiles, new Comparator<File>(){
						    public int compare(File f1, File f2)
						    {
						        return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
						    } 
						 });
						String[] storedData = {"", "", ""}; 
						// Read accuracy, training time, and testing time into storedData.
						readDatafromFolder(listOfFiles, storedData);
						
						String [] accuracySplitted = storedData[ACCURACY].split(" ");
						String [] trainTimeSplitted = storedData[TRAIN_TIME].split(" ");
						String [] testTimeSplitted = storedData[TEST_TIME].split(" ");
						
						// Compile all accuracy data.
						File accuracyFile = new File(dataFolderName + "/" + parentFolderName + "/" + folderName + "/accuracyCompiled.txt");
						if (!accuracyFile.exists()) {
//							System.out.println(accuracyFile.getPath());
							accuracyFile.createNewFile();
						}
					    try {
					        BufferedWriter out = new BufferedWriter(new FileWriter(accuracyFile));
					        for (int i = 0; i < accuracySplitted.length; i++) {
					        	out.write(accuracySplitted[i] + "\n");
					        }
					        out.close();
					    } catch (IOException e) {
					    	e.getMessage();
					    }
						// Compile all training time data.
						File trainTimeFile = new File(dataFolderName + "/" + parentFolderName + "/" + folderName + "/trainTimeCompiled.txt");
						if (!trainTimeFile.exists()) {
							trainTimeFile.createNewFile();
						}
						try {
					        BufferedWriter out = new BufferedWriter(new FileWriter(trainTimeFile));
					        for (int i = 0; i < trainTimeSplitted.length; i++) {
					        	out.write(trainTimeSplitted[i]+ "\n");
					        }
					        out.close();
					    } catch (IOException e) {
					    	e.getMessage();
					    }
						// Compile all testing time data.
						File testTimeFile = new File(dataFolderName + "/" + parentFolderName + "/" + folderName + "/testTimeCompiled.txt");
						if (!testTimeFile.exists()) {
							testTimeFile.createNewFile();
						}
						try {
					        BufferedWriter out = new BufferedWriter(new FileWriter(testTimeFile));
					        for (int i = 0; i < testTimeSplitted.length; i++) {
					        	out.write(testTimeSplitted[i]+ "\n");
					        }
					        out.close();
					    } catch (IOException e) {
					    	e.getMessage();
					    }
					}
				}
			}
		}
	}
	
	// This method reads information from the list of files in a folder and stores information read in the data string array.
	private static void readDatafromFolder(File[] listFiles, String[] data) throws IOException {
		String line = null;
		StringBuilder accuracy = new StringBuilder();
		StringBuilder trainTime = new StringBuilder();
		StringBuilder testTime = new StringBuilder();
		
		// Iterate through the list of files.
		for (int fi = 0; fi < listFiles.length; fi++) {
			File file = listFiles[fi];
			if (file.isFile() && file.getName().startsWith("Results")) {
				// Read file.
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
				while((line = br.readLine())!= null){
			        // Record accuracy rate.
					if (line.startsWith(ACCURACY_LINE_START)) { 
						// \\s+ means any number of white spaces between tokens
						String [] tokens = line.split("\\s+");
					    for (int i = 0; i < tokens.length; i ++) {
					    	String s = tokens[i];
					    	if (isDouble(s) && Double.parseDouble(s) != IMG_NUM) {
					    		accuracy.append(s + " ");
					    	}
					    }
			        }
					// Record training time.
			        else if (line.startsWith(TRAIN_TIME_LINE_START)) {
						// \\s+ means any number of white spaces between tokens
						String [] tokens = line.split("\\s+");
					    for (int i = 0; i < tokens.length; i ++) {
					    	String s = tokens[i];
					    	if (isDouble(s)) {
					    		trainTime.append(s + " ");
					    	}
					    }
			        }
					// Record testing time.
			        else if (line.startsWith(TEST_TIME_LINE_START)) {
			        	// \\s+ means any number of white spaces between tokens
						String [] tokens = line.split("\\s+");
					    for (int i = 0; i < tokens.length; i ++) {
					    	String s = tokens[i];
					    	if (isDouble(s)) {
					    		testTime.append(s + " ");
					    	}
					    }
			        }
				}
				br.close();
			}
		}
		data[ACCURACY] += accuracy.toString();
		data[TRAIN_TIME] += trainTime.toString();
		data[TEST_TIME] += testTime.toString();
	}
	
	// Check if this string is a number.
	public static boolean isDouble(String str) {
	        try {
	            Double.parseDouble(str);
	            return true;
	        } catch (NumberFormatException e) {
	            return false;
	        }
	    }
}
