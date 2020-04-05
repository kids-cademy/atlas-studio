package com.kidscademy.atlas.studio.www;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import js.xpath.client.XPath;

@Path("{path}")
public interface PlantVillage {
    @XPath("//*[@id='info-Description']/following-sibling::P[1]")
    String getDescription(@PathParam("path") String path);

    @XPath("//*[@id='info-Uses']/following-sibling::text()")
    String getUses(@PathParam("path") String path);

    @XPath("//*[@id='info-Propagation']/following-sibling::*[1]/following-sibling::text()")
    String getRequirements(@PathParam("path") String path);

    @XPath("//*[@id='info-Propagation']/following-sibling::*[2]/following-sibling::text()")
    String getPropagation(@PathParam("path") String path);

    @XPath("//*[@id='info-Propagation']/following-sibling::*[3]/following-sibling::text()")
    String getPlanting(@PathParam("path") String path);

    @XPath("//*[@id='info-Propagation']/following-sibling::*[4]/following-sibling::text()")
    String getMaintenance(@PathParam("path") String path);
    
    PlantVillageArticle getArticle(@PathParam("path") String path);
}
