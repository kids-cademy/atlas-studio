package com.kidscademy.atlas.studio;

import java.io.File;
import java.io.IOException;

import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.tool.ImageProcessor;

import js.core.Factory;
import js.rmi.BusinessException;

public class BusinessRules {
    private static final int NOT_UNIQUE_PICTURE_FILE_NAME = 0x0001;
    private static final int NOT_TRANSPARENT_FEATURED_PICTURE = 0x0002;

    private static final AtlasDao dao;
    private static final ImageProcessor image;
    static {
	dao = Factory.getInstance(AtlasDao.class);
	image = Factory.getInstance(ImageProcessor.class);
    }

    public static void transparentFeaturedPicture(String imageKind, File imageFile)
	    throws BusinessException, IOException {
	if (imageKind.contentEquals("featured") && image.isOpaque(imageFile)) {
	    throw new BusinessException(NOT_TRANSPARENT_FEATURED_PICTURE);
	}
    }

    public static void uniquePictureName(int objectId, String name) throws BusinessException {
	if (dao.getPictureByName(objectId, name) != null) {
	    throw new BusinessException(NOT_UNIQUE_PICTURE_FILE_NAME);
	}
    }
}
