package com.kidscademy.atlas.studio.tool;

import java.io.IOException;
import java.lang.reflect.Type;

public interface ToolProcess {
    void exec(String command) throws IOException;

    <T> T exec(Type resultType, String command) throws IOException;
}
