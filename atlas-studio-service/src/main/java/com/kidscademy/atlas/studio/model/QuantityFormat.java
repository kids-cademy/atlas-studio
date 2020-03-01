package com.kidscademy.atlas.studio.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import js.lang.BugError;
import js.lang.Pair;

public class QuantityFormat implements Comparator<Pair> {
    private final String value;
    private final String units;

    public QuantityFormat(double value, PhysicalQuantity physicalQuantity) {
	Unit[] units = UNITS.get(physicalQuantity);
	if (units == null) {
	    throw new BugError("Missing units for physical quantity |%s|.", physicalQuantity);
	}

	NumberFormat numberFormat = NumberFormat.getNumberInstance();
	numberFormat.setMaximumFractionDigits(10);
	numberFormat.setGroupingUsed(false);

	List<Pair> displayVariants = new ArrayList<>();
	for (Unit unit : units) {
	    displayVariants.add(new Pair(numberFormat.format(round(value * unit.factor(), 2)), unit.symbol()));
	}

	Collections.sort(displayVariants, this);
	this.value = displayVariants.get(0).first();
	this.units = displayVariants.get(0).second();
    }

    public String value() {
	return value;
    }

    public String units() {
	return units;
    }

    @Override
    public int compare(Pair leftQuantity, Pair rightQuantity) {
	final DecimalNumeral left = new DecimalNumeral(leftQuantity.first());
	final DecimalNumeral right = new DecimalNumeral(rightQuantity.first());

	if (left.isZero()) {
	    return 1;
	}
	if (right.isZero()) {
	    return -1;
	}

	if (left.isSubunit()) {
	    return 1;
	}
	if (right.isSubunit()) {
	    return -1;
	}

	if (left.length() < right.length()) {
	    return -1;
	}
	if (left.length() > right.length()) {
	    return 1;
	}

	if (left.weight() > right.weight()) {
	    return 1;
	}
	if (left.weight() < right.weight()) {
	    return -1;
	}

	if (left.integerPartSize() < right.integerPartSize()) {
	    return -1;
	}
	if (left.integerPartSize() > right.integerPartSize()) {
	    return 1;
	}

	return 0;
    }

    // --------------------------------------------------------------------------------------------

    private static double round(double value, int scale) {
	BigDecimal decimal = new BigDecimal(value).setScale(scale, RoundingMode.HALF_EVEN);
	return decimal.doubleValue();
    }

    private static class DecimalNumeral {
	private final int length;
	private final String integerPart;
	private final String fractionalPart;

	private DecimalNumeral(String value) {
	    int decimalSeparator = value.indexOf('.');
	    if (decimalSeparator != -1) {
		this.integerPart = value.substring(0, decimalSeparator);
		this.fractionalPart = value.substring(decimalSeparator + 1);
	    } else {
		this.integerPart = value;
		this.fractionalPart = "";
	    }

	    this.length = this.integerPart.length() + this.fractionalPart.length();
	}

	public int length() {
	    return length;
	}

	public boolean isSubunit() {
	    return integerPart.equals("0");
	}

	public boolean isZero() {
	    return integerPart.equals("0") && fractionalPart.length() == 0;
	}

	public int integerPartSize() {
	    return integerPart.length();
	}

	public int weight() {
	    return Math.abs(integerPart.length() - fractionalPart.length());
	}
    }

    private static final Map<PhysicalQuantity, Unit[]> UNITS = new HashMap<>();
    static {
	UNITS.put(PhysicalQuantity.MASS, MassUnits.values());
	UNITS.put(PhysicalQuantity.TIME, TimeUnits.values());
	UNITS.put(PhysicalQuantity.LENGTH, LengthUnits.values());
	UNITS.put(PhysicalQuantity.SPEED, SpeedUnits.values());
	UNITS.put(PhysicalQuantity.FOOD_ENERGY, FoodEnergyUnits.values());
    }

    private interface Unit {
	double factor();

	String symbol();
    }

    private enum MassUnits implements Unit {
	GRAM(1000, "grams"), KILOGRAM(1, "kg"), TON(0.001, "tons");

	private double factor;
	private String symbol;

	private MassUnits(double factor, String symbol) {
	    this.factor = factor;
	    this.symbol = symbol;
	}

	@Override
	public double factor() {
	    return factor;
	}

	@Override
	public String symbol() {
	    return symbol;
	}
    }

    private enum TimeUnits implements Unit {
	SECOND(1, "s"), MINUTE(1.0 / 60.0, "min"), HOUR(1.0 / 3600.0, "h"), YEAR(1.0 / 31556952.0, "years");

	private double factor;
	private String symbol;

	private TimeUnits(double factor, String symbol) {
	    this.factor = factor;
	    this.symbol = symbol;
	}

	@Override
	public double factor() {
	    return factor;
	}

	@Override
	public String symbol() {
	    return symbol;
	}
    }

    private enum LengthUnits implements Unit {
	MILLIMETRE(1000, "mm"), CENTIMETRE(100, "cm"), METRE(1, "m"), KILOMETRE(0.001, "km");

	private double factor;
	private String symbol;

	private LengthUnits(double factor, String symbol) {
	    this.factor = factor;
	    this.symbol = symbol;
	}

	@Override
	public double factor() {
	    return factor;
	}

	@Override
	public String symbol() {
	    return symbol;
	}
    }

    private enum SpeedUnits implements Unit {
	KILOMETRE_PER_HOUR(3.6, "km/h");

	private double factor;
	private String symbol;

	private SpeedUnits(double factor, String symbol) {
	    this.factor = factor;
	    this.symbol = symbol;
	}

	@Override
	public double factor() {
	    return factor;
	}

	@Override
	public String symbol() {
	    return symbol;
	}
    }

    private enum FoodEnergyUnits implements Unit {
	CALORIE(1, "calories");

	private double factor;
	private String symbol;

	private FoodEnergyUnits(double factor, String symbol) {
	    this.factor = factor;
	    this.symbol = symbol;
	}

	@Override
	public double factor() {
	    return factor;
	}

	@Override
	public String symbol() {
	    return symbol;
	}
    }
}
