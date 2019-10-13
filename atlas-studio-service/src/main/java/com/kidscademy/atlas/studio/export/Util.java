package com.kidscademy.atlas.studio.export;

public class Util {
    public static String path(String objectName, String fileName) {
	return String.format("atlas/%s/%s", objectName, fileName);
    }
}
