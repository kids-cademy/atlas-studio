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
import java.util.Collections;
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
import com.kidscademy.atlas.studio.model.InstrumentCategory;
import com.kidscademy.atlas.studio.model.Link;
import com.kidscademy.atlas.studio.model.MediaSRC;
import com.kidscademy.atlas.studio.model.Region;
import com.kidscademy.atlas.studio.model.User;
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

	assertThat(atlasItem.getCollectionName(), equalTo("instrument"));
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
     * When create new instrument with not null media source values, media files
     * fields should initialized before actual database persist.
     */
    @Test
    public void prePersistObject() {
	AtlasObject object = new AtlasObject();
	object.setUser(new User(1));
	object.setCollection(new AtlasCollection(1, "instrument"));
	object.setState(AtlasObject.State.DEVELOPMENT);
	object.setRank(9999);
	object.setName("banjo");
	object.setDisplay("Banjo");

	List<Image> pictures = new ArrayList<>();
	object.setImages(pictures);
	pictures.add(new Image(src("banjo", "icon.jpg")));
	pictures.add(new Image(src("banjo", "thumbnail.png")));
	pictures.add(new Image(src("banjo", "picture.jpg")));

	object.setSampleSrc(src("banjo", "sample.mp3"));
	object.setWaveformSrc(src("banjo", "waveform.png"));

	dao.saveAtlasObject(object);

	AtlasObject persistedInstrument = dao.getAtlasObject(object.getId());

	assertThat(persistedInstrument.getImages().get(0).getFileName(), equalTo("icon.jpg"));
	assertThat(persistedInstrument.getImages().get(1).getFileName(), equalTo("thumbnail.png"));
	assertThat(persistedInstrument.getImages().get(2).getFileName(), equalTo("picture.jpg"));

	assertThat(persistedInstrument.getSampleName(), equalTo("sample.mp3"));
	assertThat(persistedInstrument.getWaveformName(), equalTo("waveform.png"));
    }

    /**
     * Create an UI object that is detached from persistence context and set SRC for
     * all media files. On DAO save {@link EntityManager#merge()} is enacted. DAO
     * logic should invoke {@link AtlasObject#postMerge(AtlasObject)} and update
     * media file names from already set SRC values.
     */
    @Test
    public void postMergeInstrument() {
	AtlasObject object = new AtlasObject();
	object.setUser(new User(1));
	object.setCollection(new AtlasCollection(1, "instrument"));
	object.setState(AtlasObject.State.DEVELOPMENT);
	object.setRank(9999);
	object.setName("banjo");
	object.setDisplay("Banjo");

	List<Image> pictures = new ArrayList<>();
	object.setImages(pictures);
	pictures.add(new Image(src("banjo", "icon.jpg")));
	pictures.add(new Image(src("banjo", "thumbnail.png")));
	pictures.add(new Image(src("banjo", "picture.jpg")));

	dao.saveAtlasObject(object);

	AtlasObject dbInstrument = dao.getAtlasObject(object.getId());
	assertThat(dbInstrument.getSampleName(), nullValue());
	assertThat(dbInstrument.getWaveformName(), nullValue());

	object.setSampleSrc(src("banjo", "sample.mp3"));
	object.setWaveformSrc(src("banjo", "waveform.png"));
	// database merge is triggered on DAO because instrument ID is not zero
	assertThat(object.getId(), not(equalTo(0)));
	dao.saveAtlasObject(object);

	dbInstrument = dao.getAtlasObject(object.getId());

	assertThat(dbInstrument.getImages().get(0).getFileName(), equalTo("icon.jpg"));
	assertThat(dbInstrument.getImages().get(1).getFileName(), equalTo("thumbnail.png"));
	assertThat(dbInstrument.getImages().get(2).getFileName(), equalTo("picture.jpg"));

	assertThat(dbInstrument.getSampleName(), equalTo("sample.mp3"));
	assertThat(dbInstrument.getWaveformName(), equalTo("waveform.png"));
    }

    @Test
    public void postLoadInstrument() {
	AtlasObject instrument = dao.getAtlasObject(1);

	assertThat(instrument.getImages(), notNullValue());
	assertThat(instrument.getImages().size(), equalTo(4));
	assertThat(instrument.getImages().get(0).getSrc(), equalTo(src("accordion", "icon.jpg")));
	assertThat(instrument.getImages().get(1).getSrc(), equalTo(src("accordion", "piano-accordion.png")));
	assertThat(instrument.getImages().get(2).getSrc(), equalTo(src("accordion", "button-accordion.png")));
	assertThat(instrument.getImages().get(3).getSrc(), equalTo(src("accordion", "image.jpg")));

	assertThat(instrument.getSampleSrc(), equalTo(src("accordion", "sample.mp3")));
	assertThat(instrument.getWaveformSrc(), equalTo(src("accordion", "waveform.png")));
    }

    @Test
    public void getCollectionItems() {
	List<AtlasItem> items = dao.getCollectionItems(1);
	assertThat(items, notNullValue());
	assertThat(items, hasSize(2));
	assertAtlasItem(items.get(0));
    }

    @Test
    public void findObjectByType_Instrument() throws MalformedURLException {
	List<AtlasObject> instruments = dao.findObjectsByCategory("instrument");

	assertThat(instruments, notNullValue());
	assertThat(instruments, not(empty()));
	assertThat(instruments, hasSize(2));

	assertAtlasObject(instruments.get(0));
    }

    private static void assertAtlasObject(AtlasObject object) throws MalformedURLException {
	assertThat(object, notNullValue());
	assertThat(object.getId(), equalTo(1));

	assertThat(object.getUser(), notNullValue());
	assertThat(object.getUser().getId(), equalTo(1));
	assertThat(object.getUser().getEmailAddress(), equalTo("john.doe@email.com"));
	assertThat(object.getUser().getPassword(), equalTo("secret"));

	assertThat(object.getCollection(), notNullValue());
	assertThat(object.getCollection().getId(), equalTo(1));
	assertThat(object.getCollection().getName(), equalTo("instrument"));
	assertThat(object.getCollection().getDisplay(), equalTo("Instrument"));
	assertThat(object.getCollection().getIconName(), equalTo("instrument.png"));

	assertThat(object.getRank(), equalTo(1234));
	assertThat(object.getState(), equalTo(AtlasObject.State.DEVELOPMENT));
	assertThat(object.getName(), equalTo("accordion"));
	assertThat(object.getName(), equalTo("accordion"));
	assertThat(object.getDisplay(), equalTo("Accordion"));
	assertThat(object.getDescription(), equalTo("Accordion description."));

	assertThat(object.getImages(), notNullValue());
	assertThat(object.getImages().size(), equalTo(4));
	assertThat(object.getImages().get(0).getFileName(), equalTo("icon.jpg"));
	assertThat(object.getImages().get(0).getSrc(), equalTo(src("accordion", "icon.jpg")));
	assertThat(object.getImages().get(1).getFileName(), equalTo("piano-accordion.png"));
	assertThat(object.getImages().get(1).getSrc(), equalTo(src("accordion", "piano-accordion.png")));
	assertThat(object.getImages().get(2).getFileName(), equalTo("button-accordion.png"));
	assertThat(object.getImages().get(2).getSrc(), equalTo(src("accordion", "button-accordion.png")));
	assertThat(object.getImages().get(3).getFileName(), equalTo("image.jpg"));
	assertThat(object.getImages().get(3).getSrc(), equalTo(src("accordion", "image.jpg")));

	assertThat(object.getClassification(), notNullValue());
	assertThat(object.getClassification().keySet(), not(empty()));
	assertThat(object.getClassification().keySet(), hasSize(1));
	assertThat(object.getClassification().get("Classification"), equalTo("WOODWIND"));

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
	object.setUser(new User(1));
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

	List<Image> pictures = new ArrayList<>();
	object.setImages(pictures);
	pictures.add(new Image(src("banjo", "icon.jpg")));
	pictures.add(new Image(src("banjo", "contextual.jpg")));
	pictures.add(new Image(src("banjo", "cover.png")));
	pictures.add(new Image(src("banjo", "featured.png")));

	Map<String, String> classification = new HashMap<>();
	classification.put("Classification", InstrumentCategory.STRINGS.name());
	object.setClassification(classification);

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

	assertThat(expected.getUser(), notNullValue());
	assertThat(expected.getUser().getId(), equalTo(1));
	assertThat(expected.getUser().getEmailAddress(), equalTo("john.doe@email.com"));
	assertThat(expected.getUser().getPassword(), equalTo("secret"));

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
	assertThat(expected.getImages().get(0).getFileName(), equalTo("icon.jpg"));
	assertThat(expected.getImages().get(0).getSrc(), equalTo(src("banjo", "icon.jpg")));
	assertThat(expected.getImages().get(1).getFileName(), equalTo("contextual.jpg"));
	assertThat(expected.getImages().get(1).getSrc(), equalTo(src("banjo", "contextual.jpg")));
	assertThat(expected.getImages().get(2).getFileName(), equalTo("cover.png"));
	assertThat(expected.getImages().get(2).getSrc(), equalTo(src("banjo", "cover.png")));
	assertThat(expected.getImages().get(3).getFileName(), equalTo("featured.png"));
	assertThat(expected.getImages().get(3).getSrc(), equalTo(src("banjo", "featured.png")));

	assertThat(expected.getClassification(), notNullValue());
	assertThat(expected.getClassification().keySet(), not(empty()));
	assertThat(expected.getClassification().keySet(), hasSize(1));
	assertThat(expected.getClassification().get("Classification"), equalTo("STRINGS"));

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
	List<AtlasItem> objects = dao.findObjectsByNames(1, Arrays.asList("accordion", "banjo"));
	assertThat(objects, notNullValue());
	assertThat(objects, hasSize(1));

	AtlasItem object = objects.get(0);
	assertThat(object, notNullValue());
	assertThat(object, not(instanceOf(AtlasObject.class)));
	assertThat(object.getId(), equalTo(1));
	assertThat(object.getCollectionName(), equalTo("instrument"));
	assertThat(object.getName(), equalTo("accordion"));
	assertThat(object.getDisplay(), equalTo("Accordion"));
	assertThat(object.getIconName(), equalTo("icon.jpg"));
	assertThat(object.getIconSrc(), equalTo(src("accordion", "icon_96x96.jpg")));
    }


    @Test
    public void findObjectsByName_EmptyNames() {
	List<String> emptyNames = new ArrayList<>(0);
	List<AtlasItem> objects = dao.findObjectsByNames(1, emptyNames);
	assertThat(objects, notNullValue());
	assertThat(objects, empty());
    }
    
    public void getInstrumentsByCategory() {
	List<AtlasItem> items = dao.getCollectionItems(1);
	assertThat(items, notNullValue());
	assertThat(items, hasSize(2));

	AtlasItem object = items.get(0);
	assertThat(object, notNullValue());
	assertThat(object, not(instanceOf(AtlasObject.class)));
	assertThat(object.getId(), equalTo(1));
	assertThat(object.getCollectionName(), equalTo("instrument"));
	assertThat(object.getName(), equalTo("accordion"));
	assertThat(object.getDisplay(), equalTo("Accordion"));
	assertThat(object.getIconName(), equalTo("icon.jpg"));
	assertThat(object.getIconSrc(), equalTo(src("accordion", "icon_96x96.jpg")));
    }

    public void getInstruments() {
	List<AtlasItem> items = dao.getCollectionItems(1);
	assertThat(items, notNullValue());
	assertThat(items, hasSize(3));

	AtlasItem object = items.get(0);
	assertThat(object, notNullValue());
	assertThat(object, not(instanceOf(AtlasObject.class)));
	assertThat(object.getId(), equalTo(1));
	assertThat(object.getCollectionName(), equalTo("instrument"));
	assertThat(object.getName(), equalTo("accordion"));
	assertThat(object.getDisplay(), equalTo("Accordion"));
	assertThat(object.getIconName(), equalTo("icon.jpg"));
	assertThat(object.getIconSrc(), equalTo(src("accordion", "icon_96x96.jpg")));
    }

    @Test
    public void objectState() {
	AtlasObject instrument = dao.getAtlasObject(1);
	assertThat(instrument.getState(), equalTo(AtlasObject.State.DEVELOPMENT));

	instrument = dao.getAtlasObject(3);
	assertThat(instrument.getState(), equalTo(AtlasObject.State.PUBLISHED));
    }

    @Test
    public void resetObjectSample() {
	dao.resetObjectSample(1);

	AtlasObject instrument = dao.getAtlasObject(1);
	assertThat(instrument.getSampleTitle(), nullValue());
	assertThat(instrument.getSampleName(), nullValue());
	assertThat(instrument.getSampleSrc(), nullValue());
	assertThat(instrument.getWaveformName(), nullValue());
	assertThat(instrument.getWaveformSrc(), nullValue());
    }

    @Test
    public void getPictureByFileName() {
	Image picture = dao.getPictureByName(1, "icon");
	assertThat(picture, notNullValue());
	assertThat(picture.getName(), equalTo("icon"));
	assertThat(picture.getSource(),
		equalTo("https://upload.wikimedia.org/wikipedia/commons/f/f5/Paris_-_Accordion_Player_-_0956.jpg"));
	assertThat(picture.getFileName(), equalTo("icon.jpg"));
	assertThat(picture.getFileSize(), equalTo(12345));
	assertThat(picture.getWidth(), equalTo(96));
	assertThat(picture.getHeight(), equalTo(96));
    }

    @Test
    public void pictureReoder() {
	AtlasObject instrument = dao.getAtlasObject(1);
	assertThat(instrument, notNullValue());

	List<Image> pictures = instrument.getImages();
	assertThat(pictures, notNullValue());
	assertThat(pictures.size(), equalTo(4));
	assertThat(pictures.get(0).getName(), equalTo("icon"));
	assertThat(pictures.get(1).getName(), equalTo("piano-accordion"));
	assertThat(pictures.get(2).getName(), equalTo("button-accordion"));
	assertThat(pictures.get(3).getName(), equalTo("image"));

	Collections.swap(pictures, 0, 2);
	dao.saveAtlasObject(instrument);

	instrument = dao.getAtlasObject(1);
	assertThat(instrument, notNullValue());

	pictures = instrument.getImages();
	assertThat(pictures, notNullValue());
	assertThat(pictures.size(), equalTo(4));
	assertThat(pictures.get(0).getName(), equalTo("button-accordion"));
	assertThat(pictures.get(1).getName(), equalTo("piano-accordion"));
	assertThat(pictures.get(2).getName(), equalTo("icon"));
	assertThat(pictures.get(3).getName(), equalTo("image"));
    }

    // ----------------------------------------------------------------------------------------------

    private static MediaSRC src(String objectName, String mediaFile) {
	return Files.mediaSrc("instrument", objectName, mediaFile);
    }
}
