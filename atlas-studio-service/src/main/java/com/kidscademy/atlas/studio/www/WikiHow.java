package com.kidscademy.atlas.studio.www;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import js.xpath.client.XPath;

public interface WikiHow {
    @Path("{path}")
    @XPath("/HTML/BODY/DIV[3]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/H1/A")
    String getTitle(@PathParam("path") String path);
}
