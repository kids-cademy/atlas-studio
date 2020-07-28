package com.kidscademy.atlas.studio.util;

import java.io.File;

public class OS {
    public static boolean isWindows() {
	String os = System.getProperty("os.name");
	return os != null && os.toLowerCase().contains("win");
    }

    public static String escapePath(File file) {
	String path = file.getAbsolutePath();
	if (!isWindows()) {
	    return path;
	}

	// cannot use String#replaceAll because replacement string contains backslash
	StringBuilder builder = new StringBuilder();
	for (int i = 0; i < path.length(); ++i) {
	    char c = path.charAt(i);
	    builder.append(c);
	    if (c == '\\') {
		builder.append('\\');
	    }
	}
	return builder.toString();
    }
}
