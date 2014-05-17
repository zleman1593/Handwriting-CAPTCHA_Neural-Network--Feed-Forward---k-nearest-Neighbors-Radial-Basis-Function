MNIST Handwriting Recongnition Neural Networks. Feed-Forward back propagation, k-nearest-Neighbor, and Radial Basis Function
===========================================================

Hand Writing Recognition and Simple CAPTCHA Neural Networks:
Feed-Forward Backpropagation network,  K-nearest Neighbor network,  Radial Basis Function Network

  CS 3425 Final project
  Spring 2014
  Min "Ivy" Xing, Zackery Leman
  
  This network works by reading in an image (image data provided by MNIST) and then selecting the number 0-9.
 
All of the networks are objects that need to be created via a constructor in another file (testAllNetworksQueue). If the testAllNetworksQueue.java file is compiled and run, then for demonstration purposes, one object of each neural net type will be created and run. (Default w/ 8 avalible core assumption). For this to work make sure you first do the following:
1. First unzip the NeuralNetOutput.zip in a directory.
2. Change all instances of  "/Users/zackeryleman/Desktop" in testAllNetworksQueue.java to be the file path of the NeuralNetOutput folder.

// NOTES: To run these networks we had to pass the argument " -Xmx3000M"  to the java virtual machine to increase heap memory allocation.
//When testing Captchas if you get a, cannot find file error, either add or remove the "src/" before all file paths in the CAPTCHA loading method.

