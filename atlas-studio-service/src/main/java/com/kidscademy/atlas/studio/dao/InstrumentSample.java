package com.kidscademy.atlas.studio.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="instrument")
public class InstrumentSample {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String sampleTitle;
    private String sampleName;
    private String waveformName;

    public String getSampleTitle() {
	return sampleTitle;
    }

    public void setSampleTitle(String sampleTitle) {
	this.sampleTitle = sampleTitle;
    }

    public String getSampleName() {
	return sampleName;
    }

    public void setSampleName(String sampleName) {
	this.sampleName = sampleName;
    }

    public String getWaveformName() {
	return waveformName;
    }

    public void setWaveformName(String waveformName) {
	this.waveformName = waveformName;
    }

    public int getId() {
	return id;
    }
}
