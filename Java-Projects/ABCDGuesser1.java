import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * This program calculates 4 numbers and inputs them into jagers formula to
 * Approximate mu
 *
 * @author Tyler McDowell
 *
 */
public final class ABCDGuesser1 {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private ABCDGuesser1() {
    }

    /**
     * user input is calculated using jagers formula then returns how close the
     * numbers are to mu, in percentages.
     *
     * IGNORE NOTE: FIXED
     *
     * NOTE: this program has different results from the example used in the
     * project description. This is because the code exits the loop as soon as
     * the result is within 1% instead of finding the least relative error. If
     * the error is changed to .0008 the same result in the example is achieved.
     */
    private static double jagersFormula(double mu, double w, double x,
            double y, double z, SimpleWriter out) {
        double wValue = 0;
        double xValue = 0;
        double yValue = 0;
        double zValue = 0;
        double result = 0;
        double calcPercent = 100;
        int wPos = 0;
        int xPos = 0;
        int yPos = 0;
        int zPos = 0;
        double error = .0001;
        double[] jagerFormulaNums = { -5, -4, -3, -2, -1, -1.0 / 2, -1.0 / 3,
                -1.0 / 4, 0, 1.0 / 4, 1.0 / 3, 1.0 / 2, 1, 2, 3, 4, 5 };
        /*
         * loop through jagers formula to calculate mu with a relative error of
         * less than 1%
         */
        while (calcPercent > error) {
            /*
             * this loop keeps increasing the relive error to help find the
             * lowest possible result
             */
            if (error < 1) {
                error = error * 10;
            }
            while (calcPercent > error && wPos < jagerFormulaNums.length) {
                wValue = Math.pow(w, jagerFormulaNums[wPos]);
                xPos = 0;
                while (calcPercent > error && xPos < jagerFormulaNums.length) {
                    xValue = Math.pow(x, jagerFormulaNums[xPos]);
                    yPos = 0;
                    while (calcPercent > error
                            && yPos < jagerFormulaNums.length) {
                        //System.out.println(jagerFormulaNums[yPos]);//debug
                        yValue = Math.pow(y, jagerFormulaNums[yPos]);
                        zPos = 0;
                        while (calcPercent > error
                                && zPos < jagerFormulaNums.length) {
                            zValue = Math.pow(z, jagerFormulaNums[zPos]);
                            /*
                             * calc goal percent, if not within 1% keep looping
                             */
                            result = (wValue * xValue * yValue * zValue);
                            calcPercent = Math.abs((mu - result) / mu);
                            zPos++;
                        }
                        yPos++;
                    }
                    xPos++;
                }
                wPos++;
            }
        }
        /*
         * display jager formula with position
         */
        wPos = wPos - 1;
        xPos = xPos - 1;
        yPos = yPos - 1;
        zPos = zPos - 1;
        out.println("w^" + jagerFormulaNums[wPos] + " x^"
                + jagerFormulaNums[xPos] + " y^" + jagerFormulaNums[yPos]
                        + " z^" + jagerFormulaNums[zPos]);
        return calcPercent;
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        double mu = 0;
        double w = 0;
        double x = 0;
        double y = 0;
        double z = 0;
        double returnedPercent = 0;
        /*
         * ask user for variable values to enter into the jagers formula
         */
        out.print("What value for mu?: ");
        mu = in.nextDouble();
        out.print("Enter a personal number for w: ");
        w = in.nextDouble();
        out.print("Enter a personal number for x: ");
        x = in.nextDouble();
        out.print("Enter a personal number for y: ");
        y = in.nextDouble();
        out.print("Enter a personal number for z: ");
        z = in.nextDouble();
        out.println();
        /*
         * send values to jagersFormula method
         */
        returnedPercent = jagersFormula(mu, w, x, y, z, out);
        /*
         * display relative error results
         */
        out.print("Your relative error was: ");
        out.print(returnedPercent * 100, 2, false);
        out.print("%");
        /*
         * Close input and output streams
         */
        in.close();
        out.close();
    }

}
