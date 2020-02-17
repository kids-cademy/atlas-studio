package com.kidscademy.atlas.studio.www;

import java.net.URL;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
	if (sentences.size() == 20) {
	    return false;
	}
	System.out.println(element.getText());

	String text = element.getText().trim();
	text = Strings.removeReferences(text);

	// break iterator deals with shorthands but only simple cases
	// for now is acceptable to have bad sentences breaking

	BreakIterator border = BreakIterator.getSentenceInstance(Locale.US);
	border.setText(text);
	int start = border.first();
	for (int end = border.next(); end != BreakIterator.DONE; start = end, end = border.next()) {
	    sentences.add(text.substring(start, end).trim());
	}

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
