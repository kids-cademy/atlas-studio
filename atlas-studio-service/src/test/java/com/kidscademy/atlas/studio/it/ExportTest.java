package com.kidscademy.atlas.studio.it;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;

import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.dao.AtlasDaoImpl;
import com.kidscademy.atlas.studio.export.AtlasCollectionExportView;
import com.kidscademy.atlas.studio.model.AtlasObject;

import js.json.Json;
import js.transaction.TransactionFactory;
import js.transaction.eclipselink.TransactionFactoryImpl;
import js.util.Classes;

public class ExportTest {
    private final AtlasDao atlasDao;
    private final Json json;

    public ExportTest() {
	TransactionFactory factory = new TransactionFactoryImpl("import");
	this.atlasDao = factory.newInstance(AtlasDaoImpl.class);
	this.json = Classes.loadService(Json.class);
    }

    @Test
    public void exportAtlasCollection() throws IOException {
	AtlasCollectionExportView export = new AtlasCollectionExportView(atlasDao, json, 2, AtlasObject.State.PUBLISHED);
	OutputStream stream = new FileOutputStream("fixture/atlas.zip");
	export.serialize(stream);
	stream.close();
    }
}
