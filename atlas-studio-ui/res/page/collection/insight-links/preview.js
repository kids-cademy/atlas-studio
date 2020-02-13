WinMain.on("load", function() {
	const listView = WinMain.doc.getByCssClass("insight-links");
	AtlasService.getCollectionLinks({ state: "NONE" }, 10, items => listView.setObject(items).show());
});