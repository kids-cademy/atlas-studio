package com.kidscademy.atlas.studio.unit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.kidscademy.atlas.studio.impl.MediaFileHandler;
import com.kidscademy.atlas.studio.model.AtlasCollectionKey;
import com.kidscademy.atlas.studio.model.AtlasItem;
import com.kidscademy.atlas.studio.model.MediaSRC;
import com.kidscademy.atlas.studio.util.Files;

import js.lang.BugError;
import js.util.Classes;
import js.util.Strings;

public class MediFileHandlerTest {
    @BeforeClass
    public static void beforeClass() {
	Classes.setFieldValue(Files.class, "REPOSITORY_DIR", "fixture/tomcat/webapps/");
    }

    @Test
    public void upload() throws IOException {
	file("media.ext").delete();
	MediaFileHandler handler = handler("media.ext");

	File uploadFile = new File("fixture/upload.jpg");
	Files.copy(new File("fixture/icon.jpg"), uploadFile);

	handler.upload(new File("fixture/upload.jpg"));
	Assert.assertFalse(uploadFile.exists());
	Assert.assertTrue(handler.source().exists());

	int version = Classes.getFieldValue(handler, "version");
	assertThat(version, equalTo(0));
    }

    @Test
    public void source() throws IOException {
	Files.copy(new File("fixture/icon.jpg"), file("media.ext"));
	MediaFileHandler handler = handler("media.ext");

	File file = handler.source();
	assertThat(file, notNullValue());
	assertThat(file, equalTo(file("media.ext")));
    }

    @Test(expected = BugError.class)
    public void source_exception() {
	handler("fake-media.ext").source();
    }

    @Test
    public void sourcePath() throws IOException {
	Files.copy(new File("fixture/icon.jpg"), file("media.ext"));
	MediaFileHandler handler = handler("media.ext");

	MediaSRC path = handler.sourceSrc();
	assertThat(path, notNullValue());
	assertThat(path, equalTo(new MediaSRC("/media/atlas/instrument/object/media.ext")));
    }

    @Test(expected = BugError.class)
    public void sourcePath_exception() {
	handler("fake-media.ext").sourceSrc();
    }

    @Test
    public void target() throws IOException {
	Files.copy(new File("fixture/icon.jpg"), file("media.ext"));
	MediaFileHandler handler = handler("media.ext");

	File file = handler.target();
	assertThat(file, notNullValue());
	assertThat(file, equalTo(file("media_1.ext")));
    }

    @Test(expected = BugError.class)
    public void target_exception() {
	handler("fake-media.ext").target();
    }

    @Test
    public void targetPath() throws IOException {
	Files.copy(new File("fixture/icon.jpg"), file("media.ext"));
	MediaFileHandler handler = handler("media.ext");

	MediaSRC path = handler.targetSrc();
	assertThat(path, notNullValue());
	assertThat(path, equalTo(new MediaSRC("/media/atlas/instrument/object/media_1.ext")));
    }

    @Test(expected = BugError.class)
    public void targetPath_exception() {
	handler("fake-media.ext").targetSrc();
    }

    @Test
    public void commit() throws IOException {
	File mediaFile = file("media.ext");
	File mediaFile_1 = file("media_1.ext");
	File mediaFile_2 = file("media_2.ext");
	File mediaFile_3 = file("media_3.ext");

	Strings.save("media file", mediaFile);
	Strings.save("media file 1", mediaFile_1);
	Strings.save("media file 2", mediaFile_2);
	Strings.save("media file 3", mediaFile_3);

	MediaFileHandler handler = handler("media.ext");
	assertThat(Strings.load(mediaFile), equalTo("media file"));
	handler.commit();

	Assert.assertTrue(mediaFile.exists());
	Assert.assertFalse(mediaFile_1.exists());
	Assert.assertFalse(mediaFile_2.exists());
	Assert.assertFalse(mediaFile_3.exists());

	File source = Classes.getFieldValue(handler, "source");
	String target = Classes.getFieldValue(handler, "target");
	int version = Classes.getFieldValue(handler, "version");

	assertThat(source, nullValue());
	assertThat(target, nullValue());
	assertThat(version, equalTo(0));

	assertThat(Strings.load(mediaFile), equalTo("media file 3"));
	assertThat(Strings.load(handler.source()), equalTo("media file 3"));

	mediaFile.delete();
	mediaFile_1.delete();
	mediaFile_2.delete();
	mediaFile_3.delete();
    }

    @Test
    public void rollback() throws IOException {
	File mediaFile = file("media.ext");
	File mediaFile_1 = file("media_1.ext");
	File mediaFile_2 = file("media_2.ext");
	File mediaFile_3 = file("media_3.ext");

	Strings.save("media file", mediaFile);
	Strings.save("media file 1", mediaFile_1);
	Strings.save("media file 2", mediaFile_2);
	Strings.save("media file 3", mediaFile_3);

	MediaFileHandler handler = handler("media.ext");
	assertThat(Strings.load(handler.source()), equalTo("media file 3"));
	handler.undo();

	File source = Classes.getFieldValue(handler, "source");
	String target = Classes.getFieldValue(handler, "target");
	int version = Classes.getFieldValue(handler, "version");

	assertThat(source, nullValue());
	assertThat(target, nullValue());
	assertThat(version, equalTo(2));

	assertThat(Strings.load(handler.source()), equalTo("media file 2"));

	Assert.assertTrue(mediaFile.exists());
	Assert.assertTrue(mediaFile_1.exists());
	Assert.assertTrue(mediaFile_2.exists());
	Assert.assertFalse(mediaFile_3.exists());

	mediaFile.delete();
	mediaFile_1.delete();
	mediaFile_2.delete();
	mediaFile_3.delete();
    }

    @Test
    public void delete() throws IOException {
	File mediaFile = file("media.ext");
	File mediaFile_1 = file("media_1.ext");
	File mediaFile_2 = file("media_2.ext");
	File mediaFile_3 = file("media_3.ext");

	Strings.save("media file", mediaFile);
	Strings.save("media file 1", mediaFile_1);
	Strings.save("media file 2", mediaFile_2);
	Strings.save("media file 3", mediaFile_3);

	MediaFileHandler handler = handler("media.ext");
	assertThat(Strings.load(handler.source()), equalTo("media file 3"));
	handler.delete();

	File source = Classes.getFieldValue(handler, "source");
	String target = Classes.getFieldValue(handler, "target");
	int version = Classes.getFieldValue(handler, "version");

	assertThat(source, nullValue());
	assertThat(target, nullValue());
	assertThat(version, equalTo(-1));

	Assert.assertFalse(mediaFile.exists());
	Assert.assertFalse(mediaFile_1.exists());
	Assert.assertFalse(mediaFile_2.exists());
	Assert.assertFalse(mediaFile_3.exists());
    }

    /** Attempting to delete not existing media files should not throw exception. */
    @Test
    public void delete_nop() throws IOException {
	handler("fake-media.ext").delete();
    }

    // ----------------------------------------------------------------------------------------------

    private static MediaFileHandler handler(String fileName) {
	AtlasCollectionKey collection = new AtlasCollectionKey(1, "instrument");
	return new MediaFileHandler(new AtlasItem(collection, 1, "object"), fileName);
    }

    private static File file(String fileName) {
	return new File("fixture/tomcat/webapps/media/atlas/instrument/object/" + fileName);
    }
}
