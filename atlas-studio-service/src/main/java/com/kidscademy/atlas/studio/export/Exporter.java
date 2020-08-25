package com.kidscademy.atlas.studio.export;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.kidscademy.atlas.studio.Application;
import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.model.AtlasItem;
import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.Image;
import com.kidscademy.atlas.studio.model.Theme;
import com.kidscademy.atlas.studio.model.Translator;
import com.kidscademy.atlas.studio.search.SearchIndexProcessor;
import com.kidscademy.atlas.studio.tool.ConvertProcess;
import com.kidscademy.atlas.studio.tool.IdentifyProcess;
import com.kidscademy.atlas.studio.tool.ImageProcessor;
import com.kidscademy.atlas.studio.tool.ImageProcessorImpl;
import com.kidscademy.atlas.studio.util.Files;

import js.tiny.container.net.EventStreamManager;
import js.util.Classes;

public class Exporter
{
  private final AtlasDao dao;
  private final ExportTarget target;
  private final Theme theme;
  private final List<ExportItem> items;
  private final List<String> languages;

  private Date buildTimestamp = new Date();

  /**
   * Exporter for application atlas content.
   * 
   * @param dao persistence layer,
   * @param target where to export, for now file system or ZIP file,
   * @param theme release theme,
   * @param items release items.
   */
  public Exporter(AtlasDao dao, ExportTarget target, Theme theme, List<AtlasItem> items, List<String> languages) {
    this.dao = dao;
    this.target = target;
    this.items = new ArrayList<>(items.size());
    this.theme = theme;
    for(AtlasItem item : items) {
      this.items.add(new ExportItem(item));
    }
    this.languages = languages;
  }

  public Exporter(AtlasDao dao, ExportTarget target, List<ExportItem> items, List<String> languages) {
    this.dao = dao;
    this.target = target;
    this.theme = Theme.CLASSIC;
    this.items = items;
    this.languages = languages;
  }

  public void setBuildTimestamp(Date buildTimestamp) {
    this.buildTimestamp = buildTimestamp;
  }

  /**
   * If underlying target does not require stream, invoker should explicitly set it to null.
   * 
   * @param stream optional output stream, null if underlying target does not require it.
   * @throws IOException
   */
  public void serialize(OutputStream stream) throws IOException {
    serialize(stream, true);
  }

  public void serialize(OutputStream stream, boolean mediaProcessing) throws IOException {
    target.open(stream);
    try {
      serialize(mediaProcessing);
      target.commit();
    }
    catch(NoSuchMethodException e) {
      throw new IOException(e);
    }
    finally {
      target.close();
    }
  }

  private void serialize(boolean mediaProcessing) throws NoSuchMethodException, IOException {
    // uses linked hash map to preserve insertion order
    Map<String, ExportItem> itemsMap = new LinkedHashMap<>(items.size());
    for(int index = 0; index < items.size(); ++index) {
      final ExportItem item = items.get(index);
      item.setIndex(index);
      item.setIconPath(Util.path(item.getName(), item.getIconName()));
      itemsMap.put(item.getName(), item);
    }
    target.writeObjectsList(itemsMap.keySet());

    SearchIndexProcessor indexProcessor = new SearchIndexProcessor();
    List<ExportObject> exportObjects = new ArrayList<>();
    for(ExportItem item : items) {
      AtlasObject atlasObject = dao.getAtlasObject(item.getId());
      ExportObject exportObject = new ExportObject(atlasObject, theme);
      exportObjects.add(exportObject);

      exportObject.setIndex(item.getIndex());
      indexProcessor.createDirectIndex(exportObject);

      for(String relatedName : atlasObject.getRelated()) {
        ExportItem relatedItem = itemsMap.get(relatedName);
        if(relatedItem != null) {
          exportObject.addRelated(new ExportRelatedObject(relatedItem));
        }
      }

      target.writeObject(exportObject);

      if(atlasObject.getSampleTimestamp().after(buildTimestamp) || !target.isMediaFile(atlasObject, atlasObject.getSampleName())) {
        target.writeMedia(mediaFile(atlasObject, atlasObject.getSampleName()), atlasObject, atlasObject.getSampleName());
        target.writeMedia(mediaFile(atlasObject, atlasObject.getWaveformName()), atlasObject, atlasObject.getWaveformName());
      }
      target.commitMedia(atlasObject, atlasObject.getSampleName());
      target.commitMedia(atlasObject, atlasObject.getWaveformName());

      for(Map.Entry<String, Image> entry : atlasObject.getImages().entrySet()) {
        Image image = entry.getValue();
        if(image.getTimestamp().after(buildTimestamp) || !target.isMediaFile(atlasObject, image.getFileName())) {
          Dimension d = DIMENSIONS.get(entry.getKey());
          target.writeMedia(mediaFile(atlasObject, entry.getKey(), d.width, d.height), atlasObject, imageFileName(atlasObject, entry.getKey()));
        }
        target.commitMedia(atlasObject, imageFileName(atlasObject, entry.getKey()));
      }
    }
    target.writeSearchIndex(indexProcessor.updateSearchIndex(), Translator.DEFAULT_LANGUAGE);

    for(String language : languages) {
      if(Translator.isDefaultLanguage(language)) {
        continue;
      }
      indexProcessor = new SearchIndexProcessor();
      for(ExportObject exportObject : exportObjects) {
        Translator translator = new Translator(dao, language);
        exportObject.translate(translator);
        indexProcessor.createDirectIndex(exportObject);
        target.writeObject(exportObject);
      }
      target.writeSearchIndex(indexProcessor.updateSearchIndex(), language);
    }
  }

  private static String imageFileName(AtlasObject object, String imageKey) {
    Image image = object.getImage(imageKey);
    return image != null ? image.getFileName() : null;
  }

  /**
   * Source media file.
   * 
   * @param atlasObject
   * @param mediaFileName
   * @return
   */
  private static File mediaFile(AtlasObject atlasObject, String mediaFileName) {
    if(mediaFileName == null) {
      return null;
    }
    return Files.mediaFile(atlasObject, mediaFileName);
  }

  /**
   * Rescaled source media file.
   * 
   * @param object
   * @param imageKey
   * @param width
   * @param height
   * @return
   * @throws IOException
   */
  private static File mediaFile(AtlasObject object, String imageKey, int width, int height) throws IOException {
    Image image = object.getImage(imageKey);
    if(image == null) {
      return null;
    }
    File file = Files.mediaFile(object, image.getFileName());
    if(!file.exists()) {
      return null;
    }

    Application application = Application.instance();
    Classes.setFieldValue(ConvertProcess.class, "BIN", application.getProperty("image.magick.convert"));
    Classes.setFieldValue(IdentifyProcess.class, "BIN", application.getProperty("image.magick.identify"));
    ImageProcessor processor = new ImageProcessorImpl(application.getContext().getInstance(EventStreamManager.class));

    File targetFile = File.createTempFile("picture", image.getFileName());
    targetFile.deleteOnExit();
    processor.resize(file, targetFile, width, height);
    return targetFile;
  }

  private static class Dimension
  {
    int width;
    int height;

    Dimension(int width, int height) {
      this.width = width;
      this.height = height;
    }
  }

  private static Map<String, Dimension> DIMENSIONS = new HashMap<>();
  static {
    DIMENSIONS.put("icon", new Dimension(96, 96));
    DIMENSIONS.put("trivia", new Dimension(500, 0));
    DIMENSIONS.put("cover", new Dimension(0, 500));
    DIMENSIONS.put("featured", new Dimension(560, 0));
    DIMENSIONS.put("contextual", new Dimension(920, 560));
  }
}
