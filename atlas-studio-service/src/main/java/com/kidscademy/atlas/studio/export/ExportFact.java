package com.kidscademy.atlas.studio.export;

import js.lang.Displayable;
import js.util.Strings;

public class ExportFact implements Displayable
{
  private String name;
  private String value;

  public ExportFact() {
  }

  public ExportFact(String name, String value) {
    this.name = name;
    this.value = value;
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
