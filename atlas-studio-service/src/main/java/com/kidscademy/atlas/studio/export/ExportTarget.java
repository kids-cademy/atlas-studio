package com.kidscademy.atlas.studio.export;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public interface ExportTarget {

    void open(OutputStream stream);

    void close() throws IOException;

    /**
     * Write object as JSON to destination path.
     * 
     * @param object
     *            source object to serialize as JSON,
     * @param path
     *            relative target path.
     * @throws IOException
     */
    void write(Object object, String path) throws IOException;

    /**
     * Copy source file content to target path. Is acceptable for source file to be
     * null in which this method should be NOP.
     * 
     * @param file
     *            source file, possible null.
     * @param path
     *            relative target path.
     * @throws IOException
     */
    void write(File file, String path) throws IOException;
}
