package com.kidscademy.atlas.studio.tool;

public class VolumeInfo implements ResultParser {
    private static final String LABEL_AVERAGE = "mean_volume:";
    private static final String LABEL_PEAK = "max_volume:";

    private double average;
    private double peak;

    public VolumeInfo() {
    }

    /**
     * Test constructor.
     * 
     * @param average
     * @param peak
     */
    public VolumeInfo(double average, double peak) {
	this.average = average;
	this.peak = peak;
    }

    @Override
    public void parse(String line) {
	if (parseAverage(line)) {
	    return;
	}
	if (parsePeak(line)) {
	    return;
	}
    }

    public double getAverage() {
	return average;
    }

    public double getPeak() {
	return peak;
    }

    private boolean parseAverage(String line) {
	int index = line.indexOf(LABEL_AVERAGE);
	if (index == -1) {
	    return false;
	}
	average = parseDouble(line.substring(index + LABEL_AVERAGE.length()));
	return true;
    }

    private boolean parsePeak(String line) {
	int index = line.indexOf(LABEL_PEAK);
	if (index == -1) {
	    return false;
	}
	peak = parseDouble(line.substring(index + LABEL_PEAK.length()));
	return true;
    }

    private static double parseDouble(String value) {
	int unitsIndex = value.indexOf("dB");
	return Double.parseDouble(value.substring(0, unitsIndex));
    }
}
