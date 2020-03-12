package com.kidscademy.atlas.studio.unit;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import com.kidscademy.atlas.studio.util.Strings;

public class StringsTest {
    @Test
    public void basedomain() throws MalformedURLException {
	assertThat(Strings.basedomain(new URL("https://en.wikipedia.org/wiki/Mountain")), equalTo("wikipedia.org"));
	assertThat(Strings.basedomain(null), nullValue());
    }
}
