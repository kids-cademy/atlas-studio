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

    public WikipediaArticleText(URL articleURL) {
	this(content(articleURL));
    }

    public WikipediaArticleText(EList content) {
	for (Element element : content) {
	    // collect all <p> elements till found table of content
	    if ("toc".equals(element.getAttr("id"))) {
		break;
	    }

	    if ("p".equals(element.getTag())) {
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
	StringBuilder descriptionBuilder = new StringBuilder();
	for (int i = firstIndex; i < sentences.size(); ++i) {
	    descriptionBuilder.append("<p>");
	    descriptionBuilder.append(sentences.get(i));
	    descriptionBuilder.append("</p>");
	}
	return descriptionBuilder.toString();
    }

    private static EList content(URL articleURL) {
	DocumentBuilder builder = Classes.loadService(DocumentBuilder.class);
	Document page = builder.loadHTML(articleURL);
	return page.findByXPath("//DIV[@id='mw-content-text']/DIV[contains(@class,'mw-parser-output')]/*");
    }
}
