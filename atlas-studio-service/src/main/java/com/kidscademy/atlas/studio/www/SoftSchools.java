package com.kidscademy.atlas.studio.www;

import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import js.xpath.client.XPath;

public interface SoftSchools {
    @Path("{path}")
    @XPath("//*[@id='hdrcnt']/H1")
    String getTitle(@PathParam("path") String path);

    @Path("{path}")
    @XPath("//*[@class='factsmaintbl']//TD")
    String getDescription(@PathParam("path") String path);

    @Path("{path}")
    SoftSchoolsFacts getFacts(@PathParam("path") String path);

    @Path("{path}")
    @XPath(value = "//*[@id='sortable_list']/A", attribute = "href")
    List<String> getFactLinks(@PathParam("path") String path);
}
