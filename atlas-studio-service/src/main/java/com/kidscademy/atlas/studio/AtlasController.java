package com.kidscademy.atlas.studio;

import com.kidscademy.atlas.studio.model.AtlasObject;

import js.tiny.container.annotation.Controller;
import js.tiny.container.http.Resource;
import js.tiny.container.mvc.View;

@Controller
public interface AtlasController {
    View exportAllAtlasCollections();

    View exportAtlasCollection(int collectionId, AtlasObject.State state);

    Resource apk(String name);
}
