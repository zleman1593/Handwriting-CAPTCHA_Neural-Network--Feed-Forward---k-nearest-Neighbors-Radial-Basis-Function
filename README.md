Handwriting-Neural-Net--Feed-Forward---k-nearest-Neighbors-
===========================================================

Hand Writing Recognition and Simple CAPTCHA Neural Networks:
Feed-Forward Backpropagation network & K-nearest Neighbor network

  CS 3425 Final project
  Spring 2014
  Min "Ivy" Xing, Zackery Leman
  
  This network works by reading in an image (image data provided by MNIST) and then selecting the number 0-9 that corresponds to the output node with greatest activation.
  The first network is a a feed-forward neural network that uses back propagation and the second is a  K-nearest Neighbor network.
 

// NOTES: To run this we had to pass the argument " -Xmx800M"  to the java virtual machine to increase heap memory allocation.
// NOTES: For the feed-forward neural network we got 89 percent accuracy when we set epochs to 30 and hidden nodes to 15. However, it took 30 minutes to run.
