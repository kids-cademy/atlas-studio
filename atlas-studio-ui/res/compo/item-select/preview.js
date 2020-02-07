WinMain.on("load", () => {
	const itemSelect = WinMain.doc.getByClass(com.kidscademy.ItemSelect);
	AtlasService.getCollections(collections => itemSelect.load(collections));
	itemSelect.open(item => {
		alert(item.display)
	})
});