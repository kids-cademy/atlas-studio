package com.kidscademy.atlas.studio.www;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import js.xpath.client.XPath;

public interface WikiHow {
    @Path("{path}")
    @XPath("//H1[@id='section_0']/A")
    String getTitle(@PathParam("path") String path);
}
