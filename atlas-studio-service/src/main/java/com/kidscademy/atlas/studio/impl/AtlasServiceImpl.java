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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kidscademy.atlas.studio.AtlasService;
import com.kidscademy.atlas.studio.BusinessRules;
import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.dao.TaxonomyDao;
import com.kidscademy.atlas.studio.model.AtlasCollection;
import com.kidscademy.atlas.studio.model.AtlasItem;
import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.Image;
import com.kidscademy.atlas.studio.model.Link;
import com.kidscademy.atlas.studio.model.MediaSRC;
import com.kidscademy.atlas.studio.model.RepositoryObject;
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
import com.kidscademy.atlas.studio.util.Files;
import com.kidscademy.atlas.studio.util.Strings;
import com.kidscademy.atlas.studio.www.CambridgeDictionary;
import com.kidscademy.atlas.studio.www.MerriamWebster;
import com.kidscademy.atlas.studio.www.SoftSchools;
import com.kidscademy.atlas.studio.www.TheFreeDictionary;
import com.kidscademy.atlas.studio.www.WikiHow;
import com.kidscademy.atlas.studio.www.Wikipedia;
import com.kidscademy.atlas.studio.www.WikipediaPageSummary;

import js.json.Json;
import js.lang.BugError;
import js.lang.GType;
import js.log.Log;
import js.log.LogFactory;
import js.rmi.BusinessException;
import js.tiny.container.core.AppContext;
import js.tiny.container.http.form.Form;
import js.tiny.container.http.form.UploadedFile;
import js.util.Classes;
import js.util.Params;

public class AtlasServiceImpl implements AtlasService {
    private static final Log log = LogFactory.getLog(AtlasServiceImpl.class);

    private final AppContext context;
    private final Json json;

    private final AtlasDao atlasDao;
    private final TaxonomyDao taxonomyDao;
    private final AudioProcessor audioProcessor;
    private final ImageProcessor imageProcessor;
    private final Wikipedia wikipedia;
    private final SoftSchools softSchools;
    private final TheFreeDictionary freeDictionary;
    private final CambridgeDictionary cambridgeDictionary;
    private final MerriamWebster merriamWebster;

    private KeywordTree<KeywordIndex<Integer>> index;

    public AtlasServiceImpl(AppContext context) throws IOException {
	log.trace("AtlasServiceImpl(AppContext)");

	this.context = context;
	this.json = Classes.loadService(Json.class);

	this.atlasDao = context.getInstance(AtlasDao.class);
	this.taxonomyDao = context.getInstance(TaxonomyDao.class);
	this.audioProcessor = context.getInstance(AudioProcessor.class);
	this.imageProcessor = context.getInstance(ImageProcessor.class);
	this.wikipedia = context.getInstance(Wikipedia.class);
	this.softSchools = context.getInstance(SoftSchools.class);
	this.freeDictionary = context.getInstance(TheFreeDictionary.class);
	this.cambridgeDictionary = context.getInstance(CambridgeDictionary.class);
	this.merriamWebster = context.getInstance(MerriamWebster.class);

	File file = context.getAppFile("search-index");
	List<KeywordIndex<Integer>> searchIndex;
	if (file.exists()) {
	    searchIndex = json.parse(new FileReader(file),
		    new GType(List.class, new GType(KeywordIndex.class, Integer.class)));
	} else {
	    searchIndex = Collections.emptyList();
	}

	this.index = new KeywordTree<>(searchIndex);
    }

    @Override
    public List<AtlasCollection> getCollections() {
	return atlasDao.getCollections();
    }

    @Override
    public List<AtlasItem> getCollectionItems(Map<String, String> filter, int collectionId) {
	return atlasDao.getCollectionItems(filter, collectionId);
    }

    @Override
    public List<AtlasItem> getCollectionItemsByTaxon(int collectionId, Taxon taxon, List<AtlasItem> excludes) {
	List<AtlasItem> items = atlasDao.getCollectionItemsByTaxon(collectionId, taxon);
	items.removeAll(excludes);
	return items;
    }

    @Override
    public AtlasObject createAtlasObject(AtlasCollection collection) {
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
    public AtlasObject saveAtlasObject(AtlasObject object) throws IOException {
	Params.notNull(object.getName(), "Atlas object name");
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

	atlasDao.saveAtlasObject(object);
	return object;
    }

    @Override
    public void removeAtlasObject(int objectId) {
	atlasDao.removeAtlasObject(objectId);
    }

    @Override
    public List<AtlasItem> getRelatedAtlasObjects(int collectionId, List<String> relatedNames) {
	return atlasDao.getRelatedAtlasObjects(collectionId, relatedNames);
    }

    @Override
    public Link createLink(Link link) {
	return Link.create(link);
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
    public String importObjectDescription(Link link) {
	switch (link.getDomain()) {
	case "softschools.com":
	    return Strings.html(softSchools.getFacts(link.getPath()).getDescription());

	case "wikipedia.org":
	    WikipediaPageSummary summary = wikipedia.getPageSummary(link.getFileName());
	    return summary != null ? Strings.html(summary.getExtract()) : null;

	default:
	    return null;
	}
    }

    @Override
    public Map<String, String> importObjectsFacts(Link link) {
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

    private static final String[] TAXON_NAMES = new String[] { "Kingdom", "Phylum", "Class", "Order", "Suborder",
	    "Family", "Genus", "Species", "Subspecies" };

    @Override
    public List<Taxon> loadAtlasObjectTaxonomy(String objectName) {
	Params.notNullOrEmpty(objectName, "Atlas object name");
	Map<String, String> sourceTaxonomy = taxonomyDao.getObjectTaxonomy(Strings.binomialName(objectName));
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
	AtlasItem atlasItem = getAtlasItem(imageForm);
	String imageKey = imageForm.getValue("image-key");

	Params.notZero(atlasItem.getId(), "Atlas item ID");
	Params.notNullOrEmpty(imageKey, "Image key");

	BusinessRules.uniqueImage(atlasItem.getId(), imageKey);
	BusinessRules.transparentImage(imageKey, imageFile);

	ImageInfo imageInfo = imageProcessor.getImageInfo(imageFile);

	File targetFile = Files.mediaFile(atlasItem, imageKey, imageInfo.getType().extension());
	targetFile.getParentFile().mkdirs();
	targetFile.delete();

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
    public Image cloneImageToIcon(AtlasItem atlasItem, Image image) throws IOException {
	image.setImageKey(Image.KEY_ICON);

	// by convention image key is used as image file base name
	File iconFile = Files.mediaFile(atlasItem, image.getImageKey(), Files.getExtension(image.getFileName()));
	iconFile.getParentFile().mkdirs();
	iconFile.delete();

	Files.copy(Files.mediaFile(image.getSrc()), iconFile);

	image.setUploadDate(new Date());
	image.setFileName(iconFile.getName());
	atlasDao.addObjectImage(atlasItem.getId(), image);

	updateImage(image, iconFile, Files.mediaSrc(atlasItem, iconFile.getName()));
	return image;
    }

    @Override
    public Image trimImage(AtlasItem atlasItem, Image image) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(atlasItem, image.getFileName());
	imageProcessor.trim(handler.source(), handler.target());
	updateImage(image, handler.target(), handler.targetSrc());
	return image;
    }

    @Override
    public Image flopImage(AtlasItem atlasItem, Image image) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(atlasItem, image.getFileName());
	imageProcessor.flop(handler.source(), handler.target());
	updateImage(image, handler.target(), handler.targetSrc());
	return image;
    }

    @Override
    public Image flipImage(AtlasItem atlasItem, Image image) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(atlasItem, image.getFileName());
	imageProcessor.flip(handler.source(), handler.target());
	updateImage(image, handler.target(), handler.targetSrc());
	return image;
    }

    @Override
    public Image rotateImageLeft(AtlasItem atlasItem, Image image) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(atlasItem, image.getFileName());
	imageProcessor.rotate(handler.source(), handler.target(), -22.5F);
	updateImage(image, handler.target(), handler.targetSrc());
	return image;
    }

    @Override
    public Image rotateImageRight(AtlasItem atlasItem, Image image) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(atlasItem, image.getFileName());
	imageProcessor.rotate(handler.source(), handler.target(), 22.4F);
	updateImage(image, handler.target(), handler.targetSrc());
	return image;
    }

    @Override
    public Image cropImage(AtlasItem atlasItem, Image image, int width, int height, int xoffset, int yoffset)
	    throws IOException {
	MediaFileHandler handler = new MediaFileHandler(atlasItem, image.getFileName());
	imageProcessor.crop(handler.source(), handler.target(), width, height, xoffset, yoffset);
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

    @Override
    public Image commitImage(AtlasItem atlasItem, Image image) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(atlasItem, image.getFileName());
	handler.commit();
	image.updateIcon(atlasItem);
	updateImage(image, handler.source(), handler.sourceSrc());
	return image;
    }

    @Override
    public void rollbackImage(AtlasItem atlasItem, Image image) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(atlasItem, image.getFileName());
	handler.rollback();
    }

    @Override
    public Image undoImage(AtlasItem atlasItem, Image image) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(atlasItem, image.getFileName());
	handler.undo();
	updateImage(image, handler.source(), handler.sourceSrc());
	return image;
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
	AtlasItem atlasItem = getAtlasItem(audioSampleForm);
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

    private AtlasItem getAtlasItem(Form mediaForm) {
	String objectId = mediaForm.getValue("atlas-object-id");
	if (objectId == null) {
	    throw new BugError("Media form should have <atlas-object-id> field.");
	}
	try {
	    return atlasDao.getAtlasItem(Integer.parseInt(objectId));
	} catch (NumberFormatException unused) {
	    throw new BugError("Media form <atlas-object-id> field should be numeric.");
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
}
