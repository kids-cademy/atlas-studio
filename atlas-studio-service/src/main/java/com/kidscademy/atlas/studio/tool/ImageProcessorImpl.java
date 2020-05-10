package com.kidscademy.atlas.studio.tool;

import static com.kidscademy.atlas.studio.tool.AbstractToolProcess.buildCommand;

import java.io.File;
import java.io.IOException;
import java.util.List;

import js.tiny.container.annotation.TestConstructor;

public class ImageProcessorImpl implements ImageProcessor {
    private final ImageMagickProcess convert;
    private final ImageMagickProcess identify;

    public ImageProcessorImpl() {
	this.convert = new ConvertProcess();
	this.identify = new IdentifyProcess();
    }

    @TestConstructor
    public ImageProcessorImpl(ImageMagickProcess convert, ImageMagickProcess identify) {
	this.convert = convert;
	this.identify = identify;
    }

    @Override
    public void convert(File imageFile, File targetFile, int... quality) throws IOException {
	if (quality.length == 1) {
	    exec("${imageFile} -quality ${quality} ${targetFile}", imageFile, quality[0], targetFile);
	} else {
	    exec("${imageFile} ${targetFile}", imageFile, targetFile);
	}
    }

    @Override
    public void resize(File imageFile, File targetFile, int width, int height) throws IOException {
	String w = width != 0 ? Integer.toString(width) : "";
	String h = height != 0 ? Integer.toString(height) : "";
	exec("${imageFile} -resize ${width}x${height} ${targetFile}", imageFile, w, h, targetFile);
    }

    @Override
    public ImageInfo getImageInfo(File imageFile) throws IOException {
	ImageInfoResult result = identify.exec(ImageInfoResult.class, buildCommand("${imageFile}", imageFile));
	return result.getImageInfo();
    }

    @Override
    public void generateXAxis(File imageFile, int canvasWidth, int canvasHeight, String xaxisColor) throws IOException {
	float y = (canvasHeight - 1) / 2.0F;
	exec("-size ${canvasWidth}x${canvasHeight} xc:transparent -fill white -stroke ${xaxisColor} -draw \"line 0,${y},${canvasWidth},${y}\" ${imageFile}",
		canvasWidth, canvasHeight, xaxisColor, y, canvasWidth, y, imageFile);
    }

    @Override
    public void generateRainbowGradient(File imageFile, int canvasWidth, int canvasHeight) throws IOException {
	// reverse order of width and height because of -rotate 90
	exec("-size ${canvasHeight}x${canvasWidth} xc:red -colorspace HSB gradient: -compose CopyRed -composite -colorspace RGB -rotate 90.0 ${imageFile}",
		canvasHeight, canvasWidth, imageFile);
    }

    @Override
    public void trim(File imageFile, File targetFile) throws IOException {
	exec("${imageFile} -fuzz 1% -trim +repage ${targetFile}", imageFile, targetFile);
    }

    @Override
    public void flop(File imageFile, File targetFile) throws IOException {
	exec("${imageFile} -flop ${targetFile}", imageFile, targetFile);
    }

    @Override
    public void flip(File imageFile, File targetFile) throws IOException {
	exec("${imageFile} -flip ${targetFile}", imageFile, targetFile);
    }

    @Override
    public void rotate(File imageFile, File targetFile, float degree) throws IOException {
	exec("-rotate ${degree} -background transparent ${imageFile} ${targetFile}", degree, imageFile, targetFile);
    }

    @Override
    public void crop(File imageFile, File targetFile, int width, int height, int xoffset, int yoffset)
	    throws IOException {
	exec("${imageFile} -crop ${width}x${height}+${xoffset}+${yoffset} ${targetFile}", imageFile, width, height,
		xoffset, yoffset, targetFile);
    }

    @Override
    public void cropCircle(File imageFile, File targetFile, int width, int height, int xoffset, int yoffset,
	    String borderColor, int borderWidth) throws IOException {
	exec("${imageFile} -crop ${width}x${height}+${xoffset}+${yoffset} ${targetFile}", imageFile, width, height,
		xoffset, yoffset, targetFile);

	int y = 1;
	if (borderColor != null) {
	    int borderPadding = (int) (Math.max(width, height) * 0.008);
	    y = borderWidth + borderPadding;
	    width += 2 * y;
	    height += 2 * y;
	}
	int cx = width / 2;
	int cy = height / 2;
	exec("-size ${width}x${height} xc:none -fill ${targetFile} -draw \"circle ${cx},${cy} ${cx},${y}\" ${targetFile}",
		width, height, targetFile, cx, cy, cx, y, targetFile);

	if (borderColor != null) {
	    y = borderWidth / 2;
	    File borderFile = new File(targetFile.getParentFile(), "border.png");
	    exec("-size ${width}x${height} xc:none -stroke ${borderColor} -fill #FF000000 -strokewidth ${borderWidth} -draw \"circle ${cx},${cy} ${cx},${y}\" ${borderFile}",
		    width, height, borderColor, borderWidth, cx, cy, cx, y, borderFile);

	    exec("-composite ${borderFile} ${targetFile} ${targetFile}", borderFile, targetFile, targetFile);
	}
    }

    @Override
    public void compose(File imageFile, File maskFile, ImageCompose compose) throws IOException {
	exec("-composite -compose ${compose} ${imageFile} ${maskFile} ${imageFile}", compose.value(), imageFile,
		maskFile, imageFile);
    }

    @Override
    public void overlap(File imageFile, File... overlapImageFiles) throws IOException {
	for (File overlapImageFile : overlapImageFiles) {
	    exec("-composite ${imageFile} ${overlapImageFile} ${imageFile}", imageFile, overlapImageFile, imageFile);
	}
    }

    @Override
    public void brightnessContrast(File imageFile, File targetFile, int brightness, int contrast) throws IOException {
	exec("${imageFile} -brightness-contrast ${brightness}x${contrast} ${targetFile}", imageFile, brightness,
		contrast, targetFile);
    }

    @Override
    public String perceptualHash(File imageFile) throws IOException {
	PerceptualHashResult result = identify.exec(PerceptualHashResult.class,
		buildCommand("-verbose -define identify:moments ${imageFile}", imageFile));
	return result.getHash();
    }

    @Override
    public double perceptualDistance(File imageFile1, File imageFile2) throws IOException {
	String command1 = buildCommand("-verbose -define identify:moments ${imageFile}", imageFile1);
	PerceptualHashResult result1 = identify.exec(PerceptualHashResult.class, command1);
	List<Double> values1 = result1.getValues();

	String command2 = buildCommand("-verbose -define identify:moments ${imageFile}", imageFile2);
	PerceptualHashResult result2 = identify.exec(PerceptualHashResult.class, command2);
	List<Double> values2 = result2.getValues();

	double sum = 0.0;
	for (int i = 0; i < values1.size(); ++i) {
	    sum += Math.pow(values1.get(i) - values2.get(i), 2);
	}
	return Math.sqrt(sum);
    }

    @Override
    public <T> T info(File imageFile, String attribute, Class<T> type) throws IOException {
	// https://imagemagick.org/script/escape.php
	String command = buildCommand("${imageFile} -format %[${attribute}] info:", imageFile, attribute);
	ValueResult<T> result = convert.exec(ValueResult.class, command);
	return result.getValue(type);
    }

    @Override
    public boolean isOpaque(File imageFile) throws IOException {
	return info(imageFile, "opaque", boolean.class);
    }

    @Override
    public void exec(String format, Object... args) throws IOException {
	convert.exec(buildCommand(format, args));
    }
}
