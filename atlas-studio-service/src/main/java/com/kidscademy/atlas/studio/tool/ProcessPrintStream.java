package com.kidscademy.atlas.studio.tool;

import java.io.IOException;
import java.io.OutputStream;

import js.log.Log;
import js.log.LogFactory;
import js.tiny.container.net.EventStreamManager;

public class ProcessPrintStream extends OutputStream {
    private static final Log log = LogFactory.getLog(ProcessPrintStream.class);

    private final EventStreamManager eventStream;
    private final StringBuilder messageBuilder;

    public ProcessPrintStream(EventStreamManager eventStream) {
	this.eventStream = eventStream;
	this.messageBuilder = new StringBuilder();
    }

    @Override
    public void write(int b) throws IOException {
	switch (b) {
	case '\r':
	    break;

	case '\n':
	    log.debug(messageBuilder.toString());
	    eventStream.push(new ConsoleMessage(messageBuilder.toString()));
	    messageBuilder.setLength(0);
	    break;

	default:
	    messageBuilder.append((char) b);
	}
    }
}
