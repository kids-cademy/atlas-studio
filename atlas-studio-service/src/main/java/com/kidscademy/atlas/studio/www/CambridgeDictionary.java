package com.kidscademy.atlas.studio.www;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import js.xpath.client.XPath;

public interface CambridgeDictionary {
    @Path("dictionary/english/{word}")
    @XPath("//*[@id=\"page-content\"]/DIV[2]/DIV/DIV[1]/DIV[2]/DIV/DIV[3]/DIV/DIV/DIV/DIV[3]/DIV/DIV[2]/DIV/DIV[2]/DIV[1]/DIV[1]/DIV")
    String getDefinition(@PathParam("word") String word);
}
