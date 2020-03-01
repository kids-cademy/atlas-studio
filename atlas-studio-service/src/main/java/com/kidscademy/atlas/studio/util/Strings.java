package com.kidscademy.atlas.studio.util;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Strings extends js.util.Strings {
    /**
     * Return an excerpt from given paragraph. Select start substring till first
     * punctuation character, exclusive.
     * 
     * @param paragraph
     * @return
     */
    public static String excerpt(String paragraph) {
	int firstPuctuationIndex = Strings.indexOneOf(paragraph, ',', ';', '.');
	if (firstPuctuationIndex == -1) {
	    firstPuctuationIndex = paragraph.length();
	}
	return paragraph.substring(0, Math.min(firstPuctuationIndex, 64));
    }

    public static List<String> breakSentences(String text) {
	List<String> sentences = new ArrayList<>();
	collectSentences(text, sentences);
	return sentences;
    }

    public static void collectSentences(String text, List<String> sentences) {
	// break iterator deals with shorthands but only simple cases
	// for now is acceptable to have bad sentences breaking

	BreakIterator border = BreakIterator.getSentenceInstance(Locale.US);
	border.setText(text);
	int start = border.first();
	for (int end = border.next(); end != BreakIterator.DONE; start = end, end = border.next()) {
	    sentences.add(text.substring(start, end).trim());
	}
    }

    public static String substringAfter(String string, String prefix) {
	if (prefix.length() >= string.length()) {
	    return "";
	}
	return string.substring(prefix.length());
    }

    public static String dashedToScientificName(String dashedName) {
	List<String> parts = split(dashedName, '-');
	StringBuilder scientificName = new StringBuilder(toTitleCase(parts.get(0)));
	for (int i = 1; i < parts.size(); ++i) {
	    scientificName.append(' ');
	    scientificName.append(parts.get(i));
	}
	return scientificName.toString();
    }

    public static String scientificToDashedName(String scientificName) {
	return scientificName.toLowerCase().replace(' ', '-');
    }

    public static String removeReferences(String text) {
	return text.replaceAll("\\[[^\\]]+\\]", "");
    }

    public static String removeParentheses(String text) {
	return text.replaceAll("\\([^\\)]+\\)", "");
    }
}
