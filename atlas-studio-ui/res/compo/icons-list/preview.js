WinMain.on("load", () => {
	const iconsList = WinMain.doc.getByCssClass("icons-list");
	AtlasService.getCollections(collections => iconsList.setObject(collections).show());
});
