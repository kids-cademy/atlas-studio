package com.kidscademy.atlas.studio.tool;

import java.io.File;
import java.io.IOException;

import com.kidscademy.atlas.studio.CT;

/**
 * Process audio files.
 * 
 * @author Iulian Rotaru
 */
public interface AudioProcessor {
    AudioSampleInfo getAudioFileInfo(File audioFile) throws IOException;

    void generateWaveform(File audioFile, File waveformFile) throws IOException;

    /**
     * Trim silence from both start and end of given audio file. Inside silence is
     * not processed. Trimmed audio is saved on target file.
     * <p>
     * Silence trim is performed till found at least half seconds of signal. This
     * allows for eliminating bursts of noises.
     * <p>
     * Implementation note: dues to implementation limitations files larger than
     * {@link CT#MAX_TRIM_FILE_SIZE} are not guaranteed to be processed correctly
     * and is possible to inadvertently remove inside silence.
     * 
     * @param audioFile
     *            source audio file,
     * @param targetFile
     *            target file.
     * @throws IOException
     *             if processing fails.
     */
    void trimSilence(File audioFile, File targetFile) throws IOException;

    /**
     * Extract audio segment specified by requested interval, expressed in seconds
     * with decimals. Audio sample outside interval are removed.
     * 
     * @param audioFile
     *            source audio file,
     * @param targetFile
     *            target file,
     * @param start
     *            interval start timestamp,
     * @param end
     *            interval end timestamp.
     * @throws IOException
     *             if processing fails.
     */
    void cutSegment(File audioFile, File targetFile, double start, double end) throws IOException;

    /**
     * Convert audio file to mono, that is, a single channel. This method assume
     * input is stereo, that is, has two channels. Before mixing both channels,
     * level is reduced to half in order to avoid peak level trimming.
     * <p>
     * This method does nothing if audio file has already a single channel.
     * 
     * @param audioFile
     * @param targetFile
     * @throws IOException
     *             if processing fails.
     */
    void convertToMono(File audioFile, File targetFile) throws IOException;

    /**
     * Normalize audio level peak to 0dB. Normalized audio is saved on target file.
     * 
     * @param audioFile
     *            source audio file,
     * @param targetFile
     *            target file.
     * @throws IOException
     *             if processing fails.
     */
    void normalizeLevel(File audioFile, File targetFile) throws IOException;

    /**
     * Add triangular fade-in effect of specified duration at the beginning of given
     * audio file. Processed audio is saved into target file.
     * 
     * @param audioFile
     *            source audio file,
     * @param targetFile
     *            target file.
     * @param duration
     *            fade-in effect duration, expressed as seconds with decimals.
     * @throws IOException
     *             if processing fails.
     */
    void fadeIn(File audioFile, File targetFile, double duration) throws IOException;

    /**
     * Add triangular fade-out effect of specified duration at the end of given
     * audio file. Processed audio is saved into target file.
     * 
     * @param audioFile
     *            source audio file,
     * @param targetFile
     *            target file.
     * @param duration
     *            fade-out effect duration, expressed as seconds with decimals.
     * @throws IOException
     *             if processing fails.
     */
    void fadeOut(File audioFile, File targetFile, double duration) throws IOException;
}
