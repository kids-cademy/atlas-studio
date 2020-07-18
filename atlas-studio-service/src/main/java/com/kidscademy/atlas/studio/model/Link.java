package com.kidscademy.atlas.studio.model;

import java.net.URL;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.kidscademy.atlas.studio.util.Files;

import js.lang.Displayable;
import js.util.Params;

@Embeddable
public class Link implements Displayable, Domain {
    @ManyToOne
    private LinkSource linkSource;

    /**
     * Link URL to external source document. That document should provide data about
     * advertised features, see {@link #apis}.
     */
    private URL url;

    /**
     * Link name displayed on user interface. May be subject of
     * internationalization.
     */
    @Transient
    private String display;

    /**
     * Brief link description displayed on user interface. Usually is created
     * automatically from external source definition template or loaded from article
     * URL. Anyway, user is free to change it at will.
     */
    private String definition;

    /**
     * Root-relative media SRC for link icon. This value is initialized when load
     * link from database with some contextual path and icon file name from
     * {@link #iconName}.
     */
    @Transient
    private MediaSRC iconSrc;

    @Transient
    private String iconPath;

    public Link() {
    }

    public Link(LinkSource linkSource, URL url, String definition) {
	Params.notNull(linkSource, "Link source");
	Params.notNull(linkSource.getExternalSource(), "External source");
	this.linkSource = linkSource;
	this.url = url;
	this.definition = definition;
    }

    public void postLoad() {
	display = linkSource.getExternalSource().getDisplay();
	iconSrc = Files.mediaSrc(this);
    }

    public void setLinkSource(LinkSource linkSource) {
        this.linkSource = linkSource;
    }

    public LinkSource getLinkSource() {
        return linkSource;
    }

    public void setIconSrc(MediaSRC iconSrc) {
	this.iconSrc = iconSrc;
    }

    public URL getUrl() {
	return url;
    }

    public ExternalSource getExternalSource() {
	return linkSource.getExternalSource();
    }

    @Override
    public String getDomain() {
	return linkSource.getExternalSource().getDomain();
    }

    @Override
    public String toDisplay() {
	return display;
    }

    public String getDefinition() {
	return definition;
    }

    public MediaSRC getIconSrc() {
	return iconSrc;
    }

    /**
     * Convenient way to access path component of this link URL.
     * 
     * @return URL path component.
     * @see URL#getPath()
     */
    public String getPath() {
	return url.getPath();
    }

    /**
     * Convenient way to access file name from path component of this link URL.
     * Returned file name may include extension. It is the last path component, that
     * is, component after last path separator. If URL path ends with path separator
     * returns empty string.
     * 
     * @return file name from URL path, possible empty.
     */
    public String getFileName() {
	String path = url.getPath();
	int lastPathseparator = path.lastIndexOf('/') + 1;
	if (lastPathseparator >= path.length()) {
	    return "";
	}
	return path.substring(lastPathseparator);
    }

    public String getBasename() {
	return Files.basename(url.getPath());
    }

    /**
     * Return a comma separated list of import APIs provided by this link source.
     * 
     * @return this link features.
     * @see #apis
     */
    public String getApis() {
	return linkSource.getApis();
    }

    public void setIconPath(String iconPath) {
	this.iconPath = iconPath;
    }

    public String getIconPath() {
	return iconPath;
    }

    @Override
    public String toString() {
	return url != null ? url.toExternalForm() : "null";
    }

    public static Link create(LinkSource linkSource, URL articleURL, String definition) {
	Link link = new Link(linkSource, articleURL, definition);
	link.postLoad();
	return link;
    }
}
