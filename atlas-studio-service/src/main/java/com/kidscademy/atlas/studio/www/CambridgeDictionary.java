package com.kidscademy.atlas.studio.www;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import js.xpath.client.XPath;

public interface CambridgeDictionary {
    @Path("dictionary/english/{word}")
    @XPath("//*[@id='cald4-1-1-1']/DIV[1]/DIV[2]/P/B")
    String getDefinition(@PathParam("word") String word);
}
