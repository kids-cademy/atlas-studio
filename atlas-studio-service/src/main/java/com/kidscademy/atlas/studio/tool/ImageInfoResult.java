package com.kidscademy.atlas.studio.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import js.lang.BugError;

public class ImageInfoResult implements ResultParser {
    private final ImageInfo imageInfo;

    public ImageInfoResult() {
	this.imageInfo = new ImageInfo();
    }

    public ImageInfo getImageInfo() {
	return imageInfo;
    }

    // picture.jpg JPEG 920x560 920x560+0+0 8-bit sRGB 178854B 0.047u 0:00.045
    // picture.png PNG 800x140 800x140+0+0 16-bit Grayscale Gray 720B 0.000u 0:00.000
    // picture.png PNG 1200x733 1200x733+0+0 8-bit sRGB 989.227KiB 0.000u 0:00.000
    // picture.png PNG 1723x1853 1723x1853-450-293 8-bit sRGB 946990B 0.000u 0:00.000
    private static final Pattern FORMAT = Pattern.compile(
	    "^(\\S+) (JPEG|PNG) (\\d+)x(\\d+) (\\d+)x(\\d+)[+-](\\d+)[+-](\\d+) (8|16)-bit (sRGB|Grayscale Gray) (\\d+(?:\\.\\d+)?)(B|KiB|MiB) .+$");

    @Override
    public void parse(String line) {
	Matcher matcher = FORMAT.matcher(line);
	if (!matcher.find()) {
	    throw new BugError("Not recognized imagick identify response pattern |%s|.", line);
	}
	imageInfo.setFileName(matcher.group(1));
	imageInfo.setFileSize(getFileSize(matcher.group(11), matcher.group(12)));
	imageInfo.setType(MediaType.valueOf(matcher.group(2)));
	imageInfo.setWidth(Integer.parseInt(matcher.group(3)));
	imageInfo.setHeight(Integer.parseInt(matcher.group(4)));
    }

    private static int getFileSize(String value, String units) {
	double fileSize = Double.parseDouble(value);
	switch (units) {
	case "B":
	    return (int) fileSize;

	case "KiB":
	    return (int) (1024 * fileSize);

	case "MiB":
	    return (int) (1048576 * fileSize);
	}
	return 0;
    }
}
