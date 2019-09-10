package com.kidscademy.atlas.studio;

import java.io.File;
import java.io.IOException;

import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.model.Image;
import com.kidscademy.atlas.studio.tool.ImageProcessor;

import js.core.Factory;
import js.rmi.BusinessException;

public class BusinessRules {
    private static final int NOT_UNIQUE_PICTURE_FILE_NAME = 0x0001;
    private static final int NOT_TRANSPARENT_FEATURED_PICTURE = 0x0002;

    private static final AtlasDao atlasDao;
    private static final ImageProcessor imageProcessor;
    static {
	atlasDao = Factory.getInstance(AtlasDao.class);
	imageProcessor = Factory.getInstance(ImageProcessor.class);
    }

    public static void transparentImage(String imageKey, File imageFile) throws BusinessException, IOException {
	if ((Image.KEY_COVER.equals(imageKey) || Image.KEY_FEATURED.equals(imageKey)) && imageProcessor.isOpaque(imageFile)) {
	    throw new BusinessException(NOT_TRANSPARENT_FEATURED_PICTURE);
	}
    }

    public static void uniqueImage(int objectId, String imageKey) throws BusinessException {
	if (atlasDao.getImageByKey(objectId, imageKey) != null) {
	    throw new BusinessException(NOT_UNIQUE_PICTURE_FILE_NAME);
	}
    }
}
