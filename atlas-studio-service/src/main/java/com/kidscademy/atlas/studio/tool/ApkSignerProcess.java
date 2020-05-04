package com.kidscademy.atlas.studio.tool;

import java.io.File;

import js.tiny.container.annotation.ContextParam;

public class ApkSignerProcess extends CommandProcess {
    @ContextParam("android.apksigner")
    private static String COMMAND_PATH;

    public ApkSignerProcess(File appDir) {
	super(appDir);
    }

    @Override
    protected String getCommandPath() {
	return COMMAND_PATH;
    }
}
