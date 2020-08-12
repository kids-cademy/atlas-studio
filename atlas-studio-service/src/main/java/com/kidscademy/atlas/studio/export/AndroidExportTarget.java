package com.kidscademy.atlas.studio.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;

import com.kidscademy.atlas.studio.model.AndroidProject;
import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.Translator;
import com.kidscademy.atlas.studio.search.KeywordIndex;

import js.json.Json;
import js.util.Classes;
import js.util.Files;
import js.util.Params;

/**
 * Export target implementation for Android project.
 * 
 * <pre>
 * --+-- assets --+-- atlas --+-- balaena-mysticetus --+-- contextual.jpg
 *   |            |           |                        +-- cover.png 
 *   |            |           |                        +-- icon.jpg
 *   |            |           |                        +-- sample.mp3
 *   |            |           |                        +-- waveform.png
 *   |            |           +-- dugong-dugon --------+-- contextual.jpg
 *   |            |           |                        +-- cover.png 
 *   |            |           |                        +-- icon.jpg
 *   |            |           |                        +-- sample.mp3
 *   |            |           |                        +-- waveform.png 
 *   ~            ~           ~
 *   |            |           +-- objects-list.json
 *   ~            ~    
 *   +-- res -----+-- raw ----+-- balaena_mysticetus.json
 *   |            |           +-- dugong_dugon.json
 *   ~            ~           ~
 *   |            |           +-- search_index.json
 *   |            +-- raw-ro -+-- balaena_mysticetus.json
 *   |            |           +-- dugong_dugon.json
 *   ~            ~           ~
 *   |            |           +-- search_index.json
 * </pre>
 * 
 * @author Iulian Rotaru
 *
 */
public class AndroidExportTarget implements ExportTarget {
    private final Json json;
    private final File location;

    public AndroidExportTarget(AndroidProject project) {
	this.json = Classes.loadService(Json.class);
	this.location = project.getMainDir();
    }

    @Override
    public void open(OutputStream stream) {
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public void writeObject(ExportObject object, String language) throws IOException {
	StringBuilder path = getRawResourceBuilder(language);
	path.append(object.getName().replace('-', '_'));
	path.append(".json");
	writeJson(object, path.toString());
    }

    @Override
    public void writeObjectsList(Set<String> objectsList) throws IOException {
	writeJson(objectsList, "assets/atlas/objects-list.json");
    }

    @Override
    public void writeSearchIndex(List<KeywordIndex<Integer>> searchIndex, String language) throws IOException {
	StringBuilder path = getRawResourceBuilder(language);
	path.append("search_index.json");
	writeJson(searchIndex, path.toString());
    }

    private static StringBuilder getRawResourceBuilder(String language) {
	StringBuilder builder = new StringBuilder();
	builder.append("res/raw");
	if (!Translator.isDefaultLanguage(language)) {
	    builder.append('-');
	    builder.append(language.toLowerCase());
	}
	builder.append('/');
	return builder;
    }

    @Override
    public void writeMedia(File sourceMediaFile, AtlasObject object, String mediaFileName) throws IOException {
	if (sourceMediaFile == null || mediaFileName == null) {
	    return;
	}
	Params.notNull(object, "Null atlas object");

	StringBuilder path = new StringBuilder();
	path.append("assets/atlas/");
	path.append(object.getName());
	path.append('/');
	path.append(mediaFileName);

	File file = new File(location, path.toString());
	file.getParentFile().mkdirs();
	Files.copy(sourceMediaFile, file);
    }

    private void writeJson(Object object, String path) throws IOException {
	Params.notNull(object, "Null object");
	Params.notNullOrEmpty(path, "File path");

	File file = new File(location, path);
	file.getParentFile().mkdirs();
	FileWriter writer = new FileWriter(file);
	try {
	    json.stringify(writer, object);
	} finally {
	    writer.close();
	}
    }
}
