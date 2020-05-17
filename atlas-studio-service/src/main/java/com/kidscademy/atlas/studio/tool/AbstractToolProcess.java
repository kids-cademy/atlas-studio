package com.kidscademy.atlas.studio.tool;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.List;

import js.lang.BugError;
import js.log.Log;
import js.log.LogFactory;
import js.util.Strings;

public abstract class AbstractToolProcess implements ToolProcess {
    private static final Log log = LogFactory.getLog(AbstractToolProcess.class);

    private static final long EXECUTION_TIMEOUT = 16000L;

    protected PrintStream console;

    protected AbstractToolProcess() {
	this.console = System.out;
    }

    @Override
    public void setConsole(PrintStream console) {
        this.console = console;
    }
    
    public void exec(String command) throws IOException {
	exec(VoidResult.class, command);
    }

    protected Process start(List<String> command) throws IOException {
	ProcessBuilder builder = new ProcessBuilder(command);
	// redirect STDERR to STDOUT so that reading process.getInputStream get them
	// both
	builder.redirectErrorStream(true);
	log.debug("Create process |%s|.", Strings.join(command));
	console.println("-------------------------------------------");
	console.printf("Create process |%s|.\n", Strings.join(command));
	return builder.start();
    }

    protected Process start(File directory, List<String> command) throws IOException {
	ProcessBuilder builder = new ProcessBuilder(command);
	builder.directory(directory);
	// redirect STDERR to STDOUT so that reading process.getInputStream get them
	// both
	builder.redirectErrorStream(true);
	log.debug("Create process |%s|.", Strings.join(command));
	console.println("-------------------------------------------");
	console.printf("Create process |%s|.\n", Strings.join(command));
	return builder.start();
    }

    protected static void wait(Process process, Thread stdinThread, Object lock) throws IOException {
	wait(process, EXECUTION_TIMEOUT, stdinThread, lock);
    }

    protected static void wait(Process process, long timeout, Thread stdinThread, Object lock) throws IOException {
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
	    throw new IOException("Process execution timeout. See stdout logs for process printout.");
	}

	int returnCode = -1;
	try {
	    returnCode = process.waitFor();
	} catch (InterruptedException e) {
	    log.error(e);
	}
	if (returnCode != 0) {
	    throw new IOException(String.format(
		    "Process execution fail. Exit code |%d|. See stdout logs for process printout.", returnCode));
	}
    }

    protected static void close(Reader reader) {
	try {
	    reader.close();
	} catch (IOException e) {
	    log.error(e);
	}
    }

    /**
     * Inject arguments into command template. Arguments are processed by position
     * so order should correct. It is caller responsibility to provide arguments in
     * proper order.
     * <p>
     * File arguments are handled specially: always uses absolute path and double
     * quote.
     * 
     * @param commandTemplate
     *            command template,
     * @param arguments
     *            command template arguments.
     * @return command string.
     */
    public static String buildCommand(String commandTemplate, Object... arguments) {
	// 0: NONE
	// 1: APPEND
	// 2: WAIT_OPEN_BRACE
	// 3: VARIABLE
	int state = 1;

	StringBuilder valueBuilder = new StringBuilder();
	for (int charIndex = 0, argIndex = 0; charIndex < commandTemplate.length(); ++charIndex) {
	    char c = commandTemplate.charAt(charIndex);
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
		    if (argIndex == arguments.length) {
			throw new BugError("Not enough arguments provided for given command format.");
		    }

		    Object argument = arguments[argIndex];
		    String value;
		    if (argument instanceof File) {
			value = Strings.concat('"', ((File) argument).getAbsolutePath(), '"');
		    } else {
			value = argument.toString();
		    }

		    valueBuilder.append(value);
		    ++argIndex;
		    state = 1;
		}
		break;
	    }
	}
	return valueBuilder.toString();
    }
}
