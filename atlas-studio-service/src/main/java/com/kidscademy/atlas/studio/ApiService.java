package com.kidscademy.atlas.studio;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.kidscademy.atlas.studio.model.ApiDescriptor;
import com.kidscademy.atlas.studio.model.Feature;
import com.kidscademy.atlas.studio.model.Link;
import com.kidscademy.atlas.studio.model.Taxon;

import js.tiny.container.annotation.Service;

@Service
public interface ApiService {
    List<ApiDescriptor> getAvailableApis();

    List<ApiDescriptor> getApiDescriptors(List<String> apiNames);
    
    String getDefinition(Link link);

    LinkedHashMap<String, String> getDescription(Link link);

    Map<String, String> getFacts(Link link);

    List<Taxon> getTaxonomy(Link link);

    List<Feature> getFeatures(int collectionId, Link link);
}
