# NetworkClass
A neural network class written in java

### How to use ###
Create a Network instance >> **Network nn = new Network(int[] layers)** where layers is an array of integers that represent
the amount of neurons in each layer[x]. Then add data to the 2d dataset array by using the method **AddEntry(double[] entry)** which is not very efficient or with **setDataSet(double[][] dataset)** where one **double[] entry** is an array composed of an array of inputs followed by an array of targets/outputs. **double[][] dataset** is an array of **double[] entry**. Inputs are ideally between -1 and 1. However, the targets/outputs cannot be outside the range of between 0 and 1.

After that, you can train the network by using **iterate(int times, boolean show_error)** where **int times** is how many times it will loop through the dataset and **boolean show_error** is true if you want the network to print the error while learning.

You can test the network by using **fwpass(double[] inputs)** where **double[] inputs** is an array of inputs. This will return
a double[] array of outputs. You can clear the dataset with **clearDataSet()**

There are other methods that may help you see what's happening

* PrintDataSet()
* PrintStructure()
* PrintWeights()
