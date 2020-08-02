package com.kidscademy.atlas.studio;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.kidscademy.atlas.studio.model.ApiDescriptor;
import com.kidscademy.atlas.studio.model.AtlasItem;
import com.kidscademy.atlas.studio.model.Feature;
import com.kidscademy.atlas.studio.model.Link;
import com.kidscademy.atlas.studio.model.Taxon;

import js.tiny.container.annotation.Service;

@Service
public interface ApiService {
    List<ApiDescriptor> getAvailableApis();

    List<ApiDescriptor> getApiDescriptors(List<String> apiNames);
    
    Object invokeAPI(String apiName, Link link);
    
    String getDefinition(Link link);

    LinkedHashMap<String, String> getDescription(Link link);

    Map<String, String> getFacts(Link link);

    List<Taxon> getTaxonomy(Link link);
    
    List<Feature> getPhysicalTraits(Link link);
    
    List<Feature> getEdibleNutrients(Link link);

    List<Feature> getMetroStatistics(Link link);

    List<Feature> getMilitarySpecifications(Link link);
    
    AtlasItem importWikipediaObject(int collectionId, URL articleURL);
}
