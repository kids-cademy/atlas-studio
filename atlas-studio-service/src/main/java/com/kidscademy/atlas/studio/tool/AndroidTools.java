package com.kidscademy.atlas.studio.tool;

import java.io.File;
import java.io.IOException;

import com.kidscademy.atlas.studio.model.AndroidApp;

public interface AndroidTools {
    void cleanProject(File appDir) throws IOException;

    void build(File appDir) throws IOException;

    void buildClean(File appDir) throws IOException;

    void initLocalGitRepository(AndroidApp app) throws IOException;
}
