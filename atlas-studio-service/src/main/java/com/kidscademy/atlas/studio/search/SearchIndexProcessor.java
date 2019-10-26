package com.kidscademy.atlas.studio.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kidscademy.atlas.studio.export.ExportItem;
import com.kidscademy.atlas.studio.export.ExportObject;
import com.kidscademy.atlas.studio.model.ConservationStatus;
import com.kidscademy.atlas.studio.model.Region;
import com.kidscademy.atlas.studio.model.Taxon;

import js.lang.BugError;

public class SearchIndexProcessor {
    private final List<ExportItem> items;
    private final Map<String, ExportItem> itemsMap = new HashMap<>();
    private final Map<String, DirectIndex> directIndices = new HashMap<>();

    public SearchIndexProcessor(List<ExportItem> items) {
	this.items = items;
	for (ExportItem item : items) {
	    itemsMap.put(item.getName(), item);
	}
    }

    public void createDirectIndex(ExportObject object) throws IOException {
	// direct index is per atlas object
	// it stores all keywords and their relevance
	DirectIndex directIndex = new DirectIndex();

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

	directIndices.put(object.getName(), directIndex);
    }

    public List<SearchIndex> createSearchIndex() throws IOException {
	Map<String, SearchIndex> invertedIndexMap = new HashMap<>();

	for (ExportItem item : items) {
	    DirectIndex directIndex = directIndices.get(item.getName());
	    if (directIndex == null) {
		throw new BugError("Inconsistent object collections. Object |%s| not found.", item.getName());
	    }
	    for (String word : directIndex) {
		SearchIndex searchIndex = invertedIndexMap.get(word);
		if (searchIndex == null) {
		    searchIndex = new SearchIndex(word);
		    invertedIndexMap.put(word, searchIndex);
		}
		searchIndex.setKeywordRelevance(directIndex.getRelevance(word));
		searchIndex.addObject(item.getIndex(), directIndex.getRelevance(word));
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
