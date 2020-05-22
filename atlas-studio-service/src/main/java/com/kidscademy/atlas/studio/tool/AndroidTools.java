package com.kidscademy.atlas.studio.tool;

import java.io.File;
import java.io.IOException;

import com.kidscademy.atlas.studio.model.AndroidApp;

public interface AndroidTools {
    String sdkDir();
    
    void cleanProject(File appDir) throws IOException;

    void buildAPK(File appDir) throws IOException;

    void buildSignedAPK(AndroidApp app) throws IOException;

    /**
     * Build signed application bundle.
     * 
     * @param app
     *            android application.
     * @throws IOException
     */
    void buildBundle(AndroidApp app) throws IOException;

    void buildClean(File appDir) throws IOException;

    void initLocalGitRepository(AndroidApp app) throws IOException;
}
