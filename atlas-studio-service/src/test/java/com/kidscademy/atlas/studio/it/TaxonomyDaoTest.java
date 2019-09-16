package com.kidscademy.atlas.studio.it;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.kidscademy.atlas.studio.dao.TaxonomyDao;
import com.kidscademy.atlas.studio.dao.TaxonomyDaoImpl;

import js.lang.Pair;
import js.transaction.TransactionFactory;
import js.transaction.eclipselink.TransactionFactoryImpl;
import js.util.Strings;

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
	assertThat(hierarchy, notNullValue());
	assertThat(hierarchy, hasSize(15));

	int[] expected = new int[] { 202423, 914154, 914156, 158852, 331030, 914179, 914181, 179913, 179916, 179925,
		180539, 552303, 180540, 180541, 180543 };
	for (int i = 0; i < expected.length; ++i) {
	    assertThat(hierarchy.get(i), equalTo(expected[i]));
	}
    }

    @Test
    public void getObjectTaxonomy() {
	Map<String, String> taxonomy = dao.getObjectTaxonomy("ursus arctos");
	assertThat(taxonomy, notNullValue());
	assertThat(taxonomy.values(), hasSize(18));

	String value = "Kingdom=Animalia, Subkingdom=Bilateria, Infrakingdom=Deuterostomia, Phylum=Chordata, Division=Chordata, Subphylum=Vertebrata, Subdivision=Vertebrata, Infraphylum=Gnathostomata, Infradivision=Gnathostomata, Superclass=Tetrapoda, Class=Mammalia, Subclass=Theria, Infraclass=Eutheria, Order=Carnivora, Suborder=Caniformia, Family=Ursidae, Genus=Ursus, Species=Ursus arctos";
	List<Pair> expected = Strings.splitPairs(value, ',', '=');
	int i = 0;
	for (Map.Entry<String, String> entry : taxonomy.entrySet()) {
	    assertThat(entry.getKey(), equalTo(expected.get(i).key()));
	    assertThat(entry.getValue(), equalTo(expected.get(i).value()));
	    ++i;
	}
    }
}
