package com.kidscademy.atlas.studio.tool;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import js.lang.BugError;
import js.log.Log;
import js.log.LogFactory;
import js.util.Strings;

public abstract class AbstractToolProcess implements ToolProcess {
    private static final Log log = LogFactory.getLog(AbstractToolProcess.class);

    private static final long FFMPEG_TIMEOUT = 16000L;

    public void exec(String command) throws IOException {
	exec(VoidResult.class, command);
    }

    protected static Process start(List<String> command) throws IOException {
	ProcessBuilder builder = new ProcessBuilder(command);
	// redirect STDERR to STDOUT so that reading process.getInputStream get them
	// both
	builder.redirectErrorStream(true);
	log.debug("Create process |%s|.", Strings.join(command));
	return builder.start();
    }

    protected static void wait(Process process, Thread stdinThread, Object lock) throws IOException {
	long timeout = FFMPEG_TIMEOUT;
	long timestamp = System.currentTimeMillis() + timeout;

	synchronized (lock) {
	    stdinThread.start();
	    while (timeout > 0) {
		try {
		    lock.wait(timeout);
		    timeout = timestamp - System.currentTimeMillis();
		} catch (InterruptedException e) {
		    continue;
		}
		break;
	    }
	}

	if (timeout <= 0) {
	    process.destroy();
	    throw new IOException("FFmpeg process timeout. See stdout logs for process printout.");
	}

	int returnCode = -1;
	try {
	    returnCode = process.waitFor();
	} catch (InterruptedException e) {
	    log.error(e);
	}
	if (returnCode != 0) {
	    throw new IOException(String
		    .format("FFmpeg process fail. Exit code |%d|. See stdout logs for process printout.", returnCode));
	}
    }

    protected static void close(Reader reader) {
	try {
	    reader.close();
	} catch (IOException e) {
	    log.error(e);
	}
    }

    public static String format(String format, Object... args) {
	// 0: NONE
	// 1: APPEND
	// 2: WAIT_OPEN_BRACE
	// 3: VARIABLE
	int state = 1;

	StringBuilder valueBuilder = new StringBuilder();
	for (int charIndex = 0, argIndex = 0; charIndex < format.length(); ++charIndex) {
	    char c = format.charAt(charIndex);
	    switch (state) {
	    case 1:
		if (c == '$') {
		    state = 2;
		    break;
		}
		valueBuilder.append(c);
		break;

	    case 2:
		if (c != '{') {
		    throw new BugError("Invalid command format. Missing open brace after variable mark ($).");
		}
		state = 3;

	    case 3:
		if (c == '}') {
		    if (argIndex == args.length) {
			throw new BugError("Not enough arguments provided for given command format.");
		    }
		    Object arg = args[argIndex];
		    // special handling for files argument; always uses absolute path
		    valueBuilder.append(arg instanceof File? ((File)arg).getAbsolutePath(): arg.toString());
		    ++argIndex;
		    state = 1;
		}
		break;
	    }
	}
	return valueBuilder.toString();
    }
}
