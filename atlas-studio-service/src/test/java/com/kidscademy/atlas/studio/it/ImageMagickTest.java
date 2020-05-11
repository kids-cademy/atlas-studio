package com.kidscademy.atlas.studio.it;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.io.FileMatchers.anExistingFile;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.kidscademy.atlas.studio.tool.ConvertProcess;
import com.kidscademy.atlas.studio.tool.IdentifyProcess;
import com.kidscademy.atlas.studio.tool.ImageCompose;
import com.kidscademy.atlas.studio.tool.ImageInfo;
import com.kidscademy.atlas.studio.tool.ImageProcessor;
import com.kidscademy.atlas.studio.tool.ImageProcessorImpl;
import com.kidscademy.atlas.studio.tool.MediaType;

import js.tiny.container.net.EventStreamManager;
import js.util.Classes;
import js.util.Files;

@RunWith(MockitoJUnitRunner.class)
public class ImageMagickTest {
    @Mock
    private EventStreamManager eventStream;

    private ImageProcessor image;

    @BeforeClass
    public static void beforeClass() {
	Classes.setFieldValue(ConvertProcess.class, "BIN", "C://Program Files/ImageMagick-6.9.9-Q16-HDRI/convert.exe");
	Classes.setFieldValue(IdentifyProcess.class, "BIN",
		"C://Program Files/ImageMagick-6.9.9-Q16-HDRI/identify.exe");
    }

    @Before
    public void beforeTest() {
	image = new ImageProcessorImpl(eventStream);
    }

    @Test
    public void patternForImageInfoResult() {
	Pattern FORMAT = Pattern.compile(
		"^(.+\\.(?i)(?:jpg|png)(?-i)) (JPEG|PNG) (\\d+)x(\\d+) (\\d+)x(\\d+)\\+(\\d+)\\+(\\d+) (8|16)-bit (sRGB|Grayscale Gray) (\\d+(?:B|MiB)) .+$");

	Matcher matcher = FORMAT.matcher("picture.jpg JPEG 920x560 920x560+0+0 8-bit sRGB 178854B 0.047u 0:00.045");
	assertThat(matcher, notNullValue());
	assertTrue(matcher.find());
	assertThat(matcher.group(1), equalTo("picture.jpg"));
	assertThat(matcher.group(2), equalTo("JPEG"));
	assertThat(matcher.group(3), equalTo("920"));
	assertThat(matcher.group(4), equalTo("560"));
	assertThat(matcher.group(5), equalTo("920"));
	assertThat(matcher.group(6), equalTo("560"));
	assertThat(matcher.group(7), equalTo("0"));
	assertThat(matcher.group(8), equalTo("0"));
	assertThat(matcher.group(9), equalTo("8"));
	assertThat(matcher.group(10), equalTo("sRGB"));
	assertThat(matcher.group(11), equalTo("178854B"));

	matcher = FORMAT.matcher("image.png PNG 800x140 800x140+0+0 16-bit Grayscale Gray 720B 0.000u 0:00.000");
	assertThat(matcher, notNullValue());
	assertTrue(matcher.find());
	assertThat(matcher.group(1), equalTo("image.png"));
	assertThat(matcher.group(2), equalTo("PNG"));
	assertThat(matcher.group(3), equalTo("800"));
	assertThat(matcher.group(4), equalTo("140"));
	assertThat(matcher.group(5), equalTo("800"));
	assertThat(matcher.group(6), equalTo("140"));
	assertThat(matcher.group(7), equalTo("0"));
	assertThat(matcher.group(8), equalTo("0"));
	assertThat(matcher.group(9), equalTo("16"));
	assertThat(matcher.group(10), equalTo("Grayscale Gray"));
	assertThat(matcher.group(11), equalTo("720B"));

	matcher = FORMAT.matcher("IMAGE.PNG PNG 800x140 800x140+0+0 16-bit Grayscale Gray 720B 0.000u 0:00.000");
	assertThat(matcher, notNullValue());
	assertTrue(matcher.find());
	assertThat(matcher.group(1), equalTo("IMAGE.PNG"));
    }

    @Test
    public void getImageInfo() throws IOException {
	File imageFile = new File("fixture/image/picture_hq.jpg");
	ImageInfo imageInfo = image.getImageInfo(imageFile);

	assertThat(imageInfo, notNullValue());
	assertThat(imageInfo.getFileName(), equalTo(imageFile.getAbsolutePath()));
	assertThat(imageInfo.getFileSize(), equalTo(178854));
	assertThat(imageInfo.getType(), equalTo(MediaType.JPEG));
	assertThat(imageInfo.getWidth(), equalTo(920));
	assertThat(imageInfo.getHeight(), equalTo(560));
    }

    @Test
    public void generateXAxis() throws IOException {
	File imageFile = new File("fixture/image/image.png");
	imageFile.delete();

	image.generateXAxis(imageFile, 800, 140, "black");
	assertThat(imageFile, anExistingFile());
	assertThat(image.perceptualHash(imageFile), equalTo("3BFE6B8FF8483A58910D7833CEBE7BCC"));

	ImageInfo imageInfo = image.getImageInfo(imageFile);
	assertThat(imageInfo, notNullValue());
	assertThat(imageInfo.getFileName(), equalTo(imageFile.getAbsolutePath()));
	assertThat(imageInfo.getFileSize(), equalTo(720));
	assertThat(imageInfo.getType(), equalTo(MediaType.PNG));
	assertThat(imageInfo.getWidth(), equalTo(800));
	assertThat(imageInfo.getHeight(), equalTo(140));

	imageFile.delete();
    }

    @Test
    public void generateRainbowGradient() throws IOException {
	File imageFile = new File("fixture/image/image.jpg");
	imageFile.delete();

	image.generateRainbowGradient(imageFile, 800, 140);
	assertThat(imageFile, anExistingFile());
	assertThat(image.perceptualHash(imageFile), equalTo("BDEF3863DB801D8837C3EBC226E29ABB"));

	ImageInfo imageInfo = image.getImageInfo(imageFile);
	assertThat(imageInfo, notNullValue());
	assertThat(imageInfo.getFileName(), equalTo(imageFile.getAbsolutePath()));
	assertThat(imageInfo.getFileSize(), equalTo(9516));
	assertThat(imageInfo.getType(), equalTo(MediaType.JPEG));
	assertThat(imageInfo.getWidth(), equalTo(800));
	assertThat(imageInfo.getHeight(), equalTo(140));

	imageFile.delete();
    }

    @Test
    public void crop() throws IOException {
	File imageFile = new File("fixture/image/picture_hq.jpg");
	File targetFile = new File("fixture/image/picture.jpg");

	targetFile.delete();
	assertThat(targetFile, not(anExistingFile()));

	image.crop(imageFile, targetFile, 96, 128, 0, 0);
	assertThat(targetFile, anExistingFile());
	assertThat(image.perceptualHash(imageFile), equalTo("951C04938E96E00052A0FFF8B0D7E70F"));

	ImageInfo imageInfo = image.getImageInfo(targetFile);
	assertThat(imageInfo, notNullValue());
	assertThat(imageInfo.getFileName(), equalTo(targetFile.getAbsolutePath()));
	assertThat(imageInfo.getFileSize(), equalTo(27700));
	assertThat(imageInfo.getType(), equalTo(MediaType.JPEG));
	assertThat(imageInfo.getWidth(), equalTo(96));
	assertThat(imageInfo.getHeight(), equalTo(128));

	targetFile.delete();
    }

    @Test
    public void compose() throws IOException {
	File maskFile = new File("fixture/image/mask.png");
	image.generateRainbowGradient(maskFile, 960, 140);

	File waveformFile = new File("fixture/image/waveform.png");
	File imageFile = new File("fixture/image/image.png");
	Files.copy(waveformFile, imageFile);

	image.compose(imageFile, maskFile, ImageCompose.SRCIN);
	assertThat(imageFile, anExistingFile());
	assertThat(image.perceptualHash(imageFile), equalTo("7418287F34FC1576EB6828E52A837EA4"));

	ImageInfo imageInfo = image.getImageInfo(imageFile);
	assertThat(imageInfo, notNullValue());
	assertThat(imageInfo.getFileName(), equalTo(imageFile.getAbsolutePath()));
	assertThat(imageInfo.getFileSize(), equalTo(8086));
	assertThat(imageInfo.getType(), equalTo(MediaType.PNG));
	assertThat(imageInfo.getWidth(), equalTo(960));
	assertThat(imageInfo.getHeight(), equalTo(140));

	maskFile.delete();
	imageFile.delete();
    }

    @Test
    public void perceptualHash() throws IOException {
	File imageFile = new File("fixture/image/picture_hq.jpg");
	File hashFile1 = new File("fixture/image/hash_1.jpg");
	File hashFile2 = new File("fixture/image/hash_2.png");

	image.convert(imageFile, hashFile1);
	image.convert(imageFile, hashFile2);

	String hash1 = image.perceptualHash(hashFile1);
	String hash2 = image.perceptualHash(hashFile2);

	assertThat(hash1, equalTo(hash2));

	hashFile1.delete();
	hashFile2.delete();
    }

    @Test
    public void perceptualDistance() throws IOException {
	File jpgImage = new File("fixture/image/picture_hq.jpg");
	File waveform = new File("fixture/image/waveform.png");

	File pngImage = new File("fixture/image/picture_hq.png");
	File resizedImage = new File("fixture/image/resized-image.jpg");
	File lowQualityImage = new File("fixture/image/low-quality-image.jpg");

	image.convert(jpgImage, pngImage);
	image.resize(jpgImage, resizedImage, 100, 0);
	image.convert(jpgImage, lowQualityImage, 20);

	// PNG is identical - distance is 0
	assertThat(image.perceptualDistance(jpgImage, pngImage), equalTo(0.0));

	// resized and low quality images are pretty close to original - distance is
	// about 0.75
	assertThat(image.perceptualDistance(jpgImage, resizedImage), is(closeTo(0.75, 0.25)));
	assertThat(image.perceptualDistance(jpgImage, lowQualityImage), is(closeTo(0.75, 0.25)));

	// waveform is far for original - distance is greater than 30.75
	assertThat(image.perceptualDistance(jpgImage, waveform), is(greaterThan(30.75)));

	pngImage.delete();
	resizedImage.delete();
	lowQualityImage.delete();
    }

    @Test
    public void info() throws IOException {
	File imageFile = new File("fixture/image/picture_hq.jpg");
	assertThat(image.info(imageFile, "basename", String.class), equalTo("picture_hq"));
	assertThat(image.info(imageFile, "bit-depth", int.class), equalTo(8));
	assertThat(image.info(imageFile, "colors", int.class), equalTo(68152));
	assertThat(image.info(imageFile, "colorspace", String.class), equalTo("sRGB"));
	assertThat(image.info(imageFile, "compression", String.class), equalTo("JPEG"));
	assertThat(image.info(imageFile, "depth", int.class), equalTo(8));
	assertThat(image.info(imageFile, "opaque", boolean.class), equalTo(true));
	assertThat(image.info(imageFile, "size", String.class), equalTo("178854B"));
    }

    @Test
    public void isOpaque() throws IOException {
	File imageFile = new File("fixture/image/picture_hq.jpg");
	assertThat(image.isOpaque(imageFile), equalTo(true));
    }
}
