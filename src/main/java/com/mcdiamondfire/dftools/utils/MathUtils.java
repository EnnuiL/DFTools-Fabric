package com.mcdiamondfire.dftools.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtils {
	public static double clamp(double number, double min, double max) {
		
		// If min is larger than max, swap the values.
		if (min > max) {
			double tempMin = min;
			min = max;
			max = tempMin;
		}
		
		if (number < min)
			number = min;
		
		if (number > max)
			number = max;
		
		return number;
	}

	public static double round(double number, int places) {
		if (places < 0) throw new IllegalArgumentException();
	
		BigDecimal bd = BigDecimal.valueOf(number);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static double roundAndClamp(double number, double min, double max, int places) {
		double clampledNumber = MathUtils.clamp(number, min, max);
		return MathUtils.round(clampledNumber, places);
	}
}