package com.kidscademy.atlas.studio.tool;

import java.io.File;

import com.kidscademy.atlas.studio.util.OS;

public class GradlewProcess extends CommandProcess {
    private final String commandPath;

    public GradlewProcess(File appDir) {
	super(appDir);
	String gradlew = "gradlew";
	if (OS.isWindows()) {
	    gradlew += ".bat";
	}
	commandPath = new File(appDir, gradlew).getAbsolutePath();
    }

    @Override
    protected String getCommandPath() {
	return commandPath;
    }
}
