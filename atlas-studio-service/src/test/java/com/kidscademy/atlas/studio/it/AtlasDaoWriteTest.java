package com.kidscademy.atlas.studio.it;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.dao.AtlasDaoImpl;
import com.kidscademy.atlas.studio.model.AtlasCollection;
import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.DescriptionMeta;
import com.kidscademy.atlas.studio.model.ExternalSource;
import com.kidscademy.atlas.studio.model.Feature;
import com.kidscademy.atlas.studio.model.FeatureMeta;
import com.kidscademy.atlas.studio.model.HDate;
import com.kidscademy.atlas.studio.model.Image;
import com.kidscademy.atlas.studio.model.Link;
import com.kidscademy.atlas.studio.model.LinkSource;
import com.kidscademy.atlas.studio.model.MediaSRC;
import com.kidscademy.atlas.studio.model.PhysicalQuantity;
import com.kidscademy.atlas.studio.model.Region;
import com.kidscademy.atlas.studio.model.ReleaseParent;
import com.kidscademy.atlas.studio.model.Taxon;
import com.kidscademy.atlas.studio.model.TaxonMeta;
import com.kidscademy.atlas.studio.util.Files;

import js.transaction.TransactionFactory;
import js.transaction.eclipselink.TransactionFactoryImpl;
import js.unit.db.Database;
import js.util.Classes;

public class AtlasDaoWriteTest {
    private static Database database;
    private static TransactionFactory factory;

    @BeforeClass
    public static void beforeClass() throws IOException {
	database = new Database("atlas_test", "kids_cademy", "kids_cademy");
	factory = new TransactionFactoryImpl();
    }

    private AtlasDao dao;

    @Before
    public void beforeTest() throws SQLException {
	database.load(Classes.getResourceAsStream("atlas-data-set.xml"));
	dao = factory.newInstance(AtlasDaoImpl.class);
    }

    @Test
    public void saveAtlasCollection() {
	AtlasCollection collection = new AtlasCollection(3, "farm-animals");
	collection.setDisplay("Farm Animals");
	collection.setDefinition("Domesticated animals, both mammals and birds.");

	List<DescriptionMeta> descriptionMeta = new ArrayList<>();
	descriptionMeta.add(new DescriptionMeta("description", "Morphological description."));
	descriptionMeta.add(new DescriptionMeta("habitat", "Bird natural habitat."));
	descriptionMeta.add(new DescriptionMeta("feeding", "Feeding behavior and diet."));
	collection.setDescriptionMeta(descriptionMeta);

	List<TaxonMeta> taxonomyMeta = new ArrayList<>();
	taxonomyMeta.add(new TaxonMeta("kingdom", "Animalia"));
	taxonomyMeta.add(new TaxonMeta("phylum", "Chordata"));
	taxonomyMeta.add(new TaxonMeta("class"));
	collection.setTaxonomyMeta(taxonomyMeta);

	List<FeatureMeta> featuresMeta = new ArrayList<>();
	featuresMeta.add(new FeatureMeta(5, PhysicalQuantity.LENGTH, "depth", "Diving Depth",
		"Depth range, measured from sea level, where a particular family of objects can be found or operate."));
	featuresMeta.add(new FeatureMeta(6, PhysicalQuantity.ANGLE, "axial.tilt", "Axial Tilt",
		"In astronomy, axial tilt is the angle between an object's rotational axis and its orbital axis, or, equivalently, the angle between its equatorial plane and orbital plane."));
	featuresMeta.add(new FeatureMeta(7, PhysicalQuantity.TIME, "lifespan", "Lifespan",
		"Average life expectancy observed on a particular species."));
	collection.setFeaturesMeta(featuresMeta);

	List<LinkSource> linkSources = new ArrayList<>();
	linkSources.add(new LinkSource(3, dao.getExternalSourceById(1)));
	linkSources.add(new LinkSource(4, dao.getExternalSourceById(2)));
	linkSources.add(new LinkSource(5, dao.getExternalSourceById(3)));
	collection.setLinkSources(linkSources);

	dao.saveAtlasCollection(collection);
    }

    @Test
    public void saveAtlasCollection_UpdateLinkSource() {
	AtlasCollection collection = dao.getCollectionById(1);
	assertThat(collection, notNullValue());
	
	List<LinkSource> linkSources = collection.getLinkSources();
	assertThat(linkSources, notNullValue());
	assertThat(linkSources, hasSize(2));
	
	LinkSource linkSource = linkSources.get(0);
	assertThat(linkSource, notNullValue());
	assertThat(linkSource.getApis(), equalTo("definition,description,taxonomy"));
	
	linkSource.setApis("definition,description,features,taxonomy");
	dao.saveAtlasCollection(collection);
    }

    /**
     * When create new object with not null media source values, media files fields
     * should initialized before actual database persist.
     */
    @Test
    public void prePersistObject() {
	AtlasObject object = new AtlasObject(new AtlasCollection(1, "instrument"));
	object.setState(AtlasObject.State.DEVELOPMENT);
	object.setName("banjo");
	object.setDisplay("Banjo");
	object.setDefinition("Definition");

	Map<String, Image> pictures = new HashMap<>();
	object.setImages(pictures);
	pictures.put("icon", new Image(src("banjo", "icon.jpg")));
	pictures.put("cover", new Image(src("banjo", "cover.png")));
	pictures.put("contextual", new Image(src("banjo", "contextual.jpg")));

	object.setSampleSrc(src("banjo", "sample.mp3"));
	object.setWaveformSrc(src("banjo", "waveform.png"));

	dao.saveAtlasObject(object);

	AtlasObject persistedObject = dao.getAtlasObject(object.getId());

	assertThat(persistedObject.getImages().get("icon").getFileName(), equalTo("icon.jpg"));
	assertThat(persistedObject.getImages().get("cover").getFileName(), equalTo("cover.png"));
	assertThat(persistedObject.getImages().get("contextual").getFileName(), equalTo("contextual.jpg"));

	assertThat(persistedObject.getSampleName(), equalTo("sample.mp3"));
	assertThat(persistedObject.getWaveformName(), equalTo("waveform.png"));
    }

    /**
     * Create an UI object that is detached from persistence context and set SRC for
     * all media files. On DAO save {@link EntityManager#merge()} is enacted. DAO
     * logic should invoke {@link AtlasObject#postMerge(AtlasObject)} and update
     * media file names from already set SRC values.
     */
    @Test
    public void postMergeAtlasObject() {
	AtlasObject object = new AtlasObject(new AtlasCollection(1, "instrument"));
	object.setState(AtlasObject.State.DEVELOPMENT);
	object.setName("banjo");
	object.setDisplay("Banjo");
	object.setDefinition("Definition");

	Map<String, Image> images = new HashMap<>();
	object.setImages(images);
	images.put("icon", new Image(src("banjo", "icon.jpg")));
	images.put("cover", new Image(src("banjo", "cover.png")));
	images.put("featured", new Image(src("banjo", "featured.png")));
	images.put("contextual", new Image(src("banjo", "contextual.jpg")));

	dao.saveAtlasObject(object);

	AtlasObject dbObject = dao.getAtlasObject(object.getId());
	assertThat(dbObject.getSampleName(), nullValue());
	assertThat(dbObject.getWaveformName(), nullValue());

	object.setSampleSrc(src("banjo", "sample.mp3"));
	object.setWaveformSrc(src("banjo", "waveform.png"));
	// database merge is triggered on DAO because object ID is not zero
	assertThat(object.getId(), not(equalTo(0)));
	dao.saveAtlasObject(object);

	dbObject = dao.getAtlasObject(object.getId());

	assertThat(dbObject.getImage("icon").getFileName(), equalTo("icon.jpg"));
	assertThat(dbObject.getImage("cover").getFileName(), equalTo("cover.png"));
	assertThat(dbObject.getImage("featured").getFileName(), equalTo("featured.png"));
	assertThat(dbObject.getImage("contextual").getFileName(), equalTo("contextual.jpg"));

	assertThat(dbObject.getSampleName(), equalTo("sample.mp3"));
	assertThat(dbObject.getWaveformName(), equalTo("waveform.png"));
    }

    @Test
    public void saveAtlasObject() throws MalformedURLException {
	AtlasObject object = new AtlasObject(new AtlasCollection(1, "instrument"));

	object.setState(AtlasObject.State.DEVELOPMENT);
	object.setName("banjo");
	object.setDisplay("Banjo");
	object.setDefinition("Banjo definition.");
	object.setDescription("Banjo description.");
	object.setSampleTitle("Banjo Solo");
	// sample and waveform name are created from respective SRC on preSave hook
	object.setSampleSrc(src("banjo", "sample.mp3"));
	object.setWaveformSrc(src("banjo", "waveform.png"));

	Map<String, Image> images = new HashMap<>();
	object.setImages(images);
	images.put("icon", new Image(src("banjo", "icon.jpg")));
	images.put("cover", new Image(src("banjo", "cover.png")));
	images.put("contextual", new Image(src("banjo", "contextual.jpg")));
	images.put("featured", new Image(src("banjo", "featured.png")));

	List<Taxon> taxonomy = new ArrayList<Taxon>();
	taxonomy.add(new Taxon("Classification", "STRINGS"));
	object.setTaxonomy(taxonomy);

	List<String> aliases = new ArrayList<String>();
	aliases.add("Banjo Alias #1");
	aliases.add("Banjo Alias #2");
	object.setAliases(aliases);

	List<Region> spreading = new ArrayList<Region>();
	spreading.add(new Region("Europe", Region.Area.SOUTH));
	spreading.add(new Region("Africa", Region.Area.NORTH));
	object.setSpreading(spreading);

	object.setStartDate(new HDate(1821, HDate.Format.YEAR, HDate.Period.MIDDLE));

	ExternalSource externalSource = new ExternalSource(1, "https://en.wikipedia.org/wiki/",
		"Wikipedia article about ${display}", "definition,description,features,taxonomy");
	List<Link> links = new ArrayList<>();
	links.add(new Link(new LinkSource(1, externalSource), new URL("https://en.wikipedia.org/wiki/Accordion"),
		"Accordion"));
	links.add(new Link(new LinkSource(2, externalSource), new URL("https://www.youtube.com/watch?v=kXXhp_bZvck"),
		"Accordion"));
	object.setLinks(links);

	Map<String, String> facts = new HashMap<>();
	facts.put("Banjo Fact #1", "Banjo fact #1 description.");
	facts.put("Banjo Fact #2", "Banjo fact #2 description.");
	object.setFacts(facts);

	List<Feature> features = new ArrayList<>();
	features.add(new Feature(dao.getFeatureMetaById(1), 0.3556, 0.4826));
	features.add(new Feature(dao.getFeatureMetaById(2), 0.4826));
	object.setFeatures(features);

	List<String> related = new ArrayList<>();
	related.add("accordion");
	object.setRelated(related);

	assertThat(object.getId(), equalTo(0));
	dao.saveAtlasObject(object);
	assertThat(object.getId(), not(equalTo(0)));

	AtlasObject expected = dao.getAtlasObject(object.getId());
	assertThat(expected, notNullValue());
	assertThat(expected.getId(), not(equalTo(0)));

	assertThat(expected.getCollection(), notNullValue());
	assertThat(expected.getCollection().getId(), equalTo(1));
	assertThat(expected.getCollection().getName(), equalTo("instrument"));

	assertThat(expected.getState(), equalTo(AtlasObject.State.DEVELOPMENT));
	assertThat(expected.getName(), equalTo("banjo"));
	assertThat(expected.getDisplay(), equalTo("Banjo"));
	assertThat(expected.getDefinition(), equalTo("Banjo definition."));
	assertThat(expected.getDescription(), equalTo("Banjo description."));

	assertThat(expected.getImages(), notNullValue());
	assertThat(expected.getImages().size(), equalTo(4));
	assertThat(expected.getImage("icon").getFileName(), equalTo("icon.jpg"));
	assertThat(expected.getImage("icon").getSrc(), equalTo(src("banjo", "icon.jpg")));
	assertThat(expected.getImage("contextual").getFileName(), equalTo("contextual.jpg"));
	assertThat(expected.getImage("contextual").getSrc(), equalTo(src("banjo", "contextual.jpg")));
	assertThat(expected.getImage("cover").getFileName(), equalTo("cover.png"));
	assertThat(expected.getImage("cover").getSrc(), equalTo(src("banjo", "cover.png")));
	assertThat(expected.getImage("featured").getFileName(), equalTo("featured.png"));
	assertThat(expected.getImage("featured").getSrc(), equalTo(src("banjo", "featured.png")));

	assertThat(expected.getTaxonomy(), notNullValue());
	assertThat(expected.getTaxonomy(), not(empty()));
	assertThat(expected.getTaxonomy(), hasSize(1));
	assertThat(expected.getTaxonomy().get(0).getName(), equalTo("Classification"));
	assertThat(expected.getTaxonomy().get(0).getValue(), equalTo("STRINGS"));

	assertThat(expected.getSampleTitle(), equalTo("Banjo Solo"));
	assertThat(expected.getSampleName(), equalTo("sample.mp3"));
	assertThat(expected.getWaveformName(), equalTo("waveform.png"));
	assertThat(expected.getSampleSrc(), equalTo(src("banjo", "sample.mp3")));
	assertThat(expected.getWaveformSrc(), equalTo(src("banjo", "waveform.png")));

	assertThat(expected.getStartDate().getValue(), equalTo(1821.0));
	assertThat(expected.getStartDate().getFormat(), equalTo(HDate.Format.YEAR));
	assertThat(expected.getStartDate().getPeriod(), equalTo(HDate.Period.MIDDLE));
	assertThat(expected.getStartDate().getEra(), equalTo(HDate.Era.CE));

	assertThat(expected.getAliases(), notNullValue());
	assertThat(expected.getAliases(), hasSize(2));
	assertThat(expected.getAliases().get(0), equalTo("Banjo Alias #1"));
	assertThat(expected.getAliases().get(1), equalTo("Banjo Alias #2"));

	assertThat(expected.getSpreading(), notNullValue());
	assertThat(expected.getSpreading(), hasSize(2));
	assertThat(expected.getSpreading().get(0), notNullValue());
	assertThat(expected.getSpreading().get(0).getName(), equalTo("Europe"));
	assertThat(expected.getSpreading().get(0).getArea(), equalTo(Region.Area.SOUTH));
	assertThat(expected.getSpreading().get(1), notNullValue());
	assertThat(expected.getSpreading().get(1).getName(), equalTo("Africa"));
	assertThat(expected.getSpreading().get(1).getArea(), equalTo(Region.Area.NORTH));

	assertThat(expected.getFacts(), notNullValue());
	assertThat(expected.getFacts().keySet(), not(empty()));
	assertThat(expected.getFacts().keySet(), hasSize(2));
	assertThat(expected.getFacts().get("Banjo Fact #1"), equalTo("Banjo fact #1 description."));
	assertThat(expected.getFacts().get("Banjo Fact #2"), equalTo("Banjo fact #2 description."));

	assertThat(object.getFeatures(), notNullValue());
	assertThat(object.getFeatures(), not(empty()));
	assertThat(object.getFeatures(), hasSize(2));
	assertThat(object.getFeatures().get(0).getName(), equalTo("height"));
	assertThat(object.getFeatures().get(0).getValue(), equalTo(0.3556));
	assertThat(object.getFeatures().get(0).getMaximum(), equalTo(0.4826));
	assertThat(object.getFeatures().get(0).getQuantity(), equalTo(PhysicalQuantity.LENGTH));
	assertThat(object.getFeatures().get(1).getName(), equalTo("width"));
	assertThat(object.getFeatures().get(1).getValue(), equalTo(0.4826));
	assertThat(object.getFeatures().get(1).getMaximum(), nullValue());
	assertThat(object.getFeatures().get(1).getQuantity(), equalTo(PhysicalQuantity.LENGTH));

	assertThat(expected.getLinks(), notNullValue());
	assertThat(expected.getLinks(), hasSize(2));

	Link link = expected.getLinks().get(0);
	assertThat(link, notNullValue());
	assertThat(link.toDisplay(), equalTo("Wikipedia"));
	assertThat(link.getIconSrc(), equalTo(new MediaSRC("/media/link/wikipedia.png")));
	assertThat(link.getUrl(), equalTo(new URL("https://en.wikipedia.org/wiki/Accordion")));

	link = expected.getLinks().get(1);
	assertThat(link, notNullValue());
	assertThat(link.toDisplay(), equalTo("Britannica"));
	assertThat(link.getIconSrc(), equalTo(new MediaSRC("/media/link/britannica.png")));
	assertThat(link.getUrl(), equalTo(new URL("https://www.youtube.com/watch?v=kXXhp_bZvck")));

	assertThat(expected.getRelated(), notNullValue());
	assertThat(expected.getRelated(), not(empty()));
	assertThat(expected.getRelated(), hasSize(1));
	assertThat(expected.getRelated().get(0), equalTo("accordion"));
    }

    @Test
    public void saveAtlasObject_FeaturesReorder() {
	AtlasObject object = dao.getAtlasObject(1);
	assertThat(object.getFeatures(), notNullValue());
	assertThat(object.getFeatures(), hasSize(2));
	assertThat(object.getFeatures().get(0).getName(), equalTo("height"));
	assertThat(object.getFeatures().get(1).getName(), equalTo("width"));

	Feature feature = object.getFeatures().remove(0);
	object.getFeatures().add(feature);
	assertThat(object.getFeatures().get(0).getName(), equalTo("width"));
	assertThat(object.getFeatures().get(1).getName(), equalTo("height"));
	dao.saveAtlasObject(object);

	object = dao.getAtlasObject(1);
	assertThat(object.getFeatures(), notNullValue());
	assertThat(object.getFeatures(), hasSize(2));
	assertThat(object.getFeatures().get(0).getName(), equalTo("width"));
	assertThat(object.getFeatures().get(1).getName(), equalTo("height"));
    }

    @Test
    public void saveAtlasObject_LinksReorder() {
	AtlasObject object = dao.getAtlasObject(1);
	assertThat(object.getLinks(), notNullValue());
	assertThat(object.getLinks(), hasSize(2));
	assertThat(object.getLinks().get(0).getDomain(), equalTo("wikipedia.org"));
	assertThat(object.getLinks().get(1).getDomain(), equalTo("britannica.com"));

	Link link = object.getLinks().remove(0);
	object.getLinks().add(link);
	assertThat(object.getLinks().get(0).getDomain(), equalTo("britannica.com"));
	assertThat(object.getLinks().get(1).getDomain(), equalTo("wikipedia.org"));
	dao.saveAtlasObject(object);

	object = dao.getAtlasObject(1);
	assertThat(object.getLinks(), notNullValue());
	assertThat(object.getLinks(), hasSize(2));
	assertThat(object.getLinks().get(0).getDomain(), equalTo("britannica.com"));
	assertThat(object.getLinks().get(1).getDomain(), equalTo("wikipedia.org"));
    }

    @Test
    public void saveAtlasObject_RelatedReorder() {
	AtlasObject object = dao.getAtlasObject(1);
	assertThat(object.getRelated(), notNullValue());
	assertThat(object.getRelated(), hasSize(2));
	assertThat(object.getRelated().get(0), equalTo("bandoneon"));
	assertThat(object.getRelated().get(1), equalTo("cimbalom"));

	String feature = object.getRelated().remove(0);
	object.getRelated().add(feature);
	assertThat(object.getRelated().get(0), equalTo("cimbalom"));
	assertThat(object.getRelated().get(1), equalTo("bandoneon"));
	dao.saveAtlasObject(object);

	object = dao.getAtlasObject(1);
	assertThat(object.getRelated(), notNullValue());
	assertThat(object.getRelated(), hasSize(2));
	assertThat(object.getRelated().get(0), equalTo("cimbalom"));
	assertThat(object.getRelated().get(1), equalTo("bandoneon"));
    }

    @Test
    public void saveAtlasObject_Rename() {
	// object #1 has relation with object #3
	AtlasObject object = dao.getAtlasObject(1);
	assertThat(object.getRelated(), notNullValue());
	assertThat(object.getRelated(), hasSize(2));
	assertThat(object.getRelated().get(0), equalTo("bandoneon"));

	// change name of object #3
	object = dao.getAtlasObject(3);
	assertThat(object.getRelated(), notNullValue());
	assertThat(object.getName(), equalTo("bandoneon"));

	object.setName("bandoneon-changed");
	dao.saveAtlasObject(object);

	// object #3 name should be changed
	object = dao.getAtlasObject(3);
	assertThat(object.getRelated(), notNullValue());
	assertThat(object.getName(), equalTo("bandoneon-changed"));

	// object #1 relation should be updated
	object = dao.getAtlasObject(1);
	assertThat(object.getRelated(), notNullValue());
	assertThat(object.getRelated(), hasSize(2));
	assertThat(object.getRelated().get(0), equalTo("bandoneon-changed"));
    }

    @Test
    public void resetObjectSample() {
	dao.resetObjectSample(1);

	AtlasObject object = dao.getAtlasObject(1);
	assertThat(object.getSampleTitle(), nullValue());
	assertThat(object.getSampleName(), nullValue());
	assertThat(object.getSampleSrc(), nullValue());
	assertThat(object.getWaveformName(), nullValue());
	assertThat(object.getWaveformSrc(), nullValue());
    }

    @Test
    public void addReleaseChild() {
	dao.addReleaseChild(1, 4);
	ReleaseParent release = dao.getReleaseParentById(1);
	assertThat(release, notNullValue());
	assertThat(release.getChildren(), notNullValue());
	assertThat(release.getChildren(), hasSize(3));
	assertThat(release.getChildren().get(0).getId(), equalTo(1));
	assertThat(release.getChildren().get(1).getId(), equalTo(2));
	assertThat(release.getChildren().get(2).getId(), equalTo(4));
    }

    @Test
    public void addReleaseChildren() {
	dao.addReleaseChildren(1, Arrays.asList(4, 3));
	ReleaseParent release = dao.getReleaseParentById(1);
	assertThat(release, notNullValue());
	assertThat(release.getChildren(), notNullValue());
	assertThat(release.getChildren(), hasSize(4));
	// surprisingly children are ordered by id instead of insertion order
	// perhaps because ReleaseChild has equals and hashCode dependent on id
	assertThat(release.getChildren().get(0).getId(), equalTo(1));
	assertThat(release.getChildren().get(1).getId(), equalTo(2));
	assertThat(release.getChildren().get(2).getId(), equalTo(3));
	assertThat(release.getChildren().get(3).getId(), equalTo(4));
    }

    @Test
    public void removeReleaseChild() {
	dao.removeReleaseChild(1, 1);
	ReleaseParent release = dao.getReleaseParentById(1);
	assertThat(release, notNullValue());
	assertThat(release.getChildren(), notNullValue());
	assertThat(release.getChildren(), hasSize(1));
	assertThat(release.getChildren().get(0).getId(), equalTo(2));
    }

    // ----------------------------------------------------------------------------------------------

    private static MediaSRC src(String objectName, String mediaFile) {
	return Files.mediaSrc("instrument", objectName, mediaFile);
    }
}
