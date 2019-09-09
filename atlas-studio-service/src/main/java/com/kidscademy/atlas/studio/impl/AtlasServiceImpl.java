package com.kidscademy.atlas.studio.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kidscademy.atlas.studio.AtlasService;
import com.kidscademy.atlas.studio.BusinessRules;
import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.model.AtlasCollection;
import com.kidscademy.atlas.studio.model.AtlasItem;
import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.CollectionItem;
import com.kidscademy.atlas.studio.model.Image;
import com.kidscademy.atlas.studio.model.Link;
import com.kidscademy.atlas.studio.model.MediaSRC;
import com.kidscademy.atlas.studio.tool.AudioProcessor;
import com.kidscademy.atlas.studio.tool.AudioSampleInfo;
import com.kidscademy.atlas.studio.tool.ImageInfo;
import com.kidscademy.atlas.studio.tool.ImageProcessor;
import com.kidscademy.atlas.studio.util.Files;
import com.kidscademy.atlas.studio.util.Strings;
import com.kidscademy.atlas.studio.www.CambridgeDictionary;
import com.kidscademy.atlas.studio.www.SoftSchools;
import com.kidscademy.atlas.studio.www.TheFreeDictionary;
import com.kidscademy.atlas.studio.www.Wikipedia;
import com.kidscademy.atlas.studio.www.WikipediaPageSummary;

import js.http.form.Form;
import js.http.form.UploadedFile;
import js.lang.BugError;
import js.log.Log;
import js.log.LogFactory;
import js.rmi.BusinessException;
import js.util.Params;

public class AtlasServiceImpl implements AtlasService {
    private static final Log log = LogFactory.getLog(AtlasServiceImpl.class);

    private final AtlasDao atlasDao;
    private final AudioProcessor audioProcessor;
    private final ImageProcessor imageProcessor;
    private final Wikipedia wikipedia;
    private final SoftSchools softSchools;
    private final TheFreeDictionary freeDictionary;
    private final CambridgeDictionary cambridgeDictionary;

    public AtlasServiceImpl(AtlasDao atlasDao, AudioProcessor audioProcessor, ImageProcessor imageProcessor,
	    Wikipedia wikipedia, SoftSchools softSchools, TheFreeDictionary freeDictionary,
	    CambridgeDictionary cambridgeDictionary) {
	log.trace(
		"AtlasServiceImpl(AtlasDao,AudioProcessor,ImageProcessor,Wikipedia,SoftSchools,TheFreeDictionary,CambridgeDictionary)");
	this.atlasDao = atlasDao;
	this.audioProcessor = audioProcessor;
	this.imageProcessor = imageProcessor;
	this.wikipedia = wikipedia;
	this.softSchools = softSchools;
	this.freeDictionary = freeDictionary;
	this.cambridgeDictionary = cambridgeDictionary;
    }

    @Override
    public List<AtlasCollection> getCollections() {
	return atlasDao.getCollections();
    }

    @Override
    public List<AtlasItem> getCollectionItems(int collectionId) {
	return atlasDao.getCollectionItems(collectionId);
    }

    @Override
    public AtlasObject getAtlasObject(int objectId) throws IOException {
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
    public AtlasObject getAtlasObjectByName(String name) {
	// return dao.getObjectByName(name);
	// TODO null placeholder
	return null;
    }

    @Override
    public AtlasObject saveAtlasObject(AtlasObject AtlasObject) throws IOException {
	if (AtlasObject.getSampleSrc() != null) {
	    MediaFileHandler handler = new MediaFileHandler(AtlasObject, "sample.mp3");
	    handler.commit();
	    AtlasObject.setSampleSrc(handler.sourceSrc());
	    AtlasObject.setWaveformSrc(generateWaveform(AtlasObject, handler.source()));
	}

	if (AtlasObject.getImages() != null) {
	    for (Image picture : AtlasObject.getImages()) {
		MediaFileHandler handler = new MediaFileHandler(AtlasObject, picture.getFileName());
		handler.commit();
	    }
	}

	atlasDao.saveAtlasObject(AtlasObject);
	return AtlasObject;
    }

    @Override
    public List<AtlasItem> getRelatedAtlasObjects(int collectionId, List<String> objectNames) {
	return atlasDao.findObjectsByNames(collectionId, objectNames);
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
	String imageName = imageForm.getValue("name");

	Params.notZero(atlasItem.getId(), "Atlas item ID");
	Params.notNullOrEmpty(imageName, "Image name");

	BusinessRules.uniquePictureName(atlasItem.getId(), imageName);
	BusinessRules.transparentFeaturedPicture(imageName, imageFile);

	ImageInfo imageInfo = imageProcessor.getImageInfo(imageFile);

	File targetFile = Files.mediaFile(atlasItem, imageName, imageInfo.getType().extension());
	targetFile.getParentFile().mkdirs();
	targetFile.delete();

	if (!imageFile.renameTo(targetFile)) {
	    throw new IOException("Unable to upload " + targetFile);
	}

	Image image = new Image();
	image.setName(imageName);
	image.setUploadDate(new Date());
	image.setSource(imageForm.getValue("source"));
	image.setFileName(targetFile.getName());

	image.setFileSize(imageInfo.getFileSize());
	image.setWidth(imageInfo.getWidth());
	image.setHeight(imageInfo.getHeight());

	atlasDao.addObjectPicture(atlasItem.getId(), image);

	image.setSrc(Files.mediaSrc(atlasItem, targetFile.getName()));
	return image;
    }

    @Override
    public Image duplicateImage(AtlasItem atlasItem, Image image) throws IOException {
	File targetFile = Files.mediaFile(atlasItem, image.getName(), Files.getExtension(image.getFileName()));
	targetFile.getParentFile().mkdirs();
	targetFile.delete();

	Files.copy(Files.mediaFile(image.getSrc()), targetFile);

	image.setUploadDate(new Date());
	image.setFileName(targetFile.getName());
	atlasDao.addObjectPicture(atlasItem.getId(), image);

	updateImage(image, targetFile, Files.mediaSrc(atlasItem, targetFile.getName()));
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
	atlasDao.removeObjectPicture(atlasItem.getId(), image);
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

    private AudioSampleInfo getAudioSampleInfo(CollectionItem collectionItem, File file, MediaSRC mediaSrc)
	    throws IOException {
	AudioSampleInfo info = audioProcessor.getAudioFileInfo(file);
	info.setSampleSrc(mediaSrc);
	info.setWaveformSrc(generateWaveform(collectionItem, file));
	return info;
    }

    private MediaSRC generateWaveform(CollectionItem collectionItem, File audioFile) throws IOException {
	MediaSRC waveformSrc = Files.mediaSrc(collectionItem, "waveform.png");
	File waveformFile = Files.mediaFile(waveformSrc);
	audioProcessor.generateWaveform(audioFile, waveformFile);
	return waveformSrc;
    }
}
