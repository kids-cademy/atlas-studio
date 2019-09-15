package com.kidscademy.atlas.studio.it;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.kidscademy.atlas.studio.dao.TaxonomyDao;
import com.kidscademy.atlas.studio.dao.TaxonomyDaoImpl;

import js.transaction.TransactionFactory;
import js.transaction.eclipselink.TransactionFactoryImpl;

public class TaxonomyDaoTest {
    private static TransactionFactory factory;

    @BeforeClass
    public static void beforeClass() throws IOException {
	factory = new TransactionFactoryImpl("itis-test");
    }

    private TaxonomyDao dao;

    @Before
    public void beforeTest() {
	dao = factory.newInstance(TaxonomyDaoImpl.class);
    }

    @Test
    public void getObjectHierarchy() {
	List<Integer> hierarchy = dao.getObjectHierarchy("ursus arctos");
	System.out.println(hierarchy);
    }

    @Test
    public void getObjectTaxonomy() {
	Map<String, String> taxonomy = dao.getObjectTaxonomy("ursus arctos");
	System.out.println(taxonomy);
    }
}
