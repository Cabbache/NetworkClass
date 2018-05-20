import java.util.*;

public class NetworkMainExample{
	public static void main (String[] args){
		Scanner sc = new Scanner(System.in);
		
		/*
		 Create a Network instance with 3 inputs and one
		 output.
		*/

		Network nn = new Network(new int[] {3, 5, 3, 2});

		/*
		Add some data in the dataset
		first 3 elements are inputs, the last two are targets because
		the network is defined as new int[] {3,...,...,2}
		*/

		nn.AddEntry(new double[] {-1,1,-1,0.5,0.1});
		nn.AddEntry(new double[] {1,-1,0,1,0});
		nn.AddEntry(new double[] {0,1,0,0.2,0.8});
		nn.AddEntry(new double[] {0.2,-0.5,0.7,0.3,0.24});

		/*
		Train on the dataset 10000 times and print progress
		*/

		nn.iterate(200000, true);
		while (true){
			nn.PrintStructure(); // Look at the network visually
			nn.PrintDataSet(); //show contents of dataset

			double[] inputs = new double[3];
			for (int k = 0;k < 3;k++){
				System.out.print("input " + (k+1) + " >> ");
				inputs[k] = sc.nextDouble();
			}
			double[] output = nn.fwpass(inputs); /* Process the user input to get an output array.
			in this case we only have output[0]
			*/
			System.out.println("output >> ");
			for (double o : output){
				System.out.println(o + " ");// display contents of output[]	
			}
		}
	}
}
