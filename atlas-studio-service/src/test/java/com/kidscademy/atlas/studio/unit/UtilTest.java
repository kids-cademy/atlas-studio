package com.kidscademy.atlas.studio.unit;

import static org.hamcrest.Matchers.endsWith;

import static org.junit.Assert.assertThat;

import java.io.File;

import org.junit.Test;

import com.kidscademy.atlas.studio.util.OS;

public class UtilTest {
    @Test
    public void OS_escapePath() {
	String os = System.setProperty("os.name", "win");
	assertThat(OS.escapePath(new File("D:\\tmp\\apps\\lib")), endsWith("D:\\\\tmp\\\\apps\\\\lib"));
	System.setProperty("os.name", os);
    }
}
