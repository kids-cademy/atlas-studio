package com.kidscademy.atlas.studio.export;

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
import com.kidscademy.atlas.studio.model.ConservationStatus;
import com.kidscademy.atlas.studio.model.Region;
import com.kidscademy.atlas.studio.model.Taxon;

public class SearchIndexProcessor {
    private final List<ExportItem> items;
    private final Map<String, ExportItem> itemsMap = new HashMap<>();
    private final Map<String, Map<String, Integer>> directIndices = new HashMap<>();

    private final WordSteams wordSteams = new WordSteams();
    private final StopWords stopWords = new StopWords();

    public SearchIndexProcessor(List<ExportItem> items) {
	this.items = items;
	for (ExportItem item : items) {
	    itemsMap.put(item.getName(), item);
	}
    }

    public void createDirectIndex(AtlasObject object) throws IOException {
	// direct index is per atlas object
	// it stores all keywords and their relevance
	Map<String, Integer> directIndex = new HashMap<>();

	putIndex(directIndex, object.getDisplay(), 256);
	putIndex(directIndex, object.getAliases(), 128);
	putIndex(directIndex, relatedObjectsDisplay(object.getRelated()), 64);
	putTaxonomyIndex(directIndex, object.getTaxonomy(), 32);
	putConservationIndex(directIndex, object.getConservation(), 16);
	putSpreadingIndex(directIndex, object.getSpreading(), 8);
	putIndex(directIndex, object.getSampleTitle(), 4);
	putIndex(directIndex, tokenize(object.getDefinition()), 2);
	putIndex(directIndex, tokenize(object.getDescription()), 1);

	directIndices.put(object.getName(), directIndex);
    }

    public List<SearchIndex> createSearchIndex() throws IOException {
	Map<String, SearchIndex> invertedIndexMap = new HashMap<>();

	for (ExportItem item : items) {
	    Map<String, Integer> directIndex = directIndices.get(item.getName());
	    for (String keyword : directIndex.keySet()) {
		SearchIndex searchIndex = invertedIndexMap.get(keyword);
		if (searchIndex == null) {
		    searchIndex = new SearchIndex(keyword);
		    invertedIndexMap.put(keyword, searchIndex);
		}
		searchIndex.setKeywordRelevance(directIndex.get(keyword));
		searchIndex.addObject(item.getIndex(), directIndex.get(keyword));
	    }
	}

	List<SearchIndex> invertedIndex = new ArrayList<>(invertedIndexMap.values());
	Collections.sort(invertedIndex, new Comparator<SearchIndex>() {
	    @Override
	    public int compare(SearchIndex left, SearchIndex right) {
		return left.compareTo(right);
	    }
	});

	for (SearchIndex searchIndex : invertedIndex) {
	    searchIndex.updateObjectIds();
	}

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
	if (text == null) {
	    return;
	}
	for (String word : text.toLowerCase().split("[\\s-+:;.]+")) {
	    if (!stopWords.contains(word)) {
		index.put(wordSteams.getSteam(word), keyRelevance);
	    }
	}
    }

    private void putSpreadingIndex(Map<String, Integer> index, List<Region> regions, int keyRelevance) {
	for (Region region : regions) {
	    index.put(region.getName().toLowerCase(), keyRelevance);
	}
    }

    private void putTaxonomyIndex(Map<String, Integer> index, List<Taxon> taxons, int keyRelevance) {
	for (Taxon token : taxons) {
	    index.put(token.getName().toLowerCase(), keyRelevance);
	    index.put(token.getValue().toLowerCase(), keyRelevance);
	}
    }

    private void putConservationIndex(Map<String, Integer> index, ConservationStatus conservation, int keyRelevance) {
	if (conservation != null) {
	    putIndex(index, conservation.display(), keyRelevance);
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

    private Iterable<String> relatedObjectsDisplay(List<String> relatedNames) throws IOException {
	List<String> names = new ArrayList<>();
	for (String relatedName : relatedNames) {
	    ExportItem object = itemsMap.get(relatedName);
	    if (object != null) {
		// object can be null for not published objects
		names.add(object.getDisplay());
	    }
	}
	return names;
    }
}
