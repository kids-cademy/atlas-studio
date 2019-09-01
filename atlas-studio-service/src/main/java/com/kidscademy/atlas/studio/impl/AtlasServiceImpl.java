package com.kidscademy.atlas.studio.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kidscademy.atlas.studio.AtlasService;
import com.kidscademy.atlas.studio.BusinessRules;
import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.model.AtlasItem;
import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.CollectionObject;
import com.kidscademy.atlas.studio.model.Image;
import com.kidscademy.atlas.studio.model.Link;
import com.kidscademy.atlas.studio.model.MediaSRC;
import com.kidscademy.atlas.studio.model.User;
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

import js.core.AppContext;
import js.http.form.Form;
import js.http.form.UploadedFile;
import js.log.Log;
import js.log.LogFactory;
import js.rmi.BusinessException;
import js.util.Params;

public class AtlasServiceImpl implements AtlasService {
    private static final Log log = LogFactory.getLog(AtlasServiceImpl.class);

    private final AppContext context;
    private final AtlasDao atlasDao;
    private final AudioProcessor audioProcessor;
    private final ImageProcessor imageProcessor;
    private final Wikipedia wikipedia;
    private final SoftSchools softSchools;
    private final TheFreeDictionary freeDictionary;
    private final CambridgeDictionary cambridgeDictionary;

    public AtlasServiceImpl(AppContext context, AtlasDao atlasDao, AudioProcessor audioProcessor,
	    ImageProcessor imageProcessor, Wikipedia wikipedia, SoftSchools softSchools,
	    TheFreeDictionary freeDictionary, CambridgeDictionary cambridgeDictionary) {
	log.trace(
		"AtlasServiceImpl(AppContext,AtlasDao,AudioProcessor,ImageProcessor,Wikipedia,SoftSchools,TheFreeDictionary,CambridgeDictionary)");
	this.context = context;
	this.atlasDao = atlasDao;
	this.audioProcessor = audioProcessor;
	this.imageProcessor = imageProcessor;
	this.wikipedia = wikipedia;
	this.softSchools = softSchools;
	this.freeDictionary = freeDictionary;
	this.cambridgeDictionary = cambridgeDictionary;
    }

    @Override
    public List<AtlasItem> getCollectionItems(String collectionName) {
	return atlasDao.getCollectionItems(collectionName);
    }

    @Override
    public AtlasObject getAtlasObject(int objectId) throws IOException {
	if (objectId == 0) {
	    User user = context.getUserPrincipal();
	    // TODO: null placeholder
	    return AtlasObject.create(user, null);
	}
	AtlasObject object = atlasDao.getObjectById(objectId);

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

	atlasDao.saveObject(AtlasObject);
	return AtlasObject;
    }

    @Override
    public List<AtlasItem> getRelatedAtlasObjects(List<String> names) {
	// return dao.findObjectsByName(AtlasObject.class, names);
	// TODO empty placeholder
	return Collections.EMPTY_LIST;
    }

    @Override
    public List<AtlasItem> getAvailableAtlasObjects(String category, List<AtlasItem> related) {
	// List<AtlasItem> AtlasObjects = dao.getAtlasObjectsByCategory(category);
	// AtlasObjects.removeAll(related);
	// return AtlasObjects;
	// TODO empty placeholder
	return Collections.EMPTY_LIST;
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
    public Image uploadPicture(Form form) throws IOException, BusinessException {
	UploadedFile uploadedFile = form.getUploadedFile("media-file");
	return upload(form, uploadedFile.getFile());
    }

    @Override
    public Image uploadPictureBySource(Form form) throws IOException, BusinessException {
	Params.notNull(form.getValue("source"), "Picture source");
	URL url = new URL(form.getValue("source"));
	return upload(form, Files.copy(url));
    }

    private Image upload(Form form, File file) throws IOException, BusinessException {
	int objectId = Integer.parseInt(form.getValue("object-id"));
	String pictureName = form.getValue("name");

	Params.notZero(objectId, "Object ID");
	Params.notNullOrEmpty(pictureName, "Picture name");

	BusinessRules.uniquePictureName(objectId, pictureName);
	BusinessRules.transparentFeaturedPicture(pictureName, file);

	ImageInfo imageInfo = imageProcessor.getImageInfo(file);

	AtlasItem object = new AtlasItem(form.getValue("object-dtype"), form.getValue("object-name"));
	File targetFile = Files.mediaFile(object, pictureName, imageInfo.getType().extension());
	targetFile.getParentFile().mkdirs();
	targetFile.delete();

	if (!file.renameTo(targetFile)) {
	    throw new IOException("Unable to upload " + targetFile);
	}

	Image picture = new Image();
	picture.setName(pictureName);
	picture.setUploadDate(new Date());
	picture.setSource(form.getValue("source"));
	picture.setFileName(targetFile.getName());

	picture.setFileSize(imageInfo.getFileSize());
	picture.setWidth(imageInfo.getWidth());
	picture.setHeight(imageInfo.getHeight());

	atlasDao.addObjectPicture(objectId, picture);

	picture.setSrc(Files.mediaSrc(object, targetFile.getName()));
	return picture;
    }

    @Override
    public Image duplicatePicture(AtlasItem object, Image picture) throws IOException {
	File targetFile = Files.mediaFile(object, picture.getName(), Files.getExtension(picture.getFileName()));
	targetFile.getParentFile().mkdirs();
	targetFile.delete();

	Files.copy(Files.mediaFile(picture.getSrc()), targetFile);

	picture.setUploadDate(new Date());
	picture.setFileName(targetFile.getName());
	atlasDao.addObjectPicture(object.getId(), picture);

	updateImage(picture, targetFile, Files.mediaSrc(object, targetFile.getName()));
	return picture;
    }

    @Override
    public Image trimPicture(AtlasItem object, Image picture) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(object, picture.getFileName());
	imageProcessor.trim(handler.source(), handler.target());
	updateImage(picture, handler.target(), handler.targetSrc());
	return picture;
    }

    @Override
    public Image flopPicture(AtlasItem object, Image picture) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(object, picture.getFileName());
	imageProcessor.flop(handler.source(), handler.target());
	updateImage(picture, handler.target(), handler.targetSrc());
	return picture;
    }

    @Override
    public Image flipPicture(AtlasItem object, Image picture) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(object, picture.getFileName());
	imageProcessor.flip(handler.source(), handler.target());
	updateImage(picture, handler.target(), handler.targetSrc());
	return picture;
    }

    @Override
    public Image cropPicture(AtlasItem object, Image picture, int width, int height, int xoffset, int yoffset)
	    throws IOException {
	MediaFileHandler handler = new MediaFileHandler(object, picture.getFileName());
	imageProcessor.crop(handler.source(), handler.target(), width, height, xoffset, yoffset);
	updateImage(picture, handler.target(), handler.targetSrc());
	return picture;
    }

    @Override
    public void removePicture(AtlasItem object, Image picture) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(object, picture.getFileName());
	handler.delete();
	picture.removeIcon(object);
	atlasDao.removeObjectPicture(object.getId(), picture);
    }

    @Override
    public Image commitPicture(AtlasItem object, Image picture) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(object, picture.getFileName());
	handler.commit();
	picture.updateIcon(object);
	updateImage(picture, handler.source(), handler.sourceSrc());
	return picture;
    }

    @Override
    public void rollbackPicture(AtlasItem object, Image picture) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(object, picture.getFileName());
	handler.rollback();
    }

    @Override
    public Image undoPicture(AtlasItem object, Image picture) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(object, picture.getFileName());
	handler.undo();
	updateImage(picture, handler.source(), handler.sourceSrc());
	return picture;
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
    public AudioSampleInfo uploadAudioSample(Form form) throws IOException {
	AtlasItem object = new AtlasItem(form.getValue("dtype"), form.getValue("name"));
	MediaFileHandler handler = new MediaFileHandler(object, "sample.mp3");
	handler.upload(form.getFile("file"));
	return getAudioSampleInfo(object, handler.source(), handler.sourceSrc());
    }

    @Override
    public AudioSampleInfo normalizeAudioSample(AtlasItem object) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(object, "sample.mp3");
	audioProcessor.normalizeLevel(handler.source(), handler.target());
	if (handler.target().exists()) {
	    return getAudioSampleInfo(object, handler.target(), handler.targetSrc());
	}
	return getAudioSampleInfo(object, handler.source(), handler.sourceSrc());
    }

    @Override
    public AudioSampleInfo convertAudioSampleToMono(AtlasItem object) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(object, "sample.mp3");
	audioProcessor.convertToMono(handler.source(), handler.target());
	return getAudioSampleInfo(object, handler.target(), handler.targetSrc());
    }

    @Override
    public AudioSampleInfo trimAudioSampleSilence(AtlasItem object) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(object, "sample.mp3");
	audioProcessor.trimSilence(handler.source(), handler.target());
	return getAudioSampleInfo(object, handler.target(), handler.targetSrc());
    }

    @Override
    public AudioSampleInfo cutAudioSample(AtlasItem object, float start) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(object, "sample.mp3");
	audioProcessor.cutSegment(handler.source(), handler.target(), start, start + 30);
	return getAudioSampleInfo(object, handler.target(), handler.targetSrc());
    }

    @Override
    public AudioSampleInfo fadeInAudioSample(AtlasItem object) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(object, "sample.mp3");
	audioProcessor.fadeIn(handler.source(), handler.target(), 1.5F);
	return getAudioSampleInfo(object, handler.target(), handler.targetSrc());
    }

    @Override
    public AudioSampleInfo fadeOutAudioSample(AtlasItem object) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(object, "sample.mp3");
	audioProcessor.fadeOut(handler.source(), handler.target(), 1.5F);
	return getAudioSampleInfo(object, handler.target(), handler.targetSrc());
    }

    @Override
    public MediaSRC generateWaveform(AtlasItem object) throws IOException {
	Params.notZero(object.getId(), "Object ID");

	File sampleFile = Files.mediaFile(object, "sample.mp3");
	if (!sampleFile.exists()) {
	    log.error("Database not consistent. Missing sample file |%s|. Reset sample and waveform for object |%s|.",
		    sampleFile, object.getName());
	    atlasDao.resetObjectSample(object.getId());
	    return null;
	}
	return generateWaveform(object, sampleFile);
    }

    @Override
    public AudioSampleInfo undoAudioSampleProcessing(AtlasItem object) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(object, "sample.mp3");
	handler.undo();
	return getAudioSampleInfo(object, handler.source(), handler.sourceSrc());
    }

    @Override
    public AudioSampleInfo rollbackAudioSampleProcessing(AtlasItem object) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(object, "sample.mp3");
	handler.rollback();
	return getAudioSampleInfo(object, handler.source(), handler.sourceSrc());
    }

    @Override
    public void removeAudioSample(AtlasItem object) throws IOException {
	Params.notZero(object.getId(), "Object ID");

	MediaFileHandler handler = new MediaFileHandler(object, "sample.mp3");
	handler.delete();
	atlasDao.resetObjectSample(object.getId());
	Files.mediaFile(object, "sample.mp3").delete();
	Files.mediaFile(object, "waveform.png").delete();
    }

    // ----------------------------------------------------------------------------------------------

    private AudioSampleInfo getAudioSampleInfo(CollectionObject object, File file, MediaSRC mediaSrc)
	    throws IOException {
	AudioSampleInfo info = audioProcessor.getAudioFileInfo(file);
	info.setSampleSrc(mediaSrc);
	info.setWaveformSrc(generateWaveform(object, file));
	return info;
    }

    private MediaSRC generateWaveform(CollectionObject object, File audioFile) throws IOException {
	MediaSRC waveformSrc = Files.mediaSrc(object, "waveform.png");
	File waveformFile = Files.mediaFile(waveformSrc);
	audioProcessor.generateWaveform(audioFile, waveformFile);
	return waveformSrc;
    }
}
