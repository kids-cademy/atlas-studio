package com.kidscademy.atlas.studio;

import com.kidscademy.atlas.studio.model.Login;

import js.tiny.container.annotation.Controller;
import js.tiny.container.annotation.Public;
import js.tiny.container.http.Resource;
import js.tiny.container.mvc.View;

@Controller
@Public
public interface AtlasController {
    Resource login(Login login);
    
    View exportAllAtlasCollections();

    View exportAtlasCollection(int collectionId);
}
