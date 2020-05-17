package com.kidscademy.atlas.studio.tool;

import static com.kidscademy.atlas.studio.tool.AbstractToolProcess.buildCommand;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Type;

import com.kidscademy.atlas.studio.CT;
import com.kidscademy.atlas.studio.util.EventPrintStream;

import js.log.Log;
import js.log.LogFactory;
import js.tiny.container.annotation.TestConstructor;
import js.tiny.container.net.EventStreamManager;
import js.util.Files;

public class AudioProcessorImpl implements AudioProcessor {
    private static final Log log = LogFactory.getLog(AudioProcessorImpl.class);

    /** Duration of non-silence to stop audio silence processing. */
    private static final float SILENCE_DURATION = 0.5F;

    private final ToolProcess ffmpeg;
    private final ToolProcess ffprobe;
    private final Waveform waveform;

    public AudioProcessorImpl(EventStreamManager eventStream) throws IOException {
	this(new FFmpegProcess(), new FFprobeProcess(), new Waveform(),
		new PrintStream(new EventPrintStream(eventStream)));
    }

    @TestConstructor
    public AudioProcessorImpl(ToolProcess ffmpeg, ToolProcess ffprobe, Waveform waveform, PrintStream console) {
	this.ffmpeg = ffmpeg;
	this.ffprobe = ffprobe;
	this.waveform = waveform;
	this.ffmpeg.setConsole(console);
	this.ffprobe.setConsole(console);
    }

    @Override
    public AudioSampleInfo getAudioFileInfo(File audioFile) throws IOException {
	ProbeResult result = probe("-show_format -show_streams ${audioFile}", audioFile);

	AudioSampleInfo info = new AudioSampleInfo();
	info.setFileName(new File(result.format.filename).getName());
	info.setFileSize(result.format.size);

	ProbeResult.Stream stream = result.streams[0];
	info.setCodec(stream.codec_long_name);
	info.setDuration((int) Math.round(stream.duration * 1000));
	info.setChannels(stream.channels);
	info.setSampleRate(stream.sample_rate);
	info.setBitRate(stream.bit_rate);
	return info;
    }

    @Override
    public void generateWaveform(File audioFile, File waveformFile) throws IOException {
	// uses ffmpeg to convert file from mp3 to .wav since waveform generator knows
	// only wave format

	long timestamp = System.currentTimeMillis();
	File wavFile = new File(audioFile.getParentFile(), Files.basename(audioFile) + ".wav");
	try {
	    exec("-i ${audioFile} ${wavFile}", audioFile, wavFile);
	    waveform.generate(wavFile, waveformFile);
	} finally {
	    if (wavFile.exists() && !wavFile.delete()) {
		throw new IOException(String.format("Fail to delete file |%s|.", wavFile));
	    }
	}
	log.debug("Waveform generation time: %d msec.", System.currentTimeMillis() - timestamp);
    }

    @Override
    public void trimSilence(File audioFile, File targetFile) throws IOException {

	// first attempt was to use stop parameters - as stated in API, for end silence
	// remove but I was not able to avoid removing inside silence

	// the only solution found on net for end silence is to reverse audio sample and
	// perform start silence remove, that is working properly

	// https://superuser.com/questions/1362176/how-to-trim-silence-only-from-beginning-and-end-of-mp3-files-using-ffmpeg

	// anyway there is warning on reverse filter about memory consumption for which
	// reason limit audio file size to hard coded magic value
	// for large audio samples fall back to 'standard' silence remove and accept
	// risk for inside silence remove

	if (audioFile.length() > CT.MAX_TRIM_FILE_SIZE) {
	    String start = buildCommand(
		    "silenceremove=start_periods=1:start_duration=${start_duration}:start_threshold=-65dB:detection=peak",
		    SILENCE_DURATION);

	    String stop = buildCommand(
		    "silenceremove=stop_periods=1:stop_duration=${stop_duration}:stop_threshold=-65dB:detection=peak",
		    SILENCE_DURATION);

	    exec("-i ${audioFile} -af ${start},${stop} ${targetFile}", audioFile, start, stop, targetFile);
	    return;
	}

	String remove = buildCommand(
		"silenceremove=start_periods=1:start_duration=${start_duration}:start_threshold=-60dB:detection=peak",
		SILENCE_DURATION);

	String reverse = "aformat=dblp,areverse";

	exec("-i ${audioFile} -af ${remove},${reverse},${remove},${reverse} ${targetFile}", audioFile, remove, reverse,
		remove, reverse, targetFile);
    }

    @Override
    public void cutSegment(File audioFile, File targetFile, double start, double end) throws IOException {
	exec("-i ${audioFile} -ss ${start} -to ${end} ${targetFile}", audioFile, start, end, targetFile);
    }

    @Override
    public void convertToMono(File audioFile, File targetFile) throws IOException {
	// do not use ffmpeg down mix because it adjust levels, accordingly
	// recommendations, with -3dB
	// exec("-i ${audioFile} -ac 1 ${targetFile}", audioFile, targetFile);

	// uses instead pan with half level to ensure peaks are not trimmed
	exec("-i ${audioFile} -af pan=mono|c0=0.5*c0+0.5*c1 ${targetFile}", audioFile, targetFile);

	// when save using 'mono' audio layout, audio codec reduce bit rate to half,
	// e.g. 64Kbit/s if original was 128Kbit/s
	// one can force audio bit rate with -b:a but do not see any reason to do it
	// exec("-i ${audioFile} -af pan=mono|c0=0.5*c0+0.5*c1 -b:a 128k ${targetFile}",
	// audioFile, targetFile);
    }

    @Override
    public void normalizeLevel(File audioFile, File targetFile) throws IOException {
	VolumeInfo volume = exec(VolumeInfo.class, "-i ${audioFile} -af volumedetect -f null /dev/null", audioFile);

	// do not see how peak can exceed 0dB but just to be sure...
	if (volume.getPeak() > 0 || volume.getPeak() < -0.4) {
	    exec("-i ${audioFile} -af volume=${adjustment}dB ${targetFile}", audioFile, -volume.getPeak(), targetFile);
	}
    }

    @Override
    public void fadeIn(File audioFile, File targetFile, double duration) throws IOException {
	exec("-i ${audioFile} -af afade=t=in:ss=0:d=${duration}:curve=tri ${targetFile}", audioFile, duration,
		targetFile);
    }

    @Override
    public void fadeOut(File audioFile, File targetFile, double duration) throws IOException {
	ProbeResult result = probe("-show_streams ${audioFile}", audioFile);
	double start = result.streams[0].duration - duration;
	exec("-i ${audioFile} -af afade=t=out:st=${start}:d=${duration}:curve=tri ${targetFile}", audioFile, start,
		duration, targetFile);
    }

    // --------------------------------------------------------------------------------------------

    private void exec(String format, Object... args) throws IOException {
	ffmpeg.exec(buildCommand(format, args));
    }

    private <T> T exec(Type type, String format, Object... args) throws IOException {
	return ffmpeg.exec(type, buildCommand(format, args));
    }

    private ProbeResult probe(String format, Object... args) throws IOException {
	return ffprobe.exec(ProbeResult.class, buildCommand(format, args));
    }
}
