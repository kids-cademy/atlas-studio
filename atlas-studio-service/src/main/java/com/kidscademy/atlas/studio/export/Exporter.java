package com.kidscademy.atlas.studio.export;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.kidscademy.atlas.studio.Application;
import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.model.AtlasItem;
import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.Image;
import com.kidscademy.atlas.studio.model.Theme;
import com.kidscademy.atlas.studio.search.SearchIndexProcessor;
import com.kidscademy.atlas.studio.tool.ConvertProcess;
import com.kidscademy.atlas.studio.tool.IdentifyProcess;
import com.kidscademy.atlas.studio.tool.ImageProcessor;
import com.kidscademy.atlas.studio.tool.ImageProcessorImpl;
import com.kidscademy.atlas.studio.util.Files;

import js.tiny.container.net.EventStreamManager;
import js.util.Classes;
import js.util.Strings;

public class Exporter {
    private final AtlasDao dao;
    private final ExportTarget target;
    private final Theme theme;
    private final List<ExportItem> items;

    /**
     * Exporter for application atlas content.
     * 
     * @param dao
     *            persistence layer,
     * @param target
     *            where to export, for now file system or ZIP file,
     * @param theme
     *            release theme,
     * @param items
     *            release items.
     */
    public Exporter(AtlasDao dao, ExportTarget target, Theme theme, List<AtlasItem> items) {
	this.dao = dao;
	this.target = target;
	this.items = new ArrayList<>(items.size());
	this.theme = theme;
	for (AtlasItem item : items) {
	    this.items.add(new ExportItem(item));
	}
    }

    /**
     * Exporter for preview reader.
     * 
     * @param dao
     *            persistence layer,
     * @param target
     *            where to export, for now file system or ZIP file,
     * @param items
     *            release items.
     */
    public Exporter(AtlasDao dao, ExportTarget target, List<ExportItem> items) {
	this.dao = dao;
	this.target = target;
	this.theme = Theme.CLASSIC;
	this.items = items;
    }

    /**
     * If underlying target does not require stream, invoker should explicitly set
     * it to null.
     * 
     * @param stream
     *            optional output stream, null if underlying target does not require
     *            it.
     * @throws IOException
     */
    public void serialize(OutputStream stream) throws IOException {
	serialize(stream, true);
    }

    public void serialize(OutputStream stream, boolean mediaProcessing) throws IOException {
	target.open(stream);
	try {
	    serialize(mediaProcessing);
	} catch (NoSuchMethodException e) {
	    throw new IOException(e);
	} finally {
	    target.close();
	}
    }

    private void serialize(boolean mediaProcessing) throws NoSuchMethodException, IOException {
	SearchIndexProcessor processor = new SearchIndexProcessor();

	// uses linked hash map to preserve insertion order
	Map<String, ExportItem> itemsMap = new LinkedHashMap<>(items.size());
	for (int index = 0; index < items.size(); ++index) {
	    final ExportItem item = items.get(index);
	    item.setIndex(index);
	    item.setIconPath(Util.path(item.getName(), item.getIconName()));
	    itemsMap.put(item.getName(), item);
	}

	for (ExportItem item : items) {
	    AtlasObject atlasObject = dao.getAtlasObject(item.getId());
	    ExportObject exportObject = new ExportObject(theme, atlasObject);
	    exportObject.setIndex(item.getIndex());
	    processor.createDirectIndex(exportObject);

	    for (String relatedName : atlasObject.getRelated()) {
		ExportItem relatedItem = itemsMap.get(relatedName);
		if (relatedItem != null) {
		    exportObject.addRelated(new ExportRelatedObject(relatedItem));
		}
	    }

	    target.write(exportObject, filePath(atlasObject, "object_en.json"));

	    if (mediaProcessing) {
		target.write(file(atlasObject, atlasObject.getSampleName()),
			filePath(atlasObject, atlasObject.getSampleName()));
		target.write(file(atlasObject, atlasObject.getWaveformName()),
			filePath(atlasObject, atlasObject.getWaveformName()));

		target.write(file(atlasObject, "icon", 96, 96), imagePath(atlasObject, "icon"));
		target.write(file(atlasObject, "trivia", 500, 0), imagePath(atlasObject, "trivia"));
		target.write(file(atlasObject, "cover", 0, 500), imagePath(atlasObject, "cover"));
		target.write(file(atlasObject, "featured", 560, 0), imagePath(atlasObject, "featured"));
		target.write(file(atlasObject, "contextual", 920, 560), imagePath(atlasObject, "contextual"));
	    }
	}

	target.write(processor.updateSearchIndex(), "search-index.json");
	target.write(itemsMap.keySet(), "objects-list.json");
    }

    private static String filePath(AtlasObject object, String fileName) {
	return Strings.concat(object.getName(), '/', fileName);
    }

    /**
     * Target path.
     * 
     * @param object
     * @param imageKey
     * @return
     */
    private static String imagePath(AtlasObject object, String imageKey) {
	Image image = object.getImage(imageKey);
	if (image == null) {
	    return null;
	}
	return Strings.concat(object.getName(), '/', image.getFileName());
    }

    /**
     * Source media file.
     * 
     * @param atlasObject
     * @param mediaFileName
     * @return
     */
    private static File file(AtlasObject atlasObject, String mediaFileName) {
	if (mediaFileName == null) {
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
    private static File file(AtlasObject object, String imageKey, int width, int height) throws IOException {
	Image image = object.getImage(imageKey);
	if (image == null) {
	    return null;
	}
	File file = Files.mediaFile(object, image.getFileName());
	if (!file.exists()) {
	    return null;
	}

	Application application = Application.instance();
	Classes.setFieldValue(ConvertProcess.class, "BIN", application.getProperty("image.magick.convert"));
	Classes.setFieldValue(IdentifyProcess.class, "BIN", application.getProperty("image.magick.identify"));
	ImageProcessor processor = new ImageProcessorImpl(
		application.getContext().getInstance(EventStreamManager.class));

	File targetFile = File.createTempFile("picture", image.getFileName());
	targetFile.deleteOnExit();
	processor.resize(file, targetFile, width, height);
	return targetFile;
    }
}
