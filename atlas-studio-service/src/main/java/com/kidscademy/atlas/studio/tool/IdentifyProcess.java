package com.kidscademy.atlas.studio.tool;

import js.tiny.container.annotation.ContextParam;
import js.util.Strings;

public class IdentifyProcess extends ImageMagickProcess {
    @ContextParam("image.magick.identify")
    private static String BIN;

    @Override
    protected String args(String command) {
	return Strings.concat(BIN, " ", command);
    }
}
