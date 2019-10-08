package com.kidscademy.atlas.studio;

import js.annotation.Controller;
import js.annotation.Public;
import js.mvc.View;

@Controller
@Public
public interface AtlasController {
    View exportAllAtlasCollections();

    View exportAtlasCollection(int collectionId);
}
