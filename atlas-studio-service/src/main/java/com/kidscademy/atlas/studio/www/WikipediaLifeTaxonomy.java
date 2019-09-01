package com.kidscademy.atlas.studio.www;

import js.xpath.client.XPath;

public class WikipediaLifeTaxonomy {
    @XPath("//TD[contains(text(),'Domain')]/following-sibling::TD/A")
    private String domain;

    @XPath("//TD[contains(text(),'Kingdom')]/following-sibling::TD/A")
    private String kingdom;

    @XPath("//TD[contains(text(),'Phylum')]/following-sibling::TD/A")
    private String phylum;

    @XPath("//TD[contains(text(),'Class')]/following-sibling::TD/A")
    private String clazz;

    @XPath("//TD[contains(text(),'Order')]/following-sibling::TD/A")
    private String order;

    @XPath("//TD[contains(text(),'Family')]/following-sibling::TD/A")
    private String family;

    @XPath("//TD[contains(text(),'Genus')]/following-sibling::TD/A")
    private String genus;

    @XPath("//TD[contains(text(),'Species')]/following-sibling::TD")
    private String species;

    public String getDomain() {
	return domain;
    }

    public String getKingdom() {
	return kingdom;
    }

    public String getPhylum() {
	return phylum;
    }

    public String getClazz() {
	return clazz;
    }

    public String getOrder() {
	return order;
    }

    public String getFamily() {
	return family;
    }

    public String getGenus() {
	return genus;
    }

    public String getSpecies() {
	return species;
    }
}
