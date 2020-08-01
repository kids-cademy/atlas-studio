package com.kidscademy.atlas.studio.util;

import java.lang.reflect.Type;

public class Types extends js.util.Types {
    public static Type getType(Object object) {
	Class<?> clazz = object.getClass();
	return clazz;
    }
}
