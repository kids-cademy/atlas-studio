package com.kidscademy.atlas.studio.export;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.search.KeywordIndex;

import js.json.Json;
import js.json.impl.JsonParserException;
import js.lang.BugError;
import js.util.Classes;
import js.util.Params;

public class ZipExportTarget implements ExportTarget
{
  private final Json json;

  public ZipExportTarget() {
    this.json = Classes.loadService(Json.class);
  }

  public ZipExportTarget(Json json) {
    this.json = json;
  }

  private ZipOutputStream zip;

  @Override
  public void open(OutputStream stream) {
    Params.notNull(stream, "Output stream");
    zip = new ZipOutputStream(stream);
  }

  @Override
  public void writeObject(ExportObject object) throws IOException {
    // TODO Auto-generated method stub

  }

  @Override
  public void writeObjectsList(Collection<String> objectsList) throws IOException {
    // TODO Auto-generated method stub

  }

  @Override
  public void writeSearchIndex(List<KeywordIndex<Integer>> searchIndex, String language) throws IOException {
    // TODO Auto-generated method stub

  }

  @Override
  public void writeMedia(File sourceMediaFile, AtlasObject object, String mediaFileName) throws IOException {
    // TODO Auto-generated method stub

  }

  @Override
  public void commitMedia(AtlasObject object, String mediaFileName) throws IOException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public boolean isMediaFile(AtlasObject object, String mediaFileName) {
    // TODO Auto-generated method stub
    return false;
  }

  public void write(Object object, String path, Type type) throws IOException, JsonParserException {
    Params.notNull(object, "Null object");
    Params.notNullOrEmpty(path, "File path");

    ZipEntry entry = new ZipEntry(path);
    zip.putNextEntry(entry);
    String stringifiedObject = json.stringify(object);
    zip.write(stringifiedObject.getBytes("UTF-8"));
    zip.closeEntry();

    try {
      json.parse(stringifiedObject, type);
    }
    catch(Exception e) {
      throw new BugError("Bad serialization for |%s|: %s", path, e.getMessage());
    }
  }

  public void write(File file, String path) throws IOException {
    // test null file here in order to simplify invoker logic
    if(file == null || !file.exists()) {
      return;
    }
    ZipEntry entry = new ZipEntry(path);
    zip.putNextEntry(entry);
    copy(file, zip);
    zip.closeEntry();
  }

  @Override
  public void commit() throws IOException {
    // TODO Auto-generated method stub

  }

  @Override
  public void close() throws IOException {
    zip.close();
  }

  /**
   * Copy source file binary content to output stream keeping it opened.
   * 
   * @param file binary source file
   * @param outputStream output stream.
   * @return
   * @throws IOException
   * @throws IllegalArgumentException
   */
  private static long copy(File file, OutputStream outputStream) throws IOException, IllegalArgumentException {
    InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
    outputStream = new BufferedOutputStream(outputStream);

    long bytes = 0;
    try {
      byte[] buffer = new byte[4096];
      int length;
      while((length = inputStream.read(buffer)) != -1) {
        bytes += length;
        outputStream.write(buffer, 0, length);
      }
    }
    finally {
      inputStream.close();
      outputStream.flush();
    }
    return bytes;
  }
}
