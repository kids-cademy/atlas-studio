package com.kidscademy.atlas.studio.tool;

import java.io.File;

public class PandocProcess extends CommandProcess {
    private final String commandPath;

    public PandocProcess() {
	this(new File("."));
    }

    public PandocProcess(File appDir) {
	super(appDir);
	commandPath = "pandoc";
    }

    @Override
    protected String getCommandPath() {
	return commandPath;
    }
}
