package com.kidscademy.atlas.studio.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * Any measurable or observable characteristic of an object. A feature has a unique name, a value - or a range of values, and a physical quantity that is used
 * to determine measurement unit.
 * <p>
 * For living things a more accurate term would be <em>trait</em> but <em>feature</em> is more general and applicable to not living objects too.
 * 
 * @author Iulian Rotaru
 */
@Entity
public class Feature
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  private FeatureMeta meta;

  private Date timestamp;

  /**
   * Mandatory numeric value. If optional {@link #maximum} value is provided this value should be interpreted as minimum. This may be the case if this feature
   * represent a range.
   */
  private double value;

  /**
   * Optional maximum value used when feature represent a range. If this maximum is provided {@link #value} should be interpreted as minimum.
   */
  private Double maximum;

  private String display;

  public Feature() {
  }

  public Feature(FeatureMeta meta, double value, Double... maximum) {
    this.meta = meta;
    this.value = value;
    if(maximum.length == 1) {
      this.maximum = maximum[0];
    }
    this.display = new FeatureValueFormat(this).display();
  }

  @PreUpdate
  @PrePersist
  public void preSave() {
    timestamp = new Date();
  }

  public int getId() {
    return id;
  }

  public FeatureMeta getMeta() {
    return meta;
  }

  public boolean isScalar() {
    return meta.getQuantity() == PhysicalQuantity.SCALAR;
  }

  public Date getTimestamp() {
    return timestamp;
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

  public Feature updateDisplay() {
    display = new FeatureValueFormat(this).display();
    return this;
  }

  public String getDisplay() {
    return display;
  }

  public String getName() {
    return meta.getName();
  }

  public PhysicalQuantity getQuantity() {
    return meta.getQuantity();
  }

  @Override
  public String toString() {
    return meta.getName();
  }
}
