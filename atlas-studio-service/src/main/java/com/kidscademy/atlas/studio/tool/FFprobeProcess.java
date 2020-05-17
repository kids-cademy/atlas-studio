package com.kidscademy.atlas.studio.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import js.json.Json;
import js.json.impl.JsonParserException;
import js.log.Log;
import js.log.LogFactory;
import js.tiny.container.annotation.ContextParam;
import js.util.Classes;
import js.util.Strings;

public class FFprobeProcess extends AbstractToolProcess {
    private static final Log log = LogFactory.getLog(FFprobeProcess.class);

    @ContextParam("audio.tool.ffprobe")
    private static String BIN;

    @Override
    public <T> T exec(final Type resultType, String command) throws IOException {
	List<String> args = Strings.split(Strings.concat(BIN, " -v quiet -print_format json ", command));
	final Process process = start(args);
	final Object lock = new Object();

	class StdinReader implements Runnable {
	    private final Json json;
	    volatile T result;

	    StdinReader() {
		this.json = Classes.loadService(Json.class);
	    }

	    @Override
	    public void run() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

		try {
		    StringBuilder resultBuilder = new StringBuilder();
		    String line = null;
		    while ((line = reader.readLine()) != null) {
			console.println(line);
			resultBuilder.append(line);
		    }
		    result = json.parse(resultBuilder.toString(), resultType);
		} catch (IOException | JsonParserException e) {
		    log.error(e);
		} finally {
		    close(reader);
		}

		synchronized (lock) {
		    lock.notify();
		}
	    }
	}

	StdinReader stdinReader = new StdinReader();
	Thread stdinThread = new Thread(stdinReader);

	wait(process, stdinThread, lock);
	return stdinReader.result;
    }
}
