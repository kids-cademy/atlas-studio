package com.kidscademy.atlas.studio.tool;

import java.io.File;

import js.tiny.container.annotation.ContextParam;

public class JarSignerProcess extends CommandProcess {
    @ContextParam("java.jarsigner")
    private static String COMMAND_PATH;

    public JarSignerProcess(File appDir) {
	super(appDir);
    }

    @Override
    protected String getCommandPath() {
	return COMMAND_PATH;
    }
}
