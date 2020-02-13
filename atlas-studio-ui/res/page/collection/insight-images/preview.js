WinMain.on("load", function() {
	const listView = WinMain.doc.getByCssClass("insight-images");
	AtlasService.getCollectionImages({ state: "NONE" }, 10, items => listView.setObject(items).show());
});