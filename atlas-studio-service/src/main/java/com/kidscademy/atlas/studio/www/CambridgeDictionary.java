package com.kidscademy.atlas.studio.www;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import js.xpath.client.XPath;

public interface CambridgeDictionary {
    @Path("dictionary/english/{word}")
    @XPath("//DIV[@class='def ddef_d db']")
    String getDefinition(@PathParam("word") String word);
}
