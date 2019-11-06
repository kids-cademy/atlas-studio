package com.kidscademy.atlas.studio.www;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import js.xpath.client.XPath;

public interface MerriamWebster {
    @Path("{word}")
    @XPath("//DIV[@id='dictionary-entry-1']//SPAN[@class='dtText']")
    String getDefinition(@PathParam("word") String word);
}
