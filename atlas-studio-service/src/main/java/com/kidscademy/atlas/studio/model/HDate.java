package com.kidscademy.atlas.studio.model;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Embeddable;

import js.lang.Displayable;

@Embeddable
public class HDate implements Comparable<HDate>, Displayable {
    private Double value;
    private Integer mask;

    public HDate() {
    }

    public HDate(java.util.Date date) {
	this(date.getTime());
    }

    public HDate(java.sql.Date date) {
	this(date.getTime());
    }

    public HDate(java.sql.Time time) {
	this(time.getTime());
    }

    public HDate(long value) {
	this(value, Format.DATE, Period.FULL, Era.CE);
    }

    // 1822 CE : new HDate(1822, HDate.Unit.YEAR)
    // XIV-th Century CE : new HDate(14, HDate.Unit.CENTURY)
    // 93.5 million years ago : new HDate(93.5, HDate.Unit.MYA)
    public HDate(double value, Format format) {
	this(value, format, Period.FULL, Era.CE);
    }

    // 1000 BCE : new HDate(1000, HDate.Unit.YEAR, HDate.Era.BCE)
    public HDate(double value, Format format, Era era) {
	this(value, format, Period.FULL, era);
    }

    // End of XVII-th Century CE : new HDate(17, HDate.Unit.CENTURY,
    // HDate.Period.END)
    // Middle of XIX-th Century CE : new HDate(19, HDate.Unit.CENTURY,
    // HDate.Period.MIDDLE)
    public HDate(double value, Format format, Period period) {
	this(value, format, period, Era.CE);
    }

    public HDate(double value, Format format, Period period, Era era) {
	this.value = value;
	this.mask = format.ordinal() + (period.ordinal() << 8) + (era.ordinal() << 16);
    }

    public Double getValue() {
	return value;
    }

    public Format getFormat() {
	if (mask == null) {
	    return null;
	}
	return Format.values()[mask & 0x000000FF];
    }

    public Period getPeriod() {
	if (mask == null) {
	    return null;
	}
	return Period.values()[(mask & 0x0000FF00) >> 8];
    }

    public Era getEra() {
	if (mask == null) {
	    return null;
	}
	return Era.values()[(mask & 0x00FF0000) >> 16];
    }

    public HDate roundToCenturies() {
	switch (getFormat()) {
	case CENTURY:
	    break;

	case DECADE:
	    value = value / 10 + 1;
	    break;

	case YEAR:
	    value = value / 100 + 1;
	    break;

	case DATE:
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(new Date(value.longValue()));
	    value = (double) (calendar.get(Calendar.YEAR) / 100L);
	    break;

	default:
	    break;
	}

	format(Format.CENTURY);
	period(Period.FULL);
	return this;
    }

    private void format(Format format) {
	mask |= (format.ordinal() & 0x000000FF);
    }

    private void period(Period period) {
	mask |= (period.ordinal() & 0x0000FF00);
    }

    @Override
    public String toDisplay() {
	return toString();
    }

    public String toString() {
	switch (getFormat()) {
	case YEAR:
	    return String.format("%d %s", value, getEra());

	case DECADE:
	    return String.format("%d0s %s", value, getEra());

	case CENTURY:
	    switch (getPeriod()) {
	    case BEGINNING:
		return String.format("Beginning of %d-th Century, %s", value, getEra());

	    case MIDDLE:
		return String.format("Middle of %d-th Century, %s", value, getEra());

	    case END:
		return String.format("End of %d-th Century, %s", value, getEra());

	    default:
		return String.format("%d-th Century, %s", value, getEra());
	    }

	case KYA:
	    return String.format("%.1f kilo years ago", value);

	case MYA:
	    return String.format("%.1f million years ago", value);

	case BYA:
	    return String.format("%.1f billion years ago", value);

	default:
	    break;
	}

	return "";
    }

    @Override
    public int compareTo(HDate other) {
	switch (getEra()) {
	case CE:
	    if (other.getEra() == Era.CE) {
		return ((Double) this.value).compareTo(other.value);
	    }
	    return 1;

	case BCE:
	    if (other.getEra() == Era.BCE) {
		return ((Double) other.value).compareTo(this.value);
	    }
	    return -1;

	case NA:
	    if (other.getEra() == Era.NA) {
		return ((Double) this.value).compareTo(other.value);
	    }
	    return 1;
	}
	return 0;
    }

    public enum Format {
	DATE, YEAR, DECADE, CENTURY, MILLENNIA, KYA, MYA, BYA;

	private static Map<String, HDate.Format> FORMATS = new HashMap<>();
	static {
	    FORMATS.put("ka", HDate.Format.KYA);
	    FORMATS.put("ma", HDate.Format.MYA);
	    FORMATS.put("ga", HDate.Format.BYA);
	}

	public static Format yearsAgo(String value) {
	    return FORMATS.get(value);
	}
    }

    public enum Period {
	FULL, BEGINNING, MIDDLE, END, NA
    }

    public enum Era {
	CE, BCE, NA
    }
}
