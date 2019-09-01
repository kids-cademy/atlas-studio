package com.kidscademy.atlas.studio.tool;

import java.io.File;
import java.io.IOException;

public interface TextConverter {
    void convertMdToHtm(File mdFile, File htmFile) throws IOException;

    void convertMdToStandaloneHtm(File mdFile, File htmFile) throws IOException;
}
