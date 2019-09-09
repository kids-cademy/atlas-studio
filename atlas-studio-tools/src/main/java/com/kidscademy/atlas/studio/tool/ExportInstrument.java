package com.kidscademy.atlas.studio.tool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import js.json.Json;
import js.lang.ConfigException;
import js.transaction.TransactionFactory;
import js.transaction.eclipselink.TransactionFactoryImpl;
import js.util.Classes;
import js.util.Strings;

public class ExportInstrument {
    public static void main(String... args) throws IOException, ConfigException {
	Json json = Classes.loadService(Json.class);

	TransactionFactory factory = new TransactionFactoryImpl("import");
	AtlasDao dao = factory.newInstance(AtlasDaoImpl.class);

	ZipOutputStream zip = new ZipOutputStream(new FileOutputStream("d://tmp/instruments.zip"));

	List<AtlasObject> instruments = dao.findPublishedObjects("instrument");

	List<String> objectNames = new ArrayList<>(instruments.size());
	for (AtlasObject instrument : instruments) {
	    objectNames.add(instrument.getName());
	}

	for (int i = 0, repositoryIndex = 0; i < instruments.size(); ++i) {
	    AtlasObject instrument = instruments.get(i);

	    InstrumentDTO dto = new InstrumentDTO();
	    dto.repositoryIndex = repositoryIndex;
	    // TODO: repository index ignored
	    //instrument.setRepositoryIndex(repositoryIndex);
	    ++repositoryIndex;

	    dto.rank = instrument.getRank();
	    dto.name = instrument.getName();
	    dto.display = instrument.getDisplay();
	    dto.definition = instrument.getDefinition();
	    dto.description = instrument.getDescription();

	    addImage(dto.images, instrument, "icon");
	    addImage(dto.images, instrument, "cover");
	    addImage(dto.images, instrument, "featured");
	    addImage(dto.images, instrument, "contextual");

	    dto.sampleTitle = instrument.getSampleTitle();
	    dto.samplePath = path(instrument.getName(), instrument.getSampleName());

	    dto.classification = instrument.getClassification();
	    dto.aliases = instrument.getAliases();
	    dto.spreading = instrument.getSpreading();
	    dto.date = instrument.getStartDate();

	    dto.facts = instrument.getFacts();

	    for (Link link : instrument.getLinks()) {
		dto.links.add(new LinkDTO(link));
	    }

	    for (int j = 0, l = instrument.getRelated().size(); j < l; ++j) {
		String relatedName = instrument.getRelated().get(j);
		if (objectNames.contains(relatedName)) {
		    dto.related.add(relatedName);
		}
	    }

	    ZipEntry entry = entry(dto.name, "instrument_en.json");
	    zip.putNextEntry(entry);
	    zip.write(json.stringify(dto).getBytes("UTF-8"));
	    zip.closeEntry();

	    addEntry(zip, dto.name, instrument.getSampleName());
	    addEntry(zip, dto.name, "icon.jpg", pictureFile(instrument, "icon", 96, 96));
	    addEntry(zip, dto.name, "contextual.jpg", pictureFile(instrument, "contextual", 920, 560));
	    addEntry(zip, dto.name, "cover.png", pictureFile(instrument, "cover", 560, 0));
	    addEntry(zip, dto.name, "featured.png", pictureFile(instrument, "featured", 560, 0));
	}

	IndexTask task = new IndexTask(instruments);
	ZipEntry entry = new ZipEntry("atlas/search-index.json");
	zip.putNextEntry(entry);
	zip.write(json.stringify(task.createSearchIndex()).getBytes("UTF-8"));
	zip.closeEntry();

	Collections.sort(objectNames);
	entry = new ZipEntry("atlas/objects-list.json");
	zip.putNextEntry(entry);
	zip.write(json.stringify(objectNames).getBytes("UTF-8"));
	zip.closeEntry();

	zip.close();
    }

    private static void addEntry(ZipOutputStream zip, String objectName, String fileName) throws IOException {
	// test null file name here in order to simplify invoker logic
	if (fileName == null) {
	    return;
	}
	ZipEntry entry = entry(objectName, fileName);
	zip.putNextEntry(entry);
	copy(file(objectName, fileName), zip);
	zip.closeEntry();

    }

    private static void addEntry(ZipOutputStream zip, String objectName, String fileName, File file)
	    throws IOException {
	// test null file here in order to simplify invoker logic
	if (file == null) {
	    return;
	}
	ZipEntry entry = entry(objectName, fileName);
	zip.putNextEntry(entry);
	copy(file, zip);
	zip.closeEntry();

    }

    private static void addImage(Map<String, ImageDTO> images, AtlasObject instrument, String pictureName) {
	Image picture = picture(instrument, pictureName);
	if (picture != null) {
	    images.put(pictureName, new ImageDTO(instrument.getName(), pictureName, picture));
	}
    }

    private static Image picture(AtlasObject instrument, String pictureName) {
	for (Image picture : instrument.getImages()) {
	    if (picture.getName().equals(pictureName)) {
		return picture;
	    }
	}
	return null;
    }

    private static File pictureFile(AtlasObject instrument, String pictureName, int width, int height)
	    throws IOException {
	Image picture = picture(instrument, pictureName);
	if (picture == null) {
	    return null;
	}

//	Classes.setFieldValue(ConvertProcess.class, "BIN", "C://Program Files/ImageMagick-6.9.9-Q16-HDRI/convert.exe");
//	Classes.setFieldValue(IdentifyProcess.class, "BIN",
//		"C://Program Files/ImageMagick-6.9.9-Q16-HDRI/identify.exe");
//	ImageProcessor processor = new ImageProcessorImpl();

	File targetFile = File.createTempFile("picture", picture.getFileName());
	targetFile.deleteOnExit();
//	processor.resize(file(instrument.getName(), picture.getFileName()), targetFile, width, height);
	return targetFile;
    }

    private static String path(String objectName, String fileName) {
	return String.format("atlas/%s/%s", objectName, fileName);
    }

    private static String picturePath(AtlasObject instrument, String pictureName) {
	Image picture = picture(instrument, pictureName);
	if (picture == null) {
	    return null;
	}
	return path(instrument.getName(), picture.getFileName());
    }

    private static ZipEntry entry(String objectName, String fileName) {
	return new ZipEntry(Strings.concat("atlas/", objectName, '/', fileName));
    }

    public static File file(String objectName, String fileName) {
	return new File(Strings.concat("D:\\runtime\\kids-cademy\\webapps\\media\\atlas\\instrument\\", objectName,
		'\\', fileName));
    }

    public static long copy(File file, OutputStream outputStream) throws IOException, IllegalArgumentException {
	InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
	outputStream = new BufferedOutputStream(outputStream);

	long bytes = 0;
	try {
	    byte[] buffer = new byte[4096];
	    int length;
	    while ((length = inputStream.read(buffer)) != -1) {
		bytes += length;
		outputStream.write(buffer, 0, length);
	    }
	} finally {
	    inputStream.close();
	    outputStream.flush();
	}
	return bytes;
    }

    private static class InstrumentDTO {
	int repositoryIndex;
	int rank;
	String name;
	String display;
	String definition;
	String description;

	/** keys: icon, cover, featured, contextual */
	Map<String, ImageDTO> images = new HashMap<>();

	String sampleTitle;
	String samplePath;

	// infobox
	Map<String, String> classification = new HashMap<>();
	List<String> aliases;
	List<Region> spreading;
	HDate date;

	Map<String, String> facts;
	Map<String, String> features = Collections.emptyMap();
	List<String> related = new ArrayList<>();
	List<LinkDTO> links = new ArrayList<>();
    }

    private static class ImageDTO {
	String path;
	String caption;

	ImageDTO(String objectName, String pictureName, Image picture) {
	    path = path(objectName, picture.getFileName());
	    caption = picture.getCaption();
	}
    }

    private static class LinkDTO {
	String url;
	String display;
	String description;
	String iconPath;

	LinkDTO(Link link) {
	    url = link.getUrl().toExternalForm();
	    display = link.getDisplay();
	    description = link.getDescription();
	    iconPath = Strings.concat("links/", link.getIconName());
	}
    }
}
