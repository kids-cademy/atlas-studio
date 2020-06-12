package com.kidscademy.atlas.studio;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.kidscademy.atlas.studio.model.AndroidApp;
import com.kidscademy.atlas.studio.model.AtlasItem;
import com.kidscademy.atlas.studio.model.Image;
import com.kidscademy.atlas.studio.model.Release;
import com.kidscademy.atlas.studio.model.ReleaseItem;

import js.rmi.BusinessException;
import js.tiny.container.annotation.Public;
import js.tiny.container.annotation.RolesAllowed;
import js.tiny.container.annotation.Service;
import js.tiny.container.http.form.Form;

@Service
//@RolesAllowed("LIBRARIAN")
public interface ReleaseService {
    List<ReleaseItem> getReleases();

    @Public
    List<ReleaseItem> getApplications();

    Release createRelease() throws IOException;

    Release getRelease(int releaseId);

    Release getReleaseByName(String releaseName);

    Release saveRelease(Release release) throws IOException;

    void clearRelease(int releaseId);

    void removeRelease(int releaseId) throws IOException, BusinessException;

    void updateReleaseGraphics(int releaseId, String background) throws IOException;

    Image uploadReleaseImage(Form imageForm) throws IOException, BusinessException;

    List<AtlasItem> getReleaseItems(int releaseId);

    void addReleaseChild(int releaseId, int childId) throws IOException;

    void addReleaseChildren(int releaseId, List<Integer> childIds) throws IOException;

    void removeReleaseChild(int releaseId, int childId) throws IOException;

    /**
     * Get Android app used to deliver requested release, identified by its unique
     * name. Returns null if there is no android application for requested release.
     * 
     * @param releaseName
     *            release name.
     * @return release android app, possible null.
     */
    AndroidApp getAndroidAppForRelease(String releaseName);

    AndroidApp createAndroidApp(String releaseName);

    AndroidApp getAndroidApp(int appId);

    AndroidApp updateAndroidApp(AndroidApp app) throws IOException;

    void removeAndroidApp(int appId) throws IOException;

    void cleanAndroidProject(int appId) throws IOException;

    void buildAndroidApp(int appId) throws IOException;

    void buildSignedAndroidApp(int appId) throws IOException;

    void buildAndroidBundle(int appId) throws IOException;

    Map<String, String> getAndroidStoreListing(int appId);
}
