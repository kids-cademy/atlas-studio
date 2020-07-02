package com.kidscademy.atlas.studio.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import js.json.Json;
import js.util.Classes;

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

	BreakIterator it = BreakIterator.getSentenceInstance(Locale.US);
	it.setText(text);
	int start = it.first();
	for (int end = it.next(); end != BreakIterator.DONE; start = end, end = it.next()) {
	    sentences.add(text.substring(start, end).trim());
	}
    }

    public static String firstSentence(String text) {
	BreakIterator it = BreakIterator.getSentenceInstance(Locale.US);
	it.setText(text);
	return text.substring(it.first(), it.next()).trim();
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
	if (scientificName == null) {
	    return null;
	}
	return scientificName.toLowerCase().replace(' ', '-');
    }

    public static String removeReferences(String text) {
	return text.replaceAll("\\[[^\\]]+\\]", "").replaceAll("  ", " ");
    }

    public static String removeParentheses(String text) {
	return text.replaceAll("\\([^\\)]+\\)", "").replaceAll("  ", " ");
    }

    private static Pattern BASE_DOMAIN_PATTERN = Pattern
	    .compile("^(?:http|https|ftp|file):\\/\\/(?:[^.]+\\.)*([^.]+\\.[^:/]+)\\/.*$");

    public static String basedomain(URL url) {
	if (url == null) {
	    return null;
	}
	Matcher matcher = BASE_DOMAIN_PATTERN.matcher(url.toExternalForm());
	if (!matcher.find()) {
	    return null;
	}
	return matcher.group(1);
    }

    public static <T> T load(URL url, Class<T> type) throws IOException {
	OutputStream stream = new ByteArrayOutputStream();
	Files.copy(url, stream);
	Json json = Classes.loadService(Json.class);
	return json.parse(stream.toString(), type);
    }

    /** States enumeration for plain text parser automata. */
    private static enum PlainTextState {
	TEXT, START_TAG, END_TAG
    }

    /**
     * Convenient variant for {@link #toPlainText(String, int, int)} when using
     * entire source text.
     * 
     * @param text
     *            HTML formatted text.
     * @return newly created plain text.
     */
    public static String toPlainText(String text) {
	return toPlainText(text, 0, Integer.MAX_VALUE);
    }

    /**
     * Convert HTML formatted into plain text. This method parses source text and
     * detect HTML tags. Current implementation handle only &lt;p&gt; tags replacing
     * them with line break.
     * <p>
     * <b>Implementation note</b>: experimental implementation. This implementation
     * is work in progress and is marked as deprecated to warn developer about logic
     * evolution. Final parser should handle &lt;br&gt; to line break, &lt;p&gt; to
     * double line breaks, &lt;q&gt; to simple quotation mark, &lt;ul&gt; to new
     * lines beginning with tab and dash, &lt;ol&gt; to new lines beginning with tab
     * and ordinal and all &lt;h&gt; tags to triple line breaks.
     * 
     * @param text
     *            HTML formatted source text,
     * @param offset
     *            source text offset,
     * @param capacity
     *            generated plain text maximum allowed length.
     * @return newly created plain text.
     */
    public static String toPlainText(String text, int offset, int capacity) {
	PlainTextState state = PlainTextState.TEXT;

	StringBuilder plainText = new StringBuilder();
	for (int i = offset; i < text.length() && plainText.length() <= capacity; ++i) {
	    int c = text.charAt(i);

	    switch (state) {
	    case TEXT:
		if (c == '<') {
		    state = PlainTextState.START_TAG;
		    break;
		}
		plainText.append((char) c);
		break;

	    case START_TAG:
		if (c == '/') {
		    state = PlainTextState.END_TAG;
		    break;
		}
		if (c == '>') {
		    state = PlainTextState.TEXT;
		}
		break;

	    case END_TAG:
		if (c == 'p') {
		    plainText.append("\r\n");
		}
		if (c == '>') {
		    state = PlainTextState.TEXT;
		}
		break;
	    }
	}
	return plainText.toString();
    }

    public static String toDotCase(String name) {
	return name.replace(' ', '.').toLowerCase();
    }
}
