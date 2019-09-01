package com.kidscademy.atlas.studio.tool;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.HashSet;
import java.util.Set;

public class StopWords {
	private static final Set<String> WORDS = new HashSet<>();
	static {
		WORDS.add("no");
		WORDS.add("and");
		WORDS.add("for");
		WORDS.add("the");
		WORDS.add("age");
		WORDS.add("oh");
		WORDS.add("on");
		WORDS.add("of");
		WORDS.add("one");
		WORDS.add("me");
		WORDS.add("op");
		WORDS.add("rd");
		WORDS.add("in");
		WORDS.add("give");
		WORDS.add("good");
		WORDS.add("added");
		WORDS.add("along");
		WORDS.add("also");
		WORDS.add("although");
		WORDS.add("any");
		WORDS.add("anyway");
		WORDS.add("anywhere");
		WORDS.add("are");
		WORDS.add("around");
		WORDS.add("base");
		WORDS.add("basically");
		WORDS.add("been");
		WORDS.add("before");
		WORDS.add("becoming");
		WORDS.add("beginnings");
		WORDS.add("between");
		WORDS.add("both");
		WORDS.add("but");
		WORDS.add("call");
		WORDS.add("can");
		WORDS.add("carry");
		WORDS.add("commes");
		WORDS.add("common");
		WORDS.add("consists");
		WORDS.add("day");
		WORDS.add("early");
		WORDS.add("either");
		WORDS.add("end");
		WORDS.add("enough");
		WORDS.add("every");
		WORDS.add("few");
		WORDS.add("from");
		WORDS.add("had");
		WORDS.add("has");
		WORDS.add("have");
		WORDS.add("having");
		WORDS.add("held");
		WORDS.add("hold");
		WORDS.add("into");
		WORDS.add("its");
		WORDS.add("least");
		WORDS.add("less");
		WORDS.add("like");
		WORDS.add("lowest");
		WORDS.add("many");
		WORDS.add("may");
		WORDS.add("more");
		WORDS.add("most");
		WORDS.add("much");
		WORDS.add("nearly");
		WORDS.add("off");
		WORDS.add("often");
		WORDS.add("other");
		WORDS.add("out");
		WORDS.add("over");
		WORDS.add("pick");
		WORDS.add("same");
		WORDS.add("several");
		WORDS.add("some");
		WORDS.add("still");
		WORDS.add("such");
		WORDS.add("than");
		WORDS.add("them");
		WORDS.add("these");
		WORDS.add("they");
		WORDS.add("this");
		WORDS.add("those");
		WORDS.add("though");
		WORDS.add("under");
		WORDS.add("underneath");
		WORDS.add("used");
		WORDS.add("using");
		WORDS.add("very");
		WORDS.add("was");
		WORDS.add("when");
		WORDS.add("which");
		WORDS.add("while");
		WORDS.add("with");
		WORDS.add("without");
		WORDS.add("would");
	}

	public boolean contains(String word) {
		if (word.length() <= 2) {
			return true;
		}
		if (isNumeric(word)) {
			return true;
		}
		return WORDS.contains(word);
	}

	private static boolean isNumeric(String str) {
		NumberFormat formatter = NumberFormat.getInstance();
		ParsePosition pos = new ParsePosition(0);
		formatter.parse(str, pos);
		return str.length() == pos.getIndex();
	}
}
