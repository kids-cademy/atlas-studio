package com.kidscademy.atlas.studio.www;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.kidscademy.atlas.studio.util.Strings;

import js.dom.Document;
import js.dom.DocumentBuilder;
import js.dom.EList;
import js.dom.Element;
import js.util.Classes;

public class WikipediaArticleText {
    private final List<String> sentences = new ArrayList<String>();

    private enum State {
	INTRO, TOC, DESCRIPTION
    }

    private static boolean id(Element element, String id) {
	return id.equalsIgnoreCase(element.getAttr("id"))
		|| (element.hasChildren() && id.equalsIgnoreCase(element.getFirstChild().getAttr("id")));
    }

    private static boolean tag(Element element, String tag) {
	return tag.equalsIgnoreCase(element.getTag());
    }

    private boolean add(Element element) {
	Strings.collectSentences(Strings.removeReferences(element.getText().trim()), sentences);
	return true;
    }

    public WikipediaArticleText(URL articleURL) {
	this(content(articleURL));
    }

    public WikipediaArticleText(EList content) {
	State state = State.INTRO;

	COLLECT: for (Element element : content) {
	    switch (state) {
	    case INTRO:
		if (id(element, "toc")) {
		    state = State.TOC;
		    break;
		}
		if (tag(element, "p")) {
		    if (!add(element)) {
			break COLLECT;
		    }
		}
		break;

	    case TOC:
		if (id(element, "description")) {
		    state = State.DESCRIPTION;
		}
		continue;

	    case DESCRIPTION:
		if (!tag(element, "p")) {
		    break COLLECT;
		}
		if (!add(element)) {
		    break COLLECT;
		}
		break;

	    }
	}
    }

    public String getDefinition() {
	if (sentences.size() == 0) {
	    return null;
	}
	return Strings.removeParentheses(sentences.get(0));
    }

    public String getDescription() {
	return text(1);
    }

    public String getText() {
	return text(0);
    }

    public String text(int firstIndex) {
	return Strings.join(sentences, "\r\n\r\n");
    }

    private static EList content(URL articleURL) {
	DocumentBuilder builder = Classes.loadService(DocumentBuilder.class);
	Document page = builder.loadHTML(articleURL);
	return page.findByXPath("//DIV[@id='mw-content-text']/DIV[contains(@class,'mw-parser-output')]/*");
    }
}
