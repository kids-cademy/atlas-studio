package com.kidscademy.atlas.studio.tool;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Waveform generator.
 * 
 * @author Iulian Rotaru
 */
public class Waveform {
    private static final String TYPE = "png";
    private static final int WAVEFORM_WIDTH = 800;
    private static final int WAVEFORM_HEIGHT = 160;
    private static final Color BACKGROUND_COLOR = new Color(0xFF, 0xFF, 0xFF, 0x00);

    // vertical gradient, reflected on x-axis
    // private static final Color[] GRADIENT_COLORS = new Color[] { new Color(0x00,
    // 0xFF, 0xFF),
    // new Color(0x00, 0x00, 0xFF), new Color(0x00, 0x00, 0x80) };
    // private static final float[] GRADIENT_DISTRIBUTION = new float[] { 0.0F,
    // 0.8F, 1.0F };

    // rainbow gradient
    private static final Color[] GRADIENT_COLORS = new Color[] { Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN,
	    Color.BLUE, Color.MAGENTA };
    private static final float[] GRADIENT_DISTRIBUTION = new float[] { 0.15F, 0.3F, 0.45F, 0.6F, 0.75F, 0.9F };

    private static final Color X_AXIS_COLOR = new Color(0xD0, 0xD0, 0xD0);

    private final int width;
    private final int height;

    public Waveform() {
	this.width = WAVEFORM_WIDTH;
	this.height = WAVEFORM_HEIGHT;
    }

    public void generate(File wavFile, File imageFile) throws IOException {
	BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
	Graphics2D graphics = image.createGraphics();

	graphics.setBackground(BACKGROUND_COLOR);
	graphics.clearRect(0, 0, width, height);

	// vertical gradient, reflected on x-axis
	// this.gradient = new LinearGradientPaint(0, 0, 0, height / 2,
	// GRADIENT_DISTRIBUTION, GRADIENT_COLORS,
	// MultipleGradientPaint.CycleMethod.REFLECT);

	// rainbow gradient
	Paint gradient = new LinearGradientPaint(0, 0, width, 0, GRADIENT_DISTRIBUTION, GRADIENT_COLORS);
	graphics.setPaint(gradient);

	WaveStream stream = new WaveStream(wavFile);
	int decimationBlockSize = stream.getSamplesCount() / width;

	Double previousSample = null;
	int decimationIndex = 0;
	double maxSampleValue = 0;

	int x = 0;
	try {
	    Double sample = null;
	    while ((sample = stream.readSample()) != null) {
		if (decimationIndex < decimationBlockSize) {
		    decimationIndex++;
		    if (sample > maxSampleValue) {
			maxSampleValue = sample;
		    }
		    continue;
		}

		if (previousSample != null) {
		    sample = 1 - maxSampleValue;
		    drawLine(graphics, x, sample * height, x, height - sample * height);
		    x++;
		}

		previousSample = sample;
		decimationIndex = 0;
		maxSampleValue = 0;
	    }
	} finally {
	    stream.close();
	}

	graphics.setColor(X_AXIS_COLOR);
	drawLine(graphics, 0, height / 2, width, height / 2);

	graphics.dispose();
	ImageIO.write(image, TYPE, imageFile);
    }

    private void drawLine(Graphics2D graphics, double fromX, double fromY, double toX, double toY) {
	Point2D.Double from = new Point2D.Double(fromX, fromY);
	Point2D.Double to = new Point2D.Double(toX, toY);
	Line2D line = new Line2D.Double(from, to);
	graphics.draw(line);
    }
}
