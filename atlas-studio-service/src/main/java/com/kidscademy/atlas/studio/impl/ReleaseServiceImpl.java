package com.kidscademy.atlas.studio.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
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
import com.kidscademy.atlas.studio.model.AtlasItem;
import com.kidscademy.atlas.studio.model.Project;
import com.kidscademy.atlas.studio.model.Release;
import com.kidscademy.atlas.studio.model.ReleaseItem;
import com.kidscademy.atlas.studio.util.Files;

import js.io.VariablesWriter;
import js.rmi.BusinessException;
import js.util.Classes;

public class ReleaseServiceImpl implements ReleaseService {
    private final Application application;
    private final AtlasDao dao;

    public ReleaseServiceImpl(AtlasDao dao) {
	this.application = Application.instance();
	this.dao = dao;
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
    public Release saveRelease(Release release) throws IOException {
	File releaseDir = Files.releaseDir(release.getName());
	if (!releaseDir.exists()) {
	    // create release project on the fly, first time newly release is saved
	    // maybe a more natural approach would be to use separated interfaces for
	    // 'create' and 'save'

	    Map<String, String> variables = new HashMap<>();
	    variables.put("prj", release.getName());
	    variables.put("package", release.getPackageName());

	    BufferedReader reader = new BufferedReader(Classes.getResourceAsReader("/release/layout"));
	    boolean useVariables = false;
	    String path = null;
	    while ((path = reader.readLine()) != null) {
		useVariables = path.charAt(0) == '!';
		if (useVariables) {
		    path = path.substring(1);
		}

		File file = new File(releaseDir, path);
		File dir = file.getParentFile();
		if (!dir.exists() && !dir.mkdirs()) {
		    throw new IOException("Cannot create parent directory for file " + file);
		}

		if (useVariables) {
		    Writer writer = new VariablesWriter(new FileWriter(file), variables);
		    Files.copy(Classes.getResourceAsReader("/release/template/" + path), writer);
		} else {
		    Files.copy(Classes.getResourceAsStream("/release/template/" + path), file);
		}
	    }

	}

	dao.saveRelease(release);
	return release;
    }

    @Override
    public void removeRelease(int releaseId) throws IOException, BusinessException {
	BusinessRules.emptyRelease(releaseId);
	Release release = dao.getReleaseById(releaseId);
	File releaseDir = Files.releaseDir(release.getName());
	Files.removeFilesHierarchy(releaseDir);
	releaseDir.delete();
	dao.removeRelease(releaseId);
    }

    @Override
    public void createIcon() {
	// convert -size 512x512 -define gradient:center=192,256 -define
	// gradient:radii=384,384 radial-gradient:white-#1d4f82 background.png
	//
	// convert cover.png -resize 460x460 -gravity center -background none -extent
	// 512x512 image.png
	//
	// convert background.png image.png -compose over -composite icon.png
    }

    @Override
    public List<AtlasItem> getReleaseItems(int releaseId) {
	return dao.getReleaseItems(releaseId);
    }

    @Override
    public void addReleaseChild(int releaseId, int childId) throws IOException {
	dao.addReleaseChild(releaseId, childId);
	updateAtlasContent(releaseId);
    }

    @Override
    public void addReleaseChildren(int releaseId, List<Integer> childIds) throws IOException {
	dao.addReleaseChildren(releaseId, childIds);
	updateAtlasContent(releaseId);
    }

    @Override
    public void removeReleaseChild(int releaseId, int childId) throws IOException {
	dao.removeReleaseChild(releaseId, childId);
	updateAtlasContent(releaseId);
    }

    private void updateAtlasContent(int releaseId) throws IOException {
	Release release = dao.getReleaseById(releaseId);
	Project project = new Project(application, release.getName());
	File atlasDir = project.getAtlasDir();
	Files.removeFilesHierarchy(atlasDir);

	ExportTarget target = new FsExportTarget(atlasDir);
	Exporter exporter = new Exporter(dao, target, dao.getReleaseItems(releaseId));
	exporter.serialize(null);
    }
}
