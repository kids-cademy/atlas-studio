package com.kidscademy.atlas.studio.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

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
@Entity
public class Feature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private FeatureMeta meta;

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
     * Display is used on atlas studio user interface and is not strictly part of
     * domain model.
     */
    @Transient
    private String display;

    public Feature() {
    }

    /**
     * Test constructor.
     * 
     * @param meta
     * @param value
     * @param maximum
     */
    public Feature(FeatureMeta meta, double value, Double... maximum) {
	this.meta = meta;
	this.value = value;
	if (maximum.length == 1) {
	    this.maximum = maximum[0];
	}
    }

    public void postLoad() {
	display = new FeatureValueFormat(this).display();
    }

    public boolean isScalar() {
	return meta.getQuantity() == PhysicalQuantity.SCALAR;
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

    public String getName() {
	return meta.getName();
    }

    public String getDisplay() {
	return meta.getDisplay();
    }

    public PhysicalQuantity getQuantity() {
	return meta.getQuantity();
    }

    @Override
    public String toString() {
	return meta.getName();
    }
}
