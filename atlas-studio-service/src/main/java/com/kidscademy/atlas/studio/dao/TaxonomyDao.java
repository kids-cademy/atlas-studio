package com.kidscademy.atlas.studio.dao;

import java.util.List;
import java.util.Map;

public interface TaxonomyDao {
    List<Integer> getObjectHierarchy(String binomialName);
    
    Map<String,String> getObjectTaxonomy(String binomialName);
}
