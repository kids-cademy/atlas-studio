package com.kidscademy.atlas.studio.model;

public enum TaxonomyClass {
    FAMILY, BIOLOGICAL;

    // EXPERIMENTAL: implement taxonomy templates on server instead of UI

    public static class Taxon {
	private final String name;
	/** Taxon value is always null. It is used only on user interface. */
	private final String value;

	public Taxon(String name) {
	    this.name = name;
	    this.value = null;
	}

	public String getName() {
	    return name;
	}

	public String getValue() {
	    return value;
	}
    }

    public enum ValueType {
	STRING, ENUM
    }

    public static class Family {
	private static final Taxon[] template = new Taxon[] { //
		new Taxon(TaxonomyClass.FAMILY.name()) //
	};

	private static final ValueType type = ValueType.ENUM;
	private static final String[] values = new String[] { "PERCUSSION", "STRINGS", "WOODWIND", "KEYBOARD" };

	public static Taxon[] getTemplate() {
	    return template;
	}

	public static ValueType getType() {
	    return type;
	}

	public static String[] getValues() {
	    return values;
	}
    }

    public static class Biological {
	private static final Taxon[] template = new Taxon[] { //
		new Taxon("Kingdom"), //
		new Taxon("Phylum"), //
		new Taxon("Class"), //
		new Taxon("Order"), //
		new Taxon("Family"), //
		new Taxon("Genus"), //
		new Taxon("Species") //
	};
	private static final ValueType type = ValueType.STRING;
	private static final String[] values = null;

	public static Taxon[] getTemplate() {
	    return template;
	}

	public static ValueType getType() {
	    return type;
	}

	public static String[] getValues() {
	    return values;
	}
    }
}
