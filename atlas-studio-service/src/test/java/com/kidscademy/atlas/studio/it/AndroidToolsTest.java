package com.kidscademy.atlas.studio.it;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.kidscademy.atlas.studio.tool.GitProcess;
import com.kidscademy.atlas.studio.tool.GradlewProcess;
import com.kidscademy.atlas.studio.util.Files;

import js.util.Classes;

public class AndroidToolsTest {
    @Test
    public void build() throws IOException {
	GradlewProcess gradlew = new GradlewProcess(new File("fixture/tools/wild-birds"));
	gradlew.exec("clean build");

	// TODO: assertions

	gradlew.exec("clean");
    }

    @Test
    public void gitInit() throws IOException {
	Classes.setFieldValue(GitProcess.class, "BIN", "C://Program Files/Git/cmd/git.exe");
	GitProcess git = new GitProcess(new File("fixture/tools/wild-birds"));
	git.exec("init");

	// TODO: assertions

	Files.removeFilesHierarchy(new File("fixture/tools/wild-birds/.git")).delete();
    }
}
