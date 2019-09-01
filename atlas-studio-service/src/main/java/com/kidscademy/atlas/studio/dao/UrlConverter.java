package com.kidscademy.atlas.studio.dao;

import java.net.MalformedURLException;
import java.net.URL;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import js.log.Log;
import js.log.LogFactory;

@Converter(autoApply = true)
public class UrlConverter implements AttributeConverter<URL, String> {
    private static final Log log = LogFactory.getLog(UrlConverter.class);

    @Override
    public String convertToDatabaseColumn(URL url) {
	return url != null ? url.toExternalForm() : null;
    }

    @Override
    public URL convertToEntityAttribute(String value) {
	if (value == null) {
	    return null;
	}

	try {
	    return new URL(value);
	} catch (MalformedURLException e) {
	    log.error(e);
	}
	return null;
    }
}
