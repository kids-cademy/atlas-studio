package com.kidscademy.atlas.studio.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import js.log.Log;
import js.log.LogFactory;
import js.util.Strings;

public abstract class CommandProcess extends AbstractToolProcess {
    private static final Log log = LogFactory.getLog(CommandProcess.class);

    private static final long DEFAULT_TIMEOUT = 60000L;

    protected abstract String getCommandPath();

    private final File appDir;

    public CommandProcess(File appDir) {
	this.appDir = appDir;
    }

    @Override
    public <T> T exec(final Type resultType, String command) throws IOException {
	List<String> args = Strings.split(Strings.concat(getCommandPath(), ' ', command));
	final Process process = start(appDir, args);

	final Object lock = new Object();

	class StdinReader implements Runnable {
	    @Override
	    public void run() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

		try {
		    String line = null;
		    while ((line = reader.readLine()) != null) {
			System.out.println(line);
		    }
		} catch (IOException e) {
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

	wait(process, getTimeout(), stdinThread, lock);

	return null;
    }

    protected long getTimeout() {
	return DEFAULT_TIMEOUT;
    }
}
