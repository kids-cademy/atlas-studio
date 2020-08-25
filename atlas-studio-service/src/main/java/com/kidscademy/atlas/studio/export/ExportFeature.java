package com.kidscademy.atlas.studio.export;

import js.lang.Displayable;
import js.util.Strings;

public class ExportFeature implements Displayable
{
  private String nameDisplay;
  private String valueDisplay;

  public ExportFeature() {
  }

  public ExportFeature(String name, String value) {
    this.nameDisplay = name;
    this.valueDisplay = value;
  }

  @Override
  public String toDisplay() {
    return Strings.concat(nameDisplay, ' ', valueDisplay);
  }

  @Override
  public String toString() {
    return Strings.toString(nameDisplay, valueDisplay);
  }
}
