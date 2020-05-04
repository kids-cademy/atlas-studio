package com.kidscademy.atlas.studio.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.kidscademy.atlas.studio.Application;

import js.dom.Document;
import js.dom.DocumentBuilder;
import js.dom.Element;
import js.lang.BugError;
import js.util.Classes;
import js.util.Strings;

public class AndroidProject {
    /** Strings resources XML file path, relative to project root. */
    private static final String PATH_STRINGS = "app/src/main/res/values/strings.xml";

    private static final String DIR_ATLAS = "app/src/main/assets/atlas";

    private static final String PATH_APK_DEBUG = "app/build/outputs/apk/debug/app-debug.apk";
    private static final String PATH_APK_RELEASE_UNSIGNED = "app/build/outputs/apk/release/app-release-unsigned.apk";

    /** Project location on file system, absolute path. */
    private final File location;
    private final String name;

    private final File apkDebugFile;
    private final File apkReleaseUnsignedFile;

    private Document strings;

    public AndroidProject(String name) {
	this.location = new File(appsDir(), name);
	this.name = name;

	this.apkDebugFile = new File(location, PATH_APK_DEBUG);
	this.apkReleaseUnsignedFile = new File(location, PATH_APK_RELEASE_UNSIGNED);

	DocumentBuilder builder = Classes.loadService(DocumentBuilder.class);
	try {
	    this.strings = builder.loadXML(getStringValuesFile());
	} catch (IllegalArgumentException | FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public String getName() {
	return name;
    }

    public String getValue(String name) {
	if (strings == null) {
	    return null;
	}
	return strings.getByXPath("//string[@name='%s']", name).getText();
    }

    public Map<String, String> getProprties() {
	if (strings == null) {
	    return Collections.emptyMap();
	}
	Map<String, String> properties = new HashMap<>();
	for (Element value : strings.findByTag("string")) {
	    properties.put(value.getAttr("name"), value.getText());
	}
	return properties;
    }

    public void setProperties(Map<String, String> properties) throws IOException {
	if (strings == null || properties == null) {
	    return;
	}
	for (Map.Entry<String, String> entry : properties.entrySet()) {
	    Element valueEl = strings.getByAttr("name", entry.getKey());
	    if (valueEl == null) {
		throw new BugError("Invalid release properties. Missing value name |%s|.", entry.getKey());
	    }
	    valueEl.setText(Strings.escapeXML(entry.getValue()));
	}
	strings.serialize(new FileWriter(getStringValuesFile()), true);
    }

    public File getAtlasDir() {
	return new File(location, DIR_ATLAS);
    }

    public File getAtlasObjectDir(String objectName) {
	return new File(getAtlasDir(), objectName);
    }

    public File getApkDebugFile() {
	return apkDebugFile;
    }

    public File getApkReleaseUnsignedFile() {
	return apkReleaseUnsignedFile;
    }

    public void setReadme(String description) {
	// TODO convert description HTML to MD and write to README.md file

    }

    public void setPrivacy(String policy) {
	// TODO convert HTML to TXT and write to PRIVACY file

    }

    // --------------------------------------------------------------------------------------------

    private File getStringValuesFile() {
	return new File(location, PATH_STRINGS);
    }

    private static File appsDir;

    private static final Object APPS_DIR_MUTEX = new Object();

    public static File appsDir() {
	if (appsDir == null) {
	    synchronized (APPS_DIR_MUTEX) {
		if (appsDir == null) {
		    appsDir = Application.instance().getProperty("android.apps.path", File.class);
		}
	    }
	}
	return appsDir;
    }

    public static File appDir(String appName) {
	return new File(appsDir(), appName);
    }
}
