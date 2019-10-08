WinMain.on("load", function() {
	var atlasObject = {
		definition : "The accordion is a musical instrument that has keys similar to a piano, but is small enough for a person to hold.",
		images : {
			cover : {
				src : "/atlas-studio-preview/res/page/reader/atlas/accordion/thumbnail.png"
			}
		}
	};

	var objectView = WinMain.doc.getByCss(".object-view");
	objectView.setObject(atlasObject);
});
