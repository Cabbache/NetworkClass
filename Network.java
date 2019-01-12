/**
 * This is a neural network class.
 *
 * @author Cabbache
 * @version 20.12.17
 */

import java.util.*;
public class Network{
	
	private Random rnd = new Random();
	private double error, learningrate = 1, avge = 0;
	private double[][][] neuron;
	private double[] weights, target, output;
	private int[] aon;
	private int hw, biggest, length;
	private final int sample = 20;
	private double[][] dataset;
	private double[][] dsRange;

	//setup network structure, weights, sizes
	public Network(int[] aon){
		int hl = aon.length;
		this.aon = new int[hl];
		biggest = 0;
		for (int k = 0;k < hl;k++){
			this.aon[k] = aon[k];
			biggest = aon[k] > biggest ? aon[k]:biggest;
		}
		neuron = new double[hl][biggest][2];
		target = new double[aon[aon.length - 1]];
		hw = 0;
		for (int w = 0;w < aon.length - 1;w++)
			hw += (aon[w] * aon[w + 1]);
		int biases = 0;
		for (int bs = 1;bs < aon.length;bs++)
			biases += aon[bs];
		weights = new double[hw + biases];
		for (int i = 0;i < weights.length;i++)
			weights[i] = (rnd.nextDouble()*2)-1;
		length = aon[0]+aon[aon.length-1];
		clearDataSet();
	}

	//give inputs and get output
	public double[] test(double[] inputs){
		for (int i = 0;i < aon[0];i++)
			neuron[0][i][1] = map(dsRange[i][0], dsRange[i][1], -1, 1, inputs[i]);
		double[] think = ForwardPass();
		for (int i = 0;i < think.length;i++)
			think[i] = map(0, 1, dsRange[aon[0]+i][0], dsRange[aon[0]+i][1], think[i]);
		return think;
	}
	
	//add record in dataset
	public void AddEntry(double[] entry){
		double[][] newset = new double[dataset.length+1][length];
		for (int j = 0;j < dataset.length;j++){
			for (int i = 0;i < length;i++)
				newset[j][i] = dataset[j][i];
		}
		for (int k = 0;k < length;k++)
			newset[dataset.length][k] = entry[k];
		dataset = newset;
	}

	public void clearDataSet(){
		dataset = new double[0][aon[0] + aon[aon.length - 1]];
	}

	public void setDataSet(double[][] set){
		dataset = new double[set.length][set[0].length];
		for (int m = 0;m < set.length;m++){
			for (int n = 0;n < set[0].length;n++){
				dataset[m][n] = set[m][n];
			}
		}
	}

	private double map(double fmin, double fmax, double tmin, double tmax, double value){
		return tmin + ((value - fmin) * (tmax-tmin)/(fmax-fmin));
	}

	//make all inputs between -1 and 1, and all outputs between 0 and 1
	public double[][] mapDataSet(){
		if (dataset.length == 0)
			return null;
		dsRange = new double[dataset[0].length][2];
		for (int i = 0;i < dataset[0].length;i++){
			double min = dataset[0][i];
			double max = dataset[0][i];
			for (int y = 0;y < dataset.length;y++){
				double comp = dataset[y][i];
				min = comp < min ? comp:min;
				max = comp > max ? comp:max;
			}
			dsRange[i][0] = min;
			dsRange[i][1] = max;
		}
		double[][] mappedDs = new double[dataset.length][dataset[0].length];
		for (int x = 0;x < mappedDs.length;x++){
			for (int y = 0;y < mappedDs[0].length;y++){
				double min = y >= aon[0] ? 0.0:-1.0;
				mappedDs[x][y] = map(dsRange[y][0], dsRange[y][1], min, 1, dataset[x][y]);
			}
		}
		return mappedDs;
	}

	public void PrintDataSet(){
		for (int entry = 0;entry < dataset.length;entry++){
			for (int i = 0;i < length;i++){
				if (i == aon[0])
					System.out.print("Targets: ");
				System.out.print(dataset[entry][i] + " ");
			}
			System.out.println();
		}
	}

	//learn on the dataset
	public void iterate(int times, boolean calcErr){
		if (dataset.length == 0){
			System.out.println("No dataset");
			return;
		}
		double[][] mapped = mapDataSet();
		for (int i = 0;i < times;i++){
			int x = (int)(Math.random() * (mapped.length));
			for (int inp = 0;inp < aon[0];inp++)
				neuron[0][inp][1] = mapped[x][inp];
			for (int outp = 0;outp < aon[aon.length - 1];outp++)
				target[outp] = mapped[x][aon[0] + outp];
			ForwardPass();
			BackPropagation();
			if (!calcErr)
				continue;
			if (i % 100000 < sample){
				double thisavg = 0;
				for (int j = 0;j < aon[aon.length - 1];j++){
					thisavg += Math.abs(target[j] - output[j]);
				}
				thisavg /= aon[aon.length - 1];
				avge += thisavg;
			}
			if (i % 100000 == sample){
				avge /= sample;
				System.out.println("Avg err: " + avge);
				avge = 0;
			}
		}
	}
	
	public void PrintStructure(){
		for (int k = 0;k < aon.length;k++){
			int len = (biggest - aon[k]);
			for (int n = 0;n < len;n++)
				System.out.print(" ");
			for (int m = 0;m < aon[k];m++)
				System.out.print("O ");
			System.out.println();
		}
	}

	public void PrintWeights(){
		for (double x : weights)
			System.out.println(x);
	}

	//calculate the output given the inputs and the network
	private double[] ForwardPass(){
		for (int lay = 1;lay < aon.length;lay++){
			for (int i = 0;i < aon[lay];i++){
				double ttal = 0;
				for (int j = 0;j < aon[lay - 1];j++){
					int wn = 0;
					for (int id = 0;id < lay-1;id++)
						wn += aon[id] * aon[id + 1];
					wn += (j*aon[lay]) + i;
					ttal += neuron[lay - 1][j][1] * weights[wn];
				}
				int add = 0;
				for (int nub = 1;nub < lay;nub++)
					add += aon[nub];
				neuron[lay][i][0] = ttal+(1 * weights[hw + add + i]);
				neuron[lay][i][1] = SigmoidFunction(neuron[lay][i][0]);
			}
		}
		output = new double[aon[aon.length - 1]];
		for (int ou = 0; ou < output.length;ou++ )
			output[ou] = neuron[aon.length - 1][ou][1];
		return output;
	}

	private double SigmoidFunctiondev(double x){
		return (SigmoidFunction(x) * (1-SigmoidFunction(x)));
	}

	private double SigmoidFunction(double x){
		return 1/(1+Math.pow(Math.E, -1 * x));
	}

	//improve
	private void BackPropagation(){
		double[][] deltsums = new double[aon.length - 1][biggest];
		for (int i = 0;i < aon[aon.length - 1];i++)
			deltsums[0][i] = SigmoidFunctiondev(neuron[aon.length - 1][i][0]) * (target[i] - output[i]);
		for (int lay = 1;lay < (aon.length - 1);lay++){
			for (int j = 0;j < aon[aon.length - lay - 1];j++){
				double ttal = 0;
				for (int h = 0;h < aon[aon.length - lay];h++){
					int ws = 0;
					for (int l = 0;l < aon.length - lay - 1;l++)
						ws += aon[l] * aon[l+1];
					ws += j*aon[aon.length - lay] + h;
					ttal += deltsums[lay - 1][h] * weights[ws];
				}
				deltsums[lay][j] = ttal * SigmoidFunctiondev(neuron[aon.length - lay - 1][j][0]);
			}
		}
		for (int lay = 0;lay < aon.length - 1;lay++){
			for (int j = 0;j < aon[lay];j++){
				for (int h = 0;h < aon[lay + 1];h++){
					int wn = 0;
					for (int i = 0;i < lay;i++)
						wn += aon[i] * aon[i+1];
					wn += j*aon[lay + 1] + h;
					weights[wn] += (deltsums[deltsums.length - lay - 1][h] * neuron[lay][j][1]) * learningrate;
				}
			}
		}
		int wn = hw;
		for (int g = 1;g < aon.length;g++){
			for (int j = 0;j < aon[g];j++){
				weights[wn] += (deltsums[deltsums.length - g][j]/1) * learningrate;
				wn++;
			}
		}
	}
}
