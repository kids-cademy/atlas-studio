package com.kidscademy.atlas.studio.unit;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.kidscademy.atlas.studio.search.DirectIndex;

public class DirectIndexTest {
    private DirectIndex index;

    @Before
    public void beforeTest() {
	index = new DirectIndex();
    }

    @Test
    public void text() throws IOException {
	String text = "This is a 1940s text. It is followed by another paragraphs follows paragraph following India Indian.";
	index.add(text, 1);
	
	for(String word:index) {
	    System.out.println(word);
	}
    }
}
// java.lang.IllegalArgumentException: A SPI class of type
// org.apache.lucene.analysis.util.TokenFilterFactory with name 'html' does not
// exist. You need to add the corresponding JAR file supporting this SPI to your
// classpath. The current classpath supports the following names: [apostrophe,
// arabicnormalization, arabicstem, bulgarianstem, bengalinormalization,
// bengalistem, brazilianstem, cjkbigram, cjkwidth, soraninormalization,
// soranistem, commongrams, commongramsquery, dictionarycompoundword,
// hyphenationcompoundword, decimaldigit, lowercase, stop, type, uppercase,
// czechstem, germanlightstem, germanminimalstem, germannormalization,
// germanstem, greeklowercase, greekstem, englishminimalstem, englishpossessive,
// kstem, porterstem, spanishlightstem, persiannormalization, finnishlightstem,
// frenchlightstem, frenchminimalstem, irishlowercase, galicianminimalstem,
// galicianstem, hindinormalization, hindistem, hungarianlightstem,
// hunspellstem, indonesianstem, indicnormalization, italianlightstem,
// latvianstem, minhash, asciifolding, capitalization, codepointcount,
// concatenategraph, daterecognizer, delimitedtermfrequency, fingerprint,
// fixbrokenoffsets, hyphenatedwords, keepword, keywordmarker, keywordrepeat,
// length, limittokencount, limittokenoffset, limittokenposition,
// removeduplicates, stemmeroverride, protectedterm, trim, truncate,
// typeassynonym, worddelimiter, worddelimitergraph, scandinavianfolding,
// scandinaviannormalization, edgengram, ngram, norwegianlightstem,
// norwegianminimalstem, patternreplace, patterncapturegroup, delimitedpayload,
// numericpayload, tokenoffsetpayload, typeaspayload, portugueselightstem,
// portugueseminimalstem, portuguesestem, reversestring, russianlightstem,
// shingle, fixedshingle, snowballporter, serbiannormalization, classic,
// standard, swedishlightstem, synonym, synonymgraph, flattengraph,
// turkishlowercase, elision]
