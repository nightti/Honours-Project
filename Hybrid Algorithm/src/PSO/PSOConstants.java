package PSO;

// interface  for the PSO
//PSO swarm size no need > 30

public interface PSOConstants {

	int SWARM_SIZE = 50;
	int MAX_ITERATION = 100;
	int PROBLEM_DIMENSION = 2;
	double C1 = 2.0;
	double C2 = 2.0;
	double W_UPPERBOUND = 1.0;
	double W_LOWERBOUND = 0.0;
}