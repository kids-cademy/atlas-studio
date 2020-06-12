package com.kidscademy.atlas.studio;

import com.kidscademy.atlas.studio.model.AtlasObject;

import js.tiny.container.annotation.Controller;
import js.tiny.container.annotation.Public;
import js.tiny.container.mvc.FileResource;
import js.tiny.container.mvc.View;

@Controller
public interface AtlasController {
    View exportAllAtlasCollections();

    View exportAtlasCollection(int collectionId, AtlasObject.State state);

    @Public
    FileResource exportAndroidApk(String name);

    FileResource exportAndroidBundle(String name);
}
