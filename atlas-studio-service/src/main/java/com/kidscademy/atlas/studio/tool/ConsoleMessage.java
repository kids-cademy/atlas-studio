package com.kidscademy.atlas.studio.tool;

import js.lang.Event;

public class ConsoleMessage implements Event
{
  private final String text;

  public ConsoleMessage(String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
