package com.kidscademy.atlas.studio.tool;

import java.util.List;

public interface AtlasDao {

    void saveObject(AtlasObject object);

    List<AtlasObject> findPublishedObjects(String string);

}
