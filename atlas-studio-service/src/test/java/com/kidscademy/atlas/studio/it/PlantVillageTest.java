package com.kidscademy.atlas.studio.it;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.kidscademy.atlas.studio.www.PlantVillage;
import com.kidscademy.atlas.studio.www.PlantVillageArticle;

import js.tiny.container.core.AppContext;
import js.tiny.container.unit.TestContext;

public class PlantVillageTest {
    private static final String DESCRIPTOR = "" + //
	    "<app-descriptor>" + //
	    " <managed-classes>" + //
	    "  <plant-village interface='com.kidscademy.atlas.studio.www.PlantVillage' type='REMOTE' url='https:xpath://plantvillage.psu.edu/' />"
	    + //
	    " </managed-classes>" + //
	    "</app-descriptor>";

    private PlantVillage plantVillage;

    @Before
    public void beforeTest() throws Exception {
	AppContext context = TestContext.start(DESCRIPTOR);
	plantVillage = context.getInstance(PlantVillage.class);
    }

    @Test
    public void getDescription() {
	String description = plantVillage.getDescription("/topics/apricot/infos");
	assertThat(description, notNullValue());
	assertThat(description, startsWith("Apricot, Prunus armeniaca is a deciduous tree"));
    }

    @Test
    public void getUses() {
	String description = plantVillage.getUses("/topics/apricot/infos");
	assertThat(description, notNullValue());
	assertThat(description, startsWith("Apricots can be consumed fresh or dried."));
    }
    
    @Test
    public void getRequirements() {
	String description = plantVillage.getRequirements("/topics/apricot/infos");
	assertThat(description, notNullValue());
	assertThat(description, startsWith("Apricots have a high genetic variability"));
    }
    
    @Test
    public void getPropagation() {
	String description = plantVillage.getPropagation("/topics/apricot/infos");
	assertThat(description, notNullValue());
	assertThat(description, startsWith("Apricot trees are usually propagated vegetatively"));
    }
    
    @Test
    public void getPlanting() {
	String description = plantVillage.getPlanting("/topics/apricot/infos");
	assertThat(description, notNullValue());
	assertThat(description, startsWith("Apricot trees should be planted in full sun."));
    }
    
    @Test
    public void getMaintenance() {
	String description = plantVillage.getMaintenance("/topics/apricot/infos");
	assertThat(description, notNullValue());
	assertThat(description, startsWith("Apricots should be pruned annually"));
    }
    
    @Test
    public void getArticle() {
	PlantVillageArticle article = plantVillage.getArticle("/topics/apricot/infos");
	assertThat(article, notNullValue());

	assertThat(article.getDescription(), notNullValue());
	assertThat(article.getDescription(), startsWith("Apricot, Prunus armeniaca is a deciduous tree"));

	assertThat(article.getUses(), notNullValue());
	assertThat(article.getUses(), startsWith("Apricots can be consumed fresh or dried."));

	assertThat(article.getRequirements(), notNullValue());
	assertThat(article.getRequirements(), startsWith("Apricots have a high genetic variability"));

	assertThat(article.getPropagation(), notNullValue());
	assertThat(article.getPropagation(), startsWith("Apricot trees are usually propagated vegetatively"));

	assertThat(article.getPlanting(), notNullValue());
	assertThat(article.getPlanting(), startsWith("Apricot trees should be planted in full sun."));
	
	assertThat(article.getMaintenance(), notNullValue());
	assertThat(article.getMaintenance(), startsWith("Apricots should be pruned annually"));
    }
}
