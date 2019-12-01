package com.kidscademy.atlas.studio.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.kidscademy.atlas.studio.tool.ImageInfo;
import com.kidscademy.atlas.studio.tool.ImageInfoResult;
import com.kidscademy.atlas.studio.tool.MediaType;

import js.util.Classes;

public class ImageInfoResultTest {
    @Test
    public void formatPattern1() {
	String line = "picture.jpg JPEG 920x560 920x560+0+0 8-bit sRGB 178854B 0.047u 0:00.045";
	Pattern format = Classes.getFieldValue(ImageInfoResult.class, "FORMAT");
	Matcher matcher = format.matcher(line);
	assertTrue(matcher.find());
	assertThat(matcher.groupCount(), equalTo(12));
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
	assertThat(matcher.group(11), equalTo("178854"));
	assertThat(matcher.group(12), equalTo("B"));
    }
    
    @Test
    public void formatPattern2() {
	String line = "image.png PNG 800x140 800x140+0+0 16-bit Grayscale Gray 720B 0.000u 0:00.000";
	Pattern format = Classes.getFieldValue(ImageInfoResult.class, "FORMAT");
	Matcher matcher = format.matcher(line);
	assertTrue(matcher.find());
	assertThat(matcher.groupCount(), equalTo(12));
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
	assertThat(matcher.group(11), equalTo("720"));
	assertThat(matcher.group(12), equalTo("B"));
    }

    @Test
    public void formatPattern3() {
	String line = "picture.png PNG 1200x733 1200x733+0+0 8-bit sRGB 989.227KiB 0.000u 0:00.000";
	Pattern format = Classes.getFieldValue(ImageInfoResult.class, "FORMAT");
	Matcher matcher = format.matcher(line);
	assertTrue(matcher.find());
	assertThat(matcher.groupCount(), equalTo(12));
	assertThat(matcher.group(1), equalTo("picture.png"));
	assertThat(matcher.group(2), equalTo("PNG"));
	assertThat(matcher.group(3), equalTo("1200"));
	assertThat(matcher.group(4), equalTo("733"));
	assertThat(matcher.group(5), equalTo("1200"));
	assertThat(matcher.group(6), equalTo("733"));
	assertThat(matcher.group(7), equalTo("0"));
	assertThat(matcher.group(8), equalTo("0"));
	assertThat(matcher.group(9), equalTo("8"));
	assertThat(matcher.group(10), equalTo("sRGB"));
	assertThat(matcher.group(11), equalTo("989.227"));
	assertThat(matcher.group(12), equalTo("KiB"));
    }

    @Test
    public void formatPattern4() {
	String line = "picture.png PNG 1723x1853 1723x1853-450-293 8-bit sRGB 946990B 0.000u 0:00.000";
	Pattern format = Classes.getFieldValue(ImageInfoResult.class, "FORMAT");
	Matcher matcher = format.matcher(line);
	assertTrue(matcher.find());
	assertThat(matcher.groupCount(), equalTo(12));
	assertThat(matcher.group(1), equalTo("picture.png"));
	assertThat(matcher.group(2), equalTo("PNG"));
	assertThat(matcher.group(3), equalTo("1723"));
	assertThat(matcher.group(4), equalTo("1853"));
	assertThat(matcher.group(5), equalTo("1723"));
	assertThat(matcher.group(6), equalTo("1853"));
	assertThat(matcher.group(7), equalTo("450"));
	assertThat(matcher.group(8), equalTo("293"));
	assertThat(matcher.group(9), equalTo("8"));
	assertThat(matcher.group(10), equalTo("sRGB"));
	assertThat(matcher.group(11), equalTo("946990"));
	assertThat(matcher.group(12), equalTo("B"));
    }
    
    @Test
    public void formatPattern5() {
	String line = "picture file.jpg JPEG 920x560 920x560+0+0 8-bit sRGB 178854B 0.047u 0:00.045";
	Pattern format = Classes.getFieldValue(ImageInfoResult.class, "FORMAT");
	Matcher matcher = format.matcher(line);
	assertTrue(matcher.find());
	assertThat(matcher.groupCount(), equalTo(12));
	assertThat(matcher.group(1), equalTo("picture file.jpg"));
	assertThat(matcher.group(2), equalTo("JPEG"));
	assertThat(matcher.group(3), equalTo("920"));
	assertThat(matcher.group(4), equalTo("560"));
	assertThat(matcher.group(5), equalTo("920"));
	assertThat(matcher.group(6), equalTo("560"));
	assertThat(matcher.group(7), equalTo("0"));
	assertThat(matcher.group(8), equalTo("0"));
	assertThat(matcher.group(9), equalTo("8"));
	assertThat(matcher.group(10), equalTo("sRGB"));
	assertThat(matcher.group(11), equalTo("178854"));
	assertThat(matcher.group(12), equalTo("B"));
    }
    

    @Test
    public void getImageInfo1() {
	String line = "picture.jpg JPEG 920x560 920x560+0+0 8-bit sRGB 178854B 0.047u 0:00.045";
	ImageInfoResult result = new ImageInfoResult();
	result.parse(line);

	ImageInfo info = result.getImageInfo();
	assertThat(info, notNullValue());
	assertThat(info.getFileName(), equalTo("picture.jpg"));
	assertThat(info.getFileSize(), equalTo(178854));
	assertThat(info.getType(), equalTo(MediaType.JPEG));
	assertThat(info.getWidth(), equalTo(920));
	assertThat(info.getHeight(), equalTo(560));
    }

    @Test
    public void getImageInfo2() {
	String line = "image.png PNG 800x140 800x140+0+0 16-bit Grayscale Gray 720B 0.000u 0:00.000";
	ImageInfoResult result = new ImageInfoResult();
	result.parse(line);

	ImageInfo info = result.getImageInfo();
	assertThat(info, notNullValue());
	assertThat(info.getFileName(), equalTo("image.png"));
	assertThat(info.getFileSize(), equalTo(720));
	assertThat(info.getType(), equalTo(MediaType.PNG));
	assertThat(info.getWidth(), equalTo(800));
	assertThat(info.getHeight(), equalTo(140));
    }

    @Test
    public void getImageInfo3() {
	String line = "picture.png PNG 1200x733 1200x733+0+0 8-bit sRGB 989.227KiB 0.000u 0:00.000";
	ImageInfoResult result = new ImageInfoResult();
	result.parse(line);

	ImageInfo info = result.getImageInfo();
	assertThat(info, notNullValue());
	assertThat(info.getFileName(), equalTo("picture.png"));
	assertThat(info.getFileSize(), equalTo(1012968));
	assertThat(info.getType(), equalTo(MediaType.PNG));
	assertThat(info.getWidth(), equalTo(1200));
	assertThat(info.getHeight(), equalTo(733));
    }
}
