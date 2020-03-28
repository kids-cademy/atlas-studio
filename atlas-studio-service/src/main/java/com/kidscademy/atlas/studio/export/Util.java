package com.kidscademy.atlas.studio.export;

import js.util.Params;

public class Util
{
  public static String path(String objectName, String fileName)
  {
    Params.notNullOrEmpty(objectName, "Object name");
    // test null file name here in order to simplify invoker logic
    if(fileName == null) {
      return null;
    }
    return String.format("atlas/%s/%s", objectName, fileName);
  }
}
