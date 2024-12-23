public class PolynomialUtils {
    public static double calculate(String[] coefficients, double x) {
        double result = 0;
        for (String coeff : coefficients) {
            result = result * x + Double.parseDouble(coeff);
        }
        return result;
    }
}
