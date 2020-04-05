package com.kidscademy.atlas.studio.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class Html2Md {
    private NodeList elements;

    public Html2Md(String html) {
	try {
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    Document doc = builder.parse(new InputSource(new StringReader(html)));
	    elements = doc.getDocumentElement().getChildNodes();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public String converter() {
	StringBuilder builder = new StringBuilder();
	for (int i = 0; i < elements.getLength(); ++i) {
	    Element element = (Element) elements.item(i);

	    switch (element.getTagName()) {
	    case "h1":
		builder.append("# ");
		builder.append(markdown(element));
		break;

	    case "h2":
		builder.append("## ");
		builder.append(markdown(element));
		break;

	    case "p":
		builder.append(markdown(element));
		break;

	    case "ul":
		NodeList ulitems = element.getChildNodes();
		for (int j = 0;;) {
		    builder.append("- ");
		    builder.append(markdown((Element) ulitems.item(j)));
		    if (++j == ulitems.getLength()) {
			break;
		    }
		    builder.append("\r\n");
		}
		break;

	    case "ol":
		NodeList olitems = element.getChildNodes();
		for (int j = 0;;) {
		    builder.append(Integer.toString(j + 1));
		    builder.append(". ");
		    builder.append(markdown((Element) olitems.item(j)));
		    if (++j == olitems.getLength()) {
			break;
		    }
		    builder.append("\r\n");
		}
		break;

	    case "table":
		List<List<String>> layout = new ArrayList<>();
		List<Integer> columnWidths = new ArrayList<>();

		NodeList rows = element.getElementsByTagName("tr");
		for (int j = 0; j < rows.getLength(); ++j) {
		    layout.add(new ArrayList<String>());
		    NodeList cells = rows.item(j).getChildNodes();
		    for (int k = 0; k < cells.getLength(); ++k) {
			layout.get(j).add(cells.item(k).getTextContent().trim());
		    }
		    for (int k = 0; k < cells.getLength(); ++k) {
			if (k == columnWidths.size()) {
			    columnWidths.add(0);
			}
			columnWidths.set(k, Math.max(columnWidths.get(k), layout.get(j).get(k).length()));
		    }
		}

		for (int j = 0; j < layout.get(0).size(); ++j) {
		    builder.append("| ");
		    builder.append(text(layout.get(0).get(j), columnWidths.get(j), ' '));
		    builder.append(' ');
		}
		builder.append("\r\n");

		for (int j = 0; j < layout.get(0).size(); ++j) {
		    builder.append("|-");
		    builder.append(text("", columnWidths.get(j), '-'));
		    builder.append("-");
		}
		builder.append("\r\n");

		for (int j = 1;;) {
		    for (int k = 0; k < layout.get(j).size(); ++k) {
			builder.append("| ");
			builder.append(text(layout.get(j).get(k), columnWidths.get(k), ' '));
			builder.append(' ');
		    }
		    if (++j == layout.size()) {
			break;
		    }
		    builder.append("\r\n");
		}
		break;
	    }
	    builder.append("\r\n\r\n");
	}
	return builder.toString();
    }

    private String text(String text, int width, char padding) {
	text = text.trim();
	while (text.length() < width) {
	    text += padding;
	}
	return text;
    }

    private String markdown(Element element) {
	StringBuilder builder = new StringBuilder();
	NodeList children = element.getChildNodes();
	for (int i = 0; i < children.getLength(); ++i) {
	    Node child = children.item(i);
	    switch (child.getNodeType()) {
	    case Node.TEXT_NODE:
		builder.append(child.getNodeValue());
		break;

	    case Node.ELEMENT_NODE:
		Element childElement = (Element) child;
		switch (childElement.getTagName().toLowerCase()) {
		case "strong":
		    builder.append('`');
		    builder.append(markdown(childElement));
		    builder.append('`');
		    break;

		case "em":
		    builder.append('"');
		    builder.append(markdown(childElement));
		    builder.append('"');
		    break;
		}
		break;
	    }
	}
	return builder.toString();
    }
}
