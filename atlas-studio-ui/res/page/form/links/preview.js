WinMain.on("load", function() {
	var linksControl = WinMain.doc.getByClass(com.kidscademy.atlas.LinksControl);

	const formPage = {
		getObject : function() {
			return {};
		}
	};

	linksControl.onCreate(formPage);
	linksControl.onStart();
	
	linksControl.setValue([ {
		id : 816,
		url : "http://www.softschools.com/facts/music_instruments/cajon_facts/2999/",
		name : "Soft Schools",
		iconSrc : "/media/link/softschools.png"
	} ]);
});