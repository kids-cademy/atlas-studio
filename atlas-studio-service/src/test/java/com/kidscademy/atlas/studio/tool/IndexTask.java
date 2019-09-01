package com.kidscademy.atlas.studio.tool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.Region;
import com.kidscademy.atlas.studio.model.Related;

import js.log.Log;
import js.log.LogFactory;

public class IndexTask {
    private static final Log log = LogFactory.getLog(IndexTask.class);

    private final List<AtlasObject> objects;
    private final Map<String, AtlasObject> objectsMap = new HashMap<>();
    private final WordSteams wordSteams = new WordSteams();
    private final StopWords stopWords = new StopWords();

    public IndexTask(List<AtlasObject> objects) {
	this.objects = objects;
	for (AtlasObject instrument : objects) {
	    objectsMap.put(instrument.getName(), instrument);
	}
    }

    public List<SearchIndex> createSearchIndex() throws IOException {
	log.trace("exec()");
	Map<String, Map<String, Integer>> directIndex = createDirectIndices();
	return createInvertedIndex(directIndex);
    }

    private Map<String, Map<String, Integer>> createDirectIndices() throws IOException {
	Map<String, Map<String, Integer>> directIndices = new HashMap<>();
	for (AtlasObject instrument : objects) {
	    log.debug("build index for %s", instrument.getName());
	    // direct index is per atlas object
	    // it stores all keywords and their relevance
	    Map<String, Integer> directIndex = new HashMap<>();

	    putIndex(directIndex, tokenize(instrument.getDescription()), 1);
	    putIndex(directIndex, tokenize(instrument.getDefinition()), 2);
	    putIndex(directIndex, instrument.getSampleTitle(), 4);
	    putIndexSpreading(directIndex, instrument.getSpreading(), 8);
	    putIndex(directIndex, instrument.getClassification(), 16);
	    putIndex(directIndex, relatedNames(instrument.getRelated()), 32);
	    putIndex(directIndex, instrument.getAliases(), 64);
	    putIndex(directIndex, instrument.getDisplay(), 128);

	    directIndices.put(instrument.getName(), directIndex);
	}
	return directIndices;
    }

    private List<SearchIndex> createInvertedIndex(Map<String, Map<String, Integer>> directIndices) throws IOException {
	Map<String, SearchIndex> invertedIndexMap = new HashMap<>();

	for (AtlasObject instrument : objects) {
	    Map<String, Integer> directIndex = directIndices.get(instrument.getName());
	    for (String keyword : directIndex.keySet()) {
		SearchIndex searchIndex = invertedIndexMap.get(keyword);
		if (searchIndex == null) {
		    searchIndex = new SearchIndex(keyword);
		    invertedIndexMap.put(keyword, searchIndex);
		}
		searchIndex.setKeywordRelevance(directIndex.get(keyword));
		// TODO: ignored repository index
		// searchIndex.addObject(instrument.getRepositoryIndex(),
		// directIndex.get(keyword));
		searchIndex.addObject(0, directIndex.get(keyword));
	    }
	}

	List<SearchIndex> invertedIndex = new ArrayList<>(invertedIndexMap.values());
	Collections.sort(invertedIndex, new Comparator<SearchIndex>() {
	    @Override
	    public int compare(SearchIndex left, SearchIndex right) {
		return left.getKeyword().compareTo(right.getKeyword());
	    }
	});

	for (SearchIndex searchIndex : invertedIndex) {
	    searchIndex.updateObjectIds();
	}

	log.info("Index table size: %d", invertedIndex.size());
	return invertedIndex;
    }

    // --------------------------------------------------------------------------------------------
    // UTILITY METHODS

    private void putIndex(Map<String, Integer> index, Iterable<String> words, int keyRelevance) {
	for (String word : words) {
	    word = word.toLowerCase();
	    if (!stopWords.contains(word)) {
		index.put(wordSteams.getSteam(word), keyRelevance);
	    }
	}
    }

    private void putIndex(Map<String, Integer> index, String text, int keyRelevance) {
	for (String word : text.toLowerCase().split("[\\s-+:;.]+")) {
	    if (!stopWords.contains(word)) {
		index.put(wordSteams.getSteam(word), keyRelevance);
	    }
	}
    }

    private void putIndexSpreading(Map<String, Integer> index, List<Region> regions, int keyRelevance) {
	for (Region region : regions) {
	    index.put(region.getName().toLowerCase(), keyRelevance);
	}
    }

    private void putIndex(Map<String, Integer> index, Map<String, String> classification, int keyRelevance) {
	for (Map.Entry<String, String> entry : classification.entrySet()) {
	    if (entry.getKey() != null) {
		index.put(entry.getKey().toLowerCase(), keyRelevance);
	    }
	    index.put(entry.getValue().toLowerCase(), keyRelevance);
	}
    }

    private Iterable<String> tokenize(String text) {
	if (text == null) {
	    return Collections.emptySet();
	}

	// 1: WAIT_TOKEN
	// 2: TAG
	// 3: WORD

	int state = 1; // WAIT_TOKEN
	Set<String> words = new HashSet<>();
	StringBuilder wordBuilder = new StringBuilder();

	for (int i = 0; i < text.length(); ++i) {
	    char c = text.charAt(i);

	    switch (state) {
	    case 1: // WAIT_TOKEN
		if (c == '<') {
		    state = 2; // TAG
		    break;
		}
		if (!Character.isLetterOrDigit(c)) {
		    break;
		}
		wordBuilder.setLength(0);
		state = 3; // WORD
		// fall through next case

	    case 3: // WORD
		if (!Character.isLetterOrDigit(c)) {
		    words.add(wordBuilder.toString());
		    state = 1; // WAIT_TOKEN
		    break;
		}
		wordBuilder.append(c);
		break;

	    case 2: // TAG
		if (c == '>') {
		    state = 1; // WAIT_TOKEN
		}
		break;
	    }
	}

	return words;
    }

    private Iterable<String> relatedNames(List<Related> relatedNames) throws IOException {
	List<String> names = new ArrayList<>();
	for (Related relatedName : relatedNames) {
	    AtlasObject instrument = objectsMap.get(relatedName.getName());
	    if (instrument != null) {
		// instrument can be null for not published objects
		names.add(instrument.getDisplay());
	    }
	}
	return names;
    }
}
