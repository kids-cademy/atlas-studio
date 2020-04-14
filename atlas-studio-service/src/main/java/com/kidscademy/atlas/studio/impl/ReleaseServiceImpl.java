package com.kidscademy.atlas.studio.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kidscademy.atlas.studio.Application;
import com.kidscademy.atlas.studio.BusinessRules;
import com.kidscademy.atlas.studio.ReleaseService;
import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.export.ExportTarget;
import com.kidscademy.atlas.studio.export.Exporter;
import com.kidscademy.atlas.studio.export.FsExportTarget;
import com.kidscademy.atlas.studio.model.AndroidApp;
import com.kidscademy.atlas.studio.model.AndroidProject;
import com.kidscademy.atlas.studio.model.AtlasItem;
import com.kidscademy.atlas.studio.model.Release;
import com.kidscademy.atlas.studio.model.ReleaseItem;
import com.kidscademy.atlas.studio.tool.AndroidTools;
import com.kidscademy.atlas.studio.util.Files;
import com.kidscademy.atlas.studio.util.Html2Md;

import js.dom.Document;
import js.dom.DocumentBuilder;
import js.dom.EList;
import js.dom.Element;
import js.format.LongDate;
import js.io.VariablesWriter;
import js.lang.AsyncTask;
import js.rmi.BusinessException;
import js.util.Classes;

public class ReleaseServiceImpl implements ReleaseService {
    private final Application application;
    private final AtlasDao dao;
    private final AndroidTools androidTools;

    public ReleaseServiceImpl(AtlasDao dao, AndroidTools androidTools) {
	this.application = Application.instance();
	this.dao = dao;
	this.androidTools = androidTools;
    }

    @Override
    public List<ReleaseItem> getReleases() {
	return dao.getReleases();
    }

    @Override
    public Release createRelease() {
	Release release = new Release();
	return release;
    }

    @Override
    public Release getRelease(int releaseId) {
	return dao.getReleaseById(releaseId);
    }

    @Override
    public Release getReleaseByName(String releaseName) {
	return dao.getReleaseByName(releaseName);
    }

    @Override
    public Release saveRelease(Release release) throws IOException {
	dao.saveRelease(release);
	return release;
    }

    @Override
    public void removeRelease(int releaseId) throws IOException, BusinessException {
	BusinessRules.emptyRelease(releaseId);
	dao.removeRelease(releaseId);
    }

    @Override
    public void createIcon() {
	// ICON
	// convert -size 512x512 -define gradient:center=192,256 -define
	// gradient:radii=384,384 radial-gradient:white-#3eb12f background.png
	//
	// convert cover.png -resize 460x460 -gravity center -background none -extent
	// 512x512 image.png
	//
	// convert background.png image.png -compose over -composite icon.png

	// FEATURE
	// convert -size 1024x500 -define gradient:center=192,256 -define
	// gradient:radii=700,384 radial-gradient:white-#3eb12f feature-bg.png
	//
	// convert feature-bg.png image.png -compose over -composite feature.png

    }

    @Override
    public List<AtlasItem> getReleaseItems(int releaseId) {
	return dao.getReleaseItems(releaseId);
    }

    @Override
    public void addReleaseChild(int releaseId, int childId) throws IOException {
	dao.addReleaseChild(releaseId, childId);
    }

    @Override
    public void addReleaseChildren(int releaseId, List<Integer> childIds) throws IOException {
	dao.addReleaseChildren(releaseId, childIds);
    }

    @Override
    public void removeReleaseChild(int releaseId, int childId) throws IOException {
	dao.removeReleaseChild(releaseId, childId);
    }

    @Override
    public AndroidApp getAndroidAppForRelease(String releaseName) {
	// by convention application have the same name as parent release
	AndroidApp app = dao.getAndroidAppByName(releaseName);
	if (app != null) {
	    return app;
	}
	Release release = dao.getReleaseByName(releaseName);
	return AndroidApp.create(release);
    }

    @Override
    public AndroidApp getAndroidApp(int appId) {
	return dao.getAndroidAppById(appId);
    }

    @Override
    public AndroidApp updateAndroidApp(final AndroidApp app) throws IOException {
	final File appDir = app.getDir();
	final boolean createProject = !appDir.exists();

	if (createProject) {
	    if (!appDir.mkdir()) {
		throw new IOException("Cannot create application directory " + appDir);
	    }

	    BufferedReader layoutDescriptor = new BufferedReader(Classes.getResourceAsReader("/android-app/layout"));
	    String path = null;
	    while ((path = layoutDescriptor.readLine()) != null) {
		if (path.charAt(0) == '!') {
		    path = path.substring(1);
		}
		File targetFile = new File(appDir, path);
		File targetDir = targetFile.getParentFile();
		if (!targetDir.exists() && !targetDir.mkdirs()) {
		    throw new IOException("Cannot create parent directory for target file " + targetFile);
		}
		Files.copy(Classes.getResourceAsStream("/android-app/template/" + path), targetFile);
	    }
	}

	dao.saveAndroidApp(app);

	AsyncTask<Void> task = new AsyncTask<Void>() {
	    @Override
	    protected Void execute() throws Throwable {

		Map<String, String> variables = new HashMap<>();
		variables.put("update-date", new LongDate().format(new Date()));
		variables.put("project", app.getName());
		variables.put("package", app.getPackageName());
		variables.put("version-code", Integer.toString(app.getVersionCode()));
		variables.put("version-name", app.getRelease().getVersion());
		variables.put("app_name", app.getDisplay());
		variables.put("app_logotype", app.getRelease().getBrief());
		variables.put("app_definition", app.getDefinition());
		variables.put("publisher", app.getRelease().getPublisher());
		variables.put("edition", app.getRelease().getEdition());
		variables.put("license", app.getRelease().getLicense());

		BufferedReader layoutDescriptor = new BufferedReader(
			Classes.getResourceAsReader("/android-app/layout"));
		String path = null;
		while ((path = layoutDescriptor.readLine()) != null) {
		    if (path.charAt(0) != '!') {
			continue;
		    }
		    path = path.substring(1);

		    String sourcePath = "/android-app/template/" + path;
		    File targetFile = new File(appDir, path);
		    Writer writer = new VariablesWriter(new FileWriter(targetFile), variables);
		    Files.copy(Classes.getResourceAsReader(sourcePath), writer);
		}

		copy(app.getRelease().getReadme(), new File(appDir, "README.md"));
		copy(app.getRelease().getPrivacy(), new File(appDir, "PRIVACY.md"));

		AndroidProject project = new AndroidProject(application, app.getName());
		File atlasDir = project.getAtlasDir();
		Files.removeFilesHierarchy(atlasDir);

		ExportTarget target = new FsExportTarget(atlasDir);
		Exporter exporter = new Exporter(dao, target, dao.getReleaseItems(app.getRelease().getId()));
		exporter.serialize(null);

		if (createProject) {
		    androidTools.initLocalGitRepository(app);
		}
		return null;
	    }
	};
	task.start();

	return app;
    }

    private static void copy(String html, File file) throws IOException {
	Html2Md html2md = new Html2Md(html);
	Files.copy(new StringReader(html2md.converter()), new FileWriter(file));
    }

    @Override
    public void removeAndroidApp(int appId) throws IOException {
	AndroidApp app = dao.getAndroidAppById(appId);
	File appDir = AndroidProject.appDir(app.getName());
	Files.removeFilesHierarchy(appDir);
	appDir.delete();
	dao.removeAndroidApp(appId);
    }

    @Override
    public void cleanAndroidProject(int appId) throws IOException {
	AndroidApp app = dao.getAndroidAppById(appId);
	androidTools.cleanProject(app.getDir());
    }

    @Override
    public void buildAndroidApp(int appId) throws IOException {
	AndroidApp app = dao.getAndroidAppById(appId);
	androidTools.build(app.getDir());
    }

    @Override
    public Map<String, String> getAndroidStoreListing(int appId) {
	AndroidApp app = dao.getAndroidAppById(appId);

	StringBuilder readme = new StringBuilder();
	DocumentBuilder builder = Classes.loadService(DocumentBuilder.class);
	Document doc = builder.loadXML(new StringReader(app.getRelease().getReadme()));
	for (Element element : doc.getRoot().getChildren()) {
	    if (!element.getTag().equals("table")) {
		readme.append(element.getText());
	    } else {
		EList rows = element.findByTag("tr");
		for (int i = 1;;) {
		    readme.append("- ");
		    readme.append(rows.item(i).getFirstChild().getText().trim());
		    if (++i == rows.size()) {
			break;
		    }
		    readme.append("\r\n");
		}
	    }
	    readme.append("\r\n\r\n");
	}

	Map<String, String> listing = new HashMap<>();
	listing.put("title", app.getDisplay());
	listing.put("shortDescription", app.getRelease().getBrief());
	listing.put("fullDescription", readme.toString());
	listing.put("website", "http://kids-cademy.com/");
	listing.put("email", "contact@kids-cademy.com");
	listing.put("privacy", app.getGitRepository().toExternalForm().replace(".git", "/") + "blob/master/PRIVACY.md");
	return listing;
    }
}
