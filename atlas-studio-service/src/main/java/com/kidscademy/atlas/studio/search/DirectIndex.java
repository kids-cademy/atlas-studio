package com.kidscademy.atlas.studio.search;

import java.io.IOException;
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
public class DirectIndex<T extends Comparable<T>> implements Iterable<String> {
    private final T objectKey;
    private final Map<String, Integer> words = new HashMap<>();

    public DirectIndex(T objectKey) {
	this.objectKey = objectKey;
    }

    public void add(String fieldName, String text, int relevance) throws IOException {
	Analyzer analyzer = CustomAnalyzer.builder() //
		.withTokenizer("standard") //
		.addTokenFilter("lowercase") //
		.addTokenFilter("englishpossessive") //
		.addTokenFilter("stop") //
		.addTokenFilter("englishminimalstem") //
		.addTokenFilter("commongrams") //
		.addTokenFilter("decimaldigit") //
		.build();

	TokenStream tokenStream = analyzer.tokenStream(fieldName, text);
	CharTermAttribute attribute = tokenStream.addAttribute(CharTermAttribute.class);
	tokenStream.reset();
	while (tokenStream.incrementToken()) {
	    final String word = attribute.toString();
	    int currentRelevance = getRelevance(word);
	    words.put(attribute.toString(), Math.max(currentRelevance, relevance));
	}
	analyzer.close();
    }

    public T getObjectKey() {
	return objectKey;
    }

    public int getRelevance(String word) {
	Integer relevance = words.get(word);
	return relevance != null ? relevance : 0;
    }

    /**
     * Return direct index size, that is, the number of indexed words. This method
     * is designed mainly for tests.
     * 
     * @return this index size.
     */
    public int size() {
	return words.size();
    }

    @Override
    public Iterator<String> iterator() {
	return words.keySet().iterator();
    }
}
