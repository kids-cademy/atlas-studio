package com.kidscademy.atlas.studio.tool;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;

import com.kidscademy.atlas.studio.model.AndroidApp;
import com.kidscademy.atlas.studio.model.AndroidProject;
import com.kidscademy.atlas.studio.util.EventPrintStream;

import js.tiny.container.annotation.ContextParam;
import js.tiny.container.net.EventStreamManager;

public class AndroidToolsImpl implements AndroidTools {
    @ContextParam("atlas.keystore")
    private static File KEYSTORE;

    @ContextParam("atlas.keypass")
    private static String KEYPASS;

    private final PrintStream console;

    public AndroidToolsImpl(EventStreamManager eventStream) {
	this.console = new PrintStream(new EventPrintStream(eventStream));
    }

    @Override
    public void cleanProject(File appDir) throws IOException {
	gradlew(appDir, "clean");
    }

    @Override
    public void buildAPK(File appDir) throws IOException {
	gradlew(appDir, "build");
    }

    @Override
    public void buildSignedAPK(AndroidApp app) throws IOException {
	gradlew(app.getDir(), "build");

	AndroidProject prj = app.getProject();
	File unsignedAPK = prj.getApkReleaseUnsignedFile();
	File alignedAPK = prj.getApkReleaseAlignedFile();
	alignedAPK.delete();

	zipalign(app.getDir(), "-v -p 4 %s %s", unsignedAPK.getPath(), alignedAPK.getPath());

	File signedAPK = prj.getApkReleaseSignedFile();
	signedAPK.getParentFile().mkdirs();
	// by convention keystore alias is the application package
	String alias = app.getPackageName();

	apksigner(app.getDir(), "sign --ks %s --ks-pass pass:%s --ks-key-alias %s --out %s %s",
		KEYSTORE.getAbsolutePath(), KEYPASS, alias, signedAPK.getPath(), alignedAPK.getPath());
    }

    @Override
    public void buildBundle(AndroidApp app) throws IOException {
	// gradlew(app.getDir(), "bundleDebug");
	gradlew(app.getDir(), "bundleRelease");

	AndroidProject prj = app.getProject();
	File bundle = prj.getBundleReleaseFile();
	// by convention keystore alias is the application package
	String alias = app.getPackageName();
	jarsigner(app.getDir(), "-verbose -sigalg SHA256withRSA -digestalg SHA-256 -keystore %s -storepass %s %s %s",
		KEYSTORE.getAbsolutePath(), KEYPASS, bundle.getPath(), alias);
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

    private void gradlew(File appDir, String command) throws IOException {
	GradlewProcess gradlew = new GradlewProcess(appDir);
	gradlew.setConsole(console);
	gradlew.exec(command);
    }

    private void git(File appDir, String command, Object... args) throws IOException {
	GitProcess git = new GitProcess(appDir);
	git.setConsole(console);
	git.exec(String.format(command, args));
    }

    private void jarsigner(File appDir, String command, Object... args) throws IOException {
	JarSignerProcess jarsigner = new JarSignerProcess(appDir);
	jarsigner.setConsole(console);
	jarsigner.exec(String.format(command, args));
    }

    private void zipalign(File appDir, String command, Object... args) throws IOException {
	ZipAlignProcess zipalign = new ZipAlignProcess(appDir);
	zipalign.setConsole(console);
	zipalign.exec(String.format(command, args));
    }

    private void apksigner(File appDir, String command, Object... args) throws IOException {
	ApkSignerProcess apksigner = new ApkSignerProcess(appDir);
	apksigner.setConsole(console);
	apksigner.exec(String.format(command, args));
    }
}
