package com.kidscademy.atlas.studio.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.Link;
import com.kidscademy.atlas.studio.model.Image;
import com.kidscademy.atlas.studio.model.AtlasItem;

public interface AtlasDao {
    /**
     * Persist or merge AtlasObject entity, depending on ID value. If ID is zero
     * instance is considered never created into database and
     * {@link EntityManager#persist(Object)} is enacted. Otherwise instance is
     * considered persisted but detached and {@link EntityManager#merge(Object)} is
     * performed.
     * <p>
     * AtlasObject entity has JPA event listener for {@link PostLoad} used to
     * initialize root-relative media SRC, used on web interface, from media file
     * names that are persisted. Also has {@link PrePersist} for reverse operation.
     * <p>
     * Because media SRC fields are {@link Transient}, merge is not able to detect
     * they are changed and {@link PreUpdate} cannot be used because will be not
     * triggered. This method takes care to explicitly invoke
     * {@link AtlasObject#postMerge(AtlasObject)} on attached entity.
     * 
     * @param AtlasObject
     *            atlasObject instance.
     */
    void saveObject(AtlasObject object);

    AtlasObject getObjectById(int objectId);

    AtlasObject getObjectByName(String collectionName, String name);

    List<AtlasItem> getAtlasObjects();

    List<Link> getObjectLinks(AtlasItem object);

    List<AtlasObject> findObjectsByCategory(String category);

    List<AtlasObject> findPublishedObjects(String category);

    void removeObject(Object object);

    List<AtlasItem> findObjectsByNames(String category, List<String> names);

    void resetObjectSample(int objectId);

    void removeObjectPicture(int objectId, Image picture);

    void addObjectPicture(int objectId, Image picture);

    Image getPictureByName(int objectId, String name);
}
