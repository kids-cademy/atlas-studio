package com.kidscademy.atlas.studio.it;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.kidscademy.atlas.studio.Application;
import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.dao.AtlasDaoImpl;
import com.kidscademy.atlas.studio.model.AtlasCollection;
import com.kidscademy.atlas.studio.model.AtlasCollectionKey;
import com.kidscademy.atlas.studio.model.AtlasItem;
import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.DescriptionMeta;
import com.kidscademy.atlas.studio.model.ExternalSource;
import com.kidscademy.atlas.studio.model.FeatureMeta;
import com.kidscademy.atlas.studio.model.HDate;
import com.kidscademy.atlas.studio.model.Image;
import com.kidscademy.atlas.studio.model.LinkSource;
import com.kidscademy.atlas.studio.model.MediaSRC;
import com.kidscademy.atlas.studio.model.PhysicalQuantity;
import com.kidscademy.atlas.studio.model.Region;
import com.kidscademy.atlas.studio.model.Release;
import com.kidscademy.atlas.studio.model.ReleaseParent;
import com.kidscademy.atlas.studio.model.SearchFilter;
import com.kidscademy.atlas.studio.model.Taxon;
import com.kidscademy.atlas.studio.util.Files;

import js.json.Json;
import js.tiny.container.core.AppContext;
import js.transaction.TransactionFactory;
import js.transaction.eclipselink.TransactionFactoryImpl;
import js.unit.db.Database;
import js.util.Classes;

@RunWith(MockitoJUnitRunner.class)
public class AtlasDaoReadTest {
    private static Database database;
    private static TransactionFactory factory;

    @BeforeClass
    public static void beforeClass() throws SQLException {
	database = new Database("atlas_test", "kids_cademy", "kids_cademy");
	database.setVerbose(false);
	database.load(Classes.getResourceAsStream("atlas-data-set.xml"));
	factory = new TransactionFactoryImpl();
    }

    @Mock
    private AppContext context;

    private AtlasDao dao;

    @Before
    public void beforeTest() {
	dao = factory.newInstance(AtlasDaoImpl.class);
	new Application(context);
    }

    @Test
    public void getAtlasItem() {
	AtlasItem atlasItem = dao.getAtlasItem(1);
	assertAtlasItem(atlasItem);
    }

    private void assertAtlasItem(AtlasItem atlasItem) {
	assertThat(atlasItem, notNullValue());
	assertThat(atlasItem.getId(), equalTo(1));

	AtlasCollectionKey collection = atlasItem.getCollection();
	assertThat(collection, notNullValue());
	assertThat(collection.getId(), equalTo(1));
	assertThat(collection.getName(), equalTo("instrument"));
//	assertThat(collection.getDisplay(), equalTo("Instrument"));
//	assertThat(collection.getIconSrc(), equalTo(Files.mediaSrc(collection)));

	assertThat(atlasItem.getRepositoryName(), equalTo("instrument"));
	assertThat(atlasItem.getName(), equalTo("accordion"));
	assertThat(atlasItem.getDisplay(), equalTo("Accordion"));
	assertThat(atlasItem.getIconName(), equalTo("icon.jpg"));
	assertThat(atlasItem.getIconSrc(), equalTo(src("accordion", "icon.jpg")));
    }

    @Test
    public void getAtlasObject() throws MalformedURLException {
	AtlasObject object = dao.getAtlasObject(1);
	assertAtlasObject(object);
    }

    @Test
    public void postLoadAtlasObject() {
	AtlasObject object = dao.getAtlasObject(1);

	assertThat(object.getImages(), notNullValue());
	assertThat(object.getImages().size(), equalTo(4));
	assertThat(object.getImage("icon").getSrc(), equalTo(src("accordion", "icon.jpg")));
	assertThat(object.getImage("cover").getSrc(), equalTo(src("accordion", "piano-accordion.png")));
	assertThat(object.getImage("featured").getSrc(), equalTo(src("accordion", "button-accordion.png")));
	assertThat(object.getImage("contextual").getSrc(), equalTo(src("accordion", "image.jpg")));

	assertThat(object.getSampleSrc(), equalTo(src("accordion", "sample.mp3")));
	assertThat(object.getWaveformSrc(), equalTo(src("accordion", "waveform.png")));
    }

    @Test
    public void getAtlasObjectName() {
	String name = dao.getAtlasObjectName(1);
	assertThat(name, notNullValue());
	assertThat(name, equalTo("accordion"));
    }

    @Test
    public void getCollectionItems() {
	Map<String, String> criteria = new HashMap<>();
	criteria.put("state", "NONE");

	List<AtlasItem> items = dao.getCollectionItems(new SearchFilter(criteria), 1);
	assertThat(items, notNullValue());
	assertThat(items, hasSize(2));
	assertAtlasItem(items.get(0));
    }

    @Test
    public void getCollectionItemsByTaxon() {
	Taxon taxon = new Taxon("Family", "WOODWIND");
	List<AtlasItem> items = dao.getCollectionItemsByTaxon(1, taxon);
	assertThat(items, notNullValue());
	assertThat(items, hasSize(1));
	assertAtlasItem(items.get(0));
    }

    @Test
    public void getCollectionById() {
	AtlasCollection collection = dao.getCollectionById(1);
	assertThat(collection, notNullValue());

	assertCollectionFeaturesMeta(collection.getFeaturesMeta());

	assertThat(collection.getLinkSources(), notNullValue());
	assertThat(collection.getLinkSources(), hasSize(2));

	LinkSource linkSource = collection.getLinkSources().iterator().next();
	ExternalSource source = linkSource.getExternalSource();
	assertThat(source, notNullValue());
	assertThat(source.getId(), equalTo(1));
	assertThat(source.getHome(), equalTo("https://en.wikipedia.org/wiki/"));
	assertThat(source.getDomain(), equalTo("wikipedia.org"));
	assertThat(source.getDisplay(), equalTo("Wikipedia"));
	assertThat(source.getDefinitionTemplate(), equalTo("${display} article on Wikipedia."));

	assertThat(source.getApis(), equalTo("definition,description,features,taxonomy"));
	assertThat(linkSource.getApis(), equalTo("definition,description,taxonomy"));
    }

    @Test
    public void getCollectionFeaturesMeta() {
	assertCollectionFeaturesMeta(dao.getCollectionFeaturesMeta(1));
    }

    private static void assertCollectionFeaturesMeta(List<FeatureMeta> featuresMeta) {
	assertThat(featuresMeta, notNullValue());
	assertThat(featuresMeta, hasSize(3));

	assertThat(featuresMeta.get(0).getId(), equalTo(1));
	assertThat(featuresMeta.get(0).getName(), equalTo("height"));
	assertThat(featuresMeta.get(0).getDefinition(), equalTo("height"));
	assertThat(featuresMeta.get(0).getQuantity(), equalTo(PhysicalQuantity.LENGTH));
	assertThat(featuresMeta.get(0).getId(), equalTo(1));

	assertThat(featuresMeta.get(0).getName(), equalTo("height"));
	assertThat(featuresMeta.get(1).getName(), equalTo("length"));
	assertThat(featuresMeta.get(2).getName(), equalTo("width"));
    }

    @Test
    public void getCollectionDescriptionMeta() {
	List<DescriptionMeta> descriptionsMeta = dao.getCollectionDescriptionsMeta(1);
	assertThat(descriptionsMeta, notNullValue());
	assertThat(descriptionsMeta, hasSize(4));
	assertThat(descriptionsMeta.get(0).getName(), equalTo("description"));
	assertThat(descriptionsMeta.get(1).getName(), equalTo("uses"));
	assertThat(descriptionsMeta.get(2).getName(), equalTo("cultivation"));
	assertThat(descriptionsMeta.get(3).getName(), equalTo("origin"));
    }
    
    private static void assertAtlasObject(AtlasObject object) throws MalformedURLException {
	assertThat(object, notNullValue());
	assertThat(object.getId(), equalTo(1));

	assertThat(object.getCollection(), notNullValue());
	assertThat(object.getCollection().getId(), equalTo(1));
	assertThat(object.getCollection().getName(), equalTo("instrument"));
//	assertThat(object.getCollection().getDisplay(), equalTo("Instrument"));
//
//	assertThat(object.getCollection().getTaxonomyMeta(), notNullValue());
//	assertThat(object.getCollection().getTaxonomyMeta().size(), equalTo(1));
//	assertThat(object.getCollection().getTaxonomyMeta().get(0).getName(), equalTo("family"));
//	assertThat(object.getCollection().getTaxonomyMeta().get(0).getValues(),
//		equalTo("KEYBOARD,PERCUSSION,WOODWIND,BRASS,STRINGS,LAMELLOPHONE"));
//
//	assertTrue(object.getCollection().getFlags().hasEndDate());
//	assertTrue(object.getCollection().getFlags().hasConservationStatus());
//	assertTrue(object.getCollection().getFlags().hasAudioSample());

	assertThat(object.getState(), equalTo(AtlasObject.State.DEVELOPMENT));
	assertThat(object.getName(), equalTo("accordion"));
	assertThat(object.getName(), equalTo("accordion"));
	assertThat(object.getDisplay(), equalTo("Accordion"));
	assertThat(object.getDescription(), equalTo("Accordion description."));

	assertThat(object.getImages(), notNullValue());
	assertThat(object.getImages().size(), equalTo(4));
	assertThat(object.getImages().get("icon").getFileName(), equalTo("icon.jpg"));
	assertThat(object.getImages().get("icon").getSrc(), equalTo(src("accordion", "icon.jpg")));
	assertThat(object.getImages().get("cover").getFileName(), equalTo("piano-accordion.png"));
	assertThat(object.getImages().get("cover").getSrc(), equalTo(src("accordion", "piano-accordion.png")));
	assertThat(object.getImages().get("featured").getFileName(), equalTo("button-accordion.png"));
	assertThat(object.getImages().get("featured").getSrc(), equalTo(src("accordion", "button-accordion.png")));
	assertThat(object.getImages().get("contextual").getFileName(), equalTo("image.jpg"));
	assertThat(object.getImages().get("contextual").getSrc(), equalTo(src("accordion", "image.jpg")));

	assertThat(object.getTaxonomy(), notNullValue());
	assertThat(object.getTaxonomy(), not(empty()));
	assertThat(object.getTaxonomy(), hasSize(2));
	assertThat(object.getTaxonomy().get(0).getName(), equalTo("Group"));
	assertThat(object.getTaxonomy().get(0).getValue(), equalTo("WOODEN"));
	assertThat(object.getTaxonomy().get(1).getName(), equalTo("Family"));
	assertThat(object.getTaxonomy().get(1).getValue(), equalTo("WOODWIND"));

	assertThat(object.getSampleTitle(), equalTo("Sample"));
	assertThat(object.getSampleName(), equalTo("sample.mp3"));
	assertThat(object.getWaveformName(), equalTo("waveform.png"));
	assertThat(object.getSampleSrc(), equalTo(src("accordion", "sample.mp3")));
	assertThat(object.getWaveformSrc(), equalTo(src("accordion", "waveform.png")));

	assertThat(object.getStartDate().getValue(), equalTo(1234567890.0));
	assertThat(object.getStartDate().getFormat(), equalTo(HDate.Format.DATE));
	assertThat(object.getStartDate().getPeriod(), equalTo(HDate.Period.FULL));
	assertThat(object.getStartDate().getEra(), equalTo(HDate.Era.CE));

	assertThat(object.getAliases(), notNullValue());
	assertThat(object.getAliases(), hasSize(1));
	assertThat(object.getAliases().get(0), equalTo("Squeezebox"));

	assertThat(object.getSpreading(), notNullValue());
	assertThat(object.getSpreading(), hasSize(1));
	assertThat(object.getSpreading().get(0), notNullValue());
	assertThat(object.getSpreading().get(0).getName(), equalTo("Romania"));
	assertThat(object.getSpreading().get(0).getArea(), equalTo(Region.Area.CENTRAL));

	assertThat(object.getFacts(), notNullValue());
	assertThat(object.getFacts().keySet(), not(empty()));
	assertThat(object.getFacts().keySet(), hasSize(2));
	assertThat(object.getFacts().get("Fact #1"), equalTo("Fact #1 description."));
	assertThat(object.getFacts().get("Fact #2"), equalTo("Fact #2 description."));

	assertThat(object.getFeatures(), notNullValue());
	assertThat(object.getFeatures(), not(empty()));
	assertThat(object.getFeatures(), hasSize(2));
	assertThat(object.getFeatures().get(0).getName(), equalTo("height"));
	assertThat(object.getFeatures().get(0).getValue(), equalTo(5.3));
	assertThat(object.getFeatures().get(0).getMaximum(), nullValue());
	assertThat(object.getFeatures().get(0).getQuantity(), equalTo(PhysicalQuantity.LENGTH));
	assertThat(object.getFeatures().get(1).getName(), equalTo("width"));
	assertThat(object.getFeatures().get(1).getValue(), equalTo(1.00584));
	assertThat(object.getFeatures().get(1).getMaximum(), equalTo(1.09728));
	assertThat(object.getFeatures().get(1).getQuantity(), equalTo(PhysicalQuantity.LENGTH));

	assertThat(object.getLinks(), notNullValue());
	assertThat(object.getLinks(), hasSize(2));

	assertThat(object.getLinks().get(0), notNullValue());
	assertThat(object.getLinks().get(0).toDisplay(), equalTo("Wikipedia"));
	assertThat(object.getLinks().get(0).getIconSrc(), equalTo(new MediaSRC("/media/link/wikipedia.png")));
	assertThat(object.getLinks().get(0).getUrl(), equalTo(new URL("https://en.wikipedia.org/wiki/Accordion")));
	assertThat(object.getLinks().get(1), notNullValue());
	assertThat(object.getLinks().get(1).toDisplay(), equalTo("Britannica"));
	assertThat(object.getLinks().get(1).getIconSrc(), equalTo(new MediaSRC("/media/link/britannica.png")));
	assertThat(object.getLinks().get(1).getUrl(), equalTo(new URL("https://www.britannica.com/art/bandonion")));

	assertThat(object.getRelated(), notNullValue());
	assertThat(object.getRelated(), not(empty()));
	assertThat(object.getRelated(), hasSize(2));
	assertThat(object.getRelated().get(0), equalTo("bandoneon"));
	assertThat(object.getRelated().get(1), equalTo("cimbalom"));
    }

    @Test
    public void serializeAtlasObject() {
	AtlasObject object = dao.getAtlasObject(1);
	Json json = Classes.loadService(Json.class);
	String value = json.stringify(object);
	System.out.println(value);
    }

    @Test
    public void getRelatedAtlasObjects() {
	List<AtlasItem> objects = dao.getRelatedAtlasObjects(1, Arrays.asList("accordion", "banjo"));
	assertThat(objects, notNullValue());
	assertThat(objects, hasSize(1));

	AtlasItem object = objects.get(0);
	assertThat(object, notNullValue());
	assertThat(object, not(instanceOf(AtlasObject.class)));
	assertThat(object.getId(), equalTo(1));
	assertThat(object.getRepositoryName(), equalTo("instrument"));
	assertThat(object.getName(), equalTo("accordion"));
	assertThat(object.getDisplay(), equalTo("Accordion"));
	assertThat(object.getIconName(), equalTo("icon.jpg"));
	assertThat(object.getIconSrc(), equalTo(src("accordion", "icon.jpg")));
    }

    @Test
    public void getRelatedAtlasObjects_EmptyNames() {
	List<String> emptyNames = new ArrayList<>(0);
	List<AtlasItem> objects = dao.getRelatedAtlasObjects(1, emptyNames);
	assertThat(objects, notNullValue());
	assertThat(objects, empty());
    }

    @Test
    public void getAtlasObject_ObjectState() {
	AtlasObject object = dao.getAtlasObject(1);
	assertThat(object.getState(), equalTo(AtlasObject.State.DEVELOPMENT));

	object = dao.getAtlasObject(3);
	assertThat(object.getState(), equalTo(AtlasObject.State.PUBLISHED));
    }

    @Test
    public void getAtlasObject_FeaturesOrder() {
	AtlasObject object = dao.getAtlasObject(1);
	assertThat(object.getFeatures(), notNullValue());
	assertThat(object.getFeatures(), hasSize(2));
	assertThat(object.getFeatures().get(0).getName(), equalTo("height"));
	assertThat(object.getFeatures().get(1).getName(), equalTo("width"));
    }

    @Test
    public void getImageByKey() {
	Image image = dao.getImageByKey(1, "icon");
	assertThat(image, notNullValue());
	assertThat(image.getImageKey(), equalTo("icon"));
	assertThat(image.getSource(),
		equalTo("https://upload.wikimedia.org/wikipedia/commons/f/f5/Paris_-_Accordion_Player_-_0956.jpg"));
	assertThat(image.getFileName(), equalTo("icon.jpg"));
	assertThat(image.getFileSize(), equalTo(12345));
	assertThat(image.getWidth(), equalTo(96));
	assertThat(image.getHeight(), equalTo(96));
    }

    @Test
    public void getNextAtlasObject() throws MalformedURLException {
	AtlasObject object = dao.getNextAtlasObject(0);
	assertThat(object, notNullValue());
	assertAtlasObject(object);

	object = dao.getNextAtlasObject(object.getId());
	assertThat(object.getName(), equalTo("eagle"));

	object = dao.getNextAtlasObject(object.getId());
	assertThat(object.getName(), equalTo("bandoneon"));

	object = dao.getNextAtlasObject(object.getId());
	assertThat(object.getName(), equalTo("cimbalom"));

	object = dao.getNextAtlasObject(object.getId());
	assertThat(object, nullValue());
    }

    @Test
    public void getReleaseById() {
	Release release = dao.getReleaseById(1);
	assertThat(release, notNullValue());
    }

    @Test
    public void getReleaseParentById() {
	ReleaseParent release = dao.getReleaseParentById(1);
	assertThat(release, notNullValue());

	assertThat(release.getChildren(), notNullValue());
	assertThat(release.getChildren(), hasSize(2));
	assertThat(release.getChildren().get(0).getId(), equalTo(1));
	assertThat(release.getChildren().get(1).getId(), equalTo(2));
    }

    @Test
    public void getReleaseItems() {
	List<AtlasItem> items = dao.getReleaseItems(1);
	assertThat(items, notNullValue());
	assertThat(items, hasSize(2));
	assertThat(items.get(0).getName(), equalTo("accordion"));
	assertThat(items.get(1).getName(), equalTo("eagle"));
    }

    @Test
    public void getLinkSourceByDomain() {
	LinkSource source = dao.getLinkSourceByDomain(1, "wikipedia.org");
	assertThat(source, notNullValue());
    }

    @Test
    public void getExternalSourceCandidates() {
	List<ExternalSource> candidates = dao.getExternalSourceCandidates(Arrays.asList(1, 3));
	assertThat(candidates, notNullValue());
	assertThat(candidates, hasSize(1));
	assertThat(candidates.get(0).getId(), equalTo(2));
    }

    @Test
    public void getFeaturesMeta() {
	List<FeatureMeta> featuresMeta = dao.getFeaturesMeta(Arrays.asList(1, 3));
	assertThat(featuresMeta, notNullValue());
	assertThat(featuresMeta, hasSize(2));

	assertThat(featuresMeta.get(0).getId(), equalTo(4));
	assertThat(featuresMeta.get(1).getId(), equalTo(2));

	assertThat(featuresMeta.get(0).getName(), equalTo("weight"));
	assertThat(featuresMeta.get(1).getName(), equalTo("width"));

	FeatureMeta featureMeta = featuresMeta.get(0);
	assertThat(featureMeta, notNullValue());
	assertThat(featureMeta.getId(), equalTo(4));
	assertThat(featureMeta.getName(), equalTo("weight"));
	assertThat(featureMeta.getDisplay(), equalTo("Weight"));
	assertThat(featureMeta.getDefinition(), equalTo("weight"));
	assertThat(featureMeta.getQuantity(), equalTo(PhysicalQuantity.MASS));
    }

    /**
     * If excludes list is empty returns all features meta from database, in name
     * order.
     */
    @Test
    public void getFeaturesMeta_EmptyExcludes() {
	List<FeatureMeta> featuresMeta = dao.getFeaturesMeta(new ArrayList<Integer>());
	assertThat(featuresMeta, notNullValue());
	assertThat(featuresMeta, hasSize(4));

	assertThat(featuresMeta.get(0).getId(), equalTo(1));
	assertThat(featuresMeta.get(1).getId(), equalTo(3));
	assertThat(featuresMeta.get(2).getId(), equalTo(4));
	assertThat(featuresMeta.get(3).getId(), equalTo(2));

	assertThat(featuresMeta.get(0).getName(), equalTo("height"));
	assertThat(featuresMeta.get(1).getName(), equalTo("length"));
	assertThat(featuresMeta.get(2).getName(), equalTo("weight"));
	assertThat(featuresMeta.get(3).getName(), equalTo("width"));

	FeatureMeta featureMeta = featuresMeta.get(0);
	assertThat(featureMeta, notNullValue());
	assertThat(featureMeta.getId(), equalTo(1));
	assertThat(featureMeta.getName(), equalTo("height"));
	assertThat(featureMeta.getDisplay(), equalTo("Height"));
	assertThat(featureMeta.getDefinition(), equalTo("height"));
	assertThat(featureMeta.getQuantity(), equalTo(PhysicalQuantity.LENGTH));
    }

    @Test
    public void searchFeaturesMeta() {
	List<FeatureMeta> featuresMeta = dao.searchFeaturesMeta("eig", Arrays.asList(1));
	assertThat(featuresMeta, notNullValue());
	assertThat(featuresMeta, hasSize(1));

	assertThat(featuresMeta.get(0).getId(), equalTo(4));
	assertThat(featuresMeta.get(0).getName(), equalTo("weight"));

	FeatureMeta featureMeta = featuresMeta.get(0);
	assertThat(featureMeta, notNullValue());
	assertThat(featureMeta.getId(), equalTo(4));
	assertThat(featureMeta.getName(), equalTo("weight"));
	assertThat(featureMeta.getDisplay(), equalTo("Weight"));
	assertThat(featureMeta.getDefinition(), equalTo("weight"));
	assertThat(featureMeta.getQuantity(), equalTo(PhysicalQuantity.MASS));
    }

    @Test
    public void searchFeaturesMeta_EmptyExcludes() {
	List<FeatureMeta> featuresMeta = dao.searchFeaturesMeta("eig", new ArrayList<Integer>());
	assertThat(featuresMeta, notNullValue());
	assertThat(featuresMeta, hasSize(2));

	assertThat(featuresMeta.get(0).getId(), equalTo(1));
	assertThat(featuresMeta.get(1).getId(), equalTo(4));

	assertThat(featuresMeta.get(0).getName(), equalTo("height"));
	assertThat(featuresMeta.get(1).getName(), equalTo("weight"));

	FeatureMeta featureMeta = featuresMeta.get(0);
	assertThat(featureMeta, notNullValue());
	assertThat(featureMeta.getId(), equalTo(1));
	assertThat(featureMeta.getName(), equalTo("height"));
	assertThat(featureMeta.getDisplay(), equalTo("Height"));
	assertThat(featureMeta.getDefinition(), equalTo("height"));
	assertThat(featureMeta.getQuantity(), equalTo(PhysicalQuantity.LENGTH));
    }

    /**
     * If search criterion is null and excludes list is empty returns all features
     * meta from database, in name order.
     */
    @Test
    public void searchFeaturesMeta_NullSearchEmptyExcludes() {
	List<FeatureMeta> featuresMeta = dao.searchFeaturesMeta(null, new ArrayList<Integer>());
	assertThat(featuresMeta, notNullValue());
	assertThat(featuresMeta, hasSize(4));

	assertThat(featuresMeta.get(0).getId(), equalTo(1));
	assertThat(featuresMeta.get(1).getId(), equalTo(3));
	assertThat(featuresMeta.get(2).getId(), equalTo(4));
	assertThat(featuresMeta.get(3).getId(), equalTo(2));

	assertThat(featuresMeta.get(0).getName(), equalTo("height"));
	assertThat(featuresMeta.get(1).getName(), equalTo("length"));
	assertThat(featuresMeta.get(2).getName(), equalTo("weight"));
	assertThat(featuresMeta.get(3).getName(), equalTo("width"));

	FeatureMeta featureMeta = featuresMeta.get(0);
	assertThat(featureMeta, notNullValue());
	assertThat(featureMeta.getId(), equalTo(1));
	assertThat(featureMeta.getName(), equalTo("height"));
	assertThat(featureMeta.getDisplay(), equalTo("Height"));
	assertThat(featureMeta.getDefinition(), equalTo("height"));
	assertThat(featureMeta.getQuantity(), equalTo(PhysicalQuantity.LENGTH));
    }

    // ----------------------------------------------------------------------------------------------

    private static MediaSRC src(String objectName, String mediaFile) {
	return Files.mediaSrc("instrument", objectName, mediaFile);
    }
}
