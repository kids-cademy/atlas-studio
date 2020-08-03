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

    // 1. picture.jpg JPEG 920x560 920x560+0+0 8-bit sRGB 178854B 0.047u 0:00.045
    // 2. picture.png PNG 800x140 800x140+0+0 16-bit Grayscale Gray 720B 0.000u 0:00.000
    // 3. picture.png PNG 1200x733 1200x733+0+0 8-bit sRGB 989.227KiB 0.000u 0:00.000
    // 4. picture.png PNG 1723x1853 1723x1853-450-293 8-bit sRGB 946990B 0.000u 0:00.000
    // # next pattern - #5, has file name with space
    // 5. picture file.jpg JPEG 920x560 920x560+0+0 8-bit sRGB 178854B 0.047u 0:00.045
    // 6. picture.png PNG 400x400 400x400+0+0 8-bit sRGB 256c 71081B 0.000u 0:00.000
    // 7. picture.jpg JPEG 1256x1256 1256x1256+0+0 8-bit Gray 256c 251950B 0.000u 0:00.000
    // 8. picture.png PNG 361x362 361x362+0+0 8-bit Gray 300B 0.000u 0:00.000
    // 9. picture.jpg JPEG 880x660 880x660+0+0 8-bit sRGB 88.8KB 0.000u 0:00.000
    //10. picture.jpg JPEG 1024x780 1024x780+0+0 8-bit Grayscale Gray 256c 205KB 0.000u 0:00.000
    
    private static final Pattern FORMAT = Pattern.compile(
	    "^(.+) (JPEG|PNG) (\\d+)x(\\d+) (\\d+)x(\\d+)[+-](\\d+)[+-](\\d+) (8|16)-bit (sRGB|sRGB 256c|Grayscale Gray|Gray 256c|Gray|Grayscale Gray 256c) (\\d+(?:\\.\\d+)?)(B|KB|MB|KiB|MiB) .+$");

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

	case "KB":
	    return (int) (1000 * fileSize);

	case "MB":
	    return (int) (1000000 * fileSize);

	case "KiB":
	    return (int) (1024 * fileSize);

	case "MiB":
	    return (int) (1048576 * fileSize);
	}
	return 0;
    }
}
