package com.kidscademy.atlas.studio.tool;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Embeddable;

@Embeddable
public class HDate implements Comparable<HDate> {
    private Long value;
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
    public HDate(long value, Format format) {
	this(value, format, Period.FULL, Era.CE);
    }

    // 1000 BCE : new HDate(1000, HDate.Unit.YEAR, HDate.Era.BCE)
    public HDate(long value, Format format, Era era) {
	this(value, format, Period.FULL, era);
    }

    // End of XVII-th Century CE : new HDate(17, HDate.Unit.CENTURY,
    // HDate.Period.END)
    // Middle of XIX-th Century CE : new HDate(19, HDate.Unit.CENTURY,
    // HDate.Period.MIDDLE)
    public HDate(long value, Format format, Period period) {
	this(value, format, period, Era.CE);
    }

    public HDate(long value, Format format, Period period, Era era) {
	this.value = value;
	this.mask = format.ordinal() + (period.ordinal() << 8) + (era.ordinal() << 16);
    }

    public Long getValue() {
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
	    calendar.setTime(new Date(value));
	    value = calendar.get(Calendar.YEAR) / 100L;
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

    public String toString() {
	switch (getFormat()) {
	case YEAR:
	    return String.format("%d %s", value, getEra());

	case CENTURY:
	    return String.format("%d %s", (value - 1) * 100, getEra());

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
		return ((Long) this.value).compareTo(other.value);
	    }
	    return 1;

	case BCE:
	    if (other.getEra() == Era.BCE) {
		return ((Long) other.value).compareTo(this.value);
	    }
	    return -1;
	}
	return 0;
    }

    public enum Format {
	DATE, YEAR, DECADE, CENTURY, MILLENNIA, KYA, MYA, BYA
    }

    public enum Period {
	FULL, BEGINNING, MIDDLE, END
    }

    public enum Era {
	CE, BCE
    }
}
