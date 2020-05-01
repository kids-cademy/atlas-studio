package com.kidscademy.atlas.studio.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.kidscademy.atlas.studio.model.PhysicalQuantity;
import com.kidscademy.atlas.studio.model.QuantityFormat;

import js.util.Strings;

public class QuantityFormatTest {
    @Test
    public void mass_integer() {
	assertThat(mass(0.001), equalTo("1 grams"));
	assertThat(mass(0.01), equalTo("10 grams"));
	assertThat(mass(0.1), equalTo("100 grams"));
	assertThat(mass(1), equalTo("1 kg"));
	assertThat(mass(10), equalTo("10 kg"));
	assertThat(mass(100), equalTo("100 kg"));
	assertThat(mass(1000), equalTo("1 tons"));
	assertThat(mass(10000), equalTo("10 tons"));
	assertThat(mass(100000), equalTo("100 tons"));
    }

    @Test
    public void mass_decimal() {
	assertThat(mass(0.00123), equalTo("1.23 grams"));
	assertThat(mass(0.0123), equalTo("12.3 grams"));
	assertThat(mass(0.123), equalTo("123 grams"));
	assertThat(mass(1.23), equalTo("1.23 kg"));
	assertThat(mass(12.3), equalTo("12.3 kg"));
	assertThat(mass(123.0), equalTo("123 kg"));
	assertThat(mass(1230), equalTo("1.23 tons"));
	assertThat(mass(12300), equalTo("12.3 tons"));
	assertThat(mass(123000), equalTo("123 tons"));
    }

    @Test
    public void mass_large() {
	assertThat(mass(1251000000000000000000.0), equalTo("1.2510E+18 tons"));
    }

    private static String mass(double value) {
	QuantityFormat qf = new QuantityFormat(value, PhysicalQuantity.MASS);
	return Strings.concat(qf.value(), ' ', qf.units());
    }

    @Test
    public void time_integer() {
	assertThat(time(1), equalTo("1 s"));
	assertThat(time(10), equalTo("10 s"));
	assertThat(time(100), equalTo("100 s"));
	assertThat(time(1000), equalTo("1,000 s"));
	assertThat(time(10000), equalTo("2.7778 h"));
	assertThat(time(10000000), equalTo("115.7407 days"));
	assertThat(time(100000000), equalTo("3.1689 years"));
	assertThat(time(1000000000), equalTo("31.6887 years"));
	assertThat(time(10000000000.0), equalTo("316.8874 years"));
    }

    @Test
    public void time_decimal() {
	assertThat(time(1.23), equalTo("1.23 s"));
	assertThat(time(12.3), equalTo("12.3 s"));
	assertThat(time(123.0), equalTo("2.05 min"));
	assertThat(time(1230), equalTo("20.5 min"));
	assertThat(time(12300), equalTo("205 min"));
	assertThat(time(123000), equalTo("1.4236 days"));
	assertThat(time(1230000), equalTo("14.2361 days"));
	assertThat(time(12300000), equalTo("142.3611 days"));
	assertThat(time(123000000), equalTo("3.8977 years"));
	assertThat(time(1230000000), equalTo("38.9771 years"));
	assertThat(time(12300000000.0), equalTo("389.7715 years"));
    }

    private static String time(double value) {
	QuantityFormat qf = new QuantityFormat(value, PhysicalQuantity.TIME);
	return Strings.concat(qf.value(), ' ', qf.units());
    }

    @Test
    public void length_integer() {
	assertThat(length(0.001), equalTo("1 mm"));
	assertThat(length(0.01), equalTo("1 cm"));
	assertThat(length(0.1), equalTo("10 cm"));
	assertThat(length(1), equalTo("1 m"));
	assertThat(length(10), equalTo("10 m"));
	assertThat(length(100), equalTo("100 m"));
	assertThat(length(1000), equalTo("1 km"));
	assertThat(length(10000), equalTo("10 km"));
	assertThat(length(100000), equalTo("100 km"));
	assertThat(length(2875271074854.0), equalTo("19.22 AU"));
    }

    @Test
    public void length_decimal() {
	assertThat(length(0.00123), equalTo("1.23 mm"));
	assertThat(length(0.0123), equalTo("1.23 cm"));
	assertThat(length(0.123), equalTo("12.3 cm"));
	assertThat(length(1.23), equalTo("1.23 m"));
	assertThat(length(12.3), equalTo("12.3 m"));
	assertThat(length(123.0), equalTo("123 m"));
	assertThat(length(1230), equalTo("1.23 km"));
    }

    private static String length(double value) {
	QuantityFormat qf = new QuantityFormat(value, PhysicalQuantity.LENGTH);
	return Strings.concat(qf.value(), ' ', qf.units());
    }

    @Test
    public void speed_integer() {
	assertThat(speed(0.1), equalTo("0.36 km/h"));
	assertThat(speed(1), equalTo("3.6 km/h"));
	assertThat(speed(10), equalTo("36 km/h"));
	assertThat(speed(100), equalTo("360 km/h"));
	assertThat(speed(1000), equalTo("1 km/s"));
	assertThat(speed(10000), equalTo("10 km/s"));
	assertThat(speed(100000), equalTo("100 km/s"));
	assertThat(speed(1000000), equalTo("1,000 km/s"));
	assertThat(speed(10000000), equalTo("10,000 km/s"));
    }

    @Test
    public void speed_decimal() {
	assertThat(speed(0.123), equalTo("0.4428 km/h"));
	assertThat(speed(1.23), equalTo("4.428 km/h"));
	assertThat(speed(12.3), equalTo("44.28 km/h"));
	assertThat(speed(123.0), equalTo("442.8 km/h"));
	assertThat(speed(1230), equalTo("1.23 km/s"));
    }
    
    private static String speed(double value) {
	QuantityFormat qf = new QuantityFormat(value, PhysicalQuantity.SPEED);
	return Strings.concat(qf.value(), ' ', qf.units());
    }
}
