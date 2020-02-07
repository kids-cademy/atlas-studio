WinMain.on("load", () => {
	const contextMenu = WinMain.doc.getByClass(com.kidscademy.ContextMenu);

	const listView = WinMain.doc.getByCssClass("list-view");
	listView.on("click", ev => {
		const itemView = ev.target.getParentByCssClass("item");
		if(itemView) {
			contextMenu.open(itemView);
		}
	});
	
	AtlasService.getCollections(collections => listView.setObject(collections).show());
	
//	AtlasService.getAtlasItem(1, item => contextMenu)
//	contextMenu.show();
});
