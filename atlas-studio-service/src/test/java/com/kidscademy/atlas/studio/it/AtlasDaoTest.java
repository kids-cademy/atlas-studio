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
import com.kidscademy.atlas.studio.model.AtlasItem;
import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.HDate;
import com.kidscademy.atlas.studio.model.Image;
import com.kidscademy.atlas.studio.model.Link;
import com.kidscademy.atlas.studio.model.MediaSRC;
import com.kidscademy.atlas.studio.model.Region;
import com.kidscademy.atlas.studio.model.Related;
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
    public void getObjectById() throws MalformedURLException {
	AtlasObject instrument = dao.getObjectById(1);
	assertInstrument(instrument);
    }

    /**
     * When create new instrument with not null media source values, media files
     * fields should initialized before actual database persist.
     */
    public void prePersistInstrument() {
	AtlasObject instrument = new AtlasObject();
	instrument.setUser(new User(1));
	instrument.setState(AtlasObject.State.DEVELOPMENT);
	instrument.setRank(9999);
	instrument.setName("banjo");
	instrument.setDisplay("Banjo");

	List<Image> pictures = new ArrayList<>();
	instrument.setImages(pictures);
	pictures.add(new Image(src("banjo", "icon.jpg")));
	pictures.add(new Image(src("banjo", "thumbnail.png")));
	pictures.add(new Image(src("banjo", "picture.jpg")));

	instrument.setSampleSrc(src("banjo", "sample.mp3"));
	instrument.setWaveformSrc(src("banjo", "waveform.png"));

	dao.saveObject(instrument);

	AtlasObject persistedInstrument = dao.getObjectById(instrument.getId());

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
    public void postMergeInstrument() {
	AtlasObject uiInstrument = new AtlasObject();
	uiInstrument.setUser(new User(1));
	uiInstrument.setState(AtlasObject.State.DEVELOPMENT);
	uiInstrument.setRank(9999);
	uiInstrument.setName("banjo");
	uiInstrument.setDisplay("Banjo");

	List<Image> pictures = new ArrayList<>();
	uiInstrument.setImages(pictures);
	pictures.add(new Image(src("banjo", "icon.jpg")));
	pictures.add(new Image(src("banjo", "thumbnail.png")));
	pictures.add(new Image(src("banjo", "picture.jpg")));

	dao.saveObject(uiInstrument);

	AtlasObject dbInstrument = dao.getObjectById(uiInstrument.getId());
	assertThat(dbInstrument.getSampleName(), nullValue());
	assertThat(dbInstrument.getWaveformName(), nullValue());

	uiInstrument.setSampleSrc(src("banjo", "sample.mp3"));
	uiInstrument.setWaveformSrc(src("banjo", "waveform.png"));
	// database merge is triggered on DAO because instrument ID is not zero
	assertThat(uiInstrument.getId(), not(equalTo(0)));
	dao.saveObject(uiInstrument);

	dbInstrument = dao.getObjectById(uiInstrument.getId());

	assertThat(dbInstrument.getImages().get(0).getFileName(), equalTo("icon.jpg"));
	assertThat(dbInstrument.getImages().get(1).getFileName(), equalTo("thumbnail.png"));
	assertThat(dbInstrument.getImages().get(2).getFileName(), equalTo("picture.jpg"));

	assertThat(dbInstrument.getSampleName(), equalTo("sample.mp3"));
	assertThat(dbInstrument.getWaveformName(), equalTo("waveform.png"));
    }

    @Test
    public void postLoadInstrument() {
	AtlasObject instrument = dao.getObjectById(1);

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
    public void findObjectByType_Instrument() throws MalformedURLException {
	List<AtlasObject> instruments = dao.findObjectsByCategory("instrument");

	assertThat(instruments, notNullValue());
	assertThat(instruments, not(empty()));
	assertThat(instruments, hasSize(2));

	assertInstrument(instruments.get(0));
    }

    private static void assertInstrument(AtlasObject instrument) throws MalformedURLException {
	assertThat(instrument, notNullValue());
	assertThat(instrument.getId(), equalTo(1));
	assertThat(instrument.getUser(), notNullValue());
	assertThat(instrument.getUser().getId(), equalTo(1));
	assertThat(instrument.getUser().getEmailAddress(), equalTo("john.doe@email.com"));
	assertThat(instrument.getUser().getPassword(), equalTo("secret"));
	assertThat(instrument.getRank(), equalTo(1234));
	assertThat(instrument.getState(), equalTo(AtlasObject.State.DEVELOPMENT));
	assertThat(instrument.getName(), equalTo("accordion"));
	assertThat(instrument.getName(), equalTo("accordion"));
	assertThat(instrument.getDisplay(), equalTo("Accordion"));
	assertThat(instrument.getDescription(), equalTo("Accordion description."));

	assertThat(instrument.getImages(), notNullValue());
	assertThat(instrument.getImages().size(), equalTo(4));
	assertThat(instrument.getImages().get(0).getFileName(), equalTo("icon.jpg"));
	assertThat(instrument.getImages().get(0).getSrc(), equalTo(src("accordion", "icon.jpg")));
	assertThat(instrument.getImages().get(1).getFileName(), equalTo("piano-accordion.png"));
	assertThat(instrument.getImages().get(1).getSrc(), equalTo(src("accordion", "piano-accordion.png")));
	assertThat(instrument.getImages().get(2).getFileName(), equalTo("button-accordion.png"));
	assertThat(instrument.getImages().get(2).getSrc(), equalTo(src("accordion", "button-accordion.png")));
	assertThat(instrument.getImages().get(3).getFileName(), equalTo("image.jpg"));
	assertThat(instrument.getImages().get(3).getSrc(), equalTo(src("accordion", "image.jpg")));

	assertThat(instrument.getSampleTitle(), equalTo("Sample"));
	assertThat(instrument.getSampleName(), equalTo("sample.mp3"));
	assertThat(instrument.getWaveformName(), equalTo("waveform.png"));
	assertThat(instrument.getSampleSrc(), equalTo(src("accordion", "sample.mp3")));
	assertThat(instrument.getWaveformSrc(), equalTo(src("accordion", "waveform.png")));

	assertThat(instrument.getStartDate().getValue(), equalTo(1234567890L));
	assertThat(instrument.getStartDate().getFormat(), equalTo(HDate.Format.DATE));
	assertThat(instrument.getStartDate().getPeriod(), equalTo(HDate.Period.FULL));
	assertThat(instrument.getStartDate().getEra(), equalTo(HDate.Era.CE));

	assertThat(instrument.getAliases(), notNullValue());
	assertThat(instrument.getAliases(), hasSize(1));
	assertThat(instrument.getAliases().get(0), equalTo("Squeezebox"));

	assertThat(instrument.getSpreading(), notNullValue());
	assertThat(instrument.getSpreading(), hasSize(1));
	assertThat(instrument.getSpreading().get(0), notNullValue());
	assertThat(instrument.getSpreading().get(0).getName(), equalTo("RO"));
	assertThat(instrument.getSpreading().get(0).getArea(), equalTo(Region.Area.CENTRAL));

	assertThat(instrument.getFacts(), notNullValue());
	assertThat(instrument.getFacts().keySet(), not(empty()));
	assertThat(instrument.getFacts().keySet(), hasSize(1));
	assertThat(instrument.getFacts().get("Fact #1"), equalTo("Fact #1 description."));

	assertThat(instrument.getLinks(), notNullValue());
	assertThat(instrument.getLinks(), hasSize(1));

	Link link = instrument.getLinks().get(0);
	assertThat(link, notNullValue());
	assertThat(link.getDisplay(), equalTo("Wikipedia"));
	assertThat(link.getIconName(), equalTo("wikipedia.png"));
	assertThat(link.getIconSrc(), equalTo(new MediaSRC("/media/link/wikipedia.png")));
	assertThat(link.getUrl(), equalTo(new URL("https://en.wikipedia.org/wiki/Accordion")));

	assertThat(instrument.getRelated(), notNullValue());
	assertThat(instrument.getRelated(), not(empty()));
	assertThat(instrument.getRelated(), hasSize(2));
	// assertThat(instrument.getRelated().get(0), equalTo("bandoneon"));
	// assertThat(instrument.getRelated().get(1), equalTo("cimbalom"));
    }

    @Test
    public void serializeInstrument() {
	AtlasObject instrument = dao.getObjectById(1);

	Json json = Classes.loadService(Json.class);
	String value = json.stringify(instrument);
	System.out.println(value);
    }

    public void saveInstrument() throws MalformedURLException {
	AtlasObject instrument = new AtlasObject();
	instrument.setUser(new User(1));
	instrument.setState(AtlasObject.State.DEVELOPMENT);
	instrument.setRank(9999);
	instrument.setName("banjo");
	instrument.setDisplay("Banjo");
	instrument.setDescription("Banjo description.");
	instrument.setSampleTitle("Banjo solo");
	instrument.setSampleName("sample.mp3");
	instrument.setWaveformName("waveform.png");
	instrument.setStartDate(new HDate(1821, HDate.Format.YEAR, HDate.Period.MIDDLE));

	List<Image> pictures = new ArrayList<>();
	instrument.setImages(pictures);
	pictures.add(new Image(src("banjo", "icon.jpg")));
	pictures.add(new Image(src("banjo", "thumbnail.png")));
	pictures.add(new Image(src("banjo", "picture.jpg")));

	List<String> aliases = new ArrayList<String>();
	aliases.add("Banjo Alias #1");
	aliases.add("Banjo Alias #2");
	instrument.setAliases(aliases);

	List<Region> spreading = new ArrayList<Region>();
	spreading.add(new Region("Europe", Region.Area.SOUTH));
	spreading.add(new Region("Africa", Region.Area.NORTH));
	instrument.setSpreading(spreading);

	List<Link> links = new ArrayList<>();
	links.add(new Link(new URL("https://en.wikipedia.org/wiki/AccordionXXX"), "Wikipedia-xxx", "Wikipedia article",
		new MediaSRC("/media/link/wikipedia.png")));
	links.add(new Link(new URL("http://en.wikipedia.org:443/wiki/Accordion"), "Wikipedia-www", "Wikipedia article",
		new MediaSRC("/media/link/wikipedia.png")));
	instrument.setLinks(links);

	Map<String, String> facts = new HashMap<>();
	facts.put("Banjo Fact #1", "Banjo fact #1 description.");
	facts.put("Banjo Fact #2", "Banjo fact #2 description.");
	instrument.setFacts(facts);

	List<Related> related = new ArrayList<>();
	related.add(new Related("accordion", 0.5F));
	instrument.setRelated(related);

	assertThat(instrument.getId(), equalTo(0));
	dao.saveObject(instrument);
	assertThat(instrument.getId(), not(equalTo(0)));
    }

    public void findObjectsByName() {
	List<AtlasItem> objects = dao.findObjectsByNames("instrument", Arrays.asList("accordion", "banjo"));
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
	AtlasObject instrument = dao.getObjectById(1);
	assertThat(instrument.getState(), equalTo(AtlasObject.State.DEVELOPMENT));

	instrument = dao.getObjectById(3);
	assertThat(instrument.getState(), equalTo(AtlasObject.State.PUBLISHED));
    }

    @Test
    public void resetObjectSample() {
	dao.resetObjectSample(1);

	AtlasObject instrument = dao.getObjectById(1);
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
	AtlasObject instrument = dao.getObjectById(1);
	assertThat(instrument, notNullValue());

	List<Image> pictures = instrument.getImages();
	assertThat(pictures, notNullValue());
	assertThat(pictures.size(), equalTo(4));
	assertThat(pictures.get(0).getName(), equalTo("icon"));
	assertThat(pictures.get(1).getName(), equalTo("piano-accordion"));
	assertThat(pictures.get(2).getName(), equalTo("button-accordion"));
	assertThat(pictures.get(3).getName(), equalTo("image"));

	Collections.swap(pictures, 0, 2);
	dao.saveObject(instrument);

	instrument = dao.getObjectById(1);
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
