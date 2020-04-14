WinMain.on("load", () => {
	const listControl = WinMain.doc.getByCssClass("list-control");
	listControl.setLayout("icons");
	AtlasService.getCollections(collections => listControl.setObject(collections).show());
});
