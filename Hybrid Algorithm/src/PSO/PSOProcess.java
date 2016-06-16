package PSO;

import java.text.DecimalFormat;


// this is the heart of the PSO program
// the code is for 2-dimensional space problem
// but you can easily modify it to solve higher dimensional space problem

import java.util.Random;
import java.util.Vector;

public class PSOProcess implements PSOConstants {
	private Vector<Particle> swarm = new Vector<Particle>();
	private double[] pBest = new double[SWARM_SIZE];
	private Vector<Location> pBestLocation = new Vector<Location>();
	private double gBest;
	private Location gBestLocation;
	private double[] fitnessValueList = new double[SWARM_SIZE];
	double currentBest = 0;
	double previousBest = 0;
	int noImprovement = 0;
	Random generator = new Random();
	static double upperBoundary = 5.0;
	static double lowerBoundary = -5.0;
	double[] EvaluationsArray = new double[SWARM_SIZE];
	double[][] EvaluationsArrayCoords = new double[SWARM_SIZE][PROBLEM_DIMENSION];
	int evalArraySpace = 0;
	
	//important variables for performance/testing
	double errorThreshold = 0.02;
	int improvementThreshold = 3; //

	public static void setBoundary(double[] upperBounds, double[] lowerBounds) {
		upperBoundary = upperBounds[0];
		lowerBoundary = lowerBounds[0];

		ProblemSet.setBoundaries(upperBoundary, lowerBoundary);
	}

	public double[][] execute() {

		/*
		 * Evaluate by coordinates (for testing only) double[] testingArr = new
		 * double[2]; testingArr[0] = -4.589901019532682; testingArr[1] =
		 * -3.6800891004591487;
		 * 
		 * Location test = new Location(testingArr); DecimalFormat df = new
		 * DecimalFormat("#.000"); double value = ProblemSet.evaluate(test);
		 * System.out.println("TEST-PSO-- >"+df.format(value));
		 */
		initializeSwarm();
		updateFitnessList();

		for (int i = 0; i < SWARM_SIZE; i++) {
			pBest[i] = fitnessValueList[i];
			pBestLocation.add(swarm.get(i).getLocation());
		}

		int t = 0;
		double w;
		double err = 9999;

		while (t < MAX_ITERATION && noImprovement < improvementThreshold) {
			// step 1 - update pBest
			for (int i = 0; i < SWARM_SIZE; i++) {
				if (fitnessValueList[i] < pBest[i]) {
					pBest[i] = fitnessValueList[i];
					pBestLocation.set(i, swarm.get(i).getLocation());
				}
			}

			// step 2 - update gBest
			int bestParticleIndex = PSOUtility.getMinPos(fitnessValueList);
			if (t == 0 || fitnessValueList[bestParticleIndex] < gBest) {
				gBest = fitnessValueList[bestParticleIndex];
				gBestLocation = swarm.get(bestParticleIndex).getLocation();
			}

			w = W_UPPERBOUND - (((double) t) / MAX_ITERATION) * (W_UPPERBOUND - W_LOWERBOUND);

			for (int i = 0; i < SWARM_SIZE; i++) {
				double r1 = generator.nextDouble();
				double r2 = generator.nextDouble();

				Particle p = swarm.get(i);

				// step 3 - update velocity
				double[] newVel = new double[PROBLEM_DIMENSION];
				newVel[0] = (w * p.getVelocity().getPos()[0])
						+ (r1 * C1) * (pBestLocation.get(i).getLoc()[0] - p.getLocation().getLoc()[0])
						+ (r2 * C2) * (gBestLocation.getLoc()[0] - p.getLocation().getLoc()[0]);
				newVel[1] = (w * p.getVelocity().getPos()[1])
						+ (r1 * C1) * (pBestLocation.get(i).getLoc()[1] - p.getLocation().getLoc()[1])
						+ (r2 * C2) * (gBestLocation.getLoc()[1] - p.getLocation().getLoc()[1]);
				Velocity vel = new Velocity(newVel);
				p.setVelocity(vel);

				// step 4 - update location
				double[] newLoc = new double[PROBLEM_DIMENSION];
				newLoc[0] = p.getLocation().getLoc()[0] + newVel[0];
				newLoc[1] = p.getLocation().getLoc()[1] + newVel[1];

				// stop location go outside of the boundaries
				for (int j = 0; j < PROBLEM_DIMENSION; j++) {
					if (newLoc[j] > upperBoundary) {
						newLoc[j] = upperBoundary;
					}
					if (newLoc[j] < lowerBoundary) {
						newLoc[j] = lowerBoundary;
					}
				}
				Location loc = new Location(newLoc);
				// System.out.println(newLoc[0]+"<-----------X");
				// System.out.println(newLoc[1]+"<-----------Y");
				// New location add to evaluating function

				for (int k = 0; k < SWARM_SIZE; k++) {

					if (evalArraySpace < SWARM_SIZE) {
						if (ProblemSet.evaluate(loc) > EvaluationsArray[k] && EvaluationsArray[k] == 0.0) {
							EvaluationsArray[k] = ProblemSet.evaluate(loc);
							EvaluationsArrayCoords[k][0] = newLoc[0];
							EvaluationsArrayCoords[k][1] = newLoc[1];
							k = SWARM_SIZE; // prevent overriding lower values
							evalArraySpace++;
						}
					} else {
						if (ProblemSet.evaluate(loc) > EvaluationsArray[k]) {
							EvaluationsArray[k] = ProblemSet.evaluate(loc);
							EvaluationsArrayCoords[k][0] = newLoc[0];
							EvaluationsArrayCoords[k][1] = newLoc[1];
							k = SWARM_SIZE; // prevent overriding lower values
						}
					}

				}

				// print out the fitness of all particles at the current iteration
				//System.out.println(ProblemSet.evaluate(loc) + "<----values");
				p.setLocation(loc);
			}

			// calculating error margin
			previousBest = currentBest;
			// System.out.println(" Previous best - " + previousBest);

			currentBest = ProblemSet.evaluate(gBestLocation);
			// System.out.println(" Current best - " + currentBest);

			err = Math.abs(currentBest - previousBest);
			 //System.out.println(" err - " + err);

			if (err < errorThreshold) {
				noImprovement++;
				//System.out.println("---->" + noImprovement + " times error < ("+errorThreshold+")error threshold");
				if (noImprovement == improvementThreshold) {
					//System.out.println("	Cannot improve anymore");
					/*
					 * Print out the final best coordinates collected from PSO
					 */

				}
			}

			// print out data
			//System.out.println("PSO iteration " + t);
			// System.out.println(" Best X: " + gBestLocation.getLoc()[0]);
			// System.out.println(" Best Y: " + gBestLocation.getLoc()[1]);
			// System.out.println(" Value: " +
			// ProblemSet.evaluate(gBestLocation));

			t++;
			updateFitnessList();
		}
		for (int k = 0; k < EvaluationsArray.length; k++) {
			//System.out.println(EvaluationsArray[k] + " - best value");
		}
		// print out the evaluation array
		/*
		 * for(int k=0;k<SWARM_SIZE;k++){ System.out.println(k); for(int
		 * j=0;j<PROBLEM_DIMENSION;j++){
		 * System.out.println(EvaluationsArrayCoords[k][j]+" -best coordinate");
		 * }
		 * 
		 * }
		 */

		// print out final
		// System.out.println("Solution found at iteration " + (t - 1));
		// System.out.println(" Best X: " + gBestLocation.getLoc()[0]);
		// System.out.println(" Best Y: " + gBestLocation.getLoc()[1]);
		return EvaluationsArrayCoords;

	}

	public void initializeSwarm() {
		Particle p;
		for (int i = 0; i < SWARM_SIZE; i++) {
			p = new Particle();

			// randomize location inside a space defined in Problem Set
			double[] loc = new double[PROBLEM_DIMENSION];
			loc[0] = ProblemSet.loc_x_low + generator.nextDouble() * (ProblemSet.loc_x_high - ProblemSet.loc_x_low);
			// System.out.println("---------->x : " + loc[0]);
			loc[1] = ProblemSet.loc_y_low + generator.nextDouble() * (ProblemSet.loc_y_high - ProblemSet.loc_y_low);
			// System.out.println("---------->y : " + loc[1]);
			Location location = new Location(loc);

			// randomize velocity in the range defined in Problem Set
			double[] vel = new double[PROBLEM_DIMENSION];
			vel[0] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
			vel[1] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
			Velocity velocity = new Velocity(vel);

			p.setLocation(location);
			p.setVelocity(velocity);
			swarm.add(p);
		}
	}

	public void updateFitnessList() {
		for (int i = 0; i < SWARM_SIZE; i++) {
			fitnessValueList[i] = swarm.get(i).getFitnessValue();
		}
	}

}