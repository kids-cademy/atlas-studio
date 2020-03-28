package com.kidscademy.atlas.studio;

import js.log.Log;
import js.log.LogFactory;
import js.tiny.container.core.App;
import js.tiny.container.core.AppContext;

public class Application extends App {
    private static final Log log = LogFactory.getLog(Application.class);

    private static Application instance;

    public static Application instance() {
	return instance;
    }

    public Application(AppContext context) {
	super(context);
	log.trace("Application(AppContext)");
	instance = this;
    }

    public String getProperty(String name) {
	return getContext().getProperty(name);
    }

    public <T> T getProperty(String name, Class<T> type) {
	return getContext().getProperty(name, type);
    }
}
