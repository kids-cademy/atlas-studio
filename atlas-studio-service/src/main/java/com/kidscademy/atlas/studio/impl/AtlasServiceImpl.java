package com.kidscademy.atlas.studio.impl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kidscademy.atlas.studio.AtlasService;
import com.kidscademy.atlas.studio.BusinessRules;
import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.dto.AtlasObjectTranslation;
import com.kidscademy.atlas.studio.dto.FeatureMetaTranslation;
import com.kidscademy.atlas.studio.export.ExportObject;
import com.kidscademy.atlas.studio.model.AtlasCollection;
import com.kidscademy.atlas.studio.model.AtlasImages;
import com.kidscademy.atlas.studio.model.AtlasItem;
import com.kidscademy.atlas.studio.model.AtlasLinks;
import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.AtlasRelated;
import com.kidscademy.atlas.studio.model.DescriptionMeta;
import com.kidscademy.atlas.studio.model.ExternalSource;
import com.kidscademy.atlas.studio.model.Fact;
import com.kidscademy.atlas.studio.model.Feature;
import com.kidscademy.atlas.studio.model.FeatureMeta;
import com.kidscademy.atlas.studio.model.Image;
import com.kidscademy.atlas.studio.model.Link;
import com.kidscademy.atlas.studio.model.LinkSource;
import com.kidscademy.atlas.studio.model.MediaSRC;
import com.kidscademy.atlas.studio.model.Option;
import com.kidscademy.atlas.studio.model.PhysicalQuantity;
import com.kidscademy.atlas.studio.model.QuantityFormat;
import com.kidscademy.atlas.studio.model.RepositoryObject;
import com.kidscademy.atlas.studio.model.SearchFilter;
import com.kidscademy.atlas.studio.model.Taxon;
import com.kidscademy.atlas.studio.model.TaxonMeta;
import com.kidscademy.atlas.studio.model.Translator;
import com.kidscademy.atlas.studio.search.DirectIndex;
import com.kidscademy.atlas.studio.search.KeywordIndex;
import com.kidscademy.atlas.studio.search.KeywordTree;
import com.kidscademy.atlas.studio.search.ObjectFields;
import com.kidscademy.atlas.studio.search.ObjectIndexer;
import com.kidscademy.atlas.studio.search.SearchIndexProcessor;
import com.kidscademy.atlas.studio.tool.AudioProcessor;
import com.kidscademy.atlas.studio.tool.AudioSampleInfo;
import com.kidscademy.atlas.studio.tool.ImageInfo;
import com.kidscademy.atlas.studio.tool.ImageProcessor;
import com.kidscademy.atlas.studio.tool.MediaType;
import com.kidscademy.atlas.studio.util.Files;
import com.kidscademy.atlas.studio.util.Strings;
import com.kidscademy.atlas.studio.www.WikiHow;

import js.json.Json;
import js.lang.BugError;
import js.lang.GType;
import js.log.Log;
import js.log.LogFactory;
import js.rmi.BusinessException;
import js.tiny.container.core.AppContext;
import js.tiny.container.http.form.Form;
import js.tiny.container.http.form.UploadedFile;
import js.util.Params;

public class AtlasServiceImpl implements AtlasService
{
  private static final Log log = LogFactory.getLog(AtlasServiceImpl.class);

  private final AppContext context;
  private final Json json;

  private final AtlasDao atlasDao;
  private final AudioProcessor audioProcessor;
  private final ImageProcessor imageProcessor;
  private final BusinessRules businessRules;

  private KeywordTree<KeywordIndex<Integer>> index;

  public AtlasServiceImpl(AppContext context) throws IOException {
    log.trace("AtlasServiceImpl(AppContext)");

    this.context = context;
    this.json = context.loadService(Json.class);

    this.atlasDao = context.getInstance(AtlasDao.class);
    this.audioProcessor = context.getInstance(AudioProcessor.class);
    this.imageProcessor = context.getInstance(ImageProcessor.class);
    this.businessRules = context.getInstance(BusinessRules.class);

    File file = context.getAppFile("search-index");
    List<KeywordIndex<Integer>> searchIndex;
    if(file != null && file.exists()) {
      searchIndex = json.parse(new FileReader(file), new GType(List.class, new GType(KeywordIndex.class, Integer.class)));
    }
    else {
      searchIndex = Collections.emptyList();
    }

    this.index = new KeywordTree<>(searchIndex);
  }

  @Override
  public AtlasCollection createAtlasCollection() {
    return AtlasCollection.create();
  }

  @Override
  public void removeAtlasCollection(int collectionId) throws BusinessException {
    businessRules.emptyCollection(collectionId);
    atlasDao.removeAtlasCollection(collectionId);
  }

  @Override
  public AtlasCollection saveAtlasCollection(AtlasCollection collection) throws BusinessException {
    businessRules.uniqueCollectionName(collection);

    if(collection.isPersisted()) {
      AtlasCollection currentCollection = atlasDao.getCollectionById(collection.getId());

      if(!currentCollection.getName().equals(collection.getName())) {
        File currentIcon = Files.mediaFile(currentCollection);
        Files.removeVariants(currentIcon);

        File newIcon = Files.mediaFile(collection);
        if(!currentIcon.renameTo(newIcon)) {
          log.error("Fail to rename collection icon |%s| to |%s|.", currentIcon, newIcon);
        }

        File currentRepository = Files.repositoryDir(currentCollection.getName());
        File newRepository = Files.repositoryDir(collection.getName());
        if(!currentRepository.renameTo(newRepository)) {
          log.error("Failt to rename collection repository |%s| to |%s|.", currentRepository, newRepository);
        }
      }

      if(currentCollection.hasFeaturesMeta() && !collection.hasFeaturesMeta()) {
        log.debug("Collection features meta remove detected. Remove all objects features.");
        // collection features meta was removed; remove features from all child objects
        for(Integer objectId : atlasDao.getCollectionObjectIds(collection.getId())) {
          atlasDao.removeObjectFeatures(objectId);
        }
      }
    }

    Files.mediaFile(collection, "96x96").delete();
    atlasDao.saveAtlasCollection(collection);
    return collection;
  }

  @Override
  public AtlasCollection getCollection(int collectionId) {
    return atlasDao.getCollectionById(collectionId);
  }

  @Override
  public List<AtlasCollection> getCollections() {
    return atlasDao.getCollections();
  }

  @Override
  public List<DescriptionMeta> getCollectionDescriptionsMeta(int collectionId) {
    return atlasDao.getCollectionDescriptionsMeta(collectionId);
  }

  @Override
  public List<TaxonMeta> getCollectionTaxonomyMeta(int collectionId) {
    return atlasDao.getCollectionTaxonomyMeta(collectionId);
  }

  @Override
  public List<FeatureMeta> getCollectionFeaturesMeta(int collectionId) {
    return atlasDao.getCollectionFeaturesMeta(collectionId);
  }

  @Override
  public List<LinkSource> getCollectionLinkSources(int collectionId) {
    return atlasDao.getCollectionLinkSources(collectionId);
  }

  @Override
  public List<AtlasItem> getRecentUsedAtlasObjects() {
    return atlasDao.getRecentUsedAtlasObjects();
  }

  @Override
  public List<AtlasItem> getCollectionItems(Map<String, String> criteria, int collectionId) {
    return atlasDao.getCollectionItems(new SearchFilter(criteria), collectionId);
  }

  @Override
  public List<AtlasImages> getCollectionImages(Map<String, String> criteria, int collectionId) {
    return atlasDao.getCollectionImages(new SearchFilter(criteria), collectionId);
  }

  @Override
  public List<AtlasRelated> getCollectionRelated(Map<String, String> criteria, int collectionId) {
    return atlasDao.getCollectionRelated(new SearchFilter(criteria), collectionId);
  }

  @Override
  public List<AtlasLinks> getCollectionLinks(Map<String, String> criteria, int collectionId) {
    return atlasDao.getCollectionLinks(new SearchFilter(criteria), collectionId);
  }

  @Override
  public List<AtlasItem> getCollectionItemsByTaxon(int collectionId, Taxon taxon, List<AtlasItem> excludes) {
    List<AtlasItem> items;
    if(taxon.isWildcard()) {
      items = atlasDao.getAtlasCollectionItems(collectionId);
    }
    else {
      items = atlasDao.getCollectionItemsByTaxon(collectionId, taxon);
    }
    items.removeAll(excludes);
    return items;
  }

  @Override
  public boolean checkAtlasObjectName(String name) {
    return !atlasDao.objectNameExists(name);
  }

  @Override
  public AtlasObject createAtlasObject(int collectionId) {
    Params.notZero(collectionId, "Collection ID");
    AtlasCollection collection = atlasDao.getCollectionById(collectionId);
    if(collection == null) {
      throw new BugError("Invalid collection ID |%d|. Record not existing in database.", collectionId);
    }
    return AtlasObject.create(collection);
  }

  @Override
  public AtlasObject getAtlasObject(int objectId) throws IOException {
    Params.notZero(objectId, "Atlas object ID");
    AtlasObject object = atlasDao.getAtlasObject(objectId);

    if(object.getSampleSrc() != null) {
      File sampleFile = Files.mediaFile(object.getSampleSrc());
      if(sampleFile.exists()) {
        AudioSampleInfo sampleInfo = audioProcessor.getAudioFileInfo(sampleFile);
        object.setSampleInfo(sampleInfo);
      }
    }

    return object;
  }

  @Override
  public AtlasObject saveAtlasObject(AtlasObject object) throws IOException, BusinessException {
    Params.notNull(object.getName(), "Atlas object name");
    businessRules.uniqueObjectName(object);

    if(object.getId() != 0) {
      String currentObjectName = atlasDao.getAtlasObjectName(object.getId());
      if(!currentObjectName.equals(object.getName())) {
        File currentObjectDir = Files.objectDir(object.getRepositoryName(), currentObjectName);
        File newObjectDir = Files.objectDir(object);
        currentObjectDir.renameTo(newObjectDir);
      }
    }

    if(object.getSampleSrc() != null) {
      MediaFileHandler handler = new MediaFileHandler(object, "sample.mp3");
      handler.commit();
      object.setSampleSrc(handler.sourceSrc());
      object.setWaveformSrc(generateWaveform(object, handler.source()));
    }

    if(object.getImages() != null) {
      for(Image picture : object.getImages().values()) {
        MediaFileHandler handler = new MediaFileHandler(object, picture.getFileName());
        handler.commit();
      }
    }

    Image icon = object.getImage(Image.KEY_ICON);
    if(icon != null) {
      Files.mediaFile(object, icon.getFileName(), "96x96").delete();
    }
    atlasDao.saveAtlasObject(object);
    // need to reload object from database to ensure IDs are updated
    return atlasDao.getAtlasObject(object.getId());
  }

  @Override
  public void removeAtlasObject(int objectId) {
    atlasDao.removeAtlasObject(objectId);
  }

  @Override
  synchronized public void moveAtlasObject(AtlasItem object, int collectionId) {
    File objectDir = Files.objectDir(object);
    atlasDao.moveAtlasObject(object.getId(), collectionId);

    object = atlasDao.getAtlasItem(object.getId());
    File targetObjectDir = Files.objectDir(object);
    File targetCollectionDir = targetObjectDir.getParentFile();
    if(!targetCollectionDir.exists() && !targetObjectDir.getParentFile().mkdirs()) {
      log.error("Fail to create missing collectiopn directory |%s|.", targetCollectionDir);
    }
    if(!objectDir.renameTo(targetObjectDir)) {
      log.error("Fail to move object media directory |%s| to new location |%s|.", objectDir, targetObjectDir);
    }
  }

  @Override
  public List<AtlasItem> getRelatedAtlasObjects(int collectionId, List<String> relatedNames) {
    return atlasDao.getRelatedAtlasObjects(collectionId, relatedNames);
  }

  @Override
  public Link createLink(int collectionId, Link formData) throws BusinessException {
    String domain = Strings.basedomain(formData.getUrl());
    LinkSource linkSource = atlasDao.getLinkSourceByDomain(collectionId, domain);
    businessRules.registeredLinkSource(linkSource);
    return Link.create(linkSource, formData.getUrl(), formData.getDefinition());
  }

  @Override
  public String getLinkDefinition(URL link, String objectDisplay) {
    ExternalSource externalSource = atlasDao.getExternalSourceByDomain(Strings.basedomain(link));
    return externalSource != null ? externalSource.getLinkDefinition(link, objectDisplay) : null;
  }

  @Override
  public ExternalSource createExternalSource() {
    return ExternalSource.create();
  }

  @Override
  public ExternalSource getExternalSource(int externalSourceId) {
    return atlasDao.getExternalSourceById(externalSourceId);
  }

  @Override
  public List<ExternalSource> getExternalSources() {
    return atlasDao.getExternalSources();
  }

  @Override
  public List<ExternalSource> getExternalSourceCandidates(List<Integer> excludeIds) {
    return atlasDao.getExternalSourceCandidates(excludeIds);
  }

  @Override
  public FeatureMeta createFeatureMeta() {
    return FeatureMeta.create();
  }

  @Override
  public FeatureMeta getFeatureMeta(int featureMetaId) {
    return atlasDao.getFeatureMetaById(featureMetaId);
  }

  @Override
  public FeatureMeta saveFeatureMeta(FeatureMeta featureMeta) {
    atlasDao.saveFeatureMeta(featureMeta);
    return featureMeta;
  }

  @Override
  public void removeFeatureMeta(int featureMetaId) {
    atlasDao.removeFeatureMeta(featureMetaId);
  }

  @Override
  public List<Option> getQuantityUnits(PhysicalQuantity quantity) {
    return QuantityFormat.getOptions(quantity);
  }

  @Override
  public ExternalSource saveExternalSource(ExternalSource externalSource) {
    if(externalSource.isPersisted()) {
      ExternalSource currentExternalSource = atlasDao.getExternalSourceById(externalSource.getId());
      if(!currentExternalSource.getDomain().equals(externalSource.getDomain())) {
        File currentIcon = Files.mediaFile(currentExternalSource);
        File newIcon = Files.mediaFile(externalSource);
        currentIcon.renameTo(newIcon);
      }
    }

    Files.mediaFile(externalSource, "96x96").delete();
    atlasDao.saveExternalSource(externalSource);
    return externalSource;
  }

  @Override
  public void removeExternalSource(int externalSourceId) {
    atlasDao.removeExternalSource(externalSourceId);
  }

  @Override
  public List<FeatureMeta> getFeaturesMeta(String search) {
    if(search != null) {
      return atlasDao.searchFeaturesMeta(search);
    }
    return atlasDao.getFeaturesMeta();
  }

  @Override
  public String formatLines(String text, String separator) {
    if(text == null) {
      return null;
    }
    return Strings.join(Strings.breakSentences(Strings.removeReferences(text)), separator);
  }

  // ----------------------------------------------------------------------------------------------
  // OBJECT IMAGE SERVICES

  @Override
  public Image uploadImage(Form imageForm) throws IOException, BusinessException {
    UploadedFile uploadedFile = imageForm.getUploadedFile("media-file");
    return uploadImage(imageForm, uploadedFile.getFile());
  }

  @Override
  public Image uploadImageBySource(Form imageForm) throws IOException, BusinessException {
    Params.notNull(imageForm.getValue("source"), "Picture source");
    URL url = new URL(imageForm.getValue("source"));
    return uploadImage(imageForm, Files.copy(url));
  }

  private Image uploadImage(Form imageForm, File imageFile) throws IOException, BusinessException {
    final Image.Kind imageKind = imageForm.getValue("image-kind", Image.Kind.class);
    if(imageKind == null) {
      throw new BugError("Missing <image-kind> field from image form.");
    }

    switch(imageKind) {
    case COLLECTION:
      return uploadCollectionImage(imageForm, imageFile);

    case OBJECT:
      return uploadObjectImage(imageForm, imageFile);

    case LINK:
      return uploadLinkImage(imageForm, imageFile);

    default:
      throw new BugError("Not processed image kind |%s|.", imageKind);
    }
  }

  /**
   * 
   * @param imageForm form data from user interface
   * @param imageFile uploaded image file, temporary file processed by caller logic.
   * @return
   * @throws IOException
   */
  private Image uploadCollectionImage(Form imageForm, File imageFile) throws IOException {
    AtlasCollection collection = getObjectByForm(AtlasCollection.class, imageForm);
    ImageInfo imageInfo = imageProcessor.getImageInfo(imageFile);

    File targetFile = Files.mediaFile(collection);
    targetFile.getParentFile().mkdirs();
    targetFile.delete();

    if(!imageFile.renameTo(targetFile)) {
      throw new IOException("Unable to upload " + targetFile);
    }

    Image image = new Image();
    image.setImageKey(Image.KEY_ICON);
    image.setUploadDate(new Date());
    image.setSource(imageForm.getValue("source"));
    image.setFileName(targetFile.getName());

    image.setFileSize(imageInfo.getFileSize());
    image.setWidth(imageInfo.getWidth());
    image.setHeight(imageInfo.getHeight());
    image.setSrc(Files.mediaSrc(collection));
    return image;
  }

  private Image uploadLinkImage(Form imageForm, File imageFile) throws IOException {
    ExternalSource externalSource = getObjectByForm(ExternalSource.class, imageForm);

    ImageInfo imageInfo = imageProcessor.getImageInfo(imageFile);
    if(imageInfo.getType() != MediaType.PNG) {
      File pngFile = Files.replaceExtension(imageFile, "png");
      imageProcessor.convert(imageFile, pngFile);
      imageFile = pngFile;
      imageInfo = imageProcessor.getImageInfo(imageFile);
    }

    File targetFile = Files.mediaFile(externalSource);
    targetFile.getParentFile().mkdirs();
    targetFile.delete();

    if(!imageFile.renameTo(targetFile)) {
      throw new IOException("Unable to upload " + targetFile);
    }

    Image image = new Image();
    image.setImageKey(Image.KEY_ICON);
    image.setUploadDate(new Date());
    image.setSource(imageForm.getValue("source"));
    image.setFileName(targetFile.getName());

    image.setFileSize(imageInfo.getFileSize());
    image.setWidth(imageInfo.getWidth());
    image.setHeight(imageInfo.getHeight());
    image.setSrc(Files.mediaSrc(externalSource));
    return image;
  }

  private Image uploadObjectImage(Form imageForm, File imageFile) throws IOException, BusinessException {
    AtlasItem atlasItem = getObjectByForm(AtlasItem.class, imageForm);
    String imageKey = imageForm.getValue("image-key");

    Params.notZero(atlasItem.getId(), "Atlas item ID");
    Params.notNullOrEmpty(imageKey, "Image key");

    businessRules.uniqueImage(atlasItem.getId(), imageKey);

    ImageInfo imageInfo = imageProcessor.getImageInfo(imageFile);
    String targetExtension = null;
    switch(imageKey) {
    case Image.KEY_ICON:
    case Image.KEY_CONTEXTUAL:
      targetExtension = "jpg";
      break;

    case Image.KEY_COVER:
    case Image.KEY_FEATURED:
      targetExtension = "png";
      break;

    default:
      targetExtension = imageInfo.getType().extension();
    }

    File targetFile = Files.mediaFileExt(atlasItem, imageKey, targetExtension);
    if(!targetFile.getParentFile().exists() && !targetFile.getParentFile().mkdirs()) {
      throw new IOException(String.format("Unable to create directories path for target file |%s|.", targetFile));
    }
    if(targetFile.exists() && !targetFile.delete()) {
      throw new IOException(String.format("Unable to create remove target file |%s|.", targetFile));
    }

    if(!imageInfo.getType().extension().equals(targetExtension)) {
      imageProcessor.convert(imageFile, (imageFile = Files.replaceExtension(imageFile, targetExtension)));
    }
    if(!imageFile.renameTo(targetFile)) {
      throw new IOException(String.format("Unable to rename |%s| to |%s|.", imageFile, targetFile));
    }

    Image image = new Image();
    image.setImageKey(imageKey);
    image.setUploadDate(new Date());
    image.setSource(imageForm.getValue("source"));
    image.setFileName(targetFile.getName());

    image.setFileSize(imageInfo.getFileSize());
    image.setWidth(imageInfo.getWidth());
    image.setHeight(imageInfo.getHeight());

    atlasDao.putObjectImage(atlasItem.getId(), image);

    image.setSrc(Files.mediaSrc(atlasItem, targetFile.getName()));
    return image;
  }

  @Override
  public Image replaceImage(Form imageForm) throws IOException {
    UploadedFile uploadedFile = imageForm.getUploadedFile("media-file");
    return replaceImage(imageForm, uploadedFile.getFile());
  }

  @Override
  public Image replaceImageBySource(Form imageForm) throws IOException {
    Params.notNull(imageForm.getValue("source"), "Picture source");
    URL url = new URL(imageForm.getValue("source"));
    return replaceImage(imageForm, Files.copy(url));
  }

  private Image replaceImage(Form imageForm, File imageFile) throws IOException {
    Params.EQ(imageForm.getValue("image-kind"), "OBJECT", "Object class");
    AtlasItem atlasItem = getObjectByForm(AtlasItem.class, imageForm);
    String imageKey = imageForm.getValue("image-key");

    Params.notZero(atlasItem.getId(), "Atlas item ID");
    Params.notNullOrEmpty(imageKey, "Image key");

    Image image = atlasDao.getImageByKey(atlasItem.getId(), imageKey);
    MediaFileHandler handler = new MediaFileHandler(atlasItem, image.getFileName());
    imageFile.renameTo(handler.target());
    updateImage(image, handler.target(), handler.targetSrc());

    atlasDao.putObjectImage(atlasItem.getId(), image);
    return image;
  }

  @Override
  public void removeImage(AtlasItem atlasItem, Image image) throws IOException {
    MediaFileHandler handler = new MediaFileHandler(atlasItem, image.getFileName());
    handler.delete();
    atlasDao.removeObjectImage(atlasItem.getId(), image);
  }

  private void updateImage(Image image, File file, MediaSRC src) throws IOException {
    ImageInfo info = imageProcessor.getImageInfo(file);
    image.setFileSize(info.getFileSize());
    image.setWidth(info.getWidth());
    image.setHeight(info.getHeight());
    image.setSrc(src);
  }

  // ----------------------------------------------------------------------------------------------
  // OBJECT AUDIO SAMPLE SERVICES

  @Override
  public AudioSampleInfo uploadAudioSample(Form audioSampleForm) throws IOException {
    AtlasItem atlasItem = getObjectByForm(AtlasItem.class, audioSampleForm);
    MediaFileHandler handler = new MediaFileHandler(atlasItem, "sample.mp3");
    handler.upload(audioSampleForm.getFile("file"));
    return getAudioSampleInfo(atlasItem, handler.source(), handler.sourceSrc());
  }

  @Override
  public AudioSampleInfo normalizeAudioSample(AtlasItem atlasItem) throws IOException {
    MediaFileHandler handler = new MediaFileHandler(atlasItem, "sample.mp3");
    audioProcessor.normalizeLevel(handler.source(), handler.target());
    if(handler.target().exists()) {
      return getAudioSampleInfo(atlasItem, handler.target(), handler.targetSrc());
    }
    return getAudioSampleInfo(atlasItem, handler.source(), handler.sourceSrc());
  }

  @Override
  public AudioSampleInfo convertAudioSampleToMono(AtlasItem atlasItem) throws IOException {
    MediaFileHandler handler = new MediaFileHandler(atlasItem, "sample.mp3");
    audioProcessor.convertToMono(handler.source(), handler.target());
    return getAudioSampleInfo(atlasItem, handler.target(), handler.targetSrc());
  }

  @Override
  public AudioSampleInfo trimAudioSampleSilence(AtlasItem atlasItem) throws IOException {
    MediaFileHandler handler = new MediaFileHandler(atlasItem, "sample.mp3");
    audioProcessor.trimSilence(handler.source(), handler.target());
    return getAudioSampleInfo(atlasItem, handler.target(), handler.targetSrc());
  }

  @Override
  public AudioSampleInfo cutAudioSample(AtlasItem atlasItem, float start) throws IOException {
    MediaFileHandler handler = new MediaFileHandler(atlasItem, "sample.mp3");
    audioProcessor.cutSegment(handler.source(), handler.target(), start, start + 30);
    return getAudioSampleInfo(atlasItem, handler.target(), handler.targetSrc());
  }

  @Override
  public AudioSampleInfo fadeInAudioSample(AtlasItem atlasItem) throws IOException {
    MediaFileHandler handler = new MediaFileHandler(atlasItem, "sample.mp3");
    audioProcessor.fadeIn(handler.source(), handler.target(), 1.5F);
    return getAudioSampleInfo(atlasItem, handler.target(), handler.targetSrc());
  }

  @Override
  public AudioSampleInfo fadeOutAudioSample(AtlasItem atlasItem) throws IOException {
    MediaFileHandler handler = new MediaFileHandler(atlasItem, "sample.mp3");
    audioProcessor.fadeOut(handler.source(), handler.target(), 1.5F);
    return getAudioSampleInfo(atlasItem, handler.target(), handler.targetSrc());
  }

  @Override
  public MediaSRC generateWaveform(AtlasItem atlasItem) throws IOException {
    Params.notZero(atlasItem.getId(), "Atlas item ID");

    File sampleFile = Files.mediaFile(atlasItem, "sample.mp3");
    if(!sampleFile.exists()) {
      log.error("Database not consistent. Missing sample file |%s|. Reset sample and waveform for object |%s|.", sampleFile, atlasItem.getName());
      atlasDao.resetObjectSample(atlasItem.getId());
      return null;
    }
    return generateWaveform(atlasItem, sampleFile);
  }

  @Override
  public AudioSampleInfo undoAudioSampleProcessing(AtlasItem atlasItem) throws IOException {
    MediaFileHandler handler = new MediaFileHandler(atlasItem, "sample.mp3");
    handler.undo();
    return getAudioSampleInfo(atlasItem, handler.source(), handler.sourceSrc());
  }

  @Override
  public AudioSampleInfo rollbackAudioSampleProcessing(AtlasItem atlasItem) throws IOException {
    MediaFileHandler handler = new MediaFileHandler(atlasItem, "sample.mp3");
    handler.rollback();
    return getAudioSampleInfo(atlasItem, handler.source(), handler.sourceSrc());
  }

  @Override
  public void removeAudioSample(AtlasItem atlasItem) throws IOException {
    Params.notZero(atlasItem.getId(), "Atlas item ID");

    MediaFileHandler handler = new MediaFileHandler(atlasItem, "sample.mp3");
    handler.delete();
    atlasDao.resetObjectSample(atlasItem.getId());
    Files.mediaFile(atlasItem, "sample.mp3").delete();
    Files.mediaFile(atlasItem, "waveform.png").delete();
  }

  // ----------------------------------------------------------------------------------------------

  private <T> T getObjectByForm(Class<T> type, Form mediaForm) {
    String objectId = mediaForm.getValue("object-id");
    if(objectId == null) {
      throw new BugError("Media form should have <object-id> field.");
    }
    try {
      return atlasDao.getObjectById(type, Integer.parseInt(objectId));
    }
    catch(NumberFormatException unused) {
      throw new BugError("Media form <object-id> field should be numeric.");
    }
  }

  private AudioSampleInfo getAudioSampleInfo(RepositoryObject collectionItem, File file, MediaSRC mediaSrc) throws IOException {
    AudioSampleInfo info = audioProcessor.getAudioFileInfo(file);
    info.setSampleSrc(mediaSrc);
    info.setWaveformSrc(generateWaveform(collectionItem, file));
    return info;
  }

  private MediaSRC generateWaveform(RepositoryObject collectionItem, File audioFile) throws IOException {
    MediaSRC waveformSrc = Files.mediaSrc(collectionItem, "waveform.png");
    File waveformFile = Files.mediaFile(waveformSrc);
    audioProcessor.generateWaveform(audioFile, waveformFile);
    return waveformSrc;
  }

  @Override
  public void updateIndex() throws NoSuchMethodException, IOException {
    ObjectFields<AtlasObject> fields = new ObjectFields<>(AtlasObject.class);
    fields.addField("description");
    fields.addField("facts");
    fields.addField("definition");
    fields.addField("sampleTitle");
    fields.addField("spreading");
    fields.addField("conservation");
    fields.addField("taxonomy");
    fields.addField("related");
    fields.addField("aliases");
    fields.addField("display");

    ObjectIndexer<AtlasObject, Integer> indexer = new ObjectIndexer<>(fields);
    List<DirectIndex<Integer>> indices = new ArrayList<>();

    AtlasObject object = new AtlasObject();
    while((object = atlasDao.getNextAtlasObject(object.getId())) != null) {
      log.debug("Create direct index for |%s|.", object.getName());
      indices.add(indexer.scanObject(object, object.getId()));
    }

    List<KeywordIndex<Integer>> searchIndex = SearchIndexProcessor.createSearchIndex(indices);

    File file = context.getAppFile("search-index");
    json.stringify(new FileWriter(file), searchIndex);

    index = new KeywordTree<>(searchIndex);
  }

  @Override
  public Set<AtlasItem> search(String criterion) {
    Set<AtlasItem> items = new LinkedHashSet<>();
    for(KeywordIndex<Integer> keywordIndex : index.find(criterion)) {
      items.addAll(atlasDao.getAtlasItems(keywordIndex.getObjectKeys()));
    }
    return items;
  }

  @Override
  public String getWikiHowTitle(URL url) {
    WikiHow wikiHow = context.getInstance(WikiHow.class);
    return wikiHow.getTitle(url.getPath());
  }

  @Override
  public ExportObject getReaderObject(int objectId, String language) {
    AtlasObject object = atlasDao.getAtlasObject(objectId);
    ExportObject exportObject = new ExportObject(object);
    if(!Translator.isDefaultLanguage(language)) {
      Translator translator = new Translator(atlasDao, language);
      exportObject.translate(translator);
    }
    for(AtlasItem related : atlasDao.getRelatedAtlasObjects(object.getCollection().getId(), object.getRelated())) {
      exportObject.addRelated(related);
    }
    return exportObject;
  }

  @Override
  public Feature updateFeatureDisplay(Feature feature) {
    return feature.updateDisplay();
  }

  @Override
  public AtlasObjectTranslation getAtlasObjectTranslate(int objectId, String language) {
    AtlasObject object = atlasDao.getAtlasObject(objectId);
    Translator translator = new Translator(atlasDao, language);
    return new AtlasObjectTranslation(object, translator);
  }

  @Override
  public void saveAtlasObjectTranslate(int objectId, String language, AtlasObjectTranslation translate) {
    Translator translator = new Translator(atlasDao, language);
    translator.saveAtlasObjectDisplay(objectId, translate.getDisplay());
    translator.saveAtlasObjectAliases(objectId, translate.getAliases());
    translator.saveAtlasObjectDefinition(objectId, translate.getDefinition());
    translator.saveAtlasObjectDescription(objectId, translate.getDescription());
    translator.saveAtlasObjectSampleTitle(objectId, translate.getSampleTitle());

    for(Fact fact : translate.getFacts()) {
      translator.saveFactTitle(fact.getId(), fact.getTitle());
      translator.saveFactText(fact.getId(), fact.getText());
    }
  }

  @Override
  public AtlasObjectTranslation translateAtlasObject(int objectId, String language) {
    AtlasObject object = atlasDao.getAtlasObject(objectId);
    Translator translator = new Translator(atlasDao, language);

    translator.translateAtlasObjectDisplay(objectId, object.getDisplay());
    translator.translateAtlasObjectAliases(objectId, object.getAliases());
    translator.translateAtlasObjectDefinition(objectId, object.getDefinition());
    translator.translateAtlasObjectDescription(objectId, object.getDescription());
    translator.translateAtlasObjectSampleTitle(objectId, object.getSampleTitle());

    for(Fact fact : object.getFacts()) {
      translator.translateFactTitle(fact.getId(), fact.getTitle());
      translator.translateFactText(fact.getId(), fact.getText());
    }

    return new AtlasObjectTranslation(object, translator);
  }

  @Override
  public List<FeatureMetaTranslation> getFeatureMetaTranslations(String search, String language) {
    List<FeatureMeta> featuresMeta = getFeaturesMeta(search);
    List<FeatureMetaTranslation> translations = new ArrayList<>(featuresMeta.size());

    Translator translator = new Translator(atlasDao, language);
    for(FeatureMeta meta : featuresMeta) {
      translations.add(new FeatureMetaTranslation(meta, translator.getFeatureMetaDisplay(meta.getId())));
    }
    return translations;
  }

  @Override
  public void translateAllFeaturesMetaDisplay(List<Integer> featureMetaIds, String language) {
    Translator translator = new Translator(atlasDao, language);
    for(int featureMetaId : featureMetaIds) {
      FeatureMeta featureMeta = atlasDao.getFeatureMetaById(featureMetaId);
      translator.translateFeatureMetaDisplay(featureMeta.getId(), featureMeta.getDisplay());
    }
  }

  @Override
  public void saveFeatureMetaTranslations(List<FeatureMetaTranslation> translations, String language) {
    Translator translator = new Translator(atlasDao, language);
    for(FeatureMetaTranslation translation : translations) {
      translator.saveFeatureMetaDisplay(translation.getId(), translation.getTranslation());
    }
  }
}
