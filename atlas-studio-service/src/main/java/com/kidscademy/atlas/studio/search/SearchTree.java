package com.kidscademy.atlas.studio.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import js.lang.BugError;

/**
 * This immutable tree map uses a single char as key and is optimized for value
 * search by a sequence of chars. A value is identified by a sequence of chars
 * that should be unique.
 * <p>
 * A sequence of chars can be used to locate a value. Provides constant
 * complexity regarding dictionary getObjectsCount, O(m * log n) where m is char
 * sequence length and n is alphabet getObjectsCount.
 *
 * @param <V>
 *            the type of mapped values.
 * @author Iulian Rotaru
 */
public class SearchTree<V> {
    private final Node<V> root;

    public SearchTree() {
	root = new Node<>();
    }

    public SearchTree(Map<String, V> values) {
	this();
	for (Map.Entry<String, V> entry : values.entrySet()) {
	    load(root, entry.getKey(), 0, entry.getValue());
	}
    }

    private static <V> void load(Node<V> node, String keyword, int keywordIndex, V value) {
	if (keywordIndex >= keyword.length()) {
	    throw new BugError("Keyword overflow.");
	}
	// current key is the character from value keyword in current iteration
	final char key = keyword.charAt(keywordIndex);

	// value is always stored on a child node of the current iteration node
	// but only if one of the next condition is met:
	// 1. child node is not yet created for current key
	// 2. value keyword is fully traversed, that is, current key is the last char
	// from keyword

	// get child node for current key; if there is no such node create it as value
	// node
	Node<V> child = node.getChild(key);
	if (child == null) {
	    node.putChildValue(key, value);
	    return;
	}

	// at this point child node exists

	if (child.hasValue()) {
	    if (keywordIndex < keyword.length() - 1) {
		child.moveValueToChild(keywordIndex + 1);
	    }
	}

	// check if value keyword is completely traversed, that is, this iteration is on
	// last keyword char
	// if so, store value on current node but takes care to move inner value, if one
	// exists
	if (keyword.length() - 1 == keywordIndex) {
	    child.putValue(value);
	    return;
	}

	load(child, keyword, keywordIndex + 1, value);
    }

    public List<V> find(String prefix) {
	return find(prefix, 10);
    }

    public List<V> find(String prefix, int count) {
	List<V> values = new ArrayList<>();
	if (prefix.isEmpty()) {
	    return values;
	}
	prefix = prefix.toLowerCase();

	Node<V> node = root;
	for (int i = 0; i < prefix.length(); ++i) {
	    Node<V> child = node.getChild(prefix.charAt(i));

	    if (child == null) {
		if (node.hasValue(prefix)) {
		    values.add(node.getValue());
		    return values;
		}
		return Collections.emptyList();
	    }

	    if (child.hasValue(prefix)) {
		values.add(child.getValue());
		return values;
	    }

	    node = child;
	}

	// load value nodes from subtree of which located node is the root

	collect(node, values, count);
	return values;
    }

    /**
     * Recursively traverse node subtree collecting values.
     *
     * @param node
     *            subtree root node,
     * @param values
     *            values container,
     * @param count
     *            maximum values count.
     * @return true if recursive iteration should continue, false to interrupt
     *         recursive iteration
     */
    private static <V> boolean collect(Node<V> node, List<V> values, int count) {
	// TODO: add guard for infinite loop

	if (!node.hasChildren()) {
	    return true;
	}

	// because map implementation is tree map values - child nodes, are iterated in
	// ascending order
	for (Node<V> child : node.getChildren()) {
	    if (child.hasValue()) {
		values.add(child.getValue());
		if (values.size() == count) {
		    return false;
		}
	    }
	    if (!collect(child, values, count)) {
		return false;
	    }
	}
	return true;
    }

    public void dump() {
	System.out.println("root");
	dump('.', root, 0);
    }

    private void dump(char key, Node<V> node, int indent) {
	for (int i = 0; i < indent; ++i) {
	    System.out.print(" ");
	}
	System.out.print(key);
	if (node.value != null) {
	    System.out.print(": ");
	    System.out.print(node.getKeyword());
	}
	System.out.println();

	if (node.children == null) {
	    return;
	}
	for (Map.Entry<Character, Node<V>> child : node.children.entrySet()) {
	    dump(child.getKey(), child.getValue(), indent + 1);
	}
    }

    private static class Node<V> {
	private String keyword;

	/**
	 * Optional value, default to null.
	 */
	private V value;

	/**
	 * Optional children mapped by character key, default to null.
	 */
	private TreeMap<Character, Node<V>> children;

	public Node() {
	    children = new TreeMap<>();
	}

	public Node(V value) {
	    this.value = value;
	}

	public boolean hasChildren() {
	    return children != null;
	}

	public Collection<Node<V>> getChildren() {
	    assert children != null;
	    return children.values();
	}

	public Node<V> getChild(char key) {
	    return children != null ? children.get(key) : null;
	}

	public void putValue(V value) {
	    this.value = value;
	}

	public boolean hasValue() {
	    return value != null;
	}

	public boolean hasValue(String prefix) {
	    return value != null && keyword.startsWith(prefix);
	}

	public String getKeyword() {
	    return keyword;
	}

	public V getValue() {
	    assert value != null;
	    return value;
	}

	public void putChildValue(char key, V value) {
	    if (children == null) {
		children = new TreeMap<>();
	    }
	    children.put(key, new Node<>(value));
	}

	public void moveValueToChild(int keyIndex) {
	    assert value != null;
	    if (keyword.length() <= keyIndex) {
		throw new BugError("Keyword overflow.");
	    }
	    char key = keyword.charAt(keyIndex);
	    assert children == null;
	    children = new TreeMap<>();
	    putChildValue(key, value);
	    this.value = null;
	}
    }
}
