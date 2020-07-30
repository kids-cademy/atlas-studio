package com.kidscademy.atlas.studio.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.kidscademy.atlas.studio.model.Domain;
import com.kidscademy.atlas.studio.model.Key;
import com.kidscademy.atlas.studio.model.MediaSRC;
import com.kidscademy.atlas.studio.model.Release;
import com.kidscademy.atlas.studio.model.RepositoryObject;

import js.io.VariablesWriter;
import js.log.Log;
import js.log.LogFactory;
import js.tiny.container.annotation.ContextParam;
import js.util.Classes;
import js.util.Params;
import js.util.Strings;

public final class Files extends js.util.Files {
    private static Log log = LogFactory.getLog(Files.class);

    @ContextParam("media.repository.path")
    private static File REPOSITORY_DIR;

    private Files() {
    }

    public static MediaSRC mediaSrc(RepositoryObject object, String mediaFile) {
	return mediaSrc(object.getRepositoryName(), object.getName(), mediaFile);
    }

    public static MediaSRC mediaSrc(RepositoryObject collectionItem, String mediaFile, String annotation) {
	String basepath = Files.removeExtension(mediaFile);
	String extension = Files.getExtension(mediaFile);
	return mediaSrc(collectionItem.getRepositoryName(), collectionItem.getName(),
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

    public static MediaSRC mediaSrc(Domain link) {
	return new MediaSRC(mediaPath(link));
    }

    public static File mediaFile(Domain link, String... variant) {
	// repository dir := ${catalina.base}/webapps
	return new File(REPOSITORY_DIR, mediaPath(link, variant));
    }

    private static String mediaPath(Domain link, String... variant) {
	StringBuilder builder = new StringBuilder();
	builder.append("/media/link/");
	builder.append(Files.basename(link.getDomain()));
	if (variant.length == 1) {
	    builder.append('_');
	    builder.append(variant[0]);
	}
	builder.append(".png");
	return builder.toString();
    }

    public static MediaSRC mediaSrc(Key collection) {
	return new MediaSRC(mediaPath(collection));
    }

    public static File mediaFile(Key collection, String... variant) {
	// repository dir := ${catalina.base}/webapps
	// media SRC := /media/collection/${collection.iconName}
	return new File(REPOSITORY_DIR, mediaPath(collection, variant));
    }

    private static String mediaPath(Key collection, String... variant) {
	StringBuilder builder = new StringBuilder();
	builder.append("/media/collection/");
	builder.append(collection.getName());
	if (variant.length == 1) {
	    builder.append('_');
	    builder.append(variant[0]);
	}
	builder.append(".png");
	return builder.toString();
    }

    public static File mediaDir(Release release) {
	return new File(REPOSITORY_DIR, Strings.concat("/media/release/", release.getName(), '/'));
    }

    public static File mediaFile(Release release, String imageKey, String... variant) {
	File file = new File(REPOSITORY_DIR, mediaPath(release, imageKey, variant));
	File dir = file.getParentFile();
	if (dir != null && !dir.exists()) {
	    dir.mkdirs();
	}
	return file;
    }

    public static MediaSRC mediaSrc(Release release, String imageKey) {
	return new MediaSRC(mediaPath(release, imageKey));
    }

    private static String mediaPath(Release release, String imageKey, String... variant) {
	StringBuilder builder = new StringBuilder();
	builder.append("/media/release/");
	builder.append(release.getName());
	builder.append('/');
	builder.append(imageKey);
	if (variant.length == 1) {
	    builder.append('_');
	    builder.append(variant[0]);
	}
	builder.append(".png");
	return builder.toString();
    }

    public static File repositoryDir(String repositoryName) {
	return new File(REPOSITORY_DIR, Strings.concat("/media/atlas/", repositoryName, '/'));
    }

    public static File objectDir(RepositoryObject object) {
	return objectDir(object.getRepositoryName(), object.getName());
    }

    public static File objectDir(String repositoryName, String objectName) {
	return new File(REPOSITORY_DIR, Strings.concat("/media/atlas/", repositoryName, "/", objectName, '/'));
    }

    public static File mediaFile(MediaSRC mediaSrc) {
	// repository dir := ${catalina.base}/webapps
	// media SRC := /media/atlas/collection/object/file
	return new File(REPOSITORY_DIR, mediaSrc.value());
    }

    public static File mediaFile(RepositoryObject object, String mediaFileName, String... variant) {
	// repository dir := ${catalina.base}/webapps
	// path := /media/atlas/collection/object/file
	return new File(REPOSITORY_DIR, Files.mediaPath(object, mediaFileName, variant));
    }

    private static String mediaPath(RepositoryObject object, String mediaFileName, String... variant) {
	StringBuilder builder = new StringBuilder();
	builder.append("/media/atlas/");
	builder.append(object.getRepositoryName());
	builder.append('/');
	builder.append(object.getName());
	builder.append('/');
	builder.append(Files.basename(mediaFileName));
	if (variant.length == 1) {
	    builder.append('_');
	    builder.append(variant[0]);
	}
	builder.append('.');
	builder.append(Files.getExtension(mediaFileName));
	return builder.toString();
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
    public static File mediaFileExt(RepositoryObject collectionItem, String basename, String extension) {
	String fileName = Strings.concat(basename, '.', extension);
	return new File(REPOSITORY_DIR, Files.mediaSrc(collectionItem, fileName).value());
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

    public static void removeVariants(File imageFile) {
	// search image parent directory for image variants and remove all
	// images variants are generated on the fly using base image content
	// by convention images name uses underscore ('_') for variants separator

	final String imageVariantPattern = basename(imageFile) + "_";
	File[] variants = imageFile.getParentFile().listFiles(new FilenameFilter() {
	    @Override
	    public boolean accept(File dir, String name) {
		return name.contains(imageVariantPattern);
	    }
	});
	if (variants != null) {
	    for (File variant : variants) {
		if (!variant.delete()) {
		    log.error("Fail to delete image variant file |%s|.", variant);
		}
	    }
	}
    }

    public static void copy(String templateResource, Map<String, String> variables, Writer writer) throws IOException {
	Files.copy(Classes.getResourceAsReader(templateResource), new VariablesWriter(writer, variables));
    }
}
