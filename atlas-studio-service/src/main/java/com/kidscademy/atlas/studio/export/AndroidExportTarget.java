package com.kidscademy.atlas.studio.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.kidscademy.atlas.studio.impl.Translator;
import com.kidscademy.atlas.studio.model.AndroidProject;
import com.kidscademy.atlas.studio.model.AtlasObject;
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
public class AndroidExportTarget implements ExportTarget
{
  private final Json json;
  private final File location;
  /** All files existing on Android project assets and raw resources at the moment this export target is created. */
  private final List<File> files;

  public AndroidExportTarget(AndroidProject project) {
    this.json = Classes.loadService(Json.class);
    this.location = project.getMainDir();

    this.files = new ArrayList<>();

    File atlasDir = new File(this.location, "assets/atlas");
    for(File file : atlasDir.listFiles()) {
      if(file.isFile()) {
        this.files.add(file);
        continue;
      }
      // at this point file is an atlas object directory; add all its files
      this.files.addAll(Arrays.asList(file.listFiles()));
    }

    File resourcesDir = new File(this.location, "res");
    for(File file : resourcesDir.listFiles()) {
      if(file.getName().startsWith("raw")) {
        // at this point file is an raw resource directory; add all its files
        this.files.addAll(Arrays.asList(file.listFiles()));
      }
    }

    for(File file : this.files) {
      System.out.println(file);
    }
  }

  @Override
  public void open(OutputStream stream) {
  }

  @Override
  public void commit() throws IOException {
    for(File file : files) {
      file.delete();
    }
  }

  @Override
  public void close() throws IOException {
  }

  @Override
  public void writeObject(ExportObject object) throws IOException {
    StringBuilder path = getRawResourceBuilder(object.getLanguage());
    path.append(object.getName().replace('-', '_'));
    path.append(".json");
    writeJson(object, path.toString());
  }

  @Override
  public void writeObjectsList(Collection<String> objectsList) throws IOException {
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
    if(!Translator.isDefaultLanguage(language)) {
      builder.append('-');
      builder.append(language.toLowerCase());
    }
    builder.append('/');
    return builder;
  }

  @Override
  public void writeMedia(File sourceMediaFile, AtlasObject object, String mediaFileName) throws IOException {
    if(sourceMediaFile == null || mediaFileName == null) {
      return;
    }
    Params.notNull(object, "Null atlas object");

    File file = mediaFile(object, mediaFileName);
    file.getParentFile().mkdirs();
    Files.copy(sourceMediaFile, file);
  }

  @Override
  public void commitMedia(AtlasObject object, String mediaFileName) throws IOException {
    files.remove(mediaFile(object, mediaFileName));
  }

  @Override
  public boolean isMediaFile(AtlasObject object, String mediaFileName) {
    return mediaFile(object, mediaFileName).exists();
  }

  private File mediaFile(AtlasObject object, String mediaFileName) {
    StringBuilder path = new StringBuilder();
    path.append("assets/atlas/");
    path.append(object.getName());
    path.append('/');
    path.append(mediaFileName);

    return new File(location, path.toString());
  }

  private void writeJson(Object object, String path) throws IOException {
    Params.notNull(object, "Null object");
    Params.notNullOrEmpty(path, "File path");

    File file = new File(location, path);
    file.getParentFile().mkdirs();
    FileWriter writer = new FileWriter(file);
    try {
      json.stringify(writer, object);
    }
    finally {
      writer.close();
    }
    files.remove(file);
  }
}
