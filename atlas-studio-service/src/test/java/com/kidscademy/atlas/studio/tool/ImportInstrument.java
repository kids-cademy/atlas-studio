package com.kidscademy.atlas.studio.tool;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.dao.AtlasDaoImpl;
import com.kidscademy.atlas.studio.model.InstrumentCategory;
import com.kidscademy.atlas.studio.model.HDate;
import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.Link;
import com.kidscademy.atlas.studio.model.MediaSRC;
import com.kidscademy.atlas.studio.model.Region;

import js.json.Json;
import js.transaction.TransactionFactory;
import js.transaction.eclipselink.TransactionFactoryImpl;
import js.util.Classes;

public class ImportInstrument {
    private static final File SOURCE_DIR = new File(
	    "D:\\docs\\workspaces\\kids-cademy\\data\\instruments\\build\\collection");

    public static void main(String... args) throws Exception {
	Json json = Classes.loadService(Json.class);

	TransactionFactory factory = new TransactionFactoryImpl();
	AtlasDao dao = factory.newInstance(AtlasDaoImpl.class);

	List<InstrumentObject> objects = new ArrayList<>();

	for (File objectDir : SOURCE_DIR.listFiles()) {
	    File objectFile = new File(objectDir, "instrument_en.json");
	    System.out.println(objectFile);

	    InstrumentObject object = json.parse(new FileReader(objectFile), InstrumentObject.class);
	    objects.add(object);

	}

	Map<InstrumentObject, AtlasObject> instruments = new HashMap<>();

	for (InstrumentObject object : objects) {
	    AtlasObject instrument = new AtlasObject();
	    instruments.put(object, instrument);

	    instrument.setRank(object.rank);
	    instrument.setName(object.name);
	    instrument.setDisplay(object.title);
	    instrument.setDescription(object.description);

	    // TODO: add pictures
	    
	    instrument.setSampleTitle(object.sampleTitle);
	    instrument.setSampleName(object.samplePath);
	    instrument.setStartDate(new HDate(object.date.value, object.date.format, object.date.period, object.date.era));

	    instrument.setAliases(Arrays.asList(object.aliases));

	    List<Region> spreading = new ArrayList<>();
	    for (Region region : object.spreading) {
		spreading.add(region);
	    }
	    instrument.setSpreading(spreading);

	    List<Link> links = new ArrayList<>();
	    for (ExternalSource source : object.sources) {
		// TODO: add description
		links.add(new Link(source.url, source.name, "", new MediaSRC("/media/link/" + source.icon.getName())));
	    }
	    instrument.setLinks(links);

	    instrument.setFacts(object.facts);

	    dao.saveObject(instrument);
	}

	for (InstrumentObject object : instruments.keySet()) {
	    AtlasObject instrument = instruments.get(object);

	    List<String> related = new ArrayList<>();
	    for (int relatedIndex : object.related) {
		related.add(objects.get(relatedIndex).name);
	    }
	    // instrument.setRelated(related);

	    dao.saveObject(instrument);
	}
    }

    private static class InstrumentObject {
	int id;
	int rank;
	String name;
	String title;
	String[] aliases;
	InstrumentCategory category;
	HDateSource date;
	String picturePath;
	String iconPath;
	String thumbnailPath;
	String description;
	String sampleTitle;
	Region[] spreading;
	Map<String, String> facts;
	int[] related;
	String samplePath;
	ExternalSource[] sources;
    }

    private static class HDateSource {
	long value;
	HDate.Format format;
	HDate.Period period;
	HDate.Era era;
    }

    private static class ExternalSource {
	String name;
	URL url;
	File icon;
    }
}
