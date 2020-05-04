package com.kidscademy.atlas.studio.tool;

import java.io.File;

import js.tiny.container.annotation.ContextParam;

public class ZipAlignProcess extends CommandProcess {
    @ContextParam("android.zipalign")
    private static String COMMAND_PATH;

    public ZipAlignProcess(File appDir) {
	super(appDir);
    }

    @Override
    protected String getCommandPath() {
	return COMMAND_PATH;
    }
}
