package com.kidscademy.atlas.studio.search;

import java.util.ArrayList;
import java.util.List;

public class SearchEngine<T extends Comparable<T>> {
    private final KeywordTree<KeywordIndex<T>> searchTree;

    public SearchEngine(List<KeywordIndex<T>> searchIndex) {
	this.searchTree = new KeywordTree<>(searchIndex);
    }

    public List<T> find(String string) {
	List<T> objectKeys = new ArrayList<>();
	for (KeywordIndex<T> index : searchTree.find(string)) {
	    for (T objectKey : index.getObjectKeys()) {
		objectKeys.add(objectKey);
	    }
	}
	return objectKeys;
    }
}
