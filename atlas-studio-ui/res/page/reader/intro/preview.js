WinMain.on("load", function() {
	var atlasObject = {
		display : "Accordion",
		introImagePath : "sync-preview/res/form/accordion/thumbnail.png",
		introText : "The accordion is a musical instrument that has keys similar to a piano, but is small enough for a person to hold.",

		description : [ "The bellows are the section of cloth, cardboard, leather, and metal located in between keyboards and buttons.", "The bellows are expanded and contracted by the accordionist, which creates vacuum and pressure, driving air through the reed chambers that creates the sound.", "The accordion is used in pop music, folk music, cajun music, jazz, classical, zydeco and dance-pop.", "The accordion has also been referred to affectionately as the squeezebox." ],

		featuredImagePath : "sync-preview/res/form/accordion/button-accordion.png",
		featuredCaption : "There is also a variant that uses buttons to play notes.",

		aliases : [ "Sun-Fin-Chin", "Bayan", "Trekspill", "Fisarmonica" ],
		date : "XVII-th Century",
		spreading : [],

		picturePath : "sync-preview/res/form/accordion/picture.jpg",
		sampleTitle : "Vittorio Monti - Csárdás",
		samplePath : "atlas/accordion/sample.mp3",

		facts : [ {
			name : "First played on Carnegie Hall in 1939",
			value : "The first to play the accordion in New York City's famous Carnegie Hall were Gene von Halberg, Abe Goldman, and Joe Biviano, in 1939."
		}, {
			name : "Most parts are hand made",
			value : "Accordions are still made with a lot of human hands as opposed to by machinery. Some parts are made by machine but the best are mostly hand made by craftsman."
		}, {
			name : "Golden Age of the Accordion",
			value : "The 1900s to the 1960s has been referred to as the 'Golden Age of the Accordion'."
		}, {
			name : "Pietro Deiro is the 'Daddy of the Accordion'",
			value : "Pietro Deiro, an Italy-born accordionist was known as the 'Daddy of the Accordion'. He had a career in San Francisco during the vaudeville era and was even signed to RCA Victor Records."
		}, {
			name : "Can generate long duration sounds",
			value : "The accordion is able to sustain sound for a much longer time than most other instruments."
		} ],
		features : {
			"Conservation" : "Least Concern",
			"Range" : "Worldwide, except for Australia and the extreme polar regions",
			"Habitat" : "Forest and woodland",
			"Diet" : "Insects, fruits, acirns and nuts",
			"Life Span" : "4 - 11 years",
			"Wing Span" : "66 cm. to 75 cm.",
			"Length" : "40 cm. to 49 cm.",
			"Mass" : "250g. to 400g."
		},

		links : [ {
			id : 643,
			url : "https://en.wikipedia.org/wiki/Accordion",
			display : "Wikipedia",
			description : "Wikipedia article about accordion.",
			iconPath : "sync-preview/res/form/links/wikipedia.png"
		}, {
			id : 644,
			url : "http://www.softschools.com/facts/music_instruments/accordion_facts/3037/",
			display : "Soft Schools",
			description : "Soft Schools facts about accordion.",
			iconPath : "sync-preview/res/form/links/softschools.png"
		} ],
		related : [ {
			display : "Bandoneon",
			description : "The bandoneon is a type of concertina particularly popular in Argentina and Uruguay.",
			iconPath : "sync-preview/res/form/bandoneon/icon.jpg"
		}, {
			display : "Concertina",
			description : "A concertina is a free-reed musical instrument, like the various accordions and the harmonica.",
			iconPath : "sync-preview/res/form/concertina/icon.jpg"
		}, {
			display : "Harmonium",
			description : "A harmonium is a keyboard instrument similar to an organ. It blows air through reeds, producing musical notes.",
			iconPath : "sync-preview/res/form/harmonium/icon.jpg"
		} ]
	};

	WinMain.page.setObject(atlasObject);
});
