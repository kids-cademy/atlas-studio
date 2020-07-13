package com.kidscademy.atlas.studio;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.model.AtlasCollection;
import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.Image;
import com.kidscademy.atlas.studio.model.LinkSource;
import com.kidscademy.atlas.studio.tool.ImageProcessor;
import com.kidscademy.atlas.studio.util.Strings;

import js.rmi.BusinessException;

public class BusinessRules {
    private static final int UNIQUE_PICTURE_FILE_NAME = 0x0001;
    private static final int TRANSPARENT_FEATURED_PICTURE = 0x0002;
    private static final int UNIQUE_COLLECTION_NAME = 0x0003;
    private static final int UNIQUE_OBJECT_NAME = 0x0004;
    private static final int EMPTY_COLLECTION = 0x0005;
    private static final int EMPTY_RELEASE = 0x0006;
    private static final int REGISTERED_LINK_DOMAIN = 0x0007;
    private static final int IMAGE_DIMENSIONS = 0x0008;
    private static final int NO_LINK_SOURCE = 0x0009;

    private final AtlasDao atlasDao;
    private final ImageProcessor imageProcessor;

    public BusinessRules(AtlasDao atlasDao, ImageProcessor imageProcessor) {
	this.atlasDao = atlasDao;
	this.imageProcessor = imageProcessor;
    }

    public void transparentImage(String imageKey, File imageFile) throws BusinessException, IOException {
	if ((Image.KEY_COVER.equals(imageKey) || Image.KEY_FEATURED.equals(imageKey))
		&& imageProcessor.isOpaque(imageFile)) {
	    throw new BusinessException(TRANSPARENT_FEATURED_PICTURE);
	}
    }

    public void uniqueImage(int objectId, String imageKey) throws BusinessException {
	if (atlasDao.getImageByKey(objectId, imageKey) != null) {
	    throw new BusinessException(UNIQUE_PICTURE_FILE_NAME);
	}
    }

    public void uniqueCollectionName(AtlasCollection collection) throws BusinessException {
	if (!atlasDao.uniqueCollectionName(collection)) {
	    throw new BusinessException(UNIQUE_COLLECTION_NAME);
	}
    }

    public void uniqueObjectName(AtlasObject object) throws BusinessException {
	if (!atlasDao.uniqueObjectName(object)) {
	    throw new BusinessException(UNIQUE_OBJECT_NAME);
	}
    }

    public void emptyCollection(int collectionId) throws BusinessException {
	if (atlasDao.getCollectionSize(collectionId) > 0) {
	    throw new BusinessException(EMPTY_COLLECTION);
	}
    }

    public void emptyRelease(int releaseId) throws BusinessException {
	if (atlasDao.getReleaseSize(releaseId) > 0) {
	    throw new BusinessException(EMPTY_RELEASE);
	}
    }

    public void registeredLinkDomain(URL link) throws BusinessException {
	if (atlasDao.getExternalSourceByDomain(Strings.basedomain(link)) == null) {
	    throw new BusinessException(REGISTERED_LINK_DOMAIN);
	}
    }

    public void imageDimensions(int dimension, int desired) throws BusinessException {
	if (dimension < desired) {
	    throw new BusinessException(IMAGE_DIMENSIONS);
	}
    }

    public void registeredLinkSource(LinkSource linkSource) throws BusinessException {
	if (linkSource == null) {
	    throw new BusinessException(NO_LINK_SOURCE);
	}
    }
}
