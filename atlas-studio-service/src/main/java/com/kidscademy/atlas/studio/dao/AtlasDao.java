package com.kidscademy.atlas.studio.dao;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

import com.kidscademy.atlas.studio.export.ExportItem;
import com.kidscademy.atlas.studio.model.AndroidApp;
import com.kidscademy.atlas.studio.model.AtlasCollection;
import com.kidscademy.atlas.studio.model.AtlasCollectionKey;
import com.kidscademy.atlas.studio.model.AtlasImages;
import com.kidscademy.atlas.studio.model.AtlasItem;
import com.kidscademy.atlas.studio.model.AtlasLinks;
import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.AtlasRelated;
import com.kidscademy.atlas.studio.model.DescriptionMeta;
import com.kidscademy.atlas.studio.model.ExternalSource;
import com.kidscademy.atlas.studio.model.Feature;
import com.kidscademy.atlas.studio.model.FeatureMeta;
import com.kidscademy.atlas.studio.model.Image;
import com.kidscademy.atlas.studio.model.Link;
import com.kidscademy.atlas.studio.model.LinkSource;
import com.kidscademy.atlas.studio.model.Release;
import com.kidscademy.atlas.studio.model.ReleaseItem;
import com.kidscademy.atlas.studio.model.ReleaseParent;
import com.kidscademy.atlas.studio.model.SearchFilter;
import com.kidscademy.atlas.studio.model.Taxon;
import com.kidscademy.atlas.studio.model.TaxonMeta;
import com.kidscademy.atlas.studio.model.TaxonUnit;
import com.kidscademy.atlas.studio.model.TranslationData;
import com.kidscademy.atlas.studio.model.TranslationKey;

public interface AtlasDao
{
  <T> T getObjectById(Class<T> type, int id);

  void saveAtlasCollection(AtlasCollection collection);

  void removeAtlasCollection(int collectionId);

  AtlasCollectionKey getCollectionKeyById(int collectionId);

  AtlasCollection getCollectionById(int collectionId);

  AtlasCollection getCollectionByLinkSource(int linkSourceId);

  String getCollectionName(int collectionId);

  List<DescriptionMeta> getCollectionDescriptionsMeta(int collectionId);

  boolean uniqueCollectionName(AtlasCollection collection);

  boolean uniqueObjectName(AtlasObject object);

  boolean objectNameExists(String name);

  List<AtlasCollection> getCollections();

  List<AtlasItem> getRecentUsedAtlasObjects();

  List<AtlasItem> getCollectionItems(SearchFilter filter, int collectionId);

  List<Integer> getCollectionObjectIds(int collectionId);

  List<AtlasImages> getCollectionImages(SearchFilter filter, int collectionId);

  List<AtlasRelated> getCollectionRelated(SearchFilter filter, int collectionId);

  List<AtlasLinks> getCollectionLinks(SearchFilter filter, int collectionId);

  List<AtlasItem> getCollectionItemsByTaxon(int collectionId, Taxon taxon);

  List<ExportItem> getCollectionExportItems(int collectionId);

  List<ExportItem> getCollectionExportItemsByState(int collectionId, AtlasObject.State state);

  List<ExportItem> getAllExportItems();

  /**
   * Persist or merge AtlasObject entity, depending on ID value. If ID is zero instance is considered never created into database and
   * {@link EntityManager#persist(Object)} is enacted. Otherwise instance is considered persisted but detached and {@link EntityManager#merge(Object)} is
   * performed.
   * <p>
   * AtlasObject entity has JPA event listener for {@link PostLoad} used to initialize root-relative media SRC, used on web interface, from media file names
   * that are persisted. Also has {@link PrePersist} for reverse operation.
   * <p>
   * Because media SRC fields are {@link Transient}, merge is not able to detect they are changed and {@link PreUpdate} cannot be used because will be not
   * triggered. This method takes care to explicitly invoke {@link AtlasObject#postMerge(AtlasObject)} on attached entity.
   * 
   * @param AtlasObject atlasObject instance.
   */
  void saveAtlasObject(AtlasObject object);

  void removeAtlasObject(int objectId);

  void moveAtlasObject(int sourceObjectId, int targetCollectionId);

  void removeObjectFeatures(int objectId);

  AtlasItem getAtlasItem(int objectId);

  AtlasObject getAtlasObject(int objectId);

  AtlasObject getObjectByName(String collectionName, String name);

  String getAtlasObjectName(int objectId);

  List<Link> getObjectLinks(int objectId);

  LinkSource getLinkSourceByDomain(int collectionId, String domain);

  List<ExternalSource> getExternalSources();

  List<ExternalSource> getExternalSourceCandidates(List<Integer> excludeIds);

  ExternalSource getExternalSourceById(int externalSourceId);

  ExternalSource getExternalSourceByDomain(String domain);

  void saveExternalSource(ExternalSource externalSource);

  void removeExternalSource(int externalSource);

  List<AtlasItem> getRelatedAtlasObjects(int collectionId, List<String> relatedNames);

  void resetObjectSample(int objectId);

  void removeObjectImage(int objectId, Image image);

  /**
   * Add image to atlas object. Object images are stored in a map with {@link Image#getImageKey()} as key. If image key already exists, given image replace
   * existing one.
   * 
   * @param objectId atlas object database ID,
   * @param image image descriptor to add.
   */
  void putObjectImage(int objectId, Image image);

  /**
   * Get atlas object image identified by its key. Key value is unique per atlas object. Return null if atlas object has no image with requested key value.
   * 
   * @param objectId database primary key for atlas object owning the image,
   * @param imageKey image key value, unique per atlas object.
   * @return atlas object image or null.
   */
  Image getImageByKey(int objectId, String imageKey);

  AtlasObject getNextAtlasObject(int objectId);

  List<AtlasItem> getAtlasItems(List<Integer> objectIds);

  List<AtlasItem> getAtlasCollectionItems(int collectionId);

  int getCollectionSize(int collectionId);

  int getReleaseSize(int releaseId);

  List<FeatureMeta> getFeaturesMeta();

  List<FeatureMeta> searchFeaturesMeta(String search, List<Integer> excludes);

  List<FeatureMeta> searchFeaturesMeta(String search);

  List<FeatureMeta> getCollectionFeaturesMeta(int collectionId);

  List<TaxonMeta> getCollectionTaxonomyMeta(int collectionId);

  List<LinkSource> getCollectionLinkSources(int collectionId);

  TaxonUnit getTaxonUnit(int id);

  void saveTaxonUnit(TaxonUnit taxonUnit);

  void removeTaxonUnit(int id);

  List<TaxonUnit> getTaxonUnits();

  List<TaxonUnit> searchTaxonUnits(String search, List<Integer> excludes);

  List<TaxonUnit> searchTaxonUnits(String search);

  /**
   * Retrieve taxon meta identified by its database ID or null if none found.
   * 
   * @param id taxon meta database ID.
   * @return taxon meta instance, possible null.
   */
  TaxonMeta getTaxonMetaById(int id);

  /**
   * Retrieve existing collection taxon meta identified by its name. Named taxon meta should exit into database otherwise exception is thrown.
   * 
   * @param collectionId collection database ID,
   * @param taxonName taxon name.
   * @return collection taxon with requested name.
   * @throws NoResultException if requested name does not identify an existing database taxon meta record.
   */
  TaxonMeta getTaxonMetaByName(int collectionId, String taxonName) throws NoResultException;

  FeatureMeta getFeatureMetaById(int featureMetaId);

  FeatureMeta getFeatureMetaByKey(int collectionId, String featureKey);

  void saveFeatureMeta(FeatureMeta featureMeta);

  void removeFeatureMeta(int featureMetaId);

  List<ReleaseItem> getReleases();

  Release getReleaseById(int releaseId);

  Release getReleaseByName(String releaseName);

  void saveRelease(Release release) throws IOException;

  void removeReleaseChildren(int releaseId);

  void removeRelease(int releaseId);

  ReleaseParent getReleaseParentById(int releaseId);

  /**
   * Add atlas object to release, both parent release and child atlas object being identified by database IDs. Is legal for child to already be registered in
   * which case this method does nothing.
   * 
   * @param releaseId
   * @param childId
   */
  void addReleaseChild(int releaseId, int childId);

  /**
   * Add atlas objects to release, both parent release and child atlas objects identified by database IDs. Child IDs argument may contain already released atlas
   * objects. This method takes care to avoid adding multiple times.
   * 
   * @param releaseId parent release,
   * @param childIds atlas objects.
   */
  void addReleaseChildren(int releaseId, List<Integer> childIds);

  void removeReleaseChild(int releaseId, int childId);

  List<AtlasItem> getReleaseItems(int releaseId);

  List<ExternalSource> getReleaseExternalSources(int releaseId);

  AndroidApp getAndroidAppByName(String name);

  void saveAndroidApp(AndroidApp app);

  void removeAndroidApp(int appId);

  AndroidApp getAndroidAppById(int appId);

  AndroidApp getAndroidAppByRelease(int releaseId);

  TranslationData getTranslationData(TranslationKey key);

  String getTranslation(TranslationKey key);

  boolean hasTranslation(TranslationKey key);

  void saveTranslation(TranslationData translation);

  void removeTranslation(TranslationKey key);

  List<Feature> getFeatures();

  void saveFeature(Feature feature);
}
