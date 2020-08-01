package com.kidscademy.atlas.studio.export;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;

import js.json.Json;
import js.lang.BugError;
import js.util.Classes;
import js.util.Files;
import js.util.Params;

public class FsExportTarget implements ExportTarget {
    private final Json json;
    private final File location;

    public FsExportTarget(File location) {
	this.json = Classes.loadService(Json.class);
	this.location = location;
    }

    @Override
    public void open(OutputStream stream) {
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public void write(Object object, String path, Type type) throws IOException, BugError {
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

	FileReader reader = new FileReader(file);
	try {
	    json.parse(reader, type);
	} catch (Exception e) {
	    throw new BugError("Bad serialization for |%s|: %s", path, e.getMessage());
	} finally {
	    reader.close();
	}
    }

    @Override
    public void write(File file, String path) throws IOException {
	// test null file here in order to simplify invoker logic
	if (file == null || !file.exists()) {
	    return;
	}
	file.getParentFile().mkdirs();
	Files.copy(file, new File(location, path));
    }
}
