package com.kidscademy.atlas.studio.export;

import js.lang.Displayable;
import js.util.Strings;

public class ExportTaxon implements Displayable
{
  private String display;
  private String value;

  public ExportTaxon() {
  }

  public ExportTaxon(String display, String value) {
    this.display = display;
    this.value = value;
  }

  public String getDisplay() {
    return display;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toDisplay() {
    return Strings.concat(display, ' ', value);
  }

  @Override
  public String toString() {
    return Strings.toString(display, value);
  }
}
