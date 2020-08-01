package com.kidscademy.atlas.studio.export;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;

import js.lang.BugError;

public interface ExportTarget {

    void open(OutputStream stream);

    void close() throws IOException;

    /**
     * Write object as JSON to destination path. Implementation must ensure
     * serialized object is readable by JSON parser; for that it must re-parse
     * serialization result and throw bug error.
     * 
     * @param object
     *            source object to serialize as JSON,
     * @param path
     *            relative target path,
     * @param type
     *            target type for parser test.
     * @throws IOException
     *             if write / read operation fails.
     * @throws BugError
     *             if parser check fails.
     */
    void write(Object object, String path, Type type) throws IOException, BugError;

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
