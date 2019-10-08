WinMain.on("load", function() {
	var atlasObject = {
		conservationStatus : "VU"
	};

	var objectView = WinMain.doc.getByCss(".object-view");
	objectView.setObject(atlasObject);
});
