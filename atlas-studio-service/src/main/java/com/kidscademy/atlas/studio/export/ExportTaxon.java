package com.kidscademy.atlas.studio.export;

import java.util.HashMap;
import java.util.Map;

import com.kidscademy.atlas.studio.model.Taxon;

import js.lang.BugError;
import js.lang.Displayable;
import js.util.Strings;

public class ExportTaxon implements Displayable {
    private final String name;
    private final String display;
    private final String value;

    public ExportTaxon(Taxon taxon) {
	this.name = taxon.getName();
	this.display = display(taxon.getName());
	this.value = taxon.getValue();
    }

    @Override
    public String toDisplay() {
	return Strings.concat(display, ' ', value);
    }

    @Override
    public String toString() {
	return Strings.toString(name, display, value);
    }

    public static String display(String name) {
	String display = EN.get(name);
	if (display == null) {
	    throw new BugError("Not mapped taxon name |%s|.", name);
	}
	return display;
    }

    private static final Map<String, String> EN = new HashMap<>();
    static {
	EN.put("kingdom", "Kingdom");
	EN.put("phylum", "Phylum");
	EN.put("class", "Class");
	EN.put("infraclass", "Infraclass");
	EN.put("order", "Order");
	EN.put("family", "Family");
	EN.put("genus", "Genus");
	EN.put("subgenus", "Subgenus");
	EN.put("species", "Species");
	EN.put("suborder", "Suborder");
	EN.put("subfamily", "Subfamily");
	EN.put("tribe", "Tribe");
	EN.put("subspecies", "Subspecies");
	EN.put("infraorder", "Infraorder");
	EN.put("parvorder", "Parvorder");
	EN.put("variety", "Variety");
	EN.put("type", "Type");
    }
}
