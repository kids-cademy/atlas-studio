package com.kidscademy.atlas.studio.www;

import java.util.Map;

import com.kidscademy.atlas.studio.util.Strings;

import js.xpath.client.XPath;

public class PlantVillageArticle {
    @XPath("//*[@id='info-Description']/following-sibling::P[1]")
    private String description;

    @XPath("//*[@id='info-Description']/following-sibling::I[1]/following-sibling::text()")
    private String descriptionVariant;

    @XPath("//*[@id='info-Uses']/following-sibling::text()")
    private String uses;

    @XPath("//*[@id='info-Propagation']/following-sibling::*[1]/following-sibling::text()")
    private String requirements;

    @XPath("//*[@id='info-Propagation']/following-sibling::*[2]/following-sibling::text()")
    private String propagation;

    @XPath("//*[@id='info-Propagation']/following-sibling::*[3]/following-sibling::text()")
    private String planting;

    @XPath("//*[@id='info-Propagation']/following-sibling::*[4]/following-sibling::text()")
    private String maintenance;

    public String getDescription() {
	return description;
    }

    public String getUses() {
	return uses;
    }

    public String getRequirements() {
	return requirements;
    }

    public String getPropagation() {
	return propagation;
    }

    public String getPlanting() {
	return planting;
    }

    public String getMaintenance() {
	return maintenance;
    }

    public void getSections(Map<String, String> sections) {
	if (description != null) {
	    sections.put("description", text(description));
	} else {
	    sections.put("description", text(descriptionVariant));
	}
	sections.put("uses", text(uses));
	sections.put("requirements", text(requirements));
	sections.put("propagation", text(propagation));
	sections.put("planting", text(planting));
	sections.put("maintenance", text(maintenance));
    }

    public static String text(String text) {
	if (text == null) {
	    return null;
	}
	return Strings.join(Strings.breakSentences(text), "\r\n\r\n");
    }
}
