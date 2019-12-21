package com.kidscademy.atlas.studio.util;

import js.lang.BugError;

public class Units {
    // --------------------------------------------------------------------------------------------
    // MASS

    public static double toKilograms(String value, String units) {
	switch (units) {
	case "ounce":
	case "ounces":
	    return ouncesToKilograms(value);

	case "pound":
	case "pounds":
	    return poundsToKilograms(value);
	}
	throw new BugError("Unrecognized mass units |%s|.", units);
    }

    public static double ouncesToKilograms(double ounces) {
	return ounces / 35.274;
    }

    public static double ouncesToKilograms(String ounces) {
	return ouncesToKilograms(Double.parseDouble(ounces));
    }

    public static double poundsToKilograms(double pounds) {
	return pounds * 0.453592;
    }

    public static double poundsToKilograms(String pounds) {
	return poundsToKilograms(Double.parseDouble(pounds));
    }

    // --------------------------------------------------------------------------------------------
    // TIME

    public static double yearsToSeconds(double years) {
	return years * 31556952;
    }

    public static double yearsToSeconds(String years) {
	return yearsToSeconds(Double.parseDouble(years));
    }

    // --------------------------------------------------------------------------------------------
    // DISTANCE

    public static double toMeters(String value, String units) {
	switch (units.toLowerCase()) {
	case "foot":
	case "feet":
	    return feetToMeters(value);

	case "inch":
	case "inches":
	    return inchesToMeters(value);
	}
	throw new BugError("Unrecognized distance units |%s|.", units);
    }

    public static double feetToMeters(double feet) {
	return feet / 3.28084;
    }

    public static double feetToMeters(String feet) {
	return feetToMeters(Double.parseDouble(feet));
    }

    public static double inchesToMeters(double inches) {
	return inches * 0.0254;
    }

    public static double inchesToMeters(String inches) {
	return inchesToMeters(Double.parseDouble(inches));
    }
}
