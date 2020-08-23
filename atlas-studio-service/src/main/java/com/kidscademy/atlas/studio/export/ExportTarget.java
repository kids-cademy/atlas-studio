package com.kidscademy.atlas.studio.export;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.search.KeywordIndex;

public interface ExportTarget {

    void open(OutputStream stream);
    
    void commit() throws IOException;
    
    void close() throws IOException;

    void writeObject(ExportObject object) throws IOException;
    
    void writeObjectsList(Collection<String> objectsList) throws IOException;
    
    void writeSearchIndex(List<KeywordIndex<Integer>> searchIndex, String language) throws IOException;
    
    /**
     * Copy source media file content to target media path. To simplify invoker
     * logic both source media file and media file name can be null, in which case
     * this method should be NOP.
     * 
     * @param sourceMediaFile
     *            source media file, null accepted,
     * @param object
     *            atlas object owning media file,
     * @param mediaFileName
     *            file name for target media path, null accepted.
     * @throws IOException
     */
    void writeMedia(File sourceMediaFile, AtlasObject object, String mediaFileName) throws IOException;

    void commitMedia(AtlasObject object, String mediaFileName) throws IOException;
    
    boolean isMediaFile(AtlasObject object, String mediaFileName);
}
