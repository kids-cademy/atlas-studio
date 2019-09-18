package com.kidscademy.atlas.studio.it;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
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
import com.kidscademy.atlas.studio.model.AtlasItem;
import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.HDate;
import com.kidscademy.atlas.studio.model.Image;
import com.kidscademy.atlas.studio.model.Link;
import com.kidscademy.atlas.studio.model.MediaSRC;
import com.kidscademy.atlas.studio.model.Region;
import com.kidscademy.atlas.studio.model.Taxon;
import com.kidscademy.atlas.studio.model.TaxonomyClass;
import com.kidscademy.atlas.studio.util.Files;

import js.json.Json;
import js.transaction.TransactionFactory;
import js.transaction.eclipselink.TransactionFactoryImpl;
import js.unit.db.Database;
import js.util.Classes;

public class AtlasDaoTest {
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
    public void getAtlasItem() {
	AtlasItem atlasItem = dao.getAtlasItem(1);
	assertAtlasItem(atlasItem);
    }

    private void assertAtlasItem(AtlasItem atlasItem) {
	assertThat(atlasItem, notNullValue());
	assertThat(atlasItem.getId(), equalTo(1));

	AtlasCollection collection = atlasItem.getCollection();
	assertThat(collection, notNullValue());
	assertThat(collection.getId(), equalTo(1));
	assertThat(collection.getName(), equalTo("instrument"));
	assertThat(collection.getDisplay(), equalTo("Instrument"));
	assertThat(collection.getIconName(), equalTo("instrument.png"));
	assertThat(collection.getIconSrc(), equalTo(Files.collectionSrc("instrument.png")));

	assertThat(atlasItem.getRepositoryName(), equalTo("instrument"));
	assertThat(atlasItem.getName(), equalTo("accordion"));
	assertThat(atlasItem.getDisplay(), equalTo("Accordion"));
	assertThat(atlasItem.getIconName(), equalTo("icon.jpg"));
	assertThat(atlasItem.getIconSrc(), equalTo(src("accordion", "icon_96x96.jpg")));
    }

    @Test
    public void getAtlasObject() throws MalformedURLException {
	AtlasObject object = dao.getAtlasObject(1);
	assertAtlasObject(object);
    }

    /**
     * When create new object with not null media source values, media files fields
     * should initialized before actual database persist.
     */
    @Test
    public void prePersistObject() {
	AtlasObject object = new AtlasObject();
	object.setCollection(new AtlasCollection(1, "instrument"));
	object.setState(AtlasObject.State.DEVELOPMENT);
	object.setRank(9999);
	object.setName("banjo");
	object.setDisplay("Banjo");

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
	AtlasObject object = new AtlasObject();
	object.setCollection(new AtlasCollection(1, "instrument"));
	object.setState(AtlasObject.State.DEVELOPMENT);
	object.setRank(9999);
	object.setName("banjo");
	object.setDisplay("Banjo");

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
    public void postLoadAtlasObjectt() {
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
	List<AtlasItem> items = dao.getCollectionItems(1);
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

    private static void assertAtlasObject(AtlasObject object) throws MalformedURLException {
	assertThat(object, notNullValue());
	assertThat(object.getId(), equalTo(1));

	assertThat(object.getCollection(), notNullValue());
	assertThat(object.getCollection().getId(), equalTo(1));
	assertThat(object.getCollection().getName(), equalTo("instrument"));
	assertThat(object.getCollection().getDisplay(), equalTo("Instrument"));
	assertThat(object.getCollection().getIconName(), equalTo("instrument.png"));
	assertThat(object.getCollection().getTaxonomyClass(), equalTo(TaxonomyClass.MUSICAL_INSTRUMENT));

	assertThat(object.getRank(), equalTo(1234));
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

	assertThat(object.getStartDate().getValue(), equalTo(1234567890L));
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
	assertThat(object.getFeatures().keySet(), not(empty()));
	assertThat(object.getFeatures().keySet(), hasSize(2));
	assertThat(object.getFeatures().get("Feature #1"), equalTo("Feature #1 value."));
	assertThat(object.getFeatures().get("Feature #2"), equalTo("Feature #2 value."));

	assertThat(object.getLinks(), notNullValue());
	assertThat(object.getLinks(), hasSize(1));

	Link link = object.getLinks().get(0);
	assertThat(link, notNullValue());
	assertThat(link.getDisplay(), equalTo("Wikipedia"));
	assertThat(link.getIconName(), equalTo("wikipedia.png"));
	assertThat(link.getIconSrc(), equalTo(new MediaSRC("/media/link/wikipedia.png")));
	assertThat(link.getUrl(), equalTo(new URL("https://en.wikipedia.org/wiki/Accordion")));

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
    public void saveAtlasObject() throws MalformedURLException {
	AtlasObject object = new AtlasObject();
	object.setCollection(new AtlasCollection(1, "instrument"));

	object.setState(AtlasObject.State.DEVELOPMENT);
	object.setRank(9999);
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

	List<Link> links = new ArrayList<>();
	links.add(new Link(new URL("https://en.wikipedia.org/wiki/Accordion"), "Wikipedia", "Wikipedia article",
		new MediaSRC("/media/link/wikipedia.png")));
	links.add(new Link(new URL("https://www.youtube.com/watch?v=kXXhp_bZvck"), "YouTube", "YouTube video",
		new MediaSRC("/media/link/youtube.png")));
	object.setLinks(links);

	Map<String, String> facts = new HashMap<>();
	facts.put("Banjo Fact #1", "Banjo fact #1 description.");
	facts.put("Banjo Fact #2", "Banjo fact #2 description.");
	object.setFacts(facts);

	Map<String, String> features = new HashMap<>();
	features.put("Banjo Feature #1", "Banjo feature #1 value");
	features.put("Banjo Feature #2", "Banjo feature #2 value");
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
	assertThat(expected.getCollection().getDisplay(), equalTo("Instrument"));
	assertThat(expected.getCollection().getIconName(), equalTo("instrument.png"));

	assertThat(expected.getRank(), equalTo(9999));
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

	assertThat(expected.getStartDate().getValue(), equalTo(1821L));
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
	assertThat(expected.getSpreading().get(0).getName(), equalTo("Africa"));
	assertThat(expected.getSpreading().get(0).getArea(), equalTo(Region.Area.NORTH));
	assertThat(expected.getSpreading().get(1), notNullValue());
	assertThat(expected.getSpreading().get(1).getName(), equalTo("Europe"));
	assertThat(expected.getSpreading().get(1).getArea(), equalTo(Region.Area.SOUTH));

	assertThat(expected.getFacts(), notNullValue());
	assertThat(expected.getFacts().keySet(), not(empty()));
	assertThat(expected.getFacts().keySet(), hasSize(2));
	assertThat(expected.getFacts().get("Banjo Fact #1"), equalTo("Banjo fact #1 description."));
	assertThat(expected.getFacts().get("Banjo Fact #2"), equalTo("Banjo fact #2 description."));

	assertThat(expected.getFeatures(), notNullValue());
	assertThat(expected.getFeatures().keySet(), not(empty()));
	assertThat(expected.getFeatures().keySet(), hasSize(2));
	assertThat(expected.getFeatures().get("Banjo Feature #1"), equalTo("Banjo feature #1 value"));
	assertThat(expected.getFeatures().get("Banjo Feature #2"), equalTo("Banjo feature #2 value"));

	assertThat(expected.getLinks(), notNullValue());
	assertThat(expected.getLinks(), hasSize(2));

	Link link = expected.getLinks().get(0);
	assertThat(link, notNullValue());
	assertThat(link.getDisplay(), equalTo("Wikipedia"));
	assertThat(link.getIconName(), equalTo("wikipedia.png"));
	assertThat(link.getIconSrc(), equalTo(new MediaSRC("/media/link/wikipedia.png")));
	assertThat(link.getUrl(), equalTo(new URL("https://en.wikipedia.org/wiki/Accordion")));

	link = expected.getLinks().get(1);
	assertThat(link, notNullValue());
	assertThat(link.getDisplay(), equalTo("YouTube"));
	assertThat(link.getIconName(), equalTo("youtube.png"));
	assertThat(link.getIconSrc(), equalTo(new MediaSRC("/media/link/youtube.png")));
	assertThat(link.getUrl(), equalTo(new URL("https://www.youtube.com/watch?v=kXXhp_bZvck")));

	assertThat(expected.getRelated(), notNullValue());
	assertThat(expected.getRelated(), not(empty()));
	assertThat(expected.getRelated(), hasSize(1));
	assertThat(expected.getRelated().get(0), equalTo("accordion"));
    }

    @Test
    public void findObjectsByName() {
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
	assertThat(object.getIconSrc(), equalTo(src("accordion", "icon_96x96.jpg")));
    }

    @Test
    public void findObjectsByName_EmptyNames() {
	List<String> emptyNames = new ArrayList<>(0);
	List<AtlasItem> objects = dao.getRelatedAtlasObjects(1, emptyNames);
	assertThat(objects, notNullValue());
	assertThat(objects, empty());
    }

    @Test
    public void objectState() {
	AtlasObject object = dao.getAtlasObject(1);
	assertThat(object.getState(), equalTo(AtlasObject.State.DEVELOPMENT));

	object = dao.getAtlasObject(3);
	assertThat(object.getState(), equalTo(AtlasObject.State.PUBLISHED));
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

    // ----------------------------------------------------------------------------------------------

    private static MediaSRC src(String objectName, String mediaFile) {
	return Files.mediaSrc("instrument", objectName, mediaFile);
    }
}
