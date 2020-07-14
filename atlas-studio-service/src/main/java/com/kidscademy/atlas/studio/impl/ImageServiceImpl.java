package com.kidscademy.atlas.studio.impl;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import com.kidscademy.atlas.studio.ImageService;
import com.kidscademy.atlas.studio.model.Image;
import com.kidscademy.atlas.studio.model.MediaSRC;
import com.kidscademy.atlas.studio.model.RotateDirection;
import com.kidscademy.atlas.studio.tool.ImageInfo;
import com.kidscademy.atlas.studio.tool.ImageProcessor;

import js.log.Log;
import js.log.LogFactory;
import js.tiny.container.core.AppContext;

public class ImageServiceImpl implements ImageService {
    private static final Log log = LogFactory.getLog(ImageService.class);

    private final ImageProcessor imageProcessor;

    public ImageServiceImpl(AppContext context) {
	this.imageProcessor = context.getInstance(ImageProcessor.class);
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
    public Image cropCircleImage(Image image, int width, int height, int xoffset, int yoffset, String borderColor,
	    int borderWidth) throws IOException {
	MediaFileHandler handler = new MediaFileHandler(image.getSrc());
	imageProcessor.cropCircle(handler.source(), handler.target(), width, height, xoffset, yoffset, borderColor,
		borderWidth);
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

	// search image parent directory for image variants and remove all
	// images variants are generated on the fly using base image content
	// by convention images name uses underscore ('_') for variants separator

	File[] variants = handler.source().getParentFile().listFiles(new FilenameFilter() {
	    @Override
	    public boolean accept(File dir, String name) {
		return name.contains("_");
	    }
	});
	if (variants != null) {
	    for (File variant : variants) {
		if (!variant.delete()) {
		    log.error("Fail to delete image variant file |%s|.", variant);
		}
	    }
	}

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

    // ----------------------------------------------------------------------------------------------

    private void updateImage(Image image, File file, MediaSRC src) throws IOException {
	ImageInfo info = imageProcessor.getImageInfo(file);
	image.setFileSize(info.getFileSize());
	image.setWidth(info.getWidth());
	image.setHeight(info.getHeight());
	image.setSrc(src);
    }
}
