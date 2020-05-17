package com.kidscademy.atlas.studio.it;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.io.FileMatchers.anExistingFile;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.kidscademy.atlas.studio.tool.AudioProcessor;
import com.kidscademy.atlas.studio.tool.AudioProcessorImpl;
import com.kidscademy.atlas.studio.tool.AudioSampleInfo;
import com.kidscademy.atlas.studio.tool.FFmpegProcess;
import com.kidscademy.atlas.studio.tool.FFprobeProcess;

import js.tiny.container.net.EventStreamManager;
import js.util.Classes;

@RunWith(MockitoJUnitRunner.class)
public class FFmpegTest {
    @Mock
    private EventStreamManager eventStream;

    private AudioProcessor audio;

    @BeforeClass
    public static void beforeClass() {
	Classes.setFieldValue(FFmpegProcess.class, "BIN", "C://ffmpeg/ffmpeg.exe");
	Classes.setFieldValue(FFprobeProcess.class, "BIN", "C://ffmpeg/ffprobe.exe");
    }

    @Before
    public void beforeTest() throws IOException {
	audio = new AudioProcessorImpl(eventStream);
    }

    @After
    public void afterTest() {
	File targetFile = new File("fixture/audio/target.mp3");
	targetFile.delete();
    }

    @Test
    public void getAudioFileInfo() throws IOException {
	File audioFile = new File("fixture/audio/sample.mp3");
	AudioSampleInfo info = audio.getAudioFileInfo(audioFile);

	assertThat(info, notNullValue());
	assertThat(info.getFileName(), equalTo("sample.mp3"));
	assertThat(info.getFileSize(), equalTo(3041906));
	assertThat(info.getCodec(), equalTo("MP3 (MPEG audio layer 3)"));
	assertThat(info.getDuration(), equalTo(190119));
	assertThat(info.getChannels(), equalTo(2));
	assertThat(info.getSampleRate(), equalTo(44100));
	assertThat(info.getBitRate(), equalTo(128000));
    }

    @Test
    public void trimSilence() throws IOException {
	File audioFile = new File("fixture/audio/silence.mp3");
	File targetFile = new File("fixture/audio/target.mp3");

	audio.trimSilence(audioFile, targetFile);
	assertThat(targetFile, anExistingFile());

	AudioSampleInfo info = audio.getAudioFileInfo(targetFile);
	assertThat((double) info.getDuration(), closeTo(28000, 100));
    }

    @Test
    public void convertToMono() throws IOException {
	File stereoFile = new File("fixture/audio/sample.mp3");
	File monoFile = new File("fixture/audio/target.mp3");

	audio.convertToMono(stereoFile, monoFile);
	assertThat(monoFile, anExistingFile());

	assertThat(audio.getAudioFileInfo(stereoFile).getChannels(), equalTo(2));
	assertThat(audio.getAudioFileInfo(monoFile).getChannels(), equalTo(1));
    }

    @Test
    public void normalizeLevel() throws IOException {
	File audioFile = new File("fixture/audio/sample.mp3");
	File targetFile = new File("fixture/audio/target.mp3");

	audio.normalizeLevel(audioFile, targetFile);
    }
}
