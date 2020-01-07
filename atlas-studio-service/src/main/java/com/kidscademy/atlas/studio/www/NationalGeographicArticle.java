package com.kidscademy.atlas.studio.www;

import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kidscademy.atlas.studio.util.Units;

import js.dom.Document;
import js.dom.DocumentBuilder;
import js.dom.Element;
import js.json.Json;
import js.lang.BugError;
import js.util.Classes;
import js.util.Strings;

public class NationalGeographicArticle {
    private Double minimumLifespan;
    private Double maximumLifespan;
    private Double minimumLength;
    private Double maximumLength;
    private Double minimumHeight;
    private Double maximumHeight;
    private Double minimumWingspan;
    private Double maximumWingspan;
    private Double minimumBillLength;
    private Double maximumBillLength;
    private Double minimumWeight;
    private Double maximumWeight;

    public NationalGeographicArticle(URL articleURL) {
	DocumentBuilder builder = Classes.loadService(DocumentBuilder.class);
	Document page = builder.loadHTML(articleURL);

	Json json = Classes.loadService(Json.class);
	Element factsScript = page.getByXPath("//DIV[@data-pestle-module='FastFacts']/SCRIPT");
	if (factsScript == null) {
	    throw new BugError("Missing fast facts script.");
	}
	Facts facts = json.parse(factsScript.getText(), Facts.class);
	System.out.println(facts.populationTrend);

	Pattern pattern = null;
	Matcher matcher = null;
	for (Fact fact : facts.facts) {
	    System.out.println(fact);

	    switch (fact.key) {
	    case "Common Name":
		break;

	    case "Scientific Name":
		break;

	    case "Type":
		break;

	    case "Diet":
		break;

	    case "Average life span in The Wild":
		pattern = Pattern.compile("^([\\d\\.]+)(?: or more)? years$");
		matcher = pattern.matcher(fact.value);
		if (matcher.find()) {
		    minimumLifespan = round(Units.yearsToSeconds(matcher.group(1)));
		    maximumLifespan = 0.0;
		    break;
		}

		pattern = Pattern.compile("^Up to ([\\d\\.]+) years$");
		matcher = pattern.matcher(fact.value);
		if (matcher.find()) {
		    minimumLifespan = 0.0;
		    maximumLifespan = round(Units.yearsToSeconds(matcher.group(1)));
		    break;
		}

		pattern = Pattern.compile("^([\\d\\.]+) to ([\\d\\.]+) years$");
		matcher = pattern.matcher(fact.value);
		if (matcher.find()) {
		    minimumLifespan = round(Units.yearsToSeconds(matcher.group(1)));
		    maximumLifespan = round(Units.yearsToSeconds(matcher.group(2)));
		    break;
		}

		throw new BugError("Invalid lifespan pattern |%s|.", fact.value);

	    case "Size":
		pattern = Pattern.compile(
			"^(?:Body\\: )?([\\d\\.]+) to ([\\d\\.]+) (inches|feet); wingspan: ([\\d\\.]+) to ([\\d\\.]+) (inches|feet)$");
		matcher = pattern.matcher(fact.value);
		if (matcher.find()) {
		    minimumLength = round(Units.toMeters(matcher.group(1), matcher.group(3)));
		    maximumLength = round(Units.toMeters(matcher.group(2), matcher.group(3)));
		    minimumWingspan = round(Units.toMeters(matcher.group(4), matcher.group(6)));
		    maximumWingspan = round(Units.toMeters(matcher.group(5), matcher.group(6)));
		    break;
		}

		pattern = Pattern.compile(
			"^(?:Body\\: )?([\\d\\.]+) to ([\\d\\.]+) (inches|feet); wingspan: ([\\d\\.]+) (inches|feet)$");
		matcher = pattern.matcher(fact.value);
		if (matcher.find()) {
		    minimumLength = round(Units.toMeters(matcher.group(1), matcher.group(3)));
		    maximumLength = round(Units.toMeters(matcher.group(2), matcher.group(3)));
		    minimumWingspan = round(Units.toMeters(matcher.group(4), matcher.group(5)));
		    maximumWingspan = 0.0;
		    break;
		}

		pattern = Pattern
			.compile("^(?:Body\\: )?([\\d\\.]+) (inches|feet); wingspan: up to ([\\d\\.]+) (inches|feet)$");
		matcher = pattern.matcher(fact.value);
		if (matcher.find()) {
		    minimumLength = round(Units.toMeters(matcher.group(1), matcher.group(2)));
		    maximumLength = 0.0;
		    minimumWingspan = 0.0;
		    maximumWingspan = round(Units.toMeters(matcher.group(3), matcher.group(4)));
		    break;
		}

		pattern = Pattern.compile("^([\\d\\.]+) to ([\\d\\.]+) (inches|feet)$");
		matcher = pattern.matcher(fact.value);
		if (matcher.find()) {
		    minimumLength = round(Units.toMeters(matcher.group(1), matcher.group(3)));
		    maximumLength = round(Units.toMeters(matcher.group(2), matcher.group(3)));
		    break;
		}

		pattern = Pattern.compile("^([\\d\\.]+) to ([\\d\\.]+) (inches|feet) high$");
		matcher = pattern.matcher(fact.value);
		if (matcher.find()) {
		    minimumHeight = round(Units.toMeters(matcher.group(1), matcher.group(3)));
		    maximumHeight = round(Units.toMeters(matcher.group(2), matcher.group(3)));
		    break;
		}

		pattern = Pattern.compile("^(?:Length\\: )?([\\d\\.]+) (inches|feet)$");
		matcher = pattern.matcher(fact.value);
		if (matcher.find()) {
		    minimumLength = round(Units.toMeters(matcher.group(1), matcher.group(2)));
		    maximumLength = 0.0;
		    break;
		}

		// Body: 25 inches; bill: 7.5 inches
		pattern = Pattern.compile("^(?:Body\\: )?([\\d\\.]+) (inches|feet); bill: ([\\d\\.]+) (inches|feet)$");
		matcher = pattern.matcher(fact.value);
		if (matcher.find()) {
		    minimumLength = round(Units.toMeters(matcher.group(1), matcher.group(2)));
		    maximumLength = 0.0;
		    minimumBillLength = round(Units.toMeters(matcher.group(3), matcher.group(4)));
		    maximumBillLength = 0.0;
		    break;
		}

		// Height at the shoulder: 8.2 to 13 feet
		pattern = Pattern.compile("^Height at the shoulder: ([\\d\\.]+) to ([\\d\\.]+) (inches|feet)$");
		matcher = pattern.matcher(fact.value);
		if (matcher.find()) {
		    minimumHeight = round(Units.toMeters(matcher.group(1), matcher.group(3)));
		    maximumHeight = round(Units.toMeters(matcher.group(2), matcher.group(3)));
		    break;
		}

		throw new BugError("Invalid size pattern |%s|.", fact.value);

	    case "Weight":
		pattern = Pattern.compile("^([\\d\\.]+) to ([\\d\\.]+) (ounces|pounds|tons)$");
		matcher = pattern.matcher(fact.value);
		if (matcher.find()) {
		    minimumWeight = round(Units.toKilograms(matcher.group(1), matcher.group(3)));
		    maximumWeight = round(Units.toKilograms(matcher.group(2), matcher.group(3)));
		    break;
		}

		pattern = Pattern.compile("^([\\d\\.]+) (ounces|pounds|tons)$");
		matcher = pattern.matcher(fact.value);
		if (matcher.find()) {
		    minimumWeight = round(Units.toKilograms(matcher.group(1), matcher.group(2)));
		    maximumWeight = 0.0;
		    break;
		}

		pattern = Pattern.compile("^Up to ([\\d\\.]+) (ounces|pounds|tons)$");
		matcher = pattern.matcher(fact.value);
		if (matcher.find()) {
		    minimumWeight = 0.0;
		    maximumWeight = round(Units.toKilograms(matcher.group(1), matcher.group(2)));
		    break;
		}
		
		throw new BugError("Invalid weight pattern |%s|.", fact.value);
	    }
	}
    }

    private static double round(double value) {
	return Math.round(value * 100) / 100.00;
    }

    public boolean hasLifespan() {
	return minimumLifespan != null;
    }

    public double getMinimumLifespan() {
	return minimumLifespan;
    }

    public double getMaximumLifespan() {
	return maximumLifespan;
    }

    public boolean hasLength() {
	return minimumLength != null;
    }

    public double getMinimumLength() {
	return minimumLength;
    }

    public double getMaximumLength() {
	return maximumLength;
    }

    public boolean hasHeight() {
	return minimumHeight != null;
    }

    public double getMinimumHeight() {
	return minimumHeight;
    }

    public double getMaximumHeight() {
	return maximumHeight;
    }

    public boolean hasWingspan() {
	return minimumWingspan != null;
    }

    public double getMinimumWingspan() {
	return minimumWingspan;
    }

    public double getMaximumWingspan() {
	return maximumWingspan;
    }

    public boolean hasWeight() {
	return minimumWeight != null;
    }

    public double getMinimumWeight() {
	return minimumWeight;
    }

    public double getMaximumWeight() {
	return maximumWeight;
    }

    public boolean hasBillLength() {
	return minimumBillLength != null;
    }

    public Double getMinimumBillLength() {
	return minimumBillLength;
    }

    public Double getMaximumBillLength() {
	return maximumBillLength;
    }

    @SuppressWarnings("unused")
    private static class Facts {
	private String id;
	private URL thumbnail;
	private boolean isKids;
	private List<Fact> facts;
	private String statusCode;
	private String populationTrend;
    }

    private static class Fact {
	private String key;
	private String value;

	public String toString() {
	    return Strings.toString(key, value);
	}
    }
}
