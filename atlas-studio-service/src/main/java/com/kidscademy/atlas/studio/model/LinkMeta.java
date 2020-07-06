package com.kidscademy.atlas.studio.model;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.PostRemove;
import javax.persistence.Transient;

import com.kidscademy.atlas.studio.util.Files;
import com.kidscademy.atlas.studio.util.Strings;

import js.dom.Document;
import js.dom.DocumentBuilder;
import js.dom.Element;
import js.lang.BugError;
import js.log.Log;
import js.log.LogFactory;
import js.util.Classes;

@Entity
public class LinkMeta implements Domain, GraphicObject {
    private static final Log log = LogFactory.getLog(LinkMeta.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Transient
    private final String title = "External Source";

    private Date timestamp;
    private String home;
    private String domain;
    private String display;
    private String definition;
    private String features;

    @Transient
    private String name;
    
    /**
     * Root-relative media SRC for link icon. This value is initialized when load
     * link from database with some contextual path and icon file name from
     * {@link #iconName}.
     */
    @Transient
    private MediaSRC iconSrc;

    public LinkMeta() {

    }

    public LinkMeta(String domain, String display, String definition) {
	this.domain = domain;
	this.display = display;
	this.definition = definition;
    }

    @PostLoad
    public void postLoad() {
	name = domain;
	iconSrc = Files.mediaSrc(this);
    }

    @PostRemove
    public void postRemove() throws IOException {
	File file = Files.mediaFile(this);
	if (!file.delete()) {
	    log.warn("Cannot remove link meta icon file |%s|.", file);
	}
    }

    public int getId() {
	return id;
    }

    public boolean isPersisted() {
	return id != 0;
    }

    public String getHome() {
        return home;
    }

    @Override
    public String getDomain() {
	return domain;
    }

    @Override
    public Date getTimestamp() {
	return timestamp;
    }

    @Override
    public String getTitle() {
	return title;
    }

    @Override
    public String getName() {
	return name;
    }

    @Override
    public String getDisplay() {
	return display;
    }

    @Override
    public String getDefinition() {
	return definition;
    }

    @Override
    public MediaSRC getIconSrc() {
	return iconSrc;
    }

    public String getFeatures() {
	return features;
    }

    public static LinkMeta create() {
	LinkMeta linkMeta = new LinkMeta();
	return linkMeta;
    }

    public String getLinkDefinition(URL link, String objectDisplay) {
	StringBuilder definitionBuilder = new StringBuilder();
	StringBuilder variableBuilder = new StringBuilder();

	State state = State.TEXT;
	for (int i = 0; i < definition.length(); ++i) {
	    char c = definition.charAt(i);
	    switch (state) {
	    case TEXT:
		if (c == '$') {
		    state = State.VAR_MARK;
		    break;
		}
		definitionBuilder.append(c);
		break;

	    case VAR_MARK:
		if (c != '{') {
		    definitionBuilder.append('$');
		    definitionBuilder.append(c);
		    state = State.TEXT;
		    break;
		}
		state = State.VAR_BODY;
		variableBuilder.setLength(0);
		break;

	    case VAR_BODY:
		if (c == '}') {
		    definitionBuilder.append(evaluateVariable(variableBuilder.toString(), link, objectDisplay));
		    state = State.TEXT;
		    break;
		}
		variableBuilder.append(c);
		break;

	    default:
		throw new BugError("Not handled state |%s|.", state);
	    }
	}
	return definitionBuilder.toString();
    }

    private String evaluateVariable(String variable, URL link, String display) {
	List<String> parts = Strings.split(variable, '.');
	if (parts.size() == 1) {
	    return display;
	}

	String methodName = parts.get(1);
	Method method = null;
	Object instance = null;
	Object[] parameters = null;

	switch (parts.get(0)) {
	case "API":
	    method = Classes.getMethod(LinkMeta.class, methodName, URL.class);
	    instance = this;
	    parameters = new Object[] { link };
	    break;

	case "display":
	    method = Classes.getMethod(String.class, methodName);
	    instance = display;
	    parameters = new Object[0];
	    break;

	default:
	    throw new BugError("Invalid syntax on link meta definition.");
	}

	try {
	    return (String) method.invoke(instance, parameters);
	} catch (Exception e) {
	    throw new BugError(e);
	}
    }

    @SuppressWarnings("unused") // invoked reflexively
    private String getYouTubeTitle(URL youtubeLink) {
	try {
	    URL url = new URL(
		    "https://noembed.com/embed?url=" + URLEncoder.encode(youtubeLink.toExternalForm(), "UTF-8"));
	    YouTubeResult result = Strings.load(url, YouTubeResult.class);
	    return result != null ? result.title : null;
	} catch (IOException e) {
	    return null;
	}
    }

    @SuppressWarnings("unused") // invoked reflexively
    private String getWikiHowTitle(URL wikihowLink) {
	DocumentBuilder builder = Classes.loadService(DocumentBuilder.class);
	Document document = builder.loadHTML(wikihowLink);
	Element element = document.getByXPath("//H1[@id='section_0']/A");
	return element != null ? element.getText() : null;
    }

    private enum State {
	TEXT, VAR_MARK, VAR_BODY
    }

    private static class YouTubeResult {
	String title;
    }
}
