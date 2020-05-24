package com.kidscademy.atlas.studio.util;

public class OS {
    public static boolean isWindows() {
	String os = System.getProperty("os.name");
	return os != null && os.toLowerCase().contains("win");
    }
}
