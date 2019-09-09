package com.kidscademy.atlas.studio.impl;

import java.io.File;
import java.io.IOException;

import com.kidscademy.atlas.studio.model.MediaSRC;
import com.kidscademy.atlas.studio.model.CollectionItem;
import com.kidscademy.atlas.studio.util.Files;

import js.lang.BugError;
import js.util.Params;
import js.util.Strings;

/**
 * Media file transaction handler for chained processing. A media processing
 * transaction can span multiple transforms: every transform reads media from a
 * source file and stores result in a target file. Current transform source is
 * previous transform output so that transforms are chained. Transformed media
 * files are versioned incrementally. On commit latest version is copied into
 * original / base file and all versions removed. On roolback version is
 * decremented and file removed.
 * <p>
 * There are two abstractions to identify source and target: absolute
 * <code>file</code> and root relative URL named <code>path</code> in this
 * context. Files are used by media processors whereas paths are used by web
 * views to render media content. Root relative URL is starts with path
 * separator and media repository context. Absolute file is concatenated from
 * server document root directory and root relative URL.
 * <p>
 * Usage is straightforward: create handler instance and pass it to media
 * processor. Processor uses {@link #source()} to read media content and store
 * transform result to {@link #target()}. Please note that target file is not
 * created on file system at handler instance creation; it is created by media
 * processor and till processing complete it does not exists.
 * 
 * <pre>
 * MediaFileHandler handler = new MediaFileHandler(objectName, "sample.mp3");
 * // here handler.target() returns a valid file object but not existing on file
 * // system
 * audioProcessor.normalizeLevel(handler);
 * // after media processing completes target file is create and can be returned
 * // to user interface
 * return handler.targetPath();
 * </pre>
 * 
 * @author Iulian Rotaru
 */
public class MediaFileHandler {
    /** Absolute file used as source for media transform. */
    private File source;
    /** Root relative URL for source used by web views to render media content. */
    private MediaSRC sourceSrc;
    /**
     * Absolute file used as media transform target - where processing result is
     * stored. It does not actually exists on file system till processor creates it.
     */
    private File target;
    /**
     * Root relative URL for target used by web views to render media content. It
     * does not actually exists on file system till processor creates it.
     */
    private MediaSRC targetSrc;

    /** Base directory stores all source / target files and is absolute file. */
    private File baseDir;
    /**
     * Base path is the root relative URL without last segment, that is, without
     * file name.
     */
    private String basePath;

    /** Base name is the media file name without extension. */
    private String basename;
    /** Media file extension. */
    private String extension;
    /**
     * Last media processing version. Version is detecting by this handler
     * constructor by scanning all files with requested name and annotated with
     * version, e.g. <code>media-file_version.ext</code>. This property holds the
     * greatest version, that is, points to most recent processing. When a new
     * processing occurs on this media file handler, latest version is processing
     * source file and a new incremented version is created with processing result.
     * <p>
     * If there is not transform performed on this media file, but base file exists,
     * version is zero. If there is no media file with requested file name version
     * value is -1.
     */
    private int version;

    /**
     * Initialize internal state from given arguments and version value by scanning
     * file system.
     * 
     * @param collectionItem
     *            collection item owning media file,
     * @param mediaFile
     *            media file name.
     * @throws IllegalArgumentException
     *             if arguments are null or media file has no extension.
     */
    public MediaFileHandler(CollectionItem collectionItem, String mediaFile) {
	Params.notNull(collectionItem, "Collection item");
	MediaSRC mediaSrc = Files.mediaSrc(collectionItem.getCollectionName(), collectionItem.getName(), mediaFile);
	// infer base path from path in order to avoid hard coded path dependency
	basePath = mediaSrc.basePath();

	File file = Files.mediaFile(mediaSrc);
	baseDir = file.getParentFile();
	basename = Files.basename(mediaFile);
	extension = Files.getExtension(mediaFile);
	if (extension.isEmpty()) {
	    throw new IllegalArgumentException(
		    Strings.format("Invalid media file |%s|. Missing extension.", mediaFile));
	}

	// scan files system in order to detect version
	version = 0;
	while (file(baseDir, basename, version, extension).exists()) {
	    ++version;
	}
	// if base file is missing, that is, no media file at all version will become -1
	--version;
    }

    /**
     * Upload file to media repository and remove any existing version, if any.
     * Takes care to create parent directories.
     * 
     * @param file
     *            source file.
     * @throws IllegalArgumentException
     *             if <code>file</code> argument is not an existing regular file.
     * @throws IOException
     *             if IO operation fails.
     */
    public void upload(File file) throws IOException {
	Params.isFile(file, "Source file");
	File source = file(baseDir, basename, 0, extension);
	File parentDir = source.getParentFile();

	if (!parentDir.exists() && !parentDir.mkdirs()) {
	    throw new IOException(Strings.format("Fail to create parent directory |%s.|", parentDir));
	}

	if (source.exists()) {
	    delete();
	}
	// after delete() version is -1 since all media file were removed; take care to
	// initialize it to base file
	version = 0;

	if (!file.renameTo(source)) {
	    throw new IOException(Strings.format("Fail to move source file |%s| to |%s|", file, source));
	}
    }

    /**
     * Get absolute file for processing source. Source is the file from where
     * processor reads media content.
     * 
     * @return source absolute file.
     * @throws BugError
     *             if attempt to retrieve source file but not media file actually
     *             exists on file system.
     * @see #source
     */
    public File source() {
	if (version == -1) {
	    throw new BugError("Attempt to processs not existing media file.");
	}
	if (source == null) {
	    source = file(baseDir, basename, version, extension);
	}
	return source;
    }

    /**
     * Get root-relative URL, aka media SRC for processing source. Source is the
     * file from where processor reads media content. This media SRC is used by web
     * user interface to render media content.
     * 
     * @return root-relative URL for source file.
     * @throws BugError
     *             if attempt to retrieve source path but not media file actually
     *             exists on file system.
     * @see #sourceSrc
     */
    public MediaSRC sourceSrc() {
	if (version == -1) {
	    throw new BugError("Attempt to retrieve not existing media file.");
	}
	if (sourceSrc == null) {
	    sourceSrc = src(basePath, basename, version, extension);
	}
	return sourceSrc;
    }

    /**
     * Get absolute file for processing target. Target is the file on which
     * processor writes transformed media content. It does not actually exists on
     * file system till processor creates it.
     * 
     * @return target absolute file.
     * @throws BugError
     *             if attempt to retrieve target file but not media file actually
     *             exists on file system.
     * @see #target
     */
    public File target() {
	if (version == -1) {
	    throw new BugError("Attempt to processs not existing media file.");
	}
	if (target == null) {
	    target = file(baseDir, basename, version + 1, extension);
	}
	return target;
    }

    /**
     * Get root-relative URL, aka media SRC for processing target. Target is the
     * file on which processor writes transformed media content. This media SRC is
     * used by web user interface to render media content.
     * 
     * @return root-relative URL for target file.
     * @throws BugError
     *             if attempt to retrieve target path but not media file actually
     *             exists on file system.
     * @see #targetSrc
     */
    public MediaSRC targetSrc() {
	if (version == -1) {
	    throw new BugError("Attempt to retrieve not existing media file.");
	}
	if (targetSrc == null) {
	    targetSrc = src(basePath, basename, version + 1, extension);
	}
	return targetSrc;
    }

    /**
     * Not atomic commit media file transaction. This method copy latest version to
     * base file - base file is that with version zero, then remove all versions.
     * Reset {@link #version} value to zero. After this method execution all chained
     * transforms from transaction are stored into base file.
     * <p>
     * If there is no version files, that is, {@link #version} value is zero, this
     * method does nothing. If IO operation fails commit state is undefined.
     * 
     * @throws IOException
     *             if IO operation fail.
     */
    public void commit() throws IOException {
	if (version < 1) {
	    return;
	}

	File target = target();

	// latest version is target file, if exists on file system - created by media
	// processor
	// otherwise latest version is current source file
	File latestVersionFile = target;
	if (!latestVersionFile.exists()) {
	    latestVersionFile = source();
	}
	// copy latest version to base file, that is, with version 0
	Files.copy(latestVersionFile, file(baseDir, basename, 0, extension));

	if (target.exists() && !target.delete()) {
	    throw new IOException(String.format("Unable to remove media target file with version |%d|.", version + 1));
	}

	for (; version > 0; --version) {
	    if (!file(baseDir, basename, version, extension).delete()) {
		throw new IOException(String.format("Unable to remove media file with version |%d|.", version));
	    }
	}

	this.version = 0;
	this.source = null;
	this.target = null;
    }

    /**
     * Remove latest version source and target files from file system and decrease
     * this handler version by one. If there is no version files, that is,
     * {@link #version} value is zero, this method does nothing.
     * 
     * @throws IOException
     *             if file remove operation fails.
     */
    public void undo() throws IOException {
	if (version < 1) {
	    return;
	}

	File target = target();
	if (target.exists() && !target.delete()) {
	    throw new IOException(String.format("Unable to remove media target file with version |%d|.", version + 1));
	}
	if (!source().delete()) {
	    throw new IOException(String.format("Unable to remove media source file with version |%d|.", version));
	}

	--version;
	this.source = null;
	this.target = null;
    }

    /**
     * Remove all versioned files and leave only base.
     * 
     * @throws IOException
     *             if clear operation fails.
     */
    public void rollback() throws IOException {
	if (version < 1) {
	    return;
	}

	File target = target();
	if (target.exists() && !target.delete()) {
	    throw new IOException(String.format("Unable to remove media target file with version |%d|.", version + 1));
	}

	for (; version > 0; --version) {
	    if (!file(baseDir, basename, version, extension).delete()) {
		throw new IOException(String.format("Unable to remove media file with version |%d|.", version));
	    }
	}

	this.version = 0;
	this.source = null;
	this.target = null;
    }

    /**
     * Remove <code>ALL</code> files, including base version zero from file system
     * and reset version value. This method remove every file version, one by one in
     * sequence. If a file remove operation fails all lower versions are left on
     * file system; {@link #version} value is guaranteed to reflect this incomplete
     * remove. If this method is invoked after media processor has been created
     * target file, remove it too.
     * <p>
     * If there is no media file to delete this method does nothing.
     * 
     * @throws IOException
     *             if a file remove operation fails.
     */
    public void delete() throws IOException {
	if (version == -1) {
	    return;
	}

	File target = target();
	if (target.exists() && !target.delete()) {
	    throw new IOException(String.format("Unable to remove media target file with version |%d|.", version + 1));
	}

	for (; version >= 0; --version) {
	    if (!file(baseDir, basename, version, extension).delete()) {
		throw new IOException(String.format("Unable to remove media file with version |%d|.", version));
	    }
	}

	this.source = null;
	this.target = null;
    }

    // ----------------------------------------------------------------------------------------------
    // UTILITY FUNCTIONS

    private static File file(File dir, String basename, int version, String extension) {
	StringBuilder fileName = new StringBuilder();
	fileName.append(basename);
	if (version > 0) {
	    fileName.append('_');
	    fileName.append(version);
	}
	fileName.append('.');
	fileName.append(extension);
	return new File(dir, fileName.toString());
    }

    private static MediaSRC src(String basePath, String basename, int version, String extension) {
	StringBuilder path = new StringBuilder();
	path.append(basePath);
	path.append(basename);
	if (version > 0) {
	    path.append('_');
	    path.append(version);
	}
	path.append('.');
	path.append(extension);
	return new MediaSRC(path.toString());
    }
}
