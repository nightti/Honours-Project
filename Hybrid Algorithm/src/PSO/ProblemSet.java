package PSO;

// this is the problem to be solved
// to find an x and a y that minimize the function below:
// f(x, y) = (2.8125 - x + x * y^4)^2 + (2.25 - x + x * y^2)^2 + (1.5 - x + x*y)^2
// where 1 <= x <= 4, and -1 <= y <= 1

// you can modify the function depends on your needs
// if your problem space is greater than 2-dimensional space
// you need to introduce a new variable (other than x and y)

public class ProblemSet {
	public static double loc_x_low = -2;
	public static double loc_x_high = 2;
	public static double loc_y_low = -2;
	public static double loc_y_high = 2;

	public static void setBoundaries(double upper, double lower) {
		loc_x_low = lower;
		loc_y_low = lower;

		loc_x_high = upper;
		loc_y_high = upper;
		System.out.println(upper + "Boundaries");
		System.out.println(lower + "Boundaries");
	}

	public static final double VEL_LOW = -1;
	public static final double VEL_HIGH = 1;

	public static final double ERR_TOLERANCE = 1E-20; // the smaller the
														// tolerance, the more
														// accurate the result,
														// but the number of
														// iteration is
														// increased

	public static double evaluate(Location location) {
		double result = 0;
		double x = location.getLoc()[0]; // the "x" part of the location
		double y = location.getLoc()[1]; // the "y" part of the location
		// x = dimention 0 / y = dimention 1

		result =
		// Testing Functions
				
		 y* Math.sin(2*Math.PI*y) - x*Math.sin(2*(Math.PI*x+Math.PI)) +
		 Math.sin(2*Math.PI);

		//x * Math.sin(4 * Math.PI * x) - y * Math.sin(4 * Math.PI * y + Math.PI) + 1;

		return result;
	}
}