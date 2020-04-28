package com.kidscademy.atlas.studio.impl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kidscademy.atlas.studio.AtlasService;
import com.kidscademy.atlas.studio.BusinessRules;
import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.dao.TaxonomyDao;
import com.kidscademy.atlas.studio.export.ExportObject;
import com.kidscademy.atlas.studio.model.AtlasCollection;
import com.kidscademy.atlas.studio.model.AtlasImages;
import com.kidscademy.atlas.studio.model.AtlasItem;
import com.kidscademy.atlas.studio.model.AtlasLinks;
import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.AtlasRelated;
import com.kidscademy.atlas.studio.model.ConservationStatus;
import com.kidscademy.atlas.studio.model.Feature;
import com.kidscademy.atlas.studio.model.FeatureMeta;
import com.kidscademy.atlas.studio.model.Image;
import com.kidscademy.atlas.studio.model.Link;
import com.kidscademy.atlas.studio.model.LinkMeta;
import com.kidscademy.atlas.studio.model.MediaSRC;
import com.kidscademy.atlas.studio.model.Option;
import com.kidscademy.atlas.studio.model.PhysicalQuantity;
import com.kidscademy.atlas.studio.model.QuantityFormat;
import com.kidscademy.atlas.studio.model.RepositoryObject;
import com.kidscademy.atlas.studio.model.SearchFilter;
import com.kidscademy.atlas.studio.model.Taxon;
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
import com.kidscademy.atlas.studio.www.CambridgeDictionary;
import com.kidscademy.atlas.studio.www.LifeFormWikipediaArticle;
import com.kidscademy.atlas.studio.www.MerriamWebster;
import com.kidscademy.atlas.studio.www.NationalGeographicArticle;
import com.kidscademy.atlas.studio.www.PlantVillage;
import com.kidscademy.atlas.studio.www.SoftSchools;
import com.kidscademy.atlas.studio.www.TheFreeDictionary;
import com.kidscademy.atlas.studio.www.WikiHow;
import com.kidscademy.atlas.studio.www.WikipediaArticleText;

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

public class AtlasServiceImpl implements AtlasService {
    private static final Log log = LogFactory.getLog(AtlasServiceImpl.class);

    private final AppContext context;
    private final Json json;

    private final AtlasDao atlasDao;
    private final TaxonomyDao taxonomyDao;
    private final AudioProcessor audioProcessor;
    private final ImageProcessor imageProcessor;
    private final SoftSchools softSchools;
    private final TheFreeDictionary freeDictionary;
    private final CambridgeDictionary cambridgeDictionary;
    private final MerriamWebster merriamWebster;
    private final BusinessRules businessRules;

    private KeywordTree<KeywordIndex<Integer>> index;

    public AtlasServiceImpl(AppContext context) throws IOException {
	log.trace("AtlasServiceImpl(AppContext)");

	this.context = context;
	this.json = context.loadService(Json.class);

	this.atlasDao = context.getInstance(AtlasDao.class);
	this.taxonomyDao = context.getInstance(TaxonomyDao.class);
	this.audioProcessor = context.getInstance(AudioProcessor.class);
	this.imageProcessor = context.getInstance(ImageProcessor.class);
	this.softSchools = context.getInstance(SoftSchools.class);
	this.freeDictionary = context.getInstance(TheFreeDictionary.class);
	this.cambridgeDictionary = context.getInstance(CambridgeDictionary.class);
	this.merriamWebster = context.getInstance(MerriamWebster.class);
	this.businessRules = context.getInstance(BusinessRules.class);

	File file = context.getAppFile("search-index");
	List<KeywordIndex<Integer>> searchIndex;
	if (file != null && file.exists()) {
	    searchIndex = json.parse(new FileReader(file),
		    new GType(List.class, new GType(KeywordIndex.class, Integer.class)));
	} else {
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
	if (collection.isPersisted()) {
	    AtlasCollection currentCollection = atlasDao.getCollectionById(collection.getId());
	    if (!currentCollection.getName().equals(collection.getName())) {
		File currentIcon = Files.mediaFile(currentCollection);
		File newIcon = Files.mediaFile(collection);
		currentIcon.renameTo(newIcon);
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
	List<AtlasItem> items = atlasDao.getCollectionItemsByTaxon(collectionId, taxon);
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
	if (collection == null) {
	    throw new BugError("Invalid collection ID |%d|. Record not existing in database.", collectionId);
	}
	return AtlasObject.create(collection);
    }

    @Override
    public AtlasObject getAtlasObject(int objectId) throws IOException {
	Params.notZero(objectId, "Atlas object ID");
	AtlasObject object = atlasDao.getAtlasObject(objectId);

	if (object.getSampleSrc() != null) {
	    File sampleFile = Files.mediaFile(object.getSampleSrc());
	    if (sampleFile.exists()) {
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

	if (object.getId() != 0) {
	    String currentObjectName = atlasDao.getAtlasObjectName(object.getId());
	    if (!currentObjectName.equals(object.getName())) {
		File currentObjectDir = Files.objectDir(object.getRepositoryName(), currentObjectName);
		File newObjectDir = Files.objectDir(object);
		currentObjectDir.renameTo(newObjectDir);
	    }
	}

	if (object.getSampleSrc() != null) {
	    MediaFileHandler handler = new MediaFileHandler(object, "sample.mp3");
	    handler.commit();
	    object.setSampleSrc(handler.sourceSrc());
	    object.setWaveformSrc(generateWaveform(object, handler.source()));
	}

	if (object.getImages() != null) {
	    for (Image picture : object.getImages().values()) {
		MediaFileHandler handler = new MediaFileHandler(object, picture.getFileName());
		handler.commit();
	    }
	}

	Image icon = object.getImage(Image.KEY_ICON);
	if (icon != null) {
	    Files.mediaFile(object, icon.getFileName(), "96x96").delete();
	}
	atlasDao.saveAtlasObject(object);
	return object;
    }

    @Override
    public void removeAtlasObject(int objectId) {
	atlasDao.removeAtlasObject(objectId);
    }

    @Override
    synchronized public void moveAtlasObject(AtlasItem object, int collectionId) {
	File objectDir = Files.objectDir(object);
	object = atlasDao.moveAtlasObject(object.getId(), collectionId);
	if (object != null) {
	    File newObjectDir = Files.objectDir(object);
	    objectDir.renameTo(newObjectDir);
	}
    }

    @Override
    public List<AtlasItem> getRelatedAtlasObjects(int collectionId, List<String> relatedNames) {
	return atlasDao.getRelatedAtlasObjects(collectionId, relatedNames);
    }

    @Override
    public Link createLink(Link link) throws BusinessException {
	businessRules.registeredLinkDomain(link.getUrl());
	URL articleURL = link.getUrl();
	LinkMeta linkMeta = atlasDao.getLinkMetaByDomain(Strings.basedomain(articleURL));
	return Link.create(linkMeta, articleURL, link.getDefinition());
    }

    @Override
    public String getLinkDefinition(URL link, String objectDisplay) {
	LinkMeta linkMeta = atlasDao.getLinkMetaByDomain(Strings.basedomain(link));
	return linkMeta != null ? linkMeta.getLinkDefinition(link, objectDisplay) : null;
    }

    @Override
    public LinkMeta createLinkMeta() {
	return LinkMeta.create();
    }

    @Override
    public LinkMeta getLinkMeta(int linkMetaId) {
	return atlasDao.getLinkMetaById(linkMetaId);
    }

    @Override
    public List<LinkMeta> getLinksMeta() {
	return atlasDao.getLinksMeta();
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
    public List<FeatureMeta> getFeaturesMetaCandidates(List<FeatureMeta> current) {
	List<FeatureMeta> featuresMeta = atlasDao.getFeaturesMeta();
	featuresMeta.removeAll(current);
	return featuresMeta;
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
    public LinkMeta saveLinkMeta(LinkMeta linkMeta) {
	if (linkMeta.isPersisted()) {
	    LinkMeta currentLinkMeta = atlasDao.getLinkMetaById(linkMeta.getId());
	    if (!currentLinkMeta.getDomain().equals(linkMeta.getDomain())) {
		File currentIcon = Files.mediaFile(currentLinkMeta);
		File newIcon = Files.mediaFile(linkMeta);
		currentIcon.renameTo(newIcon);
	    }
	}

	Files.mediaFile(linkMeta, "96x96").delete();
	atlasDao.saveLinkMeta(linkMeta);
	return linkMeta;
    }

    @Override
    public void removeLinkMeta(int linkMetaId) {
	atlasDao.removeLinkMeta(linkMetaId);
    }

    @Override
    public List<FeatureMeta> getFeaturesMeta() {
	return atlasDao.getFeaturesMeta();
    }

    @Override
    public String importObjectDefinition(Link link) {
	switch (link.getDomain()) {
	case "thefreedictionary.com":
	    return freeDictionary.getDefinition(link.getFileName());

	case "cambridge.org":
	    return cambridgeDictionary.getDefinition(link.getFileName());

	case "merriam-webster.com":
	    return merriamWebster.getDefinition(link.getFileName());

	default:
	    return null;
	}
    }

    @Override
    public Map<String, String> importObjectDescription(Link link) {
	Map<String, String> sections = new LinkedHashMap<>();
	switch (link.getDomain()) {
	case "softschools.com":
	    sections.put("softschools", Strings
		    .join(Strings.breakSentences(softSchools.getFacts(link.getPath()).getDescription()), "\r\n\r\n"));
	    break;

	case "wikipedia.org":
	    WikipediaArticleText article = new WikipediaArticleText(link.getUrl());
	    sections.put("wikipedia", article.getText());
	    break;

	case "psu.edu":
	    PlantVillage plantVillage = context.getInstance(PlantVillage.class);
	    plantVillage.getArticle(link.getPath()).getSections(sections);
	    break;

	default:
	    break;
	}
	return sections;
    }

    @Override
    public String formatLines(String text, String separator) {
	if (text == null) {
	    return null;
	}
	return Strings.join(Strings.breakSentences(text), separator);
    }

    @Override
    public Map<String, String> importObjectFacts(Link link) {
	switch (link.getDomain()) {
	case "softschools.com":
	    Map<String, String> facts = new HashMap<>();
	    for (String fact : softSchools.getFacts(link.getPath()).getFacts()) {
		facts.put(Strings.excerpt(fact), fact);
	    }
	    return facts;

	default:
	    return null;
	}
    }

    @Override
    public List<Feature> importObjectFeatures(Link link) {
	switch (link.getDomain()) {
	case "nationalgeographic.com":
	    NationalGeographicArticle article = new NationalGeographicArticle(link.getUrl());
	    List<Feature> features = new ArrayList<>();

	    if (article.hasLifespan()) {
		features.add(new Feature("lifespan", article.getMinimumLifespan(), article.getMaximumLifespan(),
			PhysicalQuantity.TIME));
	    }

	    if (article.hasWingspan()) {
		features.add(new Feature("wingspan", article.getMinimumWingspan(), article.getMaximumWingspan(),
			PhysicalQuantity.LENGTH));
	    }

	    if (article.hasLength()) {
		features.add(new Feature("length", article.getMinimumLength(), article.getMaximumLength(),
			PhysicalQuantity.LENGTH));
	    }

	    if (article.hasBillLength()) {
		features.add(new Feature("length.bill", article.getMinimumBillLength(), article.getMaximumBillLength(),
			PhysicalQuantity.LENGTH));
	    }

	    if (article.hasHeight()) {
		features.add(new Feature("height", article.getMinimumHeight(), article.getMaximumHeight(),
			PhysicalQuantity.LENGTH));
	    }

	    if (article.hasWeight()) {
		features.add(new Feature("weight", article.getMinimumWeight(), article.getMaximumWeight(),
			PhysicalQuantity.MASS));
	    }

	    return features;

	default:
	    return null;
	}
    }

    @Override
    public AtlasItem importWikipediaObject(int collectionId, URL articleURL) throws IOException, BusinessException {
	LifeFormWikipediaArticle article = new LifeFormWikipediaArticle(articleURL);

	AtlasCollection collection = atlasDao.getCollectionById(collectionId);
	AtlasObject object = new AtlasObject();
	object.setCollection(collection);

	object.setName(Strings.scientificToDashedName(article.getScientificName()));
	if (object.getName() == null) {
	    object.setName(article.getCommonName().trim().toLowerCase().replaceAll("[()]", "").replaceAll(" ", "-"));
	}
	object.setDisplay(article.getCommonName());
	object.setDefinition(article.getDefinition());
	object.setDescription(article.getDescription());

	object.setStartDate(article.getStartDate());
	if (collection.getFlags().hasEndDate()) {
	    object.setEndDate(article.getEndDate());
	}
	if (collection.getFlags().hasConservationStatus()) {
	    object.setConservation(ConservationStatus.forDisplay(article.getConservationStatus()));
	}

	List<Taxon> taxonomy = loadAtlasObjectTaxonomy(object.getName());
	if (taxonomy.isEmpty()) {
	    taxonomy = article.getTaxonomy();
	}
	if (!taxonomy.isEmpty()) {
	    object.setTaxonomy(taxonomy);
	}

	List<Link> links = new ArrayList<>();
	LinkMeta linkMeta = atlasDao.getLinkMetaByDomain(Strings.basedomain(articleURL));
	links.add(Link.create(linkMeta, articleURL, linkMeta.getLinkDefinition(articleURL, object.getDisplay())));
	object.setLinks(links);

	object.setState(AtlasObject.State.CREATED);
	object.setAliases(new ArrayList<String>());
	object.setImages(new HashMap<String, Image>());
	object.setTimestamp(new Date());

	saveAtlasObject(object);
	return atlasDao.getAtlasItem(object.getId());
    }

    @Override
    public List<Taxon> importAtlasObjectTaxonomy(URL pageURL) {
	switch (Strings.basedomain(pageURL)) {
	case "wikipedia.org":
	    LifeFormWikipediaArticle article = new LifeFormWikipediaArticle(pageURL);
	    return article.getTaxonomy();
	}
	return null;
    }

    private static final String[] TAXON_NAMES = new String[] { "kingdom", "phylum", "class", "order", "suborder",
	    "family", "genus", "species", "subspecies" };

    @Override
    public List<Taxon> loadAtlasObjectTaxonomy(String objectName) {
	Params.notNullOrEmpty(objectName, "Atlas object name");
	Map<String, String> sourceTaxonomy = taxonomyDao.getObjectTaxonomy(Strings.dashedToScientificName(objectName));
	List<Taxon> taxonomy = new ArrayList<>(TAXON_NAMES.length);
	for (String taxonName : TAXON_NAMES) {
	    if (sourceTaxonomy.containsKey(taxonName)) {
		taxonomy.add(new Taxon(taxonName, sourceTaxonomy.get(taxonName)));
	    }
	}
	return taxonomy;
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
	if (imageKind == null) {
	    throw new BugError("Missing <image-kind> field from image form.");
	}

	switch (imageKind) {
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
     * @param imageForm
     *            form data from user interface
     * @param imageFile
     *            uploaded image file, temporary file processed by caller logic.
     * @return
     * @throws IOException
     */
    private Image uploadCollectionImage(Form imageForm, File imageFile) throws IOException {
	AtlasCollection collection = getObjectByForm(AtlasCollection.class, imageForm);
	ImageInfo imageInfo = imageProcessor.getImageInfo(imageFile);

	File targetFile = Files.mediaFile(collection);
	targetFile.getParentFile().mkdirs();
	targetFile.delete();

	if (!imageFile.renameTo(targetFile)) {
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
	LinkMeta linkMeta = getObjectByForm(LinkMeta.class, imageForm);

	ImageInfo imageInfo = imageProcessor.getImageInfo(imageFile);
	if (imageInfo.getType() != MediaType.PNG) {
	    File pngFile = Files.replaceExtension(imageFile, "png");
	    imageProcessor.convert(imageFile, pngFile);
	    imageFile = pngFile;
	    imageInfo = imageProcessor.getImageInfo(imageFile);
	}

	File targetFile = Files.mediaFile(linkMeta);
	targetFile.getParentFile().mkdirs();
	targetFile.delete();

	if (!imageFile.renameTo(targetFile)) {
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
	image.setSrc(Files.mediaSrc(linkMeta));
	return image;
    }

    private Image uploadObjectImage(Form imageForm, File imageFile) throws IOException, BusinessException {
	AtlasItem atlasItem = getObjectByForm(AtlasItem.class, imageForm);
	String imageKey = imageForm.getValue("image-key");

	Params.notZero(atlasItem.getId(), "Atlas item ID");
	Params.notNullOrEmpty(imageKey, "Image key");

	businessRules.uniqueImage(atlasItem.getId(), imageKey);
	businessRules.transparentImage(imageKey, imageFile);

	ImageInfo imageInfo = imageProcessor.getImageInfo(imageFile);
	String targetExtension = null;
	switch (imageKey) {
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
	targetFile.getParentFile().mkdirs();
	targetFile.delete();

	if (!imageInfo.getType().extension().equals(targetExtension)) {
	    imageProcessor.convert(imageFile, (imageFile = Files.replaceExtension(imageFile, targetExtension)));
	}
	if (!imageFile.renameTo(targetFile)) {
	    throw new IOException("Unable to upload " + targetFile);
	}

	Image image = new Image();
	image.setImageKey(imageKey);
	image.setUploadDate(new Date());
	image.setSource(imageForm.getValue("source"));
	image.setFileName(targetFile.getName());

	image.setFileSize(imageInfo.getFileSize());
	image.setWidth(imageInfo.getWidth());
	image.setHeight(imageInfo.getHeight());

	atlasDao.addObjectImage(atlasItem.getId(), image);

	image.setSrc(Files.mediaSrc(atlasItem, targetFile.getName()));
	return image;
    }

    @Override
    public Image replaceImage(Form imageForm) throws IOException, BusinessException {
	UploadedFile uploadedFile = imageForm.getUploadedFile("media-file");
	return replaceImage(imageForm, uploadedFile.getFile());
    }

    @Override
    public Image replaceImageBySource(Form imageForm) throws IOException, BusinessException {
	Params.notNull(imageForm.getValue("source"), "Picture source");
	URL url = new URL(imageForm.getValue("source"));
	return replaceImage(imageForm, Files.copy(url));
    }

    private Image replaceImage(Form imageForm, File imageFile) throws BusinessException, IOException {
	Params.EQ(imageForm.getValue("image-kind"), "OBJECT", "Object class");
	AtlasItem atlasItem = getObjectByForm(AtlasItem.class, imageForm);
	String imageKey = imageForm.getValue("image-key");

	Params.notZero(atlasItem.getId(), "Atlas item ID");
	Params.notNullOrEmpty(imageKey, "Image key");
	businessRules.transparentImage(imageKey, imageFile);

	Image image = atlasDao.getImageByKey(atlasItem.getId(), imageKey);
	MediaFileHandler handler = new MediaFileHandler(atlasItem, image.getFileName());
	imageFile.renameTo(handler.target());
	updateImage(image, handler.target(), handler.targetSrc());
	return image;
    }

    @Override
    public void removeImage(AtlasItem atlasItem, Image image) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(atlasItem, image.getFileName());
	handler.delete();
	image.removeIcon(atlasItem);
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
	if (handler.target().exists()) {
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
	if (!sampleFile.exists()) {
	    log.error("Database not consistent. Missing sample file |%s|. Reset sample and waveform for object |%s|.",
		    sampleFile, atlasItem.getName());
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
	if (objectId == null) {
	    throw new BugError("Media form should have <object-id> field.");
	}
	try {
	    return atlasDao.getObjectById(type, Integer.parseInt(objectId));
	} catch (NumberFormatException unused) {
	    throw new BugError("Media form <object-id> field should be numeric.");
	}
    }

    private AudioSampleInfo getAudioSampleInfo(RepositoryObject collectionItem, File file, MediaSRC mediaSrc)
	    throws IOException {
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
	while ((object = atlasDao.getNextAtlasObject(object.getId())) != null) {
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
	for (KeywordIndex<Integer> keywordIndex : index.find(criterion)) {
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
    public ExportObject getExportObject(int objectId) {
	AtlasObject object = atlasDao.getAtlasObject(objectId);
	ExportObject exportObject = new ExportObject(object);
	for (AtlasItem related : atlasDao.getRelatedAtlasObjects(object.getCollection().getId(), object.getRelated())) {
	    exportObject.addRelated(related);
	}
	return exportObject;
    }

    @Override
    public Feature updateFeatureDisplay(Feature feature) {
	// reuse handler used to update feature display after loading object from
	// database
	feature.postLoad();
	return feature;
    }
}
