package com.kidscademy.atlas.studio.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import js.json.Json;
import js.util.Classes;
import js.util.Files;

public class FsExportTarget implements ExportTarget
{
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
  public void write(Object object, String path) throws IOException {
    File file = new File(location, path);
    file.getParentFile().mkdirs();
    FileWriter writer = new FileWriter(file);
    try {
      json.stringify(writer, object);
    }
    finally {
      writer.close();
    }
  }

  @Override
  public void write(File file, String path) throws IOException {
    // test null file here in order to simplify invoker logic
    if(file == null || !file.exists()) {
      return;
    }
    file.getParentFile().mkdirs();
    Files.copy(file, new File(location, path));
  }
}
