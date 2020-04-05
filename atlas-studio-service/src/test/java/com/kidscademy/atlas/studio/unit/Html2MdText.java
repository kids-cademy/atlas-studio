package com.kidscademy.atlas.studio.unit;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.kidscademy.atlas.studio.util.Html2Md;

public class Html2MdText {
    @Test
    public void paragraph() {
	Html2Md html2md = new Html2Md("<readme><p>paragraph</p></readme>");
	assertThat(html2md.converter(), equalTo("paragraph\r\n\r\n"));
    }

    @Test
    public void h1() {
	Html2Md html2md = new Html2Md("<readme><h1>header</h1></readme>");
	assertThat(html2md.converter(), equalTo("# header\r\n\r\n"));
    }

    @Test
    public void h2() {
	Html2Md html2md = new Html2Md("<readme><h2>header</h2></readme>");
	assertThat(html2md.converter(), equalTo("## header\r\n\r\n"));
    }

    @Test
    public void table() {
	Html2Md html2md = new Html2Md(
		"<readme><table><tr><th>column #1</th><th>column #2</th></tr><tr><td>data #1</td><td>data #2</td></tr></table></readme>");
	assertThat(html2md.converter(),
		equalTo("| column #1 | column #2 \r\n|-----------|-----------\r\n| data #1   | data #2   \r\n\r\n"));
    }

    @Test
    public void text() {
	Html2Md html2md = new Html2Md("<readme><h1>header</h1><p>paragraph</p></readme>");
	assertThat(html2md.converter(), equalTo("# header\r\n\r\nparagraph\r\n\r\n"));
    }
}
