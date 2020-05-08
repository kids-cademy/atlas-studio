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

public class QuantityFormat implements Comparator<Variant> {
    private final String value;
    private final String units;

    public QuantityFormat(double value, PhysicalQuantity physicalQuantity) {
	Unit[] units = quantityUnits(physicalQuantity);

	List<Variant> variants = new ArrayList<>();
	for (Unit unit : units) {
	    variants.add(new Variant(value, unit));
	}

	Collections.sort(variants, this);
	Variant variant = variants.get(0);

	String stringValue = variant.formattedValue;
	if (stringValue.length() > 10) {
	    stringValue = String.format("%.4E", variant.numericValue);
	}

	this.value = stringValue;
	this.units = variant.units;
    }

    public String value() {
	return value;
    }

    public String units() {
	return units;
    }

    @Override
    public int compare(Variant leftQuantity, Variant rightQuantity) {
	final DecimalNumeral left = new DecimalNumeral(leftQuantity.formattedValue);
	final DecimalNumeral right = new DecimalNumeral(rightQuantity.formattedValue);

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

    public static List<Option> getOptions(PhysicalQuantity quantity) {
	List<Option> options = new ArrayList<>();
	for (Unit unit : quantityUnits(quantity)) {
	    options.add(new Option(unit.symbol(), Double.toString(1.0 / unit.factor())));
	}
	return options;
    }

    // --------------------------------------------------------------------------------------------

    private static Unit[] quantityUnits(PhysicalQuantity quantity) {
	Unit[] units = UNITS.get(quantity);
	if (units == null) {
	    throw new BugError("Missing units for physical quantity |%s|.", quantity);
	}
	return units;
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
	UNITS.put(PhysicalQuantity.SCALAR, ScalarUnits.values());
	UNITS.put(PhysicalQuantity.MASS, MassUnits.values());
	UNITS.put(PhysicalQuantity.TIME, TimeUnits.values());
	UNITS.put(PhysicalQuantity.LENGTH, LengthUnits.values());
	UNITS.put(PhysicalQuantity.SPEED, SpeedUnits.values());
	UNITS.put(PhysicalQuantity.POWER, PowerUnits.values());
	UNITS.put(PhysicalQuantity.FOOD_ENERGY, FoodEnergyUnits.values());
	UNITS.put(PhysicalQuantity.DENSITY, DensityUnits.values());
	UNITS.put(PhysicalQuantity.ACCELERATION, AccelerationUnits.values());
	UNITS.put(PhysicalQuantity.ANGLE, AngleUnits.values());
	UNITS.put(PhysicalQuantity.AREA, AreaUnits.values());
	UNITS.put(PhysicalQuantity.POPULATION_DENSITY, PopulationDensityUnits.values());
	UNITS.put(PhysicalQuantity.CURRENCY, CurrencyUnits.values());
    }

    interface Unit {
	double factor();

	String symbol();
    }

    private enum ScalarUnits implements Unit {
	UNITARY;

	@Override
	public double factor() {
	    return 1;
	}

	@Override
	public String symbol() {
	    return null;
	}
    }

    private enum MassUnits implements Unit {
	MICROGRAM(1000000000, "micrograms"), MILLIGRAM(1000000, "milligrams"), GRAM(1000, "grams"), KILOGRAM(1,
		"kg"), TON(0.001, "tons");

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
	SECOND(1, "s"), MINUTE(1.0 / 60.0, "min"), HOUR(1.0 / 3600.0, "h"), DAY(1.0 / 86400.0,
		"days"), YEAR(1.0 / 31556952.0, "years");

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
	MILLIMETER(1000, "mm"), CENTIMETER(100, "cm"), METER(1, "m"), KILOMETER(1 / 1000.0,
		"km"), AU(1 / 149597870700.0, "AU");

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
	KILOMETER_PER_HOUR(3.6, "km/h"), KILOMETER_PER_SECOND(1 / 1000.0, "km/s");

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

    private enum PowerUnits implements Unit {
	WATT(1, "W"), KILOWATT(1 / 1000.0, "kW"), MEGAWATT(1 / 1000000.0, "MW"), HORSEPOWER(1 / 735.5, "hp");

	private double factor;
	private String symbol;

	private PowerUnits(double factor, String symbol) {
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

    private enum DensityUnits implements Unit {
	KILOGRAM_PER_CUBIC_METER(1, "kg/m3"), GRAM_PER_CUBIC_CENTIMETER(0.001, "g/cm3");

	private double factor;
	private String symbol;

	private DensityUnits(double factor, String symbol) {
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

    private enum AccelerationUnits implements Unit {
	METER_PER_SQUARE_SECOND(1, "m/s2");

	private double factor;
	private String symbol;

	private AccelerationUnits(double factor, String symbol) {
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

    private enum AngleUnits implements Unit {
	DEGREE(1, "°");

	private double factor;
	private String symbol;

	private AngleUnits(double factor, String symbol) {
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

    private enum AreaUnits implements Unit {
	SQUARE_METER(1, "m2"), SQUARE_KILOMETER(1 / 1000000.0, "km2");

	private double factor;
	private String symbol;

	private AreaUnits(double factor, String symbol) {
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

    private enum PopulationDensityUnits implements Unit {
	PER_SQUARE_METER(1, "per m2"), PER_SQUARE_KILOMETER(1000000.0, "per km2");

	private double factor;
	private String symbol;

	private PopulationDensityUnits(double factor, String symbol) {
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

    private enum CurrencyUnits implements Unit {
	USD(1, "USD"), EUR(1, "EUR"), JPY(1, "JPY"), GBP(1, "GBP");

	private double factor;
	private String symbol;

	private CurrencyUnits(double factor, String symbol) {
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

final class Variant {
    private static final NumberFormat numberFormat = NumberFormat.getNumberInstance();
    static {
	numberFormat.setMaximumFractionDigits(10);
	numberFormat.setGroupingUsed(true);
    }

    final double numericValue;
    final String formattedValue;
    final String units;

    Variant(double value, QuantityFormat.Unit unit) {
	this.numericValue = round(value * unit.factor(), 4);
	this.formattedValue = numberFormat.format(this.numericValue);
	this.units = unit.symbol();
    }

    private static double round(double value, int scale) {
	BigDecimal decimal = new BigDecimal(value).setScale(scale, RoundingMode.HALF_EVEN);
	return decimal.doubleValue();
    }
}
