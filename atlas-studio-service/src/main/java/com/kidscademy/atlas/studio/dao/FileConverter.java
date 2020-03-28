package com.kidscademy.atlas.studio.dao;

import java.io.File;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class FileConverter implements AttributeConverter<File, String> {
    @Override
    public String convertToDatabaseColumn(File file) {
	return file != null ? file.getAbsolutePath() : null;
    }

    @Override
    public File convertToEntityAttribute(String path) {
	return path != null ? new File(path) : null;
    }
}
