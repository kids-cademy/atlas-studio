package com.kidscademy.atlas.studio.util;

import java.io.IOException;
import java.io.OutputStream;

import js.tiny.container.net.EventStreamManager;

public class EventPrintStream extends OutputStream {
    private final EventStreamManager eventStream;
    private final StringBuilder messageBuilder;

    public EventPrintStream(EventStreamManager eventStream) {
	this.eventStream = eventStream;
	this.messageBuilder = new StringBuilder();
    }

    @Override
    public void write(int b) throws IOException {
	switch (b) {
	case '\r':
	    break;

	case '\n':
	    eventStream.push(new ConsoleMessage(messageBuilder.toString()));
	    messageBuilder.setLength(0);
	    break;

	default:
	    messageBuilder.append((char) b);
	}
    }
}
