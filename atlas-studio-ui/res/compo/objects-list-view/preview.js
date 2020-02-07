WinMain.on("load", () => {
	const listView = WinMain.doc.getByCssClass("list-view");
	AtlasService.getCollections(collections => listView.setObject(collections).show());
});
