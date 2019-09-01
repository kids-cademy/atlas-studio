package com.kidscademy.atlas.studio.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.kidscademy.atlas.studio.CT;
import com.kidscademy.atlas.studio.tool.AudioProcessor;
import com.kidscademy.atlas.studio.tool.AudioProcessorImpl;
import com.kidscademy.atlas.studio.tool.AudioSampleInfo;
import com.kidscademy.atlas.studio.tool.ProbeResult;
import com.kidscademy.atlas.studio.tool.ToolProcess;
import com.kidscademy.atlas.studio.tool.VolumeInfo;
import com.kidscademy.atlas.studio.tool.Waveform;

@RunWith(MockitoJUnitRunner.class)
public class AudioProcessorTest {
    @Mock
    private ToolProcess ffmpeg;
    @Mock
    private ToolProcess ffprobe;
    @Mock
    private Waveform waveform;

    private AudioProcessor audio;

    @Before
    public void beforeTest() throws IOException {
	audio = new AudioProcessorImpl(ffmpeg, ffprobe, waveform);
    }

    @Test
    public void getAudioFileInfo() throws IOException {
	File audioFile = new File("audio.mp3");

	ProbeResult probeResult = new ProbeResult("audio.mp3", 12345, "MP3 Codec", 123.4, 2, 44100, 128000);
	when(ffprobe.exec(eq(ProbeResult.class), anyString())).thenReturn(probeResult);

	AudioSampleInfo info = audio.getAudioFileInfo(audioFile);

	assertThat(info, notNullValue());
	assertThat(info.getFileName(), equalTo("audio.mp3"));
	assertThat(info.getFileSize(), equalTo(12345));
	assertThat(info.getCodec(), equalTo("MP3 Codec"));
	assertThat(info.getDuration(), equalTo(123400));
	assertThat(info.getChannels(), equalTo(2));
	assertThat(info.getSampleRate(), equalTo(44100));
	assertThat(info.getBitRate(), equalTo(128000));
    }

    @Test
    public void generateWaveform() throws IOException {
	File audioFile = new File("audio.mp3");
	File waveformFile = new File("waveform.png");

	audio.generateWaveform(audioFile, waveformFile);

	ArgumentCaptor<String> commandCaptor = ArgumentCaptor.forClass(String.class);
	verify(ffmpeg).exec(commandCaptor.capture());
	assertThat(commandCaptor.getValue(), equalToFormat("-i %s %s", audioFile, new File("audio.wav")));

	ArgumentCaptor<File> wavFileCaptor = ArgumentCaptor.forClass(File.class);
	ArgumentCaptor<File> waveformFileCaptor = ArgumentCaptor.forClass(File.class);
	verify(waveform).generate(wavFileCaptor.capture(), waveformFileCaptor.capture());
	assertThat(wavFileCaptor.getValue(), equalTo(new File("audio.wav")));
	assertThat(waveformFileCaptor.getValue(), equalTo(waveformFile));
    }

    @Test
    public void trimSilence_RegularFile() throws IOException {
	File audioFile = new File("audio.mp3");
	File targetFile = new File("target.mp3");

	audio.trimSilence(audioFile, targetFile);

	ArgumentCaptor<String> commandCaptor = ArgumentCaptor.forClass(String.class);
	verify(ffmpeg).exec(commandCaptor.capture());
	assertThat(commandCaptor.getValue(), equalToFormat(
		"-i %s -af silenceremove=start_periods=1:start_duration=0.5:start_threshold=-60dB:detection=peak,aformat=dblp,areverse,silenceremove=start_periods=1:start_duration=0.5:start_threshold=-60dB:detection=peak,aformat=dblp,areverse %s",
		audioFile, targetFile));
    }

    @Test
    public void trimSilence_LargeFile() throws IOException {
	File audioFile = Mockito.mock(File.class);
	File targetFile = new File("target.mp3");

	when(audioFile.length()).thenReturn(CT.MAX_TRIM_FILE_SIZE + 1L);

	audio.trimSilence(audioFile, targetFile);

	ArgumentCaptor<String> commandCaptor = ArgumentCaptor.forClass(String.class);
	verify(ffmpeg).exec(commandCaptor.capture());
	assertThat(commandCaptor.getValue(), equalToFormat(
		"-i %s -af silenceremove=start_periods=1:start_duration=0.5:start_threshold=-65dB:detection=peak,silenceremove=stop_periods=1:stop_duration=0.5:stop_threshold=-65dB:detection=peak %s",
		audioFile, targetFile));
    }

    @Test
    public void cutSegment() throws IOException {
	File audioFile = new File("audio.mp3");
	File targetFile = new File("target.mp3");

	audio.cutSegment(audioFile, targetFile, 12.3, 45.67);

	ArgumentCaptor<String> commandCaptor = ArgumentCaptor.forClass(String.class);
	verify(ffmpeg).exec(commandCaptor.capture());
	assertThat(commandCaptor.getValue(), equalToFormat("-i %s -ss 12.3 -to 45.67 %s", audioFile, targetFile));
    }

    @Test
    public void convertToMono() throws IOException {
	File audioFile = new File("audio.mp3");
	File targetFile = new File("target.mp3");

	audio.convertToMono(audioFile, targetFile);

	ArgumentCaptor<String> commandCaptor = ArgumentCaptor.forClass(String.class);
	verify(ffmpeg).exec(commandCaptor.capture());
	assertThat(commandCaptor.getValue(), equalToFormat("-i %s -af pan=mono|c0=0.5*c0+0.5*c1 %s", audioFile, targetFile));
    }

    @Test
    public void normalizeLevel() throws IOException {
	File audioFile = new File("audio.mp3");
	File targetFile = new File("target.mp3");

	VolumeInfo info = new VolumeInfo(-16.5, -3.2);
	when(ffmpeg.exec(eq(VolumeInfo.class), anyString())).thenReturn(info);

	audio.normalizeLevel(audioFile, targetFile);

	ArgumentCaptor<Type> typeCaptor = ArgumentCaptor.forClass(Type.class);
	ArgumentCaptor<String> commandCaptor = ArgumentCaptor.forClass(String.class);

	verify(ffmpeg).exec(typeCaptor.capture(), commandCaptor.capture());
	assertThat(typeCaptor.getValue(), equalTo((Type) VolumeInfo.class));
	assertThat(commandCaptor.getValue(), equalToFormat("-i %s -af volumedetect -f null /dev/null", audioFile));

	verify(ffmpeg).exec(commandCaptor.capture());
	assertThat(commandCaptor.getValue(), equalToFormat("-i %s -af volume=3.2dB %s", audioFile, targetFile));
    }

    @Test
    public void fadeIn() throws IOException {
	File audioFile = new File("audio.mp3");
	File targetFile = new File("target.mp3");

	audio.fadeIn(audioFile, targetFile, 12.345);

	ArgumentCaptor<String> commandCaptor = ArgumentCaptor.forClass(String.class);
	verify(ffmpeg).exec(commandCaptor.capture());
	assertThat(commandCaptor.getValue(),
		equalToFormat("-i %s -af afade=t=in:ss=0:d=12.345:curve=tri %s", audioFile, targetFile));
    }

    @Test
    public void fadeOut() throws IOException {
	File audioFile = new File("audio.mp3");
	File targetFile = new File("target.mp3");

	ProbeResult probeResult = new ProbeResult(23.1);
	when(ffprobe.exec(eq(ProbeResult.class), anyString())).thenReturn(probeResult);

	audio.fadeOut(audioFile, targetFile, 2.1);

	ArgumentCaptor<String> commandCaptor = ArgumentCaptor.forClass(String.class);
	verify(ffmpeg).exec(commandCaptor.capture());
	assertThat(commandCaptor.getValue(),
		equalToFormat("-i %s -af afade=t=out:st=21.0:d=2.1:curve=tri %s", audioFile, targetFile));
    }

    // --------------------------------------------------------------------------------------------

    private static Matcher<String> equalToFormat(String format, Object... args) {
	class FormatToMatcher extends TypeSafeMatcher<String> {
	    private String expected;

	    public FormatToMatcher(String format, Object... args) {
		for (int i = 0; i < args.length; ++i) {
		    if (args[i] instanceof File) {
			args[i] = ((File) args[i]).getAbsolutePath();
		    }
		}
		this.expected = String.format(format, args);
	    }

	    @Override
	    public void describeTo(Description description) {
		description.appendText(expected);
	    }

	    @Override
	    protected boolean matchesSafely(String item) {
		return expected.equals(item);
	    }
	}

	return new FormatToMatcher(format, args);
    }
}
