package com.kidscademy.atlas.studio.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

import com.kidscademy.atlas.studio.export.ExportItem;
import com.kidscademy.atlas.studio.model.AtlasCollection;
import com.kidscademy.atlas.studio.model.AtlasImages;
import com.kidscademy.atlas.studio.model.AtlasItem;
import com.kidscademy.atlas.studio.model.AtlasLinks;
import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.AtlasRelated;
import com.kidscademy.atlas.studio.model.FeatureMeta;
import com.kidscademy.atlas.studio.model.Image;
import com.kidscademy.atlas.studio.model.Link;
import com.kidscademy.atlas.studio.model.LinkMeta;
import com.kidscademy.atlas.studio.model.SearchFilter;
import com.kidscademy.atlas.studio.model.Taxon;

public interface AtlasDao {
    void saveAtlasCollection(AtlasCollection collection);

    void removeAtlasCollection(int collectionId);

    AtlasCollection getCollectionById(int collectionId);

    boolean uniqueCollectionName(AtlasCollection collection);

    List<AtlasCollection> getCollections();

    List<AtlasItem> getCollectionItems(SearchFilter filter, int collectionId);

    List<AtlasImages> getCollectionImages(SearchFilter filter, int collectionId);

    List<AtlasRelated> getCollectionRelated(SearchFilter filter, int collectionId);

    List<AtlasLinks> getCollectionLinks(SearchFilter filter, int collectionId);

    List<AtlasItem> getCollectionItemsByTaxon(int collectionId, Taxon taxon);

    List<ExportItem> getCollectionExportItems(int collectionId);

    List<ExportItem> getCollectionExportItemsByState(int collectionId, AtlasObject.State state);

    List<ExportItem> getAllExportItems();

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
    void saveAtlasObject(AtlasObject object);

    void removeAtlasObject(int objectId);

    AtlasItem moveAtlasObject(int objectId, int collectionId);

    AtlasItem getAtlasItem(int objectId);

    AtlasObject getAtlasObject(int objectId);

    AtlasObject getObjectByName(String collectionName, String name);

    String getAtlasObjectName(int objectId);

    List<Link> getObjectLinks(AtlasItem object);

    List<LinkMeta> getLinksMeta();

    LinkMeta getLinkMetaById(int linkMetaId);

    LinkMeta getLinkMetaByDomain(String domain);

    void saveLinkMeta(LinkMeta linkMeta);

    void removeLinkMeta(int linkMeta);

    List<AtlasItem> getRelatedAtlasObjects(int collectionId, List<String> relatedNames);

    void resetObjectSample(int objectId);

    void removeObjectImage(int objectId, Image image);

    void addObjectImage(int objectId, Image image);

    /**
     * Get atlas object image identified by its key. Key value is unique per atlas
     * object. Return null if atlas object has no image with requested key value.
     * 
     * @param objectId
     *            database primary key for atlas object owning the image,
     * @param imageKey
     *            image key value, unique per atlas object.
     * @return atlas object image or null.
     */
    Image getImageByKey(int objectId, String imageKey);

    AtlasObject getNextAtlasObject(int objectId);

    List<AtlasItem> getAtlasItems(List<Integer> objectIds);

    int getCollectionSize(int collectionId);

    List<FeatureMeta> getFeaturesMeta();
    
    void saveFeatureMeta(FeatureMeta featureMeta);
    
    void removeFeatureMeta(int featureMetaId);
}
