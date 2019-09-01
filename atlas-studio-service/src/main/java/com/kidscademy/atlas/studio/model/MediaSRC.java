package com.kidscademy.atlas.studio.model;

import js.converter.Converter;
import js.converter.ConverterException;

public class MediaSRC implements Converter
{
  private String value;

  public MediaSRC()
  {
  }

  public MediaSRC(String value)
  {
    this.value = value;
  }

  public String basePath()
  {
    return value.substring(0, value.lastIndexOf('/') + 1);
  }

  public String fileName()
  {
    return value != null ? value.substring(value.lastIndexOf('/') + 1) : null;
  }

  public String value()
  {
    return value;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if(this == obj) return true;
    if(obj == null) return false;
    if(getClass() != obj.getClass()) return false;
    MediaSRC other = (MediaSRC)obj;
    if(value == null) {
      if(other.value != null) return false;
    }
    else if(!value.equals(other.value)) return false;
    return true;
  }

  @Override
  public String toString()
  {
    return value;
  }

  // ----------------------------------------------------------------------------------------------
  // implementation for Converter interface
  // runs in separated instance

  @SuppressWarnings("unchecked")
  @Override
  public <T> T asObject(String string, Class<T> valueType) throws IllegalArgumentException, ConverterException
  {
    return (T)new MediaSRC(string);
  }

  @Override
  public String asString(Object object) throws ConverterException
  {
    return ((MediaSRC)object).value;
  }
}
