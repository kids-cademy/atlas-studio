package com.kidscademy.atlas.studio.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.kidscademy.atlas.studio.tool.ImageInfo;
import com.kidscademy.atlas.studio.tool.ImageInfoResult;
import com.kidscademy.atlas.studio.tool.ImageMagickProcess;
import com.kidscademy.atlas.studio.tool.ImageProcessor;
import com.kidscademy.atlas.studio.tool.ImageProcessorImpl;
import com.kidscademy.atlas.studio.tool.MediaType;

@RunWith(MockitoJUnitRunner.class)
public class ImageProcessorTest {
    @Mock
    private ImageMagickProcess convert;
    @Mock
    private ImageMagickProcess identify;
    @Mock
    private PrintStream console;

    private ImageProcessor processor;

    @Before
    public void beforeTest() {
	processor = new ImageProcessorImpl(convert, identify, console);
    }

    @Test
    public void getImageInfo() throws IOException {
	ImageInfoResult result = new ImageInfoResult();
	result.parse("picture.jpg JPEG 920x560 920x560+0+0 8-bit sRGB 178854B 0.047u 0:00.045");

	when(identify.exec(eq(ImageInfoResult.class), anyString())).thenReturn(result);

	File imageFile = new File("picture.jpg");
	ImageInfo imageInfo = processor.getImageInfo(imageFile);

	assertThat(imageInfo, notNullValue());
	assertThat(imageInfo.getFileName(), equalTo("picture.jpg"));
	assertThat(imageInfo.getFileSize(), equalTo(178854));
	assertThat(imageInfo.getType(), equalTo(MediaType.JPEG));
	assertThat(imageInfo.getWidth(), equalTo(920));
	assertThat(imageInfo.getHeight(), equalTo(560));
    }

    @Test
    public void getImageInfo_Gray256c() throws IOException {
	ImageInfoResult result = new ImageInfoResult();
	result.parse("picture.jpg JPEG 1256x1256 1256x1256+0+0 8-bit Gray 256c 251950B 0.000u 0:00.000");

	when(identify.exec(eq(ImageInfoResult.class), anyString())).thenReturn(result);

	File imageFile = new File("picture.jpg");
	ImageInfo imageInfo = processor.getImageInfo(imageFile);

	assertThat(imageInfo, notNullValue());
	assertThat(imageInfo.getFileName(), equalTo("picture.jpg"));
	assertThat(imageInfo.getFileSize(), equalTo(251950));
	assertThat(imageInfo.getType(), equalTo(MediaType.JPEG));
	assertThat(imageInfo.getWidth(), equalTo(1256));
	assertThat(imageInfo.getHeight(), equalTo(1256));
    }
}
