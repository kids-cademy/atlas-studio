package com.kidscademy.atlas.studio.it;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.kidscademy.atlas.studio.tool.GitProcess;
import com.kidscademy.atlas.studio.tool.GradlewProcess;

import js.util.Classes;

public class AndroidToolsTest {
    //@Test
    public void build() throws IOException {
	GradlewProcess gradlew = new GradlewProcess(new File("D:\\tmp\\apps\\www"));
	gradlew.exec("clean build");
    }
    
    @Test
    public void gitInit() throws IOException {
	Classes.setFieldValue(GitProcess.class, "BIN", "C://Program Files/Git/cmd/git.exe");
	GitProcess git = new GitProcess(new File("D://tmp/apps/www"));
	git.exec("init");
    }
}
