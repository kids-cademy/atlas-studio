package com.kidscademy.atlas.studio.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import com.kidscademy.atlas.studio.AtlasController;
import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.export.AtlasCollectionExportView;
import com.kidscademy.atlas.studio.model.AndroidProject;
import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.util.Files;

import js.tiny.container.http.ContentType;
import js.tiny.container.http.Resource;
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
    public Resource apk(final String name) {
	return new Resource() {
	    @Override
	    public void serialize(HttpServletResponse httpResponse) throws IOException {
		AndroidProject project = new AndroidProject(name);
		File apkFile = project.getApkDebugFile();
		if (!apkFile.exists()) {
		    httpResponse.sendError(404);
		    return;
		}

		InputStream apkStream = new FileInputStream(apkFile);
		try {
		    httpResponse.setContentType(ContentType.APPLICATION_STREAM.getValue());
		    httpResponse.setContentLength((int) apkFile.length());
		    Files.copy(apkStream, httpResponse.getOutputStream());
		} finally {
		    apkStream.close();
		}
	    }
	};
    }
}
