package com.kidscademy.atlas.studio.tool;

import java.util.Date;

public class Image {
    public static final String TYPE_ICON = "icon";
    public static final String TYPE_COVER = "cover";
    public static final String TYPE_FEATURED = "featured";
    public static final String TYPE_CONTEXTUAL = "contextual";

    private String name;
    private Date uploadDate;
    private String source = "";
    private String caption;
    private String fileName;
    private int fileSize;
    private int width;
    private int height;

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
}
