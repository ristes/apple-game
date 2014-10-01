package utils;

public class GameUtils {
	
	
	/**
	 * mathod that generates random number with biasing
	 * if low = 0.0, high = 1.0 and bias = 1.0 the average is 0.5
	 * if low = 0.0, high = 1.0 and bias = 2.0 the average is 0.25
	 * if low = 0.0, high = 1.0 and bias = 0.5 the average is 0.75
	 * @param low
	 * @param high
	 * @param bias
	 * @return
	 */
	public static Double random(Double low, Double high, Double bias) {
		Double r= Math.random();
		r = Math.pow(r, bias);
		return low + (high-low)*r;
	}

}
