package com.kidscademy.atlas.studio.tool;

import java.util.HashMap;
import java.util.Map;

public enum MediaType {
    PNG, JPEG;

    private static Map<MediaType, String> EXTENSIONS = new HashMap<>();
    static {
	EXTENSIONS.put(PNG, "png");
	EXTENSIONS.put(JPEG, "jpg");
    }

    public String extension() {
	return EXTENSIONS.get(this);
    }
}
