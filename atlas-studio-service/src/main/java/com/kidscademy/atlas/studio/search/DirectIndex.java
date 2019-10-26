package com.kidscademy.atlas.studio.search;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/**
 * Direct index stores words and their relevance. Every atlas object has its own
 * direct index.
 * 
 * @author Iulian Rotaru
 */
public class DirectIndex implements Iterable<String> {
    private final Map<String, Integer> words = new HashMap<>();

    public void add(String text, int relevance) throws IOException {
	if (text == null || text.isEmpty()) {
	    return;
	}
	add(new StringReader(text), relevance);
    }

    public void add(Reader text, int relevance) throws IOException {
	Analyzer analyzer = CustomAnalyzer.builder() //
		.withTokenizer("standard") //
		.addTokenFilter("lowercase") //
		.addTokenFilter("englishpossessive") //
		.addTokenFilter("stop") //
		.addTokenFilter("englishminimalstem") //
		.addTokenFilter("commongrams") //
		.addTokenFilter("decimaldigit") //
		.build();

	TokenStream tokenStream = analyzer.tokenStream("text", text);
	CharTermAttribute attr = tokenStream.addAttribute(CharTermAttribute.class);
	tokenStream.reset();
	while (tokenStream.incrementToken()) {
	    final String word = attr.toString();
	    int currentRelevance = getRelevance(word);
	    words.put(attr.toString(), Math.max(currentRelevance, relevance));
	}
	analyzer.close();

    }

    public int getRelevance(String word) {
	Integer relevance = words.get(word);
	return relevance != null ? relevance : 0;
    }

    @Override
    public Iterator<String> iterator() {
	return words.keySet().iterator();
    }
}
