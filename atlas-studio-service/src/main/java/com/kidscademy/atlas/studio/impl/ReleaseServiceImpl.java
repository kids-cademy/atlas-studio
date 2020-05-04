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

import com.kidscademy.atlas.studio.BusinessRules;
import com.kidscademy.atlas.studio.ReleaseService;
import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.export.ExportTarget;
import com.kidscademy.atlas.studio.export.Exporter;
import com.kidscademy.atlas.studio.export.FsExportTarget;
import com.kidscademy.atlas.studio.model.AndroidApp;
import com.kidscademy.atlas.studio.model.AndroidProject;
import com.kidscademy.atlas.studio.model.AtlasItem;
import com.kidscademy.atlas.studio.model.Image;
import com.kidscademy.atlas.studio.model.Release;
import com.kidscademy.atlas.studio.model.ReleaseItem;
import com.kidscademy.atlas.studio.tool.AndroidTools;
import com.kidscademy.atlas.studio.tool.ImageInfo;
import com.kidscademy.atlas.studio.tool.ImageProcessor;
import com.kidscademy.atlas.studio.util.Files;
import com.kidscademy.atlas.studio.util.Html2Md;

import js.dom.Document;
import js.dom.DocumentBuilder;
import js.dom.EList;
import js.dom.Element;
import js.format.LongDate;
import js.io.VariablesWriter;
import js.lang.AsyncTask;
import js.lang.BugError;
import js.rmi.BusinessException;
import js.tiny.container.http.form.Form;
import js.util.Classes;
import js.util.TextTemplate;

public class ReleaseServiceImpl implements ReleaseService {
    private final AtlasDao dao;
    private final AndroidTools androidTools;
    private final ImageProcessor imageProcessor;
    private final BusinessRules businessRules;

    public ReleaseServiceImpl(AtlasDao dao, AndroidTools androidTools, ImageProcessor imageProcessor,
	    BusinessRules businessRules) {
	this.dao = dao;
	this.androidTools = androidTools;
	this.imageProcessor = imageProcessor;
	this.businessRules = businessRules;
    }

    @Override
    public List<ReleaseItem> getReleases() {
	return dao.getReleases();
    }

    @Override
    public Release createRelease() throws IOException {
	return Release.create();
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
	if (!release.isPersisted()) {
	    release.setContentTimestamp(new Date());
	    loadReleasePolicyFiles(release);
	    createReleaseGraphics(release);
	} else {
	    Release currentRelease = dao.getReleaseById(release.getId());
	    if (!currentRelease.getName().equals(release.getName())) {
		File currentDir = Files.mediaDir(currentRelease);
		File newDir = Files.mediaDir(release);
		currentDir.renameTo(newDir);
	    }
	    if (!currentRelease.getGraphicsBackground().equals(release.getGraphicsBackground())) {
		createReleaseGraphics(release);
	    }
	}

	Files.mediaFile(release, Image.KEY_ICON, "96x96").delete();
	dao.saveRelease(release);
	return release;
    }

    @Override
    public void clearRelease(int releaseId) {
	dao.removeReleaseChildren(releaseId);
    }

    @Override
    public void removeRelease(int releaseId) throws IOException, BusinessException {
	businessRules.emptyRelease(releaseId);

	AndroidApp app = dao.getAndroidAppByRelease(releaseId);
	if (app != null) {
	    Files.removeFilesHierarchy(AndroidProject.appDir(app.getName())).delete();
	    dao.removeAndroidApp(app.getId());
	}

	Release release = dao.getReleaseById(releaseId);
	Files.removeFilesHierarchy(Files.mediaDir(release)).delete();

	dao.removeRelease(releaseId);
    }

    @Override
    public void updateReleaseGraphics(int releaseId, String background) throws IOException {
	Release release = dao.getReleaseById(releaseId);
	release.setGraphicsBackground(background);
	createReleaseGraphics(release);
	dao.saveRelease(release);
    }

    @Override
    public Image uploadReleaseImage(Form imageForm) throws IOException, BusinessException {
	File imageFile = imageForm.getUploadedFile("media-file").getFile();
	businessRules.transparentImage(Image.KEY_RELEASE, imageFile);

	Release release = getAtlasReleaseByForm(imageForm);
	ImageInfo imageInfo = imageProcessor.getImageInfo(imageFile);
	businessRules.imageDimensions(Math.min(imageInfo.getWidth(), imageInfo.getHeight()), 558);
	int size = Math.max(imageInfo.getWidth(), imageInfo.getHeight());

	File targetFile = Files.mediaFile(release, Image.KEY_RELEASE);
	targetFile.getParentFile().mkdirs();
	targetFile.delete();

	imageProcessor.exec("${imageFile} -resize ${size}x${size} -gravity center -background none ${targetFile}",
		imageFile, size, size, targetFile);

	Image image = new Image();
	image.setImageKey(Image.KEY_RELEASE);
	image.setUploadDate(new Date());
	image.setSource(imageForm.getValue("source"));
	image.setFileName(targetFile.getName());

	image.setFileSize(imageInfo.getFileSize());
	image.setWidth(imageInfo.getWidth());
	image.setHeight(imageInfo.getHeight());
	image.setSrc(Files.mediaSrc(release, Image.KEY_RELEASE));

	createReleaseGraphics(release);
	return image;
    }

    private Release getAtlasReleaseByForm(Form mediaForm) {
	String objectId = mediaForm.getValue("object-id");
	if (objectId == null) {
	    throw new BugError("Media form should have <object-id> field.");
	}
	try {
	    return dao.getReleaseById(Integer.parseInt(objectId));
	} catch (NumberFormatException unused) {
	    throw new BugError("Media form <object-id> field should be numeric |%s|.", objectId);
	}
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
	return dao.getAndroidAppByName(releaseName);
    }

    @Override
    public AndroidApp createAndroidApp(String releaseName) {
	Release release = dao.getReleaseByName(releaseName);
	return AndroidApp.create(release);
    }

    @Override
    public AndroidApp getAndroidApp(int appId) {
	return dao.getAndroidAppById(appId);
    }

    @Override
    public AndroidApp updateAndroidApp(final AndroidApp app) throws IOException {
	if (app.getId() == 0) {
	    dao.saveAndroidApp(app);
	}

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

		final Release release = app.getRelease();
		copy(release.getReadme(), new File(appDir, "README.md"));
		copy(release.getPrivacy(), new File(appDir, "PRIVACY.md"));

		copy(release, "icon", appDir, "app/src/main/res/drawable/ic_app.png", 96, 96);
		copy(release, "icon", appDir, "app/src/main/res/drawable-hdpi/ic_app.png", 192, 192);
		copy(release, "icon", appDir, "app/src/main/res/drawable-xhdpi/ic_app.png", 384, 384);

		copy(release, "cover", appDir, "app/src/main/res/drawable/cover_page.png", 240, 240);
		copy(release, "cover", appDir, "app/src/main/res/drawable-hdpi/cover_page.png", 480, 480);
		copy(release, "cover", appDir, "app/src/main/res/drawable-xhdpi/cover_page.png", 788, 788);

		updateAndroidAppContent(app.getId());

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

    private void copy(Release release, String imageKey, File appDir, String imagePath, int width, int height)
	    throws IOException {
	File releaseImage = Files.mediaFile(release, imageKey);
	if (!releaseImage.exists()) {
	    return;
	}
	File appImage = new File(appDir, imagePath);
	appImage.getParentFile().mkdirs();
	imageProcessor.resize(releaseImage, appImage, width, height);
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
	AndroidApp app = updateAndroidAppContent(appId);
	androidTools.buildAPK(app.getDir());
    }

    @Override
    public void buildSignedAndroidApp(int appId) throws IOException {
	AndroidApp app = updateAndroidAppContent(appId);
	androidTools.buildSignedAPK(app.getDir());
    }

    @Override
    public void buildAndroidBundle(int appId) throws IOException {
	AndroidApp app = updateAndroidAppContent(appId);
	androidTools.buildBundle(app.getDir());
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

    // --------------------------------------------------------------------------------------------

    private void loadReleasePolicyFiles(Release release) throws IOException {
	TextTemplate readme = new TextTemplate(Classes.getResourceAsString("templates/README.xml"));
	readme.put("app-name", release.getDisplay());
	readme.put("app-definition", release.getDefinition());
	release.setReadme(readme.toString());

	TextTemplate privacy = new TextTemplate(Classes.getResourceAsString("templates/PRIVACY.xml"));
	privacy.put("app-name", release.getDisplay());
	release.setPrivacy(privacy.toString());
    }

    private void createReleaseGraphics(Release release) throws IOException {
	String background = release.getGraphicsBackground();

	File iconFile = Files.mediaFile(release, "icon");
	iconFile.delete();
	imageProcessor.exec(
		"-size 512x512 -define gradient:center=192,256 -define gradient:radii=384,384 radial-gradient:white-#${background} ${iconFile}",
		background, iconFile);

	File featureFile = Files.mediaFile(release, "feature");
	featureFile.delete();
	imageProcessor.exec(
		"-size 1024x500 -define gradient:center=192,256 -define gradient:radii=700,384 radial-gradient:white-#${background} ${featureFile}",
		background, featureFile);

	File coverFile = Files.mediaFile(release, "cover");
	coverFile.delete();
	imageProcessor.exec( //
		"-size 788x788 " + // overall size
			"xc:none -fill #${background} -draw \"circle 394,394 394,1\" " + // first layer is a solid
											 // circle
			"-define gradient:center=192,256 -define gradient:radii=384,384 radial-gradient:white-#${background} "
			+ // second layer is gradient
			"-compose srcin -composite ${coverFile}", // compose
		background, background, coverFile);

	File releaseFile = Files.mediaFile(release, "release");
	if (releaseFile.exists()) {
	    File imageFile = Files.mediaFile(release, "image");
	    imageProcessor.exec(
		    "${releaseFile} -resize 460x460 -gravity center -background none -extent 512x512 ${imageFile}",
		    releaseFile, imageFile);
	    imageProcessor.exec("${iconFile} ${imageFile} -compose over -composite ${iconFile}", iconFile, imageFile,
		    iconFile);
	    imageProcessor.exec("${featureFile} ${imageFile} -compose over -composite ${featureFile}", featureFile,
		    imageFile, featureFile);

	    imageProcessor.exec(
		    "${releaseFile} -resize 540x540 -gravity center -background none -extent 558x558 ${imageFile}",
		    releaseFile, imageFile);
	    imageProcessor.exec("${coverFile} ${imageFile} -gravity center -compose over -composite ${coverFile}",
		    coverFile, imageFile, coverFile);

	    imageFile.delete();
	}
    }

    private AndroidApp updateAndroidAppContent(int appId) throws IllegalArgumentException, IOException {
	AndroidApp app = dao.getAndroidAppById(appId);
	AndroidProject prj = new AndroidProject(app);
	File atlasDir = prj.getAtlasDir();
	if (atlasDir.lastModified() < app.getRelease().getContentTimestamp().getTime()) {
	    Files.removeFilesHierarchy(atlasDir);
	    ExportTarget target = new FsExportTarget(atlasDir);
	    Exporter exporter = new Exporter(dao, target, dao.getReleaseItems(app.getRelease().getId()));
	    exporter.serialize(null);
	}
	return app;
    }
}
