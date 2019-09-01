package com.kidscademy.atlas.studio.dao;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.kidscademy.atlas.studio.model.AtlasObject;

@Converter(autoApply = true)
public class AtlasObjectStateConverter implements AttributeConverter<AtlasObject.State, String>
{

  @Override
  public String convertToDatabaseColumn(AtlasObject.State state)
  {
    return state.name();
  }

  @Override
  public AtlasObject.State convertToEntityAttribute(String value)
  {
    return AtlasObject.State.valueOf(value);
  }
}
