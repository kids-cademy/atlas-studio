package com.kidscademy.atlas.studio.impl;

import java.io.File;

import com.kidscademy.atlas.studio.AtlasController;
import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.export.AtlasCollectionExportView;
import com.kidscademy.atlas.studio.model.AndroidProject;
import com.kidscademy.atlas.studio.model.AtlasObject;

import js.tiny.container.http.ContentType;
import js.tiny.container.mvc.FileResource;
import js.tiny.container.mvc.View;

public class AtlasControllerImpl implements AtlasController {
    private final AtlasDao atlasDao;

    public AtlasControllerImpl(AtlasDao atlasDao) {
	this.atlasDao = atlasDao;
    }

    @Override
    public View exportAllAtlasCollections() {
	return new AtlasCollectionExportView(atlasDao);
    }

    @Override
    public View exportAtlasCollection(int collectionId, AtlasObject.State state) {
	return new AtlasCollectionExportView(atlasDao, collectionId, state);
    }

    @Override
    public FileResource exportAndroidApk(String name) {
	AndroidProject project = new AndroidProject(name);
	File apkFile = project.getApkDebugFile();
	return new FileResource(apkFile, ContentType.APPLICATION_STREAM);
    }

    @Override
    public FileResource exportAndroidBundle(String name) {
	AndroidProject project = new AndroidProject(name);
	File apkFile = project.getBundleReleaseFile();
	return new FileResource(apkFile, ContentType.APPLICATION_STREAM);
    }
}
