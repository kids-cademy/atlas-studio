package com.kidscademy.atlas.studio.www;

import java.util.List;

import js.xpath.client.XPath;

public class SoftSchoolsFacts {
    @XPath("//*[@id='hdrcnt']/H1")
    private String title;

    @XPath("//*[@class='factsmaintbl']//TD")
    private String description;

    @XPath("//*[@class='fact_topbar']//TD")
    private List<String> facts;

    public String getTitle() {
	return title;
    }

    public String getDescription() {
	return description;
    }

    public List<String> getFacts() {
	return facts;
    }
}
