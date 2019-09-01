package com.kidscademy.atlas.studio.it;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.io.FileMatchers.anExistingFile;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.kidscademy.atlas.studio.tool.TextConverter;
import com.kidscademy.atlas.studio.tool.TextConverterImpl;

import js.util.Strings;

public class PandocTest {
    private TextConverter text;

    @Before
    public void beforeTest() {
	text = new TextConverterImpl();
    }

    @Test
    public void convertMdToHtm() throws IOException {
	File mdFile = new File("fixture/text/document.md");
	File htmFile = new File("fixture/text/target.htm");
	htmFile.delete();

	text.convertMdToHtm(mdFile, htmFile);
	assertThat(htmFile, anExistingFile());
	assertThat(Strings.load(htmFile), equalTo("<h1>Header</h1>\r\n<p>Paragraph.</p>\r\n"));

	htmFile.delete();
    }

    @Test
    public void convertMdToStandaloneHtm() throws IOException {
	File mdFile = new File("fixture/text/document.md");
	File htmFile = new File("fixture/text/target.htm");
	htmFile.delete();

	text.convertMdToStandaloneHtm(mdFile, htmFile);
	assertThat(htmFile, anExistingFile());
	assertThat(Strings.load(htmFile), startsWith("<!DOCTYPE html>"));

	htmFile.delete();
    }
}
