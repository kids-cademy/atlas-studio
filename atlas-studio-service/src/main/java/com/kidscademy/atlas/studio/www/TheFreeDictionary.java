package com.kidscademy.atlas.studio.www;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import js.xpath.client.Header;
import js.xpath.client.XPath;

@Header("User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0")
public interface TheFreeDictionary {
    @Path("{word}")
    @XPath("//*[@id='Definition']/SECTION[1]/DIV[1]/DIV")
    String getDefinition(@PathParam("word") String word);
}
