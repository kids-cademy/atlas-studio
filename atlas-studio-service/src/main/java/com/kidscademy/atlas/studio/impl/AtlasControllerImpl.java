package com.kidscademy.atlas.studio.impl;

import com.kidscademy.atlas.studio.AtlasController;
import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.export.AtlasCollectionExportView;

import js.json.Json;
import js.mvc.View;

public class AtlasControllerImpl implements AtlasController {
    private final AtlasDao atlasDao;
    private final Json json;

    public AtlasControllerImpl(AtlasDao atlasDao, Json json) {
	this.atlasDao = atlasDao;
	this.json = json;
    }

    @Override
    public View exportAtlasCollection(int collectionId) {
	return new AtlasCollectionExportView(atlasDao, json, collectionId);
    }
}
