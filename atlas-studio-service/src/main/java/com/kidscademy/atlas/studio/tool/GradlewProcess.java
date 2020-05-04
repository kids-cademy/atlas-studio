package com.kidscademy.atlas.studio.tool;

import java.io.File;

public class GradlewProcess extends CommandProcess {
    private final String commandPath;

    public GradlewProcess(File appDir) {
	super(appDir);
	commandPath = appDir.getAbsolutePath() + "\\gradlew.bat";
    }

    @Override
    protected String getCommandPath() {
	return commandPath;
    }
}
