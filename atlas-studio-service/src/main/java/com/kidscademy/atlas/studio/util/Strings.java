package com.kidscademy.atlas.studio.util;

import java.util.List;

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

    public static String substringAfter(String string, String prefix) {
	if (prefix.length() >= string.length()) {
	    return "";
	}
	return string.substring(prefix.length());
    }

    public static String html(String text) {
	return "<p>" + text.replaceAll("\\. ", ".</p><p>") + "</p>";
    }

    public static String binomialName(String name) {
	List<String> parts = split(name, '-');
	StringBuilder binomialName = new StringBuilder(toTitleCase(parts.get(0)));
	for (int i = 1; i < parts.size(); ++i) {
	    binomialName.append(' ');
	    binomialName.append(parts.get(i));
	}
	return binomialName.toString();
    }
}
