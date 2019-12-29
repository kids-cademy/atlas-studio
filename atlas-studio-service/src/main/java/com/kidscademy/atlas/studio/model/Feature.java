package com.kidscademy.atlas.studio.model;

import javax.persistence.Embeddable;

/**
 * Any measurable or observable characteristic of an object. A feature has a
 * unique name, a value - or a range of values, and a physical quantity that is
 * used to determine measurement unit.
 * <p>
 * For living things a more accurate term would be <em>trait</em> but
 * <em>feature</em> is more general and applicable to not living objects too.
 * 
 * @author Iulian Rotaru
 */
@Embeddable
public class Feature {
    /**
     * Feature name is for internal use only; it is not meant to be displayed on
     * user interface.
     */
    private String name;

    /**
     * Mandatory numeric value. If optional {@link #maximum} value is provided this
     * value should be interpreted as minimum. This may be the case if this feature
     * represent a range.
     */
    private double value;

    /**
     * Optional maximum value used when feature represent a range. If this maximum
     * is provided {@link #value} should be interpreted as minimum.
     */
    private Double maximum;

    /**
     * Physical quantity is used to determine measurement unit. Features always use
     * non prefixed units from International System of Units, also known as metric.
     * Non prefixed units are one (1) references without any factor, e.g. meter not
     * kilometer or other multiple or submultiple.
     */
    private PhysicalQuantity quantity;

    public Feature() {

    }

    public Feature(String name, PhysicalQuantity quantity) {
	this.name = name;
	this.quantity = quantity;
    }

    public Feature(String name, double value, PhysicalQuantity quantity) {
	this.name = name;
	this.value = value;
	this.quantity = quantity;
    }

    public Feature(String name, double value, double maximum, PhysicalQuantity quantity) {
	this.name = name;
	this.value = value;
	this.maximum = maximum;
	this.quantity = quantity;
    }

    public String getName() {
	return name;
    }

    public double getValue() {
	return value;
    }

    public Double getMaximum() {
	return maximum;
    }

    public boolean hasMaximum() {
	return maximum != null;
    }

    public PhysicalQuantity getQuantity() {
	return quantity;
    }
}
