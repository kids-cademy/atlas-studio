package com.kidscademy.atlas.studio.tool;

import java.io.File;
import java.io.FileReader;

import js.json.Json;
import js.transaction.TransactionFactory;
import js.transaction.eclipselink.TransactionFactoryImpl;
import js.util.Classes;

public class ImportCollection {
    private static final File SOURCE_DIR = new File("D:\\work\\atlas\\musical-instruments");

    public static void main(String... args) throws Exception {
	Json json = Classes.loadService(Json.class);
	
	TransactionFactory factory = new TransactionFactoryImpl();
	AtlasDao dao = factory.newInstance(AtlasDaoImpl.class);

	for (File objectDir : SOURCE_DIR.listFiles()) {

	    AtlasObject object = json.parse(new FileReader(new File(objectDir, "object.json")), AtlasObject.class);
	    System.out.println(object.getDisplay());
	    
	    object.setUser_id(1);
	    object.setColleciton_id(1);
	    object.setState(AtlasObject.State.DEVELOPMENT);
	    
	    dao.saveObject(object);
	}

    }
}
