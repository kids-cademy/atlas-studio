package com.kidscademy.atlas.studio.export;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.Image;
import com.kidscademy.atlas.studio.model.RepositoryObject;
import com.kidscademy.atlas.studio.search.SearchIndexProcessor;
import com.kidscademy.atlas.studio.tool.ConvertProcess;
import com.kidscademy.atlas.studio.tool.IdentifyProcess;
import com.kidscademy.atlas.studio.tool.ImageProcessor;
import com.kidscademy.atlas.studio.tool.ImageProcessorImpl;

import js.json.Json;
import js.tiny.container.http.ContentType;
import js.tiny.container.mvc.View;
import js.util.Classes;
import js.util.Strings;

public class AtlasCollectionExportView implements View {
    private final AtlasDao atlasDao;
    private final Json json;
    private final List<ExportItem> items;

    public AtlasCollectionExportView(AtlasDao atlasDao, Json json) {
	this.atlasDao = atlasDao;
	this.json = json;
	this.items = atlasDao.getAllExportItems();
    }

    public AtlasCollectionExportView(AtlasDao atlasDao, Json json, int collectionId) {
	this.atlasDao = atlasDao;
	this.json = json;
	this.items = atlasDao.getCollectionExportItems(collectionId);
    }

    @Override
    public View setModel(Object model) {
	throw new UnsupportedOperationException();
    }

    @Override
    public void serialize(HttpServletResponse httpResponse) throws IOException {
	httpResponse.setContentType(ContentType.TEXT_PLAIN.getValue());
	serialize(httpResponse.getOutputStream());
    }

    public void serialize(OutputStream stream) throws IOException {
	ZipOutputStream zip = new ZipOutputStream(stream);
	SearchIndexProcessor processor;
	try {
	    processor = new SearchIndexProcessor();
	} catch (NoSuchMethodException e) {
	    throw new IOException(e);
	}

	// uses linked hash map to preserve insertion order
	Map<String, ExportItem> itemsMap = new LinkedHashMap<>(items.size());
	for (int index = 0; index < items.size(); ++index) {
	    final ExportItem item = items.get(index);
	    item.setIndex(index);
	    item.setIconPath(Util.path(item.getName(), item.getIconName()));
	    itemsMap.put(item.getName(), item);
	}

	for (ExportItem item : items) {
	    AtlasObject atlasObject = atlasDao.getAtlasObject(item.getId());
	    ExportObject exportObject = new ExportObject(atlasObject);
	    exportObject.setIndex(item.getIndex());
	    processor.createDirectIndex(exportObject);

	    for (String relatedName : atlasObject.getRelated()) {
		ExportItem relatedItem = itemsMap.get(relatedName);
		if (relatedItem != null) {
		    exportObject.addRelated(new ExportRelatedObject(relatedItem));
		}
	    }

	    ZipEntry entry = entry(exportObject.getName(), "object_en.json");
	    zip.putNextEntry(entry);
	    zip.write(json.stringify(exportObject).getBytes("UTF-8"));
	    zip.closeEntry();

	    addEntry(zip, item, atlasObject.getSampleName());
	    addEntry(zip, exportObject.getName(), "icon.jpg", pictureFile(atlasObject, "icon", 96, 96));
	    addEntry(zip, exportObject.getName(), "contextual.jpg", pictureFile(atlasObject, "contextual", 920, 560));
	    addEntry(zip, exportObject.getName(), "cover.png", pictureFile(atlasObject, "cover", 560, 0));
	    addEntry(zip, exportObject.getName(), "featured.png", pictureFile(atlasObject, "featured", 560, 0));
	}

	ZipEntry entry = new ZipEntry("atlas/search-index.json");
	zip.putNextEntry(entry);
	zip.write(json.stringify(processor.updateSearchIndex()).getBytes("UTF-8"));
	zip.closeEntry();

	entry = new ZipEntry("atlas/objects-list.json");
	zip.putNextEntry(entry);
	zip.write(json.stringify(itemsMap.keySet()).getBytes("UTF-8"));
	zip.closeEntry();

	zip.close();
    }

    private static void addEntry(ZipOutputStream zip, ExportItem object, String fileName) throws IOException {
	// test null file name here in order to simplify invoker logic
	if (fileName != null) {
	    addEntry(zip, object.getName(), fileName, file(object, fileName));
	}
    }

    private static void addEntry(ZipOutputStream zip, String objectName, String fileName, File file)
	    throws IOException {
	// test null file here in order to simplify invoker logic
	if (file == null || !file.exists()) {
	    return;
	}
	ZipEntry entry = entry(objectName, fileName);
	zip.putNextEntry(entry);
	copy(file, zip);
	zip.closeEntry();
    }

    private static File pictureFile(AtlasObject object, String imageKey, int width, int height) throws IOException {
	Image picture = object.getImage(imageKey);
	if (picture == null) {
	    return null;
	}
	File file = file(object, picture.getFileName());
	if (!file.exists()) {
	    return null;
	}

	// TODO: hard coded values
	Classes.setFieldValue(ConvertProcess.class, "BIN", "C://Program Files/ImageMagick-6.9.9-Q16-HDRI/convert.exe");
	Classes.setFieldValue(IdentifyProcess.class, "BIN",
		"C://Program Files/ImageMagick-6.9.9-Q16-HDRI/identify.exe");
	ImageProcessor processor = new ImageProcessorImpl();

	File targetFile = File.createTempFile("picture", picture.getFileName());
	targetFile.deleteOnExit();
	processor.resize(file, targetFile, width, height);
	return targetFile;
    }

    private static ZipEntry entry(String objectName, String fileName) {
	return new ZipEntry(Strings.concat("atlas/", objectName, '/', fileName));
    }

    public static File file(RepositoryObject object, String fileName) {
	// TODO: hard coded value
	return new File(
		Strings.concat("D:\\runtime\\kids-cademy\\webapps\\media\\atlas\\" + object.getRepositoryName() + "\\",
			object.getName(), '\\', fileName));
    }

    public static long copy(File file, OutputStream outputStream) throws IOException, IllegalArgumentException {
	InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
	outputStream = new BufferedOutputStream(outputStream);

	long bytes = 0;
	try {
	    byte[] buffer = new byte[4096];
	    int length;
	    while ((length = inputStream.read(buffer)) != -1) {
		bytes += length;
		outputStream.write(buffer, 0, length);
	    }
	} finally {
	    inputStream.close();
	    outputStream.flush();
	}
	return bytes;
    }
}
