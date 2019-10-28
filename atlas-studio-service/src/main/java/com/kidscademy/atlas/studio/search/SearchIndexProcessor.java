package com.kidscademy.atlas.studio.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kidscademy.atlas.studio.export.ExportObject;
import com.kidscademy.atlas.studio.model.ConservationStatus;
import com.kidscademy.atlas.studio.model.Region;
import com.kidscademy.atlas.studio.model.Taxon;

public class SearchIndexProcessor {
    private final List<DirectIndex<Integer>> directIndices = new ArrayList<>();

    public void createDirectIndex(ExportObject object) throws IOException {
	// direct index is per atlas object
	// it stores all keywords and their relevance
	DirectIndex<Integer> directIndex = new DirectIndex<>(object.getIndex());

	directIndex.add(object.getDisplay(), 512);
	directIndex.add(text(object.getAliases()), 256);
	directIndex.add(related(object.getRelated()), 128);
	directIndex.add(taxons(object.getTaxonomy()), 64);
	directIndex.add(text(object.getConservation()), 32);
	directIndex.add(regions(object.getSpreading()), 16);
	directIndex.add(object.getSampleTitle(), 8);
	directIndex.add(object.getDefinition(), 4);
	directIndex.add(facts(object.getFacts()), 2);
	directIndex.add(object.getDescription(), 1);

	directIndices.add(directIndex);
    }

    public List<SearchIndex<Integer>> updateSearchIndex() throws IOException {
	return createSearchIndex(directIndices);
    }

    private static List<SearchIndex<Integer>> createSearchIndex(List<DirectIndex<Integer>> directIndices) throws IOException {
	Map<String, SearchIndex<Integer>> invertedIndexMap = new HashMap<>();

	for (DirectIndex<Integer> directIndex : directIndices) {
	    for (String word : directIndex) {
		SearchIndex<Integer> searchIndex = invertedIndexMap.get(word);
		if (searchIndex == null) {
		    searchIndex = new SearchIndex<Integer>(word);
		    invertedIndexMap.put(word, searchIndex);
		}
		searchIndex.setKeywordRelevance(directIndex.getRelevance(word));
		searchIndex.addObject(directIndex.getObjectKey(), directIndex.getRelevance(word));
	    }
	}

	List<SearchIndex<Integer>> invertedIndex = new ArrayList<>(invertedIndexMap.values());
	Collections.sort(invertedIndex, new Comparator<SearchIndex<Integer>>() {
	    @Override
	    public int compare(SearchIndex<Integer> left, SearchIndex<Integer> right) {
		return left.compareTo(right);
	    }
	});

	for (SearchIndex<Integer> searchIndex : invertedIndex) {
	    searchIndex.update();
	}

	return invertedIndex;
    }

    // --------------------------------------------------------------------------------------------
    // UTILITY METHODS

    private static String text(List<String> words) throws IOException {
	StringBuilder builder = new StringBuilder();
	for (String word : words) {
	    builder.append(word);
	    builder.append(' ');
	}
	return builder.toString();
    }

    private static String text(ConservationStatus conservation) throws IOException {
	return conservation != null ? conservation.display() : null;
    }

    private static String regions(List<Region> regions) throws IOException {
	StringBuilder builder = new StringBuilder();
	for (Region region : regions) {
	    builder.append(region.getName());
	    builder.append(' ');
	}
	return builder.toString();
    }

    private static String taxons(List<Taxon> taxons) throws IOException {
	StringBuilder builder = new StringBuilder();
	for (Taxon token : taxons) {
	    builder.append(token.getName());
	    builder.append(' ');
	    builder.append(token.getValue());
	    builder.append(' ');
	}
	return builder.toString();
    }

    private static String related(List<? extends SearchWord> relatedObjects) throws IOException {
	StringBuilder builder = new StringBuilder();
	for (SearchWord relatedObject : relatedObjects) {
	    builder.append(relatedObject.toSearchWord());
	    builder.append(' ');
	}
	return builder.toString();
    }

    private static String facts(List<? extends SearchWord> facts) {
	StringBuilder builder = new StringBuilder();
	for (SearchWord fact : facts) {
	    builder.append(fact.toSearchWord());
	    builder.append(' ');
	}
	return builder.toString();
    }
}
