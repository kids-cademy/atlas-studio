package com.kidscademy.atlas.studio.tool;

import java.io.File;
import java.io.IOException;

public interface ImageProcessor {
    /**
     * Convert an image file type into another. Image file type is identified by
     * file extension.
     * 
     * @param imageFile
     * @param targetFile
     * @param quality
     *            optional quality, in range 0 ... 100.
     * @throws IOException
     */
    void convert(File imageFile, File targetFile, int... quality) throws IOException;

    /**
     * Resize image.
     * 
     * @param imageFile
     * @param targetFile
     * @param width
     *            width, 0 for auto,
     * @param height
     *            height, 0 for auto.
     * @throws IOException
     */
    void resize(File imageFile, File targetFile, int width, int height) throws IOException;

    void canvasSize(File imageFile, File targetFile, int width, int height, String background, String gravity)
	    throws IOException;

    ImageInfo getImageInfo(File source) throws IOException;

    /**
     * Generate horizontal axis. If target file does exist it will be silently
     * overridden.
     * 
     * @param targetFile
     *            not existing target file,
     * @param canvasWidth
     * @param canvasHeight
     * @param xaxisColor
     * @throws IOException
     */
    void generateXAxis(File targetFile, int canvasWidth, int canvasHeight, String xaxisColor) throws IOException;

    /**
     * Generate rainbow gradient. If target file does exist it will be silently
     * overridden.
     * 
     * @param targetFile
     *            not existing target file,
     * @param canvasWidth
     * @param canvasHeight
     * @throws IOException
     */
    void generateRainbowGradient(File targetFile, int canvasWidth, int canvasHeight) throws IOException;

    /**
     * Remove unused space around image. This option removes any edges that are
     * exactly the same color as the corner pixels.
     * 
     * @param imageFile
     * @param targetFile
     * @throws IOException
     */
    void trim(File imageFile, File targetFile) throws IOException;

    void flop(File imageFile, File targetFile) throws IOException;

    void flip(File imageFile, File targetFile) throws IOException;

    void rotate(File imageFile, File targetFile, float degree) throws IOException;

    void crop(File imageFile, File targetFile, int width, int height, int xoffset, int yoffset) throws IOException;

    void cropCircle(File imageFile, File targetFile, int width, int height, int xoffset, int yoffset,
	    String borderColor, int borderWidth) throws IOException;

    void compose(File imageFile, File maskFile, ImageCompose compose) throws IOException;

    void overlap(File imageFile, File... overlapImageFiles) throws IOException;

    void brightnessContrast(File imageFile, File targetFile, int brightness, int contrast) throws IOException;

    /**
     * Get MD5 digest of image content. Returned value is useful only to identify
     * content that is strictly similar, actually the same image. At least in theory
     * this value is not dependent on image codec, that is, JPEG or PNG, only on
     * content. Anyway, different image qualities can generate different hash
     * values.
     * <p>
     * This value cannot be used to asses at what degree two images are
     * (dis)similar.
     * 
     * @param imageFile
     *            image file.
     * @return image perceptual hash.
     * @throws IOException
     *             if image file reading or process fail.
     */
    String perceptualHash(File imageFile) throws IOException;

    /**
     * Compute perceptual distance between two images. Returned value is zero for
     * identical images and greater than 30 for completely different images. This
     * distance is not based on image content similarities but on perceptual hash.
     * <p>
     * Used algorithm consider those 42 double values from perceptual hash as
     * coordinates in a space with 42 dimensions and compute Euclidean distance
     * between points.
     * <p>
     * Some results using a JPG image as first file and various second file:
     * <ul>
     * <li>converted to PNG - 0.0
     * <li>resized about 8 times - 0.8
     * <li>low quality of 20 - 0.7
     * <li>completely different - 31.2
     * </ul>
     * As one can see, the same content, no mater codec, size and quality has
     * distance less than 1 whereas very different images has distance larger than
     * 30.
     * 
     * @param imageFile1
     *            first image,
     * @param imageFile2
     *            second image.
     * @return perceptual distance between images.
     * @throws IOException
     *             if images read operation fails.
     */
    double perceptualDistance(File imageFile1, File imageFile2) throws IOException;

    <T> T info(File imageFile, String attribute, Class<T> type) throws IOException;

    boolean isOpaque(File imageFile) throws IOException;

    void exec(String format, Object... args) throws IOException;
}
