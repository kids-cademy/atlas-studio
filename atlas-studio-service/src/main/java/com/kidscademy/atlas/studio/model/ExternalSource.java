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

import com.kidscademy.atlas.studio.ApiService;
import com.kidscademy.atlas.studio.util.Files;
import com.kidscademy.atlas.studio.util.Strings;

import js.dom.Document;
import js.dom.DocumentBuilder;
import js.dom.Element;
import js.lang.BugError;
import js.log.Log;
import js.log.LogFactory;
import js.tiny.container.annotation.TestConstructor;
import js.util.Classes;

@Entity
public class ExternalSource implements GraphicObject, Domain {
    private static final Log log = LogFactory.getLog(ExternalSource.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Transient
    private final String title = "External Source";

    @Transient
    private String name;

    @Transient
    private String definition;

    private Date timestamp;
    private String home;

    /**
     * URL base domain contains only domain and TLD. For example for
     * <code>www.softschools.com</code> links this domain value is
     * <code>softschools.com</code>.
     */
    private String domain;

    /**
     * Link name displayed on user interface. May be subject of
     * internationalization.
     */
    private String display;

    /**
     * Short description about linked resource content. This is a template used to
     * instantiate concrete link definition, see
     * {@link #getLinkDefinition(URL, String)}.
     */
    private String definitionTemplate;

    /**
     * Designators list of import APIs available for this external source. APIs are
     * provided by back-end services, see {@link ApiService} and an external source
     * may implement only part of them.
     * <p>
     * This field contains a list of comma separated strings. If there is no import
     * API support for an external source this field is empty.
     */
    private String apis;

    /**
     * Root-relative media SRC for link icon. This value is initialized when load
     * link from database with some contextual path and icon file name from
     * {@link #iconName}.
     */
    @Transient
    private MediaSRC iconSrc;

    public ExternalSource() {

    }

    @TestConstructor
    public ExternalSource(String home, String display) {
	this.id = 0;
	this.home = home;
	this.domain = Strings.basedomain(home);
	this.name = Files.basename(this.domain);
	this.display = display;
    }

    @TestConstructor
    public ExternalSource(int id, String home, String definitionTemplate, String apis) {
	this.id = id;
	this.home = home;
	this.domain = Strings.basedomain(home);
	this.name = Files.basename(this.domain);
	this.display = Strings.toTitleCase(this.name);
	this.definitionTemplate = definitionTemplate;
	this.definition = this.definitionTemplate;
    }

    @PostLoad
    public void postLoad() {
	name = Files.basename(domain);
	definition = definitionTemplate;
	iconSrc = Files.mediaSrc(this);
    }

    @PostRemove
    public void postRemove() throws IOException {
	File file = Files.mediaFile(this);
	if (!file.delete()) {
	    log.warn("Cannot remove external source icon file |%s|.", file);
	}
    }

    @Override
    public int getId() {
	return id;
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

    @Override
    public String getDomain() {
	return domain;
    }

    public boolean isPersisted() {
	return id != 0;
    }

    public String getHome() {
	return home;
    }

    public Date getTimestamp() {
	return timestamp;
    }

    public String getDefinitionTemplate() {
	return definitionTemplate;
    }

    public String getApis() {
	return apis;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((domain == null) ? 0 : domain.hashCode());
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
	ExternalSource other = (ExternalSource) obj;
	if (domain == null) {
	    if (other.domain != null)
		return false;
	} else if (!domain.equals(other.domain))
	    return false;
	return true;
    }

    public String getLinkDefinition(URL link, String objectDisplay) {
	StringBuilder definitionBuilder = new StringBuilder();
	StringBuilder variableBuilder = new StringBuilder();

	State state = State.TEXT;
	for (int i = 0; i < definitionTemplate.length(); ++i) {
	    char c = definitionTemplate.charAt(i);
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
	    method = Classes.getMethod(ExternalSource.class, methodName, URL.class);
	    instance = this;
	    parameters = new Object[] { link };
	    break;

	case "display":
	    method = Classes.getMethod(String.class, methodName);
	    instance = display;
	    parameters = new Object[0];
	    break;

	default:
	    throw new BugError("Invalid syntax on external source definition.");
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

    public static ExternalSource create() {
	ExternalSource externalSource = new ExternalSource();
	externalSource.apis = "";
	return externalSource;
    }
}
