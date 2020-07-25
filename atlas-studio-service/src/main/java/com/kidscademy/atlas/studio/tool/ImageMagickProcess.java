package com.kidscademy.atlas.studio.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import js.lang.BugError;
import js.log.Log;
import js.log.LogFactory;
import js.util.Classes;
import js.util.Types;

public abstract class ImageMagickProcess extends AbstractToolProcess {
    private static final Log log = LogFactory.getLog(ImageMagickProcess.class);

    protected abstract String args(String command);

    @Override
    public <T> T exec(Type resultType, String command) throws IOException {
	if (!Types.isKindOf(resultType, ResultParser.class)) {
	    throw new BugError("ImageMagick processor requires result parser class.");
	}

	final Process process = start(split(args(command)));
	final T result = Classes.newInstance(resultType);

	final Object lock = new Object();
	// create process standard output reader and wait process printout
	Thread stdinReader = new Thread(new Runnable() {
	    @Override
	    public void run() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

		try {
		    String line = null;
		    while ((line = reader.readLine()) != null) {
			console.println(line);
			((ResultParser) result).parse(line);
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
	});

	wait(process, stdinReader, lock);
	return result;
    }

    private static List<String> split(String command) {
	List<String> args = new ArrayList<>();
	StringBuilder arg = new StringBuilder();

	SplitState state = SplitState.SPACE;
	for (int i = 0; i < command.length(); ++i) {
	    char c = command.charAt(i);

	    switch (state) {
	    case SPACE:
		if (c == ' ') {
		    break;
		}
		if (c == '"') {
		    state = SplitState.ESCAPE;
		    break;
		}
		state = SplitState.TEXT;
		// fall through next case

	    case TEXT:
		if (c == ' ') {
		    args.add(arg.toString());
		    arg.setLength(0);
		    state = SplitState.SPACE;
		} else {
		    arg.append(c);
		}
		break;

	    case ESCAPE:
		if (c == '"') {
		    args.add(arg.toString());
		    arg.setLength(0);
		    state = SplitState.SPACE;
		} else {
		    arg.append(c);
		}
		break;
	    }
	}

	args.add(arg.toString());
	return args;
    }

    private enum SplitState {
	SPACE, TEXT, ESCAPE
    }
}
