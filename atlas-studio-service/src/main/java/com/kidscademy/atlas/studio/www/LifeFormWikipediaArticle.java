package com.kidscademy.atlas.studio.www;

import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kidscademy.atlas.studio.model.HDate;
import com.kidscademy.atlas.studio.model.HDateRange;
import com.kidscademy.atlas.studio.model.Taxon;
import com.kidscademy.atlas.studio.util.Strings;

import js.dom.Document;
import js.dom.DocumentBuilder;
import js.dom.EList;
import js.dom.Element;
import js.util.Classes;

public class LifeFormWikipediaArticle implements HDateRange {
    /** Binomial or trinomial name. */
    private String scientificName;
    private boolean trinomialName;
    private final String commonName;
    private HDate startDate;
    private HDate endDate;
    private final List<Taxon> taxonomy = new ArrayList<>();
    private String conservationStatus;
    private final String definition;
    private final String description;

    public LifeFormWikipediaArticle(URL articleURL) {
	DocumentBuilder builder = Classes.loadService(DocumentBuilder.class);
	Document page = builder.loadHTML(articleURL);

	commonName = text(page.getByXPath("//H1[@id='firstHeading']"));

	EList content = page.findByXPath("//DIV[@id='mw-content-text']/DIV[contains(@class,'mw-parser-output')]/*");
	WikipediaArticleText articleText = new WikipediaArticleText(content);
	definition = articleText.getDefinition();
	description = articleText.getDescription();

	EList biota = page.findByXPath("//TABLE[contains(@class,'biota')]/TBODY/TR");
	BiotaSection section = BiotaSection.NONE;
	for (Element element : biota) {
	    String text = element.getText().trim();

	    Element header = element.getByTag("TH");
	    if (header == null) {
		// if section header is null we are in section body

		switch (section) {
		case CONSERVATION:
		    conservationStatus = conservation(element);
		    break;

		case TAXONOMY:
		    Taxon taxon = taxon(element);
		    if (taxon != null) {
			taxonomy.add(taxon);
		    }
		    break;

		case TRINOMIAL:
		    trinomialName = true;

		case BINOMIAL:
		case SPECIES:
		    // is possible to have distribution map mixed with name section
		    // keep first not null text value
		    if (scientificName == null) {
			scientificName = text(element.getByTag("I"));
		    }
		    break;

		default:
		    break;
		}

		continue;
	    }

	    section = BiotaSection.NONE;
	    if (text.contains("Temporal range")) {
		// temporal range is included on header and need to be handled here
		updateTemporalRange(header);
		continue;
	    }

	    // detect current section
	    // note that biota table sections order is not defined
	    if (text.contains("Conservation status")) {
		section = BiotaSection.CONSERVATION;
	    } else if (text.contains("Scientific classification")) {
		section = BiotaSection.TAXONOMY;
	    } else if (text.contains("Type species") || text.contains("Species")) {
		section = BiotaSection.SPECIES;
	    } else if (text.contains("Trinomial name")) {
		section = BiotaSection.TRINOMIAL;
	    } else if (text.contains("Binomial name")) {
		section = BiotaSection.BINOMIAL;
	    }
	}

	// normalize scientific name
	// it can happen to have scientific name using shorthand for genus

	if (scientificName.contains(".")) {
	    String[] parts = scientificName.split(" ");
	    Taxon genus = taxon(taxonomy, "genus");
	    if (genus != null) {
		parts[0] = genus.getValue();
	    }
	    scientificName = Strings.join(parts);
	}

	// normalize taxonomy
	// update species and subspecies since Wikipedia uses shorthand genus

	String speciesValue = scientificName;
	String subspeciesValue = null;
	if (trinomialName) {
	    int lastSeparator = scientificName.lastIndexOf(' ');
	    if (lastSeparator == -1) {
		return;
	    }
	    speciesValue = scientificName.substring(0, lastSeparator).trim();
	    subspeciesValue = scientificName;
	}

	Taxon species = taxon(taxonomy, "species");
	if (species != null) {
	    species.setValue(speciesValue);
	} else {
	    species = new Taxon("species", speciesValue);
	    taxonomy.add(species);
	}

	if (subspeciesValue == null) {
	    return;
	}
	Taxon subspecies = taxon(taxonomy, "subspecies");
	if (subspecies != null) {
	    subspecies.setValue(subspeciesValue);
	} else {
	    subspecies = new Taxon("subspecies", subspeciesValue);
	    taxonomy.add(subspecies);
	}
    }

    private static Taxon taxon(List<Taxon> taxonomy, String taxonName) {
	for (Taxon taxon : taxonomy) {
	    if (taxon.getName().equals(taxonName)) {
		return taxon;
	    }
	}
	return null;
    }

    public String getScientificName() {
	return scientificName;
    }

    public String getCommonName() {
	return commonName;
    }

    public HDate getStartDate() {
	return startDate;
    }

    public HDate getEndDate() {
	return endDate;
    }

    public List<Taxon> getTaxonomy() {
	return taxonomy;
    }

    public String getConservationStatus() {
	return conservationStatus;
    }

    public String getDefinition() {
	return definition;
    }

    public String getDescription() {
	return description;
    }

    // --------------------------------------------------------------------------------------------

    private void updateTemporalRange(Element header) {
	String text = header.getText();
	int separatorIndex = text.indexOf(':') + 2;
	if (separatorIndex == 1) {
	    return;
	}
	text = text.substring(separatorIndex);

	Matcher matcher = null;
	boolean found = false;
	for (Pattern pattern : TEMPORAL_RANGE_PATTERNS) {
	    matcher = pattern.matcher(text);
	    if (matcher.find()) {
		found = true;
		break;
	    }
	}
	if (!found) {
	    return;
	}

	if (matcher.groupCount() == 2) {
	    Double value = number(matcher.group(1));
	    if (value == null) {
		return;
	    }
	    String unit = matcher.group(2);
	    if ("years ago".equals(unit)) {
		value = value / 1000;
		unit = "ka";
	    }
	    HDate.Format format = HDate.Format.yearsAgo(unit);
	    if (format == null) {
		return;
	    }
	    startDate = new HDate(value, format);
	    return;
	}

	Double startValue = number(matcher.group(1));
	if (startValue == null) {
	    return;
	}
	Double endValue = number(matcher.group(2));
	if (endValue == null) {
	    return;
	}
	String unit = matcher.group(3).toLowerCase();
	if ("years ago".equals(unit)) {
	    startValue = startValue / 1000;
	    endValue = endValue / 1000;
	    unit = "ka";
	}
	HDate.Format format = HDate.Format.yearsAgo(unit);
	if (format == null) {
	    return;
	}
	startDate = new HDate(startValue, format);
	endDate = new HDate(endValue, format);
    }

    private static String conservation(Element row) {
	return text(row, CONSERVATION_XPATHS);
    }

    private static Taxon taxon(Element row) {
	Taxon taxon = new Taxon();
	String name = row.getByXPath("TD[1]").getTextContent().trim();
	if (name.charAt(name.length() - 1) == ':') {
	    name = name.substring(0, name.length() - 1);
	}

	// it seems Wikipedia author(s) mix two taxonomic systems:
	// Linnaean taxonomy and Cladistic method

	// anyway clade name seems to have italic with XPath TD[1]/I, in which
	// case current name variable is empty; return null to reject clades
	if (name.isEmpty()) {
	    return null;
	}

	taxon.setName(name.toLowerCase());
	taxon.setValue(text(row, TAXON_XPATHS));
	return taxon;
    }

    private static String text(Element element, String[] xpaths) {
	for (String xpath : xpaths) {
	    Element textElement = element.getByXPath(xpath);
	    if (textElement != null) {
		String text = textElement.getTextContent().trim();
		if (!text.isEmpty()) {
		    return text;
		}
	    }
	}
	return null;
    }

    private static String text(Element element) {
	if (element == null) {
	    return null;
	}
	return Strings.removeReferences(element.getText().trim());
    }

    private static Double number(String value) {
	NumberFormat format = NumberFormat.getNumberInstance(Locale.ENGLISH);
	try {
	    return format.parse(value).doubleValue();
	} catch (ParseException e) {
	    return null;
	}
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Biota infobox is a table with multiple sections. A section starts with a
     * table row containing a TH element. Section order is not defined.
     * 
     * @author Iulian Rotaru
     */
    private static enum BiotaSection {
	NONE, CONSERVATION, TAXONOMY, SPECIES, TRINOMIAL, BINOMIAL
    }

    /**
     * Regular expression patterns for temporal range values. Temporal range value
     * contains numerical value(s) and related measurement unit, mixed with some
     * text - not relevant for this case.
     * <p>
     * Currently there are two major types: range with both start and end date, or
     * only start date till present. First pattern has two numeric values followed
     * by unit; the second pattern has only one numeric value and units.
     * <p>
     * Numeric value is English formatted and may have grouping and decimals. Unit
     * names are also English for years ago and multiples.
     * <p>
     * Both patterns uses special - non ASCII, characters for units space and dash
     * range separator.
     */
    private static final Pattern[] TEMPORAL_RANGE_PATTERNS = new Pattern[] {
	    // sample: Late Jurassic, 155-150 Ma...
	    // range values separator is not dash, is a UTF-8 character - not very clear
	    // what code
	    // identify it as one or many non numeric characters
	    // for white space before units see below pattern
	    Pattern.compile(
		    "[^\\(](\\d+(?:,\\d+)*(?:\\.\\d+)?)[^\\d,\\.]+(\\d+(?:,\\d+)*(?:\\.\\d+)?)[^\\d](years ago|ka|ma|ga)[^\\)]",
		    Pattern.CASE_INSENSITIVE),

	    // sample: At least 14,200 years ago – present[2]
	    // space before units is a single white space character with code 160
	    // uses dot (.) for any
	    Pattern.compile("(\\d+(?:,\\d+)*(?:\\.\\d+)?).(years ago|ka|ma|ga)", Pattern.CASE_INSENSITIVE)

    };

    /**
     * XPath variants of descendant of the biota table row containing conservation
     * status.
     */
    private static final String[] CONSERVATION_XPATHS = new String[] { //
	    "TD/DIV/A", //
	    "TD/DIV" //
    };

    /**
     * Possible XPaths of descendant of the biota table row that contains taxon
     * value. This values are depends on article source page structure and should be
     * maintained.
     * <p>
     * This list cannot be exhaustive. Is quite possible to upgrade it when new
     * variants will be discovered.
     */
    private static final String[] TAXON_XPATHS = new String[] { //
	    "TD[2]/A", //
	    "TD[2]/A/I", //
	    "TD[2]/DIV/I/A", //
	    "TD[2]/DIV/I/B" //
    };
}
