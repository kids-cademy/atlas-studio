package com.kidscademy.atlas.studio.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.kidscademy.atlas.studio.util.Files;

@Entity
public class MediaFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private User user;

    /** Media type, also known as MIME type, as classified by IANA. */
    private String mediaType;

    private String fileName;

    private long fileSize;

    private Date uploadDate;

    private String license;

    private String description;

    private String source;

    @Transient
    private MediaSRC src;

    public MediaFile() {
    }

    /**
     * Test constructor.
     * 
     * @param src
     */
    public MediaFile(MediaSRC src) {
	this.user = new User(1);
	this.fileName = src.fileName();
	switch (Files.getExtension(this.fileName)) {
	case "jpg":
	    this.mediaType = "image/jpeg";
	    break;

	case "png":
	    this.mediaType = "image/png";
	    break;
	}
	this.src = src;
	this.uploadDate = new Date();
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public String getMediaType() {
	return mediaType;
    }

    public void setMediaType(String mediaType) {
	this.mediaType = mediaType;
    }

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    public long getFileSize() {
	return fileSize;
    }

    public void setFileSize(long fileSize) {
	this.fileSize = fileSize;
    }

    public Date getUploadDate() {
	return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
	this.uploadDate = uploadDate;
    }

    public String getLicense() {
	return license;
    }

    public void setLicense(String license) {
	this.license = license;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getSource() {
	return source;
    }

    public void setSource(String source) {
	this.source = source;
    }

    public MediaSRC getSrc() {
	return src;
    }

    public void setSrc(MediaSRC src) {
	this.src = src;
    }

    public int getId() {
	return id;
    }

    public void postLoad(AtlasObject object) {
	src = Files.mediaSrc(object, fileName);
    }

    public void postMerge(MediaFile source) {
	fileName = source.src != null ? source.src.fileName() : null;
    }

    public enum Type {
	IMAGE, AUDIO, VIDEO
    }
}
