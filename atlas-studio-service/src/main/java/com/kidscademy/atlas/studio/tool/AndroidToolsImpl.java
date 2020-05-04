package com.kidscademy.atlas.studio.tool;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.kidscademy.atlas.studio.model.AndroidApp;

import js.tiny.container.annotation.ContextParam;

public class AndroidToolsImpl implements AndroidTools {
    @ContextParam("atlas.keystore")
    private static File KEYSTORE;

    @ContextParam("atlas.keypass")
    private static String KEYPASS;

    @Override
    public void cleanProject(File appDir) throws IOException {
	gradlew(appDir, "clean");
    }

    @Override
    public void buildAPK(File appDir) throws IOException {
	gradlew(appDir, "build");
    }

    @Override
    public void buildSignedAPK(File appDir) throws IOException {
	gradlew(appDir, "build");

	String unsignedapk = "app/build/outputs/apk/release/app-release-unsigned.apk";
	File alignedapk = new File(appDir, "app/build/outputs/apk/release/app-release-aligned.apk");
	alignedapk.delete();

	File signedapk = new File(appDir, "app/release/app-release.apk");
	signedapk.getParentFile().mkdirs();
	String alias = "atlas.wild-wirds.test";

	zipalign(appDir, "-v -p 4 %s %s", unsignedapk, alignedapk.getPath());

	apksigner(appDir, "sign --ks %s --ks-pass pass:%s --ks-key-alias %s --out %s %s", KEYSTORE.getAbsolutePath(),
		KEYPASS, alias, signedapk.getPath(), alignedapk.getPath());
    }

    @Override
    public void buildBundle(File appDir) throws IOException {
	// gradlew(appDir, "bundleDebug");
	gradlew(appDir, "bundleRelease");

	// String bundle = "app/build/outputs/bundle/debug/app-debug.aab";
	String bundle = "app/build/outputs/bundle/release/app-release.aab";
	String alias = "atlas.wild-wirds.test";
	jarsigner(appDir, "-verbose -sigalg SHA256withRSA -digestalg SHA-256 -keystore %s -storepass %s %s %s",
		KEYSTORE.getAbsolutePath(), KEYPASS, bundle, alias);
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

    private static void jarsigner(File appDir, String command, Object... args) throws IOException {
	JarSignerProcess jarsigner = new JarSignerProcess(appDir);
	jarsigner.exec(String.format(command, args));
    }

    private static void zipalign(File appDir, String command, Object... args) throws IOException {
	ZipAlignProcess zipalign = new ZipAlignProcess(appDir);
	zipalign.exec(String.format(command, args));
    }

    private static void apksigner(File appDir, String command, Object... args) throws IOException {
	ApkSignerProcess apksigner = new ApkSignerProcess(appDir);
	apksigner.exec(String.format(command, args));
    }
}
