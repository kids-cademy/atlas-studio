package com.kidscademy.atlas.studio.export;

import java.util.Map;

import js.lang.Displayable;
import js.util.Strings;

public class ExportFact implements Displayable
{
  private String name;
  private String value;

  public ExportFact() {
  }

  public ExportFact(Map.Entry<String, String> fact) {
    this.name = fact.getKey();
    this.value = fact.getValue();
  }

  @Override
  public String toDisplay() {
    return Strings.concat(name, ' ', value);
  }

  @Override
  public String toString() {
    return Strings.toString(name, value);
  }
}
