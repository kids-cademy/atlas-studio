package com.kidscademy.atlas.studio;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import js.tiny.container.annotation.ContextParam;

public class CT
{
  public static final List<String> EMPTY_STRINGS = new ArrayList<>(0);

  @ContextParam("objects.repository.path")
  private static File REPOSITORY_DIR;

  @ContextParam("image.magick.path")
  private static String IMAGE_MAGICK_PATH;

  @ContextParam("release.privacy.url")
  private static String PRIVACY_URL;

  @ContextParam("release.privacy.path")
  private static File PRIVACY_PATH;

  @ContextParam("google.api.key")
  private static String GOOGLE_API_KEY;

  public static File repositoryDir() {
    return REPOSITORY_DIR;
  }

  public static String imageMagickPath() {
    return IMAGE_MAGICK_PATH;
  }

  public static String privacyURL() {
    return PRIVACY_URL;
  }

  public static File privacyPath() {
    return PRIVACY_PATH;
  }

  public static String gooleApiKey() {
    return GOOGLE_API_KEY;
  }

  /**
   * Maximum file size accepted by silence trim operation. For files larger that this limit processing does not guarantee inside silence is not removed.
   */
  public static final int MAX_TRIM_FILE_SIZE = 4000000;

  public static final String PICTURE_FILE_NAME = "picture.jpg";
  public static final String ICON_FILE_NAME = "icon.jpg";
  public static final String THUMBNAIL_FILE_NAME = "thumbnail.png";
  public static final String SAMPLE_FILE_NAME = "sample.mp3";
  public static final String WAVEFORM_FILE_NAME = "waveform.png";
}
