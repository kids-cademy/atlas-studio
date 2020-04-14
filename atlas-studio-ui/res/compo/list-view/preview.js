WinMain.on("load", () => {
	const listView = WinMain.doc.getByCssClass("list-view");
	AtlasService.getCollections(collections => listView.setObject(collections).show());
	
	const contextMenu = WinMain.doc.getByCssClass("context-menu");
	listView.setContextMenu(contextMenu);
	
	listView.on("load-items", () => alert("load items"));
	listView.on("reset-filter", () => alert("reset filter"));
	listView.on("item-click", object => alert(object.display));
});
