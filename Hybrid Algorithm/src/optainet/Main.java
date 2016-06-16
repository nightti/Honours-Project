/*
 * Main.java
 *
 * Author: Paul Andrews
 *
 * Last Modified: 20/10/2005
 */

package optainet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Main class of the optainet package. Checks that a configuration file has been
 * presented, reads that configuration file and executes the opt-aiNet algorithm
 * appropriately using an instance of the OptAinet class.
 */
public class Main {

	private int numCells; // Number of initial network cells
	private int numDimensions; // Number of optimisation problem dimensions
	private int numClones; // Number of clones to make of each network cell
	private int maxIter; // Maximum number of algorithm iterations
	private double suppThres; // Threshold value for network cell suppression
	private double errorThres; // Threshold value for average population error
								// during clonal selection
	private double divRatio; // Proportion of current population size to be
								// added for diversity
	private double mutnParam; // Affinity proportionate mutation parameter
	private double[] lowerBounds; // Lower bound on each optimisation problem
									// dimension
	private double[] upperBounds; // Upper bound on each optimisation problem
									// dimension

	/**
	 * Creates a new instance of Main
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public Main(String[] args) {

		// If the number of command line parameters is not 1 then issue warning
		// and
		// exit, else if values are read in correctly from the configuration
		// file
		// provided as a command line parameter, then create an instance of the
		// OptAinet class and run the optimisation once

		if (args.length != 1)
			System.out.println("Usage: please supply config file");
		else if (readConfig(args[0])) {
			OptAinet opt = new OptAinet(numCells, numClones, maxIter, suppThres, errorThres, divRatio, mutnParam,
					numDimensions, lowerBounds, upperBounds);
			opt.optimise();
		} else
			System.out.println("Exiting");
	}

	/**
	 * Reads the contents of the configuration file into appropriate variables
	 * 
	 * @param filename
	 *            the configuration file
	 * @return true if file configuration file read successfully, false if not
	 */
	private boolean readConfig(String filename) {
		File file = new File(filename);

		// Check that file exists

		if (!file.exists()) {
			System.out.println("Config file " + filename + " does not exist");
			return false;
		}

		// Read each line from the config file, setting the relevent variables

		try {
			BufferedReader in = new BufferedReader(new FileReader(file));

			numCells = Integer.parseInt(readNextLine(in));
			numClones = Integer.parseInt(readNextLine(in));
			maxIter = Integer.parseInt(readNextLine(in));
			suppThres = Double.parseDouble(readNextLine(in));
			errorThres = Double.parseDouble(readNextLine(in));
			divRatio = Double.parseDouble(readNextLine(in));
			mutnParam = Double.parseDouble(readNextLine(in));
			numDimensions = Integer.parseInt(readNextLine(in));

			lowerBounds = new double[numDimensions];
			upperBounds = new double[numDimensions];

			// Specific bounds for each dimension must be provided

			for (int i = 0; i < numDimensions; i++) {
				String s = readNextLine(in);

				if (s == null) {
					System.out.println("Upper and lower bounds for each dimension must be provided");
					return false;
				}

				// Split the input string in two using the "," character

				String[] bits = s.split(",");
				lowerBounds[i] = Double.parseDouble(bits[0]);
				upperBounds[i] = Double.parseDouble(bits[1]);
			}
		} catch (Exception e) {
			System.out.println("Error opening config file " + filename);
			return false;
		}

		// Return true as no problems have been encountered

		return true;
	}

	/**
	 * Reads a single line from the configuration file, ignoring empty lines and
	 * removing commenting characters
	 * 
	 * @param in
	 *            the buffered input stream for the config file
	 * @throws java.lang.Exception
	 *             a line cannot be read
	 * @return the String containing the contents of the line read
	 */
	private String readNextLine(BufferedReader in) throws Exception {
		String str = null;
		boolean proceed;

		// Read in lines from the input stream ignoring lines that are empty or
		// start with the "#" character

		do {
			proceed = false;

			str = in.readLine();
			str = str.trim();

			if (str.startsWith("#") || str.matches(""))
				proceed = true;

		} while (proceed);

		return str;
	}

	/**
	 * Creates an instance of Main
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		new Main(args);
	}

}
