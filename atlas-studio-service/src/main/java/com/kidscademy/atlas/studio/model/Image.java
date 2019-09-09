package com.kidscademy.atlas.studio.model;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.kidscademy.atlas.studio.tool.ImageProcessor;
import com.kidscademy.atlas.studio.util.Files;

import js.core.Factory;

@Entity
@Cacheable
public class Image {
    public static final String TYPE_ICON = "icon";
    public static final String TYPE_COVER = "cover";
    public static final String TYPE_FEATURED = "featured";
    public static final String TYPE_CONTEXTUAL = "contextual";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /** Picture name, unique per atlas object. */
    private String name;

    private Date uploadDate;

    private String source = "";

    private String caption;

    /**
     * Picture file name.
     * <p>
     * This fields contains only file name, including extension, but does not
     * contain any path components. This is to avoid keeping path structure into
     * database. Path components are added when object is loaded from persistence by
     * a specialized hook, from every concrete object class. Media file name plus
     * path components are a root-relative URL and is named media SRC; remember that
     * root-relative URL is the URL path after server that starts from context.
     */
    private String fileName;

    private int fileSize;

    private int width;

    private int height;

    @Transient
    private MediaSRC src;

    public Image() {
    }

    /**
     * Test constructor.
     * 
     * @param src
     */
    public Image(MediaSRC src) {
	this.src = src;
	this.name = Files.basename(src.fileName());
	this.uploadDate = new Date();
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Date getUploadDate() {
	return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
	this.uploadDate = uploadDate;
    }

    public String getSource() {
	return source;
    }

    public void setSource(String source) {
	this.source = source;
    }

    public String getCaption() {
	return caption;
    }

    public void setCaption(String caption) {
	this.caption = caption;
    }

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    public int getFileSize() {
	return fileSize;
    }

    public void setFileSize(int fileSize) {
	this.fileSize = fileSize;
    }

    public int getWidth() {
	return width;
    }

    public void setWidth(int width) {
	this.width = width;
    }

    public int getHeight() {
	return height;
    }

    public void setHeight(int height) {
	this.height = height;
    }

    public void setSrc(MediaSRC src) {
	this.src = src;
    }

    public MediaSRC getSrc() {
	return src;
    }

    public void postLoad(AtlasObject object) {
	src = Files.mediaSrc(object, fileName);
    }

    public void postMerge(Image source) {
	if (fileName == null && source.src != null) {
	    fileName = source.src.fileName();
	}
    }

    public void updateIcon(AtlasItem object) throws IOException {
	if (TYPE_ICON.equals(name)) {
	    File pictureFile = Files.mediaFile(object, fileName);
	    File iconFile = icon(object, fileName);
	    ImageProcessor image = Factory.getInstance(ImageProcessor.class);
	    image.resize(pictureFile, iconFile, 96, 96);
	}
    }

    public void removeIcon(AtlasItem object) throws IOException {
	if (TYPE_ICON.equals(name)) {
	    File icon = icon(object, fileName);
	    if (icon.exists() && !icon.delete()) {
		throw new IOException(String.format("Unable to remove icon file |%s|.", icon.getName()));
	    }
	}
    }

    private static File icon(AtlasItem object, String fileName) {
	StringBuilder iconName = new StringBuilder();
	iconName.append(Files.basename(fileName));
	iconName.append("_96x96");
	iconName.append('.');
	iconName.append(Files.getExtension(fileName));
	return Files.mediaFile(object, iconName.toString());
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Image other = (Image) obj;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return name;
    }
}
