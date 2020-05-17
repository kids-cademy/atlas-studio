package com.kidscademy.atlas.studio.tool;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Type;

public interface ToolProcess {
    void setConsole(PrintStream console);

    void exec(String command) throws IOException;

    <T> T exec(Type resultType, String command) throws IOException;
}
