package com.kidscademy.atlas.studio;

import js.tiny.container.annotation.Controller;
import js.tiny.container.annotation.Public;
import js.tiny.container.mvc.View;

@Controller
@Public
public interface AtlasController {
    View exportAllAtlasCollections();

    View exportAtlasCollection(int collectionId);
}
