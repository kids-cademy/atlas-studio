package com.kidscademy.atlas.studio;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kidscademy.atlas.studio.export.ExportObject;
import com.kidscademy.atlas.studio.model.AtlasCollection;
import com.kidscademy.atlas.studio.model.AtlasImages;
import com.kidscademy.atlas.studio.model.AtlasItem;
import com.kidscademy.atlas.studio.model.AtlasLinks;
import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.AtlasRelated;
import com.kidscademy.atlas.studio.model.DescriptionMeta;
import com.kidscademy.atlas.studio.model.Feature;
import com.kidscademy.atlas.studio.model.FeatureMeta;
import com.kidscademy.atlas.studio.model.Image;
import com.kidscademy.atlas.studio.model.Link;
import com.kidscademy.atlas.studio.model.LinkMeta;
import com.kidscademy.atlas.studio.model.MediaSRC;
import com.kidscademy.atlas.studio.model.Option;
import com.kidscademy.atlas.studio.model.PhysicalQuantity;
import com.kidscademy.atlas.studio.model.Taxon;
import com.kidscademy.atlas.studio.tool.AudioSampleInfo;

import js.rmi.BusinessException;
import js.tiny.container.annotation.Service;
import js.tiny.container.http.form.Form;

@Service
public interface AtlasService {
    AtlasCollection createAtlasCollection();

    AtlasCollection saveAtlasCollection(AtlasCollection collection) throws BusinessException;

    void removeAtlasCollection(int collectionId) throws BusinessException;

    AtlasCollection getCollection(int collectionId);

    List<AtlasCollection> getCollections();

    List<AtlasItem> getRecentUsedAtlasObjects();

    List<AtlasItem> getCollectionItems(Map<String, String> filter, int collectionId);

    List<AtlasImages> getCollectionImages(Map<String, String> filter, int collectionId);

    List<AtlasRelated> getCollectionRelated(Map<String, String> filter, int collectionId);

    List<AtlasLinks> getCollectionLinks(Map<String, String> filter, int collectionId);

    List<AtlasItem> getCollectionItemsByTaxon(int collectionId, Taxon taxon, List<AtlasItem> excludes);

    AtlasObject createAtlasObject(int collectionId);

    /**
     * Get AtlasObject entity. If ID is zero returns an empty instance.
     * 
     * @param objectId
     * @return AtlasObject instance, possible empty or null.
     * @throws IOException
     */
    AtlasObject getAtlasObject(int objectId) throws IOException;

    AtlasObject saveAtlasObject(AtlasObject object) throws IOException, BusinessException;

    void removeAtlasObject(int objectId);

    void moveAtlasObject(AtlasItem object, int collectionId);

    List<AtlasItem> getRelatedAtlasObjects(int collectionId, List<String> relatedNames);

    Link createLink(Link link) throws BusinessException;

    String getLinkDefinition(URL link, String objectDisplay);

    LinkMeta createLinkMeta();

    LinkMeta getLinkMeta(int linkMetaId);

    List<LinkMeta> getLinksMeta();

    LinkMeta saveLinkMeta(LinkMeta linkMeta);

    List<DescriptionMeta> getCollectionDescriptionMeta(int collectionId);
    
    void removeLinkMeta(int linkMetaId);

    String formatLines(String text, String separator);

    AtlasItem importWikipediaObject(int collectionId, URL articleURL) throws IOException, BusinessException;

    boolean checkAtlasObjectName(String name);

    FeatureMeta createFeatureMeta();

    FeatureMeta getFeatureMeta(int featureMetaId);

    List<FeatureMeta> getFeaturesMeta();

    List<FeatureMeta> getFeaturesMetaCandidates(List<FeatureMeta> current);

    FeatureMeta saveFeatureMeta(FeatureMeta featureMeta);

    void removeFeatureMeta(int featureMetaId);

    List<Option> getQuantityUnits(PhysicalQuantity quantity);

    // ----------------------------------------------------------------------------------------------
    // OBJECT IMAGE SERVICES

    Image uploadImage(Form form) throws IOException, BusinessException;

    /**
     * Upload picture, identified by its URL, from third party server.
     * 
     * @param form
     * @return
     * @throws IOException
     * @throws BusinessException
     */
    Image uploadImageBySource(Form form) throws IOException, BusinessException;

    Image replaceImage(Form form) throws IOException, BusinessException;

    Image replaceImageBySource(Form form) throws IOException, BusinessException;

    /**
     * Remove object picture from media repository and from database.
     * 
     * @param object
     * @param picture
     * @throws NullPointerException
     *             if picture instance is null.
     * @throws IOException
     */
    void removeImage(AtlasItem object, Image image) throws IOException;

    // ----------------------------------------------------------------------------------------------
    // OBJECT AUDIO SAMPLE SERVICES

    /**
     * Upload audio sample. Form should have <code>dtype</code> and
     * <code>name</code> values - see {@link AtlasItem}, that are used to create
     * uploaded file path. Also form should have <code>file</code> stream.
     * <p>
     * This method creates object audio sample file, overwriting any existing.
     * 
     * @param form
     *            web interface form.
     * @return audio sample info with status after upload.
     * @throws IOException
     *             if upload fail for whatever reason.
     */
    AudioSampleInfo uploadAudioSample(Form form) throws IOException;

    /**
     * Normalize object audio sample peak level to 0dB. If file level is already
     * normalized this service does nothing and returns current info state.
     * 
     * @param object
     *            object owning audio sample.
     * @return audio sample info with status after processing.
     * @throws IOException
     *             if processing fail.
     */
    AudioSampleInfo normalizeAudioSample(AtlasItem object) throws IOException;

    /**
     * Convert object audio sample to mono, if currently is stereo. Before mixing
     * both channels, level is reduced to half in order to avoid peak level
     * trimming.
     * <p>
     * This method does nothing if audio file has already a single channel.
     * 
     * @param object
     *            object owning audio sample.
     * @return audio sample info with status after processing.
     * @throws IOException
     *             if processing fail.
     */
    AudioSampleInfo convertAudioSampleToMono(AtlasItem object) throws IOException;

    /**
     * Trim silence from both start and end of object audio sample. Inside silence
     * is not processed.
     * <p>
     * Silence trim is performed till found at least half seconds of signal. This
     * allows for eliminating bursts of noises.
     * <p>
     * Implementation note: dues to implementation limitations files larger than
     * {@link CT#MAX_TRIM_FILE_SIZE} are not guaranteed to be processed correctly
     * and is possible to inadvertently remove inside silence.
     * 
     * @param object
     *            object owning audio sample.
     * @return audio sample info with status after processing.
     * @throws IOException
     *             if processing fail.
     */
    AudioSampleInfo trimAudioSampleSilence(AtlasItem object) throws IOException;

    /**
     * Extract 30 seconds sample from object audio file. Sample is extracted
     * starting from given timestamp, expressed in seconds with decimal.
     * 
     * @param object
     *            object owning audio sample.
     * @param start
     *            sample start timestamp, in seconds with decimals.
     * @return audio sample info with status after processing.
     * @throws IOException
     *             if processing fail.
     */
    AudioSampleInfo cutAudioSample(AtlasItem object, float start) throws IOException;

    /**
     * Add triangular fade-in of hard coded duration at the beginning of object
     * audio sample. Current hard coded duration is 2.5 seconds.
     * 
     * @param object
     *            object owning audio sample.
     * @return audio sample info with status after processing.
     * @throws IOException
     *             if processing fail.
     */
    AudioSampleInfo fadeInAudioSample(AtlasItem object) throws IOException;

    /**
     * Add triangular fade-out of hard coded duration at the end of object audio
     * sample. Current hard coded duration is 2.5 seconds.
     * 
     * @param object
     *            object owning audio sample.
     * @return audio sample info with status after processing.
     * @throws IOException
     *             if processing fail.
     */
    AudioSampleInfo fadeOutAudioSample(AtlasItem object) throws IOException;

    /**
     * Generate waveform image for object audio sample. Generated image file is
     * stored on object media directory - the same directory where audio sample file
     * resides.
     * 
     * @param object
     *            object owning audio sample.
     * @return root-relative media SRC for generated waveform image.
     * @throws IOException
     *             if processing fail.
     */
    MediaSRC generateWaveform(AtlasItem object) throws IOException;

    /**
     * Undo last processing on object audio sample.
     * 
     * @param object
     *            object owning audio sample.
     * @return audio sample info with status after undo.
     * @throws IOException
     *             if processing fail.
     */
    AudioSampleInfo undoAudioSampleProcessing(AtlasItem object) throws IOException;

    /**
     * Clear all processing on object audio sample.
     * 
     * @param object
     *            object owning audio sample.
     * @return audio sample info with status after clear.
     * @throws IOException
     *             if processing fail.
     */
    AudioSampleInfo rollbackAudioSampleProcessing(AtlasItem object) throws IOException;

    /**
     * Remove object audio sample from media repository and update database record.
     * If there are not commited audio transforms they are also removed.
     * 
     * @param object
     *            object owning audio sample.
     * @throws IOException
     *             if processing fail.
     */
    void removeAudioSample(AtlasItem object) throws IOException;

    void updateIndex() throws NoSuchMethodException, IOException;

    Set<AtlasItem> search(String criterion);

    String getWikiHowTitle(URL url);

    ExportObject getExportObject(int objectId);

    Feature updateFeatureDisplay(Feature feature);
}
