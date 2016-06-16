/*
 * OptFunction.java
 *
 * Author: Paul Andrews
 *
 * Last Modified: 12/10/2005
 */

package optainet;

/**
 * This class represents the optimisation problem that is to be solved by
 * opt-aiNet. It contains just one method that evaluates an array of real value
 * numbers that represents the dimension values of a network cell.
 */
public class OptFunction {

	/**
	 * Creates a new instance of OptFunction
	 */
	public OptFunction() {
	}

	/**
	 * Evaluates an array of real value numbers that represent the dimensions of
	 * a network cell, returning a single real number value that represents the
	 * fitness of the provided array values. This fitness is calculated by
	 * applying the array values to the fitness function: f(x,y) = x.sin(4x.PI)
	 * - y.sin(4y.PI+PI) + 1
	 * 
	 * @param dimensions
	 *            the dimension values of a network cell
	 * @return the evaluated fitness of the network cell dimension values
	 */
	public static double evaluateCell(double[] dimensions) {
		double fitness;

		fitness =
		// Testing Functions		
				
		 dimensions[1]* Math.sin(2*Math.PI*dimensions[1]) -
		 dimensions[0]*Math.sin(2*(Math.PI*dimensions[0]+Math.PI)) +
		 Math.sin(2*Math.PI);

		//dimensions[0] * Math.sin(4 * Math.PI * dimensions[0])
				//- dimensions[1] * Math.sin(4 * Math.PI * dimensions[1] + Math.PI) + 1;

		return fitness;
	}

}