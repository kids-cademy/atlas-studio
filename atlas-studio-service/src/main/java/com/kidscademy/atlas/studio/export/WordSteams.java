package com.kidscademy.atlas.studio.export;

import java.util.HashMap;
import java.util.Map;

public class WordSteams {
	private static final Map<String, String> STEAMS = new HashMap<>();
	static {
		STEAMS.put("american", "america");
		STEAMS.put("indian", "india");
		STEAMS.put("african", "africa");
		STEAMS.put("chambers", "chamber");
		STEAMS.put("concerto", "concert");
		STEAMS.put("covering", "cover");
		STEAMS.put("cuatros", "cuatro");
		STEAMS.put("dancing", "dance");
		STEAMS.put("doubled", "double");
		STEAMS.put("families", "family");
		STEAMS.put("fingers", "finger");
		STEAMS.put("freely", "free");
		STEAMS.put("guitars", "guitar");
		STEAMS.put("hands", "hand");
		STEAMS.put("heads", "head");
		STEAMS.put("holes", "hole");
		STEAMS.put("instruments", "instrument");
		STEAMS.put("keys", "key");
		STEAMS.put("keyboards", "keyboard");
		STEAMS.put("lengths", "length");
		STEAMS.put("made", "make");
		STEAMS.put("makers", "maker");
		STEAMS.put("mandolins", "mandolin");
		STEAMS.put("metallic", "metal");
		STEAMS.put("narrows", "narrow");
		STEAMS.put("naturally", "natural");
		STEAMS.put("origins", "origin");
		STEAMS.put("parts", "part");
		STEAMS.put("pastoral", "pasture");
		STEAMS.put("pipes", "pipe");
		STEAMS.put("places", "place");
		STEAMS.put("played", "play");
		STEAMS.put("plectra", "plectrum");
		STEAMS.put("produced", "produce");
		STEAMS.put("punks", "punk");
		STEAMS.put("reeds", "reed");
		STEAMS.put("resonating", "resonate");
		STEAMS.put("rests", "rest");
		STEAMS.put("rounded", "round");
		STEAMS.put("russian", "russia");
		STEAMS.put("sets", "set");
		STEAMS.put("shaped", "shape");
		STEAMS.put("shapes", "shape");
		STEAMS.put("sitars", "sitar");
		STEAMS.put("sizes", "size");
		STEAMS.put("smaller", "small");
		STEAMS.put("sounds", "sound");
		STEAMS.put("stringed", "string");
		STEAMS.put("strings", "string");
		STEAMS.put("turkish", "turkey");
		STEAMS.put("ukrainian", "ukraine");
	}

	public String getSteam(String word) {
		String steam = STEAMS.get(word);
		return steam != null ? steam : word;
	}
}
