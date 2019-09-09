package com.kidscademy.atlas.studio.dao;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kidscademy.atlas.studio.model.AtlasCollection;
import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.User;

import js.json.Json;
import js.transaction.TransactionFactory;
import js.transaction.eclipselink.TransactionFactoryImpl;
import js.util.Classes;

public class ImportCollection {
    private static final File SOURCE_DIR = new File("D:\\work\\atlas\\musical-instruments");

    public static void main(String... args) throws Exception {
	Json json = Classes.loadService(Json.class);

	TransactionFactory factory = new TransactionFactoryImpl("import");
	AtlasDao dao = factory.newInstance(AtlasDaoImpl.class);

	User user = dao.getUserById(1);
	AtlasCollection collection = dao.getCollectionById(1);

	Map<AtlasObject, List<String>> objects = new HashMap<>();
	for (File objectDir : SOURCE_DIR.listFiles()) {

	    AtlasObject object = json.parse(new FileReader(new File(objectDir, "object.json")), AtlasObject.class);
	    System.out.println(object.getDisplay());

	    object.setUser(user);
	    object.setCollection(collection);
	    object.setState(AtlasObject.State.DEVELOPMENT);

	    objects.put(object, object.getRelated());
	    List<String> emptyRelated = new ArrayList<>(0);
	    object.setRelated(emptyRelated);

	    dao.saveAtlasObject(object);
	}

	for (Map.Entry<AtlasObject, List<String>> entry : objects.entrySet()) {
	    final AtlasObject object = entry.getKey();
	    object.setRelated(entry.getValue());
	    dao.saveAtlasObject(object);
	}
    }
}
