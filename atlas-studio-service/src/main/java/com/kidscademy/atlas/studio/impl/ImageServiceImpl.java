package com.kidscademy.atlas.studio.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

import com.kidscademy.atlas.studio.BusinessRules;
import com.kidscademy.atlas.studio.ImageService;
import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.model.AtlasItem;
import com.kidscademy.atlas.studio.model.Image;
import com.kidscademy.atlas.studio.model.MediaSRC;
import com.kidscademy.atlas.studio.model.RotateDirection;
import com.kidscademy.atlas.studio.tool.ImageInfo;
import com.kidscademy.atlas.studio.tool.ImageProcessor;
import com.kidscademy.atlas.studio.tool.MediaType;
import com.kidscademy.atlas.studio.util.Files;

import js.lang.BugError;
import js.rmi.BusinessException;
import js.tiny.container.core.AppContext;
import js.tiny.container.http.form.Form;
import js.tiny.container.http.form.UploadedFile;
import js.util.Params;

public class ImageServiceImpl implements ImageService {
    private final AtlasDao atlasDao;
    private final ImageProcessor imageProcessor;

    public ImageServiceImpl(AppContext context) {
	this.atlasDao = context.getInstance(AtlasDao.class);
	this.imageProcessor = context.getInstance(ImageProcessor.class);
    }

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
	AtlasItem atlasItem = getAtlasItemByForm(imageForm);
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
    public void removeImage(AtlasItem atlasItem, Image image) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(image.getSrc());
	handler.delete();
	image.removeIcon(atlasItem);
	atlasDao.removeObjectImage(atlasItem.getId(), image);
    }

    @Override
    public Image trimImage(Image image) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(image.getSrc());
	imageProcessor.trim(handler.source(), handler.target());
	updateImage(image, handler.target(), handler.targetSrc());
	return image;
    }

    @Override
    public Image flopImage(Image image) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(image.getSrc());
	imageProcessor.flop(handler.source(), handler.target());
	updateImage(image, handler.target(), handler.targetSrc());
	return image;
    }

    @Override
    public Image flipImage(Image image) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(image.getSrc());
	imageProcessor.flip(handler.source(), handler.target());
	updateImage(image, handler.target(), handler.targetSrc());
	return image;
    }

    @Override
    public Image rotateImage(Image image, RotateDirection direction, float angle) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(image.getSrc());
	imageProcessor.rotate(handler.source(), handler.target(), direction == RotateDirection.CW ? angle : -angle);
	updateImage(image, handler.target(), handler.targetSrc());
	return image;
    }

    @Override
    public Image cropRectangleImage(Image image, int width, int height, int xoffset, int yoffset) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(image.getSrc());
	imageProcessor.crop(handler.source(), handler.target(), width, height, xoffset, yoffset);
	updateImage(image, handler.target(), handler.targetSrc());
	return image;
    }

    @Override
    public Image cropCircleImage(Image image, int width, int height, int xoffset, int yoffset) throws IOException {
//	File imageFile = Files.mediaFile(image.getSrc());
//	ImageInfo imageInfo = imageProcessor.getImageInfo(imageFile);
//	if (imageInfo.getType() != MediaType.PNG) {
//	    File pngFile = Files.replaceExtension(imageFile, "png");
//	    imageProcessor.convert(imageFile, pngFile);
//	    image.replaceExtension("png");
//	}

	MediaFileHandler handler = new MediaFileHandler(image.getSrc());
	imageProcessor.cropCircle(handler.source(), handler.target(), width, height, xoffset, yoffset);
	updateImage(image, handler.target(), handler.targetSrc());
	return image;
    }

    @Override
    public Image adjustBrightnessContrast(Image image, int brightness, int contrast) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(image.getSrc());
	imageProcessor.brightnessContrast(handler.source(), handler.target(), brightness, contrast);
	updateImage(image, handler.target(), handler.targetSrc());
	return image;
    }

    @Override
    public Image commitImage(Image image) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(image.getSrc());
	handler.commit();
	updateImage(image, handler.source(), handler.sourceSrc());
	return image;
    }

    @Override
    public void rollbackImage(Image image) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(image.getSrc());
	handler.rollback();
    }

    @Override
    public Image undoImage(Image image) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(image.getSrc());
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

    private AtlasItem getAtlasItemByForm(Form mediaForm) {
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
}
