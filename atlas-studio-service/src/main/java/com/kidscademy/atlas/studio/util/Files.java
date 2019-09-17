package com.kidscademy.atlas.studio.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.kidscademy.atlas.studio.model.CollectionItem;
import com.kidscademy.atlas.studio.model.MediaSRC;

import js.util.Params;
import js.util.Strings;

public final class Files extends js.util.Files {
    private Files() {
    }

    public static MediaSRC mediaSrc(CollectionItem collecitonItem, String mediaFile) {
	return mediaSrc(collecitonItem.getCollectionName(), collecitonItem.getName(), mediaFile);
    }

    public static MediaSRC mediaSrc(CollectionItem collectionItem, String mediaFile, String annotation) {
	String basepath = Files.removeExtension(mediaFile);
	String extension = Files.getExtension(mediaFile);
	return mediaSrc(collectionItem.getCollectionName(), collectionItem.getName(),
		Strings.concat(basepath, '_', annotation, '.', extension));
    }

    public static MediaSRC mediaSrc(String collectionName, String objectName, String mediaFile) {
	Params.notNullOrEmpty(collectionName, "Collection name");
	Params.notNullOrEmpty(objectName, "Object name");
	if (mediaFile == null) {
	    return null;
	}
	return new MediaSRC(Strings.concat("/media/atlas/", collectionName, "/", objectName, '/', mediaFile));
    }

    public static MediaSRC linkSrc(String iconName) {
	return new MediaSRC("/media/link/" + iconName);
    }

    public static MediaSRC collectionSrc(String iconName) {
	return new MediaSRC("/media/collection/" + iconName);
    }

    private static File REPOSIOTRY_DIR = new File(System.getProperty("catalina.base") + "/webapps");

    public static File mediaDir(CollectionItem collectionItem) {
	return new File(REPOSIOTRY_DIR, Strings.concat("/media/atlas/", collectionItem.getCollectionName(), "/",
		collectionItem.getName(), '/'));
    }

    public static File mediaFile(MediaSRC mediaSrc) {
	// repository dir := ${catalina.base}/webapps
	// media SRC := /media/atlas/instrument/object/file
	return new File(REPOSIOTRY_DIR, mediaSrc.value());
    }

    public static File mediaFile(CollectionItem object, String mediaFileName) {
	// repository dir := ${catalina.base}/webapps
	// path := /media/atlas/instrument/object/file
	return new File(REPOSIOTRY_DIR, Files.mediaSrc(object, mediaFileName).value());
    }

    /**
     * Get media file path with given base name and extension. Returned file is not
     * tested for existence.
     * 
     * @param collectionItem
     *            object owning requested media file,
     * @param basename
     *            base name for media file,
     * @param extension
     *            media file extension.
     * @return media file path.
     */
    public static File mediaFile(CollectionItem collectionItem, String basename, String extension) {
	String fileName = Strings.concat(basename, '.', extension);
	return new File(REPOSIOTRY_DIR, Files.mediaSrc(collectionItem, fileName).value());
    }

    private static final Map<String, String> MEDIA_TYPES = new HashMap<>();
    static {
	MEDIA_TYPES.put("jpg", "image/jpeg");
	MEDIA_TYPES.put("jpeg", "image/jpeg");
	MEDIA_TYPES.put("png", "image/png");
    }

    public static String probeContentType(String path) {
	return MEDIA_TYPES.get(getExtension(path));
    }
}
