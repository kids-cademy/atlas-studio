package com.kidscademy.atlas.studio.tool;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.kidscademy.atlas.studio.model.AndroidApp;

public class AndroidToolsImpl implements AndroidTools {
    @Override
    public void cleanProject(File appDir) throws IOException {
	gradlew(appDir, "clean");
    }

    @Override
    public void build(File appDir) throws IOException {
	gradlew(appDir, "build");
    }

    @Override
    public void buildClean(File appDir) throws IOException {
	gradlew(appDir, "clean build");
    }

    @Override
    public void initLocalGitRepository(AndroidApp app) throws IOException {
	git(app.getDir(), "init");

	URL url = app.getGitRepository();
	String origin = String.format("%s://%s:%s@%s%s", url.getProtocol(), app.getGitUserName(), app.getGitPassword(),
		url.getHost(), url.getPath());
	git(app.getDir(), "remote add origin %s", origin);

	git(app.getDir(), "add .");
	git(app.getDir(), "commit -m \"Initial import.\"");
	git(app.getDir(), "push --set-upstream origin master");
    }

    // --------------------------------------------------------------------------------------------

    private static void gradlew(File appDir, String command) throws IOException {
	GradlewProcess gradlew = new GradlewProcess(appDir);
	gradlew.exec(command);
    }

    private static void git(File appDir, String command, Object... args) throws IOException {
	GitProcess git = new GitProcess(appDir);
	git.exec(String.format(command, args));
    }
}
