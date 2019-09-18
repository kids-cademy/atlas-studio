package com.kidscademy.atlas.studio.model;

/**
 * Media repository object. In current media repository layout, atlas object
 * media files are stored on separated directories. By convention directory name
 * is the object name. An atlas collection is stored on a separated repository;
 * a repository is simple directory that stores object directories.
 * <p>
 * Both repository and object have names. Media file of an atlas object has also
 * a name. As a consequence, media file path, relative to media storage
 * directory, has the form ${repository-name}/${object-name}/${media-file-name}.
 * 
 * @author Iulian Rotaru
 */
public interface RepositoryObject {
    /**
     * Get repository name. In current layout, repository stores media files for a
     * collection of atlas objects. By convention repository name is the collection
     * name.
     * 
     * @return repository name.
     */
    String getRepositoryName();

    /**
     * Get object name. Media files for an atlas object are stored on separated
     * directory. By convention directory name is the object name.
     * 
     * @return object name.
     */
    String getName();
}
