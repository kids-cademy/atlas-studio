package com.kidscademy.atlas.studio.search;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import js.util.Strings;

public class SearchIndex<T extends Comparable<T>> implements Keyword, Comparable<SearchIndex<T>> {
    private String keyword;
    private int keywordRelevance;
    private List<T> objectKeys;

    private transient SortedSet<ObjectKey<T>> objects;

    public SearchIndex(String keyword) {
	this.keyword = keyword;
	this.objects = new TreeSet<ObjectKey<T>>();
    }

    public void setKeywordRelevance(int keywordRelevance) {
	if (this.keywordRelevance < keywordRelevance) {
	    this.keywordRelevance = keywordRelevance;
	}
    }

    @Override
    public String getKeyword() {
	return keyword;
    }

    public int getKeywordRelevance() {
	return keywordRelevance;
    }

    public void addObject(T objectKey, int rank) {
	objects.add(new ObjectKey<T>(objectKey, rank));
    }

    public void update() {
	objectKeys = new ArrayList<T>(objects.size());
	for (ObjectKey<T> object : objects) {
	    objectKeys.add(object.key);
	}
    }

    public List<T> getObjectKeys() {
	return objectKeys;
    }

    @Override
    public int compareTo(SearchIndex<T> that) {
	if (this.keywordRelevance == that.keywordRelevance) {
	    return this.keyword.compareTo(that.keyword);
	}
	return this.keywordRelevance > that.keywordRelevance ? 1 : -1;
    }

    // --------------------------------------------------------------------------------------------

    private static class ObjectKey<T extends Comparable<T>> implements Comparable<ObjectKey<T>> {
	private final T key;
	private final int rank;

	ObjectKey(T key, int rank) {
	    super();
	    this.key = key;
	    this.rank = rank;
	}

	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((key == null) ? 0 : key.hashCode());
	    result = prime * result + rank;
	    return result;
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
		return true;
	    if (obj == null)
		return false;
	    if (getClass() != obj.getClass())
		return false;
	    @SuppressWarnings("unchecked")
	    ObjectKey<T> other = (ObjectKey<T>) obj;
	    if (key == null) {
		if (other.key != null)
		    return false;
	    } else if (!key.equals(other.key))
		return false;
	    if (rank != other.rank)
		return false;
	    return true;
	}

	@Override
	public int compareTo(ObjectKey<T> that) {
	    if (this.rank == that.rank) {
		return this.key.compareTo(that.key);
	    }
	    return Integer.compare(that.rank, this.rank);
	}

	@Override
	public String toString() {
	    return Strings.toString(key, rank);
	}
    }
}
