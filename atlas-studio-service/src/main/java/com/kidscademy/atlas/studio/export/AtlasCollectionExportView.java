package com.kidscademy.atlas.studio.export;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.model.AtlasItem;
import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.Image;
import com.kidscademy.atlas.studio.model.Link;
import com.kidscademy.atlas.studio.model.RepositoryObject;
import com.kidscademy.atlas.studio.tool.ConvertProcess;
import com.kidscademy.atlas.studio.tool.IdentifyProcess;
import com.kidscademy.atlas.studio.tool.ImageProcessor;
import com.kidscademy.atlas.studio.tool.ImageProcessorImpl;

import js.http.ContentType;
import js.json.Json;
import js.mvc.View;
import js.util.Classes;
import js.util.Strings;

public class AtlasCollectionExportView implements View {
    private final AtlasDao atlasDao;
    private final Json json;
    private final int collectionId;

    public AtlasCollectionExportView(AtlasDao atlasDao, Json json, int collectionId) {
	this.atlasDao = atlasDao;
	this.json = json;
	this.collectionId = collectionId;
    }

    @Override
    public View setModel(Object model) {
	return null;
    }

    @Override
    public void serialize(HttpServletResponse httpResponse) throws IOException {
	httpResponse.setContentType(ContentType.TEXT_PLAIN.getValue());
	serialize(httpResponse.getOutputStream());
    }

    public void serialize(OutputStream stream) throws IOException {
	ZipOutputStream zip = new ZipOutputStream(stream);
	List<AtlasItem> items = atlasDao.getCollectionItemsByState(collectionId, AtlasObject.State.PUBLISHED);
	SearchIndexProcessor processor = new SearchIndexProcessor(items);

	List<String> objectNames = new ArrayList<>(items.size());
	for (AtlasItem item : items) {
	    objectNames.add(item.getName());
	}

	for (AtlasItem item : items) {
	    AtlasObject object = atlasDao.getAtlasObject(item.getId());
	    processor.createDirectIndex(object);

	    List<String> related = new ArrayList<>();
	    for (String relatedName : object.getRelated()) {
		if (objectNames.contains(relatedName)) {
		    related.add(relatedName);
		}
	    }
	    object.setRelated(related);

	    object.setSamplePath(path(object.getName(), object.getSampleName()));
	    for (Image image : object.getImages().values()) {
		image.setPath(path(object.getName(), image.getFileName()));
	    }
	    for (Link link : object.getLinks()) {
		link.setIconPath(Strings.concat("links/", link.getIconName()));
	    }

	    ZipEntry entry = entry(object.getName(), "object_en.json");
	    zip.putNextEntry(entry);
	    zip.write(json.stringify(object).getBytes("UTF-8"));
	    zip.closeEntry();

	    addEntry(zip, object, object.getSampleName());
	    addEntry(zip, object.getName(), "icon.jpg", pictureFile(object, "icon", 96, 96));
	    addEntry(zip, object.getName(), "contextual.jpg", pictureFile(object, "contextual", 920, 560));
	    addEntry(zip, object.getName(), "cover.png", pictureFile(object, "cover", 560, 0));
	    addEntry(zip, object.getName(), "featured.png", pictureFile(object, "featured", 560, 0));
	}

	ZipEntry entry = new ZipEntry("atlas/search-index.json");
	zip.putNextEntry(entry);
	zip.write(json.stringify(processor.createSearchIndex()).getBytes("UTF-8"));
	zip.closeEntry();

	Collections.sort(objectNames);
	entry = new ZipEntry("atlas/objects-list.json");
	zip.putNextEntry(entry);
	zip.write(json.stringify(objectNames).getBytes("UTF-8"));
	zip.closeEntry();

	zip.close();
    }

    private static void addEntry(ZipOutputStream zip, AtlasObject object, String fileName) throws IOException {
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

    private static File pictureFile(AtlasObject object, String pictureName, int width, int height) throws IOException {
	Image picture = object.getImage(pictureName);
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

    private static String path(String objectName, String fileName) {
	return String.format("atlas/%s/%s", objectName, fileName);
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
