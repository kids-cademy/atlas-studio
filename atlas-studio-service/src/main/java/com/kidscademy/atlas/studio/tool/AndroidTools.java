package com.kidscademy.atlas.studio.tool;

import java.io.File;
import java.io.IOException;

import com.kidscademy.atlas.studio.model.AndroidApp;

public interface AndroidTools {
    void cleanProject(File appDir) throws IOException;

    void buildAPK(File appDir) throws IOException;

    void buildSignedAPK(File appDir) throws IOException;

    /**
     * Build signed application bundle.
     * 
     * @param appDir
     *            application project root directory.
     * @throws IOException
     */
    void buildBundle(File appDir) throws IOException;

    void buildClean(File appDir) throws IOException;

    void initLocalGitRepository(AndroidApp app) throws IOException;
}
