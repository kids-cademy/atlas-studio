package com.kidscademy.atlas.studio.impl;

import com.kidscademy.atlas.studio.AtlasController;
import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.export.AtlasCollectionExportView;
import com.kidscademy.atlas.studio.model.Login;

import js.json.Json;
import js.tiny.container.core.AppContext;
import js.tiny.container.http.Resource;
import js.tiny.container.mvc.Redirect;
import js.tiny.container.mvc.View;

public class AtlasControllerImpl implements AtlasController {
    private final AppContext context;
    private final AtlasDao atlasDao;
    private final Json json;

    public AtlasControllerImpl(AppContext context, AtlasDao atlasDao, Json json) {
	this.context = context;
	this.atlasDao = atlasDao;
	this.json = json;
    }

    @Override
    public Resource login(Login login) {
	if (!context.login(login.getEmailAddress(), login.getPassword())) {
	    return new Redirect("home.htm");
	}
	return new Redirect("library.htm");
    }

    @Override
    public View exportAllAtlasCollections() {
	return new AtlasCollectionExportView(atlasDao, json);
    }

    @Override
    public View exportAtlasCollection(int collectionId) {
	return new AtlasCollectionExportView(atlasDao, json, collectionId);
    }
}
